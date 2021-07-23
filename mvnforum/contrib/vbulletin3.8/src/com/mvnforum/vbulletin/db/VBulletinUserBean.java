/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinUserBean.java,v 1.3 2009/10/07 04:23:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2009/10/07 04:23:22 $
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
package com.mvnforum.vbulletin.db;

import java.sql.Date;

/**
 * The Class VBulletinUserBean.
 */
public class VBulletinUserBean {

    /** The user id. */
    private int userID;
    
    /** The user group id. */
    private int userGroupID;
    
    /** The member group i ds. */
    private String memberGroupIDs;
    
    /** The display group id. */
    private int displayGroupID;
    
    /** The user name. */
    private String userName;
    
    /** The password. */
    private String password;
    
    /** The password date. */
    private Date passwordDate;
    
    /** The email. */
    private String email;
    
    /** The style id. */
    private int styleID;
    
    /** The parent email. */
    private String parentEmail;
    
    /** The home page. */
    private String homePage;
    
    /** The icq. */
    private String icq;
    
    /** The aim. */
    private String aim;
    
    /** The yahoo. */
    private String yahoo;
    
    /** The msn. */
    private String msn;
    
    /** The skype. */
    private String skype;
    
    /** The show vbcode. */
    private int showVbcode;
    
    /** The show birthday. */
    private int showBirthday;
    
    /** The user title. */
    private String userTitle;
    
    /** The custom title. */
    private int customTitle;
    
    /** The join date. */
    private long joinDate;
    
    /** The day sprune. */
    private int daySprune;
    
    /** The last visit. */
    private long lastVisit;
    
    /** The last activity. */
    private long lastActivity;
    
    /** The last post. */
    private long lastPost;
    
    /** The last post id. */
    private int lastPostID;
    
    /** The posts. */
    private int posts;
    
    /** The reputation. */
    private int reputation;
    
    /** The reputation level id. */
    private int reputationLevelID;
    
    /** The time zone offset. */
    private String timeZoneOffset;
    
    /** The pm popup. */
    private int pmPopup;
    
    /** The avatar id. */
    private int avatarID;
    
    /** The avatar revision. */
    private int avatarRevision;
    
    /** The profile pic revision. */
    private int profilePicRevision;
    
    /** The sig pic revision. */
    private int sigPicRevision;
    
    /** The options. */
    private int options;
    
    /** The birthday. */
    private String birthday;
    
    /** The birthday search. */
    private Date birthdaySearch;
    
    /** The max posts. */
    private int maxPosts;
    
    /** The start of week. */
    private int startOfWeek;
    
    /** The ip address. */
    private String ipAddress;
    
    /** The referrer id. */
    private int referrerID;
    
    /** The language id. */
    private int languageID;
    
    /** The email stamp. */
    private int emailStamp;
    
    /** The threaded mode. */
    private int threadedMode;
    
    /** The auto subscribe. */
    private int autoSubscribe;
    
    /** The pm total. */
    private int pmTotal;
    
    /** The pm unread. */
    private int pmUnread;
    
    /** The salt. */
    private String salt;
    
    /** The i points. */
    private int iPoints;
    
    /** The infractions. */
    private int infractions;
    
    /** The warnings. */
    private int warnings;
    
    /** The infraction group i ds. */
    private String infractionGroupIDs;
    
    /** The infraction group id. */
    private int infractionGroupID;
    
    /** The admin options. */
    private int adminOptions;
    
    /** The profile visits. */
    private int profileVisits;
    
    /** The friend count. */
    private int friendCount;
    
    /** The friend req count. */
    private int friendReqCount;
    
    /** The vm unread count. */
    private int vmUnreadCount;
    
    /** The vm moderated count. */
    private int vmModeratedCount;
    
    /** The soc group invite count. */
    private int socGroupInviteCount;
    
    /** The soc group req count. */
    private int socGroupReqCount;
    
    /** The pc unread count. */
    private int pcUnreadCount;
    
    /** The pc moderated count. */
    private int pcModeratedCount;
    
