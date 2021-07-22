/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/MemberForumWebHandler.java,v 1.71 2009/04/16 09:23:43 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.71 $
 * $Date: 2009/04/16 09:23:43 $
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

import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.db.*;
import com.mvnforum.service.*;

public class MemberForumWebHandler {
    
    private static final Logger log = LoggerFactory.getLogger(MemberForumWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    private static CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();

    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    public MemberForumWebHandler() {
    }

    public void prepareAssignForumToMember(GenericRequest request, GenericResponse response)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        int memberID = GenericParamUtil.getParameterInt(request, "memberid");
        MemberBean memberBean = null;
        try {
            memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(memberID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        Collection memberForumBeans = DAOFactory.getMemberForumDAO().getBeans_inMember(memberID);

        request.setAttribute("MemberBean", memberBean);
        request.setAttribute("MemberForumBeans", memberForumBeans);

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, "assignforumtogroup", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());

    }

    public void prepareAssignMemberToForum(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        int forumID = GenericParamUtil.getParameterInt(request, "forum");

        permission.ensureCanAssignToForum(forumID);
        Locale locale = I18nUtil.getLocaleInRequest(request);
        ForumBean forumBean = null;

        try {
            forumBean = ForumCache.getInstance().getBean(forumID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        Collection groupsBeans = DAOFactory.getGroupsDAO().getGroups();
        Collection groupForumBeans = DAOFactory.getGroupForumDAO().getBeans_inForum(forumID);
        Collection memberForumBeans = DAOFactory.getMemberForumDAO().getBeans_inForum(forumID);

        for (Iterator iter = groupForumBeans.iterator(); iter.hasNext(); ) {
            GroupForumBean groupForumBean = (GroupForumBean)iter.next();
            groupForumBean.setGroupsBean(GroupsBean.getGroupsBean(groupsBeans, groupForumBean.getGroupID()));
        }
        for (Iterator iter = memberForumBeans.iterator(); iter.hasNext(); ) {
            MemberForumBean memberForumBean = (MemberForumBean)iter.next();
            //@todo: Optimize (this method can be called many time for just one memberID)
            MemberBean memberBean = null;
            int memberID = memberForumBean.getMemberID();
            try {
                memberBean = DAOFactory.getMemberDAO().getMember(memberID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(memberID)});
                throw new ObjectNotFoundException(localizedMessage);
            }
            memberForumBean.setMemberBean(memberBean);
        }

        HashMap groupPermissionMap = new HashMap();
        for (Iterator iterator = groupForumBeans.iterator(); iterator.hasNext(); ) {
            GroupForumBean groupForumBean = (GroupForumBean)iterator.next();
            String groupName = groupForumBean.getGroupsBean().getGroupName();
            int currentPermission = groupForumBean.getPermission();
            ArrayList combinedPerms;
            ArrayList individualPerms;

            if (groupPermissionMap.containsKey(groupName) == false) {
                ArrayList[] perm = new ArrayList[2];
                combinedPerms = new ArrayList();
                individualPerms = new ArrayList();
                perm[0] = combinedPerms;
                perm[1] = individualPerms;
                groupPermissionMap.put(groupName, perm);

            } else {//map does not have groupName
                ArrayList[] perm = (ArrayList[])groupPermissionMap.get(groupName);
                combinedPerms = perm[0];
                individualPerms = perm[1];
            }

            if (AbstractPermission.isCombinedPerm(currentPermission)) {
                combinedPerms.add(AbstractPermission.getDescription(currentPermission));
            } else if (AbstractPermission.isIndividualPerm(currentPermission)) {
                individualPerms.add(AbstractPermission.getDescription(currentPermission));
            } else {
                // should not be here
                AssertionUtil.doAssert(false, "Serious error!!!");
            }
        }//end for

        HashMap memberPermissionMap = new HashMap();
        for (Iterator iterator = memberForumBeans.iterator(); iterator.hasNext(); ) {
            MemberForumBean memberForumBean = (MemberForumBean)iterator.next();
            String memberName = memberForumBean.getMemberBean().getMemberName();
            int currentPermission = memberForumBean.getPermission();
            ArrayList combinedPerms;
            ArrayList individualPerms;

            if (memberPermissionMap.containsKey(memberName) == false) {
                ArrayList[] perm = new ArrayList[2];
                combinedPerms = new ArrayList();
                individualPerms = new ArrayList();
                perm[0] = combinedPerms;
                perm[1] = individualPerms;
                memberPermissionMap.put(memberName, perm);

            } else {//map does not have memberName
                ArrayList[] perm = (ArrayList[])memberPermissionMap.get(memberName);
                combinedPerms = perm[0];
                individualPerms = perm[1];
            }

            if (AbstractPermission.isCombinedPerm(currentPermission)) {
                combinedPerms.add(AbstractPermission.getDescription(currentPermission));
            } else if (AbstractPermission.isIndividualPerm(currentPermission)) {
                individualPerms.add(AbstractPermission.getDescription(currentPermission));
            } else {
                // should not be here
                AssertionUtil.doAssert(false, "Serious error!!!");
            }
        }//end for

        request.setAttribute("ForumBean", forumBean);
        request.setAttribute("GroupForumBeans", groupForumBeans);
        request.setAttribute("MemberForumBeans", memberForumBeans);
        request.setAttribute("GroupForumPermission", groupPermissionMap);
        request.setAttribute("MemberForumPermission", memberPermissionMap);
    }

    public void processUpdate(GenericRequest request)
        throws CreateException, ObjectNotFoundException, BadInputException, DatabaseException,
        DuplicateKeyException, ForeignKeyNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

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

        int memberID = GenericParamUtil.getParameterInt(request, "memberid");
        try {
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch (Exception e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(memberID)});
            throw new ObjectNotFoundException(localizedMessage); 
        }
        
        int forumID = GenericParamUtil.getParameterInt(request, "forum");
        try {
            DAOFactory.getForumDAO().findByPrimaryKey(forumID);
        } catch (Exception e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage); 
        }
        
