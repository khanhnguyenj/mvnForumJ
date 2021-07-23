/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/db/DBOptions.java,v 1.26 2007/04/05 09:13:49 phuongpdd Exp $
 * $Author: phuongpdd $
 * $Revision: 1.26 $
 * $Date: 2007/04/05 09:13:49 $
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
 * @author: Phong Ta Quoc
 */
package net.myvietnam.mvncore.db;

public class DBOptions {
    
    public static final String MAIN = "Main";
    
    protected String dbManagerName;
    protected int dbType;
    protected boolean useDataSource;
    protected String dataSourceName;
    protected String driverClass;
    protected String dbUrl;
    protected String username;
    protected String password;
    protected int conMax;
    protected int timeout;
    protected int refreshMinutes;

    public DBOptions(String dbManagerName) {
        this.dbManagerName = dbManagerName;
    }
    public String getDbManagerName() {
        return dbManagerName;
    }
    
    public void setDbType(int dbType) {
        this.dbType = dbType;
    }
    public int getDbType() {
        return dbType;
    }

    public void setUseDataSource(boolean useDataSource) {
        this.useDataSource = useDataSource;
    }
    public boolean isUseDataSource() {
        return useDataSource;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
    public String getDriverClass() {
        return driverClass;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    public String getDbUrl() {
        return dbUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setConMax(int conMax) {
        this.conMax = conMax;
    }
    public int getConMax() {
        return conMax;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getTimeout() {
        return timeout;
    }
    
    public void setRefreshMinutes(int refreshMinutes) {
        this.refreshMinutes = refreshMinutes;
    }
    public int getRefreshMinutes() {
        return refreshMinutes;
    }

}
