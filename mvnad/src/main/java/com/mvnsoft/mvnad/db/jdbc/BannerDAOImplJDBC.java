/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/jdbc/BannerDAOImplJDBC.java,v 1.10 2009/12/03 07:45:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/12/03 07:45:50 $
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
import net.myvietnam.mvncore.util.AssertionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnsoft.mvnad.db.BannerBean;
import com.mvnsoft.mvnad.db.BannerDAO;

public class BannerDAOImplJDBC implements BannerDAO {

    private static final Logger log = LoggerFactory.getLogger(BannerDAOImplJDBC.class);

    // Prevent instantiation from classes other than derived classes
    public BannerDAOImplJDBC() {}

    public void findByPrimaryKey(int bannerID) 
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT BannerID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, bannerID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + bannerID + ") in table 'Banner'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_MemberName_BannerName(String memberName, String bannerName)
        throws ObjectNotFoundException, DatabaseException {
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberName, BannerName");
        sql.append(" FROM " + TABLE_NAME);
        if (DBUtils.isCaseSensitiveDatebase()) {
            sql.append(" WHERE (lower(MemberName) = lower(?)) AND (lower(BannerName) = lower(?))");
        } else {
            sql.append(" WHERE (MemberName = ?) AND (BannerName = ?)");
        }
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, memberName);
            statement.setString(2, bannerName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [MemberName, BannerName] (" + memberName + ", " + bannerName + ") in table 'mvnadBanner'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.findByAlternateKey_MemberName_BannerName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, 
     *                   BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, 
     *                   BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, 
     *                   BannerReceivedClick, BannerZonePosisionX, BannerZonePosisionY, BannerStartDate, BannerEndDate, 
     *                   BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, 
     *                   BannerType, BannerCreationDate, BannerModifiedDate
     * Excluded columns: BannerID
     */
    public void create(String memberName, String bannerName, String bannerDesc, 
                        String bannerAltText, String bannerMimeType, String bannerPreText, 
                        String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
                        int bannerWidth, int bannerHeight, int bannerWeight, 
                        int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
                        int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
                        Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
                        String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
                        int bannerStatus, int bannerType, Timestamp bannerCreationDate, 
                        Timestamp bannerModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        try {
            // @todo: modify the parameter list as needed
            // You may have to regenerate this method if the needed columns don't have attribute 'include'
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
        } catch(ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Foreign key refers to table 'Member' does not exist. Cannot create new Banner.");
        }

        // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberName_BannerName(memberName, bannerName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new mvnadBanner with the same [MemberName, BannerName] (" + memberName + ", " + bannerName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setString(1, memberName);
            statement.setString(2, bannerName);
            statement.setString(3, bannerDesc);
            statement.setString(4, bannerAltText);
            statement.setString(5, bannerMimeType);
            statement.setString(6, bannerPreText);
            statement.setString(7, bannerAfterText);
            statement.setString(8, bannerTargetURL);
            statement.setString(9, bannerImageURL);
            statement.setInt(10, bannerWidth);
            statement.setInt(11, bannerHeight);
            statement.setInt(12, bannerWeight);
            statement.setInt(13, bannerMaxImpression);
            statement.setInt(14, bannerReceivedImpression);
            statement.setInt(15, bannerMaxClick);
            statement.setInt(16, bannerReceivedClick);
            statement.setInt(17, bannerZonePositionX);
            statement.setInt(18, bannerZonePositionY);
            statement.setTimestamp(19, bannerStartDate);
            statement.setTimestamp(20, bannerEndDate);
            statement.setInt(21, bannerIsHtml);
            statement.setString(22, bannerHtmlCode);
            statement.setInt(23, bannerCanTrackClicks);
            statement.setInt(24, bannerOption);
            statement.setInt(25, bannerStatus);
            statement.setInt(26, bannerType);
            statement.setTimestamp(27, bannerCreationDate);
            statement.setTimestamp(28, bannerModifiedDate);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Banner'.");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public int createBanner(String memberName, String bannerName, String bannerDesc, 
            String bannerAltText, String bannerMimeType, String bannerPreText, 
            String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
            int bannerWidth, int bannerHeight, int bannerWeight, 
            int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
            int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
            Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
            String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
            int bannerStatus, int bannerType, Timestamp bannerCreationDate, 
            Timestamp bannerModifiedDate)
    throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        
        create(memberName, bannerName, bannerDesc, bannerAltText, bannerMimeType, bannerPreText, bannerAfterText, bannerTargetURL, bannerImageURL, bannerWidth, bannerHeight, bannerWeight, bannerMaxImpression, bannerReceivedImpression, bannerMaxClick, bannerReceivedClick, bannerZonePositionX, bannerZonePositionY, bannerStartDate, bannerEndDate, bannerIsHtml, bannerHtmlCode, bannerCanTrackClicks, bannerOption, bannerStatus, bannerType, bannerCreationDate, bannerModifiedDate);
        BannerBean bannerBean = null;
        try {
            bannerBean = getBean_byAlternateKey_MemberName_BannerName(memberName, bannerName);
        } catch (ObjectNotFoundException ex) {
            log.error("Cannot find BannerID after creating banner.", ex);
        }
        return bannerBean.getBannerID();
    }
    

