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
import java.io.File;
import java.net.URL;
import javax.activation.MimeType;
import org.apache.log4j.Logger;

/**
 * Date of create: Oct 6, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.1006
 */
public class DefaultPostProcessorManager implements PostProcessorManager{
    
    private static Logger log = Logger.getLogger(DefaultPostProcessorManager.class);
    
    private static PostProcessor[] pps = {};
    
    public Page launchPagePostProcessing(URL url, MimeType mimeType, 
            Page page){
        Page result = page;
        for (int i = 0; i < pps.length; i++) {
            PostProcessor pp = pps[i];
            log.debug("Post-processing: "+pp.getClass().getName()+
                    " for mime-type: "+mimeType.toString()+" and url: "+url);
            if(pp.canProcessUrl(url) && pp.canProcessMimeType(mimeType)){
                result = pp.processPage(result);
            }
        }
        return result;
    }
    
    public File launchFilePostProcessing(URL url, MimeType mimeType, 
            File file){
        File result = file;
        for (int i = 0; i < pps.length; i++) {
            PostProcessor pp = pps[i];
            if(pp.canProcessUrl(url) && pp.canProcessMimeType(mimeType)){
                result = pp.processFile(file);
            }
        }
        return result;
    }
}
