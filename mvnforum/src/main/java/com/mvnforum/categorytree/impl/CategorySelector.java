/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/CategorySelector.java,v 1.31 2009/04/14 04:11:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
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
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class CategorySelector extends FtlCategoryTreeListener  {

    private static final Logger log = LoggerFactory.getLogger(CategorySelector.class);

    private GenericRequest request;
    private GenericResponse response;
    /*
     * page:in case performing only category (not include forum child)
     *      such as addcategory page --> page == null
     */
    private String page = null;
    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private static URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private static Template template;
    private boolean page_Condition;
    private int forumID;
    private boolean addForum;
    private int categoryID;
    private boolean checkAddCategory = false;
    private Locale locale;

    private int forum;
    private int category;

    private String selectName;

    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(CategorySelector.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("categoryselector.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for CategorySelector", e);
        }
    }

    public CategorySelector() {
        //default
    }

    public CategorySelector(GenericRequest request, GenericResponse response, String page)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        this.page = page;
        locale = I18nUtil.getLocaleInRequest(request);

        init(template);
    }

    public CategorySelector(GenericRequest request, GenericResponse response, String page, int forum, int category)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        this.page = page;
        locale = I18nUtil.getLocaleInRequest(request);

        this.forum = forum;
        this.category = category;

        init(template);
    }

    public CategorySelector(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);

        init(template);
    }

    public CategorySelector(GenericRequest request, GenericResponse response, boolean addForum, String selectName)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        this.addForum = addForum;
        locale = I18nUtil.getLocaleInRequest(request);
        this.selectName = selectName;

        init(template);
    }

    public CategorySelector(GenericRequest request, GenericResponse response, String page, int forumID)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        this.page = page;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        this.forumID = forumID;
        locale = I18nUtil.getLocaleInRequest(request);

        init(template);
    }

    public CategorySelector(GenericRequest request, GenericResponse response, ForumBean forumBean)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        this.categoryID = forumBean.getCategoryID();
        this.addForum = true;
        locale = I18nUtil.getLocaleInRequest(request);

        init(template);
    }

    @Override
    public String drawHeader(CategoryTreeEvent event) {
        SimpleHash row = new SimpleHash();

        row.put("name", "header");

        String choose_forum = MVNForumResourceBundle.getString(locale, "mvnforum.common.prompt.choose_forum");
        row.put("choose_forum", choose_forum);
        String search_forum = MVNForumResourceBundle.getString(locale, "mvnforum.user.search.all_forums");
        row.put("search_forum", search_forum);

        boolean checkPageNotNull = (page != null);
        row.put("checkPageNotNull", checkPageNotNull);

        boolean isAddForum = addForum;
        row.put("isAddForum", isAddForum);

        if (checkPageNotNull) {
            boolean isSearchPage = page.equals("search") || page.equals("searchattachments");
            row.put("isSearchPage", isSearchPage);
            //search
            if (isSearchPage == false) {
                //assignforumtogroup and movethread has the same style
                boolean isPageContentAssignForumToGroup = (page.indexOf("assignforumtogroup") != -1);
                row.put("isPageContentAssignForumToGroup", isPageContentAssignForumToGroup);

                if (isPageContentAssignForumToGroup) {
                    boolean isPageContentMoveThread = (page.indexOf("movethread") != -1);
                    row.put("isPageContentMoveThread", isPageContentMoveThread);

                } else {
                    row.put("isPageListUnansweredThreads", (("listunansweredthreads").equals(page) || ("listattachments").equals(page)));
                }
                //listrecentthreads
                //consist of Category (has forum child) and Forum
            }
        } else if (isAddForum) {
            row.put("addwatch", Boolean.valueOf((selectName != null) && (selectName.equals("category"))));
            row.put("choose_category", MVNForumResourceBundle.getString(locale, "mvnforum.common.prompt.choose_category"));
            //addForum
        } else {
            checkAddCategory = true;
            //addcategory
            //only need Category
            //Mvnforum, not Mvnforum Enterprise
        }

        rows.add(row);

        return "";

    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();

        row.put("name", "footer");

        rows.add(row);

        return "";
    }

    @Override
    public String drawCategory(CategoryTreeEvent event) {

        if (!checkAddCategory) {
            SimpleHash subRoot = new SimpleHash();
            CategoryBean bean  = (CategoryBean) event.getSource();
            int cateId = -1;
            subRoot.put("name", "category");

            try {
                cateId = ParamUtil.getParameterInt(request.getServletRequest(), "category");
            } catch (Exception ex) {}

            if (bean == null) {
                return "";
            }

            boolean noForum_Condition = ((page != null) && (MyUtil.canViewAtLeastOneForumInCategory(bean.getCategoryID(), permission) == false));
            subRoot.put("noForum_Condition", noForum_Condition);

            if (noForum_Condition) {
                //In case Category has no Forum
                return "";
            }

            //edit forum, need categoryid contain this forum
            boolean forumId_Condition = false;
            if (bean.getCategoryID() == this.categoryID) {
                forumId_Condition = true;
            }
            subRoot.put("forumId_Condition", forumId_Condition);

            page_Condition = (page == null);
            subRoot.put("page_Condition", page_Condition);

            StringBuffer url_CategoryId = new StringBuffer();
            url_CategoryId.append(bean.getCategoryID());
            subRoot.put("url_CategoryId", url_CategoryId.toString());

            if (page_Condition) {
                if (forumId_Condition == false) {
                    subRoot.put("addForum", addForum);
                    if (addForum) {
                        subRoot.put("categoryId_Condition", (bean.getCategoryID() == cateId));
                    }
                }
            } else {
                //page != null
                boolean search_Page = ("search").equals(page) || ("searchattachments").equals(page);
                subRoot.put("search_Page", search_Page);

                if (search_Page) {
                    StringBuffer forum_CategoryId = new StringBuffer();
                    forum_CategoryId.append("forum-").append(bean.getCategoryID());
                    subRoot.put("forum_CategoryId", forum_CategoryId.toString());
                } else {
                    boolean assignforumtogroup_Page = (page.indexOf("assignforumtogroup") != -1);
                    subRoot.put("assignforumtogroup_Page", assignforumtogroup_Page);

                    if (assignforumtogroup_Page == false) {
                        boolean listunansweredthreads_Page = ("listunansweredthreads").equals(page);
                        boolean listattachments_Page = ("listattachments").equals(page);

                        String destURL = "";
                        subRoot.put("isPageListUnansweredThreads", (listunansweredthreads_Page || listattachments_Page));
                        if (listunansweredthreads_Page || listattachments_Page) {
                            subRoot.put("select_this_category", (bean.getCategoryID() == this.category));
                            destURL = "c" + bean.getCategoryID();
                        } else {
                            destURL = urlResolver.encodeURL(request, response, "listforums");
                        }
                        subRoot.put("destURL", destURL);
                    }
                }
            }

            boolean page_ElseCondition = (page != null);
            subRoot.put("page_ElseCondition", page_ElseCondition);

            String catName = bean.getCategoryName();
            subRoot.put("catName", catName);

            rows.add(subRoot);
        }
        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();

        subRoot.put("name", "forum");

        subRoot.put("hasForums", (checkAddCategory == false));
        if (checkAddCategory == false) {
            ForumBean forum = (ForumBean) event.getSource();

            page_Condition = (page != null);
            subRoot.put("page_Condition", page_Condition);
            if (page_Condition) {
                boolean permiss = permission.canReadPost(forum.getForumID())&&(forum.getForumStatus()!= ForumBean.FORUM_STATUS_DISABLED);
                subRoot.put("permiss", permiss);

                if (permiss) {
                    boolean search_Page = ((("search").equals(page)) || ("searchattachments").equals(page) || (page.indexOf("assignforumtogroup") != -1));
                    subRoot.put("search_Page", search_Page);
                    if (search_Page) {
                        boolean isMovethreadPage = (page.indexOf("movethread") != -1);
                        subRoot.put("isMovethreadPage", isMovethreadPage);

                        // foID:forumID file in search page
                        StringBuffer foID = new StringBuffer();
                        foID.append(forum.getForumID());
                        subRoot.put("foID", foID.toString());

                        if (isMovethreadPage) {
                            boolean checkID = (forum.getForumID() != this.forumID);
                            subRoot.put("checkID", checkID);
                        }
                    } else {

                        boolean listunansweredthreads_Page = ("listunansweredthreads").equals(page);
                        boolean listattachments_Page = ("listattachments").equals(page);

                        String destURL = "";
                        subRoot.put("isPageListUnansweredThreads", (listunansweredthreads_Page || listattachments_Page));
                        if (listunansweredthreads_Page || listattachments_Page) {
                            subRoot.put("select_this_forum", (forum.getForumID() == this.forum));
                            destURL = "f" + forum.getForumID();
                        } else {
                            destURL = urlResolver.encodeURL(request, response, "listthreads?forum=" + forum.getForumID());
                        }
                        subRoot.put("destURL", destURL);

                        boolean forumId_Condition = forum.getForumID() == forumID;
                        subRoot.put("forumId_Condition", forumId_Condition);

                    }

                    String forumName = forum.getForumName();
                    subRoot.put("forumName", forumName);
                }
            }
        }
        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {
        //no need
        return "";
    }

    @Override
    public void setDepthTree(int depth) {
        //noneed
    }

}
