/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/BinaryStorageServiceImplEmpty.java,v 1.10 2010/06/17 10:40:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2010/06/17 10:40:37 $
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
package net.myvietnam.mvncore.service.impl;

import java.io.IOException;
import java.io.InputStream;

import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.AssertionUtil;

import org.apache.commons.lang.NotImplementedException;

public class BinaryStorageServiceImplEmpty extends BinaryStorageServiceImplBase implements BinaryStorageService {
    
    private static int count;
    
    public BinaryStorageServiceImplEmpty() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   

    public BinaryStorageHandle storeData(String category,
                                         String nameId,
                                         String fileName,
                                         InputStream inputStream,
                                         int storageFileSize,
                                         int storageOption,
                                         int storageStatus,
                                         String contentType,
                                         String storageIP) throws IOException {
        
        throw new NotImplementedException("Not implemented yet.");

    }

    public InputStream getInputStream(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {
        
        throw new NotImplementedException("Not implemented yet.");
    }
    
    public BinaryStorageInfo getBinaryStorageInfo(String category, String nameId, BinaryStorageHandle handle)
        throws IOException  {
        
        throw new NotImplementedException("Not implemented yet.");
    }
    
    public void deleteData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {
     
        throw new NotImplementedException("Not implemented yet.");

    }

}
