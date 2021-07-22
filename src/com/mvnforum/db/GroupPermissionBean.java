/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/GroupPermissionBean.java,v 1.10 2008/05/21 09:00:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2008/05/21 09:00:16 $
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

/*
 * Included columns: GroupID, Permission
 * Excluded columns:
 */
public class GroupPermissionBean {
    private int groupID;
    private int permission;

    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getPermission() {
        return permission;
    }
    public void setPermission(int permission) {
        this.permission = permission;
    }

} //end of class GroupPermissionBean
