/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbPrivmMsgs.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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
 * Included columns: privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, 
 *                   privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, 
 *                   privmsgs_attach_sig
 * Excluded columns: 
 */
public class PhpbbPrivmMsgs {
    private int privmsgs_id;
    private int privmsgs_type;
    private String privmsgs_subject;
    private int privmsgs_from_userid;
    private int privmsgs_to_userid;
    private long privmsgs_date;
    private String privmsgs_ip;
    private int privmsgs_enable_bbcode;
    private int privmsgs_enable_html;
    private int privmsgs_enable_smilies;
    private int privmsgs_attach_sig;

    public int getprivmsgs_id() {
        return privmsgs_id;
    }
    public void setprivmsgs_id(int privmsgs_id) {
        this.privmsgs_id = privmsgs_id;
    }

    public int getprivmsgs_type() {
        return privmsgs_type;
    }
    public void setprivmsgs_type(int privmsgs_type) {
        this.privmsgs_type = privmsgs_type;
    }

    public String getprivmsgs_subject() {
        return privmsgs_subject;
    }
    public void setprivmsgs_subject(String privmsgs_subject) {
        this.privmsgs_subject = privmsgs_subject;
    }

    public int getprivmsgs_from_userid() {
        return privmsgs_from_userid;
    }
    public void setprivmsgs_from_userid(int privmsgs_from_userid) {
        this.privmsgs_from_userid = privmsgs_from_userid;
    }

    public int getprivmsgs_to_userid() {
        return privmsgs_to_userid;
    }
    public void setprivmsgs_to_userid(int privmsgs_to_userid) {
        this.privmsgs_to_userid = privmsgs_to_userid;
    }

    public long getprivmsgs_date() {
        return privmsgs_date;
    }
    public void setprivmsgs_date(long privmsgs_date) {
        this.privmsgs_date = privmsgs_date;
    }

    public String getprivmsgs_ip() {
        return privmsgs_ip;
    }
    public void setprivmsgs_ip(String privmsgs_ip) {
        this.privmsgs_ip = privmsgs_ip;
    }

    public int getprivmsgs_enable_bbcode() {
        return privmsgs_enable_bbcode;
    }
    public void setprivmsgs_enable_bbcode(int privmsgs_enable_bbcode) {
        this.privmsgs_enable_bbcode = privmsgs_enable_bbcode;
    }

    public int getprivmsgs_enable_html() {
        return privmsgs_enable_html;
    }
    public void setprivmsgs_enable_html(int privmsgs_enable_html) {
        this.privmsgs_enable_html = privmsgs_enable_html;
    }

    public int getprivmsgs_enable_smilies() {
        return privmsgs_enable_smilies;
    }
    public void setprivmsgs_enable_smilies(int privmsgs_enable_smilies) {
        this.privmsgs_enable_smilies = privmsgs_enable_smilies;
    }

    public int getprivmsgs_attach_sig() {
        return privmsgs_attach_sig;
    }
    public void setprivmsgs_attach_sig(int privmsgs_attach_sig) {
        this.privmsgs_attach_sig = privmsgs_attach_sig;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_privmsgsSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_type</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_type)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_subject</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_subject)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_from_userid</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_from_userid)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_to_userid</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_to_userid)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_date</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_date)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_ip</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_ip)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_enable_bbcode</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_enable_bbcode)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_enable_html</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_enable_html)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_enable_smilies</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_enable_smilies)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>privmsgs_attach_sig</Name>\n");
        xml.append("        <Value>").append(String.valueOf(privmsgs_attach_sig)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_privmsgsSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_privmsgsBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_privmsgsBeans.iterator();
        xml.append("<phpbb_privmsgsSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbPrivmMsgs objphpbb_privmsgsBean = (PhpbbPrivmMsgs)iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_type</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_type)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_subject</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_subject)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_from_userid</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_from_userid)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_to_userid</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_to_userid)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_date</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_date)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_ip</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_ip)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_enable_bbcode</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_enable_bbcode)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_enable_html</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_enable_html)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_enable_smilies</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_enable_smilies)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>privmsgs_attach_sig</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_privmsgsBean.privmsgs_attach_sig)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_privmsgsSection>\n");
        return xml.toString();
    }
} //end of class phpbb_privmsgsBean
