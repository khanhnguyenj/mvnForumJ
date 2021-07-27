/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/SetParentRule.java,v 1.8 2009/01/02 18:33:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2009/01/02 18:33:42 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Igor Manic   
 */
package com.mvnforum.admin.importexport;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * @author Igor Manic
 * @version $Revision: 1.8 $, $Date: 2009/01/02 18:33:42 $
 * <br/>
 * <code>SetParentRule</code> implements a digester rule that calls a "set parent"
 * method on the top (child) object, passing the (top-1) (parent) object as an argument.
 * This is same as in <code>SetTopRule</code>, except that this rule is calling
 * the desired method at the XML element begin (not at the end).
 */
public class SetParentRule extends Rule {

    protected String methodName = null;
    protected String paramType = null;
    protected boolean useExactMatch = false;

    public SetParentRule(Digester digester, String methodName) {
        this(methodName);
    }

    public SetParentRule(Digester digester, String methodName, String paramType) {
        this(methodName, paramType);
    }

    public SetParentRule(String methodName) {
        this(methodName, null);
    }

    public SetParentRule(String methodName, String paramType) {
        this.methodName = methodName;
        this.paramType = paramType;
    }

    public boolean isExactMatch() {
        return useExactMatch;
    }

    public void setExactMatch(boolean useExactMatch) {
        this.useExactMatch = useExactMatch;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("SetParentRule[");
        sb.append("methodName=");
        sb.append(methodName);
        sb.append(", paramType=");
        sb.append(paramType);
        sb.append("]");
        return (sb.toString());
    }

    public void begin(String namespace, String name, Attributes attributes)
    throws Exception {
        begin(attributes);
    }

    public void begin(Attributes attributes) throws Exception {
        // Identify the objects to be used
        Object child = digester.peek(0);
        Object parent = digester.peek(1);

        /*
        if (digester.log.isDebugEnabled()) {
            if (child == null) {
                digester.log.debug("[SetParentRule]{" + digester.match +
                        "} Call [NULL CHILD]." +
                        methodName + "(" + parent + ")");
            } else {
                digester.log.debug("[SetParentRule]{" + digester.match +
                        "} Call " + child.getClass().getName() + "." +
                        methodName + "(" + parent + ")");
            }
        }*/

        // Call the specified method
        Class[] paramTypes = new Class[1];
        if (paramType != null) {
            paramTypes[0] =
                    digester.getClassLoader().loadClass(paramType);
        } else {
            paramTypes[0] = parent.getClass();
        }

        if (useExactMatch) {
            MethodUtils.invokeExactMethod(child, methodName,
                new Object[]{ parent }, paramTypes);
        } else {
            MethodUtils.invokeMethod(child, methodName,
                new Object[]{ parent }, paramTypes);
        }
    }


}

