/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/util/Utils.java,v 1.5 2007/01/15 10:27:31 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:27:31 $
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
 * @author: 
 */
package org.mvnforum.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;

//==============================================================================
//  The JavaReference.com Software License, Version 1.0 
//  Copyright (c) 2002-2005  JavaReference.com. All rights reserved.
//
//  
//  Redistribution and use in source and binary forms, with or without 
//  modification, are permitted provided that the following conditions 
//  are met: 
//  
//  1. Redistributions of source code must retain the above copyright notice, 
//     this list of conditions and the following disclaimer. 
//  
//  2. Redistributions in binary form must reproduce the above copyright notice, 
//     this list of conditions and the following disclaimer in the documentation 
//     and/or other materials provided with the distribution. 
//     
//  3. The end-user documentation included with the redistribution, if any, must 
//     include the following acknowlegement: 
//     
//     "This product includes software developed by the Javareference.com 
//     (http://www.javareference.com/)." 
//     
//     Alternately, this acknowlegement may appear in the software itself, if and 
//     wherever such third-party acknowlegements normally appear. 
//     
//  4. The names "JavaReference" and "Javareference.com", must not be used to 
//     endorse or promote products derived from this software without prior written 
//     permission. For written permission, please contact webmaster@javareference.com. 
//     
//  5. Products derived from this software may not be called "Javareference" nor may 
//     "Javareference" appear in their names without prior written permission of  
//     Javareference.com. 
//     
//  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
//  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
//  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
//  JAVAREFERENCE.COM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
//  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
//  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
//  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
//  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
//  
//================================================================================ 
//  Software from this site consists of contributions made by various individuals 
//  on behalf of Javareference.com. For more information on Javareference.com, 
//  please see http://www.javareference.com 
//================================================================================

/**
 * @author anandh
 */
public class Utils {

    static SimpleDateFormat datetimeFmt = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");// TODO why 2 spaces ???

    static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

    static Hashtable str2hex = new Hashtable();
    
    static {
        str2hex.put("0", new Integer(0));
        str2hex.put("1", new Integer(1));
        str2hex.put("2", new Integer(2));
        str2hex.put("3", new Integer(3));
        str2hex.put("4", new Integer(4));
        str2hex.put("5", new Integer(5));
        str2hex.put("6", new Integer(6));
        str2hex.put("7", new Integer(7));
        str2hex.put("8", new Integer(8));
        str2hex.put("9", new Integer(9));
        str2hex.put("A", new Integer(10));
        str2hex.put("B", new Integer(11));
        str2hex.put("C", new Integer(12));
        str2hex.put("D", new Integer(13));
        str2hex.put("E", new Integer(14));
        str2hex.put("F", new Integer(15));
        str2hex.put("a", new Integer(10));
        str2hex.put("b", new Integer(11));
        str2hex.put("c", new Integer(12));
        str2hex.put("d", new Integer(13));
        str2hex.put("e", new Integer(14));
        str2hex.put("f", new Integer(15));
    }

