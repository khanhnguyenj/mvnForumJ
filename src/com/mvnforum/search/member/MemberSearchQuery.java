/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/member/MemberSearchQuery.java,v 1.23 2009/12/05 08:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
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
package com.mvnforum.search.member;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

/**
 * This class is used for specifying query that should be searched for. Query
 * can contain keywords and further it can be filtered by specifying member
 * name of the Member, address as well as date interval for searching.
 *
 * searchString contains one or more keywords. Each keyword can use wildcards.
 * ? for single character and * for multiple characters.
 * For specifying boolean operators AND and OR operators can be used.....
 *
 * For all available options consult Lucene documentation http://jakarta.apache.org/lucene
 *
 * @author Dejan Krsmanovic dejan_krsmanovic@yahoo.com
 */
public class MemberSearchQuery {

    // search engine properties
    private static final Logger log = LoggerFactory.getLogger(MemberSearchQuery.class);
    
    // search object constants
    public static final int SEARCH_ANY_DATE = 0;

    public static final int SEARCH_NEWER    = 1;
    public static final int SEARCH_OLDER    = 2;

    // search object properties
    private String memberEmailKey = null;
    private String memberNameKey = null;

    private String memberLastNameKey = null;
    private String memberFirstNameKey = null;
    //private String memberGender = null;
    private String memberCountry =  null;

    private Timestamp from = null;
    private Timestamp to = null;

    private int hitCount = 0;
    private Collection searchResult = null;

    public MemberSearchQuery() {
    }

    /**
     * Set string that should be searched for.
     * @param memberNameKey
     */
    public void setMemberNameKey(String memberNameKey) {
        this.memberNameKey = memberNameKey;
    }

    public void setMemberEmailKey(String memberEmailKey) {
        this.memberEmailKey = memberEmailKey;
    }

    public void setFromDate(Timestamp from) {
        this.from = from;
    }

    public void setToDate(Timestamp to) {
        this.to = to;
    }

    public void setMemberLastNameKey(String lastNameKey) {
        this.memberLastNameKey = lastNameKey;
    }

    public void setMemberFirstNameKey(String firstNameKey) {
        this.memberFirstNameKey = firstNameKey;
    }

