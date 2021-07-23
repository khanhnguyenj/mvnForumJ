/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/MessageDAOImplJDBC.java,v 1.50 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.50 $
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

import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public class MessageDAOImplJDBC implements MessageDAO {

    private static final Logger log = LoggerFactory.getLogger(MessageDAOImplJDBC.class);

    public MessageDAOImplJDBC() {
    }

    private int findMessageID(int memberID, int messageSenderID, Timestamp messageCreationDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND MessageSenderID = ? AND MessageCreationDate = ? ");
        sql.append(" ORDER BY MessageID DESC");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, messageSenderID);
            statement.setTimestamp(3, messageCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the Message in table Message.");
            }

            return resultSet.getInt("MessageID");
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.findMessageID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByPrimaryKey(int messageID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + messageID + ") in table 'Message'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList,
     *                   MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType,
     *                   MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon,
     *                   MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns: MessageID
     */
    public int create(String folderName, int memberID, int messageSenderID,
                        String messageSenderName, String messageToList, String messageCcList,
                        String messageBccList, String messageTopic, String messageBody,
                        int messageType, int messageOption, int messageStatus,
                        int messageReadStatus, int messageNotify, String messageIcon,
                        int messageAttachCount, String messageIP, Timestamp messageCreationDate)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        int messageID = 0;

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMessageFolderDAO().findByPrimaryKey(folderName, memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'MessageFolder' does not exist. Cannot create new Message.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Message.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey2(messageSenderID, messageSenderName);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Message.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            statement.setInt(3, messageSenderID);
            statement.setString(4, messageSenderName);
            statement.setString(5, messageToList);
            statement.setString(6, messageCcList);
            statement.setString(7, messageBccList);
            statement.setString(8, messageTopic);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(9, new StringReader(messageBody), messageBody.length());
            } else {
                statement.setString(9, messageBody);
            }
            statement.setInt(10, messageType);
            statement.setInt(11, messageOption);
            statement.setInt(12, messageStatus);
            statement.setInt(13, messageReadStatus);
            statement.setInt(14, messageNotify);
            statement.setString(15, messageIcon);
            statement.setInt(16, messageAttachCount);
            statement.setString(17, messageIP);
            statement.setTimestamp(18, messageCreationDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Message'.");
            }
            // Search returned MessageID here
            try {
                messageID = findMessageID(memberID, messageSenderID, messageCreationDate);
            } catch (ObjectNotFoundException onfe) {
                // Hack the Oracle 9i problem.
                Timestamp roundTimestamp = new Timestamp((messageCreationDate.getTime()/1000)*1000);
                try {
                    messageID = findMessageID(memberID, messageSenderID, roundTimestamp);
                } catch (ObjectNotFoundException e) {
                   throw new CreateException("Cannot find the Message in table Message.");
                }
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
        return messageID;
    }

    public Collection getAllMessages_inMember_inFolder_withSortSupport_limit(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("MessageSenderName")) &&
            (!sort.equals("MessageTopic")) &&
            (!sort.equals("MessageReadStatus")) &&
            (!sort.equals("MessageAttachCount")) &&
            (!sort.equals("MessageCreationDate")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inMember_inFolder_withSortSupport_limit_mysql(memberID, folderName, offset, rowsToReturn, sort, order, false);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inMember_inFolder_withSortSupport_limit_noscroll(memberID, folderName, offset, rowsToReturn, sort, order, false);
        }
        return getBeans_inMember_inFolder_withSortSupport_limit_general(memberID, folderName, offset, rowsToReturn, sort, order, false);
    }

    public Collection getNonPublicMessages_inMember_inFolder_withSortSupport_limit(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("MessageSenderName")) &&
            (!sort.equals("MessageTopic")) &&
            (!sort.equals("MessageReadStatus")) &&
            (!sort.equals("MessageAttachCount")) &&
            (!sort.equals("MessageCreationDate")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getBeans_inMember_inFolder_withSortSupport_limit_mysql(memberID, folderName, offset, rowsToReturn, sort, order, true);
        } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
            return getBeans_inMember_inFolder_withSortSupport_limit_noscroll(memberID, folderName, offset, rowsToReturn, sort, order, true);
        }
        return getBeans_inMember_inFolder_withSortSupport_limit_general(memberID, folderName, offset, rowsToReturn, sort, order, true);
    }

    /*
     * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
     *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
     *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
     *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns:
     */
    public Collection getBeans_inMember_inFolder_withSortSupport_limit_mysql(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order, boolean onlyNonPublic)
        throws DatabaseException {

        // IMPORTANT NOTE: the checking of parameters is moved to method getBeans_inMember_inFolder_withSortSupport_limit
        // IF THERE ARE ANY CHANGES HERE, PLEASE MOVE BACK THE CHECKING OF PARAMETERS

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        sql.append(" AND FolderName = ?");
        if (onlyNonPublic) {
            sql.append(" AND MessageType <> " + MessageBean.MESSAGE_TYPE_PUBLIC);
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setString(2, folderName);
            statement.setInt(3, offset);
            statement.setInt(4, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MessageBean bean = new MessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setFolderName(resultSet.getString("FolderName"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMessageSenderID(resultSet.getInt("MessageSenderID"));
                bean.setMessageSenderName(resultSet.getString("MessageSenderName"));
                bean.setMessageToList(resultSet.getString("MessageToList"));
                bean.setMessageCcList(resultSet.getString("MessageCcList"));
                bean.setMessageBccList(resultSet.getString("MessageBccList"));
                bean.setMessageTopic(resultSet.getString("MessageTopic"));
                bean.setMessageBody(resultSet.getString("MessageBody"));
                bean.setMessageType(resultSet.getInt("MessageType"));
                bean.setMessageOption(resultSet.getInt("MessageOption"));
                bean.setMessageStatus(resultSet.getInt("MessageStatus"));
                bean.setMessageReadStatus(resultSet.getInt("MessageReadStatus"));
                bean.setMessageNotify(resultSet.getInt("MessageNotify"));
                bean.setMessageIcon(resultSet.getString("MessageIcon"));
                bean.setMessageAttachCount(resultSet.getInt("MessageAttachCount"));
                bean.setMessageIP(resultSet.getString("MessageIP"));
                bean.setMessageCreationDate(resultSet.getTimestamp("MessageCreationDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getBeans_inMember_inFolder_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
     *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
     *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
     *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns:
     */
    public Collection getBeans_inMember_inFolder_withSortSupport_limit_noscroll(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order, boolean onlyNonPublic)
        throws DatabaseException {

        // IMPORTANT NOTE: the checking of parameters is moved to method getBeans_inMember_inFolder_withSortSupport_limit
        // IF THERE ARE ANY CHANGES HERE, PLEASE MOVE BACK THE CHECKING OF PARAMETERS

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        sql.append(" AND FolderName = ?");
        if (onlyNonPublic) {
            sql.append(" AND MessageType <> " + MessageBean.MESSAGE_TYPE_PUBLIC);
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setMaxRows(offset + rowsToReturn);
            statement.setInt(1, memberID);
            statement.setString(2, folderName);
            resultSet = statement.executeQuery();
            int rowIndex = -1;
            while (resultSet.next()) {
                rowIndex++;
                if (rowIndex < offset) {
                    continue;
                }
                MessageBean bean = new MessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setFolderName(resultSet.getString("FolderName"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMessageSenderID(resultSet.getInt("MessageSenderID"));
                bean.setMessageSenderName(resultSet.getString("MessageSenderName"));
                bean.setMessageToList(resultSet.getString("MessageToList"));
                bean.setMessageCcList(resultSet.getString("MessageCcList"));
                bean.setMessageBccList(resultSet.getString("MessageBccList"));
                bean.setMessageTopic(resultSet.getString("MessageTopic"));
                bean.setMessageBody(resultSet.getString("MessageBody"));
                bean.setMessageType(resultSet.getInt("MessageType"));
                bean.setMessageOption(resultSet.getInt("MessageOption"));
                bean.setMessageStatus(resultSet.getInt("MessageStatus"));
                bean.setMessageReadStatus(resultSet.getInt("MessageReadStatus"));
                bean.setMessageNotify(resultSet.getInt("MessageNotify"));
                bean.setMessageIcon(resultSet.getString("MessageIcon"));
                bean.setMessageAttachCount(resultSet.getInt("MessageAttachCount"));
                bean.setMessageIP(resultSet.getString("MessageIP"));
                bean.setMessageCreationDate(resultSet.getTimestamp("MessageCreationDate"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getBeans_inMember_inFolder_withSortSupport_limit_noscroll.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
     *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
     *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
     *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns:
     */
    public Collection getBeans_inMember_inFolder_withSortSupport_limit_general(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order, boolean onlyNonPublic)
        throws DatabaseException {

        // IMPORTANT NOTE: the checking of parameters is moved to method getBeans_inMember_inFolder_withSortSupport_limit
        // IF THERE ARE ANY CHANGES HERE, PLEASE MOVE BACK THE CHECKING OF PARAMETERS

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        sql.append(" AND FolderName = ?");
        if (onlyNonPublic) {
            sql.append(" AND MessageType <> " + MessageBean.MESSAGE_TYPE_PUBLIC);
        }
        sql.append(" ORDER BY " + sort + " " + order);// ColumnName, ASC|DESC
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, memberID);
            statement.setString(2, folderName);
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                MessageBean bean = new MessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setFolderName(resultSet.getString("FolderName"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMessageSenderID(resultSet.getInt("MessageSenderID"));
                bean.setMessageSenderName(resultSet.getString("MessageSenderName"));
                bean.setMessageToList(resultSet.getString("MessageToList"));
                bean.setMessageCcList(resultSet.getString("MessageCcList"));
                bean.setMessageBccList(resultSet.getString("MessageBccList"));
                bean.setMessageTopic(resultSet.getString("MessageTopic"));
                bean.setMessageBody(resultSet.getString("MessageBody"));
                bean.setMessageType(resultSet.getInt("MessageType"));
                bean.setMessageOption(resultSet.getInt("MessageOption"));
                bean.setMessageStatus(resultSet.getInt("MessageStatus"));
                bean.setMessageReadStatus(resultSet.getInt("MessageReadStatus"));
                bean.setMessageNotify(resultSet.getInt("MessageNotify"));
                bean.setMessageIcon(resultSet.getString("MessageIcon"));
                bean.setMessageAttachCount(resultSet.getInt("MessageAttachCount"));
                bean.setMessageIP(resultSet.getString("MessageIP"));
                bean.setMessageCreationDate(resultSet.getTimestamp("MessageCreationDate"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getBeans_inMember_inFolder_withSortSupport_limit_general.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
     *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
     *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
     *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns:
     */
    public Collection getPublicMessages()
        throws DatabaseException {

        // IMPORTANT NOTE: the checking of parameters is moved to method getBeans_inMember_inFolder_withSortSupport_limit
        // IF THERE ARE ANY CHANGES HERE, PLEASE MOVE BACK THE CHECKING OF PARAMETERS

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageType = " + MessageBean.MESSAGE_TYPE_PUBLIC);
        sql.append(" ORDER BY MessageCreationDate DESC");
        try {
            connection = DBUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql.toString());
            while (resultSet.next()) {
                MessageBean bean = new MessageBean();
                bean.setMessageID(resultSet.getInt("MessageID"));
                bean.setFolderName(resultSet.getString("FolderName"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setMessageSenderID(resultSet.getInt("MessageSenderID"));
                bean.setMessageSenderName(resultSet.getString("MessageSenderName"));
                bean.setMessageToList(resultSet.getString("MessageToList"));
                bean.setMessageCcList(resultSet.getString("MessageCcList"));
                bean.setMessageBccList(resultSet.getString("MessageBccList"));
                bean.setMessageTopic(resultSet.getString("MessageTopic"));
                bean.setMessageBody(resultSet.getString("MessageBody"));
                bean.setMessageType(resultSet.getInt("MessageType"));
                bean.setMessageOption(resultSet.getInt("MessageOption"));
                bean.setMessageStatus(resultSet.getInt("MessageStatus"));
                bean.setMessageReadStatus(resultSet.getInt("MessageReadStatus"));
                bean.setMessageNotify(resultSet.getInt("MessageNotify"));
                bean.setMessageIcon(resultSet.getString("MessageIcon"));
                bean.setMessageAttachCount(resultSet.getInt("MessageAttachCount"));
                bean.setMessageIP(resultSet.getString("MessageIP"));
                bean.setMessageCreationDate(resultSet.getTimestamp("MessageCreationDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getPublicMessages.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName,
     *                   MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody,
     *                   MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify,
     *                   MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate
     * Excluded columns:
     */
    public MessageBean getMessage(int messageID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MessageID, FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Message where primary key = (" + messageID + ").");
            }

            MessageBean bean = new MessageBean();
            // @todo: uncomment the following line(s) as needed
            //bean.setMessageID(messageID);
            bean.setMessageID(resultSet.getInt("MessageID"));
            bean.setFolderName(resultSet.getString("FolderName"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setMessageSenderID(resultSet.getInt("MessageSenderID"));
            bean.setMessageSenderName(resultSet.getString("MessageSenderName"));
            bean.setMessageToList(resultSet.getString("MessageToList"));
            bean.setMessageCcList(resultSet.getString("MessageCcList"));
            bean.setMessageBccList(resultSet.getString("MessageBccList"));
            bean.setMessageTopic(resultSet.getString("MessageTopic"));
            bean.setMessageBody(resultSet.getString("MessageBody"));
            bean.setMessageType(resultSet.getInt("MessageType"));
            bean.setMessageOption(resultSet.getInt("MessageOption"));
            bean.setMessageStatus(resultSet.getInt("MessageStatus"));
            bean.setMessageReadStatus(resultSet.getInt("MessageReadStatus"));
            bean.setMessageNotify(resultSet.getInt("MessageNotify"));
            bean.setMessageIcon(resultSet.getString("MessageIcon"));
            bean.setMessageAttachCount(resultSet.getInt("MessageAttachCount"));
            bean.setMessageIP(resultSet.getString("MessageIP"));
            bean.setMessageCreationDate(resultSet.getTimestamp("MessageCreationDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getMessage(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfNonPublicMessages_inMember(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        boolean onlyNonPublic = true;
        if (onlyNonPublic) {
            sql.append(" AND MessageType <> " + MessageBean.MESSAGE_TYPE_PUBLIC);
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in MessageDAOImplJDBC.getNumberOfNonPublicMessages_inMember.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getNumberOfNonPublicMessages_inMember.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateMessageReadStatus(int messageID, // primary key
                                        int memberID, int messageReadStatus)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET MessageReadStatus = ?");
        sql.append(" WHERE MessageID = ?");
        sql.append(" AND MemberID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, messageReadStatus);
            // primary key column(s)
            statement.setInt(2, messageID);

            statement.setInt(3, memberID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Message where primary key = (" + messageID + ") and MemberID = " + memberID + ".");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.updateMessageReadStatus.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    //@todo: should we update also based on MemberID ???
    public void updateAttachCount(int messageID, int messageAttachCount)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET MessageAttachCount = ?");
        sql.append(" WHERE MessageID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setInt(1, messageAttachCount);

            // primary key column(s)
            statement.setInt(2, messageID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update AttachCount in table Message where primary key = (" + messageID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.updateAttachCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

   public void updateFolderName(int messageID, // primary key
                                int memberID, String folderName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET FolderName = ?");
        sql.append(" WHERE MessageID = ? AND MemberID= ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, folderName);
            // primary key column(s)
            statement.setInt(2, messageID);
            statement.setInt(3, memberID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Message where primary key = (" + messageID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.updateFolderName.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void deleteMessage(int messageID, int memberID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MessageID = ?");
        sql.append(" AND MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            statement.setInt(2, memberID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Message where primary key = (" + messageID + ") and MemberID = " + memberID + ".");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.deleteMessage.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void deleteSenderMessages(int senderID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE MessageSenderID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, senderID);

            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.deleteSenderMessages.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void deleteMessages_inFolderName_inMember(String folderName, int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE FolderName = ?");
        sql.append(" AND MemberID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);

            statement.executeUpdate();

        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.deleteMessages_inFolderName_inMember.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }

    public int getNumberOfNonPublicMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException {
        // get the number of message in folder "folderName" and belong to "memberID"
        return getNumberOfMessages_inMember_inFolder(memberID, folderName, false, true);
    }

    public int getNumberOfUnreadNonPublicMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException {
        // get the number of unread message in folder "folderName" and belong to "memberID"
        return getNumberOfMessages_inMember_inFolder(memberID, folderName, true, true);
    }

    public int getNumberOfAllMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException {
        // get the number of message in folder "folderName" and belong to "memberID"
        return getNumberOfMessages_inMember_inFolder(memberID, folderName, false, false);
    }

    public int getNumberOfUnreadAllMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException {
        // get the number of unread message in folder "folderName" and belong to "memberID"
        return getNumberOfMessages_inMember_inFolder(memberID, folderName, true, false);
    }

    // if unread == true get the number of unread message in "folderName" and belong to "memberID"
    // else get the number of message in "folderName"
    private int getNumberOfMessages_inMember_inFolder(int memberID, String folderName, boolean unread, boolean onlyNonPublic)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE FolderName = ? AND MemberID = ? ");
        if (unread) {
            sql.append(" AND MessageReadStatus = 0");
        }
        if (onlyNonPublic) {
            sql.append(" AND MessageType <> " + MessageBean.MESSAGE_TYPE_PUBLIC);
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in MessageDAOImplJDBC.getNumberOfMessages_inMember_inFolder.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MessageDAOImplJDBC.getNumberOfMessages_inMember_inFolder.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
