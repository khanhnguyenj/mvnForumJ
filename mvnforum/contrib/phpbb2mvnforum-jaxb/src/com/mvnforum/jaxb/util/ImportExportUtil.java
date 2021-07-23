package com.mvnforum.jaxb.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;

public class ImportExportUtil {

    static SimpleDateFormat datetimefmt = new SimpleDateFormat("yyyy-MM-dd  kk:mm:ss");

    static SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd");

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

    /*public static String wrapit(String text)
     {
     StringBuffer strbuf = new StringBuffer();
     strbuf.append('\'');
     for (int i=0; i<text.length(); i++)
     {
     if (text.charAt(i) == '\'' )
     strbuf.append("\\\'");
     else
     strbuf.append(text.charAt(i));         
     }      
     strbuf.append('\'');

     return strbuf.toString();
     }*/

    public static String wrapit(String text) {
        StringBuffer strbuf = new StringBuffer();
        //strbuf.append('\'');
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\'')
                strbuf.append("\'\'");
            else
                strbuf.append(text.charAt(i));
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
        result = result.replaceAll("(.*)\\[quote:[0-9,abcdef]*\\](.*)\\[/quote:[0-9,abcdef]*\\](.*)",
                "$1\\[quote=\\]$2\\[/quote\\]$3");
        // fancy quote
        result = result.replaceAll("(.*)\\[quote:[0-9,abcdef]*=\\\"(.*)\\\"\\](.*)\\[/quote:[0-9,abcdef]*\\](.*)",
                "$1\\[quote=\\\"$2\\\"\\]$3\\[/quote\\]$4");
        return result;
    }

    public static String HexIPtoString(String hexrep) {
        if (hexrep.length() < 8)
            return "0.0.0.0";

        byte[] ipaddr = new byte[8];
        for (int i = 0; i < ipaddr.length; i++) {
            String letter = "" + hexrep.charAt(i);
            int val = ((Integer) str2hex.get(letter)).intValue();
            ipaddr[i] = (byte) val;
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

        return "" + A + "." + B + "." + C + "." + D;
    }

    public static String stripPHPBBQuotes(String in) {
        boolean err = false;
        StringBuffer endstr = new StringBuffer();

        do {
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
        } while (false);

        if (err)
            return in;
        else
            return endstr.toString();
    }

    public static String dateTimeFormat(long s) {
        Date d = new Date(s * 1000);
        return datetimefmt.format(d);
    }

    public static String dateFormat(long s) {
        Date d = new Date(s * 1000);
        return datefmt.format(d);
    }

    public static String dateTimeFormat(Timestamp timestamp) {
        return datetimefmt.format(timestamp);
    }

    public static String dateFormat(Timestamp timestamp) {
        return datefmt.format(timestamp);
    }

    public static String getFormatDate(Date date) {
        return datefmt.format(date);
    }

    public static Timestamp getTimeStamp(long data) {
        return new Timestamp(data * 1000);
    }

    public static Timestamp string2TimeStamp(String value)
        throws ParseException {
        Date d = datetimefmt.parse(value);
        return new Timestamp(d.getTime());
    }
    
    public static java.sql.Date string2Date (String value) throws ParseException {
        return new java.sql.Date (datetimefmt.parse(value).getTime());
        
    }
}
