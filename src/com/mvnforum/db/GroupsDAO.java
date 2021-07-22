/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/GroupsDAO.java,v 1.21 2009/01/02 15:12:55 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.21 $
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

public interface GroupsDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Groups";

    public void findByPrimaryKey(int groupID)
        throws ObjectNotFoundException, DatabaseException;

    public void findByAlternateKey_GroupName(String groupName)
        throws ObjectNotFoundException, DatabaseException;

    public void create(String groupOwnerName, String groupName,
                       String groupDesc, int groupOption, Timestamp groupCreationDate,
                       Timestamp groupModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void delete(int groupID)
        throws DatabaseException, ObjectNotFoundException;

    public void update(int groupID, // primary key
                       String groupName, String groupDesc, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException;

    public void updateOwner(int groupID, // primary key
                            String groupOwnerName, Timestamp groupModifiedDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;

    public GroupsBean getGroup(int groupID)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getMyGroups(int memberID)
        throws DatabaseException;

    public Collection getGroups()
        throws DatabaseException;

    // Note that this method is not used anywhere
    public int getNumberOfGroups()
        throws DatabaseException;

    public int getGroupIDFromGroupName(String groupName)
        throws ObjectNotFoundException, DatabaseException;

}
