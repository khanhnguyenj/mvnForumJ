/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/VBulletinPostDAO.java,v 1.2 2009/10/07 01:53:39 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.2 $
 * $Date: 2009/10/07 01:53:39 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.vbulletin.db;

import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

/**
 * The Interface VBulletinPostDAO.
 */
public interface VBulletinPostDAO {

    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "post";

    /**
     * Gets the bean.
     * 
     * @param postID the post id
     * 
     * @return the bean
     * 
     * @throws ObjectNotFoundException the object not found exception
     * @throws DatabaseException the database exception
     */
    public VBulletinPostBean getBean(int postID)
        throws ObjectNotFoundException, DatabaseException;
    
    /**
     * Gets the first post in thread.
     * 
     * @param threadID the thread id
     * 
     * @return the first post in thread
     * 
     * @throws ObjectNotFoundException the object not found exception
     * @throws DatabaseException the database exception
     */
    public VBulletinPostBean getFirstPostInThread(int threadID)
        throws ObjectNotFoundException, DatabaseException;

    /**
     * Gets the child post.
     * 
     * @param postID the post id
     * 
     * @return the child post
     * 
     * @throws DatabaseException the database exception
     */
    public Collection getChildPost(int postID)
        throws DatabaseException;
}
