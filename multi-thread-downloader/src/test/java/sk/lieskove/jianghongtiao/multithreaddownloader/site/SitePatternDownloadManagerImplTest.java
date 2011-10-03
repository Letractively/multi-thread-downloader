/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.site;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.DownloadItemStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.Persist;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 */
public class SitePatternDownloadManagerImplTest extends TestCase {

    SitePatternDownloadManager manager;
    long start = (new Date()).getTime();
    long step = 15000L;
    SiteSettings settings = new SiteSettings();
    String[] urlList = {
        "http://oreilly.com/catalog/expjava/excerpt/index.html",
        "http://download.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/locks/ReentrantLock.html",
        "http://jeremymanson.blogspot.com/2007/08/atomicity-visibility-and-ordering.html",
        "http://www.fi.muni.cz/~popel/red/",
        "http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html",
        "http://jena.sourceforge.net/DB/creating-db-models.html",
        "http://jena.sourceforge.net/DB/",
        "http://en.wikipedia.org/wiki/RDFS",
        "http://www.agilemodeling.com/essays/introductionToAM.htm",
        "http://openjena.org/tutorial/RDF_API/index.html#glos-Resource",
        "http://xmlns.com/foaf/spec/#term_page",
        "http://orsr.sk/search_subjekt.asp",
        "http://mariae.ic.cz/",
        "http://sk.wikibooks.org/wiki/Modlitby:_Ru%C5%BEenec#Mari.C3.A1nsky_ru.C5.BEenec"
    };
    List<RemoteDocumentThread> remDoc = new ArrayList<RemoteDocumentThread>();

    public SitePatternDownloadManagerImplTest(String testName) {
        super(testName);
        for (int i = 0; i < urlList.length; i++) {
            try {
                String url = urlList[i];
                UUID uuid = UUID.randomUUID();
                remDoc.add(new RemoteDocumentThread(new URL(url), uuid.toString()));
                
            } catch (MalformedURLException ex) {
                Logger.getLogger(SitePatternDownloadManagerImplTest.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void setUp() throws Exception {
        //super.setUp();
        settings.setConnections(3);
        settings.setInTime(3);
        settings.setMaxConnections(2);
        settings.setMaxConnectionsPerTime("15/1", "seconds");
        settings.setNoProxyEnabled(true);
        settings.setSitePattern(".*");
        settings.setWaitTimeout("5-1500");

        Random rand = new Random();

        //fill DB
        Persist persist = Persist.getSingleton();
        URL url = new URL("http://google.com");
        UUID uuid = UUID.randomUUID();
        RemoteDocumentThread thread = new RemoteDocumentThread(url, uuid.toString());
        DownloadItem item = new DownloadItem(11, thread, uuid);

        //1316808341002 - almost actual datetime (2011-09-23 21:07 +-)

        item.setAddedToQueue(new Timestamp(start));
        start += step;
        item.setRunThread(new Timestamp(start));
        start += step;
        item.setThreadFinished(new Timestamp(start));
        start += step;

        DownloadItemStatistics statistic = new DownloadItemStatistics(item,
                settings.getSitePattern());
//        persist.persist(statistic);

    }

    @Override
    protected void tearDown() throws Exception {
        //super.tearDown();
    }

    /**
     * Test of addItem method, of class SitePatternDownloadManagerImpl.
     */
    public synchronized  void testAddItem() {
        System.out.println("addItem");


        SitePatternDownloadManagerImpl instance = new SitePatternDownloadManagerImpl(
                settings);
        for (int i = 0; i < urlList.length; i++) {
                DownloadItem item = new DownloadItem(i, remDoc.get(i), UUID.randomUUID());
                instance.addItem(item);
        }
        try {
            this.wait(20000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SitePatternDownloadManagerImplTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        for (Iterator<RemoteDocumentThread> it = remDoc.iterator(); it.hasNext();) {
            try {
                RemoteDocumentThread remoteDocumentThread = it.next();
                remoteDocumentThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SitePatternDownloadManagerImplTest.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Test of getUsageStatistics method, of class SitePatternDownloadManagerImpl.
     */
    public void testGetUsageStatistics() {
//        System.out.println("getUsageStatistics");
//        long maxMinutes = TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES);
//        
//        SitePatternDownloadManagerImpl instance = new SitePatternDownloadManagerImpl(
//                settings);
//        List<Timestamp> resultList = instance.getUsageStatistics(maxMinutes);
//        
//        for (Timestamp timestamp : resultList) {
//            System.out.println(timestamp.toString());
//        }
    }

    /**
     * Test of finishedDownload method, of class SitePatternDownloadManagerImpl.
     */
    public void testFinishedDownload() {
        System.out.println("finishedDownload");
//        RemoteDocument document = null;
//        SitePatternDownloadManagerImpl instance = null;
//        instance.finishedDownload(document);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}
