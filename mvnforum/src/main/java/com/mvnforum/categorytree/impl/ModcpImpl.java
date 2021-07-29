/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/ModcpImpl.java,v 1.19 2009/04/14 04:11:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.19 $
 * $Date: 2009/04/14 04:11:26 $
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
package com.mvnforum.categorytree.impl;
import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryTreeEvent;
import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.ForumBean;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class ModcpImpl extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(ModcpImpl.class);

    private GenericRequest request;
    private GenericResponse response;

    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private static URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private static Template template;
    private CategoryBean categoryBean;

    private static int count = 0;

    private static int forumCountInCurrentCategory = 0;
    private Locale locale;
    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(ModcpImpl.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("modcp.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for Moderation", e);
        }
    }

    public ModcpImpl(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);

        super.init(template);
    }

    @Override
    public String drawHeader(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "header");

        String forum_name_desc = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.forum_name_desc");
        subRoot.put("forum_name_desc", forum_name_desc);
        String pending_threads = MVNForumResourceBundle.getString(locale, "mvnforum.common.numberof.pending_threads");
        subRoot.put("pending_threads", pending_threads);
        String threads_with_pending_posts = MVNForumResourceBundle.getString(locale, "mvnforum.common.numberof.threads_with_pending_posts");
        subRoot.put("threads_with_pending_posts", threads_with_pending_posts);
        String pending_posts = MVNForumResourceBundle.getString(locale, "mvnforum.common.numberof.pending_posts");
        subRoot.put("pending_posts", pending_posts);

        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "footer");

        boolean checkNoCategory = (event.getDepth() == 0);
        subRoot.put("checkNoCategory", checkNoCategory);

        if (checkNoCategory) {
            String no_category = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_category");
            subRoot.put("no_category", no_category);
        }

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawCategory(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        forumCountInCurrentCategory = 0;

        boolean check_Count = (count != 0);
        subRoot.put("check_Count", check_Count);

        count++;

        categoryBean = (CategoryBean)event.getSource();

        subRoot.put("categoryId", String.valueOf(categoryBean.getCategoryID()));
        subRoot.put("categoryName", categoryBean.getCategoryName());
        subRoot.put("name", "category");

        if (MyUtil.canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission) == false) {
            return "";
        }

        subRoot.put("MyUtilFilter", MyUtil.filter(categoryBean.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/));

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        ForumBean forum = (ForumBean) event.getSource();

        subRoot.put("name", "forum");

        int forumID = forum.getForumID();

        boolean checkForumToSee = ((forum.getCategoryID() == categoryBean.getCategoryID()) && permission.canModerateThread(forumID) && (forum.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED));
        subRoot.put("checkForumToSee", checkForumToSee);

        if (checkForumToSee) {
            subRoot.put("forumName", forum.getForumName());
            subRoot.put("forumID", String.valueOf(forum.getForumID()));

            subRoot.put("forumCountInCurrentCategory", new Integer(forumCountInCurrentCategory));
            forumCountInCurrentCategory++;

            subRoot.put("urlListThreads", urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID));
            subRoot.put("filterForumDesc", MyUtil.filter(forum.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/));

            String PendingThreadCountClass = forum.getPendingThreadCount()>0? "pendingyes" : "pendingno";
            subRoot.put("PendingThreadCountClass", PendingThreadCountClass);

            subRoot.put("PendingThreadCount", String.valueOf(forum.getPendingThreadCount()));

            boolean checkPendingThreadCount = (forum.getPendingThreadCount()>0);
            subRoot.put("checkPendingThreadCount", checkPendingThreadCount);

            subRoot.put("moderatependingthreads", urlResolver.encodeURL(request, response, "moderatependingthreads?forum=" + forumID));

            String ThreadsWithPendingPostsCountClass = forum.getThreadsWithPendingPostsCount()>0? "pendingyes" : "pendingno";
            subRoot.put("ThreadsWithPendingPostsCountClass", ThreadsWithPendingPostsCountClass);

            subRoot.put("ThreadsWithPendingPostsCount", String.valueOf(forum.getThreadsWithPendingPostsCount()));

            boolean checkThreadsWithPendingPostsCount = (forum.getThreadsWithPendingPostsCount()>0);
            subRoot.put("checkThreadsWithPendingPostsCount", checkThreadsWithPendingPostsCount);

            subRoot.put("listthreadswithpendingposts", urlResolver.encodeURL(request, response, "listthreadswithpendingposts?forum=" + forumID));

            String PendingPostCountClass = forum.getPendingPostCount() > 0 ? "pendingyes" : "pendingno";
            subRoot.put("PendingPostCountClass", PendingPostCountClass);

            subRoot.put("PendingPostCount", String.valueOf(forum.getPendingPostCount()));

            subRoot.put("pending_threads", MVNForumResourceBundle.getString(locale, "mvnforum.user.modcp.pending_threads"));
            subRoot.put("threads_has_pending_posts", MVNForumResourceBundle.getString(locale, "mvnforum.user.modcp.threads_has_pending_posts"));
        }
        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "separator");

        boolean checkNoForum = (forumCountInCurrentCategory == 0);
        subRoot.put("checkNoForum", checkNoForum);
        if (checkNoForum) {
            String no_forum = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_forum");
            subRoot.put("no_forum", no_forum);
        }
        rows.add(subRoot);

        return "";
    }

    @Override
    public void setDepthTree(int depth) {
        //this.depth = depth;
    }
}
