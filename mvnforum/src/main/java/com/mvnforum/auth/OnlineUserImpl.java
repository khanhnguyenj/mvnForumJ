/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUserImpl.java,v 1.92 2009/07/17
 * 06:56:00 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.92 $ $Date: 2009/07/17 06:56:00
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
package com.mvnforum.auth;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.huongdanjava.mvnforum.http.GoogleRecaptchaAdapterImpl;
import com.huongdanjava.mvnforum.usecases.adapter.GoogleRecaptchaAdapter;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import lombok.extern.slf4j.Slf4j;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;
import net.myvietnam.mvncore.web.impl.GenericResponseServletImpl;

@Slf4j
public class OnlineUserImpl implements OnlineUser {

  private static final long CHECK_NEW_MESSAGE_INTERVAL = 5 * DateUtil.MINUTE; // five minutes

  private int memberID = MVNForumConstant.MEMBER_ID_OF_GUEST;

  private String memberName = "";
  private String alternateIdentity = null; // must be null if not login with OpenID

  private int authenticationType = AUTHENTICATION_TYPE_UNAUTHENTICATED;

  private MVNForumPermission permission = null;

  private OnlineUserAction onlineUserAction = new OnlineUserAction();

  private int memberPostsPerPage = 10;
  private int memberMessagesPerPage = 10;

  private boolean invisible = false;

  private int newMessageCount = 0;

  private String memberCssPath = null;
  private String memberLogoPath = null;

  private double timezone = 0;
  /*
   * private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Igor: previous
   * line should be: new SimpleDateFormat(..., Locale.US) Otherwise won't work for users who don't
   * have en/US as default.
   */
  private DateFormat timestampFormatter = null;
  private DateFormat dateFormatter = null;

  private Timestamp lastLogonTimestamp = null;

  private boolean passwordExpired = false;

  private Timestamp lastCheckNewMessageTimestamp = null;

  private String lastLogonIP = null;

  private String localeName = "";

  private Locale locale = null;

  private boolean gender = true;

  private boolean isMobileAgent = false;

  private Object conn = null;
  private Set participants = new HashSet();
  private Set waitingList = new HashSet();

  /**
   * Default access constructor, prevent outsite creation NOTE: the implementation should init the
   * following: - Is Guest or not <br>
   * - Call initRemoteAddr_UserAgent() <br>
   * - The memberCssPath <br>
   * - The memberLogoPath <br>
   */
  OnlineUserImpl(HttpServletRequest request, boolean isGuest) {
    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    init(genericRequest, isGuest);
  }

  OnlineUserImpl(GenericRequest request, boolean isGuest) {
    init(request, isGuest);
  }

  private void init(GenericRequest request, boolean isGuest) {
    if (isGuest) {
      setMemberID(MVNForumConstant.MEMBER_ID_OF_GUEST);
      setMemberName(MVNForumConfig.getDefaultGuestName());
    }
    getOnlineUserAction().initRemoteAddr_UserAgent(request);
    String contextPath = request.getContextPath();
    memberCssPath = contextPath + MVNForumGlobal.CSS_FULLPATH;
    memberLogoPath = contextPath + MVNForumGlobal.LOGO_FULLPATH;
    isMobileAgent = MyUtil.isRequestFromMobileDevice(request);
  }

  @Override
  public int getMemberID() {
    return memberID;
  }

  @Override
  public String getMemberName() {
    return memberName;
  }

  @Override
  public String getAlternateIdentity() {
    return alternateIdentity;
  }

