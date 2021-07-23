/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbGroup.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:33 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db;

/*
 * Included columns: group_id, group_type, group_name, group_description, group_moderator, 
 *                   group_single_user
 * Excluded columns: 
 */
public class PhpbbGroup {
    private int group_id;

    private int group_type;

    private String group_name;

    private String group_description;

    private int group_moderator;

    private int group_single_user;

    public int getgroup_id() {
        return group_id;
    }

    public void setgroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getgroup_type() {
        return group_type;
    }

    public void setgroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getgroup_name() {
        return group_name;
    }

    public void setgroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getgroup_description() {
        return group_description;
    }

    public void setgroup_description(String group_description) {
        this.group_description = group_description;
    }

    public int getgroup_moderator() {
        return group_moderator;
    }

    public void setgroup_moderator(int group_moderator) {
        this.group_moderator = group_moderator;
    }

    public int getgroup_single_user() {
        return group_single_user;
    }

    public void setgroup_single_user(int group_single_user) {
        this.group_single_user = group_single_user;
    }
}
