/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/ParamUtil.java,v 1.35 2009/12/05 08:15:25 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.35 $
 * $Date: 2009/12/05 08:15:25 $
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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;

public final class ParamUtil {

    private ParamUtil() { // prevent instantiation
    }

    //private static String contextPath = (new ParamOptions()).contextPath;
    //private static String serverPath = (new ParamOptions()).serverPath;//@todo combine 2 line to a static block

    private static DateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy");
    
    private static String contextPath;

    public static String getContextPath() {
        return MVNCoreConfig.getContextPath();
    }
    public static void setContextPath(String path) {
        contextPath = path;
    }

    public static String getServerPath() {
        return MVNCoreConfig.getServerPath();
    }

    public static String getServer(HttpServletRequest request) {
        
        StringBuffer server = new StringBuffer(128);
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        server.append(scheme);
        server.append ("://");
        server.append(request.getServerName());
        if ( (scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443)) ) {
            server.append(':');
            server.append(port);
        }
        return server.toString();
    }

    public static String getServer2(HttpServletRequest request) {

        StringBuffer server = new StringBuffer(128);
        server.append(request.getScheme());
        server.append ("://");
        server.append(request.getHeader("host"));
        return server.toString();
    }

    public static String getParameter(HttpServletRequest request, String param) {
        
        String ret = request.getParameter(param);
        if (ret == null) {
            return "";
        }
        return ret.trim();
    }

    public static String getParameterFilter(HttpServletRequest request, String param) {
        return DisableHtmlTagFilter.filter(getParameter(request, param));
    }

    public static String getParameter(HttpServletRequest request, String param, boolean checkEmpty)
        throws BadInputException {

        String ret = request.getParameter(param);
        if (ret == null) {
            ret = "";
        }
        ret = ret.trim();
        if ( checkEmpty && (ret.length() == 0) ) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.not_allow_to_be_empty", new Object[] {DisableHtmlTagFilter.filter(param)});
            throw new BadInputException(localizedMessage);
        }
        return ret;
    }

    public static String getParameterFilter(HttpServletRequest request, String param, boolean checkEmpty)
        throws BadInputException {
        return DisableHtmlTagFilter.filter(getParameter(request, param, checkEmpty)); 
    }

    /** @todo review this method */
    public static String getParameterSafe(HttpServletRequest request, String param, boolean checkEmpty)
        throws BadInputException {

        String ret = getParameter(request, param, checkEmpty);
        if ( (ret.indexOf('<') != -1) ||
             (ret.indexOf('>') != -1)) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.parameter_safe", new Object[] {DisableHtmlTagFilter.filter(param)});
            throw new BadInputException(localizedMessage);
        }
        return ret;
    }

    public static int getParameterInt(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            int ret = Integer.parseInt(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "int"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static int getParameterUnsignedInt(HttpServletRequest request, String param)
        throws BadInputException {

        int retValue = getParameterInt(request, param);
        if (retValue < 0) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.must_be_unsigned_value", new Object[] {DisableHtmlTagFilter.filter(param)});
            throw new BadInputException(localizedMessage);
        }
        return retValue;
    }

    public static int getParameterInt(HttpServletRequest request, String param, int defaultValue)
        throws BadInputException {

        String inputStr = getParameter(request, param, false);
        if (inputStr.length() == 0) {
            return defaultValue;
        }
        try {
            int ret = Integer.parseInt(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "int"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static int getParameterUnsignedInt(HttpServletRequest request, String param, int defaultValue)
        throws BadInputException {

        int retValue = getParameterInt(request, param, defaultValue);
        if (retValue < 0) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.must_be_unsigned_value", new Object[] {DisableHtmlTagFilter.filter(param)});
            throw new BadInputException(localizedMessage);
        }
        return retValue;
    }

    public static long getParameterLong(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            long ret = Long.parseLong(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "long"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static long getParameterLong(HttpServletRequest request, String param, long defaultValue)
        throws BadInputException {

        String inputStr = getParameter(request, param, false);
        if (inputStr.length() == 0) {
            return defaultValue;
        }

        try {
            long ret = Long.parseLong(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "long"});
            throw new BadInputException(localizedMessage);
        }
    }

    /**
     * @param  param is the name of variable
     * @return true if the value of param is not empty
     */
    public static boolean getParameterBoolean(HttpServletRequest request, String param) {

        String inputStr = getParameter(request, param);
        if (inputStr.length() == 0) {
            return false;
        }
        return true;
    }

    public static byte getParameterByte(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            byte ret = Byte.parseByte(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "byte"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static double getParameterDouble(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            double ret = Double.parseDouble(inputStr);
            return ret;
        } catch (NumberFormatException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[] {DisableHtmlTagFilter.filter(param), "double"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static String getParameterUrl(HttpServletRequest request, String param)
        throws BadInputException {

        String ret = getParameter(request, param);
        if ( ret.length() > 0 ) {
            if ( !ret.startsWith("http://") &&
                 !ret.startsWith("https://") &&
                 !ret.startsWith("ftp://") ) {
                Locale locale = I18nUtil.getLocaleInRequest(request);
                String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.not_url", new Object[] {DisableHtmlTagFilter.filter(param)});
                throw new BadInputException(localizedMessage);
            }
        }
        return DisableHtmlTagFilter.filterForURL(ret);
    }

    public static String getParameterURI(HttpServletRequest request, String param)
        throws BadInputException {
    
        String ret = getParameter(request, param);
        if ( ret.length() > 0 ) {
            try {
                new URL(ret);
            } catch (MalformedURLException e) {
                Locale locale = I18nUtil.getLocaleInRequest(request);
                String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.not_uri", new Object[] {DisableHtmlTagFilter.filter(param)});
                throw new BadInputException(localizedMessage);
            }
        }
        return DisableHtmlTagFilter.filterForURL(ret);
    }

    public static String getParameterPassword(HttpServletRequest request, String param, int minLength, int option)
        throws BadInputException {

        if (minLength < 1) {
            minLength = 1;
        }
        String ret = request.getParameter(param);
        if (ret == null) {
            ret = "";
        }
        ret = ret.trim();

        if ( ret.length() < minLength ) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.password_too_short", new Object[] {new Integer(minLength)});
            throw new BadInputException(localizedMessage);
        }

        /** @todo implement this feature */
        if (option == 1) {//char and number

        } else if (option == 2) {// lower char, upper char and number

        }
        return ret;
    }

    public static String getParameterEmail(HttpServletRequest request, String param)
        throws BadInputException {
        
        String email = getParameterSafe(request, param, true);

        Locale locale = I18nUtil.getLocaleInRequest(request);
        MailUtil.checkGoodEmail(email, locale);
        
        return email;
    }

    /**
     *
     */
    public static java.sql.Date getParameterDate(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            java.util.Date ret = dateFormat.parse(inputStr);
            return new java.sql.Date(ret.getTime());
        } catch (java.text.ParseException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[]{DisableHtmlTagFilter.filter(param), "Date"});
            throw new BadInputException(localizedMessage);
        }
    }

    /**
     *
     */
    public static java.util.Date getParameterDateUtil(HttpServletRequest request, String param)
        throws BadInputException {

        String inputStr = getParameter(request, param, true);
        try {
            java.util.Date ret = dateFormat.parse(inputStr);
            return ret;
        } catch (java.text.ParseException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[]{DisableHtmlTagFilter.filter(param), "Date"});
            throw new BadInputException(localizedMessage);
        }
    }

    /**
     *
     */
    public static java.sql.Date getParameterDate(HttpServletRequest request, String paramDay, String paramMonth, String paramYear)
        throws BadInputException {

        int day = getParameterInt(request, paramDay);
        int month = getParameterInt(request, paramMonth);
        int year = getParameterInt(request, paramYear);
        StringBuffer buffer = new StringBuffer();
        buffer.append(day).append("/").append(month).append("/").append(year);
        String inputStr = buffer.toString();

        try {
            java.util.Date ret = dateFormat.parse(inputStr);
            return new java.sql.Date(ret.getTime());
        } catch (java.text.ParseException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_parse", new Object[]{DisableHtmlTagFilter.filter(inputStr), "Date"});
            throw new BadInputException(localizedMessage);
        }
    }

    public static double getParameterTimeZone(HttpServletRequest request, String param)
        throws BadInputException {

        double timeZone = getParameterDouble(request, param);
        if (timeZone < -12 || timeZone > 13) {
            timeZone = 0;
        }
        return timeZone;
    }

    public static String getAttribute(HttpSession session, String name) {

        String ret = (String)session.getAttribute(name);
        if (ret == null) {
            return "";
        }
        return ret.trim();
    }

    /**
     * Note: sometime in the dispatched request, we don't receive HttpServletRequest
     * but receive ServletRequest, such as com.caucho.server.webapp.DispatchRequest
     *
     * @param request ServletRequest
     * @param name String
     * @return String
     */
    public static String getAttribute(ServletRequest request, String name) {

        String ret = (String)request.getAttribute(name);
        if (ret == null) {
            return "";
        }
        return ret.trim();
    }

    /**
     * Note: we have to use this method because (very strange) that the
     * HttpServletRequest object does not accept above method
     * getAttribute(ServletRequest request, String name)
     *
     * @param request HttpServletRequest
     * @param name String
     * @return String
     */
    public static String getAttribute(HttpServletRequest request, String name) {

        String ret = (String)request.getAttribute(name);
        if (ret == null) { 
            return "";
        }
        return ret.trim();
    }

}
