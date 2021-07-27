/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ForumXML.java,v 1.25 2009/12/04 07:51:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.25 $
 * $Date: 2009/12/04 07:51:34 $
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
import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.db.*;

/**
 * @author Igor Manic
 * @version $Revision: 1.25 $, $Date: 2009/12/04 07:51:34 $
 * <br/>
 * <code>ForumXML</code> todo Igor: enter description
 *
 */
public class ForumXML {

    private int forumID;
    /** Returns <code>ForumID</code> of this forum or
      * <code>-1</code> if forum is not created yet. */
    public int getForumID() { return forumID; }

    private int parentCategoryID;
    /** Returns <code>CategoryID</code> of this forum's parent category or
      * <code>-1</code> if this forum is not created yet. */
    public int getParentCategoryID() { return parentCategoryID; }

    public ForumXML() {
        forumID = -1;
        parentCategoryID = -1;
    }

    public void setForumID(String id) {
        forumID=XMLUtil.stringToIntDef(id, -1);
    }

    public void setParentCategory(CategoryXML parentCategory) {
        parentCategoryID=parentCategory.getCategoryID();
    }

    public void setParentCategoryID(int value) {
        if (value < 0) {
            parentCategoryID = -1;
        } else {
            parentCategoryID = value;
        }
    }

