/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/GroupsWebHandler.java,v 1.55 2009/12/03 08:39:02 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.55 $
 * $Date: 2009/12/03 08:39:02 $
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

import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.GroupsBean;

public class GroupsWebHandler {

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    private EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    public GroupsWebHandler() {
    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws ObjectNotFoundException, BadInputException, CreateException, DatabaseException, 
        ForeignKeyNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);
 
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        MyUtil.saveVNTyperMode(request, response);
        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        String groupName        = GenericParamUtil.getParameterSafe(request, "GroupName", true);
        String groupDesc        = GenericParamUtil.getParameterSafe(request, "GroupDesc", false);
        int groupOption         = 0;//GenericParamUtil.getParameterInt(request, "GroupOption");

        try {
            DAOFactory.getGroupsDAO().create(""/*groupOwnerName*/, groupName, groupDesc,
                               groupOption, now/*groupCreationDate*/, now/*groupModifiedDate*/);
        } catch (DuplicateKeyException ex) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.group_exists", new Object[] {groupName});
            throw new BadInputException(localizedMessage);
        }
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.AddGroupProcess", new Object[]{groupName});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "add group", actionDesc, EventLogService.MEDIUM);

        //now add owner to group if there is owner for this group
        String groupOwnerName   = GenericParamUtil.getParameterSafe(request, "GroupOwnerName", false);
        if (groupOwnerName.length() > 0) {
            int groupID     = DAOFactory.getGroupsDAO().getGroupIDFromGroupName(groupName);
            int privilege   = 0;//@todo review and support it later, should be GroupAdmin
            try {
                DAOFactory.getGroupsDAO().updateOwner(groupID, // primary key
                                       groupOwnerName, now/*groupModifiedDate*/);
                DAOFactory.getMemberGroupDAO().create(groupID, groupOwnerName, privilege, now, now, now);
            } catch (ForeignKeyNotFoundException ex) {
                // what should do when member not found ???
                // now, I just do nothing
            } catch (DuplicateKeyException ex) {
                // do nothing, it is not an error (member already in this group)
            }
        }
    }

    public void prepareDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int groupID = GenericParamUtil.getParameterInt(request, "group");

        //make sure reserved groups are never deleted (like "Registered Members" group)
        if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_group.id_less_than");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete group with id <= " + Integer.toString(MVNForumConstant.LAST_RESERVED_GROUP_ID));
        }

        GroupsBean groupsBean = null;
        try {
            groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.groupid_not_exists", new Object[] {new Integer(groupID)});
            throw new BadInputException(localizedMessage);
        }

        request.setAttribute("GroupsBean", groupsBean);
    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int groupID = GenericParamUtil.getParameterInt(request, "group");

        //make sure reserved groups are never deleted (like "Registered Members" group)
        if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_group.id_less_than", new Object[]{Integer.toString(MVNForumConstant.LAST_RESERVED_GROUP_ID)});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete group with id <= " + Integer.toString(MVNForumConstant.LAST_RESERVED_GROUP_ID));
        }

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        DAOFactory.getGroupForumDAO().delete_inGroup(groupID);

        DAOFactory.getGroupPermissionDAO().delete_inGroup(groupID);

        DAOFactory.getMemberGroupDAO().delete_inGroup(groupID);

        try {
            DAOFactory.getGroupsDAO().delete(groupID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.groupid_not_exists", new Object[] {new Integer(groupID)});
            throw new BadInputException(localizedMessage);
        }

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.DeleteGroupProcess", new Object[]{new Integer(groupID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "delete group", actionDesc, EventLogService.HIGH);

    }

    public void processUpdate(GenericRequest request, GenericResponse response)
        throws BadInputException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        MyUtil.saveVNTyperMode(request, response);
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        
        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int groupID = GenericParamUtil.getParameterInt(request, "group");

        // column(s) to update
        String groupName = GenericParamUtil.getParameterSafe(request, "GroupName", true);
        String groupDesc = GenericParamUtil.getParameterSafe(request, "GroupDesc", true);

        try {
            DAOFactory.getGroupsDAO().update(groupID, // primary key
                               groupName, groupDesc, now/*groupModifiedDate*/);
        } catch (DuplicateKeyException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_update_group", new Object[] {groupName});
            throw new BadInputException(localizedMessage);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.groupid_not_exists", new Object[] {new Integer(groupID)});
            throw new BadInputException(localizedMessage);
        }

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateGroupProcess", new Object[]{new Integer(groupID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update group", actionDesc, EventLogService.MEDIUM);
        
    }

    public void processUpdateGroupOwner(GenericRequest request)
        throws BadInputException, DatabaseException, ForeignKeyNotFoundException,
        CreateException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        // primary key column(s)
        int groupID = GenericParamUtil.getParameterInt(request, "group");

        //make sure group owners for reserved groups can't be changed
        if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete_group_owner_for_reserved_groups");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot update group owner for reserved (virtual) groups.");
        }
        //@todo: Igor: Why don't we allow changing of GroupOwner for reserved groups? I think we have no reason to disallow that.

        // column(s) to update
        String groupOwnerName = GenericParamUtil.getParameterSafe(request, "GroupOwnerName", false);

        try {
            DAOFactory.getGroupsDAO().updateOwner(groupID, // primary key
                                   groupOwnerName, now/*groupModifiedDate*/);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.groupid_not_exists", new Object[] {new Integer(groupID)});
            throw new BadInputException(localizedMessage);
        }

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateGroupOwnerProcess", new Object[]{new Integer(groupID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update group owner", actionDesc, EventLogService.MEDIUM);

        /*
         * now add owner to group if there is owner for this group
         * if member already in the group, we don't throw Exception (DuplicateKeyException)
         */
        if (groupOwnerName.length() > 0) {
            int privilege = 0;//@todo review and support it later
            try {
                DAOFactory.getMemberGroupDAO().create(groupID, groupOwnerName, privilege, now, now, now);
            } catch (DuplicateKeyException ex) {
                // do nothing, it is not an error (member already in this group)
            }
        }
    }

    public void prepareView(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // primary key column(s)
        int groupID = GenericParamUtil.getParameterInt(request, "group");

        GroupsBean groupsBean = null;
        try {
            groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);
        } catch (ObjectNotFoundException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.groupid_not_exists", new Object[] {new Integer(groupID)});
            throw new BadInputException(localizedMessage);
        }

        request.setAttribute("GroupsBean", groupsBean);
    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Collection groupsBeans = DAOFactory.getGroupsDAO().getGroups();

        // now count the number of members in each group
        for (Iterator iterator = groupsBeans.iterator(); iterator.hasNext(); ) {
            GroupsBean groupsBean = (GroupsBean)iterator.next();
            int groupID = groupsBean.getGroupID();
            if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
                int memberCount = DAOFactory.getMemberDAO().getNumberOfMembers();
                if (MVNForumConfig.isGuestUserInDatabase()) {
                    //"Registered Members" group. Exclude virtual guest from count.
                    memberCount--;
                }
                groupsBean.setGroupMemberCount(memberCount);
            } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
                //other reserved groups
                groupsBean.setGroupMemberCount(0);
            } else {
                groupsBean.setGroupMemberCount(DAOFactory.getMemberGroupDAO().getNumberOfBeans_inGroup(groupID));
            }
        }

        request.setAttribute("GroupsBeans", groupsBeans);
    }

}
