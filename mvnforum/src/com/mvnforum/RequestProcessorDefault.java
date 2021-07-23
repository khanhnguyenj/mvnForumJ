/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/RequestProcessorDefault.java,v 1.10 2008/12/31 03:50:25 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.10 $
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
 * @author: Luis Miguel Hernanz
 * @author: Minh Nguyen
 */
package com.mvnforum;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Empty implementation of the RequestProcessor interface.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision: 1.10 $
 */
public class RequestProcessorDefault implements RequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(RequestProcessorDefault.class);

    /**
     * This is the first method called in the request processing. This
     * method returns always <code>null</code>.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     */
    public String preLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("Error setting the character encoding from the request", e);
        }
        return null;
    }

    /**
     * The method receives the user request just before the
     * authentication has been checked. This method always returns
     * <code>null</code>.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     */
    public String preProcess(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * This method is called just before the call to the final request
     * dispatcher forward. This method always returns
     * <code>responseURI</code>.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @param responseURI the path to which the control will be
     * forwarded after this method has finished.
     */
    public String postProcess(HttpServletRequest request, HttpServletResponse response, String responseURI) {
        return responseURI;
    }
}
