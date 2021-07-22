/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/XMLUtil.java,v 1.10 2009/12/03 11:17:39 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.10 $
 * $Date: 2009/12/03 11:17:39 $
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
 * @author: Igor Manic   
 */
package com.mvnforum.admin.importexport;

import java.sql.Timestamp;
import java.text.*;
import java.util.Locale;

import net.myvietnam.mvncore.util.DateUtil;

/**
 * @author Igor Manic
 * @version $Revision: 1.10 $, $Date: 2009/12/03 11:17:39 $
 * <br/>
 * <code>XMLUtil</code> todo Igor: enter description
 *
 */
public final class XMLUtil {

    private XMLUtil() {
    }

    /**
     * Parses integer value out of a string.
     * If the string is invalid, it throws <code>NumberFormatException</code>,
     * so a calling method knows of that issue.<br/>
     *
     * @param value <code>String</code> to be parsed
     * @param defaultValue Default <code>int</code> value if <code>value==null</code>
     *
     * @return Parsed <code>int</code> value.
     * @throws NumberFormatException If <code>value</code> is not valid number.
     *
     */
    public static int stringToIntDef(String value, int defaultValue)
        throws NumberFormatException {
        
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    //1=male, 0=female
    public static int stringToGender(String s) {
        if (s.equalsIgnoreCase("male") || s.equals("1")) {
            return 1;
        } else if (s.equalsIgnoreCase("female") || s.equals("0")) {
            return 0;
        } else {
            throw new IllegalArgumentException("Illegal gender string format.");
        }
    }

    public static int stringToGenderDef(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return stringToGender(value);
        }
    }

    public static String genderToString(int gender) {
        if (gender == 0) {
            return "0";       //or, is it better to return "female" ?
        } else if (gender==1) {
            return "1"; //"male"
        } else {
            throw new IllegalArgumentException("Illegal gender value.");
        }
    }

//todo Igor: add utility methods for IPs and permissions

