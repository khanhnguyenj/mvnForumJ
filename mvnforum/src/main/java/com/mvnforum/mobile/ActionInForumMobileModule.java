/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/mobile/ActionInForumMobileModule.java,v 1.1 2009/02/02 08:51:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2009/02/02 08:51:34 $
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
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.AbstractLocalizableAction;

public class ActionInForumMobileModule extends AbstractLocalizableAction {

    public ActionInForumMobileModule(GenericRequest request, String requestURI) throws MissingURLMapEntryException {

        url  = null;// url may be null after the code below
        localeKey = null;// but localeKey is never be null
        
        if (requestURI.equals("/index") || requestURI.equals("") || requestURI.equals("/")) {
            localeKey = "Index";
        } else if (requestURI.equals("/error")) {
            localeKey = "Error";
        }
        
        // this localeKey should never be null
        if (localeKey == null) {
            //String errorMessage = "Cannot find matching entry in ActionInUserModule for '" + requestURI + "'. Please contact the administrator.";
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] {requestURI});
            MissingURLMapEntryException e = new MissingURLMapEntryException(localizedMessage);
            throw e;
        }
    }

}
