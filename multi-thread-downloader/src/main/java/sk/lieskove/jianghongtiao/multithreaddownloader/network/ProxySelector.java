/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.network;

import java.net.URL;
import java.util.List;

/**
 * Proxy selector is responsible for managing proxies assigned to every download
 * Class is responsible for statistics about proxy failure but also about assigned
 * proxies to every download. Proxy selector will return list of proxies which 
 * given download can use. Every download will try these proxies in the order
 * they appear in the list. In case of success, the first one is used. In the case
 * of connection failure next one is picked. Download will inform which proxy used
 * but also which proxy does not succeed. Instance also control how many times 
 * which proxy was used and compare it with maximal number of connections in 
 * time unit. When number of connections was reached, proxy server is removed
 * from the list of available proxies. It is returned back when it can provide
 * all new connections or when no more proxies in the list of available left.
 * 
 * Date of create: Sep 28, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0928
 */
public interface ProxySelector {
    
    /**
     * Get list of available proxies which can be used to download resources from the Internet. 
     * 
     * @return list of available proxies
     */
    public List<AuthenticateProxy> getProxyList();
    
    /**
     * Inform instance about proxy failure. When connection to the resource
     * fails because of proxy error, instance is informed and it writes it to
     * the database. Instance can use this information and temporary remove
     * this proxy from the list of available proxies. When instance make so, it 
     * check also in the future if it is possible to connect again thorough this
     * server.
     * 
     * @param proxy not available proxy server
     * @param url resource location to download
     */
    public void proxyFailure(AuthenticateProxy proxy, URL url, 
            String exception);
    
    /**
     * Inform instance about used proxy for URL download. When URL is successfully 
     * downloaded or connection through which proxy connection success, download 
     * thread inform instance about this and it writes to the database. 
     * 
     * @param proxy proxy used for download resource URL
     * @param url resource location to download
     * @param usedProxies list of proxies used during downloading
     */
    public void proxyUse(AuthenticateProxy proxy, URL url, List<AuthenticateProxy> usedProxies);
    
    /**
     * Inform instance that no more proxies to connect left and that download finished with failure.
     * Instance makes steps based on this information. 
     * 
     * @param url URL of failed download
     * @param usedProxies list of proxies used during downloading
     */
    public void noMoreProxies(URL url, List<AuthenticateProxy> usedProxies);
    
    /**
     * Ask instance if there is any proxy server to use in the other download. 
     * If in the list is more proxy servers which can be used for a download,
     * instance can eventually over reach number of maximal connections per time
     * simply by changing used proxy server with another IP address
     * 
     * @param numberOfRunningDownloads means how many active download threads are running
     * @return <code>true</code> if there is ability to use another proxy server, otherwise <code>false</code>.
     */
    public boolean canUseAnotherProxy();
    
}