    /** The gm moderated count. */
    private int gmModeratedCount;
    
    /**
     * Gets the user id.
     * 
     * @return the user id
     */
    public int getUserID() {
        return userID;
    }
    
    /**
     * Sets the user id.
     * 
     * @param userID the new user id
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    /**
     * Gets the user group id.
     * 
     * @return the user group id
     */
    public int getUserGroupID() {
        return userGroupID;
    }
    
    /**
     * Sets the user group id.
     * 
     * @param userGroupID the new user group id
     */
    public void setUserGroupID(int userGroupID) {
        this.userGroupID = userGroupID;
    }
    
    /**
     * Gets the member group i ds.
     * 
     * @return the member group i ds
     */
    public String getMemberGroupIDs() {
        return memberGroupIDs;
    }
    
    /**
     * Sets the member group i ds.
     * 
     * @param memberGroupIDs the new member group i ds
     */
    public void setMemberGroupIDs(String memberGroupIDs) {
        this.memberGroupIDs = memberGroupIDs;
    }
    
    /**
     * Gets the display group id.
     * 
     * @return the display group id
     */
    public int getDisplayGroupID() {
        return displayGroupID;
    }
    
    /**
     * Sets the display group id.
     * 
     * @param displayGroupID the new display group id
     */
    public void setDisplayGroupID(int displayGroupID) {
        this.displayGroupID = displayGroupID;
    }
    
    /**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Sets the user name.
     * 
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the password date.
     * 
     * @return the password date
     */
    public Date getPasswordDate() {
        return passwordDate;
    }
    
    /**
     * Sets the password date.
     * 
     * @param passwordDate the new password date
     */
    public void setPasswordDate(Date passwordDate) {
        this.passwordDate = passwordDate;
    }
    
    /**
     * Gets the email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email.
     * 
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the style id.
     * 
     * @return the style id
     */
    public int getStyleID() {
        return styleID;
    }
    
    /**
     * Sets the style id.
     * 
     * @param styleID the new style id
     */
    public void setStyleID(int styleID) {
        this.styleID = styleID;
    }
    
    /**
     * Gets the parent email.
     * 
     * @return the parent email
     */
    public String getParentEmail() {
        return parentEmail;
    }
    
