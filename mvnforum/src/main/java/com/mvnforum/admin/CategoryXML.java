/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/CategoryXML.java,v 1.16 2009/01/06 18:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.16 $
 * $Date: 2009/01/06 18:31:30 $
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
package com.mvnforum.admin;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.db.*;

/**
 * @author Igor Manic
 * @version $Revision: 1.16 $, $Date: 2009/01/06 18:31:30 $
 * <br/>
 * <code>CategoryXML</code> todo Igor: enter description
 *
 */
public class CategoryXML {

    private int categoryID;
    /** Returns <code>CategoryID</code> of this category or
      * <code>-1</code> if category is not created yet. */
    public int getCategoryID() { return categoryID; }

    private int parentCategoryID;

    /** Returns <code>ThreadID</code> of this category's parent category or
      * <code>0</code> if this category is not created yet or has no parent category. */
     public int getParentCategoryID() {
         return parentCategoryID;
     }

    public CategoryXML() {
        categoryID = -1;
        parentCategoryID = 0;
    }

    public void setCategoryID(String id) {
        categoryID = XMLUtil.stringToIntDef(id, -1);
    }

    public void setParentCategory(CategoryXML parentCategory) {
        parentCategoryID = parentCategory.getCategoryID();
    }

    public void setParentCategoryID(int value) {
        if (value < 0) {
            parentCategoryID = -1;
        } else {
            parentCategoryID = value;
        }
    }

    /**
     * Creates a category. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param categoryName Name of a category to be created.
     * @param categoryDesc Can be null.
     * @param categoryCreationDate Can be null.
     * @param categoryModifiedDate Can be null.
     * @param categoryOrder Can be null.
     * @param categoryOption Can be null.
     * @param categoryStatus Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addCategory(String categoryName,
                            String categoryDesc, String categoryCreationDate,
                            String categoryModifiedDate, String categoryOrder,
                            String categoryOption, String categoryStatus)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException {

        //parentCategoryID can be 0, and don't need to check if it's set or not
        if ((categoryName == null) || (categoryName.equals(""))) {
            throw new CreateException("Can't create a category with empty CategoryName.");
        } else {
            java.sql.Timestamp categoryCreationDate1;
            java.sql.Timestamp categoryModifiedDate1;
            int categoryOrder1;
            int categoryOption1;
            int categoryStatus1;

            try {
                if (categoryDesc == null) {
                    categoryDesc = "";
                }
                categoryCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(categoryCreationDate);
                categoryModifiedDate1 = XMLUtil.stringToSqlTimestampDefNow(categoryModifiedDate);
                categoryOrder1 = XMLUtil.stringToIntDef(categoryOrder, 0);
                categoryOption1 = XMLUtil.stringToIntDef(categoryOption, 0);
                categoryStatus1 = XMLUtil.stringToIntDef(categoryStatus, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a category. Expected a number.");
            }

            categoryName = EnableHtmlTagFilter.filter(categoryName);
            categoryDesc = EnableHtmlTagFilter.filter(categoryDesc);

            DAOFactory.getCategoryDAO().create(
                parentCategoryID, categoryName, categoryDesc,
                categoryCreationDate1, categoryModifiedDate1,
                categoryOrder1, categoryOption1, categoryStatus1);

            //todo Igor: Minh, you could move next piece of code into CategoryWebHelper.getCategoryIDFromPrimaryKey method
            Collection categories = DAOFactory.getCategoryDAO().getCategories();
            Iterator iter = categories.iterator();
            try {
                CategoryBean cat = null;
                categoryID = -1;
                while ((cat = (CategoryBean) iter.next()) != null) {
                    if ((cat.getCategoryName().equals(categoryName)) &&
                        (cat.getParentCategoryID() == parentCategoryID)) {
                        categoryID = cat.getCategoryID();
                        break;
                    }
                }
                if (categoryID < 0) {
                    throw new ObjectNotFoundException("Can't find category I've just added.");
                }
            } catch (NoSuchElementException e) {
                throw new ObjectNotFoundException("Can't find category I've just added.");
            }

        }
    }

    /**
     * Creates a category watch for this category. In order to know which category we are
     * referring to, this method is supposed to be called after {@link #setCategoryID(String)}
     * or {@link #addCategory(String, String, String, String, String, String, String)}
     * have been called. Otherwise, this watch will be simply ignored.
     *
     * @param memberName
     * @param watchType Can be null.
     * @param watchOption Can be null.
     * @param watchStatus Can be null.
     * @param watchCreationDate Can be null.
     * @param watchLastSentDate Can be null.
     * @param watchEndDate Can be null.
     *
     * @throws BadInputException
     * @throws CreateException
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addCategoryWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DatabaseException, ObjectNotFoundException,
        DuplicateKeyException, ForeignKeyNotFoundException {

        if (categoryID < 0) {
            throw new CreateException("Found category watch that is not assigned to any known category.");
        }

        int watchType1;
        int watchOption1;
        int watchStatus1;
        java.sql.Timestamp watchCreationDate1;
        java.sql.Timestamp watchLastSentDate1;
        java.sql.Timestamp watchEndDate1;

        try {
            if (memberName == null) {
                memberName = "";
            }
            watchType1 = XMLUtil.stringToIntDef(watchType, 0);
            watchOption1 = XMLUtil.stringToIntDef(watchOption, 0);
            watchStatus1 = XMLUtil.stringToIntDef(watchStatus, 0);
            watchCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(watchCreationDate);
            watchLastSentDate1 = XMLUtil.stringToSqlTimestampDefNull(watchLastSentDate);
            watchEndDate1 = XMLUtil.stringToSqlTimestampDefNull(watchEndDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a category. Expected a number.");
        }

        //todo Igor: Shoud I allow memberID==0 here?
        int memberID = 0;
        if (!memberName.equals("")) {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }
        DAOFactory.getWatchDAO().create(
             memberID, categoryID, 0/*forumId*/, 0/*threadId*/,
             watchType1, watchOption1, watchStatus1,
             watchCreationDate1, watchLastSentDate1, watchEndDate1);
    }

// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================

    public static void exportCategoryWatchesForCategory(XMLWriter xmlWriter, int categoryID)
        throws IOException, ExportException, NumberFormatException,
        ObjectNotFoundException, DatabaseException {

        Collection categoryWatches=ExportWebHelper.execSqlQuery(
                   "SELECT MemberID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate"+
                   " FROM "+WatchDAO.TABLE_NAME+
                   " WHERE ForumID=0 AND ThreadID=0"+
                   " AND CategoryID="+Integer.toString(categoryID));
        Iterator iter=categoryWatches.iterator();
        String[] categoryWatch=null;
        //try {
            xmlWriter.startElement("CategoryWatchList");
            try {
                while ( (categoryWatch=(String[])iter.next()) !=null) {
                    if (categoryWatch.length!=7) {
                        throw new ExportException("Error while retrieving data about category watch for categoryID=="+categoryID);
                    }
                    String memberName=DAOFactory.getMemberDAO().getMember(Integer.parseInt(categoryWatch[0])).getMemberName();
                    xmlWriter.startElement("CategoryWatch");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("WatchType");
                    xmlWriter.writeData(categoryWatch[1]);
                    xmlWriter.endElement("WatchType");
                    xmlWriter.startElement("WatchOption");
                    xmlWriter.writeData(categoryWatch[2]);
                    xmlWriter.endElement("WatchOption");
                    xmlWriter.startElement("WatchStatus");
                    xmlWriter.writeData(categoryWatch[3]);
                    xmlWriter.endElement("WatchStatus");
                    xmlWriter.startElement("WatchCreationDate");
                    xmlWriter.writeData(categoryWatch[4]);
                    xmlWriter.endElement("WatchCreationDate");
                    xmlWriter.startElement("WatchLastSentDate");
                    xmlWriter.writeData(categoryWatch[5]);
                    xmlWriter.endElement("WatchLastSentDate");
                    xmlWriter.startElement("WatchEndDate");
                    xmlWriter.writeData(categoryWatch[6]);
                    xmlWriter.endElement("WatchEndDate");
                    xmlWriter.endElement("CategoryWatch");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("CategoryWatchList");
         //} catch throw exportexception
    }

    public static void exportCategory(XMLWriter xmlWriter, int categoryID)
        throws NumberFormatException, IOException, ExportException, ObjectNotFoundException, DatabaseException {

        Collection category1=ExportWebHelper.execSqlQuery(
                   "SELECT CategoryName, CategoryDesc,"+
                   " CategoryCreationDate, CategoryModifiedDate, CategoryOrder,"+
                   " CategoryOption, CategoryStatus FROM "+CategoryDAO.TABLE_NAME+
                   " WHERE CategoryID="+Integer.toString(categoryID));
        Iterator iter = category1.iterator();
        String[] category = null;
        //try {
            try {
                if ((category = (String[]) iter.next()) == null) {
                    throw new ExportException("Can't find data for categoryID==" + categoryID);
                }
                if (category.length != 7) {
                    throw new ExportException("Error while retrieving data about category with categoryID==" + categoryID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for categoryID=="+categoryID);
            }

            //if I am here, that means I now have correct object category
            xmlWriter.startElement("Category");
            xmlWriter.startElement("CategoryName");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(category[0]));
            xmlWriter.endElement("CategoryName");
            xmlWriter.startElement("CategoryDesc");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(category[1]));
            xmlWriter.endElement("CategoryDesc");
            xmlWriter.startElement("CategoryCreationDate");
            xmlWriter.writeData(category[2]);
            xmlWriter.endElement("CategoryCreationDate");
            xmlWriter.startElement("CategoryModifiedDate");
            xmlWriter.writeData(category[3]);
            xmlWriter.endElement("CategoryModifiedDate");
            xmlWriter.startElement("CategoryOrder");
            xmlWriter.writeData(category[4]);
            xmlWriter.endElement("CategoryOrder");
            xmlWriter.startElement("CategoryOption");
            xmlWriter.writeData(category[5]);
            xmlWriter.endElement("CategoryOption");
            xmlWriter.startElement("CategoryStatus");
            xmlWriter.writeData(category[6]);
            xmlWriter.endElement("CategoryStatus");
            exportCategoryWatchesForCategory(xmlWriter, categoryID);
            ForumXML.exportForumList(xmlWriter, categoryID);
            exportSubCategoryList(xmlWriter, categoryID);
            xmlWriter.endElement("Category");
         //} catch throw exportexception
    }

    public static void exportSubCategoryList(XMLWriter xmlWriter, int parentCategoryID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {

        Collection categoryIDs=ExportWebHelper.execSqlQuery(
                   "SELECT CategoryID"+
                   " FROM "+CategoryDAO.TABLE_NAME+
                   " WHERE ParentCategoryID="+Integer.toString(parentCategoryID));
        Iterator iter = categoryIDs.iterator();
        String[] categoryID = null;
        //try {
            xmlWriter.startElement("CategoryList");
            try {
                while ((categoryID = (String[]) iter.next()) != null) {
                    if (categoryID.length != 1) {
                        throw new ExportException("Error while retrieving list of categories.");
                    }
                    try {
                        int i = Integer.parseInt(categoryID[0]);
                        exportCategory(xmlWriter, i);
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of categories.");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("CategoryList");
         //} catch throw exportexception
    }

    public static void exportCategoryList(XMLWriter xmlWriter)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {

        exportSubCategoryList(xmlWriter, 0/*parentCategoryID*/);
    }

}

