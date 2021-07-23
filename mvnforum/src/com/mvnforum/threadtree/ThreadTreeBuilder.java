/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/threadtree/ThreadTreeBuilder.java,v 1.4 2009/06/16 03:15:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2009/06/16 03:15:16 $
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
 * @author: Eg
 */
package com.mvnforum.threadtree;

import java.util.Observable;

import net.myvietnam.mvncore.exception.DatabaseException;

public class ThreadTreeBuilder extends Observable {
    
    //protected     Collection     threadBeans;
    protected int    forumID;
    protected String filterCriteria;
    
    /**
     * Constructor
     * @param forumID
     * @param filterCriteria
     * @throws DatabaseException
     */
    public ThreadTreeBuilder(int forumID, String filterCriteria) throws DatabaseException {
        //threadBeans = ThreadCache.getInstance().getNormalEnableThreads_inForum_withSortSupport_limit(forumID, 0, ThreadCache.getInstance().getNumberOfEnableThreads_inForum(forumID), "ThreadViewCount", "ASC");
        this.forumID        = forumID;
        this.filterCriteria = filterCriteria;
    }

//    /**
//     * Returns 
//     * @return the threadBeans
//     */
//    public Collection getThreadBeans() {
//        return threadBeans;
//    }

    /**
     * @return the filterCriteria
     */
    public String getFilterCriteria() {
        return filterCriteria;
    }

    /**
     * @return the forumID
     */
    public int getForumID() {
        return forumID;
    }
}
