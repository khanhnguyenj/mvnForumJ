/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/AbstractLocalizableAction.java,v 1.13 2008/01/16 06:57:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2008/01/16 06:57:37 $
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
package com.mvnforum.auth;

import java.util.Locale;

import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;

public abstract class AbstractLocalizableAction implements Action {

    protected String url;

    protected String localeKey;

    protected Object[] localeParams;

    protected int pageID;

    protected Object pageParam;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalizedDesc(GenericRequest request) {
        Locale locale = I18nUtil.getLocaleInRequest(request);
        if ( (localeParams == null) || (localeParams.length == 0) ) {
            return MVNForumResourceBundle.getString(locale, localeKey);
        }
        return MVNForumResourceBundle.getString(locale, localeKey, localeParams);
    }

    public int getPageID() {
        return pageID;
    }

    public Object getPageParam() {
        return pageParam;
    }

    // Below methods are helper methods, not from interface
    public String getLocaleKey() {
        return localeKey;
    }
    public void setLocaleKey(String key) {
        this.localeKey = key;
    }

    public Object[] getLocaleParams() {
        return localeParams;
    }
    public void setLocaleParams(Object[] params) {
        this.localeParams = params;
    }

}
