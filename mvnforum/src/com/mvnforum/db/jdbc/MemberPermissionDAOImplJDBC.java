/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/MemberPermissionDAOImplJDBC.java,v 1.14 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/01/02 15:12:55 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.db.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class MemberPermissionDAOImplJDBC implements MemberPermissionDAO {

    private static final Logger log = LoggerFactory.getLogger(MemberPermissionDAOImplJDBC.class);

    public MemberPermissionDAOImplJDBC() {
    }

    public void findByPrimaryKey(int memberID, int permission)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND Permission = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, permission);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + memberID + ", " + permission + ") in table 'MemberPermission'.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, Permission
     * Excluded columns:
     */
    public void create(int memberID, int permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increment column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(memberID, permission);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new MemberPermission with the same [MemberID, Permission] (" + memberID + ", " + permission + ").");
        } catch (ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        if (memberID != 0) {
            try {
                // @todo: modify the parameter list as needed
                // You may have to regenerate this method if the needed columns don't have attribute 'include'
                DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
            } catch (ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnforumMember' does not exist. Cannot create new MemberPermission.");
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, Permission)");
        sql.append(" VALUES (?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setInt(2, permission);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'MemberPermission'.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int memberID, int permission)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND Permission = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, permission);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table MemberPermission where primary key = (" + memberID + ", " + permission + ").");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.delete_inMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, Permission
     * Excluded columns:
     */
    public Collection getBeans_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Permission");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MemberPermissionBean bean = new MemberPermissionBean();
                bean.setMemberID(memberID);
                bean.setPermission(resultSet.getInt("Permission"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberPermissionDAOImplJDBC.getBeans_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

} // end of class MemberPermissionDAOImplJDBC
