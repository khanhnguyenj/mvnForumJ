/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/MVNAdConfig.java,v 1.8 2009/08/31 03:41:04 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2009/08/31 03:41:04 $
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

import java.io.*;
import java.util.Locale;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MVNAdConfig {

    private static final Logger log = LoggerFactory.getLogger(MVNAdConfig.class);

    private static final String OPTION_FILE_NAME = "mvnad.xml";
    
    private MVNAdConfig() {
    }
    
    private static String MVNAdHome = "mvnAdHome";
    public static String getMVNAdHome() {
        return MVNAdHome;
    }

    private static String cityDatabaseFile = "";
    public static String getCityDatabaseFile() {
        return cityDatabaseFile;
    }
    
    private static String countryDatabaseFile = "";
    public static String getCountryDatabaseFile() {
        return countryDatabaseFile;
    }

    private static String webUploadFolder = "/mvnplugindata/mvnad";
    public static String getWebUploadFolder() {
        return webUploadFolder;
    }

    private static String mvnAdServiceClassName = "com.mvnsoft.mvnad.service.impl.MvnAdServiceImplDefault";
    public static String getMvnAdServiceClassName() {
        return mvnAdServiceClassName;
    }
    public static void setMvnAdServiceClassName(String mvnAdServiceClassName) {
        MVNAdConfig.mvnAdServiceClassName = mvnAdServiceClassName;
    }

    private static Locale eventLogLocale = Locale.ENGLISH;
    public static Locale getEventLogLocale() {
        return eventLogLocale;
    }
    public static void setEventLogLocale(Locale logLocale) {
        eventLogLocale = logLocale;
    }
    
    static {
        try {
            String configFilename   = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));

            MVNAdHome               = conf.getString("mvnadconfig.mvnad_home", "");
            setMVNAdHome(MVNAdHome);
            
            mvnAdServiceClassName   = conf.getString("mvnadfactoryconfig.mvnad_service_implementation", mvnAdServiceClassName);
        } catch (Exception e) {
            log.error("Error loading the factory properties", e);
        }
    }

    private static void setMVNAdHome(String home) {
        
        // now check the read/write permission by writing a temporary file
        try {
            // always create a directory, if the directory already existed, nothing happens
            FileUtil.createDirs(home, true);

            String tempFilename = home + File.separatorChar + "mvnad_tempfile.tmp";
            File tempFile = new File(tempFilename);
            if (log.isDebugEnabled()) {
                log.debug("Temp file = " + tempFilename);
                log.debug("Absolute filename of temp file = " + tempFile.getAbsolutePath());
            }

            FileOutputStream fos = new FileOutputStream(tempFilename);
            fos.write(tempFilename.getBytes());
            fos.close();

            tempFile.delete();

            String geoIPdir = home + File.separatorChar + "geoip";
            cityDatabaseFile = geoIPdir + File.separatorChar + "GeoLiteCity.dat";
            countryDatabaseFile = geoIPdir + File.separatorChar + "GeoIP.dat";

        } catch (IOException ex) {
            log.error("Cannot setup the mvnAdHome folder. Please correct it first.", ex);
            MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldStop("Check your mvnAdHome. Detail : " + ex.getMessage());
        } catch (AssertionError ae) {
            log.error("Assertion error. Please correct it first.", ae);
            MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldStop("Assertion error. Detail : " + ae.getMessage());
        }
    }
}
