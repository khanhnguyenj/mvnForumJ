/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUserManager.java,v 1.111
 * 2010/08/10 11:52:50 minhnn Exp $ $Author: minhnn $ $Revision: 1.111 $ $Date: 2010/08/10 11:52:50
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.ManagerFactory;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.event.MvnForumEvent;
import com.mvnforum.event.MvnForumEventManager;
import com.mvnforum.service.MvnForumServiceFactory;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.FloodException;
import net.myvietnam.mvncore.exception.NotLoginException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;
import net.myvietnam.mvncore.web.impl.GenericResponseServletImpl;

public final class OnlineUserManager {

  private static final Logger log = LoggerFactory.getLogger(OnlineUserManager.class);

  private static final int REMOVE_INTERVAL = 2000; // update every 2 second

  private static final String MVNFORUM_SESSION_USERNAME = "mvnforum.membername";
  private static final String MVNFORUM_SESSION_PASSWORD = "mvnforum.encodedpassword";

  private static final String MVNFORUM_COOKIE_USERNAME = "mvnforum.membername";
  private static final String MVNFORUM_COOKIE_PASSWORD = "mvnforum.encodedpassword";

  public static final String MVNFORUM_SESSION_OPENID_USERNAME = "mvnforum.openid.membername";
  public static final String MVNFORUM_SESSION_OPENID_IDENTITY = "mvnforum.openid.identity";

  public static final String MVNFORUM_SESSION_LDAP_USERNAME = "mvnforum.ldap.membername";

  public static final String MVNFORUM_COOKIE_PATH = "/";

  public static final String PASSWORD_OF_METHOD_REALM = "Realm"; // must not be changed in all cases
  public static final String PASSWORD_OF_METHOD_CUSTOMIZATION = "Remote"; // must not be changed in
                                                                          // all cases
  public static final String PASSWORD_OF_METHOD_CAS = "Cas"; // must not be changed in all cases
  public static final String PASSWORD_OF_METHOD_OPENID = "OpenID"; // must not be changed in all
                                                                   // cases

  // static variable
  private static OnlineUserManager instance = new OnlineUserManager();

  /**
   * Returns a collection view of the values contained in this map. The collection is backed by the
   * map, so changes to the map are reflected in the collection, and vice-versa. If the map is
   * modified while an iteration over the collection is in progress (except through the iterator's
   * own <tt>remove</tt> operation), the results of the iteration are undefined.
   */
  // instance variable
  private Map userMap = new TreeMap();
  private long timeOfLastRemoveAction = 0;
  private Collection onlineUserListeners;

  private Authenticator authenticator = null;

  private OnlineUserManager() {
    onlineUserListeners = new ArrayList();
  }

  public static OnlineUserManager getInstance() {
    return instance;
  }

  public Authenticator getAuthenticator() {
    return authenticator;
  }

  public void setAuthenticator(Authenticator authenticator) {
    this.authenticator = authenticator;
  }

  public boolean isAlreadyOnlineUser(HttpServletRequest request) {
    return userMap.containsKey(request.getSession().getId());
  }

