/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/DefenseUtil.java,v 1.7 2009/08/11 09:41:20 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.7 $
 * $Date: 2009/08/11 09:41:20 $
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
 * @author: Xuan Tran Le  
 */
package net.myvietnam.mvncore.util;

import java.util.Locale;

import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;

public class DefenseUtil {
    
    /**
     * Private Constructor to prevent initialize many instances of DefenseUtil.
     */
    private DefenseUtil() {
    }

    public static void ensureUnsignedInt(Locale locale, int input) throws BadInputException {
        
        if (input < 0) {
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.must_be_unsigned_value", new Object[] {String.valueOf(input)});
            throw new BadInputException(localizedMessage);
        }
    }
    
    public static void ensureUnsignedIntExcludeZero(Locale locale, int input) throws BadInputException {
        
        if (input <= 0) {
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.must_be_unsigned_value_exclude_zero", new Object[] {String.valueOf(input)});
            throw new BadInputException(localizedMessage);
        }
    }
    
}
