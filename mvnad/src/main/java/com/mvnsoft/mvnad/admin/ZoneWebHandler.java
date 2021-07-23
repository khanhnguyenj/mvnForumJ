/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/ZoneWebHandler.java,v 1.10 2009/09/01 11:08:09 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
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

import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.*;
import com.mvnsoft.mvnad.*;
import com.mvnsoft.mvnad.db.DAOFactoryAd;
import com.mvnsoft.mvnad.db.ZoneBean;
import com.mvnsoft.mvnad.delivery.ZoneManager;

public class ZoneWebHandler {

    private static final Logger log = LoggerFactory.getLogger(ZoneWebHandler.class);
    
    private OnlineUserManager       onlineUserManager   = OnlineUserManager.getInstance();
    
    private static EventLogService  eventLogService     = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    
    public void prepareAddZone(GenericRequest request)
        throws DatabaseException, AuthenticationException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddZone();
    }

    public void processAddZone(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, CreateException, DuplicateKeyException, ForeignKeyNotFoundException {
    
        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddZone();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
    
        String zoneName             = GenericParamUtil.getParameterSafe(request, "ZoneName", true);
        String zoneDesc             = GenericParamUtil.getParameterSafe(request, "ZoneDesc", false);
        String zoneTargetWindow     = GenericParamUtil.getParameterSafe(request, "ZoneTargetWindow", true);
        int zoneCellWidth           = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellWidth");
        int zoneCellHeight          = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellHeight");
        int zoneCellHorizontalCount = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellHorizontalCount");
        int zoneCellVerticalCount   = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellVerticalCount");
        int zoneMaxBanners          = GenericParamUtil.getParameterUnsignedInt(request, "ZoneMaxBanners");
        int zoneDirection           = GenericParamUtil.getParameterUnsignedInt(request, "ZoneDirection");
        int zonerMaxImpression      = Integer.MAX_VALUE;
        int zoneReceivedImpression  = 0;
        int zoneMaxClick            = Integer.MAX_VALUE;
        int zoneReceivedClick       = 0;
        int zoneAutoReloadTime      = GenericParamUtil.getParameterUnsignedInt(request, "ZoneAutoReloadTime");
        int zoneOption              = 0;
        int zoneStatus              = 0;
        int zoneType                = GenericParamUtil.getParameterUnsignedInt(request, "ZoneType");
        Timestamp zoneCreationDate  = now;
        Timestamp zoneModifiedDate  = now;
    
        if (zoneMaxBanners < 1) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.mustbe_greater_or_equal_to", new Object[] {"Zone Max Banners", "1"});
            throw new BadInputException(localizedMessage);
        }
        
        
        int zoneID = 0;
        try {
            zoneID = DAOFactoryAd.getZoneDAO().createZone(onlineUser.getMemberName(), zoneName, zoneDesc,
                                                        zoneTargetWindow, zoneCellWidth, zoneCellHeight,
                                                        zoneCellHorizontalCount, zoneCellVerticalCount,
                                                        zoneMaxBanners, zoneDirection, zonerMaxImpression,
                                                        zoneReceivedImpression, zoneMaxClick, zoneReceivedClick,
                                                        zoneAutoReloadTime, zoneOption, zoneStatus, zoneType,
                                                        zoneCreationDate, zoneModifiedDate);
        } catch (DuplicateKeyException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addzone.cannot_add_zone");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.DuplicateKeyException.zone_member_exist", new Object[] {onlineUser.getMemberName(), zoneName});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ForeignKeyNotFoundException fe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addzone.cannot_add_zone");
            localizedMessage = localizedMessage + " " + MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {onlineUser.getMemberName()});
            throw new ForeignKeyNotFoundException(localizedMessage);
        }
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.addzone", new Object[]{new Integer(zoneID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"add zone", actionDesc, EventLogService.MEDIUM);
        
    }

    public void prepareEditZone(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, AuthenticationException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditZone();

        int zoneID = GenericParamUtil.getParameterInt(request, "zoneID");
        
        ZoneBean zoneBean = null;
        try {
            zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        } catch (ObjectNotFoundException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        request.setAttribute("adZoneBean", zoneBean);
    }

    public void processUpdateZone(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, DuplicateKeyException, AuthenticationException {
    
        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditZone();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        MyUtil.ensureCorrectCurrentPassword(request);
    
        int zoneID                  = GenericParamUtil.getParameterInt(request, "ZoneID");
        ZoneBean zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        
        String zoneName             = GenericParamUtil.getParameterSafe(request, "ZoneName", true);
        String zoneDesc             = GenericParamUtil.getParameterSafe(request, "ZoneDesc", false);
        String zoneTargetWindow     = GenericParamUtil.getParameterSafe(request, "ZoneTargetWindow", true);
        int zoneCellWidth           = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellWidth");
        int zoneCellHeight          = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellHeight");
        int zoneCellHorizontalCount = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellHorizontalCount");
        int zoneCellVerticalCount   = GenericParamUtil.getParameterUnsignedInt(request, "ZoneCellVerticalCount");
        int zoneMaxBanners          = GenericParamUtil.getParameterUnsignedInt(request, "ZoneMaxBanners");
        int zoneDirection           = GenericParamUtil.getParameterUnsignedInt(request, "ZoneDirection");
        int zonerMaxImpression      = zoneBean.getZoneMaxImpression();
        int zoneReceivedImpression  = zoneBean.getZoneReceivedImpression();
        int zoneMaxClick            = zoneBean.getZoneMaxClick();
        int zoneReceivedClick       = zoneBean.getZoneReceivedClick();
        int zoneAutoReloadTime      = GenericParamUtil.getParameterUnsignedInt(request, "ZoneAutoReloadTime");
        int zoneOption              = zoneBean.getZoneOption();
        int zoneStatus              = zoneBean.getZoneStatus();
        int zoneType                = GenericParamUtil.getParameterUnsignedInt(request, "ZoneType");
        Timestamp zoneModifiedDate  = DateUtil.getCurrentGMTTimestamp();
    
        if (zoneMaxBanners < 1) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.mustbe_greater_or_equal_to", new Object[] {"Zone Max Banners", "1"});
            throw new BadInputException(localizedMessage);
        }
        
        try {
            DAOFactoryAd.getZoneDAO().update(zoneID,
                                    onlineUser.getMemberName(), zoneName, zoneDesc,
                                    zoneTargetWindow, zoneCellWidth, zoneCellHeight,
                                    zoneCellHorizontalCount, zoneCellVerticalCount, zoneMaxBanners, zoneDirection,
                                    zonerMaxImpression, zoneReceivedImpression, zoneMaxClick, zoneReceivedClick,
                                    zoneAutoReloadTime, zoneOption, zoneStatus, zoneType, zoneModifiedDate);
        } catch (DuplicateKeyException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editzone.cannot_edit_zone");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.DuplicateKeyException.zone_member_exist", new Object[] {onlineUser.getMemberName(), zoneName});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editzone.cannot_edit_zone");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.editzone", new Object[] {(new Integer(zoneID))});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"edit zone", actionDesc, EventLogService.MEDIUM);
        
        ZoneManager.clear();
    }

    public void prepareDeleteZone(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, AuthenticationException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanDeleteZone();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        int zoneID = GenericParamUtil.getParameterInt(request, "zoneID");
        ZoneBean zoneBean = null;
        try {
            zoneBean = DAOFactoryAd.getZoneDAO().getBean(zoneID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        request.setAttribute("ZoneBean", zoneBean);
    }

    public void processDeleteZone(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanDeleteZone();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        MyUtil.ensureCorrectCurrentPassword(request);
            
        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID", 0);
    
        try {
            DAOFactoryAd.getZoneDAO().delete(zoneID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.zoneid_not_exists", new Object[] {new Integer(zoneID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        DAOFactoryAd.getZoneBannerDAO().deleteInZone(zoneID);
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.deletezone", new Object[] {(new Integer(zoneID))});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"delete zone", actionDesc, EventLogService.MEDIUM);
    }

    public void listZones(GenericRequest request)
        throws DatabaseException, AuthenticationException, BadInputException {
    
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanViewZone();
        
        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) sort = "ZoneID";
        if (order.length()== 0) order = "DESC";

        // we continue
        int rowsToReturn = onlineUser.getPostsPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException e) {
            // do nothing
        }

        int totalZones = DAOFactoryAd.getZoneDAO().getNumberOfBeans();
        if (offset > totalZones) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
        }
    
        Collection zoneBeans = DAOFactoryAd.getZoneDAO().getBeans_withSortSupport_limit(offset, rowsToReturn, sort, order);
        for (Iterator iter = zoneBeans.iterator(); iter.hasNext(); ) {
            ZoneBean zoneBean = (ZoneBean) iter.next();
            int bannerCount = DAOFactoryAd.getZoneBannerDAO().getNumberOfBeans_inZone(zoneBean.getZoneID());
            zoneBean.setBannerCount(bannerCount);
        }
        request.setAttribute("ZoneBeans", zoneBeans);
        request.setAttribute("TotalZones", new Integer(totalZones));
    }

}
