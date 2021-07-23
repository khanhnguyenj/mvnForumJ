/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/MyUtil.java,v 1.159 2010/06/22 09:00:27 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.159 $
 * $Date: 2010/06/22 09:00:27 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.*;

import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.*;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.auth.*;
import com.mvnforum.common.ForumIconLegend;
import com.mvnforum.common.ThreadIconLegend;
import com.mvnforum.db.*;
import com.mvnforum.service.MvnForumInfoService;
import com.mvnforum.service.MvnForumServiceFactory;

import freemarker.template.*;

public final class MyUtil {

    private static final Logger log = LoggerFactory.getLogger(MyUtil.class);

    public static final String[] KEY_WORDS_OF_MOBILE_DEVICE = new String[] {"phone", "fone", "midp", "palm", "windows ce", "mmp", "mobile", "pda", "wii", "nintendo", "symbian", "j2me"};

    private static RankCache rankCache = RankCache.getInstance();

    private static MvnForumInfoService mvnForumInfo = MvnForumServiceFactory.getMvnForumService().getMvnForumInfoService();
    private static MvnCoreInfoService  mvnCoreInfo  = MvnCoreServiceFactory.getMvnCoreService().getMvnCoreInfoService();
    private static EnvironmentService  environment  = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
    
    public static final int MAX_TEXT_AREA_WIDTH  = 2000;
    public static final int MAX_TEXT_AREA_HEIGHT = 1000;
    public static final int MIN_TEXT_AREA_WIDTH  = 100;
    public static final int MIN_TEXT_AREA_HEIGHT = 100;
    
    //DEFAULT_EDITOR_MODE should hold either "bbEditor" or "richTextEditor" only
    private static final String DEFAULT_EDITOR_MODE = "bbEditor";
    
    private MyUtil() {
    }

    /**
     * We need to use URLFilter.filter() first to avoid very long string parsed by EnableMVNCodeFilter.filter().
     * because if EnableMVNCodeFilter.filter() is called first, then the return string is too long 
     * (longer than URLFilter.MAX_CHARACTER_IN_SHOW_URL) and will cause some unexpected effect when URLFilter filter it.
     * Examples: this string will be parsed incorrectly if EnableMVNCodeFilter is called before URLFilter
     * [url=http://10.0.0.68:18080][font=arial][size=5][s][u][i][b]123[/b][/i][/u][/s][/size][/font][/url]
     **/
    public static String filter(String input, boolean enableHTML, boolean enableEmotion,
                                boolean enableMVNCode, boolean enableNewLine, boolean enableURL) {
        String output = input;

        if (enableHTML) {
            output = EnableHtmlTagFilter.filter(output);
        } else {
            output = DisableHtmlTagFilter.filter(output);
        }
        if (enableEmotion) {
            output = EnableEmotionFilter.filter(output, ParamUtil.getContextPath() + MVNForumGlobal.EMOTION_DIR);
        }
        if (enableMVNCode) {
            output = EnableMVNCodeFilter.filter(output);
        }
        if (enableNewLine) {
            output = HtmlNewLineFilter.filter(output);
        }
        if (enableURL) {
            output = URLFilter.filter(output);
        }
        return output;
    }
    
    public static String filterWithWordWrap(String input, boolean enableHTML, boolean enableEmotion,
                                            boolean enableMVNCode, boolean enableNewLine, boolean enableURL) {
        
        String output = StringUtil.wordwrap(input, 100, "\n");
        
        output = filter(output, enableHTML, enableEmotion, enableMVNCode, enableNewLine, enableURL);
        
        return output;
    }

    public static String filterWithoutBBCode(String input, boolean enableHTML, boolean enableEmotion,
                                             boolean enableMVNCode, boolean enableNewLine, boolean enableURL) {

        String output = input;
        
        if (enableHTML) {
            output = EnableHtmlTagFilter.filter(output);
        } else {
            output = DisableHtmlTagFilter.filter(output);
        }
        
        if (enableMVNCode) {
            output = EnableMVNCodeFilter.filter(input);
        }
        
        output = EnableMVNCodeFilter.removeBBCode(input);
        
        if (enableEmotion) {
            output = EnableEmotionFilter.filter(output, ParamUtil.getContextPath() + MVNForumGlobal.EMOTION_DIR);
        }
        
        if (enableNewLine) {
            output = HtmlNewLineFilter.filter(output);
        }
        
        if (enableURL) {
            output = URLFilter.filter(output);
        }
        return output;
    }
    
