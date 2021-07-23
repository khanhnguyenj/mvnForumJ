/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/mobile/MobileModuleConfig.java,v 1.5 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.5 $
 * $Date: 2008/12/31 03:50:24 $
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

import java.io.File;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MobileModuleConfig {

    private static final Logger log = LoggerFactory.getLogger(MobileModuleConfig.class);

    private static final String OPTION_FILE_NAME = "mvnforum.xml";

    private static String urlPattern = "/m";

    private MobileModuleConfig() {
    }

    public static String getUrlPattern() {
        return urlPattern;
    }

    static {
        try {
            String configFilename = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));
            urlPattern = conf.getString("mobilemoduleconfig.url_pattern", urlPattern);
        } catch (Exception e) {
            String message = "com.mvnforum.mobile.MobileModuleConfig: Can't read the configuration file: '" + OPTION_FILE_NAME +
                "'. Make sure the file is in your CLASSPATH";
            log.error(message, e);
        }
    }

}
