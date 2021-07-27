/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/MVNForumPermissionImpl.java,v 1.59 2009/11/06 09:17:00 nhanld Exp $
 * $Author: nhanld $
 * $Revision: 1.59 $
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

import java.util.*;

import net.myvietnam.mvncore.exception.NotLoginException;
import net.myvietnam.mvncore.util.AssertionUtil;

import com.mvnforum.db.GroupsBean;

/**
 * This class is private in the package
 */
class MVNForumPermissionImpl extends AbstractPermission implements MVNForumPermission {

    private Collection groupsContainMember = new ArrayList();

    MVNForumPermissionImpl() {
    }

    public Collection getGroupsContainMember() {
        return groupsContainMember;
    }

    public void setGroupsContainMember(Collection groupsContainMember) {
        this.groupsContainMember = groupsContainMember;
    }

    private void setBypassPrivateForum(boolean bypass) {

        editForum.setBypassPrivateForum(bypass);
        deleteForum.setBypassPrivateForum(bypass);
        assignToForum.setBypassPrivateForum(bypass);

        readPost.setBypassPrivateForum(bypass);
        addThread.setBypassPrivateForum(bypass);
        addPost.setBypassPrivateForum(bypass);
        editPost.setBypassPrivateForum(bypass);
        editOwnPost.setBypassPrivateForum(bypass);
        deletePost.setBypassPrivateForum(bypass);
        addPoll.setBypassPrivateForum(bypass);
        editPoll.setBypassPrivateForum(bypass);
        editOwnPoll.setBypassPrivateForum(bypass);
        addOwnThreadPoll.setBypassPrivateForum(bypass);
        deletePoll.setBypassPrivateForum(bypass);
        addAttachment.setBypassPrivateForum(bypass);
        getAttachment.setBypassPrivateForum(bypass);
        moderateThread.setBypassPrivateForum(bypass);
    }

    private void setBypassPrivateChannel(boolean bypass) {

        writeContent.setBypassPrivateChannel(bypass);
        writeContentInStepWithChannel.setBypassPrivateChannel(bypass);
        editContent.setBypassPrivateChannel(bypass);
        editContentInStepWithChannel.setBypassPrivateChannel(bypass);
        approveContent.setBypassPrivateChannel(bypass);
        publishContent.setBypassPrivateChannel(bypass);
        deleteContent.setBypassPrivateChannel(bypass);
        deleteContentInStepWithChannel.setBypassPrivateChannel(bypass);
        viewContentInStepWithChannel.setBypassPrivateChannel(bypass);
        viewContentStepWithChannel.setBypassPrivateChannel(bypass);
        goToContentStepWithChannel.setBypassPrivateChannel(bypass);
        deployContentInStepWithChannel.setBypassPrivateChannel(bypass);
    }

