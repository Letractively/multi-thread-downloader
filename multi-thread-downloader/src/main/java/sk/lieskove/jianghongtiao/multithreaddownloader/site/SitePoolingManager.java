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

import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;

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
    public void addItem(DownloadItem item) throws NullPointerException, IllegalArgumentException;
    
}
