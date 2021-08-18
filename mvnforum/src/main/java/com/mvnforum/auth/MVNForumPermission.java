/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/MVNForumPermission.java,v 1.67
 * 2009/11/06 09:17:00 nhanld Exp $ $Author: nhanld $ $Revision: 1.67 $ $Date: 2009/11/06 09:17:00 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.auth;

public interface MVNForumPermission {

  /**************************************************************************
   * Below are constant, once a constant have been assigned a value, it CANNOT be changed (for
   * compatibility).
   **************************************************************************/


  /**************************************************************************
   * Special permissions, range from 0 to 99
   **************************************************************************/
  /**
   * No permission, just use to reserved the value 0 Normally, this permission should never be used.
   */
  public static final int PERMISSION_EMPTY = 0;

  /**
   * All users will have this permission once they have logged in.
   */
  public static final int PERMISSION_AUTHENTICATED = 1;

  /**
   * If a user have this permission, it means he is revoked all permissions This permission is still
   * reserved for future uses.
   */
  public static final int PERMISSION_NO_PERMISSIONS = 2;

  /**
   * All users will have this permission once they have logged in and they have activated their
   * account using email activation process
   */
  public static final int PERMISSION_ACTIVATED = 3;


  /**************************************************************************
   * Combined permissions, range from 100 to 199 A permission in this range is the combination of
   * other individual permissions NOTE: values from 200 to (1000-1) are still reserved
   **************************************************************************/
  /**
   * This permission is combination of all permissions, but excludes all special permissions.
   */
  public static final int PERMISSION_SYSTEM_ADMIN = 100;

  /**
   */
  public static final int PERMISSION_GROUP_ADMIN = 101;

  /**
   */
  public static final int PERMISSION_GROUP_MODERATOR = 102;

  /**
   */
  public static final int PERMISSION_USER_ADMIN = 103;

  /**
   */
  public static final int PERMISSION_USER_MODERATOR = 104;

  /**
   */
  public static final int PERMISSION_FORUM_ADMIN = 105;

  /**
   */
  public static final int PERMISSION_FORUM_MODERATOR = 106;

  /**
   * Reserved
   */
  public static final int PERMISSION_CATEGORY_ADMIN = 107;

  /**
   * Reserved
   */
  public static final int PERMISSION_CATEGORY_MODERATOR = 108;

  /**
   * Can: - login, read thread and post, reply to a thread
   */
  public static final int PERMISSION_LIMITED_USER = 109;

  /**
   * Can: - login, read thread and post, reply to a thread - Add thread, use avatar, get attachment
   */
  public static final int PERMISSION_NORMAL_USER = 110;

  /**
   * Can: - login, read thread and post, reply to a thread - Add thread, use avatar, get attachment
   * - use attachment, create poll
   */
  public static final int PERMISSION_POWER_USER = 111;


  /**************************************************************************
   * Individual Permissions, range begin at 1000 A permission in this range is an single permission
   * A Combined Permission usually includes some Individual Permissions
   **************************************************************************/

  /**************************************************************************
   * Individual Permissions for global usages, range from 1000 to (2000-1) NOTE: values from 2000
   * and above are still reserved
   **************************************************************************/
  /**************************************************************************
   * Individual Global Permissions which high-level permission, range from 1000 to (1100-1)
   **************************************************************************/
  /**
   * Login
   */
  public static final int PERMISSION_LOGIN = 1000;

  /**
   * Admin the system, this permission should not be used instead, use the combined
   * PERMISSION_SYSTEM_ADMIN
   */
  // public static final int PERMISSION_ADMIN_SYSTEM = 1001;

  /**
   * Add a Forum
   */
  public static final int PERMISSION_ADD_FORUM = 1002;

  /**
   * Add a Category
   */
  public static final int PERMISSION_ADD_CATEGORY = 1003;

  /**
   * Edit a Category
   */
  public static final int PERMISSION_EDIT_CATEGORY = 1004;

  /**
   * Delete a Category
   */
  public static final int PERMISSION_DELETE_CATEGORY = 1005;

