/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ForumBean.java,v 1.31 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Locale;

import net.myvietnam.mvncore.util.StringUtil;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;

/*
 * Included columns: ForumID, CategoryID, LastPostMemberName, ForumName, ForumDesc,
 *                   ForumCreationDate, ForumModifiedDate, ForumLastPostDate, ForumOrder, ForumType,
 *                   ForumFormatOption, ForumOption, ForumStatus, ForumModerationMode, ForumPassword,
 *                   ForumThreadCount, ForumPostCount
 * Excluded columns:
 */
public class ForumBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/

    /** The default value mean forum is enable and normal */
    public static final int FORUM_STATUS_DEFAULT                  = 0;

    /** The disabled value mean forum is disabled */
    public static final int FORUM_STATUS_DISABLED                 = 1;

    /**
     * No changes (edit, attach, reply) could be maded, moderator
     * have to change this status before making any changes is possible
     */
    public static final int FORUM_STATUS_LOCKED                   = 2;

    /** Noone can reply, but moderator can change it */
    public static final int FORUM_STATUS_CLOSED                   = 3;


    /**  */
    public static final int FORUM_TYPE_DEFAULT                    = 0;

    /**  */
    public static final int FORUM_TYPE_PRIVATE                    = 1;


    /**  */
    public static final int FORUM_MODERATION_MODE_SYSTEM_DEFAULT  = 0;

    /**  */
    public static final int FORUM_MODERATION_MODE_NO_MODERATION   = 1;

    /**  */
    public static final int FORUM_MODERATION_MODE_THREAD_AND_POST = 2;

    /**  */
    public static final int FORUM_MODERATION_MODE_THREAD_ONLY     = 3;

    /**  */
    public static final int FORUM_MODERATION_MODE_POST_ONLY       = 4;

    private int forumID;
    private int categoryID;
    private String forumOwnerName;
    private String lastPostMemberName;
    private String forumName;
    private String forumDesc;
    private Timestamp forumCreationDate;
    private Timestamp forumModifiedDate;
    private Timestamp forumLastPostDate;
    private int forumOrder;
    private int forumType;
    private int forumFormatOption;
    private int forumOption;
    private int forumStatus;
    private int forumModerationMode;
    private String forumPassword;
    private int forumThreadCount;
    private int forumPostCount;

    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getForumOwnerName() {
        return forumOwnerName;
    }
    public void setForumOwnerName(String forumOwnerName) {
        this.forumOwnerName = (forumOwnerName == null) ? "" : forumOwnerName;
    }

    public String getLastPostMemberName() {
        return lastPostMemberName;
    }
    public void setLastPostMemberName(String lastPostMemberName) {
        this.lastPostMemberName = StringUtil.getEmptyStringIfNull(lastPostMemberName);
    }

    public String getForumName() {
        return forumName;
    }
    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getForumDesc() {
        return forumDesc;
    }
    public void setForumDesc(String forumDesc) {
        this.forumDesc = StringUtil.getEmptyStringIfNull(forumDesc);
    }

    public Timestamp getForumCreationDate() {
        return forumCreationDate;
    }
    public void setForumCreationDate(Timestamp forumCreationDate) {
        this.forumCreationDate = forumCreationDate;
    }

    public Timestamp getForumModifiedDate() {
        return forumModifiedDate;
    }
    public void setForumModifiedDate(Timestamp forumModifiedDate) {
        this.forumModifiedDate = forumModifiedDate;
    }

    public Timestamp getForumLastPostDate() {
        return forumLastPostDate;
    }
    public void setForumLastPostDate(Timestamp forumLastPostDate) {
        this.forumLastPostDate = forumLastPostDate;
    }

    public int getForumOrder() {
        return forumOrder;
    }
    public void setForumOrder(int forumOrder) {
        this.forumOrder = forumOrder;
    }

    public int getForumType() {
        return forumType;
    }
    public void setForumType(int forumType) {
        this.forumType = forumType;
    }

    public int getForumFormatOption() {
        return forumFormatOption;
    }
    public void setForumFormatOption(int forumFormatOption) {
        this.forumFormatOption = forumFormatOption;
    }

    public int getForumOption() {
        return forumOption;
    }
    public void setForumOption(int forumOption) {
        this.forumOption = forumOption;
    }

    public int getForumStatus() {
        return forumStatus;
    }
    public void setForumStatus(int forumStatus) {
        this.forumStatus = forumStatus;
    }

    public int getForumModerationMode() {
        return forumModerationMode;
    }
    public void setForumModerationMode(int forumModerationMode) {
        this.forumModerationMode = forumModerationMode;
    }

    public String getForumPassword() {
        return forumPassword;
    }
    public void setForumPassword(String forumPassword) {
        this.forumPassword = StringUtil.getEmptyStringIfNull(forumPassword);
    }

    public int getForumThreadCount() {
        return forumThreadCount;
    }
    public void setForumThreadCount(int forumThreadCount) {
        this.forumThreadCount = forumThreadCount;
    }

    public int getForumPostCount() {
        return forumPostCount;
    }
    public void setForumPostCount(int forumPostCount) {
        this.forumPostCount = forumPostCount;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private int pendingThreadCount = 0;
    private int threadsWithPendingPostsCount = 0;
    private int pendingPostCount = 0;
    private int viewCount = 0;

    public int getPendingPostCount() {
        return pendingPostCount;
    }
    public void setPendingPostCount(int pendingPostCount) {
        this.pendingPostCount = pendingPostCount;
    }

    public int getPendingThreadCount() {
        return pendingThreadCount;
    }
    public void setPendingThreadCount(int pendingThreadCount) {
        this.pendingThreadCount = pendingThreadCount;
    }

    public int getThreadsWithPendingPostsCount() {
        return threadsWithPendingPostsCount;
    }
    public void setThreadsWithPendingPostsCount(int threadsWithPendingPostsCount) {
        this.threadsWithPendingPostsCount = threadsWithPendingPostsCount;
    }

    // count sum of view in threads
    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public static void validateForumType(int type) throws IllegalArgumentException {
        if ((type < FORUM_TYPE_DEFAULT) || (type > FORUM_TYPE_PRIVATE)) {
            throw new IllegalArgumentException("Invalid ForumType = " + type);
        }
    }

    public static void validateForumModerationMode(int moderationMod) throws IllegalArgumentException {
        if ((moderationMod < FORUM_MODERATION_MODE_SYSTEM_DEFAULT) || (moderationMod > FORUM_MODERATION_MODE_POST_ONLY)) {
            throw new IllegalArgumentException("Invalid ForumModerationMod = " + moderationMod);
        }
    }

    public static void validateForumStatus(int status) throws IllegalArgumentException {
        if ((status < FORUM_STATUS_DEFAULT) || (status > FORUM_STATUS_CLOSED)) {
            throw new IllegalArgumentException("Invalid ForumStatus = " + status);
        }
    }

    public static void validateForumOption(int option) throws IllegalArgumentException {
        if ((option < 0) || (option > 0)) {
            throw new IllegalArgumentException("Invalid ForumOption = " + option);
        }
    }

    public static void validateForumFormatOption(int option) throws IllegalArgumentException {
        if ((option < 0) || (option > 0)) {
            throw new IllegalArgumentException("Invalid ForumFormatOption = " + option);
        }
    }

    public void ensureNotDisabledForum(Locale locale) {
        if (forumStatus == ForumBean.FORUM_STATUS_DISABLED) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.cannot_process_in_disabled_forum");
            throw new IllegalStateException(localizedMessage);
        }
    }

    public void ensureNotLockedForum(Locale locale) {
        if (forumStatus == ForumBean.FORUM_STATUS_LOCKED) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.cannot_process_in_locked_forum");
            throw new IllegalStateException(localizedMessage);
        }
    }

    public void ensureNotClosedForum(Locale locale) {
        if (forumStatus == ForumBean.FORUM_STATUS_CLOSED) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.cannot_process_in_closed_forum");
            throw new IllegalStateException(localizedMessage);
        }
    }

    public boolean shouldModeratePost() {
        int mode = forumModerationMode;
        if (mode == FORUM_MODERATION_MODE_SYSTEM_DEFAULT) {
            mode = MVNForumConfig.getDefaultModerationOption();
        }
        if ((mode == FORUM_MODERATION_MODE_POST_ONLY) || (mode == FORUM_MODERATION_MODE_THREAD_AND_POST)) {
            return true;
        }
        return false;
    }

    public boolean shouldModerateThread() {
        int mode = forumModerationMode;
        if (mode == FORUM_MODERATION_MODE_SYSTEM_DEFAULT) {
            mode = MVNForumConfig.getDefaultModerationOption();
        }
        if ((mode == FORUM_MODERATION_MODE_THREAD_ONLY) || (mode == FORUM_MODERATION_MODE_THREAD_AND_POST)) {
            return true;
        }
        return false;
    }

} //end of class ForumBean
