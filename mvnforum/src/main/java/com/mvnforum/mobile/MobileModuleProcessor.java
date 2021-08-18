/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/mobile/MobileModuleProcessor.java,v 1.13
 * 2009/02/02 08:51:34 minhnn Exp $ $Author: minhnn $ $Revision: 1.13 $ $Date: 2009/02/02 08:51:34 $
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
 * @author: Nhan Luu Duy
 */
package com.mvnforum.mobile;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.Action;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.service.ModuleProcessor;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.exception.NotLoginException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;
import net.myvietnam.mvncore.web.impl.GenericResponseServletImpl;

public class MobileModuleProcessor implements ModuleProcessor {

  private static final Logger log = LoggerFactory.getLogger(MobileModuleProcessor.class);

  protected static final String ORIGINAL_REQUEST = "mvnforum.user.OriginalRequest";

  private static int count;

  private HttpServlet mainServlet = null;
  protected ServletContext servletContext = null;

  protected OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
  protected MobileModuleURLMapHandler urlMapHandler = new MobileModuleURLMapHandler();

  public MobileModuleProcessor() {
    count++;
    AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
  }

  @Override
  public void setServlet(HttpServlet servlet) {
    mainServlet = servlet;
    servletContext = servlet.getServletContext();
  }

  @Override
  public String process(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
    String responseURI = null;
    OnlineUser onlineUser = null;

    long start = 0;
    if (log.isDebugEnabled()) {
      start = System.currentTimeMillis();
      log.debug("MobileModuleProcessor : requestURI  = " + requestURI);
    }

    GenericRequest genericRequest = new GenericRequestServletImpl(request, servletContext);
    GenericResponse genericResponse = new GenericResponseServletImpl(response);

    // step 1: some command need to be processed before we do the URI mapping (of the MODAL)
    // MODAL processing
    try {
      // TODO could throw Exception, so onlineUser will be null, caused NPE later
      onlineUser = onlineUserManager.getOnlineUser(genericRequest);
      onlineUser.updateNewMessageCount(false);

      if (requestURI.equals("/index")) {

      } else if (requestURI.equals("/error")) {

      }
    } catch (AuthenticationException e) {
      if (e.getReason() == NotLoginException.NOT_ACTIVATED) {
        // TODO mobile module does not support sendactivationcode
        requestURI = "/sendactivationcode";
      } else {
        // make sure not from login page, we cannot set original request in this situation
        // and also make sure the request's method must be GET to set the OriginalRequest
        boolean shouldSaveOriginalRequest = (e.getReason() == NotLoginException.NOT_LOGIN)
            || (e.getReason() == NotLoginException.NOT_ENOUGH_RIGHTS);
        if (shouldSaveOriginalRequest && (request.getMethod().equals("GET"))) {
          String url = MobileModuleConfig.getUrlPattern() + requestURI;
          if (request.getQueryString() != null) {
            url = url + "?" + request.getQueryString();
          }
          request.getSession().setAttribute(ORIGINAL_REQUEST, url);
        }
        if (MVNForumConfig.getRedirectLoginURL().equals(MVNForumConfig.DEFAULT)) {
          requestURI = "/login";
        } else {
          responseURI = MVNForumConfig.getRedirectLoginURL();
        }

        if (onlineUser != null) {
          request.setAttribute("Reason", e.getReasonExplanation(onlineUser.getLocale()));
        } else {
          request.setAttribute("Reason", e.getReasonExplanation());
          log.error("Error cannot get online user", e);
        }
      }
    } catch (Throwable e) {
      if (e instanceof BadInputException) {
        // we log in WARN level if this is the exception from user input
        log.warn("Exception in MobileModuleProcessor e = " + e.getMessage(), e);
      } else if (e instanceof AssertionError) {
        // we log in FATAL level if this is the exception from user input
        log.error("Exception in MobileModuleProcessor e = " + e.getMessage(), e);
      } else {
        log.error("Exception in MobileModuleProcessor [" + e.getClass().getName() + "] : "
            + e.getMessage(), e);
      }
      requestURI = "/error";
      String message = StringUtil.getEmptyStringIfNull(e.getMessage());
      if (message.length() == 0) {
        message = e.getClass().getName();
      }
      request.getSession().setAttribute("ErrorMessage", DisableHtmlTagFilter.filter(message));
    }

    // step 2: map the URI (of the CONTROLLER)
    try {
      // NOTE 1: there is one situation when responseURI != null (after login successfully for the
      // first time),
      // but since it will make a NEW request via sendRedirect, so we don't count
      // NOTE 2: there are 2 situation when requestURI is different from the original requestURI
      // that is /login and /error, because of this so we must pass the requestURI
      /* TODO Could below the MapHandler ??? */

      Action action = new ActionInForumMobileModule(genericRequest, requestURI);// may throw
                                                                                // MissingURLMapEntryException
      onlineUserManager.updateOnlineUserAction(genericRequest, action);

      // now updateOnlineUserAction is ok, we go ahead
      if (responseURI == null) {
        URLMap map = urlMapHandler.getMap(requestURI, genericRequest, onlineUser.getLocaleName());
        responseURI = map.getResponse();
      } // if
    } catch (MissingURLMapEntryException e) {
      log.error("Exception: missing urlmap entry in forum module: requestURI = " + requestURI);
      responseURI = "/WEB-INF/mvnplugin/mvnforum/mobile/error.jsp";
      request.getSession().setAttribute("ErrorMessage",
          DisableHtmlTagFilter.filter(e.getMessage()));
    } catch (Throwable e) {
      // This will catch AuthenticationException, AssertionError, DatabaseException
      // in the method onlineUserManager.updateOnlineUserAction(request, action)
      responseURI = "/WEB-INF/mvnplugin/mvnforum/mobile/error.jsp";
      request.getSession().setAttribute("ErrorMessage",
          DisableHtmlTagFilter.filter(e.getMessage()));
    }

    // step 3: forward or dispatch to the VIEW
    if (log.isDebugEnabled()) {
      long duration = System.currentTimeMillis() - start;
      log.debug("MobileModuleProcessor : responseURI = " + responseURI + ". (" + duration + " ms)");
    }

    return responseURI;
  }
}
