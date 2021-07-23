/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/sample/NoPublicMailInterceptor.java,v 1.6 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/01/01 18:30:12 $
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
package net.myvietnam.mvncore.interceptor.sample;

import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.interceptor.MailInterceptor;

/**
 * This sample MailInterceptor show how to check that user cannot
 * use public email such as @hotmail.com to use the system. This class
 * show simple method for checking but for real case you should build
 * a list of not-allowed domain and use other data struture for efficiency
 * You can based on this sample to write your own MailInterceptor.
 */
public class NoPublicMailInterceptor implements MailInterceptor {

    public NoPublicMailInterceptor() {
    }

    /**
     * In this sample interceptor, it check that emails in public
     * domain will not be accepted
     *
     * @param email String email to be validated
     * @throws InterceptorException
     */
    public void validateEmail(String email) throws InterceptorException {
        email = email.toLowerCase();
        if (email.endsWith("@yahoo.com") ||
            email.endsWith("@hotmail.com") ||
            email.endsWith("@aol.com") ) {
            throw new InterceptorException("Cannot accept email that is in public domain. Your email = " + email);
        }
    }
}
