/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/DateUtil.java,v 1.50 2009/06/18 09:08:39 tamdlt Exp $
 * $Author: tamdlt $
 * $Revision: 1.50 $
 * $Date: 2009/06/18 09:08:39 $
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
package net.myvietnam.mvncore.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import net.myvietnam.mvncore.MVNCoreConfig;

/**
 * @todo: add config option to this class
 */
public final class DateUtil {

    public static final long SECOND  = 1000;
    public static final long MINUTE  = SECOND * 60;
    public static final long HOUR    = MINUTE * 60;
    public static final long DAY     = HOUR * 24;
    public static final long WEEK    = DAY * 7;
    public static final long YEAR    = DAY * 365; // or 366 ???

    /**
     * This is the time difference between GMT time and Vietnamese time
     */
    public static final long GMT_VIETNAM_TIME_OFFSET = HOUR * 7;

    /**
     * RFC 822 date format, for RSS 2.0
     * Sat, 07 Sep 2002 00:00:01 GMT
     */
    public static final String RFC_822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    /**
     * ISO 8601 [W3CDTF] date format
     * 2005-06-05T14:52:57EDT
     * Note: not sure Z or z is correct
     */
    public static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * UTC style date format
     */
    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * This is the time difference between GMT time and SERVER time
     */
    //private static long SERVER_TIME_OFFSET = HOUR * (new DateOptions()).serverHourOffset;

    private static DateFormat ddMMyyyyFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat rfc822Format = new SimpleDateFormat(RFC_822_DATE_FORMAT, Locale.US);
    private static DateFormat iso8601Format = new SimpleDateFormat(ISO_8601_DATE_FORMAT, Locale.US);
    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
    private static DateFormat datetimeFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    private static DateFormat viDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale("vi", "VN"));
    private static DateFormat viDatetimeFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, new Locale("vi", "VN"));
    private static DateFormat headerTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    //private static DateFormat utcFormat = new SimpleDateFormat(UTC_DATE_FORMAT);

    static {
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        headerTimeFormat.setTimeZone(gmt);
    }

    /**
     * private constructor
     */
    private DateUtil() {// prevent instantiation
    }

    public static synchronized String getDateDDMMYYYY(Date date) {
        return ddMMyyyyFormat.format(date);
    }

    public static synchronized String getDateYYYYMMDD(Date date) {
        return yyyyMMddFormat.format(date);
    }

    public static synchronized String getDateRFC822(Date date) {
        return rfc822Format.format(date);
    }

    public static synchronized String getDateISO8601(Date date) {
        String formattedDate = iso8601Format.format(date);
        int length = formattedDate.length();
        if (length > 2) {
            return new StringBuffer(64).append(formattedDate.substring(0, length - 2)).append(":").append(formattedDate.substring(length - 2)).toString();
        }
        return formattedDate;
    }

    public static synchronized String getHTTPHeaderTime(Date date) {
        return headerTimeFormat.format(date);
    }

    public static synchronized String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static synchronized String formatDateTime(Date date) {
        return datetimeFormat.format(date);
    }

    public static synchronized String formatViDate(Date date) {
        return viDateFormat.format(date);
    }

    public static synchronized String formatViDateTime(Date date) {
        return viDatetimeFormat.format(date);
    }

    public static String getViDateTimeDesc() {
        return formatViDateTime(getVietnamDateFromGMTDate(getCurrentGMTTimestamp()));
    }

    public static String getViDateDesc() {
        return formatViDate(getVietnamDateFromGMTDate(getCurrentGMTTimestamp()));
    }

    public static Timestamp getCurrentGMTTimestamp() {
        return new Timestamp(System.currentTimeMillis() - (HOUR * MVNCoreConfig.getServerHourOffset()));
    }

    public static void updateCurrentGMTTimestamp(Timestamp timeToUpdate) {
        timeToUpdate.setTime(System.currentTimeMillis() - (HOUR * MVNCoreConfig.getServerHourOffset()));
    }

    public static Date getVietnamDateFromGMTDate(Date date) {
        return new Date(date.getTime() + GMT_VIETNAM_TIME_OFFSET);
    }

    /* Note that Timestamp is extended from Date
    public static Date getVietnamTimestampFromGMTTimestamp(Timestamp date) {
        return new Timestamp(date.getTime() + GMT_VIETNAM_TIME_OFFSET);
    }*/

    public static Date convertGMTDate(Date gmtDate, double hourOffset) {
        return new Date(gmtDate.getTime() + (long)(hourOffset*HOUR));
    }

    public static Timestamp convertGMTTimestamp(Timestamp gmtTimestamp, double hourOffset) {
        return new Timestamp(gmtTimestamp.getTime() + (long)(hourOffset*HOUR));
    }

    public static Timestamp getCurrentGMTTimestampExpiredYear(int offsetYear) {
        Timestamp currentTimestamp = getCurrentGMTTimestamp();
        Timestamp expiredYear = new Timestamp(currentTimestamp.getTime() + YEAR*offsetYear);
        return expiredYear;
    }

    public static Timestamp getCurrentGMTTimestampExpiredMonth(int offsetMonth) {
        Timestamp currentTimestamp = getCurrentGMTTimestamp();
        Timestamp expiredYear = new Timestamp(currentTimestamp.getTime() + DAY*30*offsetMonth);
        return expiredYear;
    }

    public static Timestamp getCurrentGMTTimestampExpiredDay(int offsetDay) {
        Timestamp currentTimestamp = getCurrentGMTTimestamp();
        Timestamp expiredYear = new Timestamp(currentTimestamp.getTime() + DAY*offsetDay);
        return expiredYear;
        /*
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE,offsetDay);
        return new Timestamp(now.getTime().getTime());
        */
    }

    public static String format(Date date, String pattern) {
        DateFormat formatter = new SimpleDateFormat (pattern, Locale.US);
        return formatter.format(date);
    }

    public static String formatDuration(long duration, String pattern) {
        DurationFormater time = new DurationFormater(duration, pattern);
        return time.toString();
    }

    public static String formatDuration(long duration) {
        DurationFormater time = new DurationFormater(duration, null);
        return time.toString();
    }

    public static Timestamp getStartDate(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
     
    public static Timestamp getEndDate(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getStartHour(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getEndHour(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
      
    public static Timestamp getStartMonth(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }    
    
    public static Timestamp getEndMonth(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getNextMonth(time).getTime() - 1);
        return new Timestamp(calendar.getTimeInMillis());
    }   

    
    public static Timestamp getNextMonth(Timestamp time) {        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        if (currentMonth < 11) {
            calendar.set(Calendar.MONTH, currentMonth + 1);
        } else {
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.YEAR, currentYear + 1);
        }
        return getStartMonth(new Timestamp(calendar.getTimeInMillis()));
    }

    public static Date convertTimestampToDate(Timestamp time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time.getTime());
        return new Date(calendar.getTimeInMillis());
    }

    /*
    public static void main (String[] agrs) {
        long duration = (long)1000 * 60 * 60 *24 * 130 + (long)1000 * 60 * 80;
        System.out.println(duration);
        System.out.println("Duration of " + duration + " duration = " + formatDuration(duration));
        Date d = new Date();
        System.out.println(" Date is " + DateUtil.getDateISO8601(d));
    }*/
    
}