  /**
   * Send Mail
   */
  public static final int PERMISSION_SEND_MAIL = 1006;

  /**
   * Permission to bypass/ignore the private forum
   */
  public static final int PERMISSION_BYPASS_PRIVATE_FORUM = 1007;

  /**
   * Moderate User (can reset avatar, reset signature, disable user, etc)
   */
  public static final int PERMISSION_MODERATE_USER = 1008;

  /**************************************************************************
   * Individual Global Permissions which low-level permission, range from 1100 to (2000-1)
   **************************************************************************/
  /**
   * Use Private Message
   */
  public static final int PERMISSION_USE_MESSAGE = 1100;

  /**
   * Use Avatar
   */
  public static final int PERMISSION_USE_AVATAR = 1101;

  /**
   * Use (upload) Attachment in Private Message
   */
  public static final int PERMISSION_ADD_MESSAGE_ATTACHMENT = 1102;

  /**
   * Use Album
   */
  public static final int PERMISSION_USE_ALBUM = 1103;

  /**
   * Can manage orphan poll
   */
  public static final int PERMISSION_MANAGE_ORPHAN_POLL = 1104;

  /**
   * Can manage album item poll
   */
  public static final int PERMISSION_MANAGE_ALBUMITEM_POLL = 1105;

  /**
   * Can set poll to anonymous type
   */
  public static final int PERMISSION_SET_POLL_TO_ANONYMOUS_TYPE = 1106;

  /**************************************************************************
   * Individual Permissions that can be applied for individual forum usages, (of course it can be
   * applied to all forums), range from 2000 to (3000-1)
   **************************************************************************/
  /**************************************************************************
   * Individual Permissions which high-level permission, range from 2000 to (2100-1) Usually Forum
   * Admin has these permissions
   **************************************************************************/
  /**
   * Edit a Forum
   */
  public static final int PERMISSION_EDIT_FORUM = 2000;

  /**
   * Delete a Forum
   */
  public static final int PERMISSION_DELETE_FORUM = 2001;

  /**
   * Assign permission
   */
  public static final int PERMISSION_ASSIGN_TO_FORUM = 2002;

  /**************************************************************************
   * Individual Permissions which low-level permission, range from (2100 to 3000-1)
   **************************************************************************/
  /**
   * Read post in the forum
   */
  public static final int PERMISSION_READ_POST = 2100;

  /**
   * Create add thread
   */
  public static final int PERMISSION_ADD_THREAD = 2101;

  /**
   * Add a post (reply to a thread)
   */
  public static final int PERMISSION_ADD_POST = 2102;

  /**
   * Edit post in the forum This permission allow edit all posts and attach all files to the post
   */
  public static final int PERMISSION_EDIT_POST = 2103;

  /**
   * Delete post in the forum
   */
  public static final int PERMISSION_DELETE_POST = 2104;

  /**
   * Add a poll
   */
  public static final int PERMISSION_ADD_POLL = 2105;

  /**
   * Edit a poll
   */
  public static final int PERMISSION_EDIT_POLL = 2106;

  /**
   * Delete a poll
   */
  public static final int PERMISSION_DELETE_POLL = 2107;

  /**
   * Can attach files when posting
   */
  public static final int PERMISSION_ADD_ATTACHMENT = 2108;

  /**
   * Can download attached files
   */
  public static final int PERMISSION_GET_ATTACHMENT = 2109;

  /**
   * Can moderate the forum, such as lock thread or approve pending threads. Please note that there
   * is not PERMISSION_MODERATE_POST because who can moderate threads obviously can moderate posts
   * too.
   */
  public static final int PERMISSION_MODERATE_THREAD = 2110;

  /**
   * Can edit user own posts
   */
  public static final int PERMISSION_EDIT_OWN_POST = 2111;

  /**
   * Can edit user own polls
   */
  public static final int PERMISSION_EDIT_OWN_POLL = 2112;

  /**
   * Can add own thread polls
   */
  public static final int PERMISSION_ADD_OWN_THREAD_POLL = 2113;

