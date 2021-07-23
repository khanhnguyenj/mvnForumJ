/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/servlet/filter/Error404Filter.java,v 1.2 2009/04/20 08:30:35 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2009/04/20 08:30:35 $
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
 * @author: Nhan Luu Duy  
 */
package net.myvietnam.mvncore.servlet.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Error404Filter implements Filter {
    
    private static final Logger log = LoggerFactory.getLogger(Error404Filter.class);
    
    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) {
    
    }

    /**
     * Take this filter out of service.
     */
     public void destroy() {

     }
     
     public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
         throws IOException, ServletException {
         
         HttpServletRequest httpServletRequest = (HttpServletRequest) request;
         HttpServletResponse httpServletResponse = (HttpServletResponse) response;
         
         String requestURI = StringUtil.getEmptyStringIfNull(httpServletRequest.getRequestURI());
         String contextPath = httpServletRequest.getContextPath();
         
         if (requestURI.startsWith(contextPath + "/publishadmin/")) {
             RequestDispatcher requestDispatcher = request.getRequestDispatcher("/mvnplugin/mvnforum/404.jsp");
             requestDispatcher.forward(httpServletRequest, httpServletResponse);
         }
     }
}
