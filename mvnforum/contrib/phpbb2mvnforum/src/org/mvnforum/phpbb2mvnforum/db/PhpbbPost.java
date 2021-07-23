/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbPost.java,v 1.4 2007/01/15 10:27:33 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.4 $
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
 * Included columns: post_id, topic_id, forum_id, poster_id, post_time, 
 *                   poster_ip, post_username, enable_bbcode, enable_html, enable_smilies, 
 *                   enable_sig, post_edit_time, post_edit_count
 * Excluded columns: 
 */
public class PhpbbPost {
    private int post_id;
    private int topic_id;
    private int forum_id;
    private int poster_id;
    private int post_time;
    private String poster_ip;
    private String post_username;
    private int enable_bbcode;
    private int enable_html;
    private int enable_smilies;
    private int enable_sig;
    private int post_edit_time;
    private int post_edit_count;

    public int getpost_id() {
        return post_id;
    }
    public void setpost_id(int post_id) {
        this.post_id = post_id;
    }

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

    public int getposter_id() {
        return poster_id;
    }
    public void setposter_id(int poster_id) {
        this.poster_id = poster_id;
    }

    public int getpost_time() {
        return post_time;
    }
    public void setpost_time(int post_time) {
        this.post_time = post_time;
    }

    public String getposter_ip() {
        return poster_ip;
    }
    public void setposter_ip(String poster_ip) {
        this.poster_ip = poster_ip;
    }

    public String getpost_username() {
        return post_username;
    }
    public void setpost_username(String post_username) {
        this.post_username = post_username;
    }

    public int getenable_bbcode() {
        return enable_bbcode;
    }
    public void setenable_bbcode(int enable_bbcode) {
        this.enable_bbcode = enable_bbcode;
    }

    public int getenable_html() {
        return enable_html;
    }
    public void setenable_html(int enable_html) {
        this.enable_html = enable_html;
    }

    public int getenable_smilies() {
        return enable_smilies;
    }
    public void setenable_smilies(int enable_smilies) {
        this.enable_smilies = enable_smilies;
    }

    public int getenable_sig() {
        return enable_sig;
    }
    public void setenable_sig(int enable_sig) {
        this.enable_sig = enable_sig;
    }

    public int getpost_edit_time() {
        return post_edit_time;
    }
    public void setpost_edit_time(int post_edit_time) {
        this.post_edit_time = post_edit_time;
    }

    public int getpost_edit_count() {
        return post_edit_count;
    }
    public void setpost_edit_count(int post_edit_count) {
        this.post_edit_count = post_edit_count;
    }

    /*public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_postsSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>topic_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(topic_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>forum_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(forum_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>poster_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(poster_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_time</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_time)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>poster_ip</Name>\n");
        xml.append("        <Value>").append(String.valueOf(poster_ip)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_username</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_username)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>enable_bbcode</Name>\n");
        xml.append("        <Value>").append(String.valueOf(enable_bbcode)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>enable_html</Name>\n");
        xml.append("        <Value>").append(String.valueOf(enable_html)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>enable_smilies</Name>\n");
        xml.append("        <Value>").append(String.valueOf(enable_smilies)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>enable_sig</Name>\n");
        xml.append("        <Value>").append(String.valueOf(enable_sig)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_edit_time</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_edit_time)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_edit_count</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_edit_count)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_postsSection>\n");
        return xml.toString();
    }*/

    public static String getXML(Collection objphpbb_postsBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_postsBeans.iterator();
        xml.append("<phpbb_postsSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbPost objphpbb_postsBean = (PhpbbPost)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.post_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>topic_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.topic_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>forum_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.forum_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>poster_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.poster_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_time</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.post_time)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>poster_ip</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.poster_ip)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_username</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.post_username)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>enable_bbcode</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.enable_bbcode)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>enable_html</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.enable_html)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>enable_smilies</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.enable_smilies)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>enable_sig</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.enable_sig)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_edit_time</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.post_edit_time)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_edit_count</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_postsBean.post_edit_count)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_postsSection>\n");
        return xml.toString();
    }
} //end of class phpbb_postsBean