  /**
   * Can manage vote
   */
  public static final int PERMISSION_MANAGE_VOTE = 2114;

  /**************************************************************************
   * MVN CMS Permission Constants Individual Permissions that can be applied for individual Channel
   * usages, (of course it can be applied to all channels), range from 10,000 to (20,000-1)
   **************************************************************************/
  /**************************************************************************
   * Individual Permissions which high-level permission, range from 10,000 to (10,099) Usually CMS
   * Admin has these permissions
   **************************************************************************/

  /**
   * @todo add the description here
   */
  public static final int PERMISSION_CMS_ADD_CHANNEL = 10000;

  /**
   *
   */
  public static final int PERMISSION_CMS_EDIT_CHANNEL = 10001;

  /**
   *
   */
  public static final int PERMISSION_CMS_DELETE_CHANNEL = 10002;

  /**
   * Permission to be an editor
   */
  public static final int PERMISSION_CMS_HAVE_ROLE_EDITOR = 10003;

  /**
   * Permission to be an chief editor
   */
  public static final int PERMISSION_CMS_HAVE_ROLE_CHIEF_EDITOR = 10004;

  /**
   * This permission allow user to manage the Gold Price, Weather Information, etc
   */
  public static final int PERMISSION_CMS_MANAGE_INFO_IN_DAY = 10005;

  /**
   * This permission will have full permissions on on CMS System
   */
  public static final int PERMISSION_CMS_MANAGE_CMS_SYSTEM = 10006;

  /**
   * This permission allow editing the layout of the Content Display System
   */
  public static final int PERMISSION_CMS_EDIT_CDS_LAYOUT = 10007;

  /**************************************************************************
   * Individual Permissions which low-level permission, range from (10,100 to 10,999)
   **************************************************************************/
  /**
   *
   */
  public static final int PERMISSION_CMS_WRITE_CONTENT = 10100;

  /**
   *
   */
  public static final int PERMISSION_CMS_EDIT_CONTENT = 10101;

  /**
   *
   */
  public static final int PERMISSION_CMS_APPROVE_CONTENT = 10102;

  /**
   *
   */
  public static final int PERMISSION_CMS_PUBLISH_CONTENT = 10103;

  /**
   *
   */
  public static final int PERMISSION_CMS_DELETE_CONTENT = 10104;

  /**
   *
   */
  public static final int PERMISSION_CMS_UPLOAD_FILE = 10105;

  // ---------------------------------------------------------------------
  /**
   *
   */
  public static final int PERMISSION_CMS_VIEW_CONTENT = 10106;

  /**
   *
   */
  public static final int PERMISSION_CMS_DEPLOY_CONTENT = 10107;

  /**
   *
   */
  public static final int PERMISSION_CMS_VIEW_STEP = 10108;

  /**
   *
   */
  public static final int PERMISSION_CMS_GOTO_STEP = 10109;

  /**
  *
  */
  public static final int PERMISSION_CMS_VIEW_CHART = 10110;

  /**
   *
   */
  public static final int PERMISSION_CMS_ADD_INTERVIEW = 10111;

  /**
   *
   */
  public static final int PERMISSION_CMS_VIEW_INTERVIEW = 10112;

  /**
   *
   */
  public static final int PERMISSION_CMS_ADD_TO_TAXONOMY = 10113;

  /**
  *
  */
  public static final int PERMISSION_CMS_MANAGE_CONTACT_US = 10114;

  /**************************************************************************
   * MVN Ads Permission Constants Individual Permissions that can be applied for individual Channel
   * usages, (of course it can be applied to all channels), range from 20,000 to (30,000-1)
   **************************************************************************/
  /**************************************************************************
   * Individual Permissions which high-level permission, range from 20,000 to (20,099) Usually Ads
   * Admin has these permissions
   **************************************************************************/

  /**
   * This permission will have full permissions on on Ads System
   */
  public static final int PERMISSION_ADS_MANAGE_ADS = 20000;

  /**
   * Can add a new zone
   */
  public static final int PERMISSION_ADS_ADD_ZONE = 20001;

