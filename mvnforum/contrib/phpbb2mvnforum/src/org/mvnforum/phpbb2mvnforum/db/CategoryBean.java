/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/CategoryBean.java,v 1.5 2008/12/04 08:58:45 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2008/12/04 08:58:45 $
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
package org.mvnforum.phpbb2mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

    public String getXMLTag() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<Category");
        xml.append(" categoryID=\"").append(String.valueOf(categoryID)).append("\"");
        xml.append(" parentCategoryID=\"").append(String.valueOf(parentCategoryID)).append("\"");
        xml.append(" categoryName=\"").append(String.valueOf(categoryName)).append("\"");
        xml.append(" categoryDesc=\"").append(String.valueOf(categoryDesc)).append("\"");
        xml.append(" categoryCreationDate=\"").append(String.valueOf(categoryCreationDate)).append("\"");
        xml.append(" categoryModifiedDate=\"").append(String.valueOf(categoryModifiedDate)).append("\"");
        xml.append(" categoryOrder=\"").append(String.valueOf(categoryOrder)).append("\"");
        xml.append(" categoryOption=\"").append(String.valueOf(categoryOption)).append("\"");
        xml.append(" categoryStatus=\"").append(String.valueOf(categoryStatus)).append("\"");
        xml.append(">");
        return xml.toString();
    }
    
    public void getBeanDocument(Document doc, Element element) {
        Element category = doc.createElement("Category");
        element.appendChild(category);
        
        category.appendChild(getNode(doc, "CategoryID", String.valueOf(categoryID)));
        category.appendChild(getNode(doc, "ParentCategoryID", String.valueOf(parentCategoryID)));
        category.appendChild(getNode(doc, "CategoryName", String.valueOf(categoryName)));
        category.appendChild(getNode(doc, "CategoryDesc", String.valueOf(categoryDesc)));
        category.appendChild(getNode(doc, "CategoryModifiedDate", String.valueOf(categoryModifiedDate)));
        category.appendChild(getNode(doc, "CategoryCreationDate", String.valueOf(categoryCreationDate)));
        category.appendChild(getNode(doc, "CategoryOrder", String.valueOf(categoryOrder)));
        category.appendChild(getNode(doc, "CategoryOption", String.valueOf(categoryOption)));
        category.appendChild(getNode(doc, "CategoryStatus", String.valueOf(categoryStatus)));
    }
    
    public static Node getNode (Document doc, String childName, String childValue ) {
        Element child = doc.createElement(childName);
        child.appendChild(doc.createTextNode(childValue));
        return child;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<Category>\n");
        
        xml.append("<CategoryID>" + categoryID + "</CategoryID>\n");
        xml.append("<ParentCategoryID>" + parentCategoryID + "</ParentCategoryID>\n");
        xml.append("<CategoryName>" + categoryName + "</CategoryName>\n");
        xml.append("<CategoryDesc>" + categoryDesc + "</CategoryDesc>\n");
        xml.append("<CategoryCreationDate>" + categoryCreationDate + "</CategoryCreationDate>\n");
        xml.append("<CategoryModifiedDate>" + categoryModifiedDate + "</CategoryModifiedDate>\n");
        xml.append("<CategoryModifiedDate>" + categoryModifiedDate + "</CategoryModifiedDate>\n");
        xml.append("<CategoryOrder>" + categoryOrder + "</CategoryOrder>\n");
        xml.append("<CategoryOption>" + categoryOption + "</CategoryOption>\n");
        xml.append("<CategoryStatus>" + categoryStatus + "</CategoryStatus>\n");
        
        xml.append("</Category>\n");
        return xml.toString();
    }
    
    public static String getXML (Collection beans) {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<CategoryList>");
        
        for (Iterator iter = beans.iterator(); iter.hasNext(); ) {
            CategoryBean bean = (CategoryBean)iter.next();
            xml.append(bean.getXML());
        }
        xml.append("</CategoryList>");
        return xml.toString();
    }

} //end of class CategoryBean

