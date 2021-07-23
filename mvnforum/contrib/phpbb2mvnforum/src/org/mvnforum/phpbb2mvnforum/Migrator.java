/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/Migrator.java,v 1.16 2007/01/15 10:27:35 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.16 $
 * $Date: 2007/01/15 10:27:35 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum;

import java.io.PrintStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.CategoryBean;
import org.mvnforum.phpbb2mvnforum.db.CategoryDAO;
import org.mvnforum.phpbb2mvnforum.db.ForumBean;
import org.mvnforum.phpbb2mvnforum.db.ForumDAO;
import org.mvnforum.phpbb2mvnforum.db.MemberDAO;
import org.mvnforum.phpbb2mvnforum.db.MessageDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbCategories;
import org.mvnforum.phpbb2mvnforum.db.PhpbbCategoriesDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbForum;
import org.mvnforum.phpbb2mvnforum.db.PhpbbForumDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPost;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostText;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgs;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopics;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUsers;
import org.mvnforum.phpbb2mvnforum.db.PostBean;
import org.mvnforum.phpbb2mvnforum.db.PostDAO;
import org.mvnforum.phpbb2mvnforum.db.ThreadBean;
import org.mvnforum.phpbb2mvnforum.db.ThreadDAO;
import org.mvnforum.phpbb2mvnforum.db.jdbc.DAOFactory;
import org.mvnforum.util.MD5;
import org.mvnforum.util.Utils;

//==============================================================================
//  The JavaReference.com Software License, Version 1.0 
//  Copyright (c) 2002-2005  JavaReference.com. All rights reserved.
//
//  
//  Redistribution and use in source and binary forms, with or without 
//  modification, are permitted provided that the following conditions 
//  are met: 
//  
//  1. Redistributions of source code must retain the above copyright notice, 
//     this list of conditions and the following disclaimer. 
//  
//  2. Redistributions in binary form must reproduce the above copyright notice, 
//     this list of conditions and the following disclaimer in the documentation 
//     and/or other materials provided with the distribution. 
//     
//  3. The end-user documentation included with the redistribution, if any, must 
//     include the following acknowlegement: 
//     
//     "This product includes software developed by the Javareference.com 
//     (http://www.javareference.com/)." 
//     
//     Alternately, this acknowlegement may appear in the software itself, if and 
//     wherever such third-party acknowlegements normally appear. 
//     
//  4. The names "JavaReference" and "Javareference.com", must not be used to 
//     endorse or promote products derived from this software without prior written 
//     permission. For written permission, please contact webmaster@javareference.com. 
//     
//  5. Products derived from this software may not be called "Javareference" nor may 
//     "Javareference" appear in their names without prior written permission of  
//     Javareference.com. 
//     
//  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
//  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
//  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
//  JAVAREFERENCE.COM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
//  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
//  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
//  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
//  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
//  
//================================================================================ 
//  Software from this site consists of contributions made by various individuals 
//  on behalf of Javareference.com. For more information on Javareference.com, 
//  please see http://www.javareference.com 
//================================================================================

/**
 * @author anandh
 */
public class Migrator {

    private static DAOFactory factory = new DAOFactory();

    private static int PHPBB_ADMIN_ID = 2;

    private static int PHPBB_GUEST_ID = -1;

    private static int ADMIN_ID = 1;

    private static int GUEST_ID = 2;

    public static int correctMvnforumMemberID(int phpbbUserID) {
        if (phpbbUserID == PHPBB_ADMIN_ID)
            return ADMIN_ID;
        else if (phpbbUserID == PHPBB_GUEST_ID)
            return GUEST_ID;
        return phpbbUserID;
    }

