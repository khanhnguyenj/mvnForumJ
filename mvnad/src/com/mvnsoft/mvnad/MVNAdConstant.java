/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/MVNAdConstant.java,v 1.2 2008/06/03 08:20:47 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2008/06/03 08:20:47 $
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
package com.mvnsoft.mvnad;

public final class MVNAdConstant {

    /** Cannot instantiate. */
    private MVNAdConstant() {
    }

/*************************************************************************
 * NOTE: below constants MUST NOT be changed IN ALL CASES,
 *       or it will break the compatibility
 *************************************************************************/

    public static final String EVENT_LOG_MAIN_MODULE        = "mvnAd";
    
    public static final String EVENT_LOG_SUB_MODULE_USER    = "User";
    
    public static final String EVENT_LOG_SUB_MODULE_ADMIN   = "Admin";
    
    public static final String N_A = "N/A";
}
