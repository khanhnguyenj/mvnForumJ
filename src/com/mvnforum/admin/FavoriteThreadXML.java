/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/FavoriteThreadXML.java,v 1.11 2009/01/06 18:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2009/01/06 18:31:30 $
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
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.db.DAOFactory;

/**
 * @author Igor Manic
 * @version $Revision: 1.11 $, $Date: 2009/01/06 18:31:30 $
 * <br/>
 * <code>FavoriteThreadXML</code> todo Igor: enter description
 *
 */
public class FavoriteThreadXML {

    /* There is no FavoriteThreadID!
    private int favoriteThreadID;
    ** Returns <code>FavoriteThreadID</code> of this favorite-thread or
      * <code>-1</code> if favorite-thread is not created yet. *
    public int getFavoriteThreadID() { return favoriteThreadID; } */

    private int parentThreadID;
    /** Returns <code>ThreadID</code> of this favorite-thread's parent thread or
      * <code>-1</code> if this favorite-thread is not created yet. */
    public int getParentThreadID() { return parentThreadID; }

    private int parentForumID;
    /** Returns <code>ForumID</code> of this favorite-thread's parent forum or
      * <code>-1</code> if this favorite-thread is not created yet. */
    public int getParentForumID() { return parentForumID; }

    public FavoriteThreadXML() {
        //favoriteThreadID=-1;
        parentThreadID = -1;
        parentForumID = -1;
    }

    /*public void setFavoriteThreadID(String id) {
        favoriteThreadID=XMLUtil.stringToIntDef(id, -1);
    }*/

    public void setParentThread(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof ThreadXML) {
            parentThreadID=((ThreadXML)o).getThreadID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent thread's ID");
        }
    }

    public void setParentThreadID(int value) {
        if (value < 0) {
            parentThreadID = -1;
        } else {
            parentThreadID = value;
        }
    }

    public void setParentForum(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof ThreadXML) {
            parentForumID=((ThreadXML)o).getParentForumID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent forum's ID");
        }
    }

    public void setParentForumID(int value) {
        if (value < 0) {
            parentForumID = -1;
        } else {
            parentForumID = value;
        }
    }

    /**
     * Creates a favorite-thread. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param memberName Member who owns this favorite-thread record.
     * @param favoriteCreationDate Can be null.
     * @param favoriteType Can be null.
     * @param favoriteOption Can be null.
     * @param favoriteStatus Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addFavoriteThread(String memberName,
                       String favoriteCreationDate, String favoriteType,
                       String favoriteOption, String favoriteStatus)
    throws CreateException, DuplicateKeyException, ObjectNotFoundException,
    DatabaseException, ForeignKeyNotFoundException {
        if (parentThreadID<0) {
            throw new CreateException("Can't create a favorite-thread, because no parent thread assigned yet.");
        } else if (parentForumID<0) {
            throw new CreateException("Can't create a favorite-thread, because no parent forum assigned yet.");
        } else if ( (memberName==null) || (memberName.equals("")) ) {
            throw new CreateException("Can't create a favorite-thread for a member with empty MemberName.");
        } else {
            java.sql.Timestamp favoriteCreationDate1;
            int favoriteType1;
            int favoriteOption1;
            int favoriteStatus1;
            try {
                favoriteCreationDate1= XMLUtil.stringToSqlTimestampDefNow(favoriteCreationDate);
                favoriteType1= XMLUtil.stringToIntDef(favoriteType, 0);
                favoriteOption1= XMLUtil.stringToIntDef(favoriteOption, 0);
                favoriteStatus1= XMLUtil.stringToIntDef(favoriteStatus, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a favorite-thread. Expected a number.");
            }

            int memberID=DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
            DAOFactory.getFavoriteThreadDAO().create(
                          memberID, parentThreadID, parentForumID,
                          favoriteCreationDate1, favoriteType1,
                          favoriteOption1, favoriteStatus1);

        }
    }

}

