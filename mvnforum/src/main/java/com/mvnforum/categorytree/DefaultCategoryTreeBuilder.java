/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/categorytree/DefaultCategoryTreeBuilder.java,v 1.4 2009/05/04 10:48:38 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
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
 * @author: MVN Developers
 */
package com.mvnforum.categorytree;

import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.DatabaseException;

import com.mvnforum.db.CategoryBean;

public class DefaultCategoryTreeBuilder extends CategoryBuilder {
    
    public DefaultCategoryTreeBuilder() throws DatabaseException {
    }

    public void drawBody() throws IllegalArgumentException, DatabaseException {
        CategoryBean categoryRoot = getCategoryRoot();
        if (categoryRoot != null) {
            setID(TOP_CATEGORY_PREFIX + categoryRoot.getCategoryID());
            drawCategoryAndItsChildren(tree, categoryRoot);
        } else {
            for (Iterator iter = categories.iterator(); iter.hasNext();) {
                CategoryBean category = (CategoryBean) iter.next();
                drawCategoryAndItsChildren(tree, category);
            }
        }
    }

    private void drawCategoryAndItsChildren(StringBuffer html, CategoryBean category) throws DatabaseException {

        String prefix = TOP_CATEGORY_PREFIX + category.getCategoryID();
        Collection forumChildren = getForumsByCategoryId(category.getCategoryID());
        
        CategoryTreeEvent event = new CategoryTreeEvent(category);
        event.setHasSubCategory(false);
        event.setDepth(depth);
        event.setLevel(DEFAULT_LEVEL);
        event.setIdsPath(prefix);
        event.setHasForum(forumChildren.isEmpty() == false);
        
        //draw category
        html.append(getCategoryTreeListener().drawCategory(event));

        //draw forums
        for (Iterator iterator = forumChildren.iterator(); iterator.hasNext(); ) {
            CategoryTreeEvent e = new CategoryTreeEvent(iterator.next());
            e.setDepth(depth);
            e.setLevel(DEFAULT_LEVEL);
            e.setIdsPath(prefix);
            html.append(getCategoryTreeListener().drawForum(e));
        }
    }
}