    /**
     * The ONLY way to set permission, so this MUST have default package access
     */
    void setPermission(int permission) {
        switch (permission) {
            case PERMISSION_AUTHENTICATED:
                authenticated   = true;
                break;

            case PERMISSION_ACTIVATED:
                activated   = true;
                break;
/**************************************************************************
 * Combined permissions, range from 100 to 199
 * A permission in this range is the combination of other permissions
 **************************************************************************/
            case PERMISSION_SYSTEM_ADMIN:
                adminSystem          = true;
                addForum             = true;
                editForum.setAllForumsPermission(true);
                deleteForum.setAllForumsPermission(true);
                assignToForum.setAllForumsPermission(true);
                addCategory            = true;
                editCategory           = true;
                deleteCategory         = true;
                sendMail               = true;
                moderateUser           = true;
                useAvatar              = true;
                useMessage             = true;
                addMessageAttachment   = true;
                useAlbum               = true;
                manageOrphanPoll       = true;
                manageAlbumItemPoll    = true;
                setPollToAnonymousType = true;
                manageVote             = true;
                
                readPost.setAllForumsPermission(true);
                addThread.setAllForumsPermission(true);
                addPost.setAllForumsPermission(true);
                editPost.setAllForumsPermission(true);
                editOwnPost.setAllForumsPermission(true);
                deletePost.setAllForumsPermission(true);
                addPoll.setAllForumsPermission(true);
                editPoll.setAllForumsPermission(true);
                editOwnPoll.setAllForumsPermission(true);
                addOwnThreadPoll.setAllForumsPermission(true);
                deletePoll.setAllForumsPermission(true);
                addAttachment.setAllForumsPermission(true);
                getAttachment.setAllForumsPermission(true);
                moderateThread.setAllForumsPermission(true);

                setBypassPrivateForum(true);

                // CMS permissions
                manageCMS            = true;
                addChannel           = true;
                editChannel          = true;
                deleteChannel        = true;
                haveRoleEditor       = true;
                haveRoleChiefEditor  = true;
                manageInfoInDay      = true;
                editCDSLayout        = true;
                viewChartCMS         = true;
                addInterview         = true; 
                viewInterview        = true;   
                addToTaxonomy        = true;
                manageContactUs      = true;

                writeContent.setAllChannelsPermission(true);
                writeContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                editContent.setAllChannelsPermission(true);
                editContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                approveContent.setAllChannelsPermission(true);
                publishContent.setAllChannelsPermission(true);
                deleteContent.setAllChannelsPermission(true);
                deleteContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                viewContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                viewContentStepWithChannel.setAllStepAndChannelsPermission(true);
                goToContentStepWithChannel.setAllStepAndChannelsPermission(true);
                deployContentInStepWithChannel.setAllStepAndChannelsPermission(true);

                setBypassPrivateChannel(true);

                // Ads permissions
                manageAds            = true;
                manageForumAdvertisement = true;
                addZone              = true;
                editZone             = true;
                deleteZone           = true;
                addBanner            = true;
                editBanner           = true;
                deleteBanner         = true;
                viewZone             = true;
                viewBanner           = true;
                uploadMedia          = true;
                viewChartAds         = true;
                break;

            case PERMISSION_FORUM_ADMIN:
                addForum        = true;
                editForum.setAllForumsPermission(true);
                deleteForum.setAllForumsPermission(true);
                assignToForum.setAllForumsPermission(true);
                addCategory     = true;
                editCategory    = true;
                deleteCategory  = true;

                moderateThread.setAllForumsPermission(true);

                readPost.setAllForumsPermission(true);
                addThread.setAllForumsPermission(true);
                addPost.setAllForumsPermission(true);
                editPost.setAllForumsPermission(true);
                editOwnPost.setAllForumsPermission(true);
                deletePost.setAllForumsPermission(true);
                addPoll.setAllForumsPermission(true);
                editPoll.setAllForumsPermission(true);
                editOwnPoll.setAllForumsPermission(true);
                addOwnThreadPoll.setAllForumsPermission(true);
                deletePoll.setAllForumsPermission(true);
                addAttachment.setAllForumsPermission(true);
                getAttachment.setAllForumsPermission(true);
                break;

            case PERMISSION_FORUM_MODERATOR:
                editForum.setAllForumsPermission(true);
                editCategory    = true;

                moderateThread.setAllForumsPermission(true);

                readPost.setAllForumsPermission(true);
                addThread.setAllForumsPermission(true);
                addPost.setAllForumsPermission(true);
                editPost.setAllForumsPermission(true);
                editOwnPost.setAllForumsPermission(true);
                deletePost.setAllForumsPermission(true);
                addPoll.setAllForumsPermission(true);
                editPoll.setAllForumsPermission(true);
                editOwnPoll.setAllForumsPermission(true);
                addOwnThreadPoll.setAllForumsPermission(true);
                deletePoll.setAllForumsPermission(true);
                addAttachment.setAllForumsPermission(true);
                getAttachment.setAllForumsPermission(true);
                break;

            case PERMISSION_LIMITED_USER:
                readPost.setAllForumsPermission(true);
                /** @todo at the 1.0.0 beta2/beta3/RC1/RC2 release, add post is disable*/
//                addPost         = true;
                break;

            case PERMISSION_NORMAL_USER:
                useAvatar       = true;
                useMessage      = true;

                readPost.setAllForumsPermission(true);
                addThread.setAllForumsPermission(true);
                addPost.setAllForumsPermission(true);
                getAttachment.setAllForumsPermission(true);
                break;

            /**
             * Can:
             * - login, read thread and post, reply to a thread
             * - add thread, use avatar, use private message, get attachment
             * - use attachment, create poll
             */
            case PERMISSION_POWER_USER:
                useAvatar            = true;
                useMessage           = true;
                addMessageAttachment = true;
                useAlbum             = true;

                readPost.setAllForumsPermission(true);
                addThread.setAllForumsPermission(true);
                addPost.setAllForumsPermission(true);
                editOwnPost.setAllForumsPermission(true);
                editOwnPoll.setAllForumsPermission(true);
                addOwnThreadPoll.setAllForumsPermission(true);
                addAttachment.setAllForumsPermission(true);
                getAttachment.setAllForumsPermission(true);
                break;

/**************************************************************************
 * Individual Permissions for global usages, range from 1000 to 2000
 **************************************************************************/
            case PERMISSION_LOGIN:
                login           = true;
                break;
            //case PERMISSION_ADMIN_SYSTEM:
            //    adminSystem     = true;
            //    break;
            case PERMISSION_ADD_FORUM:
                addForum          = true;
                break;
            case PERMISSION_ADD_CATEGORY:
                addCategory     = true;
                break;
            case PERMISSION_EDIT_CATEGORY:
                editCategory    = true;
                break;
            case PERMISSION_DELETE_CATEGORY:
                deleteCategory  = true;
                break;
            case PERMISSION_SEND_MAIL:
                sendMail        = true;
                break;
            case PERMISSION_MODERATE_USER:
                moderateUser    = true;
                break;
            case PERMISSION_BYPASS_PRIVATE_FORUM:
                setBypassPrivateForum(true);
                break;
            case PERMISSION_USE_MESSAGE:
                useMessage      = true;
                break;
            case PERMISSION_ADD_MESSAGE_ATTACHMENT:
                addMessageAttachment = true;
                break;
            case PERMISSION_USE_AVATAR:
                useAvatar       = true;
                break;
            case PERMISSION_USE_ALBUM:
                useAlbum        = true;
                break;

            case PERMISSION_MANAGE_ORPHAN_POLL:
                manageOrphanPoll      = true;
                break;

            case PERMISSION_MANAGE_ALBUMITEM_POLL:
                manageAlbumItemPoll   = true;
                break;

            case PERMISSION_SET_POLL_TO_ANONYMOUS_TYPE:
                setPollToAnonymousType = true;
                break;
                
            case PERMISSION_MANAGE_VOTE:
                manageVote = true;
                break;

/**************************************************************************
 * Individual Permissions that can be applied for individual forum usages,
 * (of course it can be applied to all forums), range from 2000 to 3000
 **************************************************************************/
            case PERMISSION_EDIT_FORUM:
                editForum.setAllForumsPermission(true);
                break;
            case PERMISSION_DELETE_FORUM:
                deleteForum.setAllForumsPermission(true);
                break;
            case PERMISSION_ASSIGN_TO_FORUM:
                assignToForum.setAllForumsPermission(true);
                break;
            case PERMISSION_READ_POST:
                readPost.setAllForumsPermission(true);
                break;
            case PERMISSION_ADD_THREAD:
                addThread.setAllForumsPermission(true);
                break;
            case PERMISSION_ADD_POST:
                addPost.setAllForumsPermission(true);
                break;
            case PERMISSION_EDIT_POST:
                editPost.setAllForumsPermission(true);
                break;
            case PERMISSION_EDIT_OWN_POST:
                editOwnPost.setAllForumsPermission(true);
                break;
            case PERMISSION_DELETE_POST:
                deletePost.setAllForumsPermission(true);
                break;
            case PERMISSION_ADD_POLL:
                addPoll.setAllForumsPermission(true);
                addOwnThreadPoll.setAllForumsPermission(true);
                break;
            case PERMISSION_EDIT_POLL:
                editPoll.setAllForumsPermission(true);
                break;
            case PERMISSION_EDIT_OWN_POLL:
                editOwnPoll.setAllForumsPermission(true);
                break;
            case PERMISSION_ADD_OWN_THREAD_POLL:
                addOwnThreadPoll.setAllForumsPermission(true);
                break;
            case PERMISSION_DELETE_POLL:
                deletePoll.setAllForumsPermission(true);
                break;
            case PERMISSION_ADD_ATTACHMENT:
                addAttachment.setAllForumsPermission(true);
                break;
            case PERMISSION_GET_ATTACHMENT:
                getAttachment.setAllForumsPermission(true);
                break;
            case PERMISSION_MODERATE_THREAD:
                moderateThread.setAllForumsPermission(true);
                break;
                
/**************************************************************************
 * Individual CMS Permissions for global usages
 **************************************************************************/
            case PERMISSION_CMS_MANAGE_CMS_SYSTEM:
                manageCMS           = true;
                addChannel          = true;
                editChannel         = true;
                deleteChannel       = true;
                haveRoleEditor      = true;
                haveRoleChiefEditor = true;
                manageInfoInDay     = true;
                editCDSLayout       = true;
                viewChartCMS        = true;
                addInterview        = true;
                viewInterview       = true;
                addToTaxonomy       = true;
                manageContactUs     = true;

                writeContent.setAllChannelsPermission(true);
                writeContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                editContent.setAllChannelsPermission(true);
                editContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                approveContent.setAllChannelsPermission(true);
                publishContent.setAllChannelsPermission(true);
                deleteContent.setAllChannelsPermission(true);
                deleteContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                viewContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                viewContentStepWithChannel.setAllStepAndChannelsPermission(true);
                goToContentStepWithChannel.setAllStepAndChannelsPermission(true);
                deployContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;

            case PERMISSION_CMS_ADD_CHANNEL:
                addChannel            = true;
                break;
            case PERMISSION_CMS_EDIT_CHANNEL:
                editChannel           = true;
                break;
            case PERMISSION_CMS_DELETE_CHANNEL:
                deleteChannel         = true;
                break;

            case PERMISSION_CMS_HAVE_ROLE_EDITOR:
                haveRoleEditor        = true;
                break;
                
            case PERMISSION_CMS_HAVE_ROLE_CHIEF_EDITOR:
                haveRoleChiefEditor   = true;
                break;

            case PERMISSION_CMS_MANAGE_INFO_IN_DAY:
                manageInfoInDay       = true;
                break;
                
            case PERMISSION_CMS_EDIT_CDS_LAYOUT:
                editCDSLayout         = true;
                break;
                
            case PERMISSION_CMS_VIEW_CHART:
                viewChartCMS          = true;
                break;

            case PERMISSION_CMS_ADD_INTERVIEW:
                addInterview          = true;
                break;
                
            case PERMISSION_CMS_VIEW_INTERVIEW:
                viewInterview         = true;
                break;
            
            case PERMISSION_CMS_ADD_TO_TAXONOMY:
                addToTaxonomy         = true;
                break;
                
            case PERMISSION_CMS_MANAGE_CONTACT_US:
                manageContactUs       = true;
                break;

/**************************************************************************
 * Individual CMS Permissions that can be applied for individual channel usages,
 * (of course it can be applied to all channels)
 **************************************************************************/
            case PERMISSION_CMS_WRITE_CONTENT:
                writeContent.setAllChannelsPermission(true);
                writeContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;
            case PERMISSION_CMS_EDIT_CONTENT:
                editContent.setAllChannelsPermission(true);
                editContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;
            case PERMISSION_CMS_APPROVE_CONTENT:
                approveContent.setAllChannelsPermission(true);
                break;
            case PERMISSION_CMS_PUBLISH_CONTENT:
                publishContent.setAllChannelsPermission(true);
                break;
            case PERMISSION_CMS_DELETE_CONTENT:
                deleteContent.setAllChannelsPermission(true);
                deleteContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;
            case PERMISSION_CMS_VIEW_CONTENT:
                viewContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;

            case PERMISSION_CMS_VIEW_STEP:
                viewContentStepWithChannel.setAllStepAndChannelsPermission(true);
                break;

            case PERMISSION_CMS_GOTO_STEP:
                goToContentStepWithChannel.setAllStepAndChannelsPermission(true);
                break;

            case PERMISSION_CMS_DEPLOY_CONTENT:
                deployContentInStepWithChannel.setAllStepAndChannelsPermission(true);
                break;

/**************************************************************************
 * Individual Ads Permissions for global usages
 **************************************************************************/
            case PERMISSION_ADS_MANAGE_ADS:
                manageAds                = true;
                manageForumAdvertisement = true;
                addZone                  = true;
                editZone                 = true;
                deleteZone               = true;
                addBanner                = true;
                editBanner               = true;
                deleteBanner             = true;
                viewZone                 = true;
                viewBanner               = true;
                uploadMedia              = true;
                viewChartAds             = true;
                break;

            case PERMISSION_ADS_MANAGE_FORUM_ADVERTISEMENT:
                manageForumAdvertisement = true;
                break;

            case PERMISSION_ADS_ADD_ZONE:
                addZone                  = true;
                break;

            case PERMISSION_ADS_EDIT_ZONE:
                editZone                 = true;
                break;

            case PERMISSION_ADS_DELETE_ZONE:
                deleteZone               = true;
                break;

            case PERMISSION_ADS_ADD_BANNER:
                addBanner                = true;
                break;

            case PERMISSION_ADS_EDIT_BANNER:
                editBanner               = true;
                break;

            case PERMISSION_ADS_DELETE_BANNER:
                deleteBanner             = true;
                break;

            case PERMISSION_ADS_VIEW_ZONE:
                viewZone                 = true;
                break;

            case PERMISSION_ADS_VIEW_BANNER:
                viewBanner               = true;
                break;

            case PERMISSION_ADS_UPLOAD_MEDIA:
                uploadMedia              = true;
                break;
                
            case PERMISSION_ADS_VIEW_CHART:
                viewChartAds             = true;
                break;

/**************************************************************************
 * cannot find, just throw an Exception
 **************************************************************************/

            default:
                AssertionUtil.doAssert(false, "Currently in setPermission do not support permission = " + permission);
                break;
        }//switch
    }//setPermission

