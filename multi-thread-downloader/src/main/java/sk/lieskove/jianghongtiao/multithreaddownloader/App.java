package sk.lieskove.jianghongtiao.multithreaddownloader;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocument;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SettingsReader;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 * Hello world!
 *
 */
public class App {
    private static String[] urlList = {
        "http://oreilly.com/catalog/expjava/excerpt/index.html",
        "http://download.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/locks/ReentrantLock.html",
        "http://jeremymanson.blogspot.com/2007/08/atomicity-visibility-and-ordering.html",
        "http://www.fi.muni.cz/~popel/red/",
        "http://download.oracle.com/javase/6/docs/technotes/guides/net/proxies.html",
        "http://jena.sourceforge.net/DB/creating-db-models.html",
        "http://jena.sourceforge.net/DB/",
        "http://en.wikipedia.org/wiki/RDFS",
        "http://www.agilemodeling.com/essays/introductionToAM.htm",
        "http://openjena.org/tutorial/RDF_API/index.html#glos-Resource",
        "http://xmlns.com/foaf/spec/#term_page",
        "http://orsr.sk/search_subjekt.asp",
        "http://mariae.ic.cz/",
        "http://sk.wikibooks.org/wiki/Modlitby:_Ru%C5%BEenec#Mari.C3.A1nsky_ru.C5.BEenec"
    };
    
    public static void main( String[] args ) throws MalformedURLException, IOException{
        
        URL url = new URL("http://www.google.com");
        Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("lala.com", 22));
        Proxy m = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("cache.fi.muni.cz", 3128));
        
        URLConnection conn = url.openConnection();
        try {
            conn.connect();
            System.out.println("Successfull connection without proxy");
        } catch (ConnectException e) {
            System.out.println("connect exception was thrown without proxy!");
        }
        
        conn = url.openConnection(p);
        try {
            conn.connect();
            System.out.println("Successfull connection with lala.com:22");
        } catch (ConnectException e) {
            System.out.println("connect exception was thrown with lala.com:22");
        }
        
        conn = url.openConnection(m);
        try {
            conn.connect();
            System.out.println("Successfull connection with cache.fi.muni.cz:3128");
        } catch (ConnectException e) {
            System.out.println("connect exception was thrown with cache.fi.muni.cz:3128");
        }
        
        if(true) return;
        MultiThreadDownloadManager downloadManager = new MultiThreadDownloader();
        UUID uuid = UUID.randomUUID();
        for (String string : urlList) {
            downloadManager.addLink(new URL(string), uuid);
        }
        List<RemoteDocument> aPackage = downloadManager.getPackage(uuid);
        for (RemoteDocument remoteDocument : aPackage) {
            System.out.println(remoteDocument.getFile().getName());
        }
    }
}
