/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/ServletUtil.java,v 1.5 2010/03/23 09:44:20 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2010/03/23 09:44:20 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Trung Tang Bao
 */
package net.myvietnam.mvncore.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collection of utility methods when develop with JSP/Servlet
 */
public class ServletUtil {

    private static final Logger log = LoggerFactory.getLogger(ServletUtil.class);

    public static final String JAVAX_SERVLET_FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";
    
    public static final String JAVAX_SERVLET_INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";
    
    /**
     * Check if this is a forward request or not, only support Servlet 2.4 and later
     * 
     * @param request the request to check
     * @return true if this is a forward request
     */
    public static boolean isForwardRequest(HttpServletRequest request) {
        return (request.getAttribute(JAVAX_SERVLET_FORWARD_REQUEST_URI) != null);
    }

    /**
     * Check if this is a include request or not, only support Servlet 2.3 and later
     * 
     * @param request the request to check
     * @return true if this is a include request
     */
    public static boolean isIncludeRequest(HttpServletRequest request) {
        return (request.getAttribute(JAVAX_SERVLET_INCLUDE_REQUEST_URI) != null);
    }
    
    /**
     * Print all the headers of a request
     * @param request the HttpServletRequest object
     */
    public static void printRequestHeaders(HttpServletRequest request) {
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String header = (String) enumeration.nextElement();
            log.debug("request header: " + header + " " + request.getHeader(header));
        }
    }

    /**
     * Print all the parameters of a request
     * @param request the HttpServletRequest object
     */
    public static void printRequestParameters(HttpServletRequest request) {
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            log.debug("request parameter: " + parameter + " " + request.getParameter(parameter));
        }
    }

    /**
     * Print all the parameters of a request
     * @param request the GenericRequest object
     */
    public static void printRequestParameters(GenericRequest request) {
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            log.debug("request parameter: " + parameter + " " + request.getParameter(parameter));
        }
    }

    /**
     * Print all the attributes of a request
     * @param request the HttpServletRequest object
     */
    public static void printRequestAttributes(HttpServletRequest request) {
        Enumeration enumeration = request.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            log.debug("request attribute: " + parameter + " " + request.getAttribute(parameter));
        }
    }

    /**
     * Print all the attributes of a request
     * @param request the GenericRequest object
     */
    public static void printRequestAttributes(GenericRequest request) {
        Enumeration enumeration = request.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            log.debug("request attribute: " + parameter + " " + request.getAttribute(parameter));
        }
    }

    /**
     * Print all the attributes of a session
     * @param session the HttpSession object
     */
    public static void printSessionAttributes(HttpSession session) {
        Enumeration enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            String parameter = (String) enumeration.nextElement();
            log.debug("session attribute: " + parameter + " " + session.getAttribute(parameter));
        }
    }

}