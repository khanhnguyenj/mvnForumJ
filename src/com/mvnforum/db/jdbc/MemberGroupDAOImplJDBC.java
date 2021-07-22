/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/MemberGroupDAOImplJDBC.java,v 1.35 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.35 $
 * $Date: 2009/01/02 18:31:46 $
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
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class MemberGroupDAOImplJDBC implements MemberGroupDAO {

    private static final Logger log = LoggerFactory.getLogger(MemberGroupDAOImplJDBC.class);

    public MemberGroupDAOImplJDBC() {
    }

    public void findByPrimaryKey(int groupID, int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, MemberID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + groupID + ", " + memberID + ") in table 'MemberGroup'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, MemberID, MemberName, Privilege, CreationDate,
     *                   ModifiedDate, ExpireDate
     * Excluded columns:
     */
    public void create(int groupID, String memberName,
                       int privilege, Timestamp creationDate, Timestamp modifiedDate, Timestamp expireDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        int memberID = 0;
        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            memberName = DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);//redundant ???
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new MemberGroup.");
        }

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(groupID, memberID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new MemberGroup with the same [GroupID, MemberID] (" + groupID + ", " + memberID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getGroupsDAO().findByPrimaryKey(groupID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Groups' does not exist. Cannot create new MemberGroup.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, groupID);
            statement.setInt(2, memberID);
            statement.setString(3, memberName);
            statement.setInt(4, privilege);
            statement.setTimestamp(5, creationDate);
            statement.setTimestamp(6, modifiedDate);
            statement.setTimestamp(7, expireDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'MemberGroup'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
            
    public void update(int memberID, int groupID, Timestamp modifiedDate, Timestamp expireDate)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ModifiedDate = ?, ExpireDate = ?");
        sql.append(" WHERE MemberID = ? AND GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
          
            statement.setTimestamp(1, modifiedDate);
            statement.setTimestamp(2, expireDate);
            
            statement.setInt(3, memberID);
            statement.setInt(4, groupID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table mvnforumMemberGroup where primary key = (" + groupID + ", " + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int groupID, int memberID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ? AND MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, memberID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table mvnforumMemberGroup where primary key = (" + groupID + ", " + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inGroup(int groupID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);

            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.delete_inGroup.");
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
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.delete_inMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public void deleteExpiredMembers()
        throws DatabaseException {
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE (CreationDate <> ExpireDate) AND (ExpireDate <= ?) ");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setTimestamp(1, now);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.deleteExpiredMembers.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
/************************************************
 * Customized methods come below
 ************************************************/
    /*
     * Included columns: MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate
     * Excluded columns: GroupID
     */
    public Collection getBeans_inGroup(int groupID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        sql.append(" ORDER BY MemberID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MemberGroupBean bean = new MemberGroupBean();
                bean.setGroupID(groupID);
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setPrivilege(resultSet.getInt("Privilege"));
                bean.setCreationDate(resultSet.getTimestamp("CreationDate"));
                bean.setModifiedDate(resultSet.getTimestamp("ModifiedDate"));
                bean.setExpireDate(resultSet.getTimestamp("ExpireDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.getBeans_inGroup.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans_inGroup(int groupID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in MemberGroupDAOImplJDBC.getNumberOfBeans_inGroup.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.getNumberOfBeans_inGroup.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: GroupID, MemberID, MemberName, Privilege, CreationDate,
     *                   ModifiedDate, ExpireDate
     * Excluded columns:
     */
    // huumai: only support MySQL
    //@todo: check if need support for other database???
    public Collection getBeans_limit(int groupID, int offset, int rowsToReturn)
        throws IllegalArgumentException, DatabaseException {
        
        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE GroupID = ?");
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, groupID);
            statement.setInt(2, offset);
            statement.setInt(3, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MemberGroupBean bean = new MemberGroupBean();
                bean.setGroupID(resultSet.getInt("GroupID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setPrivilege(resultSet.getInt("Privilege"));
                bean.setCreationDate(resultSet.getTimestamp("CreationDate"));
                bean.setModifiedDate(resultSet.getTimestamp("ModifiedDate"));
                bean.setExpireDate(resultSet.getTimestamp("ExpireDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.getBeans_limit.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public MemberGroupBean getMemberGroup(int memberID, int groupID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND GroupID=?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, groupID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table mvnforumMemberGroup where primary key = (" + groupID + ", " + memberID + ").");
            }

            MemberGroupBean bean = new MemberGroupBean();
            // @todo: uncomment the following line(s) as needed
            bean.setGroupID(resultSet.getInt("GroupID"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setMemberName(resultSet.getString("MemberName"));
            bean.setPrivilege(resultSet.getInt("Privilege"));
            bean.setCreationDate(resultSet.getTimestamp("CreationDate"));
            bean.setModifiedDate(resultSet.getTimestamp("ModifiedDate"));
            bean.setExpireDate(resultSet.getTimestamp("ExpireDate"));
            
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberGroupDAOImplJDBC.getMemberGroup.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
}// end of class MemberGroupDAOImplJDBC
