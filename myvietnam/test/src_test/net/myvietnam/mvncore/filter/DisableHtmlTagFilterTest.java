/*
 * $Header: /cvsroot/mvnforum/myvietnam/test/src_test/net/myvietnam/mvncore/filter/DisableHtmlTagFilterTest.java,v 1.2 2010/07/21 10:35:29 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2010/07/21 10:35:29 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2010 by MyVietnam.net
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
 */
package net.myvietnam.mvncore.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DisableHtmlTagFilterTest {

    @Test
    public void testFilter() {
        
        System.out.println("DisableHtmlTagFilterTest.testFilter()");
        
        String input1    = " ";
        String expected1 = " ";
        String input2    = "<>&'\"";
        String expected2 = "&lt;&gt;&amp;&#39;&quot;";
        String input3    = " < > $ & test ' again \" \"";
        String expected3 = " &lt; &gt; $ &amp; test &#39; again &quot; &quot;";
        String input4    = " <javascript scr=\"test?a=1&b='\">";
        String expected4 = " &lt;javascript scr=&quot;test?a=1&amp;b=&#39;&quot;&gt;";
        String input5    = "&lt;&gt;&amp;&#39;&quot;";
        String expected5 = "&lt;&gt;&amp;&#39;&quot;";

        /*
        System.out.println("DisableHtmlTagFilterTest.testFilter() 1 = " + DisableHtmlTagFilter.filter(input1));
        System.out.println("DisableHtmlTagFilterTest.testFilter() 2 = " + DisableHtmlTagFilter.filter(input2));
        System.out.println("DisableHtmlTagFilterTest.testFilter() 3 = " + DisableHtmlTagFilter.filter(input3));
        System.out.println("DisableHtmlTagFilterTest.testFilter() 4 = " + DisableHtmlTagFilter.filter(input4));
        System.out.println("DisableHtmlTagFilterTest.testFilter() 5 = " + DisableHtmlTagFilter.filter(input5));
         */
        assertEquals(expected1, DisableHtmlTagFilter.filter(input1));
        assertEquals(expected2, DisableHtmlTagFilter.filter(input2));
        assertEquals(expected3, DisableHtmlTagFilter.filter(input3));
        assertEquals(expected4, DisableHtmlTagFilter.filter(input4));
        assertEquals(expected5, DisableHtmlTagFilter.filter(input5));
    }
    
    @Test
    public void filterForURL() {
        System.out.println("DisableHtmlTagFilterTest.filterForURL()");
    }
    
}
