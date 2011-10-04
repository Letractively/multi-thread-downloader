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
package sk.lieskove.jianghongtiao.multithreaddownloader.download;

import java.sql.Timestamp;
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocumentThread;

/**
 * Date of create: Sep 21, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0921
 */
public class DownloadItem implements Comparable<DownloadItem> {
    
    private DownloadItemState state = DownloadItemState.CREATED;
    private Integer priority;
    private RemoteDocumentThread document;
    private Timestamp addedToQueue;
    private Timestamp runThread;
    private Timestamp threadFinished;
    private UUID uuid;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Timestamp getAddedToQueue() {
        return addedToQueue;
    }

    public void setAddedToQueue(Timestamp addedToQueue) {
        this.addedToQueue = addedToQueue;
    }

    public Timestamp getRunThread() {
        return runThread;
    }

    public void setRunThread(Timestamp runThread) {
        this.runThread = runThread;
    }

    public Timestamp getThreadFinished() {
        return threadFinished;
    }

    public void setThreadFinished(Timestamp threadFinished) {
        this.threadFinished = threadFinished;
    }

    public DownloadItem(Integer priority, RemoteDocumentThread document,
            UUID uuid) {
        this.priority = priority;
        this.document = document;
        this.uuid = uuid;
//        System.n
    }

    public RemoteDocumentThread getDocument() {
        return document;
    }

    public Integer getPriority() {
        return priority;
    }

    public DownloadItemState getState() {
        return state;
    }

    public void setState(DownloadItemState state) {
        this.state = state;
    }

    public int compareTo(DownloadItem o) {
        if(this.priority <= o.priority){
            return 1;
        } else {
            return -1;
        }
    }
    
}
