/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/jdbc/VBulletinUserDAOImplJDBC.java,v 1.3 2009/10/07 04:23:23 lexuanttkhtn Exp $
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

import net.myvietnam.mvncore.db.*;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.db.MemberBean;
import com.mvnforum.vbulletin.db.VBulletinUserBean;
import com.mvnforum.vbulletin.db.VBulletinUserDAO;

/**
 * The Class VBulletinUserDAOImplJDBC.
 */
public class VBulletinUserDAOImplJDBC implements VBulletinUserDAO {

    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinUserDAO#getUser(int)
     */
    public VBulletinUserBean getUser(int userID) throws DatabaseException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * Included columns: UserID, UserGroupID, MemberGroupIDs, DisplayGroupID, UserName, 
     *                   Password, PasswordDate, Email, StyleID, ParentEmail, 
     *                   HomePage, Icq, Aim, Yahoo, Msn, 
     *                   Skype, ShowvBcode, ShowBirthday, UserTitle, CustomTitle, 
     *                   JoinDate, DaySprune, LastVisit, LastActivity, LastPost, 
     *                   LastPostID, Posts, Reputation, ReputationLevelID, TimeZoneOffset, 
     *                   PmPopup, AvatarID, AvatarRevision, ProfilePicRevision, SigPicRevision, 
     *                   Options, Birthday, Birthday_Search, MaxPosts, StartOfWeek, 
     *                   IpAddress, ReferrerID, LanguageID, EmailStamp, ThreadedMode, 
     *                   AutoSubscribe, PmTotal, PmUnread, Salt, IPoints, 
     *                   Infractions, Warnings, InfractionGroupIDs, InfractionGroupID, AdminOptions, 
     *                   ProfileVisits, FriendCount, FriendReqCount, VmUnreadCount, VmModeratedCount, 
     *                   SocGroupInviteCount, SocGroupReqCount, PcUnreadCount, PcModeratedCount, GmModeratedCount
     * Excluded columns: 
     */
    /* (non-Javadoc)
     * @see com.mvnforum.vbulletin.db.VBulletinUserDAO#getNextUser(int)
     */
    public VBulletinUserBean getNextUser(int userID)
        throws ObjectNotFoundException, DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT UserID, UserGroupID, MemberGroupIDs, DisplayGroupID, UserName, Password, PasswordDate, Email, StyleID, ParentEmail, HomePage, Icq, Aim, Yahoo, Msn, Skype, ShowvBcode, ShowBirthday, UserTitle, CustomTitle, JoinDate, DaySprune, LastVisit, LastActivity, LastPost, LastPostID, Posts, Reputation, ReputationLevelID, TimeZoneOffset, PmPopup, AvatarID, AvatarRevision, ProfilePicRevision, SigPicRevision, Options, Birthday, Birthday_Search, MaxPosts, StartOfWeek, IpAddress, ReferrerID, LanguageID, EmailStamp, ThreadedMode, AutoSubscribe, PmTotal, PmUnread, Salt, IPoints, Infractions, Warnings, InfractionGroupIDs, InfractionGroupID, AdminOptions, ProfileVisits, FriendCount, FriendReqCount, VmUnreadCount, VmModeratedCount, SocGroupInviteCount, SocGroupReqCount, PcUnreadCount, PcModeratedCount, GmModeratedCount");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE UserID > ?");
        sql.append(" ORDER BY UserID ASC");
        try {
            connection = DBUtils2.getConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, userID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            VBulletinUserBean bean = new VBulletinUserBean();
            // TODO: uncomment the following line(s) as needed
            //bean.setUserID(userID);
            bean.setUserID(resultSet.getInt("UserID"));
            bean.setUserGroupID(resultSet.getInt("UserGroupID"));
            bean.setMemberGroupIDs(resultSet.getString("MemberGroupIDs"));
            bean.setDisplayGroupID(resultSet.getInt("DisplayGroupID"));
            bean.setUserName(resultSet.getString("UserName"));
            bean.setPassword(resultSet.getString("Password"));
            bean.setPasswordDate(resultSet.getDate("PasswordDate"));
            bean.setEmail(resultSet.getString("Email"));
            bean.setStyleID(resultSet.getInt("StyleID"));
            bean.setParentEmail(resultSet.getString("ParentEmail"));
            bean.setHomePage(resultSet.getString("HomePage"));
            bean.setIcq(resultSet.getString("Icq"));
            bean.setAim(resultSet.getString("Aim"));
            bean.setYahoo(resultSet.getString("Yahoo"));
            bean.setMsn(resultSet.getString("Msn"));
            bean.setSkype(resultSet.getString("Skype"));
            bean.setShowVbcode(resultSet.getInt("ShowvBcode"));
            bean.setShowBirthday(resultSet.getInt("ShowBirthday"));
            bean.setUserTitle(resultSet.getString("UserTitle"));
            bean.setCustomTitle(resultSet.getInt("CustomTitle"));
            bean.setJoinDate(resultSet.getLong("JoinDate"));
            bean.setDaySprune(resultSet.getInt("DaySprune"));
            bean.setLastVisit(resultSet.getLong("LastVisit"));
            bean.setLastActivity(resultSet.getLong("LastActivity"));
            bean.setLastPost(resultSet.getLong("LastPost"));
            bean.setLastPostID(resultSet.getInt("LastPostID"));
            bean.setPosts(resultSet.getInt("Posts"));
            bean.setReputation(resultSet.getInt("Reputation"));
            bean.setReputationLevelID(resultSet.getInt("ReputationLevelID"));
            bean.setTimeZoneOffset(resultSet.getString("TimeZoneOffset"));
            bean.setPmPopup(resultSet.getInt("PmPopup"));
            bean.setAvatarID(resultSet.getInt("AvatarID"));
            bean.setAvatarRevision(resultSet.getInt("AvatarRevision"));
            bean.setProfilePicRevision(resultSet.getInt("ProfilePicRevision"));
            bean.setSigPicRevision(resultSet.getInt("SigPicRevision"));
            bean.setOptions(resultSet.getInt("Options"));
            bean.setBirthday(resultSet.getString("Birthday"));
            try {
                bean.setBirthdaySearch(resultSet.getDate("Birthday_Search"));
            } catch (Exception e) {
                bean.setBirthdaySearch(MemberBean.MEMBER_NOT_REQUIRE_BIRTHDAY);
            }
            bean.setMaxPosts(resultSet.getInt("MaxPosts"));
            bean.setStartOfWeek(resultSet.getInt("StartOfWeek"));
            bean.setIpAddress(resultSet.getString("IpAddress"));
            bean.setReferrerID(resultSet.getInt("ReferrerID"));
            bean.setLanguageID(resultSet.getInt("LanguageID"));
            bean.setEmailStamp(resultSet.getInt("EmailStamp"));
            bean.setThreadedMode(resultSet.getInt("ThreadedMode"));
            bean.setAutoSubscribe(resultSet.getInt("AutoSubscribe"));
            bean.setPmTotal(resultSet.getInt("PmTotal"));
            bean.setPmUnread(resultSet.getInt("PmUnread"));
            bean.setSalt(resultSet.getString("Salt"));
            bean.setIPoints(resultSet.getInt("IPoints"));
            bean.setInfractions(resultSet.getInt("Infractions"));
            bean.setWarnings(resultSet.getInt("Warnings"));
            bean.setInfractionGroupIDs(resultSet.getString("InfractionGroupIDs"));
            bean.setInfractionGroupID(resultSet.getInt("InfractionGroupID"));
            bean.setAdminOptions(resultSet.getInt("AdminOptions"));
            bean.setProfileVisits(resultSet.getInt("ProfileVisits"));
            bean.setFriendCount(resultSet.getInt("FriendCount"));
            bean.setFriendReqCount(resultSet.getInt("FriendReqCount"));
            bean.setVmUnreadCount(resultSet.getInt("VmUnreadCount"));
            bean.setVmModeratedCount(resultSet.getInt("VmModeratedCount"));
            bean.setSocGroupInviteCount(resultSet.getInt("SocGroupInviteCount"));
            bean.setSocGroupReqCount(resultSet.getInt("SocGroupReqCount"));
            bean.setPcUnreadCount(resultSet.getInt("PcUnreadCount"));
            bean.setPcModeratedCount(resultSet.getInt("PcModeratedCount"));
            bean.setGmModeratedCount(resultSet.getInt("GmModeratedCount"));
            return bean;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in UserDAOImplJDBC.getBean(pk).");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils2.closeConnection(connection);
        }
    }

}
