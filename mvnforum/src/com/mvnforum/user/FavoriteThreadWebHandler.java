/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/FavoriteThreadWebHandler.java,v 1.30 2009/05/04 09:34:27 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.30 $
 * $Date: 2009/05/04 09:34:27 $
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
package com.mvnforum.user;

import java.sql.Timestamp;
import java.util.Locale;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;
import com.mvnforum.db.*;

public class FavoriteThreadWebHandler {

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public FavoriteThreadWebHandler() {
    }

    public void processAdd(GenericRequest request)
        throws BadInputException, CreateException, DatabaseException, ForeignKeyNotFoundException,
        ObjectNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int memberID = onlineUser.getMemberID();

        // check to make sure that this user does not exceed his favorite max
        int currentFavoriteCount = DAOFactory.getFavoriteThreadDAO().getNumberOfFavoriteThreads_inMember(memberID);
        int maxFavorites = MVNForumConfig.getMaxFavoriteThreads();
        if (currentFavoriteCount >= maxFavorites) {
            //@todo: choose a better exception class
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.over_favorite_quota", new Object[] {new Integer(maxFavorites)});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("You have already use all your favorite quota (" + maxFavorites + ").");
        }

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        int threadID                    = GenericParamUtil.getParameterInt(request, "thread");
        Timestamp favoriteCreationDate  = now;
        int favoriteType                = 0;//@todo implement it later
        int favoriteOption              = 0;//@todo implement it later
        int favoriteStatus              = 0;//@todo implement it later
        Locale locale = I18nUtil.getLocaleInRequest(request);

        ThreadBean threadBean = null;
        try {
            threadBean = DAOFactory.getThreadDAO().getThread(threadID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        int forumID = threadBean.getForumID();

        ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);

        // now check permission the this user have the readPost permission
        permission.ensureCanReadPost(forumID);

        // has the permission now, then insert to database
        try {
            DAOFactory.getFavoriteThreadDAO().create(memberID, threadID, forumID,
                                               favoriteCreationDate, favoriteType, favoriteOption,
                                               favoriteStatus);
        } catch (DuplicateKeyException ex) {
            // already add favorite thread, just ignore
        }
    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        // primary key column(s)
        int memberID = onlineUser.getMemberID();
        int threadID = GenericParamUtil.getParameterInt(request, "thread");

        DAOFactory.getFavoriteThreadDAO().delete(memberID, threadID);
    }
}