    public static void migrateUsers(PrintStream ostream, int gender) 
        throws SQLException, ObjectNotFoundException, DatabaseException, DuplicateKeyException, CreateException {

        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();

        MemberDAO memberDAO = factory.getMemberDAO();

        Collection phpbbUserBeans = phpbbUserDAO.getBeans();

        int userCount = 0;

        for (Iterator iter = phpbbUserBeans.iterator(); iter.hasNext();) {
            PhpbbUsers bean = (PhpbbUsers) iter.next();
            int memberID = correctMvnforumMemberID(bean.getUserId());
            String memberName = bean.getUsername();
            if (memberName.equalsIgnoreCase("Anonymous")) {
                memberName = "Guest";
            }

            String memberPassword = bean.getUserPassword();
            if (memberPassword.equals("")) {
                memberPassword = "Guest";
            } else {
                memberPassword = Utils.wrapIt(MD5.getBase64FromHEX(memberPassword));
            }
            String memberFirstEmail = Utils.wrapIt(bean.getUserEmail());
            String memberEmail = memberFirstEmail;

            int memberEmailVisible = bean.getUserViewemail();
            // Keep name invisible (no equivalent in phpbb)
            int memberNameVisible = 0;
            String memberFirstIP = "0.0.0.0";
            String memberLastIP = "0.0.0.0";
            int memberViewCount = 0;
            int memberPostCount = bean.getUserPosts();
            Timestamp memberCreationDate = Utils.getTimeStamp(bean.getUserRegdate());
            Timestamp memberModifiedDate = memberCreationDate;
            Timestamp memberExpireDate = memberCreationDate;
            Timestamp memberLastLogon = Utils.getTimeStamp(bean.getUserLastvisit());
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

            double tzone = bean.getUserTimezone().doubleValue();

            String memberSignature = Utils.wrapIt(bean.getUserSig());
            String memberAvatar = Utils.wrapIt(bean.getUserAvatar());
            String memberSkin = "";
            //replace default language 

            //String memberLanguage = bean.getUserLang();
            String memberLanguage = "en";

            //PHPBB does not store names
            String memberFirstname = memberName;
            //because mvnforum doesn't allow empty name
            String memberLastname = "";

            //PHPBB does not store gender. This can be an issue. Everyone will be made a female
            int memberGender = gender;
            Date memberBirthday = new Date(0);
            String memberAddress = bean.getUserFrom();
            String memberCity = "";
            String memberState = "";
            String memberCountry = "";
            String memberPhone = "";
            String memberMobile = "";
            String memberFax = "";
            String memberCareer = Utils.wrapIt(bean.getUserOcc());
            String memberHomepage = Utils.wrapIt(bean.getUserWebsite());
            String memberYahoo = Utils.wrapIt(bean.getUserYim());
            String memberAol = Utils.wrapIt(bean.getUserAim());
            String memberIcq = "";
            String memberMsn = Utils.wrapIt(bean.getUserMsnm());
            // No interests collumn in MvnForum. We will just put the interests in CoolLink1 so that
            // no data is lost. 
            String memberCoolLink1 = Utils.wrapIt(bean.getUserInterests());
            String memberCoolLink2 = "";

            if (memberName.equalsIgnoreCase("admin")) {
                memberDAO.update(ADMIN_ID, memberName, memberPassword, memberFirstEmail, memberEmail,
                        memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                        memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                        memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                        memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                        memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature, memberAvatar,
                        memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday,
                        memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile, memberFax,
                        memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq, memberMsn, memberCoolLink1,
                        memberCoolLink2);
            } else {
                if (memberName.equalsIgnoreCase("Guest")) {
                    memberDAO.create(GUEST_ID, memberName, memberPassword, memberFirstEmail, memberEmail,
                            memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                            memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                            memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                            memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                            memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature,
                            memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender,
                            memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone,
                            memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq,
                            memberMsn, memberCoolLink1, memberCoolLink2);
                } else {
                    memberDAO.create(memberID, memberName, memberPassword, memberFirstEmail, memberEmail,
                            memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                            memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                            memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                            memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                            memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature,
                            memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender,
                            memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone,
                            memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq,
                            memberMsn, memberCoolLink1, memberCoolLink2);
                }

            }

            userCount++;
            //            System.out.println(userCount + " : " + memberName + " added!");

        }

        System.out.println(userCount);
    }

