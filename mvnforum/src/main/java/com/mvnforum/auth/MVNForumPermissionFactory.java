/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/MVNForumPermissionFactory.java,v 1.34
 * 2008/12/10 10:36:39 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.34 $ $Date:
 * 2008/12/10 10:36:39 $
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
package com.mvnforum.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MyUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

public final class MVNForumPermissionFactory {

  private MVNForumPermissionFactory() {}

  static MVNForumPermission getAnonymousPermission() throws DatabaseException {

    MVNForumPermissionImpl permission = new MVNForumPermissionImpl();

    // we will clear 'getPermissionsForGroupGuest' cache in 2 minutes (see
    // MemberCache.SHORT_TIME_OUT)
    Collection permList = MemberCache.getInstance().getPermissionsForGroupGuest();
    for (Iterator iter = permList.iterator(); iter.hasNext();) {
      int perm = ((Integer) iter.next()).intValue();
      permission.setPermission(perm);
    }

    // we will clear 'getPermissionsForGroupGuestInForums' cache in 2 minutes (see
    // MemberCache.SHORT_TIME_OUT)
    Collection forumPermList = MemberCache.getInstance().getPermissionsForGroupGuestInForums();
    for (Iterator iter = forumPermList.iterator(); iter.hasNext();) {
      ForumPermission perm = (ForumPermission) iter.next();
      permission.setPermissionInForum(perm.getForumID(), perm.getPermission());
    }

    return permission;
  }

  public static MVNForumPermission getAuthenticatedPermission(MemberBean memberBean)
      throws DatabaseException, ObjectNotFoundException {

    int memberID = memberBean.getMemberID();

    if (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST || memberID == 0) {
      throw new AssertionError("Cannot get authenticated permission for user Guest.");
    }

    MVNForumPermissionImpl permission = new MVNForumPermissionImpl();

    Collection memberPermList = MVNForumPermissionWebHelper.getMemberPermissions(memberID);
    for (Iterator iter = memberPermList.iterator(); iter.hasNext();) {
      int perm = ((Integer) iter.next()).intValue();
      permission.setPermission(perm);
    }

    Collection groupPermList = MVNForumPermissionWebHelper.getGroupPermissions(memberID);
    for (Iterator iter = groupPermList.iterator(); iter.hasNext();) {
      int perm = ((Integer) iter.next()).intValue();
      permission.setPermission(perm);
    }

    Collection groupMemberPermList =
        MVNForumPermissionWebHelper.getGroupMemberPermissions(memberID);
    for (Iterator iter = groupMemberPermList.iterator(); iter.hasNext();) {
      int perm = ((Integer) iter.next()).intValue();
      permission.setPermission(perm);
    }

    Collection forumMemberPermList =
        MVNForumPermissionWebHelper.getMemberPermissionsInForums(memberID);
    for (Iterator iter = forumMemberPermList.iterator(); iter.hasNext();) {
      ForumPermission perm = (ForumPermission) iter.next();
      permission.setPermissionInForum(perm.getForumID(), perm.getPermission());
    }

    Collection forumGroupPermList =
        MVNForumPermissionWebHelper.getGroupPermissionsInForums(memberID);
    for (Iterator iter = forumGroupPermList.iterator(); iter.hasNext();) {
      ForumPermission perm = (ForumPermission) iter.next();
      permission.setPermissionInForum(perm.getForumID(), perm.getPermission());
    }

    Collection forumGroupMemberPermList =
        MVNForumPermissionWebHelper.getGroupMemberPermissionsInForums(memberID);
    for (Iterator iter = forumGroupMemberPermList.iterator(); iter.hasNext();) {
      ForumPermission perm = (ForumPermission) iter.next();
      permission.setPermissionInForum(perm.getForumID(), perm.getPermission());
    }

    Collection groupsContainMember = DAOFactory.getGroupsDAO().getMyGroups(memberID);
    permission.setGroupsContainMember(groupsContainMember);

    if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
        .getCmsRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
      ArrayList cmsGroupPermList =
          MVNCmsPermissionWebHelper.getGroupPermissionsInStepWithChannel(memberID);
      for (int i = 0; i < cmsGroupPermList.size(); i++) {
        CmsPermission perm = (CmsPermission) cmsGroupPermList.get(i);
        if (perm.getChannelID() == 0) {
          permission.setPermissionInStep(perm.getStepID(), perm.getPermission());
        } else {
          permission.setPermissionInStepInChannel(perm.getStepID(), perm.getChannelID(),
              perm.getPermission());
        }
      }
    }

    // get list of forums owned by this member
    String memberName = memberBean.getMemberName();
    Collection listForums = MyUtil.getForumsOwnedByMemberName(memberName);

    // set permissions for list of forums
    for (Iterator iter = listForums.iterator(); iter.hasNext();) {
      ForumBean forumBean = (ForumBean) iter.next();
      int forumID = forumBean.getForumID();
      permission.setPermissionInForum(forumID, MVNForumPermission.PERMISSION_FORUM_ADMIN);
    }

    // After user login always have PERMISSION_AUTHENTICATED
    permission.setPermission(MVNForumPermission.PERMISSION_AUTHENTICATED);

    // now check if user account is activated or not
    if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet() == false) { // disable
                                                                                                  // portlet,
                                                                                                  // run
                                                                                                  // only
                                                                                                  // servlets
      if (DAOFactory.getMemberDAO().getActivateCode(memberID)
          .equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED)) {
        permission.setPermission(MVNForumPermission.PERMISSION_ACTIVATED);
      }
    }

    if (MVNForumConfig.getAlwaysActivation()) {
      permission.setPermission(MVNForumPermission.PERMISSION_ACTIVATED);
    }

    // The Admin (id = 1) always has SystemAdmin permission
    if (MyUtil.isRootAdminID(memberID)) {
      permission.setPermission(MVNForumPermission.PERMISSION_SYSTEM_ADMIN);
    }
    return permission;
  }
}