  /**
   * Can edit a zone
   */
  public static final int PERMISSION_ADS_EDIT_ZONE = 20002;

  /**
   * Can delete a zone
   */
  public static final int PERMISSION_ADS_DELETE_ZONE = 20003;

  /**
   * Can add a new banner
   */
  public static final int PERMISSION_ADS_ADD_BANNER = 20004;

  /**
   * Can edit a banner
   */
  public static final int PERMISSION_ADS_EDIT_BANNER = 20005;

  /**
   * Can delete a banner
   */
  public static final int PERMISSION_ADS_DELETE_BANNER = 20006;

  /**
   * Can view zone
   */
  public static final int PERMISSION_ADS_VIEW_ZONE = 20007;

  /**
   * Can view banner
   */
  public static final int PERMISSION_ADS_VIEW_BANNER = 20008;

  /**
   * Can upload media
   */
  public static final int PERMISSION_ADS_UPLOAD_MEDIA = 20009;

  /**
   * Can manage forum advertisement
   */
  public static final int PERMISSION_ADS_MANAGE_FORUM_ADVERTISEMENT = 20010;

  /**
   * Can view chart in mvnAd
   */
  public static final int PERMISSION_ADS_VIEW_CHART = 20011;


  /**************************************************************************
   * Define array of permissions
   **************************************************************************/
  public static final int[] globalCombinedPermissionArray = {PERMISSION_SYSTEM_ADMIN,

      // below are forum-applicable permissions
      PERMISSION_FORUM_ADMIN, PERMISSION_FORUM_MODERATOR, PERMISSION_POWER_USER,
      PERMISSION_NORMAL_USER, PERMISSION_LIMITED_USER};

  public static final int[] forumCombinedPermissionArray =
      {PERMISSION_FORUM_ADMIN, PERMISSION_FORUM_MODERATOR, PERMISSION_POWER_USER,
          PERMISSION_NORMAL_USER, PERMISSION_LIMITED_USER};

  public static final int[] globalIndividualPermissionArray = {
      // PERMISSION_LOGIN,//minhnn: login is not used, so I removed it to avoid confusion
      // PERMISSION_ADMIN_SYSTEM,
      PERMISSION_ADD_CATEGORY, PERMISSION_EDIT_CATEGORY, PERMISSION_DELETE_CATEGORY,
      PERMISSION_ADD_FORUM,

      // these 2 permission should be in forum-applicable permissions,
      // However, I put it here for the more natural order that user
      // can see in interface
      PERMISSION_EDIT_FORUM, PERMISSION_DELETE_FORUM, PERMISSION_ASSIGN_TO_FORUM,
      PERMISSION_BYPASS_PRIVATE_FORUM,

      // forum-non-applicable permissions
      PERMISSION_SEND_MAIL, PERMISSION_MODERATE_USER, PERMISSION_USE_MESSAGE,
      PERMISSION_ADD_MESSAGE_ATTACHMENT, PERMISSION_USE_AVATAR, PERMISSION_USE_ALBUM,
      PERMISSION_MANAGE_ORPHAN_POLL, PERMISSION_MANAGE_ALBUMITEM_POLL,
      PERMISSION_SET_POLL_TO_ANONYMOUS_TYPE, PERMISSION_MANAGE_VOTE,

      // below are forum-applicable permissions
      PERMISSION_MODERATE_THREAD, PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST,
      PERMISSION_EDIT_POST, PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL,
      PERMISSION_EDIT_POLL, PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL,
      PERMISSION_DELETE_POLL, PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT};

  public static final int[] forumIndividualPermissionArray = {PERMISSION_EDIT_FORUM,
      PERMISSION_DELETE_FORUM, PERMISSION_ASSIGN_TO_FORUM, PERMISSION_MODERATE_THREAD,
      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_POST,
      PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL, PERMISSION_EDIT_POLL,
      PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_DELETE_POLL,
      PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT};

