/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PostCache.java,v 1.28 2009/09/07 07:00:25 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.28 $
 * $Date: 2009/09/07 07:00:25 $
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

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.whirlycott.cache.*;

public class PostCache {
    
    private static final Logger log = LoggerFactory.getLogger(PostCache.class);

    public static final long TIME_OUT = DateUtil.MINUTE * 10;

    // static singleton variable
    private static PostCache instance = new PostCache();

    // instance variable
    private Cache cache;

    public PostCache() {
        //Use the cache manager to create the default cache
        try {
            if (MVNForumConfig.getEnableCachePost()) {
                cache = CacheManager.getInstance().getCache("post");
            }
        } catch (CacheException ex) {
            log.error("Cannot get the WhirlyCache. Post caching is disabled.", ex);
        } catch (LinkageError e) {
            // @todo: Should be never throw
            log.error("Cannot get the WhirlyCache caused by Package Conflict. Post caching is disabled.", e);
        }
    }

    /**
     * Returns the single instance
     * @return PostCache : the singleton instance.
     *
     * NOTE: if use normal singleton pattern, this method should be synchronized
     */
    public static PostCache getInstance() {
        return instance;
    }

    public String getEfficiencyReport() {
        String result = "No report";
        if (cache == null) {
            if (MVNForumConfig.getEnableCachePost() == false) {
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

    public PostBean getLastEnablePost_inThread(int threadID)
        throws DatabaseException {

        PostBean postBean = null;
        if (cache != null) {
            String key = "getLastEnablePost_inThread" + threadID;
            postBean = (PostBean) cache.retrieve(key);
            if (postBean == null) {
                //log.debug("PostCache: about to call getLastEnablePost_inThread with id = " + threadID);

                Collection cPostTemp = DAOFactory.getPostDAO().getLastEnablePosts_inThread_limit(threadID, 1);
                AssertionUtil.doAssert(cPostTemp.size() == 1, "Assertion: in thread = " + threadID + ", PostBeans size == 1 (but the value = " + cPostTemp.size() + ")");
                postBean = (PostBean) (cPostTemp.iterator().next());

                cache.store(key, postBean, TIME_OUT);
            }
        } else {
            Collection cPostTemp = DAOFactory.getPostDAO().getLastEnablePosts_inThread_limit(threadID, 1);
            AssertionUtil.doAssert(cPostTemp.size() == 1, "Assertion: in thread = " + threadID + ", PostBeans size == 1 (but the value = " + cPostTemp.size() + ")");
            postBean = (PostBean) (cPostTemp.iterator().next());
        }
        AssertionUtil.doAssert(postBean != null, "Assertion cannot find the row in table Post where getLastEnablePosts_inThread_limit = (" + threadID + ").");
        return postBean;
    }

    public Collection getEnablePosts_inThread_limit(int threadID, int offset, int rowsToReturn)
        throws DatabaseException {

        Collection result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getEnablePosts_inThread_limit");
            buffer.append(threadID).append("_");
            buffer.append(offset).append("_");
            buffer.append(rowsToReturn).append("_");
            String key = buffer.toString();
            result = (Collection) cache.retrieve(key);
            if (result == null) {
                result = DAOFactory.getPostDAO().getEnablePosts_inThread_limit(threadID, offset, rowsToReturn);

                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = DAOFactory.getPostDAO().getEnablePosts_inThread_limit(threadID, offset, rowsToReturn);
        }

        return result;
    }
    
    public PostBean getLastEnablePost_inForum(int forumID)
        throws DatabaseException {
    
        PostBean postBean = null;
        if (cache != null) {
            String key = "getLastEnablePost_inForum" + forumID;
            postBean = (PostBean) cache.retrieve(key);
            if (postBean == null) {
                //log.debug("PostCache: about to call getLastEnablePost_inThread with id = " + threadID);
    
                Collection cPostTemp = DAOFactory.getPostDAO().getLastEnablePosts_inForum_limit(forumID, 1);
                AssertionUtil.doAssert(cPostTemp.size() == 1, "Assertion: in forum = " + forumID + ", PostBeans size == 1 (but the value = " + cPostTemp.size() + ")");
                postBean = (PostBean) (cPostTemp.iterator().next());
    
                cache.store(key, postBean, TIME_OUT);
            }
        } else {
            Collection cPostTemp = DAOFactory.getPostDAO().getLastEnablePosts_inForum_limit(forumID, 1);
            AssertionUtil.doAssert(cPostTemp.size() == 1, "Assertion: in forum = " + forumID + ", PostBeans size == 1 (but the value = " + cPostTemp.size() + ")");
            postBean = (PostBean) (cPostTemp.iterator().next());
        }
        AssertionUtil.doAssert(postBean != null, "Assertion cannot find the row in table Post where getLastEnablePosts_inForum_limit = (" + forumID + ").");
        return postBean;
    }

    public Collection getMostActiveMembers(Timestamp since, int rowsToReturn)
        throws DatabaseException {

        Collection result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getMostActiveMembers");
            //buffer.append(since.getTime()).append("_");
            buffer.append(rowsToReturn).append("_");
            String key = buffer.toString();
            result = (Collection) cache.retrieve(key);
            if (result == null) {
                result = DAOFactory.getPostDAO().getMostActiveMembers(since, rowsToReturn);

                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = DAOFactory.getPostDAO().getMostActiveMembers(since, rowsToReturn);
        }

        return result;
    }

    public Collection getMostActiveThreads(Timestamp since, int rowsToReturn)
        throws DatabaseException {

        Collection result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getMostActiveThreads");
            //buffer.append(since.getTime()).append("_");
            buffer.append(rowsToReturn).append("_");
            String key = buffer.toString();
            result = (Collection) cache.retrieve(key);
            if (result == null) {
                result = DAOFactory.getPostDAO().getMostActiveThreads(since, rowsToReturn);

                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = DAOFactory.getPostDAO().getMostActiveThreads(since, rowsToReturn);
        }

        return result;
    }

    public PostBean getPost(int postID) 
        throws DatabaseException, ObjectNotFoundException {

        PostBean result = null;
        if (cache != null) {
            StringBuffer buffer = new StringBuffer(128);
            buffer.append("getPost_").append(postID);
            String key = buffer.toString();
            result = (PostBean) cache.retrieve(key);
            if (result == null) {
                result = DAOFactory.getPostDAO().getPost(postID);
    
                cache.store(key, result, TIME_OUT);
            }
        } else {
            result = DAOFactory.getPostDAO().getPost(postID);
        }
    
        return result;
    }

}
