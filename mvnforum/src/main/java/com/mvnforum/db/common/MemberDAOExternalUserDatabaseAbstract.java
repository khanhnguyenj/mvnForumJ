/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/common/MemberDAOExternalUserDatabaseAbstract.java,v 1.24 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.24 $
 * $Date: 2009/01/12 15:02:34 $
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
 * @author: Phong Ta
 */
package com.mvnforum.db.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberDAO;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

public class MemberDAOExternalUserDatabaseAbstract implements MemberDAO {

    private static final Logger log = LoggerFactory.getLogger(MemberDAOExternalUserDatabaseAbstract.class);

    protected MemberDAO memberDAO = DAOFactory.getLocalMemberDAO();

    @Override
    public boolean isSupportCreate() {
        return true;
    }
    // To create forum-account if this is the first time the existing member go to the forum in portal
    @Override
    public void create(String memberName, String memberPassword, String memberFirstEmail, String memberEmail, int memberEmailVisible, int memberNameVisible, String memberFirstIP, String memberLastIP, int memberViewCount, int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, Timestamp memberExpireDate, Timestamp memberPasswordExpireDate, Timestamp memberLastLogon, int memberOption, int memberStatus, String memberActivateCode, String memberTempPassword, int memberMessageCount, int memberMessageOption, int memberPostsPerPage, int memberWarnCount, int memberVoteCount, int memberVoteTotalStars, int memberRewardPoints, String memberTitle, double memberTimeZone, String memberSignature, String memberAvatar, String memberSkin, String memberLanguage, String memberFirstname, String memberLastname, int memberGender, Date memberBirthday, String memberAddress, String memberCity, String memberState, String memberCountry, String memberPhone, String memberMobile, String memberFax, String memberCareer, String memberHomepage)
        throws CreateException, DatabaseException, DuplicateKeyException {

        memberDAO.create(memberName, memberPassword, memberFirstEmail, memberEmail, memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount, memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberPasswordExpireDate, memberLastLogon, memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount, memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount, memberVoteTotalStars, memberRewardPoints, memberTitle, memberTimeZone, memberSignature, memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile, memberFax, memberCareer, memberHomepage);
    }

