/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/user/AdUserModuleURLMapHandler.java,v 1.3 2008/06/06 07:04:44 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2008/06/06 07:04:44 $
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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.I18nUtil;

import com.mvnforum.MVNForumResourceBundle;

public class AdUserModuleURLMapHandler {

    public AdUserModuleURLMapHandler() {}

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we do not use the param request
     */
    public URLMap getMap(String requestURI, HttpServletRequest request)
        throws MissingURLMapEntryException {

        URLMap map = new URLMap();

        if (requestURI.equals("") || requestURI.equals("/") || requestURI.equals("/index")) {
            map.setResponse(AdUserModuleConfig.getUrlPattern() + "/listbanners");
        } else if (requestURI.equals("/listbanners")) {
            map.setResponse("/mvnplugin/mvnad/user/listbanners.jsp");
        } else if (requestURI.equals("/logout")) {
            map.setResponse("/mvnplugin/mvnad/user/login.jsp");
        } else if (requestURI.equals("/login")) {
            map.setResponse("/mvnplugin/mvnad/user/login.jsp");
        } else if (requestURI.equals("/loginprocess")) {
            map.setResponse("/mvnplugin/mvnad/user/listbanners.jsp"); 
        }

        Locale locale = I18nUtil.getLocaleInRequest(request);
        // unknown module, we throw an exception
        if (map.getResponse() == null) {
            //String errorMessage = "Cannot find matching entry in URLMap for
            // '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                    "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] { requestURI });
            throw new MissingURLMapEntryException(localizedMessage);
        }
        request.setAttribute("Focus", requestURI);
        return map;
    }
}