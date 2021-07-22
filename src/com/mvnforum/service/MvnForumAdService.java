/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/MvnForumAdService.java,v 1.7 2008/03/21 03:01:15 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.7 $
 * $Date: 2008/03/21 03:01:15 $
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
 * @author: Trung Tang Bao
 */
package com.mvnforum.service;

public interface MvnForumAdService {

    public static final int ZONE_NAME_FIELD_COUNT                   = 32;
    
    public static final int ZONE_NAME_FORUM_AD_POSTITION_HEART      = 0;
    
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_1     = 1;
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_2     = 2;
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_3     = 3;
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_4     = 4;
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_5     = 5;
    public static final int ZONE_NAME_FORUM_HEADER_1_POSITION_6     = 6;
    
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_1     = 7;
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_2     = 8;
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_3     = 9;
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_4     = 10;
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_5     = 11;
    public static final int ZONE_NAME_FORUM_HEADER_2_POSITION_6     = 12;
    
    public static final int ZONE_NAME_FORUM_HEADER_3_POSITION_1     = 13;
    public static final int ZONE_NAME_FORUM_HEADER_3_POSITION_2     = 14;
    public static final int ZONE_NAME_FORUM_HEADER_3_POSITION_3     = 15;

    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_1     = 16;
    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_2     = 17;
    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_3     = 18;
    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_4     = 19;
    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_5     = 20;
    public static final int ZONE_NAME_FORUM_FOOTER_1_POSITION_6     = 21;
    
    public static final int ZONE_NAME_FORUM_FIRST_POST              = 22;
    public static final int ZONE_NAME_FORUM_FIRST_POST_PAGE_2       = 23;
    public static final int ZONE_NAME_FORUM_LAST_ODD_POST           = 24;
    public static final int ZONE_NAME_FORUM_LAST_EVEN_POST          = 25;
    public static final int ZONE_NAME_FORUM_FIRST_ATTACHEMNT        = 26;
    public static final int ZONE_NAME_FORUM_VIEW_MESSAGE            = 27;
    public static final int ZONE_NAME_FORUM_FIRST_NORMAL_THREAD     = 28;
    public static final int ZONE_NAME_FORUM_FIRST_ACTIVE_THREAD     = 29;
    public static final int ZONE_NAME_FORUM_FIRST_UNANSWERED_THREAD = 30;
    public static final int ZONE_NAME_FORUM_FIRST_RECEND_THREAD     = 31;

    public int getAdZone(int zoneName);
    
    public String getZone(int zoneID);

    public void reload();
}
