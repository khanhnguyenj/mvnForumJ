/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/sample/CombinedMailInterceptor.java,v 1.6 2009/01/01 18:30:12 minhnn Exp $
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
 * This sample show how to write new MailInterceptor that combine
 * other mail interceptor. You could even add your own checking.
 */
public class CombinedMailInterceptor implements MailInterceptor {

    NoPublicMailInterceptor noPublicMailInterceptor = new NoPublicMailInterceptor();

    MyCompanyMailInterceptor myCompanyMailInterceptor = new MyCompanyMailInterceptor();

    public CombinedMailInterceptor() {
    }

    /**
     * In this sample interceptor, it based on other MailInterceptor
     * and even add checking at the end of this method.
     *
     * @param email String email to be validated
     * @throws InterceptorException
     */
    public void validateEmail(String email) throws InterceptorException {
        noPublicMailInterceptor.validateEmail(email);
        myCompanyMailInterceptor.validateEmail(email);

        // now you can even add some more checking here :-)
    }
}
