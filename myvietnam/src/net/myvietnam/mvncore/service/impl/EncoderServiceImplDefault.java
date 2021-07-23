/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/EncoderServiceImplDefault.java,v 1.8 2007/01/15 10:31:19 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.8 $
 * $Date: 2007/01/15 10:31:19 $
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
 * @author: Trong Vo
 */
package net.myvietnam.mvncore.service.impl;

import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.service.EncoderService;
import net.myvietnam.mvncore.util.AssertionUtil;

public class EncoderServiceImplDefault implements EncoderService {
    
    private static int count;
    
    public EncoderServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public String filterUrl(String url) {
        
        return Encoder.filterUrl(url);
        
    }

}
