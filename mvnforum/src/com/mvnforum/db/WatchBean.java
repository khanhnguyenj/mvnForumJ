/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/WatchBean.java,v 1.15 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.15 $
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

/*
 * Included columns: WatchID, MemberID, CategoryID, ForumID, ThreadID,
 *                   WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate,
 *                   WatchEndDate
 * Excluded columns:
 */
public class WatchBean {
    /*************************************************************************
     * NOTE: below constants MUST NOT be changed IN ALL CASES,
     *       or it will break the compatibility
     *************************************************************************/
    public static final int WATCH_OPTION_DEFAULT    = 0;
    public static final int WATCH_OPTION_LIVE       = 1;
    public static final int WATCH_OPTION_HOURLY     = 2;
    public static final int WATCH_OPTION_DAILY      = 3;
    public static final int WATCH_OPTION_WEEKLY     = 4;

    public static final int WATCH_TYPE_DEFAULT      = 0;
    public static final int WATCH_TYPE_DIGEST       = 1;
    public static final int WATCH_TYPE_NONDIGEST    = 2;

    public static final int SELECT_GLOBAL_WATCH     = 0;
    public static final int SELECT_CATEGORY_WATCH   = 1;
    public static final int SELECT_FORUM_WATCH      = 2;
    public static final int SELECT_THREAD_WATCH     = 3;

    private int watchID;
    private int memberID;
    private int categoryID;
    private int forumID;
    private int threadID;
    private int watchType;
    private int watchOption;
    private int watchStatus;
    private Timestamp watchCreationDate;
    private Timestamp watchLastSentDate;
    private Timestamp watchEndDate;

    public int getWatchID() {
        return watchID;
    }
    public void setWatchID(int watchID) {
        this.watchID = watchID;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getForumID() {
        return forumID;
    }
    public void setForumID(int forumID) {
        this.forumID = forumID;
    }

    public int getThreadID() {
        return threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getWatchType() {
        return watchType;
    }
    public void setWatchType(int watchType) {
        this.watchType = watchType;
    }

    public int getWatchOption() {
        return watchOption;
    }
    public void setWatchOption(int watchOption) {
        this.watchOption = watchOption;
    }

    public int getWatchStatus() {
        return watchStatus;
    }
    public void setWatchStatus(int watchStatus) {
        this.watchStatus = watchStatus;
    }

    public Timestamp getWatchCreationDate() {
        return watchCreationDate;
    }
    public void setWatchCreationDate(Timestamp watchCreationDate) {
        this.watchCreationDate = watchCreationDate;
    }

    public Timestamp getWatchLastSentDate() {
        return watchLastSentDate;
    }
    public void setWatchLastSentDate(Timestamp watchLastSentDate) {
        this.watchLastSentDate = watchLastSentDate;
    }

    public Timestamp getWatchEndDate() {
        return watchEndDate;
    }
    public void setWatchEndDate(Timestamp watchEndDate) {
        this.watchEndDate = watchEndDate;
    }

    // Store the ThreadBean if this is a Thread Watch
    ThreadBean threadBean = null;

    public ThreadBean getThreadBean() {
        return threadBean;
    }
    public void setThreadBean(ThreadBean threadBean) {
        this.threadBean = threadBean;
    }

    public static void validateWatchType(int type) throws IllegalArgumentException {
        if ((type < WATCH_TYPE_DEFAULT) || (type > WATCH_TYPE_NONDIGEST)) {
            throw new IllegalArgumentException("Invalid WatchType = " + type);
        }
    }

    public static void validateWatchOption(int option) throws IllegalArgumentException {
        if ((option < WATCH_OPTION_DEFAULT) || (option > WATCH_OPTION_WEEKLY)) {
            throw new IllegalArgumentException("Invalid WatchOption = " + option);
        }
    }
    
    public static void validateWatchSelect(int select) throws IllegalArgumentException {
        if ((select < SELECT_GLOBAL_WATCH) || (select > SELECT_THREAD_WATCH)) {
            throw new IllegalArgumentException("Invalid WatchSelect = " + select);
        }
    }
    
} //end of class WatchBean
