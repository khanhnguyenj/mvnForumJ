/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/jive/JiveXML.java,v 1.15 2009/12/03 11:19:36 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.15 $
 * $Date: 2009/12/03 11:19:36 $
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
package com.mvnforum.admin.importexport.jive;

import net.myvietnam.mvncore.exception.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.admin.*;
import com.mvnforum.auth.MVNForumPermission;

/**
 * @author Igor Manic
 * @version $Revision: 1.15 $, $Date: 2009/12/03 11:19:36 $
 * <br/>
 * <code>JiveXML</code> class encapsulates auxiliary methods for conversion of
 * <code>Jive</code> permissions and passwords to <code>mvnForum</code> ones.
 * It also defines some common constants used during the migration from
 * <code>Jive</code> to <code>mvnForum</code>.<br/>
 * Instance of this class will be the root object of the <code>digester</code> stack.
 */
public class JiveXML {

    public static String allForumsPassword = "";
    public static String rootCategoryName  = "General";
    public static String rootCategoryDesc  = "Forums imported from Jive";
    public static String adminName         = "Admin";
    public static String guestName         = MVNForumConfig.getDefaultGuestName();
    
    private static final int[] EMPTY_PERMISSION = new int[0]; 


    /** Did we find Jive admin user? If we didn't, default one will be created with password "admin".
      * If we found Jive admin user, we kept his password, but now he has SYSTEM_ADMIN
      * permissions, even if he didn't have them in Jive!!!
      */
    public static boolean foundAdminUser   = false;
    public static int rootCategoryID       = -1;

    public JiveXML() 
        throws DatabaseException, CreateException, DuplicateKeyException, ObjectNotFoundException, ForeignKeyNotFoundException {
        
        foundAdminUser = false;

        /* First, I'll create guest and admin user, but, if admin already exists in Jive,
         * it will be rewritten when I come to it. For now, it's only important
         * that I have it in the database, and that it has ID==1.
         */
        ImportJive.createDefaultAdminMember();
        ImportJive.createDefaultGuestMember();
        ImportJive.createDefaultGuestGroup();
        ImportJive.createDefaultRegisteredMembersGroup();
        ImportJive.createDefaultRanks();

        /* Each MVN category can have any number of it's own forums.
         * Also, MVN supports (sub)categories in each category.
         * Jive's forums are the same as MVN's forums, but Jive doesn't have categories at all.
         * That means that all Jive's forums will be translated into MVN's forums that
         * have to be put down into the one and only MVN category I must create here.
         */
        ImportJive.createRootCategory();
    }

    public static void addedForum(ForumXML forumXML) {
        //update the root category - nothing to update
    }

    public static void addedThread(ThreadXML threadXML) {
        //update the root category - nothing to update
    }

    public static void addedPost(PostXML postXML) {
        //update the root category - nothing to update
    }


// ================================================================================
// ============== UTILITY CONVERSION METHODS FROM JIVE TO MVN FORUM ===============
// ================================================================================
    /**
     * Conversion of Jive password to MVN Forum password.
     * Jive and MVN Forum both use MD5:
     * &nbsp;&nbsp;&nbsp;Jive password -> MD5 encoding -> Hex encoding
     * &nbsp;&nbsp;&nbsp;MVN Forum password -> MD5 encoding -> Base64 encoding
     * So to convert from Jive password to MVN Forum password:
     * Jive:&nbsp;&nbsp; Hex encoding -> Hex decoding -> Base 64 encoding &nbsp;&nbsp;:MVN Forum
     *
     * @param jiveEncodedPassword Jive encoded password.
     * @return MVN Forum encoded password, or <code>null</code> in case of error.
     *
     */
    public static String convertPassword(String jiveEncodedPassword) {
        String mvnEncodedPassword=null;
        try {
            byte[] decodedByteArray = Hex.decodeHex(jiveEncodedPassword.toCharArray());
            mvnEncodedPassword=new String(Base64.encodeBase64(decodedByteArray));
        } catch (Exception e) {
            mvnEncodedPassword=null;
        }
        return mvnEncodedPassword;
    }

    public static int[] convertMemberPermission(String jivePermission) {
        return convertPermission(jivePermission, true);
    }
    
    public static int[] convertGroupPermission(String jivePermission) {
        return convertPermission(jivePermission, true);
    }
    
    public static int[] convertMemberForumPermission(String jivePermission) {
        return convertPermission(jivePermission, false);
    }
    
    public static int[] convertGroupForumPermission(String jivePermission) {
        return convertPermission(jivePermission, false);
    }
    
