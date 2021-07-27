/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/BinaryStorageInfo.java,v 1.2 2008/12/30 03:51:49 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2008/12/30 03:51:49 $
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
 * @author: Nhan Luu Duy
 */
package net.myvietnam.mvncore.service;

import java.sql.Timestamp;

public class BinaryStorageInfo {
    
    private int storageID;
    private String storageCategory;
    private String storageFileName;
    private int storageFileSize;
    private String storageIP;
    private String storageMimeType;
    private int storageOption;
    private int storageStatus;
    private int storageType;
    private Timestamp storageCreationDate;
    private Timestamp storageModifiedDate;
    
    public int getStorageID() {
        return storageID;
    }
    public void setStorageID(int storageID) {
        this.storageID = storageID;
    }

    public String getStorageCategory() {
        return storageCategory;
    }
    public void setStorageCategory(String storageCategory) {
        this.storageCategory = storageCategory;
    }

    public String getStorageFileName() {
        return storageFileName;
    }
    public void setStorageFileName(String storageFileName) {
        this.storageFileName = storageFileName;
    }

    public int getStorageFileSize() {
        return storageFileSize;
    }
    public void setStorageFileSize(int storageFileSize) {
        this.storageFileSize = storageFileSize;
    }
    
    public String getStorageIP() {
        return storageIP;
    }
    public void setStorageIP(String storageIP) {
        this.storageIP = storageIP;
    }

    public String getStorageMimeType() {
        return storageMimeType;
    }
    public void setStorageMimeType(String storageMimeType) {
        this.storageMimeType = storageMimeType;
    }

    public int getStorageOption() {
        return storageOption;
    }
    public void setStorageOption(int storageOption) {
        this.storageOption = storageOption;
    }

    public int getStorageStatus() {
        return storageStatus;
    }
    public void setStorageStatus(int storageStatus) {
        this.storageStatus = storageStatus;
    }

    public int getStorageType() {
        return storageType;
    }
    public void setStorageType(int storageType) {
        this.storageType = storageType;
    }

    public Timestamp getStorageCreationDate() {
        return storageCreationDate;
    }
    public void setStorageCreationDate(Timestamp storageCreationDate) {
        this.storageCreationDate = storageCreationDate;
    }

    public Timestamp getStorageModifiedDate() {
        return storageModifiedDate;
    }
    public void setStorageModifiedDate(Timestamp storageModifiedDate) {
        this.storageModifiedDate = storageModifiedDate;
    }

} // end of class BinaryStorageInfo
