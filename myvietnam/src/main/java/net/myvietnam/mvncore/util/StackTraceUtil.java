/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/StackTraceUtil.java,v 1.1 2009/07/30 03:04:32 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2009/07/30 03:04:32 $
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
 */
package net.myvietnam.mvncore.util;

public class StackTraceUtil {

    private StackTraceUtil() {
    }
    
    /**
     * Check if a class name is in the call hierarchy
     * 
     * @param clazz the class to check
     * @return true if if a class name is in the call hierarchy, otherwise return false
     */
    public static boolean isCalledFromClass(Class clazz) {
        String fullClassName = clazz.getName();
        return isCalledFromClass(fullClassName);
    }
    
    /**
     * Check if a class name is in the call hierarchy
     * 
     * @param fullClassName the full string of the class to check
     * @return true if if a class name is in the call hierarchy, otherwise return false
     */
    public static boolean isCalledFromClass(String fullClassName) {
        if ( (fullClassName == null) || (fullClassName.length() == 0) ) {
            return false;
        }
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement stackTraceElement = elements[i];
            if (stackTraceElement.getClassName().equals(fullClassName)) {
                return true;
            }
        }
        return false;
    }
    
}
