/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/db/ConnectionWrapper.java,v 1.6 2007/09/26 04:11:07 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2007/09/26 04:11:07 $
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
package net.myvietnam.mvncore.db;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.NotImplementedException;

public class ConnectionWrapper implements Connection {

    private DBConnectionManager connectionManager = null;
    
    private static int outsideConnection = 0;
    
    Connection delegate = null;

    ConnectionWrapper(Connection original, DBConnectionManager conManager) {
        if (original == null) {
            throw new IllegalArgumentException("Cannot accept the connection is null.");
        }
        if (conManager == null) {
            throw new IllegalArgumentException("Cannot accept the DBConnectionManager is null.");
        }
        delegate = original;
        connectionManager = conManager;
        outsideConnection++;
    }
    
    private void makeSureNotClose() {
        if (delegate == null) {
            throw new IllegalStateException("Connection has been closed (delegate == null).");
        }
    }
    
    public void close() throws SQLException {
        //delegate.close();
        if (delegate != null) {
            connectionManager.freeConnection(delegate);
            delegate = null;
            outsideConnection--;
        }
    }

    public void clearWarnings() throws SQLException {
        makeSureNotClose();
        delegate.clearWarnings();
    }

    public void commit() throws SQLException {
        makeSureNotClose();
        delegate.commit();
    }

    public Statement createStatement() throws SQLException {
        makeSureNotClose();
        return delegate.createStatement();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    public boolean getAutoCommit() throws SQLException {
        makeSureNotClose();
        return delegate.getAutoCommit();
    }

    public String getCatalog() throws SQLException {
        makeSureNotClose();
        return delegate.getCatalog();
    }

    public int getHoldability() throws SQLException {
        makeSureNotClose();
        return delegate.getHoldability();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        makeSureNotClose();
        return delegate.getMetaData();
    }

    public int getTransactionIsolation() throws SQLException {
        makeSureNotClose();
        return delegate.getTransactionIsolation();
    }

    public Map getTypeMap() throws SQLException {
        makeSureNotClose();
        return delegate.getTypeMap();
    }

    public SQLWarning getWarnings() throws SQLException {
        makeSureNotClose();
        return delegate.getWarnings();
    }

    public boolean isClosed() throws SQLException {
        makeSureNotClose();
        return delegate.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        makeSureNotClose();
        return delegate.isReadOnly();
    }

    public String nativeSQL(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.nativeSQL(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, columnNames);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        makeSureNotClose();
        delegate.releaseSavepoint(savepoint);
    }

    public void rollback() throws SQLException {
        makeSureNotClose();
        delegate.rollback();
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        makeSureNotClose();
        delegate.rollback(savepoint);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        makeSureNotClose();
        delegate.setAutoCommit(autoCommit);
    }

    public void setCatalog(String catalog) throws SQLException {
        makeSureNotClose();
        delegate.setCatalog(catalog);
    }

    public void setHoldability(int holdability) throws SQLException {
        makeSureNotClose();
        delegate.setHoldability(holdability);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        makeSureNotClose();
        delegate.setReadOnly(readOnly);
    }

    public Savepoint setSavepoint() throws SQLException {
        makeSureNotClose();
        return delegate.setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        makeSureNotClose();
        return delegate.setSavepoint(name);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        makeSureNotClose();
        delegate.setTransactionIsolation(level);
    }

    public void setTypeMap(Map map) throws SQLException {
        makeSureNotClose();
        delegate.setTypeMap(map);
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new NotImplementedException("createArrayOf");
    }

    public Blob createBlob() throws SQLException {
        throw new NotImplementedException("createBlob");
    }

    public Clob createClob() throws SQLException {
        throw new NotImplementedException("createClob");
    }

    public NClob createNClob() throws SQLException {
        throw new NotImplementedException("createNClob");
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new NotImplementedException("createSQLXML");
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new NotImplementedException("createStruct");
    }

    public Properties getClientInfo() throws SQLException {
        throw new NotImplementedException("getClientInfo");
    }

    public String getClientInfo(String name) throws SQLException {
        throw new NotImplementedException("getClientInfo");
    }

    public boolean isValid(int timeout) throws SQLException {
        throw new NotImplementedException("isValid");
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new NotImplementedException("setClientInfo");
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new NotImplementedException("setClientInfo");
    }

    public boolean isWrapperFor(Class iface) throws SQLException {
        throw new NotImplementedException("isWrapperFor");
    }

    public Object unwrap(Class iface) throws SQLException {
        throw new NotImplementedException("unwrap");
    }
}
