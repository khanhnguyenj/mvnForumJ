/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/ListForums.java,v 1.53 2010/07/19 07:29:14 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.53 $
 * $Date: 2010/07/19 07:29:14 $
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
 * @author: Phuong Pham Dinh Duy
 */
package com.mvnforum.categorytree.impl;

import java.io.IOException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryTreeEvent;
import com.mvnforum.common.ForumIconLegend;
import com.mvnforum.common.ThreadIconUtil;
import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.PostBean;
import com.mvnforum.db.PostCache;
import com.mvnforum.db.WatchBean;
import com.mvnforum.user.WatchUtil;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class ListForums extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(ListForums.class);

    private GenericRequest request;
    private GenericResponse response;

    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private static Template template;

    private int count = 0;

    private ForumIconLegend forumIconLegend = new ForumIconLegend();
    private int forumCountInCurrentCategory = 0;
    private Locale locale;
    private CategoryBean category;

    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(ListForums.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("listforums.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for List Forums", e);
        }
    }

    public ListForums(GenericRequest request)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);
        super.init(template);
    }

    public ListForums(GenericRequest request,GenericResponse response)
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

        SimpleHash row = new SimpleHash();

        row.put("name", "header");

        String forum_name_desc = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.forum_name_desc");
        row.put("forum_name_desc", forum_name_desc);
        String thread_count = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread_count");
        row.put("thread_count", thread_count);
        String post_count = MVNForumResourceBundle.getString(locale, "mvnforum.common.post_count");
        row.put("post_count", post_count);
        String last_post = MVNForumResourceBundle.getString(locale, "mvnforum.common.last_post");
        row.put("last_post", last_post);

        rows.add(row);

        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        row.put("name", "footer");
        boolean checkNoCategory = (count == 0);
        row.put("checkNoCategory", checkNoCategory);
        if (checkNoCategory) {
            //there is no any category
            String no_category = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_category");
            row.put("no_category", no_category);
        } else {

            row.put("showIconLegend", forumIconLegend.isHasIconLegend());

            if (forumIconLegend.isHasIconLegend()) {
                row.put("ContextPath", request.getContextPath());
                row.put("hasReadActiveCurrentForum", forumIconLegend.isHasReadActiveForum());
                row.put("hasUnreadActiveCurrentForum", forumIconLegend.isHasUnreadActiveForum());
                row.put("hasReadClosedCurrentForum", forumIconLegend.isHasReadClosedForum());
                row.put("hasUnreadClosedCurrentForum", forumIconLegend.isHasUnreadClosedForum());
                row.put("hasReadLockedCurrentForum", forumIconLegend.isHasReadLockedForum());
                row.put("hasUnreadLockedCurrentForum", forumIconLegend.isHasUnreadLockedForum());
                row.put("hasReadDisabledCurrentForum", forumIconLegend.isHasReadDisabledForum());
                row.put("hasUnreadDisabledCurrentForum", forumIconLegend.isHasUnreadDisabledForum());

                if (forumIconLegend.isHasReadActiveForum()) {
                    String no_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.read_active");
                    row.put("no_new", no_new);
                }
                if (forumIconLegend.isHasUnreadActiveForum()) {
                    String has_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.unread_active");
                    row.put("has_new", has_new);
                }
                if (forumIconLegend.isHasReadClosedForum()) {
                    String no_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.read_closed");
                    row.put("closed_no_new", no_new);
                }
                if (forumIconLegend.isHasUnreadClosedForum()) {
                    String has_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.unread_closed");
                    row.put("closed_has_new", has_new);
                }
                if (forumIconLegend.isHasReadLockedForum()) {
                    String no_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.read_locked");
                    row.put("locked_no_new", no_new);
                }
                if (forumIconLegend.isHasUnreadLockedForum()) {
                    String has_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.unread_locked");
                    row.put("locked_has_new", has_new);
                }
                if (forumIconLegend.isHasReadDisabledForum()) {
                    String no_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.read_disabled");
                    row.put("disabled_no_new", no_new);
                }
                if (forumIconLegend.isHasUnreadDisabledForum()) {
                    String has_new = MVNForumResourceBundle.getString(locale, "mvnforum.common.legend.forum.unread_disabled");
                    row.put("disabled_has_new", has_new);
                }
            }
        }
        rows.add(row);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) throws IllegalArgumentException, DatabaseException {

        ForumBean forum = (ForumBean) event.getSource();
        SimpleHash row = new SimpleHash();

        boolean checkForumToSee = ((forum.getCategoryID() == category.getCategoryID()) && permission.canReadPost(forum.getForumID()) && (forum.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED));
        row.put("checkForumToSee", checkForumToSee);
        row.put("name", "forum");

        if (checkForumToSee) {
            //this is not a disable forum
            String forumIcon = MyUtil.getForumIconName(onlineUser.getLastLogonTimestamp().getTime(), forum.getForumLastPostDate().getTime(), forum.getForumStatus(), forum.getForumThreadCount());
            row.put("forumIcon", forumIcon);

            forumIconLegend.updateIconLegend(forumIcon);

            row.put("forumCountInCurrentCategory", new Integer(forumCountInCurrentCategory));
            forumCountInCurrentCategory++;

            row.put("ContextPath", request.getContextPath());
            row.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));

            StringBuffer filter = new StringBuffer();
            filter.append(MyUtil.filter(forum.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/));
            row.put("filter", filter.toString());

            row.put("ForumThreadCount", String.valueOf(forum.getForumThreadCount()));
            row.put("ForumPostCount", String.valueOf(forum.getForumPostCount()));

            String listThreadsLink = urlResolver.encodeURL(request, response, "listthreads?forum=" + forum.getForumID());
            row.put("ForumName", forum.getForumName());
            row.put("ListThreadsLink", listThreadsLink);
            row.put("jsforumprefix", "__" + category.getCategoryID() + "_f" + forum.getForumID());

            boolean checkCondition = (forum.getLastPostMemberName().length() == 0) || (forum.getForumThreadCount() == 0);
            row.put("checkCondition", checkCondition);

            if (checkCondition) {
                row.put("no_post", MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_post"));
            } else {
                row.put("GMTTimestampFormat", onlineUser.getGMTTimestampFormat(forum.getForumLastPostDate()));

                PostBean lastPost = PostCache.getInstance().getLastEnablePost_inForum(forum.getForumID());

                String lastPostTopicName = lastPost.getPostTopic();
                row.put("LastPostTopicName", lastPostTopicName);

                String lastPostTopicName_shorter = StringUtil.getShorterString(lastPostTopicName, MVNForumGlobal.TOPIC_NAME_SHORTER_CHARACTERS);
                row.put("LastPostTopicName_shorter", lastPostTopicName_shorter);

                int threadID = lastPost.getThreadID();
                String viewLastTopicLink = urlResolver.encodeURL(request, response, "viewthread?thread=" + threadID + "&amp;lastpage=yes#lastpost");
                row.put("ViewLastTopicLink", viewLastTopicLink);

                String viewmemberLink = urlResolver.encodeURL(request, response, "viewmember?member=" + Encoder.encodeURL(forum.getLastPostMemberName()));
                row.put("viewmemberLink", viewmemberLink);

                row.put("by", MVNForumResourceBundle.getString(locale, "mvnforum.common.by"));
                row.put("LastPostMemberName", forum.getLastPostMemberName());
            }
        }

        rows.add(row);

        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        CategoryBean category = (CategoryBean) event.getSource();

        row.put("name", "Separator");
        row.put("CategoryId", String.valueOf(category.getCategoryID()));

        rows.add(row);

        return "";
    }

    @Override
    public void setDepthTree(int depth) {
        // default
    }

    @Override
    public String drawCategory(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        forumCountInCurrentCategory = 0;
        count++;

        category = (CategoryBean)event.getSource();
        row.put("name", "category");

        row.put("CanViewForums", MyUtil.canViewAtLeastOneForumInCategory(category.getCategoryID(), permission));
        row.put("CategoryName", category.getCategoryName());
        row.put("CategoryId", String.valueOf(category.getCategoryID()));
        row.put("ContextPath", request.getContextPath());
        row.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));
        row.put("enableEasyWatching", Boolean.valueOf(MVNForumConfig.getEnableEasyWatching()));
        if (MVNForumConfig.getEnableEasyWatching()) {
            // check whether this forum is being watched
            Boolean isWatched = Boolean.FALSE;
            if (onlineUser.isMember()) {
                try {
                    isWatched = Boolean.valueOf(WatchUtil.isCategoryWatched(onlineUser.getMemberID(), category));
                } catch (Exception ex) {
                    //do nothing
                }
            }
            row.put("isWatched", isWatched);
            if (isWatched.booleanValue() == false) {
                String addWatchCategoryLink = urlResolver.encodeURL(request, response, "addwatchprocess?WatchType=" + WatchBean.WATCH_TYPE_DEFAULT + "&amp;WatchSelector=" + WatchBean.SELECT_CATEGORY_WATCH + "&amp;category=" + category.getCategoryID() + "&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL);
                row.put("addWatchCategoryLink", addWatchCategoryLink);

                String watchThisCategory = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.watch_this_category");
                row.put("WatchThisCategory", watchThisCategory);
            } else {
                String watchingThisCategory = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.you_are_watching_this_category");
                row.put("WatchingThisCategory", watchingThisCategory);
            }

        }

        StringBuffer filter = new StringBuffer();
        filter.append(MyUtil.filter(category.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/));
        row.put("filter", filter.toString());

        rows.add(row);

        return "";//default
    }

}
