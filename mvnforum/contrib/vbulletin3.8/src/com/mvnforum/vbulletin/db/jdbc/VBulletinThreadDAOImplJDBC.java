/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinThreadDAOImplJDBC.java,v 1.4 2009/10/09 09:27:23 thonh Exp $
 * $Author: thonh $
 * $Revision: 1.4 $
 * $Date: 2009/10/09 09:27:23 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.vbulletin.db.jdbc;

import java.sql.*;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.db.DBUtils2;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.vbulletin.db.VBulletinThreadBean;
import com.mvnforum.vbulletin.db.VBulletinThreadDAO;

/**
 * The Class VBulletinThreadDAOImplJDBC.
 */
public class VBulletinThreadDAOImplJDBC implements VBulletinThreadDAO {

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinThreadDAO#getNextThread(int, int)
     */
    public VBulletinThreadBean getNextThread(int threadID, int forumID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ThreadID, Title, PrefixID, FirstPostID, LastPostID, LastPost, ForumID, PollID, Open, ReplyCount, HiddenCount, DeletedCount, PostUsername, PostUserid, LastPoster, DateLine, Views, IconID, Notes, Visible, Sticky, VoteNum, VoteTotal, Attach, Similar, Taglist");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID > ? AND ForumID = ?");
        sql.append(" ORDER BY ThreadID ASC");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            statement.setInt(2, forumID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            VBulletinThreadBean bean = new VBulletinThreadBean();
            // TODO: uncomment the following line(s) as needed
            //bean.setThreadID(threadID);
            bean.setThreadID(resultSet.getInt("ThreadID"));
            bean.setTitle(resultSet.getString("Title"));
            bean.setPrefixID(resultSet.getString("PrefixID"));
            bean.setFirstPostID(resultSet.getInt("FirstPostID"));
            bean.setLastPostID(resultSet.getInt("LastPostID"));
            bean.setLastPost(resultSet.getLong("LastPost"));
            bean.setForumID(resultSet.getInt("ForumID"));
            bean.setPollID(resultSet.getInt("PollID"));
            bean.setOpen(resultSet.getInt("Open"));
            bean.setReplyCount(resultSet.getInt("ReplyCount"));
            bean.setHiddenCount(resultSet.getInt("HiddenCount"));
            bean.setDeletedCount(resultSet.getInt("DeletedCount"));
            bean.setPostUsername(resultSet.getString("PostUsername"));
            bean.setPostUserid(resultSet.getInt("PostUserid"));
            bean.setLastPoster(resultSet.getString("LastPoster"));
            bean.setDateLine(resultSet.getLong("DateLine"));
            bean.setViews(resultSet.getInt("Views"));
            bean.setIconID(resultSet.getInt("IconID"));
            bean.setNotes(resultSet.getString("Notes"));
            bean.setVisible(resultSet.getInt("Visible"));
            bean.setSticky(resultSet.getInt("Sticky"));
            bean.setVoteNum(resultSet.getInt("VoteNum"));
            bean.setVoteTotal(resultSet.getInt("VoteTotal"));
            bean.setAttach(resultSet.getInt("Attach"));
            bean.setSimilar(resultSet.getString("Similar"));
            bean.setTaglist(resultSet.getString("Taglist"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in ThreadDAOImplJDBC.getNextThread(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
