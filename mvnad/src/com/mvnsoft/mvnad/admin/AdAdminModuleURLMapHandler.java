/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/AdAdminModuleURLMapHandler.java,v 1.4 2008/06/17 10:11:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
 * $Date: 2008/06/17 10:11:22 $
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

import java.util.Locale;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;

public class AdAdminModuleURLMapHandler {

    public AdAdminModuleURLMapHandler() {}

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we don't use the param request
     */
    public URLMap getMap(String requestURI, GenericRequest request)
        throws MissingURLMapEntryException {
        
        URLMap map = new URLMap();

        if (requestURI.equals("") || requestURI.equals("/")) {
            map.setResponse(AdAdminModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/index")) {
            map.setResponse("/mvnplugin/mvnad/admin/index.jsp");
        } else if (requestURI.equals("/error")) {
            map.setResponse("/mvnplugin/mvnad/admin/error.jsp");
            
        } else if (requestURI.equals("/login")) {
            map.setResponse("/mvnplugin/mvnad/admin/login.jsp");
        } else if (requestURI.equals("/logout")) {
            map.setResponse("/mvnplugin/mvnad/admin/login.jsp");
        } else if (requestURI.equals("/loginprocess")) {
            map.setResponse(AdAdminModuleConfig.getUrlPattern() + "/index");

        } else if (requestURI.equals("/addbanner")) {
            map.setResponse("/mvnplugin/mvnad/admin/addbanner.jsp");
        } else if (requestURI.equals("/addbannerprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/addbannersuccess.jsp");
        } else if (requestURI.equals("/uploadmediaprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/uploadmediasuccess.jsp");
        } else if (requestURI.equals("/editbanner")) {
            map.setResponse("/mvnplugin/mvnad/admin/editbanner.jsp");
        } else if (requestURI.equals("/editbannerprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/editbannersuccess.jsp");
        } else if (requestURI.equals("/deletebanner")) {
            map.setResponse("/mvnplugin/mvnad/admin/deletebanner.jsp");
        } else if (requestURI.equals("/deletebannerprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/deletebannersuccess.jsp");
        } else if (requestURI.equals("/listbanners")) {
            map.setResponse("/mvnplugin/mvnad/admin/listbanners.jsp");
            
        } else if (requestURI.equals("/managemedia")) {
            map.setResponse("/mvnplugin/mvnad/admin/managemedia.jsp");
        } else if (requestURI.equals("/deletemedia")) {
            map.setResponse("/mvnplugin/mvnad/admin/managemedia.jsp");

        } else if (requestURI.equals("/addbannerofzone")) {
            map.setResponse("/mvnplugin/mvnad/admin/addbannerofzone.jsp");
        } else if (requestURI.equals("/addbannerofzoneprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/editbannerofzonesuccess.jsp");
        } else if (requestURI.equals("/editbannerofzone")) {
            map.setResponse("/mvnplugin/mvnad/admin/editbannerofzone.jsp");
        } else if (requestURI.equals("/editbannerofzoneprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/editbannerofzonesuccess.jsp");
        } else if (requestURI.equals("/deletebannerofzone")) {
            map.setResponse("/mvnplugin/mvnad/admin/deletebannerofzone.jsp");
            
        } else if (requestURI.equals("/addzone")) {
            map.setResponse("/mvnplugin/mvnad/admin/addzone.jsp");
        } else if (requestURI.equals("/addzoneprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/addzonesuccess.jsp");
        } else if (requestURI.equals("/editzone")) {
            map.setResponse("/mvnplugin/mvnad/admin/editzone.jsp");
        } else if (requestURI.equals("/editzoneprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/editzonesuccess.jsp");
        } else if (requestURI.equals("/deletezone")) {
            map.setResponse("/mvnplugin/mvnad/admin/deletezone.jsp");
        } else if (requestURI.equals("/deletezoneprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/deletezonesuccess.jsp");
        } else if (requestURI.equals("/listzones")) {
            map.setResponse("/mvnplugin/mvnad/admin/listzones.jsp");

        } else if (requestURI.equals("/configforum")) {
            map.setResponse("/mvnplugin/mvnad/admin/configforum.jsp");
        } else if (requestURI.equals("/configforumprocess")) {
            map.setResponse("/mvnplugin/mvnad/admin/configforumsuccess.jsp");

        } else if (requestURI.equals("/getcode")) {
            map.setResponse("/mvnplugin/mvnad/admin/getcode.jsp");
            
        }

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // unknown module, we throw an exception
        if (map.getResponse() == null) {
            //String errorMessage = "Cannot find matching entry in URLMap for
            // '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                    "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] { requestURI });
            throw new MissingURLMapEntryException(localizedMessage);
            //throw new MissingURLMapEntryException(errorMessage);
        }
        return map;
    }
}
