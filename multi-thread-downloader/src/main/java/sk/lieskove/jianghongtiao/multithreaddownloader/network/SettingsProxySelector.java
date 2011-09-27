/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Date of create: Sep 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0915
 */
public class SettingsProxySelector extends ProxySelector {
    // Keep a reference on the previous default

    ProxySelector defsel = null;

    /**
     * Inner class representing a Proxy and a few extra data
     */
    class InnerProxy {

        Proxy proxy;
        SocketAddress addr;
        // How many times did we fail to reach this proxy?
        int failedCount = 0;

        InnerProxy(InetSocketAddress a) {
            addr = a;
            proxy = new Proxy(Proxy.Type.HTTP, a);
        }

        SocketAddress address() {
            return addr;
        }

        Proxy toProxy() {
            return proxy;
        }

        int failed() {
            return ++failedCount;
        }
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        
        String protocol = uri.getScheme();
        if ("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(
                protocol)) {
            ArrayList<Proxy> l = new ArrayList<Proxy>();
//            for (InnerProxy p : proxies.values()) {
//                l.add(p.toProxy());
//            }
            return l;
        }

        /*
         * Not HTTP or HTTPS (could be SOCKS or FTP)
         * defer to the default selector.
         */
        if (defsel != null) {
            return defsel.select(uri);
        } else {
            ArrayList<Proxy> l = new ArrayList<Proxy>();
            l.add(Proxy.NO_PROXY);
            return l;
        }


    }
    /*
     * Method called by the handlers when it failed to connect
     * to one of the proxies returned by select().
     */

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {                // Let's stick to the specs again.
        if (uri == null || sa == null || ioe == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }

        /*
         * Let's lookup for the proxy 
         */
//        InnerProxy p = proxies.get(sa);
//        if (p != null) {
//            /*
//             * It's one of ours, if it failed more than 3 times
//             * let's remove it from the list.
//             */
//            if (p.failed() >= 3) {
//                proxies.remove(sa);
//            }
//        } else {
//            /*
//             * Not one of ours, let's delegate to the default.
//             */
//            if (defsel != null) {
//                defsel.connectFailed(uri, sa, ioe);
//            }
//        }
    }
}
