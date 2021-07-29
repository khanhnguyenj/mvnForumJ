/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/ListAllForumImpl.java,v 1.17 2009/04/14 04:11:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.17 $
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
 * @author: Phuong Pham Dinh Duy
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

public class ListAllForumImpl extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(ListAllForumImpl.class);

    private GenericRequest request;
    private GenericResponse response;

    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private static Template template;

    private Locale locale;

    private CategoryBean category;
    private int forumCountInCurrentCategory = 0;
    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(ListAllForumImpl.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("listforums_index.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for List All Forums", e);
        }
    }

    public ListAllForumImpl(GenericRequest request)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);
        super.init(template);
    }

    public ListAllForumImpl(GenericRequest request,GenericResponse response)
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
        row.put("RowType", "header");
        row.put("urlOfListforums", urlResolver.encodeURL(request, response, "listforums"));

        String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.title");
        row.put("title", title);
        rows.add(row);

        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        row.put("RowType", "footer");

        boolean checkNoCategory = (event.getDepth() == 0);
        row.put("checkNoCategory", checkNoCategory);

        if (checkNoCategory) {
            String no_category = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_category");
            row.put("no_category", no_category);
        }
        rows.add(row);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        ForumBean forum = (ForumBean) event.getSource();

        row.put("CanReadPost", permission.canReadPost(forum.getForumID()));
        row.put("NotDisabled", forum.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED);
        row.put("RowType", "Forum");
        if ((permission.canReadPost(forum.getForumID())) && (forum.getForumStatus() !=  ForumBean.FORUM_STATUS_DISABLED)) {
            String listThreadsLink = urlResolver.encodeURL(request, response, "listthreads?forum=" + forum.getForumID());
            row.put("ListThreadsLink", listThreadsLink);

            row.put("ForumName", forum.getForumName());
            row.put("jsforumprefix", "__" + category.getCategoryID() + "_f" + forum.getForumID());
            row.put("forumCountInCurrentCategory", new Integer(forumCountInCurrentCategory));
            forumCountInCurrentCategory++;
        }

        rows.add(row);

        return "";
     }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();
        CategoryBean category = (CategoryBean) event.getSource();

        row.put("RowType", "Separator");
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
        category = (CategoryBean)event.getSource();
        row.put("RowType", "Category");
        row.put("CanViewForums", MyUtil.canViewAtLeastOneForumInCategory(category.getCategoryID(), permission));

        row.put("ListForumsLink", urlResolver.encodeURL(request, response, "listforums?category=" + category.getCategoryID()));
        row.put("CategoryName", category.getCategoryName());
        row.put("CategoryId", String.valueOf(category.getCategoryID()));
        row.put("ContextPath", request.getContextPath());
        row.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));

        forumCountInCurrentCategory = 0;

        rows.add(row);

        return "";//default
    }

}
