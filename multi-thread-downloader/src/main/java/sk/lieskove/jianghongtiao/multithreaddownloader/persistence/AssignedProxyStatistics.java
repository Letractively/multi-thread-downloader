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
public class AssignedProxyStatistics implements Serializable {
    @Id
    private String uuid;
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    
    private String url;
    private String proxyUrl;
    private String username;
    private String password;
    private Timestamp used;
    private String urlPattern;

    public AssignedProxyStatistics() {
    }

    public AssignedProxyStatistics(String uuid, String urlPattern, String url, 
            String proxyUrl, String username, String password, Timestamp lastUsed) {
        this.urlPattern = urlPattern;
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.username = username;
        this.password = password;
        this.used = lastUsed;
        this.uuid = uuid;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Timestamp getUsed() {
        return used;
    }

    public void setUsed(Timestamp used) {
        this.used = used;
    }
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
}
