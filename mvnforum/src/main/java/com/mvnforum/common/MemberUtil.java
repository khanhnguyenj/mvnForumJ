/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/MemberUtil.java,v 1.5 2010/08/10 11:57:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2010/08/10 11:57:37 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import com.mvnforum.search.member.MemberIndexer;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.web.GenericRequest;

public final class MemberUtil {

    private static final Logger log = LoggerFactory.getLogger(MemberUtil.class);

    private static final String MVNFORUM_SESSION_EXISTED_USERNAME = "mvnforum.session.existed.username";

    private MemberUtil() {
    }

    /**
     * This method is used to check the reserved username, that user should never allowed to register.
     * @param memberName
     * @param locale
     * @throws BadInputException
     */
    public static void checkReservedUsername(String memberName, Locale locale) throws BadInputException {
        /** @todo move to a name filter */
        if ( memberName.equalsIgnoreCase(MVNForumConfig.getDefaultGuestName()) ||
             memberName.equalsIgnoreCase("guest") ||
             memberName.equalsIgnoreCase("admin") ||
             memberName.equalsIgnoreCase("administrator") ||
             memberName.equalsIgnoreCase("moderator") ) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_register_with_reserved_name", new Object[] {memberName});
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot register member with a reserved name : " + memberName);
        }
    }

    /**
     * This method to store the username in the session so we dont have to check if username in database or not
     *
     * @param genericRequest
     * @param username
     */
    public static void storeUsernameExistedInSession(GenericRequest genericRequest, String username) {
        if ( (username == null) || (username.length() == 0) ) {
            throw new IllegalArgumentException("variable username must not be null or empty");
        }
        genericRequest.setSessionAttribute(MVNFORUM_SESSION_EXISTED_USERNAME, username);
    }

    /**
     * Return true if this username already store in session before by method storeUsernameExistedInSession
     *
     * @param genericRequest
     * @param username
     * @return
     */
    public static boolean checkUsernameExistedInSession(GenericRequest genericRequest, String username) {
        if ( (username == null) || (username.length() == 0) ) {
            throw new IllegalArgumentException("variable username must not be null or empty");
        }
        String sessionUsername = (String)genericRequest.getSessionAttribute(MVNFORUM_SESSION_EXISTED_USERNAME);
        return username.equals(sessionUsername);
    }

    /**
     * This method is usually be used to create new user account in the Authenticator sub-class
     *
     * @param memberName
     * @param request
     * @throws ObjectNotFoundException
     * @throws CreateException
     * @throws DatabaseException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     */
    public static void createRemoteUserAccount(String memberName, GenericRequest request)
        throws ObjectNotFoundException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Date memberBirthday = new Date(now.getTime());

        // You should think of creating the email based on your company policy
        String email = memberName + "@yourdomain.com";
        int memberEmailVisible = MVNForumConfig.getEnableShowEmailVisibleAsDefaultWhenCreateRemoteUser() ? 1 : 0;

        DAOFactory.getMemberDAO().create(memberName, OnlineUserManager.PASSWORD_OF_METHOD_CUSTOMIZATION, email,
                        email, memberEmailVisible, MemberBean.MEMBER_NAME_VISIBLE,
                        request.getRemoteAddr(), request.getRemoteAddr(), 0/* memberViewCount */,
                        0/* memberPostCount */, now /*memberCreationDate*/, now /*memberModifiedDate*/, now /*memberExpireDate*/, now /*memberPasswordExpireDate*/, now/* memberLastLogon */, 0,
                        MemberBean.MEMBER_STATUS_ENABLE, ""/* memberActivateCode */, ""/* memberTempPassword */,
                        0/* memberMessageCount */, 0, 10, 0/* memberWarnCount */,
                        0/* memberVoteCount */, 0/* memberVoteTotalStars */,
                        0/* memberRewardPoints */, ""/* memberTitle */, 0/* memberTimeZone */,
                        ""/* memberSignature */, ""/* memberAvatar */, ""/* memberSkin */,
                        ""/* memberLanguage */, ""/* memberFirstname */, ""/* memberLastname */,
                        MemberBean.MEMBER_GENDER_MALE/* memberGender */, memberBirthday, ""/* memberAddress */,
                        ""/* memberCity */, ""/* memberState */, ""/* memberCountry */,
                        ""/* memberPhone */, ""/* memberMobile */, ""/* memberFax */,
                        ""/* memberCareer */, ""/* memberHomepage */);

        // Now, create 4 default folders for each member
        int memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
        int folderStatus = 0;
        int folderOption = 0;
        int folderType = 0;
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, memberID,
                                                0/* order */, folderStatus, folderOption,
                                                folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, memberID,
                                                1/* order */, folderStatus, folderOption,
                                                folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, memberID,
                                                2/* order */, folderStatus, folderOption,
                                                folderType, now, now);
        DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, memberID,
                                                3/* order */, folderStatus, folderOption,
                                                folderType, now, now);

        // Add member to the lucene index
        MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        MemberIndexer.scheduleAddMemberTask(memberBean);

        // now, if require activation, then we will send mail
        // Note that because after this page succeed,
        // we redirect to usermanagement so not use mvnforum.mail.failed now
        if (MVNForumConfig.getRequireActivation()) {
            String serverName = ParamUtil.getServerPath(); // ParamUtil.getServer2(request);
            try {
                SendMailUtil.sendActivationCodeEmail(memberID, serverName);
            } catch (Exception ex) {
                log.error("Cannot send mail after registration!", ex);
                request.setAttribute("mvnforum.mail.failed",
                                     "Cannot send activation email after registration!");
                // @todo: save the error message to displayed later
            }
        }
    }

}
