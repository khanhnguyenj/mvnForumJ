/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbForum.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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

import java.util.Collection;//for xml support
import java.util.Iterator;//for xml support

/*
 * Included columns: forum_id, cat_id, forum_name, forum_desc, forum_status, 
 *                   forum_order, forum_posts, forum_topics, forum_last_post_id, prune_next, 
 *                   prune_enable, auth_view, auth_read, auth_post, auth_reply, 
 *                   auth_edit, auth_delete, auth_sticky, auth_announce, auth_vote, 
 *                   auth_pollcreate, auth_attachments
 * Excluded columns: 
 */
public class PhpbbForum {
    private int forum_id;
    private int cat_id;
    private String forum_name;
    private String forum_desc;
    private int forum_status;
    private int forum_order;
    private int forum_posts;
    private int forum_topics;
    private int forum_last_post_id;
    private int prune_next;
    private int prune_enable;
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

    public int getforum_id() {
        return forum_id;
    }
    public void setforum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public int getcat_id() {
        return cat_id;
    }
    public void setcat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getforum_name() {
        return forum_name;
    }
    public void setforum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public String getforum_desc() {
        return forum_desc;
    }
    public void setforum_desc(String forum_desc) {
        this.forum_desc = forum_desc;
    }

    public int getforum_status() {
        return forum_status;
    }
    public void setforum_status(int forum_status) {
        this.forum_status = forum_status;
    }

    public int getforum_order() {
        return forum_order;
    }
    public void setforum_order(int forum_order) {
        this.forum_order = forum_order;
    }

    public int getforum_posts() {
        return forum_posts;
    }
    public void setforum_posts(int forum_posts) {
        this.forum_posts = forum_posts;
    }

    public int getforum_topics() {
        return forum_topics;
    }
    public void setforum_topics(int forum_topics) {
        this.forum_topics = forum_topics;
    }

    public int getforum_last_post_id() {
        return forum_last_post_id;
    }
    public void setforum_last_post_id(int forum_last_post_id) {
        this.forum_last_post_id = forum_last_post_id;
    }

    public int getprune_next() {
        return prune_next;
    }
    public void setprune_next(int prune_next) {
        this.prune_next = prune_next;
    }

    public int getprune_enable() {
        return prune_enable;
    }
    public void setprune_enable(int prune_enable) {
        this.prune_enable = prune_enable;
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

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_forumsSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>cat_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(cat_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_name</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_name)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_desc</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_desc)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_status</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_status)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_order</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_order)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_posts</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_posts)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_topics</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_topics)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_last_post_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_last_post_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>prune_next</Name>\n");
        xml.append("        <Value>").append(String.valueOf(prune_next)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>prune_enable</Name>\n");
        xml.append("        <Value>").append(String.valueOf(prune_enable)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_view</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_view)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_read</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_read)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_post</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_post)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_reply</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_reply)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_edit</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_edit)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_delete</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_delete)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_sticky</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_sticky)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_announce</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_announce)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_vote</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_vote)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_pollcreate</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_pollcreate)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>auth_attachments</Name>\n");
        xml.append("        <Value>").append(String.valueOf(auth_attachments)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_forumsSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_forumsBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_forumsBeans.iterator();
        xml.append("<phpbb_forumsSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbForum objphpbb_forumsBean = (PhpbbForum)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>cat_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.cat_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_name</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_name)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_desc</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_desc)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_status</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_status)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_order</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_order)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_posts</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_posts)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_topics</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_topics)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_last_post_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.forum_last_post_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>prune_next</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.prune_next)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>prune_enable</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.prune_enable)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_view</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_view)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_read</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_read)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_post</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_post)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_reply</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_reply)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_edit</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_edit)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_delete</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_delete)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_sticky</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_sticky)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_announce</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_announce)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_vote</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_vote)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_pollcreate</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_pollcreate)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>auth_attachments</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_forumsBean.auth_attachments)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_forumsSection>\n");
        return xml.toString();
    }
} //end of class phpbb_forumsBean
