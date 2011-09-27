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
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.HTTPResponseStatusCode;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
public interface RemoteDocument {

    /**
     * get file response status code returned by server
     * @return response status code returned by server
     */
    public HTTPResponseStatusCode getReturnCode();

    /**
     * get file mime-type returned by server
     * @return mime-type of file returned by server
     */
    public String getServerMimeType();

    /**
     * get clean mime type of document - extracted from mime type returned by server
     * @return mime type of document
     */
    public String getMimeType();

    /**
     * get file location
     * @return file location
     */
    public File getFile();

    /**
     * get online resource address
     * @return
     */
    public URL getURL();

    /**
     * set online resource address
     * @param resource address
     */
    public void setURL(URL url);

    /**
     * file encoding
     * @return encoding of file when it is text file and encoding is known or
     * recognizable by cpDetector. Otherwise returns null (in case of non-text file
     * or not recognized encoding)
     */
    public Charset getEncoding();

    /**
     * set document encoding
     */
    public void setEncoding(Charset charset);

    /**
     * get remote content encoding
     * @return encoding name if present, otherwise null
     */
    public Charset getRemoteContentEncoding();

    /**
     * set file - e.g. after transformations
     * @param file new file to set
     */
    public void setFile(File file);

}
