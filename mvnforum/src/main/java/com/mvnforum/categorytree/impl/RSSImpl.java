/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/RSSImpl.java,v 1.22 2009/04/14 04:11:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.22 $
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
import com.mvnforum.common.ThreadIconUtil;
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

public class RSSImpl extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(RSSImpl.class);

    private GenericRequest request;
    private GenericResponse response;
    private String sort;
    private String order;

    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private static URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
    private static Template template;
    private CategoryBean categoryBean;

    private int categoryID;
    private static  int rowIndex;
    private Locale locale;
    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(RSSImpl.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("rss.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for RSS", e);
        }
    }

    public RSSImpl(GenericRequest request)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);
        super.init(template);
    }

    public RSSImpl(GenericRequest request, GenericResponse response, String sort, String order)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        this.sort = sort;
        this.order = order;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);
        super.init(template);
    }


    @Override
    public String drawHeader(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();

        subRoot.put("name", "header");
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
        rowIndex = 0;

        categoryBean = (CategoryBean)event.getSource();

        categoryID = categoryBean.getCategoryID();
        subRoot.put("categoryId", String.valueOf(categoryID));
        subRoot.put("categoryName", categoryBean.getCategoryName());
        subRoot.put("name", "category");

        if (MyUtil.canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission) == false) {
            return "";
        }

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        ForumBean forum = (ForumBean) event.getSource();

        subRoot.put("forumName", forum.getForumName());

        int forumID = forum.getForumID();
        subRoot.put("forumID", String.valueOf(forumID));

        subRoot.put("ContextPath", request.getContextPath());
        subRoot.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));

        if (forum.getCategoryID() != categoryID) {
            return "";
        }

        boolean check_permission = permission.canReadPost(forumID) && (forum.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED);
        subRoot.put("check_permission", check_permission);

        subRoot.put("forumCountInCurrentCategory", new Integer(rowIndex));
        rowIndex++;

        subRoot.put("name", "forum");
        subRoot.put("urlRSS091", urlResolver.encodeURL(request, response, "rss?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL));
        subRoot.put("urlRSS20", urlResolver.encodeURL(request, response, "rss2?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL));
        subRoot.put("urlATOM", urlResolver.encodeURL(request, response, "atom?forum=" + forumID + "&amp;sort=" + sort + "&amp;order=" + order, URLResolverService.ACTION_URL));
        subRoot.put("forum_specific_feed", MVNForumResourceBundle.getString(locale, "mvnforum.common.forum_specific_feed"));

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {
        return "";
    }

    @Override
    public void setDepthTree(int depth) {
        //this.depth = depth;
    }
}
