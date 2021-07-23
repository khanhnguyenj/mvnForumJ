/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ConfigurationWebHandler.java,v 1.144 2010/08/02 09:16:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.144 $
 * $Date: 2010/08/02 09:16:42 $
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
package com.mvnforum.admin;

import java.io.*;
import java.util.*;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.db.*;

import freemarker.template.*;

public class ConfigurationWebHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationWebHandler.class);
    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public ConfigurationWebHandler() {
    }

    public void prepareConfigMVNForum(GenericRequest request)
        throws IOException, DatabaseException, AuthenticationException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String strPathName = FileUtil.getServletClassesPath();
        DOM4JConfiguration mvnforumConfig;
        try {
            mvnforumConfig = new DOM4JConfiguration(new File(strPathName + "mvnforum.xml"));
        } catch (Exception ex) {
            log.error("Cannot load the DOM4JConfiguration.", ex);
            throw new IOException("Cannot load the DOM4JConfiguration.");
        }

        request.setAttribute("mvnforumConfig", mvnforumConfig);
    }

    public void prepareConfigMVNCore(GenericRequest request)
        throws IOException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String strPathName = FileUtil.getServletClassesPath();
        DOM4JConfiguration mvncoreConfig;
        try {
            mvncoreConfig = new DOM4JConfiguration(new File(strPathName + "mvncore.xml"));
        } catch (Exception ex) {
            log.error("Cannot load the DOM4JConfiguration.", ex);
            throw new IOException("Cannot load the DOM4JConfiguration.");
        }

        request.setAttribute("mvncoreConfig", mvncoreConfig);
    }

    public void updateConfigStepOne(GenericRequest request)
        throws BadInputException, DocumentException, IOException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        // Children of mvnforumConfig
        String mvnforum_home = GenericParamUtil.getParameterSafe(request, "mvnforum_home", true);
        String mvnforum_log = GenericParamUtil.getParameterSafe(request, "mvnforum_log", true);
        String webmaster_email = GenericParamUtil.getParameterEmail(request, "webmaster_email");
        String watch_email = GenericParamUtil.getParameterEmail(request, "watch_email");
        String logo_url = GenericParamUtil.getParameterSafe(request, "logo_url", true);
        String redirect_login_url = GenericParamUtil.getParameterSafe(request, "redirect_login_url", true);
        String redirect_logout_url = GenericParamUtil.getParameterSafe(request, "redirect_logout_url", true);
        String supported_locales = GenericParamUtil.getParameterSafe(request, "supported_locales", false);
        String default_locale_name = GenericParamUtil.getParameterSafe(request, "default_locale_name", true);
        String locale_parameter_name = GenericParamUtil.getParameterSafe(request, "locale_parameter_name", true);
        String default_guest_name = GenericParamUtil.getParameterSafe(request, "default_guest_name", true);
        int default_guest_timezone = GenericParamUtil.getParameterInt(request, "default_guest_timezone");
        int default_status_of_registered_member = GenericParamUtil.getParameterInt(request, "default_status_of_registered_member");
        boolean enable_register_rule = GenericParamUtil.getParameterBoolean(request, "enable_register_rule");

        boolean enable_cache_member = GenericParamUtil.getParameterBoolean(request, "enable_cache_member");
        boolean enable_cache_post = GenericParamUtil.getParameterBoolean(request, "enable_cache_post");
        boolean enable_cache_thread = GenericParamUtil.getParameterBoolean(request, "enable_cache_thread");
        boolean enable_cache_forum = GenericParamUtil.getParameterBoolean(request, "enable_cache_forum");
        boolean enable_cache_category = GenericParamUtil.getParameterBoolean(request, "enable_cache_category");

        boolean enable_passwordless_auth = GenericParamUtil.getParameterBoolean(request, "enable_passwordless_auth");
        boolean enable_login_info_in_cookie = GenericParamUtil.getParameterBoolean(request, "enable_login_info_in_cookie");
        boolean enable_login_info_in_session = GenericParamUtil.getParameterBoolean(request, "enable_login_info_in_session");
        boolean enable_login_info_in_realm = GenericParamUtil.getParameterBoolean(request, "enable_login_info_in_realm");
        boolean enable_login_info_in_customization = GenericParamUtil.getParameterBoolean(request, "enable_login_info_in_customization");
        boolean enable_friendly_url = GenericParamUtil.getParameterBoolean(request, "enable_friendly_url");
        boolean enable_check_invalid_session = GenericParamUtil.getParameterBoolean(request, "enable_check_invalid_session");
        boolean require_activation = GenericParamUtil.getParameterBoolean(request, "require_activation");
        boolean enable_login = GenericParamUtil.getParameterBoolean(request, "enable_login");
        boolean enable_new_member = GenericParamUtil.getParameterBoolean(request, "enable_new_member");
        boolean enable_new_post = GenericParamUtil.getParameterBoolean(request, "enable_new_post");
        boolean enable_rss = GenericParamUtil.getParameterBoolean(request, "enable_rss");
        boolean enable_watch = GenericParamUtil.getParameterBoolean(request, "enable_watch");
        boolean enable_attachment = GenericParamUtil.getParameterBoolean(request, "enable_attachment");
        boolean enable_avatar = GenericParamUtil.getParameterBoolean(request, "enable_avatar");
        boolean enable_emoticon = GenericParamUtil.getParameterBoolean(request, "enable_emoticon");
        boolean enable_captcha = GenericParamUtil.getParameterBoolean(request, "enable_captcha");
        boolean enable_portal_like_index_page = GenericParamUtil.getParameterBoolean(request, "enable_portal_like_index_page");

        boolean enable_search = GenericParamUtil.getParameterBoolean(request, "enable_search");
        int default_search_order_by = GenericParamUtil.getParameterInt(request, "default_search_order_by");
        
        boolean enable_online_users = GenericParamUtil.getParameterBoolean(request, "enable_online_users");
        boolean enable_duplicate_onlineusers = GenericParamUtil.getParameterBoolean(request, "enable_duplicate_onlineusers");
        boolean enable_invisible_users = GenericParamUtil.getParameterBoolean(request, "enable_invisible_users");
        boolean enable_listmembers = GenericParamUtil.getParameterBoolean(request, "enable_listmembers");
        boolean enable_show_last_login_of_current_member = GenericParamUtil.getParameterBoolean(request, "enable_show_last_login_of_current_member");
        boolean enable_show_last_login = GenericParamUtil.getParameterBoolean(request, "enable_show_last_login");
        boolean enable_auto_watching = GenericParamUtil.getParameterBoolean(request, "enable_auto_watching");

        boolean enable_show_birthday = GenericParamUtil.getParameterBoolean(request, "enable_show_birthday");
        boolean enable_show_gender = GenericParamUtil.getParameterBoolean(request, "enable_show_gender");
        boolean enable_show_address = GenericParamUtil.getParameterBoolean(request, "enable_show_address");
        boolean enable_show_city = GenericParamUtil.getParameterBoolean(request, "enable_show_city");
        boolean enable_show_state = GenericParamUtil.getParameterBoolean(request, "enable_show_state");
        boolean enable_show_country = GenericParamUtil.getParameterBoolean(request, "enable_show_country");
        boolean enable_show_phone = GenericParamUtil.getParameterBoolean(request, "enable_show_phone");
        boolean enable_show_mobile = GenericParamUtil.getParameterBoolean(request, "enable_show_mobile");
        boolean enable_show_fax = GenericParamUtil.getParameterBoolean(request, "enable_show_fax");
        boolean enable_show_career = GenericParamUtil.getParameterBoolean(request, "enable_show_career");
        boolean enable_show_homepage = GenericParamUtil.getParameterBoolean(request, "enable_show_homepage");
        boolean enable_show_cool_link_1 = GenericParamUtil.getParameterBoolean(request, "enable_show_cool_link_1");
        boolean enable_show_cool_link_2 = GenericParamUtil.getParameterBoolean(request, "enable_show_cool_link_2");
        boolean enable_show_yahoo = GenericParamUtil.getParameterBoolean(request, "enable_show_yahoo");
        boolean enable_show_aol = GenericParamUtil.getParameterBoolean(request, "enable_show_aol");
        boolean enable_show_icq = GenericParamUtil.getParameterBoolean(request, "enable_show_icq");
        boolean enable_show_msn = GenericParamUtil.getParameterBoolean(request, "enable_show_msn");
        boolean enable_show_join_date = GenericParamUtil.getParameterBoolean(request, "enable_show_join_date");
        boolean enable_show_post_count = GenericParamUtil.getParameterBoolean(request, "enable_show_post_count");
        boolean enable_show_online_status = GenericParamUtil.getParameterBoolean(request, "enable_show_online_status");
        boolean enable_show_lastname = GenericParamUtil.getParameterBoolean(request, "enable_show_lastname");
        boolean enable_show_firstname = GenericParamUtil.getParameterBoolean(request, "enable_show_firstname");
        boolean enable_show_view_count = GenericParamUtil.getParameterBoolean(request, "enable_show_view_count");
        boolean enable_show_email = GenericParamUtil.getParameterBoolean(request, "enable_show_email");
        boolean enable_show_name_visible = GenericParamUtil.getParameterBoolean(request, "enable_show_name_visible");
        boolean enable_show_email_visible = GenericParamUtil.getParameterBoolean(request, "enable_show_email_visible");
        boolean enable_show_posts_per_page = GenericParamUtil.getParameterBoolean(request, "enable_show_posts_per_page");
        boolean enable_show_modified_date = GenericParamUtil.getParameterBoolean(request, "enable_show_modified_date");
        boolean enable_show_signature = GenericParamUtil.getParameterBoolean(request, "enable_show_signature");

        boolean enable_use_popup_menu_in_viewthread = GenericParamUtil.getParameterBoolean(request, "enable_use_popup_menu_in_viewthread");
        boolean enable_rich_text_editor = GenericParamUtil.getParameterBoolean(request, "enable_rich_text_editor");
        boolean enable_split_thread = GenericParamUtil.getParameterBoolean(request, "enable_split_thread");
        boolean enable_listunansweredthreads = GenericParamUtil.getParameterBoolean(request, "enable_listunansweredthreads");
        boolean enable_list_users_browsing_thread = GenericParamUtil.getParameterBoolean(request, "enable_list_users_browsing_thread");
        boolean enable_email_to_admin_content_with_censored_words = GenericParamUtil.getParameterBoolean(request, "enable_email_to_admin_content_with_censored_words");
        boolean enable_guest_view_listusers = GenericParamUtil.getParameterBoolean(request, "enable_guest_view_listusers");
        boolean only_normal_thread_type_in_active_threads = GenericParamUtil.getParameterBoolean(request, "only_normal_thread_type_in_active_threads");
        boolean send_watchmail_as_html = GenericParamUtil.getParameterBoolean(request, "send_watchmail_as_html");
        boolean enable_easy_watching = GenericParamUtil.getParameterBoolean(request, "enable_easy_watching");
        boolean enable_send_watch_mail_of_my_own_post = GenericParamUtil.getParameterBoolean(request, "enable_send_watch_mail_of_my_own_post");
        int max_last_post_body_in_watch = GenericParamUtil.getParameterInt(request, "max_last_post_body_in_watch");
        
        boolean enable_private_message = GenericParamUtil.getParameterBoolean(request, "enable_private_message");
        boolean enable_public_message = GenericParamUtil.getParameterBoolean(request, "enable_public_message");
        boolean enable_message_attachment = GenericParamUtil.getParameterBoolean(request, "enable_message_attachment");
        boolean enable_most_active_threads = GenericParamUtil.getParameterBoolean(request, "enable_most_active_threads");
        boolean enable_most_active_members = GenericParamUtil.getParameterBoolean(request, "enable_most_active_members");
        boolean enable_site_statistics_overview = GenericParamUtil.getParameterBoolean(request, "enable_site_statistics_overview");
        boolean enable_admin_can_change_password = GenericParamUtil.getParameterBoolean(request, "enable_admin_can_change_password");
        boolean enable_guest_view_image_attachment = GenericParamUtil.getParameterBoolean(request, "enable_guest_view_image_attachment");
        boolean enable_expanse_category_tree_by_default = GenericParamUtil.getParameterBoolean(request, "enable_expanse_category_tree_by_default");

        boolean enable_encrypt_password_on_browser = GenericParamUtil.getParameterBoolean(request, "enable_encrypt_password_on_browser");
        boolean enable_external_user_database = GenericParamUtil.getParameterBoolean(request, "enable_external_user_database");
        int days_to_show_recent_members = GenericParamUtil.getParameterInt(request, "days_to_show_recent_members");
        boolean enable_list_new_members_in_recent_days = GenericParamUtil.getParameterBoolean(request, "enable_list_new_members_in_recent_days");
        boolean enable_list_users_browsing_forum = GenericParamUtil.getParameterBoolean(request, "enable_list_users_browsing_forum");
        boolean enable_email_threatening_content = GenericParamUtil.getParameterBoolean(request, "enable_email_threatening_content");
        boolean have_internet = GenericParamUtil.getParameterBoolean(request, "have_internet");
        String event_log_locale = GenericParamUtil.getParameterSafe(request, "event_log_locale", true);
        int default_category_id = GenericParamUtil.getParameterInt(request, "default_category_id");
        int default_watch_type = GenericParamUtil.getParameterInt(request, "default_watch_type");

        int default_watch_option = GenericParamUtil.getParameterInt(request, "default_watch_option");
        int default_moderation_option = GenericParamUtil.getParameterInt(request, "default_moderation_option");
        
        boolean enable_image_thumbnail = GenericParamUtil.getParameterBoolean(request, "enable_image_thumbnail");
        int image_thumbnail_width = 0;
        int image_thumbnail_height = 0;
        if (enable_image_thumbnail) {
            image_thumbnail_width = GenericParamUtil.getParameterInt(request, "image_thumbnail_width");
            image_thumbnail_height = GenericParamUtil.getParameterInt(request, "image_thumbnail_height");
        }
        
        // children of mvnforumfactoryconfig
        String member_implementation = GenericParamUtil.getParameterSafe(request, "member_implementation", true);
        String onlineuser_implementation = GenericParamUtil.getParameterSafe(request, "onlineuser_implementation", true);
        String authenticator_implementation = GenericParamUtil.getParameterSafe(request, "authenticator_implementation", false);
        String requestprocessor_implementation = GenericParamUtil.getParameterSafe(request, "requestprocessor_implementation", true);
        String lucene_analyzer_implementation = GenericParamUtil.getParameterSafe(request, "lucene_analyzer_implementation", true);
        String mvnforum_service_implementation = GenericParamUtil.getParameterSafe(request, "mvnforum_service_implementation", true);
        String mvn_auth_service_implementation = GenericParamUtil.getParameterSafe(request, "mvn_auth_service_implementation", true);

        WatchBean.validateWatchOption(default_watch_option);
        ForumBean.validateForumModerationMode(default_moderation_option);
        MemberBean.validateMemberStatus(default_status_of_registered_member);
        WatchBean.validateWatchType(default_watch_type);
        
        // Now checking the validity of value
        Locale locale = onlineUser.getLocale();

        try {
            // Create a directory if it does not exist
            FileUtil.createDirs(mvnforum_home, true);

            String tempFilename = mvnforum_home + File.separatorChar + "mvnforum_tempfile.tmp";
            File tempFile = new File(tempFilename);
            if (log.isDebugEnabled()) {
                log.debug("Temp file = " + tempFilename);
                log.debug("Absolute filename of temp file = " + tempFile.getAbsolutePath());
            }

            FileOutputStream fos = new FileOutputStream(tempFilename);
            fos.write(tempFilename.getBytes());
            fos.close();

            tempFile.delete();
        } catch (IOException ex) {
            String errorMessage = "Check your mvnforum_home. Detail : " + ex.getMessage();
            throw new BadInputException(errorMessage);
        }

        try {
            int lastBackSlashIndex = mvnforum_log.lastIndexOf('\\');
            int lastForwardSlashIndex = mvnforum_log.lastIndexOf('/');
            int index = Math.max(lastBackSlashIndex, lastForwardSlashIndex);
            if (index < 0) {
                throw new BadInputException("Cannot find any '\\' or '/' in mvnforum_log : " + mvnforum_log);
            }
            String parentFolder = mvnforum_log.substring(0, index);
            log.debug("parentFolder = " + parentFolder);

            // always create a dir, if the dir already exitsted, nothing happens
            FileUtil.createDirs(parentFolder, true);

            String tempFilename = parentFolder + File.separatorChar + "mvnforum_tempfile.tmp";
            File tempFile = new File(tempFilename);
            if (log.isDebugEnabled()) {
                log.debug("Temp file in log folder = " + tempFilename);
                log.debug("Absolute filename of temp file in log folder = " + tempFile.getAbsolutePath());
            }

            FileOutputStream fos = new FileOutputStream(tempFilename);
            fos.write(tempFilename.getBytes());
            fos.close();

            tempFile.delete();
        } catch (IOException ex) {
            String errorMessage = "Check your mvnforum_log. Detail : " + ex.getMessage();
            throw new BadInputException(errorMessage);
        }

        MyUtil.checkClassName(locale, member_implementation, true);
        MyUtil.checkClassName(locale, onlineuser_implementation, true);
        MyUtil.checkClassName(locale, authenticator_implementation, false);
        MyUtil.checkClassName(locale, requestprocessor_implementation, true);
        MyUtil.checkClassName(locale, lucene_analyzer_implementation, true);
        MyUtil.checkClassName(locale, mvnforum_service_implementation, true);
        MyUtil.checkClassName(locale, mvn_auth_service_implementation, true);


        // End checking, now save the value
        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(strPathName + "mvnforum.xml"));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/mvnforum_home", mvnforum_home);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/mvnforum_log", mvnforum_log);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/webmaster_email", webmaster_email);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/watch_email", watch_email);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/logo_url", logo_url);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/redirect_login_url", redirect_login_url);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/redirect_logout_url", redirect_logout_url);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/supported_locales", supported_locales);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_locale_name", default_locale_name);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/locale_parameter_name", locale_parameter_name);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_guest_name", default_guest_name);
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_guest_timezone", String.valueOf(default_guest_timezone));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_cache_member", String.valueOf(enable_cache_member));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_cache_post", String.valueOf(enable_cache_post));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_cache_thread", String.valueOf(enable_cache_thread));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_cache_forum", String.valueOf(enable_cache_forum));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_cache_category", String.valueOf(enable_cache_category));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_passwordless_auth", String.valueOf(enable_passwordless_auth));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_login_info_in_cookie", String.valueOf(enable_login_info_in_cookie));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_login_info_in_session", String.valueOf(enable_login_info_in_session));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_login_info_in_realm", String.valueOf(enable_login_info_in_realm));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_login_info_in_customization", String.valueOf(enable_login_info_in_customization));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_friendly_url", String.valueOf(enable_friendly_url));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_check_invalid_session", String.valueOf(enable_check_invalid_session));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_activation", String.valueOf(require_activation));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_login", String.valueOf(enable_login));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_new_member", String.valueOf(enable_new_member));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_new_post", String.valueOf(enable_new_post));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_rss", String.valueOf(enable_rss));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_watch", String.valueOf(enable_watch));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_attachment", String.valueOf(enable_attachment));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_avatar", String.valueOf(enable_avatar));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_emoticon", String.valueOf(enable_emoticon));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_captcha", String.valueOf(enable_captcha));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_portal_like_index_page", String.valueOf(enable_portal_like_index_page));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_search", String.valueOf(enable_search));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_search_order_by", String.valueOf(default_search_order_by));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_online_users", String.valueOf(enable_online_users));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_duplicate_onlineusers", String.valueOf(enable_duplicate_onlineusers));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_invisible_users", String.valueOf(enable_invisible_users));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_listmembers", String.valueOf(enable_listmembers));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_last_login_of_current_member", String.valueOf(enable_show_last_login_of_current_member));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_last_login", String.valueOf(enable_show_last_login));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_auto_watching", String.valueOf(enable_auto_watching));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_birthday", String.valueOf(enable_show_birthday));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_gender", String.valueOf(enable_show_gender));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_address", String.valueOf(enable_show_address));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_city", String.valueOf(enable_show_city));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_state", String.valueOf(enable_show_state));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_country", String.valueOf(enable_show_country));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_phone", String.valueOf(enable_show_phone));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_mobile", String.valueOf(enable_show_mobile));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_fax", String.valueOf(enable_show_fax));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_career", String.valueOf(enable_show_career));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_homepage", String.valueOf(enable_show_homepage));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_cool_link_1", String.valueOf(enable_show_cool_link_1));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_cool_link_2", String.valueOf(enable_show_cool_link_2));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_yahoo", String.valueOf(enable_show_yahoo));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_aol", String.valueOf(enable_show_aol));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_msn", String.valueOf(enable_show_msn));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_icq", String.valueOf(enable_show_icq));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_join_date", String.valueOf(enable_show_join_date));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_post_count", String.valueOf(enable_show_post_count));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_online_status", String.valueOf(enable_show_online_status));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_lastname", String.valueOf(enable_show_lastname));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_firstname", String.valueOf(enable_show_firstname));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_view_count", String.valueOf(enable_show_view_count));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_email", String.valueOf(enable_show_email));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_email_visible", String.valueOf(enable_show_email_visible));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_name_visible", String.valueOf(enable_show_name_visible));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_posts_per_page", String.valueOf(enable_show_posts_per_page));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_modified_date", String.valueOf(enable_show_modified_date));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_show_signature", String.valueOf(enable_show_signature));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_use_popup_menu_in_viewthread", String.valueOf(enable_use_popup_menu_in_viewthread));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_rich_text_editor", String.valueOf(enable_rich_text_editor));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_split_thread", String.valueOf(enable_split_thread));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_listunansweredthreads", String.valueOf(enable_listunansweredthreads));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_list_users_browsing_thread", String.valueOf(enable_list_users_browsing_thread));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_email_to_admin_content_with_censored_words", String.valueOf(enable_email_to_admin_content_with_censored_words));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_guest_view_listusers", String.valueOf(enable_guest_view_listusers));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/only_normal_thread_type_in_active_threads", String.valueOf(only_normal_thread_type_in_active_threads));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/send_watchmail_as_html", String.valueOf(send_watchmail_as_html));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_easy_watching", String.valueOf(enable_easy_watching));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_send_watch_mail_of_my_own_post", String.valueOf(enable_send_watch_mail_of_my_own_post));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_last_post_body_in_watch", String.valueOf(max_last_post_body_in_watch));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_private_message", String.valueOf(enable_private_message));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_public_message", String.valueOf(enable_public_message));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_message_attachment", String.valueOf(enable_message_attachment));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_most_active_threads", String.valueOf(enable_most_active_threads));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_most_active_members", String.valueOf(enable_most_active_members));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_site_statistics_overview", String.valueOf(enable_site_statistics_overview));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_admin_can_change_password", String.valueOf(enable_admin_can_change_password));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_guest_view_image_attachment", String.valueOf(enable_guest_view_image_attachment));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_expanse_category_tree_by_default", String.valueOf(enable_expanse_category_tree_by_default));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_watch_option", String.valueOf(default_watch_option));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_moderation_option", String.valueOf(default_moderation_option));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_encrypt_password_on_browser", String.valueOf(enable_encrypt_password_on_browser));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_external_user_database", String.valueOf(enable_external_user_database));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/days_to_show_recent_members", String.valueOf(days_to_show_recent_members));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_list_new_members_in_recent_days", String.valueOf(enable_list_new_members_in_recent_days));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_list_users_browsing_forum", String.valueOf(enable_list_users_browsing_forum));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_email_threatening_content", String.valueOf(enable_email_threatening_content));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/have_internet", String.valueOf(have_internet));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/event_log_locale", String.valueOf(event_log_locale));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_category_id", String.valueOf(default_category_id));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_watch_type", String.valueOf(default_watch_type));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/default_status_of_registered_member", String.valueOf(default_status_of_registered_member));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_register_rule", String.valueOf(enable_register_rule));
        
        //  children of image_thumbnail
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/image_thumbnail/enable", String.valueOf(enable_image_thumbnail));
        if (enable_image_thumbnail) {
            XMLUtil.updateNode(root, rootName + "mvnforumconfig/image_thumbnail/width", String.valueOf(image_thumbnail_width));
            XMLUtil.updateNode(root, rootName + "mvnforumconfig/image_thumbnail/height", String.valueOf(image_thumbnail_height));
        }
                        
        // children of mvnforumfactoryconfig
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/member_implementation", member_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/onlineuser_implementation", onlineuser_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/authenticator_implementation", authenticator_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/requestprocessor_implementation", requestprocessor_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/lucene_analyzer_implementation", lucene_analyzer_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/mvnforum_service_implementation", mvnforum_service_implementation);
        XMLUtil.updateNode(root, rootName + "mvnforumfactoryconfig/mvn_auth_service_implementation", mvn_auth_service_implementation);

        saveDocument(document, strPathName + "mvnforum.xml");
        
        MVNForumConfig.reload();
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateConfigStep", new Object[]{new Integer(1)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update config step one", actionDesc, EventLogService.HIGH);

        request.setAttribute("step", "1");
    }

    public void updateConfigStepTwo(GenericRequest request)
        throws BadInputException, DocumentException, IOException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        int max_private_message = GenericParamUtil.getParameterUnsignedInt(request, "max_private_message");
        String max_message_attachment_size = GenericParamUtil.getParameterSafe(request, "max_message_attachment_size", true);
        String max_attachment_size = GenericParamUtil.getParameterSafe(request, "max_attachment_size", true);
        int max_favorite_thread = GenericParamUtil.getParameterUnsignedInt(request, "max_favorite_thread");
        int max_edit_days = GenericParamUtil.getParameterUnsignedInt(request, "max_edit_days");
        int max_attach_days = GenericParamUtil.getParameterUnsignedInt(request, "max_attach_days");
        int max_delete_days = GenericParamUtil.getParameterUnsignedInt(request, "max_delete_days");
        int rows_per_page = GenericParamUtil.getParameterUnsignedInt(request, "rows_per_page");
        int rows_per_rss = GenericParamUtil.getParameterUnsignedInt(request, "rows_per_rss");
        int hot_topic_threshold = GenericParamUtil.getParameterUnsignedInt(request, "hot_topic_threshold");

        int max_http_requests_per_hour_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_http_requests_per_hour_per_ip");
        int max_http_requests_per_minute_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_http_requests_per_minute_per_ip");
        int max_posts_per_hour_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_posts_per_hour_per_ip");
        int max_members_per_hour_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_members_per_hour_per_ip");
        int max_logins_per_hour_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_logins_per_hour_per_ip");
        int max_messages_per_hour_per_ip = GenericParamUtil.getParameterUnsignedInt(request, "max_messages_per_hour_per_ip");
        int max_password_days = GenericParamUtil.getParameterUnsignedInt(request, "max_password_days");
        int max_posts_per_hour_per_member = GenericParamUtil.getParameterUnsignedInt(request, "max_posts_per_hour_per_member");
        
        int max_chars_in_short_summary = GenericParamUtil.getParameterUnsignedInt(request, "max_chars_in_short_summary");
        int max_chars_in_long_summary = GenericParamUtil.getParameterUnsignedInt(request, "max_chars_in_long_summary");
        int max_chars_in_rss = GenericParamUtil.getParameterUnsignedInt(request, "max_chars_in_rss");

        boolean enable_backup_on_server = GenericParamUtil.getParameterBoolean(request, "enable_backup_on_server");
        int max_import_size = GenericParamUtil.getParameterUnsignedInt(request, "max_import_size");

        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(strPathName + "mvnforum.xml"));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_private_message", String.valueOf(max_private_message));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_message_attachment_size", String.valueOf(max_message_attachment_size));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_attachment_size", String.valueOf(max_attachment_size));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_favorite_thread", String.valueOf(max_favorite_thread));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_edit_days", String.valueOf(max_edit_days));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_attach_days", String.valueOf(max_attach_days));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_delete_days", String.valueOf(max_delete_days));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/rows_per_page", String.valueOf(rows_per_page));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/rows_per_rss", String.valueOf(rows_per_rss));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/hot_topic_threshold", String.valueOf(hot_topic_threshold));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_http_requests_per_hour_per_ip", String.valueOf(max_http_requests_per_hour_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_http_requests_per_minute_per_ip", String.valueOf(max_http_requests_per_minute_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_posts_per_hour_per_ip", String.valueOf(max_posts_per_hour_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_members_per_hour_per_ip", String.valueOf(max_members_per_hour_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_logins_per_hour_per_ip", String.valueOf(max_logins_per_hour_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_messages_per_hour_per_ip", String.valueOf(max_messages_per_hour_per_ip));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_password_days", String.valueOf(max_password_days));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_posts_per_hour_per_member", String.valueOf(max_posts_per_hour_per_member));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_chars_in_short_summary", String.valueOf(max_chars_in_short_summary));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_chars_in_long_summary", String.valueOf(max_chars_in_long_summary));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_chars_in_rss", String.valueOf(max_chars_in_rss));
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/enable_backup_on_server", String.valueOf(enable_backup_on_server));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/max_import_size", String.valueOf(max_import_size));

        saveDocument(document, strPathName + "mvnforum.xml");

        MVNForumConfig.reload();
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateConfigStep", new Object[]{new Integer(2)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update config step two", actionDesc, EventLogService.HIGH);

        request.setAttribute("step", "2");
    }

    public void updateConfigStepThree(GenericRequest request)
        throws BadInputException, DocumentException, IOException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        boolean require_register_firstname = GenericParamUtil.getParameterBoolean(request, "require_register_firstname");
        boolean require_register_lastname = GenericParamUtil.getParameterBoolean(request, "require_register_lastname");
        boolean require_register_gender = GenericParamUtil.getParameterBoolean(request, "require_register_gender");
        boolean require_register_birthday = GenericParamUtil.getParameterBoolean(request, "require_register_birthday");
        boolean require_register_address = GenericParamUtil.getParameterBoolean(request, "require_register_address");
        boolean require_register_city = GenericParamUtil.getParameterBoolean(request, "require_register_city");
        boolean require_register_state = GenericParamUtil.getParameterBoolean(request, "require_register_state");
        boolean require_register_country = GenericParamUtil.getParameterBoolean(request, "require_register_country");
        boolean require_register_phone = GenericParamUtil.getParameterBoolean(request, "require_register_phone");
        boolean require_register_mobile = GenericParamUtil.getParameterBoolean(request, "require_register_mobile");
        boolean require_register_fax = GenericParamUtil.getParameterBoolean(request, "require_register_fax");
        boolean require_register_career = GenericParamUtil.getParameterBoolean(request, "require_register_career");
        boolean require_register_homepage = GenericParamUtil.getParameterBoolean(request, "require_register_homepage");
        boolean require_register_yahoo = GenericParamUtil.getParameterBoolean(request, "require_register_yahoo");
        boolean require_register_aol = GenericParamUtil.getParameterBoolean(request, "require_register_aol");
        boolean require_register_icq = GenericParamUtil.getParameterBoolean(request, "require_register_icq");
        boolean require_register_msn = GenericParamUtil.getParameterBoolean(request, "require_register_msn");
        boolean require_register_link_1 = GenericParamUtil.getParameterBoolean(request, "require_register_link_1");
        boolean require_register_link_2 = GenericParamUtil.getParameterBoolean(request, "require_register_link_2");

        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(strPathName + "mvnforum.xml"));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_firstname", String.valueOf(require_register_firstname));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_lastname", String.valueOf(require_register_lastname));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_gender", String.valueOf(require_register_gender));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_birthday", String.valueOf(require_register_birthday));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_address", String.valueOf(require_register_address));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_city", String.valueOf(require_register_city));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_state", String.valueOf(require_register_state));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_country", String.valueOf(require_register_country));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_phone", String.valueOf(require_register_phone));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_mobile", String.valueOf(require_register_mobile));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_fax", String.valueOf(require_register_fax));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_career", String.valueOf(require_register_career));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_homepage", String.valueOf(require_register_homepage));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_yahoo", String.valueOf(require_register_yahoo));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_aol", String.valueOf(require_register_aol));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_icq", String.valueOf(require_register_icq));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_msn", String.valueOf(require_register_msn));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_link_1", String.valueOf(require_register_link_1));
        XMLUtil.updateNode(root, rootName + "mvnforumconfig/require_register_link_2", String.valueOf(require_register_link_2));

        saveDocument(document, strPathName + "mvnforum.xml");

        MVNForumConfig.reload();
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateConfigStep", new Object[]{new Integer(3)});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update config step three", actionDesc, EventLogService.HIGH);

        request.setAttribute("step", "3");        
    }

    public void updateConfigMVNCore(GenericRequest request)
        throws BadInputException, DocumentException, IOException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        boolean isUseDatasource = GenericParamUtil.getParameterBoolean(request, "use_datasource");

        int database_type = 0;
        String driver_class_name = "";
        String database_url = "";
        String database_user = "";
        String database_password = "";
        int max_connection = 0;
        int max_time_to_wait = 0;
        int minutes_between_refresh = 0;
        String datasource_name = "";

        database_type = GenericParamUtil.getParameterUnsignedInt(request, "database_type");
        if (isUseDatasource == false) {
            driver_class_name = GenericParamUtil.getParameterSafe(request, "driver_class_name", true);
            database_url = GenericParamUtil.getParameterSafe(request, "database_url", true);
            database_user = GenericParamUtil.getParameterSafe(request, "database_user", true);
            database_password = GenericParamUtil.getParameterSafe(request, "database_password", false);
            max_connection = GenericParamUtil.getParameterUnsignedInt(request, "max_connection");
            max_time_to_wait = GenericParamUtil.getParameterUnsignedInt(request, "max_time_to_wait");
            minutes_between_refresh = GenericParamUtil.getParameterUnsignedInt(request, "minutes_between_refresh");
        } else {
            datasource_name = GenericParamUtil.getParameterSafe(request, "datasource_name", false);
        }

        String default_mail_from = GenericParamUtil.getParameterEmail(request, "default_mail_from");

        boolean isReceiveUseMailsource = GenericParamUtil.getParameterBoolean(request, "receive_mail_enable_mail_source");
        boolean isSendUseMailsource = GenericParamUtil.getParameterBoolean(request, "send_mail_enable_mail_source");
        boolean isSendUseSMTPServer = GenericParamUtil.getParameterBoolean(request, "send_mail_use_embeded_smtp_mail_server");
        boolean isSendUseSecureConnection = false;

        String receive_mail_source_name = "";
        String receive_mail_server = "";
        String receive_mail_username = "";
        String receive_mail_password = "";
        int receive_mail_port =0;

        String send_mail_source_name = "";
        String send_mail_server = "";
        String send_mail_username = "";
        String send_mail_password = "";
        int send_mail_port = 0;

        if (isReceiveUseMailsource == false) {
            receive_mail_server = GenericParamUtil.getParameterSafe(request, "receive_mail_server", true);
            receive_mail_username = GenericParamUtil.getParameterSafe(request, "receive_mail_username", false);
            receive_mail_password = GenericParamUtil.getParameterSafe(request, "receive_mail_password", false);
            receive_mail_port = GenericParamUtil.getParameterUnsignedInt(request, "receive_mail_port");
        } else {
            receive_mail_source_name = GenericParamUtil.getParameterSafe(request, "receive_mail_source_name", true);
        }
        if (isSendUseSMTPServer) {
            // do nothing
        } else if (isSendUseMailsource) {
            send_mail_source_name = GenericParamUtil.getParameterSafe(request, "send_mail_source_name", true);
        } else {
            isSendUseSecureConnection = GenericParamUtil.getParameterBoolean(request, "send_mail_use_secure_connection");
            send_mail_server = GenericParamUtil.getParameterSafe(request, "send_mail_server", true);
            send_mail_username = GenericParamUtil.getParameterSafe(request, "send_mail_username", false);
            send_mail_password = GenericParamUtil.getParameterSafe(request, "send_mail_password", false);
            send_mail_port = GenericParamUtil.getParameterUnsignedInt(request, "send_mail_port");
        }

        String context_path = GenericParamUtil.getParameterSafe(request, "context_path", false);
        String server_path = GenericParamUtil.getParameterSafe(request, "server_path", true);

        int server_hour_offset = GenericParamUtil.getParameterInt(request, "server_hour_offset");
        if (server_hour_offset < -13 || server_hour_offset > 13) {
            server_hour_offset = 0;
        }

        String blocked_user_agent = GenericParamUtil.getParameterSafe(request, "blocked_user_agent", false);

        String timermanager_datasource = GenericParamUtil.getParameterSafe(request, "timermanager_datasource", false);
        boolean enable_link_nofollow = GenericParamUtil.getParameterBoolean(request, "enable_link_nofollow");
        boolean enable_encode_url = GenericParamUtil.getParameterBoolean(request, "enable_encode_url");

        String portal_type = GenericParamUtil.getParameterSafe(request, "portal_type", true);
        String allow_http_referer_prefix_list = GenericParamUtil.getParameterSafe(request, "allow_http_referer_prefix_list", false);
        String mvncoreservice_implementation = GenericParamUtil.getParameterSafe(request, "mvncoreservice_implementation", true);
        String timertaskext_implementation_list = GenericParamUtil.getParameterSafe(request, "timertaskext_implementation_list", false);

        String mailinterceptor_implementation = GenericParamUtil.getParameterSafe(request, "mailinterceptor_implementation", false);
        String contentinterceptor_implementation = GenericParamUtil.getParameterSafe(request, "contentinterceptor_implementation", false);
        String loginidinterceptor_implementation = GenericParamUtil.getParameterSafe(request, "loginidinterceptor_implementation", false);
        String passwordinterceptor_implementation = GenericParamUtil.getParameterSafe(request, "passwordinterceptor_implementation", false);
        
        // Now checking the validity of value
        Locale locale = onlineUser.getLocale();
        if (isUseDatasource == false) {
            MyUtil.checkClassName(locale, driver_class_name, true);
        }

        MyUtil.checkClassName(locale, mvncoreservice_implementation, true);
        
        String[] timerTaskExtImplList = timertaskext_implementation_list.split(";");
        for (int i = 0; i < timerTaskExtImplList.length; i++) {
            MyUtil.checkClassName(locale, timerTaskExtImplList[i], false);
        }

        MyUtil.checkClassName(locale, mailinterceptor_implementation, false);
        MyUtil.checkClassName(locale, contentinterceptor_implementation, false);
        MyUtil.checkClassName(locale, loginidinterceptor_implementation, false);
        MyUtil.checkClassName(locale, passwordinterceptor_implementation, false);

        // End checking, now save the value
        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(strPathName + "mvncore.xml"));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "dboptions/database_type", String.valueOf(database_type));
        XMLUtil.updateNode(root, rootName + "dboptions/use_datasource", String.valueOf(isUseDatasource));
        if (isUseDatasource == false) {
            XMLUtil.updateNode(root, rootName + "dboptions/driver_class_name", driver_class_name);
            XMLUtil.updateNode(root, rootName + "dboptions/database_url", database_url);
            XMLUtil.updateNode(root, rootName + "dboptions/database_user", database_user);
            XMLUtil.updateNode(root, rootName + "dboptions/database_password", database_password);
            XMLUtil.updateNode(root, rootName + "dboptions/max_connection", String.valueOf(max_connection));
            XMLUtil.updateNode(root, rootName + "dboptions/max_time_to_wait", String.valueOf(max_time_to_wait));
            XMLUtil.updateNode(root, rootName + "dboptions/minutes_between_refresh", String.valueOf(minutes_between_refresh));
        } else {
            XMLUtil.updateNode(root, rootName + "dboptions/datasource_name", datasource_name);
        }
        
        XMLUtil.updateNode(root, rootName + "mailoptions/default_mail_from", default_mail_from);
        XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/enable_mail_source", String.valueOf(isReceiveUseMailsource));
        if (isReceiveUseMailsource == false) {
            XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/mail_server", receive_mail_server);
            XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/username", receive_mail_username);
            XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/password", receive_mail_password);
            XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/port", String.valueOf(receive_mail_port));
        } else {
            XMLUtil.updateNode(root, rootName + "mailoptions/receive_mail/mail_source_name", receive_mail_source_name);            
        }

        XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/use_embeded_smtp_mail_server", String.valueOf(isSendUseSMTPServer));
        if (isSendUseSMTPServer) {
            //do nothing
        } else {
            XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/enable_mail_source", String.valueOf(isSendUseMailsource));
            if (isSendUseMailsource == false) {
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/use_secure_connection", String.valueOf(isSendUseSecureConnection));
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/mail_server", send_mail_server);
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/username", send_mail_username);
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/password", send_mail_password);
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/port", String.valueOf(send_mail_port));
            } else {
                XMLUtil.updateNode(root, rootName + "mailoptions/send_mail/mail_source_name", send_mail_source_name);
            }
            
        }
        
        XMLUtil.updateNode(root, rootName + "paramoptions/context_path", context_path);
        XMLUtil.updateNode(root, rootName + "paramoptions/server_path", server_path);
        
        XMLUtil.updateNode(root, rootName + "dateoptions/server_hour_offset", String.valueOf(server_hour_offset));
        
        XMLUtil.updateNode(root, rootName + "useragentoptions/blocked_user_agent", String.valueOf(blocked_user_agent));
          
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/timermanager_datasource", timermanager_datasource);
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/enable_link_nofollow", String.valueOf(enable_link_nofollow));
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/enable_encode_url", String.valueOf(enable_encode_url));
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/portal_type", portal_type);
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/allow_http_referer_prefix_list", allow_http_referer_prefix_list);
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/mvncoreservice_implementation", mvncoreservice_implementation);
        XMLUtil.updateNode(root, rootName + "mvncoreconfig/timertaskext_implementation_list", timertaskext_implementation_list);

        XMLUtil.updateNode(root, rootName + "interceptor/mailinterceptor_implementation", mailinterceptor_implementation);
        XMLUtil.updateNode(root, rootName + "interceptor/contentinterceptor_implementation", contentinterceptor_implementation);
        XMLUtil.updateNode(root, rootName + "interceptor/loginidinterceptor_implementation", loginidinterceptor_implementation);
        XMLUtil.updateNode(root, rootName + "interceptor/passwordinterceptor_implementation", passwordinterceptor_implementation);

        saveDocument(document, strPathName + "mvncore.xml");

        MVNCoreConfig.reload();
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateConfigMVNCore");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update config mvncore", actionDesc, EventLogService.HIGH);

        request.setAttribute("action", "updatemvncore");
    }

    public void updateUrlPattern(GenericRequest request)
        throws BadInputException, DocumentException, IOException, AuthenticationException, DatabaseException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // now check the password
        MyUtil.ensureCorrectCurrentPassword(request);

        String admin_url = GenericParamUtil.getParameterSafe(request, "admin_url", true);
        String user_url = GenericParamUtil.getParameterSafe(request, "user_url", true);

        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        String xmlFilePath = strPathName + "mvnforum.xml";
        Document document = reader.read(new File(xmlFilePath));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "adminmoduleconfig/url_pattern", admin_url);
        XMLUtil.updateNode(root, rootName + "usermoduleconfig/url_pattern", user_url);

        saveDocument(document, xmlFilePath);
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateURLPattern");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update url pattern", actionDesc, EventLogService.HIGH);

        request.setAttribute("action", "updateurls");
    }

    public void configBackupProcess(GenericRequest request)
        throws IOException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String servletClassPath = FileUtil.getServletClassesPath();
        EnvironmentService environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService(); 

        String mvnCoreFileName      = servletClassPath + File.separator + "mvncore.xml";
        String mvnForumFileName     = servletClassPath + File.separator + "mvnforum.xml";
        String mvnForumEntFileName  = servletClassPath + File.separator + "mvnforum_enterprise.xml";
        String mvnAdFileName        = servletClassPath + File.separator + "mvnad.xml";
        String mvnCmsFileName       = servletClassPath + File.separator + "mvncms.xml";

        String pattern = "yyyy-MM-dd_HH-mm-ss";
        String nowDesc = DateUtil.format(new Date(), pattern);
        String newMvnCoreFileName       = servletClassPath + File.separator + "mvncore_" + nowDesc + ".xml";
        String newMvnForumFileName      = servletClassPath + File.separator + "mvnforum_" + nowDesc + ".xml";
        String newMvnForumEntFileName   = servletClassPath + File.separator + "mvnforum_enterprise" + nowDesc + ".xml";
        String newMvnAdFileName         = servletClassPath + File.separator + "mvnad" + nowDesc + ".xml";
        String newMvnCmsFileName        = servletClassPath + File.separator + "mvncms" + nowDesc + ".xml";

        // We don't have to check files here, we check it in FileUtil.copyFile
        FileUtil.copyFile(mvnCoreFileName, newMvnCoreFileName, false);
        FileUtil.copyFile(mvnForumFileName, newMvnForumFileName, false);
        
        if (environmentService.getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
            FileUtil.copyFile(mvnForumEntFileName, newMvnForumEntFileName, false);
            File newMvnForumEntFile = new File(newMvnForumEntFileName);
            request.setAttribute("MvnForumEntFileName", newMvnForumEntFile.getAbsolutePath());
        }
        if (environmentService.getAdRunMode() != EnvironmentService.PRODUCT_DISABLED) {
            FileUtil.copyFile(mvnAdFileName, newMvnAdFileName, false);
            File newMvnAdFile = new File(newMvnAdFileName);
            request.setAttribute("MvnAdFileName", newMvnAdFile.getAbsolutePath());
        }
        if (environmentService.getCmsRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
            FileUtil.copyFile(mvnCmsFileName, newMvnCmsFileName, false);
            File newMvnCmsFile = new File(newMvnCmsFileName);
            request.setAttribute("MvnCmsFileName", newMvnCmsFile.getAbsolutePath());
        }

        File newMVNCoreFile = new File(newMvnCoreFileName);
        File newMVNForumFile = new File(newMvnForumFileName);

        request.setAttribute("MvnCoreFileName", newMVNCoreFile.getAbsolutePath());
        request.setAttribute("MvnForumFileName", newMVNForumFile.getAbsolutePath());
        request.setAttribute("step", "Backup");
    }

    private void saveDocument(Document doc, String fileName) throws IOException {
        XMLWriter writer = new XMLWriter(new FileWriter(fileName));
        writer.write(doc);
        writer.close();
    }

    public void prepareEditTemplate(GenericRequest request)
        throws AuthenticationException, DatabaseException, IOException,
        FileNotFoundException, BadInputException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        String templateName = GenericParamUtil.getParameterSafe(request, "template", false);
        StringUtil.checkGoodName(templateName, locale);
        String subject = "";
        String body = "";
        final String SUBJECT_SUFFIX = "_subject";
        final String BODY_SUFFIX = "_body";
        final String EXTENSION = ".ftl";

        if (templateName.equals("")) {
            templateName = MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX;
        }

        // check for valid input
        if ((templateName.equals(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX) == false)) {
            throw new AssertionError("Does not accept invalid input: " + templateName);
        }

        String templateDir = MVNForumConfig.getTemplateDir();
        String subjectFileName = templateDir + File.separator + templateName + SUBJECT_SUFFIX;
        String bodyFileName    = templateDir + File.separator + templateName + BODY_SUFFIX;

        if (MVNForumConfig.getSendWatchMailAsHTML() &&
            templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX)) {
            bodyFileName += "_html";
        }

        subjectFileName += EXTENSION;
        bodyFileName += EXTENSION;

