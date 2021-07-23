/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/Portal.java,v 1.10 2009/01/03 18:32:35 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/01/03 18:32:35 $
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
 * @author: Phong Ta
 */
package com.mvnforum;

import java.util.Enumeration;
import java.util.Hashtable;

public class Portal {

    public static final String DEFAULT      = "non-portal";
    
    public static final String UPORTAL      = "uportal";
    public static final String JETSPEED2    = "jetspeed2";
    public static final String LIFERAY      = "liferay";
    public static final String JBOSS        = "jboss";
    public static final String EXOPLATFORM  = "exoplatform";

    public static final String WEBLOGIC     = "weblogic";
    public static final String WEBSPHERE    = "websphere";

    private static Hashtable portals;
    
    private Portal() {
        // to prevent creating an instance
    }

    static {
        // TODO these below value are no longer correct
        portals = new Hashtable();
        portals.put(DEFAULT,        "com.mvnforum.db.portal.MemberDAOImplBaseOnPortal");
        
        portals.put(UPORTAL,        "com.mvnforum.db.portal.UPortalMemberService");
        portals.put(JETSPEED2,      "com.mvnforum.db.portal.Jetspeed2MemberService");
        portals.put(LIFERAY,        "com.mvnforum.db.portal.LiferayMemberService");
        portals.put(JBOSS,          "com.mvnforum.db.portal.JBossMemberService");
        portals.put(EXOPLATFORM,    "com.mvnforum.db.portal.ExoPlatformMemberService");
    }

    public static Enumeration getSupportedPortals() {
        return portals.keys();
    }

    public static boolean isSupportedPortal(String portalName) {
        return portals.contains(portalName.toLowerCase());
    }

    public static String getMemberImplementation(String portalName) {
        String impl = (String)portals.get(portalName.toLowerCase());
        if (impl == null) {
            impl = DEFAULT;
        }
        return impl;
    }
}
