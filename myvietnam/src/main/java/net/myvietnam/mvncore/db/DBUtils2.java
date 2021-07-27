/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/db/DBUtils2.java,v 1.13 2009/01/23 03:10:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/01/23 03:10:26 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package net.myvietnam.mvncore.db;

import java.sql.*;

import javax.sql.DataSource;

import net.myvietnam.mvncore.SecondaryMVNCoreConfig;
import net.myvietnam.mvncore.info.DatabaseInfo2;
import net.myvietnam.mvncore.service.MvnCoreInfoService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A  database utility class to handle all the secondary database stuffs
 * in the MyVietnam framework
 */
public final class DBUtils2 {

    private static final Logger log = LoggerFactory.getLogger(DBUtils2.class);

    private static int databaseType = DBUtils.DATABASE_UNKNOWN;

    private static boolean useDatasource = false;

    private static int maxTimeToWait = 2000;// 2 seconds

    private static int minutesBetweenRefresh = 30;// 30 minutes

    private static DBConnectionManager connectionManager = null;

    private static DataSource dataSource = null;

    //private static long lastGetConnectionTime = 0;

    private static long lastCloseAllConnectionsTime = 0;

    private static MvnCoreInfoService mvnCoreInfo = MvnCoreServiceFactory.getMvnCoreService().getMvnCoreInfoService();
    
    // static initialization of the class
    static {
        load();
    }
    
    // @question: The online users's still keep accessing db. 
    // Can we reload database ?
    private static void load() {
        DBOptions dbOptions = SecondaryMVNCoreConfig.getDbOptions();

        databaseType = dbOptions.getDbType();
        if (databaseType != DBUtils.DATABASE_UNKNOWN) {
            log.info("Set DATABASE_TYPE = " + databaseType);
        }
        useDatasource = dbOptions.isUseDataSource();
        if (useDatasource) {
            String dataSourceName = "";
            try {
                javax.naming.Context context = new javax.naming.InitialContext();
                // sample data source = java:comp/env/jdbc/MysqlDataSource
                dataSourceName = dbOptions.getDataSourceName();
                dataSource = (DataSource) context.lookup(dataSourceName);
                log.info("DBUtils2 : use datasource = " + dataSourceName);
            } catch (javax.naming.NamingException e) {
                log.error("Cannot get DataSource: datasource name = " + dataSourceName, e);
            }
        } else {
            maxTimeToWait = dbOptions.getTimeout();
            minutesBetweenRefresh = dbOptions.getRefreshMinutes();

            connectionManager = DBConnectionManager.getDBConnectionManager(dbOptions);
            
            log.info("DBUtils2 : use built-in DBConnectionManager (MAX_TIME_TO_WAIT = " + maxTimeToWait + ", MINUTES_BETWEEN_REFRESH = " + minutesBetweenRefresh + ")");
        }
        log.info("DBUtils2 (re)loaded. Detailed info: " + mvnCoreInfo.getProductVersion() + " (Build: " + mvnCoreInfo.getProductReleaseDate() + ")");
    }

    private DBUtils2() {// so cannot new an instance
    }

