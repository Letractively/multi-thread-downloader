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
package sk.lieskove.jianghongtiao.multithreaddownloader.persistence;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import sk.lieskove.jianghongtiao.paris.utils.PropertiesUtils;

/**
 * persist entities to DB by statistics type
 * @author xjuraj
 */
public class Persist {

    private static Persist pp = null;
    private EntityManagerFactory emFactory =
                Persistence.createEntityManagerFactory("MultiThreadDownloaderDatastorePU");
    private final EntityManager em = emFactory.createEntityManager();
    private PropertiesUtils pu = new PropertiesUtils(Persist.class);
    private static Logger log = Logger.getLogger(Persist.class.getName());

    public synchronized static Persist getSingleton(){
        if(pp == null){
            pp = new Persist();
        }
        return pp;
    }

    public Persist() {
        
    }

    /**
     * persist time statistics of computation if it is enabled in configuration file
     * @param entity entity to store
     */
    public void timeStatistic(Object entity){
        if(Boolean.valueOf(pu.getProperty("stat-time"))){
            persist(entity);
        }
    }

    /**
     * persist remote objects if it is enabled in configuration file
     * @param entity entity to store
     */
    public void remoteObjects(Object entity){
        if(Boolean.valueOf(pu.getProperty("stat-remote"))){
            persist(entity);
        }
    }

    /**
     * persist search results if it is enabled in configuration file
     * @param entity entity to store
     */
    public void searchResults(Object entity){
        if(Boolean.valueOf(pu.getProperty("stat-search"))){
            persist(entity);
        }
    }

    /**
     * persist any entity to database
     * @param entity entity to persist
     */
    public void persist(Object entity) {
        EntityManager emL = getEntityManager();
        synchronized(emL){
            
            EntityTransaction t = emL.getTransaction();
            t.begin();
            emL.persist(entity);
            
//            try{
//                
//            } catch(EntityExistsException e){
//                if(entity instanceof AssignedProxyStatistics){
//                    AssignedProxyStatistics aps = (AssignedProxyStatistics)entity;
//                    log.warn("*** Object already exists: "+aps.getId());
//                    t.setRollbackOnly();
//                }
//                
//            }
            t.commit();
        }
    }
    
    public EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }

}
