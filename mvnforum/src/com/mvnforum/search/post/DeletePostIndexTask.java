/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/search/post/DeletePostIndexTask.java,v 1.11 2009/01/13 17:46:01 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
 * $Date: 2009/01/13 17:46:01 $
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
package com.mvnforum.search.post;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This class is used for indexing single post
public class DeletePostIndexTask extends TimerTask {
    
    private static final Logger log = LoggerFactory.getLogger(DeletePostIndexTask.class);

    //Constants used for operations
    public static final int OBJECT_TYPE_POST   = 0;
    public static final int OBJECT_TYPE_THREAD = 1;
    public static final int OBJECT_TYPE_FORUM  = 2;

    private int objectID;
    private int objectType;

    /*
     * Constructor with default access, prevent new an instance from outside package
     */
    DeletePostIndexTask(int objectID, int objectType) {
        this.objectID = objectID;
        this.objectType = objectType;
    }

    public void run() {
        log.debug("DeletePostIndexTask.run : objectID = " + objectID + " with objectType = " + objectType);
        try {
            switch (objectType) {
                case OBJECT_TYPE_POST:
                    PostIndexer.deletePostFromIndex(objectID);
                    break;
                case OBJECT_TYPE_THREAD:
                    PostIndexer.deleteThreadFromIndex(objectID);
                    break;
                case OBJECT_TYPE_FORUM:
                    PostIndexer.deleteForumFromIndex(objectID);
                    break;
                default:
                    log.warn("Cannot process the DeletePostIndexTask with objectID = " + objectID + " with objectType = " + objectType);
                    break;
            }
        } catch (Exception ex) {
            log.error("Error while performing index operation", ex);
        }
    }
    
}
