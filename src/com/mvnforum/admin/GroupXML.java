/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/GroupXML.java,v 1.19 2009/10/20 01:41:11 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.19 $
 * $Date: 2009/10/20 01:41:11 $
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

import net.myvietnam.mvncore.db.DBUtils;
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
 * @version $Revision: 1.19 $, $Date: 2009/10/20 01:41:11 $
 * <br/>
 * <code>GroupXML</code> todo Igor: enter description
 */
public class GroupXML {

    private int groupID;
    /** Returns <code>GroupID</code> of this group or
      * <code>-1</code> if group is not created yet. */
    public int getGroupID() { 
        return groupID; 
    }

    public GroupXML() {
        groupID = -1;
    }

    public void setGroupID(String id) {
        groupID = XMLUtil.stringToIntDef(id, -1);
    }

    /**
     * Creates a group. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param groupOwnerName Can be null.
     * @param groupName Name of a group to be created.
     * @param groupDesc Can be null.
     * @param groupOption Can be null.
     * @param groupCreationDate Can be null.
     * @param groupModifiedDate Can be null.
     * 
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addGroup(String groupOwnerName, String groupName,
                         String groupDesc, String groupOption,
                         String groupCreationDate, String groupModifiedDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        String strGroupID = null;
        if (groupID >= 0) {
            strGroupID = Integer.toString(groupID);
        }
        addGroup(strGroupID, groupOwnerName, groupName,
                 groupDesc, groupOption, groupCreationDate, groupModifiedDate);
    }

    /**
     * Creates a group. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param strGroupID Can be null, and it probably will be in most occasions,
     *                   except when you want to setup an explicit value, like
     *                   for virtual "Registered Members" groups.
     * @param groupOwnerName Can be null.
     * @param groupName Name of a group to be created.
     * @param groupDesc Can be null.
     * @param groupOption Can be null.
     * @param groupCreationDate Can be null.
     * @param groupModifiedDate Can be null.
     * 
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addGroup(String strGroupID,
                         String groupOwnerName, String groupName,
                         String groupDesc, String groupOption,
                         String groupCreationDate, String groupModifiedDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (groupName==null) || (groupName.equals("")) ) {
            throw new CreateException("Can't create a group with empty GroupName.");
        } else {
            int groupOption1;
            Timestamp groupCreationDate1;
            Timestamp groupModifiedDate1;
            try {
                if (groupOwnerName == null) {
                    groupOwnerName = "";
                }
                if (groupDesc == null) {
                    groupDesc = "";
                }
                groupOption1 = XMLUtil.stringToIntDef(groupOption, 0);
                groupCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(groupCreationDate);
                groupModifiedDate1 =XMLUtil.stringToSqlTimestampDefNow(groupModifiedDate);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a group. Expected a number.");
            }

            //now ensure that strGroupID is valid number, or null
            if ( (strGroupID!=null) && (!strGroupID.equals("")) ) {
                try {
                    if (Integer.parseInt(strGroupID) < 0) {
                        strGroupID = null;
                    }
                } catch (NumberFormatException e) {
                    strGroupID = null;
                }
            } else {
                strGroupID = null;
            }

            groupName = EnableHtmlTagFilter.filter(groupName);
            groupDesc = EnableHtmlTagFilter.filter(groupDesc);
            if (strGroupID == null) {
                //GroupsWebHelper correctly replaces empty groupOwnerName with GroupOwnerID=0
                DAOFactory.getGroupsDAO().create(groupOwnerName, groupName, groupDesc,
                                                 groupOption1, groupCreationDate1, groupModifiedDate1);
            } else {
                int groupOwnerID = 0;
                try {
                    if (groupOwnerName.length() > 0) {
                        groupOwnerID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(groupOwnerName);
                    }
                } catch (ObjectNotFoundException e) {
                    groupOwnerID = 0;
                }
                String query = "INSERT INTO "+ GroupsDAO.TABLE_NAME +
                               " (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc," +
                               " GroupOption, GroupCreationDate, GroupModifiedDate)" +
                               " VALUES (" +strGroupID+ ", " +groupOwnerID+ ", '"+groupOwnerName +
                               "', '" +groupName+ "', '" +groupDesc+ "', " +groupOption1+
                               ", '" +groupCreationDate1+ "', '" +groupModifiedDate1+ "')";
                if (DBUtils.getDatabaseType() == DBUtils.DATABASE_SQLSERVER) {
                    query = "SET IDENTITY_INSERT " + GroupsDAO.TABLE_NAME + " ON;" + query + ";SET IDENTITY_INSERT " + GroupsDAO.TABLE_NAME + " OFF;";
                }
                if (ImportWebHelper.execUpdateQuery(query) != 1) {
                    throw new CreateException("Error adding group \""+groupName+"\" into table '" + GroupsDAO.TABLE_NAME +"'.");
                }
            }

            this.groupID = DAOFactory.getGroupsDAO().getGroupIDFromGroupName(groupName);
        }
    }

    /**
     * Adds a permission to this group. In order to know which group we are
     * referring to, this method is supposed to be called after {@link #setGroupID(String)},
     * {@link #addGroup(String, String, String, String, String, String, String)}
     * or {@link #addGroup(String, String, String, String, String, String)}
     * have been called. Otherwise, this permission will be simply ignored.
     *
     * @param permission Permission to be added to this group.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addGroupPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        
        if (groupID < 0) {
            throw new CreateException("Found group permission that is not assigned to any known group.");
        }
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group permission. Expected a number.");
        }
        try {
            DAOFactory.getGroupPermissionDAO().create(groupID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public static void addRegisteredMembersGroupPermission(String permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group permission. Expected a number.");
        }
        DAOFactory.getGroupPermissionDAO().create(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS, permission1);
    }

    public static void addGroupPermission(String groupName, String permission)
        throws CreateException, DatabaseException, DuplicateKeyException,
        ForeignKeyNotFoundException, ObjectNotFoundException {
        
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group permission. Expected a number.");
        }
        DAOFactory.getGroupPermissionDAO().create(DAOFactory.getGroupsDAO().getGroupIDFromGroupName(groupName), permission1);
    }

    /**
     * Adds a member to this group. In order to know which group we are
     * referring to, this method is supposed to be called after {@link #setGroupID(String)},
     * {@link #addGroup(String, String, String, String, String, String, String)}
     * or {@link #addGroup(String, String, String, String, String, String)}
     * have been called. Otherwise, this member assignment will be simply ignored.
     *
     * @param memberName MemberName of a meber to be added to this group.
     * @param privilege Can be null.
     * @param creationDate Can be null.
     * @param modifiedDate Can be null.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     */
    public void addMemberGroup(String memberName, String privilege,
                String creationDate, String modifiedDate, String expireDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        
        if (groupID < 0) {
            throw new CreateException("Found group member that is not assigned to any known group.");
        }
        if ( (memberName==null) || (memberName.equals("")) ) {
            throw new CreateException("Can't create a group member with empty MemberName.");
        }

        int privilege1;
        Timestamp creationDate1;
        Timestamp modifiedDate1;
        Timestamp expireDate1;
        try {
            privilege1 = XMLUtil.stringToIntDef(privilege, 0);
            creationDate1 = XMLUtil.stringToSqlTimestampDefNow(creationDate);
            modifiedDate1 = XMLUtil.stringToSqlTimestampDefNow(modifiedDate);
            expireDate1 = XMLUtil.stringToSqlTimestampDefNow(expireDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a group member. Expected a number.");
        }
        DAOFactory.getMemberGroupDAO().create(this.groupID, memberName,
                             privilege1, creationDate1, modifiedDate1, expireDate1);
    }

// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================

    public static void exportGlobalPermissionsForGroup(XMLWriter xmlWriter, int groupID)
        throws IOException, DatabaseException, ExportException {
        
        Collection globalPermissions=ExportWebHelper.execSqlQuery(
                   "SELECT Permission"+
                   " FROM "+GroupPermissionDAO.TABLE_NAME+
                   " WHERE GroupID="+Integer.toString(groupID));
        Iterator iter = globalPermissions.iterator();
        String[] globalPermission = null;
        //try {
            xmlWriter.startElement("GlobalPermissionList");
            try {
                while ( (globalPermission=(String[])iter.next()) != null) {
                    if (globalPermission.length != 1) {
                        throw new ExportException("Error while retrieving data about global permissions for groupID==" + groupID);
                    }
                    xmlWriter.startElement("GlobalPermission");
                    xmlWriter.writeData(globalPermission[0]);
                    xmlWriter.endElement("GlobalPermission");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GlobalPermissionList");
         //} catch throw exportexception
    }

    public static void exportGroupMembersForGroup(XMLWriter xmlWriter, int groupID)
        throws IOException, DatabaseException, ExportException {
        
        Collection groupMembers=ExportWebHelper.execSqlQuery(
                   "SELECT MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate"+
                   " FROM "+MemberGroupDAO.TABLE_NAME+
                   " WHERE GroupID="+Integer.toString(groupID));
        //todo Igor: I am using MemberName, but nobody can guarantee it will be consistent with MemberID
        Iterator iter = groupMembers.iterator();
        String[] groupMember = null;
        //try {
            xmlWriter.startElement("GroupMemberList");
            try {
                while ( (groupMember = (String[])iter.next()) != null) {
                    if (groupMember.length!=4) {
                        throw new ExportException("Error while retrieving data about group member for groupID=="+groupID);
                    }
                    xmlWriter.startElement("GroupMember");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(groupMember[0]);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("Privilege");
                    xmlWriter.writeData(groupMember[1]);
                    xmlWriter.endElement("Privilege");
                    xmlWriter.startElement("CreationDate");
                    xmlWriter.writeData(groupMember[2]);
                    xmlWriter.endElement("CreationDate");
                    xmlWriter.startElement("ModifiedDate");
                    xmlWriter.writeData(groupMember[3]);
                    xmlWriter.endElement("ModifiedDate");
                    xmlWriter.startElement("ExpireDate");
                    xmlWriter.writeData(groupMember[4]);
                    xmlWriter.endElement("ExpireDate");
                    xmlWriter.endElement("GroupMember");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GroupMemberList");
         //} catch throw exportexception
    }

    public static void exportGroup(XMLWriter xmlWriter, int groupID)
        throws IOException, DatabaseException, ExportException {
        
        Collection group1 = ExportWebHelper.execSqlQuery(
                   "SELECT GroupOwnerName, GroupName,"+
                   " GroupDesc, GroupOption, GroupCreationDate, GroupModifiedDate"+
                   " FROM "+GroupsDAO.TABLE_NAME+
                   " WHERE GroupID="+Integer.toString(groupID));
        Iterator iter = group1.iterator();
        String[] group = null;
        //try {
            try {
                if ( (group=(String[])iter.next()) == null) {
                    throw new ExportException("Can't find data for groupID==" + groupID);
                }
                if (group.length != 6) {
                    throw new ExportException("Error while retrieving data about group with groupID==" + groupID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for groupID==" + groupID);
            }

            //if I am here, that means I now have correct object group
            if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
                xmlWriter.startElement("Group", new String[]{"class", "RegisteredMembers"});
            } else {
                xmlWriter.startElement("Group");
            }
            xmlWriter.startElement("GroupOwnerName");
            xmlWriter.writeData(group[0]);
            xmlWriter.endElement("GroupOwnerName");
            xmlWriter.startElement("GroupName");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(group[1]));
            xmlWriter.endElement("GroupName");
            xmlWriter.startElement("GroupDesc");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(group[2]));
            xmlWriter.endElement("GroupDesc");
            xmlWriter.startElement("GroupOption");
            xmlWriter.writeData(group[3]);
            xmlWriter.endElement("GroupOption");
            xmlWriter.startElement("GroupCreationDate");
            xmlWriter.writeData(group[4]);
            xmlWriter.endElement("GroupCreationDate");
            xmlWriter.startElement("GroupModifiedDate");
            xmlWriter.writeData(group[5]);
            xmlWriter.endElement("GroupModifiedDate");
            exportGlobalPermissionsForGroup(xmlWriter, groupID);
            exportGroupMembersForGroup(xmlWriter, groupID);
            xmlWriter.endElement("Group");
         //} catch throw exportexception
    }

    public static void exportGroupList(XMLWriter xmlWriter)
        throws IOException, DatabaseException, ExportException {
        
        Collection groupIDs=ExportWebHelper.execSqlQuery(
                   "SELECT GroupID" +
                   " FROM " + GroupsDAO.TABLE_NAME);
        Iterator iter = groupIDs.iterator();
        String[] groupID = null;
        //try {
            xmlWriter.startElement("GroupList");
            /* First, I'll export Registered Members group. If it doesn't exist, just continue. */
            try {
                exportGroup(xmlWriter, MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS);
            } catch (Exception e) {
                //doesn't exist => ignore
            }
            try {
                while ( (groupID=(String[])iter.next()) != null) {
                    if (groupID.length!=1) {
                        throw new ExportException("Error while retrieving list of groups.");
                    }
                    try {
                        int i = Integer.parseInt(groupID[0]);
                        if (i != MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
                            exportGroup(xmlWriter, i);
                        }
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of groups.");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GroupList");
         //} catch throw exportexception
    }

}
