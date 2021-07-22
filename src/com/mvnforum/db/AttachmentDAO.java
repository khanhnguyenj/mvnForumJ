/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/AttachmentDAO.java,v 1.23 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
 * $Date: 2009/01/02 15:12:55 $
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
 * @author: Minh Nguyen
 */
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.*;

public interface AttachmentDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Attachment";

    public void create(int postID, int memberID, String attachFilename,
                        int attachFileSize, String attachMimeType, String attachDesc,
                        String attachCreationIP, Timestamp attachCreationDate, Timestamp attachModifiedDate,
                        int attachDownloadCount, int attachOption, int attachStatus)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException;

    public int createAttachment(int postID, int memberID, String attachFilename,
                         int attachFileSize, String attachMimeType, String attachDesc,
                         String attachCreationIP, Timestamp attachCreationDate, Timestamp attachModifiedDate,
                         int attachDownloadCount, int attachOption, int attachStatus)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException, ObjectNotFoundException;

    public void delete(int attachID)
        throws DatabaseException, ObjectNotFoundException;

    public AttachmentBean getAttachment(int attachID)
        throws ObjectNotFoundException, DatabaseException;

    // this method is not used now
    public Collection getAttachments()
        throws DatabaseException;

    public int getNumberOfAttachments(int category, int forum) throws DatabaseException;

    public int getNumberOfAttachments_inPost(int postID)
        throws DatabaseException;

    public int getNumberOfAttachments_inThread(int threadID)
        throws DatabaseException;

    public void delete_inPost(int postID)
        throws DatabaseException;

    public Collection getAttachments_inPost(int postID)
        throws DatabaseException;

    public Collection getAttachments_inThread(int threadID)
        throws DatabaseException;

    public Collection getAttachments_inForum(int forumID)
        throws DatabaseException;

    public void increaseDownloadCount(int attachID)
        throws DatabaseException, ObjectNotFoundException;

    public void updateAttachDesc(int attachID, String newDesc)
        throws DatabaseException, ObjectNotFoundException;

    public void updateAttachOption(int attachID, int attachOption)
        throws DatabaseException, ObjectNotFoundException;

    public Collection getAttachments_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, int category, int forum)
        throws IllegalArgumentException, DatabaseException;

    public int getMaxAttachmentID() throws DatabaseException;

    public Collection getAttachments_fromIDRange(int fromID, int toID)
        throws IllegalArgumentException, DatabaseException;
}
