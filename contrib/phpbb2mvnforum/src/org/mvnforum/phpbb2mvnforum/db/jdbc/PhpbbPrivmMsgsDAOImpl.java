/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbPrivmMsgsDAOImpl.java,v 1.5 2007/01/15 10:27:11 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:27:11 $
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

import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgs;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsDAO;
import org.mvnforum.util.DBUtils;

public class PhpbbPrivmMsgsDAOImpl implements PhpbbPrivmMsgsDAO {

    /*
     * Included columns: privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, 
     *                   privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, 
     *                   privmsgs_attach_sig
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
                .append("SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPrivmMsgs bean = new PhpbbPrivmMsgs();
                bean.setprivmsgs_id(resultSet.getInt("privmsgs_id"));
                bean.setprivmsgs_type(resultSet.getInt("privmsgs_type"));
                bean.setprivmsgs_subject(resultSet.getString("privmsgs_subject"));
                bean.setprivmsgs_from_userid(resultSet.getInt("privmsgs_from_userid"));
                bean.setprivmsgs_to_userid(resultSet.getInt("privmsgs_to_userid"));
                bean.setprivmsgs_date(resultSet.getInt("privmsgs_date"));
                bean.setprivmsgs_ip(resultSet.getString("privmsgs_ip"));
                bean.setprivmsgs_enable_bbcode(resultSet.getInt("privmsgs_enable_bbcode"));
                bean.setprivmsgs_enable_html(resultSet.getInt("privmsgs_enable_html"));
                bean.setprivmsgs_enable_smilies(resultSet.getInt("privmsgs_enable_smilies"));
                bean.setprivmsgs_attach_sig(resultSet.getInt("privmsgs_attach_sig"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansBySenderID(int senderID)
        throws DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE privmsgs_from_userid=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, senderID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPrivmMsgs bean = new PhpbbPrivmMsgs();
                bean.setprivmsgs_id(resultSet.getInt("privmsgs_id"));
                bean.setprivmsgs_type(resultSet.getInt("privmsgs_type"));
                bean.setprivmsgs_subject(resultSet.getString("privmsgs_subject"));
                bean.setprivmsgs_from_userid(resultSet.getInt("privmsgs_from_userid"));
                bean.setprivmsgs_to_userid(resultSet.getInt("privmsgs_to_userid"));
                bean.setprivmsgs_date(resultSet.getInt("privmsgs_date"));
                bean.setprivmsgs_ip(resultSet.getString("privmsgs_ip"));
                bean.setprivmsgs_enable_bbcode(resultSet.getInt("privmsgs_enable_bbcode"));
                bean.setprivmsgs_enable_html(resultSet.getInt("privmsgs_enable_html"));
                bean.setprivmsgs_enable_smilies(resultSet.getInt("privmsgs_enable_smilies"));
                bean.setprivmsgs_attach_sig(resultSet.getInt("privmsgs_attach_sig"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansByReceiverID(int receiverID)
        throws DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE privmsgs_to_userid=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, receiverID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPrivmMsgs bean = new PhpbbPrivmMsgs();
                bean.setprivmsgs_id(resultSet.getInt("privmsgs_id"));
                bean.setprivmsgs_type(resultSet.getInt("privmsgs_type"));
                bean.setprivmsgs_subject(resultSet.getString("privmsgs_subject"));
                bean.setprivmsgs_from_userid(resultSet.getInt("privmsgs_from_userid"));
                bean.setprivmsgs_to_userid(resultSet.getInt("privmsgs_to_userid"));
                bean.setprivmsgs_date(resultSet.getInt("privmsgs_date"));
                bean.setprivmsgs_ip(resultSet.getString("privmsgs_ip"));
                bean.setprivmsgs_enable_bbcode(resultSet.getInt("privmsgs_enable_bbcode"));
                bean.setprivmsgs_enable_html(resultSet.getInt("privmsgs_enable_html"));
                bean.setprivmsgs_enable_smilies(resultSet.getInt("privmsgs_enable_smilies"));
                bean.setprivmsgs_attach_sig(resultSet.getInt("privmsgs_attach_sig"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansByType(int privmsgsType, int memberID)
        throws DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE  privmsgs_type=? and privmsgs_to_userid=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, privmsgsType);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPrivmMsgs bean = new PhpbbPrivmMsgs();
                bean.setprivmsgs_id(resultSet.getInt("privmsgs_id"));
                bean.setprivmsgs_type(resultSet.getInt("privmsgs_type"));
                bean.setprivmsgs_subject(resultSet.getString("privmsgs_subject"));
                bean.setprivmsgs_from_userid(resultSet.getInt("privmsgs_from_userid"));
                bean.setprivmsgs_to_userid(resultSet.getInt("privmsgs_to_userid"));
                bean.setprivmsgs_date(resultSet.getInt("privmsgs_date"));
                bean.setprivmsgs_ip(resultSet.getString("privmsgs_ip"));
                bean.setprivmsgs_enable_bbcode(resultSet.getInt("privmsgs_enable_bbcode"));
                bean.setprivmsgs_enable_html(resultSet.getInt("privmsgs_enable_html"));
                bean.setprivmsgs_enable_smilies(resultSet.getInt("privmsgs_enable_smilies"));
                bean.setprivmsgs_attach_sig(resultSet.getInt("privmsgs_attach_sig"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeansByTypeAndReceiveUser(int privmsgsType, int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql
                .append("SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE  privmsgs_type=? and privmsgs_from_userid=?"); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, privmsgsType);
            statement.setInt(2, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbPrivmMsgs bean = new PhpbbPrivmMsgs();
                bean.setprivmsgs_id(resultSet.getInt("privmsgs_id"));
                bean.setprivmsgs_type(resultSet.getInt("privmsgs_type"));
                bean.setprivmsgs_subject(resultSet.getString("privmsgs_subject"));
                bean.setprivmsgs_from_userid(resultSet.getInt("privmsgs_from_userid"));
                bean.setprivmsgs_to_userid(resultSet.getInt("privmsgs_to_userid"));
                bean.setprivmsgs_date(resultSet.getInt("privmsgs_date"));
                bean.setprivmsgs_ip(resultSet.getString("privmsgs_ip"));
                bean.setprivmsgs_enable_bbcode(resultSet.getInt("privmsgs_enable_bbcode"));
                bean.setprivmsgs_enable_html(resultSet.getInt("privmsgs_enable_html"));
                bean.setprivmsgs_enable_smilies(resultSet.getInt("privmsgs_enable_smilies"));
                bean.setprivmsgs_attach_sig(resultSet.getInt("privmsgs_attach_sig"));
                retValue.add(bean);
            }
            return retValue;
        } catch (SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_privmsgsDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
