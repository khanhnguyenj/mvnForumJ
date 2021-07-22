/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/AbstractPermission.java,v 1.64 2009/11/06 09:17:00 nhanld Exp $
 * $Author: nhanld $
 * $Revision: 1.64 $
 * $Date: 2009/11/06 09:17:00 $
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
package com.mvnforum.auth;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.NotLoginException;

public abstract class AbstractPermission implements MVNForumPermission {

/**************************************************************************
 * global permissions variables
 **************************************************************************/
    protected boolean authenticated        = false;

    protected boolean activated            = false;

    protected boolean login                = false;

    protected boolean adminSystem            = false;

    protected boolean addForum               = false;

    protected boolean addCategory            = false;

    protected boolean editCategory           = false;

    protected boolean deleteCategory         = false;

    protected boolean sendMail               = false;

    protected boolean moderateUser           = false;

    protected boolean useAvatar              = false;

    protected boolean useMessage             = false;

    protected boolean addMessageAttachment   = false;

    protected boolean useAlbum               = false;

    protected boolean manageOrphanPoll       = false;

    protected boolean manageAlbumItemPoll    = false;

    protected boolean setPollToAnonymousType = false;
    
    protected boolean manageVote             = false;

    protected boolean haveRoleEditor         = false;

    protected boolean haveRoleChiefEditor    = false;
    
    protected boolean manageCMS              = false;
    
    protected boolean manageInfoInDay        = false;
    
    protected boolean editCDSLayout          = false;
    
    protected boolean addInterview           = false;
    
    protected boolean viewInterview          = false;
    
    protected boolean addToTaxonomy          = false;
    
    protected boolean manageContactUs        = false;

    protected boolean viewChartCMS           = false;
    
    protected boolean viewChartAds           = false;

/**************************************************************************
 * individual forum permissions variables
 **************************************************************************/
    protected ForumListPermission editForum      = new ForumListPermission();

    protected ForumListPermission deleteForum    = new ForumListPermission();

    protected ForumListPermission assignToForum  = new ForumListPermission();

    protected ForumListPermission readPost       = new ForumListPermission();

    protected ForumListPermission addThread      = new ForumListPermission();

    protected ForumListPermission addPost        = new ForumListPermission();

    protected ForumListPermission editPost       = new ForumListPermission();

    protected ForumListPermission deletePost     = new ForumListPermission();

    protected ForumListPermission addPoll        = new ForumListPermission();

    protected ForumListPermission editPoll       = new ForumListPermission();

    protected ForumListPermission deletePoll     = new ForumListPermission();

    protected ForumListPermission addAttachment  = new ForumListPermission();

    protected ForumListPermission getAttachment  = new ForumListPermission();

    protected ForumListPermission moderateThread = new ForumListPermission();

    protected ForumListPermission editOwnPost    = new ForumListPermission();

    protected ForumListPermission editOwnPoll    = new ForumListPermission();

    protected ForumListPermission addOwnThreadPoll = new ForumListPermission();

/**************************************************************************
 * global CHANNEL permissions variables
 **************************************************************************/
    protected boolean addChannel           = false;

    protected boolean editChannel          = false;

    protected boolean deleteChannel        = false;

/**************************************************************************
 * individual STEP And CHANNEL permissions variables
 **************************************************************************/
    protected ChannelListPermission writeContent                            = new ChannelListPermission();

    protected StepAndChannelListPermission writeContentInStepWithChannel    = new StepAndChannelListPermission();

    protected ChannelListPermission editContent                             = new ChannelListPermission();

    //---------------------------------------------------------------------
    protected StepAndChannelListPermission editContentInStepWithChannel     = new StepAndChannelListPermission();

    protected ChannelListPermission approveContent                          = new ChannelListPermission();

    protected ChannelListPermission publishContent                          = new ChannelListPermission();

    protected ChannelListPermission deleteContent                           = new ChannelListPermission();

    //---------------------------------------------------------------------
    protected StepAndChannelListPermission deleteContentInStepWithChannel   = new StepAndChannelListPermission();

    protected StepAndChannelListPermission viewContentInStepWithChannel     = new StepAndChannelListPermission();

    protected StepAndChannelListPermission viewContentStepWithChannel       = new StepAndChannelListPermission();

    protected StepAndChannelListPermission goToContentStepWithChannel       = new StepAndChannelListPermission();

