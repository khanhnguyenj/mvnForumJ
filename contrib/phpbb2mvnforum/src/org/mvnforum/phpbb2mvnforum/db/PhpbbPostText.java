/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbPostText.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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
 * Included columns: post_id, bbcode_uid, post_subject, post_text
 * Excluded columns: 
 */
public class PhpbbPostText {
    private int post_id;
    private String bbcode_uid;
    private String post_subject;
    private String post_text;

    public int getpost_id() {
        return post_id;
    }
    public void setpost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getbbcode_uid() {
        return bbcode_uid;
    }
    public void setbbcode_uid(String bbcode_uid) {
        this.bbcode_uid = bbcode_uid;
    }

    public String getpost_subject() {
        return post_subject;
    }
    public void setpost_subject(String post_subject) {
        this.post_subject = post_subject;
    }

    public String getpost_text() {
        return post_text;
    }
    public void setpost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_posts_textSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>bbcode_uid</Name>\n");
        xml.append("        <Value>").append(String.valueOf(bbcode_uid)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_subject</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_subject)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>post_text</Name>\n");
        xml.append("        <Value>").append(String.valueOf(post_text)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_posts_textSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_posts_textBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_posts_textBeans.iterator();
        xml.append("<phpbb_posts_textSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbPostText objphpbb_posts_textBean = (PhpbbPostText)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_posts_textBean.post_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>bbcode_uid</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_posts_textBean.bbcode_uid)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_subject</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_posts_textBean.post_subject)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>post_text</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_posts_textBean.post_text)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_posts_textSection>\n");
        return xml.toString();
    }
} //end of class phpbb_posts_textBean
