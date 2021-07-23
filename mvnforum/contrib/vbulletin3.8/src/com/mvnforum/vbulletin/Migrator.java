/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/vbulletin/Migrator.java,v 1.12 2010/02/11 03:01:10 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2010/02/11 03:01:10 $
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
package com.mvnforum.vbulletin;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import net.myvietnam.enterprise.db.MVNCoreEnterpriseDAOFactory;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.service.BinaryStorageService;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.db.*;
import com.mvnforum.util.*;
import com.mvnforum.vbulletin.db.*;
import com.mvnforum.vbulletin.db.DAOFactory;

/**
 * The Class Migrator.
 */
public class Migrator {
    
    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(Migrator.class);

    /** The ADMI n_ id. */
    private static final int ADMIN_ID = 1;
    
    /** The Constant GUEST_NAME. */
    public static final String GUEST_NAME = "Guest";
    
    /** The Constant UNTITLED_THREAD. */
    public static final String UNTITLED_THREAD = "Untitled thread";
    
    /** The Constant UNTITLED_POST. */
    public static final String UNTITLED_POST = "Untitled post";
    
    /** The message folders map. */
    private static Map messageFoldersMap = new HashMap();

    public static final int VBULLETIN_HOUR_ADJUSTMENT = -6;
    
    /**
     * Migrate users.
     * 
     * @throws SQLException the SQL exception
     * @throws ObjectNotFoundException the object not found exception
     * @throws DatabaseException the database exception
     * @throws DuplicateKeyException the duplicate key exception
     * @throws CreateException the create exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     */
    public static void migrateUsers() 
        throws SQLException, ObjectNotFoundException, DatabaseException, DuplicateKeyException, CreateException, ForeignKeyNotFoundException {

        VBulletinUserDAO vBulletinUserDAO = DAOFactory.getVBulletinUserDAO();
        
        MemberDAO memberDAO = com.mvnforum.db.DAOFactory.getMemberDAO();
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        try {
            memberDAO.create(GUEST_NAME, "N/A", "guest@yourdomain.com", "guest@yourdomain.com",
                    0/*memberEmailVisible*/, 0/*memberNameVisible*/, ""/*memberFirstIP*/, ""/*memberLastIP*/, 0/*memberViewCount*/,
                    0/*memberPostCount*/, now/*memberCreationDate*/, now/*memberModifiedDate*/, now/*memberExpireDate*/, now/*memberPasswordExpireDate*/, now/*memberLastLogon*/,
                    0/*memberOption*/, 0/*memberStatus*/, ""/*memberActivateCode*/, ""/*memberTempPassword*/, 0/*memberMessageCount*/,
                    0/*memberMessageOption*/, 10/*memberPostsPerPage*/, 0/*memberWarnCount*/, 0/*memberVoteCount*/,
                    0/*memberVoteTotalStars*/, 0/*memberRewardPoints*/, ""/*memberTitle*/, 0/*memberTimeZone*/, ""/*memberSignature*/,
                    ""/*memberAvatar*/, ""/*memberSkin*/, ""/*memberLanguage*/, ""/*memberFirstname*/, ""/*memberLastname*/, 0/*memberGender*/,
                    new Date(0)/*memberBirthday*/, ""/*memberAddress*/, ""/*memberCity*/, ""/*memberState*/, ""/*memberCountry*/, ""/*memberPhone*/,
                    ""/*memberMobile*/, ""/*memberFax*/, ""/*memberCareer*/, ""/*memberHomepage*/, ""/*memberYahoo*/, ""/*memberAol*/, ""/*memberIcq*/,
                    ""/*memberMsn*/, ""/*memberCoolLink1*/, ""/*memberCoolLink2*/);        
        } catch (DuplicateKeyException e) {
            //ignore this
        }
        
        int userCount = 0;
        int firstUserID = 0;
        VBulletinUserBean vBulletinUserBean = vBulletinUserDAO.getNextUser(firstUserID);
        while (vBulletinUserBean != null) {
            //try to migrate
            String memberName = vBulletinUserBean.getUserName();
            String memberFirstEmail = vBulletinUserBean.getEmail();
            
            String logMessage = "Adding vBulletin user \"" + memberName + "\"";
            
            String newMembername = memberName;
            String newEmail = memberFirstEmail;
            boolean hasChange = false;
            if (memberName.equalsIgnoreCase("admin") == false) {
                try {
                    StringUtil.checkGoodName(newMembername, null);
                } catch (BadInputException e) {
                    newMembername = "User";
                }
                
                if (newMembername.length() > 28) {
                    newMembername = newMembername.substring(0, 28);
                }
                
                newMembername = ConverterMapping.getAvailableMemberName(newMembername);
                
                if (memberName.equals(newMembername) == false) {
                    logMessage += " with new user name \"" + newMembername + "\"";
                    ConverterMapping.getUserNameMap().put(memberName, newMembername);
                    memberName = newMembername;
                    hasChange = true;
                }
            }
           
            if (newEmail.length() > 58) {
                newEmail = newEmail.substring(0, 58);
            }
            
            newEmail = ConverterMapping.getAvailableMemberEmail(newEmail);
            if ( memberName.equals(newMembername) == false &&
                memberFirstEmail.equals(newEmail) == false ) {
                logMessage += " and";
            }
            
            if (memberFirstEmail.equals(newEmail) == false) {
                logMessage += " with new email \"" + newEmail + "\"";
                ConverterMapping.getUserNameMap().put(memberFirstEmail, newEmail);
                memberFirstEmail = newEmail;
                hasChange = true;
            }
            logMessage += ".\n";
            
            if (hasChange) {
                log.debug(logMessage);
            }
            
            String memberPassword = vBulletinUserBean.getPassword();
            if (memberPassword.equals("")) {
                memberPassword = "Guest";
            } else {
                memberPassword = vBulletinUserBean.getPassword();
            }
            
            String memberEmail = memberFirstEmail;

            int memberEmailVisible = 0;
            int memberNameVisible = 0;
            String memberFirstIP = "0.0.0.0";
            String memberLastIP = "0.0.0.0";
            int memberViewCount = 0;
            int memberPostCount = vBulletinUserBean.getPosts();
            Timestamp memberCreationDate = Utils.getTimeStamp(vBulletinUserBean.getJoinDate());
            Timestamp memberModifiedDate = memberCreationDate;
            Timestamp memberExpireDate = memberCreationDate;
            Timestamp memberPasswordExpireDate = memberCreationDate;
            Timestamp memberLastLogon = Utils.getTimeStamp(vBulletinUserBean.getLastVisit());
            int memberOption = 0;
            int memberStatus = 0;
            String memberActivateCode = "";
            String memberTempPassword = "";
            int memberMessageCount = 0;
            int memberMessageOption = 0;
            int memberPostsPerPage = 10; //Default by mvnForum.
            int memberWarnCount = 0;
            int memberVoteCount = 0;
            int memberVoteTotalStars = 0;
            int memberRewardPoints = 0;
            String memberTitle = "";

            double memberTimeZone = 0;
            if (vBulletinUserBean.getTimeZoneOffset() != null && vBulletinUserBean.getTimeZoneOffset().length() > 0) {
                memberTimeZone = new Double(vBulletinUserBean.getTimeZoneOffset()).doubleValue();
            }

            String memberSignature = "";
            String memberAvatar = "";//get from avatar id
            String memberSkin = "";
            //replace default language 

            //String memberLanguage = bean.getUserLang();
            String memberLanguage = "en";

            //VBULLETIN does not store names
            String memberFirstname = memberName;
            //because mvnforum doesn't allow empty name
            String memberLastname = memberName;

            //VBULLETIN does not store gender. This can be an issue. Everyone will be made a female
            int memberGender = 0;
            Date memberBirthday = new Date(0);
            String memberAddress = "";
            String memberCity = "";
            String memberState = "";
            String memberCountry = "";
            String memberPhone = "";
            String memberMobile = "";
            String memberFax = "";
            String memberCareer = "";
            String memberHomepage = vBulletinUserBean.getHomePage();
            String memberYahoo = vBulletinUserBean.getYahoo();
            String memberAol = "";
            String memberIcq = vBulletinUserBean.getIcq();
            String memberMsn = vBulletinUserBean.getMsn();
            // No interests collumn in MvnForum. We will just put the interests in CoolLink1 so that
            // no data is lost. 
            String memberCoolLink1 = "";
            String memberCoolLink2 = "";

            if (memberName.equalsIgnoreCase("admin")) {
                memberDAO.update(ADMIN_ID, memberEmailVisible, memberNameVisible, memberModifiedDate, memberOption, memberStatus, memberMessageOption, memberPostsPerPage, memberTimeZone, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq, memberMsn, memberCoolLink1, memberCoolLink2);
            } else {
                memberDAO.create(memberName, memberPassword, memberFirstEmail, memberEmail,
                        memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                        memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberPasswordExpireDate, memberLastLogon,
                        memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                        memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                        memberVoteTotalStars, memberRewardPoints, memberTitle, memberTimeZone, memberSignature,
                        memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender,
                        memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone,
                        memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq,
                        memberMsn, memberCoolLink1, memberCoolLink2);
            }

            int mvnforumMemberID = com.mvnforum.db.DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
            ConverterMapping.getUserIDMap().put(new Integer(vBulletinUserBean.getUserID()), new Integer(mvnforumMemberID));
            
            int folderStatus = 0;
            int folderOption = 0;
            int folderType = 0;
            try {
                com.mvnforum.db.DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, mvnforumMemberID, 0/*order*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException e) {
                //ignore this
            }
            try {
                com.mvnforum.db.DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_DRAFT, mvnforumMemberID, 1/*order*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException e) {
                //ignore this
            }
            try {
                com.mvnforum.db.DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_SENT, mvnforumMemberID, 2/*order*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException e) {
                //ignore this
            }
            try {
                com.mvnforum.db.DAOFactory.getMessageFolderDAO().create(MVNForumConstant.MESSAGE_FOLDER_TRASH, mvnforumMemberID, 3/*order*/, folderStatus, folderOption, folderType, now, now);
            } catch (DuplicateKeyException e) {
                //ignore this
            }
            
            userCount++;
            
            System.out.println(userCount + " : " + memberName + " added.");
            vBulletinUserBean = vBulletinUserDAO.getNextUser(vBulletinUserBean.getUserID());
        }
    }

    /**
     * Migrate forums.
     * 
     * @throws SQLException the SQL exception
     * @throws DatabaseException the database exception
     * @throws ObjectNotFoundException the object not found exception
     * @throws CreateException the create exception
     * @throws DuplicateKeyException the duplicate key exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     */
    public static void migrateForums() 
        throws SQLException, DatabaseException, ObjectNotFoundException, CreateException, DuplicateKeyException, ForeignKeyNotFoundException {

        VBulletinForumDAO vBulletinForumDAO = DAOFactory.getVBulletinForumDAO();
        VBulletinThreadDAO vBulletinThreadDAO = DAOFactory.getVBulletinThreadDAO();

        Collection categories = vBulletinForumDAO.getCategories();
        int categoryCount = 0;
        for (Iterator iter = categories.iterator(); iter.hasNext();) {
            VBulletinForumBean category = (VBulletinForumBean) iter.next();
            
            // create Category bean in mvnforum db
            int parentCategoryID = 0;
            String categoryName = category.getTitle();
            String categoryDesc = category.getDescription();
            Timestamp now = DateUtil.getCurrentGMTTimestamp();
            Timestamp categoryCreationDate = now;
            Timestamp categoryModifiedDate = now;
            int categoryOrder = category.getDisplayOrder();
            int categoryOption = 0;
            int categoryStatus = 0;
            int mvnforumCategoryID = com.mvnforum.db.DAOFactory.getCategoryDAO().createCategory(parentCategoryID, categoryName, categoryDesc, categoryCreationDate, categoryModifiedDate, categoryOrder, categoryOption, categoryStatus);
            int vBulletinCategoryID = category.getForumID();
            
            Collection forums = vBulletinForumDAO.getForumsByCategory(vBulletinCategoryID);
            for (Iterator forumIter = forums.iterator(); forumIter.hasNext();) {
                VBulletinForumBean forum = (VBulletinForumBean) forumIter.next();
                
                String forumOwnerName = "";
                String lastPostMemberName = ConverterMapping.getMemberNameFromMap(forum.getLastPoster());
                String forumName = forum.getTitle();
                String forumDesc = forum.getDescription();
                Timestamp forumCreationDate = now;
                Timestamp forumModifiedDate = now;
                Timestamp forumLastPostDate = Utils.getTimeStamp(forum.getLastPost());
                int forumOrder = forum.getDisplayOrder();
                int forumType = 0;
                int forumFormatOption = 0;
                int forumOption = 0;
                int forumStatus = 0;
                int forumModerationMode = 0;
                String forumPassword = "";
                int forumThreadCount = forum.getThreadCount();
                int forumPostCount = forum.getReplyCount();
                int mvnforumForumID = com.mvnforum.db.DAOFactory.getForumDAO().
                        createForum(mvnforumCategoryID, forumOwnerName, lastPostMemberName,
                                forumName, forumDesc, forumCreationDate, forumModifiedDate,
                                forumLastPostDate, forumOrder, forumType, forumFormatOption,
                                forumOption, forumStatus, forumModerationMode, forumPassword,
                                forumThreadCount, forumPostCount);
                
                int vBulletinForumID = forum.getForumID();
                
                int firstThreadID = 0;
                VBulletinThreadBean vBulletinThreadBean = vBulletinThreadDAO.getNextThread(firstThreadID, vBulletinForumID);
                while (vBulletinThreadBean != null) {
                    String memberName = StringUtil.getEmptyStringIfNull(ConverterMapping.getMemberNameFromMap(vBulletinThreadBean.getPostUsername()));
                    String lastPostMemberName_Thread = StringUtil.getEmptyStringIfNull(ConverterMapping.getMemberNameFromMap(vBulletinThreadBean.getLastPoster()));
                    String threadTopic = vBulletinThreadBean.getTitle();
                    int threadVoteCount = vBulletinThreadBean.getVoteNum();
                    int threadVoteTotalStars = vBulletinThreadBean.getVoteTotal();
                    Timestamp threadCreationDate = Utils.getTimeStamp(vBulletinThreadBean.getDateLine());
                    Timestamp threadLastPostDate = Utils.getTimeStamp(vBulletinThreadBean.getLastPost());
                    int threadType = 0;
                    int threadPriority = 0;
                    int threadOption = 0;
                    int threadStatus = 0;
                    if (vBulletinThreadBean.getVisible() == 1) {
                        threadStatus = ThreadBean.THREAD_STATUS_DEFAULT;
                    }
                    int threadHasPoll = 0;
                    int threadViewCount = vBulletinThreadBean.getViews();
                    int threadReplyCount = vBulletinThreadBean.getReplyCount();
                    String threadIcon = "";//can get this
                    int threadDuration = 0;
                    int threadAttachCount = vBulletinThreadBean.getAttach();
                    
                    int vBulletinFirstPostID = vBulletinThreadBean.getFirstPostID();
                    VBulletinPostBean firstPostBean = DAOFactory.getVBulletinPostDAO().getBean(vBulletinFirstPostID);
                    String threadBody = MVNCodeFilter.filter(firstPostBean.getPageText());
                    
                    if (threadTopic == null || threadTopic.length() == 0) {
                        threadTopic = UNTITLED_THREAD;
                    }
                    
                    int mvnforumThreadID = com.mvnforum.db.DAOFactory.getThreadDAO().
                            createThread(mvnforumForumID, memberName, lastPostMemberName_Thread,
                                    threadTopic, threadBody, threadVoteCount,
                                    threadVoteTotalStars, threadCreationDate, threadLastPostDate,
                                    threadType, threadPriority, threadOption, threadStatus,
                                    threadHasPoll, threadViewCount, threadReplyCount,
                                    threadIcon, threadDuration, threadAttachCount);
                    
                    int vBulletinThreadID = vBulletinThreadBean.getThreadID();
                    if (firstPostBean != null) {
                        int mvnforumParentPostID = 0;
                        migratePosts(vBulletinThreadID, mvnforumForumID, mvnforumThreadID, vBulletinFirstPostID, mvnforumParentPostID);
                    }
                    vBulletinThreadBean = vBulletinThreadDAO.getNextThread(vBulletinThreadBean.getThreadID(), vBulletinForumID);
                }
                
                
            }
            categoryCount++;
            System.out.println("categoryCount=" + categoryCount);
        }
    }
    
    /**
     * Migrate posts.
     * 
     * @param vBulletinThreadID the v bulletin thread id
     * @param mvnforumForumID the mvnforum forum id
     * @param mvnforumThreadID the mvnforum thread id
     * @param parentPostID the parent post id
     * @param mvnforumParentPostID the mvnforum parent post id
     * 
     * @throws DatabaseException the database exception
     * @throws ObjectNotFoundException the object not found exception
     * @throws CreateException the create exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     */
    public static void migratePosts(int vBulletinThreadID, int mvnforumForumID, int mvnforumThreadID, int parentPostID, int mvnforumParentPostID) throws DatabaseException, ObjectNotFoundException, CreateException, ForeignKeyNotFoundException {

        VBulletinPostBean bulletinPostBean = DAOFactory.getVBulletinPostDAO().getBean(parentPostID);
        String memberName = ConverterMapping.getMemberNameFromMap(bulletinPostBean.getUsername());
        int memberID = com.mvnforum.db.DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        int currentPostID = bulletinPostBean.getPostID();
        
        int mvnforumPostID = migratePost(currentPostID, mvnforumParentPostID, mvnforumForumID, mvnforumThreadID, memberID, memberName);
        
        Collection childPosts = DAOFactory.getVBulletinPostDAO().getChildPost(parentPostID);
        for (Iterator iter = childPosts.iterator(); iter.hasNext();) {
            VBulletinPostBean childPost = (VBulletinPostBean) iter.next();
            migratePosts(vBulletinThreadID, mvnforumForumID, mvnforumThreadID, childPost.getPostID(), mvnforumPostID);
        }
    }
    
    /**
     * Migrate post.
     * 
     * @param vBulletinPostID the v bulletin post id
     * @param mvnforumParentPostID the mvnforum parent post id
     * @param mvnforumForumID the mvnforum forum id
     * @param mvnforumThreadID the mvnforum thread id
     * @param memberID the member id
     * @param memberName the member name
     * 
     * @return the int
     * 
     * @throws CreateException the create exception
     * @throws DatabaseException the database exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     * @throws ObjectNotFoundException the object not found exception
     */
    public static int migratePost(int vBulletinPostID, int mvnforumParentPostID, int mvnforumForumID, int mvnforumThreadID, int memberID, String memberName) throws CreateException, DatabaseException, ForeignKeyNotFoundException, ObjectNotFoundException {
        VBulletinPostBean bulletinPostBean = DAOFactory.getVBulletinPostDAO().getBean(vBulletinPostID);
        
        String postTopic = bulletinPostBean.getTitle();
        String postBody = MVNCodeFilter.filter(bulletinPostBean.getPageText());
        String lastEditMemberName = memberName;
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp postCreationDate = Utils.getTimeStamp(bulletinPostBean.getDateLine());
        Timestamp postLastEditDate = postCreationDate;
        String postCreationIP = bulletinPostBean.getIpAddress();
        String postLastEditIP = bulletinPostBean.getIpAddress();
        int postEditCount = 0;
        int postFormatOption = 0;
        int postOption = 0;
        int postStatus = 0;
        String postIcon = "";
        int postAttachCount = bulletinPostBean.getAttach();
        
        if (postTopic == null || postTopic.length() == 0) {
            postTopic = UNTITLED_POST;
        }
        
        int mvnforumPostID = com.mvnforum.db.DAOFactory.getPostDAO().
                createPost(mvnforumParentPostID, mvnforumForumID, mvnforumThreadID,
                        memberID, memberName, lastEditMemberName,
                        postTopic, postBody, postCreationDate, postLastEditDate,
                        postCreationIP, postLastEditIP, postEditCount, postFormatOption,
                        postOption, postStatus, postIcon, postAttachCount);
        
        Collection attachments = DAOFactory.getVBulletinAttachmentDAO().getBeans(bulletinPostBean.getPostID());
        for (Iterator iter = attachments.iterator(); iter.hasNext();) {
            VBulletinAttachmentBean attachment = (VBulletinAttachmentBean) iter.next();
            
            String attachFilename = attachment.getFileName();
            int attachFileSize = attachment.getFileSize();
            String attachMimeType = "";

            String fileExtension = attachment.getExtension();
            try {
                attachMimeType = Utils.getMimeTypeFromExtension(fileExtension);
            } catch (ObjectNotFoundException e) {
                log.debug("Cannot get mime type for extension \"" + fileExtension + "\" from table AttachmentType" + " (Post topic \"" + postTopic + "\")");
            }
            
            String attachDesc = "";
            String attachCreationIP = "";
            Timestamp attachCreationDate = Utils.getTimeStamp(attachment.getDateLine());
            Timestamp attachModifiedDate = attachCreationDate;
            int attachDownloadCount = attachment.getCounter();
            int attachStatus = 0;

            int vBulletinMemberID = attachment.getUserID();
            int mvnforumMemberID = ((Integer) ConverterMapping.getUserIDMap().get(new Integer(vBulletinMemberID))).intValue();
            
            int storageType = 0;
            String fileData = attachment.getFileData();
            String contentType = "";
            int dataID = MVNCoreEnterpriseDAOFactory.getBinaryStorageDAO().createBinaryIntoDatabase(BinaryStorageService.CATEGORY_POST_ATTACHMENT, attachFilename,
                    attachFileSize, fileData.getBytes(), attachCreationIP, contentType,
                    0, attachStatus, storageType, now, now);

            int attachOption = dataID;

            com.mvnforum.db.DAOFactory.getAttachmentDAO().create(mvnforumPostID, mvnforumMemberID, attachFilename, attachFileSize, attachMimeType, attachDesc, attachCreationIP, attachCreationDate, attachModifiedDate, attachDownloadCount, attachOption, attachStatus);
        }
        
        return mvnforumPostID;
    }
    
    /**
     * Migrate private message.
     * 
     * @throws DatabaseException the database exception
     * @throws CreateException the create exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     * @throws ObjectNotFoundException the object not found exception
     */
    public static void migratePrivateMessage() throws DatabaseException, CreateException, ForeignKeyNotFoundException, ObjectNotFoundException {
        
        Collection messageBeans = DAOFactory.getVBulletinPrivateMessageDAO().getBeans();
        
        for (Iterator iter = messageBeans.iterator(); iter.hasNext(); ) {
            VBulletinPrivateMessageBean vBulletinPrivateMessageBean = (VBulletinPrivateMessageBean) iter.next();
            int folderID = vBulletinPrivateMessageBean.getFolderID();
            String userName = vBulletinPrivateMessageBean.getToUserName();
            
            String mvnforumMemberName = ConverterMapping.getMemberNameFromMap(userName);
            int memberID = com.mvnforum.db.DAOFactory.getMemberDAO().getMemberIDFromMemberName(mvnforumMemberName);
            
            String folderName = "";
            if (folderID == 0) {//Inbox
                folderName = MVNForumConstant.MESSAGE_FOLDER_INBOX;
            } else if (folderID == -1) {//Sent
                folderName = MVNForumConstant.MESSAGE_FOLDER_SENT;
            } else {
                if (messageFoldersMap.containsKey(mvnforumMemberName)) {
                    Map messageFolderMap = new HashMap();
                    if (messageFolderMap.containsKey(new Integer(folderID))) {
                        folderName = (String) messageFolderMap.get(new Integer(folderID));
                    }
                }
            }
            
            String mvnforumSenderMemberName = ConverterMapping.getMemberNameFromMap(vBulletinPrivateMessageBean.getFromUserName());
            int messageSenderID = com.mvnforum.db.DAOFactory.getMemberDAO().getMemberIDFromMemberName(mvnforumSenderMemberName);
            
            String messageToList = "";
            String messageCcList = "";
            String messageBccList = "";
            
            String toUserArray = vBulletinPrivateMessageBean.getToUserArray();
            
            toUserArray = toUserArray.substring(toUserArray.indexOf("{") + "{".length());
            
            //messageToList1=s:2:"cc";a:3:{i:1;s:5:"admin";i:3;s:3:"tam";i:2;s:6:"xuantl";}
            String messageToList1 = toUserArray.substring(0, toUserArray.indexOf("}") + "}".length());

            //messageToList2=s:3:"bcc";a:2:{i:1;s:5:"admin";i:3;s:3:"tam";}
            String messageToList2 = toUserArray.substring(toUserArray.indexOf("}") + "}".length(), toUserArray.length() - "}".length());
            
            //message type maybe cc
            messageToList1 = messageToList1.substring(messageToList1.indexOf(":\"") + ":\"".length());
            String messageType1 = messageToList1.substring(0, messageToList1.indexOf("\";"));
            
            if (messageType1.equals("cc")) {
                messageToList = getMvnForumUserList(messageToList1);
            } else if (messageType1.equals("bcc")) {
                messageBccList = getMvnForumUserList(messageToList1);
            }
            
            //message type maybe bcc
            if (messageToList2.length() > 0) {
                messageToList2 = messageToList2.substring(messageToList2.indexOf(":\"") + ":\"".length());
                String messageType2 = messageToList2.substring(0, messageToList2.indexOf("\";"));
                
                if (messageType2.equals("cc")) {
                    messageToList = getMvnForumUserList(messageToList2);
                } else if (messageType2.equals("bcc")) {
                    messageBccList = getMvnForumUserList(messageToList2);
                }
            }
            
            String messageTopic = vBulletinPrivateMessageBean.getTitle();
            String messageBody = vBulletinPrivateMessageBean.getMessage();
            int messageType = 0;
            int messageOption = 0;
            int messageStatus = 0;
            int messageReadStatus = MessageBean.MESSAGE_READ_STATUS_DEFAULT;
            if (vBulletinPrivateMessageBean.getReadCount() > 0) {
                messageReadStatus = MessageBean.MESSAGE_READ_STATUS_READ;
            }
            int messageNotify = 0;
            String messageIcon = "";
            int messageAttachCount = 0;
            String messageIP = "";
            Timestamp messageCreationDate = vBulletinPrivateMessageBean.getDateLine();
           
            System.out.println("folderName=" + folderName);
            System.out.println("memberID=" + memberID);
            System.out.println("messageSenderID=" + messageSenderID);
            System.out.println("mvnforumSenderMemberName=" + mvnforumSenderMemberName);
            System.out.println("messageToList=" + messageToList);
            System.out.println("messageTopic=" + messageTopic);
            System.out.println("messageBody=" + messageBody);
            System.out.println("messageCreationDate=" + messageCreationDate);
            com.mvnforum.db.DAOFactory.getMessageDAO().create(folderName, memberID, messageSenderID,
                    mvnforumSenderMemberName, messageToList, messageCcList, messageBccList,
                    messageTopic, messageBody,
                    messageType, messageOption, messageStatus, messageReadStatus,
                    messageNotify, messageIcon, messageAttachCount, messageIP, messageCreationDate);
            
            if (messageSenderID != memberID) {
                com.mvnforum.db.DAOFactory.getMessageStatisticsDAO().create(messageSenderID, memberID, messageCreationDate,
                                                            messageAttachCount, messageType, messageOption, messageStatus);
            }
        }
    }
    
    /**
     * Migrate private message folders.
     * 
     * @throws DatabaseException the database exception
     * @throws ObjectNotFoundException the object not found exception
     * @throws CreateException the create exception
     * @throws DuplicateKeyException the duplicate key exception
     * @throws ForeignKeyNotFoundException the foreign key not found exception
     */
    public static void migratePrivateMessageFolders() throws DatabaseException, ObjectNotFoundException, CreateException, DuplicateKeyException, ForeignKeyNotFoundException {
        Collection messageFolders = DAOFactory.getVBulletinMessageFolderDAO().getBeans();
        for (Iterator iter = messageFolders.iterator(); iter.hasNext(); ) {
            VBulletinMessageFolderBean bulletinMessageFolderBean = (VBulletinMessageFolderBean) iter.next();
            String vBulletinUserName = bulletinMessageFolderBean.getUserName();
            String vBulletinFolderNames = bulletinMessageFolderBean.getPMFolders();
            //vBulletinFolderNames=a:5:{i:4;s:3:"aaa";i:5;s:4:"bbbb";i:1;s:5:"Drafs";i:2;s:6:"Trash1";i:3;s:6:"Trash2";}
            
            String mvnforumMemberName = ConverterMapping.getMemberNameFromMap(vBulletinUserName);
            int memberID = com.mvnforum.db.DAOFactory.getMemberDAO().getMemberIDFromMemberName(mvnforumMemberName);

            int folderCount = Integer.parseInt(vBulletinFolderNames.substring("a:".length(), vBulletinFolderNames.indexOf(":{")));
            Map messageFolderMap = new HashMap();
            for (int i = 0; i < folderCount; i++) {
                vBulletinFolderNames = vBulletinFolderNames.substring(vBulletinFolderNames.indexOf(";") + 1);
                String folderName = vBulletinFolderNames.substring(0, vBulletinFolderNames.indexOf(";"));
                vBulletinFolderNames = vBulletinFolderNames.substring(folderName.length() + 1);
                
                folderName = folderName.substring(folderName.indexOf(":") + 1);
                int folderID = Integer.parseInt(folderName.substring(0, folderName.indexOf(":")));
                
                folderName = folderName.substring(folderName.indexOf("\""));
                folderName = folderName.substring(1);
                folderName = folderName.substring(0, folderName.length() - 1);
                
                Timestamp now = DateUtil.getCurrentGMTTimestamp();
                try {
                    com.mvnforum.db.DAOFactory.getMessageFolderDAO().create(folderName, memberID, 0, 0, 0, 0, now, now);
                } catch (DuplicateKeyException e) {
                    // do nothing
                }
                messageFolderMap.put(new Integer(folderID), folderName);
            }
            messageFoldersMap.put(mvnforumMemberName, messageFolderMap);
        }
    }
    
    /**
     * Gets the mvnforum user list.
     * 
     * @param vbulletinUserList the vbulletin user list
     * 
     * @return the mvnforum user list
     * 
     * @throws DatabaseException the database exception
     */
    private static String getMvnForumUserList(String vbulletinUserList) throws DatabaseException {
        String mvnforumUserList = "";
        if (vbulletinUserList.length() > 0) {
            vbulletinUserList = vbulletinUserList.substring(vbulletinUserList.indexOf("\";") + "\";".length());
            vbulletinUserList = vbulletinUserList.substring("a:".length());
            int userCountType1 = Integer.parseInt(vbulletinUserList.substring(0, vbulletinUserList.indexOf(":")));
            for (int i = 0; i < userCountType1; i++) {
                vbulletinUserList = vbulletinUserList.substring(vbulletinUserList.indexOf(":{") + ":{".length());
                vbulletinUserList = vbulletinUserList.substring(vbulletinUserList.indexOf("s:") + "s:".length());
                int numberOfChar = Integer.parseInt(vbulletinUserList.substring(0, vbulletinUserList.indexOf(":")));
                vbulletinUserList = vbulletinUserList.substring(vbulletinUserList.indexOf(":\"") + ":\"".length());
                String vbulletinUser = vbulletinUserList.substring(0, numberOfChar);
                if (mvnforumUserList.length() > 0) {
                    mvnforumUserList += ";";
                }
                mvnforumUserList += ConverterMapping.getMemberNameFromMap(vbulletinUser);
                
                vbulletinUserList = vbulletinUserList.substring(numberOfChar);
            }
        }
        return mvnforumUserList;
    }
    
}