    public static void migrateCategories(PrintStream ostream) 
        throws SQLException, DatabaseException, CreateException, DuplicateKeyException, ForeignKeyNotFoundException {
        
        PhpbbCategoriesDAO phpbbCategoriesDAO = factory.getPhpbbCategoriesDAO();

        CategoryDAO categoryDAO = factory.getCategoryDAO();

        CategoryDAO categoryDAOXML = factory.getCategoryDAOXML();

        Collection phpbbCategoriesBeans = phpbbCategoriesDAO.getBeans();
        Collection categoryBeans = new ArrayList();

        int categoryCount = 0;

        for (Iterator iter = phpbbCategoriesBeans.iterator(); iter.hasNext();) {

            PhpbbCategories phpbbCategories = (PhpbbCategories) iter.next();
            int catID = phpbbCategories.getCatId();
            int parentCategoryID = 0;
            String categoryName = phpbbCategories.getCatTitle();
            String categoryDesc = "";
            Timestamp categoryCreationDate = new Timestamp(0);
            Timestamp categoryModifiedDate = categoryCreationDate;
            int categoryOrder = phpbbCategories.getCatOrder();
            int categoryOption = 0;
            int categoryStatus = 0;

            CategoryBean bean = new CategoryBean();
            bean.setCategoryID(catID);
            bean.setParentCategoryID(parentCategoryID);
            bean.setCategoryName(categoryName);
            bean.setCategoryDesc(categoryDesc);
            bean.setCategoryCreationDate(categoryCreationDate);
            bean.setCategoryModifiedDate(categoryModifiedDate);
            bean.setCategoryOrder(categoryOrder);
            bean.setCategoryOption(categoryOption);
            bean.setCategoryStatus(categoryStatus);

            //            categoryBeans.add(bean);
            categoryDAO.create(catID, parentCategoryID, categoryName, categoryDesc, categoryCreationDate,
                    categoryModifiedDate, categoryOrder, categoryOption, categoryStatus);

            categoryCount++;
            //            System.out.println(categoryCount + " : " + categoryName + " added!");
        }

        //        categoryDAOXML.createMultiple(categoryBeans);

        System.out.println(categoryCount);

    }

    public static void migrateForums(PrintStream ostream) 
        throws SQLException, DatabaseException, ObjectNotFoundException, CreateException, DuplicateKeyException, ForeignKeyNotFoundException {

        PhpbbForumDAO phpbbForumDAO = factory.getPhpbbForumDAO();
        PhpbbPostDAO phpbbPostDAO = factory.getPhpbbPostDAO();
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();

        ForumDAO forumDAO = factory.getForumDAO();

        ForumDAO forumDAOXML = factory.getForumDAOXML();
        Collection phpbbForumBeans = phpbbForumDAO.getBeans();
        Collection forumBeans = new ArrayList();

        int forumCount = 0;

        for (Iterator iter = phpbbForumBeans.iterator(); iter.hasNext();) {
            PhpbbForum phpbbForum = (PhpbbForum) iter.next();
            int forumID = phpbbForum.getforum_id();
            int categoryID = phpbbForum.getcat_id();
            int last_post_id = phpbbForum.getforum_last_post_id();
            if (last_post_id == 0) {
                continue;
            }
            String lastPostMemberName = "";
            if (last_post_id == PHPBB_ADMIN_ID) {
                lastPostMemberName = "Admin";
            } else {
                if (last_post_id == PHPBB_GUEST_ID) {
                    lastPostMemberName = "Guest";
                } else {
                    lastPostMemberName = phpbbUserDAO.getUserNameFromUserID(phpbbPostDAO
                            .getPosterIDFromPostID(last_post_id));
                }
            }
            String forumName = phpbbForum.getforum_name();
            String forumDesc = phpbbForum.getforum_desc();
            Timestamp forumCreationDate = new Timestamp(0);
            Timestamp forumModifiedDate = forumCreationDate;
            Timestamp forumLastPostDate = forumCreationDate;
            int forumOrder = phpbbForum.getforum_order();
            int forumType = 0;
            int forumFormatOption = 0;
            int forumOption = 0;
            int forumStatus = phpbbForum.getforum_status();
            int forumModerationMode = 0;
            String forumPassword = "";
            int forumThreadCount = phpbbForum.getforum_topics();
            int forumPostCount = phpbbForum.getforum_posts();

            /*forumDAO.create(forumID, categoryID, lastPostMemberName, forumName, forumDesc, forumCreationDate, forumModifiedDate,
             forumLastPostDate, forumOrder, forumType, forumFormatOption, forumOption, forumStatus, forumModerationMode, 
             forumPassword, forumThreadCount, forumPostCount);*/
            ForumBean bean = new ForumBean();
            bean.setForumID(forumID);
            bean.setCategoryID(categoryID);
            bean.setLastPostMemberName(lastPostMemberName);
            bean.setForumName(forumName);
            bean.setForumDesc(forumDesc);
            bean.setForumCreationDate(forumCreationDate);
            bean.setForumModifiedDate(forumModifiedDate);
            bean.setForumLastPostDate(forumLastPostDate);
            bean.setForumOrder(forumOrder);
            bean.setForumType(forumType);
            bean.setForumFormatOption(forumFormatOption);
            bean.setForumOption(forumOption);
            bean.setForumStatus(forumStatus);
            bean.setForumModerationMode(forumModerationMode);
            bean.setForumPassword(forumPassword);
            bean.setForumPostCount(forumPostCount);
            bean.setForumThreadCount(forumThreadCount);

            //            forumBeans.add(bean);

            forumDAO.create(forumID, categoryID, lastPostMemberName, forumName, forumDesc, forumCreationDate,
                    forumModifiedDate, forumLastPostDate, forumOrder, forumType, forumFormatOption, forumOption,
                    forumStatus, forumModerationMode, forumPassword, forumThreadCount, forumPostCount);

            forumCount++;
            //          System.out.println(forumCount + " : " + forumName + " added!");
        }

        System.out.println(forumCount);

        //        forumDAOXML.createMultiple(forumBeans);
    }