    /**
     * The ONLY way to set permission in forum, so this MUST have default package access
     */
    void setPermissionInForum(int forumID, int permission) {
        switch (permission) {
/**************************************************************************
 * Combined permissions, range from 100 to 199
 * A permission in this range is the combination of other permissions
 **************************************************************************/
            case PERMISSION_FORUM_ADMIN:
                editForum.setForumPermission(forumID, true);
                deleteForum.setForumPermission(forumID, true);
                assignToForum.setForumPermission(forumID, true);

                readPost.setForumPermission(forumID, true);
                addThread.setForumPermission(forumID, true);
                addPost.setForumPermission(forumID, true);
                editPost.setForumPermission(forumID, true);
                editOwnPost.setForumPermission(forumID, true);
                deletePost.setForumPermission(forumID, true);
                addPoll.setForumPermission(forumID, true);
                editPoll.setForumPermission(forumID, true);
                editOwnPoll.setForumPermission(forumID, true);
                addOwnThreadPoll.setForumPermission(forumID, true);
                deletePoll.setForumPermission(forumID, true);
                addAttachment.setForumPermission(forumID, true);
                getAttachment.setForumPermission(forumID, true);
                moderateThread.setForumPermission(forumID, true);
                break;

            case PERMISSION_FORUM_MODERATOR:
                editForum.setForumPermission(forumID, true);

                readPost.setForumPermission(forumID, true);
                addThread.setForumPermission(forumID, true);
                addPost.setForumPermission(forumID, true);
                editPost.setForumPermission(forumID, true);
                editOwnPost.setForumPermission(forumID, true);
                deletePost.setForumPermission(forumID, true);
                addPoll.setForumPermission(forumID, true);
                editPoll.setForumPermission(forumID, true);
                editOwnPoll.setForumPermission(forumID, true);
                addOwnThreadPoll.setForumPermission(forumID, true);
                deletePoll.setForumPermission(forumID, true);
                addAttachment.setForumPermission(forumID, true);
                getAttachment.setForumPermission(forumID, true);
                moderateThread.setForumPermission(forumID, true);
                break;

            case PERMISSION_LIMITED_USER:
                readPost.setForumPermission(forumID, true);
                /** @todo at the 1.0.0 beta2/beta3/RC1/RC2 release, add post is disable*/
                //addPost.setForumPermission(forumID, true);
                break;

            case PERMISSION_NORMAL_USER:
                readPost.setForumPermission(forumID, true);
                addThread.setForumPermission(forumID, true);
                addPost.setForumPermission(forumID, true);
                getAttachment.setForumPermission(forumID, true);
                break;

    /**
     * Can:
     * - login, read thread and post, reply to a thread
     * - add thread, use avatar, use private message, get attachment
     * - use attachment, create poll
     */
            case PERMISSION_POWER_USER:
                readPost.setForumPermission(forumID, true);
                addThread.setForumPermission(forumID, true);
                addPost.setForumPermission(forumID, true);
                editOwnPost.setForumPermission(forumID, true);
                editOwnPoll.setForumPermission(forumID, true);
                addOwnThreadPoll.setForumPermission(forumID, true);
                addAttachment.setForumPermission(forumID, true);
                getAttachment.setForumPermission(forumID, true);
                break;

/**************************************************************************
 * Individual Permissions that can be applied for individual forum usages,
 * (of course it can be applied to all forums), range from 2000 to 3000
 **************************************************************************/
            case PERMISSION_EDIT_FORUM:
                editForum.setForumPermission(forumID, true);
                break;
            case PERMISSION_DELETE_FORUM:
                deleteForum.setForumPermission(forumID, true);
                break;
            case PERMISSION_ASSIGN_TO_FORUM:
                assignToForum.setForumPermission(forumID, true);
                break;
            case PERMISSION_READ_POST:
                readPost.setForumPermission(forumID, true);
                break;
            case PERMISSION_ADD_THREAD:
                addThread.setForumPermission(forumID, true);
                break;
            case PERMISSION_ADD_POST:
                addPost.setForumPermission(forumID, true);
                break;
            case PERMISSION_EDIT_POST:
                editPost.setForumPermission(forumID, true);
                break;
            case PERMISSION_EDIT_OWN_POST:
                editOwnPost.setForumPermission(forumID, true);
                break;
            case PERMISSION_DELETE_POST:
                deletePost.setForumPermission(forumID, true);
                break;
            case PERMISSION_ADD_POLL:
                addPoll.setForumPermission(forumID, true);
                addOwnThreadPoll.setForumPermission(forumID, true);
                break;
            case PERMISSION_EDIT_POLL:
                editPoll.setForumPermission(forumID, true);
                break;
            case PERMISSION_EDIT_OWN_POLL:
                editOwnPoll.setForumPermission(forumID, true);
                break;
            case PERMISSION_ADD_OWN_THREAD_POLL:
                addOwnThreadPoll.setForumPermission(forumID, true);
                break;
            case PERMISSION_DELETE_POLL:
                deletePoll.setForumPermission(forumID, true);
                break;
            case PERMISSION_ADD_ATTACHMENT:
                addAttachment.setForumPermission(forumID, true);
                break;
            case PERMISSION_GET_ATTACHMENT:
                getAttachment.setForumPermission(forumID, true);
                break;
            case PERMISSION_MODERATE_THREAD:
                moderateThread.setForumPermission(forumID, true);
                break;


/**************************************************************************
 * cannot find, just throw an Exception
 **************************************************************************/

            default:
                AssertionUtil.doAssert(false, "Currently in setPermissionInForum do not support permission = " + permission);
                break;
        }//switch
    }//setPermissionInForum

