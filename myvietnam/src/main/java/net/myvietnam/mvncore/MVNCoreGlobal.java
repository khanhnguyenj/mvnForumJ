/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/MVNCoreGlobal.java,v 1.14 2009/03/20 03:12:19 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/03/20 03:12:19 $
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
package net.myvietnam.mvncore;

public final class MVNCoreGlobal {

    private MVNCoreGlobal() {
    }

/*************************************************************************
 * NOTE: below constants can be changed for each build,
 *       these constant MUST NOT break the compatibility
 *************************************************************************/
    public static final String RESOURCE_BUNDLE_NAME   = "i18n/mvncore_java_i18n";

    public static final String MVNCORE_REQUEST_LOCALE = "mvncore.request.locale";
    
    public static final String MVNCORE_SESSION_TOKEN  = "mvncore.session.token";
    
    public static final String MVNCORE_SECURITY_TOKEN = "mvncoreSecurityToken";
    
    public static final String CLIENT_IP              = "IP_OF_CLIENT";
    public static final String SERVER_IP              = "IP_OF_SERVER";
    public static final String USER_AGENT             = "USER_AGENT";
    public static final String REFERER                = "REFERER";

    public static final String UN_KNOWN_IP            = "UN.KNOWN.IP";
    public static final String UN_KNOWN_USER_AGENT    = "UN.KNOWN.USER_AGENT";
    public static final String UN_KNOWN_REFERER       = "UN.KNOWN.REFERER";

}
