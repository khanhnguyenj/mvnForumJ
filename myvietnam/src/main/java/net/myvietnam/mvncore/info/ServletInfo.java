/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/info/ServletInfo.java,v 1.10 2007/01/15 10:31:14 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.10 $
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
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.info;

import javax.servlet.ServletContext;

public class ServletInfo {

    private String serverInfo   = null;
    private int majorVersion    = 0;
    private int minorVersion    = 0;
    private String servletVersion = null;

    public ServletInfo(ServletContext context) {
        serverInfo      = context.getServerInfo();
        majorVersion    = context.getMajorVersion();
        minorVersion    = context.getMinorVersion();
        servletVersion  = new StringBuffer().append(majorVersion).append('.').append(minorVersion).toString();
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public String getServletVersion() {
        return servletVersion;
    }
}
