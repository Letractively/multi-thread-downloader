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
package sk.lieskove.jianghongtiao.multithreaddownloader;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import sk.lieskove.jianghongtiao.multithreaddownloader.document.RemoteDocument;

/**
 * Multi thread downloader is a download manager made for multi thread environment when more treads simultaneously can access to some restricted resources. 
 * Some web servers can have restrictions as how many connections can be opened 
 * in one time, how many documents can be downloaded in some time unit or how 
 * often manager can access to the server. Implementation count with all of these 
 * possible settings and tries to avoid conflicts or blocking from the server.
 * For these reasons all downloads are put to the download queue and prioritized. 
 * Somebody can put a lot of links but it can wait, but somebody can put one or 
 * few links and needs them as soon as possible, because it will slow down application. 
 * Downloads are organized as packages where user can add links to download. For 
 * every server (regular expression for the URL) it is possible to set restriction 
 * settings and proxy servers (more proxy servers in the case when some of them 
 * is not accessible or application already used all possible connections). 
 * Date of create: Sep 16, 2011
 *
 * @author JiangHongTiao <jjurco.sk_gmail.com>
 * @version 2011.0916
 */
public interface MultiThreadDownloadManager {
    
    /**
     * Add link for download. For link will be created extra package and 
     * unique UUID of the package is generated and returned. By other methods 
     * you can check when download is ready or take over finished package. 
     * Without this identifier it is impossible to get download when it will be
     * ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. 
     * When you add links throw this method, priority is by default set to 0. 
     * It means some packages with higher priority can be shifted before these
     * links or some of links can be also shifted after these links. 
     * 
     * @param url URL of the source document.
     * @return  returns UUID of the package. UUID is generated randomly.
     */
    public UUID addLink(URL url);
    
    /**
     * Add link to  the package specified by UUID to download. When no package
     * with put UUID exists, new one with this UUID is created. No exception
     * is thrown in this case. Without this identifier it is impossible to get 
     * download when it will be ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. 
     * When you add links throw this method, priority is by default set to 0. 
     * It means some packages with higher priority can be shifted before these
     * links or some of links can be also shifted after these links. 
     * 
     * @param url URL of the source document.
     * @param uuid identifier of the package 
     * @return returns UUID of the package (in this case the same as the parameter)
     */
    public UUID addLink(URL url, UUID uuid);
    
    /**
     * Add links for download. All links will be put to the same package and 
     * unique UUID of the package is generated and returned. By other methods 
     * you can check when download is ready or take over finished package. 
     * Without this identifier it is impossible to get download when it will be
     * ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application.
     * When you add links throw this method, priority is by default set to 0. 
     * It means some packages with higher priority can be shifted before these
     * links or some of links can be also shifted after these links. 
     * 
     * @param url List of document URL's to download
     * @return  returns UUID of the package. UUID is generated randomly.
     */
    public UUID addLinks(List<URL> url);
    
    /**
     * Add links to  the package specified by UUID to download. When no package
     * with put UUID exists, new one with this UUID is created. No exception
     * is thrown in this case. Without this identifier it is impossible to get 
     * download when it will be ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application.
     * When you add links throw this method, priority is by default set to 0. 
     * It means some packages with higher priority can be shifted before these
     * links or some of links can be also shifted after these links. 
     * 
     * @param url List of document URL's to download
     * @param uuid identifier of the package 
     * @return returns UUID of the package (in this case the same as the parameter)
     */
    public UUID addLinks(List<URL> url, UUID uuid);
    
    /**
     * Add link for download. For link will be created extra package and 
     * unique UUID of the package is generated and returned. By other methods 
     * you can check when download is ready or take over finished package. 
     * Without this identifier it is impossible to get download when it will be
     * ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. Be careful which priority number you use. Be kind with other
     * downloads. Higher number means higher priority. 
     * 
     * @param url URL of the source document.
     * @param priority priority for the URL in the download queue
     * @return  returns UUID of the package. UUID is generated randomly.
     */
    public UUID addLink(URL url, Integer priority);
    
    /**
     * Add link to  the package specified by UUID to download. When no package
     * with put UUID exists, new one with this UUID is created. No exception
     * is thrown in this case. Without this identifier it is impossible to get 
     * download when it will be ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. Be careful which priority number you use. Be kind with other
     * downloads. Higher number means higher priority. 
     * 
     * @param url URL of the source document.
     * @param uuid identifier of the package 
     * @param priority priority for the URL in the download queue
     * @return returns UUID of the package (in this case the same as the parameter)
     */
    public UUID addLink(URL url, UUID uuid, Integer priority);
    
    /**
     * Add links for download. All links will be put to the same package and 
     * unique UUID of the package is generated and returned. By other methods 
     * you can check when download is ready or take over finished package. 
     * Without this identifier it is impossible to get download when it will be
     * ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. Be careful which priority number you use. Be kind with other
     * downloads. Higher number means higher priority. 
     * 
     * @param url List of document URL's to download
     * @param priority priority for all URL's in the download queue
     * @return returns UUID of the package. UUID is generated randomly.
     */
    public UUID addLinks(List<URL> url, Integer priority);
    
    /**
     * Add links to  the package specified by UUID to download. When no package
     * with put UUID exists, new one with this UUID is created. No exception
     * is thrown in this case. Without this identifier it is impossible to get 
     * download when it will be ready. Priority means how soon the document will
     * be downloaded. All links added to packages are split (from all packages)
     * by server. Every server can have restrictions as how many connections
     * can be opened in one time, how many documents can be downloaded in some
     * time unit. Implementation have to count with all of these possible settings
     * and tries to avoid conflicts or blocking from the server. For these 
     * reasons all downloads are put to the download queue and prioritized. 
     * Somebody can put a lot of links but it can wait, but somebody can put one
     * or few links and needs them as soon as possible, because it will slow down 
     * application. Be careful which priority number you use. Be kind with other
     * downloads. Higher number means higher priority. 
     * 
     * @param url List of document URL's to download
     * @param uuid identifier of the package 
     * @param priority priority for all URL's in the download queue
     * @return returns UUID of the package (in this case the same as the parameter)
     */
    public UUID addLinks(List<URL> url, UUID uuid, Integer priority);
    
    /**
     * Check if all documents in package were downloaded. Checks every downloading
     * thread if already finished or not. When all threads already finished,
     * returns true, otherwise returns false.
     * 
     * @param uuid identifier of the package 
     * @return true if all documents in package with given uuid were downloaded,
     * otherwise false.
     */
    public boolean isDownloadReady(UUID uuid);
    
    /**
     * Check if the content from the Internet was already downloaded.
     * If did so, returns all remote files. When some thread is running, it waits
     * till finish and then returns downloaded content. 
     * 
     * @param uuid identifier of the downloaded package
     * @return list of downloaded remote files or <code> null</code> when such 
     * download does not exists
     */
    public RemoteDocument getDownload(UUID uuid) throws IllegalArgumentException;
    
    /**
     * Check if all threads downloading content from the Internet already finished. 
     * If did so, returns all remote files. When some thread is running, it waits
     * till finish and then returns downloaded content. 
     * 
     * @param uuid identifier of the downloaded package
     * @return list of downloaded remote files or <code> null</code> when such 
     * package does not exists
     */
    public List<RemoteDocument> getPackage(UUID uuid) throws IllegalArgumentException;
    
}
