/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/security/SecurityUtil.java,v 1.26
 * 2009/04/27 10:35:09 minhnn Exp $ $Author: minhnn $ $Revision: 1.26 $ $Date: 2009/04/27 10:35:09 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib MUST remain intact in the scripts
 * and source code.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Dung Bui
 */
package net.myvietnam.mvncore.security;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;

public class SecurityUtil {

  private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

  private SecurityUtil() {}

  public static void checkHttpPostMethod(HttpServletRequest request) {

    checkHttpPostMethod(request, true, false);
  }

  public static void checkHttpPostMethod(HttpServletRequest request, boolean checkReferer,
      boolean mutilpartForm) {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    checkHttpPostMethod(genericRequest, checkReferer, mutilpartForm);

  }

  public static void checkHttpPostMethod(GenericRequest genericRequest) {

    checkHttpPostMethod(genericRequest, true, false);
  }

  public static void checkHttpPostMethod(GenericRequest genericRequest, boolean checkReferer,
      boolean mutilpartForm) {

    if (checkReferer) {
      checkHttpReferer(genericRequest);
    }

    // we only be able to check POST method in Servlet, not Portlet
    if (genericRequest.isServletRequest()) {
      String method = genericRequest.getMethod();
      if (method.equalsIgnoreCase("POST") == false) {
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNCoreResourceBundle.getString(locale,
            "mvncore.exception.IllegalStateException.use_post_method");
        throw new IllegalStateException(localizedMessage);
      }
    }

    // because checkHttpPostMethod() is used so popular so we temporarily need this condition to
    // avoid error from not updated form with <mvn:securitytoken /> field
    if (mutilpartForm == false) {// ignore check security token because we can't get parameter from
                                 // multipart/form-data
      checkSecurityTokenMethod(genericRequest);
    }
  }

  public static void checkSecurityTokenMethod(HttpServletRequest request, String receivedToken) {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    checkSecurityTokenMethod(genericRequest, receivedToken);

  }

  public static void checkSecurityTokenMethod(HttpServletRequest request) {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    checkSecurityTokenMethod(genericRequest);

  }

  /**
   * Check security util that use for the upload form
   * 
   * @param genericRequest
   * @param receivedToken : this parameter could be null, which will also throw
   *        IllegalStateException
   */
  public static void checkSecurityTokenMethod(GenericRequest genericRequest, String receivedToken) {

    String currentToken = getSessionToken(genericRequest);
    // System.out.println("\n\n Received token: " + receivedToken +"\n\n");
    // System.out.println("\n\n Sent token: " + getSessionToken(genericRequest) +"\n\n");
    if (currentToken.equals(receivedToken) == false) {
      // throw new IllegalStateException("Token not matched or your session has been expired, please
      // try again.");
      Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
      String localizedMessage = MVNCoreResourceBundle.getString(locale,
          "mvncore.exception.IllegalStateException.invalid_security_token");
      throw new IllegalStateException(localizedMessage);
    }

    log.debug("Check security token successfully on " + System.currentTimeMillis());

  }

  public static void checkSecurityTokenMethod(GenericRequest genericRequest) {

    String receivedToken =
        GenericParamUtil.getParameter(genericRequest, MVNCoreGlobal.MVNCORE_SECURITY_TOKEN);
    checkSecurityTokenMethod(genericRequest, receivedToken);
  }

  public static void checkHttpReferer(HttpServletRequest request) {

    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    checkHttpReferer(genericRequest);

  }

  public static void checkHttpReferer(GenericRequest request) {

    String allowListStr = MVNCoreConfig.getAllowHttpRefererPrefixList();
    if ("allow_all_referer".equals(allowListStr)) {
      return;
    }
    if (request.isPortletRequest()) {
      return;
    }

    String allowedLists[] = MVNCoreConfig.getAllowHttpRefererPrefixListArray();
    boolean isValid = false;

    String referer = StringUtil.getEmptyStringIfNull(request.getReferer()).trim().toLowerCase();
    for (int i = 0; i < allowedLists.length; i++) {
      if (referer.startsWith(allowedLists[i])) {
        isValid = true;
        break;
      }
    }
    if (isValid == false) {
      log.debug("Referer " + referer + " is not in the trusted domains.");
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNCoreResourceBundle.getString(locale,
          "mvncore.exception.IllegalStateException.not_in_allowed_referer_list");
      throw new IllegalStateException(localizedMessage);
    }

  }

  public static synchronized String getSessionToken(HttpServletRequest request) {
    GenericRequest genericRequest = new GenericRequestServletImpl(request);
    return getSessionToken(genericRequest);
  }

  public static synchronized String getSessionToken(GenericRequest request) {
    String token = (String) request.getSessionAttribute(MVNCoreGlobal.MVNCORE_SESSION_TOKEN,
        GenericRequest.APPLICATION_SCOPE);
    if ((token == null) || (token.length() == 0)) {
      token = RandomGenerator.getRandomMD5_Hex();
      request.setSessionAttribute(MVNCoreGlobal.MVNCORE_SESSION_TOKEN, token,
          GenericRequest.APPLICATION_SCOPE);
    }
    return token;
  }
}
