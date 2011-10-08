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

import com.gargoylesoftware.htmlunit.BinaryPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.DefaultPostProcessorManager;
import sk.lieskove.jianghongtiao.multithreaddownloader.download.PostProcessorManager;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.AuthenticateProxy;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.HTTPResponseStatusCode;
import sk.lieskove.jianghongtiao.multithreaddownloader.network.ProxySelector;
import sk.lieskove.jianghongtiao.paris.utils.FileUtils;
import sk.lieskove.jianghongtiao.paris.utils.MimeUtils;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
@Entity
@Table(name = "remote_file")
public class RemoteDocumentImpl implements RemoteDocument, Serializable {

    @Id
    private String uuid;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,
    targetEntity = HTTPResponseStatusCode.class)
    private HTTPResponseStatusCode returnCode;
    private String serverMimeType;
    private String mimeType = null;
    private String location = null;
    private Page page;
    private Timestamp fetchTime;
    private Timestamp loadingTime;
    private String proxyString;
    private File file;
    @Transient
    private URL url;
    @Transient
    private Charset charset = null;
    @Transient
    private Charset remoteContentEncoding;
    @Transient
    private Logger log = Logger.getLogger(RemoteDocumentImpl.class.getName());
    @Transient
    private ProxySelector ps = null;
    private Boolean downloadSucceed = true;

    public RemoteDocumentImpl() {
    }

    public RemoteDocumentImpl(URL url, String uuid) {
        if (url == null) {
            throw new NullPointerException("URL was null!");
        }
        this.url = url;
        this.location = url.toString();
        this.uuid = uuid;
        try {
            this.file = File.createTempFile("MTDM_", ".tmp");
            file.deleteOnExit();
        } catch (IOException ex) {
            log.error("cannot create temporary file for URL: " + url);
        }
    }

    private void setProxy(AuthenticateProxy authenticateProxy,
            WebClient webClient) {
        if (authenticateProxy.isProxy()) {
            webClient.setProxyConfig(new ProxyConfig(
                    authenticateProxy.getHostname(),
                    authenticateProxy.getPort()));
            if ((authenticateProxy.getUsername() != null)
                    && (authenticateProxy.getPassword() != null)) {
                //set proxy username and password 
                final DefaultCredentialsProvider credentialsProvider =
                        (DefaultCredentialsProvider) webClient.
                        getCredentialsProvider();
                credentialsProvider.addCredentials(
                        authenticateProxy.getUsername(),
                        authenticateProxy.getPassword());
            }
        } else {
            webClient.setProxyConfig(new ProxyConfig());
        }
    }

    @Transient
    public void retrieveRemoteContent() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3_6);

        webClient.setCssEnabled(false);
        webClient.setAppletEnabled(false);
        webClient.setRedirectEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setThrowExceptionOnFailingStatusCode(true);
        List<AuthenticateProxy> proxies = ps.getProxyList();
        for (AuthenticateProxy authenticateProxy : proxies) {
            setProxy(authenticateProxy, webClient);
            try {
                long start = System.currentTimeMillis();
                page = webClient.getPage(url);
                if (page != null) {
                    returnCode = new HTTPResponseStatusCode(page.getWebResponse().
                            getStatusCode());
                }
                ps.proxyUse(authenticateProxy, url, uuid, proxies);
                setLoadingTime(new Timestamp(System.currentTimeMillis() - start));
                Date date = new Date();
                setFetchTime(new Timestamp(date.getTime()));
                setProxyString(authenticateProxy.toString());

                break;
            } catch (ClassNotFoundException ex) {
                log.error("Error code was not found!");
            } catch (IOException ex) {
                log.info("Download through proxy: "
                        + authenticateProxy.toString() + " failed.");
                ps.proxyFailure(authenticateProxy, url, uuid, ex.getMessage());
            } catch (FailingHttpStatusCodeException ex) {
                log.info("Cannot download file: " + url);
                ps.proxyFailure(authenticateProxy, url, uuid, ex.getMessage());
                failedToDownload();
            }
        }
        if (page == null) {
            ps.noMoreProxies(url, uuid, proxies);
        }

        if ((returnCode != null) && (returnCode.isSuccess())) {
            PostProcessorManager postProcessor = new DefaultPostProcessorManager();
            serverMimeType = page.getWebResponse().getContentType();
            MimeType mimeT = null;
            try {
                mimeT = new MimeType(serverMimeType);
            } catch (MimeTypeParseException ex) {
                log.warn("Server Mime Type is strange: " + serverMimeType);
            }
            if ((page instanceof HtmlPage) || (page instanceof SgmlPage)
                    || (page instanceof XHtmlPage) || (page instanceof XmlPage)) {
                //apply page filters
                page = postProcessor.launchPagePostProcessing(url,
                        mimeT, page);
                SgmlPage pg = (SgmlPage) page;
                try {
                    FileUtils.write(file, "UTF-8", pg.asXml());
                } catch (IOException ex) {
                    log.error("Failed to save file: " + file.getAbsolutePath()
                            + " for URL: " + url);
                }
            } else {
                if ((page instanceof JavaScriptPage)
                        || (page instanceof UnexpectedPage)
                        || (page instanceof BinaryPage)
                        || (page instanceof TextPage)) {
                    //download file
                    file = downloadFile(file, page);
                    //apply file filters
                    file = postProcessor.launchFilePostProcessing(url,
                            mimeT, file);
                    page = null;
                } else {
                    log.error("Type of downloaded page is not supported!");
                }
            }

            remoteContentEncoding = RemoteFileEncoding.makeEncoding(
                    page.getWebResponse().getContentCharset());
        } else {
            failedToDownload();
        }




