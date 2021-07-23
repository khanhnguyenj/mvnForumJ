/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/ForumDAOImplJDBC.java,v 1.5 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
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
import java.util.Collection;

import org.mvnforum.phpbb2mvnforum.db.ForumDAO;
import org.mvnforum.util.DBUtils;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

public class ForumDAOImplJDBC implements ForumDAO {

    public void findByPrimaryKey(int forumID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + forumID + ") in table 'forum'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in forumDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_ForumName_CategoryID(String forumName, int categoryID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumName, CategoryID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ForumName = ? AND CategoryID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, forumName);
            statement.setInt(2, categoryID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [ForumName, CategoryID] (" + forumName
                        + ", " + categoryID + ") in table 'forum'.");
            }
        } catch (SQLException sqle) {
            throw new DatabaseException(
                    "Error executing SQL in forumDAOImplJDBC.findByAlternateKey_ForumName_CategoryID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ForumID, CategoryID, LastPostMemberName, ForumName, ForumDesc, 
     *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, 
     *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, 
     *                   ForumThreadCount, ForumPostCount
     * Excluded columns: 
     */
    public void create(int forumID, int categoryID, String lastPostMemberName, 
                        String forumName, String forumDesc, Timestamp forumCreationDate, 
                        Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder, 
                        int forumType, int forumFormatOption, int forumOption, 
                        int forumStatus, int forumModerationMode, String forumPassword, 
                        int forumThreadCount, int forumPostCount)
        throws CreateException, DatabaseException/*, DuplicateKeyException*/, DuplicateKeyException {

        // @todo: comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(forumID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new forum with the same [ForumID] (" + forumID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_ForumName_CategoryID(forumName, categoryID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new forum with the same [ForumName, CategoryID] (" + forumName + ", " + categoryID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (ForumID, CategoryID, LastPostMemberName, ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword, ForumThreadCount, ForumPostCount)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, forumID);
            statement.setInt(2, categoryID);
            statement.setString(3, lastPostMemberName);
            statement.setString(4, forumName);
            statement.setString(5, forumDesc);
            statement.setTimestamp(6, forumCreationDate);
            statement.setTimestamp(7, forumModifiedDate);
            statement.setTimestamp(8, forumLastPostDate);
            statement.setInt(9, forumOrder);
            statement.setInt(10, forumType);
            statement.setInt(11, forumFormatOption);
            statement.setInt(12, forumOption);
            statement.setInt(13, forumStatus);
            statement.setInt(14, forumModerationMode);
            statement.setString(15, forumPassword);
            statement.setInt(16, forumThreadCount);
            statement.setInt(17, forumPostCount);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'forum'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in forumDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void createMultiple(Collection beans) {
        // TODO Auto-generated method stub
        
    }
    
}
