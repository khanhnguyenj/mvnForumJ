/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/user/AdUserModuleProcessor.java,v 1.12 2009/02/02 09:00:06 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2009/02/02 09:00:06 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.user;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.*;
import com.mvnsoft.mvnad.service.MvnAdModuleProcessor;

public class AdUserModuleProcessor implements MvnAdModuleProcessor {

    private static final Logger     log               = LoggerFactory.getLogger(AdUserModuleProcessor.class);
    private static int count;
    
    private final String            ORIGINAL_REQUEST  = "ad.user.OriginalRequest";
    
    private HttpServlet             userServlet       = null;
    protected ServletContext        servletContext    = null;
    private OnlineUserManager       onlineUserManager = OnlineUserManager.getInstance();
    private AdUserModuleURLMapHandler urlMapHandler   = new AdUserModuleURLMapHandler();
    private AdUserWebHandler        adUserWebHandler  = new AdUserWebHandler();
    
    public AdUserModuleProcessor() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public void setServlet(HttpServlet servlet) {
        userServlet = servlet;
        servletContext = userServlet.getServletContext();
    }

    public String process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        long start = 0;
        String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
        String responseURI = null;
        OnlineUser onlineUser = null;
        if (log.isDebugEnabled()) {
            start = System.currentTimeMillis();
            log.debug("AdUserModuleProcessor : requestURI  = " + requestURI);
        }
        
        GenericRequest genericRequest   = new GenericRequestServletImpl(request, servletContext);

        // step 1: some command need to be processed before we do the URI
        // mapping (of the MODAL)
        // MODAL processing
        try {
            onlineUser = onlineUserManager.getOnlineUser(request);
            MVNForumPermission permission = onlineUser.getPermission();
            if (!requestURI.equals("") && 
                !requestURI.equals("/") && 
                !requestURI.equals("/login") && 
                !requestURI.equals("/loginprocess") && 
                !requestURI.equals("/logout")) {
                
                permission.ensureIsAuthenticated();
                if (onlineUser.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_COOKIE) {
                    throw new AuthenticationException(NotLoginException.COOKIE_NOT_ALLOWED);
                }
            }
            
            if (requestURI.equals("/logout")) {
                onlineUserManager.logout(request, response);
                request.setAttribute("Reason", "Logout successfully.");
            } else if (requestURI.equals("/listbanners")) {
                adUserWebHandler.listBanners(genericRequest);
                
            } else if (requestURI.equals("/loginprocess")) {
                if (MVNForumConfig.getEnableLogin() == false) {
                    throw new AuthenticationException(NotLoginException.LOGIN_DISABLED);
                }
                onlineUserManager.processLogin(request, response, true);
                adUserWebHandler.listBanners(genericRequest);                
                String originalRequest = ParamUtil.getAttribute(request.getSession(), ORIGINAL_REQUEST);
                if (originalRequest.length() > 0) {
                    request.getSession().setAttribute(ORIGINAL_REQUEST, "");
                    responseURI = originalRequest;
                }
            }
        } catch (AuthenticationException e) {
            // make sure not from login page, we cannot set original request in this situation
            // and also make sure the request's method must be GET to set the OriginalRequest
            boolean shouldSaveOriginalRequest = (e.getReason() == NotLoginException.NOT_LOGIN)
                    || (e.getReason() == NotLoginException.NOT_ENOUGH_RIGHTS);
            if (shouldSaveOriginalRequest && (request.getMethod().equals("GET"))) {
                String url = AdUserModuleConfig.getUrlPattern() + requestURI + "?"
                        + StringUtil.getEmptyStringIfNull(request.getQueryString());
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
        } catch (Throwable e) {
            if (e instanceof BadInputException) {
                // we log in WARN level if this is the exception from user input
                log.warn("Exception in AdUserModuleProcessor e = " + e.getMessage(), e);
            } else if (e instanceof AssertionError) {
                // we log in FATAL level if this is the exception from user input
                log.error("Exception in AdUserModuleProcessor e = " + e.getMessage(), e);
            } else {
                log.error("Exception in AdUserModuleProcessor [" + e.getClass().getName() + "] : " + e.getMessage(), e);
            }
            requestURI = "/error";
            request.getSession().setAttribute("ErrorMessage", StringUtil.getEmptyStringIfNull(e.getMessage()));            
        }
        
        // step 2: map the URI (of the CONTROLLER)
        try {
            // NOTE 1: there is one situation when responseURI != null (after login successfully for the first time),
            //         but since it will make a NEW request via sendRedirect, so we do not count
            // NOTE 2: there are 2 situation when requestURI is different from the original requestURI
            //          that is /login and /error, because of this so we must pass the requestURI
            /* TODO Could below the MapHandler ??? */
            Action action = new ActionInAdUserModule(genericRequest, requestURI);// may throw MissingURLMapEntryException
            onlineUserManager.updateOnlineUserAction(genericRequest, action);
            // now updateOnlineUserAction is OK, we go ahead
            if (responseURI == null) {
                URLMap map = urlMapHandler.getMap(requestURI, request);
                responseURI = map.getResponse();
            }
        } catch (MissingURLMapEntryException e) {
            log.error("Exception: missing urlmap entry in ad user module: requestURI = " + requestURI);
            responseURI = "/mvnplugin/mvnad/user/error.jsp";
            request.getSession().setAttribute("ErrorMessage", e.getMessage());
        } catch (Throwable e) {
            // This will catch AuthenticationException, AssertionError, DatabaseException
            // in the method onlineUserManager.updateOnlineUserAction(request, action)
            responseURI = "/mvnplugin/mvnad/user/error.jsp";
            request.getSession().setAttribute("ErrorMessage", e.getMessage());
        }

        // step 3: forward or dispatch to the VIEW
        if (log.isDebugEnabled()) {
            long duration = System.currentTimeMillis() - start;
            log.debug("AdUserModuleProcessor : responseURI = " + responseURI + ". (" + duration + " ms)\n");
        }
        return responseURI;
    }
}
