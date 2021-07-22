/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/IntegerFilter.java,v 1.11 2007/10/09 11:09:21 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.11 $
 * $Date: 2007/10/09 11:09:21 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.search;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.Filter;

/**
 * A Filter that restricts search results to a range of integer.
 *
 * <p>
 * For this to work, documents must have been indexed with a
 */
public class IntegerFilter extends Filter {
    String field;
    String start = intToString(0);
    String end = intToString(Integer.MAX_VALUE);

    private IntegerFilter(String f) {
        field = f;
    }

    /**
     * Constructs a filter for field <code>f</code> matching Integers between
     * <code>from</code> and <code>to</code> inclusively.
     */
    public IntegerFilter(String field, int from, int to) {
        this.field = field;
        start = intToString(from);
        end = intToString(to);
    }

    /**
     * Constructs a filter for field <code>f</code> matching Integers between
     * <code>from</code> and <code>to</code> inclusively.
     */
    public IntegerFilter(String field, Integer from, Integer to) {
        this.field = field;
        start = intToString(from.intValue());
        end = intToString(to.intValue());
    }

    /**
     * Constructs a filter for field <code>f</code> matching Integers on or
     * more than <code>Integer</code>.
     */
    public static IntegerFilter greaterThan(String field, Integer i) {
        IntegerFilter result = new IntegerFilter(field);
        result.start = intToString(i.intValue());
        return result;
    }

    /**
     * Constructs a filter for field <code>f</code> matching Integers on or
     * more than <code>Integer</code>.
     */
    public static IntegerFilter greaterThan(String field, int i) {
        IntegerFilter result = new IntegerFilter(field);
        result.start = intToString(i);
        return result;
    }

    /**
     * Constructs a filter for field <code>f</code> matching integers on or
     * less than <code>Integer</code>.
     */
    public static IntegerFilter lessThan(String field, Integer i) {
        IntegerFilter result = new IntegerFilter(field);
        result.end = intToString(i.intValue());
        return result;
    }

    /**
     * Constructs a filter for field <code>f</code> matching Integers on or
     * more than <code>Integer</code>.
     */
    public static IntegerFilter lessThan(String field, int i) {
        IntegerFilter result = new IntegerFilter(field);
        result.end = intToString(i);
        return result;
    }

    public static String intToString(int i) {
        // please note that max int with radian=36 is "zik0zj", length is 6
        // min int is "-zik0zk"
        String temp = Integer.toString(i, Character.MAX_RADIX);
        int need0 = 6 - temp.length();
        StringBuffer result = new StringBuffer(7);
        if (i < 0) {
            result.append("0");
        } else {
            // if 0, then we MUST append 1
            result.append("1");
        }
        for (int j = 0; j < need0; j++) {
            result.append("0");
        }
        result.append(temp);
        return result.toString();
    }

    public static int stringToInt(String i) {
        return Integer.parseInt(i, Character.MAX_RADIX);
    }

    /**
     * Returns a BitSet with true for documents which should be permitted in
     * search results, and false for those that should not.
     */
    public BitSet bits(IndexReader reader) throws IOException {
        BitSet bits = new BitSet(reader.maxDoc());
        TermEnum enumerator = reader.terms(new Term(field, start));
        TermDocs termDocs = reader.termDocs();
        if (enumerator.term() == null) {
            return bits;
        }
        try {
            Term stop = new Term(field, end);
            while (enumerator.term().compareTo(stop) <= 0) {
                termDocs.seek(enumerator.term());
                while (termDocs.next()) {
                    bits.set(termDocs.doc());
                }
                if (!enumerator.next()) {
                    break;
                }
            }
        } finally {
            enumerator.close();
            termDocs.close();
        }
        return bits;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(field);
        buffer.append(":");
        buffer.append(start);
        buffer.append("-");
        buffer.append(end);
        return buffer.toString();
    }
}
