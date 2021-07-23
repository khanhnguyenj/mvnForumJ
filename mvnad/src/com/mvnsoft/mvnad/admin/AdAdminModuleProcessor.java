/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/AdAdminModuleProcessor.java,v 1.15 2009/05/07 02:35:42 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.15 $
 * $Date: 2009/05/07 02:35:42 $
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
package com.mvnsoft.mvnad.admin;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;
import net.myvietnam.mvncore.web.impl.GenericResponseServletImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.*;
import com.mvnsoft.mvnad.service.MvnAdModuleProcessor;

public class AdAdminModuleProcessor implements MvnAdModuleProcessor {

    private static final Logger        log                = LoggerFactory.getLogger(AdAdminModuleProcessor.class);
    private static int count;

    private final String               ORIGINAL_REQUEST   = "mvn.ad.admin.OriginalRequest";
    
    private HttpServlet                adminServlet       = null;
    protected ServletContext           servletContext     = null;
    private OnlineUserManager          onlineUserManager  = OnlineUserManager.getInstance();
    private AdAdminModuleURLMapHandler urlMapHandler      = new AdAdminModuleURLMapHandler();
    private AdAdminWebHandler          adAdminWebHandler  = new AdAdminWebHandler();
    private BannerWebHandler           bannerWebHandler   = new BannerWebHandler();
    private ZoneWebHandler             zoneWebHandler     = new ZoneWebHandler();
    private ConfigForumWebHandler   configForumWebHandler = new ConfigForumWebHandler();

    public AdAdminModuleProcessor() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public void setServlet(HttpServlet servlet) {
        adminServlet = servlet;
        servletContext  = adminServlet.getServletContext();
    }

    /**
     * This method handles the <code>requestURI</code>, and invokes the needed procedure (if the current user has the
     * permission to perform that task). <br/>For example, URI <code>"/addforumprocess"</code> invokes the call to
     * <code>forumWebHandler.processAdd(request);</code>. <br/>After the task is performed, we use the
     * <code>AdminModuleURLMapHandler</code> to get the <code>responseURI</code>. That <code>responseURI</code>
     * is returned back to the calling method, so it can decide and act on it (to redirect to that URI). <br/>
     * 
     * @param request
     *            The <code>HttpServletRequest</code> object of this HTTP request.
     * @param response
     *            The <code>HttpServletResponse</code> object of this HTTP request.
     * @return responseURI to be redirected to. <b>It could be null </b>, which means we are not supposed to do any
     *         redirection, since the output was already commited (for example, if we sent (downloaded) a file to the
     *         user.
     */
    public String process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        long start = 0;
        String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
        String responseURI = null;
        OnlineUser onlineUser = null;
        if (log.isDebugEnabled()) {
            start = System.currentTimeMillis();
            log.debug("AdminModuleProcessor : requestURI  = " + requestURI);
        }
        
        GenericRequest genericRequest = new GenericRequestServletImpl(request, servletContext);
        GenericResponse genericResponse = new GenericResponseServletImpl(response);

