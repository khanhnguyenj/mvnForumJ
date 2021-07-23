/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/web/GenericResponse.java,v 1.10 2008/12/30 09:39:01 dinhanhvo Exp $
 * $Author: dinhanhvo $
 * $Revision: 1.10 $
 * $Date: 2008/12/30 09:39:01 $
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
 * @author: Phong Ta Quoc 
 */
package net.myvietnam.mvncore.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public interface GenericResponse {
   
    public HttpServletResponse getServletResponse();

    public Object getPortletResponse();

    public boolean isServletResponse();

    public boolean isPortletResponse();
    
    public void addCookie(Cookie cookie); 
}
