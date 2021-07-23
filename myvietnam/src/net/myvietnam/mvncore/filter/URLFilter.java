/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/URLFilter.java,v 1.21 2009/07/15 09:02:22 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.21 $
 * $Date: 2009/07/15 09:02:22 $
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
 * @author: Anatol Pomozov (aka wassup) 
 * @author: Minh Nguyen 
 */
package net.myvietnam.mvncore.filter;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.service.EncoderService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

public final class URLFilter {

    public static final int MAX_CHARACTER_IN_SHOW_URL = 80;
    public static final int MAX_CHARACTER_IN_LEFT_CUT_SHOW_URL = 45;
    public static final int MAX_CHARACTER_IN_RIGHT_CUT_SHOW_URL = 25;
    
    static final boolean OPEN_NEW_WINDOW = true;
    
    private static EncoderService encoderService = MvnCoreServiceFactory.getMvnCoreService().getEncoderService();

    private URLFilter() { //prevent instantiation
    }

    /**
     * NOTE: For security, we should call DisableHtmlTagFilter before call this method
     * @param input the string to filter
     * @return the string after being filtered
     */
    public static String filter(String input) {
        if (input == null || input.length() == 0)
            return input;
        StringBuffer buf = new StringBuffer(input.length() + 25);
        char chars[] = input.toCharArray();
        int len = input.length();
        int index = -1;
        int i = 0;
        int j = 0;
        int oldend = 0;
        while (++index < len) {
            char cur = chars[i = index];
            j = -1;
            if ((cur == 'f' && index < len - 6 && chars[++i] == 't' && chars[++i] == 'p' ||
                 cur == 'h' && (i = index) < len - 7 && chars[++i] == 't' && chars[++i] == 't' && chars[++i] == 'p' && (chars[++i] == 's' || chars[--i] == 'p'))
                 && i < len - 4 && chars[++i] == ':' && chars[++i] == '/' && chars[++i] == '/')
                j = ++i;
            if (j > 0) {// check to process http:// or https:// or ftp://
                if (index == 0 || (cur = chars[index - 1]) != '\'' && cur != '"' && cur != '<' && cur != '=') {
                    cur = chars[j];
                    while (j < len) {
                        if (cur == ' ' || cur == '\t' || cur == '\'' ||
                            cur == '"' || cur == '<' || cur == '[' ||
                            cur == '\n' ||
                            cur == '\r' && j < len - 1 && chars[j + 1] == '\n')
                            break;
                        if (++j < len)
                            cur = chars[j];
                    }
                    cur = chars[j - 1];
                    if (cur == '.' || cur == ',' || cur == ')' || cur == ']')
                        j--;
                    buf.append(chars, oldend, index - oldend);
                    buf.append("<a href=\"");
                    String href = input.substring(index, j).trim();
                    //buf.append(chars, index, j - index);
                    buf.append(encoderService.filterUrl(href));
                    buf.append('"');
                    if (OPEN_NEW_WINDOW) {
                        buf.append(" target=\"_blank\"");
                    }
                    if (MVNCoreConfig.getEnableLinkNofollow()) {
                        buf.append(" rel=\"nofollow\"");
                    }
                    buf.append('>');
                    //buf.append(chars, index, j - index);
                    if (href.length() < MAX_CHARACTER_IN_SHOW_URL) {
                        buf.append(href);
                    } else if (href.indexOf('<') != -1) {
                        // it mean that we cannot trim string that contain a tag
                        // ex: [url=http://www.mvnforum.com] [color=green][b]Link[/b][/color][/url]
                        buf.append(href);
                    } else {
                        buf.append(href.substring(0, MAX_CHARACTER_IN_LEFT_CUT_SHOW_URL)).append("...").append(href.substring(href.length()-MAX_CHARACTER_IN_RIGHT_CUT_SHOW_URL, href.length()));
                    }
                    buf.append("</a>");
                } else {
                    buf.append(chars, oldend, j - oldend);
                }
                oldend = index = j;
            } else
            if (cur == '[' && index < len - 6 && chars[i = index + 1] == 'u' && chars[++i] == 'r' && chars[++i] == 'l' &&
                (chars[++i] == '=' || chars[i] == ' ')) {
                // process [url]
                j = ++i;
                int u2;
                int u1 = u2 = input.indexOf("]", j);
                if (u1 > 0) {
                    u2 = input.indexOf("[/url]", u1 + 1);
                }
                if (u2 < 0) {
                    buf.append(chars, oldend, j - oldend);
                    oldend = j;
                } else {
                    buf.append(chars, oldend, index - oldend);
                    buf.append("<a href=\"");
                    String href = input.substring(j, u1).trim();
                    // Add http:// to the front of links if and only if it doesn't have any protocols.
                    // Doing this handles this: "[url=sun.com]SUN[/url]"
                    // Changing it to <a href="http://sun.com">SUN</a>
                    // instead of <a href="http://localhost:8080/mvnforum/sun.com">SUN</a>
                    if ( (href.indexOf("://") == -1) && (href.startsWith("mailto:") == false) ) {
                        href = "http://" + href;
                    }
                    if (href.indexOf("javascript:") == -1 && href.indexOf("file:") == -1) {
                        buf.append(encoderService.filterUrl(href));
                    }
                    if (OPEN_NEW_WINDOW) {
                        buf.append("\" target=\"_blank");
                    }
                    if (MVNCoreConfig.getEnableLinkNofollow()) {
                        buf.append(" rel=\"nofollow\"");
                    }
                    buf.append("\">");
                    String showURL = input.substring(u1 + 1, u2).trim();
                    if (showURL.length() < MAX_CHARACTER_IN_SHOW_URL) {
                        buf.append(showURL);
                    } else if (showURL.indexOf('<') != -1) {
                        // it mean that we cannot trim string that contain a tag
                        // ex: [url=http://www.mvnforum.com] [color=green][b]Link[/b][/color][/url]
                        buf.append(showURL);
                    } else {
                        buf.append(showURL.substring(0, MAX_CHARACTER_IN_LEFT_CUT_SHOW_URL)).append("...").append(showURL.substring(showURL.length()-MAX_CHARACTER_IN_RIGHT_CUT_SHOW_URL, showURL.length()));
                    }
                    buf.append("</a>");
                    oldend = u2 + 6; // 6 == length of [/url]
                }
                index = oldend - 1;// set to the last char of the tag, that is ']'
            } else
            if (cur == '[' && index < len - 6 && chars[i = index + 1] == 'i' && chars[++i] == 'm' && chars[++i] == 'g' && chars[++i] == ']' ) {
                //process [img]
                j = ++i;
                int u1 = j-1;
                int u2 = input.indexOf("[/img]", u1 + 1);
                if (u2 < 0) {
                    buf.append(chars, oldend, j - oldend);
                    oldend = j;
                } else {
                    buf.append(chars, oldend, index - oldend);
                    buf.append("<img alt=\"\" src=\"");
                    String href = input.substring(u1 + 1, u2).trim();
                    // Add http:// to the front of links if and only if it doesn't have any protocols.
                    // Doing this handles this: "[url=sun.com]SUN[/url]"
                    // Changing it to <a href="http://sun.com">SUN</a>
                    // instead of <a href="http://localhost:8080/mvnforum/sun.com">SUN</a>
                    if (href.indexOf("://") == -1) {
                        href = "http://" + href;
                    }
                    if (href.indexOf("javascript:") == -1 && href.indexOf("file:") == -1) {
                        buf.append(encoderService.filterUrl(href));
                    }
                    buf.append("\" border=\"0\" />");
                    oldend = u2 + 6; // 6 == length of [/img]
                }
                index = oldend -1;// set to the last char of the tag, that is ']'
            }
        }
        if (oldend < len)
            buf.append(chars, oldend, len - oldend);
        return buf.toString();
    }
/*
    public static void main(String[] args) {
        //encodePath("localhost:8080/path/index.jsp");
        String[] input = {
            "[url=mailto:test@yahoo.com]Minh[/url][img]http://localhost:8080/mvnforum/mvnplugin/mvnforum/images/logo.gif[/img]",
            "-dfadg=[img] \" onmousemove=\"alert(1); [/img]",
            "(= http://a\"onmouseover='alert(1);')",
            "[url=http://sun.com]SUN[/url] http://sun.com",
            "[url sun.com]SUN[/url]", //What to do if no http???
            "[url=javascript:alert(1);]SUN[/url]",
            "[url=\" onmousemove=\"alert(1);]Hack[/url]",
            "[url=\" onmousemove='alert(1);']Hack[/url]"//somebody wants to hack us
        };

        //URLFilter enableMVNCodeFilter = new URLFilter();
        long start = System.currentTimeMillis();

        for (int i = 0; i < input.length; i++) {
            System.out.println("input = '" + input[i] + "' length = " + input[i].length());

            String output = null;
            for (int j = 0; j < 1; j++) {
                output = URLFilter.filter(input[i]);
            }

            System.out.println("output= '" + output + "'");
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("total time = " + time);
    }
 */
    /*
    public static String enableImg(String input) {
        String output = input;
        try {
            RE exp = new RE("(.*)\\[img\\](.*)\\[\\/img\\](.*)");
            boolean matched = exp.match(input);
            if (matched) {
                String front = new String();
                String back = new String();
                String matchedPattern = new String();

                front = exp.getParen(1);
                matchedPattern = exp.getParen(2);
                matchedPattern = "<img src=\"" + matchedPattern + "\" border=0 >";
                back = exp.getParen(3);

                output = front + matchedPattern + back;

                //log.info("image path is: " + output);
            }
        } catch (RESyntaxException e) {
            //log.info(e.getMessage());
        }
        return output;
    }*/

    /*
    public static void main1(String[] args) {
        URLFilter enableMVNCodeFilter = new URLFilter();
        long start = System.currentTimeMillis();

        String input = "[img]http://loclahost/test[/img] [img]http://[/img]";
        System.out.println("input = '" + input + "' length = " + input.length());

        String output = null;
        for (int j = 0; j < 1; j++) {
            output = enableMVNCodeFilter.enableImg(input);
        }

        System.out.println("output= '" + output + "'");

        long time = System.currentTimeMillis() - start;
        System.out.println("total time = " + time);
    }
    */
}