    /**
     * Use this method to get the database type. This method will automatically
     * detect the database type. You could override this value by modifying
     * the value in mvncore_db_DBOptions.properties
     * @return : the database type
     */
    public static int getDatabaseType() {
        if (databaseType == DBUtils.DATABASE_UNKNOWN) {
            Connection connection = null;
            try {
                connection = DBUtils2.getConnection();
                DatabaseMetaData dbmd = connection.getMetaData();
                String databaseName = dbmd.getDatabaseProductName().toLowerCase();
                if (databaseName.indexOf("oracle") != -1) {
                    databaseType = DBUtils.DATABASE_ORACLE;
                } else if (databaseName.indexOf("sql server") != -1) {
                    databaseType = DBUtils.DATABASE_SQLSERVER;
                } else if (databaseName.indexOf("db2") != -1) {// "DB2/NT"
                    databaseType = DBUtils.DATABASE_DB2;
                } else if (databaseName.indexOf("mysql") != -1) {// "MySQL"
                    databaseType = DBUtils.DATABASE_MYSQL;
                } else if (databaseName.indexOf("derby") != -1) {
                    databaseType = DBUtils.DATABASE_DERBY;
                } else if (databaseName.indexOf("postgresql") != -1) {
                    databaseType = DBUtils.DATABASE_POSTGRESQL;
                } else if (databaseName.indexOf("hsql") != -1) {
                    databaseType = DBUtils.DATABASE_HSQLDB;
                } else if (databaseName.indexOf("sap") != -1) {// "SAP DB"
                    databaseType = DBUtils.DATABASE_SAPDB;
                } else if (databaseName.indexOf("firebird") != -1) {// "firebird"
                    databaseType = DBUtils.DATABASE_FIREBIRD;
                } else if (databaseName.indexOf("pointbase") != -1) {// "pointbase"
                    databaseType = DBUtils.DATABASE_POINTBASE;
                } else {
                    databaseType = DBUtils.DATABASE_GENERAL;
                }
                DatabaseInfo2 databaseInfo = new DatabaseInfo2();
                log.info("DBUtils2: Auto detect DATABASE_TYPE = " + databaseType + " (" + DBUtils.getDatabaseTypeName(databaseType) + ")");
                log.info("DBUtils2: Database Name:       " + databaseInfo.getDatabaseProductName());
                log.info("DBUtils2: Database Version:    " + databaseInfo.getDatabaseProductVersion());
                log.info("DBUtils2: Database Url:        " + databaseInfo.getDatabaseUrl());
                log.info("DBUtils2: Database Username:   " + databaseInfo.getDatabaseUsername());
                log.info("DBUtils2: JDBC Driver Name:    " + databaseInfo.getDriverName());
                log.info("DBUtils2: JDBC Driver Version: " + databaseInfo.getDriverVersion());
            } catch (Exception ex) {
                log.error("Error when running getDatabaseType", ex);
            } finally {
                DBUtils2.closeConnection(connection);
            }
        }
        return databaseType;
    }

    /**
     * Get a connection from the connection pool. The returned connection
     * must be closed by calling DBUtils2.closeConnection()
     * @return : a new connection from the pool if succeed
     * @throws SQLException : if cannot get a connection from the pool
     */
    public static Connection getConnection() throws SQLException {

        long now = System.currentTimeMillis();
        //lastGetConnectionTime = now;
        // now check if we have not close all connections to refresh
        // after MINUTES_BETWEEN_REFRESH minutes, then will do it now
        if (now - lastCloseAllConnectionsTime > DateUtil.MINUTE * minutesBetweenRefresh) {
            boolean isBalance = closeAllConnections();
            if (isBalance == false) {
                try {
                    // wait for the checked-out connections to be returned and closed
                    Thread.sleep(2000);
                    log.debug("DBUtils2: sleep 2 seconds for checked-out connections to returned and closed.");
                } catch (Exception ex) { }
            }
        }

        Connection conection = null;

        if (useDatasource) {
            if (dataSource != null) {
                conection = dataSource.getConnection();
            }
        } else {
            if (connectionManager != null) {
                conection = connectionManager.getConnection(maxTimeToWait);
            } else {
                log.error("Assertion: DBUtils2.connectionManager == null");
            }
        }

        if (conection == null) {
            throw new SQLException("DBUtils2: Cannot get connection from Connection Pool.");
        }
        return conection;
    }

    /**
     * Close all the connections that currently in the pool
     * This method could be used to refresh the database connection
     * @return true if the pool is empty and balance
     *         false if the pool has returned some connection to outside
     */
    public static boolean closeAllConnections() {
        log.debug("DBUtils2.closeAllConnections is called.");
        boolean retValue = true;// balance (default)
        lastCloseAllConnectionsTime = System.currentTimeMillis();
        if (useDatasource) {
            if (dataSource != null) {
                // do nothing here now
            }
        } else {
            if (connectionManager != null) {
                retValue = connectionManager.release();
            } else {
                log.error("Assertion: DBUtils2.connectionManager == null");
            }
        }
        return retValue;
    }

    /**
     * Use this method to return the connection to the connection pool
     * Do not use this method to close connection that is not from
     * the connection pool
     * @param connection : the connection that needs to be returned to the pool
     */
    public static void closeConnection(Connection connection) {
        if (connection == null) return;

        if (useDatasource) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("DBUtils2: Cannot close connection.", e);
            }
        } else {
            //connectionManager.freeConnection(connection);
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Assertion: this should not happen when release connection.");
            }
        }
    }

    public static boolean isCaseSensitiveDatebase() {
        if ( (getDatabaseType() == DBUtils.DATABASE_ORACLE) ||
             (getDatabaseType() == DBUtils.DATABASE_SAPDB) ||
             (getDatabaseType() == DBUtils.DATABASE_POSTGRESQL) || 
             (getDatabaseType() == DBUtils.DATABASE_DERBY) ||
             (getDatabaseType() == DBUtils.DATABASE_POINTBASE)) {
            return true;
        }
        return false;
    }
    
}
