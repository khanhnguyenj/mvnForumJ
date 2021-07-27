/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/BinaryStorageService.java,v 1.22 2010/06/17 10:40:37 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.22 $
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
 * @author: Trong Vo
 */
package net.myvietnam.mvncore.service;

import java.io.IOException;
import java.io.InputStream;

public interface BinaryStorageService {

    public static final int BINARY_STORAGE_TYPE_DISK        = 1;

    public static final int BINARY_STORAGE_TYPE_DATABASE    = 2;

    public static final String BINARY_STORAGE               = "BinaryStorage";
    
    public static final String CATEGORY_POST_ATTACHMENT     = "PostAttachment";

    public static final String CATEGORY_PM_ATTACHMENT       = "PmAttachment";

    public static final String CATEGORY_AVATAR              = "AvatarAttachment";
    
    public static final String CATEGORY_FEEDBACK_ATTACHMENT = "FeedbackAttachment";
    
    public static final String CATEGORY_ALBUM_ITEM          = "AlbumItem";
    
    public static final String CATEGORY_ALBUM_AVATAR        = "AlbumAvatar";
    
    public BinaryStorageHandle storeData(String category,
                                         String nameId,
                                         String fileName,
                                         byte[] data,
                                         int storageFileSize,
                                         int storageOption,
                                         int storageStatus,
                                         String contentType,
                                         String storageIP) throws IOException;

    public BinaryStorageHandle storeData(String category,
                                         String nameId,
                                         String fileName,
                                         InputStream inputStream,
                                         int storageFileSize,
                                         int storageOption,
                                         int storageStatus,
                                         String contentType,
                                         String storageIP) throws IOException;

    public byte[] loadData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException;
    
    public InputStream getInputStream(String category, String nameId, BinaryStorageHandle handle)
        throws IOException;

    public BinaryStorageInfo getBinaryStorageInfo(String category, String nameId, BinaryStorageHandle handle)
        throws IOException;
    
    public void deleteData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException;

    public void updateData(String category, String nameId, BinaryStorageHandle handle)
        throws IOException;

}