    public static String filterForFlashPlayer(String input) {
        String output = input;
        output = output.replace("?", "%3F");
        output = output.replace("=", "%3D");
        output = output.replace("&", "%26");
        
        return output;
    }
    
    public static String getMemberTitle(int postCount) {
        String title = "";
        try {
            ArrayList rankBeans = rankCache.getBeans();
            for (int i = 0; i < rankBeans.size(); i++) {
                RankBean rankBean = (RankBean)rankBeans.get(i);
                if (rankBean.getRankMinPosts() <= postCount) {
                    title = EnableMVNCodeFilter.filter(rankBean.getRankTitle());
                } else {
                    break;
                }
            }//for
        } catch (Exception ex) {
            log.error("Exception in getMemberTitle", ex);
        }
        return title;
    }

    public static String getForumIconName(long lastLogon, long lastPost, int forumStatus, int forumThreadCount) {
        String forumIcon = null;
        if ( (lastLogon <= lastPost) && (forumThreadCount > 0) ) {// new post
            if (forumStatus == ForumBean.FORUM_STATUS_DEFAULT) {
                forumIcon = ForumIconLegend.FORUM_ICON_UNREAD_ACTIVE;
            } else if (forumStatus == ForumBean.FORUM_STATUS_CLOSED) {
                forumIcon = ForumIconLegend.FORUM_ICON_UNREAD_CLOSED;
            } else if (forumStatus == ForumBean.FORUM_STATUS_LOCKED) {
                forumIcon = ForumIconLegend.FORUM_ICON_UNREAD_LOCKED;
            } else if (forumStatus == ForumBean.FORUM_STATUS_DISABLED) {
                forumIcon = ForumIconLegend.FORUM_ICON_UNREAD_DISABLED;
            }
        } else {// no new post
            if (forumStatus == ForumBean.FORUM_STATUS_DEFAULT) {
                forumIcon = ForumIconLegend.FORUM_ICON_READ_ACTIVE;
            } else if (forumStatus == ForumBean.FORUM_STATUS_CLOSED) {
                forumIcon = ForumIconLegend.FORUM_ICON_READ_CLOSED;
            } else if (forumStatus == ForumBean.FORUM_STATUS_LOCKED) {
                forumIcon = ForumIconLegend.FORUM_ICON_READ_LOCKED;
            } else if (forumStatus == ForumBean.FORUM_STATUS_DISABLED) {
                forumIcon = ForumIconLegend.FORUM_ICON_READ_DISABLED;
            }
        }
        if (forumIcon == null) {
            log.warn("Cannot get the forumIcon");
            forumIcon = ForumIconLegend.FORUM_ICON_READ_ACTIVE;
        }
        return forumIcon;
    }

