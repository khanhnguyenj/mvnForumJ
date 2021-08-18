/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/MemberGroupWebHandler.java,v 1.58
 * 2009/04/29 07:54:56 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.58 $ $Date:
 * 2009/04/29 07:54:56 $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.GroupsBean;
import com.mvnforum.db.MemberGroupBean;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

public class MemberGroupWebHandler {

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
  private static EventLogService eventLogService =
      MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

  public MemberGroupWebHandler() {}

  public void processAdd(GenericRequest request) throws IOException, BadInputException,
      CreateException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int expireType = GenericParamUtil.getParameterUnsignedInt(request, "expiretype");

    if ((expireType != 0) && (expireType != 1)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.expire_date_not_valid");
      throw new BadInputException(localizedMessage);
    }

    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    Date expireDate = now;
    Timestamp memberExpireDate = now;
    if (expireType != 0) {
      expireDate = GenericParamUtil.getParameterDate(request, "day", "month", "year");
      if (expireDate.compareTo(now) < 0) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.expired_period_not_valid");
        throw new BadInputException(localizedMessage);
      }
      memberExpireDate = new Timestamp(expireDate.getTime());
    }

    int groupID = GenericParamUtil.getParameterInt(request, "group");

    // check if the group is one of the reserved groups
    if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
      // actually it could be ok to list member in this group
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_add_member_to_virtual_group_registered_members");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Cannot add member to virtual group Registered Members.");
    } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_add_member_to_virtual_reserved_group");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Cannot add member to a reserved (virtual) group.");
    }

    String memberNames = GenericParamUtil.getParameterSafe(request, "MemberNames", true);
    int privilege = 0;// GenericParamUtil.getParameterInt(request, "Privilege");

    // log.debug("member names = " + memberNames);
    StringReader stringReader = new StringReader(memberNames);
    BufferedReader reader = new BufferedReader(stringReader);

    String memberName = null;
    while ((memberName = reader.readLine()) != null) {
      // log.debug("name = " + memberName + " length = " + memberName.length());
      memberName = memberName.trim();
      if (memberName.length() > 0) {
        try {
          DAOFactory.getMemberGroupDAO().create(groupID, memberName, privilege,
              now/* creationDate */, now/* modifiedDate */, memberExpireDate);

          String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
              "mvnforum.eventlog.desc.AddMemberGroupProcess",
              new Object[] {new Integer(groupID), memberName});
          eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
              MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
              "add member group", actionDesc, EventLogService.MEDIUM);

        } catch (DuplicateKeyException ex) {
          // already existed, just ignore
        } catch (ForeignKeyNotFoundException ex) {
          // member not found, just ignore
        }
      } // if memberName is not empty
    } // while
  }

  /*
   * @todo: check if we should reset the GroupOwnerID from Groups table ???
   */
  public void processDelete(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    // primary key column(s)
    int groupID = GenericParamUtil.getParameterInt(request, "group");
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    DAOFactory.getMemberGroupDAO().delete(groupID, memberID);

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.DeleteMemberGroupProcess",
        new Object[] {new Integer(groupID), new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "delete member group", actionDesc, EventLogService.HIGH);

  }

  public void prepareEdit(GenericRequest request) throws BadInputException, DatabaseException,
      ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    // primary key column(s)
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    int groupID = GenericParamUtil.getParameterInt(request, "groupid");
    MemberGroupBean memberGroupBean =
        DAOFactory.getMemberGroupDAO().getMemberGroup(memberID, groupID);
    GroupsBean groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);

    request.setAttribute("MemberGroupBean", memberGroupBean);
    request.setAttribute("GroupsBean", groupsBean);
  }

  public void prepareList_inGroup_limit(GenericRequest request) throws DatabaseException,
      BadInputException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int groupID = GenericParamUtil.getParameterInt(request, "group");

    // check if the group is one of the reserved groups
    if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
      // actually it could be ok to list member in this group
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_list_member_in_virtual_group_registered_members");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Cannot list member in virtual group Registered Members.");
    } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_list_member_in_reserved_group");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Cannot list member in a reserved (virtual) group.");
    }

    GroupsBean groupsBean = DAOFactory.getGroupsDAO().getGroup(groupID);
    Collection memberGroupBeans = DAOFactory.getMemberGroupDAO().getBeans_inGroup(groupID);

    request.setAttribute("MemberGroupBeans", memberGroupBeans);
    request.setAttribute("GroupsBean", groupsBean);
  }

  public void processUpdate(GenericRequest request) throws BadInputException, DatabaseException,
      ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int expireType = GenericParamUtil.getParameterUnsignedInt(request, "expiretype");

    if ((expireType != 0) && (expireType != 1)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.expire_date_not_valid");
      throw new BadInputException(localizedMessage);
    }

    int groupID = GenericParamUtil.getParameterInt(request, "groupid");
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    MemberGroupBean memberGroupBean =
        DAOFactory.getMemberGroupDAO().getMemberGroup(memberID, groupID);

    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    Date expireDate = now;
    Timestamp memberExpireDate = now;
    if (expireType == 0) {
      memberExpireDate = memberGroupBean.getCreationDate();
    } else {
      expireDate = GenericParamUtil.getParameterDate(request, "day", "month", "year");
      if (expireDate.compareTo(now) < 0) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.expired_period_not_valid");
        throw new BadInputException(localizedMessage);
      }
      memberExpireDate = new Timestamp(expireDate.getTime());
    }

    DAOFactory.getMemberGroupDAO().update(memberID, groupID, now/* modifiedDate */,
        memberExpireDate);
    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.UpdateMemberGroupProcess",
        new Object[] {new Integer(groupID), new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "update member group", actionDesc, EventLogService.MEDIUM);
  }

  public void deleteExpiredMembers() throws DatabaseException {

    DAOFactory.getMemberGroupDAO().deleteExpiredMembers();
  }

}
