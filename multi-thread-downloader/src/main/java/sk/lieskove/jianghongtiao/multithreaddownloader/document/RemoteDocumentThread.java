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
package sk.lieskove.jianghongtiao.multithreaddownloader.document;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.HTTPResponseStatusCode;
import sk.lieskove.jianghongtiao.multithreaddownloader.site.SitePatternDownloadManager;

/**
 * Download Remote file on thread - for faster downloading
 *
 * @author xjuraj
 */
public class RemoteDocumentThread extends Thread implements RemoteDocument {

    private RemoteDocumentImpl remoteFile = new RemoteDocumentImpl();
    private Logger log = Logger.getLogger(RemoteDocumentThread.class.getName());
    private SitePatternDownloadManager spdm;

    public RemoteDocumentThread() {
    }

    public RemoteDocumentThread(URL url) {
        remoteFile.setURL(url);
    }

    @Override
    public void run() {
        log.debug("Running download thread for url: " + getURL());
        this.setName("RemoteDocumentThread: " + getURL());
        remoteFile.retrieveRemoteContent();
        spdm.finishedDownload(this);
        log.debug("Finished download thread for url: " + getURL());
    }

    @Override
    public void setURL(URL url) {
        remoteFile.setURL(url);
    }

    public RemoteDocumentImpl getRemoteFile() {
        return remoteFile;
    }

    @Override
    public HTTPResponseStatusCode getReturnCode() {
        return remoteFile.getReturnCode();
    }

    @Override
    public String getServerMimeType() {
        return remoteFile.getServerMimeType();
    }

    @Override
    public File getFile() {
        return remoteFile.getFile();
    }

    @Override
    public URL getURL() {
        return remoteFile.getURL();
    }

    @Override
    public Charset getEncoding() {
        return remoteFile.getEncoding();
    }

    @Override
    public Charset getRemoteContentEncoding() {
        return remoteFile.getRemoteContentEncoding();
    }

    @Override
    public void setEncoding(Charset charset) {
        remoteFile.setEncoding(charset);
    }

    @Override
    public String getMimeType() {
        return remoteFile.getMimeType();
    }

    @Override
    public void setFile(File file) {
        remoteFile.setFile(file);
    }

    public void setSitePatternDownloadManager(SitePatternDownloadManager spdm){
        this.spdm = spdm;
    }

}
