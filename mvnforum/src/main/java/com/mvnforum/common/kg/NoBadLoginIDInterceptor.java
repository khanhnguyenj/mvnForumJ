/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/kg/NoBadLoginIDInterceptor.java,v 1.10 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
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
 */
package com.mvnforum.common.kg;

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
            loginID.equals("administrator") ||
            loginID.startsWith(KGUtils.KG_PREFIX) ) {
            throw new InterceptorException("Cannot accept this loginID. Your loginID = " + loginID);
        }
    }
}
