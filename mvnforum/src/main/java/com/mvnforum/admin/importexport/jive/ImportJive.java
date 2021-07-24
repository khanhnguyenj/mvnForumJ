/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/jive/ImportJive.java,v 1.22 2009/12/03 11:05:28 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.22 $
 * $Date: 2009/12/03 11:05:28 $
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

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;

import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.admin.CategoryXML;
import com.mvnforum.admin.ImportWebHelper;
import com.mvnforum.admin.importexport.SetParentRule;
import com.mvnforum.common.StatisticsUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.ForumBean;

/**
 * @author Igor Manic
 * @version $Revision: 1.22 $, $Date: 2009/12/03 11:05:28 $
 * <br/>
 * <code>ImportJive</code> class encapsulates processing
 * of Jive's XMLs, and imports all the data into MVN Forum database.
 * For details see {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, Vector, int)}
 * <br/>
 * This class cannot be instantiated.
 */
public final class ImportJive extends ImportWebHelper {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ImportJive.class);
    
    private static Map memberNameMap = new HashMap();
    private static Map memberEmailMap = new HashMap();
    private static Map forumNameMap = new HashMap();

    /** Cannot instantiate. */
    private ImportJive() {
    }
    
    public static Map getMemberNameMap() {
        return memberNameMap;
    }
    
    public static Map getMemberEmailMap() {
        return memberEmailMap;
    }
    
    public static Map getForumNameMap() {
        return forumNameMap;
    }


// =================================================================
// ===================== MAIN PUBLIC METHODS =======================
// =================================================================
    /**
     * This method performs processing of Jive's XML backup file <code>importFile</code>
     * and imports the data into the MVN Forum system. It clears the database
     * and files, does necessary setup (including startup of message output),
     * and calls {@link #processXml(File, Calendar)} to do actual processing.<br/>
     *
     * @param importFile Jive XML file to be imported.
     * @param request Current session's <code>HttpServletRequest</code> object.
     * @param response Current session's <code>HttpServletResponse</code> object.
     * @param logonMemberID MemberID of user who is logged in, and who initiated import process.
     * @param logonMemberName MemberName of user who is logged in, and who initiated import process.
     * @param importTime The moment when import process was started.
     * @param importIP From this IP address admin requested import.
     * @param clearIfError Should it clear/reset the database in case of error.
     * @param otherFieldValues Vector of name/value pairs of other form fields (like
     *                         <code>RootCategory</code>, <code>ForumPasswords</code>)
     * @param messageLevel What messages should be written to output during the process.
     *                     For details see {@link com.mvnforum.MVNForumConstant#MESSAGE_LEVEL_ALL_MESSAGES},
     *                     {@link com.mvnforum.MVNForumConfig#MESSAGE_LEVEL_IMPORTANT_MESSAGES} and
     *                     {@link com.mvnforum.MVNForumConfig#MESSAGE_LEVEL_ONLY_ERRORS}.
     *
     * @exception ImportException If there is an error during the process. See {@link net.myvietnam.mvncore.exception.ImportException}.
     *
     */
    public static void importXml(File importFile,
          HttpServletRequest request, HttpServletResponse response,
          int logonMemberID, String logonMemberName,
          Calendar importTime, String importIP, boolean clearIfError,
          Vector otherFieldValues, int messageLevel)
        throws ImportException {
        
        for (int i=0; i<otherFieldValues.size()-1; i+=2) {
            String name=(String)otherFieldValues.get(i);
            String value=(String)otherFieldValues.get(i+1);
            if (name!=null) {
                if (name.equals("ForumPasswords")) {
                    JiveXML.allForumsPassword=value;
                } else if (name.equals("RootCategory")) {
                    JiveXML.rootCategoryName=value;
                } else if (name.equals("RootCategoryDesc")) {
                    JiveXML.rootCategoryDesc=value;
                } else if (name.equals("JiveGuest")) {
                    JiveXML.guestName=value;
                } else if (name.equals("JiveAdmin")) {
                    JiveXML.adminName=value;
                }
                //else ignore
            }
        }

        try {
            response.setContentType("text/html; charset=utf-8");
            setOutputHtmlWriter(response.getWriter());
            setMessageOutputLevel(messageLevel);
            startHtml(request);
            clearDatabase();
            clearFiles(request.getSession().getServletContext());
        } catch (DatabaseException e) {
            handleFatalError("Database error while clearing previous contents.",
                             e, clearIfError, request);
        } catch (IOException e) {
            handleFatalError("I/O error while clearing previous contents.",
                             e, clearIfError, request);
        }

        try {
            processXml(importFile, importTime);
            updateForumStatistics();
            handleSuccess(request);
        } catch (ImportException e) {
            handleFatalError(e.getMessage(), e.getException(),
                    clearIfError, request);
        } catch (IOException e) {
            handleFatalError("I/O error while reading XML file.",
                             e, clearIfError, request);
        } catch (SAXException e) {
            if (e.getException() == null) {
                handleFatalError("Error while parsing uploaded XML file.",
                                 e, clearIfError, request);
            } else {
                handleFatalError("Error while parsing uploaded XML file.",
                                 e.getException(), clearIfError, request);
            }
        } catch (Exception e) {
            handleFatalError(e.getMessage(), e,
                             clearIfError, request);
        } finally {
           /* Don't delete this XML since this method was maybe started from the
            * command-line, which means this file is not temporary (uploaded)
            * //if ((importFile!=null) && (importFile.exists())) importFile.delete();
            * Anyway, if neccessary, this XML will be deleted in the caller (WebHandler)
            */
        }
    }


