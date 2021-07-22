/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/CategoryDAO.java,v 1.12 2009/09/24 11:48:58 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.12 $
 * $Date: 2009/09/24 11:48:58 $
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
 */
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.*;

public interface CategoryDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Category";

    public int findByPrimaryKey(int categoryID)
        throws ObjectNotFoundException, DatabaseException;

    public int findByAlternateKey_CategoryName(String categoryName)
        throws ObjectNotFoundException, DatabaseException;

    public void create(int parentCategoryID, String categoryName, String categoryDesc,
                        Timestamp categoryCreationDate, Timestamp categoryModifiedDate, int categoryOrder,
                        int categoryOption, int categoryStatus)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public int createCategory(int parentCategoryID, String categoryName, String categoryDesc,
                              Timestamp categoryCreationDate, Timestamp categoryModifiedDate, int categoryOrder,
                              int categoryOption, int categoryStatus)
            throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException, DuplicateKeyException;
    
    public void delete(int categoryID)
        throws DatabaseException, ObjectNotFoundException;

    public void update(int categoryID, // primary key
                       String categoryName, String categoryDesc, Timestamp categoryModifiedDate,
                       int categoryOrder, int categoryOption, int categoryStatus)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException;

    public CategoryBean getCategory(int categoryID)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getCategories()
        throws DatabaseException;

    public void decreaseCategoryOrder(int categoryID, Timestamp categoryModifiedDate)
        throws DatabaseException, ObjectNotFoundException;

    public void increaseCategoryOrder(int categoryID, Timestamp categoryModifiedDate)
        throws DatabaseException, ObjectNotFoundException;

}
