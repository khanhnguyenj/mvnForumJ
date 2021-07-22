/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/CategoryBuilder.java,v 1.11 2009/05/04 10:48:38 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.11 $
 * $Date: 2009/05/04 10:48:38 $
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
package com.mvnforum.categorytree;

import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;

import com.mvnforum.db.*;

public abstract class CategoryBuilder extends Observable {

    public static final int DEFAULT_LEVEL          = 0;
    public static final String DEFAULT_IDSPATH     = "";
    
    //Category Root or categories in getTopCategories 
    public static final String TOP_CATEGORY_PREFIX = "__";

    public static final String PREFIX              = "_";
    
    private CategoryBean categoryRoot;

    protected StringBuffer tree;

    protected List forums;
    protected List categories;

    private String id;

    protected int depth = 1;

    private CategoryTreeListener categoryTreeListener;

    protected CategoryBuilder() throws DatabaseException {
        tree = new StringBuffer();
        forums = ForumCache.getInstance().getBeans();
        categories = CategoryCache.getInstance().getBeans();
    }

    public void setRoot(CategoryBean root) {
        categoryRoot = root;
    }
    public CategoryBean getCategoryRoot() {
        return categoryRoot;
    }

    public void setListener(CategoryTreeListener listener) {
        categoryTreeListener = listener;
    }
    public CategoryTreeListener getCategoryTreeListener() {
        return categoryTreeListener;
    }
    
    public Collection getForumsByCategoryId(int categoryId) {
        ArrayList children  = new ArrayList();
        for (Iterator forumIter = forums.iterator(); forumIter.hasNext(); ) {
            ForumBean forum = (ForumBean) forumIter.next();
            if (belongsTo(forum, categoryId)) {
                children.add(forum);
            }
        }
        return children;
    }

    private static boolean belongsTo(ForumBean child, int categoryId) {
        return (child.getCategoryID() == categoryId);
    }

    public void setID(String _id) {
        this.id = _id;
    }
    public String getID() {
        if (id != null) {
            return this.id;
        }
        return "";
    }

    public void drawHeader() {
        CategoryTreeEvent event = new CategoryTreeEvent();
        event.setDepth(depth);
        event.setLevel(DEFAULT_LEVEL);
        event.setIdsPath(DEFAULT_IDSPATH);
        tree.append(getCategoryTreeListener().drawHeader(event));
    }

    public abstract void drawBody() throws DatabaseException;

    public void drawFooter() {
        CategoryTreeEvent event = new CategoryTreeEvent();
        event.setDepth(depth);
        event.setLevel(DEFAULT_LEVEL);
        event.setIdsPath(DEFAULT_IDSPATH);
        tree.append(getCategoryTreeListener().drawFooter(event));
    }

    //return category tree
    public String getTree() {
        getCategoryTreeListener().commitTemplate(tree);
        return tree.toString();
    }

}
