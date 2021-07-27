/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MessageDAO.java,v 1.34 2007/12/17 09:09:40 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.34 $
 * $Date: 2007/12/17 09:09:40 $
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
 * @author: Mai  Nguyen
 */
package com.mvnforum.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.*;

public interface MessageDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Message";

    public void findByPrimaryKey(int messageID)
        throws ObjectNotFoundException, DatabaseException;

    public int create(String folderName, int memberID, int messageSenderID,
                      String messageSenderName, String messageToList, String messageCcList,
                      String messageBccList, String messageTopic, String messageBody,
                      int messageType, int messageOption, int messageStatus,
                      int messageReadStatus, int messageNotify, String messageIcon,
                      int messageAttachCount, String messageIP, Timestamp messageCreationDate)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException;

    // This method is used to get all messages for delete all messages (include public) in folder
    public Collection getAllMessages_inMember_inFolder_withSortSupport_limit(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    // This method is used to list Non public messages in MyMessage
    public Collection getNonPublicMessages_inMember_inFolder_withSortSupport_limit(int memberID, String folderName, int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;

    public MessageBean getMessage(int messageID)
        throws ObjectNotFoundException, DatabaseException;

    public int getNumberOfNonPublicMessages_inMember(int memberID)
        throws DatabaseException;

    public void updateMessageReadStatus(int messageID, int memberID, int messageReadStatus)
        throws ObjectNotFoundException, DatabaseException;

    public void deleteMessage(int messageID, int memberID)
        throws DatabaseException, ObjectNotFoundException;

    public void deleteSenderMessages(int senderID)
        throws DatabaseException;

    //@todo: should we update also based on MemberID ???
    public void updateAttachCount(int messageID, // primary key
                                  int messageAttachCount)
        throws ObjectNotFoundException, DatabaseException;

    public void updateFolderName(int messageID, // primary key
                                 int memberID, String folderName)
        throws ObjectNotFoundException, DatabaseException;

    public void deleteMessages_inFolderName_inMember(String folderName, int memberID)
        throws DatabaseException;

    public Collection getPublicMessages()
        throws DatabaseException;

    public int getNumberOfNonPublicMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException;

    public int getNumberOfUnreadNonPublicMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException;

    public int getNumberOfAllMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException;

    public int getNumberOfUnreadAllMessages_inMember_inFolder(int memberID, String folderName)
        throws DatabaseException;

}
