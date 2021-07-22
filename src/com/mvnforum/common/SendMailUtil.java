/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/SendMailUtil.java,v 1.30 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.30 $
 * $Date: 2009/01/02 15:12:54 $
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
package com.mvnforum.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberBean;
import com.mvnforum.service.MvnForumInfoService;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.user.UserModuleConfig;

import freemarker.template.*;

public final class SendMailUtil {

    private static final Logger log = LoggerFactory.getLogger(SendMailUtil.class);
    
    private static MvnForumInfoService mvnForumInfo = MvnForumServiceFactory.getMvnForumService().getMvnForumInfoService();

    private SendMailUtil() {
    }

    public static void sendActivationCodeEmail(int memberID, String serverName)
        throws ObjectNotFoundException, DatabaseException, BadInputException, MessagingException, IOException, TemplateException {

        MailMessageStruct mailMessageStruct = getActivationCodeEmail(memberID, serverName);
        try {
            MailUtil.sendMail(mailMessageStruct);
        } catch (UnsupportedEncodingException e) {
            log.error("Cannot support encoding", e);
        }
    }

    // This method can be optimized by accept input of type MemberBean
    public static MailMessageStruct getActivationCodeEmail(int memberID, String serverName)
        throws ObjectNotFoundException, DatabaseException, BadInputException, IOException, TemplateException {

        // Now, check that this member is not activated, to prevent the
        // situation that other people try to annoy this member
        String activateCode = DAOFactory.getMemberDAO().getActivateCode(memberID);
        if (activateCode.equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED)) {
            //@todo : localize me
            throw new BadInputException("Cannot activate an already activated member.");
        }

        MemberBean memberBean = DAOFactory.getMemberDAO().getMember(memberID);
        String memberName = memberBean.getMemberName();
        String memberEmail = memberBean.getMemberEmail();

        // generate a Activation code
        // Note that the activation code does not need security MD5 as in the Password Reset
        if (activateCode.equals("")) {
            // only generate activate code when the current value is empty
            // that is, if there is an activate code, re-use it.
            activateCode = String.valueOf(System.currentTimeMillis());
            DAOFactory.getMemberDAO().updateActivateCode(memberID, activateCode);
        }

        // we have pass the assertion check, go ahead
        StringBuffer activationUrl = new StringBuffer(256);
        activationUrl.append(serverName);
        activationUrl.append(ParamUtil.getContextPath());
        activationUrl.append(UserModuleConfig.getUrlPattern());
        activationUrl.append("/activatemember?activatecode=");
        activationUrl.append(activateCode);
        activationUrl.append("&member=");
        activationUrl.append(memberName);

        // Prepare the FreeMarker configuration;
        Configuration cfg = MVNForumConfig.getFreeMarkerConfiguration();

        //Below is a code to map content of email to template
        Map root = new HashMap();
        root.put("serverName", serverName);
        root.put("MVNForumInfo", mvnForumInfo.getProductDesc());
        root.put("activationUrl", activationUrl.toString());
        root.put("memberName", memberName);
        root.put("activateCode", activateCode);

        StringWriter subjectWriter = new StringWriter(256);
        Template subjectTemplate = cfg.getTemplate(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_SUBJECT, "UTF-8");
        subjectTemplate.process(root, subjectWriter);
        String subject = subjectWriter.toString();

        StringWriter bodyWriter = new StringWriter(1024);
        Template bodyTemplate = cfg.getTemplate(MVNForumGlobal.TEMPLATE_SENDACTIVATECODE_BODY, "UTF-8");
        bodyTemplate.process(root, bodyWriter);
        String body = bodyWriter.toString();

        log.debug("subject = " + subject);
        log.debug("body  = " + body);

        MailMessageStruct mailMessage = new MailMessageStruct();
        mailMessage.setFrom(MVNForumConfig.getWebMasterEmail());
        mailMessage.setTo(memberEmail);
        mailMessage.setSubject(subject);
        mailMessage.setMessage(body);

        return mailMessage;
    }
}