    public static void migratePosts(PrintStream ostream) 
        throws SQLException, DatabaseException, ObjectNotFoundException, CreateException, ForeignKeyNotFoundException, DuplicateKeyException {

        PhpbbPostDAO phpbbPostDAO = factory.getPhpbbPostDAO();
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();
        PhpbbPostTextDAO phpbbPostTextDAO = factory.getPhpbbPostTextDAO();

        PostDAO postDAO = factory.getPostDAO();

        PostDAO postDAOXML = factory.getPostDAOXML();

        Collection phpbbPostBeans = phpbbPostDAO.getBeans();
        Collection postBeans = new ArrayList();

        int postCount = 0;

        for (Iterator iter = phpbbPostBeans.iterator(); iter.hasNext();) {
            PhpbbPost phpbbPost = (PhpbbPost) iter.next();
            int postID = phpbbPost.getpost_id();
            int forumID = phpbbPost.getforum_id();
            int threadID = phpbbPost.gettopic_id();
            int memberID = phpbbPost.getposter_id();

            Collection ppidrs = phpbbPostDAO.getPostIDsFromTopicID(threadID);
            int ppid = 0;
            for (Iterator ppidrsIter = ppidrs.iterator(); ppidrsIter.hasNext();) {
                PhpbbPost phpbbPost_Top = (PhpbbPost) ppidrsIter.next();
                int ppidtmp = phpbbPost_Top.getpost_id();
                if (ppidtmp == postID)
                    break;
                else
                    ppid = ppidtmp;
            }

            int parentPostID = ppid;
            String memberName = "";
            if (memberID != PHPBB_ADMIN_ID && memberID != PHPBB_GUEST_ID) {
                memberName = phpbbUserDAO.getUserNameFromUserID(memberID);
            } else {
                if (memberID == PHPBB_GUEST_ID) {
                    memberName = "Guest";
                    memberID = GUEST_ID;
                } else {
                    // memberID = ADMIN_ID
                    memberName = "Admin";
                    memberID = ADMIN_ID;
                }
            }

            String lastEditMemberName = memberName;
            PhpbbPostText phpbbPostText = phpbbPostTextDAO.getBean(postID);
            String postTopic = Utils.wrapIt(phpbbPostText.getpost_subject());
            String postBody = Utils.wrapIt(phpbbPostText.getpost_text());

            //String PostCreationDate = Utils.wrapit(Utils.DateTimeFromS(RS.getLong("post_time")));
            Timestamp postCreationDate = Utils.getTimeStamp((long) phpbbPost.getpost_time());
            Timestamp postLastEditDate = postCreationDate;
            String postCreationIP = Utils.wrapIt(Utils.HexIPtoString(phpbbPost.getposter_ip()));
            String postLastEditIP = postCreationIP;
            int postEditCount = phpbbPost.getpost_edit_count();
            int postFormatOption = 0;
            int postOption = 0;
            int postStatus = 0;
            String postIcon = "";
            int postAttachCount = 0;

            /*postDAO.create(postID, parentPostID, forumID, threadID, memberID, memberName, lastEditMemberName, postTopic, 
             postBody, postCreationDate, postLastEditDate, postCreationIP, postLastEditIP, postEditCount, postFormatOption, 
             postOption, postStatus, postIcon, postAttachCount);*/
            PostBean bean = new PostBean();
            bean.setPostID(postID);
            bean.setParentPostID(parentPostID);
            bean.setForumID(forumID);
            bean.setThreadID(threadID);
            bean.setMemberID(memberID);
            bean.setMemberName(memberName);
            bean.setLastEditMemberName(lastEditMemberName);
            bean.setPostTopic(postTopic);
            bean.setPostBody(postBody);
            bean.setPostCreationDate(postCreationDate);
            bean.setPostLastEditDate(postLastEditDate);
            bean.setPostCreationIP(postCreationIP);
            bean.setPostLastEditIP(postLastEditIP);
            bean.setPostEditCount(postEditCount);
            bean.setPostFormatOption(postFormatOption);
            bean.setPostOption(postOption);
            bean.setPostStatus(postStatus);
            bean.setPostIcon(postIcon);
            bean.setPostAttachCount(postAttachCount);

            // postBeans.add(bean);

            postDAO.create(postID, parentPostID, forumID, threadID, memberID, memberName, lastEditMemberName,
                    postTopic, postBody, postCreationDate, postLastEditDate, postCreationIP, postLastEditIP,
                    postEditCount, postFormatOption, postOption, postStatus, postIcon, postAttachCount);

            postCount++;
            // System.out.println(postCount + " : " + postTopic + " added!");
            System.out.println(postCount);
        }

        // postDAOXML.createMultiple(postBeans);

        System.out.println(postCount);

    }

