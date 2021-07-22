/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/ThreadChecker.java,v 1.5 2009/01/03 18:32:35 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.common;

import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.DateUtil;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;
import com.mvnforum.db.*;

public class ThreadChecker {
    
    private ThreadChecker() {
        // to prevent creating an instance
    }

    public static void checkDeleteThread(OnlineUser user, ThreadBean thread)
        throws AuthenticationException, BadInputException, IllegalArgumentException, DatabaseException, ObjectNotFoundException {
        
        MVNForumPermission permission = user.getPermission();
        int forumID = thread.getForumID();
        
        if (permission.canDeletePost(forumID)) {
            // have permission, just do nothing, that is don't check the max day constraint
        } else if (isAuthorOfThread(user, thread)) {// same author
            // check date here, usually must not older than 7 days
            checkTooOld(user, thread);

            //Check to make sure that "no reply" for this thread
            checkNoReply(user, thread);

            //check to make sure this thread is not disabled
            checkDisable(user, thread);
        } else {//not an author, so this user must have Edit Permission
            permission.ensureCanDeletePost(forumID);// this method ALWAYS throws AuthenticationException
        }
    }
    
    public static boolean isAuthorOfThread(OnlineUser user, ThreadBean thread)
        throws DatabaseException, ObjectNotFoundException {
        
        int authorID = MemberCache.getInstance().getMemberIDFromMemberName(thread.getMemberName());
        return (user.getMemberID() == authorID && user.isMember());
    }

    public static void checkTooOld(OnlineUser user, ThreadBean thread)
        throws BadInputException {

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp postDate = thread.getThreadCreationDate();
        int maxDays = MVNForumConfig.getMaxDeleteDays();
        if ( (now.getTime() - postDate.getTime()) > (DateUtil.DAY * maxDays) ) {
            /** @todo choose a better Exception here */
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete.thread_is_too_old", new Object[] {new Integer(maxDays)});
            throw new BadInputException(localizedMessage);
          //throw new BadInputException("You cannot delete a thread which is older than " + maxDays + " days.");
        }
    }
    
    public static void checkNoReply(OnlineUser user, ThreadBean thread)
        throws DatabaseException, BadInputException {
        
        int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(thread.getThreadID());
        if (numberOfPosts > 1) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete.thread_has_reply");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete a thread that has reply!");
        }
    }

    public static void checkDisable(OnlineUser user, ThreadBean thread)
        throws BadInputException {
        
        if (thread.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED) {
            String localizedMessage = MVNForumResourceBundle.getString(user.getLocale(), "mvncore.exception.BadInputException.cannot_delete_your_own_disabled_thread");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete your own disabled thread.");
        }
    }

}
