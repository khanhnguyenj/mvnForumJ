/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/CSSRowReset.java,v 1.2 2008/03/21 04:03:21 phuongpdd Exp $
 * $Author: phuongpdd $
 * $Revision: 1.2 $
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

import javax.servlet.jsp.JspTagException;

public class CSSRowReset extends CSSRowsTag {

    public CSSRowReset() {
        super();
    }
    
    public int doStartTag() throws JspTagException {
        CSSRowsTag parent = (CSSRowsTag)findAncestorWithClass(this, CSSRowsTag.class);
        
        if (parent == null) {
            throw new JspTagException("Child Tag without Parent Tag");
        }
        parent.resetCount();
        
        return SKIP_BODY;
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }
}