/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/DAOFactoryAd.java,v 1.3 2008/06/03 08:20:49 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.3 $
 * $Date: 2008/06/03 08:20:49 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
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
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.db;

import com.mvnsoft.mvnad.db.jdbc.*;

public final class DAOFactoryAd {

    private static BannerDAO                bannerDAO               = null;
    private static ZoneDAO                  zoneDAO                 = null;
    private static ZoneBannerDAO            zoneBannerDAO           = null;
    
    private DAOFactoryAd() {
    }

    public static BannerDAO getBannerDAO() {
        return bannerDAO;
    }

    public static ZoneDAO getZoneDAO() {
        return zoneDAO;
    }

    public static ZoneBannerDAO getZoneBannerDAO() {
        return zoneBannerDAO;
    }

    static {
        bannerDAO               = new BannerDAOImplJDBC();
        zoneDAO                 = new ZoneDAOImplJDBC();
        zoneBannerDAO           = new ZoneBannerDAOImplJDBC();
    }

}
