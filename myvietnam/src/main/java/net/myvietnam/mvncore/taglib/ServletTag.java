/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/ServletTag.java,v 1.2 2007/05/04 03:33:28 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2007/05/04 03:33:28 $
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
 * @author: Phuong, Pham Dinh Duy  
 */
package net.myvietnam.mvncore.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

public class ServletTag extends TagSupport {
    
    private PageContext pageContext;
    private EnvironmentService environmentService;
    
    public ServletTag() {
        super();
        environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
    }
    
    public PageContext getPageContext() {
        return this.pageContext;
    }
    
    public void setPageContext(final PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Method called at start of Tag
     * @return either a EVAL_BODY_INCLUDE or a SKIP_BODY
     */
    public int doStartTag() throws JspTagException {
        HttpServletRequest request = (HttpServletRequest) getPageContext().getRequest(); 
        boolean isServlet = ((environmentService.isPortlet() == false) || (request.getAttribute("javax.portlet.request") == null));
        if (isServlet) {
            return EVAL_BODY_INCLUDE;
        } else {
            //portlet
            return SKIP_BODY;
        }
    }
    
    public int doEndTag() throws JspTagException {
        return SKIP_BODY;
    }

}
