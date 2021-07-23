/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/URLResolverServiceImplServletDefault.java,v 1.3 2008/08/18 06:52:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/08/18 06:52:30 $
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
package net.myvietnam.mvncore.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.web.*;

public class URLResolverServiceImplServletDefault implements URLResolverService {

    public boolean isSupportServlet() {
        return true;
    }

    public boolean isSupportPortlet() {
        return false;
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url) {
        if (MVNCoreConfig.getEnableEncodeURL()) {
            url = response.encodeURL(url);
        }
        return url;
    }

    public String encodeURL(GenericRequest request, GenericResponse response, String url) {
        return encodeURL(request.getServletRequest(), response.getServletResponse(), url);
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option) {
        if (MVNCoreConfig.getEnableEncodeURL()) {
            url = response.encodeURL(url);
        }
        return url;
    }

    public String encodeURL(GenericRequest request, GenericResponse response, String url, int option) {
        return encodeURL(request.getServletRequest(), response.getServletResponse(), url, option);
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option, String mode) {
        // not implemented for cross module links
        if (MVNCoreConfig.getEnableEncodeURL()) {
            url = response.encodeURL(url);
        }
        return url;
    }

    public String encodeURL(GenericRequest request, GenericResponse response, String url, int option, String mode) {
        return encodeURL(request.getServletRequest(), response.getServletResponse(), url, option, mode);
    }

    public String decodeAction(GenericRequest request, GenericResponse response) {
        return "";
    }

    public String generateFormAction(HttpServletRequest request, HttpServletResponse response, String url) {
        return "";
    }

    public String generateFormActionForJavascript(HttpServletRequest request, HttpServletResponse response, String url, String formName) {
        return "";
    }

    public String getActionParam() {
        return "";
    }

}