    public void setCountry(String country) {
        this.memberCountry = country;
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

    public void searchDocuments(int offset, int rowsToReturn)
        throws IOException, DatabaseException, ObjectNotFoundException {

        // check if those search key is empty??
        //Build the query
        BooleanQuery query = new BooleanQuery();//query.
        try {
            Query memberNameQuery = getMemberNameQuery();
            if (memberNameQuery != null) {
                query.add(memberNameQuery, BooleanClause.Occur.MUST);
                log.debug("memberNameQuery = " + memberNameQuery);
            }

            Query memberEmailQuery = getMemberEmailQuery();
            if (memberEmailQuery != null) {
                query.add(memberEmailQuery, BooleanClause.Occur.MUST);
                log.debug("memberEmailQuery = " + memberEmailQuery);
            }

            Query memberLastNameQuery = getMemberLastNameQuery();
            if (memberLastNameQuery != null) {
                query.add(memberLastNameQuery, BooleanClause.Occur.MUST);
                log.debug("memberLastNameQuery = " + memberLastNameQuery);
            }

            Query memberFirstNameQuery = getMemberFirstNameQuery();
            if (memberFirstNameQuery != null) {
                query.add(memberFirstNameQuery, BooleanClause.Occur.MUST);
                log.debug("memberFirstNameQuery = " + memberFirstNameQuery);
            }

            Query memberCountryQuery = getMemberCountryQuery();
            if (memberCountryQuery != null) {
                query.add(memberCountryQuery, BooleanClause.Occur.MUST);
                log.debug("memberCountryQuery = " + memberCountryQuery);
            }
        } catch (ParseException pe) {
            log.error("Cannot parse the search query", pe);
        }
        log.debug("booleanQuery = " + query);

        TermRangeFilter dateFilter = null;
        if (from != null && to != null) {
            dateFilter = new TermRangeFilter(MemberIndexer.FIELD_CREATION_DATE, DateTools.dateToString(from, Resolution.MILLISECOND), DateTools.dateToString(to, Resolution.MILLISECOND), true, true);
        } else if (from != null) {
            dateFilter = TermRangeFilter.More(MemberIndexer.FIELD_CREATION_DATE, DateTools.dateToString(from, Resolution.MILLISECOND));
        } else if (to != null) {
            dateFilter = TermRangeFilter.Less(MemberIndexer.FIELD_CREATION_DATE, DateTools.dateToString(to, Resolution.MILLISECOND));
        }

        //Now search the documents
        Directory directory = null;
        IndexSearcher searcher = null;
        try {
            SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
            directory = service.getSearchMemberIndexDir();
            searcher = getSearcher(directory);

            //If dateFilter set then use it
            TopDocs memberHits = null;
            //dateFilter = null;
            if (dateFilter != null) {
                memberHits = searcher.search(query, dateFilter, offset + rowsToReturn);
            } else {
                memberHits = searcher.search(query, null, offset + rowsToReturn);
            }
            hitCount = memberHits.totalHits;
            log.debug("[ HIT COUNT ]" + hitCount);
            searchResult = getMembers(searcher, memberHits, offset, rowsToReturn);
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

    public Collection getMemberResult() {
        if (searchResult == null) {
            //create an empty list, in case result is null
            searchResult = new ArrayList();
        }
        return searchResult;
    }

    private Collection getMembers(IndexSearcher searcher, TopDocs memberHits, int offset, int rowsToReturn)
        throws IOException, ObjectNotFoundException, DatabaseException {

        if (offset < 0) {
            throw new IllegalArgumentException("The offset < 0 is not allowed.");
        }
        if (rowsToReturn <= 0) {
            throw new IllegalArgumentException("The rowsToReturn <= 0 is not allowed.");
        }

        //int hitCount = getHitCount();
        ArrayList retValue = new ArrayList(hitCount);

        for (int i = offset; (i < offset + rowsToReturn) && (i < hitCount); i++) {
            int doc = memberHits.scoreDocs[i].doc;
            Document memberDocument = searcher.doc(doc);
            int memberID = Integer.parseInt(memberDocument.get(MemberIndexer.FIELD_MEMBER_ID));
            MemberBean MemberBean = DAOFactory.getMemberDAO().getMember(memberID);
            retValue.add(MemberBean);
        }
        return retValue;
    }

    private Query getMemberNameQuery() {        
        if (memberNameKey == null || memberNameKey.equals("")) {
            return null;
        }
        Term memberNameTerm = new Term(MemberIndexer.FIELD_MEMBER_NAME, memberNameKey);
        Query memberNameQuery = new TermQuery(memberNameTerm);
        return memberNameQuery;
    }

    private Query getMemberEmailQuery() {
        if (memberEmailKey == null || memberEmailKey.equals("")) {
            return null;
        }
        Term memberEmailTerm = new Term(MemberIndexer.FIELD_MEMBER_EMAIL, memberEmailKey);
        Query memberEmailQuery = new TermQuery(memberEmailTerm);
        return memberEmailQuery;
    }

    private static Query getMemberQuery(String fieldName, String keyQuery) {
        if (keyQuery == null || keyQuery.equals("")) {
            return null;
        }
        Term keyTerm = new Term(fieldName, keyQuery);
        Query memberQuery = new TermQuery(keyTerm);
        return memberQuery;
    }

    private Query getMemberLastNameQuery() throws ParseException {
        return getMemberQuery(MemberIndexer.FIELD_MEMBER_LASTNAME, memberLastNameKey);
    }
    
    private Query getMemberFirstNameQuery() {
        return getMemberQuery(MemberIndexer.FIELD_MEMBER_FIRSTNAME, memberFirstNameKey);
    }

    private Query getMemberCountryQuery() {
        return getMemberQuery(MemberIndexer.FIELD_MEMBER_COUNTRY, memberCountry);
    }
    
}
