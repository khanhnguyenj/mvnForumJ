/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/jdbc/ZoneDAOImplJDBC.java,v 1.7 2008/12/31 04:13:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.7 $
 * $Date: 2008/12/31 04:13:29 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
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
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.db.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnsoft.mvnad.db.ZoneBean;
import com.mvnsoft.mvnad.db.ZoneDAO;

public class ZoneDAOImplJDBC implements ZoneDAO {
    
    private static final Logger log = LoggerFactory.getLogger(ZoneDAOImplJDBC.class);

    // Prevent instantiation from classes other than derived classes
    public ZoneDAOImplJDBC() {}

    public void findByPrimaryKey(int zoneID) 
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + zoneID + ") in table 'mvnadZone'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_MemberName_ZoneName(String memberName, String zoneName)
        throws ObjectNotFoundException, DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneName, MemberName");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE (lower(ZoneName) = lower(?)) AND (lower(MemberName) = lower(?))");
        } else {
            sql.append(" WHERE (ZoneName = ?) AND (MemberName = ?)");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, zoneName);
            statement.setString(2, memberName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [ZoneName, MemberName] (" + zoneName + ", " + memberName + ") in table 'mvnadZone'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.findByAlternateKey_MemberName_ZoneName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, 
     *                   ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, 
     *                   ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, 
     *                   ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate
     * Excluded columns: ZoneID
     */
    public void create(String memberName, String zoneName, String zoneDesc, 
                        String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
                        int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
                        int zoneDirection, int zoneMaxImpression, int zoneReceivedImpression, 
                        int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
                        int zoneOption, int zoneStatus, int zoneType, 
                        Timestamp zoneCreationDate, Timestamp zoneModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        // @todo: Comment this try-catch block if the needed columns do not have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberName_ZoneName(memberName, zoneName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new mvnadZone with the same [ZoneName, MemberName] (" + zoneName + ", " + memberName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns dont have attribute 'include'
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Banner.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setString(1, memberName);
            statement.setString(2, zoneName);
            statement.setString(3, zoneDesc);
            statement.setString(4, zoneTargetWindow);
            statement.setInt(5, zoneCellWidth);
            statement.setInt(6, zoneCellHeight);
            statement.setInt(7, zoneCellHorizontalCount);
            statement.setInt(8, zoneCellVerticalCount);
            statement.setInt(9, zoneMaxBanners);
            statement.setInt(10, zoneDirection);
            statement.setInt(11, zoneMaxImpression);
            statement.setInt(12, zoneReceivedImpression);
            statement.setInt(13, zoneMaxClick);
            statement.setInt(14, zoneReceivedClick);
            statement.setInt(15, zoneAutoReloadTime);
            statement.setInt(16, zoneOption);
            statement.setInt(17, zoneStatus);
            statement.setInt(18, zoneType);
            statement.setTimestamp(19, zoneCreationDate);
            statement.setTimestamp(20, zoneModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'mvnadzone'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int createZone(String memberName, String zoneName, String zoneDesc, 
            String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
            int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
            int zoneDirection, int zoneMaxImpression, int zoneReceivedImpression, 
            int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
            int zoneOption, int zoneStatus, int zoneType, 
            Timestamp zoneCreationDate, Timestamp zoneModifiedDate)
    throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        create(memberName, zoneName, zoneDesc, zoneTargetWindow, zoneCellWidth, zoneCellHeight, zoneCellHorizontalCount, zoneCellVerticalCount, zoneMaxBanners, zoneDirection, zoneMaxImpression, zoneReceivedImpression, zoneMaxClick, zoneReceivedClick, zoneAutoReloadTime, zoneOption, zoneStatus, zoneType, zoneCreationDate, zoneModifiedDate);
        ZoneBean zoneBean = null;
        try {
            zoneBean = getBean_byAlternateKey_MemberName_ZoneName(memberName, zoneName);
        } catch (ObjectNotFoundException ex) {
            log.error("Cannot find ZoneID after creating zone.", ex);
        }
        return zoneBean.getZoneID();
    }

    /*
     * Included columns: ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, 
     *                   ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, 
     *                   ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, 
     *                   ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneModifiedDate
     * Excluded columns: ZoneCreationDate
     */
    public void update(int zoneID, // primary key
                        String memberName, String zoneName, String zoneDesc, 
                        String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
                        int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
                        int zoneDirection, int zoneMaxImpression, int zoneReceivedImpression, 
                        int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
                        int zoneOption, int zoneStatus, int zoneType, 
                        Timestamp zoneModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {

        ZoneBean bean = getBean(zoneID); // @todo: comment or delete this line if no alternate key are included

        if ( memberName.equalsIgnoreCase(bean.getMemberName()) == false ||
             zoneName.equalsIgnoreCase(bean.getZoneName()) == false ) {
            // mvnadZone tries to change its alternate key [MemberName, ZoneName], so we must check if it already exist
            try {
                findByAlternateKey_MemberName_ZoneName(memberName, zoneName);
                throw new DuplicateKeyException("Alternate key [MemberName, ZoneName] (" + memberName + ", " + zoneName + ") already exists. Cannot update mvnadZone.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET MemberName = ?, ZoneName = ?, ZoneDesc = ?, ZoneTargetWindow = ?, ZoneCellWidth = ?, ZoneCellHeight = ?, ZoneCellHorizontalCount = ?, ZoneCellVerticalCount = ?, ZoneMaxBanners = ?, ZoneDirection = ?, ZoneMaxImpression = ?, ZoneReceivedImpression = ?, ZoneMaxClick = ?, ZoneReceivedClick = ?, ZoneAutoReloadTime = ?, ZoneOption = ?, ZoneStatus = ?, ZoneType = ?, ZoneModifiedDate = ?");
        sql.append(" WHERE ZoneID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, memberName);
            statement.setString(2, zoneName);
            statement.setString(3, zoneDesc);
            statement.setString(4, zoneTargetWindow);
            statement.setInt(5, zoneCellWidth);
            statement.setInt(6, zoneCellHeight);
            statement.setInt(7, zoneCellHorizontalCount);
            statement.setInt(8, zoneCellVerticalCount);
            statement.setInt(9, zoneMaxBanners);
            statement.setInt(10, zoneDirection);
            statement.setInt(11, zoneMaxImpression);
            statement.setInt(12, zoneReceivedImpression);
            statement.setInt(13, zoneMaxClick);
            statement.setInt(14, zoneReceivedClick);
            statement.setInt(15, zoneAutoReloadTime);
            statement.setInt(16, zoneOption);
            statement.setInt(17, zoneStatus);
            statement.setInt(18, zoneType);
            statement.setTimestamp(19, zoneModifiedDate);

            // primary key column(s)
            statement.setInt(20, zoneID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table mvnadzone where primary key = (" + zoneID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateReceivedImpression(int zoneID, int zoneReceivedImpression)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ZoneReceivedImpression = ?");
        sql.append(" WHERE ZoneID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            // // column(s) to update
            statement.setInt(1, zoneReceivedImpression);
            
            // primary key column(s)
            statement.setInt(2, zoneID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Zone where primary key = (" + zoneID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.updateReceivedImpression.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateReceivedClick(int zoneID, int zoneReceivedClick)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET ZoneReceivedClick = ?");
        sql.append(" WHERE ZoneID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            // // column(s) to update
            statement.setInt(1, zoneReceivedClick);
            
            // primary key column(s)
            statement.setInt(2, zoneID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Zone where primary key = (" + zoneID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.updateReceivedClick.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void increaseReceivedClick(int zoneID, int receivedClickCount) 
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("UPDATE " + TABLE_NAME + " SET ZoneReceivedClick = ZoneReceivedClick + ").append(receivedClickCount);
        sql.append(" WHERE ZoneID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, zoneID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot increase ZoneReceivedClick where BannerID = " + zoneID + ".");
            }
        } catch (SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.increaseReceivedClick().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void increaseReceivedImpression(int zoneID, int receivedImpressionCount) 
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("UPDATE " + TABLE_NAME + " SET ZoneReceivedImpression = ZoneReceivedImpression + ").append(receivedImpressionCount);
        sql.append(" WHERE ZoneID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, zoneID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot increase ZoneReceivedImpression where ZoneID = " + zoneID + ".");
            }
        } catch (SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.increaseReceivedImpression().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int zoneID)
        throws DatabaseException, ObjectNotFoundException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
    
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table mvnadZone where primary key = (" + zoneID + ").");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, 
     *                   ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, 
     *                   ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, 
     *                   ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, 
     *                   ZoneModifiedDate
     * Excluded columns: ZoneID
     */
    public ZoneBean getBean(int zoneID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table mvnadZone where primary key = (" + zoneID + ").");
            }

            ZoneBean bean = new ZoneBean();
            bean.setZoneID(zoneID);
            bean.setMemberName(resultSet.getString("MemberName"));
            bean.setZoneName(resultSet.getString("ZoneName"));
            bean.setZoneDesc(resultSet.getString("ZoneDesc"));
            bean.setZoneTargetWindow(resultSet.getString("ZoneTargetWindow"));
            bean.setZoneCellWidth(resultSet.getInt("ZoneCellWidth"));
            bean.setZoneCellHeight(resultSet.getInt("ZoneCellHeight"));
            bean.setZoneCellHorizontalCount(resultSet.getInt("ZoneCellHorizontalCount"));
            bean.setZoneCellVerticalCount(resultSet.getInt("ZoneCellVerticalCount"));
            bean.setZoneMaxBanners(resultSet.getInt("ZoneMaxBanners"));
            bean.setZoneDirection(resultSet.getInt("ZoneDirection"));
            bean.setZoneMaxImpression(resultSet.getInt("ZoneMaxImpression"));
            bean.setZoneReceivedImpression(resultSet.getInt("ZoneReceivedImpression"));
            bean.setZoneMaxClick(resultSet.getInt("ZoneMaxClick"));
            bean.setZoneReceivedClick(resultSet.getInt("ZoneReceivedClick"));
            bean.setZoneAutoReloadTime(resultSet.getInt("ZoneAutoReloadTime"));
            bean.setZoneOption(resultSet.getInt("ZoneOption"));
            bean.setZoneStatus(resultSet.getInt("ZoneStatus"));
            bean.setZoneType(resultSet.getInt("ZoneType"));
            bean.setZoneCreationDate(resultSet.getTimestamp("ZoneCreationDate"));
            bean.setZoneModifiedDate(resultSet.getTimestamp("ZoneModifiedDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, 
     *                   ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, 
     *                   ZoneDirection, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, 
     *                   ZoneCreationDate, ZoneModifiedDate
     * Excluded columns: 
     */
    public ZoneBean getBean_byAlternateKey_MemberName_ZoneName(String memberName, String zoneName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneName = ? AND MemberName = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, zoneName);
            statement.setString(2, memberName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table mvnadzone where alternate key [ZoneName, MemberName] = (" + zoneName + ", " + memberName + ").");
            }

            ZoneBean bean = new ZoneBean();
            bean.setZoneName(zoneName);
            bean.setMemberName(memberName);
            bean.setZoneID(resultSet.getInt("ZoneID"));
            bean.setMemberName(resultSet.getString("MemberName"));
            bean.setZoneName(resultSet.getString("ZoneName"));
            bean.setZoneDesc(resultSet.getString("ZoneDesc"));
            bean.setZoneTargetWindow(resultSet.getString("ZoneTargetWindow"));
            bean.setZoneCellWidth(resultSet.getInt("ZoneCellWidth"));
            bean.setZoneCellHeight(resultSet.getInt("ZoneCellHeight"));
            bean.setZoneCellHorizontalCount(resultSet.getInt("ZoneCellHorizontalCount"));
            bean.setZoneCellVerticalCount(resultSet.getInt("ZoneCellVerticalCount"));
            bean.setZoneMaxBanners(resultSet.getInt("ZoneMaxBanners"));
            bean.setZoneDirection(resultSet.getInt("ZoneDirection"));
            bean.setZoneMaxImpression(resultSet.getInt("ZoneMaxImpression"));
            bean.setZoneReceivedImpression(resultSet.getInt("ZoneReceivedImpression"));
            bean.setZoneMaxClick(resultSet.getInt("ZoneMaxClick"));
            bean.setZoneReceivedClick(resultSet.getInt("ZoneReceivedClick"));
            bean.setZoneAutoReloadTime(resultSet.getInt("ZoneAutoReloadTime"));
            bean.setZoneOption(resultSet.getInt("ZoneOption"));
            bean.setZoneStatus(resultSet.getInt("ZoneStatus"));
            bean.setZoneType(resultSet.getInt("ZoneType"));
            bean.setZoneCreationDate(resultSet.getTimestamp("ZoneCreationDate"));
            bean.setZoneModifiedDate(resultSet.getTimestamp("ZoneModifiedDate"));
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.getBean_byAlternateKey_ZoneName_MemberName(ak).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, 
     *                   ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, 
     *                   ZoneDirection, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, 
     *                   ZoneCreationDate, ZoneModifiedDate
     * Excluded columns: 
     */
    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ZoneBean bean = new ZoneBean();
                bean.setZoneID(resultSet.getInt("ZoneID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setZoneName(resultSet.getString("ZoneName"));
                bean.setZoneDesc(resultSet.getString("ZoneDesc"));
                bean.setZoneTargetWindow(resultSet.getString("ZoneTargetWindow"));
                bean.setZoneCellWidth(resultSet.getInt("ZoneCellWidth"));
                bean.setZoneCellHeight(resultSet.getInt("ZoneCellHeight"));
                bean.setZoneCellHorizontalCount(resultSet.getInt("ZoneCellHorizontalCount"));
                bean.setZoneCellVerticalCount(resultSet.getInt("ZoneCellVerticalCount"));
                bean.setZoneMaxBanners(resultSet.getInt("ZoneMaxBanners"));
                bean.setZoneDirection(resultSet.getInt("ZoneDirection"));
                bean.setZoneMaxImpression(resultSet.getInt("ZoneMaxImpression"));
                bean.setZoneReceivedImpression(resultSet.getInt("ZoneReceivedImpression"));
                bean.setZoneMaxClick(resultSet.getInt("ZoneMaxClick"));
                bean.setZoneReceivedClick(resultSet.getInt("ZoneReceivedClick"));
                bean.setZoneAutoReloadTime(resultSet.getInt("ZoneAutoReloadTime"));
                bean.setZoneOption(resultSet.getInt("ZoneOption"));
                bean.setZoneStatus(resultSet.getInt("ZoneStatus"));
                bean.setZoneType(resultSet.getInt("ZoneType"));
                bean.setZoneCreationDate(resultSet.getTimestamp("ZoneCreationDate"));
                bean.setZoneModifiedDate(resultSet.getTimestamp("ZoneModifiedDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, 
     *                   ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, 
     *                   ZoneDirection, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, 
     *                   ZoneCreationDate, ZoneModifiedDate
     * Excluded columns: 
     */
    public Collection getBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) throw new IllegalArgumentException("The offset < 0 is not allowed.");
        if (rowsToReturn <= 0) throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");

        if ((!sort.equals("ZoneID")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("ZoneName")) &&
            (!sort.equals("ZoneAutoReloadTime")) &&
            (!sort.equals("ZoneCreationDate"))) {
                throw new IllegalArgumentException("Cannot sort, reason: dont understand the criteria '" + sort + "'.");
            }

        if ((!order.equals("ASC")) &&
            (!order.equals("DESC"))) {
                throw new IllegalArgumentException("Cannot sort, reason: dont understand the order '" + order + "'.");
            }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneID, MemberName, ZoneName, ZoneDesc, ZoneTargetWindow, ZoneCellWidth, ZoneCellHeight, ZoneCellHorizontalCount, ZoneCellVerticalCount, ZoneMaxBanners, ZoneDirection, ZoneMaxImpression, ZoneReceivedImpression, ZoneMaxClick, ZoneReceivedClick, ZoneAutoReloadTime, ZoneOption, ZoneStatus, ZoneType, ZoneCreationDate, ZoneModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY " + sort + " " + order);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setMaxRows(offset + rowsToReturn);
            try {
                statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
            } catch (SQLException sqle) {
                //do nothing, postgreSQL does not support this method
            }
            resultSet = statement.executeQuery();
            boolean loop = resultSet.absolute(offset + 1);// the absolute method begin with 1 instead of 0 as in the LIMIT clause
            while (loop) {
                ZoneBean bean = new ZoneBean();
                bean.setZoneID(resultSet.getInt("ZoneID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setZoneName(resultSet.getString("ZoneName"));
                bean.setZoneDesc(resultSet.getString("ZoneDesc"));
                bean.setZoneTargetWindow(resultSet.getString("ZoneTargetWindow"));
                bean.setZoneCellWidth(resultSet.getInt("ZoneCellWidth"));
                bean.setZoneCellHeight(resultSet.getInt("ZoneCellHeight"));
                bean.setZoneCellHorizontalCount(resultSet.getInt("ZoneCellHorizontalCount"));
                bean.setZoneCellVerticalCount(resultSet.getInt("ZoneCellVerticalCount"));
                bean.setZoneMaxBanners(resultSet.getInt("ZoneMaxBanners"));
                bean.setZoneDirection(resultSet.getInt("ZoneDirection"));
                bean.setZoneMaxImpression(resultSet.getInt("ZoneMaxImpression"));
                bean.setZoneReceivedImpression(resultSet.getInt("ZoneReceivedImpression"));
                bean.setZoneMaxClick(resultSet.getInt("ZoneMaxClick"));
                bean.setZoneReceivedClick(resultSet.getInt("ZoneReceivedClick"));
                bean.setZoneAutoReloadTime(resultSet.getInt("ZoneAutoReloadTime"));
                bean.setZoneOption(resultSet.getInt("ZoneOption"));
                bean.setZoneStatus(resultSet.getInt("ZoneStatus"));
                bean.setZoneType(resultSet.getInt("ZoneType"));
                bean.setZoneCreationDate(resultSet.getTimestamp("ZoneCreationDate"));
                bean.setZoneModifiedDate(resultSet.getTimestamp("ZoneModifiedDate"));
                retValue.add(bean);
                if (retValue.size() == rowsToReturn) break;// Fix the Sybase bug
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.getBeans_withSortSupport_limit.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans() throws DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            AssertionUtil.doAssert(resultSet.next(), "Assertion in ZoneDAOImplJDBC.getNumberOfBeans.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneDAOImplJDBC.getNumberOfBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
