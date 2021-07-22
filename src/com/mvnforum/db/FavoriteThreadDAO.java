/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/FavoriteThreadDAO.java,v 1.12 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
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

public interface FavoriteThreadDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "FavoriteThread";

    public void findByPrimaryKey(int memberID, int threadID)
        throws ObjectNotFoundException, DatabaseException;

    public void create(int memberID, int threadID, int forumID,
                       Timestamp favoriteCreationDate, int favoriteType, int favoriteOption,
                       int favoriteStatus)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void delete(int memberID, int threadID)
        throws DatabaseException, ObjectNotFoundException;

    public void delete_inThread(int threadID)
        throws DatabaseException;

    public void delete_inForum(int forumID)
        throws DatabaseException;

    public void delete_inMember(int memberID)
        throws DatabaseException;

    public void update_ForumID_inThread(int threadID, int forumID)
        throws DatabaseException, ForeignKeyNotFoundException;

    // this method is not used any where, should we remove it ???
    // The method used is DAOFactory.getThreadDAO().getBeans_inFavorite_inMember(memberID);
    public Collection getFavoriteThreads_inMember(int memberID)
        throws DatabaseException;

//    public int getNumberOfBeans()
//        throws DatabaseException;

    public int getNumberOfFavoriteThreads_inMember(int memberID)
        throws DatabaseException;
}
