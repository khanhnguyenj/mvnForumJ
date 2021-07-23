/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbTopicsDAOImpl.java,v 1.6 2007/12/18 04:58:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2007/12/18 04:58:44 $
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
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.PhpbbTopics;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbTopicsDAOImpl implements PhpbbTopicsDAO {
    
    /*
     * Included columns: topic_id, forum_id, topic_title, topic_poster, topic_time, 
     *                   topic_views, topic_replies, topic_status, topic_vote, topic_type, 
     *                   topic_first_post_id, topic_last_post_id, topic_moved_id
     * Excluded columns: 
     */
    public Collection getBeans() throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, forum_id, topic_title, topic_poster, topic_time, topic_views, topic_replies, topic_status, topic_vote, topic_type, topic_first_post_id, topic_last_post_id, topic_moved_id");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbTopics bean = new PhpbbTopics();
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.settopic_title(resultSet.getString("topic_title"));
                bean.settopic_poster(resultSet.getInt("topic_poster"));
                bean.settopic_time(resultSet.getInt("topic_time"));
                bean.settopic_views(resultSet.getInt("topic_views"));
                bean.settopic_replies(resultSet.getInt("topic_replies"));
                bean.settopic_status(resultSet.getInt("topic_status"));
                bean.settopic_vote(resultSet.getInt("topic_vote"));
                bean.settopic_type(resultSet.getInt("topic_type"));
                bean.settopic_first_post_id(resultSet.getInt("topic_first_post_id"));
                bean.settopic_last_post_id(resultSet.getInt("topic_last_post_id"));
                bean.settopic_moved_id(resultSet.getInt("topic_moved_id"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_topicsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansByForumID(int forumID)
        throws DatabaseException {
        // TODO Auto-generated method stub
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, forum_id, topic_title, topic_poster, topic_time, topic_views, topic_replies, topic_status, topic_vote, topic_type, topic_first_post_id, topic_last_post_id, topic_moved_id");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE forum_id = ?"); 
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, forumID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbTopics bean = new PhpbbTopics();
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.settopic_title(resultSet.getString("topic_title"));
                bean.settopic_poster(resultSet.getInt("topic_poster"));
                bean.settopic_time(resultSet.getInt("topic_time"));
                bean.settopic_views(resultSet.getInt("topic_views"));
                bean.settopic_replies(resultSet.getInt("topic_replies"));
                bean.settopic_status(resultSet.getInt("topic_status"));
                bean.settopic_vote(resultSet.getInt("topic_vote"));
                bean.settopic_type(resultSet.getInt("topic_type"));
                bean.settopic_first_post_id(resultSet.getInt("topic_first_post_id"));
                bean.settopic_last_post_id(resultSet.getInt("topic_last_post_id"));
                bean.settopic_moved_id(resultSet.getInt("topic_moved_id"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_topicsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public PhpbbTopics getBeans(int topicID) throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT topic_id, forum_id, topic_title, topic_poster, topic_time, topic_views, topic_replies, topic_status, topic_vote, topic_type, topic_first_post_id, topic_last_post_id, topic_moved_id");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE topic_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, topicID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_topics where primary key = (" + topicID + ").");
            }

            PhpbbTopics bean = new PhpbbTopics();
            // @todo: uncomment the following line(s) as needed
            //bean.settopic_id(topic_id);
            bean.settopic_id(resultSet.getInt("topic_id"));
            bean.setforum_id(resultSet.getInt("forum_id"));
            bean.settopic_title(resultSet.getString("topic_title"));
            bean.settopic_poster(resultSet.getInt("topic_poster"));
            bean.settopic_time(resultSet.getInt("topic_time"));
            bean.settopic_views(resultSet.getInt("topic_views"));
            bean.settopic_replies(resultSet.getInt("topic_replies"));
            bean.settopic_status(resultSet.getInt("topic_status"));
            bean.settopic_vote(resultSet.getInt("topic_vote"));
            bean.settopic_type(resultSet.getInt("topic_type"));
            bean.settopic_first_post_id(resultSet.getInt("topic_first_post_id"));
            bean.settopic_last_post_id(resultSet.getInt("topic_last_post_id"));
            bean.settopic_moved_id(resultSet.getInt("topic_moved_id"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_topicsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
