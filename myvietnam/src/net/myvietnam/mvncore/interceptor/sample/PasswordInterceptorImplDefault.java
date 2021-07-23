/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/sample/PasswordInterceptorImplDefault.java,v 1.3 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
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
 * @author: Phuong, Pham Dinh Duy 
 */
package net.myvietnam.mvncore.interceptor.sample;

import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.interceptor.PasswordInterceptor;
import net.myvietnam.mvncore.util.AssertionUtil;

/**
 * This sample show how to write new PasswordInterceptor that validate 
 * length, lower case, upper case and digit of the password
 */
public abstract class PasswordInterceptorImplDefault implements PasswordInterceptor {

    private int minLength;
    private int minLower;
    private int minUpper;
    private int minDigit;
    
    private static int count;

    public PasswordInterceptorImplDefault(int minLength, int minLower, int minUpper, int minDigit) {
        
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");

        this.minLength = minLength;
        this.minLower = minLower;
        this.minUpper = minUpper;
        this.minDigit = minDigit;
    }

    public void validatePassword(String password) throws InterceptorException {

        if ( (password == null) || (password.length() < minLength) ) {
            
            throw new InterceptorException("Cannot accept your password. Your password must have at least " + minLength + " character(s).");
        } 
            
        int numberOfDigit     = 0;
        int numberOfLowerCase = 0;
        int numberOfUpperCase = 0;
        
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                numberOfDigit++;
            } else if (Character.isLowerCase(ch)) {
                numberOfLowerCase++;
            } else if (Character.isUpperCase(ch)) {
                numberOfUpperCase++;
            }
        }
        
        if (numberOfDigit < minDigit) {
            throw new InterceptorException("Cannot accept your password. Your password must have at least " + minDigit + " digit(s).");
        }
        
        if (numberOfLowerCase < minLower) {
            throw new InterceptorException("Cannot accept your password. Your password must have at least " + minLower + " lower character(s).");
        }
        
        if (numberOfUpperCase < minUpper) {
            throw new InterceptorException("Cannot accept your password. Your password must have at least " + minUpper + " upper character(s).");
        }
    }

}
