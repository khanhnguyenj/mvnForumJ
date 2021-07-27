/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/MVNAdResourceBundle.java,v 1.4 2009/04/09 09:27:47 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.4 $
 * $Date: 2009/04/09 09:27:47 $
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
package com.mvnsoft.mvnad;

import java.util.Locale;
import java.util.ResourceBundle;

import net.myvietnam.mvncore.i18n.CacheResourceBundle;

public final class MVNAdResourceBundle {

    private static CacheResourceBundle cacheResourceBundle = new CacheResourceBundle("i18n/mvnad/mvnad_i18n");

    private MVNAdResourceBundle() {
    }

    public static ResourceBundle getResourceBundle(Locale locale) {
        return cacheResourceBundle.getResourceBundle(locale);
    }

    public static String getString(Locale locale, String key) {
        return cacheResourceBundle.getString(locale, key);
    }

    public static String getString(Locale locale, String key, Object[] args) {
        return cacheResourceBundle.getString(locale, key, args);
    }
}
