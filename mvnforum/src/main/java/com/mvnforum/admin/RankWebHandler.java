/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/RankWebHandler.java,v 1.37 2009/04/29 07:54:57 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.37 $
 * $Date: 2009/04/29 07:54:57 $
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
 * @author: Mai  Nguyen
 */
package com.mvnforum.admin;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.RankBean;

public class RankWebHandler {

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    public RankWebHandler() {
    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws BadInputException, CreateException, DatabaseException,
        DuplicateKeyException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        MyUtil.saveVNTyperMode(request, response);

        int rankMinPosts= GenericParamUtil.getParameterInt(request, "RankMinPosts");
        String rankTitle= GenericParamUtil.getParameterSafe(request, "RankTitle", true);

        // reserved for future
        int rankLevel   = 0;
        String rankImage= "";
        int rankType    = 0;
        int rankOption  = 0;

        DAOFactory.getRankDAO().create(rankMinPosts, rankLevel, rankTitle,
                                       rankImage, rankType, rankOption);

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.AddRankProcess",new Object[]{rankTitle});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "add rank", actionDesc, EventLogService.MEDIUM);

    }

    public void prepareEdit(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // primary key column(s)
        int rankID  = GenericParamUtil.getParameterInt(request, "rank");

        RankBean rankBean = DAOFactory.getRankDAO().getRank(rankID);

        request.setAttribute("RankBean", rankBean);
    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();
    }

    public void processUpdate(GenericRequest request, GenericResponse response)
        throws BadInputException, DatabaseException, DuplicateKeyException,
        ObjectNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        MyUtil.saveVNTyperMode(request, response);

        // primary key column(s)
        int rankID      = GenericParamUtil.getParameterInt(request, "RankID");

        // column(s) to update
        int rankMinPosts= GenericParamUtil.getParameterInt(request, "RankMinPosts");
        String rankTitle= GenericParamUtil.getParameterSafe(request, "RankTitle", true);

        //reserved for future
        int rankLevel   = 0;
        String rankImage= "";
        int rankType    = 0;
        int rankOption  = 0;

        DAOFactory.getRankDAO().update(rankID, // primary key
                             rankMinPosts, rankLevel, rankTitle,
                             rankImage, rankType, rankOption);
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateRankProcess", new Object[]{rankTitle});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update rank", actionDesc, EventLogService.MEDIUM);

    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // primary key column(s)
        int rankID  = GenericParamUtil.getParameterInt(request, "rank");

        DAOFactory.getRankDAO().delete(rankID);

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.DeleteRankProcess",new Object[]{new Integer(rankID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "delete rank", actionDesc, EventLogService.HIGH);

    }

}
