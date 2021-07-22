/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/URLResolverServiceImplServletMvnForum.java,v 1.11 2008/08/18 06:49:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2008/08/18 06:49:44 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
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
 * @author: Minh Nguyen
 */
package com.mvnforum.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.FriendlyURLParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.admin.AdminModuleConfig;
import com.mvnforum.user.UserModuleConfig;

public class URLResolverServiceImplServletMvnForum implements URLResolverService {

    public boolean isSupportServlet() {
        return true;
    }

    public boolean isSupportPortlet() {
        return false;
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url) {
        if ((url.startsWith("viewthread")) || (url.startsWith("printpost")) || (url.startsWith("printthread"))) {
            if (MVNForumConfig.getEnableFriendlyURL()) {
                url = FriendlyURLParamUtil.createFriendlyURL(url);
            }
        }
        if (MVNCoreConfig.getEnableEncodeURL()) {
            url = response.encodeURL(url);
        }
        return url;
    }

    public String encodeURL(GenericRequest request, GenericResponse response, String url) {
        return encodeURL(request.getServletRequest(), response.getServletResponse(), url);
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option) {
        return encodeURL(request, response, url, option, "");
    }

    public String encodeURL(GenericRequest request, GenericResponse response, String url, int option) {
        return encodeURL(request.getServletRequest(), response.getServletResponse(), url, option);
    }

    public String encodeURL(HttpServletRequest request, HttpServletResponse response, String url, int option, String mode) {

        String resultURL;
        if ("view".equalsIgnoreCase(mode) || "admin".equalsIgnoreCase(mode)) {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("..");
            if ("view".equalsIgnoreCase(mode)) {
                buffer.append(UserModuleConfig.getUrlPattern()).append("/");
            } else {// now mode must be 'admin'
                buffer.append(AdminModuleConfig.getUrlPattern()).append("/");
            }
            buffer.append(url);
            resultURL = buffer.toString();
        } else {
            resultURL = url;
        }

        if (MVNCoreConfig.getEnableEncodeURL()) {
            resultURL = response.encodeURL(resultURL);
        }
        return resultURL;
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
