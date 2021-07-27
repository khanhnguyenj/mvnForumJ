/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/AttachmentDAOImplJDBC.java,v 1.44 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.44 $
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

public class AttachmentDAOImplJDBC implements AttachmentDAO {

    private static final Logger log = LoggerFactory.getLogger(AttachmentDAOImplJDBC.class);

    public AttachmentDAOImplJDBC() {
    }

    /*
     * Included columns: PostID, MemberID, AttachFilename, AttachFileSize, AttachMimeType,
     *                   AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount,
     *                   AttachOption, AttachStatus
     * Excluded columns: AttachID
     */
    public void create(int postID, int memberID, String attachFilename,
                        int attachFileSize, String attachMimeType, String attachDesc,
                        String attachCreationIP, Timestamp attachCreationDate, Timestamp attachModifiedDate,
                        int attachDownloadCount, int attachOption, int attachStatus)
                        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getPostDAO().findByPrimaryKey(postID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Post' does not exist. Cannot create new Attachment.");
        }

        //if admin allowed guest to send attachments, we must allow that too
        if (memberID!=0) {
            try {
                // @todo: modify the parameter list as needed
                // You may have to regenerate this method if the needed columns don't have attribute 'include'
                DAOFactory.getMemberDAO().findByPrimaryKey(memberID);
            } catch(ObjectNotFoundException e) {
                throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Attachment.");
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (PostID, MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, postID);
            statement.setInt(2, memberID);
            statement.setString(3, attachFilename);
            statement.setInt(4, attachFileSize);
            statement.setString(5, attachMimeType);
            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(6, new StringReader(attachDesc), attachDesc.length());
            } else {
                statement.setString(6, attachDesc);
            }
            statement.setString(7, attachCreationIP);
            statement.setTimestamp(8, attachCreationDate);
            statement.setTimestamp(9, attachModifiedDate);
            statement.setInt(10, attachDownloadCount);
            statement.setInt(11, attachOption);
            statement.setInt(12, attachStatus);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Attachment'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int createAttachment(int postID, int memberID, String attachFilename,
                        int attachFileSize, String attachMimeType, String attachDesc,
                        String attachCreationIP, Timestamp attachCreationDate, Timestamp attachModifiedDate,
                        int attachDownloadCount, int attachOption, int attachStatus)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException, ObjectNotFoundException {

        create(postID, memberID, attachFilename, attachFileSize, attachMimeType, attachDesc, attachCreationIP, attachCreationDate, attachModifiedDate, attachDownloadCount, attachOption, attachStatus);

        int attachID = 0;
        try {
            attachID = findAttachID(postID, memberID, attachCreationDate);
        } catch (ObjectNotFoundException ex) {
            // Hack the Oracle 9i problem
            Timestamp roundTimestamp = new Timestamp((attachCreationDate.getTime()/1000)*1000);
            attachID = findAttachID(postID, memberID, roundTimestamp);
        }
        return attachID;
    }

