/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocument;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItemState;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SettingsReader;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;
import sk.lieskove.jianghongtiao.multithreaddownloader.site.DefaultSitePoolingManager;
import sk.lieskove.jianghongtiao.multithreaddownloader.site.SitePoolingManager;
import sk.lieskove.jianghongtiao.paris.web.content.RemoteFile;

/**
 * Date of create: Sep 18, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0918
 */
public class MultiThreadDownloader implements MultiThreadDownloadManager {

    private static MultiThreadDownloadManager manager = null;
    private static final Object syncObj = new Object();
    private Map<UUID, List<DownloadItem>> packages =
            new ConcurrentHashMap<UUID, List<DownloadItem>>();
    private SitePoolingManager poolingManager;

    public MultiThreadDownloader() {
        //read settings file
        List<SiteSettings> siteSettings = 
                SettingsReader.readXml("settings/connection-settings.xml");
        //create SitePooling manager
        poolingManager = new DefaultSitePoolingManager(siteSettings);
        //create selector - povolenie na pridelenie proxy od MT-DM, inak default
        
        
        
    }

    public RemoteDocument getDownload(UUID uuid) {
        synchronized(syncObj){
            List<RemoteDocument> documents = getPackage(uuid);
            if((documents == null) || (documents.isEmpty())){
                return null;
            }
            if(documents.size() > 1){
                throw new UnsupportedOperationException("This document is the "
                        + "part of the package. Call getPackage method please.");
            }
            return documents.get(0);
        }
    }

    public List<RemoteDocument> getPackage(UUID uuid) {
        synchronized(syncObj){
            List<DownloadItem> items = packages.get(uuid);
            if((items == null) || (items.isEmpty())){
                return null;
            }
            while(true){
                boolean allReady = true;
                for (DownloadItem downloadItem : items) {
                    allReady &= downloadItem.getState() == DownloadItemState.FINISHED;
                }
                if(allReady){
                    List<RemoteDocument> remDocs = new ArrayList<RemoteDocument>();
                    for (DownloadItem downloadItem : items) {
                        remDocs.add(downloadItem.getDocument());
                    }
                    return remDocs;
                }
                try {
                    //wait a second to finish running downloads and check again
                    //do not consume so much processor time and check it periodicaly
                    syncObj.wait(1000);
                } catch (InterruptedException ex) {
                    //do nothing
                }
            }
        }
    }

    public UUID addLinks(List<URL> urls, UUID uuid, Integer priority) {
        synchronized (syncObj) {
            List<DownloadItem> items = packages.get(uuid);
            if (items == null) {
                //there was not such a package with UUID, create a new one
                items = Collections.synchronizedList(
                        new ArrayList<DownloadItem>());
            }
            for (URL url : urls) {
                DownloadItem item = new DownloadItem(priority, 
                        new RemoteDocumentThread(url), UUID.randomUUID());
                items.add(item);
                poolingManager.addItem(item);
            }
            packages.put(uuid, items);
        }
        return uuid;
    }

    public boolean isDownloadReady(UUID uuid) {
        boolean result = true;
        synchronized (syncObj) {
            List<DownloadItem> items = packages.get(uuid);
            for (DownloadItem downloadItem : items) {
                result &= (downloadItem.getState() == DownloadItemState.FINISHED);
            }
        }
        return result;
    }

    public static MultiThreadDownloadManager getInstance() {
        synchronized (syncObj) {
            if (manager == null) {
                manager = new MultiThreadDownloader();
            }
        }
        return manager;
    }

    public UUID addLink(URL url, UUID uuid, Integer priority) {
        List<URL> urls = new ArrayList<URL>();
        urls.add(url);
        return addLinks(urls, uuid, priority);
    }

    public UUID addLink(URL url) {
        return addLink(url, UUID.randomUUID(), 0);
    }

    public UUID addLink(URL url, UUID uuid) {
        return addLink(url, uuid, 0);
    }

    public UUID addLink(URL url, Integer priority) {
        return addLink(url, UUID.randomUUID(), priority);
    }

    public UUID addLinks(List<URL> urls) {
        return addLinks(urls, UUID.randomUUID(), 0);
    }

    public UUID addLinks(List<URL> urls, UUID uuid) {
        return addLinks(urls, uuid, 0);
    }

    public UUID addLinks(List<URL> urls, Integer priority) {
        return addLinks(urls, UUID.randomUUID(), priority);
    }
}