  public static final int[] globalCMSIndividualPermissionArray = {PERMISSION_CMS_MANAGE_CMS_SYSTEM,

      PERMISSION_CMS_ADD_CHANNEL, PERMISSION_CMS_EDIT_CHANNEL, PERMISSION_CMS_DELETE_CHANNEL,

      PERMISSION_CMS_HAVE_ROLE_EDITOR, PERMISSION_CMS_HAVE_ROLE_CHIEF_EDITOR,

      PERMISSION_CMS_MANAGE_INFO_IN_DAY, PERMISSION_CMS_EDIT_CDS_LAYOUT,

      PERMISSION_CMS_DELETE_CONTENT, PERMISSION_CMS_PUBLISH_CONTENT, PERMISSION_CMS_APPROVE_CONTENT,
      PERMISSION_CMS_EDIT_CONTENT, PERMISSION_CMS_WRITE_CONTENT,
      // ---------------------------------------------------------------------
      PERMISSION_CMS_VIEW_CONTENT, PERMISSION_CMS_DEPLOY_CONTENT, PERMISSION_CMS_VIEW_STEP,
      PERMISSION_CMS_GOTO_STEP, PERMISSION_CMS_VIEW_CHART, PERMISSION_CMS_ADD_INTERVIEW,
      PERMISSION_CMS_VIEW_INTERVIEW, PERMISSION_CMS_ADD_TO_TAXONOMY,
      PERMISSION_CMS_MANAGE_CONTACT_US};

  public static final int[] globalAdsIndividualPermissionArray = {PERMISSION_ADS_MANAGE_ADS,
      PERMISSION_ADS_MANAGE_FORUM_ADVERTISEMENT, PERMISSION_ADS_VIEW_ZONE, PERMISSION_ADS_ADD_ZONE,
      PERMISSION_ADS_EDIT_ZONE, PERMISSION_ADS_DELETE_ZONE, PERMISSION_ADS_VIEW_BANNER,
      PERMISSION_ADS_ADD_BANNER, PERMISSION_ADS_EDIT_BANNER, PERMISSION_ADS_DELETE_BANNER,
      PERMISSION_ADS_UPLOAD_MEDIA, PERMISSION_ADS_VIEW_CHART};

  /**************************************************************************
   * Define array of permissions for combined permissions Please note that each combined permission
   * have 2 arrays, one for global permissions and one for forum-specific permissions
   **************************************************************************/
  public static final int[] individualForumAdminPermissionArray = {PERMISSION_ADD_CATEGORY,
      PERMISSION_EDIT_CATEGORY, PERMISSION_DELETE_CATEGORY, PERMISSION_ADD_FORUM,

      PERMISSION_EDIT_FORUM, PERMISSION_DELETE_FORUM, PERMISSION_ASSIGN_TO_FORUM,
      PERMISSION_MODERATE_THREAD,

      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_POST,
      PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL, PERMISSION_EDIT_POLL,
      PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_DELETE_POLL,
      PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT,};

  public static final int[] individualForumAdminLimitPermissionArray = {PERMISSION_EDIT_FORUM,
      PERMISSION_DELETE_FORUM, PERMISSION_ASSIGN_TO_FORUM, PERMISSION_MODERATE_THREAD,

      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_POST,
      PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL, PERMISSION_EDIT_POLL,
      PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_DELETE_POLL,
      PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT};

  public static final int[] individualForumModeratorPermissionArray = {PERMISSION_EDIT_CATEGORY,

      PERMISSION_EDIT_FORUM, PERMISSION_MODERATE_THREAD,

      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_POST,
      PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL, PERMISSION_EDIT_POLL,
      PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_DELETE_POLL,
      PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT};

  public static final int[] individualForumModeratorLimitPermissionArray =
      {PERMISSION_EDIT_FORUM, PERMISSION_MODERATE_THREAD,

          PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_POST,
          PERMISSION_EDIT_OWN_POST, PERMISSION_DELETE_POST, PERMISSION_ADD_POLL,
          PERMISSION_EDIT_POLL, PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL,
          PERMISSION_DELETE_POLL, PERMISSION_ADD_ATTACHMENT, PERMISSION_GET_ATTACHMENT};

