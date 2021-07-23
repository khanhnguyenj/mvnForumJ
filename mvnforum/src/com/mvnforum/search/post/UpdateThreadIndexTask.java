/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/post/UpdateThreadIndexTask.java,v 1.6 2009/01/02 18:31:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/01/02 18:31:46 $
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
package com.mvnforum.search.post;

import java.io.IOException;
import java.util.*;

import net.myvietnam.mvncore.exception.SearchException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.PostBean;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

/**
 * Update a thread task. This task do indexing of all post in the thread
 */
public class UpdateThreadIndexTask extends TimerTask {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateThreadIndexTask.class);

    private int threadID = 0;

    /*
     * Constructor with default access, prevent new an instance from outside package
     */
    UpdateThreadIndexTask(int threadID) {
        this.threadID = threadID;
    }

    /**
     * Delete all posts in the thread first, then add it again
     */
    public void run() {
        long start = System.currentTimeMillis();

        Collection posts = null;
        try {
            //hard code 10000 post to get. is it enough???
            posts = DAOFactory.getPostDAO().getEnablePosts_inThread_limit(threadID, 0, 10000);
        } catch (Exception ex) {
            log.error("UpdateThreadIndexTask.run : cannot get all posts from a thread (" + threadID + ") for indexing", ex);
        }

        if (posts == null) {
            return;
        }

        // First, delete all posts from the thread
        try {
            PostIndexer.deleteThreadFromIndex(threadID);
        } catch (SearchException e) {
            log.error("Cannot delete thread from lucene index.", e);
            // now we should not continue, so we return
            log.info("UpdateThreadIndexTask failed. Took " + (System.currentTimeMillis() - start) + " ms");
            return;
        }

        // then add all posts of the thread
        Directory directory = null;
        IndexWriter writer = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            writer = PostIndexer.getIndexWriter(directory, false);

            int i = 0;
            for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
                PostBean post = (PostBean) iter.next();
                PostIndexer.doIndexPost(post, writer);
                i++;
            }
            writer.optimize();
            log.info("Updating thread finished successfully! " + i + " post(s) indexed.");
        } catch (Exception e) {
            log.error("Error while updating thread.", e);
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
        log.info("UpdateThreadIndexTask took " + (System.currentTimeMillis() - start) + " ms");
    }
    
}