class DurationFormater {
    public static final long MILLISECONDS_PER_SECOND = 1000;
    public static final long SECONDS_PER_MINUTE = 60;
    public static final long MINUTES_PER_HOUR = 60;
    public static final long HOURS_PER_DAY = 24;

    public static final int MILLISECOND = 0;
    public static final int SECOND      = 1;
    public static final int MINUTE      = 2;
    public static final int HOUR        = 3;
    public static final int DAY         = 4;


    public static final String[] PATTERNS = {
        "@ms", "@s", "@m", "@h", "@d"
    };
    private static final long[] AMOUNTS = {
        MILLISECONDS_PER_SECOND,
        SECONDS_PER_MINUTE,
        MINUTES_PER_HOUR,
        HOURS_PER_DAY
    };
    private static long[] times = new long[5];
    private long time;
    private String pattern;
    private boolean detail = false;

    DurationFormater() {
    }

    DurationFormater(long time, String pattern) {
        this.time = time;
        this.pattern = pattern;
        update();
    }

    DurationFormater(long time) {
        this.time = time;
        update();
    }

    private void update() {
        long remain = time;
        for (int i = 0; i < AMOUNTS.length; i++) {
            times[i] = remain % AMOUNTS[i];
            remain = remain / AMOUNTS[i];
        }
        times[DAY] = (int) remain;
    }

    /*  @h
     *  @M  --> Month
     *  @m  --> minute
     *  @ms --> millisecond
     *  @s  --> second
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long duration) {
        time = duration;
        update();
    }

    public long getMilliseconds() {
        return times[MILLISECOND];
    }

    public long getSeconds() {
        return times[SECOND];
    }

    public long getMinutes() {
        return times[MINUTE];
    }

    public long getHours() {
        return times[HOUR];
    }

    public long getDays() {
        return times[DAY];
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public String getString() {
        StringBuffer buffer = new StringBuffer(1024);
        buffer.append(pattern);
        for (int i = 0; i < PATTERNS.length; i++) {
            int start = -1;
            int end = -1;
            // Note, in JDK 1.3, StringBuffer does not have method indexOf
            while ((start = buffer.toString().indexOf(PATTERNS[i])) > -1) {
                end = start + PATTERNS[i].length();
                buffer.replace(start, end, String.valueOf(times[i]));
            }
        }
        return buffer.toString();
    }

    public String toString() {
        if (pattern != null) {
            return getString();
        }

        StringBuffer desc = new StringBuffer(256);
        if (times[DAY] > 0) {
            desc.append(checkPlural(times[DAY], "day"));
        }
        if (times[HOUR] > 0) {
            desc.append(checkPlural(times[HOUR], "hour"));
        }
        if ((times[MINUTE] > 0) || (times[DAY] == 0 && times[MINUTE] == 0)) {
            desc.append(checkPlural(times[MINUTE], "minute"));
        }
        if (detail) {
            desc.append(checkPlural(times[SECOND], "second"));
            desc.append(checkPlural(times[MILLISECOND], "millisecond"));
        }
        return desc.toString();
    }

    private static String checkPlural(long amount, String unit) {
        StringBuffer desc = new StringBuffer(20);
       desc.append(amount).append(" ").append(unit);
        if (amount > 1) {
            desc.append("s");
        }
        return desc.append(" ").toString();
    }
}
