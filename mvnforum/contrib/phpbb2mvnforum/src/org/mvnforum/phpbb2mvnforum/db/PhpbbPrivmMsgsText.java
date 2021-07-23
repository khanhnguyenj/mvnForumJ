/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbPrivmMsgsText.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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
 * Included columns: privmsgs_text_id, privmsgs_bbcode_uid, privmsgs_text
 * Excluded columns: 
 */
public class PhpbbPrivmMsgsText {
    private int privmsgs_text_id;
    private String privmsgs_bbcode_uid;
    private String privmsgs_text;

    public int getprivmsgs_text_id() {
        return privmsgs_text_id;
    }
    public void setprivmsgs_text_id(int privmsgs_text_id) {
        this.privmsgs_text_id = privmsgs_text_id;
    }

    public String getprivmsgs_bbcode_uid() {
        return privmsgs_bbcode_uid;
    }
    public void setprivmsgs_bbcode_uid(String privmsgs_bbcode_uid) {
        this.privmsgs_bbcode_uid = privmsgs_bbcode_uid;
    }

    public String getprivmsgs_text() {
        return privmsgs_text;
    }
    public void setprivmsgs_text(String privmsgs_text) {
        this.privmsgs_text = privmsgs_text;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_privmsgs_textSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_text_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_text_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_bbcode_uid</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_bbcode_uid)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_text</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_text)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_privmsgs_textSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_privmsgs_textBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_privmsgs_textBeans.iterator();
        xml.append("<phpbb_privmsgs_textSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbPrivmMsgsText objphpbb_privmsgs_textBean = (PhpbbPrivmMsgsText)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_text_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgs_textBean.privmsgs_text_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_bbcode_uid</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgs_textBean.privmsgs_bbcode_uid)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_text</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgs_textBean.privmsgs_text)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_privmsgs_textSection>\n");
        return xml.toString();
    }
} //end of class phpbb_privmsgs_textBean
