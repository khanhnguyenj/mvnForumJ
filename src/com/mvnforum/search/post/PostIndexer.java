/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/post/PostIndexer.java,v 1.33 2009/10/29 11:15:27 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.33 $
 * $Date: 2009/10/29 11:15:27 $
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
package com.mvnforum.search.post;

import java.io.IOException;

import net.myvietnam.mvncore.exception.SearchException;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.TimerUtil;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumFactoryConfig;
import com.mvnforum.db.PostBean;
import com.mvnforum.search.IntegerFilter;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

public class PostIndexer {
    
    private static final Logger log = LoggerFactory.getLogger(PostIndexer.class);

    //Field names (used for indexing)
    public static final String FIELD_POST_ID    = "postID";
    public static final String FIELD_THREAD_ID  = "threadID";
    public static final String FIELD_FORUM_ID   = "forumID";
    public static final String FIELD_MEMBER_ID  = "memberID";
    public static final String FIELD_POST_TOPIC = "postTopic";
    public static final String FIELD_POST_BODY  = "postBody";
    public static final String FIELD_POST_DATE  = "postDate";

    public static final String FIELD_WITH_ATTACHMENT = "withAttachment";

    public static final String FIELD_ATTACHMENT_COUNT = "attachmentCount";

    //public static final String PROPERTY_SEARCH_PATH      = "search.path";
    //public static final String PROPERTY_SEARCH_AUTOINDEX = "search.autoindex";

    //Timer is used for scheduling jobs
    private static Analyzer analyzer;

    private static long lastOptimizeTime = 0;

    static {
        initializeAnalyzer();
    }

    private PostIndexer() {
        // to prevent creating an instance
    }
    