    public static String getThreadIconName(long lastLogon, long lastPost, int postCount, int threadStatus) {
        String threadIcon = null;
        if (postCount < MVNForumConfig.maxHotTopics()) {//not hot topic
            if (lastLogon > lastPost) {// no new post
                if (threadStatus == ThreadBean.THREAD_STATUS_DEFAULT) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_READ_ACTIVE;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_CLOSED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_READ_CLOSED;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_LOCKED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_READ_LOCKED;
                }

            } else {// new post
                if (threadStatus == ThreadBean.THREAD_STATUS_DEFAULT) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_UNREAD_ACTIVE;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_CLOSED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_UNREAD_CLOSED;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_LOCKED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_COLD_UNREAD_LOCKED;
                }
            }
        } else {// hot topic
            if (lastLogon > lastPost) {// no new post
                if (threadStatus == ThreadBean.THREAD_STATUS_DEFAULT) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_READ_ACTIVE;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_CLOSED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_READ_CLOSED;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_LOCKED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_READ_LOCKED;
                }
            } else {// new post
                if (threadStatus == ThreadBean.THREAD_STATUS_DEFAULT) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_UNREAD_ACTIVE;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_CLOSED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_UNREAD_CLOSED;
                } else if (threadStatus == ThreadBean.THREAD_STATUS_LOCKED) {
                    threadIcon = ThreadIconLegend.THREAD_ICON_HOT_UNREAD_LOCKED;
                }
            }
        }
        if (threadIcon == null) {
            log.warn("Cannot get the threadIcon");
            threadIcon = ThreadIconLegend.THREAD_ICON_COLD_READ_ACTIVE;
        }
        return threadIcon;
    }

    public static boolean canViewAtLeastOneForumInCategory(int categoryID, MVNForumPermission permission) {
        try {
            Collection forumBeans = ForumCache.getInstance().getBeans();
            for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
                ForumBean forumBean = (ForumBean)iter.next();
                if (forumBean.getCategoryID() == categoryID) {
                    if (canViewForum(forumBean, permission)) {
                        return true;
                    }
                }
            }
            if (environment.getForumRunMode() != EnvironmentService.PRODUCT_ENTERPRISE) {
                return false;
            }
            Collection categoryBeans = CategoryCache.getInstance().getBeans();
            for (Iterator iterator = categoryBeans.iterator(); iterator.hasNext();) {
                CategoryBean category = (CategoryBean) iterator.next();
                if (category.getParentCategoryID() == categoryID) {
                    if (canViewAtLeastOneForumInCategory(category.getCategoryID(), permission)) {
                        return true;
                    }
                }
            }
        } catch (DatabaseException ex) {
            log.error("Cannot load the data in table Forum", ex);
        }
        return false;
    }

    public static boolean canViewForum(ForumBean forumBean, MVNForumPermission permission) {
        if (permission.canReadPost(forumBean.getForumID()) &&
            (forumBean.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
            return true;
        }
        return false;
    }

    public static int getViewablePosts(Collection forumBeans, MVNForumPermission permission) {
        int count = 0;
        for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
            ForumBean forumBean = (ForumBean)iter.next();
            if (canViewForum(forumBean, permission)) {
                count += forumBean.getForumPostCount();
            }
        }
        return count;
    }

    public static int getViewableThreads(Collection forumBeans, MVNForumPermission permission) {
        int count = 0;
        for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
            ForumBean forumBean = (ForumBean)iter.next();
            if (canViewForum(forumBean, permission)) {
                count += forumBean.getForumThreadCount();
            }
        }
        return count;
    }

    public static int getViewableForums(Collection forumBeans, MVNForumPermission permission) {
        int count = 0;
        for (Iterator iter = forumBeans.iterator(); iter.hasNext(); ) {
            ForumBean forumBean = (ForumBean)iter.next();
            if (canViewForum(forumBean, permission)) {
                count++;
            }
        }
        return count;
    }

    public static int getViewableCategories(Collection categoryBeans, MVNForumPermission permission) {
        int count = 0;
        for (Iterator iter = categoryBeans.iterator(); iter.hasNext(); ) {
            CategoryBean categoryBean = (CategoryBean)iter.next();
            if (canViewAtLeastOneForumInCategory(categoryBean.getCategoryID(), permission)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the String with a slash character '/' before the locale name
     * @param localeName the user's preferred locale
     * @return the String with a slash character '/' before the locale name if
     *         this locale is configed to support it. Otherwise,
     *         an empty String will be return
     */
    public static String getLocaleNameAndSlash(String localeName) {
        if ( (localeName == null) || (localeName.length() == 0) ) {
            return "";
        }

        String retValue = "";
        String[] supportedLocales = MVNForumConfig.getSupportedLocaleNames();

        if (supportedLocales == null) {
            log.error("Assertion in MyUtil.getLocaleNameAndSlash. Please check your configuration.");
            return "";
        }

        for (int i = 0; i < supportedLocales.length; i++) {
            if (localeName.equals(supportedLocales[i])) {
                retValue = "/" + localeName;
                break;
            }
        }
        return retValue;
    }

    public static void ensureCorrectCurrentPassword(HttpServletRequest request)
        throws BadInputException, DatabaseException, AuthenticationException {

        GenericRequest genericRequest = new GenericRequestServletImpl(request, request.getSession().getServletContext());
        ensureCorrectCurrentPassword(genericRequest);
    }
    
    public static void ensureCorrectCurrentPassword(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        String password = "";
        String passwordMD5 = GenericParamUtil.getParameter(request, "md5pw", false);

        // minhnn: on 22 June 2010: I think that customized authentication could allow encrypted password in browser
        //         because external user could already have MD5 password or even plain password
        if ( (passwordMD5.length() == 0) ||
             (passwordMD5.endsWith("==") == false) ||
             (onlineUser.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION) ) {
            password = GenericParamUtil.getParameterPassword(request, "MemberCurrentMatkhau", 3, 0);
        }

        ensureCorrectCurrentPassword(request, password, passwordMD5);
    }

    public static void ensureCorrectCurrentPassword(GenericRequest request, String password, String passwordMD5)
        throws BadInputException, DatabaseException, AuthenticationException {

        if (password == null) {
            password = "";
        }
        if (passwordMD5 == null) {
            passwordMD5 = "";
        }

        OnlineUser onlineUser = OnlineUserManager.getInstance().getOnlineUser(request);
        OnlineUserFactory onlineUserFactory = ManagerFactory.getOnlineUserFactory();
        String memberName = onlineUser.getMemberName();

        try {
            if (onlineUser.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_REALM) {
                onlineUserFactory.ensureCorrectPassword(memberName, OnlineUserManager.PASSWORD_OF_METHOD_REALM, true);
            } else if (onlineUser.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_CUSTOMIZATION) {
                /*
                if (MVNForumConfig.getEnablePasswordlessAuth()) {
                    // don't need password
                    onlineUserFactory.ensureCorrectPassword(memberName, OnlineUserManager.PASSWORD_OF_METHOD_CUSTOMIZATION, true);
                } else {
                    // must have password
                    // @todo: implement this case by using Authenticator
                    onlineUserFactory.ensureCorrectPassword(memberName, OnlineUserManager.PASSWORD_OF_METHOD_CUSTOMIZATION, true);
                }*/
                //String password = GenericParamUtil.getParameterPassword(request, "MemberCurrentMatkhau", 3, 0);
                if (password.length() < 3) {
                    Locale locale = I18nUtil.getLocaleInRequest(request);
                    String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.password_too_short", new Object[] {new Integer(3)});
                    throw new BadInputException(localizedMessage);
                }
                boolean isCorrectPassword = ManagerFactory.getAuthenticator().isCorrectCurrentPassword(memberName, password, false);
                if (isCorrectPassword == false) {
                    throw new AuthenticationException(NotLoginException.WRONG_PASSWORD);
                }
            } else {
                //This user did not login by REALM or CUSTOMIZATION
                String memberPassword = "";
                //String passwordMD5 = GenericParamUtil.getParameter(request, "md5pw", false);
                if (passwordMD5.length() == 0 || (passwordMD5.endsWith("==") == false)) {
                    // md5 is not valid, try to use unencoded password method
                    //memberPassword = GenericParamUtil.getParameterPassword(request, "MemberCurrentMatkhau", 3, 0);
                    if (password.length() < 3) {
                        Locale locale = I18nUtil.getLocaleInRequest(request);
                        String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.password_too_short", new Object[] {new Integer(3)});
                        throw new BadInputException(localizedMessage);
                    }
                    memberPassword = password;

                    AssertionUtil.doAssert(memberPassword.length() > 0, "Cannot allow memberPassword's length is 0. Serious Assertion Failed.");
                }

                // Please note that below code ONLY CORRECT when the ParamUtil.getParameterPassword
                // return a NON-EMPTY string
                if (memberPassword.length() > 0) {
                    // that is we cannot find the md5 password
                    onlineUserFactory.ensureCorrectPassword(memberName, memberPassword, false);
                } else {
                    // have the md5, go ahead
                    onlineUserFactory.ensureCorrectPassword(memberName, passwordMD5, true);
                }
            }
        } catch (AuthenticationException e) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.wrong_password");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("You have typed the wrong password. Cannot proceed.");
        }
    }

    public static void writeMvnForumImage(HttpServletResponse response) throws IOException {

        BufferedImage image = mvnForumInfo.getImage();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            //encoder.encode(image);
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
    }

    public static void writeMvnCoreImage(HttpServletResponse response) throws IOException {

        BufferedImage image = mvnCoreInfo.getImage();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

//          JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            //encoder.encode(image);
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) { 
                    // ignore
                }
            }
        }
    }

    public static void checkClassName(Locale locale, String className, boolean required) throws BadInputException {
        
        if (required == false) {
            if (className.length() == 0) {
                return;
            }
        }

        try {
            Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new BadInputException("Cannot load class: " + className);
        }
    }

    public static void saveVNTyperMode(GenericRequest request, GenericResponse response) {

        if (request.isServletRequest()) {
            saveVNTyperMode(request.getServletRequest(), response.getServletResponse());
        }
    }
    
    public static void setTextAreaSize(GenericRequest request) {
        Cookie[] cookies = request.getCookies();
        int textAreaHeight = 0;
        int textAreaWidth = 0;
        boolean usePreviousTextAreaSize = false;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String cookieName = cookie.getName();
                if (cookieName.equals("TinyMCE_message_size")) {
                    String cookieValue = cookie.getValue();
                    // cookie pattern: cw%3D766%26ch%3D370
                    // check for valid cookie
                    int separatorIndex = cookieValue.indexOf("%26"); 
                    if (separatorIndex == -1) {
                        request.setAttribute("usePreviousTextAreaSize", new Boolean(false));
                        return;
                    }
                    String widthContainer = cookieValue.substring(0, separatorIndex);
                    String heightContainer = cookieValue.substring(separatorIndex + 3, cookieValue.length());
                    if (widthContainer.startsWith("cw%3D") == false) {
                        request.setAttribute("usePreviousTextAreaSize", new Boolean(false));
                        return;
                    }
                    String widthString = widthContainer.substring(5);// 5 is the length of "ch%3D"
                    
                    if (heightContainer.startsWith("ch%3D") == false) {
                        request.setAttribute("usePreviousTextAreaSize", new Boolean(false));
                        return;
                    }
                    String heightString = heightContainer.substring(5);// 5 is the length of "ch%3D"
                    try {
                        textAreaWidth = Integer.parseInt(widthString);
                        textAreaHeight = Integer.parseInt(heightString);
                        usePreviousTextAreaSize = true;
                    } catch (NumberFormatException e) {
                        log.warn("Cannot get text area size from 'TinyMCE_message_size' cookie", e);
                        request.setAttribute("usePreviousTextAreaSize", new Boolean(false));
                        return;
                    }
                    if (textAreaWidth < MIN_TEXT_AREA_WIDTH) {
                        textAreaWidth = MIN_TEXT_AREA_WIDTH;
                    } else if (textAreaWidth > MAX_TEXT_AREA_WIDTH) {
                        textAreaWidth = MAX_TEXT_AREA_WIDTH;
                    }
                    if (textAreaHeight < MIN_TEXT_AREA_HEIGHT) {
                        textAreaHeight = MIN_TEXT_AREA_HEIGHT;
                    } else if (textAreaHeight > MAX_TEXT_AREA_HEIGHT) {
                        textAreaHeight = MAX_TEXT_AREA_HEIGHT;
                    }
                } 
            }
        }
        request.setAttribute("usePreviousTextAreaSize", new Boolean(usePreviousTextAreaSize));
        request.setAttribute("textAreaWidth", new Integer(textAreaWidth));
        request.setAttribute("textAreaHeight", new Integer(textAreaHeight));
    }
    
    public static void setEditorMode(GenericRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                String cookieName = cookie.getName();
                if (cookieName.equals("mvnforum_editor_mode")) {
                    //bbEditor richTextEditor
                    if (cookie.getValue().equals("bbEditor")) {
                        request.setAttribute("editorMode", "bbEditor");
                    } else if (cookie.getValue().equals("richTextEditor")) {
                        request.setAttribute("editorMode", "richTextEditor");
                    } else {
                        request.setAttribute("editorMode", DEFAULT_EDITOR_MODE);
                    }
                }
            }
        }
    }

    public static void saveVNTyperMode(HttpServletRequest request, HttpServletResponse response) {

        String vnTyperMode = ParamUtil.getParameter(request, "vnselector");
        if (vnTyperMode.equals("VNI") || vnTyperMode.equals("TELEX") ||
            vnTyperMode.equals("VIQR") || vnTyperMode.equals("NOVN") ||
            vnTyperMode.equals("AUTO")) {
            Cookie typerModeCookie = new Cookie(MVNForumConstant.VN_TYPER_MODE, vnTyperMode);
            typerModeCookie.setPath("/");
            response.addCookie(typerModeCookie);
        }
    }

    // note that this method can check for duplicate but difference in case: Admin,admin,ADMIN
    public static Hashtable checkMembers(String[] memberNames, Locale locale)
        throws DatabaseException, BadInputException {

        Hashtable memberMap = new Hashtable();
        boolean isFailed = false;
        StringBuffer missingNames = new StringBuffer(512);

        for (int i = 0; i < memberNames.length; i++) {
            int receivedMemberID = -1;
            String memberName = memberNames[i];
            StringUtil.checkGoodName(memberName, locale);
            try {
                receivedMemberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
            } catch (ObjectNotFoundException ex) {
                isFailed = true;
                if (missingNames.length() > 0) {
                    missingNames.append(", ");
                }
                missingNames.append(memberName);
                continue;
            }
            memberMap.put(new Integer(receivedMemberID), memberName);
        } // end for

        if (isFailed) { // the receivers does not exist.
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.receivers_are_not_members", new Object[] {missingNames});
            throw new BadInputException(localizedMessage);
        }
        return memberMap;
    }

    /**
     * Check if the online user is Root Admin
     */
    public static boolean isRootAdmin(GenericRequest request) throws AuthenticationException, DatabaseException {
        return (OnlineUserManager.getInstance().getOnlineUser(request).getMemberID() == MVNForumConstant.MEMBER_ID_OF_ADMIN);
    }

    /**
     * Check if the online user is Root Admin
     */
    public static boolean isRootAdmin(HttpServletRequest request) throws AuthenticationException, DatabaseException {
        return (OnlineUserManager.getInstance().getOnlineUser(request).getMemberID() == MVNForumConstant.MEMBER_ID_OF_ADMIN);
    }

    /**
     * check if memberID belongs to admin's id
     */
    public static boolean isRootAdminID(int memberID) {
        return (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN);
    }

    /**
     * Check if request is from a mobile device
     */
    public static boolean isRequestFromMobileDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        for (int i = 0; i < KEY_WORDS_OF_MOBILE_DEVICE.length; i++) {
            if (userAgent.indexOf(KEY_WORDS_OF_MOBILE_DEVICE[i]) != -1) {
                return true;
            }
        }
            
        return false;
    }
    
    /**
     * Check if request is from a mobile device
     */
    public static boolean isRequestFromMobileDevice(GenericRequest request) {
        if (request.isServletRequest()) {
            return isRequestFromMobileDevice(request.getServletRequest());
        }
        return false;
    }

    public static String getConfirmedEmailKey (int memberID) throws ObjectNotFoundException, DatabaseException {
        MemberDAO memberDAO = DAOFactory.getMemberDAO();
        String encryptedPassword = memberDAO.getPassword(memberID);
        if (encryptedPassword.length() > 16) {
            return encryptedPassword.substring(0, 16);
        }
        if (encryptedPassword.length() < 16) {
            for (int i = encryptedPassword.length(); i < 16; i++) {
                encryptedPassword = encryptedPassword + " ";
            }
        }
        return encryptedPassword;
    }