    protected StepAndChannelListPermission deployContentInStepWithChannel   = new StepAndChannelListPermission();

/**************************************************************************
 * global Ads permissions variables
 **************************************************************************/
    protected boolean manageAds                    = false;
    
    protected boolean manageForumAdvertisement     = false;

    protected boolean addZone                      = false;

    protected boolean editZone                     = false;

    protected boolean deleteZone                   = false;

    protected boolean addBanner                    = false;

    protected boolean editBanner                   = false;

    protected boolean deleteBanner                 = false;

    protected boolean viewZone                     = false;

    protected boolean viewBanner                   = false;

    protected boolean uploadMedia                  = false;

    /**
     * constructor
     */
    protected AbstractPermission() {
    }

/**************************************************************************
 * The below methods are static methods
 **************************************************************************/

    public static String getDescription(int permission) throws BadInputException {
        String desc = "";

        switch (permission) {
            /**************************************************************************
             * Combined permissions
             **************************************************************************/
            case PERMISSION_SYSTEM_ADMIN:
                desc = "System Admin (Combined Permission)";
                break;

            case PERMISSION_FORUM_ADMIN:
                desc = "Forum Admin (Combined Permission)";
                break;

            case PERMISSION_FORUM_MODERATOR:
                desc = "Forum Moderator (Combined Permission)";
                break;

            case PERMISSION_LIMITED_USER:
                desc = "Limited User (Combined Permission)";
                break;

            case PERMISSION_NORMAL_USER:
                desc = "Normal User (Combined Permission)";
                break;

            case PERMISSION_POWER_USER:
                desc = "Power User (Combined Permission)";
                break;

            /**************************************************************************
             * Individual permissions
             **************************************************************************/
            case PERMISSION_LOGIN:
                desc = "Can Login";
                break;

            //case PERMISSION_ADMIN_SYSTEM:
            //    desc = "Admin System";
            //    break;

            case PERMISSION_ADD_FORUM:
                desc = "Add Forum";
                break;

            case PERMISSION_EDIT_FORUM:
                desc = "Edit Forum";
                break;

            case PERMISSION_DELETE_FORUM:
                desc = "Delete Forum";
                break;

            case PERMISSION_ASSIGN_TO_FORUM:
                desc = "Assign To Forum";
                break;

            case PERMISSION_ADD_CATEGORY:
                desc = "Add Category";
                break;

            case PERMISSION_EDIT_CATEGORY:
                desc = "Edit Category";
                break;

            case PERMISSION_DELETE_CATEGORY:
                desc = "Delete Category";
                break;

            case PERMISSION_SEND_MAIL:
                desc = "Send Mail";
                break;

            case PERMISSION_MODERATE_USER:
                desc = "Moderate User";
                break;

            case PERMISSION_BYPASS_PRIVATE_FORUM:
                desc = "Bypass Private Forum";
                break;

            case PERMISSION_USE_MESSAGE:
                desc = "Use Private Message";
                break;

            case PERMISSION_ADD_MESSAGE_ATTACHMENT:
                desc = "Upload Attachment in Private Message";
                break;

            case PERMISSION_USE_AVATAR:
                desc = "Use Avatar";
                break;

            case PERMISSION_USE_ALBUM:
                desc = "Use Album";
                break;

            case PERMISSION_MANAGE_ORPHAN_POLL:
                desc = "Manage Orphan Poll";
                break;

            case PERMISSION_MANAGE_ALBUMITEM_POLL:
                desc = "Manage Album Item Poll";
                break;

            case PERMISSION_SET_POLL_TO_ANONYMOUS_TYPE:
                desc = "Set Poll To Anonymous Type";
                break;
                
            case PERMISSION_MANAGE_VOTE:
                desc = "Manage Vote";
                break;

            case PERMISSION_READ_POST:
                desc = "Read Post";
                break;

            case PERMISSION_ADD_THREAD:
                desc = "Add Thread";
                break;

            case PERMISSION_ADD_POST:
                desc = "Add Post";
                break;

            case PERMISSION_EDIT_POST:
                desc = "Edit Post";
                break;

            case PERMISSION_DELETE_POST:
                desc = "Delete Post";
                break;

            case PERMISSION_ADD_POLL:
                desc = "Add Poll";
                break;

            case PERMISSION_EDIT_POLL:
                desc = "Edit Poll";
                break;

            case PERMISSION_EDIT_OWN_POLL:
                desc = "Edit Own Poll";
                break;

            case PERMISSION_ADD_OWN_THREAD_POLL:
                desc = "Add Own Thread Poll";
                break;

            case PERMISSION_DELETE_POLL:
                desc = "Delete Poll";
                break;

            case PERMISSION_ADD_ATTACHMENT:
                desc = "Add Attachment";
                break;

            case PERMISSION_GET_ATTACHMENT:
                desc = "Get Attachment";
                break;

            case PERMISSION_MODERATE_THREAD:
                desc = "Moderate Thread";
                break;

            case PERMISSION_EDIT_OWN_POST:
                desc = "Edit Own Post";
                break;

            /**************************************************************************
             * Individual permissions for CHANNEL
             **************************************************************************/
            case PERMISSION_CMS_ADD_CHANNEL:
                desc = "Add Channel";
                break;

            case PERMISSION_CMS_EDIT_CHANNEL:
                desc = "Edit Channel";
                break;

            case PERMISSION_CMS_DELETE_CHANNEL:
                desc = "Delete Channel";
                break;
                
            case PERMISSION_CMS_ADD_INTERVIEW:
                desc = "Add Interview";
                break;
                
            case PERMISSION_CMS_VIEW_INTERVIEW:
                desc = "View Interview";
                break;
                
            case PERMISSION_CMS_ADD_TO_TAXONOMY:
                desc = "Add To Taxonomy";
                break;
                
            case PERMISSION_CMS_MANAGE_CONTACT_US:
                desc = "Manage Contact Us";
                break;

            case PERMISSION_CMS_HAVE_ROLE_EDITOR:
                desc = "Have Role Editor";
                break;

            case PERMISSION_CMS_HAVE_ROLE_CHIEF_EDITOR:
                desc = "Have Role Chief Editor";
                break;

            case PERMISSION_CMS_MANAGE_INFO_IN_DAY:
                desc = "Manage Info In Day";
                break;

            case PERMISSION_CMS_MANAGE_CMS_SYSTEM:
                desc = "Manage CMS System";
                break;

            case PERMISSION_CMS_EDIT_CDS_LAYOUT:
                desc = "Edit CDS Layout";
                break;

            case PERMISSION_CMS_WRITE_CONTENT:
                desc = "Write Content";
                break;

            case PERMISSION_CMS_EDIT_CONTENT:
                desc = "Edit Content";
                break;

            case PERMISSION_CMS_APPROVE_CONTENT:
                desc = "Approve Content";
                break;

            case PERMISSION_CMS_PUBLISH_CONTENT:
                desc = "Publish Content";
                break;

            case PERMISSION_CMS_DELETE_CONTENT:
                desc = "Delete Content";
                break;

            //---------------------------------------------------------------------
            case PERMISSION_CMS_VIEW_CONTENT:
                desc = "View Content";
                break;

            case PERMISSION_CMS_DEPLOY_CONTENT:
                desc = "Deploy Content";
                break;

            case PERMISSION_CMS_VIEW_STEP:
                desc = "View Step";
                break;

            case PERMISSION_CMS_GOTO_STEP:
                desc = "Go to Step";
                break;

            case PERMISSION_CMS_VIEW_CHART:
                desc = "View Chart mvnPublish";
                break;

            /**************************************************************************
             * Individual permissions for Ads
             **************************************************************************/
            case PERMISSION_ADS_MANAGE_ADS:
                desc = "Manage the Ads System";
                break;

            case PERMISSION_ADS_MANAGE_FORUM_ADVERTISEMENT:
                desc = "Manage Forum Advertisement";
                break;

            case PERMISSION_ADS_ADD_ZONE:
                desc = "Add Zone";
                break;

            case PERMISSION_ADS_EDIT_ZONE:
                desc = "Edit Zone";
                break;

            case PERMISSION_ADS_DELETE_ZONE:
                desc = "Delete Zone";
                break;

            case PERMISSION_ADS_ADD_BANNER:
                desc = "Add Banner";
                break;

            case PERMISSION_ADS_EDIT_BANNER:
                desc = "Edit Banner";
                break;

            case PERMISSION_ADS_DELETE_BANNER:
                desc = "Delete Banner";
                break;

            case PERMISSION_ADS_VIEW_ZONE:
                desc = "View Zone";
                break;

            case PERMISSION_ADS_VIEW_BANNER:
                desc = "View Banner";
                break;

            case PERMISSION_ADS_UPLOAD_MEDIA:
                desc = "Upload Media";
                break;
              
            case PERMISSION_ADS_VIEW_CHART:
                desc = "View Chart mvnAd";
                break;
                
            default:
                throw new BadInputException("Currently do not support permission = " + permission);
        }//switch

        return desc;
    }

/**************************************************************************
 * Special permissions methods
 **************************************************************************/

