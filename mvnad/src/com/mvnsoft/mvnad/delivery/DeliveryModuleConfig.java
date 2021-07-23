/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/DeliveryModuleConfig.java,v 1.6 2008/12/31 04:13:28 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.6 $
 * $Date: 2008/12/31 04:13:28 $
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
package com.mvnsoft.mvnad.delivery;

import java.io.File;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DeliveryModuleConfig {

    private static final String OPTION_FILE_NAME  = "mvnad.xml";

    private static final Logger log = LoggerFactory.getLogger(DeliveryModuleConfig.class);

    private static String urlPattern = "/delivery";

    private DeliveryModuleConfig() {
    }
    
    public static String getUrlPattern() {
        return urlPattern;
    }

    static {
        try {
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(FileUtil.getServletClassesPath() + OPTION_FILE_NAME));
            urlPattern = conf.getString("deliverymodule.url_pattern", urlPattern);
        } catch (Exception e) {
            String message = "DeliveryModuleConfig: Cannot find the configuration file \"" + OPTION_FILE_NAME + "\". Make sure that it exists in your CLASSPATH.";
            log.error(message, e);
        }
    }

}
