/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/BinaryStorageHandle.java,v 1.9 2007/09/22 03:28:00 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2007/09/22 03:28:00 $
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

public class BinaryStorageHandle {

    private int type;
    private int primaryKey;
    private String fileName;

    public BinaryStorageHandle(int type, int primaryKey, String fileName) {
        this.type = type;
        this.primaryKey = primaryKey;
        this.fileName = fileName ;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public int getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