    public static void migrateThreads(PrintStream ostream) 
        throws SQLException, ObjectNotFoundException, CreateException, DatabaseException, 
        ForeignKeyNotFoundException, DuplicateKeyException {

        PhpbbTopicsDAO phpbbTopicsDAO = factory.getPhpbbTopicsDAO();
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();
        PhpbbPostDAO phpbbPostDAO = factory.getPhpbbPostDAO();
        PhpbbPostTextDAO phpbbPostTextDAO = factory.getPhpbbPostTextDAO();

        ThreadDAO threadDAO = factory.getThreadDAO();

        ThreadDAO threadDAOXML = factory.getThreadDAOXML();
        Collection phpbbTopicBeans = phpbbTopicsDAO.getBeans();
        Collection threadBeans = new ArrayList();

        int threadCount = 0;

        for (Iterator iter = phpbbTopicBeans.iterator(); iter.hasNext();) {
            PhpbbTopics bean = (PhpbbTopics) iter.next();
            int threadID = bean.gettopic_id();
            int forumID = bean.getforum_id();

            int memberID = bean.gettopic_poster();
            String memberName = "";
            if (memberID == PHPBB_ADMIN_ID) {
                memberName = "Admin";
            } else {
                if (memberID == PHPBB_GUEST_ID) {
                    memberName = "Guest";
                } else {
                    memberName = phpbbUserDAO.getUserNameFromUserID(memberID);
                }
            }

            String lastPostMemberName = "";
            try {
                if (memberID == PHPBB_ADMIN_ID) {
                    lastPostMemberName = "Admin";
                } else {
                    if (memberID == PHPBB_GUEST_ID) {
                        lastPostMemberName = "Guest";
                    } else {
                        lastPostMemberName = phpbbUserDAO.getUserNameFromUserID(memberID);
                    }
                }
            } catch (ObjectNotFoundException oe) {
            }
            String threadTopic = bean.gettopic_title();

            String threadBody = "";
            int first_post_id = bean.gettopic_first_post_id();
            try {
                threadBody = phpbbPostTextDAO.getPostTextFromPostID(first_post_id);
            } catch (ObjectNotFoundException oe) {
            }

            threadBody = Utils.wrapIt(Utils.stripPHPBBQuotes(threadBody));

            int threadVoteCount = 0;
            int threadVoteTotalStars = 0;
            Timestamp threadCreationDate = Utils.getTimeStamp(bean.gettopic_time());

            Timestamp threadLastPostDate = new Timestamp(0);
            int last_post_id = bean.gettopic_last_post_id();
            try {
                threadLastPostDate = Utils.getTimeStamp(phpbbPostDAO.getPostTimeFromPostID(last_post_id));
            } catch (ObjectNotFoundException oe) {
            }

            int threadType = 0;
            int threadOption = 0;
            int threadStatus = bean.gettopic_status();
            int threadHasPoll = bean.gettopic_vote();
            int threadViewCount = bean.gettopic_views();
            int threadReplyCount = bean.gettopic_replies();
            String threadIcon = "";
            int threadDuration = 0;
            int threadAttachCount = 0;

            /*threadDAO.createThread(threadID, forumID, memberName, lastPostMemberName, threadTopic, threadBody, threadVoteCount, threadVoteTotalStars, 
             threadCreationDate, threadLastPostDate, threadType, threadOption, threadStatus, threadHasPoll, threadViewCount, 
             threadReplyCount, threadIcon, threadDuration, threadAttachCount);*/

            ThreadBean threadBean = new ThreadBean();
            threadBean.setThreadID(threadID);
            threadBean.setForumID(forumID);
            threadBean.setMemberName(memberName);
            threadBean.setLastPostMemberName(lastPostMemberName);
            threadBean.setThreadTopic(threadTopic);
            threadBean.setThreadBody(threadBody);
            threadBean.setThreadVoteCount(threadVoteCount);
            threadBean.setThreadVoteTotalStars(threadVoteTotalStars);
            threadBean.setThreadCreationDate(threadCreationDate);
            threadBean.setThreadLastPostDate(threadLastPostDate);
            threadBean.setThreadType(threadType);
            threadBean.setThreadOption(threadOption);
            threadBean.setThreadStatus(threadStatus);
            threadBean.setThreadHasPoll(threadHasPoll);
            threadBean.setThreadViewCount(threadViewCount);
            threadBean.setThreadReplyCount(threadReplyCount);
            threadBean.setThreadIcon(threadIcon);
            threadBean.setThreadDuration(threadDuration);
            threadBean.setThreadAttachCount(threadAttachCount);

            //            threadBeans.add(threadBean);

            threadDAO.createThread(threadID, forumID, memberName, lastPostMemberName, threadTopic, threadBody,
                    threadVoteCount, threadVoteTotalStars, threadCreationDate, threadLastPostDate, threadType,
                    threadOption, threadStatus, threadHasPoll, threadViewCount, threadReplyCount, threadIcon,
                    threadDuration, threadAttachCount);

            threadCount++;
            System.out.println(threadCount);

        }

        //        threadDAOXML.createMultiple(threadBeans);

        System.out.println(threadCount);

    }

