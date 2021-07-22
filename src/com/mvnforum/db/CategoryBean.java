/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/CategoryBean.java,v 1.10 2007/10/09 11:09:18 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.10 $
 * $Date: 2007/10/09 11:09:18 $
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
package com.mvnforum.db;

import java.sql.Timestamp;

import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: CategoryID, ParentCategoryID, CategoryName, CategoryDesc, CategoryCreationDate,
 *                   CategoryModifiedDate, CategoryOrder, CategoryOption, CategoryStatus
 * Excluded columns:
 */
public class CategoryBean {
    private int categoryID;
    private int parentCategoryID;
    private String categoryName;
    private String categoryDesc;
    private Timestamp categoryCreationDate;
    private Timestamp categoryModifiedDate;
    private int categoryOrder;
    private int categoryOption;
    private int categoryStatus;

    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getParentCategoryID() {
        return parentCategoryID;
    }
    public void setParentCategoryID(int parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }
    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = StringUtil.getEmptyStringIfNull(categoryDesc);
    }

    public Timestamp getCategoryCreationDate() {
        return categoryCreationDate;
    }
    public void setCategoryCreationDate(Timestamp categoryCreationDate) {
        this.categoryCreationDate = categoryCreationDate;
    }

    public Timestamp getCategoryModifiedDate() {
        return categoryModifiedDate;
    }
    public void setCategoryModifiedDate(Timestamp categoryModifiedDate) {
        this.categoryModifiedDate = categoryModifiedDate;
    }

    public int getCategoryOrder() {
        return categoryOrder;
    }
    public void setCategoryOrder(int categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public int getCategoryOption() {
        return categoryOption;
    }
    public void setCategoryOption(int categoryOption) {
        this.categoryOption = categoryOption;
    }

    public int getCategoryStatus() {
        return categoryStatus;
    }
    public void setCategoryStatus(int categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

} //end of class CategoryBean
