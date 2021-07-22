/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/post/PostSearchQuery.java,v 1.30 2009/12/05 08:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.30 $
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
package com.mvnforum.search.post;

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
import com.mvnforum.search.CombineFilter;
import com.mvnforum.search.IntegerFilter;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

/**
 * This class is used for specifying query that should be searched for. Query
 * can contain keywords and further it can be filtered by specifying member
 * name of the author, forumID as well as date interval for searching.
 *
 * searchString contains one or more keywords. Each keyword can use wildcards.
 * ? for single character and * for multiple characters.
 * For specifying boolean operators AND and OR operators can be used.....
 *
 * For all available options consult Lucene documentation http://jakarta.apache.org/lucene
 *
 * @author Dejan Krsmanovic dejan_krsmanovic@yahoo.com
 */
public class PostSearchQuery {
    
    private static final Logger log = LoggerFactory.getLogger(PostSearchQuery.class);

    // constant for search by time
    public static final int SEARCH_ANY_DATE = 0;

    public static final int SEARCH_NEWER    = 1;
    public static final int SEARCH_OLDER    = 2;

    // constant for search by the post title/body
    public static final int SEARCH_ONLY_TITLE = 1;
    public static final int SEARCH_ONLY_BODY =  2;

    // constant for sorting option by time
    public static final int SEARCH_SORT_DEFAULT   = 0;
    public static final int SEARCH_SORT_TIME_DESC = 1;
    public static final int SEARCH_SORT_TIME_ASC  = 2;
    
    private int memberID = -1;
    private int forumId = 0;
    //private boolean withAttachment = false;// currently does not use this variable, use minAttachmentCount

    // search with ignore attachment (attachmentCount is negative)
    private int minAttachmentCount = -1;// or 0 should also be OK 

    private String searchString = null;

    private Timestamp fromDate = null;
    private Timestamp toDate = null;

    private int hitCount = 0;
    private Collection searchResult = null;

    // 1|2 = 3;
    private int scopeInPost = SEARCH_ONLY_TITLE|SEARCH_ONLY_BODY;

    private int sort = SEARCH_SORT_DEFAULT;
    
    public PostSearchQuery() {
    }

    /**
     * Set name of the author that should be searched for
     * @param _memberID
     */
    public void setMemberId(int _memberID) {
        this.memberID = _memberID;
    }

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

