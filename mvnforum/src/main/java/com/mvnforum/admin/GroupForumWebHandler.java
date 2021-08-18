/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/GroupForumWebHandler.java,v 1.66
 * 2009/03/20 09:17:35 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.66 $ $Date:
 * 2009/03/20 09:17:35 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AbstractPermission;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryBuilder;
import com.mvnforum.categorytree.CategoryTree;
import com.mvnforum.categorytree.CategoryTreeListener;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.ForumCache;
import com.mvnforum.db.GroupForumBean;
import com.mvnforum.db.GroupsBean;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberForumBean;
import com.mvnforum.service.CategoryBuilderService;
import com.mvnforum.service.CategoryService;
import com.mvnforum.service.MvnForumServiceFactory;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class GroupForumWebHandler {

  private static final Logger log = LoggerFactory.getLogger(GroupForumWebHandler.class);

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  private static CategoryService categoryService =
      MvnForumServiceFactory.getMvnForumService().getCategoryService();

  private static CategoryBuilderService categoryBuilderService =
      MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

  private static EventLogService eventLogService =
      MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

  public GroupForumWebHandler() {}

  public void prepareList(GenericRequest request) throws DatabaseException, BadInputException,
      AuthenticationException, ObjectNotFoundException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    int groupID = GenericParamUtil.getParameterInt(request, "group");
    int forumID = GenericParamUtil.getParameterInt(request, "forum");

    permission.ensureCanAssignToForum(forumID);
    Locale locale = I18nUtil.getLocaleInRequest(request);

    GroupsBean groupsBean = null;
    try {
      groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);
    } catch (ObjectNotFoundException e1) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.groupid_not_exists",
          new Object[] {new Integer(groupID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    ForumBean forumBean = null;
    try {
      forumBean = ForumCache.getInstance().getBean(forumID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    ArrayList groupForumBeans =
        (ArrayList) DAOFactory.getGroupForumDAO().getBeans_inGroupForum(groupID, forumID);
    int currentSize = groupForumBeans.size();
    int[] currentPermissions = new int[currentSize];
    for (int i = 0; i < currentSize; i++) {
      GroupForumBean groupForumBean = (GroupForumBean) groupForumBeans.get(i);
      currentPermissions[i] = groupForumBean.getPermission();
    }

    request.setAttribute("GroupsBean", groupsBean);
    request.setAttribute("ForumBean", forumBean);
    request.setAttribute("CurrentPermissions", currentPermissions);
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
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_process.no_add_or_remove_is_specified");
      throw new BadInputException(localizedMessage);
    }

    int groupID = GenericParamUtil.getParameterInt(request, "group");
    try {
      DAOFactory.getGroupsDAO().findByPrimaryKey(groupID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.groupid_not_exists",
          new Object[] {new Integer(groupID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int forumID = GenericParamUtil.getParameterInt(request, "forum");
    try {
      DAOFactory.getForumDAO().findByPrimaryKey(forumID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    permission.ensureCanAssignToForum(forumID);

    if (addAction) {
      log.debug("Add List");
      String[] addList = request.getParameterValues("add");
      for (int i = 0; (addList != null) && (i < addList.length); i++) {
        int perm = Integer.parseInt(addList[i]);
        log.debug("perm = " + perm);
        DAOFactory.getGroupForumDAO().create(groupID, forumID, perm);
      }
    } else {
      log.debug("Remove List");
      String[] removeList = request.getParameterValues("remove");
      for (int i = 0; (removeList != null) && (i < removeList.length); i++) {
        int perm = Integer.parseInt(removeList[i]);
        log.debug("perm = " + removeList[i]);
        DAOFactory.getGroupForumDAO().delete(groupID, forumID, perm);
      }
    } // else

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.UpdateGroupForumPermission", new Object[] {new Integer(groupID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "update group forum permission", actionDesc, EventLogService.HIGH);

  }

  public void prepareAssignForumToGroup(GenericRequest request, GenericResponse response)
      throws BadInputException, DatabaseException, ObjectNotFoundException,
      AuthenticationException {

    // In this function, we will show the current permission of this group
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    int groupID = GenericParamUtil.getParameterInt(request, "group");

    GroupsBean groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);
    Collection groupForumBeans = DAOFactory.getGroupForumDAO().getBeans_inGroup(groupID);

    request.setAttribute("GroupsBean", groupsBean);
    request.setAttribute("GroupForumBeans", groupForumBeans);

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        "assignforumtogroup", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
  }

  public void prepareAssignGroupToForum(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    // In this function, we will show the current permission of this forum
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    int forumID = GenericParamUtil.getParameterInt(request, "forum");
    Locale locale = I18nUtil.getLocaleInRequest(request);
    permission.ensureCanAssignToForum(forumID);
    ForumBean forumBean = null;
    try {
      forumBean = ForumCache.getInstance().getBean(forumID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    Collection groupsBeans = DAOFactory.getGroupsDAO().getGroups();
    Collection groupForumBeans = DAOFactory.getGroupForumDAO().getBeans_inForum(forumID);
    Collection memberForumBeans = DAOFactory.getMemberForumDAO().getBeans_inForum(forumID);

    for (Iterator iter = groupForumBeans.iterator(); iter.hasNext();) {
      GroupForumBean groupForumBean = (GroupForumBean) iter.next();
      groupForumBean
          .setGroupsBean(GroupsBean.getGroupsBean(groupsBeans, groupForumBean.getGroupID()));
    }
    int memberID = 0;
    try {
      for (Iterator iter = memberForumBeans.iterator(); iter.hasNext();) {
        MemberForumBean memberForumBean = (MemberForumBean) iter.next();
        // @todo: Optimize (this method can be called many time for just one memberID)
        memberID = memberForumBean.getMemberID();
        MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        memberForumBean.setMemberBean(memberBean);
      }
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    HashMap groupPermissionMap = new HashMap();
    for (Iterator iterator = groupForumBeans.iterator(); iterator.hasNext();) {
      GroupForumBean groupForumBean = (GroupForumBean) iterator.next();
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

      } else {// map does not have groupName
        ArrayList[] perm = (ArrayList[]) groupPermissionMap.get(groupName);
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
    } // end for

    HashMap memberPermissionMap = new HashMap();
    for (Iterator iterator = memberForumBeans.iterator(); iterator.hasNext();) {
      MemberForumBean memberForumBean = (MemberForumBean) iterator.next();
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

      } else {// map does not have memberName
        ArrayList[] perm = (ArrayList[]) memberPermissionMap.get(memberName);
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
    } // end for

    request.setAttribute("ForumBean", forumBean);
    request.setAttribute("GroupsBeans", groupsBeans);
    request.setAttribute("GroupForumBeans", groupForumBeans);
    request.setAttribute("MemberForumBeans", memberForumBeans);
    request.setAttribute("GroupForumPermission", groupPermissionMap);
    request.setAttribute("MemberForumPermission", memberPermissionMap);
  }
}
