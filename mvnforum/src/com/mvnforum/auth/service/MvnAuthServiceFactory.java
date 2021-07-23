/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/service/MvnAuthServiceFactory.java,v 1.7 2008/12/31 03:50:25 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.7 $
 * $Date: 2008/12/31 03:50:25 $
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
 * @author: Anh, Dong Thi Lan
 */
package com.mvnforum.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumFactoryConfig;
import com.mvnforum.auth.service.impl.MvnAuthServiceImplDefault;

public final class MvnAuthServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(MvnAuthServiceFactory.class);

    private MvnAuthServiceFactory() {
    }

    private static MvnAuthService mvnAuthService = null;

    public static synchronized MvnAuthService getMvnAuthService() {

        if (mvnAuthService == null) {
            try {
                Class c = Class.forName(MVNForumFactoryConfig.getMvnAuthServiceClassName());
                mvnAuthService = (MvnAuthService) c.newInstance();
                log.info("mvnAuthService = " + mvnAuthService);
            } catch (Exception e) {
                log.error("Error loading the mvnAuthService.", e);

                log.warn("Error loading the mvnAuthService. Using default mvnAuthService: " + MvnAuthServiceImplDefault.class.getName());
                mvnAuthService = new MvnAuthServiceImplDefault ();
            }
        }
        return mvnAuthService;
    }

}
