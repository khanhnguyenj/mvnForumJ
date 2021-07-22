/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/MemberAvatarTimeTracking.java,v 1.3 2010/06/17 09:49:45 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2010/06/17 09:49:45 $
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
 * @author: Nhan Luu Duy
 */
package com.mvnforum.common;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class is for tracking the time of the avatar, which will be put in the url
 * to get avatar to prevent cache in web proxy
 */
public class MemberAvatarTimeTracking {

    private static MemberAvatarTimeTracking instance = new MemberAvatarTimeTracking();
    
    private Map timeTrackingMap = new TreeMap();
    
    private MemberAvatarTimeTracking() {
    }
    
    public static MemberAvatarTimeTracking getInstance() {
        return instance;
    }
    
    public void putTimeTracking(int memberID, long lastModified) {
        timeTrackingMap.put(new Integer(memberID), new Long(lastModified));
    }
    
    public long getTimeTracking(int memberID) {
        Long timeTrackingMapInt = (Long)(timeTrackingMap.get(new Integer(memberID)));
        if (timeTrackingMapInt == null) {
            return 0;
        } 
        return timeTrackingMapInt.longValue();
    }
}
