/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbAuthAccess.java,v 1.3 2007/01/15 10:27:34 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:34 $
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
 * Included columns: group_id, forum_id, auth_view, auth_read, auth_post, 
 *                   auth_reply, auth_edit, auth_delete, auth_sticky, auth_announce, 
 *                   auth_vote, auth_pollcreate, auth_attachments, auth_mod
 * Excluded columns: 
 */
public class PhpbbAuthAccess {
    private int group_id;

    private int forum_id;

    private int auth_view;

    private int auth_read;

    private int auth_post;

    private int auth_reply;

    private int auth_edit;

    private int auth_delete;

    private int auth_sticky;

    private int auth_announce;

    private int auth_vote;

    private int auth_pollcreate;

    private int auth_attachments;

    private int auth_mod;

    public int getgroup_id() {
        return group_id;
    }

    public void setgroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getforum_id() {
        return forum_id;
    }

    public void setforum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public int getauth_view() {
        return auth_view;
    }

    public void setauth_view(int auth_view) {
        this.auth_view = auth_view;
    }

    public int getauth_read() {
        return auth_read;
    }

    public void setauth_read(int auth_read) {
        this.auth_read = auth_read;
    }

    public int getauth_post() {
        return auth_post;
    }

    public void setauth_post(int auth_post) {
        this.auth_post = auth_post;
    }

    public int getauth_reply() {
        return auth_reply;
    }

    public void setauth_reply(int auth_reply) {
        this.auth_reply = auth_reply;
    }

    public int getauth_edit() {
        return auth_edit;
    }

    public void setauth_edit(int auth_edit) {
        this.auth_edit = auth_edit;
    }

    public int getauth_delete() {
        return auth_delete;
    }

    public void setauth_delete(int auth_delete) {
        this.auth_delete = auth_delete;
    }

    public int getauth_sticky() {
        return auth_sticky;
    }

    public void setauth_sticky(int auth_sticky) {
        this.auth_sticky = auth_sticky;
    }

    public int getauth_announce() {
        return auth_announce;
    }

    public void setauth_announce(int auth_announce) {
        this.auth_announce = auth_announce;
    }

    public int getauth_vote() {
        return auth_vote;
    }

    public void setauth_vote(int auth_vote) {
        this.auth_vote = auth_vote;
    }

    public int getauth_pollcreate() {
        return auth_pollcreate;
    }

    public void setauth_pollcreate(int auth_pollcreate) {
        this.auth_pollcreate = auth_pollcreate;
    }

    public int getauth_attachments() {
        return auth_attachments;
    }

    public void setauth_attachments(int auth_attachments) {
        this.auth_attachments = auth_attachments;
    }

    public int getauth_mod() {
        return auth_mod;
    }

    public void setauth_mod(int auth_mod) {
        this.auth_mod = auth_mod;
    }
} //end of class phpbb_auth_accessBean
