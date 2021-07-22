/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/MessageDAOImplJDBC.java,v 1.3 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:12 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.MessageDAO;
import org.mvnforum.util.DBUtils;

public class MessageDAOImplJDBC implements MessageDAO {

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
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, messageID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + messageID + ") in table 'message'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in messageDAOImplJDBC.findByPrimaryKey.");
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
    public int create(String folderName, int memberID, int messageSenderID, String messageSenderName,
            String messageToList, String messageCcList, String messageBccList, String messageTopic, String messageBody,
            int messageType, int messageOption, int messageStatus, int messageReadStatus, int messageNotify,
            String messageIcon, int messageAttachCount, String messageIP, Timestamp messageCreationDate)
        throws CreateException, DatabaseException, DuplicateKeyException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("INSERT INTO "
                        + TABLE_NAME
                        + " (FolderName, MemberID, MessageSenderID, MessageSenderName, MessageToList, MessageCcList, MessageBccList, MessageTopic, MessageBody, MessageType, MessageOption, MessageStatus, MessageReadStatus, MessageNotify, MessageIcon, MessageAttachCount, MessageIP, MessageCreationDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            statement.setInt(3, messageSenderID);
            statement.setString(4, messageSenderName);
            statement.setString(5, messageToList);
            statement.setString(6, messageCcList);
            statement.setString(7, messageBccList);
            statement.setString(8, messageTopic);
            statement.setString(9, messageBody);
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
                throw new CreateException("Error adding a row into table 'message'.");
            }
            return 0;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in messageDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