    /**
     * Sets the parent email.
     * 
     * @param parentEmail the new parent email
     */
    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }
    
    /**
     * Gets the home page.
     * 
     * @return the home page
     */
    public String getHomePage() {
        return homePage;
    }
    
    /**
     * Sets the home page.
     * 
     * @param homePage the new home page
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
    
    /**
     * Gets the icq.
     * 
     * @return the icq
     */
    public String getIcq() {
        return icq;
    }
    
    /**
     * Sets the icq.
     * 
     * @param icq the new icq
     */
    public void setIcq(String icq) {
        this.icq = icq;
    }
    
    /**
     * Gets the aim.
     * 
     * @return the aim
     */
    public String getAim() {
        return aim;
    }
    
    /**
     * Sets the aim.
     * 
     * @param aim the new aim
     */
    public void setAim(String aim) {
        this.aim = aim;
    }
    
    /**
     * Gets the yahoo.
     * 
     * @return the yahoo
     */
    public String getYahoo() {
        return yahoo;
    }
    
    /**
     * Sets the yahoo.
     * 
     * @param yahoo the new yahoo
     */
    public void setYahoo(String yahoo) {
        this.yahoo = yahoo;
    }
    
    /**
     * Gets the msn.
     * 
     * @return the msn
     */
    public String getMsn() {
        return msn;
    }
    
    /**
     * Sets the msn.
     * 
     * @param msn the new msn
     */
    public void setMsn(String msn) {
        this.msn = msn;
    }
    
    /**
     * Gets the skype.
     * 
     * @return the skype
     */
    public String getSkype() {
        return skype;
    }
    
    /**
     * Sets the skype.
     * 
     * @param skype the new skype
     */
    public void setSkype(String skype) {
        this.skype = skype;
    }
    
    /**
     * Gets the show vbcode.
     * 
     * @return the show vbcode
     */
    public int getShowVbcode() {
        return showVbcode;
    }
    
    /**
     * Sets the show vbcode.
     * 
     * @param showVbcode the new show vbcode
     */
    public void setShowVbcode(int showVbcode) {
        this.showVbcode = showVbcode;
    }
    
    /**
     * Gets the show birthday.
     * 
     * @return the show birthday
     */
    public int getShowBirthday() {
        return showBirthday;
    }
    
    /**
     * Sets the show birthday.
     * 
     * @param showBirthday the new show birthday
     */
    public void setShowBirthday(int showBirthday) {
        this.showBirthday = showBirthday;
    }
    
    /**
     * Gets the user title.
     * 
     * @return the user title
     */
    public String getUserTitle() {
        return userTitle;
    }
    
    /**
     * Sets the user title.
     * 
     * @param userTitle the new user title
     */
    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }
    
    /**
     * Gets the custom title.
     * 
     * @return the custom title
     */
    public int getCustomTitle() {
        return customTitle;
    }
    
    /**
     * Sets the custom title.
     * 
     * @param customTitle the new custom title
     */
    public void setCustomTitle(int customTitle) {
        this.customTitle = customTitle;
    }
    
    /**
     * Gets the join date.
     * 
     * @return the join date
     */
    public long getJoinDate() {
        return joinDate;
    }
    
    /**
     * Sets the join date.
     * 
     * @param joinDate the new join date
     */
    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }
    
    /**
     * Gets the day sprune.
     * 
     * @return the day sprune
     */
    public int getDaySprune() {
        return daySprune;
    }
    
    /**
     * Sets the day sprune.
     * 
     * @param daySprune the new day sprune
     */
    public void setDaySprune(int daySprune) {
        this.daySprune = daySprune;
    }
    
    /**
     * Gets the last visit.
     * 
     * @return the last visit
     */
    public long getLastVisit() {
        return lastVisit;
    }
    
    /**
     * Sets the last visit.
     * 
     * @param lastVisit the new last visit
     */
    public void setLastVisit(long lastVisit) {
        this.lastVisit = lastVisit;
    }
    
    /**
     * Gets the last activity.
     * 
     * @return the last activity
     */
    public long getLastActivity() {
        return lastActivity;
    }
    
    /**
     * Sets the last activity.
     * 
     * @param lastActivity the new last activity
     */
    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    /**
     * Gets the last post.
     * 
     * @return the last post
     */
    public long getLastPost() {
        return lastPost;
    }
    
    /**
     * Sets the last post.
     * 
     * @param lastPost the new last post
     */
    public void setLastPost(long lastPost) {
        this.lastPost = lastPost;
    }
    
    /**
     * Gets the last post id.
     * 
     * @return the last post id
     */
    public int getLastPostID() {
        return lastPostID;
    }
    
    /**
     * Sets the last post id.
     * 
     * @param lastPostID the new last post id
     */
    public void setLastPostID(int lastPostID) {
        this.lastPostID = lastPostID;
    }
    
    /**
     * Gets the posts.
     * 
     * @return the posts
     */
    public int getPosts() {
        return posts;
    }
    
    /**
     * Sets the posts.
     * 
     * @param posts the new posts
     */
    public void setPosts(int posts) {
        this.posts = posts;
    }
    
    /**
     * Gets the reputation.
     * 
     * @return the reputation
     */
    public int getReputation() {
        return reputation;
    }
    
    /**
     * Sets the reputation.
     * 
     * @param reputation the new reputation
     */
    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
    
    /**
     * Gets the reputation level id.
     * 
     * @return the reputation level id
     */
    public int getReputationLevelID() {
        return reputationLevelID;
    }
    
    /**
     * Sets the reputation level id.
     * 
     * @param reputationLevelID the new reputation level id
     */
    public void setReputationLevelID(int reputationLevelID) {
        this.reputationLevelID = reputationLevelID;
    }
    
    /**
     * Gets the time zone offset.
     * 
     * @return the time zone offset
     */
    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }
    
    /**
     * Sets the time zone offset.
     * 
     * @param timeZoneOffset the new time zone offset
     */
    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
    
    /**
     * Gets the pm popup.
     * 
     * @return the pm popup
     */
    public int getPmPopup() {
        return pmPopup;
    }
    
    /**
     * Sets the pm popup.
     * 
     * @param pmPopup the new pm popup
     */
    public void setPmPopup(int pmPopup) {
        this.pmPopup = pmPopup;
    }
    
    /**
     * Gets the avatar id.
     * 
     * @return the avatar id
     */
    public int getAvatarID() {
        return avatarID;
    }
    
    /**
     * Sets the avatar id.
     * 
     * @param avatarID the new avatar id
     */
    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }
    
    /**
     * Gets the avatar revision.
     * 
     * @return the avatar revision
     */
    public int getAvatarRevision() {
        return avatarRevision;
    }
    
    /**
     * Sets the avatar revision.
     * 
     * @param avatarRevision the new avatar revision
     */
    public void setAvatarRevision(int avatarRevision) {
        this.avatarRevision = avatarRevision;
    }
    
    /**
     * Gets the profile pic revision.
     * 
     * @return the profile pic revision
     */
    public int getProfilePicRevision() {
        return profilePicRevision;
    }
    
    /**
     * Sets the profile pic revision.
     * 
     * @param profilePicRevision the new profile pic revision
     */
    public void setProfilePicRevision(int profilePicRevision) {
        this.profilePicRevision = profilePicRevision;
    }
    
    /**
     * Gets the sig pic revision.
     * 
     * @return the sig pic revision
     */
    public int getSigPicRevision() {
        return sigPicRevision;
    }
    
    /**
     * Sets the sig pic revision.
     * 
     * @param sigPicRevision the new sig pic revision
     */
    public void setSigPicRevision(int sigPicRevision) {
        this.sigPicRevision = sigPicRevision;
    }
    
    /**
     * Gets the options.
     * 
     * @return the options
     */
    public int getOptions() {
        return options;
    }
    
    /**
     * Sets the options.
     * 
     * @param options the new options
     */
    public void setOptions(int options) {
        this.options = options;
    }
    
    /**
     * Gets the birthday.
     * 
     * @return the birthday
     */
    public String getBirthday() {
        return birthday;
    }
    
    /**
     * Sets the birthday.
     * 
     * @param birthday the new birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    /**
     * Gets the birthday search.
     * 
     * @return the birthday search
     */
    public Date getBirthdaySearch() {
        return birthdaySearch;
    }
    
    /**
     * Sets the birthday search.
     * 
     * @param birthdaySearch the new birthday search
     */
    public void setBirthdaySearch(Date birthdaySearch) {
        this.birthdaySearch = birthdaySearch;
    }
    
    /**
     * Gets the max posts.
     * 
     * @return the max posts
     */
    public int getMaxPosts() {
        return maxPosts;
    }
    
    /**
     * Sets the max posts.
     * 
     * @param maxPosts the new max posts
     */
    public void setMaxPosts(int maxPosts) {
        this.maxPosts = maxPosts;
    }
    
    /**
     * Gets the start of week.
     * 
     * @return the start of week
     */
    public int getStartOfWeek() {
        return startOfWeek;
    }
    
    /**
     * Sets the start of week.
     * 
     * @param startOfWeek the new start of week
     */
    public void setStartOfWeek(int startOfWeek) {
        this.startOfWeek = startOfWeek;
    }
    
    /**
     * Gets the ip address.
     * 
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }
    
    /**
     * Sets the ip address.
     * 
     * @param ipAddress the new ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    /**
     * Gets the referrer id.
     * 
     * @return the referrer id
     */
    public int getReferrerID() {
        return referrerID;
    }
    
    /**
     * Sets the referrer id.
     * 
     * @param referrerID the new referrer id
     */
    public void setReferrerID(int referrerID) {
        this.referrerID = referrerID;
    }
    
    /**
     * Gets the language id.
     * 
     * @return the language id
     */
    public int getLanguageID() {
        return languageID;
    }
    
    /**
     * Sets the language id.
     * 
     * @param languageID the new language id
     */
    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }
    
    /**
     * Gets the email stamp.
     * 
     * @return the email stamp
     */
    public int getEmailStamp() {
        return emailStamp;
    }
    
    /**
     * Sets the email stamp.
     * 
     * @param emailStamp the new email stamp
     */
    public void setEmailStamp(int emailStamp) {
        this.emailStamp = emailStamp;
    }
    
    /**
     * Gets the threaded mode.
     * 
     * @return the threaded mode
     */
    public int getThreadedMode() {
        return threadedMode;
    }
    
    /**
     * Sets the threaded mode.
     * 
     * @param threadedMode the new threaded mode
     */
    public void setThreadedMode(int threadedMode) {
        this.threadedMode = threadedMode;
    }
    
    /**
     * Gets the auto subscribe.
     * 
     * @return the auto subscribe
     */
    public int getAutoSubscribe() {
        return autoSubscribe;
    }
    
    /**
     * Sets the auto subscribe.
     * 
     * @param autoSubscribe the new auto subscribe
     */
    public void setAutoSubscribe(int autoSubscribe) {
        this.autoSubscribe = autoSubscribe;
    }
    
    /**
     * Gets the pm total.
     * 
     * @return the pm total
     */
    public int getPmTotal() {
        return pmTotal;
    }
    
    /**
     * Sets the pm total.
     * 
     * @param pmTotal the new pm total
     */
    public void setPmTotal(int pmTotal) {
        this.pmTotal = pmTotal;
    }
    
    /**
     * Gets the pm unread.
     * 
     * @return the pm unread
     */
    public int getPmUnread() {
        return pmUnread;
    }
    
    /**
     * Sets the pm unread.
     * 
     * @param pmUnread the new pm unread
     */
    public void setPmUnread(int pmUnread) {
        this.pmUnread = pmUnread;
    }
    
    /**
     * Gets the salt.
     * 
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }
    
    /**
     * Sets the salt.
     * 
     * @param salt the new salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    /**
     * Gets the i points.
     * 
     * @return the i points
     */
    public int getIPoints() {
        return iPoints;
    }
    
    /**
     * Sets the i points.
     * 
     * @param points the new i points
     */
    public void setIPoints(int points) {
        iPoints = points;
    }
    
    /**
     * Gets the infractions.
     * 
     * @return the infractions
     */
    public int getInfractions() {
        return infractions;
    }
    
    /**
     * Sets the infractions.
     * 
     * @param infractions the new infractions
     */
    public void setInfractions(int infractions) {
        this.infractions = infractions;
    }
    
    /**
     * Gets the warnings.
     * 
     * @return the warnings
     */
    public int getWarnings() {
        return warnings;
    }
    
    /**
     * Sets the warnings.
     * 
     * @param warnings the new warnings
     */
    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }
    
    /**
     * Gets the infraction group i ds.
     * 
     * @return the infraction group i ds
     */
    public String getInfractionGroupIDs() {
        return infractionGroupIDs;
    }
    
    /**
     * Sets the infraction group i ds.
     * 
     * @param infractionGroupIDs the new infraction group i ds
     */
    public void setInfractionGroupIDs(String infractionGroupIDs) {
        this.infractionGroupIDs = infractionGroupIDs;
    }
    
    /**
     * Gets the infraction group id.
     * 
     * @return the infraction group id
     */
    public int getInfractionGroupID() {
        return infractionGroupID;
    }
    
    /**
     * Sets the infraction group id.
     * 
     * @param infractionGroupID the new infraction group id
     */
    public void setInfractionGroupID(int infractionGroupID) {
        this.infractionGroupID = infractionGroupID;
    }
    
    /**
     * Gets the admin options.
     * 
     * @return the admin options
     */
    public int getAdminOptions() {
        return adminOptions;
    }
    
    /**
     * Sets the admin options.
     * 
     * @param adminOptions the new admin options
     */
    public void setAdminOptions(int adminOptions) {
        this.adminOptions = adminOptions;
    }
    
    /**
     * Gets the profile visits.
     * 
     * @return the profile visits
     */
    public int getProfileVisits() {
        return profileVisits;
    }
    
    /**
     * Sets the profile visits.
     * 
     * @param profileVisits the new profile visits
     */
    public void setProfileVisits(int profileVisits) {
        this.profileVisits = profileVisits;
    }
    
    /**
     * Gets the friend count.
     * 
     * @return the friend count
     */
    public int getFriendCount() {
        return friendCount;
    }
    
    /**
     * Sets the friend count.
     * 
     * @param friendCount the new friend count
     */
    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }
    
    /**
     * Gets the friend req count.
     * 
     * @return the friend req count
     */
    public int getFriendReqCount() {
        return friendReqCount;
    }
    
    /**
     * Sets the friend req count.
     * 
     * @param friendReqCount the new friend req count
     */
    public void setFriendReqCount(int friendReqCount) {
        this.friendReqCount = friendReqCount;
    }
    
    /**
     * Gets the vm unread count.
     * 
     * @return the vm unread count
     */
    public int getVmUnreadCount() {
        return vmUnreadCount;
    }
    
    /**
     * Sets the vm unread count.
     * 
     * @param vmUnreadCount the new vm unread count
     */
    public void setVmUnreadCount(int vmUnreadCount) {
        this.vmUnreadCount = vmUnreadCount;
    }
    
    /**
     * Gets the vm moderated count.
     * 
     * @return the vm moderated count
     */
    public int getVmModeratedCount() {
        return vmModeratedCount;
    }
    
    /**
     * Sets the vm moderated count.
     * 
     * @param vmModeratedCount the new vm moderated count
     */
    public void setVmModeratedCount(int vmModeratedCount) {
        this.vmModeratedCount = vmModeratedCount;
    }
    
    /**
     * Gets the soc group invite count.
     * 
     * @return the soc group invite count
     */
    public int getSocGroupInviteCount() {
        return socGroupInviteCount;
    }
    
    /**
     * Sets the soc group invite count.
     * 
     * @param socGroupInviteCount the new soc group invite count
     */
    public void setSocGroupInviteCount(int socGroupInviteCount) {
        this.socGroupInviteCount = socGroupInviteCount;
    }
    
    /**
     * Gets the soc group req count.
     * 
     * @return the soc group req count
     */
    public int getSocGroupReqCount() {
        return socGroupReqCount;
    }
    
    /**
     * Sets the soc group req count.
     * 
     * @param socGroupReqCount the new soc group req count
     */
    public void setSocGroupReqCount(int socGroupReqCount) {
        this.socGroupReqCount = socGroupReqCount;
    }
    
    /**
     * Gets the pc unread count.
     * 
     * @return the pc unread count
     */
    public int getPcUnreadCount() {
        return pcUnreadCount;
    }
    
    /**
     * Sets the pc unread count.
     * 
     * @param pcUnreadCount the new pc unread count
     */
    public void setPcUnreadCount(int pcUnreadCount) {
        this.pcUnreadCount = pcUnreadCount;
    }
    
    /**
     * Gets the pc moderated count.
     * 
     * @return the pc moderated count
     */
    public int getPcModeratedCount() {
        return pcModeratedCount;
    }
    
    /**
     * Sets the pc moderated count.
     * 
     * @param pcModeratedCount the new pc moderated count
     */
    public void setPcModeratedCount(int pcModeratedCount) {
        this.pcModeratedCount = pcModeratedCount;
    }
    
    /**
     * Gets the gm moderated count.
     * 
     * @return the gm moderated count
     */
    public int getGmModeratedCount() {
        return gmModeratedCount;
    }
    
    /**
     * Sets the gm moderated count.
     * 
     * @param gmModeratedCount the new gm moderated count
     */
    public void setGmModeratedCount(int gmModeratedCount) {
        this.gmModeratedCount = gmModeratedCount;
    }
    
} //end of class VBulletinUserBean
