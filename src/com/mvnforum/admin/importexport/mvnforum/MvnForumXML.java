/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumXML.java,v 1.14 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/01/12 15:02:34 $
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
package com.mvnforum.admin.importexport.mvnforum;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.admin.*;
import com.mvnforum.db.DAOFactory;

/**
 * @author Igor Manic
 * @version $Revision: 1.14 $, $Date: 2009/01/12 15:02:34 $
 * <br/>
 * <code>MvnForumXML</code> todo Igor: enter description
 */
public class MvnForumXML {

    // Did we find Admin member? If we didn't, default one will be created, with password "admin".
    public static boolean addedAdminMember = false;
    // Did we find Guest member? If we didn't, default one will be created.
    public static boolean addedGuestMember = false;
    // Did we find Registered Members group? If we didn't, default one will be created.
    public static boolean addedRegisteredMembersGroup = false;

    public MvnForumXML() {
        MvnForumXML.addedAdminMember = false;
        MvnForumXML.addedGuestMember = false;
        MvnForumXML.addedRegisteredMembersGroup = false;
        ImportMvnForum.createDefaultContents();
    }


// ================================================================================
// ==================== METHODS TO BE CALLED FROM THE DIGESTER ====================
// ================================================================================
    public void setMvnForumXmlVersion(String value) {
        ImportMvnForum.addMessage("mvnForum XML version = \""+value+"\"");
    }

    public void setMvnForumExportDate(String value) {
        ImportMvnForum.addMessage("mvnForum XML export date = \""+value+"\"");
    }

    public void postProcessMemberList() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        MvnForumXML.checkAdminMember();
        MvnForumXML.checkGuestMember();
    }

    public void postProcessGroupList() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, 
        DatabaseException, ForeignKeyNotFoundException {
        
        MvnForumXML.checkRegisteredMembersGroup();
    }

    public static void finishImport() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        checkAdminMember();
        checkGuestMember();
        checkRegisteredMembersGroup();
    }

    public void addGuestGlobalPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        
        MvnForumXML.addGuestMemberPermission(permission);
    }

    public void addRegisteredMembersGlobalPermission(String permission)
        throws CreateException, DuplicateKeyException, DatabaseException, ForeignKeyNotFoundException {
        
        MvnForumXML.addRegisteredMembersGroupPermission(permission);
    }

    public void addRank(String rankMinPosts, String rankLevel, String rankTitle,
                        String rankImage, String rankType, String rankOption)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException {
        
        ImportMvnForum.addMessage("Adding rank title \""+rankTitle+"\".");
        (new RankXML()).addRank(rankMinPosts, rankLevel, rankTitle, rankImage, rankType, rankOption);
    }

// ================================================================================
// ====================== UTILITY METHODS ABOUT PERMISSIONS =======================
// ================================================================================
    /**
     * TODO Igor: enter description
     *
     * @param username
     * @param permission
     */
    public static void addMemberPermission(String username, String permission)
        throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {
        
        MemberXML.addMemberPermission(username, permission);
    }

    /**
     * TODO Igor: enter description
     *
     * @param permission
     */
    public static void addGuestMemberPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        
        MemberXML.addGuestMemberPermission(permission);
    }

    /**
     * TODO Igor: enter description
     *
     * @param permission
     */
    public static void addRegisteredMembersGroupPermission(String permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        
        GroupXML.addRegisteredMembersGroupPermission(permission);
    }

    /**
     * TODO Igor: enter description
     *
     * @param groupname
     * @param permission
     */
    public static void addGroupPermission(String groupname, String permission)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, ObjectNotFoundException {
        
        GroupXML.addGroupPermission(groupname, permission);
    }


// ================================================================================
// ============================ OTHER PUBLIC METHODS ==============================
// ================================================================================
    public static void checkAdminMember() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        if (addedAdminMember == false) {
            ImportMvnForum.addImportantMessage("Didn't find SYSTEM_ADMIN member. Adding default one with a name \""+
                                "admin\" and password \"admin\". For your security, "+
                                "you should first change that password.");
            ImportWebHelper.createDefaultAdminMember();
            addedAdminMember = true;
        }
    }

    public static void checkGuestMember() 
        throws CreateException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if (addedGuestMember == false) {
            ImportMvnForum.addImportantMessage("Didn't find virtual guest member. "+
                           "Adding default one with a name \""+
                           MVNForumConfig.getDefaultGuestName()+"\" and no password. "+
                           "He'll have READ_POST permissions in all forums, "+
                           "and global ADD_POST permission.");
            try {
                ImportWebHelper.createDefaultGuestMember();
            } catch (DuplicateKeyException de) {
                // do nothing
                // ignore when the guest user existed
            }
            addedGuestMember = true;
        }
    }

    public static void checkRegisteredMembersGroup() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        //ImportMvnForum.addImportantMessage("Import registered members :::  ");
        if (addedRegisteredMembersGroup == false) {
            //set GroupOwnerName to the system admin
            String groupOwnerName="";
            try {
                groupOwnerName=DAOFactory.getMemberDAO().getMember(MVNForumConstant.MEMBER_ID_OF_ADMIN).getMemberName();
            } catch (Exception e) {
                groupOwnerName = "";
            }
            ImportMvnForum.addImportantMessage("Didn't find \"Registered Members\" group. Adding default one.");
            ImportWebHelper.createDefaultRegisteredMembersGroup(groupOwnerName);
            addedRegisteredMembersGroup=true;
        }
    }

}
