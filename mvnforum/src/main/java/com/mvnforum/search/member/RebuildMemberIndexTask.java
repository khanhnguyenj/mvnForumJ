/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/member/RebuildMemberIndexTask.java,v 1.21 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.21 $
 * $Date: 2009/01/02 15:12:55 $
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
 * @author: Minh Nguyen
 * @author: Dejan Krsmanovic dejan_krsmanovic@yahoo.com
 */
package com.mvnforum.search.member;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

/**
 * Rebuilding index task. This task do indexing of all documents
 */
public class RebuildMemberIndexTask extends TimerTask {
    
    private static final Logger log = LoggerFactory.getLogger(RebuildMemberIndexTask.class);

    public static final int MEMBERS_PER_FETCH = 200;

    public static final int MERGE_FACTOR  = 20;

    private int maxMemberID = 0;

    private static boolean isRebuilding = false;

    public static boolean isRebuilding() {
        return isRebuilding;
    }

    /*
     * Constructor with default access, prevent new an instance from outside package
     */
    RebuildMemberIndexTask(int maxMemberID) {
        this.maxMemberID = maxMemberID;
    }

    /**
     * Create new index. If anything exist already - delete it
     */
    public void run() {
        isRebuilding = true;
        long start = System.currentTimeMillis();

        Directory directory = null;
        IndexWriter writer = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchMemberIndexDir();
            writer = MemberIndexer.getIndexWriter(directory, true);
            writer.setMergeFactor(MERGE_FACTOR);
            // note that the maxMemberID is got at the beginning of the method
            // so that it will index only these members. Later while indexing,
            // if new members are added, then other task will take care it
            if (maxMemberID <= 0) {
                maxMemberID = DAOFactory.getMemberDAO().getMaxMemberID();
            }
            // we index it even when there are not any members, this will
            // create an empty search index
            //if (members.size() == 0) return; ??
            int count = 0;

            for (int fromID = 0; fromID <= maxMemberID /* <= is correct */; fromID += MEMBERS_PER_FETCH) {
                int toID = fromID + MEMBERS_PER_FETCH - 1;
                if (toID > maxMemberID) {
                    toID = maxMemberID;
                }
                Collection members = DAOFactory.getMemberDAO().getMembers_fromIDRange(fromID, toID);

                for (Iterator iter = members.iterator(); iter.hasNext(); ) {
                    MemberBean memberBean = (MemberBean) iter.next();
                    MemberIndexer.doIndexMember(memberBean, writer);
                    count++;
                }
            } //end for

            writer.optimize();
            log.info("Rebuilt index finished successfully! " + count + " member(s) indexed.");
        } catch (DatabaseException e) {
            log.error("RebuildMemberIndexTask.run : cannot get members from database for indexing", e);
        } catch (Throwable e) {
            log.error("Error while rebuilding index", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.debug("Error closing Lucene IndexWriter", e);
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    log.debug("Cannot close directory.", e);
                }
            }
        }
        log.info("RebuildMemberIndexTask took " + (System.currentTimeMillis() - start) + " ms");
        isRebuilding = false;
    }

}
