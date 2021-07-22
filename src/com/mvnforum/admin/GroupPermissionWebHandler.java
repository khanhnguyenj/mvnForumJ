/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/GroupPermissionWebHandler.java,v 1.38 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.38 $
 * $Date: 2008/12/31 03:50:24 $
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

import java.util.ArrayList;
import java.util.Locale;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.db.*;

public class GroupPermissionWebHandler {

    private static final Logger log = LoggerFactory.getLogger(GroupPermissionWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    public GroupPermissionWebHandler() {
    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, BadInputException, ObjectNotFoundException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        int groupID = GenericParamUtil.getParameterInt(request, "group");

        GroupsBean groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);

        ArrayList groupPermissionBeans = (ArrayList)DAOFactory.getGroupPermissionDAO().getBeans_inGroup(groupID);
        int currentSize = groupPermissionBeans.size();
        int[] currentPermissions = new int[currentSize];
        for (int i = 0; i < currentSize; i++) {
            GroupPermissionBean groupPermissionBean = (GroupPermissionBean)groupPermissionBeans.get(i);
            currentPermissions[i] = groupPermissionBean.getPermission();
        }

        request.setAttribute("GroupsBean", groupsBean);
        request.setAttribute("CurrentPermissions", currentPermissions);
    }

    public void processUpdate(GenericRequest request)
        throws CreateException, ObjectNotFoundException, BadInputException, DatabaseException, DuplicateKeyException,
        ForeignKeyNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        MyUtil.ensureCorrectCurrentPassword(request);
        String btnAction = GenericParamUtil.getParameter(request, "btnAction");
        boolean addAction = false;
        
        if (btnAction.equals("Add")) {
            addAction = true;
        } else if (btnAction.equals("Remove")) {
            addAction = false;
        } else {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process.no_add_or_remove_is_specified");
            throw new BadInputException(localizedMessage);
        }

        int groupID = GenericParamUtil.getParameterInt(request, "group");

        if (addAction) {
            log.debug("Add List");
            String[] addList = request.getParameterValues("add");
            for (int i = 0; (addList != null) && (i < addList.length); i++) {
                int perm = Integer.parseInt(addList[i]);
                log.debug("perm = " + perm);
                DAOFactory.getGroupPermissionDAO().create(groupID, perm);
            }
        } else {
            log.debug("Remove List");
            String[] removeList = request.getParameterValues("remove");
            for (int i = 0; (removeList != null) && (i < removeList.length); i++) {
                int perm = Integer.parseInt(removeList[i]);
                log.debug("perm = " + removeList[i]);
                DAOFactory.getGroupPermissionDAO().delete(groupID, perm);
            }
        }//else

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateGroupPermission",new Object[]{new Integer(groupID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update group permission", actionDesc, EventLogService.MEDIUM);

    }

}
