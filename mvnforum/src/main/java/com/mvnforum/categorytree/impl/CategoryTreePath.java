/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/CategoryTreePath.java,v 1.19 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.19 $
 * $Date: 2009/01/02 18:31:46 $
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryTreeEvent;
import com.mvnforum.common.ThreadIconUtil;
import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.CategoryCache;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.ForumCache;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class CategoryTreePath extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(CategoryTreePath.class);

    public static final int FORUMID_IN_LIST_CATEGORY_CASE = 0;

    private static Template template;

    private OnlineUser onlineUser;
    private URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
    private GenericRequest request;
    private GenericResponse response;
    private int forumID;
    private int categoryID;
    private List path;
    private int depthOfPath;
    private Locale locale;
    private boolean showAllForumsURL = false;

    //addpost
    private String mode;
    private String action;
    private String display;

    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(CategoryTreePath.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("categorytreepath.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for CategorySelector", e);
        }
    }

    public CategoryTreePath(GenericRequest request, GenericResponse response, int forumID, int categoryID, String display)
        throws DatabaseException, ObjectNotFoundException, AuthenticationException {
        this.request = request;
        this.response = response;
        this.forumID = forumID;
        this.categoryID = categoryID;
        this.display = display;
        showAllForumsURL = ((display != null) && (display.equals("") == false));
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        locale = I18nUtil.getLocaleInRequest(request);

        path = new ArrayList();
        findPath();

        init(template);
    }

    public CategoryTreePath(GenericRequest request, GenericResponse response, int forumID, String mode, String action, String display)
        throws DatabaseException, ObjectNotFoundException, AuthenticationException {
        this.request = request;
        this.response = response;
        this.forumID = forumID;
        this.mode = mode;
        this.action = action;
        this.display = display;
        showAllForumsURL = true;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        locale = I18nUtil.getLocaleInRequest(request);

        path = new ArrayList();
        findPath();

        init(template);
    }

    public void setDepthOfPath(int depthOfPath) {
        this.depthOfPath = depthOfPath;
    }

    public int getDepthOfPath() {
        return this.depthOfPath;
    }

    private void findPath() throws DatabaseException, ObjectNotFoundException {

        int categoryIDTMP = 0;

        if (forumID != FORUMID_IN_LIST_CATEGORY_CASE) {
            /*
             * In this case [e.g: listthreads], we will find the path from forumID
             */
            ForumBean forumBean = ForumCache.getInstance().getBean(forumID);

            categoryIDTMP = forumBean.getCategoryID();
        } else {
            /*
             * In this case [e.g: listforums], we will find the path from categoryID
             */
            categoryIDTMP = categoryID;
        }
        if (categoryIDTMP != 0) {
            path.add(CategoryCache.getInstance().getBean(categoryIDTMP));
        }

        setDepthOfPath(path.size());
    }

    private boolean isInPath(int catID) {
        //check if the category with categoryID is in the path
        if (path != null) {
            Iterator iter = path.iterator();
            while(iter.hasNext()) {
                CategoryBean bean = (CategoryBean) iter.next();
                if (catID == bean.getCategoryID()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String drawHeader(CategoryTreeEvent event) {
        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "header");

        String index_desc = MVNForumResourceBundle.getString(locale, "mvnforum.common.nav.index");
        subRoot.put("index_desc", index_desc);

        String indexURL = urlResolver.encodeURL(request, response, "index");
        subRoot.put("indexURL", indexURL);
        subRoot.put("ContextPath", request.getContextPath());

        subRoot.put("enablePortalLikeIndexPage", Boolean.valueOf(MVNForumConfig.getEnablePortalLikeIndexPage()));
        if (MVNForumConfig.getEnablePortalLikeIndexPage()) {
            String listForums = MVNForumResourceBundle.getString(onlineUser.getLocale(), "mvnforum.user.listforums.title");
            subRoot.put("listForums", listForums);

            String listForumsLink = urlResolver.encodeURL(request, response, "listforums", URLResolverService.RENDER_URL);
            subRoot.put("listForumsLink", listForumsLink);

        }
        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawCategory(CategoryTreeEvent event) {
        CategoryBean categoryBean = (CategoryBean) event.getSource();

        if (isInPath(categoryBean.getCategoryID()) == false) {
            return "";
        }
        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "category");

        String categoryURL = urlResolver.encodeURL(request, response, "listforums?category=" + categoryBean.getCategoryID() + "&amp;other_category_invisible=true");
        subRoot.put("CategoryURL", categoryURL);
        subRoot.put("CategoryName", categoryBean.getCategoryName());

        //int spacer = event.getLevel() + (MVNForumConfig.getEnablePortalLikeIndexPage() ? 2 : 1);
        int spacer = event.getLevel() + 1;
        subRoot.put("Spacer", new Integer(spacer));

        subRoot.put("ContextPath", request.getContextPath());
        subRoot.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));

        String categoryPrefix = MVNForumResourceBundle.getString(locale, "mvnforum.common.category");
        subRoot.put("CategoryPrefix", categoryPrefix);

        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {
        ForumBean forumBean = (ForumBean) event.getSource();

        /*
         * In listforums, we don't need to draw forums
         */
        if ((forumBean.getForumID() != forumID) || (forumID == FORUMID_IN_LIST_CATEGORY_CASE)) {
            return "";
        }

        SimpleHash subRoot = new SimpleHash();
        subRoot.put("name", "forum");

        subRoot.put("ForumName", forumBean.getForumName());
        subRoot.put("Spacer", new Integer(getDepthOfPath()));
        subRoot.put("ContextPath", request.getContextPath());
        subRoot.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));
        subRoot.put("ShowAllForumsURL", Boolean.valueOf(showAllForumsURL));

        if (showAllForumsURL) {
            String forumURL = urlResolver.encodeURL(request, response, "listthreads?forum=" + forumID);
            subRoot.put("ForumURL", forumURL);
        }

        String forumPrefix = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum");
        subRoot.put("ForumPrefix", forumPrefix);

        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {
        SimpleHash subRoot = new SimpleHash();
        subRoot.put("ShowAllForumsURL", Boolean.valueOf(showAllForumsURL));
        subRoot.put("name", "footer");

        if (showAllForumsURL == false) {
            return "";
        }

        int spacer;
        if ((forumID == FORUMID_IN_LIST_CATEGORY_CASE) || (path.size() == 0)) {
            /*
             * - No listthreads
             * - /mvnforum_enterprise/mvnforum/listforums
             */
            spacer = getDepthOfPath();
        } else {
            spacer = getDepthOfPath() + 1;
        }
        subRoot.put("Spacer", new Integer(spacer));
        subRoot.put("ContextPath", request.getContextPath());

        boolean isAddPostType = (mode != null) && (action != null) && (display != null);
        subRoot.put("isAddPostType", Boolean.valueOf(isAddPostType));

        boolean isViewThreadType = (mode == null) && (action == null) && (display != null);
        subRoot.put("isViewThreadType", Boolean.valueOf(isViewThreadType));

        String content = "";
        if (isAddPostType) {
            //addpost
            content = mode + " : " + display;
            if (action.equals("addnew")) {
                if (display.length() > 0) {
                    content = mode + ": " + display;
                } else {
                    content = mode;
                }
            } else if (action.equals("update")) {
                content = mode + ": " + display;
            }
        } else if (isViewThreadType) {
            //viewthread or addattachment or deleteaddtachment
            content = display;
            //subRoot.put("ThreadTopic", display);
        }
        subRoot.put("Content", content);

        rows.add(subRoot);
        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {
        return "";
    }

    @Override
    public void setDepthTree(int depth) {

    }

}
