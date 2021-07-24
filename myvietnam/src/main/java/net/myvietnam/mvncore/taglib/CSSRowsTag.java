/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/CSSRowsTag.java,v 1.6 2008/03/21 04:03:21 phuongpdd Exp $
 * $Author: phuongpdd $
 * $Revision: 1.6 $
 * $Date: 2008/03/21 04:03:21 $
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

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

public class CSSRowsTag extends TagSupport {

    private PageContext pageContext;

    private int count = 0;

    public CSSRowsTag() {
        super();
    }
    
    public void increaseCount() {
        count ++;
    }
    
    public int getCount() {
        return count;
    }

    public void resetCount() {
        this.count = 1;
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
        count = 1;
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }

}
