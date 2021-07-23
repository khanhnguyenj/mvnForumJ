/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbPostDAOImpl.java,v 1.6 2007/12/18 04:58:44 minhnn Exp $
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

import org.mvnforum.phpbb2mvnforum.db.PhpbbPost;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbPostDAOImpl implements PhpbbPostDAO {

    /*
     * Included columns: post_id, topic_id, forum_id, poster_id, post_time, 
     *                   poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, 
     *                   enable_sig, post_edit_time, post_edit_count
     * Excluded columns: 
     */
    public int getPosterIDFromPostID(int post_id)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT poster_id");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE post_id = ?");
        int posterID = 0;
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, post_id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_posts where primary key = ("
                        + post_id + ").");
            }

            posterID = resultSet.getInt("poster_id");
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_postsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
        return posterID;
    }

    /*
     * Included columns: post_id, topic_id, forum_id, poster_id, post_time, 
     *                   poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, 
     *                   enable_sig, post_edit_time, post_edit_count
     * Excluded columns: 
     */
    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT post_id, topic_id, forum_id, poster_id, post_time, poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPost bean = new PhpbbPost();
                bean.setpost_id(resultSet.getInt("post_id"));
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.setposter_id(resultSet.getInt("poster_id"));
                bean.setpost_time(resultSet.getInt("post_time"));
                bean.setposter_ip(resultSet.getString("poster_ip"));
                bean.setpost_username(resultSet.getString("post_username"));
                bean.setenable_bbcode(resultSet.getInt("enable_bbcode"));
                bean.setenable_html(resultSet.getInt("enable_html"));
                bean.setenable_smilies(resultSet.getInt("enable_smilies"));
                bean.setenable_sig(resultSet.getInt("enable_sig"));
                bean.setpost_edit_time(resultSet.getInt("post_edit_time"));
                bean.setpost_edit_count(resultSet.getInt("post_edit_count"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_postsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: post_id, topic_id, forum_id, poster_id, post_time, 
     *                   poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, 
     *                   enable_sig, post_edit_time, post_edit_count
     * Excluded columns: 
     */
    public Collection getPostIDsFromTopicID(int topicID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT post_id");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE topic_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, topicID);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                PhpbbPost bean = new PhpbbPost();
                bean.setpost_id(resultSet.getInt("post_id"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_postsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public long getPostTimeFromPostID(int post_id) throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT post_time");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE post_id = ?");
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, post_id);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table phpbb_posts where primary key = (" + post_id + ").");
            }
            return resultSet.getLong("post_time");
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_postsDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }


    }

    public Collection getBeansByThreadID(int threadID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT post_id, topic_id, forum_id, poster_id, post_time, poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE topic_id = ?");
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPost bean = new PhpbbPost();
                bean.setpost_id(resultSet.getInt("post_id"));
                bean.settopic_id(resultSet.getInt("topic_id"));
                bean.setforum_id(resultSet.getInt("forum_id"));
                bean.setposter_id(resultSet.getInt("poster_id"));
                bean.setpost_time(resultSet.getInt("post_time"));
                bean.setposter_ip(resultSet.getString("poster_ip"));
                bean.setpost_username(resultSet.getString("post_username"));
                bean.setenable_bbcode(resultSet.getInt("enable_bbcode"));
                bean.setenable_html(resultSet.getInt("enable_html"));
                bean.setenable_smilies(resultSet.getInt("enable_smilies"));
                bean.setenable_sig(resultSet.getInt("enable_sig"));
                bean.setpost_edit_time(resultSet.getInt("post_edit_time"));
                bean.setpost_edit_count(resultSet.getInt("post_edit_count"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_postsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
