/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/MvnCoreServiceImplMvnForum.java,v 1.10 2008/07/26 01:33:49 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2008/07/26 01:33:49 $
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
package com.mvnforum.service.impl;

import net.myvietnam.mvncore.service.BinaryStorageService;
import net.myvietnam.mvncore.service.MvnCoreService;
import net.myvietnam.mvncore.service.URLResolverService;
import net.myvietnam.mvncore.service.impl.MvnCoreServiceImplDefault;
import net.myvietnam.mvncore.util.AssertionUtil;

public class MvnCoreServiceImplMvnForum extends MvnCoreServiceImplDefault implements MvnCoreService {

    private static int count;

    public MvnCoreServiceImplMvnForum() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public synchronized URLResolverService getURLResolverService() {
        if (urlResolverService == null) {
            urlResolverService = new URLResolverServiceImplServletMvnForum();
        }
        return urlResolverService;
    }

    public synchronized BinaryStorageService getBinaryStorageService() {
        if (binaryStorageService == null) {
            binaryStorageService = new BinaryStorageServiceImplDisk();
        }
        return binaryStorageService;
    }

}
