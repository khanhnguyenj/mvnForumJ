/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/CategoryCache.java,v 1.17 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.17 $
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

import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.whirlycott.cache.*;

public class CategoryCache {
    
    private static final Logger log = LoggerFactory.getLogger(CategoryCache.class);

    public static final long TIME_OUT = DateUtil.HOUR;

    // static singleton variable
    private static CategoryCache instance = new CategoryCache();

    // instance variable
    private Cache cache;

    public CategoryCache() {
        //Use the cache manager to create the default cache
        try {
            if (MVNForumConfig.getEnableCacheCategory()) {
                cache = CacheManager.getInstance().getCache("category");
            }
        } catch (CacheException ex) {
            log.error("Cannot get the WhirlyCache. Category caching is disabled.", ex);
        } catch (LinkageError e) {
            // @todo: Should be never throw
            log.error("Cannot get the WhirlyCache caused by Package Conflict. Category caching is disabled.", e);
        }
    }

    /**
     * Returns the single instance
     * @return CategoryCache : the singleton instance.
     *
     * NOTE: if use normal singleton pattern, this method should be synchronized
     */
    public static CategoryCache getInstance() {
        return instance;
    }

    public String getEfficiencyReport() {
        String result = "No report";
        if (cache == null) {
            if (MVNForumConfig.getEnableCacheCategory() == false) {
                result = "Cache is disabled.";
            } else {
                result = "Cache cannot be inited";
            }
        } else if (cache instanceof CacheDecorator) {
            result = ((CacheDecorator) cache).getEfficiencyReport();
        }
        return result;
    }

    public void clear() {
        if (cache != null) {
            cache.clear();
        }
    }

    // instance variable
    // private List beanArray = null;

    /**
     * IMPORTANT NOTE: The caller must not alter the returned collection.
     * Any attempt to modify it will throw an <code>UnsupportedOperationException</code>.
     */
    public List getBeans() throws DatabaseException {
        List result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getBeans");
            String key = buffer.toString();
            result = (List) cache.retrieve(key);
            if (result == null) {
                result = (List) DAOFactory.getCategoryDAO().getCategories();
                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = (List) DAOFactory.getCategoryDAO().getCategories();
        }
        return Collections.unmodifiableList(result);
    }

    public CategoryBean getBean(int categoryID) throws DatabaseException, ObjectNotFoundException {

        // ensureNewData();
        List beans = getBeans(); // We do not want the list to change in the process.

        int size = beans.size();
        for (int i = 0; i < size; i++) {
            CategoryBean bean = (CategoryBean) beans.get(i);
            if (bean.getCategoryID() == categoryID) {
                return bean;
            }
        }
        // @todo : localize me
        throw new ObjectNotFoundException("Cannot find the row in table Category " + "where primary key = (" + categoryID + ").");
    }

    public CategoryBean getBean(String categoryName) throws DatabaseException, ObjectNotFoundException {

        // ensureNewData();
        List beans = getBeans(); // We do not want the list to change in the process.

        for (Iterator it = beans.iterator(); it.hasNext();) {
            CategoryBean bean = (CategoryBean) it.next();
            if (bean.getCategoryName().equals(categoryName)) {
                return bean;
            }
        }
        // @todo : localize me
        throw new ObjectNotFoundException("Cannot find a category with the given name: " + categoryName);
    }

}
