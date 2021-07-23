/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/InterceptorService.java,v 1.14 2008/12/30 10:46:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.14 $
 * $Date: 2008/12/30 10:46:29 $
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
 * @author: Minh Nguyen  
 */
package net.myvietnam.mvncore.interceptor;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.exception.InterceptorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InterceptorService {

    private static final Logger log = LoggerFactory.getLogger(InterceptorService.class);

    //private static final String OPTION_FILE_NAME = "mvncore.xml";

    /** Singleton instance of this class */
    private static InterceptorService instance      = new InterceptorService();

    private MailInterceptor mailInterceptor         = null;

    private ContentInterceptor contentInterceptor   = null;

    private LoginIDInterceptor loginIDInterceptor   = null;

    private PasswordInterceptor passwordInterceptor = null;
    /**
     * Private constructor to prevent instantiation
     */
    private InterceptorService() {
        loadInterceptorInfo();
    }

    private void loadInterceptorInfo() {
        String mailInterceptorClassName = MVNCoreConfig.getMailInterceptorClassName();
        loadMailInterceptor(mailInterceptorClassName);

        String contentInterceptorClassName = MVNCoreConfig.getContentInterceptorClassName();
        loadContentInterceptor(contentInterceptorClassName);

        String loginIDInterceptorClassName = MVNCoreConfig.getLoginIdInterceptorClassName();
        loadLoginIDInterceptor(loginIDInterceptorClassName);
        
        String passwordInterceptorClassName = MVNCoreConfig.getPasswordInterceptorClassName();
        loadPasswordInterceptor(passwordInterceptorClassName);
        
    }

    private void loadContentInterceptor(String contentInterceptorClassName) {

        try {
            if (contentInterceptorClassName.length() > 0) {
                Class contentInterceptorClass = Class.forName(contentInterceptorClassName);
                ContentInterceptor interceptor = (ContentInterceptor)contentInterceptorClass.newInstance();
                setContentInterceptor(interceptor);
            }
        } catch (Exception ex) {
            log.error("Cannot load ContentInterceptor", ex);
        }
    }

    private void loadMailInterceptor(String mailInterceptorClassName) {

        try {
            if (mailInterceptorClassName.length() > 0) {
                Class mailInterceptorClass = Class.forName(mailInterceptorClassName);
                MailInterceptor interceptor = (MailInterceptor)mailInterceptorClass.newInstance();
                setMailInterceptor(interceptor);
            }
        } catch (Exception ex) {
            log.error("Cannot load MailInterceptor", ex);
        }
    }

    private void loadLoginIDInterceptor(String loginIDInterceptorClassName) {

        try {
            if (loginIDInterceptorClassName.length() > 0) {
                Class loginIDInterceptorClass = Class.forName(loginIDInterceptorClassName);
                LoginIDInterceptor interceptor = (LoginIDInterceptor)loginIDInterceptorClass.newInstance();
                setLoginIDInterceptor(interceptor);
            }
        } catch (Exception ex) {
            log.error("Cannot load LoginIDInterceptor", ex);
        }
    }
    
    private void loadPasswordInterceptor(String passwordInterceptorClassName) {

        try {
            if (passwordInterceptorClassName.length() > 0) {
                Class passwordInterceptorClass = Class.forName(passwordInterceptorClassName);
                
                PasswordInterceptor interceptor = (PasswordInterceptor)passwordInterceptorClass.newInstance();
                setPasswordInterceptor(interceptor);
            }
        } catch (Exception ex) {
            log.error("Cannot load PasswordInterceptor", ex);
        }
    }

    /**
     * Return singleton instance of this class
     *
     * @return InterceptorService the singleton instance of this class
     */
    public static InterceptorService getInstance() {
        return instance;
    }

    /**
     * Validate email if the MailInterceptor is present
     *
     * @param email String email to be validated
     * @throws InterceptorException if email is not valid for some reason
     */
    public void validateMail(String email) throws InterceptorException {
        if (mailInterceptor != null) {
            mailInterceptor.validateEmail(email);
        }
    }

    public MailInterceptor getMailInterceptor() {
        return mailInterceptor;
    }

    public void setMailInterceptor(MailInterceptor interceptor) {
        log.info("Use MailInterceptor = " + interceptor);
        this.mailInterceptor = interceptor;
    }

    /**
     * Validate content if the ContentInterceptor is present
     *
     * @param content String content to be validated
     * @return the new content
     * @throws InterceptorException if content is not valid for some reason
     */
    public String validateContent(String content) throws InterceptorException {
        if (contentInterceptor != null) {
            return contentInterceptor.validateContent(content);
        }
        return content;
    }

    public ContentInterceptor getContentInterceptor() {
        return contentInterceptor;
    }

    public void setContentInterceptor(ContentInterceptor interceptor) {
        log.info("Use ContentInterceptor = " + interceptor);
        this.contentInterceptor = interceptor;
    }

    /**
     * Validate loginID if the LoginIDInterceptor is present
     *
     * @param loginID String loginID to be validated
     * @throws InterceptorException if loginID is not valid for some reason
     */
    public void validateLoginID(String loginID) throws InterceptorException {
        if (loginIDInterceptor != null) {
            loginIDInterceptor.validateLoginID(loginID);
        }
    }

    public LoginIDInterceptor getLoginIDInterceptor() {
        return loginIDInterceptor;
    }

    public void setLoginIDInterceptor(LoginIDInterceptor interceptor) {
        log.info("Use LoginIDInterceptor = " + interceptor);
        this.loginIDInterceptor = interceptor;
    }

    /**
     * Validate password if the PasswordInterceptor is present
     *
     * @param password String password to be validated
     * @throws InterceptorException if password is not valid for some reason
     */
    public void validatePassword(String password) throws InterceptorException {
        if (passwordInterceptor != null) {
            passwordInterceptor.validatePassword(password);
        }
    }

    public PasswordInterceptor getPasswordInterceptor() {
        return passwordInterceptor;
    }

    public void setPasswordInterceptor(PasswordInterceptor interceptor) {
        log.info("Use PasswordInterceptor = " + interceptor);
        this.passwordInterceptor = interceptor;
    }

}
