/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinPostDAOImplJDBC.java,v 1.3 2009/10/07 04:23:23 lexuanttkhtn Exp $
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
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.vbulletin.db.VBulletinPostBean;
import com.mvnforum.vbulletin.db.VBulletinPostDAO;

/**
 * The Class VBulletinPostDAOImplJDBC.
 */
public class VBulletinPostDAOImplJDBC implements VBulletinPostDAO {

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinPostDAO#getBean(int)
     */
    public VBulletinPostBean getBean(int postID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ThreadID, ParentID, Username, UserID, Title, DateLine, PageText, AllowSmilie, ShowSignature, IpAddress, IconID, Visible, Attach, Infraction, ReportThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE PostID = ?");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Post where primary key = (" + postID + ").");
            }

            VBulletinPostBean bean = new VBulletinPostBean();
            // TODO: uncomment the following line(s) as needed
            //bean.setPostID(postID);
            bean.setPostID(resultSet.getInt("PostID"));
            bean.setThreadID(resultSet.getInt("ThreadID"));
            bean.setParentID(resultSet.getInt("ParentID"));
            bean.setUsername(resultSet.getString("Username"));
            bean.setUserID(resultSet.getInt("UserID"));
            bean.setTitle(resultSet.getString("Title"));
            bean.setDateLine(resultSet.getLong("DateLine"));
            bean.setPageText(resultSet.getString("PageText"));
            bean.setAllowSmilie(resultSet.getInt("AllowSmilie"));
            bean.setShowSignature(resultSet.getInt("ShowSignature"));
            bean.setIpAddress(resultSet.getString("IpAddress"));
            bean.setIconID(resultSet.getInt("IconID"));
            bean.setVisible(resultSet.getInt("Visible"));
            bean.setAttach(resultSet.getInt("Attach"));
            bean.setInfraction(resultSet.getInt("Infraction"));
            bean.setReportThreadID(resultSet.getInt("ReportThreadID"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }
    
    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinPostDAO#getFirstPostInThread(int)
     */
    public VBulletinPostBean getFirstPostInThread(int threadID)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ThreadID, ParentID, Username, UserID, Title, DateLine, PageText, AllowSmilie, ShowSignature, IpAddress, IconID, Visible, Attach, Infraction, ReportThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ThreadID = ? AND ParentID = 0");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, threadID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            
            VBulletinPostBean bean = new VBulletinPostBean();
            // TODO: uncomment the following line(s) as needed
            //bean.setPostID(postID);
            bean.setPostID(resultSet.getInt("PostID"));
            bean.setThreadID(resultSet.getInt("ThreadID"));
            bean.setParentID(resultSet.getInt("ParentID"));
            bean.setUsername(resultSet.getString("Username"));
            bean.setUserID(resultSet.getInt("UserID"));
            bean.setTitle(resultSet.getString("Title"));
            bean.setDateLine(resultSet.getLong("DateLine"));
            bean.setPageText(resultSet.getString("PageText"));
            bean.setAllowSmilie(resultSet.getInt("AllowSmilie"));
            bean.setShowSignature(resultSet.getInt("ShowSignature"));
            bean.setIpAddress(resultSet.getString("IpAddress"));
            bean.setIconID(resultSet.getInt("IconID"));
            bean.setVisible(resultSet.getInt("Visible"));
            bean.setAttach(resultSet.getInt("Attach"));
            bean.setInfraction(resultSet.getInt("Infraction"));
            bean.setReportThreadID(resultSet.getInt("ReportThreadID"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinPostDAO#getChildPost(int)
     */
    public Collection getChildPost(int postID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT PostID, ThreadID, ParentID, Username, UserID, Title, DateLine, PageText, AllowSmilie, ShowSignature, IpAddress, IconID, Visible, Attach, Infraction, ReportThreadID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ParentID = ?");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, postID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                VBulletinPostBean bean = new VBulletinPostBean();
                bean.setPostID(resultSet.getInt("PostID"));
                bean.setThreadID(resultSet.getInt("ThreadID"));
                bean.setParentID(resultSet.getInt("ParentID"));
                bean.setUsername(resultSet.getString("Username"));
                bean.setUserID(resultSet.getInt("UserID"));
                bean.setTitle(resultSet.getString("Title"));
                bean.setDateLine(resultSet.getInt("DateLine"));
                bean.setPageText(resultSet.getString("PageText"));
                bean.setAllowSmilie(resultSet.getInt("AllowSmilie"));
                bean.setShowSignature(resultSet.getInt("ShowSignature"));
                bean.setIpAddress(resultSet.getString("IpAddress"));
                bean.setIconID(resultSet.getInt("IconID"));
                bean.setVisible(resultSet.getInt("Visible"));
                bean.setAttach(resultSet.getInt("Attach"));
                bean.setInfraction(resultSet.getInt("Infraction"));
                bean.setReportThreadID(resultSet.getInt("ReportThreadID"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in PostDAOImplJDBC.getChildPost.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
