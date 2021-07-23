/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/GroupsBean.java,v 1.13 2008/10/17 11:13:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2008/10/17 11:13:55 $
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
import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,
 *                   GroupOption, GroupCreationDate, GroupModifiedDate
 * Excluded columns:
 */
public class GroupsBean {

    private int groupID;
    private int groupOwnerID;
    private String groupOwnerName;
    private String groupName;
    private String groupDesc;
    private int groupOption;
    private Timestamp groupCreationDate;
    private Timestamp groupModifiedDate;

    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getGroupOwnerID() {
        return groupOwnerID;
    }
    public void setGroupOwnerID(int groupOwnerID) {
        this.groupOwnerID = groupOwnerID;
    }

    public String getGroupOwnerName() {
        return groupOwnerName;
    }
    public void setGroupOwnerName(String groupOwnerName) {
        this.groupOwnerName = StringUtil.getEmptyStringIfNull(groupOwnerName);
    }

    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = StringUtil.getEmptyStringIfNull(groupDesc);
    }

    public int getGroupOption() {
        return groupOption;
    }
    public void setGroupOption(int groupOption) {
        this.groupOption = groupOption;
    }

    public Timestamp getGroupCreationDate() {
        return groupCreationDate;
    }
    public void setGroupCreationDate(Timestamp groupCreationDate) {
        this.groupCreationDate = groupCreationDate;
    }

    public Timestamp getGroupModifiedDate() {
        return groupModifiedDate;
    }
    public void setGroupModifiedDate(Timestamp groupModifiedDate) {
        this.groupModifiedDate = groupModifiedDate;
    }

    /************************************************
     * Customized methods come below
     ************************************************/
    private int groupMemberCount;

    public int getGroupMemberCount() {
        return groupMemberCount;
    }
    public void setGroupMemberCount(int groupMemberCount) {
        this.groupMemberCount = groupMemberCount;
    }

    public static GroupsBean getGroupsBean(Collection objGroupsBeans, int groupID)
        throws ObjectNotFoundException {
        Iterator iterator = objGroupsBeans.iterator();
        while (iterator.hasNext()) {
            GroupsBean objGroupsBean = (GroupsBean)iterator.next();
            if (objGroupsBean.getGroupID() == groupID) {
                return objGroupsBean;
            }
        }//while
        //@todo : localize me
        throw new ObjectNotFoundException("Cannot find GroupsBean with GroupID = " + groupID);
    }

} //end of class GroupsBean
