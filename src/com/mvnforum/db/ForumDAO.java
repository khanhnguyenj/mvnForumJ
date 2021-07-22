/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ForumDAO.java,v 1.20 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.20 $
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

public interface ForumDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Forum";

    public void findByPrimaryKey(int forumID)
        throws ObjectNotFoundException, DatabaseException;

    public void findByAlternateKey_ForumName_CategoryID(String forumName, int categoryID)
        throws ObjectNotFoundException, DatabaseException;

    public void create(int categoryID, String forumOwnerName, String lastPostMemberName,
                       String forumName, String forumDesc, Timestamp forumCreationDate,
                       Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder,
                       int forumType, int forumFormatOption, int forumOption,
                       int forumStatus, int forumModerationMode, String forumPassword,
                       int forumThreadCount, int forumPostCount)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public int createForum(int categoryID, String forumOwnerName, String lastPostMemberName,
                        String forumName, String forumDesc, Timestamp forumCreationDate,
                        Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder,
                        int forumType, int forumFormatOption, int forumOption,
                        int forumStatus, int forumModerationMode, String forumPassword,
                        int forumThreadCount, int forumPostCount)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void delete(int forumID)
        throws DatabaseException, ObjectNotFoundException;

    public void update(int forumID, // primary key
                       int categoryID, String forumOwnerName, String forumName, String forumDesc,
                       Timestamp forumModifiedDate, int forumOrder, int forumType,
                       int forumFormatOption, int forumOption, int forumStatus,
                       int forumModerationMode)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void updateLastPostMemberName(int forumID, // primary key
                                         String lastPostMemberName)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public void updateLastPostDate(int forumID, // primary key
                                   Timestamp forumLastPostDate)
        throws ObjectNotFoundException, DatabaseException;

    public void resetForumOwnerNameWhenDeleteMember(String memberName)
        throws DatabaseException;

    public void updateStatistics(int forumID, // primary key
                                 int forumThreadCount, int forumPostCount)
        throws ObjectNotFoundException, DatabaseException;

    public void increasePostCount(int forumID)
        throws DatabaseException, ObjectNotFoundException;

    public void increaseThreadCount(int forumID)
        throws DatabaseException, ObjectNotFoundException;

    public void decreaseThreadCount(int forumID)
        throws DatabaseException, ObjectNotFoundException;

    public ForumBean getForum(int forumID)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getForums()
        throws DatabaseException;

    public Collection getForums_inCategory(int categoryID)
        throws DatabaseException;

    public Collection getForums_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public Collection getForums_withSortSupport_limit_ViewCount(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public void decreaseForumOrder(int forumID, Timestamp forumModifiedDate)
        throws DatabaseException, ObjectNotFoundException;

    public void increaseForumOrder(int forumID, Timestamp forumModifiedDate)
        throws DatabaseException, ObjectNotFoundException;
}
