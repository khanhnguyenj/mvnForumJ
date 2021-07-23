/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/I18nUtil.java,v 1.9 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/01/01 18:30:12 $
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
 * @author: Ta Quoc Phong  
 */

package net.myvietnam.mvncore.util;

import java.util.Locale;

import javax.servlet.ServletRequest;

import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.web.GenericRequest;

public class I18nUtil {
    
    private I18nUtil() {
    }

    public static void setLocaleInRequest(ServletRequest request, Locale locale) {
        request.setAttribute(MVNCoreGlobal.MVNCORE_REQUEST_LOCALE, locale);
    }

    public static Locale getLocaleInRequest(ServletRequest request) {
        return (Locale)request.getAttribute(MVNCoreGlobal.MVNCORE_REQUEST_LOCALE);
    }

    public static void setLocaleInRequest(GenericRequest request, Locale locale) {
        request.setAttribute(MVNCoreGlobal.MVNCORE_REQUEST_LOCALE, locale);
    }

    public static Locale getLocaleInRequest(GenericRequest request) {
        return (Locale)request.getAttribute(MVNCoreGlobal.MVNCORE_REQUEST_LOCALE);
    }

    /**
     * Get the locale from locale name
     * @param localeName : in this format la_CO_VA, eg. en_US
     * @return the locale instance of the localeName
     */
    public static Locale getLocale(String localeName) {
        // now, find out the 3 elements of a locale: language, country, variant
        String[] localeElement = StringUtil.getStringArray(localeName, "_");
        String language = "";
        String country = "";
        String variant = "";
        if (localeElement.length >= 1) {
            language = localeElement[0];
        }
        if (localeElement.length >= 2) {
            country = localeElement[1];
        }
        if (localeElement.length >= 3) {
            variant = localeElement[2];
        }
        return new Locale(language, country, variant);
    }

}
