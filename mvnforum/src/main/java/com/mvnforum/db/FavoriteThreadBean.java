/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/FavoriteThreadBean.java,v 1.9 2007/10/09 11:09:18 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.9 $
 * $Date: 2007/10/09 11:09:18 $
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
 * Included columns: MemberID, ThreadID, ForumID, FavoriteCreationDate, FavoriteType,
 *                   FavoriteOption, FavoriteStatus
 * Excluded columns:
 */
public class FavoriteThreadBean {
    private int memberID;
    private int threadID;
    private int forumID;
    private Timestamp favoriteCreationDate;
    private int favoriteType;
    private int favoriteOption;
    private int favoriteStatus;

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

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

    public Timestamp getFavoriteCreationDate() {
        return favoriteCreationDate;
    }
    public void setFavoriteCreationDate(Timestamp favoriteCreationDate) {
        this.favoriteCreationDate = favoriteCreationDate;
    }

    public int getFavoriteType() {
        return favoriteType;
    }
    public void setFavoriteType(int favoriteType) {
        this.favoriteType = favoriteType;
    }

    public int getFavoriteOption() {
        return favoriteOption;
    }
    public void setFavoriteOption(int favoriteOption) {
        this.favoriteOption = favoriteOption;
    }

    public int getFavoriteStatus() {
        return favoriteStatus;
    }
    public void setFavoriteStatus(int favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

} //end of class FavoriteThreadBean
