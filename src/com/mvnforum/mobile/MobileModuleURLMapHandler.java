/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/mobile/MobileModuleURLMapHandler.java,v 1.11 2008/12/15 08:52:30 nhanld Exp $
 * $Author: nhanld $
 * $Revision: 1.11 $
 * $Date: 2008/12/15 08:52:30 $
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
 * @author: Nhan Luu Duy
 */
package com.mvnforum.mobile;

import java.util.Locale;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;

public class MobileModuleURLMapHandler {

    public MobileModuleURLMapHandler() {
    }

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we don't use the param request
     */
    public URLMap getMap(String requestURI, GenericRequest request, String localeName)
        throws MissingURLMapEntryException {

        URLMap map = new URLMap();
        
        if (requestURI.equals("") || requestURI.equals("/")) {
            map.setResponse(MobileModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/index")) {
            map.setResponse("/WEB-INF/mvnplugin/mvnforum/mobile/index.jsp");
        } else if (requestURI.equals("/error")) {
            map.setResponse("/WEB-INF/mvnplugin/mvnforum/mobile/error.jsp");
        } 
        
        // unknown module, we throw an exception
        if (map.getResponse() == null) {
            //String errorMessage = "Cannot find matching entry in URLMap for '" + requestURI + "'. Please contact the administrator.";
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] {requestURI});
            throw new MissingURLMapEntryException(localizedMessage);
        }
        return map;
    }
}
