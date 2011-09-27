/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.site;

import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public class DefaultSitePoolingManager implements SitePoolingManager {
    
    List<SiteSettings> siteSettings;

    public DefaultSitePoolingManager(List<SiteSettings> siteSettings) {
        this.siteSettings = siteSettings;
    }
    
    private void createSiteDownloadManagers(SiteSettings siteSettings){
        
    }

    public List<Proxy> getAvailableProxy(URL url) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUID addLink(URL url) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