    public boolean isAuthenticated() {
        return authenticated;
    }
    public void ensureIsAuthenticated() throws AuthenticationException {
        if (authenticated == false) {
            throw new AuthenticationException(NotLoginException.NOT_LOGIN);
        }
    }

    public boolean isActivated() {
        return activated;
    }
    public void ensureIsActivated() throws AuthenticationException {
        if (activated == false) {
            throw new AuthenticationException(NotLoginException.NOT_ACTIVATED);
        }
    }

    public static boolean isCombinedPerm(int permission) {

        int[] allCombinedPerms = MVNForumPermission.forumCombinedPermissionArray;
        for (int i = 0; i < allCombinedPerms.length; i++) {
            if (permission == allCombinedPerms[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIndividualPerm(int permission) {
        int[] allIndividualPerms = MVNForumPermission.forumIndividualPermissionArray;
        for (int i = 0; i < allIndividualPerms.length; i++) {
            if (permission == allIndividualPerms[i]) {
                return true;
            }
        }
        return false;
    }

/**************************************************************************
 * global permissions methods
 **************************************************************************/

    public boolean canLogin() {
        return login;
    }
    public void ensureCanLogin() throws AuthenticationException {
        if (login == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAdminSystem() {
        return adminSystem;
    }
    public void ensureCanAdminSystem() throws AuthenticationException {
        if (adminSystem == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddForum() {
        return addForum;
    }
    public void ensureCanAddForum() throws AuthenticationException {
        if (addForum == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddCategory() {
        return addCategory;
    }
    public void ensureCanAddCategory() throws AuthenticationException {
        if (addCategory == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditCategory() {
        return editCategory;
    }
    public void ensureCanEditCategory() throws AuthenticationException {
        if (editCategory == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteCategory() {
        return deleteCategory;
    }
    public void ensureCanDeleteCategory() throws AuthenticationException {
        if (deleteCategory == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canSendMail() {
        return sendMail;
    }
    public void ensureCanSendMail() throws AuthenticationException {
        if (sendMail == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canModerateUser() {
        return moderateUser;
    }
    public void ensureCanModerateUser() throws AuthenticationException {
        if (moderateUser == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canUseAvatar() {
        return useAvatar;
    }
    public void ensureCanUseAvatar() throws AuthenticationException {
        if (useAvatar == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canUseMessage() {
        return useMessage;
    }
    public void ensureCanUseMessage() throws AuthenticationException {
        if (useMessage == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddMessageAttachment() {
        return addMessageAttachment;
    }
    public void ensureCanAddMessageAttachment() throws AuthenticationException {
        if (addMessageAttachment == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canUseAlbum() {
        return useAlbum;
    }
    public void ensureCanUseAlbum() throws AuthenticationException {
        if (useAlbum == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canManageOrphanPoll() {
        return manageOrphanPoll;
    }
    public void ensureCanManageOrphanPoll() throws AuthenticationException {
        if (canManageOrphanPoll() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canManageAlbumItemPoll() {
        return manageAlbumItemPoll;
    }
    public void ensureCanManageAlbumItemPoll() throws AuthenticationException {
        if (canManageAlbumItemPoll() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canSetPollToAnonymousType() {
        return setPollToAnonymousType;
    }
    public void ensureCanSetPollToAnonymousType() throws AuthenticationException {
        if (canSetPollToAnonymousType() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canManageVote() {
        return manageVote;
    }
    public void ensureCanManageVote() throws AuthenticationException {
        if (canManageVote() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

/**************************************************************************
 * individual forum permissions methods
 **************************************************************************/
    // For Forum Admin only
    public boolean canEditForum(int forumID) {
        return editForum.hasPermission(forumID);
    }
    public void ensureCanEditForum(int forumID) throws AuthenticationException {
        if (canEditForum(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteForum(int forumID) {
        return deleteForum.hasPermission(forumID);
    }
    public void ensureCanDeleteForum(int forumID) throws AuthenticationException {
        if (canDeleteForum(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAssignToForum(int forumID) {
        return assignToForum.hasPermission(forumID);
    }
    public void ensureCanAssignToForum(int forumID) throws AuthenticationException {
        if (canAssignToForum(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    // Moderator and below permission
    public boolean canReadPost(int forumID) {
        return readPost.hasPermission(forumID);
    }
    public void ensureCanReadPost(int forumID) throws AuthenticationException {
        if (canReadPost(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddThread(int forumID) {
        return addThread.hasPermission(forumID);
    }
    public void ensureCanAddThread(int forumID) throws AuthenticationException {
        if (canAddThread(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddPost(int forumID) {
        return addPost.hasPermission(forumID);
    }
    public void ensureCanAddPost(int forumID) throws AuthenticationException {
        if (canAddPost(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditPost(int forumID) {
        return editPost.hasPermission(forumID);
    }
    public void ensureCanEditPost(int forumID) throws AuthenticationException {
        if (canEditPost(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditOwnPost(int forumID) {
        return editOwnPost.hasPermission(forumID);
    }
    public void ensureCanEditOwnPost(int forumID) throws AuthenticationException {
        if (canEditOwnPost(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeletePost(int forumID) {
        return deletePost.hasPermission(forumID);
    }
    public void ensureCanDeletePost(int forumID) throws AuthenticationException {
        if (canDeletePost(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddPoll(int forumID) {
        return addPoll.hasPermission(forumID);
    }
    public void ensureCanAddPoll(int forumID) throws AuthenticationException {
        if (canAddPoll(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddPoll() {
        return addPoll.isGlobalPermission();
    }
    public void ensureCanAddPoll() throws AuthenticationException {
        if (canAddPoll() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditPoll(int forumID) {
        return editPoll.hasPermission(forumID);
    }
    public void ensureCanEditPoll(int forumID) throws AuthenticationException {
        if (canEditPoll(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditPoll() {
        return editPoll.isGlobalPermission();
    }
    public void ensureCanEditPoll() throws AuthenticationException {
        if (canEditPoll() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditOwnPoll(int forumID) {
        return editOwnPoll.hasPermission(forumID);
    }
    public void ensureCanEditOwnPoll(int forumID) throws AuthenticationException {
        if (canEditOwnPoll(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddOwnThreadPoll(int forumID) {
        return addOwnThreadPoll.hasPermission(forumID);
    }
    public void ensureCanAddOwnThreadPoll(int forumID) throws AuthenticationException {
        if (canAddOwnThreadPoll(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeletePoll(int forumID) {
        return deletePoll.hasPermission(forumID);
    }
    public void ensureCanDeletePoll(int forumID) throws AuthenticationException {
        if (canDeletePoll(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeletePoll() {
        return deletePoll.isGlobalPermission();
    }
    public void ensureCanDeletePoll() throws AuthenticationException {
        if (canDeletePoll() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddAttachment(int forumID) {
        return addAttachment.hasPermission(forumID);
    }
    public void ensureCanAddAttachment(int forumID) throws AuthenticationException {
        if (canAddAttachment(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canGetAttachment(int forumID) {
        return getAttachment.hasPermission(forumID);
    }
    public void ensureCanGetAttachment(int forumID) throws AuthenticationException {
        if (canGetAttachment(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canModerateThread(int forumID) {
        return moderateThread.hasPermission(forumID);
    }
    public void ensureCanModerateThread(int forumID) throws AuthenticationException {
        if (canModerateThread(forumID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    /**************************************************************************
     * individual forum permissions methods
     **************************************************************************/
    // For Forum Admin only
    public boolean canEditAtLeastOneForum() {
        return editForum.hasPermssionInAtLeastOneForum();
    }
    public void ensureCanEditAtLeastOneForum() throws AuthenticationException {
        if (canEditAtLeastOneForum() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    // For moderation thread
    public boolean canModerateThreadInAtLeastOneForum() {
        return moderateThread.hasPermssionInAtLeastOneForum();
    }
    public void ensureCanModerateThreadInAtLeastOneForum() throws AuthenticationException {
        if (canModerateThreadInAtLeastOneForum() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canGetAttachmentInAtLeastOneForum() {
        return getAttachment.hasPermssionInAtLeastOneForum();
    }

    public void ensureCanGetAttachmentInAtLeastOneForum() throws AuthenticationException {
        if (canGetAttachmentInAtLeastOneForum() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

/**************************************************************************
 * individual CMS permissions methods
 **************************************************************************/
    public boolean canAddChannel() {
        return addChannel;
    }
    public void ensureCanAddChannel() throws AuthenticationException {
        if (addChannel == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditChannel() {
        return editChannel;
    }
    public void ensureCanEditChannel() throws AuthenticationException {
        if (editChannel == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteChannel() {
        return deleteChannel;
    }
    public void ensureCanDeleteChannel() throws AuthenticationException {
        if (deleteChannel == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddInterview() {
        return addInterview;
    }
    public void ensureCanAddInterview() throws AuthenticationException {
        if (addInterview == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canViewInterview() {
        return viewInterview;
    }
    public void ensureCanViewInterview() throws AuthenticationException {
        if (viewInterview == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canAddToTaxonomy() {
        return addToTaxonomy;
    }
    public void ensureCanAddToTaxonomy() throws AuthenticationException {
        if (addToTaxonomy == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canManageContactUs() {
        return manageContactUs;
    }
    public void ensureCanManageContactUs() throws AuthenticationException {
        if (manageContactUs == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canHaveRoleEditor() {
        return haveRoleEditor;
    }
    public void ensureCanHaveRoleEditor() throws AuthenticationException {
        if (haveRoleEditor == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canHaveRoleChiefEditor() {
        return haveRoleChiefEditor;
    }
    public void ensureCanHaveRoleChiefEditor() throws AuthenticationException {
        if (haveRoleChiefEditor == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canManageCMS() {
        return manageCMS;
    }
    public void ensureCanManageCMS() throws AuthenticationException {
        if (manageCMS == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canManageInfoInDay() {
        return manageInfoInDay;
    }
    public void ensureCanManageInfoInDay() throws AuthenticationException {
        if (manageInfoInDay == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditCDSLayout() {
        return editCDSLayout;
    }
    public void ensureCanEditCDSLayout() throws AuthenticationException {
        if (editCDSLayout == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewChartCMS() {
        return viewChartCMS;
    }
    public void ensureCanViewChartCMS() throws AuthenticationException {
        if (viewChartCMS == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
/**************************************************************************
 * The below methods are used to check individual Step And Channel permissions
 **************************************************************************/
    public boolean canWriteContent(int channelID) {
        return writeContent.hasPermission(channelID);
    }
    public void ensureCanWriteContent(int channelID) throws AuthenticationException {
        if (canWriteContent(channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canWriteContentInStepWithChannel(int stepID, int channelID) {
        //TODO: notice this step
        return writeContentInStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanWriteContentInStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canWriteContentInStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditContent(int channelID) {
        return editContent.hasPermission(channelID);
    }
    public void ensureCanEditContent(int channelID) throws AuthenticationException {
        if (canEditContent(channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    //---------------------------------------------------------------------
    public boolean canEditContentInStepWithChannel(int stepID, int channelID) {
        return editContentInStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanEditContentInStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canEditContentInStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canApproveContent(int channelID) {
        return approveContent.hasPermission(channelID);
    }
    public void ensureCanApproveContent(int channelID) throws AuthenticationException {
        if (canApproveContent(channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canPublishContent(int channelID) {
        return publishContent.hasPermission(channelID);
    }
    public void ensureCanPublishContent(int channelID) throws AuthenticationException {
        if (canPublishContent(channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteContent(int channelID) {
        return deleteContent.hasPermission(channelID);
    }
    public void ensureCanDeleteContent(int channelID) throws AuthenticationException {
        if (canDeleteContent(channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    //---------------------------------------------------------------------
    public boolean canDeleteContentInStepWithChannel(int stepID, int channelID) {
        return deleteContentInStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanDeleteContentInStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canDeleteContentInStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewContentInStepWithChannel(int stepID, int channelID) {
        return viewContentInStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanViewContentInStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canViewContentInStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewContentStepWithAnyChannel(int stepID) {
        return viewContentStepWithChannel.hasPermssionInStepInAtLeastOneChannel(stepID);
    }
    public void ensureCanViewContentStepWithAnyChannel(int stepID) throws AuthenticationException {
        if (canViewContentStepWithAnyChannel(stepID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewContentStepWithChannel(int stepID, int channelID) {
        return viewContentStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanViewContentStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canViewContentStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canGoToContentStepWithChannel(int stepID, int channelID) {
        return goToContentStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanGoToContentStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canGoToContentStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeployContentInStepWithChannel(int stepID, int channelID) {
        return deployContentInStepWithChannel.hasPermission(stepID, channelID);
    }
    public void ensureCanDeployContentInStepWithChannel(int stepID, int channelID) throws AuthenticationException {
        if (canDeployContentInStepWithChannel(stepID, channelID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    /**************************************************************************
     * individual CHANNEL permissions methods
     **************************************************************************/
    public boolean canWriteContentInAnyChannel() {
        return writeContent.hasPermssionInAnyChannels();
    }
    public void ensureCanWriteContentInAnyChannel() throws AuthenticationException {
        if (canWriteContentInAnyChannel() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canWriteContentInAnyChannelWithAnyStep() {
        return writeContentInStepWithChannel.hasPermssionInAtLeastOneChannelOrStep();
    }
    public void ensureCanWriteContentInAnyChannelWithAnyStep() throws AuthenticationException {
        if (canWriteContentInAnyChannelWithAnyStep() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditContentInAnyChannel() {
        return editContentInStepWithChannel.hasPermssionInAtLeastOneChannelOrStep();
    }
    public void ensureCanEditContentInAnyChannel() throws AuthenticationException {
        if (canEditContentInAnyChannel() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canApproveContentInAnyChannel() {
        return approveContent.hasPermssionInAnyChannels();
    }
    public void ensureCanApproveContentInAnyChannel() throws AuthenticationException {
        if (canApproveContentInAnyChannel() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canPublishContentInAnyChannel() {
        return publishContent.hasPermssionInAnyChannels();
    }
    public void ensureCanPublishContentInAnyChannel() throws AuthenticationException {
        if (canPublishContentInAnyChannel() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeployContentWithStepInAtLeastOneChannel(int stepID) {
        return deployContentInStepWithChannel.hasPermssionInStepInAtLeastOneChannel(stepID);
    }
    public void ensureCanDeployContentWithStepInAtLeastOneChannel(int stepID) throws AuthenticationException {
        if (canDeployContentWithStepInAtLeastOneChannel(stepID) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewAnyContentStep() {
        return viewContentStepWithChannel.hasPermssionInAtLeastOneChannelOrStep();
    }
    public void ensureCanViewAnyContentStep() throws AuthenticationException {
        if (canViewAnyContentStep() == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

/**************************************************************************
 * The below methods are used to check global Ads permissions
 **************************************************************************/

    public boolean canManageAds() {
        return manageAds;
    }
    public void ensureCanManageAds() throws AuthenticationException {
        if (manageAds == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canManageForumAdvertisement() {
        return manageForumAdvertisement;
    }
    public void ensureCanManageForumAdvertisement() throws AuthenticationException {
        if (manageForumAdvertisement == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddZone() {
        return addZone;
    }
    public void ensureCanAddZone() throws AuthenticationException {
        if (addZone == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditZone() {
        return editZone;
    }
    public void ensureCanEditZone() throws AuthenticationException {
        if (editZone == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteZone() {
        return deleteZone;
    }
    public void ensureCanDeleteZone() throws AuthenticationException {
        if (deleteZone == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canAddBanner() {
        return addBanner;
    }
    public void ensureCanAddBanner() throws AuthenticationException {
        if (addBanner == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canEditBanner() {
        return editBanner;
    }
    public void ensureCanEditBanner() throws AuthenticationException {
        if (editBanner == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canDeleteBanner() {
        return deleteBanner;
    }
    public void ensureCanDeleteBanner() throws AuthenticationException {
        if (deleteBanner == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewZone() {
        return viewZone;
    }
    public void ensureCanViewZone() throws AuthenticationException {
        if (viewZone == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canViewBanner() {
        return viewBanner;
    }
    public void ensureCanViewBanner() throws AuthenticationException {
        if (viewBanner == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

    public boolean canUploadMedia() {
        return uploadMedia;
    }
    public void ensureCanUploadMedia() throws AuthenticationException {
        if (uploadMedia == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
    public boolean canViewChartAds() {
        return viewChartAds;
    }
    public void ensureCanViewChartAds() throws AuthenticationException {
        if (viewChartAds == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }
    
}