    void setPermissionInStepInChannel(int stepID, int channelID, int permission) {
        switch (permission) {
/**************************************************************************
 * Individual CMS Permissions that can be applied for individual channel usages,
 * (of course it can be applied to all channels)
 **************************************************************************/
            case PERMISSION_CMS_WRITE_CONTENT:
                writeContent.setChannelPermission(channelID, true);
                writeContentInStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_EDIT_CONTENT:
                editContentInStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_DELETE_CONTENT:
                deleteContentInStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_VIEW_CONTENT:
                viewContentInStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_VIEW_STEP:
                viewContentStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_GOTO_STEP:
                goToContentStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
            case PERMISSION_CMS_DEPLOY_CONTENT:
                deployContentInStepWithChannel.setStepAndChannelPermission(stepID, channelID, true);
                break;
/**************************************************************************
 * cannot find, just throw an Exception
 **************************************************************************/

            default:
                AssertionUtil.doAssert(false, "Currently in setPermissionInStepInChannel do not support permission = " + permission);
                break;
        }//switch
    }//setPermissionInStepInChannel

    void setPermissionInStep(int stepID, int permission) {
        switch (permission) {
/**************************************************************************
 * Individual CMS Permissions that can be applied for individual channel usages,
 * (of course it can be applied to all channels)
 **************************************************************************/
            case PERMISSION_CMS_WRITE_CONTENT:
                writeContent.setAllChannelsPermission(true);
                writeContentInStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_EDIT_CONTENT:
                editContentInStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_DELETE_CONTENT:
                deleteContentInStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_VIEW_CONTENT:
                viewContentInStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_VIEW_STEP:
                viewContentStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_GOTO_STEP:
                goToContentStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
            case PERMISSION_CMS_DEPLOY_CONTENT:
                deployContentInStepWithChannel.setAllChannelsPermission(stepID, true);
                break;
/**************************************************************************
 * cannot find, just throw an Exception
 **************************************************************************/

            default:
                AssertionUtil.doAssert(false, "Currently in setPermissionInStep do not support permission = " + permission);
                break;
        }//switch
    }//setPermissionInStep

    public boolean isMemberInGroup(String groupName) {
        for (Iterator iter = groupsContainMember.iterator(); iter.hasNext();) {
            GroupsBean element = (GroupsBean) iter.next();
            if (element.getGroupName().equals(groupName)) {
                return true;
            }
        }
        return false;
    }

    public void ensureIsMemberInGroup(String groupName) throws AuthenticationException {
        if (isMemberInGroup(groupName) == false) {
            throw new AuthenticationException(NotLoginException.NOT_ENOUGH_RIGHTS);
        }
    }

}