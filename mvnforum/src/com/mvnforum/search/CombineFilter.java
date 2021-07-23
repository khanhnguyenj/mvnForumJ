/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/CombineFilter.java,v 1.9 2007/10/09 11:09:21 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.9 $
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
import org.apache.lucene.search.Filter;

/**
 * A Filter that restricts search results to match the results of both filter.
 */
public class CombineFilter extends Filter {

    private Filter leftFilter;

    private Filter rightFilter;

    public CombineFilter(Filter f1, Filter f2) {
        if ((f1 == null) && (f2 == null)) {
            throw new IllegalArgumentException("At least f1 or f2 must not be null.");
        }
        leftFilter = f1;
        rightFilter = f2;
    }

    public BitSet bits(IndexReader reader) throws IOException {

        BitSet resultBits = null;
        if ((leftFilter != null) && (rightFilter != null)) {
            BitSet leftBits = leftFilter.bits(reader);
            BitSet rightBits = rightFilter.bits(reader);
            resultBits = (BitSet) leftBits.clone();
            resultBits.and(rightBits);
        } else if (leftFilter != null) {
            resultBits = leftFilter.bits(reader);
        } else if (rightFilter != null) {
            resultBits = rightFilter.bits(reader);
        } else {
            // should never happen since we have checked it in the constructor
            throw new IllegalArgumentException("At least f1 or f2 must not be null.");
        }
        return resultBits;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(leftFilter);
        buffer.append("\n");
        buffer.append(rightFilter);
        return buffer.toString();
    }
}
