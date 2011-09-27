/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
