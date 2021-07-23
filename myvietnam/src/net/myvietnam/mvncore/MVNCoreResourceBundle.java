/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/MVNCoreResourceBundle.java,v 1.9 2008/05/29 17:43:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2008/05/29 17:43:42 $
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
 * @author: Pavel Av
 */
package net.myvietnam.mvncore;

import java.util.Locale;
import java.util.ResourceBundle;

import net.myvietnam.mvncore.i18n.CacheResourceBundle;

public final class MVNCoreResourceBundle {

    private static CacheResourceBundle cacheResourceBundle = new CacheResourceBundle(MVNCoreGlobal.RESOURCE_BUNDLE_NAME);

    private MVNCoreResourceBundle() {
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
