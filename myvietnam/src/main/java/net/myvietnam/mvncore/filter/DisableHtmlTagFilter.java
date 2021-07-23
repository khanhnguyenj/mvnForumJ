/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/DisableHtmlTagFilter.java,v 1.14 2009/12/04 09:39:23 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/12/04 09:39:23 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.filter;

/**
 * This class is used to filter to prevent cross site script bug, some
 * other class such as ParamUtil use it as a convenient way when get parameter
 */
public final class DisableHtmlTagFilter {

    private DisableHtmlTagFilter() { //prevent instantiation
    }

    /**
     * Filter a string to prevent XSS bug, presently it filter 5 characters: < > " ' &
     * 
     * @param input the string that need to filter
     * @return the filtered string
     */
    public static String filter(String input) {
        if (input == null) {
            return null;
        }

        char[] s = input.toCharArray();
        int length = s.length;
        StringBuffer ret = new StringBuffer(length + 100);// add more room to the result String

        for (int i = 0; i < length; i++) {
            if (s[i] == '<') {
                ret.append("&lt;");
            } else if (s[i] == '>') {
                ret.append("&gt;");
            } else if (s[i] == '"') {
                ret.append("&quot;");
            } else if (s[i] == '\'') {
                ret.append("&#39;");
            } else if (s[i] == '&') {
                // this hack the escape for unicode character, eg : &2345;
                if ( ((i + 3) < length) &&
                     (s[i+1] == '#') &&
                     (s[i+2]>='0'&&s[i+1]<='9') &&
                     (s[i+3]>='0'&&s[i+2]<='9') ) {
                    ret.append(s[i]);
                // hack &lt; (don't escape this char more than once)
                } else if ( ((i + 3) < length) &&
                            (s[i+1] == 'l') &&
                            (s[i+2] == 't') &&
                            (s[i+3] == ';') ) {
                    ret.append(s[i]);
                // hack &gt; (don't escape this char more than once)
                } else if ( ((i + 3) < length) &&
                            (s[i+1] == 'g') &&
                            (s[i+2] == 't') &&
                            (s[i+3] == ';') ) {
                    ret.append(s[i]);
                // hack &amp; (don't escape this char more than once)
                } else if ( ((i + 4) < length) &&
                            (s[i+1] == 'a') &&
                            (s[i+2] == 'm') &&
                            (s[i+3] == 'p') &&
                            (s[i+4] == ';') ) {
                    ret.append(s[i]);
                // hack &quot; (don't escape this char more than once)
                } else if ( ((i + 5) < length) &&
                            (s[i+1] == 'q') &&
                            (s[i+2] == 'u') &&
                            (s[i+3] == 'o') &&
                            (s[i+4] == 't') &&
                            (s[i+5] == ';') ) {
                    ret.append(s[i]);
                } else {
                    ret.append("&amp;");
                }
            } else {
                ret.append(s[i]);
            }
        }// for
        return ret.toString();
    }
    
    /**
     * Filter a string to prevent XSS bug, presently it filter 5 characters: < > " ' 
     * This is used for URL, because URL accept character &
     * 
     * @param input the string that need to filter
     * @return the filtered string
     */
    public static String filterForURL(String input) {
        if (input == null) {
            return null;
        }

        char[] s = input.toCharArray();
        int length = s.length;
        StringBuffer ret = new StringBuffer(length + 100);// add more room to the result String

        for (int i = 0; i < length; i++) {
            if (s[i] == '<') {
                ret.append("&lt;");
            } else if (s[i] == '>') {
                ret.append("&gt;");
            } else if (s[i] == '"') {
                ret.append("&quot;");
            } else if (s[i] == '\'') {
                ret.append("&#39;");
            } else {
                ret.append(s[i]);
            }
        }// for
        return ret.toString();
    }
}
