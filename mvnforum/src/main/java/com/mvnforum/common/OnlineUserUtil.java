/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/OnlineUserUtil.java,v 1.24 2009/02/02 11:18:58 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.24 $
 * $Date: 2009/02/02 11:18:58 $
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
 * @author: Hau Nguyen
 */
package com.mvnforum.common;

import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.*;
import com.mvnforum.user.ActionInForumUserModule;

public final class OnlineUserUtil {

    // prevent instantiation
    private OnlineUserUtil() {
    }

    public static void setRequestAttributeOfOnlineActions(GenericRequest request, int pageID, Object pageParam)
        throws AuthenticationException, DatabaseException {

        AssertionUtil.doAssert(MVNForumConfig.getEnableOnlineUsers(), "Cannot list online users because ONLINE_USERS feature is disabled by administrator.");
        
        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
        onlineUserManager.getOnlineUser(request);

        boolean duplicateUsers = MVNForumConfig.getEnableDuplicateOnlineUsers();
        Collection onlineUserActions = onlineUserManager.getOnlineUserActions(0, duplicateUsers);

        Collection userActions = new ArrayList();

        for (Iterator countIterator = onlineUserActions.iterator(); countIterator.hasNext(); ) {

            OnlineUserAction onlineUserAction = (OnlineUserAction) countIterator.next();

            if (onlineUserAction == null) {
                continue;
            }

            if (onlineUserAction.getMemberName().equals(MVNForumConfig.getDefaultGuestName())) {
                continue;
            }

            int userPageID = onlineUserAction.getPageID();
            if (userPageID == pageID) {
                if (pageParam == null) {
                    userActions.add(onlineUserAction);
                } else {// pageParam != null
                    Object userPageParam = onlineUserAction.getPageParam();
                    if (pageParam.equals(userPageParam)) {
                        userActions.add(onlineUserAction);
                    }
                }
            }
        }

        request.setAttribute("UserActions", userActions);

    }

    /**
     * This method is used to update the Action of OnlineUser
     * @param request
     * @param requestURI
     * @throws AuthenticationException
     * @throws DatabaseException
     * @throws MissingURLMapEntryException
     */
    public static void updateOnlineUserAction(GenericRequest request, String requestURI)
        throws AuthenticationException, DatabaseException, MissingURLMapEntryException {

        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

        // This will ensure that all time-out users are removed (if any)
        onlineUserManager.getOnlineUser(request);

        // the following 2 lines fix the bug that no online user found in the first time request
        Action action = new ActionInForumUserModule(request, requestURI);// may throw MissingURLMapEntryException
        onlineUserManager.updateOnlineUserAction(request, action);

    }

}
