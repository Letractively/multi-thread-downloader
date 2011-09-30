/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.AssignedProxyStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.Persist;
import sk.lieskove.jianghongtiao.multithreaddownloader.persistence.ProxyFailureStatistics;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 */
public class SettingsProxySelectorTest extends TestCase {
    
    public SettingsProxySelectorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getProxyList method, of class SettingsProxySelector.
     */
    public void testSettingsProxySelector() {
        try {
            System.out.println("SettingsProxySelector");
            SiteSettings settings = new SiteSettings();
            
            settings.setConnections(3);
            settings.setInTime(3);
            settings.setMaxConnections(2);
            settings.setMaxConnectionsPerTime("3/10", "seconds");
            settings.setNoProxyEnabled(true);
            settings.setSitePattern(".*");
            settings.setWaitTimeout("none");

            AuthenticateProxy proxy = new AuthenticateProxy("cache.muni.cz", 3128);
            AuthenticateProxy proxy2 = new AuthenticateProxy("cache2.muni.cz", 3128);
            List<AuthenticateProxy> listProxy = new ArrayList<AuthenticateProxy>();
            listProxy.add(proxy);
            listProxy.add(proxy2);
            settings.setProxies(listProxy);
            
            URL url = new URL("http://google.com");
            SettingsProxySelector instance = new SettingsProxySelector(settings);
            // 1. Can Use Another proxy? => TRUE
            assertTrue("First time", instance.canUseAnotherProxy());
            // 2. Simulate 2x download (1. proxy), list + finished download
            List<AuthenticateProxy> p11 = instance.getProxyList();
            List<AuthenticateProxy> p12 = instance.getProxyList();
            assertEquals("Simulate download, proxy 1.", 3, p11.size());
            assertEquals("Simulate download, proxy 1.", 3, p12.size());
            instance.proxyUse(proxy, url, p11);
            instance.proxyUse(proxy, url, p12);
            // 3. Can Use Another proxy? => TRUE
            assertTrue("Second time (after first insert)", instance.canUseAnotherProxy());
            // 4. Fail - 1.proxy + 2.proxy 2x
            instance.proxyFailure(proxy,  url, "1. Test exception (proxy 1)");
            instance.proxyFailure(proxy2, url, "1. Test exception (proxy 2)");
            instance.proxyFailure(proxy2, url, "2. Test exception (proxy 2)");
            // 5. Can Use Another proxy? => TRUE
            assertTrue("Second time (after failure p1, p2 2x)", instance.canUseAnotherProxy());
            // 6. Simulate download (2.proxy, 1.proxy), list + finish download
            List<AuthenticateProxy> p21 = instance.getProxyList();
            List<AuthenticateProxy> p13 = instance.getProxyList();
            assertEquals("Simulate download, proxy 2.", 3, p21.size());
            assertEquals("Simulate download, proxy 1.", 3, p13.size());
            instance.proxyUse(proxy2, url, p21);
            instance.proxyUse(proxy, url, p13);
            // 7. Can Use Another proxy? => TRUE
            assertTrue("7. Can Use Another proxy? => TRUE", instance.canUseAnotherProxy());
            // 8. Simulate download - download failed 
            List<AuthenticateProxy> pf = instance.getProxyList();
            assertEquals("Simulate download, download failed. Only 2 proxies left.", 2, pf.size());
            instance.noMoreProxies(url, pf);
            // 9. Can Use Another proxy? => TRUE
            assertTrue("9. Can Use Another proxy? => TRUE", instance.canUseAnotherProxy());
            // 10. Simulate download 3x (3.proxy) - ask for list (2.,3.proxy)
            List<AuthenticateProxy> p31 = instance.getProxyList();
            List<AuthenticateProxy> p32 = instance.getProxyList();
            List<AuthenticateProxy> p33 = instance.getProxyList();
            assertEquals("Simulate download, proxy 3.", 2, p31.size());
            assertEquals("Simulate download, proxy 3.", 2, p32.size());
            assertEquals("Simulate download, proxy 3.", 2, p33.size());
            instance.proxyUse(p11.get(2), url, p31);
            instance.proxyUse(p11.get(2), url, p32);
            instance.proxyUse(p11.get(2), url, p33);
            // 11. Cannot use another proxy, because proxy 2 failed two times in last one minute
            assertTrue("11. Can Use Another proxy? => False", instance.canUseAnotherProxy());
            List<AuthenticateProxy> p22 = instance.getProxyList();
            assertEquals("Simulate download, proxy 2.", 1, p22.size());
            
            assertEquals("Check if second proxy left in potentionally available proxies.", 
                    p22.get(0), proxy2);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(SettingsProxySelectorTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        
    }

}
