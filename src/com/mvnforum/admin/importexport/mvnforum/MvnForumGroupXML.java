/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumGroupXML.java,v 1.13 2009/12/04 07:51:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
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
package com.mvnforum.admin.importexport.mvnforum;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.admin.GroupXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.13 $, $Date: 2009/12/04 07:51:34 $
 * <br/>
 * <code>MvnForumGroupXML</code> class encapsulates processing of
 * groups' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>GroupXML</code> and other neccessary classes in order to perform
 * the actual creation of a group, as well as other related items (like
 * group members and group permissions). It also ensures that default
 * virtual <code>"Registered Members"</code> group is created, even if it
 * wasn't found in the XML.
 */
public class MvnForumGroupXML {

    private GroupXML groupXML = null;
    private boolean groupCreated = false;
    private boolean isRegisteredMembersGroup = false;

    String groupOwnerName    = null;
    String groupName         = null;
    String groupDesc         = null;
    String groupOption       = null;
    String groupCreationDate = null;
    String groupModifiedDate = null;

    public MvnForumGroupXML() {
        groupXML = new GroupXML();
        groupCreated = false;
        isRegisteredMembersGroup = false;
    }

    public int getGroupID() {
        return groupXML.getGroupID();
    }

    public void setGroupID(String id) {
        groupXML.setGroupID(id);
    }

    /**
     * This method simply calls <code>setGroupID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setGroupID()</code> as a setter
     * method for <code>groupID</code> property).
     */
    public void setGroupId(String id) {
        setGroupID(id);
    }

    public void setGroupClass(String groupClass) {
        if (groupClass!=null) {
            if (groupClass.equalsIgnoreCase("RegisteredMembers")) {
                groupXML.setGroupID(Integer.toString(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS));
                isRegisteredMembersGroup=true;
            }
        }
    }

    public void setGroupOwnerName(String value) {
        groupOwnerName=value;
    }

    public void setGroupName(String value) {
        groupName=value;
    }

    public void setGroupDesc(String value) {
        groupDesc=value;
    }

    public void setGroupOption(String value) {
        groupOption=value;
    }

    public void setGroupCreationDate(String value) {
        groupCreationDate=value;
    }

    public void setGroupModifiedDate(String value) {
        groupModifiedDate=value;
    }

    public void addGroup() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, 
        DatabaseException, ForeignKeyNotFoundException {
        
        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this group has
         * subelements that need it already be defined, so they first call
         * this method to create group before creating data that refer him.
         */
        if (groupCreated) {
            return;
        }
        /* Then, check if "Registered Members" group is created.
         * It must be the first one to be created, otherwise, DBMS might have
         * added some other (non-reserved) group with this ID
         * that is supposed to be reserved for "Registered Members".
         */
        if (!isRegisteredMembersGroup) {
            MvnForumXML.checkRegisteredMembersGroup();
        }

        ImportMvnForum.addMessage("Adding group \""+groupName+"\".");
        groupXML.addGroup(groupOwnerName, groupName, groupDesc,
                          groupOption, groupCreationDate, groupModifiedDate);
        groupCreated = true;
        if (isRegisteredMembersGroup) {
            MvnForumXML.addedRegisteredMembersGroup = true;
        }
    }

    public void addGroupPermission(String permission) 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (!groupCreated) || (groupXML.getGroupID() < 0) ) {
            addGroup();
        }
        ImportMvnForum.addMessage("Adding group permission \"" + permission + "\".");
        groupXML.addGroupPermission(permission);
    }

    public void addGroupMember(String memberName, String privilege, String creationDate, String modifiedDate, String expireDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (!groupCreated) || (groupXML.getGroupID()<0) ) {
            addGroup();
        }
        ImportMvnForum.addMessage("Adding group member \""+memberName+"\".");
        groupXML.addMemberGroup(memberName, privilege, creationDate, modifiedDate, expireDate);
    }

}
