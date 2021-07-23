/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/MethodTag.java,v 1.3 2007/07/08 03:54:19 hau_mvn Exp $
 * $Author: hau_mvn $
 * $Revision: 1.3 $
 * $Date: 2007/07/08 03:54:19 $
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

import java.io.IOException;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

public class MethodTag extends TagSupport {

    private PageContext pageContext;
    private EnvironmentService environmentService;
    private String needPostMethod = "false";
    
    public MethodTag() {
        super();
        environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
    }
    
    public void setNeedPostMethod(String needPostMethod) {
        this.needPostMethod = needPostMethod;
    }
    
    public String getNeedPostMethod() {
        return needPostMethod;
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
        JspWriter writer = getPageContext().getOut();
        
        try {
            if (("true".equalsIgnoreCase(needPostMethod)) || (environmentService.isPortlet())) {
                writer.write(" method=\"post\" ");
            } else {
                writer.write(" method=\"get\" ");
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }

        return SKIP_BODY;
    }
    
    public int doEndTag() throws JspTagException {
        return SKIP_BODY;
    }

}