  @Override
  public boolean isGuest() {
    return ((memberID == 0) || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST));
  }

  @Override
  public boolean isMember() {
    return !isGuest();
  }

  @Override
  public boolean isInvisibleMember() {
    // @todo: temp implementation
    return this.invisible;
  }

  @Override
  public boolean isPasswordExpired() {
    return passwordExpired;
  }

  @Override
  public int getAuthenticationType() {
    return authenticationType;
  }

  @Override
  public MVNForumPermission getPermission() {
    return permission;
  }

  @Override
  public void reloadPermission() {
    try {
      if (isGuest()) {
        permission = MVNForumPermissionFactory.getAnonymousPermission();
      } else {
        // Please note that getAuthenticatedPermission only need memberName, so we can get from
        // Cache for better performance
        MemberBean memberBean = MemberCache.getInstance().getMember(memberID);
        permission = MVNForumPermissionFactory.getAuthenticatedPermission(memberBean);
      }
    } catch (Exception ex) {
      log.error("Error when reload permission in OnlineUserImpl for memberID = " + memberID, ex);
    }
  }

  @Override
  public void reloadProfile(GenericRequest request, GenericResponse response) {
    try {
      if (isGuest()) {
        // currently just do nothing, implement later
      } else {
        MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);

        double theTimezone = memberBean.getMemberTimeZone();
        localeName = memberBean.getMemberLanguage();
        int postsPerPage = memberBean.getMemberPostsPerPage();

        setTimeZone(theTimezone);
        setLocaleName(localeName, request, response);
        setGender(memberBean.getMemberGender() != 0);
        setPostsPerPage(postsPerPage);
        setInvisible(memberBean.isInvisible());
      }
    } catch (Exception ex) {
      log.error("Error when reload profile in OnlineUserImpl for memberID = " + memberID, ex);
    }
  }

  @Override
  public boolean updateNewMessageCount(boolean forceUpdate) {

    if (isGuest()) {
      return false;
    }

    int currentMessageCount = newMessageCount;
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    long lastRequest = 0;
    if (lastCheckNewMessageTimestamp != null) {
      lastRequest = lastCheckNewMessageTimestamp.getTime();
    }
    if ((lastCheckNewMessageTimestamp == null) || forceUpdate
        || ((lastRequest + CHECK_NEW_MESSAGE_INTERVAL) <= now.getTime())) {
      try {
        lastCheckNewMessageTimestamp = now;
        newMessageCount =
            DAOFactory.getMessageDAO().getNumberOfUnreadNonPublicMessages_inMember_inFolder(
                memberID, MVNForumConstant.MESSAGE_FOLDER_INBOX);
        if (currentMessageCount < newMessageCount) {
          return true;
        }
      } catch (Exception ex) {
        log.error(
            "Error when udpate new message count in OnlineUserImpl for memberID = " + memberID, ex);
      }
    }
    return false;
  }

  @Override
  public OnlineUserAction getOnlineUserAction() {
    return onlineUserAction;
  }

  @Override
  public java.util.Date convertGMTDate(java.util.Date gmtDate) {
    return DateUtil.convertGMTDate(gmtDate, timezone);
  }

  @Override
  public Timestamp convertGMTTimestamp(Timestamp gmtTimestamp) {
    return DateUtil.convertGMTTimestamp(gmtTimestamp, timezone);
  }

  @Override
  public String getGMTDateFormat(java.util.Date gmtDate) {
    return getGMTDateFormat(gmtDate, true);
  }

  @Override
  public synchronized String getGMTDateFormat(java.util.Date gmtDate, boolean adjustTimeZone) {
    if (gmtDate == null) {
      return "";
    }

    java.util.Date date = gmtDate;
    if (adjustTimeZone) {
      date = DateUtil.convertGMTDate(gmtDate, timezone);
    }
    return dateFormatter.format(date);
  }

  @Override
  public String getGMTTimestampFormat(Timestamp gmtTimestamp) {
    return getGMTTimestampFormat(gmtTimestamp, true);
  }

  @Override
  public synchronized String getGMTTimestampFormat(Timestamp gmtTimestamp, boolean adjustTimeZone) {
    if (gmtTimestamp == null) {
      return "";
    }

    Timestamp timestamp = gmtTimestamp;
    if (adjustTimeZone) {
      timestamp = DateUtil.convertGMTTimestamp(gmtTimestamp, timezone);
    }
    return timestampFormatter.format(timestamp);
  }

  @Override
  public String getLocaleName() {
    return localeName;
  }

  @Override
  public void setLocaleName(String localeName, HttpServletRequest request,
      HttpServletResponse response) {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    GenericResponse genericResponse = new GenericResponseServletImpl(response);
    setLocaleName(localeName, genericRequest, genericResponse);
  }

  @Override
  public void setLocaleName(String localeName, GenericRequest request, GenericResponse response) {
    if (localeName == null) {
      localeName = "";
    }

    if ((localeName.length() == 0) || (MVNForumConfig.supportLocale(localeName) == false)) {
      this.localeName = MVNForumConfig.getDefaultLocaleName();
      this.locale = MVNForumConfig.getDefaultLocale();
    } else {
      this.localeName = localeName;
      this.locale = I18nUtil.getLocale(localeName);
    }

    // now initialize the 2 class variables
    dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
    timestampFormatter =
        DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);

    // now, try to save cookie locale for Tapestry
    if (response != null) {
      String cookiePath = "/";
      if (request != null) {
        // try to save cookie in the context path
        cookiePath = request.getContextPath() + "/";
      }
      Cookie localeCookie4 = new Cookie(MVNForumConstant.TAPESTRY4_COOKIE_LOCALE, this.localeName);
      localeCookie4.setVersion(1); // make it work with Tomcat 6.0.16
      localeCookie4.setMaxAge(-1);
      localeCookie4.setPath(cookiePath);

      Cookie localeCookie5 = new Cookie(MVNForumConstant.TAPESTRY5_COOKIE_LOCALE, this.localeName);
      localeCookie5.setVersion(1); // make it work with Tomcat 6.0.16
      localeCookie5.setMaxAge(-1);
      localeCookie5.setPath(cookiePath);

      response.addCookie(localeCookie4);
      response.addCookie(localeCookie5);
    }
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

  @Override
  public String getLastLogonIP() {
    if (isGuest()) {
      return "";
    }
    return lastLogonIP;
  }

  @Override
  public Timestamp getLastLogonTimestamp() {
    return lastLogonTimestamp;
  }

  /*
   * public boolean getGender() { return gender; }
   */
  @Override
  public int getPostsPerPage() {
    return memberPostsPerPage;
  }

  @Override
  public int getMessagesPerPage() {
    return memberMessagesPerPage;
  }

  @Override
  public int getNewMessageCount() {
    return newMessageCount;
  }

  @Override
  public String getCssPath() {
    return memberCssPath;
  }

  @Override
  public String getCssPath(HttpServletRequest request) {
    String preview = ParamUtil.getParameter(request, "csspreview");
    if ("yes".equals(preview)) {
      String contextPath = request.getContextPath();
      return contextPath + MVNForumGlobal.CSS_PREVIEW_FULLPATH;
    }
    return memberCssPath;
  }

  @Override
  public String getLogoPath() {
    return memberLogoPath;
  }

  /**
   * Validate the anwser of the captcha from user
   *
   * @param anwser String the captcha anwser from user
   * @return boolean true if the answer is valid, otherwise return false
   */
  @Override
  public boolean validateCaptchaResponse(String gRecaptchaResponse) {
    String googleRecaptchaSecretKey = MVNForumConfig.getGoogleRecaptchaSecretKey();

    GoogleRecaptchaAdapter googleRecaptchaAdapter = new GoogleRecaptchaAdapterImpl();

    try {
      return googleRecaptchaAdapter.verify(gRecaptchaResponse, googleRecaptchaSecretKey);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }

  /**
   * Check to make sure that the captcha answer is correct
   *
   * @param answer String the captcha answer to check
   * @throws BadInputException in case the captcha answer is not correct
   */
  @Override
  public void ensureCorrectCaptchaResponse(String answer) throws BadInputException {
    if (validateCaptchaResponse(answer) == false) {
      throw new BadInputException(MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.wrong_captcha"));
    }
  }

  /**
   * *************************************************************** Default-scope methods, only for
   * internal package usage ***************************************************************
   */
  void setMemberID(int memberID) {
    if (memberID == 0) {
      this.memberID = MVNForumConstant.MEMBER_ID_OF_GUEST;
    } else {
      this.memberID = memberID;
    }
    onlineUserAction.setMemberID(this.memberID);
  }

  void setPasswordExpired(boolean expired) {
    this.passwordExpired = expired;
  }

  void setMemberName(String memberName) {
    this.memberName = memberName;
    onlineUserAction.setMemberName(memberName);
  }

  void setAlternateIdentity(String identity) {
    if ((identity != null) && (identity.length() > 0)) {
      alternateIdentity = identity;
    }
  }

  void setInvisible(boolean invisible) {
    this.invisible = invisible;
    onlineUserAction.setMemberInvisible(invisible);
  }

  void setAuthenticationType(int authType) {
    authenticationType = authType;
  }

  /** NOTE: this method SHOULD ONLY BE CALLED from OnlineUserFactory */
  void setPermission(MVNForumPermission permission) {
    this.permission = permission;
  }

  public void setTimeZone(double _timezone) {
    if ((_timezone >= -12) && (_timezone <= 12)) {
      this.timezone = _timezone;
    }
  }

  void setLastLogonTimestamp(Timestamp lastLogon) {
    lastLogonTimestamp = lastLogon;
  }

  void setLastLogonIP(String lastLogonIP) {
    this.lastLogonIP = lastLogonIP;
  }

  void setGender(boolean gender) {
    this.gender = gender;
  }

  public void setPostsPerPage(int postsPerPage) {
    // minimum is 5
    if (postsPerPage < 5) {
      postsPerPage = 5;
    }

    // maximum is 200
    if (postsPerPage > 200) {
      postsPerPage = 200;
    }
    this.memberPostsPerPage = postsPerPage;
  }

  @Override
  public void setCssPath(String cssPath) {
    this.memberCssPath = cssPath;
  }

  @Override
  public void setLogoPath(String logoPath) {
    this.memberLogoPath = logoPath;
  }

  // add this method to remove eclipse's warning
  public boolean getGender() {
    return gender;
  }

  @Override
  public Object getXMPPConnection() {
    return conn;
  }

  @Override
  public void setXMPPConnection(Object conn) {
    this.conn = conn;
  }

  @Override
  public Set getParticipants() {
    return participants;
  }

  @Override
  public Set getWaitingList() {
    return waitingList;
  }

  @Override
  public void setParticipants(Object participant) {
    synchronized (participants) {
      participants.add(participant);
    }
  }

  @Override
  public void setWaitingList(Object waiting) {
    synchronized (waitingList) {
      waitingList.add(waiting);
    }
  }

  @Override
  public void removeParticipant(Object participant) {
    synchronized (participants) {
      participants.remove(participant);
    }
  }

  @Override
  public void removeWaiting(Object waiting) {
    synchronized (waitingList) {
      waitingList.remove(waiting);
    }
  }

  @Override
  public double getTimeZone() {
    return timezone;
  }

  @Override
  public String getTimeZoneFormat() {
    String returnStr = null;
    int nTimezone = (int) timezone;
    double roundedTimezone = nTimezone;
    if (timezone > 0) {
      if (roundedTimezone == timezone) {
        returnStr = String.valueOf(nTimezone);
      } else {
        returnStr = String.valueOf(timezone);
      }
      return "+" + returnStr;
    } else if (timezone == 0) {
      return "GMT";
    } else {
      if (roundedTimezone == timezone) {
        returnStr = String.valueOf(nTimezone);
      } else {
        returnStr = String.valueOf(timezone);
      }
      return returnStr;
    }
  }

  @Override
  public boolean isMobileAgent() {
    return this.isMobileAgent;
  }
}
