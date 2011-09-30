/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.network;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Query;
import javax.persistence.SqlResultSetMapping;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.AssignedProxyStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.Persist;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.ProxiesUsageStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.ProxyFailureStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;
import sk.lieskove.jianghongtiao.paris.utils.PropertiesUtils;

/**
 * Date of create: Sep 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0915
 */
public class SettingsProxySelector implements ProxySelector {

    private final Object syncObj = new Object();
    private Persist p = Persist.getSingleton();
    private SiteSettings settings;
    private List<AuthenticateProxy> proxies;
    private Map<String, Integer> activeDownloads = new TreeMap<String, Integer>();
    private int activeDownloadCount = 0;
    private EntityManager em = p.getEntityManager();
    private PropertiesUtils pu = new PropertiesUtils(SettingsProxySelector.class);

    public SettingsProxySelector(SiteSettings settings) {
        this.settings = settings;
        proxies = settings.getProxies();
        if (settings.isNoProxyEnabled()) {
            proxies.add(new AuthenticateProxy());
        }
        //initialize active downloads
        for (AuthenticateProxy authenticateProxy : proxies) {
            activeDownloads.put(authenticateProxy.toProxyString(), 0);
        }
    }
    
    private Map<String, Integer> proxyListStatistics(){
        Date now = new Date();
        synchronized (syncObj) {
            Query query = em.createNativeQuery(
                    "SELECT ap.proxyUrl as proxy, count(ap.proxyUrl) as usedTimes "
                    + "FROM assignedProxyStatistics ap "
                    + "WHERE ap.used > ?1 "
                    + "AND ap.urlPattern = ?2 "
                    + "GROUP BY ap.proxyUrl");
            Timestamp time = new Timestamp(now.getTime() - settings.
                    getInTimeInMs());
            query.setParameter(1, time);//success proxy connections - max conn per time - time
            query.setParameter(2, settings.getSitePattern());//url pattern of this instance
            List<Object[]> qeryResult = query.getResultList();
            Map<String, Integer> result = new TreeMap<String, Integer>();
            for(Object[] item : qeryResult){
                result.put((String)item[0], (Integer)item[1]);
            }
            return result;
        }
    }

    public List<AuthenticateProxy> getProxyList() {
        //number of connections was not over settings in time
        Map<String, Integer> proxyListStatistics = proxyListStatistics();
        List<AuthenticateProxy> result = new LinkedList<AuthenticateProxy>(proxies);
        for (int i = 0; i < result.size(); i++) {
            AuthenticateProxy ap = result.get(i);
            Integer alreadyUsed = proxyListStatistics.get(ap.toProxyString());
            if(alreadyUsed == null) alreadyUsed = 0;
            Integer activeUsed  = activeDownloads.get(ap.toProxyString());
            if(activeUsed == null) activeUsed = 0;
            if(alreadyUsed + activeUsed >= settings.getConnections()){
                result.remove(i);
            }
        }
        modifyUsedList(result, 1);
        activeDownloadCount++;
        return Collections.unmodifiableList(result);
    }

    public void proxyFailure(AuthenticateProxy proxy, URL url, String exception) {
        Date d = new Date();
        p.persist(new ProxyFailureStatistics(settings.getSitePattern(),
                url.toString(), proxy.toProxyString(), proxy.getUsername(),
                proxy.getPassword(), exception, new Timestamp(d.getTime())));
    }
    
    private void modifyUsedList(List<AuthenticateProxy> usedProxies, int num){
        for (AuthenticateProxy authenticateProxy : usedProxies) {
            Integer usedInstances = activeDownloads.get(authenticateProxy.toProxyString());
            usedInstances += num;
            activeDownloads.put(authenticateProxy.toProxyString(), usedInstances);
        }
    }