//        HttpURLConnection remoteContent = null;
//        try {
//            if (ps == null) {
//                remoteContent = (HttpURLConnection) url.openConnection();
//            } else {
//                List<AuthenticateProxy> proxies = ps.getProxyList();
//                for (AuthenticateProxy authenticateProxy : proxies) {
//                    try {
//                        remoteContent = null;
//                        HttpURLConnection connection = authenticateProxy.
//                                getConnection(url);
//                        connection.setRequestProperty("Us" + "er" + "-A" + "gen"
//                                + "t",
//                                "Mozilla/5.0 (X11; Linux i686; rv:7.0.1) Gecko/20100101 Firefox/7.0.1" //"M"+"ul"+ "ti"+ " "+ "T"+ "h"+ "r"+ "e"+ "a"+ "d"
//                                //+ " "+ "Do"+ "wnl"+ "oad"+ " "+ "Ma"+ "n"+ "age"+ 
//                                //"r - "+ "ht"+ "tp"+ ":/"+ "/co"+ "de.g"+ "oogl"
//                                //+ "e.c"+ "om/"+ "p/m"+ "ult"+ "i-th"+ "re"+ "ad-"
//                                //+ "dow"+ "nlo"+ "ade"+ "r/ "+ "(b"+ "y J"+ "ia"
//                                //+ "ng"+ "Ho"+ "n"+ "gTi"+ "ao)"
//                                );
//                        connection.connect();
//                        remoteContent = connection;
//                        ps.proxyUse(authenticateProxy, url, uuid, proxies);
//                        if (remoteContent != null) {
//                            returnCode = new HTTPResponseStatusCode(remoteContent.
//                                    getResponseCode());
//                        }
//                        break;
//                    } catch (SocketTimeoutException e) {
//                        log.info("Socket timeout for URL: " + url.toString(), e);
//                        failedToDownload();
//                    } catch (ConnectException ce) {
//                        ps.proxyFailure(authenticateProxy, url, uuid, ce.
//                                getMessage());
//                        failedToDownload();
//                    } catch (UnknownHostException e) {
//                        log.info("Cannot connect to the host: " + url.toString());
//                        failedToDownload();
//                    } catch (NoRouteToHostException e) {
//                        log.info("No route to host: " + url.toString());
//                        failedToDownload();
//                    } catch (PortUnreachableException e) {
//                        log.info("Port is unreachable: " + url.toString());
//                        failedToDownload();
//                    } catch (ProtocolException e) {
//                        log.error("Protocol TCP/IP exception: " + url.toString());
//                        failedToDownload();
//                    } catch (SocketException e) {
//                        log.error("Socket exception: " + url.toString());
//                        failedToDownload();
//                    } catch (UnknownServiceException e) {
//                        log.error("Unknown service exception: " + url.toString());
//                        failedToDownload();
//                    } catch (ClassNotFoundException ex) {
//                        log.info("Response code not found for: "
//                                + remoteContent.getResponseCode() + ". ");
//                        returnCode = null;
//                    }
//                }
//                if (remoteContent == null) {
//                    ps.noMoreProxies(url, uuid, proxies);
//                }
//            }
//
//            HTTPResponseStatusCode respCode = getReturnCode();
//            if ((returnCode != null) && (respCode.isSuccess())) {
//                serverMimeType = remoteContent.getContentType();
//                file = File.createTempFile("MTDM_", ".tmp");
//                file.deleteOnExit();
//                remoteContentEncoding = RemoteFileEncoding.makeEncoding(remoteContent.
//                        getContentEncoding());
//                InputStream is = remoteContent.getInputStream();
//                OutputStream os = new FileOutputStream(file);
//                byte[] buf = new byte[8192];
//                while (true) {
//                    int length = is.read(buf);
//                    if (length < 0) {
//                        break;
//                    }
//                    os.write(buf, 0, length);
//                }
//                os.flush();
//                os.close();
//            } else {
//                failedToDownload();
//            }
//        } catch (IOException ex) {
//            if (file == null) {
//                log.error("Cannot read URL: " + url.toString(), ex);
//            } else {
//                log.error("Cannot read file: " + file.getAbsolutePath(), ex);
//            }
//            failedToDownload();
//        }
    }

    private File downloadFile(File out, Page p) {
        OutputStream os = null;
        try {
            InputStream is = p.getWebResponse().getContentAsStream();
            os = new FileOutputStream(out);
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
        } catch (FileNotFoundException ex) {
            log.error("File was not found: " + out.getAbsolutePath());
            failedToDownload();
        } catch (IOException ex) {
            log.error("I/O error with file: " + out.getAbsolutePath());
            failedToDownload();
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                log.error("Error ocured while trying to close file.");
            }
        }
        return out;
    }

    private void failedToDownload() {
        file = null;
        returnCode = null;
        serverMimeType = null;
        downloadSucceed = false;
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
        return file;
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
        return "RemoteFileImpl{" + "\n\treturnCode=" + returnCode
                + "\n\tmimeType=" + serverMimeType
                + "\n\ttmpFile=" + file + "\n\turl=" + url + "\n}";
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
        if (mimeType == null) {
            mimeType = MimeUtils.getMimeTypeFromRemoteContentType(serverMimeType);
        }
        return mimeType;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUUID() {
        return this.uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public ProxySelector getProxySelector() {
        return ps;
    }

    public void setProxySelector(ProxySelector ps) {
        this.ps = ps;
    }

    public boolean downloadSucceed() {
        return downloadSucceed.booleanValue();
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFetchTime(Timestamp fetchTime) {
        this.fetchTime = fetchTime;
    }

    public void setLoadingTime(Timestamp loadingTime) {
        this.loadingTime = loadingTime;
    }

    public void setProxyString(String proxyString) {
        this.proxyString = proxyString;
    }

    public Timestamp getFetchTime() {
        return fetchTime;
    }

    public Timestamp getLoadingTime() {
        return loadingTime;
    }

    public String getProxyString() {
        return proxyString;
    }
}
