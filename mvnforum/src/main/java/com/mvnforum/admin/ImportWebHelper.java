/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ImportWebHelper.java,v 1.26 2009/12/03 11:19:36 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.26 $
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
package com.mvnforum.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.db.AttachmentDAO;
import com.mvnforum.db.CategoryDAO;
import com.mvnforum.db.FavoriteThreadDAO;
import com.mvnforum.db.ForumDAO;
import com.mvnforum.db.GroupForumDAO;
import com.mvnforum.db.GroupPermissionDAO;
import com.mvnforum.db.GroupsDAO;
import com.mvnforum.db.MemberDAO;
import com.mvnforum.db.MemberForumDAO;
import com.mvnforum.db.MemberGroupDAO;
import com.mvnforum.db.MemberPermissionDAO;
import com.mvnforum.db.MessageFolderDAO;
import com.mvnforum.db.PostDAO;
import com.mvnforum.db.RankDAO;
import com.mvnforum.db.ThreadDAO;
import com.mvnforum.db.WatchDAO;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.SearchService;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

/**
 * @author Igor Manic
 * @version $Revision: 1.26 $, $Date: 2009/12/03 11:19:36 $
 * <br/>
 * <code>ImportWebHelper</code> todo Igor: enter description
 *
 */
public class ImportWebHelper {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ImportWebHelper.class);

    protected ImportWebHelper() {
    }

    private static Map memberNameMap = new HashMap();

// =================================================================
// ======== UTILITY METHODS FOR CLEARING PREVIOUS CONTENTS =========
// =================================================================
   /* I need this method because db package doesn't allow me to enter some
    * values I need to (for example, to ensure creating predefined groups
    * having GroupIDs I want them to be (like in .sql script)
    */
    protected static int execUpdateQuery(String query) throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(query);
            return statement.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ImportWebHelper.execUpdateQuery.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }

    private static void clearTable(String tableName) throws DatabaseException {
        execUpdateQuery("DELETE FROM " + tableName);
    }

    private static void emptyDirectory(File dir) throws IOException {

        if (dir.isFile()) {
            log.error("Called emptyDirectory on a file \""+dir.getAbsolutePath()+"\".");
            throw new IOException("IOException: not a directory.");
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    file.delete();
                } else {
                    emptyDirectory(file);
                }
            }
        }
    }

    private static void emptyDirectory(String path) throws IOException {

        emptyDirectory(new File(path));
    }

    protected static void clearFiles(ServletContext context) throws IOException {

        addImportantMessage("Deleting previous files...");

        SearchService service = MvnForumServiceFactory.getMvnForumService().getSearchService();
        if (service.savePostOnDisk()) {
            emptyDirectory(MVNForumConfig.getSearchPostIndexDirName());
        }
        if (service.saveMemberOnDisk()) {
            emptyDirectory(MVNForumConfig.getSearchMemberIndexDirName());
        }

        emptyDirectory(MVNForumConfig.getAttachmentDir());
        emptyDirectory(context.getRealPath(MVNForumGlobal.UPLOADED_AVATAR_DIR));
        //DO NOT DELETE OR EMPTY getTempDir() BECAUSE IT MAY CONTAIN UPLOADED IMPORT FILE WHICH IS GOING TO BE PROCESSED
    }

    protected static void clearDatabase() throws DatabaseException {

        addImportantMessage("Clearing previous database contents...");
        clearTable(RankDAO.TABLE_NAME);
        clearTable(FavoriteThreadDAO.TABLE_NAME);

        clearTable(AttachmentDAO.TABLE_NAME);
        clearTable(WatchDAO.TABLE_NAME);
        clearTable(PostDAO.TABLE_NAME);
        clearTable(ThreadDAO.TABLE_NAME);

        clearTable(MemberForumDAO.TABLE_NAME);
        clearTable(GroupForumDAO.TABLE_NAME);
        clearTable(ForumDAO.TABLE_NAME);
        clearTable(CategoryDAO.TABLE_NAME);

        clearTable(MemberGroupDAO.TABLE_NAME);
        clearTable(GroupPermissionDAO.TABLE_NAME);
        clearTable(GroupsDAO.TABLE_NAME);
        clearTable(MemberPermissionDAO.TABLE_NAME);
        clearTable(MessageFolderDAO.TABLE_NAME);
        clearTable(MemberDAO.TABLE_NAME);
    }