  public static final int[] individualPowerUserPermissionArray = {PERMISSION_USE_MESSAGE,
      PERMISSION_ADD_MESSAGE_ATTACHMENT, PERMISSION_USE_AVATAR, PERMISSION_USE_ALBUM,

      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_OWN_POST,
      PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_ADD_ATTACHMENT,
      PERMISSION_GET_ATTACHMENT};

  public static final int[] individualPowerUserLimitPermissionArray =
      {PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_EDIT_OWN_POST,
          PERMISSION_EDIT_OWN_POLL, PERMISSION_ADD_OWN_THREAD_POLL, PERMISSION_ADD_ATTACHMENT,
          PERMISSION_GET_ATTACHMENT};

  public static final int[] individualNormalUserPermissionArray = {PERMISSION_USE_MESSAGE,
      PERMISSION_USE_AVATAR,

      PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_GET_ATTACHMENT};

  public static final int[] individualNormalUserLimitPermissionArray =
      {PERMISSION_READ_POST, PERMISSION_ADD_THREAD, PERMISSION_ADD_POST, PERMISSION_GET_ATTACHMENT};

  public static final int[] individualLimitedUserPermissionArray = {PERMISSION_READ_POST};

  public static final int[] individualLimitedUserLimitPermissionArray = {PERMISSION_READ_POST};

  /**************************************************************************
   * Special permissions methods
   **************************************************************************/

  public boolean isAuthenticated();

  public void ensureIsAuthenticated() throws AuthenticationException;

  public boolean isActivated();

  public void ensureIsActivated() throws AuthenticationException;

  /**************************************************************************
   * The below methods are used to check global permissions
   **************************************************************************/

  public boolean canLogin();

  public void ensureCanLogin() throws AuthenticationException;

  public boolean canAdminSystem();

  public void ensureCanAdminSystem() throws AuthenticationException;

  public boolean canAddForum();

  public void ensureCanAddForum() throws AuthenticationException;

  public boolean canAddCategory();

  public void ensureCanAddCategory() throws AuthenticationException;

  public boolean canEditCategory();

  public void ensureCanEditCategory() throws AuthenticationException;

  public boolean canDeleteCategory();

  public void ensureCanDeleteCategory() throws AuthenticationException;

  public boolean canSendMail();

  public void ensureCanSendMail() throws AuthenticationException;

  public boolean canModerateUser();

  public void ensureCanModerateUser() throws AuthenticationException;

  public boolean canUseAvatar();

  public void ensureCanUseAvatar() throws AuthenticationException;

  public boolean canUseMessage();

  public void ensureCanUseMessage() throws AuthenticationException;

  public boolean canAddMessageAttachment();

  public void ensureCanAddMessageAttachment() throws AuthenticationException;

  public boolean canUseAlbum();

  public void ensureCanUseAlbum() throws AuthenticationException;

  public boolean canManageOrphanPoll();

  public void ensureCanManageOrphanPoll() throws AuthenticationException;

  public boolean canManageAlbumItemPoll();

  public void ensureCanManageAlbumItemPoll() throws AuthenticationException;

  public boolean canSetPollToAnonymousType();

  public void ensureCanSetPollToAnonymousType() throws AuthenticationException;

  public boolean canManageVote();

  public void ensureCanManageVote() throws AuthenticationException;

  /**************************************************************************
   * The below methods are used to check individual forum permissions
   **************************************************************************/

  public boolean canEditForum(int forumID);

  public void ensureCanEditForum(int forumID) throws AuthenticationException;

  public boolean canDeleteForum(int forumID);

  public void ensureCanDeleteForum(int forumID) throws AuthenticationException;

  public boolean canAssignToForum(int forumID);

  public void ensureCanAssignToForum(int forumID) throws AuthenticationException;

  public boolean canReadPost(int forumID);

  public void ensureCanReadPost(int forumID) throws AuthenticationException;

  public boolean canAddThread(int forumID);

  public void ensureCanAddThread(int forumID) throws AuthenticationException;

  public boolean canAddPost(int forumID);

  public void ensureCanAddPost(int forumID) throws AuthenticationException;

