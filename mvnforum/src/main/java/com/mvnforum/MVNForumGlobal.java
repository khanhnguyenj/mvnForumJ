/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/MVNForumGlobal.java,v 1.52 2010/07/19 07:34:07 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.52 $
 * $Date: 2010/07/19 07:34:07 $
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

public final class MVNForumGlobal {

    private MVNForumGlobal() {
    }

/*************************************************************************
 * NOTE: below constants can be changed for each build,
 *       these constants if changed, MUST NOT break the compatibility
 *************************************************************************/
    public static final String IMAGE_DIR                 = "/mvnplugin/mvnforum/images";

    public static final String EMOTION_DIR               = "/mvnplugin/mvnforum/images/emotion/";

    public static final String CSS_FULLPATH              = "/mvnplugin/mvnforum/css/style.css";

    public static final String CSS_PREVIEW_FULLPATH      = "/mvnplugin/mvnforum/css/style_preview.css";

    public static final String CSS_BACKUP_FULLPATH       = "/mvnplugin/mvnforum/css/style_backup.css";

    public static final String LOGO_FULLPATH             = "/mvnplugin/mvnforum/images/logo.svg";

    // Note that we cannot put / at the end because getRealPath will remove it in Tomcat 4.1.7 :((
    // minhnn: on 30 Dec 2008, I checked and this variable should not be used any where, because now
    //         avatar is saved in mvnForumHome/memberavatars
    public static final String UPLOADED_AVATAR_DIR       = "/mvnplugin/mvnforum/upload/memberavatars";

    /**
     * These variable is used in MemberWebHandler to scale thumbnail when upload avatar
     */
    public static final int UPLOADED_MEMBER_AVATAR_MAX_WIDTH    = 150;
    public static final int UPLOADED_MEMBER_AVATAR_MAX_HEIGHT   = 150;

    /**
     * These variable is used in AlbumWebHandler to scale thumbnail when upload album avatar or set photo as cover (avatar)
     */
    public static final int UPLOADED_ALBUM_AVATAR_MAX_WIDTH     = 160;
    public static final int UPLOADED_ALBUM_AVATAR_MAX_HEIGHT    = 160;

    public static final String RESOURCE_BUNDLE_NAME       = "i18n/mvnForum_i18n";

    public static final String MVNFORUM_JAAS_LOGIN_MODULE = "mvnforum_login";

    public static final String MVNFORUM_LOGINED_USER_MAP  = "LoginedUserMap";

    /** value to control the flood prevention. Note value from 0 to 999 is belong to mvnCore */
    public static final Integer FLOOD_ID_NEW_POST_PER_IP        = new Integer(1000);
    public static final Integer FLOOD_ID_NEW_MEMBER_PER_IP      = new Integer(1001);
    public static final Integer FLOOD_ID_LOGIN_PER_IP           = new Integer(1002);
    public static final Integer FLOOD_ID_NEW_MESSAGE_PER_IP     = new Integer(1003);
    public static final Integer FLOOD_ID_NEW_POST_PER_MEMBER    = new Integer(1004);
    public static final Integer FLOOD_ID_HTTP_REQUEST_PER_IP    = new Integer(1005);

    /** The maximum length of the email in database */
    public static final int MAX_MEMBER_EMAIL_LENGTH = 60;

    /** The maximum length of the member login name in database */
    public static final int MAX_MEMBER_LOGIN_LENGTH = 30;

    /** The type of search index: Disk */
    public static final int SEARCH_INDEX_TYPE_DISK     = 0;
    /** The type of search index: Database */
    public static final int SEARCH_INDEX_TYPE_DATABASE = 1;

    public static final int MIN_MINUTES_TO_RATE_ALBUM_ITEM_AGAIN = 5;

    public static final int MIN_MINUTES_TO_VOTE_POLL_AGAIN       = 5;

    public static final int TOPIC_NAME_SHORTER_CHARACTERS        = 25;

    public static final String TEMPLATE_SENDACTIVATECODE_PREFIX  = "sendactivemailtemplate";
    public static final String TEMPLATE_SENDACTIVATECODE_SUBJECT = "sendactivemailtemplate_subject.ftl";
    public static final String TEMPLATE_SENDACTIVATECODE_BODY    = "sendactivemailtemplate_body.ftl";

    public static final String TEMPLATE_FORGOTPASSWORD_PREFIX    = "forgotpasswordtemplate";
    public static final String TEMPLATE_FORGOTPASSWORD_SUBJECT   = "forgotpasswordtemplate_subject.ftl";
    public static final String TEMPLATE_FORGOTPASSWORD_BODY      = "forgotpasswordtemplate_body.ftl";

    public static final String TEMPLATE_WATCHMAIL_DIGEST_PREFIX         = "watchmailtemplate_digest";
    public static final String TEMPLATE_WATCHMAIL_DIGEST_SUBJECT        = "watchmailtemplate_digest_subject.ftl";
    public static final String TEMPLATE_WATCHMAIL_DIGEST_BODY           = "watchmailtemplate_digest_body.ftl";

    public static final String TEMPLATE_WATCHMAIL_SINGLE_PREFIX         = "watchmailtemplate_single";
    public static final String TEMPLATE_WATCHMAIL_SINGLE_SUBJECT        = "watchmailtemplate_single_subject.ftl";
    public static final String TEMPLATE_WATCHMAIL_SINGLE_BODY           = "watchmailtemplate_single_body.ftl";

    public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_PREFIX     = "watchmailtemplate_gateway_digest";
    public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_SUBJECT    = "watchmailtemplate_gateway_digest_subject.ftl";
    public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_BODY       = "watchmailtemplate_gateway_digest_body.ftl";
    public static final String TEMPLATE_WATCHMAIL_GATEWAY_DIGEST_BODY_HTML  = "watchmailtemplate_gateway_digest_body_html.ftl";

    public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_PREFIX     = "watchmailtemplate_gateway_single";
    public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_SUBJECT    = "watchmailtemplate_gateway_single_subject.ftl";
    public static final String TEMPLATE_WATCHMAIL_GATEWAY_SINGLE_BODY       = "watchmailtemplate_gateway_single_body.ftl";

    public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_PREFIX  = "sendmailtemplate_postcensored";
    public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_SUBJECT = "sendmailtemplate_postcensored_subject.ftl";
    public static final String TEMPLATE_SENDMAIL_BECAUSE_CENSORED_POST_BODY    = "sendmailtemplate_postcensored_body.ftl";

}
