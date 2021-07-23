/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/CategoryDAOImplXML.java,v 1.4 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.4 $
 * $Date: 2007/01/15 10:27:12 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db.jdbc;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import org.mvnforum.phpbb2mvnforum.db.CategoryBean;
import org.mvnforum.phpbb2mvnforum.db.CategoryDAO;
import org.mvnforum.util.DBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

public class CategoryDAOImplXML implements CategoryDAO {

    public void findByPrimaryKey(int categoryID)
        throws ObjectNotFoundException, DatabaseException {

    }

    public void findByAlternateKey_CategoryName(String categoryName)
        throws ObjectNotFoundException, DatabaseException {

    }

    public void create(int categoryID, int parentCategoryID, String categoryName, String categoryDesc,
            Timestamp categoryCreationDate, Timestamp categoryModifiedDate, int categoryOrder, int categoryOption,
            int categoryStatus)
        throws CreateException, DatabaseException, DuplicateKeyException {
        
        CategoryBean bean = new CategoryBean();
        bean.setCategoryID(categoryID);
        bean.setParentCategoryID(parentCategoryID);
        bean.setCategoryName(categoryName);
        bean.setCategoryDesc(categoryDesc);
        bean.setCategoryCreationDate(categoryCreationDate);
        bean.setCategoryModifiedDate(categoryModifiedDate);
        bean.setCategoryOrder(categoryOrder);
        bean.setCategoryOption(categoryOption);
        bean.setCategoryStatus(categoryStatus);
    }

    public void createMultiple(Collection beans) {
        Document doc = DBUtils.getDomDocument();
        Element element = doc.createElement("CategoryList");
        doc.getFirstChild().appendChild(element);
        for (Iterator iter = beans.iterator(); iter.hasNext(); ) {
            CategoryBean bean = (CategoryBean)iter.next();
            bean.getBeanDocument(doc, element);
        }
        //DBUtils.writeXmlFile(doc, Phpbb2MvnforumConfig.EXPORT_XML);
    }
    
}
