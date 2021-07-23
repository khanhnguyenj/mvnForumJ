/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/info/DatabaseInfo.java,v 1.14 2008/12/30 10:46:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.14 $
 * $Date: 2008/12/30 10:46:29 $
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
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.info;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import net.myvietnam.mvncore.db.DBUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInfo {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInfo.class);

    private String databaseProductName;
    private String databaseProductVersion;
    private String databaseUrl;
    private String databaseUsername;
    private String driverName;
    private String driverVersion;

    private String errorMessage;

    public DatabaseInfo() {
        Connection connection = null;
        try {
            connection = DBUtils.getConnection();
            DatabaseMetaData dbmd   = connection.getMetaData();
            databaseUrl             = dbmd.getURL();
            databaseUsername        = dbmd.getUserName();
            databaseProductName     = dbmd.getDatabaseProductName();
            databaseProductVersion  = dbmd.getDatabaseProductVersion();
            driverName              = dbmd.getDriverName();
            driverVersion           = dbmd.getDriverVersion();
        } catch (Throwable ex) {
            log.error("Error when accessing database info", ex);
            errorMessage = ex.getMessage();
        } finally {
            DBUtils.closeConnection(connection);
        }
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
