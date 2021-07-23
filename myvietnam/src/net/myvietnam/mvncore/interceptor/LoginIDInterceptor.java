/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/LoginIDInterceptor.java,v 1.6 2009/10/12 04:04:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/10/12 04:04:31 $
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

import net.myvietnam.mvncore.exception.InterceptorException;

/**
 * This interface is used to intercept the loginID of users. This
 * could be used to prevent bad loginID or for reserving loginID
 */
public interface LoginIDInterceptor {
    
    /**
     * Intercept a Login ID before this LoginID can be processed further
     *
     * @param loginID String the LoginID to be validated
     * @throws InterceptorException
     */
    public void validateLoginID(String loginID) throws InterceptorException;
}
