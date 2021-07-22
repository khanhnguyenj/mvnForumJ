/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinForumDAOImplJDBC.java,v 1.3 2009/10/07 04:23:23 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2009/10/07 04:23:23 $
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
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.db.DBUtils2;
import net.myvietnam.mvncore.exception.DatabaseException;

import com.mvnforum.vbulletin.db.VBulletinForumBean;
import com.mvnforum.vbulletin.db.VBulletinForumDAO;

/**
 * The Class VBulletinForumDAOImplJDBC.
 */
public class VBulletinForumDAOImplJDBC implements VBulletinForumDAO {
    
    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinForumDAO#getForumsByCategory(int)
     */
    public Collection getForumsByCategory(int categoryID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, StyleID, Title, Title_Clean, Description, Description_Clean, Options, ShowPrivate, DisplayOrder, ReplyCount, LastPost, LastPoster, LastPostID, LastThread, LastThreadID, LastIconID, LastPrefixID, ThreadCount, DaySprune, NewPostEmail, NewThreadEmail, ParentID, ParentList, Password, Link, ChildList, DefaultSortField, DefaultSortOrder, ImagePrefix");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ParentID = ?");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, categoryID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
               VBulletinForumBean bean = new VBulletinForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setStyleID(resultSet.getInt("StyleID"));
                bean.setTitle(resultSet.getString("Title"));
                bean.setTitle_Clean(resultSet.getString("Title_Clean"));
                bean.setDescription(resultSet.getString("Description"));
                bean.setDescription_Clean(resultSet.getString("Description_Clean"));
                bean.setOptions(resultSet.getInt("Options"));
                bean.setShowPrivate(resultSet.getInt("ShowPrivate"));
                bean.setDisplayOrder(resultSet.getInt("DisplayOrder"));
                bean.setReplyCount(resultSet.getInt("ReplyCount"));
                bean.setLastPost(resultSet.getLong("LastPost"));
                bean.setLastPoster(resultSet.getString("LastPoster"));
                bean.setLastPostID(resultSet.getInt("LastPostID"));
                bean.setLastThread(resultSet.getString("LastThread"));
                bean.setLastThreadID(resultSet.getInt("LastThreadID"));
                bean.setLastIconID(resultSet.getInt("LastIconID"));
                bean.setLastPrefixID(resultSet.getString("LastPrefixID"));
                bean.setThreadCount(resultSet.getInt("ThreadCount"));
                bean.setDaySprune(resultSet.getInt("DaySprune"));
                bean.setNewPostEmail(resultSet.getString("NewPostEmail"));
                bean.setNewThreadEmail(resultSet.getString("NewThreadEmail"));
                bean.setParentID(resultSet.getInt("ParentID"));
                bean.setParentList(resultSet.getString("ParentList"));
                bean.setPassword(resultSet.getString("Password"));
                bean.setLink(resultSet.getString("Link"));
                bean.setChildList(resultSet.getString("ChildList"));
                bean.setDefaultSortField(resultSet.getString("DefaultSortField"));
                bean.setDefaultSortOrder(resultSet.getString("DefaultSortOrder"));
                bean.setImagePrefix(resultSet.getString("ImagePrefix"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }
    
    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinForumDAO#getCategories()
     */
    public Collection getCategories()
        throws DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, StyleID, Title, Title_Clean, Description, Description_Clean, Options, ShowPrivate, DisplayOrder, ReplyCount, LastPost, LastPoster, LastPostID, LastThread, LastThreadID, LastIconID, LastPrefixID, ThreadCount, DaySprune, NewPostEmail, NewThreadEmail, ParentID, ParentList, Password, Link, ChildList, DefaultSortField, DefaultSortOrder, ImagePrefix");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ParentID = -1");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                VBulletinForumBean bean = new VBulletinForumBean();
                bean.setForumID(resultSet.getInt("ForumID"));
                bean.setStyleID(resultSet.getInt("StyleID"));
                bean.setTitle(resultSet.getString("Title"));
                bean.setTitle_Clean(resultSet.getString("Title_Clean"));
                bean.setDescription(resultSet.getString("Description"));
                bean.setDescription_Clean(resultSet.getString("Description_Clean"));
                bean.setOptions(resultSet.getInt("Options"));
                bean.setShowPrivate(resultSet.getInt("ShowPrivate"));
                bean.setDisplayOrder(resultSet.getInt("DisplayOrder"));
                bean.setReplyCount(resultSet.getInt("ReplyCount"));
                bean.setLastPost(resultSet.getInt("LastPost"));
                bean.setLastPoster(resultSet.getString("LastPoster"));
                bean.setLastPostID(resultSet.getInt("LastPostID"));
                bean.setLastThread(resultSet.getString("LastThread"));
                bean.setLastThreadID(resultSet.getInt("LastThreadID"));
                bean.setLastIconID(resultSet.getInt("LastIconID"));
                bean.setLastPrefixID(resultSet.getString("LastPrefixID"));
                bean.setThreadCount(resultSet.getInt("ThreadCount"));
                bean.setDaySprune(resultSet.getInt("DaySprune"));
                bean.setNewPostEmail(resultSet.getString("NewPostEmail"));
                bean.setNewThreadEmail(resultSet.getString("NewThreadEmail"));
                bean.setParentID(resultSet.getInt("ParentID"));
                bean.setParentList(resultSet.getString("ParentList"));
                bean.setPassword(resultSet.getString("Password"));
                bean.setLink(resultSet.getString("Link"));
                bean.setChildList(resultSet.getString("ChildList"));
                bean.setDefaultSortField(resultSet.getString("DefaultSortField"));
                bean.setDefaultSortOrder(resultSet.getString("DefaultSortOrder"));
                bean.setImagePrefix(resultSet.getString("ImagePrefix"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in ForumDAOImplJDBC.getCategories.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
