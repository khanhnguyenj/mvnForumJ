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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

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

    @Override
    public void close() throws SQLException {
        //delegate.close();
        if (delegate != null) {
            connectionManager.freeConnection(delegate);
            delegate = null;
            outsideConnection--;
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        makeSureNotClose();
        delegate.clearWarnings();
    }

    @Override
    public void commit() throws SQLException {
        makeSureNotClose();
        delegate.commit();
    }

    @Override
    public Statement createStatement() throws SQLException {
        makeSureNotClose();
        return delegate.createStatement();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        makeSureNotClose();
        return delegate.getAutoCommit();
    }

    @Override
    public String getCatalog() throws SQLException {
        makeSureNotClose();
        return delegate.getCatalog();
    }

    @Override
    public int getHoldability() throws SQLException {
        makeSureNotClose();
        return delegate.getHoldability();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        makeSureNotClose();
        return delegate.getMetaData();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        makeSureNotClose();
        return delegate.getTransactionIsolation();
    }

    @Override
    public Map getTypeMap() throws SQLException {
        makeSureNotClose();
        return delegate.getTypeMap();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        makeSureNotClose();
        return delegate.getWarnings();
    }

    @Override
    public boolean isClosed() throws SQLException {
        makeSureNotClose();
        return delegate.isClosed();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        makeSureNotClose();
        return delegate.isReadOnly();
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.nativeSQL(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.prepareCall(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql, columnNames);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        makeSureNotClose();
        return delegate.prepareStatement(sql);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        makeSureNotClose();
        delegate.releaseSavepoint(savepoint);
    }

    @Override
    public void rollback() throws SQLException {
        makeSureNotClose();
        delegate.rollback();
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        makeSureNotClose();
        delegate.rollback(savepoint);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        makeSureNotClose();
        delegate.setAutoCommit(autoCommit);
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        makeSureNotClose();
        delegate.setCatalog(catalog);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        makeSureNotClose();
        delegate.setHoldability(holdability);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        makeSureNotClose();
        delegate.setReadOnly(readOnly);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        makeSureNotClose();
        return delegate.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        makeSureNotClose();
        return delegate.setSavepoint(name);
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        makeSureNotClose();
        delegate.setTransactionIsolation(level);
    }

    @Override
    public void setTypeMap(Map map) throws SQLException {
        makeSureNotClose();
        delegate.setTypeMap(map);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new NotImplementedException("createArrayOf");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new NotImplementedException("createBlob");
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new NotImplementedException("createClob");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new NotImplementedException("createNClob");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new NotImplementedException("createSQLXML");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new NotImplementedException("createStruct");
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new NotImplementedException("getClientInfo");
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new NotImplementedException("getClientInfo");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throw new NotImplementedException("isValid");
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new NotImplementedException("setClientInfo");
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new NotImplementedException("setClientInfo");
    }

    @Override
    public boolean isWrapperFor(Class iface) throws SQLException {
        throw new NotImplementedException("isWrapperFor");
    }

    @Override
    public Object unwrap(Class iface) throws SQLException {
        throw new NotImplementedException("unwrap");
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getSchema() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }
}
