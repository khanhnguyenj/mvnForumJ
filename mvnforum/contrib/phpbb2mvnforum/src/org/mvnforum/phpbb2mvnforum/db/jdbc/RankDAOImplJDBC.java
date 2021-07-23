/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/RankDAOImplJDBC.java,v 1.7 2007/01/15 10:27:11 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.7 $
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

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.util.DBUtils;

import com.mvnforum.db.RankBean;
import com.mvnforum.db.RankDAO;

public class RankDAOImplJDBC implements RankDAO{

    public void findByAlternateKey_RankTitle(String rankTitle)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT RankMinPosts");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE RankTitle = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, rankTitle);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [RankTitle] (" + rankTitle + ") in table 'Rank'.");
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in RankDAOImplJDBC.findByAlternateKey_RankTitle.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
        
    }

    public void findByAlternateKey_RankMinPosts(int rankMinPosts)
        throws ObjectNotFoundException, DatabaseException {
        
        // -1 is meaningless 
        // TODO: check it again
        if (rankMinPosts  == -1) {
            throw new IllegalArgumentException("Does not allow rankMinPosts < 0");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT RankTitle");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE RankMinPosts = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, rankMinPosts);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [RankMinPosts] (" + rankMinPosts + ") in table 'Rank'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in RankDAOImplJDBC.findByAlternateKey_RankMinPosts.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(int rankMinPosts, int rankLevel, String rankTitle, String rankImage, int rankType, int rankOption)
        throws CreateException, DatabaseException, DuplicateKeyException {

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_RankTitle(rankTitle);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Rank with the same [RankTitle] (" + rankTitle + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_RankMinPosts(rankMinPosts);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new Rank with the same [RankMinPosts] (" + rankMinPosts + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, rankMinPosts);
            statement.setInt(2, rankLevel);
            statement.setString(3, rankTitle);
            statement.setString(4, rankImage);
            statement.setInt(5, rankType);
            statement.setInt(6, rankOption);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'Rank'.");
            }
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in RankDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void update(int rankID, int rankMinPosts, int rankLevel, String rankTitle, String rankImage, int rankType, int rankOption)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {
        // TODO Auto-generated method stub
        
    }

    public void delete(int rankID)
        throws DatabaseException, ObjectNotFoundException {
        // TODO Auto-generated method stub
        
    }

    public RankBean getRank(int rankID)
        throws ObjectNotFoundException, DatabaseException {
        return null;
    }

    public Collection getRanks()
        throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT RankID, RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" ORDER BY RankMinPosts ASC ");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RankBean bean = new RankBean();
                bean.setRankID(resultSet.getInt("RankID"));
                bean.setRankMinPosts(resultSet.getInt("RankMinPosts"));
                bean.setRankLevel(resultSet.getInt("RankLevel"));
                bean.setRankTitle(resultSet.getString("RankTitle"));
                bean.setRankImage(resultSet.getString("RankImage"));
                bean.setRankType(resultSet.getInt("RankType"));
                bean.setRankOption(resultSet.getInt("RankOption"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in RankDAOImplJDBC.getRanks.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }

    }

    public int getRankIDFromRankTitle(String rankTitle)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return 0;
    }

}
