/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/MessageFolderDAOImplJDBC.java,v 1.28 2009/04/05 09:50:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.28 $
 * $Date: 2009/04/05 09:50:46 $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class MessageFolderDAOImplJDBC implements MessageFolderDAO {

    private static final Logger log = LoggerFactory.getLogger(MessageFolderDAOImplJDBC.class);

    public MessageFolderDAOImplJDBC() {
    }

    public void findByPrimaryKey(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT FolderName, MemberID");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE lower(FolderName) = lower(?) AND MemberID = ?");
        } else {
            sql.append(" WHERE FolderName = ? AND MemberID = ?");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + folderName + ", " + memberID + ") in table 'MessageFolder'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderName, MemberID, FolderOrder, FolderStatus, FolderOption,
     *                   FolderType, FolderCreationDate, FolderModifiedDate
     * Excluded columns:
     */
    public void create(String folderName, int memberID, int folderOrder,
                       int folderStatus, int folderOption, int folderType,
                       Timestamp folderCreationDate, Timestamp folderModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(folderName, memberID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new MessageFolder with the same [FolderName, MemberID] (" + folderName + ", " + memberID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new MessageFolder.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            statement.setInt(3, folderOrder);
            statement.setInt(4, folderStatus);
            statement.setInt(5, folderOption);
            statement.setInt(6, folderType);
            statement.setTimestamp(7, folderCreationDate);
            statement.setTimestamp(8, folderModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'MessageFolder'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(String folderName, int memberID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE FolderName = ? AND MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table MessageFolder where primary key = (" + folderName + ", " + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderName, MemberID, FolderOrder, FolderStatus, FolderOption,
     *                   FolderType, FolderCreationDate, FolderModifiedDate
     * Excluded columns:
     */
    public MessageFolderBean getMessageFolder(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE FolderName = ? AND MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table MessageFolder where primary key = (" + folderName + ", " + memberID + ").");
            }

            MessageFolderBean bean = new MessageFolderBean();
            // @todo: uncomment the following line(s) as needed
            //bean.setFolderName(folderName);
            //bean.setMemberID(memberID);
            bean.setFolderName(resultSet.getString("FolderName"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setFolderOrder(resultSet.getInt("FolderOrder"));
            bean.setFolderStatus(resultSet.getInt("FolderStatus"));
            bean.setFolderOption(resultSet.getInt("FolderOption"));
            bean.setFolderType(resultSet.getInt("FolderType"));
            bean.setFolderCreationDate(resultSet.getTimestamp("FolderCreationDate"));
            bean.setFolderModifiedDate(resultSet.getTimestamp("FolderModifiedDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.getMessageFolder.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderName, MemberID, FolderOrder, FolderStatus, FolderOption,
     *                   FolderType, FolderCreationDate, FolderModifiedDate
     *
     * Excluded columns:
     */
    public Collection getMessageFolders_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        sql.append(" ORDER BY FolderOrder ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MessageFolderBean bean = new MessageFolderBean();
                bean.setFolderName(resultSet.getString("FolderName"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setFolderOrder(resultSet.getInt("FolderOrder"));
                bean.setFolderStatus(resultSet.getInt("FolderStatus"));
                bean.setFolderOption(resultSet.getInt("FolderOption"));
                bean.setFolderType(resultSet.getInt("FolderType"));
                bean.setFolderCreationDate(resultSet.getTimestamp("FolderCreationDate"));
                bean.setFolderModifiedDate(resultSet.getTimestamp("FolderModifiedDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.getMessageFolders_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getMaxFolderOrder(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);

        sql.append("SELECT MAX(FolderOrder)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in MessageFolderDAOImplJDBC.getMaxFolderOrder.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.getMaxFolderOrder(memberID).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderOrder, FolderModifiedDate
     * Excluded columns: FolderName, MemberID, FolderStatus, FolderOption, FolderType,
     *                   FolderCreationDate
     */
    public void increaseFolderOrder(String folderName, int memberID, // primary key
                                    Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET FolderOrder = FolderOrder + 1, FolderModifiedDate = ?");
        sql.append(" WHERE FolderName = ? AND MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, folderModifiedDate);

            // primary key column(s)
            statement.setString(2, folderName);
            statement.setInt(3, memberID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table MessageFolder where primary key = (" + folderName + ", " + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.increaseFolderOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderOrder, FolderModifiedDate
     * Excluded columns: FolderName, MemberID, FolderStatus, FolderOption, FolderType,
     *                   FolderCreationDate
     */
    public void decreaseFolderOrder(String folderName, int memberID, // primary key
                                    Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET FolderOrder = FolderOrder - 1, FolderModifiedDate = ?");
        sql.append(" WHERE FolderName = ? AND MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setTimestamp(1, folderModifiedDate);

            // primary key column(s)
            statement.setString(2, folderName);
            statement.setInt(3, memberID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table MessageFolder where primary key = (" + folderName + ", " + memberID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.decreaseFolderOrder.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public int getNumberOfMessageFolders_inMember(int memberID)
        throws DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in MessageFolderDAOImplJDBC.getNumberOfMessageFolders_inMember.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.getNumberOfMessageFolders_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
}// end of class MessageFolderDAOImplJDBC
