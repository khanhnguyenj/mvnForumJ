/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/MemberWebHandler.java,v 1.186
 * 2009/10/02 08:03:49 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.186 $ $Date:
 * 2009/10/02 08:03:49 $
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
 *
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.MVNForumPermissionFactory;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryBuilder;
import com.mvnforum.categorytree.CategoryTree;
import com.mvnforum.categorytree.CategoryTreeListener;
import com.mvnforum.common.MemberMapping;
import com.mvnforum.common.MemberUtil;
import com.mvnforum.common.PrivateMessageUtil;
import com.mvnforum.common.SendMailUtil;
import com.mvnforum.common.StatisticsUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.GroupForumBean;
import com.mvnforum.db.GroupPermissionBean;
import com.mvnforum.db.GroupsBean;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import com.mvnforum.db.MemberDAO;
import com.mvnforum.search.member.MemberIndexer;
import com.mvnforum.search.member.MemberSearchQuery;
import com.mvnforum.service.CategoryBuilderService;
import com.mvnforum.service.CategoryService;
import com.mvnforum.service.MvnForumServiceFactory;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class MemberWebHandler {

  private static final Logger log = LoggerFactory.getLogger(MemberWebHandler.class);

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  private static CategoryService categoryService =
      MvnForumServiceFactory.getMvnForumService().getCategoryService();

  private static CategoryBuilderService categoryBuilderService =
      MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

  private static EventLogService eventLogService =
      MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

  public MemberWebHandler() {}

  public void prepareAdd(GenericRequest request) throws AuthenticationException, DatabaseException {

    if (MVNForumConfig.getEnableExternalUserDatabase()) {
      Locale locale = I18nUtil.getLocaleInRequest(request);

      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.create_user_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create user if we enable external user database.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();
  }

  public void processAdd(GenericRequest request) throws BadInputException, ObjectNotFoundException,
      CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException,
      AuthenticationException, InterceptorException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableExternalUserDatabase()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.create_user_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create user if we enable external user database.");
    }

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    String memberName = GenericParamUtil.getParameterSafe(request, "MemberName", true); // check
                                                                                        // good name
    StringUtil.checkGoodName(memberName, locale);

    MemberUtil.checkReservedUsername(memberName, locale);

    InterceptorService.getInstance().validateLoginID(memberName);

    if (memberName.length() > MVNForumGlobal.MAX_MEMBER_LOGIN_LENGTH) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.member_name_too_long");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("MemberName cannot be longer than 30 characters.");
    }

    String memberPassword1 = GenericParamUtil.getParameterPassword(request, "MemberMatkhau", 3, 0);
    String memberPassword2 =
        GenericParamUtil.getParameterPassword(request, "MemberMatkhauConfirm", 3, 0);

    InterceptorService.getInstance().validatePassword(memberPassword1);

    if (!memberPassword1.equals(memberPassword2)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.confirmed_password_is_not_match");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Password and confirmed password are not the same, please try
      // again.");
    }
    String memberPassword = Encoder.getMD5_Base64(memberPassword1);

    String memberEmail = GenericParamUtil.getParameterEmail(request, "MemberEmail");
    if (memberEmail.length() > MVNForumGlobal.MAX_MEMBER_EMAIL_LENGTH) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.member_email_too_long");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("MemberEmail cannot be longer than 60 characters.");
    }
    String memberFirstEmail = memberEmail;
    InterceptorService.getInstance().validateMail(memberFirstEmail);

    int memberEmailVisible = MemberBean.MEMBER_EMAIL_INVISIBLE;
    int memberNameVisible = MemberBean.MEMBER_NAME_VISIBLE;
    String memberFirstIP = request.getRemoteAddr();
    String memberLastIP = memberFirstIP;
    int memberOption = 0; // @todo review and support it later
    int memberStatus = MemberBean.MEMBER_STATUS_ENABLE; // MemberBean.MEMBER_STATUS_PENDING;// @todo
                                                        // review and support
    // it later, ex: should it be active or not?
    String memberActivateCode = ""; // @todo review and support it later
    int memberMessageOption = 0; // @todo review and support it later
    int memberPostsPerPage = 10; // default for all preregistered users
    Date memberBirthday = MemberBean.MEMBER_NOT_REQUIRE_BIRTHDAY;
    Timestamp memberCreationDate = now;
    Timestamp memberModifiedDate = now;
    Timestamp memberExpireDate = memberCreationDate; // equal Creation Date mean no expiration
    Timestamp memberPasswordExpireDate = memberCreationDate; // equal Creation Date mean no password
                                                             // expiration

    try {
      DAOFactory.getMemberDAO().create(memberName, memberPassword, memberFirstEmail, memberEmail,
          memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP,
          0 /* memberViewCount */, 0 /* memberPostCount */, memberCreationDate, memberModifiedDate,
          memberExpireDate, memberPasswordExpireDate, now /* memberLastLogon */, memberOption,
          memberStatus, memberActivateCode, "" /* memberTempPassword */, 0 /* memberMessageCount */,
          memberMessageOption, memberPostsPerPage, 0 /* memberWarnCount */, 0 /* memberVoteCount */,
          0 /* memberVoteTotalStars */, 0 /* memberRewardPoints */, "" /* memberTitle */,
          0 /* memberTimeZone */, "" /* memberSignature */, "" /* memberAvatar */,
          "" /* memberSkin */, "" /* memberLanguage */, " " /* memberFirstname */,
          " " /* memberLastname */, 1 /* memberGender */, memberBirthday, "" /* memberAddress */,
          "" /* memberCity */, "" /* memberState */, "" /* memberCountry */, "" /* memberPhone */,
          "" /* memberMobile */, "" /* memberFax */, "" /* memberCareer */,
          "" /* memberHomepage */);
    } catch (DuplicateKeyException ex) {
      if (ex.getMessage().indexOf("MemberEmail") >= 0) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.email_exists",
            new Object[] {memberName, memberEmail});
        throw new BadInputException(localizedMessage);
      } else if (ex.getMessage().indexOf("MemberName") >= 0) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.account_exists", new Object[] {memberName});
        throw new BadInputException(localizedMessage);
      }
    }
    // Now, create 4 default folders for each member
    int memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
    int folderStatus = 0;
    int folderOption = 0;
    int folderType = 0;
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, memberID,
        0 /* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, memberID,
        1 /* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, memberID,
        2 /* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, memberID,
        3 /* order */, folderStatus, folderOption, folderType, now, now);

    // Add member to the lucene index
    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    MemberIndexer.scheduleAddMemberTask(memberBean);

    // now, if require activation, then we will send mail
    // Note that because after this page succeed,
    // we redirect to usermanagement so not use mvnforum.mail.failed now
    if (MVNForumConfig.getRequireActivation()) {
      String serverName = ParamUtil.getServerPath(); // ParamUtil.getServer2(request);
      try {
        SendMailUtil.sendActivationCodeEmail(memberID, serverName);
      } catch (Exception ex) {
        log.error("Cannot send mail after registration!", ex);
        request.setAttribute("mvnforum.mail.failed",
            "Cannot send activation email after registration!");
        // @todo: save the error message to displayed later
      }
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.AddMemberProcess", new Object[] {memberName});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "add member", actionDesc, EventLogService.MEDIUM);
  }

  public void processUpdateMemberStatus(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    // primary key column(s)
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    // column(s) to update
    int memberStatus = GenericParamUtil.getParameterInt(request, "status");
    Locale locale = I18nUtil.getLocaleInRequest(request);

    // now check if status is in the valid range
    if ((memberStatus != MemberBean.MEMBER_STATUS_ENABLE)
        && (memberStatus != MemberBean.MEMBER_STATUS_DISABLE)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_update_member_status.unknown_status",
          new Object[] {new Integer(memberStatus)});
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot update member's status to an unknown status = " +
      // memberStatus);
    }

    // This code make sure Admin always has Enable status
    if (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) {
      memberStatus = MemberBean.MEMBER_STATUS_ENABLE;
    }
    // IMPORTANT: Guest (id=MEMBER_ID_OF_GUEST) can be disabled by administrator.
    try {
      DAOFactory.getMemberDAO().updateStatus(memberID, memberStatus);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.UpdateMemberStatus",
        new Object[] {new Integer(memberID), new Integer(memberStatus)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "update member status", actionDesc, EventLogService.MEDIUM);
  }

  public void prepareEdit(GenericRequest request) throws BadInputException, ObjectNotFoundException,
      DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    request.setAttribute("MemberBean", memberBean);
  }

  public void processEdit(GenericRequest request, GenericResponse response)
      throws BadInputException, ObjectNotFoundException, DatabaseException,
      AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    MyUtil.saveVNTyperMode(request, response);

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    MemberMapping mapping = MemberMapping.getInstance();
    boolean internalUserDatabase = !MVNForumConfig.getEnableExternalUserDatabase();

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    int memberStatus = GenericParamUtil.getParameterInt(request, "MemberStatus");
    // This code make sure Admin always has Enable status
    if (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) {
      memberStatus = MemberBean.MEMBER_STATUS_ENABLE;
    }

    // column(s) to update
    int memberEmailVisible = memberBean.getMemberEmailVisible();
    int memberNameVisible = memberBean.getMemberNameVisible();
    int memberOption = 0; // GenericParamUtil.getParameterInt(request, "MemberOption");
    int memberMessageOption = 0; // GenericParamUtil.getParameterInt(request,
                                 // "MemberMessageOption");
    int memberPostsPerPage = memberBean.getMemberPostsPerPage();
    if (memberPostsPerPage < 5) {
      memberPostsPerPage = 5;
    }
    double memberTimeZone = memberBean.getMemberTimeZone();
    String memberSkin = memberBean.getMemberSkin();
    String memberLanguage = memberBean.getMemberLanguage();
    String memberFirstname = memberBean.getMemberFirstname();
    String memberLastname = memberBean.getMemberLastname();
    int memberGender = memberBean.getMemberGender();
    Date memberBirthday = memberBean.getMemberBirthday();
    String memberAddress = memberBean.getMemberAddress();
    String memberCity = memberBean.getMemberCity();
    String memberState = memberBean.getMemberState();
    String memberCountry = memberBean.getMemberCountry();
    String memberPhone = memberBean.getMemberPhone();
    String memberMobile = memberBean.getMemberMobile();
    String memberFax = memberBean.getMemberFax();
    String memberCareer = memberBean.getMemberCareer();
    String memberHomepage = memberBean.getMemberHomepage();

    // column(s) to update
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberEmailVisible())) {
      memberEmailVisible = GenericParamUtil.getParameterBoolean(request, "MemberEmailVisible")
          ? MemberBean.MEMBER_EMAIL_VISIBLE
          : MemberBean.MEMBER_EMAIL_INVISIBLE;
    }
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberNameVisible())) {
      memberNameVisible = GenericParamUtil.getParameterBoolean(request, "MemberNameVisible")
          ? MemberBean.MEMBER_NAME_VISIBLE
          : MemberBean.MEMBER_NAME_INVISIBLE;
    }
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberPostsPerPage())) {
      memberPostsPerPage = GenericParamUtil.getParameterInt(request, "MemberPostsPerPage");
    }
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberTimeZone())) {
      memberTimeZone = GenericParamUtil.getParameterTimeZone(request, "MemberTimeZone");
    }
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberSkin())) {
      memberSkin = GenericParamUtil.getParameterFilter(request, "MemberSkin", false);
    }
    if (internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberLanguage())) {
      memberLanguage = GenericParamUtil.getParameterFilter(request, "MemberLanguage", false);
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFirstname()))
        && MVNForumConfig.getEnableShowFirstName()) {
      memberFirstname = GenericParamUtil.getParameterFilter(request, "MemberFirstname",
          MVNForumConfig.isRequireRegisterFirstname());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberLastname()))
        && MVNForumConfig.getEnableShowLastName()) {
      memberLastname = GenericParamUtil.getParameterFilter(request, "MemberLastname",
          MVNForumConfig.isRequireRegisterLastname());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberGender()))
        && MVNForumConfig.getEnableShowGender()) {
      memberGender = GenericParamUtil.getParameterBoolean(request, "MemberGender") ? 1 : 0;
      memberGender = Integer.parseInt(GenericParamUtil.getParameterFilter(request, "MemberGender",
          MVNForumConfig.isRequireRegisterGender()));
      if (memberGender != 0 && memberGender != 1) {
        memberGender = 0;
      }
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberBirthday()))
        && MVNForumConfig.getEnableShowBirthday()) {
      String memberBirthdayStr = GenericParamUtil.getParameter(request, "MemberBirthday");
      if (MVNForumConfig.isRequireRegisterBirthday() || memberBirthdayStr.length() > 0) {
        memberBirthday = GenericParamUtil.getParameterDate(request, "MemberBirthday");
      }
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberAddress()))
        && MVNForumConfig.getEnableShowAddress()) {
      memberAddress = GenericParamUtil.getParameterFilter(request, "MemberAddress",
          MVNForumConfig.isRequireRegisterAddress());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCity()))
        && MVNForumConfig.getEnableShowCity()) {
      memberCity = GenericParamUtil.getParameterFilter(request, "MemberCity",
          MVNForumConfig.isRequireRegisterCity());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberState()))
        && MVNForumConfig.getEnableShowState()) {
      memberState = GenericParamUtil.getParameterFilter(request, "MemberState",
          MVNForumConfig.isRequireRegisterState());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCountry()))
        && MVNForumConfig.getEnableShowCountry()) {
      memberCountry = GenericParamUtil.getParameterFilter(request, "MemberCountry",
          MVNForumConfig.isRequireRegisterCountry());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberPhone()))
        && MVNForumConfig.getEnableShowPhone()) {
      memberPhone = GenericParamUtil.getParameterFilter(request, "MemberPhone",
          MVNForumConfig.isRequireRegisterPhone());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberMobile()))
        && MVNForumConfig.getEnableShowMobile()) {
      memberMobile = GenericParamUtil.getParameterFilter(request, "MemberMobile",
          MVNForumConfig.isRequireRegisterMobile());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberFax()))
        && MVNForumConfig.getEnableShowFax()) {
      memberFax = GenericParamUtil.getParameterFilter(request, "MemberFax",
          MVNForumConfig.isRequireRegisterFax());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberCareer()))
        && MVNForumConfig.getEnableShowCareer()) {
      memberCareer = GenericParamUtil.getParameterFilter(request, "MemberCareer",
          MVNForumConfig.isRequireRegisterCareer());
    }
    if ((internalUserDatabase || MemberMapping.isLocalField(mapping.getMemberHomepage()))
        && MVNForumConfig.getEnableShowHomepage()) {
      String memberHomepageStr = GenericParamUtil.getParameter(request, "MemberHomepage");
      if (MVNForumConfig.isRequireRegisterHomepage() || memberHomepageStr.length() > 0) {
        memberHomepage = GenericParamUtil.getParameterUrl(request, "MemberHomepage");
      }
    }

    try {
      DAOFactory.getMemberDAO().update(memberID, // primary key
          memberEmailVisible, memberNameVisible, now /* memberModifiedDate */, memberOption,
          memberStatus, memberMessageOption, memberPostsPerPage, memberTimeZone, memberSkin,
          memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday,
          memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile,
          memberFax, memberCareer, memberHomepage);

      String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
          "mvnforum.eventlog.desc.UpdateMemberProcess", new Object[] {new Integer(memberID)});
      eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
          MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
          "update member", actionDesc, EventLogService.MEDIUM);

    } catch (ObjectNotFoundException ex) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    MemberBean justEditedMemberBean = DAOFactory.getMemberDAO().getMember(memberID);

    MemberIndexer.scheduleUpdateMemberTask(justEditedMemberBean);
    // request.setAttribute("MemberBean", justEditedMemberBean);
  }

  public void prepareView(GenericRequest request) throws BadInputException, ObjectNotFoundException,
      DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    String strMemberID = GenericParamUtil.getParameter(request, "memberid", false);
    String strMemberName = GenericParamUtil.getParameter(request, "member", false);
    String strMemberEmail = GenericParamUtil.getParameter(request, "memberemail", false);

    int memberID;
    MemberBean memberBean = null;

    if (strMemberID.length() > 0) {
      memberID = GenericParamUtil.getParameterInt(request, "memberid");
    } else if (strMemberName.length() > 0) {
      /**
       * @todo: improve this for better performance(don't use this method, and write 2 new methods)
       */
      StringUtil.checkGoodName(strMemberName, locale); // check for better security
      try {
        memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(strMemberName);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {strMemberName});
        throw new ObjectNotFoundException(localizedMessage);
      }

      // now, in the LDAP case, we can get memberID from memberName does not mean user is in LDAP or
      // not,
      // so we must check if the whole memberBean can be loaded or not
      try {
        memberBean = DAOFactory.getMemberDAO().getMember(memberID);
      } catch (ObjectNotFoundException e) {
        throw new ObjectNotFoundException("Member with login id = " + strMemberName
            + " has been deleted externally (for example, in LDAP) but the numeric id (" + memberID
            + ") still in the database.");
      }
    } else if (strMemberEmail.length() > 0) {
      String memberEmail = GenericParamUtil.getParameterEmail(request, "memberemail"); // check for
                                                                                       // better
                                                                                       // security
      try {
        memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberEmail(memberEmail);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.memberemail_not_exists",
            new Object[] {memberEmail});
        throw new ObjectNotFoundException(localizedMessage);
      }
    } else {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_get_member_info");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot get the information to view member.");
    }

    // Update statistics to make sure Admin can delete this user
    // with the actual memberPostCount
    try {
      StatisticsUtil.updateMemberStatistics(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int toMessageCount = 0;
    int fromMessageCount = 0;

    String toDay = GenericParamUtil.getParameter(request, "toDay", false);
    String toMonth = GenericParamUtil.getParameter(request, "toMonth", false);
    String toYear = GenericParamUtil.getParameter(request, "toYear", false);

    String fromDay = GenericParamUtil.getParameter(request, "fromDay", false);
    String fromMonth = GenericParamUtil.getParameter(request, "fromMonth", false);
    String fromYear = GenericParamUtil.getParameter(request, "fromYear", false);

    if (!"".equals(toDay) && !"".equals(toMonth) && !"".equals(toYear) && !"".equals(fromDay)
        && !"".equals(fromMonth) && !"".equals(fromYear)) {
      Timestamp from = new Timestamp(
          GenericParamUtil.getParameterDate(request, "fromDay", "fromMonth", "fromYear").getTime());
      Timestamp to = new Timestamp(
          GenericParamUtil.getParameterDate(request, "toDay", "toMonth", "toYear").getTime());

      toMessageCount = DAOFactory.getMessageStatisticsDAO()
          .getNumberOfBeans_inToID_supportTimestamp(memberID, from, to);
      fromMessageCount = DAOFactory.getMessageStatisticsDAO()
          .getNumberOfBeans_inFromID_supportTimestamp(memberID, from, to);
    } else {
      toMessageCount = DAOFactory.getMessageStatisticsDAO().getNumberOfBeans_inToID(memberID);
      fromMessageCount = DAOFactory.getMessageStatisticsDAO().getNumberOfBeans_inFromID(memberID);
    }

    try {
      if (memberBean == null) {
        memberBean = DAOFactory.getMemberDAO().getMember(memberID);
      }
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int watchCount = DAOFactory.getWatchDAO().getNumberOfWatches_forMember(memberID);

    request.setAttribute("MemberBean", memberBean);
    request.setAttribute("WatchCount", new Integer(watchCount));

    request.setAttribute("ToMessageCount", new Integer(toMessageCount));
    request.setAttribute("FromMessageCount", new Integer(fromMessageCount));
    request.setAttribute("toDay", toDay);
    request.setAttribute("toMonth", toMonth);
    request.setAttribute("toYear", toYear);
    request.setAttribute("fromDay", fromDay);
    request.setAttribute("fromMonth", fromMonth);
    request.setAttribute("fromYear", toYear);
  }

  public void processUpdateMemberTitle(GenericRequest request, GenericResponse response)
      throws BadInputException, DatabaseException, ObjectNotFoundException,
      AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    MyUtil.saveVNTyperMode(request, response);

    // primary key column(s)
    int memberID = GenericParamUtil.getParameterInt(request, "MemberID");

    // column(s) to update
    String memberTitle = GenericParamUtil.getParameterSafe(request, "MemberTitle", false);
    Locale locale = I18nUtil.getLocaleInRequest(request);
    try {
      DAOFactory.getMemberDAO().updateTitle(memberID, memberTitle);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.UpdateMemberTitle",
        new Object[] {new Integer(memberID), memberTitle});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "update member title", actionDesc, EventLogService.MEDIUM);

    // clear the member cache
    MemberCache.getInstance().clear();
  }

  public void processResetMemberSignature(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    try {
      DAOFactory.getMemberDAO().updateSignature(memberID, "");
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.ResetMemberSignature", new Object[] {new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "reset member signature", actionDesc, EventLogService.MEDIUM);

    request.setAttribute("MemberID", new Integer(memberID));
  }

  public void processResetMemberAvatar(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    try {
      DAOFactory.getMemberDAO().updateAvatar(memberID, "");
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.ResetMemberAvatar", new Object[] {new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "reset member avatar", actionDesc, EventLogService.MEDIUM);

    request.setAttribute("MemberID", new Integer(memberID));
  }

  public void processResetMemberActivation(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser(); // TODO: should check if we need AdminSystem permission to
                                        // do this
    // action

    Locale locale = I18nUtil.getLocaleInRequest(request);
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    try {
      DAOFactory.getMemberDAO().updateActivateCode(memberID, "");
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.ResetMemberActivation", new Object[] {new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "reset member activation", actionDesc, EventLogService.MEDIUM);

    request.setAttribute("MemberID", new Integer(memberID));
  }

  public void prepareDeleteMember(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    if ((memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) || (memberID == 0)
        || (memberID == onlineUser.getMemberID())
        || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST)) {

      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_delete_admin_or_guest");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot delete default Admin and Guest users.");
    }

    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    request.setAttribute("MemberBean", memberBean);
  }

  public void processDeleteMember(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");
    if ((memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) || (memberID == 0)
        || (memberID == onlineUser.getMemberID())
        || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_delete_admin_or_guest");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot delete default Admin and Guest users.");
    }

    boolean deleteSentMessages =
        GenericParamUtil.getParameterBoolean(request, "deletesentmessages");

    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    MemberWebHandler.deleteMember(memberBean, deleteSentMessages, /* context */ request, locale);

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.DeleteMemberProcess", new Object[] {new Integer(memberID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "delete member", actionDesc, EventLogService.HIGH);
  }

  /** This method supports sorting base on many criteria */
  public void prepareShowUserManagement(GenericRequest request)
      throws DatabaseException, BadInputException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // for sort and order stuff
    String sort = GenericParamUtil.getParameter(request, "sort");
    String order = GenericParamUtil.getParameter(request, "order");
    if (sort.length() == 0) {
      sort = "MemberCreationDate";
    }
    if (order.length() == 0) {
      order = "DESC";
    }

    // we continue
    int postsPerPage = onlineUser.getPostsPerPage();
    int offset = 0;
    try {
      offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
    } catch (BadInputException e) {
      // do nothing
    }

    int totalMembers = 0;
    Collection memberBeans = new ArrayList();
    int enabledMembers = 0;
    int pendingMembers = 0;
    int activatedMembers = 0;
    int nonactivatedMembers = 0;
    int disabledMembers = 0;
    if (DAOFactory.getMemberDAO().isSupportGetMembers_withSortSupport_limit()) {
      if (DAOFactory.getMemberDAO().isSupportGetNumberOfMembers()) {
        totalMembers = DAOFactory.getMemberDAO().getNumberOfMembers();
      }
      if (offset > totalMembers) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.offset_greater_than_total_rows");
        throw new BadInputException(localizedMessage);
        // throw new BadInputException("The offset is not allowed to be greater than total rows.");
      }
      memberBeans = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(offset, postsPerPage,
          sort, order, MemberDAO.ALL_MEMBER_STATUS);

      if (DAOFactory.getMemberDAO().isSupportGetNumberOfMembers_inActivationStatus()) {
        activatedMembers = DAOFactory.getMemberDAO().getNumberOfMembers_inActivationStatus(true);
        nonactivatedMembers =
            DAOFactory.getMemberDAO().getNumberOfMembers_inActivationStatus(false);
        if ((activatedMembers + nonactivatedMembers) != totalMembers) {
          // please do not localize this
          throw new AssertionError(
              "Assertion: (activatedMembers + nonactivatedMembers) == totalMembers");
        }
      }

      if (DAOFactory.getMemberDAO().isSupportGetNumberOfMembers_inMemberStatus()) {
        disabledMembers = DAOFactory.getMemberDAO()
            .getNumberOfMembers_inMemberStatus(MemberBean.MEMBER_STATUS_DISABLE);
        enabledMembers = DAOFactory.getMemberDAO()
            .getNumberOfMembers_inMemberStatus(MemberBean.MEMBER_STATUS_ENABLE);
        pendingMembers = DAOFactory.getMemberDAO()
            .getNumberOfMembers_inMemberStatus(MemberBean.MEMBER_STATUS_PENDING);
        if ((disabledMembers + enabledMembers + pendingMembers) != totalMembers) {
          // please do not localize this
          throw new AssertionError(
              "Assertion: (disabledMembers + enabledMembers + pendingMembers) == totalMembers");
        }
      }
    }
    request.setAttribute("MemberBeans", memberBeans);
    request.setAttribute("TotalMembers", new Integer(totalMembers));
    request.setAttribute("EnabledMembers", new Integer(enabledMembers));
    request.setAttribute("DisabledMembers", new Integer(disabledMembers));
    request.setAttribute("PendingMembers", new Integer(pendingMembers));
    request.setAttribute("ActivatedMembers", new Integer(activatedMembers));
    request.setAttribute("NonActivatedMembers", new Integer(nonactivatedMembers));
    request.setAttribute("offset", new Integer(offset));
  }

  public void prepareListUserExpire(GenericRequest request)
      throws DatabaseException, BadInputException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    // for sort and order stuff
    String sort = GenericParamUtil.getParameter(request, "sort");
    String order = GenericParamUtil.getParameter(request, "order");
    if (sort.length() == 0) {
      sort = "MemberExpireDate";
    }
    if (order.length() == 0) {
      order = "ASC";
    }

    // we continue
    int postsPerPage = onlineUser.getPostsPerPage();
    int offset = 0;
    try {
      offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
    } catch (BadInputException e) {
      // do nothing
    }
    Timestamp expiresoonDate =
        DateUtil.getCurrentGMTTimestampExpiredDay(7 /* MVNForumConfig.getExpireSoonDate() */);

    int totalExpireMembers = DAOFactory.getMemberDAO().getNumberOfMembers_inExpire(expiresoonDate);
    if (offset > totalExpireMembers) {
      // @todo : localize me
      throw new BadInputException("The offset is not allowed to be greater than total members.");
    }

    Collection expireMemberBeans = DAOFactory.getMemberDAO()
        .getMembers_inExpire_limit(expiresoonDate, offset, postsPerPage, sort, order);

    request.setAttribute("TotalMembers", new Integer(totalExpireMembers));
    request.setAttribute("ExpireMemberBeans", expireMemberBeans);
    request.setAttribute("offset", new Integer(offset));
  }

  public void prepareListPendingUsers(GenericRequest request)
      throws DatabaseException, BadInputException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // for sort and order stuff
    String sort = GenericParamUtil.getParameter(request, "sort");
    String order = GenericParamUtil.getParameter(request, "order");
    if (sort.length() == 0) {
      sort = "MemberCreationDate";
    }
    if (order.length() == 0) {
      order = "DESC";
    }

    // we continue
    int postsPerPage = onlineUser.getPostsPerPage();
    int offset = 0;
    try {
      offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
    } catch (BadInputException e) {
      // do nothing
    }

    int pendingMembersCount = 0;
    Collection pendingMemberBeans = new ArrayList();

    if (DAOFactory.getMemberDAO().isSupportGetMembers_withSortSupport_limit()
        && DAOFactory.getMemberDAO().isSupportGetNumberOfMembers_inMemberStatus()) {

      pendingMembersCount = DAOFactory.getMemberDAO()
          .getNumberOfMembers_inMemberStatus(MemberBean.MEMBER_STATUS_PENDING);
      if (offset > pendingMembersCount) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.offset_greater_than_total_rows");
        throw new BadInputException(localizedMessage);
        // throw new BadInputException("The offset is not allowed to be greater than total rows.");
      }
      pendingMemberBeans = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(offset,
          postsPerPage, sort, order, MemberBean.MEMBER_STATUS_PENDING);
    }
    request.setAttribute("PendingMemberBeans", pendingMemberBeans);
    request.setAttribute("PendingMembers", new Integer(pendingMembersCount));
    request.setAttribute("offset", new Integer(offset));
  }

  public void processListPendingUsers(GenericRequest request) throws DatabaseException,
      AuthenticationException, BadInputException, DatabaseException, ObjectNotFoundException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    String prefix = "listpendingaction_";
    for (Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements();) {
      String param = (String) enumeration.nextElement();
      if (param.startsWith(prefix)) {
        String actionValue = GenericParamUtil.getParameter(request, param, true);
        String strMemberID = param.substring(prefix.length());
        int memberID = 0;
        MemberBean memberBean = null;
        try {
          memberID = Integer.parseInt(strMemberID);
          memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        } catch (ObjectNotFoundException ex) {
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
              new Object[] {new Integer(memberID)});
          throw new ObjectNotFoundException(localizedMessage);
        } catch (NumberFormatException nfe) {
          String localizedMessage = MVNCoreResourceBundle.getString(locale,
              "mvncore.exception.BadInputException.cannot_parse",
              new Object[] {DisableHtmlTagFilter.filter(strMemberID), "int"});
          throw new BadInputException(localizedMessage);
        }

        if ("enable".equals(actionValue)) {
          DAOFactory.getMemberDAO().updateStatus(memberID, MemberBean.MEMBER_STATUS_ENABLE);
          String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
              "mvnforum.eventlog.desc.EnableMemberProcess", new Object[] {new Integer(memberID)});
          eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
              MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
              "enable pending member", actionDesc, EventLogService.MEDIUM);
        } else if ("delete".equals(actionValue)) {
          MemberWebHandler.deleteMember(memberBean, true, request, locale);
          String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
              "mvnforum.eventlog.desc.DeleteMemberProcess", new Object[] {new Integer(memberID)});
          eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
              MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
              "delete pending member", actionDesc, EventLogService.MEDIUM);
        } else if ("ignore".equals(actionValue)) {
          // we do nothing here
        } else {
          throw new IllegalArgumentException("Cannot accept action = " + actionValue);
        }
      }
    }
    request.setAttribute("Offset", GenericParamUtil.getParameter(request, "offset"));
  }

  public void updateMemberExpireProcess(GenericRequest request) throws DatabaseException,
      BadInputException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    String renew = GenericParamUtil.getParameter(request, "renew");
    String disable = GenericParamUtil.getParameter(request, "disable");
    String enable = GenericParamUtil.getParameter(request, "enable");
    int expiredDays = GenericParamUtil.getParameterUnsignedInt(request, "addtime");

    String[] checkList = request.getParameterValues("selectedmember");
    if (checkList == null) {
      return;
    }

    if ("Renew".equals(renew)) {
      // renew creation date and expire date
      long offsetDate = expiredDays * DateUtil.DAY;
      Timestamp now = DateUtil.getCurrentGMTTimestamp();
      for (int i = 0; (checkList != null) && (i < checkList.length); i++) {
        int memberID = Integer.parseInt(checkList[i]);
        log.debug("Renew expiration date for MemberID = " + memberID);
        try {
          MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
          Timestamp expireDate = memberBean.getMemberExpireDate();
          if (expireDate == null) {
            expireDate = now;
          }
          Timestamp memberExpireDate = new Timestamp(expireDate.getTime() + offsetDate);
          DAOFactory.getMemberDAO().updateMember_expireDate(memberID, memberExpireDate);
        } catch (ObjectNotFoundException e) {
          Locale locale = I18nUtil.getLocaleInRequest(request);
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
              new Object[] {new Integer(memberID)});
          throw new ObjectNotFoundException(localizedMessage);
        }
      }
    } else if ("Disable".equals(disable)) {
      for (int i = 0; (checkList != null) && (i < checkList.length); i++) {
        int memberID = Integer.parseInt(checkList[i]);
        log.debug("Disable member with MemberID = " + memberID);
        try {
          DAOFactory.getMemberDAO().updateStatus(memberID, MemberBean.MEMBER_STATUS_DISABLE);
        } catch (ObjectNotFoundException e) {
          Locale locale = I18nUtil.getLocaleInRequest(request);
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
              new Object[] {new Integer(memberID)});
          throw new ObjectNotFoundException(localizedMessage);
        }
      }
    } else if ("Enable".equals(enable)) {
      for (int i = 0; (checkList != null) && (i < checkList.length); i++) {
        int memberID = Integer.parseInt(checkList[i]);
        log.debug("Enable member with MemberID = " + memberID);
        try {
          DAOFactory.getMemberDAO().updateStatus(memberID, MemberBean.MEMBER_STATUS_ENABLE);
        } catch (ObjectNotFoundException e) {
          Locale locale = I18nUtil.getLocaleInRequest(request);
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
              new Object[] {new Integer(memberID)});
          throw new ObjectNotFoundException(localizedMessage);
        }
      }
    }
  }

  public void processSearch(GenericRequest request) throws BadInputException, IOException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    if (DAOFactory.getMemberDAO().isSupportGetMembersFromIDRange() == false) {
      throw new IllegalArgumentException(
          "Cannot search member because this method is not supported: MemberDAO.isSupportGetMembersFromIDRange()");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanModerateUser();

    String memberNameKey = GenericParamUtil.getParameter(request, "membername", false);
    String memberEmailKey = GenericParamUtil.getParameter(request, "email", false);
    String memberLastNameKey = GenericParamUtil.getParameter(request, "lastname", false);
    String memberFirstNameKey = GenericParamUtil.getParameter(request, "firstname", false);
    String memberCountryKey = GenericParamUtil.getParameter(request, "country", false);

    if ((memberNameKey.length() == 0) && (memberEmailKey.length() == 0)
        && (memberLastNameKey.length() == 0) && (memberFirstNameKey.length() == 0)
        && (memberCountryKey.length() == 0)) {
      return;
    }

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int defaultRows = onlineUser.getPostsPerPage();

    int offset = GenericParamUtil.getParameterUnsignedInt(request, "offset", 0);
    int rows = GenericParamUtil.getParameterUnsignedInt(request, "rows", defaultRows);
    if (rows == 0) {
      rows = defaultRows; // fix NullPointerException when rows = 0
    }

    // offset should be even when divide with rowsToReturn
    offset = (offset / rows) * rows;

    MemberSearchQuery query = new MemberSearchQuery();
    boolean isNullKey = true;
    if (memberNameKey.length() > 0) {
      query.setMemberNameKey(memberNameKey);
      isNullKey = false;
    }
    if (memberEmailKey.length() > 0) {
      query.setMemberEmailKey(memberEmailKey);
      isNullKey = false;
    }
    if (memberLastNameKey.length() > 0) {
      query.setMemberLastNameKey(memberLastNameKey);
      isNullKey = false;
    }
    if (memberFirstNameKey.length() > 0) {
      query.setMemberFirstNameKey(memberFirstNameKey);
      isNullKey = false;
    }
    if (memberCountryKey.length() > 0) {
      query.setCountry(memberCountryKey);
      isNullKey = false;
    }
    if (isNullKey) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_search.key_is_null");
      throw new BadInputException(localizedMessage);
    }

    int searchDate = GenericParamUtil.getParameterUnsignedInt(request, "date",
        MemberSearchQuery.SEARCH_ANY_DATE);
    int searchBeforeAfter =
        GenericParamUtil.getParameterInt(request, "beforeafter", MemberSearchQuery.SEARCH_NEWER);

    if ((searchDate != MemberSearchQuery.SEARCH_ANY_DATE) && (searchDate < 365 * 10)) { // 10 years
      long deltaTime = DateUtil.DAY * searchDate;

      Timestamp now = DateUtil.getCurrentGMTTimestamp();
      Timestamp from = null;
      Timestamp to = null;

      long currentTime = now.getTime();

      if (searchBeforeAfter == MemberSearchQuery.SEARCH_NEWER) {
        from = new Timestamp(currentTime - deltaTime);
      } else { // older
        to = new Timestamp(currentTime - deltaTime);
      }

      query.setFromDate(from);
      query.setToDate(to);
    }

    query.searchDocuments(offset, rows);
    int hitCount = query.getHitCount();
    Collection result = query.getMemberResult();

    if (offset > hitCount) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.offset_greater_than_total_rows");
      throw new BadInputException(localizedMessage);
    }

    request.setAttribute("offset", new Integer(offset));
    request.setAttribute("rows", new Integer(rows));
    request.setAttribute("TotalMembers", new Integer(hitCount));
    request.setAttribute("MemberBeans", result);
    request.setAttribute("MemberName", memberNameKey);
    request.setAttribute("MemberEmail", memberEmailKey);
    request.setAttribute("MemberLastname", memberLastNameKey);
    request.setAttribute("MemberFirstname", memberFirstNameKey);
    request.setAttribute("MemberCountry", memberCountryKey);
    request.setAttribute("DateOption", new Integer(searchDate));
    request.setAttribute("BeforeAfterOption", new Integer(searchBeforeAfter));
  }

  public void preparePermissionSummary(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Collection authorizedMembers = new ArrayList();
    if (DAOFactory.getMemberDAO().isSupportGetAuthorizedMembers()) {
      authorizedMembers = DAOFactory.getMemberDAO().getAuthorizedMembers();
    }
    Collection forumsAuthorizedMembers = new ArrayList();
    if (DAOFactory.getMemberDAO().isSupportGetForumsAuthorizedMembers()) {
      forumsAuthorizedMembers = DAOFactory.getMemberDAO().getForumsAuthorizedMembers();
    }

    Collection distinctAuthorizedGroups = DAOFactory.getGroupPermissionDAO().getDistinctGroups();
    Collection authorizedGroups = new ArrayList(distinctAuthorizedGroups.size());
    for (Iterator iter = distinctAuthorizedGroups.iterator(); iter.hasNext();) {
      GroupPermissionBean item = (GroupPermissionBean) iter.next();
      GroupsBean groupBean = DAOFactory.getGroupsDAO().getGroup(item.getGroupID());
      authorizedGroups.add(groupBean);
    }

    Collection distinctForumAuthorizedGroups = DAOFactory.getGroupForumDAO().getDistinctGroups();
    Collection forumAuthorizedGroups = new ArrayList(distinctForumAuthorizedGroups.size());
    for (Iterator iter = distinctForumAuthorizedGroups.iterator(); iter.hasNext();) {
      GroupForumBean item = (GroupForumBean) iter.next();
      GroupsBean groupBean = DAOFactory.getGroupsDAO().getGroup(item.getGroupID());
      forumAuthorizedGroups.add(groupBean);
    }

    request.setAttribute("AuthorizedMembers", authorizedMembers);
    request.setAttribute("ForumsAuthorizedMembers", forumsAuthorizedMembers);
    request.setAttribute("AuthorizedGroups", authorizedGroups);
    request.setAttribute("ForumAuthorizedGroups", forumAuthorizedGroups);
  }

  public void prepareViewPermission(GenericRequest request) throws AuthenticationException,
      BadInputException, DatabaseException, ObjectNotFoundException {
    // Check here again.
    // SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    MVNForumPermission thisPermission =
        MVNForumPermissionFactory.getAuthenticatedPermission(memberBean);
    Collection groups = DAOFactory.getGroupsDAO().getMyGroups(memberID);

    request.setAttribute("MemberBean", memberBean);
    request.setAttribute("MemberGroups", groups);
    request.setAttribute("Permissions", thisPermission);

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementViewMemberPermissions(request);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
  }

  public void prepareChangePassword(GenericRequest request) throws DatabaseException,
      AuthenticationException, ObjectNotFoundException, BadInputException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableAdminCanChangePassword() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.admin_can_change_password_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Admin Cannot change the user password. This feature is
      // disabled by administrator");
    }
    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    if (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_change_root_password");
      throw new AssertionError(localizedMessage);
    }
    MemberBean memberBean = null;
    try {
      memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    request.setAttribute("MemberBean", memberBean);
  }

  public void processChangePassword(GenericRequest request) throws DatabaseException,
      AuthenticationException, ObjectNotFoundException, BadInputException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableAdminCanChangePassword() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.admin_can_change_password_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Admin Cannot change the user password. This feature is
      // disabled by administrator");
    }

    int memberID = GenericParamUtil.getParameterInt(request, "memberid");

    if (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_change_root_password");
      throw new AssertionError(localizedMessage);
    }

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    String password = GenericParamUtil.getParameterPassword(request, "MemberMatkhau", 3, 0);
    String confirmword =
        GenericParamUtil.getParameterPassword(request, "MemberMatkhauConfirm", 3, 0);
    if (password.equals(confirmword) == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.confirmed_password_is_not_match");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Password and confirmed password are not the same, please try
      // again.");
    }
    password = Encoder.getMD5_Base64(password);

    Timestamp passwordExpireDate = null;
    if (MVNForumConfig.getMaxPasswordDays() > 0) {
      passwordExpireDate =
          DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
    }
    try {
      DAOFactory.getMemberDAO().updatePassword(memberID, password, passwordExpireDate);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.ChangePasswordProcess");
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "change password", actionDesc, EventLogService.MEDIUM);

    request.setAttribute("MemberID", new Integer(memberID));
  }

  public void prepareDeleteNonActivatedNoPostMembers(GenericRequest request)
      throws DatabaseException, AuthenticationException, BadInputException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    int days = GenericParamUtil.getParameterUnsignedInt(request, "days");

    long delta = days * DateUtil.DAY;
    Timestamp before = new Timestamp(DateUtil.getCurrentGMTTimestamp().getTime() - delta);

    Collection members = DAOFactory.getMemberDAO().getNonActivatedNoPostMembers(before);

    request.setAttribute("DeleteMembers", members);
  }

  public void processDeleteNonActivatedNoPostMembers(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, BadInputException,
      AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAdminSystem();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    int days = GenericParamUtil.getParameterUnsignedInt(request, "days");

    boolean deleteMessages = GenericParamUtil.getParameterBoolean(request, "deletesentmessages");

    long delta = days * DateUtil.DAY;
    Timestamp before = new Timestamp(DateUtil.getCurrentGMTTimestamp().getTime() - delta);

    Collection members = DAOFactory.getMemberDAO().getNonActivatedNoPostMembers(before);

    int deletedMembersCount = 0;
    for (Iterator iterator = members.iterator(); iterator.hasNext();) {
      MemberBean member = (MemberBean) iterator.next();
      if ((member.getMemberID() != 0) && (MyUtil.isRootAdminID(member.getMemberID()) == false)) {
        MemberWebHandler.deleteMember(member, deleteMessages, request, locale);
        deletedMembersCount++;
      }
    }
    log.info("DeleteNonActivatedNoPostMembers: Deleted " + deletedMembersCount + " members");

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.DeleteNonActivatedMembersProces", new Object[] {members});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "delete nonactivated members", actionDesc, EventLogService.HIGH);

    request.setAttribute("DeletedMembersCount", new Integer(deletedMembersCount));
  }

  public static void deleteMember(MemberBean memberBean, boolean deleteSentMessages,
      GenericRequest request, Locale locale)
      throws DatabaseException, BadInputException, ObjectNotFoundException {

    int memberID = memberBean.getMemberID();
    if ((memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) || (memberID == 0)
        || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_delete_admin_or_guest");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot delete default Admin and Guest users.");
    }

    // check in the MemberBean
    // Real checking in table post
    if ((memberBean.getMemberPostCount() > 0)
        || (DAOFactory.getPostDAO().getNumberOfPosts_inMember(memberID) > 0)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_delete_member_has_a_post");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot delete member that has posted at least one post.");
    }

    // first, we delete uploaded image if there is one
    String memberName = memberBean.getMemberName();

    log.info("About to delete member with login = " + memberName + " and id = " + memberID
        + " with option deleteSentMessages = " + deleteSentMessages);

    // Try to delete in related table
    boolean shouldDelete = MvnForumServiceFactory.getMvnForumService().getMvnForumDatabaseService()
        .deleteMember(memberBean, request);
    if (shouldDelete == false) {
      return;
    }

    StringBuffer bufferPicFile = new StringBuffer(256);
    bufferPicFile.append(request.getRealPath(MVNForumGlobal.UPLOADED_AVATAR_DIR));
    bufferPicFile.append(File.separatorChar).append(memberName).append(".jpg");
    String picFile = bufferPicFile.toString();

    log.debug("Delete avatar = " + picFile);
    File file = new File(picFile);
    file.delete(); // we don't need to check the returned value

    // @todo delete table Post/Thread/Attachment if we support delete posted user
    DAOFactory.getWatchDAO().delete_inMember(memberID);
    DAOFactory.getMemberForumDAO().delete_inMember(memberID);
    DAOFactory.getMemberGroupDAO().delete_inMember(memberID);
    DAOFactory.getMemberPermissionDAO().delete_inMember(memberID);
    DAOFactory.getFavoriteThreadDAO().delete_inMember(memberID);

    // Delete message, attachmessage, folder, message statistics stuff
    PrivateMessageUtil.deleteMessageFoldersInDatabase(memberID);
    DAOFactory.getMessageStatisticsDAO().delete_inMember(memberID);
    if (deleteSentMessages) {
      DAOFactory.getMessageDAO().deleteSenderMessages(memberID);
    }

    // reset ForumOwnerName before delete member
    DAOFactory.getForumDAO().resetForumOwnerNameWhenDeleteMember(memberName);

    // Delete in primary table
    DAOFactory.getMemberDAO().deleteByPrimaryKey(memberID);
    MemberIndexer.scheduleDeleteMemberTask(memberID);

    MemberCache.getInstance().clear();
  }
}
