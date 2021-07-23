/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/web/GenericRequest.java,v 1.19 2010/06/09 10:36:14 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.19 $
 * $Date: 2010/06/09 10:36:14 $
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
 * @author: Phong Ta Quoc 
 */
package net.myvietnam.mvncore.web;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public interface GenericRequest {

    public static final int APPLICATION_SCOPE = 1;
    public static final int PORTLET_SCOPE     = 2;

    public HttpServletRequest getServletRequest();

    public Object getPortletRequest();
    
    public Object getContext();

    public boolean isServletRequest();

    public boolean isPortletRequest();

    // below are common methods for both Servlet and Portlet request

    public Object getAttribute(String name);
    
    public void setAttribute(String name, Object value);

    public Enumeration getAttributeNames();

    public String getAuthType();

    public String getContextPath();

    public Locale getLocale();

    public Enumeration getLocales();

    public String getParameter(String name);

    public Map getParameterMap();

    public Enumeration getParameterNames();

    public String[] getParameterValues(String name);

    public String getSessionId();
    
    public Cookie[] getCookies();

    public String getRemoteUser();

    public String getRemoteAddr();

    public String getUserAgent();
    
    public String getReferer();

    public String getQueryString();

    // get from contexts
    public String getRealPath(String path);

    // get session's methods
    public void setSessionAttribute(String name, Object value);

    public Object getSessionAttribute(String name);

    public void setSessionAttribute(String name, Object value, int scope);

    public Object getSessionAttribute(String name, int scope);

    public String getMethod();

    public String getPortalInfo();

}