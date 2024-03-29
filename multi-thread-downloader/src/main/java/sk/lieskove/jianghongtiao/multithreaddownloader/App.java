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
    private static String[] testList = {
"http://www.2advanced.com/",
"http://www.411.com/",
"http://www.abebooks.com/",
"http://www.about.com/",
"http://www.acefitness.com/",
"http://www.agentprovocateur.com/",
"http://www.akqa.com/",
"http://www.aldaily.com/",
"http://www.alennox.net/",
"http://www.alexa.com/",
"http://www.allmusic.com/",
"http://www.allposters.com/",
"http://www.almapbbdo.com.br/",
"http://www.altmedicine.com/",
"http://www.amazon.com/",
"http://www.andante.com/",
"http://www.anywho.com/",
"http://www.artforum.com/",
"http://www.artless.gr.jp/",
"http://www.artsjournal.com/",
"http://www.arturofuentes.com/",
"http://www.bannerblog.com.au/",
"http://www.bartleby.com/",
"http://www.bbb.org/",
"http://www.bbc.co.uk/religion/",
"http://www.bbc.com/",
"http://www.beelinetv.com/",
"http://www.beliefnet.com/",
"http://www.bernhardwolff.com/",
"http://www.bigspaceship.com/",
"http://www.billboard.com/",
"http://www.bizrate.com/",
"http://www.blogger.com/",
"http://www.books.google.com/",
"http://www.canyon.com/",
"http://captology.stanford.edu/",
"http://www.careerbuilder.com/",
"http://www.cartype.com/",
"http://www.chacha.com/",
"http://www.citysearch.com/",
"http://www.classical.net/",
"http://www.classmates.com/",
"http://www.cnet.com/",
"http://www.cnn.com/",
"http://www.coft1.com/",
"http://www.colette.fr/",
"http://www.craigslist.org/",
"http://www.creativecommons.org/",
"http://www.creaturesinmyhead.com/",
"http://www.curiosityshoppeonline.com/",
"http://www.d-o-e-s.com/",
"http://www.dafont.com/",
"http://del.icio.us/",
"http://dictionary.reference.com/",
"http://www.digg.com/",
"http://www.domanistudios.com/",
"http://dominiopublico.gov.br/",
"http://www.download.com/",
"http://www.earthcam.com/",
"http://www.ebay.com/",
"http://en.proverbia.net/",
"http://www.encarta.com/",
"http://www.epinions.com/",
"http://www.espn.com/",
"http://europa.eu.int/",
"http://www.eurorscg4d.com/",
"http://www.expedia.com/",
"http://www.farfar.se/",
"http://www.fedworld.gov/jobs/jobsearch.html",
"http://www.feelthepower.biz/",
"http://www.findlaw.com/",
"http://www.firstbornmultimedia.com/",
"http://www.firstgov.gov/",
"http://www.freddyandma.com/",
"http://www.ge.com/",
"http://www.give.org/",
"http://www.gnu.org/",
"http://groups.google.com/",
"http://news.google.com/",
"http://www.group94.com/",
"http://www.grupow.com/circulo",
"http://www.guidestar.org/",
"http://www.gutenberg.net/",
"http://www.gutenberg.org/",
"http://www.headbangers.tv/",
"http://www.heftyrecords.com/",
"http://www.heiwa-alpha.co.jp/",
"http://www.hi-res.net/",
"http://www.hotmail.com/",
"http://www.howstuffworks.com/",
"http://www.icp.org/",
"http://www.iht.com/",
"http://www.imaginemusicstore.com/",
"http://www.imdb.com/",
"http://www.infoplease.com/",
"http://www.infopresse.com/prixboomerang",
"http://www.intelihealth.com/",
"http://www.ipl.org/",
"http://www.jokes.com/",
"http://kraftwerk.blocmedia.net/",
"http://kuler.adobe.com/",
"http://www.lawsofsimplicity.com/",
"http://lipsum.com/",
"http://www.livejournal.com/",
"http://www.loc.gov/",
"http://www.lrb.co.uk/",
"http://www.lowetesch.com/",
"http://www.madehow.com/",
"http://www.magwerk.com/",
"http://www.mapquest.com/",
"http://www.maps.live.com/",
"http://www.marcelod2.com.br/",
"http://mathworld.wolfram.com/",
"http://www.matthewmahon.com/",
"http://www.mayoclinic.com/",
"http://www.mecano.ca/",
"http://www.medlineplus.gov/",
"http://www.mine-control.com/",
"http://moneycentral.msn.com/",
"http://www.monster.com/",
"http://www.msdewey.com/",
"http://vlmp.museophile.com/",
"http://www.nationmaster.com/",
"http://www.nature.com/",
"http://www.netdiver.net/illustration",
"http://www.new7wonders.com/",
"http://www.nih.gov/",
"http://nikeid.nike.com/",
"http://www.nolopress.com/",
"http://www.northkingdom.com/",
"http://www.nutrition.gov/",
"http://www.nybooks.com/",
"http://www.nytimes.com/",
"http://www.oneill.com/",
"http://onlinebooks.library.upenn.edu/",
"http://www.dmoz.org/",
"http://www.ourtype.be/",
"http://www.papertoys.com/",
"http://lpi.oregonstate.edu/infocenter/",
"http://www.pcmag.com/",
"http://www.photogravure.com/",
"http://www.podcasts.yahoo.com/",
"http://www.podtropolis.com/",
"http://www.pogo.com/",
"http://www.priceline.com/",
"http://www.quicken.com/",
"http://www.radioblogclub.com/",
"http://readme.cc/",
"http://www.refdesk.com/",
"http://www.reference.com/",
"http://www.research.philips.com/",
"http://www.reuters.com/",
"http://www.rga.com/",
"http://www.rjnet.com.br/2velocimetro_php",
"http://www.rottentomatoes.com/",
"http://www.sacred-texts.com/",
"http://www.scholar.google.com/",
"http://www.sciam.com/",
"http://www.scirus.com/",
"http://www.search.com/",
"http://www.shopcomposition.com/",
"http://www.slate.com/",
"http://www.sohodolls.co.uk/",
"http://www.soleilnoir.com/",
"http://www.ucalgary.ca/%7Elipton/journals.html",
"http://www.springwise.com/",
"http://www.squidfingers.com/patterns",
"http://www.submarinechannel.com/titlesequences",
"http://www.tate.org.uk/",
"http://www.thefwa.com/",
"http://www.thinkingwithtype.com/",
"http://www.thomasedison.org/",
"http://www.ticketmaster.com/",
"http://www.time.com/",
"http://www.timesonline.co.uk/",
"http://www.touchgraph.com/",
"http://world.altavista.com/",
"http://www.trendwatching.com/",
"http://www.tutorialblog.org/free-vector-downloads",
"http://www.ucomics.com/",
"http://www.un.org/",
"http://www.usatoday.com/news/states/ns1.htm",
"http://www.useit.com/",
"http://www.uva.co.uk/",
"http://www.vincent-vella.com/",
"http://www.visual-literacy.org/",
"http://vixy.net/",
"http://www.vlib.org/",
"http://www.vote-smart.org/",
"http://www.weather.com/",
"http://www.webmd.com/",
"http://www.webring.org/",
"http://www.wefail.com/",
"http://www.wikipedia.org/",
"http://www.wikitravel.org/",
"http://windowsmedia.msn.com/radiotuner/",
"http://groups.yahoo.com/",
"http://www.yugop.com/"
    };
    
    public static void main( String[] args ) throws MalformedURLException, IOException{
        
        MultiThreadDownloadManager downloadManager = MultiThreadDownloader.getInstance();
        UUID uuid = UUID.randomUUID();
        for (String string : testList) {
            downloadManager.addLink(new URL(string), uuid);
        }
        List<RemoteDocument> aPackage = downloadManager.getPackage(uuid);
        int good=0, bad=0;
        for (RemoteDocument remoteDocument : aPackage) {
            if(remoteDocument.downloadSucceed()){
                good++;
                System.out.println(remoteDocument.getFile().getName());
            } else {
                bad++;
            }
        }
        System.out.println("Downloaded "+good+" of "+testList.length+
                " documents. "+bad+" caused connection error.");
        System.in.read();
    }
}