    public static String wrapIt(String text) {
        
        StringBuffer strbuf = new StringBuffer();
        //strbuf.append('\'');
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\'') {
                strbuf.append("\'\'");
            } else {
                strbuf.append(text.charAt(i));
            }
        }
        //strbuf.append('\'');
 
        String result = strbuf.toString();
        result = DisableHtmlTagFilter.filter(result);
        
        // correct smilies
        // smile
        result = result.replaceAll("(.*):\\)(.*)", "$1\\[:\\)\\]$2");
        // sad
        result = result.replaceAll("(.*):\\((.*)", "$1\\[:\\(\\]$2");
        // big grin
        result = result.replaceAll("(.*):D(.*)", "$1\\[:D\\]$2");
        // laughing
        result = result.replaceAll("(.*):lol:(.*)", "$1\\[:\\)\\)\\]$2");
        // crying
        result = result.replaceAll("(.*):cry:(.*)", "$1\\[:\\(\\(\\]$2");
        // wink
        result = result.replaceAll("(.*):wink:(.*)", "$1\\[;\\)\\]$2");
        // blushing
        result = result.replaceAll("(.*):oops:(.*)", "$1\\[:\">\\]$2");
        // tongue
        result = result.replaceAll("(.*):p(.*)", "$1\\[:p\\]$2");
        // cool
        result = result.replaceAll("(.*)8\\)(.*)", "$1\\[B-\\)\\]$2");
        // confused
        result = result.replaceAll("(.*):\\?(.*)", "$1\\[:-/\\]$2");
        // shock
        result = result.replaceAll("(.*):shock:(.*)", "$1\\[:O\\]$2");
        // devil
        result = result.replaceAll("(.*):evil:(.*)", "$1\\[>:\\)\\]$2");

        // correct BBcodes
        // url
        result = result.replaceAll("(.*)\\[url\\](.*)\\[/url\\](.*)", "$1\\[url=$2\\]$2\\[/url\\]$3");
        // bold
        result = result.replaceAll("(.*)\\[b:[0-9,abcdef]*\\](.*)\\[/b:[0-9,abcdef]*\\](.*)", "$1\\[b\\]$2\\[/b\\]$3");
        // underline
        result = result.replaceAll("(.*)\\[u:[0-9,abcdef]*\\](.*)\\[/u:[0-9,abcdef]*\\](.*)", "$1\\[u\\]$2\\[/u\\]$3");
        // italic
        result = result.replaceAll("(.*)\\[i:[0-9,abcdef]*\\](.*)\\[/i:[0-9,abcdef]*\\](.*)", "$1\\[i\\]$2\\[/i\\]$3");
        // color
        result = result.replaceAll("(.*)\\[color=([a-z]*):[0-9,abcdef]*\\](.*)\\[/color:[0-9,abcdef]*\\](.*)",
                "$1\\[color=$2\\]$3\\[/color\\]$4");
        //regular quote
        result = result.replaceAll("(.*)\\[quote:[0-9,abcdef]*\\](.*)\\[/quote:[0-9,abcdef]*\\](.*)", "$1\\[quote=\\]$2\\[/quote\\]$3");
        // fancy quote
        result = result.replaceAll("(.*)\\[quote:[0-9,abcdef]*=\\\"(.*)\\\"\\](.*)\\[/quote:[0-9,abcdef]*\\](.*)", "$1\\[quote=\\\"$2\\\"\\]$3\\[/quote\\]$4");
        return result;
    }

    public static String DateTimeFromS(long s) {
        Date d = new Date(s * 1000);
        return datetimeFmt.format(d);
    }

    public static String DateFromS(long s) {
        Date d = new Date(s * 1000);
        return dateFmt.format(d);
    }

    public static String HexIPtoString(String hexrep) {
        if (hexrep.length() < 8) {
            return "0.0.0.0";
        }
        
        if (hexrep.length() > 8) {
            throw new IllegalArgumentException("Does not accept hexrep = " + hexrep);
        }

        byte[] ipaddr = new byte[8];
        for (int i = 0; i < ipaddr.length; i++) {
            String letter = String.valueOf(hexrep.charAt(i));
            int val = ((Integer) str2hex.get(letter)).intValue();// TODO NPE could occur
            ipaddr[i] = (byte) val;
        }

        int A = 0;
        A |= ipaddr[0];
        A = A << 4;
        A |= ipaddr[1];
        
        int B = 0;
        B |= ipaddr[2];
        B = B << 4;
        B |= ipaddr[3];
        
        int C = 0;
        C |= ipaddr[4];
        C = C << 4;
        C |= ipaddr[5];
        
        int D = 0;
        D |= ipaddr[6];
        D = D << 4;
        D |= ipaddr[7];

        StringBuffer retValue = new StringBuffer(16);
        retValue.append(A).append(".").append(B).append(".").append(C).append(".").append(D);
        return retValue.toString();
    }

    public static String stripPHPBBQuotes(String in) {
        boolean err = false;
        StringBuffer endstr = new StringBuffer();

        while (true) {
            int firstquote = in.indexOf("[quote:");
            if (firstquote == -1) {
                err = true;
                break;
            }

            int firstclosebrace = in.indexOf(']', firstquote);
            if (firstclosebrace == -1) {
                err = true;
                break;
            }

            endstr.append(in.substring(0, firstquote));
            endstr.append("[quote]");

            int endquote = in.indexOf("[/quote:");
            if (endquote == -1) {
                err = true;
                break;
            }

            int endclosebrace = in.indexOf(']', endquote);
            if (endclosebrace == -1) {
                err = true;
                break;
            }

            endstr.append(in.substring(firstclosebrace + 1, endquote));
            endstr.append("[/quote]");
            endstr.append(in.substring(endclosebrace + 1));
        }

        if (err) {
            return in;
        } else {
            return endstr.toString();
        }
    }
    
    public static String getFormatDate(Date date) {
        return dateFmt.format(date);
    }
    
    public static Timestamp getTimeStamp(long data) {
        return new Timestamp(data*1000);
    }

    public static void main(String[] args) {
        String phpstr = " BOXO[quote:2845990e65=\"suganthan\"]Help me! This is the string[/quote:2845990e65]";

        System.out.println(Utils.stripPHPBBQuotes(phpstr));
    }
}
