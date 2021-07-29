/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/ForumManagementListenerImplDefault.java,v 1.3 2010/07/19 07:29:14 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
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

import com.mvnforum.LocaleMessageUtil;
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

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class ForumManagementListenerImplDefault extends FtlCategoryTreeListener {

    private GenericRequest request;
    private GenericResponse response;

    private OnlineUser onlineUser;
    private MVNForumPermission permission;
    private static URLResolverService urlResolver = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();

    private static Template template;

    private Locale locale;

    private ForumIconLegend forumIconLegend = new ForumIconLegend();

    static {
        // we want to load template only once but process more times
        try {
            Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
            TemplateLoader loader = new ClassTemplateLoader(ForumManagementListenerImplDefault.class, "/");
            conf.setTemplateLoader(loader);
            template = conf.getTemplate("forummanagement.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ForumManagementListenerImplDefault(GenericRequest request, GenericResponse response)
        throws AuthenticationException, DatabaseException {

        this.request = request;
        this.response = response;
        onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        permission = onlineUser.getPermission();
        locale = I18nUtil.getLocaleInRequest(request);
        init(template);
    }

    @Override
    public String drawHeader(CategoryTreeEvent event) {
        SimpleHash subRoot = new SimpleHash();

        subRoot.put("RowType", "header");

        String name_desc = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.name_desc");
        subRoot.put("name_desc", name_desc);

        String order = MVNForumResourceBundle.getString(locale, "mvnforum.common.order");
        subRoot.put("order", order);

        String forumOwnerName = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.forum_owner_name");
        subRoot.put("forumOwnerName", forumOwnerName);

        String creation_date = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.create_date");
        subRoot.put("creation_date", creation_date);

        String type = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.type");
        subRoot.put("type", type);

        String mode_moderation = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum.moderation_mode");
        subRoot.put("mode_moderation", mode_moderation);

        String add = MVNForumResourceBundle.getString(locale, "mvnforum.common.action.add");
        subRoot.put("add", add);

        String group_permission = MVNForumResourceBundle.getString(locale, "mvnforum.common.permission.group_permission");
        subRoot.put("group_permission", group_permission);

        String member_permission = MVNForumResourceBundle.getString(locale, "mvnforum.common.permission.member_permission");
        subRoot.put("member_permission", member_permission);

        String edit = MVNForumResourceBundle.getString(locale, "mvnforum.common.action.edit");
        subRoot.put("edit", edit);

        String delete = MVNForumResourceBundle.getString(locale, "mvnforum.common.action.delete");
        subRoot.put("delete", delete);

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {

        SimpleHash row = new SimpleHash();

        boolean checkNoCategory = (event.getDepth() == 0);
        row.put("checkNoCategory", checkNoCategory);
        if (checkNoCategory) {
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
        row.put("RowType", "footer");
        rows.add(row);

        return "";
    }

    @Override
    public String drawCategory(CategoryTreeEvent event) {
        SimpleHash subRoot = new SimpleHash();
        CategoryBean category = (CategoryBean)event.getSource();

        boolean isEmptyCategory = (event.hasForum() == false);
        subRoot.put("isEmptyCategory", isEmptyCategory);
        if (isEmptyCategory) {
            String no_forum_in_category = MVNForumResourceBundle.getString(locale, "mvnforum.admin.forummanagement.no_forum");
            subRoot.put("no_forum_in_category", no_forum_in_category);
        }

        boolean canEditCategory = permission.canEditCategory();
        subRoot.put("CanEditCat", canEditCategory);
        if (canEditCategory) {
            String catUpLink = urlResolver.encodeURL(request, response, "updatecategoryorder?category=" + category.getCategoryID() + "&amp;action=up&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL);
            subRoot.put("CatUpLink", catUpLink);
            subRoot.put("moveUp", MVNForumResourceBundle.getString(locale, "mvnforum.common.order.move_up"));

            String catDownLink = urlResolver.encodeURL(request, response, "updatecategoryorder?category=" + category.getCategoryID() + "&amp;action=down&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL);
            subRoot.put("CatDownLink", catDownLink);
            subRoot.put("moveDown", MVNForumResourceBundle.getString(locale, "mvnforum.common.order.move_down"));

            String editCatLink = urlResolver.encodeURL(request, response, "editcategory?category=" + category.getCategoryID());
            subRoot.put("EditCatLink", editCatLink);
        }

        boolean canDeleteCategory = !(event.hasForum() || event.hasSubCategory()) && permission.canDeleteCategory();
        subRoot.put("CanDeleteCat", canDeleteCategory);
        if (canDeleteCategory) {
            subRoot.put("deleteCathref", urlResolver.encodeURL(request, response, "deletecategory?category=" + category.getCategoryID()));
            subRoot.put("delete", MVNForumResourceBundle.getString(locale, "mvnforum.common.action.delete"));
        }

        boolean canAddForum = permission.canAddForum();
        subRoot.put("CanAddForum", canAddForum);
        if (canAddForum) {
            String add_forum = MVNForumResourceBundle.getString(locale, "mvnforum.admin.addforum.title");
            subRoot.put("add_forum", add_forum);

            String addForumLink = urlResolver.encodeURL(request, response, "addforum?category=" + category.getCategoryID());
            subRoot.put("AddForumLink", addForumLink);
        }

        subRoot.put("CatName", category.getCategoryName());
        subRoot.put("ContextPath", request.getContextPath());
        subRoot.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));
        subRoot.put("CatOrder", new Integer(category.getCategoryOrder()));

        String filteredCategoryDesc = MyUtil.filter(category.getCategoryDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
        subRoot.put("FilteredCatDesc", filteredCategoryDesc);

        String gmtCatCreationDate = onlineUser.getGMTTimestampFormat(category.getCategoryCreationDate());
        subRoot.put("GMTCatCreationDate", gmtCatCreationDate);

        subRoot.put("RowType", "Category");

        rows.add(subRoot);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {

        SimpleHash subRoot = new SimpleHash();
        subRoot.put("RowType", "Forum");

        ForumBean forum = (ForumBean) event.getSource();

        boolean canReadPost = permission.canReadPost(forum.getForumID());
        subRoot.put("CanReadPost", canReadPost);
        if (canReadPost) {
            boolean canEditForum = permission.canEditForum(forum.getForumID());
            subRoot.put("CanEditForum", canEditForum);
            if (canEditForum) {
                String forumUpLink = urlResolver.encodeURL(request, response, "updateforumorder?forum=" + forum.getForumID() + "&amp;action=up&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL);
                subRoot.put("ForumUpLink", forumUpLink);
                subRoot.put("moveUp", MVNForumResourceBundle.getString(locale, "mvnforum.common.order.move_up"));

                String forumDownLink = urlResolver.encodeURL(request, response, "updateforumorder?forum=" + forum.getForumID() + "&amp;action=down&amp;mvncoreSecurityToken=" + SecurityUtil.getSessionToken(request), URLResolverService.ACTION_URL);
                subRoot.put("ForumDownLink", forumDownLink);
                subRoot.put("moveDown", MVNForumResourceBundle.getString(locale, "mvnforum.common.order.move_down"));

                String editForumLink = urlResolver.encodeURL(request, response, "editforum?forum=" + forum.getForumID());
                subRoot.put("EditForumLink", editForumLink);
            }

            boolean canDeleteForum = permission.canDeleteForum(forum.getForumID());
            subRoot.put("CanDeleteForum", canDeleteForum);
            if (canDeleteForum) {
                String deleteForumLink = urlResolver.encodeURL(request, response, "deleteforum?forum=" + forum.getForumID());
                subRoot.put("DeleteForumLink", deleteForumLink);

                String delete = MVNForumResourceBundle.getString(locale, "mvnforum.common.action.delete");
                subRoot.put("delete", delete);
            }

            boolean canAssignForum = permission.canAssignToForum(forum.getForumID());
            subRoot.put("CanAssignForum", canAssignForum);
            if (canAssignForum) {
                String groupAssignLink = urlResolver.encodeURL(request, response, "assigngrouptoforum?forum=" + forum.getForumID());
                subRoot.put("GroupAssignLink", groupAssignLink);

                String memberAssignLink = urlResolver.encodeURL(request, response, "assignmembertoforum?forum=" + forum.getForumID());
                subRoot.put("MemberAssignLink", memberAssignLink);
            }

            subRoot.put("ContextPath", request.getContextPath());
            subRoot.put("ImagePath", request.getContextPath() + ThreadIconUtil.getImagePath(onlineUser));
            subRoot.put("Disabled", (forum.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED));
            subRoot.put("ForumName", forum.getForumName());

            String forumIcon = MyUtil.getForumIconName(onlineUser.getLastLogonTimestamp().getTime(), forum.getForumLastPostDate().getTime(), forum.getForumStatus(), forum.getForumThreadCount());
            subRoot.put("forumIcon", forumIcon);

            forumIconLegend.updateIconLegend(forumIcon);

            String filteredForumDesc = MyUtil.filter(forum.getForumDesc(), false/*html*/, true/*emotion*/, true/*mvnCode*/, true/*newLine*/, true/*URL*/);
            subRoot.put("FilteredForumDesc", filteredForumDesc);
            subRoot.put("ForumOrder", new Integer(forum.getForumOrder()));

            String gmtForumCreationDate = onlineUser.getGMTTimestampFormat(forum.getForumCreationDate());
            subRoot.put("GMTForumCreationDate", gmtForumCreationDate);
            subRoot.put("ForumOwnerName", StringUtil.getEmptyStringIfNull(forum.getForumOwnerName()));
            subRoot.put("ForumTypeName", LocaleMessageUtil.getForumTypeDescFromInt(onlineUser.getLocale(), forum.getForumType()));
            subRoot.put("ForumModeName", LocaleMessageUtil.getForumModeDescFromInt(onlineUser.getLocale(), forum.getForumModerationMode()));
        }
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
