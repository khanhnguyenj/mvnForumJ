/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/ForumUserServlet.java,v 1.89 2009/12/21
 * 07:57:48 minhnn Exp $ $Author: minhnn $ $Revision: 1.89 $ $Date: 2009/12/21 07:57:48 $
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

import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.ManagerFactory;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserAction;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.service.CentralAuthenticationService;
import com.mvnforum.service.ModuleProcessor;
import com.mvnforum.service.MvnForumInfoService;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.impl.MvnForumLifeCycleServiceImplDefault;
import lombok.extern.slf4j.Slf4j;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.FloodException;
import net.myvietnam.mvncore.filter.UserAgentFilter;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.FloodControlMinute;
import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.IPFilterService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.impl.MvnCoreLifeCycleServiceImplDefault;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.ServletUtil;

@Slf4j
public class ForumUserServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static int count = 0;

  private ModuleProcessor userModuleProcessor = null;
  private MvnForumInfoService mvnForumInfo =
      MvnForumServiceFactory.getMvnForumService().getMvnForumInfoService();
  private IPFilterService ipFilterService =
      MvnCoreServiceFactory.getMvnCoreService().getIPFilterService();
  private EnvironmentService environmentService =
      MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
  private CentralAuthenticationService centralAuthenticationService =
      MvnForumServiceFactory.getMvnForumService().getCentralAuthenticationService();
  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  /** Initialize global variables */
  @Override
  public void init() throws ServletException {
    userModuleProcessor = MvnForumServiceFactory.getMvnForumService().getModuleProcessorService()
        .getUserModuleProcessor();
    userModuleProcessor.setServlet(this);

    if (MvnCoreLifeCycleServiceImplDefault.isCalled() == false) {
      environmentService.setShouldStop("MvnCoreLifeCycleServiceImplDefault has not been called");
    }

    if (MvnForumLifeCycleServiceImplDefault.isCalled() == false) {
      environmentService.setShouldStop("MvnForumLifeCycleServiceImplDefault has not been called");
    }

    log.info("<<---- ForumUserServlet has been inited. Detailed info: "
        + mvnForumInfo.getProductVersion() + " (Build: " + mvnForumInfo.getProductReleaseDate()
        + ") ---->>");
  }

  /** Process the HTTP Get request */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    process(request, response);
  }

  /** Process the HTTP Post request */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    process(request, response);
  }

  public void process(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    if (environmentService.isShouldRun() == false) {
      String error = "Cannot init system. Reason : " + environmentService.getReason();
      request.setAttribute("fatal_error_message", error);
      getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/mvnfatalerror.jsp")
          .forward(request, response);
      return;
    }

    long startTime = 0;
    if (log.isDebugEnabled()) {
      startTime = System.currentTimeMillis();
    }
    count++;
    try {
      String currentIP = request.getRemoteAddr();

      if (ServletUtil.isIncludeRequest(request) == false) {
        // Control the HTTP request, we don't want user to try too many requests
        try {
          FloodControlMinute.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP,
              currentIP);
        } catch (FloodException fe) {
          Locale locale = MVNForumConfig.getDefaultLocale();
          if (onlineUserManager.isAlreadyOnlineUser(request)) {
            locale = onlineUserManager.getOnlineUser(request).getLocale();
          }
          String message = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.FloodException.request_in_minute_too_many_times",
              new Object[] {new Integer(MVNForumConfig.getMaxHttpRequestsPerMinutePerIP())});
          request.setAttribute("FloodMessage", message);
          response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
          getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/max_http_request.jsp")
              .forward(request, response);
          return;
        }
        FloodControlMinute.increaseCount(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);

        try {
          FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP,
              currentIP);
        } catch (FloodException fe) {
          Locale locale = MVNForumConfig.getDefaultLocale();
          if (onlineUserManager.isAlreadyOnlineUser(request)) {
            locale = onlineUserManager.getOnlineUser(request).getLocale();
          }
          String message = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.FloodException.request_in_hour_too_many_times",
              new Object[] {new Integer(MVNForumConfig.getMaxHttpRequestsPerHourPerIP())});
          request.setAttribute("FloodMessage", message);
          response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
          getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/max_http_request.jsp")
              .forward(request, response);
          return;
        }
        FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);
      }

      if (ipFilterService.filter(request) == false) {
        getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/404.jsp").forward(request,
            response);
        return;
      }
      if (UserAgentFilter.filter(request) == false) {
        getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/404.jsp").forward(request,
            response);
        return;
      }

      // request.setCharacterEncoding("utf-8");
      centralAuthenticationService.trackTicket(request);

      String responseURI = null;
      responseURI = ManagerFactory.getRequestProcessor().preLogin(request, response);

      OnlineUser onlineUser = null;
      // This will make sure that the user information is put in the request.
      try {
        onlineUser = onlineUserManager.getOnlineUser(request);// could throw DatabaseException :-(
      } catch (Throwable e) {
        log.error("Error in ForumUserServlet when getOnlineUser()", e);
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        getServletContext().getRequestDispatcher("/mvnplugin/mvnforum/systemoverloaded.jsp")
            .forward(request, response);
        return;
      }

      OnlineUserAction action = onlineUser.getOnlineUserAction();
      if (action.getRemoteAddr().equals(currentIP) == false) {
        // we will forward if this user is logged in, and ignore if he is Guest
        if (onlineUser.isMember() && MVNForumConfig.getEnableCheckInvalidSession()
            && (onlineUser.getAuthenticationType() != OnlineUser.AUTHENTICATION_TYPE_COOKIE)) {
          request.getRequestDispatcher("/mvnplugin/mvnforum/invalidsession.jsp").forward(request,
              response);
          return;
        }
      }

      // check the module is on. Otherwise, go to message inform that
      // the module is Off now. Please access later when the admin change mode to on
      if (MVNForumConfig.getShouldShowUserArea() == false) {
        if (!onlineUser.getPermission().canAdminSystem()) {
          request.getRequestDispatcher("/mvnplugin/mvnforum/turnoff.jsp").forward(request,
              response);
          return;
        }
      }

      // Config.set(request, Config.FMT_FALLBACK_LOCALE, "en");
      String localeName = ParamUtil.getParameter(request, MVNForumConfig.getLocaleParameterName());
      if (MVNForumConfig.supportLocale(localeName)) {
        onlineUser.setLocaleName(localeName, request, response);
      }
      I18nUtil.setLocaleInRequest(request, onlineUser.getLocale());

      // save Vietnamese typer mode if exists one
      // MyUtil.saveVNTyperMode(request, response);

      if (responseURI == null) {
        responseURI = ManagerFactory.getRequestProcessor().preProcess(request, response);
      }

      if (responseURI == null) {
        // this method should not throw Exception (it must catch all Exceptions)
        responseURI = userModuleProcessor.process(request, response);
      }

      responseURI =
          ManagerFactory.getRequestProcessor().postProcess(request, response, responseURI);
      if (responseURI != null) {
        if (response.isCommitted() == false) {
          if (responseURI.startsWith("http://") || responseURI.startsWith("https://")) {
            response.sendRedirect(responseURI);
          } else {
            if (responseURI.endsWith(".jsp")) {
              // this will cause the exception compiler.Compiler if the jsp cannot be compiled,
              // which cause EnvironmentService.setShouldStop to be called below

              if ((responseURI.indexOf("votepollx") != -1) || (responseURI.indexOf("poll_") != -1)
                  || (responseURI.indexOf("error_") != -1)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(responseURI);
                if (dispatcher != null) {
                  dispatcher.include(request, response);
                } else {
                  log.warn("Cannot get RequestDispatcher for responseURI = " + responseURI);
                }
              } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher(responseURI);
                if (dispatcher != null) {
                  dispatcher.forward(request, response);
                } else {
                  log.warn("Cannot get RequestDispatcher for responseURI = " + responseURI);
                }
              }
            } else {
              response.sendRedirect(request.getContextPath() + responseURI);
            }
          }
        } else {// already committed, only accept include
          if (responseURI.startsWith("http://") || responseURI.startsWith("https://")) {
            // do nothing
          } else {
            if (responseURI.endsWith(".jsp")) {
              // this will cause the exception compiler.Compiler if the jsp cannot be compiled,
              // which cause EnvironmentService.setShouldStop to be called below

              if ((responseURI.indexOf("votepollx") != -1) || (responseURI.indexOf("poll_") != -1)
                  || (responseURI.indexOf("error_") != -1)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher(responseURI);
                if (dispatcher != null) {
                  dispatcher.include(request, response);
                } else {
                  log.warn("Cannot get RequestDispatcher for responseURI = " + responseURI);
                }
              }
            }
          }
        }
      }
    } catch (SocketException e) {
      // in WebLogic, when loading and close the browser, SocketException is thrown
      log.warn("Exception in ForumUserServlet", e);
    } catch (Throwable e) {
      environmentService.setShouldStop("Assertion in ForumUserServlet.",
          EnvironmentService.DEFAULT_RUN_RETRY);

      if (e instanceof BadInputException) {
        // we log in WARN level if this is the exception from user input
        log.warn("Exception in ForumUserServlet e = " + e.getMessage(), e);
      } else if (e instanceof AssertionError) {
        // we log in FATAL level if this is the exception from user input
        log.error("Exception in ForumUserServlet e = " + e.getMessage(), e);
      } else {
        log.error(
            "Exception in ForumUserServlet [" + e.getClass().getName() + "] : " + e.getMessage(),
            e);
      }
    } finally {
      if (log.isDebugEnabled()) {
        long processTime = System.currentTimeMillis() - startTime;
        log.debug("ForumUserServlet processed " + count + " times. Took " + processTime
            + " milliseconds.\n");
      }
    }
  }// process

  /**
   * Clean up resources
   */
  @Override
  public void destroy() {
    log.info("<<---- ForumUserServlet has been destroyed. ---->>");
  }

}