    /**
     * Return an array of permission after converting from Jive
     *  
     * @param jivePermission the Jive permission 
     * @param isGlobalPermission true if global permission, false if it is forum-specific permission
     * @return
     */
    protected static int[] convertPermission(String jivePermission, boolean isGlobalPermission) {
        
        //TODO Igor: check again all permission conversions here
        if ( (jivePermission==null) || (jivePermission.equalsIgnoreCase("NONE")) ) {
            return new int[] {MVNForumPermission.PERMISSION_NO_PERMISSIONS};
        
        } else if (jivePermission.equalsIgnoreCase("SYSTEM_ADMIN")) {
            if (isGlobalPermission) {
                return new int[] {MVNForumPermission.PERMISSION_SYSTEM_ADMIN};
            } else {
                return EMPTY_PERMISSION;
            }
        
        } else if (jivePermission.equalsIgnoreCase("CATEGORY_ADMIN")) {
            if (isGlobalPermission) {
                // presently mvnForum does not support permission PERMISSION_CATEGORY_ADMIN and PERMISSION_CATEGORY_MODERATOR
                return new int[] {/*MVNForumPermission.PERMISSION_CATEGORY_ADMIN,
                                  MVNForumPermission.PERMISSION_CATEGORY_MODERATOR,*/
                                  MVNForumPermission.PERMISSION_ADD_CATEGORY,
                                  MVNForumPermission.PERMISSION_EDIT_CATEGORY,
                                  MVNForumPermission.PERMISSION_DELETE_CATEGORY};
            } else {
                return EMPTY_PERMISSION;
            }
        
        } else if (jivePermission.equalsIgnoreCase("FORUM_ADMIN")) {
            // note that PERMISSION_FORUM_ADMIN is a combined permission so it includes other permission
            return new int[] {MVNForumPermission.PERMISSION_FORUM_ADMIN/*,
                              MVNForumPermission.PERMISSION_FORUM_MODERATOR,
                              MVNForumPermission.PERMISSION_ADD_FORUM,
                              MVNForumPermission.PERMISSION_EDIT_FORUM,
                              MVNForumPermission.PERMISSION_DELETE_FORUM*/};
        
        } else if (jivePermission.equalsIgnoreCase("GROUP_ADMIN")) {
            if (isGlobalPermission) {
                return new int[] {MVNForumPermission.PERMISSION_GROUP_ADMIN,
                                  MVNForumPermission.PERMISSION_GROUP_MODERATOR};
            } else {
                return EMPTY_PERMISSION;
            }
        
        } else if (jivePermission.equalsIgnoreCase("USER_ADMIN")) {
            if (isGlobalPermission) {
                return new int[] {MVNForumPermission.PERMISSION_USER_ADMIN,
                                  MVNForumPermission.PERMISSION_USER_MODERATOR};
            } else {
                return EMPTY_PERMISSION;
            }
        
        } else if (jivePermission.equalsIgnoreCase("MODERATOR")) {
            if (isGlobalPermission) {
                return new int[] {MVNForumPermission.PERMISSION_CATEGORY_MODERATOR,
                                  MVNForumPermission.PERMISSION_FORUM_MODERATOR,
                                  MVNForumPermission.PERMISSION_GROUP_MODERATOR,
                                  MVNForumPermission.PERMISSION_USER_MODERATOR};
            } else {
                return new int[] {MVNForumPermission.PERMISSION_FORUM_MODERATOR};
            }
        
        } else if (jivePermission.equalsIgnoreCase("MODERATE_THREADS")) {
            return new int[] {MVNForumPermission.PERMISSION_READ_POST,
                              MVNForumPermission.PERMISSION_ADD_POST,
                              MVNForumPermission.PERMISSION_EDIT_POST,
                              MVNForumPermission.PERMISSION_DELETE_POST,
                              MVNForumPermission.PERMISSION_ADD_THREAD};
        
        } else if (jivePermission.equalsIgnoreCase("CREATE_THREAD")) {
            return new int[] {MVNForumPermission.PERMISSION_ADD_THREAD};
        
        } else if (jivePermission.equalsIgnoreCase("MODERATE_MESSAGES")) {
            return new int[] {MVNForumPermission.PERMISSION_READ_POST,
                              MVNForumPermission.PERMISSION_ADD_POST,
                              MVNForumPermission.PERMISSION_EDIT_POST,
                              MVNForumPermission.PERMISSION_DELETE_POST};
        
        } else if (jivePermission.equalsIgnoreCase("CREATE_MESSAGE")) {
            return new int[] {MVNForumPermission.PERMISSION_ADD_POST};
        
        } else if (jivePermission.equalsIgnoreCase("CREATE_ATTACHMENT")) {
            return new int[] {MVNForumPermission.PERMISSION_ADD_ATTACHMENT};
        
        } else if (jivePermission.equalsIgnoreCase("READ_FORUM")) {
            return new int[] {MVNForumPermission.PERMISSION_READ_POST};
        
        } else if (jivePermission.equalsIgnoreCase("READ")) {
            return new int[] {MVNForumPermission.PERMISSION_READ_POST};
        
        } else {
            // TODO minhnn: I think we should show warning here, even could throw AssertionError
            return new int[] {MVNForumPermission.PERMISSION_NO_PERMISSIONS};
        }
    }


// ================================================================================
// ==================== METHODS TO BE CALLED FROM THE DIGESTER ====================
// ================================================================================
    public void setJiveXmlVersion(String value) {
        ImportJive.addMessage("Jive XML version = \"" + value + "\"");
    }

