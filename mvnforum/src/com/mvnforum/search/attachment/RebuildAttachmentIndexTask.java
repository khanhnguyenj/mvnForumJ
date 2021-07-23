/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/attachment/RebuildAttachmentIndexTask.java,v 1.12 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
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
 */
package com.mvnforum.search.attachment;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.AttachmentBean;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

/**
 * Rebuilding index task. This task do indexing of all documents
 */
public class RebuildAttachmentIndexTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(RebuildAttachmentIndexTask.class);

    public static final int ATTACHMENTS_PER_FETCH = 200;

    public static final int MERGE_FACTOR  = 20;

    private int maxAttachmentID = 0;

    private static boolean isRebuilding = false;

    public static boolean isRebuilding() {
        return isRebuilding;
    }

    /*
     * Constructor with default access, prevent new an instance from outside package
     */
    RebuildAttachmentIndexTask(int maxAttachmentID) {
        this.maxAttachmentID = maxAttachmentID;
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
            directory = service.getSearchAttachmentIndexDir();
            writer = AttachmentIndexer.getIndexWriter(directory, true);
            writer.setMergeFactor(MERGE_FACTOR);
            // note that the maxAttachmentID is got at the beginning of the method
            // so that it will index only these attachments. Later while indexing,
            // if new attachments are added, then other task will take care it
            if (maxAttachmentID <= 0) {
                maxAttachmentID = DAOFactory.getAttachmentDAO().getMaxAttachmentID();
            }
            int count = 0;

            for (int fromID = 0; fromID <= maxAttachmentID /* <= is correct */; fromID += ATTACHMENTS_PER_FETCH) {
                int toID = fromID + ATTACHMENTS_PER_FETCH - 1;
                if (toID > maxAttachmentID) {
                    toID = maxAttachmentID;
                }
                Collection attachments = DAOFactory.getAttachmentDAO().getAttachments_fromIDRange(fromID, toID);

                for (Iterator iter = attachments.iterator(); iter.hasNext(); ) {
                    AttachmentBean attachment = (AttachmentBean) iter.next();
                    AttachmentIndexer.doIndexAttachment(attachment, writer);
                    count++;
                }
            } //end for

            writer.optimize();
            log.info("Rebuilt index finished successfully! " + count + " attachment(s) indexed.");
        } catch (DatabaseException ex) {
            log.error("RebuildAttachmentIndexTask.run : cannot get attachments from database for indexing", ex);
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
        log.info("RebuildAttachmentIndexTask took " + (System.currentTimeMillis() - start) + " ms");
        isRebuilding = false;
    }

}
