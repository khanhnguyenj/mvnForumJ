/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/sample/NoBadLoginIDInterceptor.java,v 1.10 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
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
import net.myvietnam.mvncore.interceptor.LoginIDInterceptor;
import net.myvietnam.mvncore.util.AssertionUtil;

/**
 * This sample LoginIDInterceptor show how to check that user cannot
 * use bad login id such as sex (vulgar) or administrator (to confuse users)
 * to use the system. This class
 * show simple method for checking but for real case you should build
 * a list of not-allowed loginID and use other data struture for efficiency
 * You can based on this sample to write your own LoginIDInterceptor.
 */
public class NoBadLoginIDInterceptor implements LoginIDInterceptor {

    private static int count;
    
    public NoBadLoginIDInterceptor() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   

    /**
     * In this sample interceptor, it check that loginID is good or bad
     *
     * @param loginID String loginID to be validated
     * @throws InterceptorException
     */
    public void validateLoginID(String loginID) throws InterceptorException {
        loginID = loginID.toLowerCase();
        if (loginID.equals("sex") ||
            loginID.equals("moderator") ||
            loginID.equals("administrator") ) {
            throw new InterceptorException("Cannot accept this loginID. Your loginID = " + loginID);
        }
    }
}