    public void setJiveExportDate(String value) {
        ImportJive.addMessage("Jive XML export date = \"" + value + "\"");
    }

    public void addJiveUserPermission(String usertype, String username, String jivePermission)
        throws CreateException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException {
        
        if (usertype == null) {
            throw new CreateException("Not enough data to create a member global permission.");

        } 
        
        if (usertype.equalsIgnoreCase("ANONYMOUS")) {
            int[] perms = JiveXML.convertMemberPermission(jivePermission);
            for (int j=0; j<perms.length; j++) {
//                try {
                    JiveXML.addGuestMemberPermission(Integer.toString(perms[j]));
//                } catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
//                }
            }

        } else if (usertype.equalsIgnoreCase("REGISTERED_USERS")) {
            int[] perms = JiveXML.convertGroupPermission(jivePermission);
            for (int j=0; j<perms.length; j++) {
                try {
                    JiveXML.addRegisteredMembersGroupPermission(Integer.toString(perms[j]));
                } catch (DuplicateKeyException e) {
                   /* Ignore if we doubled some permissions.
                    * Because we convert each Jive permission into the list of
                    * MVN Forum permissions (can be more than one), some permissions
                    * could be generated twice, or more times.
                    */
                }
            }

        } else if (usertype.equalsIgnoreCase("USER")) {
            int[] perms = JiveXML.convertMemberPermission(jivePermission);
            username = ImportJive.getMemberNameFromMap(username);
            for (int j=0; j<perms.length; j++) {
//                try {
                    JiveXML.addMemberPermission(username, Integer.toString(perms[j]));
//                } catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
//                }
            }

        } else {
            throw new CreateException("Invalid usertype. This Jive user global permission is ignored.");
        }
    }

    public void addJiveGroupPermission(String groupname, String jivePermission)
        throws CreateException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException {
        
        if ( (groupname==null) || (groupname.equals("")) ) {
            throw new CreateException("Not enough data to create a group global permission.");
        } else {
            int[] perms = JiveXML.convertGroupPermission(jivePermission);
            for (int j=0; j<perms.length; j++) {
                try {
                    JiveXML.addGroupPermission(groupname, Integer.toString(perms[j]));
                } catch (DuplicateKeyException e) {
                   /* Ignore if we doubled some permissions.
                    * Because we convert each Jive permission into the list of
                    * MVN Forum permissions (can be more than one), some permissions
                    * could be generated twice, or more times.
                    */
                }
            }
        }
    }


//  ================================================================================
//  ====================== UTILITY METHODS ABOUT PERMISSIONS =======================
//  ================================================================================
     public static final int[] addDefaultPermissionsToGuests = new int[]{
         //MVNForumPermission.PERMISSION_SEND_MAIL,
         //MVNForumPermission.PERMISSION_USE_MESSAGE,
         //MVNForumPermission.PERMISSION_USE_AVATAR,
         //MVNForumPermission.PERMISSION_ADD_POLL,
         //MVNForumPermission.PERMISSION_EDIT_POLL,
         //MVNForumPermission.PERMISSION_DELETE_POLL,
         //MVNForumPermission.PERMISSION_GET_ATTACHMENT
     };
     
     public static final int[] addDefaultPermissionsToMembers = new int[]{
         //MVNForumPermission.PERMISSION_SEND_MAIL,
         MVNForumPermission.PERMISSION_USE_MESSAGE,
         MVNForumPermission.PERMISSION_USE_ALBUM,
         MVNForumPermission.PERMISSION_USE_AVATAR,
         //MVNForumPermission.PERMISSION_ADD_POLL,
         //MVNForumPermission.PERMISSION_EDIT_POLL,
         //MVNForumPermission.PERMISSION_DELETE_POLL,
         MVNForumPermission.PERMISSION_GET_ATTACHMENT
     };

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
     public static void addRegisteredMembersGroupPermission(String permission)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
         
         GroupXML.addRegisteredMembersGroupPermission(permission);
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
      * @param groupname
      * @param permission
      */
     public static void addGroupPermission(String groupname, String permission)
         throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, ObjectNotFoundException {
         
         GroupXML.addGroupPermission(groupname, permission);
     }

}
