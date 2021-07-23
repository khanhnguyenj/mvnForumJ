/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/db/DBConnectionManager.java,v 1.27 2010/05/31 11:59:05 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.27 $
 * $Date: 2010/05/31 11:59:05 $
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
package net.myvietnam.mvncore.db;

import java.sql.*;
import java.util.*;

import net.myvietnam.mvncore.MVNCoreConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a Singleton that provides access to the
 * connection pool. A client gets access to the single
 * instance through the static getInstance() method
 * and can then check-out and check-in connections from a pool.
 * When the client shuts down it should call the release() method
 * to close all opened connections and do other clean up.
 */
class DBConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(DBConnectionManager.class);

    private static final int TIME_BETWEEN_RETRIES = 500; // O.5 second

    // static variable
    private static DBConnectionManager instance = null;       // The single instance

    // instance variable
    private DBConnectionPool pool = null;// please be careful if u want to make this variable static
    
    private static Map dbManagers = new HashMap();
    private static final int MANAGER_MAX = 5;
    
    /**
     * A private constructor since this is a Singleton
     * Note: This constructor is lightweight since DBConnectionPool is lightweight,
     *       so no connection is created until the first time getConnection() is called
     */
    private DBConnectionManager() {
        String driverClassName = MVNCoreConfig.getDriverClassName();
        try {
            Class.forName(driverClassName).newInstance();
        } catch (Exception e) {
            log.error("DBConnectionManager: Unable to load driver = " + driverClassName, e);
        }

        String url = MVNCoreConfig.getDatabaseURL();
        String user = MVNCoreConfig.getDatabaseUser();
        String password = MVNCoreConfig.getDatabasePassword();
        int maxConnection = MVNCoreConfig.getMaxConnection();

        //always new the pool because pool is an instance variable
        pool = new DBConnectionPool(url, user, password, maxConnection);
    }

    private DBConnectionManager(DBOptions dbOptions) {
        String driverClassName = dbOptions.getDriverClass();
        try {
            Class.forName(driverClassName).newInstance();
        } catch (Exception e) {
            log.error("DBConnectionManager: Unable to load driver = " + driverClassName, e);
        }

        String url = dbOptions.getDbUrl();
        String user = dbOptions.getUsername();
        String password = dbOptions.getPassword();
        int maxConnection = dbOptions.getConMax();

        //always new the pool because pool is an instance variable
        pool = new DBConnectionPool(url, user, password, maxConnection);
    }
    
    /**
     * Returns the single instance, creating one if it's the
     * first time this method is called.
     *
     * @return DBConnectionManager The single instance.
     */
    /*
    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            DBOptions option = new DBOptions();
            instance = new DBConnectionManager(option);
        }
        return instance;
    }*/

    /**
     * Returns the single instance, creating one if it's the
     * first time this method is called.
     *
     * @return DBConnectionManager The single instance.
     */
    /*
    private static synchronized DBConnectionManager getInstance(DBOptions option) {
        if (instance == null) {
            if (option == null) {
                option = new DBOptions();
            }
            instance = new DBConnectionManager(option);
        }
        return instance;
    }*/

    /**
     * DBUtil use this method
     */
    public static synchronized DBConnectionManager getInstance(boolean useConfig) {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    /**
     * DBUtil2 use this method
     */
    public static synchronized DBConnectionManager getDBConnectionManager(DBOptions dbOptions) {
        
        if (dbOptions == null) {
            throw new IllegalArgumentException("Cannot get DBConnectionManager. Missing DBOptions.");
        }
        
        if ( (dbOptions.getDbManagerName() == null) || (dbOptions.getDbManagerName().length() == 0) ) {
            throw new IllegalArgumentException("Cannot get DBConnectionManager. Missing [Database Connection Manager Name].");
        }

        DBConnectionManager dbManager = (DBConnectionManager)dbManagers.get(dbOptions.getDbManagerName());
        if (dbManager == null) {
            dbManager = createDbConnectionManager(dbOptions);
        }
        return dbManager;
    }
    
    private static DBConnectionManager createDbConnectionManager(DBOptions dbOptions) {
        
        if (dbManagers.size() >= MANAGER_MAX) {
            throw new IllegalStateException("System only support max " +  MANAGER_MAX + " DBConnectionManager(s)");    
        }
        
        DBConnectionManager manager = new DBConnectionManager(dbOptions);
        dbManagers.put(dbOptions.getDbManagerName(), manager);
        
        return manager;
    }

    /**
     * Returns a connection to the pool.
     *
     * @param con The Connection
     */
    void freeConnection(Connection con) {
        pool.freeConnection(con);
    }

    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created.
     *
     * @return Connection The connection or null
     */
    Connection getConnection() {
        return getConnection(0);
    }

    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created. If the max number has been reached, waits until one
     * is available or the specified time has elapsed.
     *
     * @param time The number of milliseconds to wait
     * @return Connection The connection or null
     */
    Connection getConnection(long time) {
        Connection connection = pool.getConnection(time);
        if (connection == null) {
            return null;
        }
        
        try {
            // we always setAutoCommit(true) for backward compatible with mvnForum
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Cannot setAutoCommit", e);
        }
        ConnectionWrapper wrapper = new ConnectionWrapper(connection, this);
        return wrapper;
    }

    /**
     * Closes all open connections.
     * @return true if the pool is empty and balance
     *         false if the pool has returned some connection to outside
     */
    boolean release() {
        return pool.release();
    }

    /**
     * This inner class represents a connection pool. It creates new
     * connections on demand, up to a max number if specified.
     * It also checks to make sure that the connection is still open
     * before it is returned to a client.
     */
    class DBConnectionPool {
        private int checkedOut  = 0;//NOTE: this variable should be changed in synchronized method only
        private Vector freeConnections = new Vector();

        private int    maxConn  = 0;
        private String password = null;
        private String URL      = null;
        private String user     = null;

        /**
         * Creates new connection pool.
         * NOTE: new an instance of this class is lightweight since it does not create any connections
         *
         * @param URL The JDBC URL for the database
         * @param user The database user, or null
         * @param password The database user password, or null
         * @param maxConn The maximal number of connections, or 0 for no limit
         */
        DBConnectionPool(String URL, String user, String password, int maxConn) {
            this.URL = URL;
            this.user = user;
            this.password = password;
            this.maxConn = maxConn;
        }

        /**
         * Checks in a connection to the pool. Notify other Threads that
         * may be waiting for a connection.
         *
         * @todo: Maybe we don't need notifyAll(); ???
         *
         * @param con The connection to check in
         */
        synchronized void freeConnection(Connection con) {
            // Put the connection at the end of the Vector
            if (con != null) {//make sure that the connection is not null
                if (checkedOut <= 0) {
                    // this means that connection is open too much
                    // There are 2 cases:
                    // 1. Not get from this connection pool (maybe get directly)
                    // 2. this connection is gotten and then the whole pool is released
                    // In these case, just close the connection
                    try {
                        log.debug("DBConnectionManager: about to close the orphan connection.");
                        con.close();
                    } catch (SQLException ex) { 
                        //ignore
                    }
                } else {
                    // Return this connection to the pool
                    // note that we don't have to check if the connection is not connected
                    // this will be check in the getConnection method
                    freeConnections.addElement(con);
                    // FIXME: possible negative value
                    // NOTE: checkOut should never be negative here
                    checkedOut--; // NOTE: this number can be negative (in case connection does not come from the pool)
                    notifyAll(); // can I remove it ???
                }
            }
        }

        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         */
        synchronized Connection getConnection() {
            Connection con = null;

            while ( (freeConnections.size() > 0) && (con == null) ) {
                // Pick the first Connection in the Vector
                // to get round-robin usage
                con = (Connection) freeConnections.firstElement();
                freeConnections.removeElementAt(0);
                try {
                    if (con.isClosed()) {
                        log.info("Removed bad connection in DBConnectionPool.");
                        con = null; // to make the while loop to continue
                    }
                } catch (SQLException e) {
                    con = null; // to make the while loop to continue
                }
            } // while

            if (con == null) {// cannot get any connection from the pool
                if (maxConn == 0 || checkedOut < maxConn) {// maxConn = 0 means unlimited connections
                    con = newConnection();
                }
            }
            if (con != null) {
                checkedOut++;
            }
            return con;
        }

        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         * <P>
         * If no connection is available and the max number has been
         * reached, this method waits the specified time for one to be
         * checked in.
         *
         * @param timeout The timeout value in milliseconds
         */
        /**
         * Note that this method is not synchronized since it relies on the getConnection(void) method
         * I also believe that this method SHOULD NOT synchronized because I use #sleep() method
         * @todo: check if we should synchronize this method and use wait instead of sleep ???
         */
        Connection getConnection(long timeout) {
            long startTime = System.currentTimeMillis();
            Connection con = null;
            while ((con = getConnection()) == null) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= timeout) {
                    // Timeout has expired
                    return null;
                }

                long timeToWait = timeout - elapsedTime;
                if (timeToWait > TIME_BETWEEN_RETRIES) {
                    timeToWait = TIME_BETWEEN_RETRIES;// we don't want to wait for more than TIME_BETWEEN_RETRIES second each time
                }
                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
            return con;
        }

        /**
         * Closes all available connections.
         * @return true if the pool is empty and balance
         *         false if the pool has returned some connection to outside
         */
        synchronized boolean release() {
            boolean retValue = true;
            Enumeration allConnections = freeConnections.elements();
            while (allConnections.hasMoreElements()) {
                Connection con = (Connection) allConnections.nextElement();
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error("Cannot close connection in DBConnectionPool.");
                }
            }
            freeConnections.removeAllElements();
            if (checkedOut != 0) {
                retValue = false;
                log.warn("DBConnectionManager: the built-in connection pool is not balanced.");
            }
            checkedOut = 0;
            return retValue;
        }

        /**
         * Creates a new connection, using a userid and password
         * if specified.
         * @todo: check if this method need synchronized
         */
        private Connection newConnection() {
            Connection con = null;
            try {
                if (user == null) {
                    con = DriverManager.getConnection(URL);
                } else {
                    con = DriverManager.getConnection(URL, user, password);
                }
                if (con == null) {
                    log.error("Cannot create a new connection in DBConnectionPool by method DriverManager.getConnection(). URL = " + URL);
                    throw new IllegalStateException("Variable 'con' must not be null.");
                }
                // Note that we don't need to call setAutoCommit here because we 
                // will call it at DBConnectionManager.getConnection()
                //con.setAutoCommit(true);//thread 804 by trulore
            } catch (SQLException e) {
                log.error("Cannot create a new connection in DBConnectionPool. URL = " + URL, e);
                return null;
            }
            return con;
        }
    }
    
}
