/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/util/Utils.java,v 1.4 2009/12/02 10:48:38 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
 * $Date: 2009/12/02 10:48:38 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Xuan Tran Le
 */
package com.mvnforum.util;

import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.util.DateUtil;

import com.mvnforum.vbulletin.Migrator;
import com.mvnforum.vbulletin.db.DAOFactory;

/**
 * The Class Utils.
 */
public class Utils {
    
    private Utils() {
    }

    /**
     * Gets the time stamp.
     * 
     * @param data the data
     * 
     * @return the time stamp
     */
    public static Timestamp getTimeStamp(long data) {
        return new Timestamp((data*1000) + (DateUtil.HOUR * Migrator.VBULLETIN_HOUR_ADJUSTMENT));
    }

    /**
     * Gets the mime type from extension.
     * 
     * @param extension the extension
     * 
     * @return the mime type from extension
     * 
     * @throws ObjectNotFoundException the object not found exception
     * @throws DatabaseException the database exception
     */
    public static String getMimeTypeFromExtension(String extension) throws ObjectNotFoundException, DatabaseException {
        String mimeType = DAOFactory.getVBulletinAttachmentTypeDAO().getMimeType(extension);
        
        //longMimeType = a:1:{i:0;s:24:"Content-type: image/jpeg";}
        if (mimeType.indexOf("Content-type:") != -1) {
            mimeType = mimeType.substring(mimeType.indexOf("Content-type:") + "Content-type:".length());
            if (mimeType.indexOf("\"") != -1) {
                mimeType = mimeType.substring(0, mimeType.indexOf("\""));
            }
            mimeType = mimeType.trim();
        }
        return mimeType;
    }

}
