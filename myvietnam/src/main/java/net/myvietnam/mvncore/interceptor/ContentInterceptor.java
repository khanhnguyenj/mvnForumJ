/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/interceptor/ContentInterceptor.java,v 1.7 2007/01/15 10:31:14 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.7 $
 * $Date: 2007/01/15 10:31:14 $
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

public interface ContentInterceptor {
    
    /**
     * Intercept a content before this content can be processed further
     *
     * @param content String content to be validated
     */
    public String validateContent(String content) throws InterceptorException;
}