// =================================================================
// =============== CREATING DEFAULT DATABASE RECORDS ===============
// =================================================================
   public static void createDefaultContents() throws DuplicateKeyException,
   ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {
       createDefaultAdminMember();
       createDefaultGuestMember();
       createDefaultGuestGroup();
       createDefaultRegisteredMembersGroup();
       createDefaultRanks();
   }

   public static void createDefaultGuestMember()
       throws DuplicateKeyException, ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {
       createDefaultGuestMember(MVNForumConfig.getDefaultGuestName());
   }

   public static void createDefaultGuestMember(String guestName) throws DuplicateKeyException,
    ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException {
        if ((guestName == null) || (guestName.length() <= 0)) {
            guestName=MVNForumConfig.getDefaultGuestName();
        }
        addImportantMessage("Adding virtual guest member \""+guestName+"\"...");
        //todo Igor: add an array of individual perms to the previous message
        //e.g.: "... with READ_POST, ADD_POST and ADD_THREAD permissions.");
        MemberXML memberXML = new MemberXML();
        memberXML.addMember(Integer.toString(MVNForumConstant.MEMBER_ID_OF_GUEST), guestName,
              "N/A"/*MemberPassword - not used - can't be empty*/,
              "N/A"/*MemberFirstEmail - not used - can't be empty*/,
              "N/A"/*MemberEmail - not used - can't be empty*/,
              "0", "1", "127.0.0.1", "127.0.0.1", "0", "0",
              null /*memberCreationDate*/, null /*memberModifiedDate*/, null /*memberExpireDate*/, null /*memberExpireDate*/,
              null /*memberLastLogon*/, "0", "0", "" /*memberActivateCode*/,
              "" /*memberTempPassword*/, "0" /*memberMessageCount*/,
              "0" /*memberMessageOption*/, "10" /*memberPostsPerPage*/,
              "0" /*memberWarnCount*/, "0" /*memberVoteCount*/, "0" /*memberVoteTotalStars*/,
              "0" /*memberRewardPoints*/, "" /*memberTitle*/, "0" /*memberTimeZone*/,
              "" /*memberSignature*/, "" /*memberAvatar*/, "" /*memberSkin*/,
              "" /*memberLanguage*/, guestName /*memberFirstname*/, "" /*memberLastname*/,
              "1" /*memberGender*/, null /*memberBirthday*/, "" /*memberAddress*/,
              "" /*memberCity*/, "" /*memberState*/, "" /*memberCountry*/, "" /*memberPhone*/,
              "" /*memberMobile*/, "" /*memberFax*/, "" /*memberCareer*/, "" /*memberHomepage*/);
        memberXML.addMemberPermission(Integer.toString(MVNForumPermission.PERMISSION_LIMITED_USER));
        //todo Igor: replace previous permission with an array of individual perms
    }

    public static void createDefaultAdminMember() throws ObjectNotFoundException,
    CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        createDefaultAdminMember("Admin");
    }

    public static void createDefaultAdminMember(String adminName) throws ObjectNotFoundException,
    CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        if ((adminName == null) || (adminName.length() <= 0)) {
            adminName = "Admin";
        }
        addImportantMessage("Adding system administrator \""+adminName+"\"...");
        MemberXML memberXML = new MemberXML();

        memberXML.addMember(Integer.toString(MVNForumConstant.MEMBER_ID_OF_ADMIN),
                  adminName, "ISMvKXpXpadDiUoOSoAfww==",
                  "admin@yourdomain.com", "admin@yourdomain.com",
                  "0", "1",
                  "127.0.0.1", "127.0.0.1",
                  "0", "0",
                  null /*memberCreationDate*/, null /*memberModifiedDate*/,null /*memberExpireDate*/,null /*memberPasswordExpireDate*/,
                  null /*memberLastLogon*/, "0", "0", "" /*memberActivateCode*/,
                  "" /*memberTempPassword*/, "0" /*memberMessageCount*/,
                  "0" /*memberMessageOption*/, "10" /*memberPostsPerPage*/,
                  "0" /*memberWarnCount*/, "0" /*memberVoteCount*/, "0" /*memberVoteTotalStars*/,
                  "0" /*memberRewardPoints*/, "" /*memberTitle*/, "0" /*memberTimeZone*/,
                  "" /*memberSignature*/, "" /*memberAvatar*/, "" /*memberSkin*/,
                  "" /*memberLanguage*/, adminName /*memberFirstname*/, "" /*memberLastname*/,
                  "1" /*memberGender*/, null /*memberBirthday*/, "" /*memberAddress*/,
                  "" /*memberCity*/, "" /*memberState*/, "" /*memberCountry*/, "" /*memberPhone*/,
                  "" /*memberMobile*/, "" /*memberFax*/, "" /*memberCareer*/, "" /*memberHomepage*/);
        memberXML.addMemberPermission(Integer.toString(MVNForumPermission.PERMISSION_SYSTEM_ADMIN));
        memberXML.addMessageFolder("Inbox", "0" /*folderOrder*/,
                   null /*folderCreationDate*/, null /*folderModifiedDate*/);
        memberXML.addMessageFolder("Sent", "0" /*folderOrder*/,
                   null /*folderCreationDate*/, null /*folderModifiedDate*/);
    }

    public static void createDefaultRegisteredMembersGroup() throws CreateException,
    DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        createDefaultRegisteredMembersGroup(null);
    }

    public static void createDefaultRegisteredMembersGroup(String groupOwnerName) throws CreateException,
    DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        addImportantMessage("Adding default virtual group of all registered members...");
        GroupXML groupXML = new GroupXML();
        groupXML.addGroup(Integer.toString(MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS),
                          groupOwnerName, "Registered Members",
                          "All registered users belong to this group.",
                          null/*GroupOption*/,
                          null/*GroupCreationDate*/, null/*GroupModifiedDate*/);
        groupXML.addGroupPermission(Integer.toString(MVNForumPermission.PERMISSION_NORMAL_USER));
        //todo Igor: replace previous permission with an array of individual perms
    }

    public static void createDefaultGuestGroup()
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        addImportantMessage("Adding default virtual group of all guest members...");
        GroupXML groupXML = new GroupXML();
        groupXML.addGroup(Integer.toString(MVNForumConstant.GROUP_ID_OF_GUEST),
                null, "Guest",
                "All anonymous users belong to this group.",
                null/*GroupOption*/,
                null/*GroupCreationDate*/, null/*GroupModifiedDate*/);
        groupXML.addGroupPermission(Integer.toString(MVNForumPermission.PERMISSION_LIMITED_USER));
    }

    public static void createDefaultRanks()
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException {

        addImportantMessage("Adding default rank titles \"Stranger\", \"Newbie\", \"Member\" and \"Advanced Member\"...");
        (new RankXML()).addRank("0", "0", "Stranger", "", "0", "0");
        (new RankXML()).addRank("20", "0", "Newbie", "", "0", "0");
        (new RankXML()).addRank("50", "0", "Member", "", "0", "0");
        (new RankXML()).addRank("100", "0", "Advanced Member", "", "0", "0");
    }

// =================================================================
// ===== PRINTING STATUS AND ERROR MESSAGES TO THE OUTPUT HTML =====
// =================================================================
    protected static void setOutputHtmlWriter(PrintWriter outWriter) {
        ImportWebHandler.setOutputHtmlWriter(outWriter);
    }

    protected static void setMessageOutputLevel(int messageLevel) {
        ImportWebHandler.setMessageOutputLevel(messageLevel);
    }

    protected static void startHtml(HttpServletRequest request) {
        ImportWebHandler.startHtml(request);
    }

    protected static void endHtml() {
        ImportWebHandler.endHtml();
    }

    protected static void addMessage(String message) {
        ImportWebHandler.addMessage(message);
    }

    protected static void addErrorMessage(String message) {
        ImportWebHandler.addErrorMessage(message);
    }

    protected static void addSuccessMessage(HttpServletRequest request) {
        ImportWebHandler.addSuccessMessage(request);
    }

    protected static void addImportantMessage(String message) {
        ImportWebHandler.addImportantMessage(message);
    }

    protected static void addFinalErrorHandling(HttpServletRequest request, boolean clearIfError) {
        ImportWebHandler.addFinalErrorHandling(request, clearIfError);
    }

}
