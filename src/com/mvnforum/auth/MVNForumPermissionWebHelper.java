/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/MVNForumPermissionWebHelper.java,v 1.19 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.19 $
 * $Date: 2009/01/02 15:12:54 $
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
package com.mvnforum.auth;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.db.*;

/** TODO support table prefix */
public final class MVNForumPermissionWebHelper {

    private static final Logger log = LoggerFactory.getLogger(MVNForumPermissionWebHelper.class);

    private static final String MemberGroup     = MemberGroupDAO.TABLE_NAME;
    private static final String GroupPermission = GroupPermissionDAO.TABLE_NAME;
    private static final String GroupForum      = GroupForumDAO.TABLE_NAME;
    private static final String MemberPermission= MemberPermissionDAO.TABLE_NAME;
    private static final String MemberForum     = MemberForumDAO.TABLE_NAME;

    private MVNForumPermissionWebHelper() {
    }

    static Collection getMemberPermissions(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT Permission");
        sql.append(" FROM ").append(MemberPermission);
        sql.append(" WHERE MemberID = ?");

        //for testing
        //log.debug("getMemberPermissions sql = " + sql.toString());

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer perm = new Integer(resultSet.getInt("Permission"));
                retValue.add(perm);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getMemberPermissions.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    static Collection getGroupPermissions(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT Permission");
        sql.append(" FROM ").append(GroupPermission).append(" groupperm, ").append(MemberGroup).append(" memgroup");
        sql.append(" WHERE (groupperm.GroupID = memgroup.GroupID) AND (memgroup.MemberID = ?)");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer perm = new Integer(resultSet.getInt("Permission"));
                retValue.add(perm);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getGroupPermissions.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    static Collection getGroupMemberPermissions(int memberID)
        throws DatabaseException {
        
        Collection retValue = new ArrayList();
        if ( (memberID == 0) || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST) ) {
            return retValue;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT Permission");
        sql.append(" FROM ").append(GroupPermission);
        sql.append(" WHERE GroupID = ").append(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS);

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer perm = new Integer(resultSet.getInt("Permission"));
                retValue.add(perm);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getGroupMemberPermissions.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    static Collection getMemberPermissionsInForums(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT ForumID, Permission");
        sql.append(" FROM ").append(MemberForum);
        sql.append(" WHERE MemberID = ?");

        //for testing
        //log.debug("getMemberPermissionsInForums sql = " + sql.toString());

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumPermission forumPermission = new ForumPermission();
                forumPermission.setForumID(resultSet.getInt("ForumID"));
                forumPermission.setPermission(resultSet.getInt("Permission"));
                retValue.add(forumPermission);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getMemberPermissionsInForums.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    static Collection getGroupPermissionsInForums(int memberID)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT ForumID, Permission");// belong to table GroupForum
        sql.append(" FROM ").append(GroupForum).append(" grpforum, ").append(MemberGroup).append(" memgroup");
        sql.append(" WHERE (grpforum.GroupID = memgroup.GroupID) AND (memgroup.MemberID = ?)");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumPermission forumPermission = new ForumPermission();
                forumPermission.setForumID(resultSet.getInt("ForumID"));
                forumPermission.setPermission(resultSet.getInt("Permission"));
                retValue.add(forumPermission);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getGroupPermissionsInForums.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    static Collection getGroupMemberPermissionsInForums(int memberID)
        throws DatabaseException {
        
        Collection retValue = new ArrayList();
        if ( (memberID == 0) || (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST) ) {
            return retValue;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT ForumID, Permission");// belong to table GroupForum
        sql.append(" FROM ").append(GroupForum);
        sql.append(" WHERE GroupID = ").append(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS);
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumPermission forumPermission = new ForumPermission();
                forumPermission.setForumID(resultSet.getInt("ForumID"));
                forumPermission.setPermission(resultSet.getInt("Permission"));
                retValue.add(forumPermission);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getGroupMemberPermissionsInForums.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public static Collection getPermissionsForGroupGuest()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Permission");
        sql.append(" FROM ").append(GroupPermission);
        sql.append(" WHERE GroupID = ").append(MVNForumConstant.GROUP_ID_OF_GUEST);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer perm = new Integer(resultSet.getInt("Permission"));
                retValue.add(perm);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getPermissionsForGroupGuest.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public static Collection getPermissionsForGroupGuestInForums()
        throws DatabaseException {

        Collection retValue = new ArrayList();//getMemberPermissionsInForums(MVNForumConstant.MEMBER_ID_OF_GUEST);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ForumID, Permission");
        sql.append(" FROM ").append(GroupForum);
        sql.append(" WHERE GroupID = ").append(MVNForumConstant.GROUP_ID_OF_GUEST);

        //for testing
        //log.debug("getPermissionsForGroupGuestInForums sql = " + sql.toString());

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ForumPermission forumPermission = new ForumPermission();
                forumPermission.setForumID(resultSet.getInt("ForumID"));
                forumPermission.setPermission(resultSet.getInt("Permission"));
                retValue.add(forumPermission);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MVNForumPermissionWebHelper.getPermissionsForGroupGuestInForum.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

}
