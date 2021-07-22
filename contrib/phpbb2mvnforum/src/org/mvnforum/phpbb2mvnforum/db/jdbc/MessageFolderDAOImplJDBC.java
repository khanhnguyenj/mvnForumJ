/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/MessageFolderDAOImplJDBC.java,v 1.4 2008/03/31 10:34:51 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2008/03/31 10:34:51 $
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
import java.util.Collection;

import org.mvnforum.util.DBUtils;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.db.MessageFolderBean;
import com.mvnforum.db.MessageFolderDAO;

public class MessageFolderDAOImplJDBC implements MessageFolderDAO {

    public void findByPrimaryKey(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT FolderOrder");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE FolderName = ? AND MemberID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, folderName);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + folderName + ", " + memberID + ") in table 'MessageFolder'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }


    }

    public void create(String folderName, int memberID, int folderOrder, int folderStatus, int folderOption,
            int folderType, Timestamp folderCreationDate, Timestamp folderModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        try {
            //Check if primary key already exists
            findByPrimaryKey(folderName, memberID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new MessageFolder with the same [FolderName, MemberID] (" + folderName + ", " + memberID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
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
            throw new DatabaseException("Error executing SQL in MessageFolderDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }


    }

    public void delete(String folderName, int memberID)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub

    }

    public MessageFolderBean getMessageFolder(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getMessageFolders_inMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getMaxFolderOrder(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void increaseFolderOrder(String folderName, int memberID, Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public void decreaseFolderOrder(String folderName, int memberID, Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

}
