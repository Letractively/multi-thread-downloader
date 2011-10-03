/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.persistence;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Date of create: Sep 28, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0928
 */
@Entity
public class ProxyFailureStatistics implements Serializable {
    @Id
    private String uuid;
    
    private String url;
    private String proxyUrl;
    private String username;
    private String password;
    private String exceptionMessage;
    private Timestamp failure;
    private String urlPattern;

    public ProxyFailureStatistics() {
    }

    public ProxyFailureStatistics(String uuid, String urlPattern, String url, 
            String proxyUrl, String username, String password, 
            String exceptionMessage, Timestamp failure) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.username = username;
        this.password = password;
        this.exceptionMessage = exceptionMessage;
        this.failure = failure;
        this.urlPattern = urlPattern;
        this.uuid = uuid;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public Timestamp getFailure() {
        return failure;
    }

    public void setFailure(Timestamp failure) {
        this.failure = failure;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
}
