/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ForumCache.java,v 1.20 2009/02/24 05:05:44 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.20 $
 * $Date: 2009/02/24 05:05:44 $
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

import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.whirlycott.cache.*;

public class ForumCache {
    
    private static final Logger log = LoggerFactory.getLogger(ForumCache.class);

    public static final long TIME_OUT = DateUtil.HOUR;

    // static singleton variable
    private static ForumCache instance = new ForumCache();

    // instance variable
    private Cache cache;

    public ForumCache() {
        //Use the cache manager to create the default cache
        try {
            if (MVNForumConfig.getEnableCacheForum()) {
                cache = CacheManager.getInstance().getCache("forum");
            }
        } catch (CacheException ex) {
            log.error("Cannot get the WhirlyCache. Forum caching is disabled.", ex);
        } catch (LinkageError e) {
            // @todo: Should be never throw
            log.error("Cannot get the WhirlyCache caused by Package Conflict. Forum caching is disabled.", e);
        }
    }

    /**
     * Returns the single instance
     * @return ForumCache : the singleton instance.
     *
     * NOTE: if use normal singleton pattern, this method should be synchronized
     */
    public static ForumCache getInstance() {
        return instance;
    }

    public String getEfficiencyReport() {
        String result = "No report";
        if (cache == null) {
            if (MVNForumConfig.getEnableCacheForum() == false) {
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

    /**
     * IMPORTANT NOTE: The caller must not alter the returned collection.
     * Any attempt to modify it will throw an <code>UnsupportedOperationException</code>.
     */
    public List getBeans() throws DatabaseException {
        // ensureNewData();
        List result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getBeans");
            String key = buffer.toString();
            result = (List) cache.retrieve(key);
            if (result == null) {
                result = (List) DAOFactory.getForumDAO().getForums();
                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = (List) DAOFactory.getForumDAO().getForums();
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * IMPORTANT NOTE: The caller must not alter the returned collection.
     * Any attempt to modify it will throw an <code>UnsupportedOperationException</code>.
     */
    public List getForums_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order) throws DatabaseException {
        List result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getForums_withSortSupport_limit-").append("offset").append(offset);
            buffer.append("-rowsToReturn").append(rowsToReturn);
            buffer.append("-sort").append(sort);
            buffer.append("-order").append(order);

            String key = buffer.toString();
            result = (List) cache.retrieve(key);
            if (result == null) {
                result = (List) DAOFactory.getForumDAO().getForums_withSortSupport_limit(offset, rowsToReturn, sort, order);
                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = (List) DAOFactory.getForumDAO().getForums_withSortSupport_limit(offset, rowsToReturn, sort, order);
        }

        return Collections.unmodifiableList(result);
    }

    public ForumBean getBean(int forumID) throws DatabaseException, ObjectNotFoundException {

        List beans = getBeans(); // We do not want the list to change in the process.

        int size = beans.size();
        for (int i = 0; i < size; i++) {
            ForumBean bean = (ForumBean) beans.get(i);
            if (bean.getForumID() == forumID) {
                return bean;
            }
        }
        // @todo : localize me
        throw new ObjectNotFoundException("Cannot find the row in table Forum where primary key = (" + forumID + ").");
    }

    public ForumBean getBean(String forumName) throws DatabaseException, ObjectNotFoundException {

        List beans = getBeans(); // We do not want the list to change in the process.

        for (Iterator it = beans.iterator(); it.hasNext();) {
            ForumBean bean = (ForumBean) it.next();
            if (bean.getForumName().equals(forumName)) {
                return bean;
            }
        }
        // @todo : localize me
        throw new ObjectNotFoundException("Cannot find a forum with the given name: " + forumName);
    }

    public int getNumberOfBeans(int categoryID) throws DatabaseException {

        List beans = getBeans(); // We do not want the list to change in the process.

        int forumsInCategory = 0;
        for (Iterator it = beans.iterator(); it.hasNext();) {
            ForumBean bean = (ForumBean) it.next();
            if (bean.getCategoryID() == categoryID) {
                forumsInCategory++;
            }
        }
        return forumsInCategory;
    }

}
