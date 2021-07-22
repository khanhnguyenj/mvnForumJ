/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/MailWebHandler.java,v 1.31 2008/12/31 03:50:23 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.31 $
 * $Date: 2008/12/31 03:50:23 $
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
 * @author: Hau Nguyen
 */
package com.mvnforum.user;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;
import com.mvnforum.db.MemberCache;

public class MailWebHandler {

    private static final Logger log = LoggerFactory.getLogger(MailWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public void prepareSendMail(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        // from
        int fromID = onlineUser.getMemberID();
        String fromEmail = MemberCache.getInstance().getMember(fromID).getMemberEmail();
        request.setAttribute("FromEmail", fromEmail);

        // subject
        String subject = GenericParamUtil.getParameterFilter(request, "Subject");
        request.setAttribute("Subject", subject);
        
        // message
        String message = GenericParamUtil.getParameterFilter(request, "Message");
        
        // to
        String toMember = "";
        boolean emailToAdmin = GenericParamUtil.getParameterBoolean(request, "ToAdmin");
        if (emailToAdmin == false) {
            toMember = GenericParamUtil.getParameterFilter(request, "ToMember", true);
            try {
                MemberCache.getInstance().getMemberIDFromMemberName(toMember);
            } catch(ObjectNotFoundException e) {
                Locale locale = I18nUtil.getLocaleInRequest(request);
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.membername_not_exists", new Object[] {toMember});
                throw new ObjectNotFoundException(localizedMessage);
            }
        } else if ((subject.equals("") == false) && (message.equals(""))) {
            
            final String regex = "Report threatening content \\(ThreadID = (\\d+) and PostID = (\\d+)\\)";
            
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(subject);
            
            if (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String realSubject = subject.substring(start, end);
                int firstPos = realSubject.indexOf("=");
                String threadIDByString = realSubject.substring(firstPos + 1, realSubject.indexOf(" ", firstPos + 2)).trim();
                
                StringBuffer threadURL = new StringBuffer();
                int threadID = 0;
                try {
                    threadID = Integer.parseInt(threadIDByString);
                    threadURL.append(ParamUtil.getServerPath()).append(ParamUtil.getContextPath()).append(UserModuleConfig.getUrlPattern());
                    threadURL.append("/viewthread?thread=");
                    threadURL.append(threadID);
                } catch (NumberFormatException e) {
                    
                }
                if (threadID > 0) {
                    int secondPos = realSubject.lastIndexOf("=");
                    String postIDByString = realSubject.substring(secondPos + 1, realSubject.length() - 1).trim();
                    try {
                        int postID = Integer.parseInt(postIDByString);
                        threadURL.append("#").append(postID);
                    } catch (NumberFormatException e) {
                        
                    }
                    message = "Thread: " + ((MVNForumConfig.getEnableFriendlyURL()) ? FriendlyURLParamUtil.createFriendlyURL(threadURL.toString()) : threadURL.toString()); 
                }
            }

        }
        
        request.setAttribute("Message", message);
        request.setAttribute("ToAdmin", emailToAdmin ? "true" : "");
        request.setAttribute("ToMember", toMember);
        
    }

    public void sendEmailProcess(GenericRequest request)
        throws AuthenticationException, DatabaseException, BadInputException, MessagingException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int fromID = onlineUser.getMemberID();
        String from = MemberCache.getInstance().getMember(fromID).getMemberEmail();

        String subject = GenericParamUtil.getParameter(request, "Subject", true);
        String message = GenericParamUtil.getParameter(request, "Message", true);

        String to = null;
        boolean emailToAdmin = GenericParamUtil.getParameterBoolean(request, "ToAdmin");
        if (emailToAdmin == false) {
            String toMember = GenericParamUtil.getParameterFilter(request, "ToMember", true);
            int toID = MemberCache.getInstance().getMemberIDFromMemberName(toMember);
            to = MemberCache.getInstance().getMember(toID).getMemberEmail();
        } else {
            to = MVNForumConfig.getWebMasterEmail();
        }
        if (to.length() == 0) {
            Locale locale = I18nUtil.getLocaleInRequest(request);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.please_enter_to_or_cc_or_bcc");
            throw new BadInputException(localizedMessage);
        }

        MailMessageStruct mailMessageStruct = new MailMessageStruct();
        mailMessageStruct.setFrom(from);
        mailMessageStruct.setTo(to);
        mailMessageStruct.setSubject(subject);
        mailMessageStruct.setMessage(message);

        try {
            MailUtil.sendMail(mailMessageStruct);
        } catch (UnsupportedEncodingException e) {
            log.error("Cannot support encoding", e);
        } 

    }

}
