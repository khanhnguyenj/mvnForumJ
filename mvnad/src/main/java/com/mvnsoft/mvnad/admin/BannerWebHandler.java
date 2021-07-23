/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/BannerWebHandler.java,v 1.24 2009/09/01 11:08:09 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.24 $
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

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import javax.activation.MimetypesFileTypeMap;

import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnsoft.mvnad.*;
import com.mvnsoft.mvnad.db.*;
import com.mvnsoft.mvnad.delivery.ZoneManager;

public class BannerWebHandler {

    private static final Logger log = LoggerFactory.getLogger(BannerWebHandler.class);
    
    private OnlineUserManager       onlineUserManager       = OnlineUserManager.getInstance();
    
    private static EventLogService  eventLogService         = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    private URLResolverService      urlResolverService      = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
    private FileUploadParserService fileUploadParserService = MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();
    
    public void prepareAddBanner(GenericRequest request) 
        throws AuthenticationException, DatabaseException, IOException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddBanner();
        
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        String diskFolderOfMember = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName()));
        
        File diskFolderOfMemberFile = new File(diskFolderOfMember);
        if (diskFolderOfMemberFile.exists() == false) {
            log.info("About to create folder for upload banner: " + diskFolderOfMember);
            FileUtil.createDirs(diskFolderOfMember, true);
        }
        if (diskFolderOfMemberFile.isDirectory() == false) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.io.IOException.data_folder_user_not_directory", new Object[] {onlineUser.getMemberName()});
            throw new IOException(localizedMessage);
        }
        
        log.debug("About to list files in folder: " + diskFolderOfMember);
        File[] bannerFiles = diskFolderOfMemberFile.listFiles();
    
        request.setAttribute("BannerFiles", bannerFiles);
    }

    public void processAddBanner(GenericRequest request)
        throws BadInputException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, AuthenticationException {
        
        SecurityUtil.checkHttpPostMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAddBanner();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        
        String memberName           = GenericParamUtil.getParameterSafe(request, "MemberName", true);
        String bannerName           = GenericParamUtil.getParameterSafe(request, "BannerName", true);
        String bannerDesc           = GenericParamUtil.getParameterSafe(request, "BannerDesc", false);
        String bannerAltText        = "";
        String bannerMimeType       = null;
        String bannerPreText        = "";
        String bannerAfterText      = "";
        String bannerTargetURL      = "";
        
        String bannerImageURL       = "";
        String bannerHtmlCode       = "";
        int bannerIsHtml            = 0;
       
        int bannerType              = GenericParamUtil.getParameterInt(request, "BannerType");
        
        if (bannerType == BannerBean.BANNER_TYPE_HTML) {
            bannerHtmlCode = GenericParamUtil.getParameter(request, "BannerHTMLCode", true);
            bannerIsHtml = 1;
            bannerMimeType = "text/html";
        } else if (bannerType == BannerBean.BANNER_TYPE_IMAGE || bannerType == BannerBean.BANNER_TYPE_FLASH || bannerType == BannerBean.BANNER_TYPE_MOVIE) {
            bannerImageURL        = GenericParamUtil.getParameterSafe(request, "BannerImageURL", true);
            bannerTargetURL       = GenericParamUtil.getParameterUrl(request, "BannerTargetURL");
            if ((bannerImageURL.startsWith("http://") || bannerImageURL.startsWith("https://") || bannerImageURL.startsWith("ftp://"))) {
                // external image
                try {
                    URL url = new URL(bannerImageURL);
                    URLConnection uc = url.openConnection();
                    bannerMimeType = uc.getContentType();
                } catch (Exception e) {
                    bannerMimeType = "unknown/unknown";
                }
            } else {
                // now, it must be a internal image
                String imageFile = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName()) + "/" + bannerImageURL);
                // check to make sure that image is existed
                File file = new File(imageFile);
                if (file.exists() == false) {
                    String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.image_file_not_found", new Object[] {bannerImageURL});
                    throw new BadInputException(localizedMessage);
                }
                bannerMimeType = new MimetypesFileTypeMap().getContentType(file);
                bannerImageURL = onlineUser.getMemberName().toLowerCase() + "/" + bannerImageURL;
            }
        } else {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.banner_type_not_found", new Object[] {new Integer(bannerType)});
            throw new BadInputException(localizedMessage);
        }
        
        if (bannerMimeType == null) {
            bannerMimeType = "unknown/unknown";
        }
        
        int bannerWidth             = GenericParamUtil.getParameterUnsignedInt(request, "BannerWidth");
        int bannerHeight            = GenericParamUtil.getParameterUnsignedInt(request, "BannerHeight");
        int bannerWeight            = 1;
        int bannerMaxImpression     = Integer.MAX_VALUE;
        int bannerReceivedImpression= 0;
        int bannerMaxClick          = Integer.MAX_VALUE;
        int bannerReceivedClick     = 0;
        int bannerZonePositionX     = 0;
        int bannerZonePositionY     = 0;
        
        Date startDate              = GenericParamUtil.getParameterDate(request, "BannerStartDate_Day", "BannerStartDate_Month", "BannerStartDate_Year");
        Date endDate                = GenericParamUtil.getParameterDate(request, "BannerEndDate_Day", "BannerEndDate_Month", "BannerEndDate_Year");
        if (startDate.compareTo(endDate) > 0) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.end_date_after_or_equal_start_date");
            throw new BadInputException(localizedMessage);
        }
        Timestamp bannerStartDate   = new Timestamp(startDate.getTime());
        Timestamp bannerEndDate     = new Timestamp(endDate.getTime());
        
        int bannerCanTrackClicks    = 0;
        int bannerOption            = 0;
        int bannerStatus            = 0;
        
        Timestamp bannerCreationDate= now;
        Timestamp bannerModifiedDate= now;
    
        int bannerID = 0;
        try {
            bannerID = DAOFactoryAd.getBannerDAO().createBanner(memberName, bannerName, bannerDesc, 
                                        bannerAltText, bannerMimeType, bannerPreText, 
                                        bannerAfterText, bannerTargetURL, bannerImageURL, 
                                        bannerWidth, bannerHeight, bannerWeight, 
                                        bannerMaxImpression, bannerReceivedImpression, bannerMaxClick, 
                                        bannerReceivedClick, bannerZonePositionX, bannerZonePositionY, 
                                        bannerStartDate, bannerEndDate, bannerIsHtml, 
                                        bannerHtmlCode, bannerCanTrackClicks, bannerOption, 
                                        bannerStatus, bannerType, bannerCreationDate, 
                                        bannerModifiedDate);
        } catch (DuplicateKeyException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbanner.cannot_add_banner");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.DuplicateKeyException.banner_member_exist", new Object[] {memberName, bannerName});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ForeignKeyNotFoundException fe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.addbanner.cannot_add_banner");
            localizedMessage = localizedMessage + " " + MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {memberName});
            throw new ForeignKeyNotFoundException(localizedMessage);
        }
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.addbanner", new Object[]{new Integer(bannerID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"add banner", actionDesc, EventLogService.MEDIUM);
        
    }

    public void prepareEditBanner(GenericRequest request)
        throws DatabaseException, AuthenticationException,  BadInputException, IOException, ObjectNotFoundException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditBanner();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        int bannerID = GenericParamUtil.getParameterInt(request, "id", 1);
        
        BannerBean adBannerBean = null;
        try {
            adBannerBean = DAOFactoryAd.getBannerDAO().getBean(bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        request.setAttribute("bannerBean", adBannerBean);
        
        String diskFolderOfMember = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName()));
        
        File diskFolderOfMemberFile = new File(diskFolderOfMember);
        if (diskFolderOfMemberFile.exists() == false) {
            log.info("About to create folder for upload banner: " + diskFolderOfMember);
            FileUtil.createDirs(diskFolderOfMember, true);
        }
        if (diskFolderOfMemberFile.isDirectory() == false) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.io.IOException.data_folder_user_not_directory", new Object[] {onlineUser.getMemberName()});
            throw new IOException(localizedMessage);
        }
        
        log.debug("About to list files in folder: " + diskFolderOfMember);
        File[] bannerFiles = diskFolderOfMemberFile.listFiles();
    
        request.setAttribute("BannerFiles", bannerFiles);
        
    }

    public void processUpdateBanner(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException, AuthenticationException, ForeignKeyNotFoundException, DuplicateKeyException {
    
        SecurityUtil.checkHttpPostMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanEditBanner();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        MyUtil.ensureCorrectCurrentPassword(request);
        
        int bannerID                 = GenericParamUtil.getParameterInt(request, "BannerID");
    
        String memberName            = GenericParamUtil.getParameterSafe(request, "MemberName", true);
        String bannerName            = GenericParamUtil.getParameterSafe(request, "BannerName", true);
        String bannerDesc            = GenericParamUtil.getParameterSafe(request, "BannerDesc", false);
        String bannerAltText         = "";
        String bannerMimeType        = null;
        String bannerPreText         = "";
        String bannerAfterText       = "";
        String bannerTargetURL       = "";
        
        String bannerImageURL        = "";
        String bannerHtmlCode        = "";
        int bannerIsHtml             = 0;
        
        int bannerType               = GenericParamUtil.getParameterInt(request, "BannerType");
        
        if (bannerType == BannerBean.BANNER_TYPE_HTML) {
            bannerHtmlCode = GenericParamUtil.getParameter(request, "BannerHTMLCode", true);
            bannerIsHtml = BannerBean.BANNER_IS_HTML;
            bannerMimeType = "text/html";
        } else if (bannerType == BannerBean.BANNER_TYPE_IMAGE || bannerType == BannerBean.BANNER_TYPE_FLASH || bannerType == BannerBean.BANNER_TYPE_MOVIE) {
            bannerImageURL        = GenericParamUtil.getParameterSafe(request, "BannerImageURL", true);
            bannerTargetURL       = GenericParamUtil.getParameterUrl(request, "BannerTargetURL");
            if ((bannerImageURL.startsWith("http://") || bannerImageURL.startsWith("https://") || bannerImageURL.startsWith("ftp://"))) {
                // external image
                try {
                    URL url = new URL(bannerImageURL);
                    URLConnection uc = url.openConnection();
                    bannerMimeType = uc.getContentType();
                } catch (Exception e) {
                    bannerMimeType = "unknown/unknown";
                }
            } else {
                // now, it must be a internal image
                String imageFile = request.getRealPath(MVNAdConfig.getWebUploadFolder() + "/" + bannerImageURL);
                // check to make sure that image is existed
                File file = new File(imageFile);
                if (file.exists() == false) {
                    String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.image_file_not_found", new Object[] {bannerImageURL});
                    throw new BadInputException(localizedMessage);
                }
                bannerMimeType = new MimetypesFileTypeMap().getContentType(file);
            }
        } else {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.banner_type_not_found", new Object[] {new Integer(bannerType)});
            throw new BadInputException(localizedMessage);
        }
        
        if (bannerMimeType == null) {
            bannerMimeType = "unknown/unknown";
        }
        
        BannerBean bannerBean = DAOFactoryAd.getBannerDAO().getBean(bannerID);

        int bannerWidth              = GenericParamUtil.getParameterUnsignedInt(request, "BannerWidth");
        int bannerHeight             = GenericParamUtil.getParameterUnsignedInt(request, "BannerHeight");
        int bannerWeight             = bannerBean.getBannerWeight();
        int bannerMaxImpression      = bannerBean.getBannerMaxImpression();
        int bannerReceivedImpression = bannerBean.getBannerReceivedImpression();
        int bannerMaxClick           = bannerBean.getBannerMaxClick();
        int bannerReceivedClick      = bannerBean.getBannerReceivedClick();
        int bannerZonePositionX      = bannerBean.getBannerZonePositionX();
        int bannerZonePositionY      = bannerBean.getBannerZonePositionY();                
        
        Date startDate               = GenericParamUtil.getParameterDate(request, "BannerStartDate_Day", "BannerStartDate_Month", "BannerStartDate_Year");
        Date endDate                 = GenericParamUtil.getParameterDate(request, "BannerEndDate_Day", "BannerEndDate_Month", "BannerEndDate_Year");
        if (startDate.compareTo(endDate) > 0) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.end_date_after_or_equal_start_date");
            throw new BadInputException(localizedMessage);
        }
        Timestamp bannerStartDate    = new Timestamp(startDate.getTime());
        Timestamp bannerEndDate      = new Timestamp(endDate.getTime());
    
        int bannerCanTrackClicks     = 0;
        int bannerOption             = 0;
        int bannerStatus             = 0;
        Timestamp bannerModifiedDate = DateUtil.getCurrentGMTTimestamp();
    
        try {
            DAOFactoryAd.getBannerDAO().update(bannerID, // primary key
                                        memberName, bannerName, bannerDesc, 
                                        bannerAltText, bannerMimeType, bannerPreText, 
                                        bannerAfterText, bannerTargetURL, bannerImageURL, 
                                        bannerWidth, bannerHeight, bannerWeight, 
                                        bannerMaxImpression, bannerReceivedImpression, bannerMaxClick, 
                                        bannerReceivedClick, bannerZonePositionX, bannerZonePositionY, 
                                        bannerStartDate, bannerEndDate, bannerIsHtml, 
                                        bannerHtmlCode, bannerCanTrackClicks, bannerOption, 
                                        bannerStatus, bannerType, bannerModifiedDate);
            
            BannerCache.getInstance().clear();
        } catch (DuplicateKeyException de) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editbanner.cannot_edit_banner");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.DuplicateKeyException.banner_member_exist", new Object[] {memberName, bannerName});
            throw new DuplicateKeyException(localizedMessage);
        } catch (ForeignKeyNotFoundException fe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editbanner.cannot_edit_banner");
            localizedMessage = localizedMessage + " " + MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {memberName});
            throw new ForeignKeyNotFoundException(localizedMessage);
        } catch (ObjectNotFoundException oe) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvnad.admin.editbanner.cannot_edit_banner");
            localizedMessage = localizedMessage + " " + MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.editbanner",new Object[]{new Integer(bannerID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"edit banner", actionDesc, EventLogService.MEDIUM);
        
        ZoneManager.clear();
    }

    public void processUploadMedia(GenericRequest request, GenericResponse response) 
        throws BadInputException, DatabaseException, IOException, AuthenticationException, InterceptorException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanUploadMedia();
        
        SecurityUtil.checkHttpPostMethod(request, true, true);
        
        Locale locale = I18nUtil.getLocaleInRequest(request);
    
        MyUtil.saveVNTyperMode(request, response);
    
        String tempDir = MVNForumConfig.getTempDir();
        log.debug("Upload Media : process upload with temp dir = " + tempDir);
    
        final int UNLIMITED = -1;
        int sizeMax = UNLIMITED;
        int sizeThreshold = 100000;
    
        List fileItems;
        try {
            fileItems = fileUploadParserService.parseRequest(request, sizeMax, sizeThreshold, tempDir, "UTF-8");
        } catch (FileUploadException ex) {
            log.error("Cannot upload", ex);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_upload", new Object[] {ex.getMessage()});
            throw new IOException(localizedMessage);
        }
    
        // values that must get from the form
        String attachFilename       = null;
        int attachFileSize          = 0;
        String attachMimeType       = null;
        String attachDesc           = null;
        ArrayList attachFileItems   = new ArrayList();
        boolean attachMore          = false;
        String receivedToken        = null;
        
        for (int i = 0; i < fileItems.size(); i++ ) {
            
            FileItem currentFileItem = (FileItem)fileItems.get(i);
            String fieldName = currentFileItem.getFieldName();
            if (fieldName.equals("AttachMore")) {
                String content = currentFileItem.getString("utf-8");
                attachMore = (content.length() > 0);
                log.debug("attachMore = " + attachMore);
            } else if (fieldName.equals("AttachDesc")) {
                String content = currentFileItem.getString("utf-8");
                attachDesc = DisableHtmlTagFilter.filter(content);
                log.debug("attachDesc = " + attachDesc);
                attachDesc = InterceptorService.getInstance().validateContent(attachDesc);
    
            } else if (fieldName.equals("vnselector")) {
                //ignore
            } else if (fieldName.equals("frompage")) {
                request.setAttribute("frompage", currentFileItem.getString("utf-8"));
            } else if (fieldName.equals(urlResolverService.getActionParam())) {
                //ignore ACTION_PARAM if exists
            } else if (fieldName.startsWith("AttachFilename")) { // fields has prefix AttachFileName
            //else if (fieldName.equals("AttachFilename")) {
                if (currentFileItem.isFormField()) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_uploaded_attach_file_with_form_field");
                    throw new AssertionError(localizedMessage);
                    //throw new AssertionError("Cannot process uploaded attach file with a form field.");
                }
                attachMimeType = currentFileItem.getContentType();
                attachMimeType = DisableHtmlTagFilter.filter(attachMimeType);
                attachFileSize = (int)currentFileItem.getSize();
                if (attachFileSize == 0) {
                    continue;
                    // String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process_upload_with_file_size_is_zero");
                    // throw new BadInputException(localizedMessage);
                    //throw new BadInputException("Cannot process an attach file with size = 0. Please check the file size or check if your file is missing.");
                }
                
                // now store into attachFileItem
                attachFileItems.add(currentFileItem);
            } else if (fieldName.startsWith(MVNCoreGlobal.MVNCORE_SECURITY_TOKEN)) {
                receivedToken = currentFileItem.getString("utf-8");
                log.debug("receivedToken = " + receivedToken);
            } else {
                // maybe, we don't care about the redundant fields.
                // Should we uncomment the exception statement ?
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_process_field_name", new Object[] {fieldName});
                throw new AssertionError(localizedMessage);
                //throw new AssertionError("Cannot process field name = " + fieldName);
            }
        }
        
        SecurityUtil.checkSecurityTokenMethod(request, receivedToken);
        
        for (Iterator iter = attachFileItems.iterator(); iter.hasNext();) {
            FileItem currentFileItem = (FileItem) iter.next();
            
            String fullFilePath = currentFileItem.getName();
            attachFilename = FileUtil.getFileName(fullFilePath);
            attachFilename = DisableHtmlTagFilter.filter(attachFilename);
            
            FileUtil.checkMediaFileExtension(locale, attachFilename);
    
            attachFileSize = (int)currentFileItem.getSize();
            attachMimeType = currentFileItem.getContentType();
            attachMimeType = DisableHtmlTagFilter.filter(attachMimeType);

            String filename = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName())) + File.separatorChar + attachFilename;
            
            File file = new File(filename);
            if (file.exists()) {
                String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.BadInputException.media_file_exist", new Object[] {attachFilename, onlineUser.getMemberName()});
                throw new BadInputException(localizedMessage);
            }
            
            try {
                InputStream inputStream = currentFileItem.getInputStream();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(filename);
                    IOUtils.copy(inputStream, fos);
                } catch (IOException e) {
                    log.error("Disk Error", e);
                    throw e;
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.debug("Cannot close input file", e);
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            log.debug("Cannot close output file", e);
                        }
                    }
                }
            } catch (Exception ex) { 
                log.error("Cannot save the attachment file", ex);
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_save_attach_file");
                throw new IOException(localizedMessage);
                //throw new IOException("Cannot save the attachment file to the file system.");
            }
        }
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.uploadmedia",new Object[]{attachFilename});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"upload media", actionDesc, EventLogService.MEDIUM);
    
    }

    public void prepareDeleteBanner(GenericRequest request) 
        throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException {
    
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanDeleteBanner();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        int bannerID = GenericParamUtil.getParameterInt(request, "id", 0);
        
        BannerBean adBannerBean = null;
        try {
            adBannerBean = DAOFactoryAd.getBannerDAO().getBean(bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        request.setAttribute("BannerBean", adBannerBean);
    }

    public void processDeleteBanner(GenericRequest request) 
        throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException {
    
        SecurityUtil.checkHttpPostMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanDeleteBanner();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        MyUtil.ensureCorrectCurrentPassword(request);
            
        int bannerID = GenericParamUtil.getParameterInt(request, "BannerID", 0);

        try {
            DAOFactoryAd.getBannerDAO().delete(bannerID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.bannerid_not_exists", new Object[] {new Integer(bannerID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        
        DAOFactoryAd.getZoneBannerDAO().deleteInBanner(bannerID);
        BannerCache.getInstance().clear();
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.deletebanner",new Object[]{new Integer(bannerID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN,"delete banner", actionDesc, EventLogService.MEDIUM);
        
    }

    public void prepareListBanners(GenericRequest request) 
        throws AuthenticationException, DatabaseException, ObjectNotFoundException, BadInputException {
    
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanViewBanner();
        
        //Locale locale = I18nUtil.getLocaleInRequest(request);
        
        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) sort = "BannerID";
        if (order.length()== 0) order = "DESC";

        // we continue
        int rowsToReturn = onlineUser.getPostsPerPage();
        int offset = 0;
//        try {
//            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
//        } catch (BadInputException e) {
//            // do nothing
//        }
        
        String memberName = GenericParamUtil.getParameter(request, "MemberName");
        int zoneID = GenericParamUtil.getParameterInt(request, "ZoneID", 0);

        int totalBanners = DAOFactoryAd.getBannerDAO().getNumberOfBeans();
//        if (offset > totalBanners) {
//            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
//            throw new BadInputException(localizedMessage);
//        }
        rowsToReturn = totalBanners > rowsToReturn ? totalBanners : rowsToReturn;
        Collection bannersOfUser = DAOFactoryAd.getBannerDAO().getBeans_withSortSupport_limit(offset, rowsToReturn, sort, order, memberName);
        
        // we loop and only keep banner that has been assigned to a zoneID
        Collection bannerBeans = new ArrayList();
        for (Iterator iterator = bannersOfUser.iterator(); iterator.hasNext(); ) {
            BannerBean bannerBean = (BannerBean) iterator.next();
            if (zoneID != 0) {
                try {
                    DAOFactoryAd.getZoneBannerDAO().findByPrimaryKey(zoneID, bannerBean.getBannerID());
                } catch (ObjectNotFoundException exception) {
                    continue; 
                }
            }
            
            int zoneCount = DAOFactoryAd.getZoneBannerDAO().getNumberOfBeans_inBanner(bannerBean.getBannerID());
            bannerBean.setZoneCount(zoneCount);
            
            bannerBeans.add(bannerBean);
        }
        
        Collection zoneBeans = DAOFactoryAd.getZoneDAO().getBeans();
        Collection memberBeans = DAOFactoryAd.getBannerDAO().getDistinctMemberNamesHasBanner();
        
        request.setAttribute("BannerBeans", bannerBeans);
        request.setAttribute("ZoneID", new Integer(zoneID));
        request.setAttribute("MemberName", memberName);
        request.setAttribute("ZoneBeans", zoneBeans);
        request.setAttribute("MemberBeans", memberBeans);
        //request.setAttribute("TotalBanners", new Integer(totalBanners));

    }
    
    public void prepareManageMedia(GenericRequest request) 
        throws AuthenticationException, DatabaseException, IOException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanUploadMedia();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        String diskFolderOfMember = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName()));
        
        File diskFolderOfMemberFile = new File(diskFolderOfMember);
        if (diskFolderOfMemberFile.exists() == false) {
            log.info("About to create folder for upload banner: " + diskFolderOfMember);
            FileUtil.createDirs(diskFolderOfMember, true);
        }
        if (diskFolderOfMemberFile.isDirectory() == false) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.io.IOException.data_folder_user_not_directory", new Object[] {onlineUser.getMemberName()});
            throw new IOException(localizedMessage);
        }
        
        log.debug("About to list files in folder: " + diskFolderOfMember);
        File[] mediaFiles = diskFolderOfMemberFile.listFiles();
    
        request.setAttribute("MediaFiles", mediaFiles);
        
    }

    public void processDeleteMedia(GenericRequest request) 
        throws BadInputException, AuthenticationException, DatabaseException, IOException {
        
        SecurityUtil.checkHttpPostMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanUploadMedia();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        String fileName = GenericParamUtil.getParameterSafe(request, "filename", true);
        FileUtil.checkGoodFileName(fileName);
        
        String diskFolerOfMember = request.getRealPath(AdModuleUtils.getWebFolderOfMember(onlineUser.getMemberName()));
        
        File diskFolerOfMemberFile = new File(diskFolerOfMember);
        if (diskFolerOfMemberFile.exists() == false) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.io.FileNotFoundException.data_folder_user_not_exist", new Object[] {onlineUser.getMemberName()});
            throw new FileNotFoundException(localizedMessage);
        }
        if (diskFolerOfMemberFile.isDirectory() == false) {
            String localizedMessage = MVNAdResourceBundle.getString(locale, "java.io.IOException.data_folder_user_not_directory", new Object[] {onlineUser.getMemberName()});
            throw new IOException(localizedMessage);
        }
        
        String fileToBeDeleted = diskFolerOfMember + File.separatorChar + fileName;
        FileUtil.deleteFile(fileToBeDeleted);
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.deletemedia", new Object[]{fileName});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN, "delete media", actionDesc, EventLogService.MEDIUM);
    
    }

}
