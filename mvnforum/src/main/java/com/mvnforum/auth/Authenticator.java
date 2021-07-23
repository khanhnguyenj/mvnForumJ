/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/Authenticator.java,v 1.10 2007/10/09 11:09:11 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.10 $
 * $Date: 2007/10/09 11:09:11 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.auth;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.web.GenericRequest;

/**
 * #@todo Expand guide about the overall authentication process
 * If the user is authenticated, then the memberID equals 0
 * See ForumUserServlet#process : uncomment the comment code for the hint
 */
public interface Authenticator {

    /**
     * This method will be called by mvnForum's authentication system
     *   to authenticate the customized user
     * With this generic interface, developer could integrate their own
     *   authentication to the mvnForum system
     * NOTE: The user should already be in the Member table of mvnForum database
     *       Developer could use this implementation strategy if the user's
     *       information is in other place (such as LDAP):
     *       - Check if user is login
     *       - If no, then return null
     *       - If yes, check if username is good name with StringUtil.checkGoodName
     *          - If not, then return null
     *          - If yes, check if user is in the mvnForum's mvnforumMember
     *             - If yes, return the username
     *             - If no, create the user
     * If you are successful, shareing your knowledge and experience to other
     * members of mvnForum community is highly appreciated
     *
     * @see OnlineUserManager#setAuthenticator(Authenticator)
     * @param request the servlet request
     * @return the username of the current user associated with this request
     *         If no user has been authenticated in this request, then null should be return
     */
    public String getRemoteUser(HttpServletRequest request);

    public String getRemoteUser(GenericRequest request);

    public boolean isCorrectCurrentPassword(String memberName, String password, boolean encoded);

}
