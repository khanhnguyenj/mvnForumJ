/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/CssTaskWebHandler.java,v 1.13 2009/01/06 16:43:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/01/06 16:43:12 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
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
 * @author: Minh Nguyen
 */
package com.mvnforum.admin;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.FileUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.*;
import com.mvnforum.auth.*;

public class CssTaskWebHandler {

    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public CssTaskWebHandler() {
    }

    public void prepareEditCSS(GenericRequest request)
        throws AuthenticationException, DatabaseException, IOException, FileNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String cssFileName = request.getRealPath(MVNForumGlobal.CSS_FULLPATH);
        String cssContent = FileUtil.readFile(cssFileName, "UTF-8");
        cssContent = DisableHtmlTagFilter.filter(cssContent);

        request.setAttribute("CSSBody", cssContent);
    }

    public void processEditCSS(GenericRequest request)
        throws AuthenticationException, DatabaseException, IOException,
        FileNotFoundException, BadInputException {

        SecurityUtil.checkHttpPostMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");
        String cssContent = GenericParamUtil.getParameterSafe(request, "body", true);

        String cssFile = (isPreviewing) ? MVNForumGlobal.CSS_PREVIEW_FULLPATH : MVNForumGlobal.CSS_FULLPATH;
        String cssFilePath = request.getRealPath(cssFile);
        FileUtil.touch(cssFilePath);// make sure we create this file and update the timestamp of this file
        FileUtil.writeFile(cssContent, cssFilePath, "UTF-8");

        request.setAttribute("IsPreviewing", Boolean.valueOf(isPreviewing));
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateCSS");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update css", actionDesc, EventLogService.MEDIUM);

    }
    
    public void processEditCSS_forRender(GenericRequest request) 
        throws FileNotFoundException, IOException {
        
        boolean isPreviewing = ((Boolean)request.getAttribute("IsPreviewing")).booleanValue();
        
        String cssFile = (isPreviewing) ? MVNForumGlobal.CSS_PREVIEW_FULLPATH : MVNForumGlobal.CSS_FULLPATH;
        String cssFilePath = request.getRealPath(cssFile);
        String cssContent = FileUtil.readFile(cssFilePath, "UTF-8");
        cssContent = DisableHtmlTagFilter.filter(cssContent);

        request.setAttribute("CSSBody", cssContent);

        if (isPreviewing) {
            request.setAttribute("csspreview", "yes");
        } else {
            request.setAttribute("Success", "yes");
        }
    }

    public void processRestoreCSS(GenericRequest request)
        throws AuthenticationException, DatabaseException, IOException,
        FileNotFoundException, BadInputException {
        
        SecurityUtil.checkHttpPostMethod(request);
        
        MyUtil.ensureCorrectCurrentPassword(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String cssBackup = request.getRealPath(MVNForumGlobal.CSS_BACKUP_FULLPATH);
        String fileBackup = FileUtil.readFile(cssBackup, "UTF-8");
        String cssFileName = request.getRealPath(MVNForumGlobal.CSS_FULLPATH);
        FileUtil.writeFile(fileBackup, cssFileName, "UTF-8");
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.RestoreCSS");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "restore css", actionDesc, EventLogService.MEDIUM);

    }

}
