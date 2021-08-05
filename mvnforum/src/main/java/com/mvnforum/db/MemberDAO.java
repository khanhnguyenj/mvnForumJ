/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MemberDAO.java,v 1.56 2008/11/03 03:01:45 nguyendnc Exp $
 * $Author: nguyendnc $
 * $Revision: 1.56 $
 * $Date: 2008/11/03 03:01:45 $
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
 * @author: Luis Miguel Hernanz
 * @author: Minh Nguyen
 */
package com.mvnforum.db;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

/**
 * Interface to access to the user data.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision: 1.56 $
 */
public interface MemberDAO {

  public static final String TABLE_NAME = DatabaseConfig.USER_TABLE_PREFIX + "Member";

  public static final int ALL_MEMBER_STATUS = -1;
  public static final int ALL_OFFSET = 0;

  public boolean isSupportFindByPrimaryKey();

  public void findByPrimaryKey(int memberID) throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportFindByPrimaryKey2();

  public void findByPrimaryKey2(int memberID, String memberName)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportFindByAlternateKey_MemberName();

  public String findByAlternateKey_MemberName(String memberName)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportFindByAlternateKey_MemberEmail();

  public void findByAlternateKey_MemberEmail(String memberEmail)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportDeleteByPrimaryKey();

  public void deleteByPrimaryKey(int memberID) throws DatabaseException;

  public boolean isSupportCreate();

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
      throws CreateException, DatabaseException, DuplicateKeyException;

  public boolean isSupportUpdate();

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
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateEmail();

  public void updateEmail(
      int memberID, // primary key
      String memberEmail)
      throws ObjectNotFoundException, DatabaseException, DuplicateKeyException;

  public boolean isSupportUpdatePassword();

  public void updatePassword(
      int memberID, // primary key
      String memberPassword,
      Timestamp memberPasswordExpireDate)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateTempPassword();

  public void updateTempPassword(
      int memberID, // primary key
      String memberTempPassword)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateActivateCode();

  public void updateActivateCode(
      int memberID, // primary key
      String memberActivateCode)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateAvatar();

  public void updateAvatar(
      int memberID, // primary key
      String memberAvatar)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateSignature();

  public void updateSignature(
      int memberID, // primary key
      String memberSignature)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateTitle();

  public void updateTitle(
      int memberID, // primary key
      String memberTitle)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdateLastLogon();

  public void updateLastLogon(
      int memberID, // primary key
      Timestamp memberLastLogon,
      String lastIP)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetPassword();

  public String getPassword(int memberID) throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetTempPassword();

  public String getTempPassword(int memberID) throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetActivateCode();

  public String getActivateCode(int memberID) throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetMember();

  public MemberBean getMember(int memberID) throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetNumberOfMembers();

  public int getNumberOfMembers() throws DatabaseException;

  public boolean isSupportGetNumberOfMembers_inMemberStatus();

  public int getNumberOfMembers_inMemberStatus(int memberStatus) throws DatabaseException;

  public boolean isSupportGetNumberOfMembers_inActivationStatus();

  public int getNumberOfMembers_inActivationStatus(boolean activationStatus)
      throws DatabaseException;

  /**
   * ********************************************** Customized methods come below
   * **********************************************
   */
  public boolean isSupportGetMemberIDFromMemberName();

  public int getMemberIDFromMemberName(String memberName)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetMemberIDFromMemberEmail();
  /* @todo check if this method work with other DBMS other than MySql (check case-sensitive) */
  public int getMemberIDFromMemberEmail(String memberEmail)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetMembers_withSortSupport_limit();

  public Collection getMembers_withSortSupport_limit(
      int offset, int rowsToReturn, String sort, String order, int memberStatus)
      throws IllegalArgumentException, DatabaseException;

  public boolean isSupportGetEnableMembers_inActivationStatus();
  /**
   * Get all members that based on the activation status criteria
   *
   * @param kind String can be one of three values: all, activated, nonactivated
   * @throws DatabaseException
   * @return Collection
   */
  public Collection getEnableMembers_inActivationStatus(String kind) throws DatabaseException;

  public boolean isSupportGetMaxMemberID();

  public int getMaxMemberID() throws DatabaseException;

  public boolean isSupportGetMembersFromIDRange();

  public Collection getMembers_fromIDRange(int fromID, int toID)
      throws IllegalArgumentException, DatabaseException;

  public boolean isSupportUpdateStatus();

  public void updateStatus(
      int memberID, // primary key
      int memberStatus)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportUpdatePostCount();

  public void updatePostCount(
      int memberID, // primary key
      int memberPostCount)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportIncreaseViewCount();
  /** This method should be call only when we can make sure that memberID is in database */
  public void increaseViewCount(int memberID) throws DatabaseException, ObjectNotFoundException;

  public boolean isSupportIncreasePostCount();
  /** This method should be call only when we can make sure that memberID is in database */
  public void increasePostCount(int memberID) throws DatabaseException, ObjectNotFoundException;

  public boolean isSupportGetMembers_inExpire_limit();

  public Collection getMembers_inExpire_limit(
      Timestamp expireDate, int offset, int rowsToReturn, String sort, String order)
      throws IllegalArgumentException, DatabaseException;

  public boolean isSupportGetNumberOfMembers_inExpire();

  public int getNumberOfMembers_inExpire(Timestamp expireDate) throws DatabaseException;

  public boolean isSupportUpdateMember_expireDate();

  public void updateMember_expireDate(
      int memberID, // primary key
      Timestamp expireDate)
      throws ObjectNotFoundException, DatabaseException;

  public boolean isSupportGetMembers();

  public Collection getMembers() throws DatabaseException;

  public boolean isSupportGetEnableMembers_inGroup();

  public Collection getEnableMembers_inGroup(int groupID) throws DatabaseException;

  public boolean isSupportGetForumsAuthorizedMembers();

  public Collection getForumsAuthorizedMembers() throws DatabaseException;

  public boolean isSupportGetAuthorizedMembers();

  public Collection getAuthorizedMembers() throws DatabaseException;

  public boolean isSupportGetNonActivatedNoPostMembers();

  public Collection getNonActivatedNoPostMembers(Timestamp before) throws DatabaseException;
}
