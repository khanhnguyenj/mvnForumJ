/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/LocaleMessageUtil.java,v 1.4 2009/01/03 18:32:35 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
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
 * @author: Minh Nguyen
 */
package com.mvnforum;

import java.util.Locale;

import com.mvnforum.db.*;

public class LocaleMessageUtil {
    
    private LocaleMessageUtil() {
        // to prevent creating an instance
    }

/**************************************************************************
 * Thread bean
 **************************************************************************/
    public static String getThreadStatusDescFromInt(Locale locale, int threadStatus) {
        
        String result = null;
        switch (threadStatus) {
        case ThreadBean.THREAD_STATUS_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.status.active");
            break;
            
        case ThreadBean.THREAD_STATUS_DISABLED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.status.disabled");
            break;
            
        case ThreadBean.THREAD_STATUS_LOCKED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.status.locked");
            break;
            
        case ThreadBean.THREAD_STATUS_CLOSED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.status.closed");
            break;
            
        default:
            throw new AssertionError("Cannot get thread status description with threadStatus = " + threadStatus);
        }
        return result;
    }

    public static String getThreadTypeDescFromInt(Locale locale, int threadType) {
        
        String result;
        switch (threadType) {
        case ThreadBean.THREAD_TYPE_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.type.normal_thread");
            break;
            
        case ThreadBean.THREAD_TYPE_STICKY:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.type.sticky_thread");
            break;
            
        case ThreadBean.THREAD_TYPE_FORUM_ANNOUNCEMENT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.type.announcement_thread");
            break;
            
        case ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.type.global_announcement_thread");
            break;
            
        default:
            throw new AssertionError("Cannot get thread type description with threadType = " + threadType);
        }
        return result;
    }

    public static String getThreadPriorityDescFromInt(Locale locale, int threadPriority) {
        
        String result;
        switch (threadPriority) {
        case ThreadBean.THREAD_PRIORITY_NORMAL:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.priority.normal");
            break;
            
        case ThreadBean.THREAD_PRIORITY_LOW:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.priority.low");
            break;
            
        case ThreadBean.THREAD_PRIORITY_HIGH:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread.priority.high");
            break;
            
        default:
            throw new AssertionError("Cannot get thread priority description with threadPriority = " + threadPriority);
        }
        return result;
    }

/**************************************************************************
 * Forum bean
 **************************************************************************/
    public static String getForumStatusDescFromInt(Locale locale, int forumStatus) {
        
        String result = null;
        switch (forumStatus) {
        case ForumBean.FORUM_STATUS_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.status.active");
            break;
            
        case ForumBean.FORUM_STATUS_DISABLED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.status.disabled");
            break;
            
        case ForumBean.FORUM_STATUS_LOCKED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.status.locked");
            break;
            
        case ForumBean.FORUM_STATUS_CLOSED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.status.closed");
            break;
            
        default:
            throw new AssertionError("Cannot get forum status description with forumStatus = " + forumStatus);
        }
        return result;
    }

    public static String getForumTypeDescFromInt(Locale locale, int forumType) {

        String result;
        switch (forumType) {
        case ForumBean.FORUM_TYPE_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.type.default");
            break;

        case ForumBean.FORUM_TYPE_PRIVATE:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.type.private");
            break;

        default:
            throw new AssertionError("Cannot get forum type description with forumType = " + forumType);
        }

        return result;
    }

    public static String getForumModeDescFromInt(Locale locale, int forumMode) {

        String result;
        switch (forumMode) {
        case ForumBean.FORUM_MODERATION_MODE_SYSTEM_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode.default");
            break;

        case ForumBean.FORUM_MODERATION_MODE_NO_MODERATION:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode.no");
            break;

        case ForumBean.FORUM_MODERATION_MODE_THREAD_AND_POST:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode.thread_post");
            break;

        case ForumBean.FORUM_MODERATION_MODE_THREAD_ONLY:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode.thread");
            break;

        case ForumBean.FORUM_MODERATION_MODE_POST_ONLY:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode.post");
            break;

        default:
            throw new AssertionError("Cannot get forum mode description with forumMode = " + forumMode);
        }

        return result;
    }

/**************************************************************************
 * Watch bean
 **************************************************************************/
    public static String getWatchTypeDescFromInt(Locale locale, int watchType) {

        String result;
        switch (watchType) {
        case WatchBean.WATCH_TYPE_DEFAULT:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.watch.type.default");
            break;

        case WatchBean.WATCH_TYPE_DIGEST:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.watch.type.digest");
            break;

        case WatchBean.WATCH_TYPE_NONDIGEST:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.watch.type.nondigest");
            break;

        default:
            throw new AssertionError("Cannot get watch type description with watchType = " + watchType);
        }

        return result;
    }

/**************************************************************************
 * Member bean
 **************************************************************************/
    public static String getMemberStatusDescFromInt(Locale locale, int memberStatus) {

        String result;
        switch (memberStatus) {
        case MemberBean.MEMBER_STATUS_ENABLE:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.member.status.enabled");
            break;

        case MemberBean.MEMBER_STATUS_DISABLE:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.member.status.disabled");
            break;

        case MemberBean.MEMBER_STATUS_PENDING:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.member.status.pending");
            break;

        case MemberBean.MEMBER_STATUS_DELETED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.member.status.deleted");
            break;
            
        default:
            throw new AssertionError("Cannot get member status description with memberStatus = " + memberStatus);
        }

        return result;
    }

/**************************************************************************
 * PollAnswer bean
 **************************************************************************/
    public static String getPollAnswerTypeDescFromInt(Locale locale, int pollAnswerType) {

        String result;
        switch (pollAnswerType) {
        case PollAnswerBean.POLL_ANSWER_TYPE_NORMAL:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.pollanswer.type.normal");
            break;

        case PollAnswerBean.POLL_ANSWER_TYPE_NEED_YOUR_OPINION:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.pollanswer.type.need_your_opinion");
            break;

        default:
            throw new AssertionError("Cannot get poll answer type description with pollAnswerType = " + pollAnswerType);
        }

        return result;
    }

/**************************************************************************
 * Poll bean
 **************************************************************************/
    public static String getPollStatusDescFromInt(Locale locale, int pollStatus) {

        String result;
        switch (pollStatus) {
        case PollBean.POLL_STATUS_ENABLE:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll.status.enable");
            break;

        case PollBean.POLL_STATUS_EDITING:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll.status.editing");
            break;

        case PollBean.POLL_STATUS_DISABLED:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll.status.disabled");
            break;

        default:
            throw new AssertionError("Cannot get poll status description with pollStatus = " + pollStatus);
        }

        return result;
    }

    public static String getPollMultipleDescFromInt(Locale locale, int pollMultiple) {

        String result;
        switch (pollMultiple) {
        case PollBean.POLL_MULTIPLE_NO:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll.multiple.single_choice");
            break;

        case PollBean.POLL_MULTIPLE_YES:
            result = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll.multiple.multiple_choice");
            break;

        default:
            throw new AssertionError("Cannot get poll multiple description with pollMultiple = " + pollMultiple);
        }

        return result;
    }

}
