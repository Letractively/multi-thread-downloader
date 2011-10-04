/*
 *  Copyright (C) 2011 JiangHongTiao <jjurco.sk_gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.site;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.concurrency.TimerThread;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocument;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItemState;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.ProxySelector;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.SettingsProxySelector;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.DownloadItemStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.Persist;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.TimestampComparatorAZ;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.WaitType;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public class SitePatternDownloadManagerImpl implements
        SitePatternDownloadManager {

    private final Object syncObj = new Object();
    private SiteSettings settings;
    private Integer activeConnections = new Integer(0);
    //priority queue of waiting downlaods
    private PriorityBlockingQueue<DownloadItem> queue =
            new PriorityBlockingQueue<DownloadItem>();
    private ConcurrentHashMap<RemoteDocument, DownloadItem> runningMap =
            new ConcurrentHashMap<RemoteDocument, DownloadItem>();
    private Pattern pattern;
    //remember downloaded items, bcs it removes it from the queue immidiately
    private ConcurrentMap<RemoteDocument, DownloadItem> map =
            new ConcurrentHashMap<RemoteDocument, DownloadItem>();
    private Timestamp lastFinished = getActualTimestamp();
    private final Persist persistence = Persist.getSingleton();
    private TimerThread timerThread = null;
    private static Logger log = Logger.getLogger(SitePatternDownloadManagerImpl.class.
            getName());
    private ProxySelector ps = null;

    private Timestamp getActualTimestamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }

    public SitePatternDownloadManagerImpl(SiteSettings settings) {
        if (settings == null) {
            throw new NullPointerException("Settings cannot be null!");
        }
        this.settings = settings;
        this.pattern = Pattern.compile(settings.getSitePattern(),
                Pattern.CASE_INSENSITIVE);
        this.ps = new SettingsProxySelector(settings);
        log.debug("Site Pattern Download Manager created with url: " + settings.
                getSitePattern() + " with settings: " + settings.toString());
    }

    private void run() {
        //add to runningMap, increas active connections, take from queue
        DownloadItem downloadItem;
        synchronized (syncObj) {
            if ((queue == null) || (queue.isEmpty())) {
                return;
            }
            downloadItem = queue.poll();
            downloadItem.setRunThread(getActualTimestamp());
            downloadItem.setState(DownloadItemState.RUNNING);
            downloadItem.getDocument().setProxySelector(ps);
            runningMap.put(downloadItem.getDocument(), downloadItem);
            activeConnections++;
            log.debug("Run item with UUID: " + downloadItem.getUuid()
                    + " and URL: " + downloadItem.getDocument().getURL().
                    toString());
        }
        downloadItem.getDocument().start();
    }

    public void addItem(DownloadItem item) {
        if (!pattern.matcher(item.getDocument().getURL().toString()).matches()) {
            throw new IllegalArgumentException("This instance cannot provide "
                    + "download for the URL: '" + item.getDocument().getURL().
                    toString()
                    + "' because it does not match pattern: "
                    + pattern.pattern());
        }
        synchronized (syncObj) {
            queue.add(item);
            item.setAddedToQueue(getActualTimestamp());
            item.getDocument().setSitePatternDownloadManager(this);
            item.setState(DownloadItemState.WAITING);
            map.put(item.getDocument(), item);
            log.debug("Added item with UUID: " + item.getUuid() + " with URL: "
                    + item.getDocument().getURL().toString());
            checkRun();
        }
    }

    private void checkRun() {
        synchronized (syncObj) {
            if ((queue == null) || (queue.isEmpty())) {
                return;
            }

        }
        Timestamp wait = waitToNextRun();
        if (wait.getTime() == 0) {
            run();
        }

        if (wait.getTime() > 0) {
            /**
             * wait number of milliseconds - new thread waits specified number
             * of milliseconds and then notifies this instance
             */
            synchronized (syncObj) {
                if (timerThread == null) {
                    timerThread = new TimerThread(wait.getTime(), this);
                    timerThread.start();
                }
            }
        }

        if (wait.getTime() < 0) {
            //just wait for notification about thread finish
        }
    }

    /**
     * how many milliseconds program have to wait from the last downloading
     * 
     * @param waitTime how many milliseconds we have to wait till next download
     * @return number of milliseconds till we can start next download
     */
    private Long countNextWait(Long waitTime, Timestamp lastItem) {
        synchronized (syncObj) {
            Long result = 5 + waitTime - (getActualTimestamp().getTime()
                    - lastItem.getTime());
            return result<0?0L:result;
        }
    }

    /**
     * count how much time have to thread wait to next run of the download
     * @return negative number means that it is not specified how many time we 
     * we have to wait. We are just waiting for the notification. Zero means no
     * waiting and we can launch new thread with downloading. Positive number means 
     * how many milliseconds we have to wait till next download. 
     */
    private Timestamp waitToNextRun() {
        synchronized (syncObj) {
            //means we don't know, we are waiting for thread end. wait for notification
            if (activeConnections >= settings.getMaxConnections()) {
                return new Timestamp(-1L);
            }
            //random waiting after last access
            if ((settings.getMaxConnections() == 1) && (settings.getWaitType()
                    != WaitType.NONE)) {
                //generate next wait time based on settings and substract the last
                //access to the server
                Timestamp nextWait = new Timestamp(0L);
                switch (settings.getWaitType()) {
                    case STATIC:
                        nextWait.setTime(countNextWait(settings.getWaitMin().
                                longValue(), lastFinished));
                        break;
                    case RANDOM:
                        Random rand = new Random();
                        Long randNum = ((Math.abs(rand.nextLong()) + settings.
                                getWaitMin()) % settings.getWaitMax());
                        nextWait.setTime(countNextWait(randNum, lastFinished));
                        break;
                    default:
                        throw new IllegalStateException("Type is not present: "
                                + settings.getWaitType());
                }
                return nextWait;
            }
            //connections per time
            if (settings.getInTime() > 0) {
                
                List<Timestamp> usageStatistics = getUsageStatistics(settings.
                        getInTimeInMs());
                if ((usageStatistics.size() + activeConnections) >= settings.
                        getConnections()) {
                    Timestamp oldest = usageStatistics.get(0);
                    //actual time - oldest time = we already waited
                    //how long to wait - we already waited = we have to wait
                    Long w = countNextWait(settings.getInTimeInMs(), oldest);
                    if ((w < 0) || (ps.canUseAnotherProxy())) {
                        return new Timestamp(0);
                    } else {
                        return new Timestamp(w);
                    }
                }
            }
            return new Timestamp(0L);
        }
    }

    /**
     * get list of downloads in last <code>milliseconds</code> milliseconds.
     * 
     * @param miliseconds number of milliseconds to the past
     * @return list of download item timestamps in last <code>milliseconds</code> milliseconds.
     */
    private List<Timestamp> getUsageStatistics(long miliseconds) {
        if (miliseconds <= 0) {
            throw new IllegalArgumentException(
                    "Miliseconds was smaller or equal 0.");
        }
        Timestamp actualTimestamp = getActualTimestamp();
//        long minutesInMs = TimeUnit.MILLISECONDS.convert(Long.parseLong(
//                maxMinutes), TimeUnit.MINUTES);
        Timestamp maxMs = new Timestamp(actualTimestamp.getTime() - miliseconds);
        List<Timestamp> resultList = null;
        synchronized (syncObj) {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT d.finishTime as finishTime "
                    + "FROM DownloadItemStatistics d WHERE d.finishTime > ?1 "
                    + "AND d.urlPattern = ?2 "
                    + "ORDER BY d.finishTime ASC");
            query.setParameter(1, maxMs);
            query.setParameter(2, settings.getSitePattern());
            resultList = query.getResultList();
        }
        Collections.sort(resultList, new TimestampComparatorAZ());
        return resultList;
    }

    public void finishedDownload(RemoteDocumentThread document) {

        synchronized (syncObj) {
            log.debug("Finished download with URL: " + document.getURL().
                    toString());

            DownloadItem finishedItem = runningMap.remove(document);
            lastFinished = getActualTimestamp();
            finishedItem.setThreadFinished(lastFinished);
            finishedItem.setState(DownloadItemState.FINISHED);
            activeConnections--;

            //write statistics for future usage
            DownloadItemStatistics itemStatistics = new DownloadItemStatistics(
                    finishedItem, pattern.pattern());
            persistence.persist(itemStatistics);
            checkRun();
        }
    }

    public void timeLeft() {
        synchronized (syncObj) {
            timerThread = null;
            checkRun();
        }
    }
}