    public static boolean stringToBoolean(String s) {
        if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equals("1")) {
            return true;
        } else if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equals("0")) {
            return false;
        } else {
            throw new IllegalArgumentException("Illegal boolean format.");
        }
    }

    public static boolean stringToBooleanDef(String value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return stringToBoolean(value);
        }
    }

    public static String booleanToString(boolean value) {
        if (value) {
            return "true";
        } else {
            return "false";
        }
    }

    public static java.sql.Date stringToSqlDate(String s) {
        /* I have to accept following formats:
         *    yyyy/MM/dd
         *    yyyy-MM-dd
         *    yyyyMMdd
         *    EEE MMM dd yyyy  (e.g.: "Fri Jan 16 2002")
         */
        if (s == null) {
            throw new IllegalArgumentException("null string");
        }
        s = s.trim();

        //SimpleDateFormat f1=new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        //SimpleDateFormat f2=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        //SimpleDateFormat f3=new SimpleDateFormat("yyyyMMdd", Locale.US);
        //SimpleDateFormat f4=new SimpleDateFormat("EEE MMM dd yyyy", Locale.US); //example: "Fri Jan 16 2002"
        try {
            //discover the format pattern to use for parsing
            SimpleDateFormat f =/*f3*/new SimpleDateFormat("yyyyMMdd", Locale.US);
            if (s.indexOf('/') > 0) {
                f =/*f1*/new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            } else if (s.indexOf('-') > 0) {
                f =/*f2*/new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            } else if (s.indexOf(' ') > 0) {
                f =/*f4*/new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
            }
            java.util.Date d = f.parse(s);
            return new java.sql.Date(d.getTime());
        } catch (ParseException e) {
            throw new java.lang.IllegalArgumentException("Invalid date format: \""+s+"\"");
        }
    }

    public static java.sql.Date stringToSqlDateDef(String value, java.sql.Date defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return stringToSqlDate(value);
        }
    }

    public static java.sql.Date stringToSqlDateDefNow(String value) {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        return stringToSqlDateDef(value, new java.sql.Date(now.getTime()));
    }

    public static java.sql.Date stringToSqlDateDefNull(String value) {
        /* todo Igor: important: must change this so it doesn't return now()
         * but null, as it should. For now, I must not return null, because
         * db.*WebHelper classes don't handle null dates correctly.
         * They should check if aDate is null and if it is, don't send null
         * to SQL engine (since database schema states it must be non-null),
         * but send empty string in the query, so SQL engine will do the rest.
        if (value==null) return null;
        else return stringToSqlDate(value);
         */
        return stringToSqlDateDefNow(value);
    }

    public static String sqlDateToString(java.sql.Date value) {
        DateFormat frm = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        return frm.format(value);
    }

    public static String sqlDateToStringDefNow(java.sql.Date value) {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        if (value == null) {
            return sqlDateToString(new java.sql.Date(now.getTime()));
        } else {
            return sqlDateToString(value);
        }
    }

    public static String sqlDateToStringDefEmpty(java.sql.Date value) {
        //todo Igor: should I return "0000-00-00 00:00:00" instead of ""? same for Timestamp
        if (value == null) {
            return "";
        } else {
            return sqlDateToString(value);
        }
    }

    
    public static java.sql.Timestamp stringToSqlTimestamp(String s) {
        /* I have to accept following formats:
         *    yyyy/MM/dd HH:mm:ss.nn (e.g.: "2009/11/23 11:19:48.782 CST")
         *    yyyy-MM-dd HH:mm:ss.nn (e.g.: "2009-11-23 11:19:48.782 CST")
         *    yyyyMMddHHmmssnn (e.g.: "20091123111948782 CST")
         *    EEE MMM dd HH:mm:ss z yyyy  (e.g.: "Fri Jan 16 18:48:25 CEST 2002")
         * In first three formats, last nn are hundreths and are optional
         */
        if (s == null) {
            throw new java.lang.IllegalArgumentException("null string");
        }
        s = s.trim();

        //SimpleDateFormat f1=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US); //may have extra ".nn" on the end (hundreths)
        //SimpleDateFormat f2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); //may have extra ".nn" on the end (hundreths)
        //SimpleDateFormat f3=new SimpleDateFormat("yyyyMMddHHmmss", Locale.US); //may have extra "nn" on the end (hundreths)
        //SimpleDateFormat f4=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US); //example: "Fri Jan 16 18:48:25 CEST 2002"
        try {
            //discover the format pattern to use for parsing
            SimpleDateFormat f=/*f3*/new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String timeZone = null;
            if (s.indexOf('/') > 0) {
                // timezone stand by 3 last digits
                timeZone = s.substring(s.length() - 3, s.length()).trim();
                s = s.substring(0, 19); //cut hundreths if they exist
                f=/*f1*/new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
            } else if (s.indexOf('-') > 0) {
                // timezone stand by 3 last digits
                timeZone = s.substring(s.length() - 3, s.length()).trim();
                s = s.substring(0, 19); //cut hundreths if they exist
                f = /*f2*/new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            } else if (s.indexOf(' ') > 0) {
                f = /*f4*/new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            } else {
                // timezone stand by 3 last digits
                timeZone = s.substring(s.length() - 3, s.length()).trim();
                s = s.substring(0, 14); //cut hundreths if they exist
                f = /*f3*/new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            }
            java.util.Date d = f.parse(s);
            int hourAdjustment = 0;
            if ("CDT".equals(timeZone)) {// CDT = GMT - 5
                hourAdjustment = 5;
            } else if ("CST".equals(timeZone)) {// CST = GMT - 6
                hourAdjustment = 6;
            }
            return new Timestamp(d.getTime() + (DateUtil.HOUR * hourAdjustment));
        } catch (StringIndexOutOfBoundsException e) {
            throw new java.lang.IllegalArgumentException("Invalid timestamp format: \""+s+"\"");
        } catch (ParseException e) {
            throw new java.lang.IllegalArgumentException("Invalid timestamp format: \""+s+"\"");
        }
    }

    public static java.sql.Timestamp stringToSqlTimestampDef(String value, java.sql.Timestamp defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return stringToSqlTimestamp(value);
        }
    }

    public static java.sql.Timestamp stringToSqlTimestampDefNow(String value) {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        return stringToSqlTimestampDef(value, now);
    }

    public static java.sql.Timestamp stringToSqlTimestampDefNull(String value) {
        /* todo Igor: important: must change this so it doesn't return now()
         * but null, as it should. For now, I must not return null, because
         * db.*WebHelper classes don't handle null dates correctly.
         * They should check if aTimestamp is null and if it is, don't send null
         * to SQL engine (since database schema states it must be non-null),
         * but send empty string in the query, so SQL engine will do the rest.
        if (value==null) return null;
        else return stringToSqlTimestamp(value);
         */
        return stringToSqlTimestampDefNow(value);
    }

    public static String sqlTimestampToString(java.sql.Timestamp value) {
        DateFormat frm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        return frm.format(value);
    }

    public static String sqlTimestampToStringDefNow(java.sql.Timestamp value) {
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        if (value == null) {
            return sqlTimestampToString(now);
        } else {
            return sqlTimestampToString(value);
        }
    }

    public static String sqlTimestampToStringDefEmpty(java.sql.Timestamp value) {
        if (value == null) {
            return "";
        } else {
            return sqlTimestampToString(value);
        }
    }

}
