/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/impl/ViewMemberPermissionsImpl.java,v 1.16 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.16 $
 * $Date: 2008/12/31 03:50:24 $
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
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.categorytree.CategoryTreeEvent;
import com.mvnforum.db.CategoryBean;
import com.mvnforum.db.ForumBean;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

public class ViewMemberPermissionsImpl extends FtlCategoryTreeListener {

    private static final Logger log = LoggerFactory.getLogger(ViewMemberPermissionsImpl.class);

    // template can be shared between users after loaded
    private static Template template;

    private MVNForumPermission permission;
    private Locale locale;
    private int forumCountInCurrentCategory = 0;
    static {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader loader = new ClassTemplateLoader(ViewMemberPermissionsImpl.class, "/");
        conf.setTemplateLoader(loader);
        try {
            template = conf.getTemplate("viewmemberpermissions.ftl");
        } catch (IOException e) {
            log.error("Cannot load template for View Member Permission", e);
        }
    }

    public ViewMemberPermissionsImpl(GenericRequest request) {

        permission = (MVNForumPermission)request.getAttribute("Permissions");
        locale = I18nUtil.getLocaleInRequest(request);
        init(template);

        String yesValue = MVNForumResourceBundle.getString(locale, "mvnforum.common.yes");
        root.put("YesValue", yesValue);

        String noValue = MVNForumResourceBundle.getString(locale, "mvnforum.common.no");
        root.put("NoValue", noValue);

    }

    private void addCommonValue(SimpleHash row) {

        String forumTitle = MVNForumResourceBundle.getString(locale, "mvnforum.common.forum");
        row.put("ForumTitle", forumTitle);

        String threadTitle = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread");
        row.put("ThreadTitle", threadTitle);

        String postTitle = MVNForumResourceBundle.getString(locale, "mvnforum.common.post");
        row.put("PostTitle", postTitle);

        String attachmentTitle = MVNForumResourceBundle.getString(locale, "mvnforum.common.attachment");
        row.put("AttachmentTitle", attachmentTitle);

        String pollTitle = MVNForumResourceBundle.getString(locale, "mvnforum.common.poll");
        row.put("PollTitle", pollTitle);
    }

    @Override
    public String drawHeader(CategoryTreeEvent event) {
        SimpleHash row = new SimpleHash();

        row.put("RowType", "Header");
        addCommonValue(row);
        rows.add(row);

        return "";
    }

    @Override
    public String drawFooter(CategoryTreeEvent event) {
        SimpleHash row = new SimpleHash();

        row.put("RowType", "Footer");
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
    public String drawCategory(CategoryTreeEvent event) {
        forumCountInCurrentCategory = 0;

        SimpleHash row = new SimpleHash();
        CategoryBean category = (CategoryBean) event.getSource();

        row.put("CategoryName", category.getCategoryName());
        row.put("Empty", (event.hasForum() == false));
        String no_forum = MVNForumResourceBundle.getString(locale, "mvnforum.user.listforums.table.no_forum");
        row.put("no_forum", no_forum);
        row.put("RowType", "Category");

        rows.add(row);

        return "";
    }

    @Override
    public String drawForum(CategoryTreeEvent event) {
        SimpleHash row = new SimpleHash();
        ForumBean forum = (ForumBean) event.getSource();
        int forumID = forum.getForumID();

        row.put("RowType", "Forum");
        row.put("ForumID", String.valueOf(forumID));
        row.put("ForumName", forum.getForumName());

        row.put("EditForum", permission.canEditForum(forumID));
        row.put("DeleteForum", permission.canDeleteForum(forumID));
        row.put("AssignForum", permission.canAssignToForum(forumID));

        row.put("AddThread", permission.canAddThread(forumID));
        row.put("ModerateThread", permission.canModerateThread(forumID));

        row.put("ReadPost", permission.canReadPost(forumID));
        row.put("AddPost", permission.canAddPost(forumID));
        row.put("EditOwnPost", permission.canEditOwnPost(forumID));
        row.put("EditAnyPost", permission.canEditPost(forumID));
        row.put("DeletePost", permission.canDeletePost(forumID));

        row.put("AddAttachment", permission.canAddAttachment(forumID));
        row.put("GetAttachment", permission.canGetAttachment(forumID));

        row.put("AddPoll", permission.canAddPoll(forumID));
        row.put("EditOwnPoll", permission.canEditOwnPoll(forumID));
        row.put("EditAnyPoll", permission.canEditPoll(forumID));
        row.put("DeletePoll", permission.canDeletePoll(forumID));

        row.put("forumCountInCurrentCategory", new Integer(forumCountInCurrentCategory));
        forumCountInCurrentCategory++;

        addCommonValue(row);

        rows.add(row);

        return "";
    }

    @Override
    public String drawSeparator(CategoryTreeEvent event) {
        return "";
    }

    @Override
    public void setDepthTree(int depth) {
        // this.depth = depth;
    }
}
