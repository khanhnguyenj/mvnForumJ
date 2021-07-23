/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/DeliveryProcessor.java,v 1.9 2009/09/10 03:15:00 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/09/10 03:15:00 $
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

import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnsoft.mvnad.AdModuleUtils;
import com.mvnsoft.mvnad.service.MvnAdModuleProcessor;

public class DeliveryProcessor implements MvnAdModuleProcessor {
    
    private static final Logger     log             = LoggerFactory.getLogger(DeliveryProcessor.class);
    private static int count;

    private HttpServlet             deliveryServlet = null;
    private DeliveryHandler         deliveryHander  = new DeliveryHandler();
    private DeliveryURLMapHandler   urlMapHandler   = new DeliveryURLMapHandler();
    
    public DeliveryProcessor() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public void setServlet(HttpServlet servlet) {
        deliveryServlet = servlet;
    }
    
    public String process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
        String responseURI = null;
        if (log.isDebugEnabled()) {
            log.debug("DeliveryProcessor: requestURI = " + requestURI);
        }
        
        try {
            if (requestURI.equals("/clickad")) {
                deliveryHander.processClickAd(request, response);
            } else if (requestURI.equals("/getad")) {
                deliveryHander.getZoneAd(request);
            } else if (requestURI.equals("/getadframe")) {
                deliveryHander.getAdFrame(request);

            } else if (requestURI.equals("/getmvnadimage")) {
                AdModuleUtils.writeMvnAdImage(response);
                return null;

            } else {
                // do nothing
            }
        } catch (Throwable throwable) {
            if (throwable instanceof BadInputException) {
                log.warn("Exception in DeliveryProcessor: " + throwable.getMessage(), throwable);
            } else if (throwable instanceof AssertionError) {
                log.error("Exception in DeliveryProcessor: " + throwable.getMessage(), throwable);
            } else {
                log.error("Exception in DeliveryProcessor [" + throwable.getClass().getName() + "] : " + throwable.getMessage(), throwable);
            }
            
            requestURI = "/error";
            request.getSession().setAttribute("ErrorMessage", StringUtil.getEmptyStringIfNull(throwable.getMessage()));
        }

        // step 2: map the URI (of the CONTROLLER)
        try {
            if (responseURI == null) {
                URLMap map = urlMapHandler.getMap(requestURI, request);
                responseURI = map.getResponse();
            }
        } catch (MissingURLMapEntryException e) {
            log.error("Exception: missing urlmap entry in admin module: requestURI = " + requestURI);
            responseURI = "/mvnplugin/mvnad/delivery/error.jsp";
            request.getSession().setAttribute("ErrorMessage", e.getMessage());
        } catch (Exception exception) {
            responseURI = "/mvnplugin/mvnad/delivery/error.jsp";
            request.getSession().setAttribute("ErrorMessage", exception.getMessage());
        }

        return responseURI;
    }
    
}
