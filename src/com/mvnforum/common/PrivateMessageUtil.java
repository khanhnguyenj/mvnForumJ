/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/PrivateMessageUtil.java,v 1.12 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2009/01/02 15:12:54 $
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
package com.mvnforum.common;

import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.*;

public final class PrivateMessageUtil {

    private static final Logger log = LoggerFactory.getLogger(PrivateMessageUtil.class);

    private PrivateMessageUtil() {
    }

    /**
     * This is a utility method that should only called after all checking
     * The caller should check existence of data and try to avoid the ObjectNotFoundException
     *
     * @param locale Locale
     * @param messageID int
     * @param memberID int
     * @throws DatabaseException
     * @throws ObjectNotFoundException
     */
    public static void deleteMessageInDatabase(int messageID, int memberID)
        throws DatabaseException, ObjectNotFoundException {

        log.debug("Delete Private Message with MessageID = " + messageID);

        // call this first to check the valid of memberID
        DAOFactory.getMessageDAO().deleteMessage(messageID, memberID);

        // the call this after
        DAOFactory.getPmAttachMessageDAO().delete_inMessage(messageID);
    }

    /**
     * This is a utility method that should only called after all checking
     * The caller should check existence of data and try to avoid the ObjectNotFoundException
     *
     * @param locale Locale
     * @param folderName String
     * @param memberID int
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     */
    public static void deleteMessageFolderInDatabase(String folderName, int memberID)
        throws ObjectNotFoundException, DatabaseException {

        // Delete the MessagePmAttach
        Collection messageBeans = DAOFactory.getMessageDAO().getAllMessages_inMember_inFolder_withSortSupport_limit(memberID, folderName, 0, 10000, "MessageCreationDate", "ASC");
        for (Iterator iter = messageBeans.iterator(); iter.hasNext(); ) {
            MessageBean messageBean = (MessageBean) iter.next();

            DAOFactory.getPmAttachMessageDAO().delete_inMessage(messageBean.getMessageID());
        }

        // Then delete all messages belong to this folder
        DAOFactory.getMessageDAO().deleteMessages_inFolderName_inMember(folderName, memberID);

        // Finally delete a folder name
        DAOFactory.getMessageFolderDAO().delete(folderName, memberID);
    }

    public static void deleteMessageFoldersInDatabase(int memberID)
        throws ObjectNotFoundException, DatabaseException {

        Collection folderBeans = DAOFactory.getMessageFolderDAO().getMessageFolders_inMember(memberID);
        for (Iterator iter = folderBeans.iterator(); iter.hasNext(); ) {
            MessageFolderBean messageFolderBean = (MessageFolderBean) iter.next();

            deleteMessageFolderInDatabase(messageFolderBean.getFolderName(), memberID);
        }
    }
}
