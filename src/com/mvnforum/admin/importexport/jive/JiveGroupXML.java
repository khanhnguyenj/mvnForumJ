/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/jive/JiveGroupXML.java,v 1.13 2009/09/23 08:47:48 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.13 $
 * $Date: 2009/09/23 08:47:48 $
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
package com.mvnforum.admin.importexport.jive;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.admin.GroupXML;

/**
 * @author Igor Manic
 * @version $Revision: 1.13 $, $Date: 2009/09/23 08:47:48 $
 * <br/>
 * <code>JiveGroupXML</code> class encapsulates processing of
 * groups' definitions found in the Jive XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>GroupXML</code> and other neccessary classes in order to perform
 * the actual creation of a group, as well as assigning members to the group.
 */
public class JiveGroupXML {

    private GroupXML groupXML = null;
    private boolean groupCreated = false;

    public JiveGroupXML() {
        groupXML = new GroupXML();
        groupCreated = false;
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


    private String groupName = null;
    public void setGroupName(String value) throws CreateException {
        if ( (value==null) || (value.equals("")) ) {
            throw new CreateException("Cannot create a group with an empty GroupName.");
        } else {
            this.groupName = value;
        }
    }

    private String groupDesc = null;
    public void setGroupDescription(String value) {
        this.groupDesc=value;
    }

    private String groupCreationDate = null;
    public void setGroupCreationDate(String value) {
        this.groupCreationDate=value;
    }

    private String groupModifiedDate = null;
    public void setGroupModifiedDate(String value) {
        this.groupModifiedDate=value;
    }

    private String groupOwnerName = null;
    public void setGroupOwnerName(String value) {
        this.groupOwnerName=value;
    }

    public void addJiveGroup() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        /* First check if the digester already called this method.
         * It will happen even under normal circumstances, if this group has
         * sub-elements that need it already be defined, so they first call
         * this method to create group before creating data that refer him.
         */
        if (groupCreated) {
            return;
        }
        if ( (groupName==null) || (groupName.equals("")) ) {
            throw new CreateException("Cannot create a group with an empty GroupName.");
        } else {
            
            ImportJive.addMessage("Adding Jive group \""+groupName+"\".");
            
            groupOwnerName = ImportJive.getMemberNameFromMap(groupOwnerName);
            
            groupXML.addGroup(groupOwnerName, groupName, groupDesc,
                              null/*groupOption*/, groupCreationDate, groupModifiedDate);
            groupCreated = true;
        }
    }

    public void addJiveGroupMember(String username)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (!groupCreated) || (groupXML.getGroupID()<0) ) {
            addJiveGroup();
        }
        if ( (username==null) || (username.equals("")) ) {
            throw new CreateException("Cannot create a group member with an empty MemberName.");
        } else {
            username = ImportJive.getMemberNameFromMap(username);
            ImportJive.addMessage("Adding group member \""+username+"\".");
            groupXML.addMemberGroup(username, null/*privilege*/,
                                    null/*creationDate*/, null/*modifiedDate*/, null/*expireDate*/);
        }
    }

}
