/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/PostChecker.java,v 1.11 2009/01/03 18:32:35 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2009/01/03 18:32:35 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.common;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.util.DateUtil;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;
import com.mvnforum.db.*;

public class PostChecker {

    public static final int ROOT_POST = 0;
    
    private PostChecker() {
        // to prevent creating an instance
    }

    public static void checkEditPost(OnlineUser user, PostBean post, ForumBean forum) 
        throws AuthenticationException, BadInputException, DatabaseException {

        MVNForumPermission permission = user.getPermission();
        int forumID = post.getForumID();

        // check constraint
        if (permission.canEditPost(forumID)) {
            // have permission, just do nothing, that is don't check the max day constraint
        } else if (isAuthorOfPost(user, post)) {
            // make sure user have permission to edit his own post
            permission.ensureCanEditOwnPost(forumID);

            // post should not be too old, usually 7 days by default
            checkTooOldToEdit(user, post);

            // post should not be disabled
            checkDisableForEdit(user, post);
            
            // should not edit a post that need to be moderated and has reply
            checkNoReplyForModeration(user, post, forum);
        } else {//not an author, so this user must have Edit Permission
            permission.ensureCanEditPost(forumID);// this method ALWAYS throws AuthenticationException
        }

    }

    public static void checkDeletePost(OnlineUser user, PostBean post) 
        throws AuthenticationException, BadInputException, IllegalArgumentException, DatabaseException {
        
        MVNForumPermission permission = user.getPermission();
        int forumID = post.getForumID();

        checkRootOfThread(user, post);

        // check other constraints
        if (permission.canDeletePost(forumID)) {
            // have permission, just do nothing, that is don't check the max day constraint
        } else if (isAuthorOfPost(user, post)) {// same author
            // check date here, usually must not older than 7 days
            checkTooOldToDelete(user, post);
    
            //Check to make sure that "no reply" for this post
            checkNoReplyForDelete(user, post);
    
            //Check to make ensure that this post is enabled
            checkDisableForDelete(user, post);
        } else {//not an author, so this user must have Edit Permission
            permission.ensureCanDeletePost(forumID);// this method ALWAYS throws AuthenticationException
        }

    }
    
    public static boolean isAuthorOfPost(OnlineUser user, PostBean post) {
        return (user.getMemberID() == post.getMemberID() && user.isMember());
    }

    public static boolean isDisabled(PostBean post) {
        return (post.getPostStatus() == PostBean.POST_STATUS_DISABLED);
    }

    public static void checkTooOldToEdit(OnlineUser user, PostBean post) throws BadInputException {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        int maxDays = MVNForumConfig.getMaxEditDays();
        if ( (now.getTime() - post.getPostCreationDate().getTime()) > (DateUtil.DAY * maxDays) ) {
            /** @todo choose a better Exception here */
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_edit.post_is_too_old", new Object[] {new Integer(maxDays)});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("You cannot edit a post which is older than " + maxDays + " days.");
        }
    }

    public static void checkTooOldToDelete(OnlineUser user, PostBean post) throws BadInputException {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        int maxDays = MVNForumConfig.getMaxDeleteDays();
        if ( (now.getTime() - post.getPostCreationDate().getTime()) > (DateUtil.DAY * maxDays) ) {
            /** @todo choose a better Exception here */
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete.post_is_too_old", new Object[] {new Integer(maxDays)});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("You cannot delete a post which is older than " + maxDays + " days.");
        }
    }

    public static void checkDisableForEdit(OnlineUser user, PostBean post) throws BadInputException {
        if (isDisabled(post)) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_edit_your_post.which_is_disabled");
            throw new BadInputException(localizedMessage);
        }
    }

    public static void checkDisableForDelete(OnlineUser user, PostBean post) throws BadInputException {
        if (isDisabled(post)) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete_your_post.which_is_disabled");
            throw new BadInputException(localizedMessage);
        }
    }

    public static void checkNoReplyForDelete(OnlineUser user, PostBean post) 
        throws DatabaseException, BadInputException {
        
        boolean foundReply = hasReply(post);

        if (foundReply) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete_post.post_has_reply");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete a post that has reply!");
        }
    }

    public static void checkNoReplyForModeration(OnlineUser user, PostBean post, ForumBean forumBean) 
        throws DatabaseException, BadInputException {
        
        boolean isForumModerator = user.getPermission().canModerateThread(forumBean.getForumID());
        int parentPostID = post.getParentPostID();

        // Ensure that moderator don't have to moderate the thread to enable it
        boolean shouldCheckReply = false;
        if (isForumModerator == false) {
            if ( (parentPostID == 0) && forumBean.shouldModerateThread()) {
                shouldCheckReply = true;
            } else if ( (parentPostID != 0) && forumBean.shouldModeratePost()) {
                shouldCheckReply = true;
            }
        }
        
        if (shouldCheckReply) {
            boolean foundReply = hasReply(post);
            if (foundReply) {
                String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_edit_your_post.post_has_reply_in_moderation");
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("Cannot delete a post that has reply!");
            }
        }
    }
    
    public static boolean hasReply(PostBean post) throws DatabaseException {
        
        Collection  posts = DAOFactory.getPostDAO().getEnablePosts_inThread_limit(post.getThreadID(), 0, 10000);
        boolean foundReply = false;
        for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
            PostBean tPost = (PostBean) iter.next();
            if (tPost.getParentPostID() == post.getPostID()) {
                foundReply = true;
                break;
            }
        }
        return foundReply;
    }

    public static void checkRootOfThread(OnlineUser user, PostBean post) throws BadInputException {
        if (post.getParentPostID() == ROOT_POST) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete_root_post");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete a root post. Use delete thread instead.");
        }

    }

}
