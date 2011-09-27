/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.site;

import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public interface SitePoolingManager {
    
    /**
     * Request the list of the available proxy servers throw which can manager 
     * forward communication. This request will be forwarded to appropriate 
     * instance of the {@link SitePatternDownloadManager} which will process 
     * request. Manager checks available proxy servers and forward them back. 
     * 
     * @param url for this address we request list of available proxies
     * @return list of available proxy servers which can provide download
     * @throws IllegalArgumentException when no instance of {@link SitePatternDownloadManager}
     * can process URL - no pattern from configuration file was matched.
     */
    public List<Proxy> getAvailableProxy(URL url) throws IllegalArgumentException;
    
    /**
     * Request for link to download. site Pooling manager will forward link to the 
     * correct instance of {@link SitePatternDownloadManager} which will download
     * that link. 
     * 
     * @param url
     * @return
     * @throws NullPointerException when the URL is null
     */
    public UUID addLink(URL url) throws NullPointerException;
    
}