    public void delete(int attachID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE AttachID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, attachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Attachment where primary key = (" + attachID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: PostID, MemberID, AttachFilename, AttachFileSize, AttachMimeType,
     *                   AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount,
     *                   AttachOption, AttachStatus
     * Excluded columns: AttachID
     */
    public AttachmentBean getAttachment(int attachID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT attach.PostID, attach.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus, post.ForumID");
        sql.append(" FROM " + TABLE_NAME + " attach, " + PostDAO.TABLE_NAME + " post");
        sql.append(" WHERE attach.PostID =  post.PostID");
        sql.append(" AND AttachID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, attachID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Attachment where primary key = (" + attachID + ").");
            }

            AttachmentBean bean = new AttachmentBean();
            bean.setAttachID(attachID);
            bean.setPostID(resultSet.getInt("PostID"));
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setAttachFilename(resultSet.getString("AttachFilename"));
            bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
            bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
            bean.setAttachDesc(resultSet.getString("AttachDesc"));
            bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
            bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
            bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
            bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
            bean.setAttachOption(resultSet.getInt("AttachOption"));
            bean.setAttachStatus(resultSet.getInt("AttachStatus"));
            bean.setForumID(resultSet.getInt("ForumID"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachment(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
     *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
     *                   AttachDownloadCount, AttachOption, AttachStatus
     * Excluded columns:
     */
    public Collection getAttachments()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attach.PostID, attach.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus, post.ForumID");
        sql.append(" FROM " + TABLE_NAME + " attach, " + PostDAO.TABLE_NAME + " post");
        sql.append(" WHERE attach.PostID =  post.PostID");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                bean.setForumID(resultSet.getInt("ForumID"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfAttachments(int category, int forum)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME + " attach");

        if ((forum > -1) || (category > -1)) {
            sql.append(" , " + PostDAO.TABLE_NAME + " post");
        }
        if (category > -1) {
            sql.append(" , " + ForumDAO.TABLE_NAME + " forum");
        }

        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in AttachmentDAOImplJDBC.getNumberOfAttachments.");
        } if (category > -1) {
            sql.append(" WHERE post.PostID = attach.PostID ");
            sql.append(" AND (forum.ForumID = post.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" WHERE (post.PostID = attach.PostID AND post.ForumID = ?) ");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            if (category > -1) {
                statement.setInt(1, category);
            } else if (forum > -1) {
                statement.setInt(1, forum);
            }

            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in AttachmentDAOImplJDBC.getNumberOfAttachments.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getNumberOfAttachments.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfAttachments_inPost(int postID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in AttachmentDAOImplJDBC.getNumberOfAttachments_inPost.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getNumberOfAttachments_inPost.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

   public int getNumberOfAttachments_inThread(int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME + " attachment, " + PostDAO.TABLE_NAME + " post");
        sql.append(" WHERE attachment.PostID = post.PostID ");
        sql.append(" AND post.ThreadID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in AttachmentDAOImplJDBC.getNumberOfAttachments_inThread.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getNumberOfAttachments_inThread.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    /************************************************
     * Customized methods come below
     ************************************************/

    /**
     * This is a customized method
     */
    protected static int findAttachID(int postID, int memberID, Timestamp attachCreationDate)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MAX(AttachID)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ? AND MemberID = ? AND AttachCreationDate = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            statement.setInt(2, memberID);
            statement.setTimestamp(3, attachCreationDate);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the AttachID in table Attachment.");
            }

            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.findAttachID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete_inPost(int postID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            statement.executeUpdate();
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.delete_inPost.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
     *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
     *                   AttachDownloadCount, AttachOption, AttachStatus
     * Excluded columns:
     */
    public Collection getAttachments_inPost(int postID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, PostID, MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");
        sql.append(" ORDER BY AttachID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_inPost.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
     *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
     *                   AttachDownloadCount, AttachOption, AttachStatus
     * Excluded columns:
     */
    public Collection getAttachments_inThread(int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attachment.PostID, attachment.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus");
        sql.append(" FROM " + TABLE_NAME + " attachment, " + PostDAO.TABLE_NAME + " post ");
        sql.append(" WHERE attachment.PostID = post.PostID AND post.ThreadID = ? ");
        sql.append(" ORDER BY AttachID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_inThread.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
     *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
     *                   AttachDownloadCount, AttachOption, AttachStatus
     * Excluded columns:
     */
    public Collection getAttachments_inForum(int forumID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attachment.PostID, attachment.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus");
        sql.append(" FROM " + TABLE_NAME + " attachment, " + PostDAO.TABLE_NAME + " post ");
        sql.append(" WHERE attachment.PostID = post.PostID AND post.ForumID = ? ");
        sql.append(" ORDER BY AttachID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_inForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /**
     * This method should be call only when we can make sure that postID is in database
     */
    public void increaseDownloadCount(int attachID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET AttachDownloadCount = AttachDownloadCount + 1 WHERE AttachID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, attachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the AttachDownloadCount in table Attachment. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.increaseDownloadCount.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateAttachDesc(int attachID, String attachDesc)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET AttachDesc = ? WHERE AttachID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            if (DBUtils.getDatabaseType() == DBUtils.DATABASE_ORACLE) {
                statement.setCharacterStream(1, new StringReader(attachDesc), attachDesc.length());
            } else {
                statement.setString(1, attachDesc);
            }
            statement.setInt(2, attachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the Desc in table Attachment. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.updateAttachDesc.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateAttachOption(int attachID, int attachOption)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        String sql = "UPDATE " + TABLE_NAME + " SET AttachOption = ? WHERE AttachID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, attachOption);
            statement.setInt(2, attachID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update the Option in table Attachment. Please contact Web site Administrator.");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.updateAttachOption.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getAttachments_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        if ((!sort.equals("AttachFilename")) &&
            (!sort.equals("AttachFileSize")) &&
            (!sort.equals("AttachID")) &&
            (!sort.equals("PostID")) &&
            (!sort.equals("AttachMimeType")) &&
            (!sort.equals("AttachDownloadCount")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the criteria '" + sort + "'.");
        }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC")) ) {
            throw new IllegalArgumentException("Cannot sort, reason: don't understand the order '" + order + "'.");
        }

        if ((category > -1) && (forum > -1)) {
            throw new IllegalArgumentException("getAttachments_withSortSupport_limit() does not accept (category > -1) and (forum > -1)");
        }

        if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
            return getAttachments_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order, category, forum);
        }

        return getAttachments_withSortSupport_limit_general(offset, rowsToReturn, sort, order, category, forum);
    }

    /*
     * Included columns: AttachID, PostID, MemberID, AttachFilename, AttachFileSize,
     *                   AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate,
     *                   AttachDownloadCount, AttachOption, AttachStatus
     * Excluded columns:
     */
    public Collection getAttachments_withSortSupport_limit_mysql(int offset, int rowsToReturn, String sort, String order, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attach.PostID, attach.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus, post.ForumID");
        sql.append(" FROM " + TABLE_NAME + " attach, " + PostDAO.TABLE_NAME + " post");
        if (category > -1) {
            sql.append(" , " + ForumDAO.TABLE_NAME + " forum");
        }
        sql.append(" WHERE attach.PostID = post.PostID");
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in AttachmentDAOImplJDBC.getAttachments_withSortSupport_limit_mysql()");
        } if (category > -1) {
            sql.append(" AND (forum.ForumID = post.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND post.ForumID = ? ");
        } else {
            // do nothing, because we will get all attachment
        }
        sql.append(" ORDER BY " + sort + " " + order);
        sql.append(" LIMIT ?, ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            int i = 0;
            if (category > -1) {
                statement.setInt(++i, category);
            } else if (forum > -1) {
                statement.setInt(++i, forum);
            } else {
                // do nothing, because we will get all attachment
            }
            statement.setInt(++i, offset);
            statement.setInt(++i, rowsToReturn);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                bean.setForumID(resultSet.getInt("ForumID"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getAttachments_withSortSupport_limit_general(int offset, int rowsToReturn, String sort, String order, int category, int forum)
        throws IllegalArgumentException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attach.PostID, attach.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus, post.ForumID");
        sql.append(" FROM " + TABLE_NAME + " attach, " + PostDAO.TABLE_NAME + " post");
        if (category > -1) {
            sql.append(" , " + ForumDAO.TABLE_NAME + " forum");
        }
        sql.append(" WHERE attach.PostID = post.PostID");
        if ((category > -1) && (forum > -1)) {
            throw new AssertionError("Assertion in AttachmentDAOImplJDBC.getAttachments_withSortSupport_limit_mysql()");
        } if (category > -1) {
            sql.append(" AND (forum.ForumID = post.ForumID AND forum.CategoryID = ?) ");
        } else if (forum > -1) {
            sql.append(" AND post.ForumID = ? ");
        } else {
            // do nothing, because we will get all attachment
        }
        sql.append(" ORDER BY " + sort + " " + order);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int i = 0;
            if (category > -1) {
                statement.setInt(++i, category);
            } else if (forum > -1) {
                statement.setInt(++i, forum);
            } else {
                // do nothing, because we will get all attachment
            }
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                bean.setForumID(resultSet.getInt("ForumID"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) {
                    break;// Fix the Sybase bug
                }
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_withSortSupport_limit_mysql.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getMaxAttachmentID() throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MAX(AttachID)");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in AttachmentDAOImplJDBC.getMaxAttachmentID.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getMaxAttachmentID.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getAttachments_fromIDRange(int fromID, int toID)
        throws IllegalArgumentException, DatabaseException {

        if (fromID < 0) {
            throw new IllegalArgumentException("The fromID < 0 is not allowed.");
        }
        if (toID < fromID) {
            throw new IllegalArgumentException("toID < fromID is not allowed.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT AttachID, attach.PostID, attach.MemberID, AttachFilename, AttachFileSize, AttachMimeType, AttachDesc, AttachCreationIP, AttachCreationDate, AttachModifiedDate, AttachDownloadCount, AttachOption, AttachStatus, post.ForumID");
        sql.append(" FROM " + TABLE_NAME + " attach, " + PostDAO.TABLE_NAME + " post");
        sql.append(" WHERE attach.PostID =  post.PostID");
        sql.append(" AND (AttachID >= ?) AND (AttachID <= ?)");
        sql.append(" ORDER BY AttachID ASC ");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, fromID);
            statement.setInt(2, toID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                AttachmentBean bean = new AttachmentBean();
                bean.setAttachID(resultSet.getInt("AttachID"));
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setMemberID(resultSet.getInt("MemberID"));
                bean.setAttachFilename(resultSet.getString("AttachFilename"));
                bean.setAttachFileSize(resultSet.getInt("AttachFileSize"));
                bean.setAttachMimeType(resultSet.getString("AttachMimeType"));
                bean.setAttachDesc(resultSet.getString("AttachDesc"));
                bean.setAttachCreationIP(resultSet.getString("AttachCreationIP"));
                bean.setAttachCreationDate(resultSet.getTimestamp("AttachCreationDate"));
                bean.setAttachModifiedDate(resultSet.getTimestamp("AttachModifiedDate"));
                bean.setAttachDownloadCount(resultSet.getInt("AttachDownloadCount"));
                bean.setAttachOption(resultSet.getInt("AttachOption"));
                bean.setAttachStatus(resultSet.getInt("AttachStatus"));
                bean.setForumID(resultSet.getInt("ForumID"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in AttachmentDAOImplJDBC.getAttachments_fromIDRange.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}// end of class AttachmentDAOImplJDBC
