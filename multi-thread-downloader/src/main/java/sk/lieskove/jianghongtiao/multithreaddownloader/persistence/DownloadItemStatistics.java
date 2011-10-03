/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DownloadItem;

/**
 * Statistics stored about download item to database. Represents database table.
 * Date of create: Sep 22, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0922
 */
@Entity
@Table
public class DownloadItemStatistics implements Serializable {

    @Id
    private String uuid;
    
    private Timestamp runTime;
    private Timestamp finishTime;
    private Timestamp queueTime;
    private String urlPattern;
    private String url;
    private Integer priority;

    public DownloadItemStatistics() {
    }
    
    public DownloadItemStatistics(DownloadItem item, String pattern) {
        this.finishTime = item.getThreadFinished();
        this.queueTime = item.getAddedToQueue();
        this.runTime = item.getRunThread();
        this.url = item.getDocument().getURL().toString();
        this.urlPattern = pattern;
        this.uuid = item.getUuid().toString();
        this.priority = item.getPriority();
        this.uuid = item.getUuid().toString();
    }

    /**
     * priority of the download
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * priority of the download
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * time when the thread finished downloading of the document
     */
    public Timestamp getFinishTime() {
        return finishTime;
    }

    /**
     * time when the thread finished downloading of the document
     */
    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * time when the item was added to queue, to know how long was waiting
     */
    public Timestamp getQueueTime() {
        return queueTime;
    }

    /**
     * time when the item was added to queue, to know how long was waiting
     */
    public void setQueueTime(Timestamp queueTime) {
        this.queueTime = queueTime;
    }

    /**
     * time when the thread was launched and download process started
     */
    public Timestamp getRunTime() {
        return runTime;
    }

    /**
     * time when the thread was launched and download process started
     */
    public void setRunTime(Timestamp runTime) {
        this.runTime = runTime;
    }

    /**
     * URL of the document to download
     */
    public String getUrl() {
        return url;
    }

    /**
     * URL of the document to download
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * URL pattern, to know which settings was used to process this item
     */
    public String getUrlPattern() {
        return urlPattern;
    }

    /**
     * URL pattern, to know which settings was used to process this item
     */
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
    
}
