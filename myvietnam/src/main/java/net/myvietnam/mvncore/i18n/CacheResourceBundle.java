/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/i18n/CacheResourceBundle.java,v 1.12 2008/12/31 03:05:39 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2008/12/31 03:05:39 $
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
 */
package net.myvietnam.mvncore.i18n;

import java.text.MessageFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheResourceBundle {

    private static final Logger log = LoggerFactory.getLogger(CacheResourceBundle.class);
    
    private String bundleName = null;

    private Hashtable cacheResourceBundle = new Hashtable();

    public CacheResourceBundle(String bundleName) {
        if (bundleName == null) {
            throw new IllegalArgumentException("bundleName cannot be null."); 
        }
        this.bundleName = bundleName;
    }

    /**
     * Get the ResourceBundle from a locale, if locale is null, then get from locale English
     * @param locale Locale
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        ResourceBundle resourceBundle = (ResourceBundle)cacheResourceBundle.get(locale);
        if (resourceBundle == null) {
            try {
                resourceBundle = ResourceBundle.getBundle(bundleName, locale);
            } catch (MissingResourceException e) {
                log.error("Cannot load the ResourceBundle = " + bundleName);
                
                log.info("Using EmptyResourceBundle because cannot load ResourceBundle = " + bundleName);
                resourceBundle = new EmptyResourceBundle();
            }
            cacheResourceBundle.put(locale, resourceBundle);
        }
        return resourceBundle;
    }

    public String getString(Locale locale, String key) {
        ResourceBundle resourceBundle = getResourceBundle(locale);
        try {
            return resourceBundle.getString(key);
        } catch (Exception ex) {
            return "[[" + key + "]]";
        }
    }

    public String getString(Locale locale, String key, Object[] args) {
        ResourceBundle resourceBundle = getResourceBundle(locale);
        try {
            String message = resourceBundle.getString(key);

            MessageFormat formatter = new MessageFormat(message);
            if (locale != null) {
                formatter.setLocale(locale);
            }
            message = formatter.format(args);
            return message;
        } catch (Exception ex) {
            return "[[[" + key + "]]]";
        }
    }
}