        permission.ensureCanAssignToForum(forumID);

        if (addAction) {
            log.debug("Add List");
            String[] addList = request.getParameterValues("add");
            for (int i = 0; (addList != null) && (i < addList.length); i++) {
                int perm = Integer.parseInt(addList[i]);
                log.debug("perm = " + perm);
                DAOFactory.getMemberForumDAO().create(memberID, forumID, perm);
            }
        } else {
            log.debug("Remove List");
            String[] removeList = request.getParameterValues("remove");
            for (int i = 0; (removeList != null) && (i < removeList.length); i++) {
                int perm = Integer.parseInt(removeList[i]);
                log.debug("perm = " + removeList[i]);
                DAOFactory.getMemberForumDAO().delete(memberID, forumID, perm);
            }
        } //else

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateMemberForumPermission", new Object[]{new Integer(memberID),new Integer(forumID)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update member forum permission", actionDesc, EventLogService.HIGH);

    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, BadInputException, ObjectNotFoundException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        int memberID = -1;
        String memberName = GenericParamUtil.getParameterSafe(request, "member", false);
        if (memberName.length() > 0) {
            StringUtil.checkGoodName(memberName, locale);// check for better security
            try {
                memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {memberName});
                throw new ObjectNotFoundException(localizedMessage);
            }
        } else {
            memberID = GenericParamUtil.getParameterInt(request, "memberid");
        }
        int forumID = GenericParamUtil.getParameterInt(request, "forum");

        permission.ensureCanAssignToForum(forumID);

        MemberBean memberBean = null;
        try {
            memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(memberID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        ForumBean forumBean = null;
        try {
            forumBean = ForumCache.getInstance().getBean(forumID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        ArrayList memberForumBeans = (ArrayList) DAOFactory.getMemberForumDAO().getBeans_inMemberForum(memberID, forumID);

        int currentSize = memberForumBeans.size();
        int[] currentPermissions = new int[currentSize];
        for (int i = 0; i < currentSize; i++) {
            MemberForumBean memberForumBean = (MemberForumBean) memberForumBeans.get(i);
            currentPermissions[i] = memberForumBean.getPermission();
        }

        request.setAttribute("MemberBean", memberBean);
        request.setAttribute("ForumBean", forumBean);
        request.setAttribute("CurrentPermissions", currentPermissions);
    }
}
