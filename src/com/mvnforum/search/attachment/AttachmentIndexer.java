/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/attachment/AttachmentIndexer.java,v 1.18 2009/10/29 11:15:27 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.18 $
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
package com.mvnforum.search.attachment;

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
import com.mvnforum.db.AttachmentBean;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

public class AttachmentIndexer {
    
    private static final Logger log = LoggerFactory.getLogger(AttachmentIndexer.class);

    //Field names (used for indexing)
    public static final String FIELD_ATTACHMENT_ID             = "AttachmentID";
    public static final String FIELD_ATTACHMENT_DESCRIPTION    = "AttachmentDescription";
    public static final String FIELD_ATTACHMENT_NAME           = "AttachmentName";
    public static final String FIELD_POST_ID                   = "PostID";
    public static final String FIELD_FORUM_ID                  = "ForumID";
    public static final String FIELD_ATTACHMENT_DATE           = "AttachmentDate";

    private static Analyzer analyzer;

    private static long lastOptimizeTime = 0;

    static {
        initializeAnalyzer();
    }
    
    private AttachmentIndexer() {
        // to prevent creating an instance
    }

    public static void scheduleAddAttachmentTask(AttachmentBean atachmentBean) {
        AddUpdateAttachmentIndexTask task = new AddUpdateAttachmentIndexTask(atachmentBean, AddUpdateAttachmentIndexTask.OPERATION_ADD);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleUpdateAttachmentTask(AttachmentBean attachmentBean) {
        AddUpdateAttachmentIndexTask task = new AddUpdateAttachmentIndexTask(attachmentBean, AddUpdateAttachmentIndexTask.OPERATION_UPDATE);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleDeleteAttachmentTask(int objectID) {
        DeleteAttachmentIndexTask task = new DeleteAttachmentIndexTask(objectID);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleRebuildIndexTask() {
        int maxAttachmentID = 0;
        RebuildAttachmentIndexTask task = new RebuildAttachmentIndexTask(maxAttachmentID);
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
     * or add Attachment to existing index. Creating new index will delete previous so it
     * should be used for rebuilding index.
     * @param create - true if new index should be created.
     *               - false for adding attachments to existing index
     * @return IndexWriter object that is used for adding attachments to index
     */
    static IndexWriter getIndexWriter(Directory directory, boolean create) throws SearchException {

        IndexWriter writer = null;

        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        //If create = false, we will create IndexWriter with false argument
        if (create == false) {
            try {
                writer = new IndexWriter(directory, analyzer, false, IndexWriter.MaxFieldLength.LIMITED);
                if (service.saveAttachmentOnDisk()) {
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
            if (service.saveAttachmentOnDisk()) {
                writer.setUseCompoundFile(true);
            } else {
                writer.setUseCompoundFile(false);
            }
            return writer;
        } catch (IOException e) {
            //@todo : localize me
            log.error("IOException during get index writer", e);
            throw new SearchException("Error while creating index writer");
        }
    }

    /**
     * This method is used for adding single Attachment to index
     * Note: this method does not close the writer
     * @param AttachmentBean A Attachment that should be indexed
     * @param writer IndexWriter that is used for storing
     * @throws SearchException
     */
    static void doIndexAttachment(AttachmentBean attachmentBean, IndexWriter writer) throws SearchException {
        
        if (attachmentBean == null) {
            return;
        }
        if ( (attachmentBean.getAttachFilename() == null) || attachmentBean.getAttachFilename().equals("") ||
             (attachmentBean.getAttachCreationDate() == null) ) {
            return;
        }

        //Each Attachment will be represented as a document
        Document attachmentDocument = new Document();
        //Document has following fields that could be queried on
        attachmentDocument.add(new Field(FIELD_ATTACHMENT_ID, Integer.toString(attachmentBean.getAttachID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        attachmentDocument.add(new Field(FIELD_POST_ID, Integer.toString(attachmentBean.getPostID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        attachmentDocument.add(new Field(FIELD_FORUM_ID, Integer.toString(attachmentBean.getForumID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        attachmentDocument.add(new Field(FIELD_ATTACHMENT_NAME, attachmentBean.getAttachFilename(), Field.Store.YES, Field.Index.ANALYZED));
        attachmentDocument.add(new Field(FIELD_ATTACHMENT_DESCRIPTION, attachmentBean.getAttachDesc(), Field.Store.NO, Field.Index.ANALYZED));
        attachmentDocument.add(new Field(FIELD_ATTACHMENT_DATE, DateTools.dateToString(attachmentBean.getAttachCreationDate(), Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED));

        //now we have created document with fields so we can store it        
        try {
            writer.addDocument(attachmentDocument);
        } catch (IOException e) {
            log.error("AtachmentIndexer.doIndexAtachment failed", e);
         //@todo : localize me
            throw new SearchException("Error writing new attachment to index");
        } catch (Throwable e) {
            log.error("AtachmentIndexer.doIndexAtachment failed", e);
            //@todo : localize me
            throw new SearchException("Error writing new Atachment to index");
        }
    }

    /**
     * Add single Attachment to index
     * @param AttachmentBean
     * @throws SearchException
     */
    static void addAttachmentToIndex(AttachmentBean attachmentBean) throws SearchException, IOException {
        
        Directory directory = null;
        IndexWriter writer = null;
        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        try {
            directory = service.getSearchAttachmentIndexDir();
            writer = getIndexWriter(directory, false);
            if (writer == null) {
                log.warn("Cannot get the IndexWriter");
                return;
            }
            doIndexAttachment(attachmentBean, writer);

            // now check if we should optimize index (each hour)
            long now = System.currentTimeMillis();
            long timeFromLastOptimize = now - lastOptimizeTime;

            if (service.saveAttachmentOnDisk() && (timeFromLastOptimize > DateUtil.HOUR)) {
                log.debug("writer.optimize() called in addAttachmentToIndex");
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
     * This method is used for deleting Attachment from index.
     * @param AttachmentID is id of the Attachment that should be deleted
     * @throws SearchException
     */
    static void deleteAttachmentFromIndex(int attachmentID) throws SearchException {

        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchAttachmentIndexDir();
            reader = IndexReader.open(directory, false/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return;
            }

            Term term = new Term(FIELD_ATTACHMENT_ID, String.valueOf(attachmentID));
            int deletedCount = reader.deleteDocuments(term);
            log.debug("deleteAttachmentFromIndex: deleted attachment = " + deletedCount);
        } catch (IOException e) {
            //@todo : localize me
            throw new SearchException("Error trying to delete Attachment with attachmentID = " + attachmentID);
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
            directory = service.getSearchAttachmentIndexDir();
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
