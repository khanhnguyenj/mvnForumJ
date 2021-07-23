/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/ZoneManager.java,v 1.10 2009/12/04 07:08:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/12/04 07:08:54 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.delivery;

import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnsoft.mvnad.AdModuleUtils;
import com.mvnsoft.mvnad.db.*;
import com.mvnsoft.mvnad.service.MvnAdServiceFactory;

public final class ZoneManager {
    
    private static final Logger log = LoggerFactory.getLogger(ZoneManager.class);
    
    public static final String OBJECT_TYPE_CHANNEL  = "channel";
    public static final String OBJECT_TYPE_FORUM    = "forum";
    
    public static final int MAX_RELOAD_MINUTES = 5;
    
    // static section
    private static Map zoneManagersMap = new TreeMap();
    
    // instance section
    int viewCount = 0;
    List allBanners;
    ZoneBean zoneBean;
    long lastTimeCreation;
    
    private ZoneManager() {
        allBanners = new ArrayList();

        lastTimeCreation = System.currentTimeMillis();
    }
    
    private ZoneManager(Timestamp now, int zoneID) 
        throws DatabaseException, ObjectNotFoundException {
        
        allBanners = new ArrayList(DAOFactoryAd.getZoneBannerDAO().getValidBannersOfZone(now, zoneID));
        
        zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);

        lastTimeCreation = System.currentTimeMillis();
    }
    
    private ZoneManager(Timestamp now, int zoneID, String objectType, int objectID) 
        throws DatabaseException, ObjectNotFoundException {
        
        Collection bannersInZone = DAOFactoryAd.getZoneBannerDAO().getValidBannersOfZone(now, zoneID);
        Collection bannersInZoneInChannel = MvnAdServiceFactory.getMvnAdService().getExternalBannerService().getValidBannersInZone(now, zoneID, objectType, objectID);

        allBanners = new ArrayList(bannersInZone);
        allBanners.addAll(bannersInZoneInChannel);
        
        zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);

        lastTimeCreation = System.currentTimeMillis();
    }
    
    public static synchronized ZoneManager getInstance(Timestamp now, int zoneID) { 
        
        long currentTime = System.currentTimeMillis();
        
        String key = "zone_" + zoneID;
        ZoneManager manager = null;
        if (zoneManagersMap.containsKey(key) == false) {
            try {
                manager = new ZoneManager(now, zoneID);
            } catch (Throwable throwable) {
                log.error("Error", throwable);
            }
            
            if (manager == null) {
                manager = new ZoneManager();
            }
            zoneManagersMap.put(key, manager);
        }
        
        manager = (ZoneManager) zoneManagersMap.get(key);
        
        if ( (currentTime - manager.lastTimeCreation) > (DateUtil.MINUTE * MAX_RELOAD_MINUTES) ) {
            try {
                manager = new ZoneManager(now, zoneID);
            } catch (Throwable throwable) {
                log.error("Error", throwable);
            }
            
            if (manager == null) {
                manager = new ZoneManager();
            }
            zoneManagersMap.put(key, manager);
        }
        
        return manager;
    }
    
    public static synchronized ZoneManager getInstance(Timestamp now, int zoneID, String objectType, int objectID) {
        
        long currentTime = System.currentTimeMillis();
        
        String key = "zone_" + zoneID + "_" + objectType + "_" + objectID;
        ZoneManager manager = null;
        if (zoneManagersMap.containsKey(key) == false) {
            try {
                manager = new ZoneManager(now, zoneID, objectType, objectID);
            } catch (Throwable throwable) {
                log.error("Error", throwable);
            }
            
            if (manager == null) {
                manager = new ZoneManager();
            }
            zoneManagersMap.put(key, manager);
        }
        
        manager = (ZoneManager) zoneManagersMap.get(key);

        if ( (currentTime - manager.lastTimeCreation) > (DateUtil.MINUTE * MAX_RELOAD_MINUTES) ) {
            try {
                manager = new ZoneManager(now, zoneID, objectType, objectID);
            } catch (Throwable throwable) {
                log.error("Error", throwable);
            }
            
            if (manager == null) {
                manager = new ZoneManager();
            }
            zoneManagersMap.put(key, manager);
        }
        
        return manager;
    }
    
    public static synchronized void clear() {
        zoneManagersMap.clear();
    }
    
    public ZoneBean getZoneBean() {
        return zoneBean;
    }
    
    private BannerBean getNextBanner() {
        
        if (allBanners.size() == 0) {
            return null;
        }
        
        viewCount++;
        int currentIndex = viewCount % allBanners.size();
        
        return (BannerBean) allBanners.get(currentIndex);
    }
    
    public String getBannerInZone() 
        throws DatabaseException, ObjectNotFoundException {
        
        Map map = new HashMap();
        return getBannerInZone(map);
    }

    public String getBannerInZone(Map map) 
        throws DatabaseException, ObjectNotFoundException {
        
        BannerBean currentBanner = getNextBanner();
        if (currentBanner == null) {
            return "";
        }
    
        if (currentBanner.getBannerType() == BannerBean.BANNER_TYPE_HTML) {
            return currentBanner.getBannerHtmlCode();
        }
        
        MvnAdServiceFactory.getMvnAdService().getMvnAdTrackerService().trackView(zoneBean.getZoneID(), currentBanner.getBannerID());
        
        String imageURL = AdModuleUtils.getBannerWebPath(currentBanner);
        
        StringBuffer result = new StringBuffer(1024);
        result.append("<a href='");
        result.append(AdGenerator.SERVER_URL).append("/clickad?bannerID=").append(currentBanner.getBannerID()).append("&zoneID=").append(zoneBean.getZoneID());
        AdModuleUtils.appendParams(result, map);
        result.append("' target='").append(zoneBean.getZoneTargetWindow()).append("'>");
        result.append(StringUtil.displayMediaContent(imageURL, zoneBean.getZoneCellWidth(), zoneBean.getZoneCellHeight()));
        result.append("</a>");
            
        return result.toString();
    }

}
