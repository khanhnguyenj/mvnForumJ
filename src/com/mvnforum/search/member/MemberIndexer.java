/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/member/MemberIndexer.java,v 1.33 2009/10/29 11:15:27 minhnn Exp $
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
package com.mvnforum.search.member;

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
import com.mvnforum.db.MemberBean;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

public class MemberIndexer {
    
    private static final Logger log = LoggerFactory.getLogger(MemberIndexer.class);

    //Field names (used for indexing)
    public static final String FIELD_MEMBER_ID      = "MemberID";
    public static final String FIELD_MEMBER_NAME    = "MemberName";
    public static final String FIELD_CREATION_DATE  = "MemberCreationDate";
    public static final String FIELD_MEMBER_EMAIL   = "MemberEmail";
    public static final String FIELD_MEMBER_LASTNAME    = "MemberLastName";
    public static final String FIELD_MEMBER_FIRSTNAME   = "MemberFirstName";
    public static final String FIELD_MEMBER_COUNTRY = "MemberCountry";

    //public static final String PROPERTY_SEARCH_PATH      = "search.path";
    //public static final String PROPERTY_SEARCH_AUTOINDEX = "search.autoindex";

    //Timer is used for scheduling jobs
    private static Analyzer analyzer;

    private static long lastOptimizeTime = 0;

    static {
        initializeAnalyzer();
    }
    
    private MemberIndexer() {
        // to prevent creating an instance
    }

    public static void scheduleAddMemberTask(MemberBean memberBean) {
        AddUpdateMemberIndexTask task = new AddUpdateMemberIndexTask(memberBean, AddUpdateMemberIndexTask.OPERATION_ADD);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleUpdateMemberTask(MemberBean memberBean) {
        AddUpdateMemberIndexTask task = new AddUpdateMemberIndexTask(memberBean, AddUpdateMemberIndexTask.OPERATION_UPDATE);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleDeleteMemberTask(int objectID) {
        DeleteMemberIndexTask task = new DeleteMemberIndexTask(objectID);
        TimerUtil.getInstance().schedule(task, 0);
    }

    public static void scheduleRebuildIndexTask() {
        int maxMemberID = 0;
        RebuildMemberIndexTask task = new RebuildMemberIndexTask(maxMemberID);
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
     * or add Member to existing index. Creating new index will delete previous so it
     * should be used for rebuilding index.
     * @param create - true if new index should be created.
     *               - false for adding members to existing index
     * @return IndexWriter object that is used for adding members to index
     */
    static IndexWriter getIndexWriter(Directory directory, boolean create) throws SearchException {

        IndexWriter writer = null;

        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        //If create = false, we will create IndexWriter with false argument
        if (create == false) {
            try {
                writer = new IndexWriter(directory, analyzer, false, IndexWriter.MaxFieldLength.LIMITED);
                if (service.saveMemberOnDisk()) {
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
            if (service.saveMemberOnDisk()) {
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
     * This method is used for adding single Member to index
     * Note: this method does not close the writer
     * @param MemberBean A Member that should be indexed
     * @param writer IndexWriter that is used for storing
     * @throws SearchException
     */
    static void doIndexMember(MemberBean memberBean, IndexWriter writer) throws SearchException {
        if (memberBean == null) {
            return;
        }
        if ( (memberBean.getMemberName() == null) || memberBean.getMemberName().equals("") ||
             (memberBean.getMemberCreationDate() == null) ) {
            return;
        }

        //Each Member will be represented as a document
        Document memberDocument = new Document();
        //Document has following fields that could be queried on
        memberDocument.add(new Field(FIELD_MEMBER_ID, Integer.toString(memberBean.getMemberID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        memberDocument.add(new Field(FIELD_MEMBER_NAME, memberBean.getMemberName(), Field.Store.NO, Field.Index.ANALYZED));
        memberDocument.add(new Field(FIELD_MEMBER_EMAIL, memberBean.getMemberEmail(), Field.Store.NO, Field.Index.ANALYZED));
        memberDocument.add(new Field(FIELD_MEMBER_LASTNAME, memberBean.getMemberLastname(), Field.Store.NO, Field.Index.ANALYZED));
        memberDocument.add(new Field(FIELD_MEMBER_FIRSTNAME, memberBean.getMemberFirstname(), Field.Store.NO, Field.Index.ANALYZED));
        memberDocument.add(new Field(FIELD_MEMBER_COUNTRY, memberBean.getMemberCountry(), Field.Store.NO, Field.Index.ANALYZED));
        memberDocument.add(new Field(FIELD_CREATION_DATE, DateTools.dateToString(memberBean.getMemberCreationDate(), Resolution.MILLISECOND), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        //now we have created document with fields so we can store it
        try {
            writer.addDocument(memberDocument);
        } catch (IOException e) {
            log.error("MemberIndexer.doIndexMember failed", e);
            //@todo : localize me
            throw new SearchException("Error writing new member to index");
        } catch (Throwable e) {
            log.error("MemberIndexer.doIndexMember failed", e);
            //@todo : localize me
            throw new SearchException("Error writing new member to index");
        }
    }

    /**
     * Add single Member to index
     * @param MemberBean
     * @throws SearchException
     */
    static void addMemberToIndex(MemberBean memberBean) throws SearchException, IOException {

        Directory directory = null;
        IndexWriter writer = null;
        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        try {
            directory = service.getSearchMemberIndexDir();
            writer = getIndexWriter(directory, false);
            if (writer == null) {
                log.warn("Cannot get the IndexWriter");
                return;
            }
            doIndexMember(memberBean, writer);

            // now check if we should optimize index (each hour)
            long now = System.currentTimeMillis();
            long timeFromLastOptimize = now - lastOptimizeTime;            
            if (service.saveMemberOnDisk() && (timeFromLastOptimize > DateUtil.HOUR)) {
                log.debug("writer.optimize() called in addMemberToIndex");
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
     * This method is used for deleting Member from index.
     * @param MemberID id of the Member that should be deleted
     * @throws SearchException
     */
    static void deleteMemberFromIndex(int memberID) throws SearchException {

        Directory directory = null;
        IndexReader reader = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchMemberIndexDir();
            reader = IndexReader.open(directory, false/*readOnly*/);
            if (reader == null) {
                log.warn("Cannot get the IndexReader");
                return;
            }

            Term term = new Term(FIELD_MEMBER_ID, String.valueOf(memberID));
            int deletedCount = reader.deleteDocuments(term);
            log.debug("deleteMemberFromIndex: deleted member = " + deletedCount);
        } catch (IOException e) {
            //@todo : localize me
            throw new SearchException("Error trying to delete Member with memberID = " + memberID);
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
            directory = service.getSearchMemberIndexDir();
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
