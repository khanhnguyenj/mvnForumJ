/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/jdbc/ZoneBannerDAOImplJDBC.java,v 1.5 2008/12/31 04:13:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.5 $
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
import java.util.*;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnsoft.mvnad.db.*;

public class ZoneBannerDAOImplJDBC implements ZoneBannerDAO {
    
    private static final Logger log = LoggerFactory.getLogger(ZoneBannerDAOImplJDBC.class);

    // Prevent instantiation from classes other than derived classes
    public ZoneBannerDAOImplJDBC() {}
    
    public void findByPrimaryKey(int zoneID, int bannerID) 
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT ZoneID, BannerID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ? AND BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            statement.setInt(2, bannerID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + zoneID + ", " + bannerID + ") in table mvnadZoneBanner.");
            }
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.findByPrimaryKey().");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(int zoneID, int bannerID, int relationCellX, 
                       int relationCellY, int relationCellOption, int relationWeight, 
                       int relationOption, int relationStatus, int relationType, 
                       Timestamp relationPublishStartDate, Timestamp relationPublishEndDate, Timestamp relationCreationDate, 
                       Timestamp relationModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        try {
            findByPrimaryKey(zoneID, bannerID);
            throw new DuplicateKeyException("Primary key already exists. Cannot create new ZoneBanner with the same [ZoneID, BannerID] (" + zoneID + ", " + bannerID + ").");
        } catch(ObjectNotFoundException e) {
            // do nothing
        }

        try {
            DAOFactoryAd.getZoneDAO().findByPrimaryKey(zoneID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table mvnadZone does not exist. Cannot create new ZoneBanner.");
        }

        try {
            DAOFactoryAd.getBannerDAO().findByPrimaryKey(bannerID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table mvnadBanner does not exist. Cannot create new ZoneBanner.");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(1024);
        
        sql.append("INSERT INTO " + TABLE_NAME + " (ZoneID, BannerID, RelationCellX, RelationCellY, RelationCellOption, RelationWeight, RelationOption, RelationStatus, RelationType, RelationPublishStartDate, RelationPublishEndDate, RelationCreationDate, RelationModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, zoneID);
            statement.setInt(2, bannerID);
            statement.setInt(3, relationCellX);
            statement.setInt(4, relationCellY);
            statement.setInt(5, relationCellOption);
            statement.setInt(6, relationWeight);
            statement.setInt(7, relationOption);
            statement.setInt(8, relationStatus);
            statement.setInt(9, relationType);
            statement.setTimestamp(10, relationPublishStartDate);
            statement.setTimestamp(11, relationPublishEndDate);
            statement.setTimestamp(12, relationCreationDate);
            statement.setTimestamp(13, relationModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table mvnadZoneBanner.");
            }
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.create().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void update(int zoneID, int bannerID,
                       int relationCellX, int relationCellY, int relationCellOption, 
                       int relationWeight, int relationOption, int relationStatus, 
                       int relationType, Timestamp relationPublishStartDate, Timestamp relationPublishEndDate, 
                       Timestamp relationModifiedDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        try {
            DAOFactoryAd.getBannerDAO().findByPrimaryKey(bannerID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnadBanner' does not exist. Cannot update table mvnadZoneBanner.");
        }

        try {
            DAOFactoryAd.getZoneDAO().findByPrimaryKey(zoneID);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'mvnadZone' does not exist. Cannot update table mvnadZoneBanner.");
        }

        try {
            findByPrimaryKey(zoneID, bannerID);
        } catch(ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Cannot update because row with primary key = (" + zoneID + ", " + bannerID + ") not found!");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(1024);
        
        sql.append("UPDATE " + TABLE_NAME + " SET RelationCellX = ?, RelationCellY = ?, RelationCellOption = ?, RelationWeight = ?, RelationOption = ?, RelationStatus = ?, RelationType = ?, RelationPublishStartDate = ?, RelationPublishEndDate = ?, RelationModifiedDate = ?");
        sql.append(" WHERE ZoneID = ? AND BannerID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, relationCellX);
            statement.setInt(2, relationCellY);
            statement.setInt(3, relationCellOption);
            statement.setInt(4, relationWeight);
            statement.setInt(5, relationOption);
            statement.setInt(6, relationStatus);
            statement.setInt(7, relationType);
            statement.setTimestamp(8, relationPublishStartDate);
            statement.setTimestamp(9, relationPublishEndDate);
            statement.setTimestamp(10, relationModifiedDate);

            statement.setInt(11, zoneID);
            statement.setInt(12, bannerID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table mvnadZoneBanner where primary key = (" + zoneID + ", " + bannerID + ").");
            }
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.update().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public void delete(int zoneID, int bannerID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);

        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ? AND BannerID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, zoneID);
            statement.setInt(2, bannerID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table mvnadZoneBanner where key = (" + zoneID + ", " + bannerID + ").");
            }
        } catch (SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.delete().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public void deleteInBanner(int bannerID) 
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE BannerID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, bannerID);
            statement.executeUpdate();            
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.deleteInBanner.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public void deleteInZone(int zoneID) 
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
    
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            statement.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.deleteInZone.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(1024);
        
        sql.append("SELECT ZoneID, BannerID, RelationCellX, RelationCellY, RelationCellOption, RelationWeight, RelationOption, RelationStatus, RelationType, RelationPublishStartDate, RelationPublishEndDate, RelationCreationDate, RelationModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ZoneBannerBean bean = new ZoneBannerBean();
                
                bean.setZoneID(resultSet.getInt("ZoneID"));
                bean.setBannerID(resultSet.getInt("BannerID"));
                bean.setRelationCellX(resultSet.getInt("RelationCellX"));
                bean.setRelationCellY(resultSet.getInt("RelationCellY"));
                bean.setRelationCellOption(resultSet.getInt("RelationCellOption"));
                bean.setRelationWeight(resultSet.getInt("RelationWeight"));
                bean.setRelationOption(resultSet.getInt("RelationOption"));
                bean.setRelationStatus(resultSet.getInt("RelationStatus"));
                bean.setRelationType(resultSet.getInt("RelationType"));
                bean.setRelationPublishStartDate(resultSet.getTimestamp("RelationPublishStartDate"));
                bean.setRelationPublishEndDate(resultSet.getTimestamp("RelationPublishEndDate"));
                bean.setRelationCreationDate(resultSet.getTimestamp("RelationCreationDate"));
                bean.setRelationModifiedDate(resultSet.getTimestamp("RelationModifiedDate"));
                
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getBeans().");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public Collection getBeans(int zoneID) 
        throws DatabaseException, ObjectNotFoundException { 

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(1024);
        
        sql.append("SELECT ZoneID, BannerID, RelationCellX, RelationCellY, RelationCellOption, RelationWeight, RelationOption, RelationStatus, RelationType, RelationPublishStartDate, RelationPublishEndDate, RelationCreationDate, RelationModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ZoneBannerBean bean = new ZoneBannerBean();
                
                bean.setZoneID(resultSet.getInt("ZoneID"));
                bean.setBannerID(resultSet.getInt("BannerID"));
                bean.setRelationCellX(resultSet.getInt("RelationCellX"));
                bean.setRelationCellY(resultSet.getInt("RelationCellY"));
                bean.setRelationCellOption(resultSet.getInt("RelationCellOption"));
                bean.setRelationWeight(resultSet.getInt("RelationWeight"));
                bean.setRelationOption(resultSet.getInt("RelationOption"));
                bean.setRelationStatus(resultSet.getInt("RelationStatus"));
                bean.setRelationType(resultSet.getInt("RelationType"));
                bean.setRelationPublishStartDate(resultSet.getTimestamp("RelationPublishStartDate"));
                bean.setRelationPublishEndDate(resultSet.getTimestamp("RelationPublishEndDate"));
                bean.setRelationCreationDate(resultSet.getTimestamp("RelationCreationDate"));
                bean.setRelationModifiedDate(resultSet.getTimestamp("RelationModifiedDate"));
                
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getBeans().");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public Collection getValidBannersOfZone(Timestamp now, int zoneID) 
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT banner.BannerID, MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME + " zonebanner, " + BannerDAO.TABLE_NAME + " banner");
        sql.append(" WHERE zonebanner.ZoneID = ?");
        sql.append(" AND zonebanner.BannerID = banner.BannerID");
        sql.append(" AND zonebanner.RelationPublishStartDate <= ?");
        sql.append(" AND zonebanner.RelationPublishEndDate >= ?");
        sql.append(" AND banner.BannerStartDate <= ?");
        sql.append(" AND banner.BannerEndDate >= ?");
        sql.append(" ORDER BY BannerModifiedDate ASC");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, zoneID);
            statement.setTimestamp(2, now);
            statement.setTimestamp(3, now);
            statement.setTimestamp(4, now);
            statement.setTimestamp(5, now);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BannerBean bean = new BannerBean();
                bean.setBannerID(resultSet.getInt("BannerID"));
                bean.setMemberName(resultSet.getString("MemberName"));
                bean.setBannerName(resultSet.getString("BannerName"));
                bean.setBannerDesc(resultSet.getString("BannerDesc"));
                bean.setBannerAltText(resultSet.getString("BannerAltText"));
                bean.setBannerMimeType(resultSet.getString("BannerMimeType"));
                bean.setBannerPreText(resultSet.getString("BannerPreText"));
                bean.setBannerAfterText(resultSet.getString("BannerAfterText"));
                bean.setBannerTargetURL(resultSet.getString("BannerTargetURL"));
                bean.setBannerImageURL(resultSet.getString("BannerImageURL"));
                bean.setBannerWidth(resultSet.getInt("BannerWidth"));
                bean.setBannerHeight(resultSet.getInt("BannerHeight"));
                bean.setBannerWeight(resultSet.getInt("BannerWeight"));
                bean.setBannerMaxImpression(resultSet.getInt("BannerMaxImpression"));
                bean.setBannerReceivedImpression(resultSet.getInt("BannerReceivedImpression"));
                bean.setBannerMaxClick(resultSet.getInt("BannerMaxClick"));
                bean.setBannerReceivedClick(resultSet.getInt("BannerReceivedClick"));
                bean.setBannerZonePositionX(resultSet.getInt("BannerZonePositionX"));
                bean.setBannerZonePositionY(resultSet.getInt("BannerZonePositionY"));
                bean.setBannerStartDate(resultSet.getTimestamp("BannerStartDate"));
                bean.setBannerEndDate(resultSet.getTimestamp("BannerEndDate"));
                bean.setBannerIsHtml(resultSet.getInt("BannerIsHtml"));
                bean.setBannerHtmlCode(resultSet.getString("BannerHtmlCode"));
                bean.setBannerCanTrackClicks(resultSet.getInt("BannerCanTrackClicks"));
                bean.setBannerOption(resultSet.getInt("BannerOption"));
                bean.setBannerStatus(resultSet.getInt("BannerStatus"));
                bean.setBannerType(resultSet.getInt("BannerType"));
                bean.setBannerCreationDate(resultSet.getTimestamp("BannerCreationDate"));
                bean.setBannerModifiedDate(resultSet.getTimestamp("BannerModifiedDate"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getValidBannersOfZone.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public ZoneBannerBean getBean(int zoneID, int bannerID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        StringBuffer sql = new StringBuffer(1024);
        
        sql.append("SELECT RelationCellX, RelationCellY, RelationCellOption, RelationWeight, RelationOption, RelationStatus, RelationType, RelationPublishStartDate, RelationPublishEndDate, RelationCreationDate, RelationModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ? AND BannerID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, zoneID);
            statement.setInt(2, bannerID);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next() == false) {
                throw new ObjectNotFoundException("Cannot find the row in table mvnadZoneBanner where primary key = (" + zoneID + ", " + bannerID + ").");
            }

            ZoneBannerBean bean = new ZoneBannerBean();

            bean.setZoneID(zoneID);
            bean.setBannerID(bannerID);
            bean.setRelationCellX(resultSet.getInt("RelationCellX"));
            bean.setRelationCellY(resultSet.getInt("RelationCellY"));
            bean.setRelationCellOption(resultSet.getInt("RelationCellOption"));
            bean.setRelationWeight(resultSet.getInt("RelationWeight"));
            bean.setRelationOption(resultSet.getInt("RelationOption"));
            bean.setRelationStatus(resultSet.getInt("RelationStatus"));
            bean.setRelationType(resultSet.getInt("RelationType"));
            bean.setRelationPublishStartDate(resultSet.getTimestamp("RelationPublishStartDate"));
            bean.setRelationPublishEndDate(resultSet.getTimestamp("RelationPublishEndDate"));
            bean.setRelationCreationDate(resultSet.getTimestamp("RelationCreationDate"));
            bean.setRelationModifiedDate(resultSet.getTimestamp("RelationModifiedDate"));
            
            // additional fields
            bean.setBannerBean(DAOFactoryAd.getBannerDAO().getBean(bannerID));
            
            return bean;
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getBean(primary_key).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans_inZone(int zoneID)
        throws DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE ZoneID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, zoneID);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next() == false) {
                throw new AssertionError("Assertion in ZoneDAOImplJDBC.getNumberOfBeans_inZone().");
            }
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getNumberOfBeans_inZone().");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public int getNumberOfBeans_inBanner(int bannerID)
        throws DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("SELECT Count(*)");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE BannerID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, bannerID);
            
            resultSet = statement.executeQuery();
            
            if (resultSet.next() == false) {
                throw new AssertionError("Assertion in ZoneDAOImplJDBC.getNumberOfZones_inBanner().");
            }
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ZoneBannerDAOImplJDBC.getNumberOfZones_inBanner().");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
