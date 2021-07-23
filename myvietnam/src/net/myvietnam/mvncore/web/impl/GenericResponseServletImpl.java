/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/web/impl/GenericResponseServletImpl.java,v 1.6 2007/01/15 10:31:23 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.6 $
 * $Date: 2007/01/15 10:31:23 $
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
package net.myvietnam.mvncore.web.impl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.myvietnam.mvncore.web.GenericResponse;

public class GenericResponseServletImpl extends HttpServletResponseWrapper implements GenericResponse {

    public GenericResponseServletImpl(HttpServletResponse response) {
        super(response);
    }

    public HttpServletResponse getServletResponse() {
        return (HttpServletResponse)getResponse();
    }

    public Object getPortletResponse() {
        return null;
    }

    public boolean isServletResponse() {
        return true;
    }

    public boolean isPortletResponse() {
        return false;
    }
}