  public boolean canEditPost(int forumID);

  public void ensureCanEditPost(int forumID) throws AuthenticationException;

  public boolean canEditOwnPost(int forumID);

  public void ensureCanEditOwnPost(int forumID) throws AuthenticationException;

  public boolean canDeletePost(int forumID);

  public void ensureCanDeletePost(int forumID) throws AuthenticationException;

  public boolean canAddPoll(int forumID);

  public void ensureCanAddPoll(int forumID) throws AuthenticationException;

  public boolean canAddPoll();

  public void ensureCanAddPoll() throws AuthenticationException;

  public boolean canEditPoll(int forumID);

  public void ensureCanEditPoll(int forumID) throws AuthenticationException;

  public boolean canEditPoll();

  public void ensureCanEditPoll() throws AuthenticationException;

  public boolean canEditOwnPoll(int forumID);

  public void ensureCanEditOwnPoll(int forumID) throws AuthenticationException;

  public boolean canAddOwnThreadPoll(int forumID);

  public void ensureCanAddOwnThreadPoll(int forumID) throws AuthenticationException;

  public boolean canDeletePoll(int forumID);

  public void ensureCanDeletePoll(int forumID) throws AuthenticationException;

  public boolean canDeletePoll();

  public void ensureCanDeletePoll() throws AuthenticationException;

  public boolean canAddAttachment(int forumID);

  public void ensureCanAddAttachment(int forumID) throws AuthenticationException;

  public boolean canGetAttachment(int forumID);

  public void ensureCanGetAttachment(int forumID) throws AuthenticationException;

  public boolean canModerateThread(int forumID);

  public void ensureCanModerateThread(int forumID) throws AuthenticationException;

  /**************************************************************************
   * The below methods are used to check global CHANNEL permissions
   **************************************************************************/

  public boolean canAddChannel();

  public void ensureCanAddChannel() throws AuthenticationException;

  public boolean canEditChannel();

  public void ensureCanEditChannel() throws AuthenticationException;

  public boolean canDeleteChannel();

  public void ensureCanDeleteChannel() throws AuthenticationException;

  public boolean canHaveRoleEditor();

  public void ensureCanHaveRoleEditor() throws AuthenticationException;

  public boolean canHaveRoleChiefEditor();

  public void ensureCanHaveRoleChiefEditor() throws AuthenticationException;

  public boolean canManageCMS();

  public void ensureCanManageCMS() throws AuthenticationException;

  public boolean canManageInfoInDay();

  public void ensureCanManageInfoInDay() throws AuthenticationException;

  public boolean canEditCDSLayout();

  public void ensureCanEditCDSLayout() throws AuthenticationException;

  /**************************************************************************
   * The below methods are used to check individual STEP And CHANNEL permissions
   **************************************************************************/
  // ---------------------------------------------------------------------
  public boolean isMemberInGroup(String groupName);

  public void ensureIsMemberInGroup(String groupName) throws AuthenticationException;

  public boolean canWriteContent(int channelID);

  public void ensureCanWriteContent(int channelID) throws AuthenticationException;

  public boolean canWriteContentInStepWithChannel(int stepID, int channelID);

  public void ensureCanWriteContentInStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canEditContent(int channelID);

  public void ensureCanEditContent(int channelID) throws AuthenticationException;

  // ---------------------------------------------------------------------
  public boolean canEditContentInStepWithChannel(int stepID, int channelID);

  public void ensureCanEditContentInStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canApproveContent(int channelID);

  public void ensureCanApproveContent(int channelID) throws AuthenticationException;

  public boolean canPublishContent(int channelID);

  public void ensureCanPublishContent(int channelID) throws AuthenticationException;

  public boolean canDeleteContent(int channelID);

  public void ensureCanDeleteContent(int channelID) throws AuthenticationException;

  // ---------------------------------------------------------------------
  public boolean canDeleteContentInStepWithChannel(int stepID, int channelID);

  public void ensureCanDeleteContentInStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canViewContentInStepWithChannel(int stepID, int channelID);

