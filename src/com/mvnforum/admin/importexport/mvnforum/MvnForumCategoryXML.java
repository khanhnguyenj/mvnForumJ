/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumCategoryXML.java,v 1.11 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2009/01/12 15:02:34 $
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
 * @author: Igor Manic   
 */
package com.mvnforum.admin.importexport.mvnforum;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.admin.CategoryXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.11 $, $Date: 2009/01/12 15:02:34 $
 * <br/>
 * <code>MvnForumCategoryXML</code> class encapsulates processing of
 * categories' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>CategoryXML</code> and other neccessary classes in order to perform
 * the actual creation of a category, as well as other related items (like
 * category watches).
 */
public class MvnForumCategoryXML {

    private CategoryXML categoryXML = null;
    private boolean categoryCreated = false;
    private MvnForumCategoryXML parentCategory = null;

    String categoryName         = null;
    String categoryDesc         = null;
    String categoryCreationDate = null;
    String categoryModifiedDate = null;
    String categoryOrder        = null;
    String categoryOption       = null;
    String categoryStatus       = null;

    public MvnForumCategoryXML() {
        categoryXML = new CategoryXML();
        categoryCreated = false;
        parentCategory = null;
    }

    public int getCategoryID() {
        return categoryXML.getCategoryID();
    }

    public void setCategoryID(String id) {
        categoryXML.setCategoryID(id);
    }

    /**
     * This method simply calls <code>setCategoryID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setCategoryID()</code> as a setter
     * method for <code>categoryID</code> property).
     */
    public void setCategoryId(String id) {
        setCategoryID(id);
    }

    public int getParentCategoryID() {
        return categoryXML.getParentCategoryID(); //==parentCategory.getCategoryID();
    }

    public void setParentCategoryIfHave(Object o)
        throws ForeignKeyNotFoundException {
        
        if (o instanceof MvnForumCategoryXML) {
            parentCategory = (MvnForumCategoryXML)o;
            /* warning: parent category might not be added to database yet, so
             * we don't have parentCategoryID now, and can't do this here:
             * categoryXML.setParentCategoryID(parentCategory.getCategoryID());
             */
        } else {
            //Ignore. Don't have parent category.
            //throw new ForeignKeyNotFoundException("Can't find parent category.");
        }
    }

    public void setCategoryName(String value) {
        categoryName = value;
    }

    public void setCategoryDesc(String value) {
        categoryDesc = value;
    }

    public void setCategoryCreationDate(String value) {
        categoryCreationDate = value;
    }

    public void setCategoryModifiedDate(String value) {
        categoryModifiedDate = value;
    }

    public void setCategoryOrder(String value) {
        categoryOrder = value;
    }

    public void setCategoryOption(String value) {
        categoryOption = value;
    }

    public void setCategoryStatus(String value) {
        categoryStatus = value;
    }

    public void addCategory() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, 
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this category has
         * sub-elements that need it already be defined, so they first call
         * this method to create category before creating data that refer him.
         */
        if (categoryCreated) {
            return;
        }
        /* Second, create parent category if it's not yet created. */
        if (parentCategory != null) {
            parentCategory.addCategory();
            categoryXML.setParentCategoryID(parentCategory.getCategoryID());
        }

        ImportMvnForum.addMessage("Adding category \""+categoryName+"\".");
        categoryXML.addCategory(categoryName, categoryDesc,
                                categoryCreationDate, categoryModifiedDate,
                                categoryOrder, categoryOption, categoryStatus);
        categoryCreated = true;

        if (parentCategory != null) {
            parentCategory.updateAddedCategory(this);
        }
    }

    public void addCategoryWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException, BadInputException {
        
        if ( (!categoryCreated) || (categoryXML.getCategoryID()<0) ) {
            addCategory();
        }
        ImportMvnForum.addMessage("Adding category watch for member \"" + memberName + "\".");
        categoryXML.addCategoryWatch(memberName,
                    watchType, watchOption, watchStatus,
                    watchCreationDate, watchLastSentDate, watchEndDate);
    }

    public void updateAddedCategory(MvnForumCategoryXML subCategory) {
        //do nothing
        if (parentCategory != null) {
            parentCategory.updateAddedCategory(subCategory);
        }
    }

    public void updateAddedForum(MvnForumForumXML subForum) {
        //do nothing
        if (parentCategory != null) {
            parentCategory.updateAddedForum(subForum);
        }
    }

    public void updateAddedThread(MvnForumThreadXML subThread) {
        //do nothing
        if (parentCategory != null) {
            parentCategory.updateAddedThread(subThread);
        }
    }

    public void updateAddedPost(MvnForumPostXML subPost) {
        //do nothing
        if (parentCategory != null) {
            parentCategory.updateAddedPost(subPost);
        }
    }

    public void updateAddedAttachment(MvnForumAttachmentXML subAttachment) {
        //do nothing
        if (parentCategory != null) {
            parentCategory.updateAddedAttachment(subAttachment);
        }
    }

}
