/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/AjaxValidator.java,v 1.9 2009/06/26 07:54:22 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/06/26 07:54:22 $
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
 * @author: Nguyendnc
 */
package com.mvnforum.user;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.common.MemberUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberCache;

public class AjaxValidator {

    private static final Logger log = LoggerFactory.getLogger(AjaxValidator.class);
    
    private static final int AJAX_USERNAME  = 1;
    private static final int AJAX_EMAIL     = 2;
    
    void validate(GenericRequest request, GenericResponse response) 
        throws IOException {
        
        //SecurityUtil.checkHttpPostMethod(request);

        int action = 0;
        try {
            action = GenericParamUtil.getParameterInt(request, "action");
        } catch (BadInputException e) {
            // ignore
        }
        
        if (action == AJAX_USERNAME) {
            String userName = GenericParamUtil.getParameter(request, "userName");
            processValidateUserID(userName, response);
        } else if (action == AJAX_EMAIL) {
            String emailAddress = GenericParamUtil.getParameter(request, "emailAddress");
            processValidateEmail(emailAddress, response);
        } else {
            HttpServletResponse servletResponse = response.getServletResponse();
            servletResponse.setContentType("text/xml");
            servletResponse.setHeader("Cache-Control", "no-cache");
            
            servletResponse.getWriter().write("<message>no_action</message>");
        }
    }
    
    private void processValidateUserID(String userName, GenericResponse response) 
        throws IOException {
        
        HttpServletResponse servletResponse = response.getServletResponse();
        servletResponse.setContentType("text/xml");
        servletResponse.setHeader("Cache-Control", "no-cache");
        
        if ( (userName == null) || (userName.length() == 0) ) {
            servletResponse.getWriter().write("<message>empty</message>");
            return;
        }
        
        int id = 0;
        try {
            id = MemberCache.getInstance().getMemberIDFromMemberName(userName);
        } catch (ObjectNotFoundException e) {
            // do nothing
        } catch (Exception e) {
            log.error("Cannot get member", e);
            servletResponse.getWriter().write("<message>internal_error</message>");
            return;
        }
        
        if (id != 0) {
            servletResponse.getWriter().write("<message>existed</message>");
            return;
        }

        // username does not exist in database, go ahead
        try {
            StringUtil.checkGoodName(userName, null);
            MemberUtil.checkReservedUsername(userName, null);
            InterceptorService.getInstance().validateLoginID(userName);
            servletResponse.getWriter().write("<message>valid</message>"); 
        } catch (BadInputException e) { //not good name or reserved name
            servletResponse.getWriter().write("<message>not_good_name</message>"); 
        } catch (InterceptorException e) { //this email has been banned
            servletResponse.getWriter().write("<message>banned_username</message>"); 
        }
    }
    
    private void processValidateEmail(String emailAddress, GenericResponse response) 
        throws IOException {
        
        HttpServletResponse servletResponse = response.getServletResponse();
        servletResponse.setContentType("text/xml");
        servletResponse.setHeader("Cache-Control", "no-cache");
        
        if ( (emailAddress == null) || (emailAddress.length() == 0) ) {
            servletResponse.getWriter().write("<message>empty</message>");
            return;
        }

        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(emailAddress);

            servletResponse.getWriter().write("<message>existed</message>");
            return;
        } catch (ObjectNotFoundException ex) {
            // do nothing
        } catch (Exception e) {
            log.error("Cannot find email address", e);
            servletResponse.getWriter().write("<message>internal_error</message>");
            return;
        }
        
        try {
            MailUtil.checkGoodEmail(emailAddress);
            InterceptorService.getInstance().validateMail(emailAddress); // check for banned email
            
            servletResponse.getWriter().write("<message>valid</message>"); 
        } catch (InterceptorException e) {
            servletResponse.getWriter().write("<message>banned_email</message>");
        } catch (BadInputException e) {
            servletResponse.getWriter().write("<message>not_good_email</message>");
        }
    }
    
}
