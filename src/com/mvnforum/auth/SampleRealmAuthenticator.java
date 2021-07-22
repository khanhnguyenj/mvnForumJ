/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/SampleRealmAuthenticator.java,v 1.25 2010/06/15 10:29:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.25 $
 * $Date: 2010/06/15 10:29:46 $
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
package com.mvnforum.auth;

import java.util.Locale;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.common.MemberUtil;
import com.mvnforum.db.DAOFactory;

/**
 * This is a sample class that implement Authenticator for authenticate user when the system use
 * authentication type: Realm <p>
 * Use this class as a sample to write your own Single Sign-On solution
 */
public class SampleRealmAuthenticator extends AbstractSampleAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(SampleRealmAuthenticator.class);

    public String getRemoteUser(GenericRequest request) {

        String memberName = request.getRemoteUser();
        memberName = StringUtil.getEmptyStringIfNull(memberName);
        //log.debug("Member Name is " + memberName);

        try {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            StringUtil.checkGoodName(memberName, locale);
        } catch (BadInputException e) {
            //MVN accepted for StringBeans's usernames below  :
            if (memberName.equals("default-template") ||
                memberName.equals("login-template") ||
                memberName.equals("preference-template") ||
                memberName.equals("view-user-template") ||
                memberName.equals("home-template")) {
                //do nothing
            } else {
                log.warn("Cannot accept remote user with a bad username :" + memberName, e);
            }
            return null;
        }

        if (memberName.length() > 0) {
            try {
                if (MemberUtil.checkUsernameExistedInSession(request, memberName) == false) {
                    DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
                    MemberUtil.storeUsernameExistedInSession(request, memberName);
                } 
            } catch (ObjectNotFoundException onfe) {
                // memberName does not exist, let to add an account "memberName"
                try {
                    MemberUtil.createRemoteUserAccount(memberName, request);
                } catch (Exception e) {
                    // just return null;
                    log.error("Error", e);
                    return null;// just return null
                }
            } catch (DatabaseException de) {
                log.error("Database Error", de);
                return null;// just return null
            }
        }
        //log.debug("Member Name is " + memberName);
        return memberName;
    }

    public boolean isCorrectCurrentPassword(String memberName, String password, boolean encoded) { 

        int memberID = 0; 
        try {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
            String pass = DAOFactory.getMemberDAO().getPassword(memberID);
            if (encoded == false) {
                password = Encoder.getMD5_Base64(password);
            }
            return password.equals(pass);
        } catch (ObjectNotFoundException e) {
            log.error("Error: ", e);
            return false;
        } catch (DatabaseException e) {
            log.error("Error: ", e);
            return false;
        }
    }
}
