/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/WatchDAOImplJDBC.java,v 1.5 2008/03/31 10:34:51 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
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

import com.mvnforum.MVNForumConstant;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.WatchBean;
import com.mvnforum.db.WatchDAO;

public class WatchDAOImplJDBC implements WatchDAO {

    public void findByPrimaryKey(int watchID)
        throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchType");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE WatchID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, watchID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + watchID + ") in table 'Watch'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID, int forumID,
            int threadID)
        throws ObjectNotFoundException, DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT WatchType");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ? AND CategoryID = ? AND ForumID = ? AND ThreadID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            statement.setInt(2, categoryID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [MemberID, CategoryID, ForumID, ThreadID] (" + memberID + ", " + categoryID + ", " + forumID + ", " + threadID + ") in table 'Watch'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }

    public void create(int memberID, int categoryID, int forumID, int threadID, int watchType, int watchOption,
            int watchStatus, Timestamp watchCreationDate, Timestamp watchLastSentDate, Timestamp watchEndDate)
        throws IllegalArgumentException, CreateException, DatabaseException, DuplicateKeyException,
        ForeignKeyNotFoundException {
        if ((memberID == 0) || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST)) {
            throw new IllegalArgumentException("Cannot add a new watch for Guest (id = " + memberID + ")");
        }
        int notZeroCount = 0;
        if (categoryID != 0) {
            notZeroCount++;
        }
        if (forumID != 0) {
            notZeroCount++;
        }
        if (threadID != 0) {
            notZeroCount++;
        }
        if (notZeroCount > 1) {
            throw new IllegalArgumentException("Cannot add watch with more than 1 element.");
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(memberID, categoryID, forumID, threadID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Watch with the same [MemberID, CategoryID, ForumID, ThreadID] (" + memberID + ", " + categoryID + ", " + forumID + ", " + threadID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            if (categoryID != 0) {
                DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Category' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            if (forumID != 0) {
                DAOFactory.getForumDAO().findByPrimaryKey(forumID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Forum' does not exist. Cannot create new Watch.");
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            if (threadID != 0) {
                DAOFactory.getThreadDAO().findByPrimaryKey(threadID);
            }
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Thread' does not exist. Cannot create new Watch.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, CategoryID, ForumID, ThreadID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setInt(2, categoryID);
            statement.setInt(3, forumID);
            statement.setInt(4, threadID);
            statement.setInt(5, watchType);
            statement.setInt(6, watchOption);
            statement.setInt(7, watchStatus);
            statement.setTimestamp(8, watchCreationDate);
            statement.setTimestamp(9, watchLastSentDate);
            statement.setTimestamp(10, watchEndDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Watch'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in WatchDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }

    public void delete(int watchID)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub

    }

    public void delete_inMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public void delete_inCategory(int categoryID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public void delete_inForum(int forumID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public void delete_inThread(int threadID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    public void updateLastSentDate(int watchID, Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public WatchBean getWatch(int watchID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public WatchBean getWatch_byAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID,
            int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getWatches()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public int getNumberOfWatches()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getNumberOfWatches_forMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

    public Collection getMemberBeans()
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getWatches_forMember(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateLastSentDate_forMember(int memberID, Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public void updateWatchType(int watchID, int watchType)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        
    }

}