  public void ensureCanViewContentInStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canViewContentStepWithChannel(int stepID, int channelID);

  public void ensureCanViewContentStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canGoToContentStepWithChannel(int stepID, int channelID);

  public void ensureCanGoToContentStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canViewContentStepWithAnyChannel(int stepID);

  public void ensureCanViewContentStepWithAnyChannel(int stepID) throws AuthenticationException;

  public boolean canDeployContentInStepWithChannel(int stepID, int channelID);

  public void ensureCanDeployContentInStepWithChannel(int stepID, int channelID)
      throws AuthenticationException;

  public boolean canViewChartCMS();

  public void ensureCanViewChartCMS() throws AuthenticationException;

  public boolean canAddInterview();

  public void ensureCanAddInterview() throws AuthenticationException;

  public boolean canViewInterview();

  public void ensureCanViewInterview() throws AuthenticationException;

  public boolean canAddToTaxonomy();

  public void ensureCanAddToTaxonomy() throws AuthenticationException;

  public boolean canManageContactUs();

  public void ensureCanManageContactUs() throws AuthenticationException;


  /**************************************************************************
   * The below methods are utility methods to support checking FORUM permission
   **************************************************************************/

  public boolean canEditAtLeastOneForum();

  public void ensureCanEditAtLeastOneForum() throws AuthenticationException;

  public boolean canModerateThreadInAtLeastOneForum();

  public void ensureCanModerateThreadInAtLeastOneForum() throws AuthenticationException;

  public boolean canGetAttachmentInAtLeastOneForum();

  public void ensureCanGetAttachmentInAtLeastOneForum() throws AuthenticationException;

  /**************************************************************************
   * The below methods are utility methods to support checking CHANNEL permission
   **************************************************************************/
  public boolean canWriteContentInAnyChannel();

  public void ensureCanWriteContentInAnyChannel() throws AuthenticationException;

  public boolean canWriteContentInAnyChannelWithAnyStep();

  public void ensureCanWriteContentInAnyChannelWithAnyStep() throws AuthenticationException;

  public boolean canEditContentInAnyChannel();

  public void ensureCanEditContentInAnyChannel() throws AuthenticationException;

  public boolean canApproveContentInAnyChannel();

  public void ensureCanApproveContentInAnyChannel() throws AuthenticationException;

  public boolean canPublishContentInAnyChannel();

  public void ensureCanPublishContentInAnyChannel() throws AuthenticationException;

  public boolean canDeployContentWithStepInAtLeastOneChannel(int stepID);

  public void ensureCanDeployContentWithStepInAtLeastOneChannel(int stepID)
      throws AuthenticationException;

  /**
   * Check if user can access workflow or not.
   */
  public boolean canViewAnyContentStep();

  public void ensureCanViewAnyContentStep() throws AuthenticationException;

  /**************************************************************************
   * The below methods are used to check global Ads permissions
   **************************************************************************/

  public boolean canManageAds();

  public void ensureCanManageAds() throws AuthenticationException;

  public boolean canManageForumAdvertisement();

  public void ensureCanManageForumAdvertisement() throws AuthenticationException;

  public boolean canAddZone();

  public void ensureCanAddZone() throws AuthenticationException;

  public boolean canEditZone();

  public void ensureCanEditZone() throws AuthenticationException;

  public boolean canDeleteZone();

  public void ensureCanDeleteZone() throws AuthenticationException;

  public boolean canAddBanner();

  public void ensureCanAddBanner() throws AuthenticationException;

  public boolean canEditBanner();

  public void ensureCanEditBanner() throws AuthenticationException;

  public boolean canDeleteBanner();

  public void ensureCanDeleteBanner() throws AuthenticationException;

  public boolean canViewZone();

  public void ensureCanViewZone() throws AuthenticationException;

  public boolean canViewBanner();

  public void ensureCanViewBanner() throws AuthenticationException;

  public boolean canUploadMedia();

  public void ensureCanUploadMedia() throws AuthenticationException;

  public boolean canViewChartAds();

  public void ensureCanViewChartAds() throws AuthenticationException;

}
