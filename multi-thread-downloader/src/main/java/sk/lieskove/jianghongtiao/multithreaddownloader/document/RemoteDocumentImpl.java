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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.net.URL;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.HTTPResponseStatusCode;
import sk.lieskove.jianghongtiao.paris.utils.MimeUtils;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
@Entity
@Table(name="remote_file")
public class RemoteDocumentImpl implements RemoteDocument, Serializable{

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,
    targetEntity = HTTPResponseStatusCode.class)
    private HTTPResponseStatusCode returnCode;
    private String serverMimeType;
    private String mimeType = null;
    private String location = null;
    private File tmpFile;
    @Transient
    private URL url;
    @Transient
    private Charset charset = null;
    @Transient
        private Charset remoteContentEncoding;
    @Transient
    private Logger log = Logger.getLogger(RemoteDocumentImpl.class.getName());

    public RemoteDocumentImpl() {
    }

    public RemoteDocumentImpl(URL url) {
        this.url = url;
        this.location = url.toString();
    }

    @Transient
    public void retrieveRemoteContent() {
        try {

            HttpURLConnection remoteContent = null;
            remoteContent = (HttpURLConnection) url.openConnection();
            try {
                returnCode = new HTTPResponseStatusCode(remoteContent.getResponseCode());
            } catch (ClassNotFoundException ex) {
                log.info("Response code not found for: " +
                        remoteContent.getResponseCode() + ". ");
                returnCode = null;
            }

            HTTPResponseStatusCode respCode = getReturnCode();
            if (respCode.isSuccess()) {
                serverMimeType = remoteContent.getContentType();
                tmpFile = File.createTempFile("Paris_", ".tmp");
                tmpFile.deleteOnExit();
                remoteContentEncoding = RemoteFileEncoding.
                        makeEncoding(remoteContent.getContentEncoding());
                InputStream is = remoteContent.getInputStream();
                OutputStream os = new FileOutputStream(tmpFile);
                byte[] buf = new byte[8192];
                while (true) {
                    int length = is.read(buf);
                    if (length < 0) {
                        break;
                    }
                    os.write(buf, 0, length);
                }
                os.flush();
                os.close();
            }
        } catch (IOException ex) {
            log.error("Cannot read file: " + tmpFile.getAbsolutePath(), ex);
            tmpFile = null;
            returnCode = null;
            serverMimeType = null;
        }
    }

    @Override
    public HTTPResponseStatusCode getReturnCode() {
        return returnCode;
    }

    @Override
    public String getServerMimeType() {
        return serverMimeType;
    }

    @Override
    public File getFile() {
        return tmpFile;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public void setURL(URL url) {
        this.url = url;
        this.location = url.toString();
    }

    @Override
    public String toString() {
        return "RemoteFileImpl{" + "\n\treturnCode=" + returnCode + "\n\tmimeType=" + serverMimeType
                + "\n\ttmpFile=" + tmpFile + "\n\turl=" + url + "\n}";
    }

    @Override
    public Charset getEncoding() {
        return charset;
    }

    @Override
    public Charset getRemoteContentEncoding() {
        return remoteContentEncoding;
    }

    @Override
    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String getMimeType() {
        if(mimeType == null){
            mimeType = MimeUtils.getMimeTypeFromRemoteContentType(serverMimeType);
        }
        return mimeType;
    }

    @Override
    public void setFile(File file) {
        tmpFile = file;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