    public static void migratePrivateMessages(PrintStream ostream) 
        throws SQLException, DatabaseException, ObjectNotFoundException, CreateException, DuplicateKeyException {

        PhpbbPrivmMsgsDAO phpbbPrivmMsgsDAO = factory.getPhpbbPrivmMsgsDAO();
        PhpbbPrivmMsgsTextDAO phpbbPrivmMsgsTextDAO = factory.getPhpbbPrivmMsgsTextDAO();

        MemberDAO memberDAO = factory.getMemberDAO();
        MessageDAO messageDAO = factory.getMessageDAO();

        Collection phpbbPrivmMsgsBeans = phpbbPrivmMsgsDAO.getBeans();

        for (Iterator iter = phpbbPrivmMsgsBeans.iterator(); iter.hasNext();) {
            PhpbbPrivmMsgs bean = (PhpbbPrivmMsgs) iter.next();

            int messageID = bean.getprivmsgs_id();
            int messageSenderID = bean.getprivmsgs_from_userid();
            String messageSenderName = "";
            int messageReceiverID = bean.getprivmsgs_to_userid();
            String messageToList = "";
            String messageCcList = "";
            String messageBccList = "";
            String messageTopic = Utils.wrapIt(bean.getprivmsgs_subject());
            String messageBody = Utils.wrapIt(phpbbPrivmMsgsTextDAO.getBean(messageID).getprivmsgs_text());
            int messageType = 0;
            int messageOption = 0;
            int messageStatus = 0;
            int messageRead = bean.getprivmsgs_type();
            int messageReadStatus = (messageRead == 1 || messageRead == 5) ? 0 : 1;
            int messageNotify = 0;
            String messageIcon = "";
            int messageAttachCount = 0;
            String messageIP = Utils.wrapIt(Utils.HexIPtoString(bean.getprivmsgs_ip()));
            Timestamp messageCreationDate = Utils.getTimeStamp(bean.getprivmsgs_date());

            messageSenderID = correctMvnforumMemberID(messageSenderID);
            try {
                messageSenderName = memberDAO.getMemberNameFromMemberID(messageSenderID);
            } catch (ObjectNotFoundException oe) {
            }

            messageReceiverID = correctMvnforumMemberID(messageReceiverID);
            try {
                messageToList = memberDAO.getMemberNameFromMemberID(messageReceiverID);
            } catch (ObjectNotFoundException oe) {
            }

            messageDAO.create("Inbox", messageReceiverID, messageSenderID, messageSenderName, messageToList,
                    messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                    messageStatus, messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                    messageCreationDate);
            messageDAO.create("Sent", messageSenderID, messageSenderID, messageSenderName, messageToList,
                    messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                    messageStatus, 1, messageNotify, messageIcon, messageAttachCount, messageIP, messageCreationDate);
        }
    }
}