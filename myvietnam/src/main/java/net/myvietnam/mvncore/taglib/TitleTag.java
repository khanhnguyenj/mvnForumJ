/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/TitleTag.java,v 1.5 2007/01/15 10:31:21 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:31:21 $
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
 * @author: Xuan Tran Thanh 
 */
package net.myvietnam.mvncore.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class TitleTag implements Tag {
    
    private PageContext pageContext;

    private Tag parent;

    private boolean isPortletRequest = false;

    /**
     * Constructor
     */
    public TitleTag() {
        super();
    }

    /**
     * Method called at start of Tag
     * @return either a EVAL_BODY_INCLUDE or a SKIP_BODY
     */
    public int doStartTag() throws JspTagException {
        try {
            if (isPortletRequest == false) {
                pageContext.getOut().write("<title>");
            }
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        if (isPortletRequest == false) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * Method Called at end of Tag
     * @return either EVAL_PAGE or SKIP_PAGE
     */
    public int doEndTag() throws JspTagException {
        try {
            if (isPortletRequest == false) {
                pageContext.getOut().write("</title>");
            }
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
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
        isPortletRequest = (pageContext.getRequest().getAttribute("javax.portlet.request") != null);
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