//    public static void checkInstance(String className, Class clazz) throws BadInputException {
//        try {
//            Class.forName(className);
//            throw new BadInputException("Class " + className + " is not a implementation of " + clazz);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new BadInputException("Cannot load class " + className);
//        }
//    }

    public static Collection getForumsOwnedByMemberName(String memberName) throws DatabaseException {
        ArrayList result = new ArrayList();

        Collection allForums = DAOFactory.getForumDAO().getForums();
        for (Iterator iter = allForums.iterator(); iter.hasNext(); ) {
            ForumBean forumBean = (ForumBean) iter.next();
            String forumOwnerName = forumBean.getForumOwnerName();
            if (forumOwnerName.equalsIgnoreCase(memberName)) {
                result.add(forumBean);
            }
        }

        return result;
    }

    public static Collection getPrivateForums(MVNForumPermission perms) throws DatabaseException {

        ArrayList result = new ArrayList();

        Collection allForums = ForumCache.getInstance().getBeans();
        for (Iterator iter = allForums.iterator(); iter.hasNext(); ) {
            ForumBean forum = (ForumBean) iter.next();
            int forumId = forum.getForumID();
            if ( (forum.getForumType() == ForumBean.FORUM_TYPE_PRIVATE) &&
                 (forum.getForumStatus() != ForumBean.FORUM_STATUS_DISABLED) ) {
                if (perms.canReadPost(forumId) ||
                    perms.canDeleteForum(forumId) ||
                    perms.canEditForum(forumId)) {

                    result.add(forum);
                }
            }
        }

        return result;
    }

    public static Collection getNewMembersInRecentDays(Timestamp fromDate, Timestamp toDate, String sort, String order)
        throws IllegalArgumentException, DatabaseException {

        final int PARTIAL_SIZE = 10;// each loop with size of 10

        Collection newMembers = new ArrayList();

        int offset = 0;

        do {
            // get partial result
            Collection members = DAOFactory.getMemberDAO().getMembers_withSortSupport_limit(offset, PARTIAL_SIZE, sort, order, MemberDAO.ALL_MEMBER_STATUS);

            if (members.size() == 0) {
                break;
            }

            // process new partial result
            for (Iterator iterator = members.iterator(); iterator.hasNext(); ) {

                MemberBean member = (MemberBean) iterator.next();
                Timestamp creationDate = member.getMemberCreationDate();
                if (creationDate.after(fromDate) && creationDate.before(toDate)) {
                    newMembers.add(member);
                } else {
                    return newMembers;
                }
            }

            offset += PARTIAL_SIZE;
        } while (true);

        return newMembers;
    }

    public static String getStringFromFreeMarkerTemplate(HashMap informationSet, String templateFileName) throws IOException, TemplateException {

        Configuration config = MVNForumConfig.getFreeMarkerConfiguration();
        Template template = config.getTemplate(templateFileName);

        StringWriter resultString = new StringWriter(256);
        template.process(informationSet, resultString);

        return resultString.toString();
    }

    public static StringBuffer getIndividualPermission(int combinePers, boolean globalPermission) throws BadInputException {

        StringBuffer indivialPermissions = new StringBuffer();
        int[] individualForumPermissionArray = null;
        
        switch (combinePers) {

        case MVNForumPermission.PERMISSION_SYSTEM_ADMIN:
            indivialPermissions.append("This permission includes all individual permissions");
            break;

        case MVNForumPermission.PERMISSION_FORUM_ADMIN:
            if (globalPermission) {
                individualForumPermissionArray = MVNForumPermission.individualForumAdminPermissionArray;
            } else {
                individualForumPermissionArray = MVNForumPermission.individualForumAdminLimitPermissionArray;
            }
            break;

        case MVNForumPermission.PERMISSION_FORUM_MODERATOR:
            if (globalPermission) {
                individualForumPermissionArray = MVNForumPermission.individualForumModeratorPermissionArray;
            } else {
                individualForumPermissionArray = MVNForumPermission.individualForumModeratorLimitPermissionArray;
            }
            break;
            
        case MVNForumPermission.PERMISSION_POWER_USER:
            if (globalPermission) {
                individualForumPermissionArray = MVNForumPermission.individualPowerUserPermissionArray;
            } else {
                individualForumPermissionArray = MVNForumPermission.individualPowerUserLimitPermissionArray;
            }
            break;

        case MVNForumPermission.PERMISSION_NORMAL_USER:
            if (globalPermission) {
                individualForumPermissionArray = MVNForumPermission.individualNormalUserPermissionArray;
            } else {
                individualForumPermissionArray = MVNForumPermission.individualNormalUserLimitPermissionArray;
            }
            break;
            
        case MVNForumPermission.PERMISSION_LIMITED_USER:
            if (globalPermission) {
                individualForumPermissionArray = MVNForumPermission.individualLimitedUserPermissionArray;
            } else {
                individualForumPermissionArray = MVNForumPermission.individualLimitedUserLimitPermissionArray;
            }
            break;
            
        default:
            throw new BadInputException("This permission is invalid");
            
        } // end switch
        
        if (individualForumPermissionArray != null) {
            indivialPermissions.append("This permission includes the individual permission: &lt;br /&gt;");
            indivialPermissions.append("&lt;ul&gt;");
            for (int i = 0; i < individualForumPermissionArray.length; i++) {
                indivialPermissions.append("&lt;li&gt;").append(AbstractPermission.getDescription(individualForumPermissionArray[i])).append("&lt;/li&gt;");
            }
            indivialPermissions.append("&lt;/ul&gt;");
        }
        
        return indivialPermissions;
    }

    public static String getThreadPriorityIcon(int threadPriority) {
        String threadPriorityIcon = null;
        if (threadPriority == ThreadBean.THREAD_PRIORITY_LOW) {
            threadPriorityIcon = ThreadIconLegend.THREAD_ICON_PRIORITY_LOW;
        } else if (threadPriority == ThreadBean.THREAD_PRIORITY_NORMAL) {
            threadPriorityIcon = ThreadIconLegend.THREAD_ICON_PRIORITY_NORMAL;
        } else if (threadPriority == ThreadBean.THREAD_PRIORITY_HIGH) {
            threadPriorityIcon = ThreadIconLegend.THREAD_ICON_PRIORITY_HIGH;
        } else {
            AssertionUtil.doAssert(false, "Does not support thread priority = " + threadPriority);
        }
        return threadPriorityIcon;
    }

    public static String getThreadTypeIcon(int threadType) {
        String threadTypeIcon = "";
        if (threadType == ThreadBean.THREAD_TYPE_STICKY) {
            threadTypeIcon = ThreadIconLegend.THREAD_ICON_TYPE_STICKY;
        } else if (threadType == ThreadBean.THREAD_TYPE_FORUM_ANNOUNCEMENT) {
            threadTypeIcon = ThreadIconLegend.THREAD_ICON_TYPE_FORUM_ANNOUNCEMENT;
        } else if (threadType == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT) {
            threadTypeIcon = ThreadIconLegend.THREAD_ICON_TYPE_GLOBAL_ANNOUNCEMENT;
        }
        return threadTypeIcon;
    }

    public static String getLocalizedMessageFolderName(Locale locale, String messageFolderName) {
        
        String localizedKey = "";
        if (messageFolderName.equals(MVNForumConstant.MESSAGE_FOLDER_INBOX)) {
            localizedKey = "mvnforum.common.messagefolder.name.inbox";
        } else if (messageFolderName.equals(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
            localizedKey = "mvnforum.common.messagefolder.name.draft";
        } else if (messageFolderName.equals(MVNForumConstant.MESSAGE_FOLDER_TRASH)) {
            localizedKey = "mvnforum.common.messagefolder.name.trash";
        } else if (messageFolderName.equals(MVNForumConstant.MESSAGE_FOLDER_SENT)) {
            localizedKey = "mvnforum.common.messagefolder.name.sent";
        } else {
            return messageFolderName;
        }
        String localizedValue = MVNForumResourceBundle.getString(locale, localizedKey);
        
        return localizedValue;        
    }
    
}
