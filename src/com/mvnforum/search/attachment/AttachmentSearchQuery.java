/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/attachment/AttachmentSearchQuery.java,v 1.16 2009/12/05 08:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.16 $
 * $Date: 2009/12/05 08:31:30 $
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
import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.StringUtil;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.db.*;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

public class AttachmentSearchQuery {

    private static final Logger log = LoggerFactory.getLogger(AttachmentSearchQuery.class);

    // constant for search by time
    public static final int SEARCH_ANY_DATE = 0;

    public static final int SEARCH_NEWER    = 1;
    public static final int SEARCH_OLDER    = 2;

    private int forumId = 0;

    private String searchString = null;
    private String searchFileName = null;

    private Timestamp fromDate = null;
    private Timestamp toDate = null;

    private int hitCount = 0;
    private Collection searchResult = null;

    /**
     * Id of forum where post belongs. Set to -1 if all forums should be searched
     * @param forumId
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    /**
     * Set string that should be searched for.
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = StringUtil.escapeLuceneSpecialCharacters(searchString);
    }

    public void setSearchFileName(String searchFileName) {
        this.searchFileName = StringUtil.escapeLuceneSpecialCharacters(searchFileName);
    }

    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Timestamp toDate) {
        this.toDate = toDate;
    }

    // Note that with IndexSearcher, the directory is closed automatically
    protected IndexSearcher getSearcher(Directory directory) throws IOException {
        try {
            IndexSearcher searcher = new IndexSearcher(directory, true/*readOnly*/);
            return searcher;
        } catch (IOException ex) {
            // we throw new IOException because the original exception
            // contain sensitive directory information
            log.error("Cannot access the lucene search index for query. Please check if you have configed mvnForumHome properly. You can also go to Admin Zone to rebuild the Lucene index files.", ex);
            //@todo : localize me
            throw new IOException("Cannot access the lucene search index. Please report this error to web site Administrator (check mvnForumHome or rebuild Lucene index).");
        }
    }

    public void searchDocuments(int offset, int rowsToReturn, MVNForumPermission permission)
        throws IOException, DatabaseException, ObjectNotFoundException {

        // Now check if at least one of these input is present: key, filename
        if ((searchString == null || searchString.equals("")) && (searchFileName == null || searchFileName.equals(""))) {
            // should throw an Exception here, if not, later call getAttachmentResult() will return null
            return;
        }
        //Build the query
        BooleanQuery query = new BooleanQuery();
        //query.add(BooleanClause.)

        try {
            Query desQuery = getDescriptionQuery();
            if (desQuery != null) {
                query.add(desQuery, BooleanClause.Occur.MUST);
                log.debug("desQuery = " + desQuery);
            }

            Query fileNameQuery = getFileNameQuery();
            if (fileNameQuery != null) {
                query.add(fileNameQuery, BooleanClause.Occur.MUST);
                log.debug("fileNameQuery = " + fileNameQuery);
            }

            Query categoryForumQuery = getCategoryForumQuery(permission);
            if (categoryForumQuery != null) {
                log.debug("categoryForumQuery = " + categoryForumQuery);
                query.add(categoryForumQuery, BooleanClause.Occur.MUST);
            }
        } catch (ParseException e) {
            log.error("Cannot parse the search query", e);
        }
        log.debug("booleanQuery = " + query);

        TermRangeFilter dateFilter = null;
        //Add date filter if some of dates provided
        if (fromDate != null && toDate != null) {
            dateFilter = new TermRangeFilter(AttachmentIndexer.FIELD_ATTACHMENT_DATE, DateTools.dateToString(fromDate, Resolution.MILLISECOND), DateTools.dateToString(toDate, Resolution.MILLISECOND), true, true);
        } else if (fromDate != null) {
            dateFilter = TermRangeFilter.More(AttachmentIndexer.FIELD_ATTACHMENT_DATE, DateTools.dateToString(fromDate, Resolution.MILLISECOND));
        } else if (toDate != null) {
            dateFilter = TermRangeFilter.Less(AttachmentIndexer.FIELD_ATTACHMENT_DATE, DateTools.dateToString(toDate, Resolution.MILLISECOND));
        }

        Filter filter = null;

        if (dateFilter != null) {
            filter = dateFilter;
        }
        //Now search the documents
        Directory directory = null;
        IndexSearcher searcher = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchAttachmentIndexDir();

            //directory.
            searcher = getSearcher(directory);
            
            //If filter set then use it
            TopDocs attachHits = null;
            if (filter != null) {
                attachHits = searcher.search(query, filter, offset + rowsToReturn);
            } else {
                attachHits = searcher.search(query, null, offset + rowsToReturn);
            }

            hitCount = attachHits.totalHits;

            searchResult = getAttachments(searcher, attachHits, offset, rowsToReturn);
        } catch (IOException ex) {
            throw ex;
        } finally {
            // NOTE that we don't close directory because searcher.close() already do that
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException ex) {
                    log.debug("Error closing Lucene IndexSearcher", ex);
                }
            }
        }
    }

    public int getHitCount() {
        return hitCount;
    }

    public Collection getAttachmentResult() {
        if (searchResult == null) {
            //create an empty list, in case result is null
            searchResult = new ArrayList();
        }
        return searchResult;
    }

    private Collection getAttachments(IndexSearcher searcher, TopDocs attachHits, int offset, int rowsToReturn)
        throws IOException, ObjectNotFoundException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }
        //int count = offset + rowsToReturn;
        //int hitCount = getHitCount();
        ArrayList retValue = new ArrayList(hitCount);
        for (int i = offset; (i < offset + rowsToReturn) && (i < hitCount); i++) {
            int doc = attachHits.scoreDocs[i].doc;
            Document attachDocument = searcher.doc(doc);
            int attachDocumentID = Integer.parseInt(attachDocument.get(AttachmentIndexer.FIELD_ATTACHMENT_ID));
            AttachmentBean attachBean = DAOFactory.getAttachmentDAO().getAttachment(attachDocumentID);
            retValue.add(attachBean);
        }
        return retValue;
    }

    private Query getDescriptionQuery() throws ParseException {
        if (searchString == null || searchString.equals("")) {
            return null;
        }

        Analyzer analyzer = AttachmentIndexer.getAnalyzer();
        BooleanQuery descriptionQuery = new BooleanQuery();
        Query descQuery = new QueryParser(AttachmentIndexer.FIELD_ATTACHMENT_DESCRIPTION, analyzer).parse(searchString);
        descriptionQuery.add(descQuery, BooleanClause.Occur.SHOULD);
        return descriptionQuery;
    }

    private Query getFileNameQuery() throws ParseException {
        if (searchFileName == null || searchFileName.equals("")) {
            return null;
        }

        Analyzer analyzer = AttachmentIndexer.getAnalyzer();
        BooleanQuery fileNameQuery = new BooleanQuery();
        Query nameQuery = new QueryParser(AttachmentIndexer.FIELD_ATTACHMENT_NAME, analyzer).parse(searchFileName);
        fileNameQuery.add(nameQuery, BooleanClause.Occur.SHOULD);
        return fileNameQuery;
    }

    private Query getCategoryForumQuery(MVNForumPermission permission) throws DatabaseException {
        BooleanQuery categoryForumQuery = new BooleanQuery();
        if (forumId == 0) {
            // search all forum
            Collection forumBeans = ForumCache.getInstance().getBeans();
            for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
                ForumBean forumBean = (ForumBean)iter.next();
                int currentForumID = forumBean.getForumID();
                if ((forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) &&
                    permission.canGetAttachment(currentForumID)) {

                    Term forumTerm = new Term(AttachmentIndexer.FIELD_FORUM_ID, String.valueOf(currentForumID));
                    Query forumQuery = new TermQuery(forumTerm);
                    categoryForumQuery.add(forumQuery, BooleanClause.Occur.SHOULD);
                }
            }
        } else if (forumId > 0) {
            // search in forum
            Term forumTerm = new Term(AttachmentIndexer.FIELD_FORUM_ID, String.valueOf(forumId));
            Query forumQuery = new TermQuery(forumTerm);
            categoryForumQuery.add(forumQuery, BooleanClause.Occur.MUST);
        } else if (forumId < 0) {
            // search in category
            int categoryID = -forumId;//category is the negative value of forumID in this case
            Collection forumBeans = ForumCache.getInstance().getBeans();
            for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
                ForumBean forumBean = (ForumBean)iter.next();
                if (forumBean.getCategoryID() == categoryID) {
                    int currentForumID = forumBean.getForumID();
                    if ((forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) &&
                        permission.canGetAttachment(currentForumID)) {

                        Term forumTerm = new Term(AttachmentIndexer.FIELD_FORUM_ID, String.valueOf(currentForumID));
                        Query forumQuery = new TermQuery(forumTerm);
                        categoryForumQuery.add(forumQuery, BooleanClause.Occur.SHOULD);
                    }
                }
            }
        }
        return categoryForumQuery;
    }

}
