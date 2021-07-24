/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/BinaryStorageServiceImplBase.java,v 1.8 2010/06/17 10:40:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
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

import java.io.*;
import java.sql.Timestamp;

import net.myvietnam.mvncore.service.*;
import net.myvietnam.mvncore.util.FileUtil;

public abstract class BinaryStorageServiceImplBase implements BinaryStorageService {

    public BinaryStorageHandle storeData(String category, String nameId, String fileName, byte[] data,
                                         int storageFileSize, int storageOption, int storageStatus, String contentType, String storageIP)
        throws IOException {

        return storeData(category, nameId, fileName, new ByteArrayInputStream(data, 0, storageFileSize),
                         storageFileSize, storageOption, storageStatus, contentType, storageIP);
    }

    public byte[] loadData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {

        InputStream inputStream = getInputStream(category, nameId, handle);
        return FileUtil.getBytes(inputStream);
    }

    public void updateData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException {

        throw new RuntimeException("Not emplemented yet.");
    }
    
    protected BinaryStorageInfo convertFileToBinaryStorageInfo(File file, String category)
        throws IOException {
        
        if (file.exists() == false) {
            throw new FileNotFoundException("File not existed: " + file.getName());
        }
        if (file.isFile() == false) {
            throw new FileNotFoundException("File expected, but found folder: " + file.getName());
        }
        Timestamp date = new Timestamp(file.lastModified());
        
        BinaryStorageInfo binaryStorageInfo = new BinaryStorageInfo();
        binaryStorageInfo.setStorageFileName(file.getName());
        binaryStorageInfo.setStorageCategory(category);
        binaryStorageInfo.setStorageFileSize((int)file.length());
        binaryStorageInfo.setStorageCreationDate(date);
        binaryStorageInfo.setStorageModifiedDate(date);
        
        return binaryStorageInfo;
    }
}
