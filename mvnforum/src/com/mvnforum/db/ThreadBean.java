/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ThreadBean.java,v 1.27 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.27 $
 * $Date: 2009/01/02 18:18:50 $
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
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: ThreadID, ForumID, MemberName, LastPostMemberName, ThreadTopic,
 *                   ThreadBody, ThreadVoteCount, ThreadVoteTotalStars, ThreadCreationDate, ThreadLastPostDate,
 *                   ThreadType, ThreadOption, ThreadStatus, ThreadHasPoll, ThreadViewCount,
 *                   ThreadReplyCount, ThreadIcon, ThreadDuration
 * Excluded columns:
 */
public class ThreadBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
    /**
     * The default value mean thread is enable and normal
     */
    public static final int THREAD_STATUS_DEFAULT  = 0;

    /**
     * The disable thread is invisible for normal user and visible to moderator
     */
    public static final int THREAD_STATUS_DISABLED = 1;

    /**
     * No changes (edit, attach, reply) could be maded, moderator
     * have to change this status before making any changes is possible
     */
    public static final int THREAD_STATUS_LOCKED   = 2;

    /**
     * No one can reply, but moderator can change it.
     */
    public static final int THREAD_STATUS_CLOSED   = 3;

    /**
     * Thread type is traditional
     */
    public static final int THREAD_TYPE_DEFAULT             = 0;

    public static final int THREAD_TYPE_STICKY              = 1;

    public static final int THREAD_TYPE_FORUM_ANNOUNCEMENT  = 2;

    public static final int THREAD_TYPE_GLOBAL_ANNOUNCEMENT = 3;

    public static final int THREAD_PRIORITY_NORMAL          = 0;

    public static final int THREAD_PRIORITY_LOW             = 1;

    public static final int THREAD_PRIORITY_HIGH            = 2;

    private int threadID;
    private int forumID;
    private String memberName;
    private String lastPostMemberName;
    private String threadTopic;
    private String threadBody;
    private int threadVoteCount;
    private int threadVoteTotalStars;
    private Timestamp threadCreationDate;
    private Timestamp threadLastPostDate;
    private int threadType;
    private int threadOption;
    private int threadStatus;
    private int threadHasPoll;
    private int threadViewCount;
    private int threadReplyCount;
    private String threadIcon;
    private int threadDuration;
    private int threadAttachCount;
    private int threadPriority;

    public int getThreadID() {
        return threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLastPostMemberName() {
        return lastPostMemberName;
    }
    public void setLastPostMemberName(String lastPostMemberName) {
        this.lastPostMemberName = lastPostMemberName;
    }

    public String getThreadTopic() {
        return threadTopic;
    }
    public void setThreadTopic(String threadTopic) {
        this.threadTopic = threadTopic;
    }

    public String getThreadBody() {
        return threadBody;
    }
    public void setThreadBody(String threadBody) {
        this.threadBody = threadBody;
    }

    public int getThreadVoteCount() {
        return threadVoteCount;
    }
    public void setThreadVoteCount(int threadVoteCount) {
        this.threadVoteCount = threadVoteCount;
    }

    public int getThreadVoteTotalStars() {
        return threadVoteTotalStars;
    }
    public void setThreadVoteTotalStars(int threadVoteTotalStars) {
        this.threadVoteTotalStars = threadVoteTotalStars;
    }

    public Timestamp getThreadCreationDate() {
        return threadCreationDate;
    }
    public void setThreadCreationDate(Timestamp threadCreationDate) {
        this.threadCreationDate = threadCreationDate;
    }

    public Timestamp getThreadLastPostDate() {
        return threadLastPostDate;
    }
    public void setThreadLastPostDate(Timestamp threadLastPostDate) {
        this.threadLastPostDate = threadLastPostDate;
    }

    public int getThreadPriority() {
        return threadPriority;
    }
    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }

    public int getThreadType() {
        return threadType;
    }
    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }

    public int getThreadOption() {
        return threadOption;
    }
    public void setThreadOption(int threadOption) {
        this.threadOption = threadOption;
    }

    public int getThreadStatus() {
        return threadStatus;
    }
    public void setThreadStatus(int threadStatus) {
        this.threadStatus = threadStatus;
    }

    public int getThreadHasPoll() {
        return threadHasPoll;
    }
    public void setThreadHasPoll(int threadHasPoll) {
        this.threadHasPoll = threadHasPoll;
    }

    public int getThreadViewCount() {
        return threadViewCount;
    }
    public void setThreadViewCount(int threadViewCount) {
        this.threadViewCount = threadViewCount;
    }

    public int getThreadReplyCount() {
        return threadReplyCount;
    }
    public void setThreadReplyCount(int threadReplyCount) {
        this.threadReplyCount = threadReplyCount;
    }

    public String getThreadIcon() {
        return threadIcon;
    }
    public void setThreadIcon(String threadIcon) {
        this.threadIcon = StringUtil.getEmptyStringIfNull(threadIcon);
    }

    public int getThreadDuration() {
        return threadDuration;
    }
    public void setThreadDuration(int threadDuration) {
        this.threadDuration = threadDuration;
    }

    public int getThreadAttachCount() {
        return this.threadAttachCount;
    }
    public void setThreadAttachCount(int attachCount) {
        this.threadAttachCount = attachCount;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private int threadPendingPostCount;

    public int getThreadPendingPostCount() {
        return threadPendingPostCount;
    }
    public void setThreadPendingPostCount(int threadPendingPostCount) {
        this.threadPendingPostCount = threadPendingPostCount;
    }

    private Collection pendingPosts;

    public Collection getPendingPosts() {
        return pendingPosts;
    }
    public void setPendingPosts(Collection pendingPosts) {
        this.pendingPosts = pendingPosts;
    }

    public static void validateThreadStatus(int status) throws IllegalArgumentException {
        if ((status < THREAD_TYPE_DEFAULT) || (status > THREAD_STATUS_CLOSED)) {
            throw new IllegalArgumentException("Invalid ThreadStatus = " + status);
        }
    }

    public static void validateThreadType(int type) throws IllegalArgumentException {
        if ((type < THREAD_TYPE_DEFAULT) || (type > THREAD_TYPE_GLOBAL_ANNOUNCEMENT)) {
            throw new IllegalArgumentException("Invalid ThreadType = " + type);
        }
    }

    public void ensureStatusCanReply() throws IllegalArgumentException {
        if ( (getThreadStatus() == ThreadBean.THREAD_STATUS_LOCKED) ||
            (getThreadStatus() == ThreadBean.THREAD_STATUS_CLOSED)) {
            // or we can throw AssertionError. indicate that the gui MUST hide the option
            //@todo : localize me
            throw new IllegalStateException("Cannot reply when the thread is closed or locked.");
        }
    }

    public void ensureStatusCanEdit() throws IllegalArgumentException {
        if (getThreadStatus() == ThreadBean.THREAD_STATUS_LOCKED) {
            // or we can throw AssertionError. indicate that the gui MUST hide the option
            //@todo : localize me
            throw new IllegalStateException("Cannot edit post when the thread is locked.");
        }
    }

} //end of class ThreadBean
