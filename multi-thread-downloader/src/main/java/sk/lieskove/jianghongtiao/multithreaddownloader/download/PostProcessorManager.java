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

/**
 * Date of create: Oct 6, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.1006
 */
public interface PostProcessorManager {
    
    /**
     * apply all processing operations on the Page
     * @param url original URL of the resource
     * @param mimeType mime type of the document
     * @param page page to modify
     * @return return page after all modifications and applying all post-processing steps
     */
    public Page launchPagePostProcessing(URL url, MimeType mimeType, 
            Page page);
    
    /**
     * apply all processing operations on the Page
     * @param url original URL of the resource
     * @param mimeType mime type of the document
     * @param file apply all modification to file
     * @return return modified file after applying all post-processing steps
     */
    public File launchFilePostProcessing(URL url, MimeType mimeType, 
            File file);
}