// =================================================================
// ================== MAIN PROCESSING XML METHOD ===================
// =================================================================
    /**
     * This method performs actual processing of Jive's XML file <code>inputFile</code>
     * and imports the data into the MVN Forum system.<br/>
     * Don't use this method directly. Instead, you should use
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, Vector, int)}.
     *
     * @param inputFile Jive XML file to be imported.
     * @param importTime The moment when import process was started.
     *
     * @exception IOException If there is an I/O error while processing XML file.
     * @exception SAXException If there is an error while parsing XML file.
     * @exception ImportException If there is an error while adding some default values to database.
     *
     */
    private static void processXml(File inputFile, Calendar importTime)
        throws IOException, SAXException, ImportException {
        
        addImportantMessage("Starting migration of data...");

        //SAXParserFactory factory=SAXParserFactory.newInstance();
        Digester digester=new Digester();
        //should try new Digester(SAXParser), or Digester(XMLReader)
        digester.setValidating(true);
        digester.setNamespaceAware(true);


        /* =================================================================
         * This is the main part of file - XML processing rules for Digester
         * =================================================================
         *
         * note: since SetTopRule is firing the desired method at the end of
         * the XML element matching the pattern, I had to implement my own
         * SetParentRule which does exactly the same thing (calls desired
         * child's method with a parent object as an argument). The difference
         * is that it is firing the "setParent" method at the beginning of the
         * corresponding XML element, thus leaving me the chance to, for example,
         * create messages as they arrive from the XML parser, not waiting the
         * end of the whole document (!!!), because I can't add a message, unless
         * I already added it's parent messages, thread, forum and category.
         */

        /* First, I'll create root object of class JiveXML.
         * It's constructor will create default contents of the database, including
         * admin member (with MemberID=1, MemberName=JiveXML.adminName, MemberPassword="admin").
         * Later, if I find Jive's admin, I'll just rewrite this default's admin data.
         * JiveXML constructor will also create a root category,
         * to which I am going to put all forums from Jive.
         */
        digester.addObjectCreate("Jive", JiveXML.class);
        digester.addSetProperties("Jive", "xmlversion", "jiveXmlVersion");
        digester.addSetProperties("Jive", "exportDate", "jiveExportDate");


        /* For each Jive user, create MVN Forum member. */
        digester.addObjectCreate("Jive/UserList/User", JiveUserXML.class);
        //todo Igor: check what this is about: digester.addSetProperties("Jive/UserList/User", "id", "userId");
        digester.addCallMethod("Jive/UserList/User", "addJiveUser", 11);
        digester.addCallParam("Jive/UserList/User/Username", 0);
        digester.addCallParam("Jive/UserList/User/Password", 1);
        digester.addCallParam("Jive/UserList/User/Email", 2);
        digester.addCallParam("Jive/UserList/User/Email", 3, "visible");
        digester.addCallParam("Jive/UserList/User/Name", 4);
        digester.addCallParam("Jive/UserList/User/Name", 5, "visible");
        digester.addCallParam("Jive/UserList/User/CreationDate", 6);
        digester.addCallParam("Jive/UserList/User/ModifiedDate", 7);
        digester.addCallParam("Jive/UserList/User/RewardPoints", 8);
        ImportJive.addMessage("All Jive/UserList/User/PropertyList/Property will be ignored.");


        /* For each Jive group, create MVN Forum group.
         * -> Jive/GroupList/Group/PropertyList/Property is not imported
         * -> Jive/GroupList/Group/AdministratorList/Username - only the
         *    first one will be imported (as GroupOwner)
         */
        digester.addObjectCreate("Jive/GroupList/Group", JiveGroupXML.class);
        /* addJiveGroup will be fired at the end of the <Group> XML element. */
        digester.addCallMethod("Jive/GroupList/Group", "addJiveGroup");
        //todo Igor: check what this is about: digester.addSetProperties("Jive/GroupList/Group", "id", "groupId");
        digester.addCallMethod("Jive/GroupList/Group/Name", "setGroupName", 0);
        digester.addCallMethod("Jive/GroupList/Group/Description", "setGroupDescription", 0);
        digester.addCallMethod("Jive/GroupList/Group/CreationDate", "setGroupCreationDate", 0);
        digester.addCallMethod("Jive/GroupList/Group/ModifiedDate", "setGroupModifiedDate", 0);
        digester.addCallMethod("Jive/GroupList/Group/AdministratorList/Username", "setGroupOwnerName", 0);
        ImportJive.addMessage("All Jive/GroupList/Group/PropertyList/Property will be ignored.");
        ImportJive.addMessage("All Jive/GroupList/Group/AdministratorList/Username will be ignored, except for the first one (to become GroupOwner).");

        /* addJiveGroupMember must first call addJiveGroup to create this group,
         * since it's not yet created (didn't get to the end of the <Group> XML element).
         */
        digester.addCallMethod("Jive/GroupList/Group/MemberList/Username", "addJiveGroupMember", 0);


        /* For each Jive forum, create MVN Forum forum.
         * -> Jive/ForumList/Forum/FilterList/Filter is not imported
         * -> Jive/ForumList/Forum/PropertyList/Property is not imported
         */
        digester.addObjectCreate("Jive/ForumList/Forum", JiveForumXML.class);
        /* addJiveForum will be fired at the end of the <Forum> XML element. */
        digester.addCallMethod("Jive/ForumList/Forum", "addJiveForum");
        //todo Igor: check what this is about: digester.addSetProperties("Jive/ForumList/Forum", "id", "forumId");
        digester.addCallMethod("Jive/ForumList/Forum/Name", "setForumName", 0);
        digester.addCallMethod("Jive/ForumList/Forum/Description", "setForumDescription", 0);
        digester.addCallMethod("Jive/ForumList/Forum/CreationDate", "setForumCreationDate", 0);
        digester.addCallMethod("Jive/ForumList/Forum/ModifiedDate", "setForumModifiedDate", 0);
        ImportJive.addMessage("All Jive/ForumList/Forum/FilterList/Filter will be ignored.");
        ImportJive.addMessage("All Jive/ForumList/Forum/PropertyList/Property will be ignored.");

        /* addJiveForumUser must first call addJiveForum to create this forum,
         * since it's not yet created (didn't get to the end of the <Forum> XML element).
         */
        digester.addCallMethod("Jive/ForumList/Forum/PermissionList/UserPermissionList/UserPermission", "addJiveForumUser", 3);
        digester.addCallParam("Jive/ForumList/Forum/PermissionList/UserPermissionList/UserPermission", 0, "usertype");
        digester.addCallParam("Jive/ForumList/Forum/PermissionList/UserPermissionList/UserPermission", 1, "username");
        digester.addCallParam("Jive/ForumList/Forum/PermissionList/UserPermissionList/UserPermission", 2, "permission");

        /* addJiveForumGroup must first call addJiveForum to create this forum,
         * since it's not yet created (didn't get to the end of the <Forum> XML element).
         */
        digester.addCallMethod("Jive/ForumList/Forum/PermissionList/GroupPermissionList/GroupPermission", "addJiveForumGroup", 2);
        digester.addCallParam("Jive/ForumList/Forum/PermissionList/GroupPermissionList/GroupPermission", 0, "groupname");
        digester.addCallParam("Jive/ForumList/Forum/PermissionList/GroupPermissionList/GroupPermission", 1, "permission");


        /* For each Jive thread, create MVN Forum thread.
         * -> Jive/ForumList/Forum/ThreadList/Thread/PropertyList/Property is not imported
         */
        digester.addObjectCreate("Jive/ForumList/Forum/ThreadList/Thread", JiveThreadXML.class);
        //digester.addSetTop("Jive/ForumList/Forum/ThreadList/Thread", "setParentForum");
        SetParentRule threadParentRule = new SetParentRule("setParentForum");
        digester.addRule("Jive/ForumList/Forum/ThreadList/Thread", threadParentRule);
        /* addJiveThread will be fired at the end of the <Thread> XML element. */
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread", "addJiveThread");
        //todo Igor: check what this is about: digester.addSetProperties("Jive/ForumList/Forum/ThreadList/Thread", "id", "threadId");
        /* this doesn't work here since it will be fired only after we start processing first message:
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/Message/Username", "setUsername", 0);
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/Message/Subject", "setTopic", 0);
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/Message/Body", "setBody", 0); */

        /* We consider the thread is created by the author of the first message. But, when
         * we come to <Message> element, we'll already have JiveMessageXML on top of Digester
         * stack. Thus, here we'll handle thread parameters we know now, and other parameters
         * will be known and given to JiveThreadXML object when we come to processing the
         * first Message of the thread (which always exists).
         * This applies to rules matching Message/Username, Message/Subject and Message/Body.
         * They will not only set corresponding JiveMessageXML fields, but also parent
         * JiveThreadXML fields if it is the first message in a thread.
         * One more problem is about thread watches - these Watch elements are parsed before
         * the first message. But, to enter thread watch into the database, we need to have
         * already created thread. And that's not possible before we process the first message.
         * So, all thread watches will be remembered in JiveThreadXML, and when we process the
         * first message, we'll then setup all the fields of JiveThreadXML, create the thread,
         * and then add all thread watches we remembered.
         */
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/CreationDate", "setCreationDate", 0);
        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/ModifiedDate", "setModifiedDate", 0);
        ImportJive.addMessage("All Jive/ForumList/Forum/ThreadList/Thread/PropertyList/Property will be ignored.");

        digester.addCallMethod("Jive/ForumList/Forum/ThreadList/Thread/WatchList/Watch", "addJiveThreadWatch", 3);
        digester.addCallParam("Jive/ForumList/Forum/ThreadList/Thread/WatchList/Watch", 0, "type");
        digester.addCallParam("Jive/ForumList/Forum/ThreadList/Thread/WatchList/Watch", 1, "expirable");
        digester.addCallParam("Jive/ForumList/Forum/ThreadList/Thread/WatchList/Watch/Username", 2);


        /* For each Jive message, create MVN Forum post.
         * -> * /Message/PropertyList/Property is not imported
         */
        digester.addObjectCreate("*/Message", JiveMessageXML.class);
        //digester.addSetTop("*/Message", "setParentThreadOrPost");
        SetParentRule messageParentRule = new SetParentRule("setParentThreadOrPost");
        digester.addRule("*/Message", messageParentRule);
        /* addJiveMessage will be fired at the end of the <Message> XML element. */
        digester.addCallMethod("*/Message", "addJiveMessage");
        //todo Igor: check what this is about: digester.addSetProperties("*/Message", "id", "postId");
        digester.addCallMethod("*/Message/Subject", "setPostSubject", 0);
        digester.addCallMethod("*/Message/Body", "setPostBody", 0);
        digester.addCallMethod("*/Message/Username", "setPostUsername", 0);
        digester.addCallMethod("*/Message/CreationDate", "setPostCreationDate", 0);
        digester.addCallMethod("*/Message/ModifiedDate", "setPostModifiedDate", 0);
        ImportJive.addMessage("All */Message/PropertyList/Property will be ignored.");


        /* At this point, on top of stack we have only the root JiveXML object */

        digester.addCallMethod("Jive/UserPermissionList/UserPermission", "addJiveUserPermission", 3);
        digester.addCallParam("Jive/UserPermissionList/UserPermission", 0, "usertype");
        digester.addCallParam("Jive/UserPermissionList/UserPermission", 1, "username");
        digester.addCallParam("Jive/UserPermissionList/UserPermission", 2, "permission");

        /* addJiveForumGroup must first call addJiveForum to create this forum,
         * since it's not yet created (didn't get to the end of the <Forum> XML element).
         */
        digester.addCallMethod("Jive/GroupPermissionList/GroupPermission", "addJiveGroupPermission", 2);
        digester.addCallParam("Jive/GroupPermissionList/GroupPermission", 0, "groupname");
        digester.addCallParam("Jive/GroupPermissionList/GroupPermission", 1, "permission");

        /* ==================================================================
         * This was the main part of file - XML processing rules for Digester
         * ==================================================================
         */

        digester.parse(inputFile);

        //now add some default permissions for guests and registered members (if they don't exist yet)
        try {
            for (int j=0; j<JiveXML.addDefaultPermissionsToGuests.length; j++) {
//                try {
                    JiveXML.addGuestMemberPermission(Integer.toString(JiveXML.addDefaultPermissionsToGuests[j]));
//                } catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
//                }
            }

            for (int j=0; j<JiveXML.addDefaultPermissionsToMembers.length; j++) {
                try {
                    JiveXML.addRegisteredMembersGroupPermission(Integer.toString(JiveXML.addDefaultPermissionsToMembers[j]));
                } catch (DuplicateKeyException e) {
                    /* Ignore if we doubled some permissions.
                     * Because we convert each Jive permission into the list of
                     * MVN Forum permissions (can be more than one), some permissions
                     * could be generated twice, or more times.
                     */
                }
            }
        } catch (DatabaseException e) {
            log.error("Database error while adding default permissions.", e);
            throw new ImportException("Error while adding default permissions to guests and registered members.", e);
        } catch (CreateException e) {
            log.error("Create data error while adding default permissions.", e);
            throw new ImportException("Error while adding default permissions to guests and registered members.", e);
        } catch (ForeignKeyNotFoundException e) {
            log.error("Foreign key not found error while adding default permissions.", e);
            throw new ImportException("Error while adding default permissions to guests and registered members.", e);
        }

        if (JiveXML.foundAdminUser) {
            ImportJive.addImportantMessage("IMPORTANT: Jive user \""+JiveXML.adminName+
                          "\" kept all his data (including password), and has SYSTEM_ADMIN "+
                          "permissions - even if he didn't have them in Jive!!!");
        } else {
            ImportJive.addImportantMessage("SYSTEM_ADMIN user with a name \""+
                          JiveXML.adminName+"\" and password \"admin\" was created. "+
                          "For your security, you should first change that password.");
        }
    }

    /**
     * Adds <code>message</code> to the output stream that was setup in
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, Vector, int)}.
     * <br/>This method was made public to be available to Jive XML processing classes.
     *
     * @param message Message to be written to output.
     * @see com.mvnforum.admin.ImportWebHelper#addMessage(java.lang.String)
     *
     */
    public static void addMessage(String message) {
        ImportWebHelper.addMessage(message);
    }

    /**
     * Adds important (bold) <code>message</code> to the output stream that was setup in
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, Vector, int)}.
     * <br/>This method was made public to be available to Jive XML processing classes.
     *
     * @param message Message to be written to output.
     * @see com.mvnforum.admin.ImportWebHelper#addImportantMessage(java.lang.String)
     *
     */
    public static void addImportantMessage(String message) {
        ImportWebHelper.addImportantMessage(message);
    }

    public static void createDefaultGuestMember() throws DuplicateKeyException,
    ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {
        if ((JiveXML.guestName==null) || (JiveXML.guestName.length()<=0)) {
            JiveXML.guestName=MVNForumConfig.getDefaultGuestName();
        }
        //addImportantMessage("Adding \""+JiveXML.guestName+"\" virtual guest member.");
        ImportWebHelper.createDefaultGuestMember(JiveXML.guestName);
    }

    public static void createDefaultAdminMember() throws ObjectNotFoundException,
    CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        if ((JiveXML.adminName==null) || (JiveXML.adminName.length()<=0)) {
            JiveXML.adminName="Admin";
        }
        //addImportantMessage("Adding \""+JiveXML.adminName+"\" member with SYSTEM_ADMIN permissions.");
        ImportWebHelper.createDefaultAdminMember(JiveXML.adminName);
    }

    public static void createDefaultRegisteredMembersGroup() throws CreateException,
    DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        //addMessage("Adding default group of \"Registered Members\".");
        ImportWebHelper.createDefaultRegisteredMembersGroup(JiveXML.adminName);
    }

    public static void createDefaultRanks() 
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException {
        
        //addMessage("Adding default rank titles.");
        ImportWebHelper.createDefaultRanks();
    }

    public static void createRootCategory() throws CreateException,
    DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        /* Each MVN category can have any number of it's own forums.
         * Also, MVN supports (sub)categories in each category.
         * Jive's forums are the same as MVN's forums, but Jive doesn't have categories at all.
         * That means that all Jive's forums will be translated into MVN's forums that
         * have to be put down into the one and only MVN category I must create here.
         */
        addMessage("Adding category \""+JiveXML.rootCategoryName+"\" where all Jive forums will be stored in.");
        CategoryXML categoryXML=new CategoryXML();
        categoryXML.addCategory(JiveXML.rootCategoryName, JiveXML.rootCategoryDesc,
                    null /*categoryCreationDate*/, null /*categoryModifiedDate*/,
                    null /*categoryOrder*/, null /*categoryOption*/, null /*categoryStatus*/);
        JiveXML.rootCategoryID=categoryXML.getCategoryID();
        addImportantMessage("All ForumPasswords will be set to \""+JiveXML.allForumsPassword+"\".");
    }

    private static void handleSuccess(HttpServletRequest request) {
        addSuccessMessage(request);
        endHtml();
    }

    //it's possible I called this method with e==null
    private static void handleFatalError(String message, Exception e,
             boolean clearIfError, HttpServletRequest request)
        throws ImportException {
        
        if (e == null) {
            log.error(message);
        } else {
            log.error(message, e);
        }
        if ((e==null) || (e.getMessage()==null)) {
            addErrorMessage(message);
        } else {
            addErrorMessage(message + "<br/>Cause: " + e.getMessage());
        }
        //try to clear the database and rollback to valid empty state with admin member
        ImportWebHelper.addFinalErrorHandling(request, clearIfError);
        endHtml();
        if (e == null) {
            throw new ImportException(message);
        } else {
            throw new ImportException(message, e);
        }
    }

    public static String getAvailableMemberName(String memberName) throws DatabaseException {
        
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
        } catch (ObjectNotFoundException e) {
            return memberName;
        }
        
        int index = 1;
        return getAutoIncreaseMemberName(memberName, index);
    }
    
    private static String getAutoIncreaseMemberName(String memberName, int index) throws DatabaseException {
        String newMemberName = memberName + index;
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(newMemberName);
            return getAutoIncreaseMemberName(memberName, ++index);
        } catch (ObjectNotFoundException e) {
            return newMemberName;
        }
    }

    public static String getAvailableMemberEmail(String memberEmail) throws DatabaseException {
        
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(memberEmail);
        } catch (ObjectNotFoundException e) {
            return memberEmail;
        }
        
        int index = 1;
        return getAutoIncreaseMemberEmail(memberEmail, index);
    }
    
    private static String getAutoIncreaseMemberEmail(String memberEmail, int index) throws DatabaseException {
        String newMemberEmail = memberEmail + index;
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(newMemberEmail);
            return getAutoIncreaseMemberEmail(memberEmail, ++index);
        } catch (ObjectNotFoundException e) {
            return newMemberEmail;
        }
    }

    public static String getAvailableForumName(String forumName, int categoryID) throws DatabaseException {
        
        try {
            DAOFactory.getForumDAO().findByAlternateKey_ForumName_CategoryID(forumName, categoryID);
        } catch (ObjectNotFoundException e) {
            return forumName;
        }
        
        int index = 1;
        return getAutoIncreaseForumName(forumName, categoryID, index);
    }
    
    private static String getAutoIncreaseForumName(String forumName, int categoryID, int index) throws DatabaseException {
        String newForumName = forumName + index;
        try {
            DAOFactory.getForumDAO().findByAlternateKey_ForumName_CategoryID(newForumName, categoryID);
            return getAutoIncreaseForumName(forumName, categoryID, ++index);
        } catch (ObjectNotFoundException e) {
            return newForumName;
        }
    }
    
    public static String getMemberNameFromMap(String memberName) {
        if (memberNameMap.containsKey(memberName)) {
            memberName = (String) ImportJive.getMemberNameMap().get(memberName);
        }
        return memberName;
    }
    
    public static void updateForumStatistics() throws DatabaseException, ObjectNotFoundException {
        Collection forums = DAOFactory.getForumDAO().getForums();
        for (Iterator iter = forums.iterator(); iter.hasNext(); ) {
            ForumBean forum = (ForumBean) iter.next();
            StatisticsUtil.updateForumStatistics(forum.getForumID());
        }
    }
}
