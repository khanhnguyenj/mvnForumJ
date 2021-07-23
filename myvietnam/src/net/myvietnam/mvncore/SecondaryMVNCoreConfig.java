/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/SecondaryMVNCoreConfig.java,v 1.6 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/01/01 18:30:12 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2006 by MyVietnam.net
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
 * @author: Phuong Pham Dinh Duy
 */
package net.myvietnam.mvncore;

import java.io.File;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.db.DBOptions;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondaryMVNCoreConfig {

    private static final Logger log = LoggerFactory.getLogger(SecondaryMVNCoreConfig.class);
    
    private static final String OPTION_FILE_NAME = "secondary-database.xml";

    private static DBOptions mainDbOptions = new DBOptions("ExternalDatabase");
    
    private SecondaryMVNCoreConfig() {
    }
    
    public static DBOptions getDbOptions() {
        return mainDbOptions;
    }

    static {
        String configFilename = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;
        try {
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));

            configDatabase(conf);
        } catch (Exception e) {
            String message = "com.mvnforum.SecondaryMVNCoreConfig: Can't read the configuration file: '" + configFilename + "'. Make sure the file is in your CLASSPATH";
            log.error(message, e);
        }
    }
    
    private static void configDatabase(DOM4JConfiguration conf) {
        
        boolean useDataSource = conf.getBoolean("dboptions.use_datasource", false);
        mainDbOptions.setUseDataSource(useDataSource);

        int dbType = conf.getInt("dboptions.database_type", 0);
        mainDbOptions.setDbType(dbType);
        
        if (useDataSource) {
            String dataSourceName = conf.getString("dboptions.datasource_name");
            mainDbOptions.setDataSourceName(dataSourceName);
        } else {
            String dbDriverClass = conf.getString("dboptions.driver_class_name", "");
            mainDbOptions.setDriverClass(dbDriverClass);

            String dbUrl = conf.getString("dboptions.database_url", "");
            mainDbOptions.setDbUrl(dbUrl);

            String dbUsername = conf.getString("dboptions.database_user", "");
            mainDbOptions.setUsername(dbUsername);
            
            String dbPassword = conf.getString("dboptions.database_password", "");
            mainDbOptions.setPassword(dbPassword);

            int dbConMax = conf.getInt("dboptions.max_connection", 20);
            mainDbOptions.setConMax(dbConMax);
            
            int dbTimeout = conf.getInt("dboptions.max_time_to_wait", 2000);
            mainDbOptions.setTimeout(dbTimeout);

            int dbRefreshMinutes = conf.getInt("dboptions.minutes_between_refresh", 30);
            if (dbRefreshMinutes < 1) {
                dbRefreshMinutes = 1; //min is 1 minute
            }
            mainDbOptions.setRefreshMinutes(dbRefreshMinutes);        
        }
    }
}