    public void setScopeInPost(int scopeInPost) {
        this.scopeInPost = scopeInPost;
    }

    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Timestamp toDate) {
        this.toDate = toDate;
    }

    public void setMinAttachmentCount(int count) {
        this.minAttachmentCount = count;
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

        // Now check if at least one of these input is present: key, member, attachment
        if ((searchString == null || searchString.equals("")) &&
            (memberID == -1) &&
            (minAttachmentCount < 1)) {
            // should throw an Exception here, if not, later call getPostResult() will return null
            return;
        }
        //Build the query
        BooleanQuery query = new BooleanQuery();
        try {
            Query topicBodyQuery = getTopicBodyQuery();
            if (topicBodyQuery != null) {
                query.add(topicBodyQuery, BooleanClause.Occur.MUST);
                log.debug("topicBodyQuery = " + topicBodyQuery);
            }

            /*
            Query withAttachmentQuery = getWithAttachmentQuery();
            if (withAttachment) {
                query.add(withAttachmentQuery, BooleanClause.Occur.MUST);
                log.debug("withAttachmentQuery = " + withAttachmentQuery);
            }*/

            Query categoryForumQuery = getCategoryForumQuery(permission);
            if (categoryForumQuery != null) {
                query.add(categoryForumQuery, BooleanClause.Occur.MUST);
                log.debug("categoryForumQuery = " + categoryForumQuery);
            }

            Query memberQuery = getMemberQuery();
            if (memberQuery != null) {
                query.add(memberQuery, BooleanClause.Occur.MUST);
                log.debug("memberQuery = " + memberQuery);
            }
        } catch (ParseException pe) {
            log.error("Cannot parse the search query", pe);
        }
        log.debug("booleanQuery = " + query);

        TermRangeFilter dateFilter = null;
        //Add date filter if some of dates provided
        if (fromDate != null && toDate != null) {
            dateFilter = new TermRangeFilter(PostIndexer.FIELD_POST_DATE, DateTools.dateToString(fromDate, Resolution.MILLISECOND), DateTools.dateToString(toDate, Resolution.MILLISECOND), true, true);
        } else if (fromDate != null) {
            dateFilter = TermRangeFilter.More(PostIndexer.FIELD_POST_DATE, DateTools.dateToString(fromDate, Resolution.MILLISECOND));
        } else if (toDate != null) {
            dateFilter = TermRangeFilter.Less(PostIndexer.FIELD_POST_DATE, DateTools.dateToString(toDate, Resolution.MILLISECOND));
        }

        IntegerFilter attachCountFilter = null;

        if (minAttachmentCount > 0) {
            attachCountFilter = IntegerFilter.greaterThan(PostIndexer.FIELD_ATTACHMENT_COUNT, minAttachmentCount);
        }

        Filter filter = null;

        if (dateFilter != null) {
            if (attachCountFilter != null) {
                filter = new CombineFilter(dateFilter, attachCountFilter);
            } else {
                filter = dateFilter;
            }
        } else {
            filter = attachCountFilter;
        }

        //Now search the documents
        Directory directory = null;
        IndexSearcher searcher = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchPostIndexDir();
            searcher = getSearcher(directory);

            //If filter set then use it
            TopFieldDocs postHits = null;
            if (filter != null) {
                postHits = searcher.search(query, filter, offset + rowsToReturn, getQuerySort());
            } else {
                postHits = searcher.search(query, null, offset + rowsToReturn, getQuerySort());
            }

            hitCount = postHits.totalHits;
            searchResult = getPosts(searcher, postHits, offset, rowsToReturn);
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

    public Collection getPostResult() {
        if (searchResult == null) {
            //create an empty list, in case result is null
            searchResult = new ArrayList();
        }
        return searchResult;
    }

    private Collection getPosts(IndexSearcher searcher, TopFieldDocs postHits, int offset, int rowsToReturn)
        throws IOException, ObjectNotFoundException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        //int hitCount = getHitCount();
        Collection retValue = new ArrayList(hitCount);

        for (int i = offset; (i < offset + rowsToReturn) && (i < hitCount); i++) {
            int doc = postHits.scoreDocs[i].doc;
            Document postDocument = searcher.doc(doc);
            int postID = Integer.parseInt(postDocument.get(PostIndexer.FIELD_POST_ID));
            PostBean postBean = PostCache.getInstance().getPost(postID);
            retValue.add(postBean);
        }
        return retValue;
    }

    private Query getTopicBodyQuery() throws ParseException {
        if (searchString == null || searchString.equals("")) {
            return null;
        }
        Analyzer analyzer = PostIndexer.getAnalyzer();
        BooleanQuery topicBodyQuery = new BooleanQuery();

        //add topic query
        Query topicQuery = new QueryParser(PostIndexer.FIELD_POST_TOPIC, analyzer).parse(searchString);
        topicBodyQuery.add(topicQuery, BooleanClause.Occur.SHOULD);

        //add body query
        Query bodyQuery = new QueryParser(PostIndexer.FIELD_POST_BODY, analyzer).parse(searchString);
        if (scopeInPost == SEARCH_ONLY_TITLE) {
            return topicQuery;
        } else if (scopeInPost == SEARCH_ONLY_BODY) {
            return bodyQuery;
        }
        topicBodyQuery.add(bodyQuery, BooleanClause.Occur.SHOULD);

        return topicBodyQuery;
    }

    private Query getMemberQuery() {
        Query memberQuery = null;
        if (memberID > 0) {
            Term memberTerm = new Term(PostIndexer.FIELD_MEMBER_ID, String.valueOf(memberID));
            memberQuery = new TermQuery(memberTerm);
        }
        return memberQuery;
    }

    /*
    private Query getWithAttachmentQuery() {
        Query withAttachmentQuery = null;
        if (withAttachment) {
            Term withAttachmentTerm = new Term(PostIndexer.FIELD_WITH_ATTACHMENT, String.valueOf(withAttachment));
            withAttachmentQuery = new TermQuery(withAttachmentTerm);
        }
        return withAttachmentQuery;
    }*/

    private Query getCategoryForumQuery(MVNForumPermission permission) throws DatabaseException {
        BooleanQuery categoryForumQuery = new BooleanQuery();
        if (forumId == 0) {
            // search all forum
            Collection forumBeans = ForumCache.getInstance().getBeans();
            for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
                ForumBean forumBean = (ForumBean)iter.next();
                int currentForumID = forumBean.getForumID();
                if ((forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) &&
                    permission.canReadPost(currentForumID)) {

                    Term forumTerm = new Term(PostIndexer.FIELD_FORUM_ID, String.valueOf(currentForumID));
                    Query forumQuery = new TermQuery(forumTerm);
                    categoryForumQuery.add(forumQuery, BooleanClause.Occur.SHOULD);
                }
            }
        } else if (forumId > 0) {
            // search in forum
            Term forumTerm = new Term(PostIndexer.FIELD_FORUM_ID, String.valueOf(forumId));
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
                        permission.canReadPost(currentForumID)) {

                        Term forumTerm = new Term(PostIndexer.FIELD_FORUM_ID, String.valueOf(currentForumID));
                        Query forumQuery = new TermQuery(forumTerm);
                        categoryForumQuery.add(forumQuery, BooleanClause.Occur.SHOULD);
                    }
                }
            }
        }
        return categoryForumQuery;
    }

    public void setSort(int sort) {
        if ( (sort != SEARCH_SORT_DEFAULT) && (sort != SEARCH_SORT_TIME_ASC) && (sort != SEARCH_SORT_TIME_DESC)) {
            throw new IllegalArgumentException("Does not support sort = " + sort);
        }
        this.sort = sort;
    }
    public int getSort() {
        return sort;
    }
    private Sort getQuerySort() {
        Sort sortObj = null;
        switch (sort) {
            case SEARCH_SORT_TIME_ASC:
                sortObj = new Sort(new SortField(PostIndexer.FIELD_POST_DATE, SortField.STRING, false));
                break;
            case SEARCH_SORT_TIME_DESC:
                sortObj = new Sort(new SortField(PostIndexer.FIELD_POST_DATE, SortField.STRING, true));
                break;
            default:
                sortObj = new Sort();
                break;
        }
        return sortObj;
    }

}
