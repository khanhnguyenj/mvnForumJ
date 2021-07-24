/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/user/ActionInAdUserModule.java,v 1.1 2009/02/02 09:00:06 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2009/02/02 09:00:06 $
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


import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.AbstractAction;
import com.mvnforum.auth.Action;

public class ActionInAdUserModule extends AbstractAction implements Action {

    public ActionInAdUserModule(GenericRequest request, String requestURI)
        throws MissingURLMapEntryException {
        
        url = null;// url may be null after the code below
        desc = null;// but desc is never be null
        String queryString = StringUtil.getEmptyStringIfNull(request.getQueryString());
        String defaultUrl = request.getContextPath() + AdUserModuleConfig.getUrlPattern();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (requestURI.equals("/listbanners") || requestURI.equals("") || requestURI.equals("/") || requestURI.equals("/index")) {
            url = defaultUrl + "/listbanners" + "?" + queryString;
            desc = "Listbanner";
        } else if (requestURI.equals("/login")) {
            url = "login";
            desc = "Login"; 
        } else if (requestURI.equals("/logout")) {
            desc = "Logout";
        } else if (requestURI.equals("/error")) {
            desc = "Error";
        } else if (requestURI.equals("/loginprocess")) {// will be sendRedirect
            desc = "Login process";
        }

        // check that desc is never null
        if (desc == null) {
            //String errorMessage = "Cannot find matching entry in
            // ActionInUserModule for '" + requestURI + "'. Please contact the
            // administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                    "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] { requestURI });
            throw new MissingURLMapEntryException(localizedMessage);
        }
    }

    public int getPageID() {
        return 0;
    }

    public Object getPageParam() {
        return null;
    }
    
}