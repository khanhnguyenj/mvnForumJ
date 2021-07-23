/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageFolderDAO.java,v 1.19 2008/07/17 02:54:38 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.19 $
 * $Date: 2008/07/17 02:54:38 $
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

public interface MessageFolderDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "MessageFolder";

    public void findByPrimaryKey(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException;

    public void create(String folderName, int memberID, int folderOrder,
                       int folderStatus, int folderOption, int folderType,
                       Timestamp folderCreationDate, Timestamp folderModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void delete(String folderName, int memberID)
        throws DatabaseException, ObjectNotFoundException;

    public MessageFolderBean getMessageFolder(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getMessageFolders_inMember(int memberID)
        throws DatabaseException;

    public int getMaxFolderOrder(int memberID)
        throws DatabaseException;

    public void increaseFolderOrder(String folderName, int memberID, // primary key
                                    Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException;

    public void decreaseFolderOrder(String folderName, int memberID, // primary key
                                    Timestamp folderModifiedDate)
        throws ObjectNotFoundException, DatabaseException;
    
    public int getNumberOfMessageFolders_inMember(int memberID)
        throws DatabaseException;

}
