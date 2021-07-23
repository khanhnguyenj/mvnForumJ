/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/AdAdminWebHandler.java,v 1.12 2009/09/01 11:08:09 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2009/09/01 11:08:09 $
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
package com.mvnsoft.mvnad.admin;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Locale;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumContextListener;
import com.mvnforum.auth.*;
import com.mvnsoft.mvnad.*;
import com.mvnsoft.mvnad.db.*;
import com.mvnsoft.mvnad.delivery.ZoneManager;

public class AdAdminWebHandler {

    private static final Logger log = LoggerFactory.getLogger(AdAdminWebHandler.class);
    
    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    
    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    
    public void prepareShowIndex(GenericRequest request) 
        throws AuthenticationException, DatabaseException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp startTimestamp = MVNForumContextListener.getInstance().getStartTimestamp();
        long upTime = now.getTime() - startTimestamp.getTime();
        
        if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet()) {
            request.setAttribute("PortalInfo", request.getPortalInfo());
        }
    
        request.setAttribute("StartTime", onlineUser.getGMTTimestampFormat(startTimestamp));
        request.setAttribute("NowTime", onlineUser.getGMTTimestampFormat(now));
        request.setAttribute("UpTime", DateUtil.formatDuration(upTime));
    }
    
    public void ensureAdminPermission(GenericRequest request) 
        throws AuthenticationException, DatabaseException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanManageAds();
        
    }

    public void prepareAddBannerOfZone(GenericRequest request)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException, BadInputException {
    
        Locale locale = I18nUtil.getLocaleInRequest(request);
        ensureAdminPermission(request);
        
        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID");
    
        if (zoneID > 0) {
            ZoneBean zoneBean = null;
            try {
                zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
                throw new ObjectNotFoundException(localizedMessage);
            }
            
            int numberOfBanners = DAOFactoryAd.getZoneBannerDAO().getNumberOfBeans_inZone(zoneID);
            if (numberOfBanners >= zoneBean.getZoneMaxBanners()) {
                String localizedMessage = MVNAdResourceBundle.getString(locale, "java.lang.IllegalStateException.maximum_number_banners_reached");
                throw new IllegalStateException(localizedMessage);
            }
        }
        
        Collection bannerBeans = DAOFactoryAd.getBannerDAO().getBeans();
        Collection zoneBeans = DAOFactoryAd.getZoneDAO().getBeans();
    
        request.setAttribute("BannerBeans", bannerBeans);
        request.setAttribute("ZoneBeans", zoneBeans);
        request.setAttribute("ZoneID", new Integer(zoneID));
    }

    public void processAddZoneBanner(GenericRequest request)
        throws BadInputException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);
        
        Locale locale = I18nUtil.getLocaleInRequest(request);
        ensureAdminPermission(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        
        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID");
        
        ZoneBean zoneBean = null;
        try {
            zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbannerofzone.cannot_add");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        int numberOfBanners = DAOFactoryAd.getZoneBannerDAO().getNumberOfBeans_inZone(zoneID);
        if (numberOfBanners >= zoneBean.getZoneMaxBanners()) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.lang.IllegalStateException.maximum_number_banners_reached");
            throw new IllegalStateException(localizedMessage);
        }

        int bannerID                        = GenericParamUtil.getParameterInt(request, "BannerID");
        int relationCellX                   = 0;
        int relationCellY                   = 0;
        int relationCellOption              = 0;
        int relationWeight                  = 0;
        int relationOption                  = 0;
        int relationStatus                  = 0;
        int relationType                    = 0;
        
        Date startDate                      = GenericParamUtil.getParameterDate(request, "RelationPublishStartDate_Day", "RelationPublishStartDate_Month", "RelationPublishStartDate_Year");
        Date endDate                        = GenericParamUtil.getParameterDate(request, "RelationPublishEndDate_Day", "RelationPublishEndDate_Month", "RelationPublishEndDate_Year");
        if (startDate.compareTo(endDate) > 0) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.end_date_after_or_equal_start_date");
            throw new BadInputException(localizedMessage);
        }
        
        Timestamp relationPublishStartDate  = DateUtil.getStartDate(new Timestamp(startDate.getTime()));
        Timestamp relationPublishEndDate    = DateUtil.getEndDate(new Timestamp(endDate.getTime()));
        
        Timestamp relationCreationDate      = now;
        Timestamp relationModifiedDate      = now;

        BannerBean bannerBean = null;
        try {
            bannerBean = DAOFactoryAd.getBannerDAO().getBean(bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbannerofzone.cannot_add");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        try {
            DAOFactoryAd.getZoneBannerDAO().create(zoneID, bannerID, relationCellX, relationCellY, relationCellOption, relationWeight, relationOption, relationStatus, relationType, relationPublishStartDate, relationPublishEndDate, relationCreationDate, relationModifiedDate);
        } catch (DuplicateKeyException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbannerofzone.cannot_add");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.DuplicateKeyException.zone_banner_exist", new Object[] {zoneBean.getZoneName(), bannerBean.getBannerName()});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ForeignKeyNotFoundException fe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbannerofzone.cannot_add");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ForeignKeyNotFoundException(localizedMessage);
        }
        
        request.setAttribute("ZoneID", String.valueOf(zoneID));
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.addbannerofzone",new Object[]{new Integer(bannerID), new Integer(zoneID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"add banner of zone", actionDesc, EventLogService.MEDIUM);
        ZoneManager.clear();
    }

    public void deleteBannerOfZone(GenericRequest request)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException, BadInputException {
    
        ensureAdminPermission(request);
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        SecurityUtil.checkHttpReferer(request);
    
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        int bannerID = GenericParamUtil.getParameterInt(request, "bannerID", 0);
        int zoneID = GenericParamUtil.getParameterInt(request, "zoneID", 0);
        
        try {
            DAOFactoryAd.getZoneBannerDAO().delete(zoneID, bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zonebanner_not_exists", new Object[] {new Integer(bannerID), new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        ZoneManager.clear();
        
        request.setAttribute("ZoneID", String.valueOf(zoneID));
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.deletebannerofzone",new Object[]{new Integer(bannerID), new Integer(zoneID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"delete banner of zone", actionDesc, EventLogService.MEDIUM);
    }
    
    public void prepareEditBannerOfZone(GenericRequest request)
        throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException {
        
        ensureAdminPermission(request);
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        int bannerID = GenericParamUtil.getParameterInt(request, "BannerID", 0);
        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID", 0);
        
        BannerBean bannerBean = null;
        try {
            bannerBean = DAOFactoryAd.getBannerDAO().getBean(bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID), new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        ZoneBean zoneBean = null;
        try {
            zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID), new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        ZoneBannerBean zoneBannerBean = null;
        try {
            zoneBannerBean = DAOFactoryAd.getZoneBannerDAO().getBean(zoneID, bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zonebanner_not_exists", new Object[] {new Integer(bannerID), new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        request.setAttribute("ZoneBannerBean", zoneBannerBean);
        request.setAttribute("BannerBean", bannerBean);
        request.setAttribute("ZoneBean", zoneBean);
        
    }
    
    public void processEditBannerOfZone(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException, AuthenticationException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        Locale locale = I18nUtil.getLocaleInRequest(request);
    
        SecurityUtil.checkHttpPostMethod(request);
        
        int zoneID                          = GenericParamUtil.getParameterInt(request, "ZoneID");
        int bannerID                        = GenericParamUtil.getParameterInt(request, "BannerID");
    
        int relationCellX                   = 0;
        int relationCellY                   = 0;
        int relationCellOption              = 0;
        int relationWeight                  = 0;
        int relationOption                  = 0;
        int relationStatus                  = 0;
        int relationType                    = 0;
        
        Date startDate                      = GenericParamUtil.getParameterDate(request, "RelationPublishStartDate_Day", "RelationPublishStartDate_Month", "RelationPublishStartDate_Year");
        Date endDate                        = GenericParamUtil.getParameterDate(request, "RelationPublishEndDate_Day", "RelationPublishEndDate_Month", "RelationPublishEndDate_Year");
        if (startDate.compareTo(endDate) > 0) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.end_date_after_or_equal_start_date");
            throw new BadInputException(localizedMessage);
        }
        Timestamp relationPublishStartDate    = DateUtil.getStartDate(new Timestamp(startDate.getTime()));
        Timestamp relationPublishEndDate      = DateUtil.getEndDate(new Timestamp(endDate.getTime()));

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp relationModifiedDate      = now;
    
        try {
            DAOFactoryAd.getZoneBannerDAO().update(zoneID, bannerID,
                                        relationCellX, relationCellY, relationCellOption, 
                                        relationWeight, relationOption, relationStatus, 
                                        relationType, relationPublishStartDate, relationPublishEndDate,relationModifiedDate);
        } catch (ObjectNotFoundException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editbannerofzone.cannot_edit");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zonebanner_not_exists", new Object[] {new Integer(bannerID), new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        } catch (ForeignKeyNotFoundException fe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editbannerofzone.cannot_edit");
            if (fe.getMessage().indexOf("'mvnadBanner'") != -1) {
                localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});                
            } else if (fe.getMessage().indexOf("'mvnadZone'") != -1) {
                localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});                
            }
            
            throw new ForeignKeyNotFoundException(localizedMessage);
        }
        
        request.setAttribute("ZoneID", String.valueOf(zoneID));
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.editbannerofzone",new Object[]{new Integer(bannerID), new Integer(zoneID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"edit banner of zone", actionDesc, EventLogService.MEDIUM);
        ZoneManager.clear();
    }

    public void prepareGetCode(GenericRequest request) 
        throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        // TODO check if this permission is correct or not
        permission.ensureIsAuthenticated();

        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID");
        ZoneBean zoneBean = null;
        try {
            zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        } catch (ObjectNotFoundException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        request.setAttribute("ZoneBean", zoneBean);
    }

}
