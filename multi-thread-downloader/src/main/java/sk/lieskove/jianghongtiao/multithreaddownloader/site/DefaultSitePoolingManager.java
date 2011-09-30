/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.site;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public class DefaultSitePoolingManager implements SitePoolingManager {
    
    private final Object syncObj = new Object();
    List<SiteSettings> siteSettings;
    Map<String, Pattern> managersPattern = new TreeMap<String, Pattern>();
    Map<String, SitePatternDownloadManager> downloadManagers = 
            new TreeMap<String, SitePatternDownloadManager>();

    public DefaultSitePoolingManager(List<SiteSettings> siteSettings) {
        this.siteSettings = siteSettings;
        for (SiteSettings settings : siteSettings) {
            Pattern p = Pattern.compile(settings.getSitePattern());
            managersPattern.put(settings.getSitePattern(), p);
            downloadManagers.put(settings.getSitePattern(), new SitePatternDownloadManagerImpl(settings));
        }
    }
    
    /**
     * find download manager for given URL
     * @param url address of resource for which we will look for the 
     * @return
     * @throws IllegalArgumentException 
     */
    private SitePatternDownloadManager getManager(URL url) throws IllegalArgumentException{
        for (String pattern : managersPattern.keySet()) {
            Pattern p = managersPattern.get(pattern);
            if(p.matcher(url.toString()).matches()){
                return downloadManagers.get(pattern);
            }
            
        }
        throw new IllegalArgumentException("No Pattern found in configuration "
                + "file to download such a URL: " + url.toString());
    }
    
    public List<Proxy> getAvailableProxy(URL url) throws IllegalArgumentException {
        List res = new ArrayList();
        res.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("cache.fi.muni.cz", 3128)));
        return res;
    }

    public void addItem(DownloadItem item) throws NullPointerException, IllegalArgumentException {
        if(item == null){
            throw new NullPointerException("Inserted item was null.");
        }
        synchronized(syncObj){
            getManager(item.getDocument().getURL()).addItem(item);
        }
    }
    
}
