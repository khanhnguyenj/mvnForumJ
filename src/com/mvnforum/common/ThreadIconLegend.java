/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/ThreadIconLegend.java,v 1.14 2007/10/09 11:09:16 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.14 $
 * $Date: 2007/10/09 11:09:16 $
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

import com.mvnforum.MVNForumConfig;

public class ThreadIconLegend {

    public static final String THREAD_ICON_TYPE_STICKY                = "sticky.gif";
    public static final String THREAD_ICON_TYPE_FORUM_ANNOUNCEMENT    = "announce.gif";
    public static final String THREAD_ICON_TYPE_GLOBAL_ANNOUNCEMENT   = "global_announce.gif";

    public static final String THREAD_ICON_COLD_READ_ACTIVE           = "t_cold_read_active.gif";
    public static final String THREAD_ICON_COLD_READ_CLOSED           = "t_cold_read_closed.gif";
    public static final String THREAD_ICON_COLD_READ_LOCKED           = "t_cold_read_locked.gif";
    public static final String THREAD_ICON_COLD_UNREAD_ACTIVE         = "t_cold_unread_active.gif";
    public static final String THREAD_ICON_COLD_UNREAD_CLOSED         = "t_cold_unread_closed.gif";
    public static final String THREAD_ICON_COLD_UNREAD_LOCKED         = "t_cold_unread_locked.gif";
    public static final String THREAD_ICON_HOT_READ_ACTIVE            = "t_hot_read_active.gif";
    public static final String THREAD_ICON_HOT_READ_CLOSED            = "t_hot_read_closed.gif";
    public static final String THREAD_ICON_HOT_READ_LOCKED            = "t_hot_read_locked.gif";
    public static final String THREAD_ICON_HOT_UNREAD_ACTIVE          = "t_hot_unread_active.gif";
    public static final String THREAD_ICON_HOT_UNREAD_CLOSED          = "t_hot_unread_closed.gif";
    public static final String THREAD_ICON_HOT_UNREAD_LOCKED          = "t_hot_unread_locked.gif";

    public static final String THREAD_ICON_PRIORITY_LOW               = "icon_low.gif";
    public static final String THREAD_ICON_PRIORITY_NORMAL            = "icon_normal.gif";
    public static final String THREAD_ICON_PRIORITY_HIGH              = "icon_high.gif";

    private boolean hasColdReadActive       = false;
    private boolean hasColdReadClosed       = false;
    private boolean hasColdReadLocked       = false;
    private boolean hasColdUnreadActive     = false;
    private boolean hasColdUnreadClosed     = false;
    private boolean hasColdUnreadLocked     = false;
    private boolean hasHotReadActive        = false;
    private boolean hasHotReadClosed        = false;
    private boolean hasHotReadLocked        = false;
    private boolean hasHotUnreadActive      = false;
    private boolean hasHotUnreadClosed      = false;
    private boolean hasHotUnreadLocked      = false;
    private boolean hasPriorityLow          = false;
    private boolean hasPriorityNormal       = false;
    private boolean hasPriorityHigh         = false;
    private boolean hasSticky               = false;
    private boolean hasAnnouncement         = false;
    private boolean hasGlobalAnnouncement   = false;

    //check should show info.
    //fix problem with xhtml validator in listforums page
    public boolean isHasThreadIconLegend() {
        return (hasColdReadActive || hasColdReadClosed || hasColdReadLocked || hasColdUnreadActive || hasColdUnreadClosed ||
                hasColdUnreadLocked || hasHotReadActive || hasHotReadClosed || hasHotReadLocked ||hasHotUnreadActive ||
                hasHotUnreadClosed || hasHotUnreadLocked || hasPriorityLow || hasPriorityNormal || hasPriorityHigh ||
                hasSticky || hasAnnouncement || hasGlobalAnnouncement);
    }

