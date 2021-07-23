/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/URLResolverService.java,v 1.3 2008/08/18 06:52:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/08/18 06:52:31 $
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
 * @author: Minh Nguyen
 */
package net.myvietnam.mvncore.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public interface URLResolverService {

    public static final int RENDER_URL = 0;
    public static final int ACTION_URL = 1;
    public static final int UPLOAD_URL = 2;

    public boolean isSupportServlet();

    public boolean isSupportPortlet();

    //We need GenericRequest for drawing CategoryTree
    //see getAttribute("javax.portlet.request") in Spec for detail
    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url);
    public String encodeURL(GenericRequest request, GenericResponse response, String url);

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option);
    public String encodeURL(GenericRequest request, GenericResponse response, String url, int option);

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option, String mode);
    public String encodeURL(GenericRequest request, GenericResponse response, String url, int option, String mode);

    public String decodeAction(GenericRequest request, GenericResponse response);

    public String generateFormAction(HttpServletRequest request, HttpServletResponse response, String url);
    public String generateFormActionForJavascript(HttpServletRequest request, HttpServletResponse response, String url, String formName);

    public String getActionParam();

}
