/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/MemberDAOImpl.java,v 1.13 2007/12/18 04:58:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.MemberDAO;
import org.mvnforum.util.DBUtils;

import com.mvnforum.db.MemberBean;

public class MemberDAOImpl implements MemberDAO {    

    public void findByPrimaryKey(int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");

        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the primary key (" + memberID + ") in table 'Member'.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.findByPrimaryKey.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByPrimaryKey2(int memberID, String memberName)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
    }

    public void findByAlternateKey_MemberEmail(String memberEmail)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberEmail");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberEmail = ?");

        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, memberEmail);
            resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [MemberEmail] (" + memberEmail + ") in table 'Member'.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.findByAlternateKey_MemberEmail.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void findByAlternateKey_MemberName(String memberName)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberName");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberName = ?");

        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setString(1, memberName);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the alternate key [MemberName] (" + memberName + ") in table 'Member'.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.findByAlternateKey_MemberName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void deleteByPrimaryKey(int memberID)
        throws DatabaseException {
        // TODO Auto-generated method stub

    }

    /*
     * Included columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, 
     *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, 
     *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberLastLogon, 
     *                   MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, 
     *                   MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, 
     *                   MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, 
     *                   MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, 
     *                   MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, 
     *                   MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, 
     *                   MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, 
     *                   MemberCoolLink2
     * Excluded columns: 
     */
    public void create(int memberID, String memberName, String memberPassword, 
                        String memberFirstEmail, String memberEmail, int memberEmailVisible, 
                        int memberNameVisible, String memberFirstIP, String memberLastIP, 
                        int memberViewCount, int memberPostCount, Timestamp memberCreationDate, 
                        Timestamp memberModifiedDate, Timestamp memberExpireDate, Timestamp memberLastLogon, 
                        int memberOption, int memberStatus, String memberActivateCode, 
                        String memberTempPassword, int memberMessageCount, int memberMessageOption, 
                        int memberPostsPerPage, int memberWarnCount, int memberVoteCount, 
                        int memberVoteTotalStars, int memberRewardPoints, String memberTitle, 
                        double memberTimeZone, String memberSignature, String memberAvatar, 
                        String memberSkin, String memberLanguage, String memberFirstname, 
                        String memberLastname, int memberGender, Date memberBirthday, 
                        String memberAddress, String memberCity, String memberState, 
                        String memberCountry, String memberPhone, String memberMobile, 
                        String memberFax, String memberCareer, String memberHomepage, 
                        String memberYahoo, String memberAol, String memberIcq, 
                        String memberMsn, String memberCoolLink1, String memberCoolLink2)
        throws CreateException, DatabaseException, DuplicateKeyException {

        // @todo: comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        // However, if primary key is a auto_increament column, then you can safely delete this block
        try {
            //Check if primary key already exists
            findByPrimaryKey(memberID);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Primary key already exists. Cannot create new member with the same [MemberID] (" + memberID + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberEmail(memberEmail);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new member with the same [MemberEmail] (" + memberEmail + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        // @todo: Comment this try-catch block if the needed columns dont have attribute 'include'
        // If this is the case, then it is highly recommended that you regenerate this method with the attribute 'include' turned on
        try {
            //Check if alternate key already exists
            findByAlternateKey_MemberName(memberName);
            //If so, then we have to throw an exception
            throw new DuplicateKeyException("Alternate key already exists. Cannot create new member with the same [MemberName] (" + memberName + ").");
        } catch(ObjectNotFoundException e) {
            //Otherwise we can go ahead
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("INSERT INTO " + TABLE_NAME + " (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            statement.setInt(1, memberID);
            statement.setString(2, memberName);
            statement.setString(3, memberPassword);
            statement.setString(4, memberFirstEmail);
            statement.setString(5, memberEmail);
            statement.setInt(6, memberEmailVisible);
            statement.setInt(7, memberNameVisible);
            statement.setString(8, memberFirstIP);
            statement.setString(9, memberLastIP);
            statement.setInt(10, memberViewCount);
            statement.setInt(11, memberPostCount);
            statement.setTimestamp(12, memberCreationDate);
            statement.setTimestamp(13, memberModifiedDate);
            statement.setTimestamp(14, memberExpireDate);
            statement.setTimestamp(15, memberLastLogon);
            statement.setInt(16, memberOption);
            statement.setInt(17, memberStatus);
            statement.setString(18, memberActivateCode);
            statement.setString(19, memberTempPassword);
            statement.setInt(20, memberMessageCount);
            statement.setInt(21, memberMessageOption);
            statement.setInt(22, memberPostsPerPage);
            statement.setInt(23, memberWarnCount);
            statement.setInt(24, memberVoteCount);
            statement.setInt(25, memberVoteTotalStars);
            statement.setInt(26, memberRewardPoints);
            statement.setString(27, memberTitle);
            statement.setDouble(28, memberTimeZone);
            statement.setString(29, memberSignature);
            statement.setString(30, memberAvatar);
            statement.setString(31, memberSkin);
            statement.setString(32, memberLanguage);
            statement.setString(33, memberFirstname);
            statement.setString(34, memberLastname);
            statement.setInt(35, memberGender);
            statement.setDate(36, memberBirthday);
            statement.setString(37, memberAddress);
            statement.setString(38, memberCity);
            statement.setString(39, memberState);
            statement.setString(40, memberCountry);
            statement.setString(41, memberPhone);
            statement.setString(42, memberMobile);
            statement.setString(43, memberFax);
            statement.setString(44, memberCareer);
            statement.setString(45, memberHomepage);
            statement.setString(46, memberYahoo);
            statement.setString(47, memberAol);
            statement.setString(48, memberIcq);
            statement.setString(49, memberMsn);
            statement.setString(50, memberCoolLink1);
            statement.setString(51, memberCoolLink2);

            if (statement.executeUpdate() != 1) {
                throw new CreateException("Error adding a row into table 'member'.");
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in memberDAOImplJDBC.create.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    /*
     * Included columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, 
     *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, 
     *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberLastLogon, 
     *                   MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, 
     *                   MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, 
     *                   MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, 
     *                   MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, 
     *                   MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, 
     *                   MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, 
     *                   MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, 
     *                   MemberCoolLink2
     * Excluded columns: 
     */
    public MemberBean getBean(int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Member where primary key = (" + memberID + ").");
            }

            MemberBean bean = new MemberBean();
            // @todo: uncomment the following line(s) as needed
            //bean.setMemberID(memberID);
            bean.setMemberID(resultSet.getInt("MemberID"));
            bean.setMemberName(resultSet.getString("MemberName"));
            //bean.setMemberPassword(resultSet.getString("MemberPassword"));
            bean.setMemberFirstEmail(resultSet.getString("MemberFirstEmail"));
            bean.setMemberEmail(resultSet.getString("MemberEmail"));
            bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
            bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
            bean.setMemberFirstIP(resultSet.getString("MemberFirstIP"));
            bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
            bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
            bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
            bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
            bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
            bean.setMemberExpireDate(resultSet.getTimestamp("MemberExpireDate"));
            bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
            bean.setMemberOption(resultSet.getInt("MemberOption"));
            bean.setMemberStatus(resultSet.getInt("MemberStatus"));
            bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
            //bean.setMemberTempPassword(resultSet.getString("MemberTempPassword"));
            bean.setMemberMessageCount(resultSet.getInt("MemberMessageCount"));
            bean.setMemberMessageOption(resultSet.getInt("MemberMessageOption"));
            bean.setMemberPostsPerPage(resultSet.getInt("MemberPostsPerPage"));
            bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
            bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
            bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
            bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
            bean.setMemberTitle(resultSet.getString("MemberTitle"));
            bean.setMemberTimeZone(resultSet.getDouble("MemberTimeZone"));
            bean.setMemberSignature(resultSet.getString("MemberSignature"));
            bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
            bean.setMemberSkin(resultSet.getString("MemberSkin"));
            bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
            bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
            bean.setMemberLastname(resultSet.getString("MemberLastname"));
            bean.setMemberGender(resultSet.getInt("MemberGender"));
            bean.setMemberBirthday(resultSet.getDate("MemberBirthday"));
            bean.setMemberAddress(resultSet.getString("MemberAddress"));
            bean.setMemberCity(resultSet.getString("MemberCity"));
            bean.setMemberState(resultSet.getString("MemberState"));
            bean.setMemberCountry(resultSet.getString("MemberCountry"));
            bean.setMemberPhone(resultSet.getString("MemberPhone"));
            bean.setMemberMobile(resultSet.getString("MemberMobile"));
            bean.setMemberFax(resultSet.getString("MemberFax"));
            bean.setMemberCareer(resultSet.getString("MemberCareer"));
            bean.setMemberHomepage(resultSet.getString("MemberHomepage"));
            bean.setMemberYahoo(resultSet.getString("MemberYahoo"));
            bean.setMemberAol(resultSet.getString("MemberAol"));
            bean.setMemberIcq(resultSet.getString("MemberIcq"));
            bean.setMemberMsn(resultSet.getString("MemberMsn"));
            bean.setMemberCoolLink1(resultSet.getString("MemberCoolLink1"));
            bean.setMemberCoolLink2(resultSet.getString("MemberCoolLink2"));
            return bean;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
    /*
     * Included columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, 
     *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, 
     *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberLastLogon, 
     *                   MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, 
     *                   MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, 
     *                   MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, 
     *                   MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, 
     *                   MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, 
     *                   MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, 
     *                   MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, 
     *                   MemberCoolLink2
     * Excluded columns: 
     */
    public void update(int memberID, // primary key
                        String memberName, String memberPassword, String memberFirstEmail, 
                        String memberEmail, int memberEmailVisible, int memberNameVisible, 
                        String memberFirstIP, String memberLastIP, int memberViewCount, 
                        int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, 
                        Timestamp memberExpireDate, Timestamp memberLastLogon, int memberOption, 
                        int memberStatus, String memberActivateCode, String memberTempPassword, 
                        int memberMessageCount, int memberMessageOption, int memberPostsPerPage, 
                        int memberWarnCount, int memberVoteCount, int memberVoteTotalStars, 
                        int memberRewardPoints, String memberTitle, double memberTimeZone, 
                        String memberSignature, String memberAvatar, String memberSkin, 
                        String memberLanguage, String memberFirstname, String memberLastname, 
                        int memberGender, Date memberBirthday, String memberAddress, 
                        String memberCity, String memberState, String memberCountry, 
                        String memberPhone, String memberMobile, String memberFax, 
                        String memberCareer, String memberHomepage, String memberYahoo, 
                        String memberAol, String memberIcq, String memberMsn, 
                        String memberCoolLink1, String memberCoolLink2)
        throws ObjectNotFoundException, DatabaseException/*, DuplicateKeyException*/, DuplicateKeyException {

        MemberBean bean = getBean(memberID); // @todo: comment or delete this line if no alternate key are included

        if ( !memberEmail.equals(bean.getMemberEmail()) ) {
            // member tries to change its alternate key [MemberEmail], so we must check if it already exist
            try {
                findByAlternateKey_MemberEmail(memberEmail);
                throw new DuplicateKeyException("Alternate key [MemberEmail] (" + memberEmail + ") already exists. Cannot update member.");
            } catch(ObjectNotFoundException e) {
                //Otherwise we can go ahead
            }
        }

        Connection connection = null;
        PreparedStatement statement = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("UPDATE " + TABLE_NAME + " SET MemberName = ?, MemberPassword = ?, MemberFirstEmail = ?, MemberEmail = ?, MemberEmailVisible = ?, MemberNameVisible = ?, MemberFirstIP = ?, MemberLastIP = ?, MemberViewCount = ?, MemberPostCount = ?, MemberCreationDate = ?, MemberModifiedDate = ?, MemberExpireDate = ?, MemberLastLogon = ?, MemberOption = ?, MemberStatus = ?, MemberActivateCode = ?, MemberTempPassword = ?, MemberMessageCount = ?, MemberMessageOption = ?, MemberPostsPerPage = ?, MemberWarnCount = ?, MemberVoteCount = ?, MemberVoteTotalStars = ?, MemberRewardPoints = ?, MemberTitle = ?, MemberTimeZone = ?, MemberSignature = ?, MemberAvatar = ?, MemberSkin = ?, MemberLanguage = ?, MemberFirstname = ?, MemberLastname = ?, MemberGender = ?, MemberBirthday = ?, MemberAddress = ?, MemberCity = ?, MemberState = ?, MemberCountry = ?, MemberPhone = ?, MemberMobile = ?, MemberFax = ?, MemberCareer = ?, MemberHomepage = ?, MemberYahoo = ?, MemberAol = ?, MemberIcq = ?, MemberMsn = ?, MemberCoolLink1 = ?, MemberCoolLink2 = ?");
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());

            // // column(s) to update
            statement.setString(1, memberName);
            statement.setString(2, memberPassword);
            statement.setString(3, memberFirstEmail);
            statement.setString(4, memberEmail);
            statement.setInt(5, memberEmailVisible);
            statement.setInt(6, memberNameVisible);
            statement.setString(7, memberFirstIP);
            statement.setString(8, memberLastIP);
            statement.setInt(9, memberViewCount);
            statement.setInt(10, memberPostCount);
            statement.setTimestamp(11, memberCreationDate);
            statement.setTimestamp(12, memberModifiedDate);
            statement.setTimestamp(13, memberExpireDate);
            statement.setTimestamp(14, memberLastLogon);
            statement.setInt(15, memberOption);
            statement.setInt(16, memberStatus);
            statement.setString(17, memberActivateCode);
            statement.setString(18, memberTempPassword);
            statement.setInt(19, memberMessageCount);
            statement.setInt(20, memberMessageOption);
            statement.setInt(21, memberPostsPerPage);
            statement.setInt(22, memberWarnCount);
            statement.setInt(23, memberVoteCount);
            statement.setInt(24, memberVoteTotalStars);
            statement.setInt(25, memberRewardPoints);
            statement.setString(26, memberTitle);
            statement.setDouble(27, memberTimeZone);
            statement.setString(28, memberSignature);
            statement.setString(29, memberAvatar);
            statement.setString(30, memberSkin);
            statement.setString(31, memberLanguage);
            statement.setString(32, memberFirstname);
            statement.setString(33, memberLastname);
            statement.setInt(34, memberGender);
            statement.setDate(35, memberBirthday);
            statement.setString(36, memberAddress);
            statement.setString(37, memberCity);
            statement.setString(38, memberState);
            statement.setString(39, memberCountry);
            statement.setString(40, memberPhone);
            statement.setString(41, memberMobile);
            statement.setString(42, memberFax);
            statement.setString(43, memberCareer);
            statement.setString(44, memberHomepage);
            statement.setString(45, memberYahoo);
            statement.setString(46, memberAol);
            statement.setString(47, memberIcq);
            statement.setString(48, memberMsn);
            statement.setString(49, memberCoolLink1);
            statement.setString(50, memberCoolLink2);

            // primary key column(s)
            statement.setInt(51, memberID);

            if (statement.executeUpdate() != 1) {
                throw new ObjectNotFoundException("Cannot update table member where primary key = (" + memberID + ").");
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in memberDAOImplJDBC.update.");
        } finally {
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public String getMemberNameFromMemberID(int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT MemberName");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE MemberID = ?");
        try {
            connection = DBUtils.getMvnConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table member where primary key = (" + memberID + ").");
            }
            return resultSet.getString("MemberName");
        } catch(SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in memberDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    public void create(String memberName, String memberPassword, String memberFirstEmail, String memberEmail, int memberEmailVisible, int memberNameVisible, String memberFirstIP, String memberLastIP, int memberViewCount, int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, Timestamp memberExpireDate, Timestamp memberLastLogon, int memberOption, int memberStatus, String memberActivateCode, String memberTempPassword, int memberMessageCount, int memberMessageOption, int memberPostsPerPage, int memberWarnCount, int memberVoteCount, int memberVoteTotalStars, int memberRewardPoints, String memberTitle, double tzone, String memberSignature, String memberAvatar, String memberSkin, String memberLanguage, String memberFirstname, String memberLastname, int memberGender, Date memberBirthday, String memberAddress, String memberCity, String memberState, String memberCountry, String memberPhone, String memberMobile, String memberFax, String memberCareer, String memberHomepage, String memberYahoo, String memberAol, String memberIcq, String memberMsn, String memberCoolLink1, String memberCoolLink2)
        throws CreateException, DatabaseException, DuplicateKeyException {
        // TODO Auto-generated method stub
        
    }

    public org.mvnforum.phpbb2mvnforum.db.MemberBean getMemberFromMemberName(String memberName)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }


}
