package sk.lieskove.jianghongtiao.multithreaddownloader;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SettingsReader;
import sk.lieskove.jianghongtiao.multithreaddownloader.settings.SiteSettings;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static Object sync = new Object();
    private final static Object sync2 = new Object();
    
    private static void writeMsg(){
        synchronized(sync){
            System.out.println("Inside message");
        }
    }
    
    public static void main( String[] args )
    {
        synchronized(sync){
            System.out.println("Outside message");
            writeMsg();
            
            System.out.println("Outside message");
        }
        Date date = new Date();
        Timestamp t = new Timestamp(date.getTime());
        System.out.println(t.getNanos());
        
        if(true)return;
        List<SiteSettings> settings = SettingsReader.
                readXml("test-files/connection-settings.xml");
        for (SiteSettings siteSettings : settings) {
            System.out.println(siteSettings);
        }
    }
}