    public static void scheduleAddPostTask(PostBean postBean) {
        AddUpdatePostIndexTask task = new AddUpdatePostIndexTask(postBean, AddUpdatePostIndexTask.OPERATION_ADD);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleUpdatePostTask(PostBean postBean) {
        AddUpdatePostIndexTask task = new AddUpdatePostIndexTask(postBean, AddUpdatePostIndexTask.OPERATION_UPDATE);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleDeletePostTask(int objectID, int objectType) {
        DeletePostIndexTask task = new DeletePostIndexTask(objectID, objectType);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleUpdateThreadTask(int threadID) {
        UpdateThreadIndexTask task = new UpdateThreadIndexTask(threadID);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleRebuildIndexTask() {
        int maxPostID = 0;
        RebuildPostIndexTask task = new RebuildPostIndexTask(maxPostID);
        TimerUtil.getInstance().schedule(task, 0);
    }

    static Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * This class will load analyzer when starting. If specified analyzer class
     * cannot be loaded then default analyzer will be used.
     */
    private static void initializeAnalyzer() {
        String analyzerClassName = MVNForumFactoryConfig.getLuceneAnalyzerClassName();
        if ( (analyzerClassName == null) || (analyzerClassName.equals("")) ) {
            //create standard analyzer
            //String[] stopWords = this.loadStopWords();
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            log.debug("Using StandardAnalyzer for indexing");
        } else {
            //try to create specified analyzer
            try {
                log.debug("About to load Analyzer [" + analyzerClassName + "] for indexing");
                analyzer = (Analyzer) Class.forName(analyzerClassName).newInstance();
            } catch (Exception e) {
                log.warn("Cannot load " + analyzerClassName + ". Loading StandardAnalyzer");
                analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            }
        }
    }

    /**
     * This method is used for getting new IndexWriter. It can create new index
     * or add post to existing index. Creating new index will delete previous so it
     * should be used for rebuilding index.
     * @param create - true if new index should be created.
     *               - false for adding posts to existing index
     * @return IndexWriter object that is used for adding posts to index
     */
    static IndexWriter getIndexWriter(Directory directory, boolean create) throws SearchException {

        IndexWriter writer = null;

        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        //If create = false, we will create IndexWriter with false argument
        if (create == false) {
            try {
                writer = new IndexWriter(directory, analyzer, false, IndexWriter.MaxFieldLength.LIMITED);
                //writer.setWriteLockTimeout(100000);
                if (service.savePostOnDisk()) {
                    writer.setUseCompoundFile(true);
                } else {
                    writer.setUseCompoundFile(false);
                }
                return writer;
            } catch (IOException e) {
                log.warn("Cannot open existed index. New index will be created.", e);
                //Ignore Exception. We will try to create index with true parameter
            }
        }
        // We are here in two cases: We wanted to create new index or because
        // index doesn't existed
        try {
            //This will create new index and delete existing
            service.deleteContent(directory);
            writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);// actually the directory should be 'create' = true
            //writer.setWriteLockTimeout(100000);
            if (service.savePostOnDisk()) {
                writer.setUseCompoundFile(true);
            } else {
                writer.setUseCompoundFile(false);
            }
            return writer;
        } catch (IOException e) {
            //TODO : localize me
            log.error("IOException during get index writer", e);
            throw new SearchException("Error while creating index writer");
        }
    }

    /**
     * This method is used for adding single post to index
     * Note: this method does not close the writer
     * @param post A post that should be indexed
     * @param writer IndexWriter that is used for storing
     * @throws SearchException
     */
    static void doIndexPost(PostBean post, IndexWriter writer) throws SearchException {

        if (post == null) {
            return;
        }
        //Post must include topic and body. If not then we have nothing to index.
        if ( (post.getPostTopic() == null || post.getPostTopic().equals("")) ||
             (post.getPostBody() == null || post.getPostBody().equals(""))) {
            return;
        }

        //Each post will be represented as a document
        Document postDocument = new Document();
        //Document has following fields that could be queried on
        postDocument.add(new Field(FIELD_POST_ID, Integer.toString(post.getPostID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        postDocument.add(new Field(FIELD_THREAD_ID, Integer.toString(post.getThreadID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        postDocument.add(new Field(FIELD_FORUM_ID, Integer.toString(post.getForumID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        postDocument.add(new Field(FIELD_MEMBER_ID, Integer.toString(post.getMemberID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        //postDocument.add(new Field(FIELD_WITH_ATTACHMENT, Boolean.valueOf(post.getPostAttachCount()>0).toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));// make it compilable on JDK 1.3
        postDocument.add(new Field(FIELD_ATTACHMENT_COUNT, IntegerFilter.intToString(post.getPostAttachCount()), Field.Store.YES, Field.Index.NOT_ANALYZED));

        //document body and title is not stored since we can retrieve them from database
        postDocument.add(new Field(FIELD_POST_TOPIC, post.getPostTopic(), Field.Store.NO, Field.Index.ANALYZED));
        postDocument.add(new Field(FIELD_POST_BODY, post.getPostBody(), Field.Store.NO, Field.Index.ANALYZED));
        //add date field
        postDocument.add(new Field(FIELD_POST_DATE, DateTools.dateToString(post.getPostCreationDate(), Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED));

        //now we have created document with fields so we can store it
        try {
            writer.addDocument(postDocument);
        } catch (IOException e) {
            log.error("PostIndexer.doIndexPost failed", e);
            //@todo : localize me
            throw new SearchException("Error writing new post to index");
        } catch (Throwable e) {
            log.error("PostIndexer.doIndexPost failed", e);
            //@todo : localize me
            throw new SearchException("Error writing new post to index");
        }
    }

    /**
     * Add single post to index
     * @param post
     * @throws SearchException
     */
    static void addPostToIndex(PostBean post) throws SearchException, IOException {

        Directory directory = null;
        IndexWriter writer = null;
        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        try {
            directory = service.getSearchPostIndexDir();
            writer = getIndexWriter(directory, false);
            if (writer == null) {
                log.warn("Cannot get the IndexWriter");
                return;
            }
            doIndexPost(post, writer);

            // now check if we should optimize index (each hour)
            long now = System.currentTimeMillis();
            long timeFromLastOptimize = now - lastOptimizeTime;
            if (service.savePostOnDisk() && (timeFromLastOptimize > DateUtil.HOUR)) {
                log.debug("writer.optimize() called in addPostToIndex");
                writer.optimize();
                lastOptimizeTime = now;
            }
        } catch (SearchException ex) {
            throw ex;
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
    }

    /**
     * This method is used for deleting post from index.
     * @param postID id of the post that should be deleted
     * @throws SearchException
     */
    static void deletePostFromIndex(int postID) throws SearchException {

        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            reader = IndexReader.open(directory, false/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return;
            }

            Term term = new Term(FIELD_POST_ID, String.valueOf(postID));
            int deletedCount = reader.deleteDocuments(term);
            log.debug("deletePostFromIndex: deleted posts = " + deletedCount);
        } catch (IOException e) {
            //@todo : localize me
            throw new SearchException("Error trying to delete post with postID = " + postID);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Error closing Lucene IndexReader", e);
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
    }

    /**
     * This method is used for deleting all posts in a thread from index.
     * @param threadID id of the thread that should be deleted
     * @throws SearchException
     */
    static void deleteThreadFromIndex(int threadID) throws SearchException {

        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            reader = IndexReader.open(directory, false/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return;
            }

            Term term = new Term(FIELD_THREAD_ID, String.valueOf(threadID));
            int deletedCount = reader.deleteDocuments(term);
            log.debug("deleteThreadFromIndex: deleted posts = " + deletedCount);
        } catch (IOException e) {
            //@todo : localize me
            throw new SearchException("Error trying to delete posts in index with threadID = " + threadID);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Error closing Lucene IndexReader", e);
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
    }

    /**
     * This method is used for deleting all posts in a forum from index.
     * @param forumID id of the forum that should be deleted
     * @throws SearchException
     */
    static void deleteForumFromIndex(int forumID) throws SearchException {

        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            reader = IndexReader.open(directory, false/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return;
            }

            Term term = new Term(FIELD_FORUM_ID, String.valueOf(forumID));
            int deletedCount = reader.deleteDocuments(term);
            log.debug("deleteForumFromIndex: deleted posts = " + deletedCount);
        } catch (IOException e) {
            //@todo : localize me
            throw new SearchException("Error trying to delete posts in index with forumID = " + forumID);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Error closing Lucene IndexReader", e);
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
    }

    public static int getNumDocs() {

        int numDocs = -1;
        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            reader = IndexReader.open(directory, true/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return -1;
            }
            numDocs = reader.numDocs();
        } catch (IOException ioe) {
            //ignore
            ioe.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Error closing Lucene IndexReader", e);
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
        return numDocs;
    }

}
