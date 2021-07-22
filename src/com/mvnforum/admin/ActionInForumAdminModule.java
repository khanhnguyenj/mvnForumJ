/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ActionInForumAdminModule.java,v 1.1 2009/02/02 08:49:13 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2009/02/02 08:49:13 $
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
 * @author: Mai  Nguyen
 */
package com.mvnforum.admin;

import java.util.Locale;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.AbstractLocalizableAction;
import com.mvnforum.auth.Action;

public class ActionInForumAdminModule extends AbstractLocalizableAction implements Action {

    public ActionInForumAdminModule(GenericRequest request, String requestURI) throws MissingURLMapEntryException {

        url = null; // url may be null after the code below
        localeKey = null; // but localeKey is never be null

        localeKey = "mvnforum.admin.action.desc.admin_control";

        // check that localeKey is never null
        if (localeKey == null) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            //String errorMessage = "Cannot find matching entry in ActionInAdminModule for '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry.AdminModule", new Object[] {requestURI});
            throw new MissingURLMapEntryException(localizedMessage);
        }
    }
}