    public void proxyUse(AuthenticateProxy proxy, URL url, List<AuthenticateProxy> usedProxies) {
        activeDownloadCount--;
        modifyUsedList(usedProxies, -1);
        Date d = new Date();
        p.persist(new AssignedProxyStatistics(settings.getSitePattern(),
                url.toString(), proxy.toProxyString(), proxy.getUsername(),
                proxy.getPassword(), new Timestamp(d.getTime())));
    }

    public void noMoreProxies(URL url, List<AuthenticateProxy> usedProxies) {
        modifyUsedList(usedProxies, -1);
        activeDownloadCount--;
    }

    private List<Object[]> getListOfGoodProxyCounts(Date now,
            Timestamp failsPeriod, Integer failsNum) {
        synchronized (syncObj) {
            Query query = em.createNativeQuery(
                    "SELECT ap.proxyUrl as proxy, count(ap.proxyUrl) as usedTimes "
                    + "FROM assignedProxyStatistics ap "
                    + "WHERE ap.used > ?1 "
                    + "AND ap.urlPattern = ?2 "
                    + "AND ap.proxyUrl not in ("
                    + "SELECT fs.proxyUrl as proxyUrl "
                    + "FROM proxyFailureStatistics fs "
                    + "WHERE fs.failure > ?3 "
                    + "AND fs.urlPattern = ?2 "
                    + "GROUP BY fs.proxyUrl HAVING count(fs.proxyUrl) > ?4 ) "
                    + "GROUP BY ap.proxyUrl;");
            Timestamp time = new Timestamp(now.getTime() - settings.
                    getInTimeInMs());
            query.setParameter(1, time);//success proxy connections - max conn per time - time
            query.setParameter(2, settings.getSitePattern());//url pattern of this instance
            query.setParameter(3, failsPeriod);//past time of failed proxies - from settings file
            query.setParameter(4, failsNum);//number of failures in the past - from the settings file
            return query.getResultList();
        }
    }

    private List<Object[]> getListOfProxyFailures(Timestamp failsPeriod,
            Integer failsNum) {
        synchronized (syncObj) {
            Query query = em.createNativeQuery(
                    "SELECT fs.proxyUrl as proxyUrl "
                    + "FROM proxyFailureStatistics fs "
                    + "WHERE fs.failure > ?1 "
                    + "AND fs.urlPattern = ?2 "
                    + "GROUP BY fs.proxyUrl "
                    + "HAVING count(fs.proxyUrl) > ?3 ");
            query.setParameter(1, failsPeriod);
            query.setParameter(2, settings.getSitePattern());
            query.setParameter(3, failsNum);
            return query.getResultList();
        }
    }

    public boolean canUseAnotherProxy() {
        synchronized (syncObj) {

            Date now = new Date();
            String t = pu.getProperty("time-period-of-fails", "10");
            Long l = Long.parseLong(t);
            Timestamp failsPeriod = new Timestamp(now.getTime()
                    - TimeUnit.MILLISECONDS.convert(l, TimeUnit.MINUTES));
            Integer failsNum = Integer.parseInt(
                    pu.getProperty("max-number-of-fails", "5"));
            List<Object[]> usedResult = getListOfGoodProxyCounts(now,
                    failsPeriod, failsNum);
            List<Object[]> failureResult = getListOfProxyFailures(failsPeriod,
                    failsNum);
            //counting new proxy list
            Map<String, AuthenticateProxy> newProxyList = new TreeMap<String, AuthenticateProxy>();
            for (AuthenticateProxy proxy : proxies) {
                newProxyList.put(proxy.toProxyString(), proxy);
            }
            //remove all failed proxies from new proxy list
            for (Object[] failedProxy : failureResult) {
                newProxyList.remove((String) failedProxy[0]);
            }
            //count used results
            int usedCount = 0;
            for (Object[] used : usedResult) {
                usedCount += Integer.getInteger((String) used[1]);
            }
            return ((newProxyList.size() * settings.getConnections()) - (usedCount
                    + activeDownloadCount)) > 0;
        }
    }
}
