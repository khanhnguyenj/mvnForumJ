/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageStatisticsDAO.java,v 1.11 2007/10/09 11:09:19 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.11 $
 * $Date: 2007/10/09 11:09:19 $
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

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;

public interface MessageStatisticsDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "MessageStatistics";

    public void create(int fromID, int toID, Timestamp messageCreationDate,
                       int messageAttachCount, int messageType, int messageOption, int messageStatus)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException;

    public void delete_inMember(int memberID)
        throws DatabaseException;

    public int getNumberOfBeans_inFromID(int fromID)
        throws DatabaseException;

    public int getNumberOfBeans_inToID(int toID)
        throws DatabaseException;

    public int getNumberOfBeans_inFromID_supportTimestamp(int fromID, Timestamp from, Timestamp to)
        throws DatabaseException;

    public int getNumberOfBeans_inToID_supportTimestamp(int toID, Timestamp from, Timestamp to)
        throws DatabaseException;
}
