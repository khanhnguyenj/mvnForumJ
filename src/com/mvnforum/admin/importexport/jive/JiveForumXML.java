/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/jive/JiveForumXML.java,v 1.14 2009/12/02 10:56:23 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/12/02 10:56:23 $
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

import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.*;

import com.mvnforum.admin.*;

/**
 * @author Igor Manic
 * @version $Revision: 1.14 $, $Date: 2009/12/02 10:56:23 $
 * <br/>
 * <code>JiveForumXML</code> class encapsulates processing of
 * forums' definitions found in the Jive XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>ForumXML</code> and other neccessary classes in order to perform
 * the actual creation of a forum, as well as assigning forum-specific
 * premissions to members and groups.
 */
public class JiveForumXML {

    private ForumXML forumXML = null;
    private boolean forumCreated = false;

    public JiveForumXML() {
        forumXML = new ForumXML();
        forumCreated = false;
    }

    public void setForumID(String id) {
        forumXML.setForumID(id);
    }

    /**
     * This method simply calls <code>setForumID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setForumID()</code> as a setter
     * method for <code>forumID</code> property).
     */
    public void setForumId(String id) {
        setForumID(id);
    }

    public int getForumID() { 
        return forumXML.getForumID(); 
    }

    public int getParentCategoryID() {
        return forumXML.getParentCategoryID();
    }

    private String forumName = null;
    public void setForumName(String value) throws CreateException {
        if ( (value==null) || (value.equals("")) ) {
            throw new CreateException("Cannot create a forum with an empty ForumName.");
        }
        this.forumName = value;
    }

    private String forumDesc = null;
    public void setForumDescription(String value) {
        this.forumDesc = value;
    }

    private String forumCreationDate = null;
    public void setForumCreationDate(String value) {
        this.forumCreationDate = value;
    }

    private String forumModifiedDate = null;
    public void setForumModifiedDate(String value) {
        this.forumModifiedDate = value;
    }

    public void addJiveForum()
        throws CreateException, DuplicateKeyException, ObjectNotFoundException,
        DatabaseException, ForeignKeyNotFoundException {
        /* First check if the digester already called this method.
         * It will happen even under normal circumstances, if this forum has
         * subelements that need it already be defined, so they first call
         * this method to create forum before creating data that refer him.
         */
        if (forumCreated) {
            return;
        }
        //todo Igor: add checking of JiveXML.rootCategoryID to know whether the root category is created or not
        if ( (forumName==null) || (forumName.equals("")) ) {
            throw new CreateException("Cannot create a forum with an empty ForumName.");
        } else {
            forumXML.setParentCategoryID(JiveXML.rootCategoryID);
            String forumPassword=JiveXML.allForumsPassword;
            
            String logMessage = "Adding forum \""+forumName+"\"";
            String newForumName = forumName;
            newForumName = ImportJive.getAvailableForumName(newForumName, getParentCategoryID());
            
            if (forumName.equals(newForumName) == false) {
                logMessage += " with new forum name \"" + newForumName + "\"";
                ImportJive.getMemberNameMap().put(forumName, newForumName);
                forumName = newForumName;
            }
            logMessage += ".";
            
            ImportJive.addMessage(logMessage);
            forumXML.addForum(null/*lastPostMemberName*/, forumName, forumDesc,
                              forumCreationDate, forumModifiedDate, null/*forumLastPostDate*/,
                              null/*forumOrder*/, null/*forumType*/, null/*forumFormatOption*/,
                              null/*forumOption*/, null/*forumStatus*/,
                              null/*forumModerationMode*/, forumPassword,
                              null/*forumThreadCount*/, null/*forumPostCount*/);
            forumCreated=true;

            JiveXML.addedForum(forumXML); //update parents
        }
    }

    public void updateAddedThread(ThreadXML threadXML) 
        throws ObjectNotFoundException, DatabaseException {
        
        if ((!forumCreated) || (forumXML.getForumID()<0)) {
            return; //todo Igor: process this error
        }
        forumXML.increaseThreadCount();
        JiveXML.addedThread(threadXML); //update parent category
    }

    public void updateAddedPost(PostXML postXML, String postUsername, Timestamp postCreationDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ((!forumCreated) || (forumXML.getForumID()<0)) {
            return; //todo Igor: process this error
        }
        postUsername = ImportJive.getMemberNameFromMap(postUsername);
        forumXML.increasePostCount();
        forumXML.updateLastPostMemberName(postUsername);
        forumXML.updateLastPostDate(postCreationDate);
        JiveXML.addedPost(postXML); //update parent category
    }

    public void addJiveForumUser(String usertype, String username, String jivePermission)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (!forumCreated) || (forumXML.getForumID()<0) ) {
            addJiveForum();
        }
        if (usertype == null) {
            throw new CreateException("Not enough data to create a member forum-specific permission.");

        } 
        
        if (usertype.equalsIgnoreCase("ANONYMOUS")) {
            int[] perms = JiveXML.convertMemberForumPermission(jivePermission);
            ImportJive.addMessage("Adding forum-specific permissions for guests.");
            for (int j=0; j<perms.length; j++) {
                //try {
                    forumXML.addGuestMemberForumPermission(Integer.toString(perms[j]));
                //} catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
                //}
            }

        } else if (usertype.equalsIgnoreCase("REGISTERED_USERS")) {
            int[] perms = JiveXML.convertGroupForumPermission(jivePermission);
            ImportJive.addMessage("Adding forum-specific permissions for registered members.");
            for (int j=0; j<perms.length; j++) {
                //try {
                    forumXML.addRegisteredMembersGroupForumPermission(Integer.toString(perms[j]));
                //} catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
                //}
            }

        } else if (usertype.equalsIgnoreCase("USER")) {
            username = ImportJive.getMemberNameFromMap(username);
            int[] perms = JiveXML.convertMemberForumPermission(jivePermission);
            ImportJive.addMessage("Adding forum-specific permissions for member \""+username+"\".");
            for (int j=0; j<perms.length; j++) {
//                try {
                    forumXML.addMemberForumPermission(username, Integer.toString(perms[j]));
//                } catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
//                }
            }
        } else {
            throw new CreateException("Invalid usertype. This Jive user forum-specific permission is ignored.");
        }
    }

    public void addJiveForumGroup(String groupname, String jivePermission)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        
        if ( (!forumCreated) || (forumXML.getForumID()<0) ) {
            addJiveForum();
        }
        if ( (groupname==null) || (groupname.equals("")) ) {
            throw new CreateException("Not enough data to create a group permission.");
        }
        
        int[] perms = JiveXML.convertGroupForumPermission(jivePermission);
        ImportJive.addMessage("Adding forum-specific permissions for group \""+groupname+"\".");
        for (int j=0; j<perms.length; j++) {
            //try {
                forumXML.addGroupForumPermission(groupname,  Integer.toString(perms[j]));
            //} catch (DuplicateKeyException e) {
                /* Ignore if we doubled some permissions.
                 * Because we convert each Jive permission into the list of
                 * MVN Forum permissions (can be more than one), some permissions
                 * could be generated twice, or more times.
                 */
            //}
        }
    }

}
