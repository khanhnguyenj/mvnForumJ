/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/HtmlTag.java,v 1.8 2009/04/22 10:48:26 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2009/04/22 10:48:26 $
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

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import net.myvietnam.mvncore.util.I18nUtil;

public class HtmlTag extends TagSupport {
    
    private PageContext pageContext;

    private String locale = "";

    private boolean isPortletRequest = false;
    
    private String isHTML = "";
    
    /**
     * Constructor
     */
    public HtmlTag() {
        super();
    }

    /**
     * Method called at start of Tag
     * @return either a EVAL_BODY_INCLUDE or a SKIP_BODY
     */
    public int doStartTag() throws JspTagException {
        try {
            if (isPortletRequest == false) {
                ServletRequest request = pageContext.getRequest();
                Locale localeObject = I18nUtil.getLocaleInRequest(request);
                if (localeObject != null) {
                    locale = localeObject.getLanguage();
                }
                //System.out.println("HtmlTag.doStartTag() locale = " + locale + " html = " + isHTML);
                pageContext.getOut().write("<html");
                if (locale.equals("ar")) {
                    pageContext.getOut().write(" dir=\"rtl\"");
                }
                if ("true".equals(isHTML) == false) {
                    pageContext.getOut().write(" xmlns=\"http://www.w3.org/1999/xhtml\"");
                }
                pageContext.getOut().write(">");
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
                pageContext.getOut().write("</html>");
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
     * Method used by the JSP container to set the name of locale
     * @param locale, the locale name
     */
    public void setLocale(String locale) {
        if (locale != null) {
            this.locale = locale;
        }
    }
    
    public void setIsHTML(String isHTML) {
        this.isHTML = isHTML;
    }
 
}
