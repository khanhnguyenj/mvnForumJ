/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/taglib/CSSRowTag.java,v 1.5 2007/01/15 10:31:21 dungbtm Exp $
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
 * @author: Phuong, Pham Dinh Duy
 */
package net.myvietnam.mvncore.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;

public class CSSRowTag extends CSSRowsTag {

    private String autoIncrease   = "";
    //see viewthread.jsp
    private boolean wantToReturn  = false;
    private String valueToReturn  = "";
    
    public void setAutoIncrease(String autoIncrease) {
        this.autoIncrease = autoIncrease;
    }
    
    public String getAutoIncrease() {
        return this.autoIncrease;
    }

    public void setWantToReturn(boolean wantToReturn) {
        this.wantToReturn = wantToReturn;
    }
    
    public boolean getWantToReturn() {
        return this.wantToReturn;
    }

    public void setValueToReturn(String valueToReturn) {
        this.valueToReturn = valueToReturn;
    }
    
    public String getValueToReturn() {
        return this.valueToReturn;
    }

    public CSSRowTag() {
        super();
    }
    
    public int doStartTag() throws JspTagException {
        
        CSSRowsTag parent = (CSSRowsTag)findAncestorWithClass(this, CSSRowsTag.class);
        
        if (parent == null) {
            throw new JspTagException("Child Tag without Parent Tag");
        }
        
        try {
            
            JspWriter writer = getPageContext().getOut();
            
            if (autoIncrease == null || autoIncrease.length() == 0 || autoIncrease.equalsIgnoreCase("true")) {
                parent.increaseCount();
            } else if (autoIncrease.equalsIgnoreCase("false") == false) {
                throw new JspTagException("This property is not supported.");
            }
            
            if (wantToReturn) {
                if ( (valueToReturn != null) && (valueToReturn.length() > 0) ) {
                    writer.write(valueToReturn);
                } else {
                    throw new JspTagException("This property is not supported.");
                }
            } else if ( (parent.getCount() % 2) == 0 ) {
                writer.write("portlet-section-body");
            } else {
                writer.write("portlet-section-alternate");
            }
        } catch (IOException e) {
            throw new JspTagException("IO Error: " + e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }


}