        // step 1: some command need to be processed before we do the URI
        // mapping (of the MODAL)
        try {
            // always check for Authenticated User
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
             
            if (requestURI.equals("/index")) {
                adAdminWebHandler.prepareShowIndex(genericRequest);
                
            } else if (requestURI.equals("/addzone")) {
                zoneWebHandler.prepareAddZone(genericRequest);
            } else if (requestURI.equals("/addzoneprocess")) {
                zoneWebHandler.processAddZone(genericRequest);
            } else if (requestURI.equals("/editzone")) {
                zoneWebHandler.prepareEditZone(genericRequest);
            } else if (requestURI.equals("/editzoneprocess")) {
                zoneWebHandler.processUpdateZone(genericRequest);
            } else if (requestURI.equals("/deletezone")) {
                zoneWebHandler.prepareDeleteZone(genericRequest);
            } else if (requestURI.equals("/deletezoneprocess")) {
                zoneWebHandler.processDeleteZone(genericRequest);
            } else if (requestURI.equals("/listzones")) {
                zoneWebHandler.listZones(genericRequest);
                
            } else if (requestURI.equals("/addbanner")) {
                bannerWebHandler.prepareAddBanner(genericRequest);
            } else if (requestURI.equals("/addbannerprocess")) {
                bannerWebHandler.processAddBanner(genericRequest);
            } else if (requestURI.equals("/editbanner")) {
                bannerWebHandler.prepareEditBanner(genericRequest);
            } else if (requestURI.equals("/editbannerprocess")) {
                bannerWebHandler.processUpdateBanner(genericRequest);
            } else if (requestURI.equals("/uploadmediaprocess")) {
                bannerWebHandler.processUploadMedia(genericRequest, genericResponse);
            } else if (requestURI.equals("/deletebanner")) {
                bannerWebHandler.prepareDeleteBanner(genericRequest);
            } else if (requestURI.equals("/deletebannerprocess")) {
                bannerWebHandler.processDeleteBanner(genericRequest);
            } else if (requestURI.equals("/listbanners")) {
                bannerWebHandler.prepareListBanners(genericRequest);
                
            } else if (requestURI.equals("/managemedia")) {
                bannerWebHandler.prepareManageMedia(genericRequest);
            } else if (requestURI.equals("/deletemedia")) {
                bannerWebHandler.processDeleteMedia(genericRequest);
                bannerWebHandler.prepareManageMedia(genericRequest); 
                
            } else if (requestURI.equals("/getcode")) {
                adAdminWebHandler.prepareGetCode(genericRequest);
            } else if (requestURI.equals("/addbannerofzone")) {
                adAdminWebHandler.prepareAddBannerOfZone(genericRequest);
            } else if (requestURI.equals("/addbannerofzoneprocess")) {
                adAdminWebHandler.processAddZoneBanner(genericRequest);
            } else if (requestURI.equals("/editbannerofzone")) {
                adAdminWebHandler.prepareEditBannerOfZone(genericRequest);
            } else if (requestURI.equals("/editbannerofzoneprocess")) {
                adAdminWebHandler.processEditBannerOfZone(genericRequest);
            } else if (requestURI.equals("/deletebannerofzone")) {
                adAdminWebHandler.deleteBannerOfZone(genericRequest);

            } else if (requestURI.equals("/configforum")) {
                configForumWebHandler.prepare(genericRequest);
            } else if (requestURI.equals("/configforumprocess")) {
                configForumWebHandler.process(genericRequest);
             
            } else if (requestURI.equals("/loginprocess")) {
                onlineUserManager.processLogin(request, response, true);
                String originalRequest = ParamUtil.getAttribute(request.getSession(), ORIGINAL_REQUEST);
                if (originalRequest.length() > 0) {
                    request.getSession().setAttribute(ORIGINAL_REQUEST, "");
                    responseURI = originalRequest;
                }
            } else if (requestURI.equals("/logout")) {
                onlineUserManager.logout(request, response);
                request.setAttribute("Reason", "Logout successfully.");
            }
        } catch (AuthenticationException e) {
            // make sure not from login page, we cannot set original request in this situation
            // and also make sure the request's method must be GET to set the OriginalRequest
            boolean shouldSaveOriginalRequest = (e.getReason() == NotLoginException.NOT_LOGIN)
                    || (e.getReason() == NotLoginException.NOT_ENOUGH_RIGHTS);
            if (shouldSaveOriginalRequest && (request.getMethod().equals("GET"))) {
                String url = AdAdminModuleConfig.getUrlPattern() + requestURI + "?"
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
                log.warn("Exception in AdAdminModuleProcessor e = " + e.getMessage(), e);
            } else if (e instanceof AssertionError) {
                // we log in FATAL level if this is the exception from user input
                log.error("Exception in AdAdminModuleProcessor e = " + e.getMessage(), e);
            } else {
                log.error("Exception in AdAdminModuleProcessor [" + e.getClass().getName() + "] : " + e.getMessage(), e);
            }
            requestURI = "/error";
            request.getSession().setAttribute("ErrorMessage", StringUtil.getEmptyStringIfNull(e.getMessage()));
        }

        // step 2: map the URI (of the CONTROLLER)
        try {
            // See note in the ActionInUserModule
            Action action = new ActionInAdAdminModule(genericRequest, requestURI);// may throw MissingURLMapEntryException
            onlineUserManager.updateOnlineUserAction(genericRequest, action);

            if (responseURI == null) {
                URLMap map = urlMapHandler.getMap(requestURI, genericRequest);
                responseURI = map.getResponse();
            }
        } catch (MissingURLMapEntryException e) {
            log.error("Exception: missing urlmap entry in admin module: requestURI = " + requestURI);
            responseURI = "/mvnplugin/mvnad/admin/error.jsp";
            request.getSession().setAttribute("ErrorMessage", e.getMessage());
        } catch (Throwable e) {
            // This will catch AuthenticationException, AssertionError, DatabaseException
            // in the method onlineUserManager.updateOnlineUserAction(request, action)
            responseURI = "/mvnplugin/mvnad/admin/error.jsp";
            request.getSession().setAttribute("ErrorMessage", e.getMessage());
        }

        // step 3: return URI to be forwarded to or dispatched to the VIEW
        if (log.isDebugEnabled()) {
            long duration = System.currentTimeMillis() - start;
            log.debug("AdminModuleProcessor: responseURI = " + responseURI + ". (" + duration + " ms)\n");
        }

        return responseURI;
    }// process method
}
