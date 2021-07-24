/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/member/DeleteMemberIndexTask.java,v 1.10 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.10 $
 * $Date: 2008/12/31 03:50:24 $
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
package com.mvnforum.search.member;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This class is used for indexing single Member
public class DeleteMemberIndexTask extends TimerTask {
    
    private static final Logger log = LoggerFactory.getLogger(DeleteMemberIndexTask.class);

    private int objectID;

    /*
     * Constructor with default access, prevent new an instance from outside package
     */
    DeleteMemberIndexTask(int objectID) {
        this.objectID = objectID;
    }

    public void run() {
        log.debug("DeleteMemberIndexTask.run : objectID = " + objectID);
        try {
            MemberIndexer.deleteMemberFromIndex(objectID);
        } catch (Exception ex) {
            log.error("Error while performing index operation", ex);
        }
    }
    
}
