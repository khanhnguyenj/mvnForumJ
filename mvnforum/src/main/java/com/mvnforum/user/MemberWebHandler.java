/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/MemberWebHandler.java,v 1.233
 * 2010/06/17 10:25:59 minhnn Exp $ $Author: minhnn $ $Revision: 1.233 $ $Date: 2010/06/17 10:25:59
 * $
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
package com.mvnforum.user;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserImpl;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.common.MemberAvatarTimeTracking;
import com.mvnforum.common.MemberMapping;
import com.mvnforum.common.MemberUtil;
import com.mvnforum.common.OnlineUserUtil;
import com.mvnforum.common.SendMailUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import com.mvnforum.db.MemberDAO;
import com.mvnforum.search.member.MemberIndexer;
import com.mvnforum.service.MvnForumInfoService;
import com.mvnforum.service.MvnForumServiceFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.FloodException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.RandomGenerator;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.BinaryStorageInfo;
import net.myvietnam.mvncore.service.BinaryStorageService;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.FileUploadParserService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.FileUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ImageUtil;
import net.myvietnam.mvncore.util.MailMessageStruct;
import net.myvietnam.mvncore.util.MailUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class MemberWebHandler {

  private static final Logger log = LoggerFactory.getLogger(MemberWebHandler.class);

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  private static MvnForumInfoService mvnForumInfo =
      MvnForumServiceFactory.getMvnForumService().getMvnForumInfoService();

  private static EventLogService eventLogService =
      MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

  private BinaryStorageService binaryStorageService =
      MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();

  private FileUploadParserService fileUploadParserService =
      MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();

  public MemberWebHandler() {}

  public void prepareAdd(GenericRequest request) throws DatabaseException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableExternalUserDatabase()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.create_user_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create user if we enable external user database.");
    }

    if (MVNForumConfig.getEnableNewMember() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.cannot_register.new_member_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot register new member because NEW_MEMBER feature is
      // disabled by administrator.");
    }

    boolean agree = GenericParamUtil.getParameterBoolean(request, "agree");
    if (agree) {
      // we need to check this to make sure user not use link: /mvnforum/registermember?agree=true
      SecurityUtil.checkHttpPostMethod(request);
    }

    if ((MVNForumConfig.getEnableRegisterRule()) && (agree == false)) {
      // repare to load file rule_xx.html
      String fileNameDefault = "rule_en.html";
      String fileNameLocale = "rule_" + locale.getLanguage() + ".html";
      String absolutePath = request.getRealPath("/mvnplugin/mvnforum/" + fileNameLocale);
      String relativePath = request.getContextPath() + "/mvnplugin/mvnforum/";
      File file = new File(absolutePath);
      // detect if file rule_xx.html does not exist, it will load file rule_en.html
      if (file.exists()) {
        fileNameDefault = fileNameLocale;
      }
      request.setAttribute("RuleFile", relativePath + fileNameDefault);
      return;
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    if (onlineUser.isMember()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.cannot_register.new_member_after_login",
          new Object[] {onlineUser.getMemberName()});
      throw new IllegalStateException(localizedMessage);
    }

    if (MVNForumConfig.getEnableCaptcha()) {
      // TODO check captcha
    }
  }

  public void processAdd(GenericRequest request, GenericResponse response)
      throws BadInputException, ObjectNotFoundException, CreateException, DatabaseException,
      InterceptorException, DuplicateKeyException, ForeignKeyNotFoundException, FloodException,
      DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableExternalUserDatabase()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.create_user_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create user if we enable external user database.");
    }

    if (MVNForumConfig.getEnableNewMember() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.cannot_register.new_member_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot register new member because NEW_MEMBER feature is
      // disabled by administrator.");
    }

    // use for the captcha feature
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);

    MyUtil.saveVNTyperMode(request, response);

    String currentIP = request.getRemoteAddr();
    try {
      FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_NEW_MEMBER_PER_IP, currentIP);
    } catch (FloodException fe) {
      // throw new FloodException("You have reached the maximum number of the registering actions
      // for this page. Please try this page later. This is to prevent forum from being flooded.");
      Integer maxRegisters = new Integer(
          FloodControlHour.getActionsPerHour(MVNForumGlobal.FLOOD_ID_NEW_MEMBER_PER_IP));
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.FloodException.register_too_many_times", new Object[] {maxRegisters});
      throw new FloodException(localizedMessage);
    }
    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    String memberName = GenericParamUtil.getParameterSafe(request, "MemberName", true);// check good
                                                                                       // name
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
    String memberFirstEmail = memberEmail;
    if (memberEmail.length() > MVNForumGlobal.MAX_MEMBER_EMAIL_LENGTH) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.member_email_too_long");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("MemberEmail cannot be longer than 60 characters.");
    }
    InterceptorService.getInstance().validateMail(memberFirstEmail);

    int memberEmailVisible = GenericParamUtil.getParameterBoolean(request, "MemberEmailVisible")
        ? MemberBean.MEMBER_EMAIL_VISIBLE
        : MemberBean.MEMBER_EMAIL_INVISIBLE;
    int memberNameVisible = GenericParamUtil.getParameterBoolean(request, "MemberNameVisible")
        ? MemberBean.MEMBER_NAME_VISIBLE
        : MemberBean.MEMBER_NAME_INVISIBLE;
    String memberFirstIP = currentIP;
    String memberLastIP = currentIP;
    Timestamp memberCreationDate = now;
    Timestamp memberModifiedDate = now;
    Timestamp memberLastLogon = now;// @todo review and support it later
    Timestamp memberPasswordExpireDate = now;
    if (MVNForumConfig.getMaxPasswordDays() > 0) {
      memberPasswordExpireDate =
          DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
    }
    int memberOption = 0;// @todo review and support it later
    int memberStatus = MVNForumConfig.getDefaultStatusOfRegisteredMember();// @todo review and
                                                                           // support it later, ex:
                                                                           // should it be active or
                                                                           // not?
    String memberActivateCode = "";// not activated
    int memberMessageOption = 0;// @todo review and support it later
    int memberPostsPerPage = GenericParamUtil.getParameterInt(request, "MemberPostsPerPage", 10);
    if (memberPostsPerPage < 5) {
      memberPostsPerPage = 5;
    }
    String memberTitle = "";
    double memberTimeZone = 0;
    String memberSkin = "";

    // validate data for required fields.

    String memberFirstname = "";
    if (MVNForumConfig.getEnableShowFirstName()) {
      memberFirstname = GenericParamUtil.getParameterFilter(request, "MemberFirstname",
          MVNForumConfig.isRequireRegisterFirstname());
    }
    String memberLastname = "";
    if (MVNForumConfig.getEnableShowLastName()) {
      memberLastname = GenericParamUtil.getParameterFilter(request, "MemberLastname",
          MVNForumConfig.isRequireRegisterLastname());
    }
    String memberAddress = "";
    if (MVNForumConfig.getEnableShowAddress()) {
      memberAddress = GenericParamUtil.getParameterFilter(request, "MemberAddress",
          MVNForumConfig.isRequireRegisterAddress());
    }
    String memberCity = "";
    if (MVNForumConfig.getEnableShowCity()) {
      memberCity = GenericParamUtil.getParameterFilter(request, "MemberCity",
          MVNForumConfig.isRequireRegisterCity());
    }
    String memberState = "";
    if (MVNForumConfig.getEnableShowState()) {
      memberState = GenericParamUtil.getParameterFilter(request, "MemberState",
          MVNForumConfig.isRequireRegisterState());
    }
    String memberCountry = "";
    if (MVNForumConfig.getEnableShowCountry()) {
      memberCountry = GenericParamUtil.getParameterFilter(request, "MemberCountry",
          MVNForumConfig.isRequireRegisterCountry());
    }
    String memberPhone = "";
    if (MVNForumConfig.getEnableShowPhone()) {
      memberPhone = GenericParamUtil.getParameterFilter(request, "MemberPhone",
          MVNForumConfig.isRequireRegisterPhone());
    }
    String memberMobile = "";
    if (MVNForumConfig.getEnableShowMobile()) {
      memberMobile = GenericParamUtil.getParameterFilter(request, "MemberMobile",
          MVNForumConfig.isRequireRegisterMobile());
    }
    String memberFax = "";
    if (MVNForumConfig.getEnableShowFax()) {
      memberFax = GenericParamUtil.getParameterFilter(request, "MemberFax",
          MVNForumConfig.isRequireRegisterFax());
    }
    String memberCareer = "";
    if (MVNForumConfig.getEnableShowCareer()) {
      memberCareer = GenericParamUtil.getParameterFilter(request, "MemberCareer",
          MVNForumConfig.isRequireRegisterCareer());
    }

    String memberLanguage = GenericParamUtil.getParameterFilter(request, "MemberLanguage", false);

    int memberGender = 0;
    if (MVNForumConfig.getEnableShowGender()) {
      memberGender = Integer.parseInt(GenericParamUtil.getParameterSafe(request, "MemberGender",
          MVNForumConfig.isRequireRegisterGender()));
      if (memberGender != 0 && memberGender != 1) {
        memberGender = 0;
      }
    }

    String memberHomepage = "";
    if (MVNForumConfig.getEnableShowHomepage()) {
      memberHomepage = GenericParamUtil.getParameter(request, "MemberHomepage");
      if (MVNForumConfig.isRequireRegisterHomepage() || memberHomepage.length() > 0) {
        memberHomepage = GenericParamUtil.getParameterUrl(request, "MemberHomepage");
      }
    }
    Date memberBirthday = MemberBean.MEMBER_NOT_REQUIRE_BIRTHDAY;
    if (MVNForumConfig.getEnableShowBirthday()) {
      String day = GenericParamUtil.getParameter(request, "day");
      String month = GenericParamUtil.getParameter(request, "month");
      String year = GenericParamUtil.getParameter(request, "year");
      if (MVNForumConfig.isRequireRegisterBirthday()
          || (day.length() > 0 || month.length() > 0 || year.length() > 0)) {
        memberBirthday = GenericParamUtil.getParameterDate(request, "day", "month", "year");
      }
    }

    if (MVNForumConfig.getEnableCaptcha()) {
      String captchaResponse =
          GenericParamUtil.getParameterSafe(request, "g-recaptcha-response", true);
      onlineUser.ensureCorrectCaptchaResponse(captchaResponse);
    }
    Timestamp memberExpireDate = memberCreationDate;// equal Creation Date mean no expiration
    try {
      DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(memberEmail);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.email_exists",
          new Object[] {memberName, memberEmail});
      throw new DuplicateKeyException(localizedMessage);
    } catch (ObjectNotFoundException ex) {

    }
    try {
      DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.account_exists", new Object[] {memberName});
      throw new DuplicateKeyException(localizedMessage);
    } catch (ObjectNotFoundException ex) {

    }
    DAOFactory.getMemberDAO().create(memberName, memberPassword, memberFirstEmail, memberEmail,
        memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, 0/* memberViewCount */,
        0/* memberPostCount */, memberCreationDate, memberModifiedDate, memberExpireDate,
        memberPasswordExpireDate, memberLastLogon, memberOption, memberStatus, memberActivateCode,
        ""/* memberTempPassword */, 0/* memberMessageCount */, memberMessageOption,
        memberPostsPerPage, 0/* memberWarnCount */, 0/* memberVoteCount */,
        0/* memberVoteTotalStars */, 0/* memberRewardPoints */, memberTitle, memberTimeZone,
        ""/* memberSignature */, ""/* memberAvatar */, memberSkin, memberLanguage, memberFirstname,
        memberLastname, memberGender, memberBirthday, memberAddress, memberCity, memberState,
        memberCountry, memberPhone, memberMobile, memberFax, memberCareer, memberHomepage);
    // Now, create 4 default folders for each member

    int memberID = 0;
    try {
      // NOTE: please note that we cannot get it from the MemberCache, because we have
      // bug that delete member, then immediately create this member again, it will
      // get the wrong MemberID so we cannot create a new MessageFolder
      // memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);

      memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.membername_not_exists",
          new Object[] {memberName});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int folderStatus = 0;
    int folderOption = 0;
    int folderType = 0;
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, memberID,
        0/* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, memberID,
        1/* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, memberID,
        2/* order */, folderStatus, folderOption, folderType, now, now);
    DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, memberID,
        3/* order */, folderStatus, folderOption, folderType, now, now);

    FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_NEW_MEMBER_PER_IP, currentIP);

    // Add member to the Lucene index
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

    request.setAttribute("MemberBean", memberBean);

    // now, if require activation, then we will send mail
    if (MVNForumConfig.getRequireActivation()) {
      String serverName = ParamUtil.getServerPath();// ParamUtil.getServer2(request);
      try {
        SendMailUtil.sendActivationCodeEmail(memberID, serverName);
      } catch (Exception ex) {
        log.error("Cannot send mail after registration!", ex);
        request.setAttribute("mvnforum.mail.failed",
            "Cannot send activation email after registration!");
        // @todo: save the error message to displayed later
      }
    }

  }

  public void processUpdate(GenericRequest request, GenericResponse response)
      throws BadInputException, ObjectNotFoundException, DatabaseException,
      AuthenticationException {

    // AssertionUtil.doAssert(MVNForumConfig.getEnableExternalUserDatabase() == false, "Cannot
    // update user if we enable external user database.");

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    MyUtil.saveVNTyperMode(request, response);

    int memberID = onlineUser.getMemberID();
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    MemberMapping mapping = MemberMapping.getInstance();
    boolean internalUserDatabase = !MVNForumConfig.getEnableExternalUserDatabase();

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    int memberEmailVisible = memberBean.getMemberEmailVisible();
    int memberNameVisible = memberBean.getMemberNameVisible();
    int memberOption = 0;// GenericParamUtil.getParameterInt(request, "MemberOption");
    int memberStatus = 0;// @todo review and support it later
    int memberMessageOption = 0;// GenericParamUtil.getParameterInt(request, "MemberMessageOption");
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

    DAOFactory.getMemberDAO().update(memberID, // primary key
        memberEmailVisible, memberNameVisible, now/* memberModifiedDate */, memberOption,
        memberStatus, memberMessageOption, memberPostsPerPage, memberTimeZone, memberSkin,
        memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday,
        memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile, memberFax,
        memberCareer, memberHomepage);

    // now, update the new displayed language option
    onlineUser.reloadProfile(request, response);
    MemberBean justAddedMemberBean = null;
    try {
      justAddedMemberBean = DAOFactory.getMemberDAO().getMember(memberID);
    } catch (ObjectNotFoundException ex) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    MemberIndexer.scheduleUpdateMemberTask(justAddedMemberBean);
    MemberCache.getInstance().removeMember(memberID);

  }

  public void processGuestSetting(GenericRequest request, GenericResponse response)
      throws BadInputException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUserImpl onlineUserImpl = (OnlineUserImpl) onlineUserManager.getOnlineUser(request);
    if (onlineUserImpl.isMember()) {
      return;
    }

    final int COOKIE_VERSION = 1;
    double timeZone = GenericParamUtil.getParameterDouble(request, "GuestTimeZone");
    int postsPerPage = GenericParamUtil.getParameterInt(request, "GuestPostsPerPage");
    boolean saveCookie = GenericParamUtil.getParameterBoolean(request, "SaveCookie");
    String localeName = GenericParamUtil.getParameterSafe(request, "GuestLanguage", false);

    if (MVNForumConfig.supportLocale(localeName) == false) {
      localeName = MVNForumConfig.getDefaultLocaleName();
    }

    onlineUserImpl.setLocaleName(localeName, request, response);
    onlineUserImpl.setTimeZone(timeZone);
    onlineUserImpl.setPostsPerPage(postsPerPage);

    request.setAttribute("SettingSuccsess", "true");

    if (saveCookie) {
      int guestCookiesTimeOut = (60 * 60 * 24) * 30; // 30 day
      String sTimeZone = Double.toString(timeZone);
      String sPostsPerPage = Integer.toString(postsPerPage);

      Cookie guestLanguageCK =
          new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_LANGUAGE, localeName);
      guestLanguageCK.setVersion(COOKIE_VERSION);
      guestLanguageCK.setMaxAge(guestCookiesTimeOut);
      guestLanguageCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      Cookie guestTimeZoneCK =
          new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_TIMEZONE, sTimeZone);
      guestTimeZoneCK.setVersion(COOKIE_VERSION);
      guestTimeZoneCK.setMaxAge(guestCookiesTimeOut);
      guestTimeZoneCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      Cookie guestPostsPerPageCK =
          new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_POSTS_PER_PAGE, sPostsPerPage);
      guestPostsPerPageCK.setVersion(COOKIE_VERSION);
      guestPostsPerPageCK.setMaxAge(guestCookiesTimeOut);
      guestPostsPerPageCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      response.addCookie(guestLanguageCK);
      response.addCookie(guestTimeZoneCK);
      response.addCookie(guestPostsPerPageCK);
    } else {
      Cookie guestLanguageCK = new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_LANGUAGE, "");
      guestLanguageCK.setVersion(COOKIE_VERSION);
      guestLanguageCK.setMaxAge(0);// delete this cookie
      guestLanguageCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      Cookie guestTimeZoneCK = new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_TIMEZONE, "");
      guestTimeZoneCK.setVersion(COOKIE_VERSION);
      guestTimeZoneCK.setMaxAge(0);// delete this cookie
      guestTimeZoneCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      Cookie guestPostsPerPageCK =
          new Cookie(MVNForumConstant.MVNFORUM_COOKIE_GUEST_POSTS_PER_PAGE, "");
      guestPostsPerPageCK.setVersion(COOKIE_VERSION);
      guestPostsPerPageCK.setMaxAge(0);// delete this cookie
      guestPostsPerPageCK.setPath(OnlineUserManager.MVNFORUM_COOKIE_PATH);

      response.addCookie(guestLanguageCK);
      response.addCookie(guestTimeZoneCK);
      response.addCookie(guestPostsPerPageCK);
    }
  }

  /*
   * @todo: use new method of WebHelper
   */
  public void prepareEditEmail(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    int memberID = onlineUser.getMemberID();
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    request.setAttribute("MemberEmail", memberBean.getMemberEmail());
  }

  public void processUpdateEmail(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, InterceptorException, DuplicateKeyException,
      AuthenticationException, MessagingException, IOException, TemplateException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    int memberID = onlineUser.getMemberID();

    // column(s) to update
    String memberEmail = GenericParamUtil.getParameterEmail(request, "MemberEmail");
    if (memberEmail.length() > MVNForumGlobal.MAX_MEMBER_EMAIL_LENGTH) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.member_email_too_long");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("MemberEmail cannot be longer than 60 characters.");
    }
    InterceptorService.getInstance().validateMail(memberEmail);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    // invalidate the activate status
    DAOFactory.getMemberDAO().updateActivateCode(memberID, "");

    DAOFactory.getMemberDAO().updateEmail(memberID, memberEmail);

    // now reload the permission if this online user change email (not activated now)
    onlineUser.reloadPermission();

    // now, if require activation, then we will send mail
    if (MVNForumConfig.getRequireActivation()) {
      String serverName = ParamUtil.getServerPath();// ParamUtil.getServer2(request);
      SendMailUtil.sendActivationCodeEmail(memberID, serverName);
    }
  }

  public void processUpdatePassword(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    int memberID = onlineUser.getMemberID();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);
    /*
     * // NOTE: that we don't use getParameterPassword here since it will not forward-compatible
     * String memberOldPassword = GenericParamUtil.getParameter(request, "MemberOldMatkhau", true);
     * String oldEncodedPassword = Encoder.getMD5_Base64(memberOldPassword); String currentPassword
     * = DAOFactory.getMemberDAO().getPassword(memberID); if
     * (!currentPassword.equals(oldEncodedPassword)) { String localizedMessage =
     * MVNForumResourceBundle.getString(locale,
     * "mvncore.exception.BadInputException.wrong_password"); throw new
     * BadInputException(localizedMessage); //throw new
     * BadInputException("You have typed the wrong current password, please try again."); }
     */

    // column(s) to update
    String memberPassword1 = GenericParamUtil.getParameterPassword(request, "md5pwnew", 3, 0);
    String memberPassword2 = GenericParamUtil.getParameterPassword(request, "md5pwconfirm", 3, 0);
    if (!memberPassword1.equals(memberPassword2)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.confirmed_password_is_not_match");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Password and confirmed password are not the same, please try
      // again.");
    }
    // String memberPassword = Encoder.getMD5_Base64(memberPassword1);
    String memberPassword = memberPassword1;
    if ((memberPassword1.length() == 0) || (memberPassword1.endsWith("==") == false)) {
      memberPassword = Encoder
          .getMD5_Base64(GenericParamUtil.getParameterPassword(request, "MemberMatkhau", 3, 0));
    }

    String currentPassword = DAOFactory.getMemberDAO().getPassword(memberID);
    if (currentPassword.equals(memberPassword)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.old_password_and_new_password_cannot_equal");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Old password and new password cannot equal, please try
      // again.");
    }

    Timestamp passwordExpireDate = null;
    if (MVNForumConfig.getMaxPasswordDays() > 0) {
      passwordExpireDate =
          DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
    }
    DAOFactory.getMemberDAO().updatePassword(memberID, // primary key
        memberPassword, passwordExpireDate);

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.user.action.desc.ChangePasswordProcess");
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER,
        "change password", actionDesc, EventLogService.MEDIUM);
  }

  public void prepareView_forCurrentMember(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    // always update the number of new private message count in this case
    onlineUser.updateNewMessageCount(true);

    int memberID = onlineUser.getMemberID();
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    request.setAttribute("MemberBean", memberBean);
  }

  public void prepareEdit_forCurrentMember(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    int memberID = onlineUser.getMemberID();
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    request.setAttribute("MemberBean", memberBean);
  }

  /*
   * @todo: use new method of WebHelper
   */
  public void prepareEditSignature(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");
    if (isPreviewing) {
      String signature = GenericParamUtil.getParameter(request, "MemberSignature");
      if (signature.length() > 250) {
        signature = signature.substring(0, 250);// ensure no more than 250 char (should check in
                                                // javascript ??)
      }
      request.setAttribute("MemberSignature", signature);
    } else {
      int memberID = onlineUser.getMemberID();
      MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
      request.setAttribute("MemberSignature", memberBean.getMemberSignature());
    }
  }

  public void processUpdateSignature(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, DatabaseException, AuthenticationException,
      InterceptorException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    MyUtil.saveVNTyperMode(request, response);

    int memberID = onlineUser.getMemberID();

    // column(s) to update
    String memberSignature = GenericParamUtil.getParameter(request, "MemberSignature");
    memberSignature = DisableHtmlTagFilter.filter(memberSignature);

    memberSignature = InterceptorService.getInstance().validateContent(memberSignature);

    DAOFactory.getMemberDAO().updateSignature(memberID, // primary key
        memberSignature);

    // clear the member cache
    MemberCache.getInstance().clear();
  }

  public void prepareEditAvatar(GenericRequest request)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableAvatar() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.avatar_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot use avatar because AVATAR feature is disabled by
      // administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseAvatar();

    int memberID = onlineUser.getMemberID();
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    request.setAttribute("MemberBean", memberBean);
  }

  /**
   * Change picture from our predefined picture NOTE: this method will delete uploaded image (if
   * any) of the member
   */
  public void processUpdateMemberAvatar(GenericRequest request)
      throws ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableAvatar() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.avatar_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot use avatar because AVATAR feature is disabled by
      // administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseAvatar();

    int memberID = onlineUser.getMemberID();

    try {
      binaryStorageService.deleteData(BinaryStorageService.CATEGORY_AVATAR,
          String.valueOf(memberID), null);
    } catch (IOException e) {
      log.error("Cannot delete avatar.", e);
    }

    // then we update the database with new one
    String memberPicture = GenericParamUtil.getParameterFilter(request, "MemberAvatar");
    DAOFactory.getMemberDAO().updateAvatar(memberID, memberPicture);

    // clear the member cache
    MemberCache.getInstance().clear();
  }

  /**
   * Upload user own avatar
   */
  public void processUploadAvatar(ServletConfig config, GenericRequest request)
      throws AuthenticationException, IOException, DatabaseException {

    SecurityUtil.checkHttpPostMethod(request, true, true);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableAvatar() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.avatar_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot use avatar because AVATAR feature is disabled by
      // administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();
    permission.ensureCanUseAvatar();

    URLResolverService urlResolverService =
        MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    int memberID = onlineUser.getMemberID();
    // String memberName = onlineUser.getMemberName();

    int sizeMax = 60000; // 60KB
    int sizeThreshold = 100000;// max memory used = 100K (more than needed)

    List fileItems;
    try {
      fileItems =
          fileUploadParserService.parseRequest(request, sizeMax, sizeThreshold, null, "UTF-8");
    } catch (FileUploadException ex) {
      log.error("Cannot upload", ex);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.io.IOException.cannot_upload", new Object[] {ex.getMessage()});
      throw new IOException(localizedMessage);
      // throw new IOException("Cannot upload. Detailed reason: " + ex.getMessage());
    }

    // make sure only one file upload
    int fileUploadCount = 0;
    FileItem myFile = null;
    String receivedToken = null;
    for (int i = 0; i < fileItems.size(); i++) {
      FileItem currentFileItem = (FileItem) fileItems.get(i);
      String fieldName = currentFileItem.getFieldName();
      // myFile = (FileItem)fileItems.get(i);
      if (fieldName.equals("submitbutton")
          || fieldName.equals(urlResolverService.getActionParam())) {
        // ignore
      } else if (fieldName.equals("PictureFile")) {
        if (currentFileItem.isFormField()) {
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "java.lang.AssertionError.cannot_process_uploaded_attach_file_with_form_field");
          throw new AssertionError(localizedMessage);
          // throw new AssertionError("Cannot process uploaded attach file with a form field.");
        }
        myFile = currentFileItem;
        fileUploadCount++;
      } else if (fieldName.startsWith(MVNCoreGlobal.MVNCORE_SECURITY_TOKEN)) {
        receivedToken = currentFileItem.getString("utf-8");
        log.debug("receivedToken = " + receivedToken);
      } else {
        // maybe, we don't care about the redundant fields.
        // Should we uncomment the exception statement ?
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.AssertionError.cannot_process_field_name", new Object[] {fieldName});
        throw new AssertionError(localizedMessage);
        // throw new AssertionError("Cannot process field name = " + fieldName);
      }

      AssertionUtil.doAssert(fileUploadCount <= 1,
          "Assertion: Cannot upload more than 1 file while processing upload avatar for Member.");
    }

    SecurityUtil.checkSecurityTokenMethod(request, receivedToken);

    if ((myFile == null) || myFile.isFormField()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.AssertionError.cannot_process_upload_avatar_with_form_field");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Cannot process uploaded avatar with a form field.");
    }

    // now everything all right, go ahead and create thumbnail
    InputStream is = myFile.getInputStream();
    ByteArrayOutputStream os = new ByteArrayOutputStream(60000);
    ImageUtil.createThumbnail(is, os, MVNForumGlobal.UPLOADED_MEMBER_AVATAR_MAX_WIDTH,
        MVNForumGlobal.UPLOADED_MEMBER_AVATAR_MAX_HEIGHT, locale);
    byte[] imageBinary = os.toByteArray();

    String binaryMimeType = "image/jpeg";// because method ImageUtil.createThumbnail create
                                         // thumbnail of jpeg
    int binaryFileSize = imageBinary.length; // (int)myFile.getSize();
    String fullFilePath = myFile.getName();
    String binaryFilename = FileUtil.getFileName(fullFilePath);
    binaryFilename = DisableHtmlTagFilter.filter(binaryFilename);
    String binaryCreationIP = request.getRemoteAddr();

    binaryStorageService.storeData(BinaryStorageService.CATEGORY_AVATAR, String.valueOf(memberID),
        binaryFilename, imageBinary, binaryFileSize, 0, 0, binaryMimeType, binaryCreationIP);

    // track the time of avatar to prevent cache in web proxy
    MemberAvatarTimeTracking.getInstance().putTimeTracking(memberID, System.currentTimeMillis());

    // clear the member cache
    MemberCache.getInstance().clear();
  }

  public void prepareForgotPassword(GenericRequest request)
      throws DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    if (MVNForumConfig.getEnableCaptcha()) {
      // TODO: add captcha
    }
  }

  public void forgotPassword(GenericRequest request)
      throws BadInputException, ObjectNotFoundException, DatabaseException, MessagingException,
      DatabaseException, AuthenticationException, IOException, TemplateException {

    SecurityUtil.checkHttpPostMethod(request);

    // use for the captcha feature
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    Locale locale = I18nUtil.getLocaleInRequest(request);

    int memberID = 0;
    String memberName = GenericParamUtil.getParameter(request, "MemberName");
    StringUtil.checkGoodName(memberName, locale);
    String memberEmail = GenericParamUtil.getParameter(request, "MemberEmail");
    if (memberEmail.length() > 0) {
      memberEmail = GenericParamUtil.getParameterEmail(request, "MemberEmail");
    }

    if (memberName.length() > 0) {// user enter his MemberName
      // we find the email of this memberID, not the provided email
      try {
        memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {memberName});
        throw new ObjectNotFoundException(localizedMessage);
      }
      MemberBean bean = DAOFactory.getMemberDAO().getMember(memberID);
      memberEmail = bean.getMemberEmail();
    } else if (memberEmail.length() > 0) {// user enter his email
      // we find the MemberID of this mail, now we sure that user didnt enter his MemberID
      memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberEmail(memberEmail);
      MemberBean bean = DAOFactory.getMemberDAO().getMember(memberID);
      memberName = bean.getMemberName();
    } else {// user didnt enter any thing
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.your_member_name_or_email_is_not_entered");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("You must enter at least your MemberName or email");
    }

    // now we have the correct pair of MemberID and MemberEmail

    // Check the assumption above
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    if (!memberEmail.equalsIgnoreCase(memberBean.getMemberEmail())) {
      String localizedMessage =
          MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.serious_bug");
      throw new AssertionError(localizedMessage);
      // throw new AssertionError("Assertion when process forgot password. This is a serious bug.
      // Please contact the Web site administrator to report the bug.");
    }
    // end check

    // Now check the captcha
    if (MVNForumConfig.getEnableCaptcha()) {
      String captchaResponse = GenericParamUtil.getParameterSafe(request, "CaptchaResponse", false);
      onlineUser.ensureCorrectCaptchaResponse(captchaResponse);
    }

    String currentTempPassword = DAOFactory.getMemberDAO().getTempPassword(memberID);

    // if the current value length is less then 5, we assume that it is not set
    // and we generate the new value only in this case. This will prevent the
    // different values are sent out and confuse user.
    if (currentTempPassword.length() < 5) {
      // generate a temp password
      currentTempPassword = RandomGenerator.getRandomMD5_Base64();
      DAOFactory.getMemberDAO().updateTempPassword(memberID, currentTempPassword);
    }

    // next, encode to make sure it could be put on a link
    String urlEncodedTempPassword = URLEncoder.encode(currentTempPassword, "UTF-8");

    // we have pass the assertion check, go ahead
    String serverName = ParamUtil.getServerPath();// ParamUtil.getServer2(request);

    StringBuffer passwordResetUrl = new StringBuffer(256);
    passwordResetUrl.append(serverName);
    passwordResetUrl.append(request.getContextPath());
    passwordResetUrl.append(UserModuleConfig.getUrlPattern());
    passwordResetUrl.append("/resetpassword?temppassword=");
    passwordResetUrl.append(urlEncodedTempPassword);
    passwordResetUrl.append("&member=");
    passwordResetUrl.append(memberName);

    // Prepare the FreeMarker configuration;
    Configuration cfg = MVNForumConfig.getFreeMarkerConfiguration();

    // Below is a code to map content of email to template
    Map root = new HashMap();
    root.put("serverName", serverName);
    root.put("MVNForumInfo", mvnForumInfo.getProductDesc());
    root.put("passwordResetUrl", passwordResetUrl.toString());
    root.put("memberName", memberName);
    root.put("currentTempPassword", currentTempPassword);

    StringWriter subjectWriter = new StringWriter(256);
    Template subjectTemplate =
        cfg.getTemplate(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_SUBJECT, "UTF-8");
    subjectTemplate.process(root, subjectWriter);
    String subject = subjectWriter.toString();

    StringWriter bodyWriter = new StringWriter(1024);
    Template bodyTemplate = cfg.getTemplate(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_BODY, "UTF-8");
    bodyTemplate.process(root, bodyWriter);
    String body = bodyWriter.toString();

    log.debug("subject = " + subject);
    log.debug("body = " + body);
    try {
      MailMessageStruct mailMessageStruct = new MailMessageStruct();
      mailMessageStruct.setFrom(MVNForumConfig.getWebMasterEmail());
      mailMessageStruct.setTo(memberEmail);
      mailMessageStruct.setSubject(subject);
      mailMessageStruct.setMessage(body);

      MailUtil.sendMail(mailMessageStruct);
    } catch (UnsupportedEncodingException e) {
      log.error("Cannot support encoding", e);
    }
  }

  public void resetPassword(GenericRequest request)
      throws BadInputException, ObjectNotFoundException, DatabaseException, InterceptorException {

    SecurityUtil.checkHttpPostMethod(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    String memberName = GenericParamUtil.getParameter(request, "member", true);
    StringUtil.checkGoodName(memberName, locale);
    // IMPORTANT: MUST check that temp password is not empty, because temppassword = empty
    // means cannot reset password
    String memberTempPassword = GenericParamUtil.getParameter(request, "temppassword", true);

    int memberID = 0;
    try {
      memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.membername_not_exists",
          new Object[] {memberName});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String currentTempPassword = DAOFactory.getMemberDAO().getTempPassword(memberID);
    if (memberTempPassword.equals(currentTempPassword) == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.wrong_temporary_password");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Your temporary password is not correct, please try the forgot
      // password feature.");
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

    Timestamp passwordExpireDate = null;
    if (MVNForumConfig.getMaxPasswordDays() > 0) {
      passwordExpireDate =
          DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
    }
    DAOFactory.getMemberDAO().updatePassword(memberID, memberPassword, passwordExpireDate);
    DAOFactory.getMemberDAO().updateTempPassword(memberID, "");// reset the temp password
  }

  public void sendActivateCode(GenericRequest request)
      throws BadInputException, ObjectNotFoundException, DatabaseException, MessagingException,
      IOException, TemplateException {

    SecurityUtil.checkHttpPostMethod(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);
    int memberID = 0;
    String memberName = GenericParamUtil.getParameter(request, "MemberName", true);
    StringUtil.checkGoodName(memberName, locale);
    String memberEmail = GenericParamUtil.getParameterEmail(request, "MemberEmail");

    // we find the email of this memberID, not the provided email
    try {
      memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.membername_not_exists",
          new Object[] {memberName});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // Check if the email is correct
    MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    if (!memberEmail.equalsIgnoreCase(memberBean.getMemberEmail())) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.provided_email_not_equals_member_email");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Your provided email does not equals to the member's email in
      // our database. Please try again.");
    }

    // end check, send mail now
    String serverName = ParamUtil.getServerPath();// ParamUtil.getServer2(request);
    SendMailUtil.sendActivationCodeEmail(memberID, serverName);
  }

  public void activateMember(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    String memberName = GenericParamUtil.getParameter(request, "member", true);
    Locale locale = I18nUtil.getLocaleInRequest(request);
    StringUtil.checkGoodName(memberName, locale);

    // IMPORTANT: MUST check that ActivateCode is not empty, because ActivateCode = empty
    // means invalid
    String memberActivateCode = GenericParamUtil.getParameter(request, "activatecode", true);
    if (memberActivateCode.equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_activate.invalid_activation_code");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot activate member with invalid activation code.");
    }
    int memberID = 0;
    try {
      memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.membername_not_exists",
          new Object[] {memberName});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // Now, check that this member is not activated, to prevent the
    // situation that other people try to annoy this member
    if (DAOFactory.getMemberDAO().getActivateCode(memberID)
        .equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_activate.is_activated_member");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot activate an activated member.");
    }

    String currentActivateCode = DAOFactory.getMemberDAO().getActivateCode(memberID);
    if (memberActivateCode.equals(currentActivateCode) == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_activate.wrong_activation_code");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Your activation code is not correct, please try the Member
      // Account Activation feature.");
    }

    DAOFactory.getMemberDAO().updateActivateCode(memberID,
        MemberBean.MEMBER_ACTIVATECODE_ACTIVATED);// activate member

    // now reload the permission if this online user is the activated user
    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    if (memberID == onlineUser.getMemberID()) {
      onlineUser.reloadPermission();
    }
  }

  /*************************************************
   * For public view
   *************************************************/
  public void prepareView_forPublic(GenericRequest request)
      throws BadInputException, ObjectNotFoundException, DatabaseException {

    String memberName = GenericParamUtil.getParameter(request, "member", false);
    Locale locale = I18nUtil.getLocaleInRequest(request);
    // primary key column(s)
    int memberID = -1;
    if (memberName.length() == 0) {
      memberID = GenericParamUtil.getParameterInt(request, "memberid");
    } else {// has MemberName
      /**
       * @todo: improve this for better performance(don't use this method, and write 2 new methods)
       */
      StringUtil.checkGoodName(memberName, locale);// check for better security
      try {
        memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {memberName});
        throw new ObjectNotFoundException(localizedMessage);
      }
    }

    try {
      DAOFactory.getMemberDAO().increaseViewCount(memberID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.memberid_not_exists",
          new Object[] {new Integer(memberID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
    MemberBean memberBean = MemberCache.getInstance().getMember(memberID);

    request.setAttribute("MemberBean", memberBean);
  }

  /**
   * This method supports sorting base on many criteria
   */
  public void prepareListMembers_forPublic(GenericRequest request)
      throws DatabaseException, BadInputException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableListMembers() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.list_members_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot list members because LIST_MEMBERS feature is
      // disabled by administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);

    if (onlineUser.isGuest()) {
      if (MVNForumConfig.getEnableGuestViewListUsers() == false) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.guest_cannot_view_list_users");
        throw new IllegalStateException(localizedMessage);
      }
    }
    // MVNForumPermission permission = onlineUser.getPermission();
    // @todo: some permission checking is needed ???

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

    int totalMembers = DAOFactory.getMemberDAO().getNumberOfMembers();
    if (offset > totalMembers) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.offset_greater_than_total_rows");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("The offset is not allowed to be greater than total rows.");
    }

    Collection memberBeans = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(offset,
        postsPerPage, sort, order, MemberDAO.ALL_MEMBER_STATUS);

    request.setAttribute("MemberBeans", memberBeans);
    request.setAttribute("TotalMembers", new Integer(totalMembers));
  }

  // just for showing member's avatar
  public void getAvatar(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    if (MVNForumConfig.getEnableAvatar() == false) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    int memberID = 0;
    try {
      memberID = ParamUtil.getParameterInt(request, "memberid");
    } catch (BadInputException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    MemberBean member = null;
    try {
      member = MemberCache.getInstance().getMember(memberID);
    } catch (ObjectNotFoundException e) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    } catch (DatabaseException e) {
      log.error("Error when getting member", e);
      response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      return;
    }

    String memberAvatar = member.getMemberAvatar();
    if (memberAvatar.equals(MemberBean.MEMBER_AVATAR_USING_UPLOAD)
        || memberAvatar.startsWith(BinaryStorageService.BINARY_STORAGE)
        || memberAvatar.startsWith(MVNForumGlobal.UPLOADED_AVATAR_DIR)) {
      memberAvatar = member.getMemberName() + ".jpg";
    } else {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    String imageMimeType = "image/jpeg";

    /*
     * if (memberAvatar.startsWith(BinaryStorageService.BINARY_STORAGE) == false) { File avatarFile
     * = new File(MVNForumConfig.getAvatarDir() + File.separator + memberAvatar); if
     * (avatarFile.exists() == false) { response.sendError(HttpServletResponse.SC_NOT_FOUND);
     * return; } if (avatarFile.isFile() == false) {
     * response.sendError(HttpServletResponse.SC_NO_CONTENT); return; } } else { //don't handle }
     */

    long lastModified = 0;
    try {
      BinaryStorageInfo binaryStorageInfo = binaryStorageService.getBinaryStorageInfo(
          BinaryStorageService.CATEGORY_AVATAR, String.valueOf(memberID), null);
      lastModified = binaryStorageInfo.getStorageModifiedDate().getTime();
    } catch (Exception ex) {
      log.error("Error while trying to send avatar from server", ex);
      if (ex instanceof FileNotFoundException) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      } else {
        response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      }
      return;
    }

    long ifModifiedSince = request.getDateHeader("If-Modified-Since");
    // log.debug("\n ** Last Modified : " + lastModified + " If Modified Since : " + ifModifiedSince
    // + " **");
    if (ifModifiedSince != -1) {
      if (/*
           * (request.getHeader("If-None-Match") == null) &&
           */(lastModified <= ifModifiedSince)) {
        // The entity has not been modified since the date
        // specified by the client. This is not an error case.

        log.debug(
            "MemberWebHandler.getAvatar() succeed with a status HttpServletResponse.SC_NOT_MODIFIED");

        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return;
      }
    }

    OutputStream outputStream = null;
    try {
      String httpModified = DateUtil.getHTTPHeaderTime(new Date(lastModified));
      response.setContentType(imageMimeType);
      response.setHeader("Location", memberAvatar);
      response.setHeader("Last-Modified", httpModified);
      // response.setHeader("Content-Disposition", "attachment; filename=" + memberAvatar);//always
      // download
      // response.setHeader("Content-Length", String.valueOf(avatarFile.length()));//problem with
      // compression

      // now, the header initialized, just write the file content on the output
      try {
        outputStream = response.getOutputStream();
        InputStream inputStream = binaryStorageService
            .getInputStream(BinaryStorageService.CATEGORY_AVATAR, String.valueOf(memberID), null);
        IOUtils.copy(inputStream, outputStream);
      } catch (IOException ex) {
        // CANNOT throw Exception after we output to the response
        log.error("Error while trying to send avatar from server", ex);
      }

      if (outputStream != null) {
        outputStream.flush();
        outputStream.close();
        outputStream = null;// no close twice
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException ex) {
        }
      }
    }
  }

  public void processUpdateCMSPassword(GenericRequest request, String memberCurrentMakhau,
      String memberPassword1, String memberPassword2) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureIsAuthenticated();

    int memberID = onlineUser.getMemberID();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request, memberCurrentMakhau, "");
    if (memberPassword1.equals(memberPassword2) == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.confirmed_password_is_not_match");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Password and confirmed password are not the same, please try
      // again.");
    }

    String memberPassword = Encoder.getMD5_Base64(memberPassword1);
    String currentPassword = DAOFactory.getMemberDAO().getPassword(memberID);
    if (currentPassword.equals(memberPassword)) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.old_password_and_new_password_cannot_equal");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Old password and new password cannot equal, please try
      // again.");
    }

    Timestamp passwordExpireDate = null;
    if (MVNForumConfig.getMaxPasswordDays() > 0) {
      passwordExpireDate =
          DateUtil.getCurrentGMTTimestampExpiredDay(MVNForumConfig.getMaxPasswordDays());
    }
    DAOFactory.getMemberDAO().updatePassword(memberID, // primary key
        memberPassword, passwordExpireDate);

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.user.action.desc.ChangePasswordProcess");
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_USER,
        "change password", actionDesc, EventLogService.MEDIUM);

  }

  public void prepareListOnlineUsers(GenericRequest request, String requestURI)
      throws AuthenticationException, DatabaseException, MissingURLMapEntryException {

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableOnlineUsers() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.list_online_users_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot list online users because ONLINE_USERS feature is
      // disabled by administrator.");
    }

    OnlineUserUtil.updateOnlineUserAction(request, requestURI);

    // now set the attribute
    // request.setAttribute("OnlineUserActions",
    // onlineUserManager.getOnlineUserActions(0/*default*/));// no permission
    boolean duplicateUsers = MVNForumConfig.getEnableDuplicateOnlineUsers();
    request.setAttribute("OnlineUserActions",
        onlineUserManager.getOnlineUserActions(0/* default */, duplicateUsers));// no permission

  }
}
