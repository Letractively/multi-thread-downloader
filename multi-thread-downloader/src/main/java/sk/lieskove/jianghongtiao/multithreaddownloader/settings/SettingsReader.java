/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.settings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Date of create: Sep 18, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0918
 */
public class SettingsReader {

    private static Logger log = Logger.getLogger(SettingsReader.class.getName());
    private static XPath xPath = XPathFactory.newInstance().newXPath();

    public static List<SiteSettings> readXml(String fileName) {
        List<SiteSettings> result = new ArrayList<SiteSettings>();
        try {
            Document doc = openDocument(fileName);
            NodeList nodes = getNodeListByXPathQuery("//downlaod-settings/site",
                    doc);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                SiteSettings settings = new SiteSettings();
                result.add(settings);
                //------------------------make settings-------------------------

                Double maxConn = (Double) xPath.evaluate("max-connections", node,
                        XPathConstants.NUMBER);
                String wait = getSubnode("wait-timeout", node).getTextContent();
                String maxConnPerTime = getSubnode("max-connections-per-time",
                        node).getTextContent();
                String maxConnPerTimeTimeUnit = getSubnode(
                        "max-connections-per-time@timeUnit", node).
                        getTextContent();
                Boolean noProxy = (Boolean) xPath.evaluate("no-proxy-possible",
                        node, XPathConstants.BOOLEAN);
                List<Proxy> proxyList = getProxies(node);

                settings.setNoProxyEnabled(noProxy);
                settings.setMaxConnections(maxConn.intValue());
                settings.setWaitTimeout(wait);
                settings.setMaxConnectionsPerTime(maxConnPerTime,
                        maxConnPerTimeTimeUnit);
                settings.setProxies(proxyList);
            }

        } catch (XPathExpressionException ex) {
            log.error(ex);
        } catch (SAXException ex) {
            log.error(ex);
        } catch (ParserConfigurationException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        }
        return result;
    }

    private static List<Proxy> getProxies(Node node) throws
            XPathExpressionException {
        NodeList proxies = getSubnodes("proxy", node);
        List<Proxy> result = new ArrayList<Proxy>();
        for (int i = 0; i < proxies.getLength(); i++) {
            Node proxy = proxies.item(i);

            String enabledS = (String) xPath.evaluate("enabled", proxy,
                    XPathConstants.STRING);
            Boolean proxyEnabled = Boolean.parseBoolean(enabledS);
            if (proxyEnabled) {
                String proxyServer = getSubnode("server", proxy).getTextContent();
                Double proxyPort = (Double) xPath.evaluate("port", proxy,
                        XPathConstants.NUMBER);
                result.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                        proxyServer, proxyPort.intValue())));
            }
        }
        return result;
    }

    /**
     * open XML document and return his <code>Document</code> representation.
     * @param filName of XML document
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.IOException
     */
    private static Document openDocument(String fileName)
            throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(fileName);
    }

    private static Node getSubnode(String xPathQuery, Node node) throws
            XPathExpressionException {
        return (Node) xPath.evaluate(xPathQuery, node, XPathConstants.NODE);
    }

    private static NodeList getSubnodes(String xPathQuery, Node node) throws
            XPathExpressionException {
        return (NodeList) xPath.evaluate(xPathQuery, node,
                XPathConstants.NODESET);
    }

    /**
     * Select nodes specified by xPath query
     * 
     * @param query xPath query for selecting nodes
     * @param doc XML document
     * @return nodes match xPath query
     */
    private static NodeList getNodeListByXPathQuery(String query, Document doc)
            throws XPathExpressionException {
        return (NodeList) xPath.evaluate(query, doc, XPathConstants.NODESET);
    }
}