    public void updateIconLegend(String threadIcon) {
        if (threadIcon == null) {
            return;
        }
        if (threadIcon.equals(THREAD_ICON_COLD_READ_ACTIVE)) {
            hasColdReadActive = true;
        } else if (threadIcon.equals(THREAD_ICON_COLD_READ_CLOSED)) {
            hasColdReadClosed = true;
        } else if (threadIcon.equals(THREAD_ICON_COLD_READ_LOCKED)) {
            hasColdReadLocked = true;
        } else if (threadIcon.equals(THREAD_ICON_COLD_UNREAD_ACTIVE)) {
            hasColdUnreadActive = true;
        } else if (threadIcon.equals(THREAD_ICON_COLD_UNREAD_CLOSED)) {
            hasColdUnreadClosed = true;
        } else if (threadIcon.equals(THREAD_ICON_COLD_UNREAD_LOCKED)) {
            hasColdUnreadLocked = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_READ_ACTIVE)) {
            hasHotReadActive = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_READ_CLOSED)) {
            hasHotReadClosed = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_READ_LOCKED)) {
            hasHotReadLocked = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_UNREAD_ACTIVE)) {
            hasHotUnreadActive = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_UNREAD_CLOSED)) {
            hasHotUnreadClosed = true;
        } else if (threadIcon.equals(THREAD_ICON_HOT_UNREAD_LOCKED)) {
            hasHotUnreadLocked = true;

        } else if ( (threadIcon.equals(THREAD_ICON_PRIORITY_LOW)) && MVNForumConfig.getEnableLowPriorityIcon() ) {
            hasPriorityLow = true;
        } else if ( (threadIcon.equals(THREAD_ICON_PRIORITY_NORMAL)) && MVNForumConfig.getEnableNormalPriorityIcon() ) {
            hasPriorityNormal = true;
        } else if ( (threadIcon.equals(THREAD_ICON_PRIORITY_HIGH)) && MVNForumConfig.getEnableHighPriorityIcon() ) {
            hasPriorityHigh = true;

        } else if (threadIcon.equals(THREAD_ICON_TYPE_STICKY)) {
            hasSticky = true;
        } else if (threadIcon.equals(THREAD_ICON_TYPE_FORUM_ANNOUNCEMENT)) {
            hasAnnouncement = true;
        } else if (threadIcon.equals(THREAD_ICON_TYPE_GLOBAL_ANNOUNCEMENT)) {
            hasGlobalAnnouncement = true;
        }
    }

    public boolean isHasColdReadClosed() {
        return hasColdReadClosed;
    }

    public boolean isHasColdReadLocked() {
        return hasColdReadLocked;
    }

    public boolean isHasColdReadActive() {
        return hasColdReadActive;
    }

    public boolean isHasColdUnreadClosed() {
        return hasColdUnreadClosed;
    }

    public boolean isHasColdUnreadLocked() {
        return hasColdUnreadLocked;
    }

    public boolean isHasColdUnreadActive() {
        return hasColdUnreadActive;
    }

    public boolean isHasHotReadClosed() {
        return hasHotReadClosed;
    }

    public boolean isHasHotReadLocked() {
        return hasHotReadLocked;
    }

    public boolean isHasHotReadActive() {
        return hasHotReadActive;
    }

    public boolean isHasHotUnreadClosed() {
        return hasHotUnreadClosed;
    }

    public boolean isHasHotUnreadLocked() {
        return hasHotUnreadLocked;
    }

    public boolean isHasHotUnreadActive() {
        return hasHotUnreadActive;
    }

    public boolean isHasPriorityLow() {
        return hasPriorityLow;
    }

    public boolean isHasPriorityNormal() {
        return hasPriorityNormal;
    }

    public boolean isHasPriorityHigh() {
        return hasPriorityHigh;
    }

    public boolean isHasSticky() {
        return hasSticky;
    }

    public boolean isHasAnnouncement() {
        return hasAnnouncement;
    }

    public boolean isHasGlobalAnnouncement() {
        return hasGlobalAnnouncement;
    }

}