  /**
   * MemberUtil method to be called from Processor. It assumes that to input parameters are
   * MemberName for username MemberMatkhau for password
   *
   * @deprecated should use the 3 parameters method processLogin
   */
  @Deprecated
  public void processLogin(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, DatabaseException, BadInputException, FloodException {

    boolean checkPost = true;
    processLogin(request, response, checkPost);
  }

  /**
   * MemberUtil method to be called from Processor. It assumes that to input parameters are
   * MemberName for username MemberMatkhau for password
   */
  public void processLogin(HttpServletRequest request, HttpServletResponse response,
      boolean checkPost)
      throws AuthenticationException, DatabaseException, BadInputException, FloodException {

    if (checkPost) {
      SecurityUtil.checkHttpPostMethod(request);
    }
    Locale locale = I18nUtil.getLocaleInRequest(request);
    String memberName = ParamUtil.getParameter(request, "MemberName", true);
    StringUtil.checkGoodName(memberName, locale);// check for better security
    String memberPassword = "";
    String memberPasswordMD5 = ParamUtil.getParameter(request, "md5pw", false);
    if (memberPasswordMD5.length() == 0 || (memberPasswordMD5.endsWith("==") == false)) {
      // md5 is not valid, try to use unencoded password method
      memberPassword = ParamUtil.getParameterPassword(request, "MemberMatkhau", 3, 0);
      AssertionUtil.doAssert(memberPassword.length() != 0,
          "Cannot allow memberPassword's length is 0. Serious Assertion Failed.");
    }

    processLogin(request, response, memberName, memberPassword, memberPasswordMD5);
  }

  /**
   * Login method, if memberPassword length == 0, then login with memberPasswordMD5
   */
  public void processLogin(HttpServletRequest request, HttpServletResponse response,
      String memberName, String memberPassword, String memberPasswordMD5)
      throws AuthenticationException, DatabaseException, BadInputException, FloodException {

    if ((memberName == null) || (memberName.length() == 0)) {
      throw new IllegalArgumentException("Cannot processLogin with an empty loginName");
    }

    Locale locale = I18nUtil.getLocaleInRequest(request);
    StringUtil.checkGoodName(memberName, locale);// check for better security

    String currentIP = request.getRemoteAddr();
    try {
      // Control the login action, we don't want user to try too many login attempt
      FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_LOGIN_PER_IP, currentIP);

      OnlineUser user = null;
      if (memberPassword.length() > 0) {
        // that is we cannot find the md5 password
        /*
         * if (MVNForumConfig.getEnableEncryptPasswordOnBrowser()) { user = login(request, response,
         * memberName, memberPassword, false); } else { user = login(request, response, memberName,
         * memberPassword, true); }
         */
        user = login(request, response, memberName, memberPassword, false);
      } else {
        // have the md5, go ahead
        user = login(request, response, memberName, memberPasswordMD5, true);
      }
      ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_HTML_FORM);
    } catch (AuthenticationException ex) {
      // only increase login count if unsuccessful
      FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_LOGIN_PER_IP, currentIP);

