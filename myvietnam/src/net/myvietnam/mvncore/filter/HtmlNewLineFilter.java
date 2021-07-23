/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/HtmlNewLineFilter.java,v 1.14 2009/03/30 10:53:57 nhanld Exp $
 * $Author: nhanld $
 * $Revision: 1.14 $
 * $Date: 2009/03/30 10:53:57 $
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
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.filter;

import java.io.*;

public final class HtmlNewLineFilter {

    private HtmlNewLineFilter() { //prevent instantiation
    }

    public static String filter(String input) {
        if (input == null) {
            return null;
        }
        StringReader stringReader = new StringReader(input);
        BufferedReader reader = new BufferedReader(stringReader);

        StringBuffer ret = new StringBuffer(input.length() + 200);// add more room to the result String

        String line = null;
        boolean enableNewLine = true;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("<ul>") != -1) {
                    enableNewLine = false;
                } 
                if (line.indexOf("</ul>") != -1) {
                    enableNewLine = true;
                }
                if (enableNewLine) {
                    ret.append(line).append("<br />");
                } else {
                    ret.append(line);
                }
            }//while
        } catch (IOException ex) {}
        return ret.toString();
    }
}
