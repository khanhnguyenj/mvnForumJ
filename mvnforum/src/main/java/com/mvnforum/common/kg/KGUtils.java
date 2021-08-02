/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/kg/KGUtils.java,v 1.33 2009/03/26 06:52:45 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.33 $
 * $Date: 2009/03/26 06:52:45 $
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
 * @author: Igor Manic
 */
package com.mvnforum.common.kg;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.common.SendMailUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import com.mvnforum.search.member.MemberIndexer;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.StringUtil;

/**
 * NOTE for KG
 * When deploy mvnForum, below is the check list:
 * - Set the loginID Interceptor
 * - MVNForumConfig.ENABLE_AUTO_FORUM_OWNER = true
 * - Emoticon = false
 * - Attachment = false
 * - Rss = false
 */
public final class KGUtils {

    private static final Logger log = LoggerFactory.getLogger(KGUtils.class);

    public static final String KG_PREFIX = "kg_";

    public static final int KG_FORUM_ADMIN_GROUP = 4;

    private KGUtils() {
    }

    public static void addNewMember(HttpServletRequest request, String memberName, String memberPassword, String memberEmail, Date memberBirthday)
        throws BadInputException, ObjectNotFoundException, CreateException, DatabaseException, DuplicateKeyException,
        ForeignKeyNotFoundException, InterceptorException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        memberName = KG_PREFIX + memberName;
        StringUtil.checkGoodName(memberName, locale);
        if (memberName.length() > MVNForumGlobal.MAX_MEMBER_LOGIN_LENGTH) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.member_name_too_long");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("MemberName cannot be longer than 30 characters.");
        }

        memberPassword       = Encoder.getMD5_Base64(memberPassword);

        if (memberEmail.length() > MVNForumGlobal.MAX_MEMBER_EMAIL_LENGTH) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.member_email_too_long");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("MemberEmail cannot be longer than 60 characters.");
        }
        String memberFirstEmail     = memberEmail;
        InterceptorService.getInstance().validateMail(memberFirstEmail);

        int memberEmailVisible      = MemberBean.MEMBER_EMAIL_INVISIBLE;
        int memberNameVisible       = MemberBean.MEMBER_NAME_VISIBLE;
        String memberFirstIP        = request.getRemoteAddr();
        String memberLastIP         = memberFirstIP;
        int memberOption            = 0;//@todo review and support it later
        int memberStatus            = 0;// @todo review and support it later, ex: should it be active or not?
        String memberActivateCode   = "";// @todo review and support it later
        int memberMessageOption     = 0;// @todo review and support it later
        int memberPostsPerPage      = 10; //default for all preregistered users
        if (memberBirthday == null) {
            memberBirthday          = new java.sql.Date(now.getTime());
        }
        Timestamp memberCreationDate= now;
        Timestamp memberModifiedDate= now;
        Timestamp memberExpireDate = memberCreationDate;// equal Creation Date mean no expiration
        Timestamp memberPasswordExpireDate = memberCreationDate;// equal Creation Date mean no password expiration

        DAOFactory.getMemberDAO().create(memberName, memberPassword, memberFirstEmail,
                                   memberEmail, memberEmailVisible, memberNameVisible,
                                   memberFirstIP, memberLastIP, 0/*memberViewCount*/,
                                   0/*memberPostCount*/, memberCreationDate, memberModifiedDate, memberExpireDate, memberPasswordExpireDate,
                                   now/*memberLastLogon*/, memberOption, memberStatus,
                                   memberActivateCode, ""/*memberTempPassword*/, 0/*memberMessageCount*/,
                                   memberMessageOption, memberPostsPerPage, 0/*memberWarnCount*/,
                                   0/*memberVoteCount*/, 0/*memberVoteTotalStars*/, 0/*memberRewardPoints*/,
                                   ""/*memberTitle*/, 0/*memberTimeZone*/, ""/*memberSignature*/,
                                   ""/*memberAvatar*/, ""/*memberSkin*/, ""/*memberLanguage*/,
                                   " "/*memberFirstname*/, " "/*memberLastname*/, 1/*memberGender*/,
                                   memberBirthday, ""/*memberAddress*/, ""/*memberCity*/,
                                   ""/*memberState*/, ""/*memberCountry*/, ""/*memberPhone*/,
                                   ""/*memberMobile*/, ""/*memberFax*/, ""/*memberCareer*/,
                                   ""/*memberHomepage*/);

        // Now, create 2 default folders for each member
        int memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
        int folderStatus = 0;
        int folderOption = 0;
        int folderType = 0;
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, memberID, 0/*order*/, folderStatus, folderOption, folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, memberID, 1/*order*/, folderStatus, folderOption, folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, memberID, 2/*order*/, folderStatus, folderOption, folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, memberID, 3/*order*/, folderStatus, folderOption, folderType, now, now);

        // Add member to the lucene index
        MemberBean memberBean = null;
        try {
            memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        } catch(ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(memberID)});
            throw new ObjectNotFoundException(localizedMessage);
        }
        MemberIndexer.scheduleAddMemberTask(memberBean);

        // now, if require activation, then we will send mail
        // Note that because after this page succeed,
        // we redirect to usermanagement so not use mvnforum.mail.failed now
        if (MVNForumConfig.getRequireActivation()) {
            String serverName = ParamUtil.getServerPath();//ParamUtil.getServer2(request);
            try {
                SendMailUtil.sendActivationCodeEmail(memberID, serverName);
            } catch (Exception ex) {
                log.error("Cannot send mail after registration!", ex);
                request.setAttribute("mvnforum.mail.failed", "Cannot send activation email after registration!");
                //@todo: save the error message to displayed later
            }
        }
    }

    public static void grantPermission(String memberName, int forumID)
        throws DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException, DuplicateKeyException, CreateException {

        memberName = KG_PREFIX + memberName;
        int memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        DAOFactory.getMemberForumDAO().create(memberID, forumID, MVNForumPermission.PERMISSION_FORUM_MODERATOR);
    }

    public static void revokePermission(String memberName, int forumID)
        throws DatabaseException, ObjectNotFoundException {

        memberName = KG_PREFIX + memberName;
        int memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        DAOFactory.getMemberForumDAO().delete(memberID, forumID, MVNForumPermission.PERMISSION_FORUM_MODERATOR);
    }

    public static void grantForumAdminPermission(String memberName)
        throws DatabaseException, ForeignKeyNotFoundException, DuplicateKeyException, CreateException {

        memberName = KG_PREFIX + memberName;
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        DAOFactory.getMemberGroupDAO().create(KG_FORUM_ADMIN_GROUP, memberName, 0/* default privilege*/, now/*creationDate*/, now/*modifiedDate*/, now/*expireDate*/);
    }

    /*
    public static void main(String[] args) {
        try {
            addNewMember(null, "test", "test", "test@kiengiang.gov.vn", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            grantPermission("test", 1);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }*/

}
