/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/jdbc/MemberDAOImplJDBC.java,v 1.112 2009/09/15 10:16:31 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.112 $
 * $Date: 2009/09/15 10:16:31 $
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
package com.mvnforum.db.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberDAO;
import com.mvnforum.db.MemberForumDAO;
import com.mvnforum.db.MemberGroupDAO;
import com.mvnforum.db.MemberPermissionDAO;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.StringUtil;

public class MemberDAOImplJDBC implements MemberDAO {

  private static final Logger log = LoggerFactory.getLogger(MemberDAOImplJDBC.class);

  public MemberDAOImplJDBC() {}

  @Override
  public boolean isSupportFindByPrimaryKey() {
    return true;
  }

  @Override
  public void findByPrimaryKey(int memberID) throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberID");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the primary key (" + memberID + ") in table 'Member'.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.findByPrimaryKey.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportFindByPrimaryKey2() {
    return true;
  }

  @Override
  public void findByPrimaryKey2(int memberID, String memberName)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberID, MemberName");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ? AND MemberName = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      statement.setString(2, memberName);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the primary key (" + memberID + "," + memberName + ") in table 'Member'.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.findByPrimaryKey2.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportFindByAlternateKey_MemberName() {
    return true;
  }

  @Override
  public String findByAlternateKey_MemberName(String memberName)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberName");
    sql.append(" FROM " + TABLE_NAME);
    if (DBUtils.isCaseSensitiveDatebase()) {
      sql.append(" WHERE lower(MemberName) = lower(?)");
    } else {
      sql.append(" WHERE MemberName = ?");
    }
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setString(1, memberName);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the alternate key [MemberName] (" + memberName + ") in table 'Member'.");
      }
      return resultSet.getString("MemberName");
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.findByAlternateKey_MemberName.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportFindByAlternateKey_MemberEmail() {
    return true;
  }

  @Override
  public void findByAlternateKey_MemberEmail(String memberEmail)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberEmail");
    sql.append(" FROM " + TABLE_NAME);
    if (DBUtils.isCaseSensitiveDatebase()) {
      sql.append(" WHERE lower(MemberEmail) = lower(?)");
    } else {
      sql.append(" WHERE MemberEmail = ?");
    }
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setString(1, memberEmail);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the alternate key [MemberEmail] (" + memberEmail + ") in table 'Member'.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.findByAlternateKey_MemberEmail.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportDeleteByPrimaryKey() {
    return true;
  }
  /**
   * Delete the user specified by memberID. Note that this method will not fail it the given id does
   * not exists.
   *
   * @param memberID an <code>int</code> value
   * @exception DatabaseException if an error occurs
   */
  @Override
  public void deleteByPrimaryKey(int memberID) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("DELETE");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      statement.executeUpdate();
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.deleteByPrimaryKey.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportCreate() {
    return true;
  }
  /*
   * Included columns: MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   * Excluded columns: MemberID
   */
  @Override
  public void create(
      String memberName,
      String memberPassword,
      String memberFirstEmail,
      String memberEmail,
      int memberEmailVisible,
      int memberNameVisible,
      String memberFirstIP,
      String memberLastIP,
      int memberViewCount,
      int memberPostCount,
      Timestamp memberCreationDate,
      Timestamp memberModifiedDate,
      Timestamp memberExpireDate,
      Timestamp memberPasswordExpireDate,
      Timestamp memberLastLogon,
      int memberOption,
      int memberStatus,
      String memberActivateCode,
      String memberTempPassword,
      int memberMessageCount,
      int memberMessageOption,
      int memberPostsPerPage,
      int memberWarnCount,
      int memberVoteCount,
      int memberVoteTotalStars,
      int memberRewardPoints,
      String memberTitle,
      double memberTimeZone,
      String memberSignature,
      String memberAvatar,
      String memberSkin,
      String memberLanguage,
      String memberFirstname,
      String memberLastname,
      int memberGender,
      Date memberBirthday,
      String memberAddress,
      String memberCity,
      String memberState,
      String memberCountry,
      String memberPhone,
      String memberMobile,
      String memberFax,
      String memberCareer,
      String memberHomepage)
      throws CreateException, DatabaseException, DuplicateKeyException, IllegalArgumentException {

    // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
    // If this is the case, then it is highly recommended that you regenerate this method with the
    // attribute 'include' turned on
    try {
      // Check if alternate key already exists
      findByAlternateKey_MemberName(memberName);
      // If so, then we have to throw an exception
      throw new DuplicateKeyException(
          "Alternate key already exists. Cannot create new Member with the same [MemberName] ("
              + memberName
              + ").");
    } catch (ObjectNotFoundException e) {
      // Otherwise we can go ahead
    }

    // @todo: Comment this try-catch block if the needed columns don't have attribute 'include'
    // If this is the case, then it is highly recommended that you regenerate this method with the
    // attribute 'include' turned on
    try {
      // Check if alternate key already exists
      findByAlternateKey_MemberEmail(memberEmail);
      // If so, then we have to throw an exception
      throw new DuplicateKeyException(
          "Alternate key already exists. Cannot create new Member with the same [MemberEmail] ("
              + memberEmail
              + ").");
    } catch (ObjectNotFoundException e) {
      // Otherwise we can go ahead
    }

    if (memberStatus < MemberBean.MEMBER_STATUS_ENABLE
        || memberStatus > MemberBean.MEMBER_STATUS_PENDING) {
      throw new IllegalArgumentException(
          "Cannot resolve illegal argument MemberStatus = " + memberStatus);
    }

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "INSERT INTO "
            + TABLE_NAME
            + " (MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage)");
    sql.append(
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

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
      statement.setTimestamp(14, memberPasswordExpireDate);
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

      if (statement.executeUpdate() != 1) {
        throw new CreateException("Error adding a row into table 'Member'.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.create.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdate() {
    return true;
  }
  /*
   * Included columns: MemberEmailVisible, MemberNameVisible, MemberModifiedDate, MemberOption, MemberStatus,
   *                   MemberMessageOption, MemberPostsPerPage, MemberTimeZone, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate,
   *                   MemberLastLogon, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberWarnCount,
   *                   MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature,
   *                   MemberAvatar
   */
  @Override
  public void update(
      int memberID, // primary key
      int memberEmailVisible,
      int memberNameVisible,
      Timestamp memberModifiedDate,
      int memberOption,
      int memberStatus,
      int memberMessageOption,
      int memberPostsPerPage,
      double memberTimeZone,
      String memberSkin,
      String memberLanguage,
      String memberFirstname,
      String memberLastname,
      int memberGender,
      Date memberBirthday,
      String memberAddress,
      String memberCity,
      String memberState,
      String memberCountry,
      String memberPhone,
      String memberMobile,
      String memberFax,
      String memberCareer,
      String memberHomepage)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "UPDATE "
            + TABLE_NAME
            + " SET MemberEmailVisible = ?, MemberNameVisible = ?, MemberModifiedDate = ?, MemberOption = ?, MemberStatus = ?, MemberMessageOption = ?, MemberPostsPerPage = ?, MemberTimeZone = ?, MemberSkin = ?, MemberLanguage = ?, MemberFirstname = ?, MemberLastname = ?, MemberGender = ?, MemberBirthday = ?, MemberAddress = ?, MemberCity = ?, MemberState = ?, MemberCountry = ?, MemberPhone = ?, MemberMobile = ?, MemberFax = ?, MemberCareer = ?, MemberHomepage = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setInt(1, memberEmailVisible);
      statement.setInt(2, memberNameVisible);
      statement.setTimestamp(3, memberModifiedDate);
      statement.setInt(4, memberOption);
      statement.setInt(5, memberStatus);
      statement.setInt(6, memberMessageOption);
      statement.setInt(7, memberPostsPerPage);
      statement.setDouble(8, memberTimeZone);
      statement.setString(9, memberSkin);
      statement.setString(10, memberLanguage);
      statement.setString(11, memberFirstname);
      statement.setString(12, memberLastname);
      statement.setInt(13, memberGender);
      statement.setDate(14, memberBirthday);
      statement.setString(15, memberAddress);
      statement.setString(16, memberCity);
      statement.setString(17, memberState);
      statement.setString(18, memberCountry);
      statement.setString(19, memberPhone);
      statement.setString(20, memberMobile);
      statement.setString(21, memberFax);
      statement.setString(22, memberCareer);
      statement.setString(23, memberHomepage);

      // primary key column(s)
      statement.setInt(30, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.update.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateEmail() {
    return true;
  }
  /*
   * Included columns: MemberEmail
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateEmail(
      int memberID, // primary key
      String memberEmail)
      throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {

    // @todo: use a more efficent method
    MemberBean bean =
        getMember(memberID); // @todo: comment or delete this line if no alternate key are included

    if (memberEmail.equalsIgnoreCase(bean.getMemberEmail()) == false) {
      // Member tries to change its alternate key <MemberEmail>, so we must check if it already
      // exist
      try {
        findByAlternateKey_MemberEmail(memberEmail);
        throw new DuplicateKeyException(
            "Alternate key [MemberEmail] ("
                + memberEmail
                + ") already exists. Cannot update Member.");
      } catch (ObjectNotFoundException e) {
        // Otherwise we can go ahead
      }
    }

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberEmail = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberEmail);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateEmail.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdatePassword() {
    return true;
  }
  /*
   * Included columns: MemberPassword
   * Excluded columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updatePassword(
      int memberID, // primary key
      String memberPassword,
      Timestamp memberPasswordExpireDate)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    if (memberPasswordExpireDate == null) {
      sql.append("UPDATE " + TABLE_NAME + " SET MemberPassword = ?");
    } else {
      sql.append("UPDATE " + TABLE_NAME + " SET MemberPassword = ?, MemberPasswordExpireDate = ?");
    }
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      if (memberPasswordExpireDate == null) {
        // // column(s) to update
        statement.setString(1, memberPassword);

        // primary key column(s)
        statement.setInt(2, memberID);
      } else {
        // // column(s) to update
        statement.setString(1, memberPassword);
        statement.setTimestamp(2, memberPasswordExpireDate);

        // primary key column(s)
        statement.setInt(3, memberID);
      }

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updatePassword.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateTempPassword() {
    return true;
  }
  /*
   * Included columns: MemberTempPassword
   * Excluded columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateTempPassword(
      int memberID, // primary key
      String memberTempPassword)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberTempPassword = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberTempPassword);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateTempPassword.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateActivateCode() {
    return true;
  }
  /*
   * Included columns: MemberActivateCode
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateActivateCode(
      int memberID, // primary key
      String memberActivateCode)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberActivateCode = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberActivateCode);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateActivateCode.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateAvatar() {
    return true;
  }
  /*
   * Included columns: MemberAvatar
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption,
   *                   MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberTimeZone, MemberSignature, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateAvatar(
      int memberID, // primary key
      String memberAvatar)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberAvatar = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberAvatar);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateAvatar.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateSignature() {
    return true;
  }
  /*
   * Included columns: MemberSignature
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption,
   *                   MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberTimeZone, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateSignature(
      int memberID, // primary key
      String memberSignature)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberSignature = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberSignature);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateSignature.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateTitle() {
    return true;
  }
  /*
   * Included columns: MemberTitle
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption,
   *                   MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateTitle(
      int memberID, // primary key
      String memberTitle)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberTitle = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setString(1, memberTitle);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateTitle.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateLastLogon() {
    return true;
  }
  /*
   * Included columns: MemberLastLogon
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateLastLogon(
      int memberID, // primary key
      Timestamp memberLastLogon,
      String memberLastIP)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberLastLogon = ?, MemberLastIP = ? ");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setTimestamp(1, memberLastLogon);
      statement.setString(2, memberLastIP);

      // primary key column(s)
      statement.setInt(3, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateLastLogon.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetPassword() {
    return true;
  }
  /*
   * Included columns: MemberPassword
   * Excluded columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public String getPassword(int memberID) throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberPassword");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where primary key = (" + memberID + ").");
      }

      String retValue = resultSet.getString("MemberPassword");
      if (retValue == null) {
        retValue = ""; // hack for Oracle database
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getPassword(pk).");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetTempPassword() {
    return true;
  }
  /*
   * Included columns: MemberTempPassword
   * Excluded columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
   *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
   *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
   *                   MemberActivateCode, MemberPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public String getTempPassword(int memberID) throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberTempPassword");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where primary key = (" + memberID + ").");
      }

      return StringUtil.getEmptyStringIfNull(resultSet.getString("MemberTempPassword"));
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getTempPassword(pk).");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetActivateCode() {
    return true;
  }

  /*
   * Included columns: MemberActivateCode
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public String getActivateCode(int memberID) throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberActivateCode");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where primary key = (" + memberID + ").");
      }

      String retValue = resultSet.getString("MemberActivateCode");
      if (retValue == null) {
        retValue = ""; // hack for Oracle database
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getActivateCode(pk).");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }
  //
  //    /*
  //     * Included columns: MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
  // MemberViewCount,
  //     *                   MemberPostCount, MemberCreationDate, MemberModifiedDate,
  // MemberLastLogon, MemberOption,
  //     *                   MemberStatus, MemberMessageCount, MemberMessageOption,
  // MemberPostsPerPage, MemberWarnCount,
  //     *                   MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
  // MemberTimeZone,
  //     *                   MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
  // MemberFirstname,
  //     *                   MemberLastname, MemberGender, MemberBirthday, MemberAddress,
  // MemberCity,
  //     *                   MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax,
  //     *                   MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq,
  //     *                   MemberMsn, MemberCoolLink1, MemberCoolLink2
  //     * As of 26 March:   MemberActivateCode
  //     * Excluded columns: MemberID, MemberPassword,  MemberFirstIP, MemberFirstEmail,
  //     *                   MemberTempPassword
  //     */
  //    public MemberBean getMember_forViewCurrentMember(int memberID)
  //        throws ObjectNotFoundException, DatabaseException {
  //
  //        Connection connection = null;
  //        PreparedStatement statement = null;
  //        ResultSet resultSet = null;
  //        StringBuffer sql = new StringBuffer(512);
  //        sql.append("SELECT MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
  // MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon,
  // MemberLastIP, MemberOption, MemberStatus, MemberMessageCount, MemberMessageOption,
  // MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
  // MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
  // MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity,
  // MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage,
  // MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2,
  // MemberActivateCode");
  //        sql.append(" FROM " + TABLE_NAME);
  //        sql.append(" WHERE MemberID = ?");
  //        try {
  //            connection = DBUtils.getConnection();
  //            statement = connection.prepareStatement(sql.toString());
  //            statement.setInt(1, memberID);
  //            resultSet = statement.executeQuery();
  //            if (!resultSet.next()) {
  //                throw new ObjectNotFoundException("Cannot find the row in table Member where
  // primary key = (" + memberID + ").");
  //            }
  //
  //            MemberBean bean = new MemberBean();
  //            // @todo: uncomment the following line(s) as needed
  //            bean.setMemberID(memberID);
  //            bean.setMemberName(resultSet.getString("MemberName"));
  //            bean.setMemberEmail(resultSet.getString("MemberEmail"));
  //            bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
  //            bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
  //            bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
  //            bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
  //            bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
  //            bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
  //            bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
  //            bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
  //            bean.setMemberOption(resultSet.getInt("MemberOption"));
  //            bean.setMemberStatus(resultSet.getInt("MemberStatus"));
  //            bean.setMemberMessageCount(resultSet.getInt("MemberMessageCount"));
  //            bean.setMemberMessageOption(resultSet.getInt("MemberMessageOption"));
  //            bean.setMemberPostsPerPage(resultSet.getInt("MemberPostsPerPage"));
  //            bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
  //            bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
  //            bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
  //            bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
  //            bean.setMemberTitle(resultSet.getString("MemberTitle"));
  //            bean.setMemberTimeZone(resultSet.getDouble("MemberTimeZone"));
  //            bean.setMemberSignature(resultSet.getString("MemberSignature"));
  //            bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
  //            bean.setMemberSkin(resultSet.getString("MemberSkin"));
  //            bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
  //            bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
  //            bean.setMemberLastname(resultSet.getString("MemberLastname"));
  //            bean.setMemberGender(resultSet.getInt("MemberGender"));
  //            bean.setMemberBirthday(resultSet.getDate("MemberBirthday"));
  //            bean.setMemberAddress(resultSet.getString("MemberAddress"));
  //            bean.setMemberCity(resultSet.getString("MemberCity"));
  //            bean.setMemberState(resultSet.getString("MemberState"));
  //            bean.setMemberCountry(resultSet.getString("MemberCountry"));
  //            bean.setMemberPhone(resultSet.getString("MemberPhone"));
  //            bean.setMemberMobile(resultSet.getString("MemberMobile"));
  //            bean.setMemberFax(resultSet.getString("MemberFax"));
  //            bean.setMemberCareer(resultSet.getString("MemberCareer"));
  //            bean.setMemberHomepage(resultSet.getString("MemberHomepage"));
  //            bean.setMemberYahoo(resultSet.getString("MemberYahoo"));
  //            bean.setMemberAol(resultSet.getString("MemberAol"));
  //            bean.setMemberIcq(resultSet.getString("MemberIcq"));
  //            bean.setMemberMsn(resultSet.getString("MemberMsn"));
  //            bean.setMemberCoolLink1(resultSet.getString("MemberCoolLink1"));
  //            bean.setMemberCoolLink2(resultSet.getString("MemberCoolLink2"));
  //            bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
  //            return bean;
  //        } catch(SQLException sqle) {
  //            log.error("Sql Execution Error!", sqle);
  //            throw new DatabaseException("Error executing SQL in
  // MemberDAOImplJDBC.getMember_forViewCurrentMember(pk).");
  //        } finally {
  //            DBUtils.closeResultSet(resultSet);
  //            DBUtils.closeStatement(statement);
  //            DBUtils.closeConnection(connection);
  //        }
  //    }
  //
  //    /*
  //     * Included columns: MemberEmailVisible, MemberNameVisible, MemberOption, MemberStatus,
  // MemberMessageOption,
  //     *                   MemberPostsPerPage, MemberTimeZone, MemberSkin, MemberLanguage,
  // MemberFirstname,
  //     *                   MemberLastname, MemberGender, MemberBirthday, MemberAddress,
  // MemberCity,
  //     *                   MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax,
  //     *                   MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq,
  //     *                   MemberMsn, MemberCoolLink1, MemberCoolLink2
  //     *   As of 13 Jan 2005: MemberName : this column is for show current member in viewmember in
  // Admin zone
  //     * Excluded columns: MemberID, MemberPassword, MemberFirstEmail, MemberEmail,
  //     *                   MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
  // MemberCreationDate,
  //     *                   MemberModifiedDate, MemberLastLogon, MemberActivateCode,
  // MemberTempPassword, MemberMessageCount,
  //     *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars,
  // MemberRewardPoints, MemberTitle,
  //     *                   MemberSignature, MemberAvatar
  //     */
  //    public MemberBean getMember_forEditCurrentMember(int memberID)
  //        throws ObjectNotFoundException, DatabaseException {
  //
  //        Connection connection = null;
  //        PreparedStatement statement = null;
  //        ResultSet resultSet = null;
  //        StringBuffer sql = new StringBuffer(512);
  //        sql.append("SELECT MemberName, MemberEmailVisible, MemberNameVisible, MemberOption,
  // MemberStatus, MemberMessageOption, MemberPostsPerPage, MemberTimeZone, MemberSkin,
  // MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
  // MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
  // MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1,
  // MemberCoolLink2");
  //        sql.append(" FROM " + TABLE_NAME);
  //        sql.append(" WHERE MemberID = ?");
  //        try {
  //            connection = DBUtils.getConnection();
  //            statement = connection.prepareStatement(sql.toString());
  //            statement.setInt(1, memberID);
  //            resultSet = statement.executeQuery();
  //            if (!resultSet.next()) {
  //                throw new ObjectNotFoundException("Cannot find the row in table Member where
  // primary key = (" + memberID + ").");
  //            }
  //
  //            MemberBean bean = new MemberBean();
  //            // @todo: uncomment the following line(s) as needed
  //            bean.setMemberID(memberID);
  //            bean.setMemberName(resultSet.getString("MemberName"));
  //            bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
  //            bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
  //            bean.setMemberOption(resultSet.getInt("MemberOption"));
  //            bean.setMemberStatus(resultSet.getInt("MemberStatus"));
  //            bean.setMemberMessageOption(resultSet.getInt("MemberMessageOption"));
  //            bean.setMemberPostsPerPage(resultSet.getInt("MemberPostsPerPage"));
  //            bean.setMemberTimeZone(resultSet.getDouble("MemberTimeZone"));
  //            bean.setMemberSkin(resultSet.getString("MemberSkin"));
  //            bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
  //            bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
  //            bean.setMemberLastname(resultSet.getString("MemberLastname"));
  //            bean.setMemberGender(resultSet.getInt("MemberGender"));
  //            bean.setMemberBirthday(resultSet.getDate("MemberBirthday"));
  //            bean.setMemberAddress(resultSet.getString("MemberAddress"));
  //            bean.setMemberCity(resultSet.getString("MemberCity"));
  //            bean.setMemberState(resultSet.getString("MemberState"));
  //            bean.setMemberCountry(resultSet.getString("MemberCountry"));
  //            bean.setMemberPhone(resultSet.getString("MemberPhone"));
  //            bean.setMemberMobile(resultSet.getString("MemberMobile"));
  //            bean.setMemberFax(resultSet.getString("MemberFax"));
  //            bean.setMemberCareer(resultSet.getString("MemberCareer"));
  //            bean.setMemberHomepage(resultSet.getString("MemberHomepage"));
  //            bean.setMemberYahoo(resultSet.getString("MemberYahoo"));
  //            bean.setMemberAol(resultSet.getString("MemberAol"));
  //            bean.setMemberIcq(resultSet.getString("MemberIcq"));
  //            bean.setMemberMsn(resultSet.getString("MemberMsn"));
  //            bean.setMemberCoolLink1(resultSet.getString("MemberCoolLink1"));
  //            bean.setMemberCoolLink2(resultSet.getString("MemberCoolLink2"));
  //            return bean;
  //        } catch(SQLException sqle) {
  //            log.error("Sql Execution Error!", sqle);
  //            throw new DatabaseException("Error executing SQL in
  // MemberDAOImplJDBC.getMember_forEditCurrentMember(pk).");
  //        } finally {
  //            DBUtils.closeResultSet(resultSet);
  //            DBUtils.closeStatement(statement);
  //            DBUtils.closeConnection(connection);
  //        }
  //    }

  @Override
  public boolean isSupportGetMember() {
    return true;
  }
  /*
   * Included columns: MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
   *                   MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname,
   *                   MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState,
   *                   MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
   *                   MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn,
   *                   MemberCoolLink1, MemberCoolLink2
   * As of 29 Jun 2003:    MemberActivateCode
   * As of 20 Oct 2004:    MemberExpireDate
   * As of 10 Dec 2004:    MemberFirstIP, MemberLastIP : for admin to view it
   * As of 4  Jan 2005:    MemberFirstEmail : for admin to view it
   * As of 4  Jan 2005:    MemberModifiedDate : for admin to view it
   * Excluded columns: MemberPassword,
   *                   MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberTimeZone, MemberSkin
   */
  @Override
  public MemberBean getMember(int memberID) throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberMessageOption, MemberPostsPerPage, MemberTimeZone, MemberSkin, MemberStatus, MemberMessageCount, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode, MemberFirstIP, MemberLastIP, MemberLanguage");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberID);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where primary key = (" + memberID + ").");
      }

      MemberBean bean = new MemberBean();
      // @todo: uncomment the following line(s) as needed
      // bean.setMemberID(memberID);
      bean.setMemberID(resultSet.getInt("MemberID"));
      bean.setMemberName(resultSet.getString("MemberName"));
      bean.setMemberFirstEmail(resultSet.getString("MemberFirstEmail"));
      bean.setMemberEmail(resultSet.getString("MemberEmail"));
      bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
      bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
      bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
      bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
      bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
      bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
      bean.setMemberExpireDate(resultSet.getTimestamp("MemberExpireDate"));
      bean.setMemberPasswordExpireDate(resultSet.getTimestamp("MemberPasswordExpireDate"));
      bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
      bean.setMemberOption(resultSet.getInt("MemberOption"));
      bean.setMemberStatus(resultSet.getInt("MemberStatus"));
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
      bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
      bean.setMemberLastname(resultSet.getString("MemberLastname"));
      bean.setMemberGender(resultSet.getInt("MemberGender"));
      bean.setMemberBirthday(resultSet.getDate("MemberBirthday"));
      bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
      bean.setMemberAddress(resultSet.getString("MemberAddress"));
      bean.setMemberCity(resultSet.getString("MemberCity"));
      bean.setMemberState(resultSet.getString("MemberState"));
      bean.setMemberCountry(resultSet.getString("MemberCountry"));
      bean.setMemberPhone(resultSet.getString("MemberPhone"));
      bean.setMemberMobile(resultSet.getString("MemberMobile"));
      bean.setMemberFax(resultSet.getString("MemberFax"));
      bean.setMemberCareer(resultSet.getString("MemberCareer"));
      bean.setMemberHomepage(resultSet.getString("MemberHomepage"));
      bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
      bean.setMemberFirstIP(resultSet.getString("MemberFirstIP"));
      bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
      return bean;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getMember(pk).");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetNumberOfMembers() {
    return true;
  }
  /**
   * Returns number of members in the database. Virtual guest is included.
   *
   * @return number of members
   * @throws DatabaseException
   */
  @Override
  public int getNumberOfMembers() throws DatabaseException {

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
      AssertionUtil.doAssert(
          resultSet.next(), "Assertion in MemberDAOImplJDBC.getNumberOfMembers.");
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getNumberOfMembers.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetNumberOfMembers_inMemberStatus() {
    return true;
  }
  /**
   * Returns number of members based on the memberStatus. Virtual guest is included.
   *
   * @return number of members, based on the memberStatus
   * @throws DatabaseException
   */
  @Override
  public int getNumberOfMembers_inMemberStatus(int memberStatus) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT Count(*)");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberStatus = ? ");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, memberStatus);
      resultSet = statement.executeQuery();
      AssertionUtil.doAssert(
          resultSet.next(), "Assertion in MemberDAOImplJDBC.getNumberOfMembers_inMemberStatus.");
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getNumberOfMembers_inMemberStatus.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetNumberOfMembers_inActivationStatus() {
    return true;
  }
  /**
   * Returns number of members based on the memberActivateCode. Virtual guest is included.
   *
   * @return number of members, based on the memberActivateCode
   * @throws DatabaseException
   */
  @Override
  public int getNumberOfMembers_inActivationStatus(boolean activated) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT Count(*)");
    sql.append(" FROM " + TABLE_NAME);
    if (activated) {
      sql.append(" WHERE MemberActivateCode = 'activated' ");
    } else {
      // use IS NULL to fix problem of Oracle (Thanks sssimon)
      sql.append(" WHERE (MemberActivateCode <> 'activated') OR (MemberActivateCode IS NULL) ");
    }
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      resultSet = statement.executeQuery();
      AssertionUtil.doAssert(
          resultSet.next(),
          "Assertion in MemberDAOImplJDBC.getNumberOfMembers_inActivationStatus.");
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getNumberOfMembers_inActivationStatus.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  /**
   * ********************************************** Customized methods come below
   * **********************************************
   */
  @Override
  public boolean isSupportGetMemberIDFromMemberName() {
    return true;
  }

  @Override
  public final int getMemberIDFromMemberName(String memberName)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT MemberID");
    sql.append(" FROM " + TABLE_NAME);
    if (DBUtils.isCaseSensitiveDatebase()) {
      sql.append(" WHERE lower(MemberName) = lower(?)");
    } else {
      sql.append(" WHERE MemberName = ?");
    }
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setString(1, memberName);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where MemberName = " + memberName);
      }
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getMemberIDFromMemberName.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMemberIDFromMemberEmail() {
    return true;
  }
  /* @todo check if this method work with other DBMS other than MySql (check case-sensitive) */
  @Override
  public final int getMemberIDFromMemberEmail(String memberEmail)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = "SELECT MemberID FROM " + TABLE_NAME + " WHERE MemberEmail = ?";
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setString(1, memberEmail);
      resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        throw new ObjectNotFoundException(
            "Cannot find the row in table Member where MemberEmail = " + memberEmail);
      }
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getMemberIDFromMemberEmail.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMembers_withSortSupport_limit() {
    return true;
  }

  @Override
  public Collection getMembers_withSortSupport_limit(
      int offset, int rowsToReturn, String sort, String order, int memberStatus)
      throws IllegalArgumentException, DatabaseException {
    if (DBUtils.getDatabaseType() == DBUtils.DATABASE_MYSQL) {
      return getBeans_withSortSupport_limit_mysql(offset, rowsToReturn, sort, order, memberStatus);
    } else if (DBUtils.getDatabaseType() == DBUtils.DATABASE_NOSCROLL) {
      return getBeans_withSortSupport_limit_noscroll(
          offset, rowsToReturn, sort, order, memberStatus);
    }
    return getBeans_withSortSupport_limit_general(offset, rowsToReturn, sort, order, memberStatus);
  }

  /*
   * Included columns: MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
   *                   MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname,
   *                   MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState,
   *                   MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
   *                   MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn,
   *                   MemberCoolLink1, MemberCoolLink2
   *   as of 10 March, add MemberActivateCode
   *   as of 19 Nov 2007, add MemberLanguage
   * Excluded columns: MemberPassword, MemberFirstEmail, MemberFirstIP, MemberLastIP, MemberModifiedDate,
   *                   MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberTimeZone, MemberSkin
   */
  /**
   * This method support sorting and for PUBLIC view
   *
   * @param memberStatus TODO
   */
  /* @todo fix bug that cannot prepare sort and order */
  private Collection getBeans_withSortSupport_limit_mysql(
      int offset, int rowsToReturn, String sort, String order, int memberStatus)
      throws IllegalArgumentException, DatabaseException {

    if (offset < 0) {
      throw new IllegalArgumentException("The offset < 0 is not allowed.");
    }
    if (rowsToReturn <= 0) {
      throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
    }
    /*
     * MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
     * MemberTitle,
     * MemberCity, MemberState,
     * MemberCountry, MemberCareer,
     */
    if ((!"MemberID".equals(sort))
        && (!"MemberName".equals(sort))
        && (!"MemberFirstname".equals(sort))
        && (!"MemberLastname".equals(sort))
        && (!"MemberGender".equals(sort))
        && (!"MemberBirthday".equals(sort))
        && (!"MemberCreationDate".equals(sort))
        && (!"MemberLastLogon".equals(sort))
        && (!"MemberViewCount".equals(sort))
        && (!"MemberPostCount".equals(sort))
        && (!"MemberCountry".equals(sort))
        && (!"MemberEmail".equals(sort))
        && (!"MemberHomepage".equals(sort))
        && (!"MemberStatus".equals(sort))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the criteria '" + sort + "'.");
    }

    if ((!"ASC".equals(order)) && (!"DESC".equals(order))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the order '" + order + "'.");
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberFirstEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode, MemberLanguage");
    sql.append(" FROM " + TABLE_NAME);
    if (memberStatus == MemberDAO.ALL_MEMBER_STATUS) {
      // do nothing
    } else if (memberStatus == MemberBean.MEMBER_STATUS_PENDING) {
      sql.append(" WHERE (MemberStatus = " + memberStatus + ")");
    } else {
      throw new IllegalArgumentException("Cannot process MemberStatus = " + memberStatus);
    }
    sql.append(" ORDER BY " + sort + " " + order); // ColumnName, ASC|DESC
    sql.append(" LIMIT ?, ?");

    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, offset);
      statement.setInt(2, rowsToReturn);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberFirstEmail(resultSet.getString("MemberFirstEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberFirstIP(resultSet.getString("MemberFirstIP"));
        bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getBeans_withSortSupport_limit_mysql.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  /*
   * Included columns: MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
   *                   MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname,
   *                   MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState,
   *                   MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
   *                   MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn,
   *                   MemberCoolLink1, MemberCoolLink2
   *   as of 10 March, add MemberActivateCode
   * Excluded columns: MemberPassword, MemberFirstEmail, MemberFirstIP, MemberLastIP, MemberModifiedDate,
   *                   MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberTimeZone, MemberSkin, MemberLanguage
   */
  /**
   * This method support sorting and for PUBLIC view
   *
   * @param memberStatus TODO
   */
  /* @todo fix bug that cannot prepare sort and order */
  private Collection getBeans_withSortSupport_limit_noscroll(
      int offset, int rowsToReturn, String sort, String order, int memberStatus)
      throws IllegalArgumentException, DatabaseException {

    if (offset < 0) {
      throw new IllegalArgumentException("The offset < 0 is not allowed.");
    }
    if (rowsToReturn <= 0) {
      throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
    }
    /*
     * MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
     * MemberTitle,
     * MemberCity, MemberState,
     * MemberCountry, MemberCareer,
     */
    if ((!"MemberID".equals(sort))
        && (!"MemberName".equals(sort))
        && (!"MemberFirstname".equals(sort))
        && (!"MemberLastname".equals(sort))
        && (!"MemberGender".equals(sort))
        && (!"MemberBirthday".equals(sort))
        && (!"MemberCreationDate".equals(sort))
        && (!"MemberLastLogon".equals(sort))
        && (!"MemberViewCount".equals(sort))
        && (!"MemberPostCount".equals(sort))
        && (!"MemberCountry".equals(sort))
        && (!"MemberEmai".equals(sort))
        && (!"MemberHomepage".equals(sort))
        && (!"MemberStatus".equals(sort))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the criteria '" + sort + "'.");
    }

    if ((!"ASC".equals(order)) && (!"DESC".equals(order))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the order '" + order + "'.");
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberFirstEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode, MemberLanguage");
    sql.append(" FROM " + TABLE_NAME);
    if (memberStatus == MemberDAO.ALL_MEMBER_STATUS) {
      // do nothing
    } else if (memberStatus == MemberBean.MEMBER_STATUS_PENDING) {
      sql.append(" WHERE (MemberStatus = " + memberStatus + ")");
    } else {
      throw new IllegalArgumentException("Cannot process MemberStatus = " + memberStatus);
    }
    sql.append(" ORDER BY " + sort + " " + order); // ColumnName, ASC|DESC

    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setMaxRows(offset + rowsToReturn);
      resultSet = statement.executeQuery();
      int rowIndex = -1;
      while (resultSet.next()) {
        rowIndex++;
        if (rowIndex < offset) {
          continue;
        }
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberFirstEmail(resultSet.getString("MemberFirstEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberFirstIP(resultSet.getString("MemberFirstIP"));
        bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
        retValue.add(bean);
        if (retValue.size() == rowsToReturn) {
          break; // Fix the Sybase bug
        }
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getBeans_withSortSupport_limit_noscroll.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  /*
   * Included columns: MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
   *                   MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname,
   *                   MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState,
   *                   MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
   *                   MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn,
   *                   MemberCoolLink1, MemberCoolLink2
   *   as of 10 March, add MemberActivateCode
   * Excluded columns: MemberPassword, MemberFirstEmail, MemberFirstIP, MemberLastIP, MemberModifiedDate,
   *                   MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberTimeZone, MemberSkin, MemberLanguage
   */
  /**
   * This method support sorting and for PUBLIC view
   *
   * @param memberStatus TODO
   */
  /* @todo fix bug that cannot prepare sort and order */
  private Collection getBeans_withSortSupport_limit_general(
      int offset, int rowsToReturn, String sort, String order, int memberStatus)
      throws IllegalArgumentException, DatabaseException {

    if (offset < 0) {
      throw new IllegalArgumentException("The offset < 0 is not allowed.");
    }
    if (rowsToReturn <= 0) {
      throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
    }
    /*
     * MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
     * MemberTitle,
     * MemberCity, MemberState,
     * MemberCountry, MemberCareer,
     */
    if ((!"MemberID".equals(sort))
        && (!"MemberName".equals(sort))
        && (!"MemberFirstname".equals(sort))
        && (!"MemberLastname".equals(sort))
        && (!"MemberGender".equals(sort))
        && (!"MemberBirthday".equals(sort))
        && (!"MemberCreationDate".equals(sort))
        && (!"MemberLastLogon".equals(sort))
        && (!"MemberViewCount".equals(sort))
        && (!"MemberPostCount".equals(sort))
        && (!"MemberCountry".equals(sort))
        && (!"MemberEmail".equals(sort))
        && (!"MemberHomepage".equals(sort))
        && (!"MemberStatus".equals(sort))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the criteria '" + sort + "'.");
    }

    if ((!"ASC".equals(order)) && (!"DESC".equals(order))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the order '" + order + "'.");
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberFirstEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode, MemberLanguage");
    sql.append(" FROM " + TABLE_NAME);
    if (memberStatus == MemberDAO.ALL_MEMBER_STATUS) {
      // do nothing
    } else if (memberStatus == MemberBean.MEMBER_STATUS_PENDING) {
      sql.append(" WHERE (MemberStatus = " + memberStatus + ")");
    } else {
      throw new IllegalArgumentException("Cannot process MemberStatus = " + memberStatus);
    }
    sql.append(" ORDER BY " + sort + " " + order); // ColumnName, ASC|DESC

    try {
      connection = DBUtils.getConnection();
      statement =
          connection.prepareStatement(
              sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      statement.setMaxRows(offset + rowsToReturn);
      try {
        statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
      } catch (SQLException sqle) {
        // do nothing, postgreSQL does not support this method
      }
      resultSet = statement.executeQuery();
      boolean loop =
          resultSet.absolute(
              offset + 1); // the absolute method begin with 1 instead of 0 as in the LIMIT clause
      while (loop) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberFirstEmail(resultSet.getString("MemberFirstEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberFirstIP(resultSet.getString("MemberFirstIP"));
        bean.setMemberLastIP(resultSet.getString("MemberLastIP"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberModifiedDate(resultSet.getTimestamp("MemberModifiedDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        bean.setMemberLanguage(resultSet.getString("MemberLanguage"));
        retValue.add(bean);
        if (retValue.size() == rowsToReturn) {
          break; // Fix the Sybase bug
        }
        loop = resultSet.next();
      } // while
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getBeans_withSortSupport_limit_general.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  /*
   * Included columns: MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible,
   *                   MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption,
   *                   MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
   *                   MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname,
   *                   MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState,
   *                   MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer,
   *                   MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn,
   *                   MemberCoolLink1, MemberCoolLink2
   *   as of 10 March, add MemberActivateCode
   * Excluded columns: MemberPassword, MemberFirstEmail, MemberFirstIP, MemberLastIP, MemberModifiedDate,
   *                   MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberTimeZone, MemberSkin, MemberLanguage
   */
  @Override
  public boolean isSupportGetEnableMembers_inActivationStatus() {
    return true;
  }
  /** This method support sorting and for PUBLIC view */
  @Override
  public Collection getEnableMembers_inActivationStatus(String kind)
      throws IllegalArgumentException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);

    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode");
    sql.append(" FROM " + TABLE_NAME);
    if ("activated".equals(kind)) {
      sql.append(
          " WHERE (MemberStatus = "
              + MemberBean.MEMBER_STATUS_ENABLE
              + ") AND (MemberActivateCode = 'activated') ");
    } else if ("nonactivated".equals(kind)) {
      // use IS NULL to fix problem of Oracle (Thanks sssimon)
      sql.append(
          " WHERE (MemberStatus = "
              + MemberBean.MEMBER_STATUS_ENABLE
              + ") AND ((MemberActivateCode <> 'activated') OR (MemberActivateCode IS NULL))");
    } else if ("all".equals(kind)) {
      sql.append(" WHERE (MemberStatus = " + MemberBean.MEMBER_STATUS_ENABLE + ")");
    } else {
      throw new IllegalArgumentException("Cannot process activation kind = " + kind);
    }

    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        retValue.add(bean);
      } // while
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getEnableMembers_inActivationStatus.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateStatus() {
    return true;
  }
  /*
   * Included columns: MemberStatus
   * Excluded columns: MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail,
   *                   MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount,
   *                   MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption,
   *                   MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,
   *                   MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle,
   *                   MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage,
   *                   MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress,
   *                   MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile,
   *                   MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol,
   *                   MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2
   */
  @Override
  public void updateStatus(
      int memberID, // primary key
      int memberStatus)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberStatus = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setInt(1, memberStatus);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updateStatus.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdatePostCount() {
    return true;
  }

  @Override
  public void updatePostCount(
      int memberID, // primary key
      int memberPostCount)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberPostCount = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setInt(1, memberPostCount);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.updatePostCount.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportIncreaseViewCount() {
    return true;
  }
  /** This method should be call only when we can make sure that memberID is in database */
  @Override
  public void increaseViewCount(int memberID) throws DatabaseException, ObjectNotFoundException {

    Connection connection = null;
    PreparedStatement statement = null;
    String sql =
        "UPDATE " + TABLE_NAME + " SET MemberViewCount = MemberViewCount + 1 WHERE MemberID = ?";
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setInt(1, memberID);
      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update the MemberViewCount in table Member. Please contact Web site Administrator.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.increaseViewCount.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportIncreasePostCount() {
    return true;
  }
  /** This method should be call only when we can make sure that memberID is in database */
  @Override
  public void increasePostCount(int memberID) throws DatabaseException, ObjectNotFoundException {

    Connection connection = null;
    PreparedStatement statement = null;
    String sql =
        "UPDATE " + TABLE_NAME + " SET MemberPostCount = MemberPostCount + 1 WHERE MemberID = ?";
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setInt(1, memberID);
      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update the MemberPostCount in table Member. Please contact Web site Administrator.");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.increasePostCount.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMembers_inExpire_limit() {
    return true;
  }

  @Override
  public Collection getMembers_inExpire_limit(
      Timestamp expireDate, int offset, int rowsToReturn, String sort, String order)
      throws IllegalArgumentException, DatabaseException {

    if (offset < 0) {
      throw new IllegalArgumentException("The offset < 0 is not allowed.");
    }
    if (rowsToReturn <= 0) {
      throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
    }

    /*
     * MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,
     * MemberTitle,
     * MemberCity, MemberState,
     * MemberCountry, MemberCareer,
     */
    if ((!"MemberID".equals(sort))
        && (!"MemberName".equals(sort))
        && (!"MemberFirstname".equals(sort))
        && (!"MemberLastname".equals(sort))
        && (!"MemberGender".equals(sort))
        && (!"MemberBirthday".equals(sort))
        && (!"MemberCreationDate".equals(sort))
        && (!"MemberExpireDate".equals(sort))
        && (!"MemberLastLogon".equals(sort))
        && (!"MemberViewCount".equals(sort))
        && (!"MemberPostCount".equals(sort))
        && (!"MemberStatus".equals(sort))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the criteria '" + sort + "'.");
    }

    if ((!"ASC".equals(order)) && (!"DESC".equals(order))) {
      throw new IllegalArgumentException(
          "Cannot sort, reason: don't understand the order '" + order + "'.");
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MemberID, MemberName, MemberCreationDate, MemberExpireDate, MemberStatus");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberExpireDate <= ? OR MemberExpireDate IS NULL");
    sql.append(" ORDER BY " + sort + " " + order); // ColumnName, ASC|DESC
    try {
      connection = DBUtils.getConnection();
      statement =
          connection.prepareStatement(
              sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      statement.setMaxRows(offset + rowsToReturn);
      statement.setTimestamp(1, expireDate);
      try {
        statement.setFetchSize(Math.min(rowsToReturn, DBUtils.MAX_FETCH_SIZE));
      } catch (SQLException sqle) {
        // do nothing, postgreSQL does not support this method
      }
      resultSet = statement.executeQuery();
      boolean loop =
          resultSet.absolute(
              offset + 1); // the absolute method begin with 1 instead of 0 as in the LIMIT clause
      while (loop) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberExpireDate(resultSet.getTimestamp("MemberExpireDate"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        retValue.add(bean);
        if (retValue.size() == rowsToReturn) {
          break; // Fix the Sybase bug
        }
        loop = resultSet.next();
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getMembers_inExpire_limit.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetNumberOfMembers_inExpire() {
    return true;
  }

  @Override
  public int getNumberOfMembers_inExpire(Timestamp expireDate) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT Count(*)");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE MemberExpireDate <= ? OR MemberExpireDate IS NULL");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setTimestamp(1, expireDate);
      resultSet = statement.executeQuery();
      AssertionUtil.doAssert(
          resultSet.next(), "Assertion in MemberDAOImplJDBC.getNumberOfMembers_inExpire.");
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getNumberOfMembers_inExpire.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportUpdateMember_expireDate() {
    return true;
  }

  @Override
  public void updateMember_expireDate(
      int memberID, // primary key
      Timestamp expireDate)
      throws ObjectNotFoundException, DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("UPDATE " + TABLE_NAME + " SET MemberExpireDate = ?");
    sql.append(" WHERE MemberID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());

      // // column(s) to update
      statement.setTimestamp(1, expireDate);

      // primary key column(s)
      statement.setInt(2, memberID);

      if (statement.executeUpdate() != 1) {
        throw new ObjectNotFoundException(
            "Cannot update table Member where primary key = (" + memberID + ").");
      }
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.updateMember_expireDate.");
    } finally {
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMembers() {
    return true;
  }

  @Override
  public Collection getMembers() throws DatabaseException {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode");
    sql.append(" FROM " + TABLE_NAME);

    try {
      connection = DBUtils.getConnection();
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sql.toString());
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getMembers.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMaxMemberID() {
    return true;
  }

  @Override
  public int getMaxMemberID() throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    StringBuffer sql = new StringBuffer(512);
    sql.append("SELECT MAX(MemberID)");
    sql.append(" FROM " + TABLE_NAME);
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      resultSet = statement.executeQuery();
      AssertionUtil.doAssert(resultSet.next(), "Assertion in MemberDAOImplJDBC.getMaxMemberID.");
      return resultSet.getInt(1);
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getMaxMemberID.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetMembersFromIDRange() {
    return true;
  }

  @Override
  public Collection getMembers_fromIDRange(int fromID, int toID)
      throws IllegalArgumentException, DatabaseException {

    if (fromID < 0) {
      throw new IllegalArgumentException("The fromID < 0 is not allowed.");
    }
    if (toID < fromID) {
      throw new IllegalArgumentException("toID < fromID is not allowed.");
    }

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);
    sql.append(
        "SELECT MemberID, MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(" WHERE (MemberID >= ?) AND (MemberID <= ?)");
    sql.append(" ORDER BY MemberID ASC ");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, fromID);
      statement.setInt(2, toID);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getMembers_fromIDRange.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetEnableMembers_inGroup() {
    return true;
  }

  @Override
  public Collection getEnableMembers_inGroup(int groupID) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);

    sql.append(
        "SELECT m.MemberID, m.MemberName, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberViewCount, MemberPostCount, MemberCreationDate, MemberLastLogon, MemberOption, MemberStatus, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberSignature, MemberAvatar, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberActivateCode");
    sql.append(" FROM " + TABLE_NAME + " m , " + MemberGroupDAO.TABLE_NAME + " mg");
    sql.append(
        " WHERE (MemberStatus = "
            + MemberBean.MEMBER_STATUS_ENABLE
            + ") AND (mg.MemberID = m.MemberID) AND GroupID = ?");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setInt(1, groupID);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberEmailVisible(resultSet.getInt("MemberEmailVisible"));
        bean.setMemberNameVisible(resultSet.getInt("MemberNameVisible"));
        bean.setMemberViewCount(resultSet.getInt("MemberViewCount"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberLastLogon(resultSet.getTimestamp("MemberLastLogon"));
        bean.setMemberOption(resultSet.getInt("MemberOption"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        bean.setMemberWarnCount(resultSet.getInt("MemberWarnCount"));
        bean.setMemberVoteCount(resultSet.getInt("MemberVoteCount"));
        bean.setMemberVoteTotalStars(resultSet.getInt("MemberVoteTotalStars"));
        bean.setMemberRewardPoints(resultSet.getInt("MemberRewardPoints"));
        bean.setMemberTitle(resultSet.getString("MemberTitle"));
        bean.setMemberSignature(resultSet.getString("MemberSignature"));
        bean.setMemberAvatar(resultSet.getString("MemberAvatar"));
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
        bean.setMemberActivateCode(resultSet.getString("MemberActivateCode"));
        retValue.add(bean);
      } // while
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getEnableMembers_inGroup.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetForumsAuthorizedMembers() {
    return true;
  }

  @Override
  public Collection getForumsAuthorizedMembers() throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);

    sql.append(
        "SELECT DISTINCT m.MemberID, MemberName, MemberEmail, MemberLastname, MemberFirstname, MemberCreationDate");
    sql.append(" FROM " + TABLE_NAME + " m , " + MemberForumDAO.TABLE_NAME + " mf");
    sql.append(" WHERE (m.MemberID = mf.MemberID) ");
    sql.append(" ORDER BY m.MemberID ASC");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberLastname(resultSet.getString("MemberLastname"));
        bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getForumsAuthorizedMembers");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetAuthorizedMembers() {
    return true;
  }

  @Override
  public Collection getAuthorizedMembers() throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);

    sql.append(
        "SELECT DISTINCT m.MemberID, MemberName, MemberEmail, MemberLastname, MemberFirstname, MemberCreationDate");
    sql.append(" FROM " + TABLE_NAME + " m , " + MemberPermissionDAO.TABLE_NAME + " mp");
    sql.append(" WHERE (m.MemberID = mp.MemberID) ");
    sql.append(" ORDER BY m.MemberID ASC");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberLastname(resultSet.getString("MemberLastname"));
        bean.setMemberFirstname(resultSet.getString("MemberFirstname"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException("Error executing SQL in MemberDAOImplJDBC.getAuthorizedMembers.");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }

  @Override
  public boolean isSupportGetNonActivatedNoPostMembers() {
    return true;
  }

  @Override
  public Collection getNonActivatedNoPostMembers(Timestamp before) throws DatabaseException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Collection retValue = new ArrayList();
    StringBuffer sql = new StringBuffer(512);

    sql.append(
        "SELECT MemberID, MemberName, MemberFirstName, MemberLastName, MemberEmail, MemberPostCount, MemberCreationDate, MemberStatus");
    sql.append(" FROM " + TABLE_NAME);
    sql.append(
        " WHERE ((MemberActivateCode <> 'activated') OR (MemberActivateCode IS NULL))"); // not
    // activated
    sql.append(" AND (MemberID <> 0) AND (MemberID <> 1) "); // not a guest, not Admin
    sql.append(" AND (MemberPostCount = 0) "); // with no posts
    sql.append(" AND (MemberCreationDate < ?) ");
    try {
      connection = DBUtils.getConnection();
      statement = connection.prepareStatement(sql.toString());
      statement.setTimestamp(1, before);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        MemberBean bean = new MemberBean();
        bean.setMemberID(resultSet.getInt("MemberID"));
        bean.setMemberName(resultSet.getString("MemberName"));
        bean.setMemberFirstname(resultSet.getString("MemberFirstName"));
        bean.setMemberLastname(resultSet.getString("MemberLastName"));
        bean.setMemberEmail(resultSet.getString("MemberEmail"));
        bean.setMemberPostCount(resultSet.getInt("MemberPostCount"));
        bean.setMemberCreationDate(resultSet.getTimestamp("MemberCreationDate"));
        bean.setMemberStatus(resultSet.getInt("MemberStatus"));
        retValue.add(bean);
      }
      return retValue;
    } catch (SQLException sqle) {
      log.error("Sql Execution Error!", sqle);
      throw new DatabaseException(
          "Error executing SQL in MemberDAOImplJDBC.getNonActivatedNoPostMembers");
    } finally {
      DBUtils.closeResultSet(resultSet);
      DBUtils.resetStatement(statement);
      DBUtils.closeStatement(statement);
      DBUtils.closeConnection(connection);
    }
  }
} // end of class MemberDAOImplJDBC
