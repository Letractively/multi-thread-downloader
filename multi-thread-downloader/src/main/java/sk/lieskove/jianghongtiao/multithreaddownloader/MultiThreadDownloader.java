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
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocument;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;
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
    private Map<UUID, List<RemoteDocument>> packages =
            Collections.synchronizedMap(
            new TreeMap<UUID, List<RemoteDocument>>());

    public UUID addLink(URL url) {
        UUID uuid = UUID.fromString(url.toString());
        addLink(url, uuid);
        return uuid;
    }

    public MultiThreadDownloader() {
        //read settings file
        //create regExp classes
        //create selector - povolenie na pridelenie proxy od MT-DM, inak default
    }

    public UUID addLink(URL url, UUID uuid) {
        synchronized (syncObj) {
            List remDoc = Collections.synchronizedList(new ArrayList());
            remDoc.add(new RemoteDocumentThread(url));
            packages.put(uuid, remDoc);
        }
        return uuid;
    }
    
    public UUID addLink(URL url, Integer priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUID addLink(URL url, UUID uuid, Integer priority) {
        return null;
    }

    /**
     * 
     */
    public UUID addLinks(List<URL> url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUID addLinks(List<URL> url, UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public UUID addLinks(List<URL> url, Integer priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUID addLinks(List<URL> url, UUID uuid, Integer priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDownloadReady(UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RemoteFile getDownload(UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<RemoteFile> getPackage(UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static MultiThreadDownloadManager getInstance() {
        synchronized (syncObj) {
            if (manager == null) {
                manager = new MultiThreadDownloader();
            }
        }
        return manager;
    }
    
    
}
