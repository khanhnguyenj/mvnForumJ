/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/RankXML.java,v 1.13 2009/01/06 18:31:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/01/06 18:31:31 $
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
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.RankDAO;

/**
 * @author Igor Manic
 * @version $Revision: 1.13 $, $Date: 2009/01/06 18:31:31 $
 * <br/>
 * <code>RankXML</code> todo Igor: enter description
 *
 */
public class RankXML {

    public RankXML() {
        rankID = -1;
    }

    private int rankID;
    /** Returns <code>RankID</code> of this rank or
      * <code>-1</code> if rank is not created yet. */
    public int getRankID() { return rankID; }

    public void setRankID(String id) {
        rankID=XMLUtil.stringToIntDef(id, -1);
    }

    /**
     * Creates a rank. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param rankMinPosts Minimal number of posts needed to achieve this rank.
     * @param rankLevel Can be null.
     * @param rankTitle Title of this rank ("Stranger", "Newbie", ...).
     * @param rankImage Can be null.
     * @param rankType Can be null.
     * @param rankOption Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addRank(String rankMinPosts, String rankLevel,
                        String rankTitle, String rankImage,
                        String rankType, String rankOption)
        throws CreateException, DuplicateKeyException,
        ObjectNotFoundException, DatabaseException {

        if ((rankMinPosts==null) || (rankMinPosts.length()<=0) ||
            (rankTitle==null) || (rankTitle.length()<=0)) {
            throw new CreateException("Not enough data to create a rank.");
        } else {
            int rankMinPosts1;
            int rankLevel1;
            int rankType1;
            int rankOption1;
            try {
                rankMinPosts1= XMLUtil.stringToIntDef(rankMinPosts, 0);
                rankLevel1= XMLUtil.stringToIntDef(rankLevel, 0);
                if (rankImage == null) {
                    rankImage = "";
                }
                rankType1= XMLUtil.stringToIntDef(rankType, 0);
                rankOption1= XMLUtil.stringToIntDef(rankOption, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a rank. Expected a number.");
            }

            rankTitle=EnableHtmlTagFilter.filter(rankTitle);
            rankImage=EnableHtmlTagFilter.filter(rankImage);
            DAOFactory.getRankDAO().create(rankMinPosts1, rankLevel1,
                                     rankTitle, rankImage,
                                     rankType1, rankOption1);

            this.rankID = DAOFactory.getRankDAO().getRankIDFromRankTitle(rankTitle);
        }
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================
    public static void exportRankList(XMLWriter xmlWriter)
        throws IOException, ExportException, DatabaseException {

        Collection ranks=ExportWebHelper.execSqlQuery(
                   "SELECT RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption"+
                   " FROM "+RankDAO.TABLE_NAME);
        Iterator iter=ranks.iterator();
        String[] rank=null;
        //try {
            xmlWriter.startElement("RankList");
            try {
                while ( (rank=(String[])iter.next()) !=null) {
                    if (rank.length!=6) {
                        throw new ExportException("Error while retrieving list of ranks.");
                    }
                    xmlWriter.startElement("Rank");
                    xmlWriter.startElement("RankMinPosts");
                    xmlWriter.writeData(rank[0]);
                    xmlWriter.endElement("RankMinPosts");
                    xmlWriter.startElement("RankLevel");
                    xmlWriter.writeData(rank[1]);
                    xmlWriter.endElement("RankLevel");
                    xmlWriter.startElement("RankTitle");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(rank[2]));
                    xmlWriter.endElement("RankTitle");
                    xmlWriter.startElement("RankImage");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(rank[3]));
                    xmlWriter.endElement("RankImage");
                    xmlWriter.startElement("RankType");
                    xmlWriter.writeData(rank[4]);
                    xmlWriter.endElement("RankType");
                    xmlWriter.startElement("RankOption");
                    xmlWriter.writeData(rank[5]);
                    xmlWriter.endElement("RankOption");
                    xmlWriter.endElement("Rank");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("RankList");
         //} catch throw exportexception
    }
}
