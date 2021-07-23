/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum_method1/src/Utils.java,v 1.6 2009/12/03 08:39:01 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/12/03 08:39:01 $
 *
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

/* ====================================================================
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
 * @author: Luis Miguel Hernanz 
 * @author: Minh Nguyen  
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @author anandh
 */

public class Utils {
    
    static SimpleDateFormat datetimefmt = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");
    static SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd");
    
    static Hashtable str2hex = new Hashtable();
    static 
    {
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

    
    public static String wrapit(String text)
    {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append('\'');
        for (int i=0; i<text.length(); i++) {
            if (text.charAt(i) == '\'' ) {
                strbuf.append("\\\'");
            } else {
                strbuf.append(text.charAt(i));
            }
        }
        strbuf.append('\'');

        return strbuf.toString();
    }
    
    public static String HauDateTimeFromS(long s, int timeZone) {
        
        String prefix = "GMT";
        
        if (timeZone < 0) {
            prefix = prefix + "-";
            timeZone = -timeZone;
        } else {
            prefix = prefix + "+";
        }
        
        if (timeZone < 10) {
            prefix = prefix + "0";
        }
        
        prefix = prefix + timeZone + ":00";
        
        try {
            TimeZone.setDefault(TimeZone.getTimeZone(prefix));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        datetimefmt = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");
        
        Date d = new Date(s*1000);
        return datetimefmt.format(d);

    }
    
    public static String DateTimeFromS(long s)
    {
        Date d = new Date(s*1000);
        return datetimefmt.format(d);
    }
    
    public static String DateFromS(long s)
    {
        Date d = new Date(s*1000);
        return datefmt.format(d);
    }
    
    public static String HexIPtoString(String hexrep)
    {
        if (hexrep.length() < 8) {
            return "0.0.0.0";
        }
        
        byte []ipaddr = new byte[8];
        for (int i=0; i<ipaddr.length; i++) {
            String letter = "" + hexrep.charAt(i);
            int val = ((Integer)str2hex.get(letter)).intValue();
            ipaddr[i] = (byte)val;
        }

        String strrep = "";
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

        return "" + A + "."+ B + "." + C + "." + D;
    }
    
    public static String stripPHPBBQuotes(String in)
    {
        boolean err = false;
        StringBuffer endstr = new StringBuffer();
        
        do
        {
            int firstquote = in.indexOf("[quote:");
            if (firstquote == -1)
            {
                err = true;
                break;
            }

            int firstclosebrace = in.indexOf(']', firstquote);
            if (firstclosebrace == -1)
            {
                err = true;
                break;
            }
            
            endstr.append(in.substring(0,firstquote));
            endstr.append("[quote]");
            
            int endquote = in.indexOf("[/quote:");
            if (endquote == -1)
            {
                err = true;
                break;
            }
            
            int endclosebrace = in.indexOf(']', endquote);
            if (endclosebrace == -1)
            {
                err = true;
                break;
            }
            
            endstr.append(in.substring(firstclosebrace+1, endquote));
            endstr.append("[/quote]");
            endstr.append(in.substring(endclosebrace+1));
        }
        while(false);
        
        if (err) {
            return in;
        } else {
            return endstr.toString();
        }
    }
    
    public static String HauFormatTime(String str) {
        
        str = str.replaceFirst("24:", "00:");
        
        return str;
        
    }
    
    public static void main(String []args)
    {
//        String phpstr = " BOXO[quote:2845990e65=\"suganthan\"]Help me! This is the string[/quote:2845990e65]";
// 
//        System.out.println(Utils.stripPHPBBQuotes(phpstr));
//        time: 972086460
//        timezone: -7
//        thread 3: 1146518792
        
////        current timezone
//        System.out.println("current");
//        System.out.println(datetimefmt.getTimeZone());
//        TimeZone save = datetimefmt.getTimeZone();
//        
////        create new time zone
//        System.out.println("new");
//        TimeZone myTimeZone = TimeZone.getTimeZone("GMT-07:00");
//        System.out.println(myTimeZone);
//        
//        try {
////            set new time zone
//            System.out.println("set");
////            datetimefmt.setTimeZone(myTimeZone);
//            System.out.println("successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        System.out.println(datetimefmt.format(1146518792));
        
        
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-07:00"));
        datetimefmt = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");
        System.out.println(DateTimeFromS(1146518792));
    }
}

