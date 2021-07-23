/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/PhpbbRanks.java,v 1.3 2007/01/15 10:27:33 dungbtm Exp $
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

import java.util.Collection;
import java.util.Iterator;

/*
 * Included columns: rank_id, rank_title, rank_min, rank_special, rank_image
 * Excluded columns: 
 */
public class PhpbbRanks {
    private int rank_id;

    private String rank_title;

    private int rank_min;

    private int rank_special;

    private String rank_image;

    public int getrank_id() {
        return rank_id;
    }

    public void setrank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public String getrank_title() {
        return rank_title;
    }

    public void setrank_title(String rank_title) {
        this.rank_title = rank_title;
    }

    public int getrank_min() {
        return rank_min;
    }

    public void setrank_min(int rank_min) {
        this.rank_min = rank_min;
    }

    public int getrank_special() {
        return rank_special;
    }

    public void setrank_special(int rank_special) {
        this.rank_special = rank_special;
    }

    public String getrank_image() {
        return rank_image;
    }

    public void setrank_image(String rank_image) {
        this.rank_image = rank_image;
    }

    public String getXML() {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<phpbb_ranksSection>\n");
        xml.append("  <Rows>\n");
        xml.append("    <Row>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>rank_id</Name>\n");
        xml.append("        <Value>").append(String.valueOf(rank_id)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>rank_title</Name>\n");
        xml.append("        <Value>").append(String.valueOf(rank_title)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>rank_min</Name>\n");
        xml.append("        <Value>").append(String.valueOf(rank_min)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>rank_special</Name>\n");
        xml.append("        <Value>").append(String.valueOf(rank_special)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("      <Column>\n");
        xml.append("        <Name>rank_image</Name>\n");
        xml.append("        <Value>").append(String.valueOf(rank_image)).append("</Value>\n");
        xml.append("      </Column>\n");
        xml.append("    </Row>\n");
        xml.append("  </Rows>\n");
        xml.append("</phpbb_ranksSection>\n");
        return xml.toString();
    }

    public static String getXML(Collection objphpbb_ranksBeans) {
        StringBuffer xml = new StringBuffer(1024);
        Iterator iterator = objphpbb_ranksBeans.iterator();
        xml.append("<phpbb_ranksSection>\n");
        xml.append("  <Rows>\n");
        while (iterator.hasNext()) {
            PhpbbRanks objphpbb_ranksBean = (PhpbbRanks) iterator.next();
            xml.append("    <Row>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>rank_id</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_ranksBean.rank_id)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>rank_title</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_ranksBean.rank_title)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>rank_min</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_ranksBean.rank_min)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>rank_special</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_ranksBean.rank_special)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("      <Column>\n");
            xml.append("        <Name>rank_image</Name>\n");
            xml.append("        <Value>").append(String.valueOf(objphpbb_ranksBean.rank_image)).append("</Value>\n");
            xml.append("      </Column>\n");
            xml.append("    </Row>\n");
        }//while
        xml.append("  </Rows>\n");
        xml.append("</phpbb_ranksSection>\n");
        return xml.toString();
    }
} //end of class phpbb_ranksBean

