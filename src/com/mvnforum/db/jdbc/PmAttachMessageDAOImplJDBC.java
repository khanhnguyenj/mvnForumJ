/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/PmAttachMessageDAOImplJDBC.java,v 1.15 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.15 $
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
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class PmAttachMessageDAOImplJDBC implements PmAttachMessageDAO {

    private static final Logger log = LoggerFactory.getLogger(PmAttachMessageDAOImplJDBC.class);

    public PmAttachMessageDAOImplJDBC() {
    }

    public void findByPrimaryKey(int messageID, int pmAttachID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, PmAttachID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ? AND PmAttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            statement.setInt(2, pmAttachID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + messageID + ", " + pmAttachID + ") in table 'PmAttachMessage'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, PmAttachID, RelationType, RelationOption, RelationStatus
     * Excluded columns:
     */
    public void create(int messageID, int pmAttachID, int relationType,
                       int relationOption, int relationStatus)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(messageID, pmAttachID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new PmAttachMessage with the same [MessageID, PmAttachID] (" + messageID + ", " + pmAttachID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMessageDAO().findByPrimaryKey(messageID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Message' does not exist. Cannot create new PmAttachMessage.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getPmAttachmentDAO().findByPrimaryKey(pmAttachID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'PmAttachment' does not exist. Cannot create new PmAttachMessage.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MessageID, PmAttachID, RelationType, RelationOption, RelationStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, messageID);
            statement.setInt(2, pmAttachID);
            statement.setInt(3, relationType);
            statement.setInt(4, relationOption);
            statement.setInt(5, relationStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'PmAttachMessage'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int messageID, int pmAttachID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ? AND PmAttachID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            statement.setInt(2, pmAttachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table PmAttachMessage where primary key = (" + messageID + ", " + pmAttachID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inMessage(int messageID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ? ");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.delete_inMessage.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, PmAttachID, RelationType, RelationOption, RelationStatus
     * Excluded columns:
     */
    public Collection getBeans_inMessage(int messageID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, PmAttachID, RelationType, RelationOption, RelationStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PmAttachMessageBean bean = new PmAttachMessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setPmAttachID(resultSet.getInt("PmAttachID"));
                bean.setRelationType(resultSet.getInt("RelationType"));
                bean.setRelationOption(resultSet.getInt("RelationOption"));
                bean.setRelationStatus(resultSet.getInt("RelationStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.getBeans_inMessage.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, PmAttachID, RelationType, RelationOption, RelationStatus
     * Excluded columns:
     */
    public Collection getBeans_inPmAttach(int pmAttachID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, PmAttachID, RelationType, RelationOption, RelationStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PmAttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, pmAttachID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PmAttachMessageBean bean = new PmAttachMessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setPmAttachID(resultSet.getInt("PmAttachID"));
                bean.setRelationType(resultSet.getInt("RelationType"));
                bean.setRelationOption(resultSet.getInt("RelationOption"));
                bean.setRelationStatus(resultSet.getInt("RelationStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.getBeans_inPmAttach.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans_inMessage(int messageID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PmAttachMessageDAOImplJDBC.getNumberOfBeans_inMessage.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.getNumberOfBeans_inMessage.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans_inPmAttach(int pmAttachID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PmAttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, pmAttachID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in PmAttachMessageDAOImplJDBC.getNumberOfBeans_inPmAttach.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in PmAttachMessageDAOImplJDBC.getNumberOfBeans_inPmAttach.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class PmAttachMessageDAOImplJDBC
