/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbTopics.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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

//import java.sql.*; // @todo: uncomment as needed
import java.util.Collection;//for xml support
import java.util.Iterator;//for xml support

/*
 * Included columns: topic_id, forum_id, topic_title, topic_poster, topic_time, 
 *                   topic_views, topic_replies, topic_status, topic_vote, topic_type, 
 *                   topic_first_post_id, topic_last_post_id, topic_moved_id
 * Excluded columns: 
 */
public class PhpbbTopics {
    private int topic_id;
    private int forum_id;
    private String topic_title;
    private int topic_poster;
    private int topic_time;
    private int topic_views;
    private int topic_replies;
    private int topic_status;
    private int topic_vote;
    private int topic_type;
    private int topic_first_post_id;
    private int topic_last_post_id;
    private int topic_moved_id;

    public int gettopic_id() {
        return topic_id;
    }
    public void settopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getforum_id() {
        return forum_id;
    }
    public void setforum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public String gettopic_title() {
        return topic_title;
    }
    public void settopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public int gettopic_poster() {
        return topic_poster;
    }
    public void settopic_poster(int topic_poster) {
        this.topic_poster = topic_poster;
    }

    public int gettopic_time() {
        return topic_time;
    }
    public void settopic_time(int topic_time) {
        this.topic_time = topic_time;
    }

    public int gettopic_views() {
        return topic_views;
    }
    public void settopic_views(int topic_views) {
        this.topic_views = topic_views;
    }

    public int gettopic_replies() {
        return topic_replies;
    }
    public void settopic_replies(int topic_replies) {
        this.topic_replies = topic_replies;
    }

    public int gettopic_status() {
        return topic_status;
    }
    public void settopic_status(int topic_status) {
        this.topic_status = topic_status;
    }

    public int gettopic_vote() {
        return topic_vote;
    }
    public void settopic_vote(int topic_vote) {
        this.topic_vote = topic_vote;
    }

    public int gettopic_type() {
        return topic_type;
    }
    public void settopic_type(int topic_type) {
        this.topic_type = topic_type;
    }

    public int gettopic_first_post_id() {
        return topic_first_post_id;
    }
    public void settopic_first_post_id(int topic_first_post_id) {
        this.topic_first_post_id = topic_first_post_id;
    }

    public int gettopic_last_post_id() {
        return topic_last_post_id;
    }
    public void settopic_last_post_id(int topic_last_post_id) {
        this.topic_last_post_id = topic_last_post_id;
    }

    public int gettopic_moved_id() {
        return topic_moved_id;
    }
    public void settopic_moved_id(int topic_moved_id) {
        this.topic_moved_id = topic_moved_id;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_topicsSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_title</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_title)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_poster</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_poster)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_time</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_time)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_views</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_views)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_replies</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_replies)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_status</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_status)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_vote</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_vote)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_type</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_type)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_first_post_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_first_post_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_last_post_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_last_post_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_moved_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_moved_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_topicsSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_topicsBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_topicsBeans.iterator();
        xml.append("<phpbb_topicsSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbTopics objphpbb_topicsBean = (PhpbbTopics)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.forum_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_title</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_title)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_poster</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_poster)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_time</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_time)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_views</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_views)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_replies</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_replies)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_status</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_status)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_vote</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_vote)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_type</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_type)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_first_post_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_first_post_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_last_post_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_last_post_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_moved_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_topicsBean.topic_moved_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_topicsSection>\n");
        return xml.toString();
    }
} //end of class phpbb_topicsBean
