/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MemberGroupBean.java,v 1.13 2008/10/22 08:09:50 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.13 $
 * $Date: 2008/10/22 08:09:50 $
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

/*
 * Included columns: GroupID, MemberID, MemberName, Privilege, CreationDate,
 *                   ModifiedDate
 * Excluded columns:
 */
public class MemberGroupBean {
    
    private int groupID;
    private int memberID;
    private String memberName;
    private int privilege;
    private Timestamp creationDate;
    private Timestamp modifiedDate;
    private Timestamp expireDate;
    
    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getPrivilege() {
        return privilege;
    }
    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
    
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
   
    public Timestamp getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

} //end of class MemberGroupBean
