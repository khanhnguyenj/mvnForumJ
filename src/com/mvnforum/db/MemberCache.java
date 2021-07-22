/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MemberCache.java,v 1.23 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
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

import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.MVNForumPermissionWebHelper;
import com.whirlycott.cache.*;

public class MemberCache {
    
    private static final Logger log = LoggerFactory.getLogger(MemberCache.class);

    public static final long TIME_OUT       = DateUtil.HOUR;
    public static final long SHORT_TIME_OUT = 2 * DateUtil.MINUTE;

    // static singleton variable
    private static MemberCache instance = new MemberCache();

    // instance variable
    private Cache cache;

    public MemberCache() {
        //Use the cache manager to create the default cache
        try {
            if (MVNForumConfig.getEnableCacheMember()) {
                cache = CacheManager.getInstance().getCache("member");
            }
        } catch (CacheException ex) {
            log.error("Cannot get the WhirlyCache. Member caching is disabled.", ex);
        } catch (LinkageError e) {
            // TODO: Should be never throw
            log.error("Cannot get the WhirlyCache caused by Package Conflict. Member caching is disabled.", e);
        }
    }

    /**
     * Returns the single instance
     * @return MemberCache : the singleton instance.
     *
     * NOTE: if use normal singleton pattern, this method should be synchronized
     */
    public static MemberCache getInstance() {
        return instance;
    }

    public String getEfficiencyReport() {
        String result = "No report";
        if (cache == null) {
            if (MVNForumConfig.getEnableCacheMember() == false) {
                result = "Cache is disabled.";
            } else {
                result = "Cache cannot be inited";
            }
        } else if (cache instanceof CacheDecorator) {
            result = ((CacheDecorator)cache).getEfficiencyReport();
        }
        return result;
    }

    public void clear() {
        if (cache != null) {
            cache.clear();
        }
    }

    public MemberBean getMember(int memberID)
        throws DatabaseException, ObjectNotFoundException {

        MemberBean memberBean = null;
        if (cache != null) {
            String key = "getMember" + memberID;
            memberBean = (MemberBean)cache.retrieve(key);
            if (memberBean == null) {
                //log.debug("MemberCache: about to call getMember with id = " + memberID);
                memberBean = DAOFactory.getMemberDAO().getMember(memberID);
                cache.store(key, memberBean, TIME_OUT);
            }
        } else {
            memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        }

        if (memberBean == null) {
            throw new ObjectNotFoundException("Cannot find the row in table Member "
                                          + "where primary key = (" + memberID + ").");
        }
        return memberBean;
    }
    public void removeMember(int memberID) {
        if (cache != null) {
            cache.remove("getMember" + memberID);
        }
    }

    public int getMemberIDFromMemberName(String memberName)
        throws DatabaseException, ObjectNotFoundException {

        int memberID = -1;
        if (cache != null) {
            String key = "getMemberIDFromMemberName_" + memberName;
            Integer memberIDInterger = (Integer)cache.retrieve(key);
            if (memberIDInterger == null) {
                //log.debug("MemberCache: about to call getMemberIDFromMemberName with id = " + memberID);
                memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
                cache.store(key, new Integer(memberID), TIME_OUT);
            } else {
                memberID = memberIDInterger.intValue();
            }
        } else {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }

        if (memberID == -1) {
            throw new ObjectNotFoundException("Cannot find the row in table Member "
                                          + "where alternate key = (" + memberName + ").");
        }
        return memberID;
    }

    public Collection getPermissionsForGroupGuest()
        throws DatabaseException {

        Collection permList = null;
        if (cache != null) {
            String key = "getPermissionsForGroupGuest";
            permList = (Collection) cache.retrieve(key);
            if (permList == null) {
                permList = MVNForumPermissionWebHelper.getPermissionsForGroupGuest();
                cache.store(key, permList, SHORT_TIME_OUT);
            }
        } else {
            permList = MVNForumPermissionWebHelper.getPermissionsForGroupGuest();
        }

        return permList;
    }
    public void removePermissionsForGroupGuest() {
        if (cache != null) {
            cache.remove("getPermissionsForGroupGuest");
        }
    }

    public Collection getPermissionsForGroupGuestInForums()
        throws DatabaseException {

        Collection permList = null;
        if (cache != null) {
            String key = "getPermissionsForGroupGuestInForums";
            permList = (Collection) cache.retrieve(key);
            if (permList == null) {
                permList = MVNForumPermissionWebHelper.getPermissionsForGroupGuestInForums();
                cache.store(key, permList, SHORT_TIME_OUT);
            }
        } else {
            permList = MVNForumPermissionWebHelper.getPermissionsForGroupGuestInForums();
        }

        return permList;
    }
    public void removePermissionsForGroupGuestInForums() {
        if (cache != null) {
            cache.remove("getPermissionsForGroupGuestInForums");
        }
    }

}
