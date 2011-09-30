/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import org.postgresql.util.Base64;

/**
 * Date of create: Sep 28, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0928
 */
public class AuthenticateProxy {
    
    private String username = null;
    private String password = null;
    private String hostname = null;
    private int port;
    private Proxy.Type type = Proxy.Type.HTTP;
    private boolean noProxy = false;
    
    private static Proxy createProxy(String hostname, int port, Proxy.Type type){
        return new Proxy(type, new InetSocketAddress(hostname, port));
    }

    /**
     * this constructor means NO PROXY configuration
     */
    public AuthenticateProxy() {
        this.noProxy = true;
    }
    
    public AuthenticateProxy(String hostname, int port) {
        if(hostname == null){
            throw new NullPointerException("Hostname is null!");
        }
        if(port < 0){
            throw new IllegalArgumentException("Port cannot be negative number!");
        }
        if(hostname.equals("")){
            throw new IllegalArgumentException("Hostname is empty.");
        }
        this.hostname = hostname;
        this.port = port;
    }
    
    public AuthenticateProxy(String hostname, int port, String username, String password) {
        if(hostname == null){
            throw new NullPointerException("Hostname is null!");
        }
        if(username == null){
            throw new NullPointerException("Username is null!");
        }
        if(password == null){
            throw new NullPointerException("Password is null!");
        }
        if(port < 0){
            throw new IllegalArgumentException("Port cannot be negative number!");
        }
        if(username.equals("")){
            username = null;
            password = null;
        }
        if(hostname.equals("")){
            throw new IllegalArgumentException("Hostname is empty.");
        }
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    public AuthenticateProxy(String hostname, int port, Proxy.Type type) {
        if(hostname == null){
            throw new NullPointerException("Hostname is null!");
        }
        if(port < 0){
            throw new IllegalArgumentException("Port cannot be negative number!");
        }
        if(hostname.equals("")){
            throw new IllegalArgumentException("Hostname is empty.");
        }
        this.hostname = hostname;
        this.port = port;
        this.type = type;
    }
    
    public AuthenticateProxy(String hostname, int port, String username, 
            String password, Proxy.Type type) {
        if(hostname == null){
            throw new NullPointerException("Hostname is null!");
        }
        if(username == null){
            throw new NullPointerException("Username is null!");
        }
        if(password == null){
            throw new NullPointerException("Password is null!");
        }
        if(port < 0){
            throw new IllegalArgumentException("Port cannot be negative number!");
        }
        if(username.equals("")){
            username = null;
            password = null;
        }
        if(hostname.equals("")){
            throw new IllegalArgumentException("Hostname is empty.");
        }
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.type = type;
    }
    
    public Proxy getProxy(){
        if(noProxy){
            return Proxy.NO_PROXY;
        }
        return createProxy(hostname, port, type);
    }
    
    /**
     * create authenticated URL connection to the server with given URL. URL 
     * connection is ready to be opened.
     * 
     * @param url address of the resource
     * @return connection with set username and password, ready to connect
     * @throws IOException 
     */
    public HttpURLConnection getAuthenticatedProxyConnection(URL url) throws IOException{
        if(url == null){
            throw new NullPointerException("URL cannot be null.");
        }
        if((username == null) || (password == null)){
            throw new NullPointerException("Userename or password is null!");
        }
        return getConnection(url);
    } 
    
    /**
     * Get connection to the server. If there is set (not <code>null</code>)
     * username and password, connection is with these authentication information
     * otherwise it returns only connection with this proxy details
     * 
     * @param url address of the resource
     * @return connection with set username and password, ready to connect
     * @throws IOException if an I/O exception occurs.
     */
    public HttpURLConnection getConnection(URL url) throws IOException{
        if(url == null){
            throw new NullPointerException("URL cannot be null.");
        }
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(getProxy());
        if((username != null) && (password != null)){
            String encodeBytes = Base64.encodeBytes((username+":"+password).getBytes());
            uc.setRequestProperty("Proxy-Authorization", "Basic " + encodeBytes);
        }
        return uc;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String toProxyString(){
        if(noProxy){
            return "NO PROXY";
        }
        return hostname + ":" + port + "(" + type + ")";
    }

    @Override
    public String toString() {
        if(noProxy){
            return "NO PROXY";
        }
        return username + ":" + password + "@" + toProxyString();
    }
}
