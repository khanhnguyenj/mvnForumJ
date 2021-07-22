/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/db/DAOFactory.java,v 1.3 2009/10/07 01:53:39 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
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

import com.mvnforum.vbulletin.db.jdbc.*;

/**
 * A factory for creating DAO objects.
 */
public final class DAOFactory {
    
    /**
     * Instantiates a new dAO factory.
     */
    private DAOFactory() {}
    
    /** The v bulletin user dao. */
    private static VBulletinUserDAO vBulletinUserDAO;
    
    /** The v bulletin forum dao. */
    private static VBulletinForumDAO vBulletinForumDAO;
    
    /** The v bulletin thread dao. */
    private static VBulletinThreadDAO vBulletinThreadDAO;
    
    /** The v bulletin post dao. */
    private static VBulletinPostDAO vBulletinPostDAO;
    
    /** The v bulletin attachment dao. */
    private static VBulletinAttachmentDAO vBulletinAttachmentDAO;
    
    /** The v bulletin attachment type dao. */
    private static VBulletinAttachmentTypeDAO vBulletinAttachmentTypeDAO;
    
    /** The v bulletin message folder dao. */
    private static VBulletinMessageFolderDAO vBulletinMessageFolderDAO;
    
    /** The v bulletin private message dao. */
    private static VBulletinPrivateMessageDAO vBulletinPrivateMessageDAO;
    
    /**
     * Gets the v bulletin user dao.
     * 
     * @return the v bulletin user dao
     */
    public static VBulletinUserDAO getVBulletinUserDAO() {
        return vBulletinUserDAO;
    }
    
    /**
     * Gets the v bulletin forum dao.
     * 
     * @return the v bulletin forum dao
     */
    public static VBulletinForumDAO getVBulletinForumDAO() {
        return vBulletinForumDAO;
    }
    
    /**
     * Gets the v bulletin thread dao.
     * 
     * @return the v bulletin thread dao
     */
    public static VBulletinThreadDAO getVBulletinThreadDAO() {
        return vBulletinThreadDAO;
    }
    
    /**
     * Gets the v bulletin post dao.
     * 
     * @return the v bulletin post dao
     */
    public static VBulletinPostDAO getVBulletinPostDAO() {
        return vBulletinPostDAO;
    }
    
    /**
     * Gets the v bulletin attachment dao.
     * 
     * @return the v bulletin attachment dao
     */
    public static VBulletinAttachmentDAO getVBulletinAttachmentDAO() {
        return vBulletinAttachmentDAO;
    }
    
    /**
     * Gets the v bulletin attachment type dao.
     * 
     * @return the v bulletin attachment type dao
     */
    public static VBulletinAttachmentTypeDAO getVBulletinAttachmentTypeDAO() {
        return vBulletinAttachmentTypeDAO;
    }
    
    /**
     * Gets the v bulletin message folder dao.
     * 
     * @return the v bulletin message folder dao
     */
    public static VBulletinMessageFolderDAO getVBulletinMessageFolderDAO() {
        return vBulletinMessageFolderDAO;
    }
    
    /**
     * Gets the v bulletin private message dao.
     * 
     * @return the v bulletin private message dao
     */
    public static VBulletinPrivateMessageDAO getVBulletinPrivateMessageDAO() {
        return vBulletinPrivateMessageDAO;
    }

    static {
        vBulletinUserDAO = new VBulletinUserDAOImplJDBC();
        vBulletinForumDAO = new VBulletinForumDAOImplJDBC();
        vBulletinThreadDAO = new VBulletinThreadDAOImplJDBC();
        vBulletinPostDAO = new VBulletinPostDAOImplJDBC();
        vBulletinAttachmentDAO = new VBulletinAttachmentDAOImplJDBC();
        vBulletinAttachmentTypeDAO = new VBulletinAttachmentTypeDAOImplJDBC();
        vBulletinMessageFolderDAO = new VBulletinMessageFolderDAOImplJDBC();
        vBulletinPrivateMessageDAO = new VBulletinPrivateMessageDAOImplJDBC();
    }

}