//        subject = new String(FileUtil.getBytes(new FileInputStream(subjectFileName)), "UTF-8");
//        body    = new String(FileUtil.getBytes(new FileInputStream(bodyFileName)), "UTF-8");
        subject = FileUtil.readFile(subjectFileName, "UTF-8");
        body    = FileUtil.readFile(bodyFileName, "UTF-8");

        request.setAttribute("TemplateSubject", subject);
        request.setAttribute("TemplateBody", body);
    }

    public void processEditTemplate(GenericRequest request)
        throws DatabaseException, AuthenticationException, IOException,
        FileNotFoundException, BadInputException, TemplateException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        final String SUBJECT_SUFFIX = "_subject";
        final String BODY_SUFFIX = "_body";
        final String EXTENSION = ".ftl";

        String templateName = GenericParamUtil.getParameterSafe(request, "template", true);
        StringUtil.checkGoodName(templateName, locale);

        // checking valid input here
        if ((templateName.equals(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX) == false) &&
            (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX) == false)) {
            throw new AssertionError("Does not accept invalid input: " + templateName);
        }

        String subject = GenericParamUtil.getParameter(request, "subject", true);
        String body    = GenericParamUtil.getParameter(request, "body", true);

        String templateDir = MVNForumConfig.getTemplateDir();
        String subjectFileName = templateDir + File.separator + templateName + SUBJECT_SUFFIX;
        String bodyFileName    = templateDir + File.separator + templateName + BODY_SUFFIX;

        if (MVNForumConfig.getSendWatchMailAsHTML() &&
            templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX)) {
            bodyFileName += "_html";
        }

        subjectFileName += EXTENSION;
        bodyFileName += EXTENSION;

        // just for testing FreeMarker
        SimpleHash root = new SimpleHash();
        try {
            Configuration cfg = MVNForumConfig.getFreeMarkerConfiguration();
            if (templateName.equals(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_PREFIX)) {
                root.put("serverName", "");
                root.put("MVNForumInfo", "");
                root.put("activationUrl", "");
                root.put("memberName", "");
                root.put("activateCode", "");
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_FORGOTPASSWORD_PREFIX)) {
                root.put("serverName", "");
                root.put("MVNForumInfo", "");
                root.put("passwordResetUrl", "");
                root.put("memberName", "");
                root.put("currentTempPassword", "");
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX)) {
                root.put("postID", "");
                root.put("forumID", "");
                root.put("threadID", "");
                root.put("memberID", "");
                root.put("postURL", "");
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_SINGLE_PREFIX)) {
                root.put("lastSent", "");
                root.put("now", "");
                root.put("forumBase", "");

                SimpleHash subRoot = new SimpleHash();
                subRoot.put("categoryName", "");
                subRoot.put("forumName", "");
                subRoot.put("threadTopic", "");
                subRoot.put("threadBody", "");
                subRoot.put("memberName", "");
                subRoot.put("threadUrl", "");
                subRoot.put("lastPostTopic", "");
                subRoot.put("lastPostBody", "");
                subRoot.put("lastPostUrl", "");                
                subRoot.put("lastPostMemberName", "");
                subRoot.put("threadLastPostDate", "");
                subRoot.put("leader", true);

                ArrayList list = new ArrayList();
                list.add(subRoot);

                Iterator iter = list.iterator();

                root.put("threadWatch", iter.next());
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_DIGEST_PREFIX)) {
                root.put("lastSent", "");
                root.put("now", "");
                root.put("forumBase", "");

                ArrayList list = new ArrayList();
                SimpleHash subRoot = new SimpleHash();
                subRoot.put("leader", true);
                subRoot.put("categoryName", "");
                subRoot.put("forumName", "");
                subRoot.put("threadTopic", "");
                subRoot.put("threadBody", "");
                subRoot.put("memberName", "");
                subRoot.put("threadUrl", "");
                subRoot.put("lastPostTopic", "");
                subRoot.put("lastPostBody", "");
                subRoot.put("lastPostUrl", "");
                subRoot.put("lastPostMemberName", "");
                subRoot.put("threadLastPostDate", "");
                list.add(subRoot);

                root.put("threadWatchList", list);
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX)) {
                root.put("lastSent", "");
                root.put("now", "");
                root.put("forumBase", "");

                SimpleHash subRoot = new SimpleHash();
                subRoot.put("categoryName", "");
                subRoot.put("forumName", "");
                subRoot.put("memberReceiveMail", "");
                subRoot.put("confirmedCode", "");
                subRoot.put("threadID", "");
                subRoot.put("memberName", "");
                subRoot.put("threadTopic", "");
                subRoot.put("threadBody", "");
                subRoot.put("threadBodyLong", "");
                subRoot.put("threadBodyShort", "");
                subRoot.put("threadUrl", "");
                subRoot.put("lastPostTopic", "");
                subRoot.put("lastPostBody", "");
                subRoot.put("lastPostUrl", "");
                subRoot.put("lastPostMemberName", "");
                subRoot.put("threadLastPostDate", "");
                subRoot.put("leader", true);
                subRoot.put("haveConfirmedCode", true);

                ArrayList list = new ArrayList();
                list.add(subRoot);

                Iterator iter = list.iterator();

                root.put("threadWatch", iter.next());
            } else if (templateName.equals(MVNForumGlobal.TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX)) {
                root.put("lastSent", "");
                root.put("now", "");
                root.put("forumBase", "");

                SimpleHash subRoot = new SimpleHash();
                subRoot.put("categoryName", "");
                subRoot.put("forumName", "");
                subRoot.put("memberReceiveMail", "");
                subRoot.put("confirmedCode", "");
                subRoot.put("threadID", "");
                subRoot.put("memberName", "");
                subRoot.put("threadTopic", "");
                subRoot.put("threadBody", "");
                subRoot.put("threadBodyLong", "");
                subRoot.put("threadUrl", "");
                subRoot.put("lastPostTopic", "");
                subRoot.put("lastPostBody", "");
                subRoot.put("lastPostUrl", "");
                subRoot.put("lastPostMemberName", "");
                subRoot.put("threadLastPostDate", "");
                subRoot.put("leader", true);
                subRoot.put("haveConfirmedCode", true);
                subRoot.put("mailFrom", "");

                ArrayList list = new ArrayList();
                list.add(subRoot);

                root.put("threadWatchList", list);
            } else {
                AssertionUtil.doAssert(false, "Does not accept invalid input: " + templateName);
            }
            StringReader subjectReader = new StringReader(subject);
            StringReader bodyReader = new StringReader(body);

            Template subjectTemplate = new Template("", subjectReader, cfg, "");
            StringWriter subjectWriter = new StringWriter(256);
            subjectTemplate.process(root, subjectWriter);

            Template bodyTemplate = new Template("", bodyReader, cfg, "");
            StringWriter bodyWriter = new StringWriter(1024);
            bodyTemplate.process(root, bodyWriter);
        } catch(TemplateException ex) {
            throw ex;
        }
        FileUtil.writeFile(subject, subjectFileName, "UTF-8");
        FileUtil.writeFile(body, bodyFileName, "UTF-8");
        
        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.UpdateTemplate", new Object[]{templateName});
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update template", actionDesc, EventLogService.MEDIUM);

        request.setAttribute("Successful", "true");
        request.setAttribute("TemplateSubject", subject);
        request.setAttribute("TemplateBody", body);
    }

    /**
     * This method reload the mvnforum.xml and mvncore.xml, then overload the environment.
     * 
     * @param request
     * @throws DatabaseException
     * @throws AuthenticationException
     */
    public void commitConfig(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();
        
        MVNForumConfig.reload();
        MVNCoreConfig.reload();
        
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().overloadEnvironment();
        
        request.setAttribute("action", "commit");
    }

}
