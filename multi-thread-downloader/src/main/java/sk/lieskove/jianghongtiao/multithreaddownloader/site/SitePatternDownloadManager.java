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

import sk.lieskove.jianghongtiao.multithreaddownloader.concurrency.TimerClient;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public interface SitePatternDownloadManager extends TimerClient{
    
    /**
     * Add new download item to download queue. 
     * 
     * @param item download item
     */
    public void addItem(DownloadItem item);
    
    /**
     * Inform instance of SitePatternDownloadManager about finished download.
     * The running thread will call this method to inform it already finished and
     * it can run another thread for downloading.
     * 
     * @param uuid unique identifier of download link
     */
    public void finishedDownload(RemoteDocumentThread document);
    
}