    /*
     * Included columns: MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, 
     *                   BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, 
     *                   BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, 
     *                   BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, 
     *                   BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, 
     *                   BannerType, BannerModifiedDate
     * Excluded columns: BannerID, BannerCreationDate
     */
    public void update(int bannerID, // primary key
                       String memberName, String bannerName, String bannerDesc, 
                       String bannerAltText, String bannerMimeType, String bannerPreText, 
                       String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
                       int bannerWidth, int bannerHeight, int bannerWeight, 
                       int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
                       int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
                       Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
                       String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
                       int bannerStatus, int bannerType, Timestamp bannerModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        BannerBean bean = getBean(bannerID); // @todo: comment or delete this line if no alternate key are included

        if ( memberName.equalsIgnoreCase(bean.getMemberName()) == false ||
             bannerName.equalsIgnoreCase(bean.getBannerName()) == false ) {
            // mvnadBanner tries to change its alternate key [MemberName, BannerName], so we must check if it already exist
            try {
                findByAlternateKey_MemberName_BannerName(memberName, bannerName);
                throw new DuplicateKeyException("Alternate key [MemberName, BannerName] (" + memberName + ", " + bannerName + ") already exists. Cannot update mvnadBanner.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
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
        sql.append("UPDATE " + TABLE_NAME + " SET MemberName = ?, BannerName = ?, BannerDesc = ?, BannerAltText = ?, BannerMimeType = ?, BannerPreText = ?, BannerAfterText = ?, BannerTargetURL = ?, BannerImageURL = ?, BannerWidth = ?, BannerHeight = ?, BannerWeight = ?, BannerMaxImpression = ?, BannerReceivedImpression = ?, BannerMaxClick = ?, BannerReceivedClick = ?, BannerZonePositionX = ?, BannerZonePositionY = ?, BannerStartDate = ?, BannerEndDate = ?, BannerIsHtml = ?, BannerHtmlCode = ?, BannerCanTrackClicks = ?, BannerOption = ?, BannerStatus = ?, BannerType = ?, BannerModifiedDate = ?");
        sql.append(" WHERE BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, memberName);
            statement.setString(2, bannerName);
            statement.setString(3, bannerDesc);
            statement.setString(4, bannerAltText);
            statement.setString(5, bannerMimeType);
            statement.setString(6, bannerPreText);
            statement.setString(7, bannerAfterText);
            statement.setString(8, bannerTargetURL);
            statement.setString(9, bannerImageURL);
            statement.setInt(10, bannerWidth);
            statement.setInt(11, bannerHeight);
            statement.setInt(12, bannerWeight);
            statement.setInt(13, bannerMaxImpression);
            statement.setInt(14, bannerReceivedImpression);
            statement.setInt(15, bannerMaxClick);
            statement.setInt(16, bannerReceivedClick);
            statement.setInt(17, bannerZonePositionX);
            statement.setInt(18, bannerZonePositionY);
            statement.setTimestamp(19, bannerStartDate);
            statement.setTimestamp(20, bannerEndDate);
            statement.setInt(21, bannerIsHtml);
            statement.setString(22, bannerHtmlCode);
            statement.setInt(23, bannerCanTrackClicks);
            statement.setInt(24, bannerOption);
            statement.setInt(25, bannerStatus);
            statement.setInt(26, bannerType);
            statement.setTimestamp(27, bannerModifiedDate);

            // primary key column(s)
            statement.setInt(28, bannerID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Banner where primary key = (" + bannerID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void increaseReceivedClick(int bannerID, int receivedClickCount) 
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("UPDATE " + TABLE_NAME + " SET BannerReceivedClick = BannerReceivedClick + ").append(receivedClickCount);
        sql.append(" WHERE BannerID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, bannerID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot increase BannerReceivedClick where BannerID = " + bannerID + ".");
            }
        } catch (SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.increaseReceivedClick().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void increaseReceivedImpression(int bannerID, int receivedImpressionCount) 
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        StringBuffer sql = new StringBuffer(512);
        
        sql.append("UPDATE " + TABLE_NAME + " SET BannerReceivedImpression = BannerReceivedImpression + ").append(receivedImpressionCount);
        sql.append(" WHERE BannerID = ?");
        
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setInt(1, bannerID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot increase BannerReceivedImpression where BannerID = " + bannerID + ".");
            }
        } catch (SQLException sqle) {
            log.error("SQL Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.increaseReceivedImpression().");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateReceivedImpression(int bannerID, // primary key
                                         int bannerReceivedImpression)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET BannerReceivedImpression = ?");
        sql.append(" WHERE BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            // // column(s) to update
            statement.setInt(1, bannerReceivedImpression);
            
            // primary key column(s)
            statement.setInt(2, bannerID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Banner where primary key = (" + bannerID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.updateReceivedImpression.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void updateReceivedClick(int bannerID, // primary key
            int bannerReceivedClick)
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET BannerReceivedClick = ?");
        sql.append(" WHERE BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            // // column(s) to update
            statement.setInt(1, bannerReceivedClick);
            
            // primary key column(s)
            statement.setInt(2, bannerID);
            
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table Banner where primary key = (" + bannerID + ").");
            }
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.updateReceivedClick.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void delete(int bannerID)
        throws DatabaseException, ObjectNotFoundException {

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("DELETE FROM " + TABLE_NAME);
        sql.append(" WHERE BannerID = ?");

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, bannerID);
            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot delete a row in table Banner where primary key = (" + bannerID + ").");
            }
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.delete.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, 
     *                   BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, 
     *                   BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, 
     *                   BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, 
     *                   BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, 
     *                   BannerType, BannerCreationDate, BannerModifiedDate
     * Excluded columns: BannerID
     */
    public BannerBean getBean(int bannerID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE BannerID = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, bannerID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Banner where primary key = (" + bannerID + ").");
            }

            BannerBean bean = new BannerBean();
            bean.setBannerID(bannerID);
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
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: BannerID, MemberName, BannerName, BannerDesc, BannerAltText, 
     *                   BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, 
     *                   BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, 
     *                   BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, 
     *                   BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, 
     *                   BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate
     * Excluded columns: 
     */
    public BannerBean getBean_byAlternateKey_MemberName_BannerName(String memberName, String bannerName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT BannerID, MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberName = ? AND BannerName = ?");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, memberName);
            statement.setString(2, bannerName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Banner where alternate key [MemberName, BannerName] = (" + memberName + ", " + bannerName + ").");
            }

            BannerBean bean = new BannerBean();
            bean.setMemberName(memberName);
            bean.setBannerName(bannerName);
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
            return bean;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getBean_byAlternateKey_MemberName_BannerName(ak).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: BannerID, MemberName, BannerName, BannerDesc, BannerAltText, 
     *                   BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, 
     *                   BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, 
     *                   BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, 
     *                   BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, 
     *                   BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate
     * Excluded columns: 
     */
    public Collection getBeans()
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT BannerID, MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY BannerModifiedDate ASC");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
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
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: BannerID, MemberName, BannerName, BannerDesc, BannerAltText, 
     *                   BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, 
     *                   BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, 
     *                   BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, 
     *                   BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, 
     *                   BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate
     * Excluded columns: 
     */
    public Collection getBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, String memberName)
        throws IllegalArgumentException, DatabaseException {

        if (offset < 0) throw new IllegalArgumentException("The offset < 0 is not allowed.");
        if (rowsToReturn <= 0) throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");

        if ((!sort.equals("BannerID")) &&
            (!sort.equals("MemberName")) &&
            (!sort.equals("BannerName")) &&
            (!sort.equals("BannerReceivedImpression")) &&
            (!sort.equals("BannerReceivedClick")) &&
            (!sort.equals("BannerStartDate")) &&
            (!sort.equals("BannerEndDate")) &&
            (!sort.equals("BannerCreationDate"))) {
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
        sql.append("SELECT BannerID, MemberName, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        if (memberName != null && memberName.length() != 0) {
            sql.append(" WHERE MemberName = ?");
        }
        sql.append(" ORDER BY " + sort + " " + order);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (memberName != null && memberName.length() != 0) {
                statement.setString(1, memberName);
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
                if (retValue.size() == rowsToReturn) break;// Fix the Sybase bug
                loop = resultSet.next();
            }//while
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getBeans_withSortSupport_limit.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.resetStatement(statement);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public Collection getValidBanners(Timestamp now) 
        throws DatabaseException, ObjectNotFoundException {
        
        Collection result = new ArrayList();
        
        Collection bannerBeans = getBeans();
        
        for (Iterator iterator = bannerBeans.iterator(); iterator.hasNext(); ) {
            BannerBean bannerBean = (BannerBean) iterator.next();
            
            if (bannerBean.isValid(now)) {
                result.add(bannerBean);
            }
        }
        
        return result;
    }

    public Collection getBannersOfUser(String memberName)
        throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(1024);
        sql.append("SELECT BannerID, BannerName, BannerDesc, BannerAltText, BannerMimeType, BannerPreText, BannerAfterText, BannerTargetURL, BannerImageURL, BannerWidth, BannerHeight, BannerWeight, BannerMaxImpression, BannerReceivedImpression, BannerMaxClick, BannerReceivedClick, BannerZonePositionX, BannerZonePositionY, BannerStartDate, BannerEndDate, BannerIsHtml, BannerHtmlCode, BannerCanTrackClicks, BannerOption, BannerStatus, BannerType, BannerCreationDate, BannerModifiedDate");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberName = ?");
        sql.append(" ORDER BY BannerModifiedDate ASC");
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            
            statement.setString(1, memberName);
            
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BannerBean bean = new BannerBean();
                bean.setBannerID(resultSet.getInt("BannerID"));
                bean.setMemberName(memberName);
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
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getBannersOfUser.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    public Collection getDistinctMemberNamesHasBanner() 
        throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(1024);
        sql.append("SELECT DISTINCT MemberName");
        sql.append(" FROM " + TABLE_NAME);
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String memberName = resultSet.getString("MemberName");
                int memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
                retValue.add(DAOFactory.getMemberDAO().getMember(memberID));
            }
            return retValue;
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getDistinctMemberNamesHasBanner.");
        } finally {
            DBUtils.closeResultSet(resultSet);
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
            AssertionUtil.doAssert(resultSet.next(), "Assertion in BannerDAOImplJDBC.getNumberOfBeans.");
            return resultSet.getInt(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in BannerDAOImplJDBC.getNumberOfBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
}
