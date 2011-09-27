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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Date of create: May 15, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0515
 */
public class RemoteFileEncoding {
    private CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();


    /**
     * try to get file encoding from server
     * @param connection connection to server with remote file
     * @return relevant encoding if server returns encoding type, otherwise null
     */
    public Charset getRemoteEncoding(HttpURLConnection connection) {
        Charset result = null;
        String encoding = connection.getContentEncoding();
        if ((encoding != null) && (!encoding.equals("")) && (Charset.isSupported(encoding))) {
            result = Charset.forName(encoding);
        }
        return result;
    }

    public static Charset makeEncoding(String encoding){
        Charset result = null;
        if ((encoding != null) && (!encoding.equals("")) && (Charset.isSupported(encoding))) {
            result = Charset.forName(encoding);
        }
        return result;
    }

    /**
     * try to parse mime type and extract charset. Charset of file is usually
     * returned by server as part of mime type.
     * @param mimeType mime type to parse
     * @return if extraction is successful, then relevant charset, otherwise null
     */
    public Charset tryParseMimeType(String mimeType) {
        //test for removing: " ;
        //known: repeating encoding
        //occuring encodings:
        //utf-8, iso-8859-1, us-ascii, windows-1252,latin-1,windows-1255,windows-1256,
        if(mimeType == null){
            throw new NullPointerException("Mime-type was null!");
        }
        Charset result = null;
        mimeType = mimeType.toLowerCase().replaceAll("\\\"", "");
        int charsetPos = mimeType.indexOf("charset=");
        if (charsetPos >= 0) {
            String charset = mimeType.substring(charsetPos + 8);
            //take the longest string which can be recognized as encoding
            if ((charset != null) && (!charset.equals(""))) {
                int cut = charset.length();

                while ((cut > 0) && (result == null)) {
                    String subcharset = charset.substring(0, cut);
                    if (Charset.isSupported(subcharset)) {
                        result = Charset.forName(subcharset);
                    }
                    cut--;
                }
            }
        }
        return result;
    }

    /**
     * try to recognize encoding from file
     * @param url file url
     * @return detected charset
     * @throws IOException throws when there is an exception thrown while reading file
     */
    public Charset fileEncoding(URL url) throws IOException {
        Charset result = null;
        // The first instance delegated to tries to detect the meta charset attribut
        //in html pages.
        detector.add(new ParsingDetector(false)); // be verbose about parsing.
        // Add the implementations of info.monitorenter.cpdetector.io.ICodepageDetector:
        // This one is quick if we deal with unicode codepages:
        detector.add(new ByteOrderMarkDetector());
        // This one does the tricks of exclusion and frequency detection, if first implementation is
        // unsuccessful:
        detector.add(UnicodeDetector.getInstance());
        detector.add(JChardetFacade.getInstance()); // Another singleton.
        detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.
        result = detector.detectCodepage(url);
        return result;
    }

    /**
     * compare charsets and returns common charset
     * @param charsets list of charsets to check
     * @return not-null encoding if is present, otherwise null
     * @throws TooManyEncodings throws when more then one encoding type is present
     */
    public static Charset selectCharset(Charset ... charsets) throws TooManyEncodings{
        Map<Charset, Integer> counter = new HashMap<Charset, Integer>();
        for (int i = 0; i < charsets.length; i++) {
            Charset charset = charsets[i];
            if(charset != null){
                //if there is such encoding, just increse number of occurences
                if(counter.containsKey(charset)){
                    Integer num = counter.get(charset);
                    num++;
                    counter.put(charset, num);
                } else {
                    //when counter does not contains charset, put new one
                    counter.put(charset, 0);
                }
            }
        }
        if(counter.isEmpty()){
            return null;
        }
        if(counter.size() == 1){
            return (Charset) counter.keySet().toArray()[0];
        } else {
            //find maximal occurences or throws an exception?
            throw new TooManyEncodings("Too many encodings (" + counter.size() + ").");
        }
    }
}
