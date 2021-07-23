/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/SecurityTokenTag.java,v 1.2 2009/02/16 09:45:36 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2009/02/16 09:45:36 $
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
 * @author: Nguyen Dang Ngoc Chan
 */
package net.myvietnam.mvncore.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.security.SecurityUtil;

public class SecurityTokenTag implements Tag {
    
    private PageContext pageContext;

    private Tag parent;

    /**
     * Constructor
     */
    public SecurityTokenTag() {
        super();
    }

    /**
     * Method called at start of Tag
     * @return either a EVAL_BODY_INCLUDE or a SKIP_BODY
     */
    public int doStartTag() throws JspTagException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
            pageContext.getOut().write("<input type=\"hidden\" name=\"" + MVNCoreGlobal.MVNCORE_SECURITY_TOKEN + "\" value=\"" + SecurityUtil.getSessionToken(httpRequest) + "\" />");
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * Method Called at end of Tag
     * @return either EVAL_PAGE or SKIP_PAGE
     */
    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }

    /**
     * Method called to releases all resources
     */
    public void release() {
    }

    /** 
     * Method used by the JSP container to set the current PageContext
     * @param pageContext, the current PageContext
     */
    public void setPageContext(final PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /** 
     * Method used by the JSP container to set the parent of the Tag
     * @param parent, the parent Tag
     */
    public void setParent(final Tag parent) {
        this.parent = parent;
    }

    /** 
     * Method for retrieving the parent
     * @return the parent
     */
    public Tag getParent() {
        return parent;
    }
}