    /**
     * Creates a forum. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param lastPostMemberName Can be null.
     * @param forumName Name of a forum to be created.
     * @param forumDesc Can be null.
     * @param forumCreationDate Can be null.
     * @param forumModifiedDate Can be null.
     * @param forumLastPostDate Can be null.
     * @param forumOrder Can be null.
     * @param forumType Can be null.
     * @param forumFormatOption Can be null.
     * @param forumOption Can be null.
     * @param forumStatus Can be null.
     * @param forumModerationMode Can be null.
     * @param forumPassword Password of a forum to be created. Can be null (or empty "").
     * @param forumThreadCount Can be null.
     * @param forumPostCount Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addForum(String lastPostMemberName, String forumName,
                         String forumDesc, String forumCreationDate,
                         String forumModifiedDate, String forumLastPostDate,
                         String forumOrder, String forumType,
                         String forumFormatOption, String forumOption,
                         String forumStatus, String forumModerationMode,
                         String forumPassword, String forumThreadCount,
                         String forumPostCount)
    throws CreateException, DuplicateKeyException, ObjectNotFoundException,
    DatabaseException, ForeignKeyNotFoundException {
        if (parentCategoryID<0) {
            throw new CreateException("Can't create a forum, because no parent category assigned yet.");
        }
        if ((forumName==null) || (forumName.equals(""))) {
            throw new CreateException("Can't create a forum with empty ForumName.");
        } else {
            java.sql.Timestamp forumCreationDate1;
            java.sql.Timestamp forumModifiedDate1;
            java.sql.Timestamp forumLastPostDate1;
            int forumOrder1;
            int forumType1;
            int forumFormatOption1;
            int forumOption1;
            int forumStatus1;
            int forumModerationMode1;
            int forumThreadCount1;
            int forumPostCount1;

            try {
                if (lastPostMemberName == null) {
                    lastPostMemberName = "";
                }
                if (forumDesc == null) {
                    forumDesc = "";
                }
                forumCreationDate1= XMLUtil.stringToSqlTimestampDefNow(forumCreationDate);
                forumModifiedDate1= XMLUtil.stringToSqlTimestampDefNull(forumModifiedDate);
                forumLastPostDate1= XMLUtil.stringToSqlTimestampDefNull(forumLastPostDate);
                forumOrder1 = XMLUtil.stringToIntDef(forumOrder, 0);
                forumType1 = XMLUtil.stringToIntDef(forumType, 0);
                forumFormatOption1 = XMLUtil.stringToIntDef(forumFormatOption, 0);
                forumOption1 = XMLUtil.stringToIntDef(forumOption, 0);
                forumStatus1 = XMLUtil.stringToIntDef(forumStatus, 0);
                forumModerationMode1 = XMLUtil.stringToIntDef(forumModerationMode, 0);
                if (forumPassword == null) {
                    forumPassword = "";
                }
                forumThreadCount1 = XMLUtil.stringToIntDef(forumThreadCount, 0);
                forumPostCount1 = XMLUtil.stringToIntDef(forumPostCount, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a forum. Expected a number.");
            }

            forumName=EnableHtmlTagFilter.filter(forumName);
            forumDesc=EnableHtmlTagFilter.filter(forumDesc);
            forumPassword=EnableHtmlTagFilter.filter(forumPassword);

            DAOFactory.getForumDAO().create(
                     parentCategoryID, ""/*forumOwnerName*/,
                     lastPostMemberName, forumName,
                     forumDesc, forumCreationDate1, forumModifiedDate1,
                     forumLastPostDate1, forumOrder1, forumType1,
                     forumFormatOption1, forumOption1, forumStatus1,
                     forumModerationMode1, forumPassword, forumThreadCount1, forumPostCount1);

            //todo Igor: Minh, you could move next piece of code into ForumWebHelper.getForumIDFromPrimaryKey method
            Collection forums=DAOFactory.getForumDAO().getForums_inCategory(parentCategoryID);
            Iterator iter=forums.iterator();
            try {
                ForumBean forum=null;
                forumID=-1;
                while ( (forum=(ForumBean)iter.next() )!=null) {
                    if ((forum.getForumName().equals(forumName)) && (forum.getCategoryID()==parentCategoryID)) {
                        forumID=forum.getForumID();
                        break;
                    }
                }
                if (forumID<0) {
                    throw new ObjectNotFoundException("Can't find forum I've just added.");
                }
            } catch (NoSuchElementException e) {
                throw new ObjectNotFoundException("Can't find forum I've just added.");
            }

        }
    }

    /**
     * Adds a forum-specific permission to a member. In order to know which forum we are
     * referring to, this method is supposed to be called after {@link #setForumID(String)} or
     * {@link #addForum(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this permission will be simply ignored.
     *
     * @param memberName Member we are assigning permissions to.
     * @param permission Permission to be added.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addMemberForumPermission(String memberName, String permission)
        throws CreateException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException {

        if (forumID < 0) {
            throw new CreateException("Found member's forum-specific permission that is not assigned to any known forum.");
        }
        if ( (memberName==null) || (memberName.equals("")) ) {
            throw new CreateException("Can't create a member's forum-specific permission for a member with empty MemberName.");
        }

        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a member forum-specific permission. Expected a number.");
        }
        int memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        try {
            DAOFactory.getMemberForumDAO().create(memberID, forumID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    /**
     * Adds a forum-specific permission to a group. In order to know which forum we are
     * referring to, this method is supposed to be called after {@link #setForumID(String)} or
     * {@link #addForum(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this permission will be simply ignored.
     *
     * @param groupName Group we are assigning permissions to.
     * @param permission Permission to be added.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addGroupForumPermission(String groupName, String permission)
        throws CreateException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException {

        if (forumID < 0) {
            throw new CreateException("Found group's forum-specific permission that is not assigned to any known forum.");
        }
        if ( (groupName == null) || (groupName.equals(""))) {
            throw new CreateException("Can't create a group's forum-specific permission for a group with empty GroupName.");
        }

        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group forum-specific permission. Expected a number.");
        }
        int groupID = DAOFactory.getGroupsDAO().getGroupIDFromGroupName(groupName);
        try {
            DAOFactory.getGroupForumDAO().create(groupID, forumID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public void addGuestMemberForumPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {

        if (forumID < 0) {
            throw new CreateException("Found guest's forum-specific permission that is not assigned to any known forum.");
        }
        
        int permission1;
        try {
            permission1=XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a guest member forum-specific permission. Expected a number.");
        }
        try {
            DAOFactory.getMemberForumDAO().create(MVNForumConstant.MEMBER_ID_OF_GUEST, forumID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public void addRegisteredMembersGroupForumPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        
        if (forumID < 0) {
            throw new CreateException("Found group's forum-specific permission that is not assigned to any known forum.");
        }
        
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group forum-specific permission. Expected a number.");
        }
        try {
            DAOFactory.getGroupForumDAO().create(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS, forumID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    /**
     * Creates a forum watch for this forum. In order to know which forum we are
     * referring to, this method is supposed to be called after {@link #setForumID(String)}
     * or {@link #addForum(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
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
    public void addForumWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DatabaseException, ObjectNotFoundException, DuplicateKeyException, ForeignKeyNotFoundException {

        if (forumID<0) {
            throw new CreateException("Found forum watch that is not assigned to any known forum.");
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
            watchType1= XMLUtil.stringToIntDef(watchType, 0);
            watchOption1= XMLUtil.stringToIntDef(watchOption, 0);
            watchStatus1= XMLUtil.stringToIntDef(watchStatus, 0);
            watchCreationDate1= XMLUtil.stringToSqlTimestampDefNow(watchCreationDate);
            watchLastSentDate1= XMLUtil.stringToSqlTimestampDefNull(watchLastSentDate);
            watchEndDate1= XMLUtil.stringToSqlTimestampDefNull(watchEndDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a forum. Expected a number.");
        }

        //todo Igor: Shoud I allow memberID==0 here?
        int memberID=0;
        if (memberName.length() > 0) {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }
        DAOFactory.getWatchDAO().create(
              memberID, 0/*categoryID*/, forumID, 0/*threadID*/,
              watchType1, watchOption1, watchStatus1,
              watchCreationDate1, watchLastSentDate1, watchEndDate1);
    }

    public void updateLastPostMemberName(String value)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if (forumID < 0) {
            throw new ObjectNotFoundException("Can't update ForumLastPostMemberName on forum that is not created yet.");
        }
        DAOFactory.getForumDAO().updateLastPostMemberName(forumID, value);
    }

    public void updateLastPostDate(Timestamp value)
        throws ObjectNotFoundException, DatabaseException {
        
        if (forumID < 0) {
            throw new ObjectNotFoundException("Can't update ForumLastPostDate on forum that is not created yet.");
        }
        DAOFactory.getForumDAO().updateLastPostDate(forumID, value);
    }

    public void increaseThreadCount()
        throws ObjectNotFoundException, DatabaseException {
        
        if (forumID < 0) {
            throw new ObjectNotFoundException("Can't update ForumThreadCount on forum that is not created yet.");
        }
        DAOFactory.getForumDAO().increaseThreadCount(forumID);
    }

    public void increasePostCount()
        throws ObjectNotFoundException, DatabaseException {
        
        if (forumID < 0) {
            throw new ObjectNotFoundException("Can't update ForumPostCount on forum that is not created yet.");
        }
        DAOFactory.getForumDAO().increasePostCount(forumID);
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================

    public static void exportForumWatchesForForum(XMLWriter xmlWriter, int forumID)
        throws IOException, ExportException, NumberFormatException, ObjectNotFoundException, DatabaseException {
        
        Collection forumWatches=ExportWebHelper.execSqlQuery(
                   "SELECT MemberID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate"+
                   " FROM "+WatchDAO.TABLE_NAME+
                   " WHERE ThreadID=0"+ //AND CategoryID=0
                   " AND ForumID="+Integer.toString(forumID));
        Iterator iter=forumWatches.iterator();
        String[] forumWatch=null;
        //try {
            xmlWriter.startElement("ForumWatchList");
            try {
                while ( (forumWatch=(String[])iter.next()) != null) {
                    if (forumWatch.length != 7) {
                        throw new ExportException("Error while retrieving data about forum watch for forumID=="+forumID);
                    }
                    String memberName=DAOFactory.getMemberDAO().getMember(Integer.parseInt(forumWatch[0])).getMemberName();
                    xmlWriter.startElement("ForumWatch");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("WatchType");
                    xmlWriter.writeData(forumWatch[1]);
                    xmlWriter.endElement("WatchType");
                    xmlWriter.startElement("WatchOption");
                    xmlWriter.writeData(forumWatch[2]);
                    xmlWriter.endElement("WatchOption");
                    xmlWriter.startElement("WatchStatus");
                    xmlWriter.writeData(forumWatch[3]);
                    xmlWriter.endElement("WatchStatus");
                    xmlWriter.startElement("WatchCreationDate");
                    xmlWriter.writeData(forumWatch[4]);
                    xmlWriter.endElement("WatchCreationDate");
                    xmlWriter.startElement("WatchLastSentDate");
                    xmlWriter.writeData(forumWatch[5]);
                    xmlWriter.endElement("WatchLastSentDate");
                    xmlWriter.startElement("WatchEndDate");
                    xmlWriter.writeData(forumWatch[6]);
                    xmlWriter.endElement("WatchEndDate");
                    xmlWriter.endElement("ForumWatch");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("ForumWatchList");
         //} catch throw exportexception
    }

    public static void exportMemberForumPermissionsForForum(XMLWriter xmlWriter, int forumID)
        throws IOException, ExportException, NumberFormatException, ObjectNotFoundException, DatabaseException {
        
        Collection memberForumPermissions = ExportWebHelper.execSqlQuery(
                   "SELECT MemberID, Permission"+
                   " FROM "+MemberForumDAO.TABLE_NAME+
                   " WHERE ForumID="+Integer.toString(forumID));
        String[] memberForumPermission=null;
        //try {
            xmlWriter.startElement("MemberForumPermissionList");
            try {
                Iterator iter = memberForumPermissions.iterator();
                while ( (memberForumPermission=(String[])iter.next()) != null) {
                    if (memberForumPermission.length != 2) {
                        throw new ExportException("Error while retrieving data about member forum-specific permissions for forumID=="+forumID);
                    }
                    String memberName = DAOFactory.getMemberDAO().getMember(Integer.parseInt(memberForumPermission[0])).getMemberName();
                    xmlWriter.startElement("MemberForumPermission");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("ForumPermission");
                    xmlWriter.writeData(memberForumPermission[1]);
                    xmlWriter.endElement("ForumPermission");
                    xmlWriter.endElement("MemberForumPermission");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("MemberForumPermissionList");
         //} catch throw exportexception
    }

    public static void exportGroupForumPermissionsForForum(XMLWriter xmlWriter, int forumID)
        throws IOException, DatabaseException, ExportException {
        
        Collection groupForumPermissions=ExportWebHelper.execSqlQuery(
                   "SELECT G.GroupName, GF.Permission"+
                   " FROM "+GroupForumDAO.TABLE_NAME+" AS GF, "+
                   GroupsDAO.TABLE_NAME+" AS G "+
                   " WHERE G.GroupID=GF.GroupID AND ForumID="+Integer.toString(forumID));
        Iterator iter=groupForumPermissions.iterator();
        String[] groupForumPermission=null;
        //try {
            xmlWriter.startElement("GroupForumPermissionList");
            try {
                while ( (groupForumPermission=(String[])iter.next()) !=null) {
                    if (groupForumPermission.length!=2) {
                        throw new ExportException("Error while retrieving data about group forum-specific permissions for forumID=="+forumID);
                    }
                    xmlWriter.startElement("GroupForumPermission");
                    xmlWriter.startElement("GroupName");
                    xmlWriter.writeData(groupForumPermission[0]);
                    xmlWriter.endElement("GroupName");
                    xmlWriter.startElement("ForumPermission");
                    xmlWriter.writeData(groupForumPermission[1]);
                    xmlWriter.endElement("ForumPermission");
                    xmlWriter.endElement("GroupForumPermission");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GroupForumPermissionList");
         //} catch throw exportexception
    }

    public static void exportForum(XMLWriter xmlWriter, int forumID)
        throws NumberFormatException, IOException, ExportException, ObjectNotFoundException, DatabaseException {
        Collection forum1=ExportWebHelper.execSqlQuery(
                   "SELECT LastPostMemberName, ForumName,"+
                   " ForumDesc, ForumCreationDate, ForumModifiedDate, ForumLastPostDate,"+
                   " ForumOrder, ForumType, ForumFormatOption, ForumOption,"+
                   " ForumStatus, ForumModerationMode, ForumPassword,"+
                   " ForumThreadCount, ForumPostCount"+
                   " FROM "+ForumDAO.TABLE_NAME+
                   " WHERE ForumID="+Integer.toString(forumID));
        Iterator iter=forum1.iterator();
        String[] forum=null;
        //try {
            try {
                if ( (forum=(String[])iter.next()) ==null) {
                    throw new ExportException("Can't find data for forumID=="+forumID);
                }
                if (forum.length!=15) {
                    throw new ExportException("Error while retrieving data about forum with forumID=="+forumID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for forumID=="+forumID);
            }

            //if I am here, that means I now have correct object forum
            xmlWriter.startElement("Forum");

            xmlWriter.startElement("ForumLastPostMemberName");
            xmlWriter.writeData(forum[0]);
            xmlWriter.endElement("ForumLastPostMemberName");
            xmlWriter.startElement("ForumName");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(forum[1]));
            xmlWriter.endElement("ForumName");
            xmlWriter.startElement("ForumDesc");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(forum[2]));
            xmlWriter.endElement("ForumDesc");
            xmlWriter.startElement("ForumCreationDate");
            xmlWriter.writeData(forum[3]);
            xmlWriter.endElement("ForumCreationDate");
            xmlWriter.startElement("ForumModifiedDate");
            xmlWriter.writeData(forum[4]);
            xmlWriter.endElement("ForumModifiedDate");

            xmlWriter.startElement("ForumLastPostDate");
            xmlWriter.writeData(forum[5]);
            xmlWriter.endElement("ForumLastPostDate");
            xmlWriter.startElement("ForumOrder");
            xmlWriter.writeData(forum[6]);
            xmlWriter.endElement("ForumOrder");
            xmlWriter.startElement("ForumType");
            xmlWriter.writeData(forum[7]);
            xmlWriter.endElement("ForumType");
            xmlWriter.startElement("ForumFormatOption");
            xmlWriter.writeData(forum[8]);
            xmlWriter.endElement("ForumFormatOption");
            xmlWriter.startElement("ForumOption");
            xmlWriter.writeData(forum[9]);
            xmlWriter.endElement("ForumOption");

            xmlWriter.startElement("ForumStatus");
            xmlWriter.writeData(forum[10]);
            xmlWriter.endElement("ForumStatus");
            xmlWriter.startElement("ForumModerationMode");
            xmlWriter.writeData(forum[11]);
            xmlWriter.endElement("ForumModerationMode");
            xmlWriter.startElement("ForumPassword");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(forum[12]));
            xmlWriter.endElement("ForumPassword");
            xmlWriter.startElement("ForumThreadCount");
            xmlWriter.writeData(forum[13]);
            xmlWriter.endElement("ForumThreadCount");
            xmlWriter.startElement("ForumPostCount");
            xmlWriter.writeData(forum[14]);
            xmlWriter.endElement("ForumPostCount");

            exportMemberForumPermissionsForForum(xmlWriter, forumID);
            exportGroupForumPermissionsForForum(xmlWriter, forumID);
            exportForumWatchesForForum(xmlWriter, forumID);
            ThreadXML.exportThreadList(xmlWriter, forumID);
            xmlWriter.endElement("Forum");
         //} catch throw exportexception
    }

    //todo Igor important: merge exportForumList and exportForum so I use only one SQL query
    //same for category(list), ...
    public static void exportForumList(XMLWriter xmlWriter, int parentCategoryID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        Collection forumIDs=ExportWebHelper.execSqlQuery(
                   "SELECT ForumID"+
                   " FROM "+ForumDAO.TABLE_NAME+
                   " WHERE CategoryID="+Integer.toString(parentCategoryID));
        Iterator iter=forumIDs.iterator();
        String[] forumID=null;
        //try {
            xmlWriter.startElement("ForumList");
            try {
                while ( (forumID=(String[])iter.next()) !=null) {
                    if (forumID.length!=1) {
                        throw new ExportException("Error while retrieving list of forums.");
                    }
                    try {
                        int i=Integer.parseInt(forumID[0]);
                        exportForum(xmlWriter, i);
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of forums.");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("ForumList");
         //} catch throw exportexception
    }

}

