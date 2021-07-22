/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/WatchUtil.java,v 1.14 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/01/02 15:12:55 $
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
 * @author: Cord         cord_sw@lupinex.com
 */
package com.mvnforum.user;

import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.db.*;

public final class WatchUtil {

    // prevent instantiation
    private WatchUtil() {
    }

    private static boolean isCategoryInWatchs(int categoryID, ArrayList categoryWatchs) {
        for (int catIndex = 0; catIndex < categoryWatchs.size(); catIndex++) {
            WatchBean watchBean = (WatchBean) categoryWatchs.get(catIndex);
            int currentCategoryID = watchBean.getCategoryID();
            if (currentCategoryID == categoryID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Optimize the watch collection, note that the thread is not optimized
     * @param watchBeans
     * @return the watch collection that has been optimized
     */
    static Collection optimize(Collection watchBeans)
        throws DatabaseException, ObjectNotFoundException {

        // now check the global watch first
        Collection globalWatchs = getGlobalWatchs(watchBeans);
        if (globalWatchs.size() == 1) {
            return globalWatchs;
        }

        ArrayList categoryWatchs = getCategoryWatchs(watchBeans);

        // next, set it to the category watch beans
        ArrayList optimizedWatchs = new ArrayList();// MUST use ArrayList for the optimizedWatchs
        optimizedWatchs.addAll(categoryWatchs);

        // now remove the redundant forum watch beans
        ArrayList forumWatchs = getForumWatchs(watchBeans);
        ForumCache forumCache = ForumCache.getInstance();
        for (int forumIndex = 0; forumIndex < forumWatchs.size(); forumIndex++) {
            WatchBean forumWatch = (WatchBean) forumWatchs.get(forumIndex);
            ForumBean forumBean = forumCache.getBean(forumWatch.getForumID());
            int categoryID = forumBean.getCategoryID();

            // now check if the categoryID is in categoryWatchs or not
            if (isCategoryInWatchs(categoryID, categoryWatchs) == false) {
                optimizedWatchs.add(forumWatch);
            }
        }

        // finally, add the thread watches (not optimize thread) (why not optimize thread ???)
        ArrayList threadWatchs = getThreadWatchs(watchBeans);
        optimizedWatchs.addAll(threadWatchs);

        return optimizedWatchs;
    }

    static ArrayList getGlobalWatchs(Collection watchBeans) {

        ArrayList globalWatchs = new ArrayList(1);//maximum is 1 global watch

        for (Iterator iterator = watchBeans.iterator(); iterator.hasNext(); ) {
            WatchBean watchBean = (WatchBean) iterator.next();
            if ( (watchBean.getCategoryID() == 0) && (watchBean.getForumID() == 0) && (watchBean.getThreadID() == 0) ) {
                globalWatchs.add(watchBean);
            }
        }
        return globalWatchs;
    }

    static ArrayList getCategoryWatchs(Collection watchBeans) {

        ArrayList categoryWatchs = new ArrayList();

        for (Iterator iterator = watchBeans.iterator(); iterator.hasNext(); ) {
            WatchBean watchBean = (WatchBean) iterator.next();
            if (watchBean.getCategoryID() != 0) {
                if ( (watchBean.getForumID()==0) && (watchBean.getThreadID()==0) ) {
                    categoryWatchs.add(watchBean);
                } else {
                    //TODO: should we delete watch here ???
                }
            }
        }
        return categoryWatchs;
    }

    static ArrayList getForumWatchs(Collection watchBeans) {
        ArrayList forumWatchs = new ArrayList();
        Iterator iterator = watchBeans.iterator();
        while (iterator.hasNext()) {
            WatchBean watchBean = (WatchBean) iterator.next();
            if (watchBean.getForumID() != 0) {
                if ( (watchBean.getCategoryID()==0) && (watchBean.getThreadID()==0) ) {
                    forumWatchs.add(watchBean);
                } else {
                    //TODO: delete watch here
                }
            }
        }
        return forumWatchs;
    }

    static ArrayList getThreadWatchs(Collection watchBeans) {
        ArrayList threadWatchs = new ArrayList();
        Iterator iterator = watchBeans.iterator();
        while (iterator.hasNext()) {
            WatchBean watchBean = (WatchBean) iterator.next();
            if (watchBean.getThreadID() != 0) {
                if ( (watchBean.getCategoryID()==0) && (watchBean.getForumID()==0) ) {
                    threadWatchs.add(watchBean);
                } else {
                    //TODO: delete watch here
                }
            }
        }
        return threadWatchs;
    }

    public static boolean isWatched(int memberID, int categoryID, int forumID, int threadID) throws DatabaseException {
        try {
            DAOFactory.getWatchDAO().findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(memberID, categoryID, forumID, threadID);
        } catch (ObjectNotFoundException e) {
            return false;
        }
        return true;
    }

    public static boolean isGlobalWatched(int memberID)  throws DatabaseException {
        return isWatched(memberID, 0, 0, 0);
    }

    public static boolean isCategoryWatched(int memberID, CategoryBean categoryBean) throws DatabaseException, ObjectNotFoundException {
        int parentCategoryID = categoryBean.getParentCategoryID();
        if (parentCategoryID != 0) {
            CategoryBean parentCategoryBean = CategoryCache.getInstance().getBean(parentCategoryID);
            if (isCategoryWatched(memberID, parentCategoryBean)) {
                //temporarily comment out until we implement category hierarchy watching
                //return true;
            }
        }
        return isWatched(memberID, categoryBean.getCategoryID(), 0, 0) || isGlobalWatched(memberID);
    }

    public static boolean isForumWatched(int memberID, ForumBean forumBean) throws DatabaseException, ObjectNotFoundException {
        int categoryID = forumBean.getCategoryID();
        CategoryBean categoryBean = CategoryCache.getInstance().getBean(categoryID);
        return isWatched(memberID, 0, forumBean.getForumID(), 0) || isCategoryWatched(memberID, categoryBean);
    }

    public static boolean isThreadWatched(int memberID, ThreadBean threadBean, ForumBean forumBean) throws DatabaseException, ObjectNotFoundException {
        return isWatched(memberID, 0, 0, threadBean.getThreadID()) || isForumWatched(memberID, forumBean);
    }

}
