/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/BodyTag.java,v 1.8 2007/06/14 18:18:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2007/06/14 18:18:55 $
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

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

public class BodyTag extends TagSupport {
    
    private PageContext pageContext;

    private String onunload = "";
    
    private String onload = "";

    private boolean isPortletRequest = false;

    /**
     * Constructor
     */
    public BodyTag() {
        super();
    }

    /**
     * Method called at start of Tag
     * @return either a EVAL_BODY_INCLUDE or a SKIP_BODY
     */
    public int doStartTag() throws JspTagException {
        try {
            if (isPortletRequest == false) {
                JspWriter writer = pageContext.getOut();
                writer.write("<body");
                if (onload.length() > 0) {
                    writer.write(" onload=\"");
                    writer.write(onload);
                    writer.write("\"");
                }
                if (onunload.length() > 0) {
                    writer.write(" onunload=\"");
                    writer.write(onunload);
                    writer.write("\"");
                }
                writer.write(">");
            }
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Method Called at end of Tag
     * @return either EVAL_PAGE or SKIP_PAGE
     */
    public int doEndTag() throws JspTagException {
        try {
            if (isPortletRequest == false) {
                pageContext.getOut().write("</body>");
            }
        } catch (java.io.IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_PAGE;
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
     * Method used by the JSP container to set the name of onunload
     * @param onunload, the onunload name
     */
    public void setOnunload(String onunload) {
        if (onunload != null) {
            this.onunload = onunload;
        }
    }
    
    /** 
     * Method used by the JSP container to set the name of onload
     * @param onload, the onload name
     */
    public void setOnload(String onload) {
        if (onload != null) {
            this.onload = onload;
        }
    }
 
}