    public final String getNameFromID(int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT MemberName FROM " + TABLE_NAME + " WHERE MemberID = ?";
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, memberID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find the row in table Member where MemberName = " + memberID);
            }
            return resultSet.getString(1);
        } catch(SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in MemberDAOExternalUserDatabaseAbstract.getMemberIDFromMemberName.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    @Override
    public boolean isSupportDeleteByPrimaryKey() {
        return false;
    }
    @Override
    public void deleteByPrimaryKey(int memberID) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportFindByAlternateKey_MemberEmail() {
        return false;
    }
    @Override
    public void findByAlternateKey_MemberEmail(String memberEmail) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportFindByAlternateKey_MemberName() {
        return true;
    }
    // check reference to the other tables in forum portlet
    @Override
    public String findByAlternateKey_MemberName(String memberName) throws ObjectNotFoundException, DatabaseException {
        return memberDAO.findByAlternateKey_MemberName(memberName);
    }

    @Override
    public boolean isSupportFindByPrimaryKey() {
        return true;
    }
    // check reference to the other tables in forum portlet
    @Override
    public void findByPrimaryKey(int memberID) throws ObjectNotFoundException, DatabaseException {
        memberDAO.findByPrimaryKey(memberID);
    }

    @Override
    public boolean isSupportFindByPrimaryKey2() {
        return true;
    }
    // check reference to the other tables in forum portlet
    @Override
    public void findByPrimaryKey2(int memberID, String memberName) throws ObjectNotFoundException, DatabaseException {
        memberDAO.findByPrimaryKey2(memberID, memberName);
    }

    @Override
    public boolean isSupportGetActivateCode() {
        return true;
    }
    // need in OnlineUserFactoryImpl to check for user is activated or not
    @Override
    public String getActivateCode(int memberID) throws ObjectNotFoundException, DatabaseException {
        return memberDAO.getActivateCode(memberID);
        //throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetAuthorizedMembers() {
        return false;
    }
    @Override
    public Collection getAuthorizedMembers() throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetEnableMembers_inActivationStatus() {
        return false;
    }
    // need in GeneralAdminTasksWebHandler.sendMail() to send email to all activation members
    @Override
    public Collection getEnableMembers_inActivationStatus(String kind) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetEnableMembers_inGroup() {
        return false;
    }
    @Override
    public Collection getEnableMembers_inGroup(int groupID) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetForumsAuthorizedMembers() {
        return false;
    }
    @Override
    public Collection getForumsAuthorizedMembers() throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMember() {
        return true;
    }
    // Temporarily, to get member information when list post in a certain thread
    @Override
    public MemberBean getMember(int memberID) throws ObjectNotFoundException, DatabaseException {
        return memberDAO.getMember(memberID);
    }

    @Override
    public boolean isSupportGetMemberIDFromMemberEmail() {
        return false;
    }
    @Override
    public int getMemberIDFromMemberEmail(String memberEmail) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMemberIDFromMemberName() {
        return true;
    }
    // we need this method because the viewmember and login need this method
    @Override
    public int getMemberIDFromMemberName(String memberName) throws ObjectNotFoundException, DatabaseException {
        return memberDAO.getMemberIDFromMemberName(memberName);
    }

    @Override
    public boolean isSupportGetMembers_inExpire_limit() {
        return false;
    }
    @Override
    public Collection getMembers_inExpire_limit(Timestamp expireDate, int offset, int rowsToReturn, String sort, String order) throws IllegalArgumentException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMembers_withSortSupport_limit() {
        return false;
    }
    @Override
    public Collection getMembers_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, int memberStatus) throws IllegalArgumentException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMembers() {
        return false;
    }
    @Override
    public Collection getMembers() throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetNonActivatedNoPostMembers() {
        return false;
    }
    @Override
    public Collection getNonActivatedNoPostMembers(Timestamp before) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetNumberOfMembers_inActivationStatus() {
        return false;
    }
    @Override
    public int getNumberOfMembers_inActivationStatus(boolean activationStatus) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetNumberOfMembers_inExpire() {
        return false;
    }
    @Override
    public int getNumberOfMembers_inExpire(Timestamp expireDate) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetNumberOfMembers_inMemberStatus() {
        return false;
    }
    @Override
    public int getNumberOfMembers_inMemberStatus(int memberStatus) throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetNumberOfMembers() {
        return true;
    }
    // Ensure the forum is inited successfully @see com.mvnforum.MVNForumConfig
    @Override
    public int getNumberOfMembers() throws DatabaseException {
        return memberDAO.getNumberOfMembers();
    }

    @Override
    public boolean isSupportGetPassword() {
        return true;
    }
    // This method is need by WatchMail.createWatchMessageBean to create the ConfirmedCode
    @Override
    public String getPassword(int memberID) throws ObjectNotFoundException, DatabaseException {
        return memberDAO.getPassword(memberID);
    }

    @Override
    public boolean isSupportGetTempPassword() {
        return false;
    }
    @Override
    public String getTempPassword(int memberID) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportIncreasePostCount() {
        return true;
    }
    // it is neccessary to change post count
    @Override
    public void increasePostCount(int memberID) throws DatabaseException, ObjectNotFoundException {
        memberDAO.increasePostCount(memberID);
    }

    @Override
    public boolean isSupportIncreaseViewCount() {
        return true;
    }
    // it is neccessary to change view count
    @Override
    public void increaseViewCount(int memberID) throws DatabaseException, ObjectNotFoundException {
        memberDAO.increaseViewCount(memberID);
    }

    @Override
    public boolean isSupportUpdate() {
        return false;
    }
    @Override
    public void update(int memberID, int memberEmailVisible, int memberNameVisible, Timestamp memberModifiedDate, int memberOption, int memberStatus, int memberMessageOption, int memberPostsPerPage, double memberTimeZone, String memberSkin, String memberLanguage, String memberFirstname, String memberLastname, int memberGender, Date memberBirthday, String memberAddress, String memberCity, String memberState, String memberCountry, String memberPhone, String memberMobile, String memberFax, String memberCareer, String memberHomepage)
        throws ObjectNotFoundException, DatabaseException {

        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateActivateCode() {
        return true;
    }
    // SendMailUtil.getActivationCodeEmail() use this method to change the activation code
    @Override
    public void updateActivateCode(int memberID, String memberActivateCode) throws ObjectNotFoundException, DatabaseException {
        memberDAO.updateActivateCode(memberID, memberActivateCode);
    }

    @Override
    public boolean isSupportUpdateAvatar() {
        return false;
    }
    @Override
    public void updateAvatar(int memberID, String memberAvatar) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateEmail() {
        return false;
    }
    @Override
    public void updateEmail(int memberID, String memberEmail) throws ObjectNotFoundException, DatabaseException, DuplicateKeyException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateLastLogon() {
        return true;
    }
    // need in method OnlineUserFactoryImpl.getAuthenticatedUser
    @Override
    public void updateLastLogon(int memberID, Timestamp memberLastLogon, String lastIP) throws ObjectNotFoundException, DatabaseException {
        memberDAO.updateLastLogon(memberID, memberLastLogon, lastIP);
    }

    @Override
    public boolean isSupportUpdateMember_expireDate() {
        return false;
    }
    @Override
    public void updateMember_expireDate(int memberID, Timestamp expireDate) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdatePassword() {
        return false;
    }
    @Override
    public void updatePassword(int memberID, String memberPassword, Timestamp memberPasswordExpireDate) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdatePostCount() {
        return true;
    }
    // use in StatisticsUtil.updateMemberStatistics called by PostWebHandler.processAdd
    @Override
    public void updatePostCount(int memberID, int memberPostCount) throws ObjectNotFoundException, DatabaseException {
        memberDAO.updatePostCount(memberID, memberPostCount);
    }

    @Override
    public boolean isSupportUpdateSignature() {
        return false;
    }
    @Override
    public void updateSignature(int memberID, String memberSignature) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateStatus() {
        return false;
    }
    @Override
    public void updateStatus(int memberID, int memberStatus) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateTempPassword() {
        return false;
    }
    @Override
    public void updateTempPassword(int memberID, String memberTempPassword) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportUpdateTitle() {
        return false;
    }
    @Override
    public void updateTitle(int memberID, String memberTitle) throws ObjectNotFoundException, DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMaxMemberID () {
        return false;
    }
    @Override
    public int getMaxMemberID() throws DatabaseException {
        throw new IllegalStateException();
    }

    @Override
    public boolean isSupportGetMembersFromIDRange() {
        return false;
    }
    @Override
    public Collection getMembers_fromIDRange(int fromID, int toID) throws IllegalArgumentException, DatabaseException {
        throw new IllegalStateException();
    }
}
