/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/VotePeriodBuilderServiceImplDefault.java,v 1.1 2009/04/16 11:09:56 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.1 $
 * $Date: 2009/04/16 11:09:56 $
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
package com.mvnforum.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.service.VotePeriodBuilderService;

public class VotePeriodBuilderServiceImplDefault implements VotePeriodBuilderService {
    
    public String drawThreadVote(int threadID, HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {
        
        return "";
    }
    
    public String drawPostVote(int postID, HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException, AuthenticationException, ObjectNotFoundException {
        
        return "";
    }

}
