/*
 * Copyright (C) 2011 JiangHongTiao <jjurco.sk_gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.lieskove.jianghongtiao.multithreaddownloader.download;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.File;
import java.net.URL;
import javax.activation.MimeType;

/**
 * Process downloaded documents. Instances should modify downloaded document. 
 * This include running JS actions on the web page, extraction of the text 
 * from PDF file or another files. Post Processor Manager will take original
 * document and try to apply filters. First PostProcess instance get original document, 
 * second one modified from the first instance and so on. Finally modified 
 * document is returned back to download thread. File or page inserted to this 
 * instance will be modified and returned. If you want to modify copy of the 
 * document, you have to insert another instance of the document!
 * Date of create: Oct 5, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.1005
 */
public interface PostProcessor {
    
    /**
     * process {@link HtmlPage}, {@link SgmlPage} or {@link XmlPage}
     * @param page make processing steps on the page 
     * @return modified page
     */
    public Page processPage(Page page);
    
    /**
     * make post processing steps on the file. File is specified by mime-type.
     * @param file file which will be post-processed
     * @return modified document
     */
    public File processFile(File file);
    
    /**
     * If this class can process such a mime-type
     * @param mimeType mime-type of the source document
     * @return <code>true</code>, if instance can process this Mime-Type, otherwise <code>fasle</code>
     */
    public boolean canProcessMimeType(MimeType mimeType);
    
    /**
     * If this class can process such a URL pattern
     * @param url original URL of the resource document
     * @return <code>true</code>, if instance can process this URL, otherwise <code>fasle</code>
     */
    public boolean canProcessUrl(URL url);
    
}