      if (ex.getReason() == NotLoginException.WRONG_PASSWORD) {
        request.setAttribute("MemberName", memberName);// so user don't have to retype USER NAME
      }
      throw ex;
    } catch (FloodException fe) {
      Integer maxWrongLogins =
          new Integer(FloodControlHour.getActionsPerHour(MVNForumGlobal.FLOOD_ID_LOGIN_PER_IP));
      // throw new FloodException("You have reached the maximum number of wrong login actions for
      // this page. Please try this page later. This is to prevent forum from being flooded.");
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.FloodException.login_too_many_times", new Object[] {maxWrongLogins});
      throw new FloodException(localizedMessage);
    }
  }

  /**
   * NOTE: This method MUST be the only way to authenticate a user NOTE: the parameter response can
   * be null
   */
  private OnlineUser login(HttpServletRequest request, HttpServletResponse response,
      String memberName, String memberPassword, boolean passwordEncoded)
      throws AuthenticationException, DatabaseException {

    try {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      StringUtil.checkGoodName(memberName, locale);
    } catch (Exception ex) {
      throw new AuthenticationException(ex.getMessage(), NotLoginException.WRONG_NAME);
    }

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    MvnForumEvent event = new MvnForumEvent(this, genericRequest);
    MvnForumEventManager.getInstance().firePreLogin(event);

    String encodedPassword;
    OnlineUser user;
    if (passwordEncoded) {
      encodedPassword = memberPassword;
      user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, response,
          memberName, encodedPassword, true);
    } else {
      encodedPassword =
          ManagerFactory.getOnlineUserFactory().getEncodedPassword(memberName, memberPassword);
      // user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, response,
      // memberName, memberPassword, false);
      user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, response,
          memberName, encodedPassword, true);
    }

    HttpSession session = request.getSession();
    String sessionID = session.getId();
    setOnlineUser(sessionID, user);

    // now save the login info in the session only if we support
    // encoded passwords
    if (encodedPassword != null) {
      if ((user.getAuthenticationType() != OnlineUser.AUTHENTICATION_TYPE_REALM)
          && (user.getAuthenticationType() != OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION)
          && (user.getAuthenticationType() != OnlineUser.AUTHENTICATION_TYPE_CAS)
          && (encodedPassword.equals(PASSWORD_OF_METHOD_REALM) == false)
          && (encodedPassword.equals(PASSWORD_OF_METHOD_CUSTOMIZATION) == false)
          && (encodedPassword.equals(PASSWORD_OF_METHOD_CAS) == false)) {

        session.setAttribute(MVNFORUM_SESSION_USERNAME, memberName);
        session.setAttribute(MVNFORUM_SESSION_PASSWORD, encodedPassword);
      }
    }

    boolean fromLoginPage = ParamUtil.getParameterBoolean(request, "FromLoginPage");
    if (fromLoginPage && (response != null)) {
      manageAutoLogin(memberName, encodedPassword, request, response);
    }

    // Now call the postLogin method, in the default implementation, the default folder
    // is checked and created if not existed
    ManagerFactory.getOnlineUserFactory().postLogin(request, response, user);
    MvnForumEventManager.getInstance().firePostLogin(event);

    return user;
  }

  private OnlineUser login(GenericRequest request, GenericResponse response, String memberName,
      String memberPassword, boolean passwordEncoded)
      throws AuthenticationException, DatabaseException {

    try {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      StringUtil.checkGoodName(memberName, locale);
    } catch (Exception ex) {
      throw new AuthenticationException(ex.getMessage(), NotLoginException.WRONG_NAME);
    }

    MvnForumEvent event = new MvnForumEvent(this, request);
    MvnForumEventManager.getInstance().firePreLogin(event);

    String encodedPassword;
    OnlineUser user;
    if (passwordEncoded) {
      encodedPassword = memberPassword;
      user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, null, memberName,
          encodedPassword, true);

    } else {
      encodedPassword =
          ManagerFactory.getOnlineUserFactory().getEncodedPassword(memberName, memberPassword);
      // user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, response,
      // memberName, memberPassword, false);
      user = ManagerFactory.getOnlineUserFactory().getAuthenticatedUser(request, response,
          memberName, encodedPassword, true);
    }

    String sessionID = request.getSessionId();
    setOnlineUser(sessionID, user);

    // now save the login info in the session only if we support
    // encoded passwords
    /*
     * if (null != encodedPassword) { session.setAttribute(MVNFORUM_SESSION_USERNAME, memberName);
     * session.setAttribute(MVNFORUM_SESSION_PASSWORD, encodedPassword); }
     */

    /*
     * boolean fromLoginPage = ParamUtil.getParameterBoolean(request, "FromLoginPage"); if (
     * fromLoginPage && (response != null) ) { manageAutoLogin(memberName, encodedPassword, request,
     * response); }
     */

    // Now call the postLogin method, in the default implementation, the default folder
    // is checked and created if not existed
    ManagerFactory.getOnlineUserFactory().postLogin(null, null, user);
    MvnForumEventManager.getInstance().firePostLogin(event);

    return user;
  }

  private static void manageAutoLogin(String memberName, String encodedPassword,
      HttpServletRequest request, HttpServletResponse response) {

    if (MVNForumConfig.getEnableEncryptPasswordOnBrowser() == false) {
      return;// for security, we don't remember clear password
    }

    boolean autoLogin = ParamUtil.getParameterBoolean(request, "AutoLogin");
    if (autoLogin) {
      int autoLoginExpire = (60 * 60 * 24) * 1; // 1 day
      try {
        autoLoginExpire = ParamUtil.getParameterInt(request, "AutoLoginExpire");
      } catch (Exception ex) {
        // do nothing
      }
      Cookie nameCookie = new Cookie(MVNFORUM_COOKIE_USERNAME, Encoder.encodeURL(memberName));
      nameCookie.setVersion(1);// make it work with Tomcat 6.0.16
      nameCookie.setMaxAge(autoLoginExpire);
      nameCookie.setPath(MVNFORUM_COOKIE_PATH);

      Cookie passwordCookie = new Cookie(MVNFORUM_COOKIE_PASSWORD, encodedPassword);
      passwordCookie.setVersion(1);// make it work with Tomcat 6.0.16
      passwordCookie.setMaxAge(autoLoginExpire);
      passwordCookie.setPath(MVNFORUM_COOKIE_PATH);

      response.addCookie(nameCookie);
      response.addCookie(passwordCookie);
    }
  }

  public void logout(HttpServletRequest request, HttpServletResponse response)
      throws DatabaseException, AuthenticationException {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    GenericResponse genericResponse = null;

    if (response != null) {
      genericResponse = new GenericResponseServletImpl(response);
    }

    logout(genericRequest, genericResponse);
  }

  public void logout(GenericRequest request, GenericResponse response)
      throws DatabaseException, AuthenticationException {

    MvnForumEvent event = new MvnForumEvent(this, request);
    MvnForumEventManager.getInstance().firePreLogout(event);

    String sessionID = request.getSessionId();

    OnlineUser user = null;
    if (authenticator == null) {
      // temporary hack, if no authenticator has been set,
      // then we use the old method
      // @todo: more thought on this later
      OnlineUser oldUser = getOnlineUser(request);
      String cssPath = oldUser.getCssPath();
      String logoPath = oldUser.getLogoPath();
      user = ManagerFactory.getOnlineUserFactory().getAnonymousUser(request, response);
      user.setCssPath(cssPath);
      user.setLogoPath(logoPath);
    }

    // remove current user, then set new user is a guest
    setOnlineUser(sessionID, user);
    ManagerFactory.getOnlineUserFactory().logout(request, response);

    // now always clear the session information
    request.setSessionAttribute(MVNFORUM_SESSION_USERNAME, null);
    request.setSessionAttribute(MVNFORUM_SESSION_PASSWORD, null);
    request.setSessionAttribute(MVNFORUM_SESSION_LDAP_USERNAME, null);

    MvnForumEventManager.getInstance().firePostLogout(event);
  }

  public void deleteCookie(HttpServletRequest request, HttpServletResponse response) {

    if (MVNForumConfig.getEnableDeleteCookie() == false) {
      throw new IllegalStateException(
          "Cannot run this because MVNForumConfig.getEnableDeleteCookie() is false.");
    }

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    Cookie nameCookie = new Cookie(MVNFORUM_COOKIE_USERNAME, "");
    nameCookie.setPath(MVNFORUM_COOKIE_PATH);
    nameCookie.setMaxAge(0);// delete this cookie

    Cookie passwordCookie = new Cookie(MVNFORUM_COOKIE_PASSWORD, "");
    passwordCookie.setPath(MVNFORUM_COOKIE_PATH);
    passwordCookie.setMaxAge(0);// delete this cookie

    response.addCookie(nameCookie);
    response.addCookie(passwordCookie);
  }

  public OnlineUser getOnlineUser(GenericRequest request)
      throws AuthenticationException, DatabaseException {

    if (request.isServletRequest()) {
      return getOnlineUser(request.getServletRequest());
    }

    String sessionID = request.getSessionId();
    OnlineUser user = getOnlineUser(sessionID);

    // When logged in as remote or customization, the remote user is logged out
    // and mvnForum still have the old non-guest users, then we should logout this user
    // in OnlineUser too.
    if ((user != null) && (user.isGuest() == false)) {
      if (user.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_REALM) {
        String currentRemoteUser = request.getRemoteUser();
        if (currentRemoteUser == null) {
          logout(request, null /* response */);
        } else if (currentRemoteUser.equalsIgnoreCase(user.getMemberName()) == false) {
          logout(request, null /* response */);
          // throw new AssertionError("ASSERTION: Current remote user should equals the current
          // member in OnlineUser: Remote User: " + currentRemoteUser + ". But forum user is " +
          // user.getMemberName() +". Please report bug to mvnForum developers.");
        }
      } else if (user.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION) {
        String currentRemoteUser =
            StringUtil.getEmptyStringIfNull(authenticator.getRemoteUser(request));
        if (currentRemoteUser.equals("")) {
          logout(request, null /* response */);
        } else if (currentRemoteUser.equalsIgnoreCase(user.getMemberName()) == false) {
          logout(request, null /* response */);
          // throw new AssertionError("ASSERTION: Current remote user should equals the current
          // member in OnlineUser: Remote User: " + currentRemoteUser + ". But forum user is " +
          // user.getMemberName() +". Please report bug to mvnForum developers.");
        }
      }
    }
    // end of checking

    // re-get the online user to continue
    user = getOnlineUser(sessionID);

    boolean enableLoginInfoInRealm = MVNForumConfig.getEnableLoginInfoInRealm();
    if (user == null) {
      // when authenticator is null
      user = ManagerFactory.getOnlineUserFactory().getAnonymousUser(request, null);
      setOnlineUser(sessionID, user);
    }

    if (user.isGuest() && enableLoginInfoInRealm && (null != request.getRemoteUser())) {
      String memberName = StringUtil.getEmptyStringIfNull(request.getRemoteUser());
      if (memberName.length() > 0) {
        try {
          DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
          user = login(request, null, memberName, PASSWORD_OF_METHOD_REALM, true);
          ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_REALM);
          ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                            // expired password
        } catch (ObjectNotFoundException oe) {
          // ignore
        }
      }
    }

    // now we have the user, check if it is a Guest and we can login in CUSTOM
    boolean enableLoginInfoInCustomization = MVNForumConfig.getEnableLoginInfoInCustomization();
    if (authenticator == null) {
      authenticator = ManagerFactory.getAuthenticator();
    }
    if (authenticator != null) {
      if (user.isGuest() && enableLoginInfoInCustomization
          && (null != authenticator.getRemoteUser(request))) {
        String memberName = StringUtil.getEmptyStringIfNull(authenticator.getRemoteUser(request));
        if (memberName.length() > 0) {
          try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            user = login(request, null, memberName, PASSWORD_OF_METHOD_CUSTOMIZATION, true);
            ((OnlineUserImpl) user)
                .setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (ObjectNotFoundException oe) {
            // ignore, the implementation of Authenticator should create the member in database
            // first
          }
        }
      }
    }

    user.getOnlineUserAction().updateLastRequestTime();
    return user;
  }

  public OnlineUser getOnlineUser(HttpServletRequest request)
      throws AuthenticationException, DatabaseException {

    long currentTime = System.currentTimeMillis();
    if (currentTime - timeOfLastRemoveAction > REMOVE_INTERVAL) {// update every minute
      removeTimeoutUsers();
      // update MostOnline here
      int currentOnlineUserCount = userMap.size();
      fireDataChanged(new OnlineUserEvent(this, currentOnlineUserCount));
    }

    HttpSession session = request.getSession();
    String sessionID = session.getId();
    OnlineUser user = getOnlineUser(sessionID);

    // When logged in as remote or customization, the remote user is logged out
    // and mvnForum still have the old non-guest users, then we should logout this user
    // in OnlineUser too.
    if ((user != null) && (user.isGuest() == false)) {
      if (user.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_REALM) {
        String currentRemoteUser = request.getRemoteUser();
        if (currentRemoteUser == null) {
          logout(request, null /* response */);
        } else if (currentRemoteUser.equalsIgnoreCase(user.getMemberName()) == false) {
          logout(request, null /* response */);
          // throw new AssertionError("ASSERTION: Current remote user should equals the current
          // member in OnlineUser. Please report bug to mvnForum developers.");
        }
      } else if (user.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION) {
        String currentRemoteUser =
            StringUtil.getEmptyStringIfNull(authenticator.getRemoteUser(request));
        if (currentRemoteUser.equals("")) {
          logout(request, null /* response */);
        } else if (currentRemoteUser.equalsIgnoreCase(user.getMemberName()) == false) {
          logout(request, null /* response */);
          // throw new AssertionError("ASSERTION: Current remote user should equals the current
          // member in OnlineUser. Please report bug to mvnForum developers.");
        }
      }
    }
    // end of checking

    // re-get the online user to continue
    user = getOnlineUser(sessionID);
    if (user == null) {

      boolean enableLoginInfoInCookie = MVNForumConfig.getEnableLoginInfoInCookie();
      boolean enableLoginInfoInSession = MVNForumConfig.getEnableLoginInfoInSession();
      boolean enableLoginInfoInRealm = MVNForumConfig.getEnableLoginInfoInRealm();
      boolean enableLoginInfoInCAS = MVNForumConfig.getEnableLoginInfoInCAS();
      boolean enableLoginInfoInOpenID = MVNForumConfig.getEnableLoginInfoInOpenID();
      boolean enableLoginInfoInCustomization = MVNForumConfig.getEnableLoginInfoInCustomization();

      if (/* (user == null) && because user always be null here */ enableLoginInfoInSession) {
        String memberName = ParamUtil.getAttribute(session, MVNFORUM_SESSION_USERNAME);
        String encodedPassword = ParamUtil.getAttribute(session, MVNFORUM_SESSION_PASSWORD);
        if ((memberName.length() > 0) && (encodedPassword.length() > 0)) {
          try {
            user = login(request, null, memberName, encodedPassword, true);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_SESSION);
          } catch (AuthenticationException e) {
            // do nothing, some time the login info in the session
            // is not correct, we don't consider this case as error
          }
        }
      }

      if ((user == null) && enableLoginInfoInCAS) {
        String memberName = MvnForumServiceFactory.getMvnForumService()
            .getCentralAuthenticationService().tryLogin(request);
        if ((memberName != null) && (memberName.length() > 0)) {
          try {
            user = login(request, null, memberName, OnlineUserManager.PASSWORD_OF_METHOD_CAS, true);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_CAS);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (AuthenticationException e) {
            // do nothing
          }
        }
      }

      if ((user == null) && enableLoginInfoInOpenID) {
        String memberName = ParamUtil.getAttribute(session, MVNFORUM_SESSION_OPENID_USERNAME);// this
                                                                                              // method
                                                                                              // never
                                                                                              // return
                                                                                              // null
        // System.out.println("OnlineUserManager.getOnlineUser() (user is null) for OpenID
        // memberName = " + memberName);
        if (memberName.length() > 0) {
          String identity = ParamUtil.getAttribute(session, MVNFORUM_SESSION_OPENID_IDENTITY);
          session.setAttribute(MVNFORUM_SESSION_OPENID_USERNAME, null);
          session.setAttribute(MVNFORUM_SESSION_OPENID_IDENTITY, null);
          try {
            user =
                login(request, null, memberName, OnlineUserManager.PASSWORD_OF_METHOD_OPENID, true);
            ((OnlineUserImpl) user).setAlternateIdentity(identity);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_OPENID);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (AuthenticationException e) {
            // do nothing
          }
        }
      }

      if ((user == null) && enableLoginInfoInCookie) {
        String memberName = "";
        String encodedPassword = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
          for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            String cookieName = cookie.getName();
            if (cookieName.equals(MVNFORUM_COOKIE_USERNAME)) {
              memberName = Encoder.decodeURL(cookie.getValue());
            } else if (cookieName.equals(MVNFORUM_COOKIE_PASSWORD)) {
              encodedPassword = cookie.getValue();
            }
          }
        }
        if ((memberName.length() > 0) && (encodedPassword.length() > 0)
            && (encodedPassword.equalsIgnoreCase(PASSWORD_OF_METHOD_REALM) == false)
            && (encodedPassword.equalsIgnoreCase(PASSWORD_OF_METHOD_CUSTOMIZATION) == false)) {
          try {
            user = login(request, null, memberName, encodedPassword, true);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_COOKIE);
          } catch (AuthenticationException e) {
            // do nothing, some time the login info in the cookie
            // is not correct, we don't consider this case as error
          }
        }
      }

      if ((user == null) && enableLoginInfoInRealm && (null != request.getRemoteUser())) {
        String memberName = StringUtil.getEmptyStringIfNull(request.getRemoteUser());
        if (memberName.length() > 0) {
          try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            user = login(request, null, memberName, PASSWORD_OF_METHOD_REALM, true);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_REALM);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (ObjectNotFoundException oe) {
            // ignore
          }
        }
      }

      if ((user == null) && enableLoginInfoInCustomization) {
        if (authenticator == null) {
          authenticator = ManagerFactory.getAuthenticator();
        }
        if (authenticator != null) {
          String memberName = StringUtil.getEmptyStringIfNull(authenticator.getRemoteUser(request));
          if (memberName.length() > 0) {
            try {
              DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
              user = login(request, null, memberName, PASSWORD_OF_METHOD_CUSTOMIZATION, true);
              ((OnlineUserImpl) user)
                  .setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION);
              ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never
                                                                // have expired password
            } catch (ObjectNotFoundException oe) {
              // ignore
            }
          }
        }
      }

      if (user == null) {
        user = ManagerFactory.getOnlineUserFactory().getAnonymousUser(request, null);
        setOnlineUser(sessionID, user);
      }

    } else if (user.isGuest()) { // user != null and user is a guest

      boolean enableLoginInfoInRealm = MVNForumConfig.getEnableLoginInfoInRealm();
      boolean enableLoginInfoInOpenID = MVNForumConfig.getEnableLoginInfoInOpenID();
      boolean enableLoginInfoInCustomization = MVNForumConfig.getEnableLoginInfoInCustomization();


      // now we have the user and it is a Guest, check if we can login in REALM
      if (enableLoginInfoInRealm && (null != request.getRemoteUser())) {
        String memberName = StringUtil.getEmptyStringIfNull(request.getRemoteUser());
        if (memberName.length() > 0) {
          try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            user = login(request, null, memberName, PASSWORD_OF_METHOD_REALM, true);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_REALM);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (ObjectNotFoundException oe) {
            // ignore
          }
        }
      }

      // now we have the user and it is a Guest, check if we can login in OpenID
      if (enableLoginInfoInOpenID) {
        String memberName = ParamUtil.getAttribute(session, MVNFORUM_SESSION_OPENID_USERNAME);// this
                                                                                              // method
                                                                                              // never
                                                                                              // return
                                                                                              // null
        // System.out.println("OnlineUserManager.getOnlineUser() (user not null) for OpenID
        // memberName = " + memberName);
        if (memberName.length() > 0) {
          String identity = ParamUtil.getAttribute(session, MVNFORUM_SESSION_OPENID_IDENTITY);
          session.setAttribute(MVNFORUM_SESSION_OPENID_USERNAME, null);
          session.setAttribute(MVNFORUM_SESSION_OPENID_IDENTITY, null);
          try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
            user =
                login(request, null, memberName, OnlineUserManager.PASSWORD_OF_METHOD_OPENID, true);
            ((OnlineUserImpl) user).setAlternateIdentity(identity);
            ((OnlineUserImpl) user).setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_OPENID);
            ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never have
                                                              // expired password
          } catch (ObjectNotFoundException e) {
            log.error("OpenID cannot find memberName = " + memberName);
          }
        }
      }

      // now we have the user and it is a Guest, check if we can login in CUSTOM
      if (authenticator == null) {
        authenticator = ManagerFactory.getAuthenticator();
      }
      if (authenticator != null) {
        if (enableLoginInfoInCustomization && (null != authenticator.getRemoteUser(request))) {
          String memberName = StringUtil.getEmptyStringIfNull(authenticator.getRemoteUser(request));
          if (memberName.length() > 0) {
            try {
              DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
              user = login(request, null, memberName, PASSWORD_OF_METHOD_CUSTOMIZATION, true);
              ((OnlineUserImpl) user)
                  .setAuthenticationType(OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION);
              ((OnlineUserImpl) user).setPasswordExpired(false);// external authentication never
                                                                // have expired password
            } catch (ObjectNotFoundException oe) {
              // ignore, the implementation of Authenticator should create the member in database
              // first
            }
          }
        }
      }
    } else {
      // we have user, and it is a member, then just do nothing
    }

    user.getOnlineUserAction().updateLastRequestTime();
    return user;
  }

  public synchronized Collection getOnlineUserActions(int sortOption, boolean duplicateUsers) {

    Collection collection = userMap.values();
    // TODO: find a better solution, I don't want to copy the Collection
    // this ArrayList is used to copy the values
    ArrayList retValue = new ArrayList(collection.size());

    // Get members first
    if (duplicateUsers) {
      for (Iterator memberIterator = collection.iterator(); memberIterator.hasNext();) {
        OnlineUser onlineUser = (OnlineUser) memberIterator.next();
        if (onlineUser.isMember()) {
          OnlineUserAction onlineUserAction = onlineUser.getOnlineUserAction();
          retValue.add(onlineUserAction);
        }
      }
    } else {
      // now we combine the duplicated users
      Hashtable distinctUserActions = new Hashtable();
      OnlineUserAction lastUserAction = null;
      for (Iterator memberIterator = collection.iterator(); memberIterator.hasNext();) {
        OnlineUser onlineUser = (OnlineUser) memberIterator.next();
        if (onlineUser.isMember()) {
          OnlineUserAction onlineUserAction = onlineUser.getOnlineUserAction();
          String memberName = onlineUserAction.getMemberName();
          onlineUserAction.resetSessionCount();
          lastUserAction = (OnlineUserAction) distinctUserActions.get(memberName);
          if (lastUserAction == null) {
            distinctUserActions.put(memberName, onlineUserAction);
          } else if (onlineUserAction.getLastRequestTime()
              .after(lastUserAction.getLastRequestTime())) {
            distinctUserActions.put(memberName, onlineUserAction);
            onlineUserAction.increaseSessionCount(lastUserAction.getSessionCount());
          } else {
            lastUserAction.increaseSessionCount(1);
          }
        }
      }
      // now add the distinct member to the returned value
      Collection distinctCollection = distinctUserActions.values();
      for (Iterator iterator = distinctCollection.iterator(); iterator.hasNext();) {
        OnlineUserAction onlineUserAction = (OnlineUserAction) iterator.next();
        retValue.add(onlineUserAction);
      }
    }

    // then get guest last
    for (Iterator guestIterator = collection.iterator(); guestIterator.hasNext();) {
      OnlineUser onlineUser = (OnlineUser) guestIterator.next();
      if (onlineUser.isGuest()) {
        OnlineUserAction onlineUserAction = onlineUser.getOnlineUserAction();
        retValue.add(onlineUserAction);
      }
    }
    return retValue;
  }

  public void updateOnlineUserAction(HttpServletRequest request, Action action)
      throws DatabaseException, AuthenticationException {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    updateOnlineUserAction(genericRequest, action);
  }

  public void updateOnlineUserAction(GenericRequest request, Action action)
      throws DatabaseException, AuthenticationException {

    if (action != null) {
      OnlineUser onlineUser = getOnlineUser(request);
      OnlineUserAction onlineUserAction = onlineUser.getOnlineUserAction();
      // the setAction has package-default access
      // so this is the only way to set action to a user
      onlineUserAction.setAction(action);
    }
  }

  public synchronized boolean isUserOnline(String username) {

    Collection collection = userMap.values();
    Iterator iterator = collection.iterator();
    while (iterator.hasNext()) {
      OnlineUser onlineUser = (OnlineUser) iterator.next();
      String currentUser = onlineUser.getMemberName();
      if (username.equalsIgnoreCase(currentUser)) {
        return true;
      }
    }
    return false;
  }

  /************************************************************************
   * private methods
   ************************************************************************/
  private synchronized OnlineUser getOnlineUser(String sessionID) {

    return (OnlineUser) userMap.get(sessionID);
  }

  private synchronized void setOnlineUser(String sessionID, OnlineUser user) {

    OnlineUser onlineUser = getOnlineUser(sessionID);
    if (null == user) {
      userMap.remove(sessionID);
    } else {
      userMap.put(sessionID, user);
    }

    if (onlineUser != null) {
      final int DONT_CARE = -1;
      fireUserLogout(new OnlineUserEvent(onlineUser, DONT_CARE));
    }
  }

  private synchronized void removeTimeoutUsers() {

    long currentTimeMillis = System.currentTimeMillis();
    // try to resolve problem with synchronization on the class-variable : timeOfLastRemoveAction
    if (currentTimeMillis - timeOfLastRemoveAction < REMOVE_INTERVAL) {
      return;
    }
    // okie now, go ahead
    timeOfLastRemoveAction = currentTimeMillis;

    Timestamp currentTime = DateUtil.getCurrentGMTTimestamp();

    Collection collection = userMap.values();
    Iterator iterator = collection.iterator();
    while (iterator.hasNext()) {
      OnlineUser onlineUser = (OnlineUser) iterator.next();
      OnlineUserAction onlineUserAction = onlineUser.getOnlineUserAction();
      long duration = currentTime.getTime() - onlineUserAction.getLastRequestTime().getTime();
      if (duration > MVNForumConfig.SESSION_DURATION) {
        iterator.remove();
        final int DONT_CARE = -1;
        fireUserLogout(new OnlineUserEvent(onlineUser, DONT_CARE));
      }
    }
  }

  public Map getUserMap() {
    return this.userMap;
  }

  /************************************************************************
   * Event method
   ************************************************************************/
  public synchronized void removeOnlineUserListener(OnlineUserListener listener) {
    onlineUserListeners.remove(listener);
  }

  public synchronized void addOnlineUserListener(OnlineUserListener listener) {
    onlineUserListeners.add(listener);
  }

  private void fireDataChanged(OnlineUserEvent e) {
    for (Iterator iterator = onlineUserListeners.iterator(); iterator.hasNext();) {
      ((OnlineUserListener) iterator.next()).onChange(e);
    }
  }

  private void fireUserLogout(OnlineUserEvent e) {
    for (Iterator iterator = onlineUserListeners.iterator(); iterator.hasNext();) {
      ((OnlineUserListener) iterator.next()).onLogout(e);
    }
  }

}
