/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/DeliveryServlet.java,v 1.14 2009/12/21 08:53:56 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.14 $
 * $Date: 2009/12/21 08:53:56 $
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
package com.mvnsoft.mvnad.delivery;

import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.FloodException;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.FloodControlMinute;
import net.myvietnam.mvncore.util.ServletUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnsoft.mvnad.service.MvnAdModuleProcessor;
import com.mvnsoft.mvnad.service.MvnAdServiceFactory;

public class DeliveryServlet extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(DeliveryServlet.class);
    
    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    
    private MvnAdModuleProcessor deliveryProcessor = null;

    public void init() throws ServletException {
        deliveryProcessor = MvnAdServiceFactory.getMvnAdService().getMvnAdModuleProcessorService().getDeliveryModuleProcessor(); 
            //new DeliveryProcessor(this);
        log.info("DeliveryServlet as been initialized!");
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        try {
            String currentIP = request.getRemoteAddr();
            
            if (ServletUtil.isIncludeRequest(request) == false) {
                // Control the HTTP request, we don't want user to try too many request
                try {
                    FloodControlMinute.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);
                } catch (FloodException fe) {
                    Locale locale = MVNForumConfig.getDefaultLocale();
                    if (onlineUserManager.isAlreadyOnlineUser(request)) {
                        locale = onlineUserManager.getOnlineUser(request).getLocale();
                    }
                    String message = MVNForumResourceBundle.getString(locale, "mvncore.exception.FloodException.request_in_minute_too_many_times", new Object[] {new Integer(MVNForumConfig.getMaxHttpRequestsPerMinutePerIP())});
                    request.setAttribute("FloodMessage", message);
                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                    getServletContext().getRequestDispatcher("/mvnplugin/mvnad/max_http_request.jsp").forward(request, response);
                    return;
                }
                FloodControlMinute.increaseCount(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);
    
                try {
                    FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);
                } catch (FloodException fe) {
                    Locale locale = MVNForumConfig.getDefaultLocale();
                    if (onlineUserManager.isAlreadyOnlineUser(request)) {
                        locale = onlineUserManager.getOnlineUser(request).getLocale();
                    }
                    String message = MVNForumResourceBundle.getString(locale, "mvncore.exception.FloodException.request_in_hour_too_many_times", new Object[] {new Integer(MVNForumConfig.getMaxHttpRequestsPerHourPerIP())});
                    request.setAttribute("FloodMessage", message);
                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                    getServletContext().getRequestDispatcher("/mvnplugin/mvnad/max_http_request.jsp").forward(request, response);
                    return;
                }
                FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP, currentIP);
            }
            
            request.setCharacterEncoding("utf-8");

            String responseURI = deliveryProcessor.process(request, response);
            
            if ((responseURI != null) && (response.isCommitted() == false)) {
                if (responseURI.startsWith("http://") || responseURI.startsWith("https://")) {
                    response.sendRedirect(responseURI);
                } else {
                    if (responseURI.endsWith(".jsp")) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher(responseURI);
                        if (dispatcher != null) {
                            dispatcher.forward(request, response);
                        } else {
                            log.warn("Cannot get RequestDispatcher for responseURI = " + responseURI);
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + responseURI);
                    }
                }
            }
        } catch (SocketException e) {
            // in WebLogic, when loading and close the browser, SocketException is thrown
            log.warn("Exception in DeliveryServlet", e);
        } catch (Exception e) {
            log.error("Assertion Error: ", e);
        }
    }

    public void destroy() {
        DBUtils.closeAllConnections();
        log.info("DeliveryServlet as been destroyed!");
    }

}
