/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/VBulletin2MvnForum.java,v 1.5 2009/10/12 03:33:13 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.5 $
 * $Date: 2009/10/12 03:33:13 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.vbulletin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VBulletin2MvnForum.
 */
public class VBulletin2MvnForum {

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(VBulletin2MvnForum.class);
    
    /**
     * Instantiates a new vbulletin 2 mvnforum.
     * 
     * @throws Exception the exception
     */
    public VBulletin2MvnForum() throws Exception {
    }

    /**
     * Performs Conversion of vbulletin database to mvnforum database.
     */
    public void convert() {
        try {
            log.debug("Conversion started...");
            Migrator.migrateUsers();
            Migrator.migrateForums();
            Migrator.migratePrivateMessageFolders();
            Migrator.migratePrivateMessage();
            log.debug("Conversion complete...");
        } catch (Exception e) {
            log.error("Exception during conversion", e);
        }

    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

        try {
            VBulletin2MvnForum vbulletinmvnforum = new VBulletin2MvnForum(); 
            vbulletinmvnforum.convert();
        } catch (Exception e) {
            log.error("Exception during conversion", e);
        }
    }
}
