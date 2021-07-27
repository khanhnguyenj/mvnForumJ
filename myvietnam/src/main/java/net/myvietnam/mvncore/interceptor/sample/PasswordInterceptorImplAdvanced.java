/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/sample/PasswordInterceptorImplAdvanced.java,v 1.1 2007/07/04 13:42:18 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2007/07/04 13:42:18 $
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

/**
 * This class check password with the min length is 6, 
 * and has at least min 1 lower case, 1 upper case and 1 digit 
 */
public class PasswordInterceptorImplAdvanced extends PasswordInterceptorImplDefault {
    
    public PasswordInterceptorImplAdvanced() {
        super(6, 1, 1, 1);
    }
}
