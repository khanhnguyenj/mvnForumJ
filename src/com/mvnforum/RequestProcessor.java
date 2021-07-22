/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/RequestProcessor.java,v 1.7 2007/10/09 11:09:22 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.7 $
 * $Date: 2007/10/09 11:09:22 $
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
 * @author: Luis Miguel Hernanz
 * @author: Minh Nguyen
 */
package com.mvnforum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface to hook additional preprocessing to the user request.
 * This interface is meatn as extension points to ease the integration
 * of the forum system with external systems.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision: 1.7 $
 */
public interface RequestProcessor {

    /**
     * This is the first method called in the request processing.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the new responseURI to redirect to. The control will be
     * forwarded to this page (if responseURI is an jsp). No furher
     * processing will take place.
     */
    public String preLogin(HttpServletRequest request, HttpServletResponse response);

    /**
     * The method receives teh user request just before the
     * authentication has been checked.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @return the new responseURI to redirect to.
     */
    public String preProcess(HttpServletRequest request, HttpServletResponse response);

    /**
     * This method is called just before the call to the final request
     * dispatcher forward.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @param responseURI the path to which the control will be
     * forwarded after this method has finished.
     * @return the new responseURI to redirect to.
     */
    public String postProcess(HttpServletRequest request, HttpServletResponse response, String responseURI);

}
