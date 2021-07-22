/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/ForumIconLegend.java,v 1.6 2008/12/31 03:46:07 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2008/12/31 03:46:07 $
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
 * @author: Dung Bui
 */
package com.mvnforum.common;

public class ForumIconLegend {

    public static final String FORUM_ICON_READ_ACTIVE       = "f_read_active.gif";
    public static final String FORUM_ICON_READ_CLOSED       = "f_read_closed.gif";
    public static final String FORUM_ICON_READ_LOCKED       = "f_read_locked.gif";
    public static final String FORUM_ICON_READ_DISABLED     = "f_read_disabled.gif";
    public static final String FORUM_ICON_UNREAD_ACTIVE     = "f_unread_active.gif";
    public static final String FORUM_ICON_UNREAD_CLOSED     = "f_unread_closed.gif";
    public static final String FORUM_ICON_UNREAD_LOCKED     = "f_unread_locked.gif";
    public static final String FORUM_ICON_UNREAD_DISABLED   = "f_unread_disabled.gif";

    private boolean hasReadActiveForum        = false;
    private boolean hasUnreadActiveForum      = false;
    private boolean hasReadClosedForum        = false;
    private boolean hasUnreadClosedForum      = false;
    private boolean hasReadLockedForum        = false;
    private boolean hasUnreadLockedForum      = false;
    private boolean hasReadDisabledForum      = false;
    private boolean hasUnreadDisabledForum    = false;

    public ForumIconLegend() {
        
    }
    
    public void updateIconLegend(String forumIcon) {
        if (forumIcon.equals(FORUM_ICON_READ_ACTIVE)) {
            hasReadActiveForum = true;
        } else if (forumIcon.equals(FORUM_ICON_READ_CLOSED)) {
            hasReadClosedForum = true;
        } else if (forumIcon.equals(FORUM_ICON_READ_LOCKED)) {
            hasReadLockedForum = true;
        } else if (forumIcon.equals(FORUM_ICON_READ_DISABLED)) {
            hasReadDisabledForum = true;
        } else if (forumIcon.equals(FORUM_ICON_UNREAD_ACTIVE)) {
            hasUnreadActiveForum = true;
        } else if (forumIcon.equals(FORUM_ICON_UNREAD_CLOSED)) {
            hasUnreadClosedForum = true;
        } else if (forumIcon.equals(FORUM_ICON_UNREAD_LOCKED)) {
            hasUnreadLockedForum = true;
        } else if (forumIcon.equals(FORUM_ICON_UNREAD_DISABLED)) {
            hasUnreadDisabledForum = true;
        }
    }

    public boolean isHasReadActiveForum() {
        return hasReadActiveForum;
    }

    public boolean isHasReadClosedForum() {
        return hasReadClosedForum;
    }

    public boolean isHasReadLockedForum() {
        return hasReadLockedForum;
    }

    public boolean isHasReadDisabledForum() {
        return hasReadDisabledForum;
    }

    public boolean isHasUnreadActiveForum() {
        return hasUnreadActiveForum;
    }

    public boolean isHasUnreadClosedForum() {
        return hasUnreadClosedForum;
    }

    public boolean isHasUnreadLockedForum() {
        return hasUnreadLockedForum;
    }

    public boolean isHasUnreadDisabledForum() {
        return hasUnreadDisabledForum;
    }
    
    public boolean isHasIconLegend() {
        return (hasReadActiveForum || hasUnreadActiveForum || hasReadClosedForum || 
                hasUnreadClosedForum || hasReadLockedForum || hasUnreadLockedForum || 
                hasReadDisabledForum || hasUnreadDisabledForum);
    }

}
