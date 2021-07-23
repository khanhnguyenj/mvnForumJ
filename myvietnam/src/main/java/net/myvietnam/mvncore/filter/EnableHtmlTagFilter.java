/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/EnableHtmlTagFilter.java,v 1.10 2009/03/10 15:18:19 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/03/10 15:18:19 $
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

public final class EnableHtmlTagFilter {

    private EnableHtmlTagFilter() { //prevent instantiation
    }

    public static String filter(String input) {
        if (input == null) {
            return null;
        }

        char[] s = input.toCharArray();
        int length = s.length;
        StringBuffer ret = new StringBuffer(length);

        for (int i = 0; i < length; i++) {
            if (s[i] == '&') {
                if ( ((i + 3) < length) &&
                     (s[i + 1] == 'l') &&
                     (s[i + 2] == 't') &&
                     (s[i + 3] == ';')) { // &lt; = <
                    ret.append('<');
                    i += 3;
                } else if ( ((i + 3) < length) &&
                            (s[i+1] == 'g') &&
                            (s[i+2] == 't') &&
                            (s[i+3] == ';') ) { // &gt; = >
                    ret.append('>');
                    i += 3;
                } else if ( ((i + 4) < length) &&
                            (s[i+1] == 'a') &&
                            (s[i+2] == 'm') &&
                            (s[i+3] == 'p') &&
                            (s[i+4] == ';') ) { // &amp; = &
                    ret.append('&');
                    i += 4;
                } else if ( ((i + 4) < length) &&
                        (s[i+1] == '#') &&
                        (s[i+2] == '3') &&
                        (s[i+3] == '9') &&
                        (s[i+4] == ';') ) { // &#39; = '
                    ret.append('\'');
                    i += 4;
                } else if ( ((i + 5) < length) &&
                            (s[i+1] == 'q') &&
                            (s[i+2] == 'u') &&
                            (s[i+3] == 'o') &&
                            (s[i+4] == 't') &&
                            (s[i+5] == ';') ) { // &quot; = "
                    ret.append('"');
                    i += 5;
                } else {
                    ret.append('&');
                }
            } else {
                ret.append(s[i]);
            }
        }// for
        return ret.toString();
    }

    /*
    public static void main(String[] args) {
        //String input = " &lt; &gt; &amp; &quot;";
        String input = "&lt&gt; &lt;mvn&gt; &amp; &amp;amp;&quot ;";
        System.out.println("input = '" + input + "' length = " + input.length());
        EnableHtmlTagFilter enableEmotionFilter = new EnableHtmlTagFilter();

        long start = System.currentTimeMillis();
        String output = null;
        for (int i = 0; i <100; i++) {
            output = enableEmotionFilter.filter(input);
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("total time = " + time);

        System.out.println(output);
    }
    */
}
