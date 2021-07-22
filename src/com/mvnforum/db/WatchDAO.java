/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/WatchDAO.java,v 1.17 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.17 $
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

public interface WatchDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Watch";

    public void findByPrimaryKey(int watchID)
        throws ObjectNotFoundException, DatabaseException;

    public void findByAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException;

    public void create(int memberID, int categoryID, int forumID,
                       int threadID, int watchType, int watchOption,
                       int watchStatus, Timestamp watchCreationDate, Timestamp watchLastSentDate,
                       Timestamp watchEndDate)
        throws IllegalArgumentException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void delete(int watchID)
        throws DatabaseException, ObjectNotFoundException;

    public void delete_inMember(int memberID)
        throws DatabaseException;

    public void delete_inCategory(int categoryID)
        throws DatabaseException;

    public void delete_inForum(int forumID)
        throws DatabaseException;

    public void delete_inThread(int threadID)
        throws DatabaseException;

    public void updateLastSentDate(int watchID, // primary key
                                   Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException;

    public WatchBean getWatch(int watchID)
        throws ObjectNotFoundException, DatabaseException;

    // this method is not used anywhere, should we remove it ???
    public WatchBean getWatch_byAlternateKey_MemberID_CategoryID_ForumID_ThreadID(int memberID, int categoryID, int forumID, int threadID)
        throws ObjectNotFoundException, DatabaseException;

    // this method is not used anywhere, should we remove it ???
    public Collection getWatches()
        throws DatabaseException;

    // this method is not used anywhere, should we remove it ???
    public int getNumberOfWatches()
        throws DatabaseException;

    public int getNumberOfWatches_forMember(int memberID)
        throws DatabaseException;

    // must only affect enable threads ???
    public Collection getMemberBeans()
        throws DatabaseException;

    // must only affect enable threads ???
    public Collection getWatches_forMember(int memberID)
        throws DatabaseException;

    // must only affect enable threads ???
    public void updateLastSentDate_forMember(int memberID, Timestamp watchLastSentDate)
        throws ObjectNotFoundException, DatabaseException;

    public void updateWatchType (int watchID, int watchType)
        throws ObjectNotFoundException, DatabaseException;
}
