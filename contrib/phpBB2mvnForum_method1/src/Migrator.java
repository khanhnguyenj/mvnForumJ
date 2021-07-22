/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum_method1/src/Migrator.java,v 1.8 2009/12/03 08:39:02 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2009/12/03 08:39:02 $
 *
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

/* ====================================================================
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
 * @author: Luis Miguel Hernanz 
 * @author: Minh Nguyen  
 */

import java.io.*;
import java.sql.*;
import java.util.Date;

/**
 * @author anandh
 */

public class Migrator {

    public static String usernamefromid(Connection php, String id) throws SQLException
    {
        String memname = "";
        Statement mstmt = php.createStatement();
        ResultSet mrs = mstmt.executeQuery("SELECT username from phpbb_users where user_id =" + id);
        if (mrs.next()) {
            memname = mrs.getString("username");
        }
        
        mrs.close();
        mstmt.close();
        
        return memname;
    }
    
    public static int timeZoneFromId(Connection php, String id) throws SQLException {
        
        // default
        int timeZone = 7;
        
        Statement statement = php.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT user_timezone from phpbb_users where user_id =" + id);
        
        if (resultSet.next()) {
            
            timeZone = resultSet.getInt("user_timezone");
            
        }
        
        return timeZone;
    }
    
        
    public static void migrateusers(Connection php, Connection mvn, PrintStream ostream) throws SQLException, FileNotFoundException {
    
        // tao statement cho PHPBB
        Statement Stmt = php.createStatement();
        
        // tao resultset cho PHPBB
        ResultSet RS = Stmt.executeQuery("SELECT * FROM phpbb_users");
        
        int userCount = 0;
    
        while (RS.next()) {
            
            // lay username va user_id tu PHPBB
            String MemberID = RS.getString("user_id");
            String MemberName = RS.getString("username");
            
            int timeZone = RS.getInt("user_timezone");
            
            // khong convert the Anonymous
            if (MemberName.equalsIgnoreCase("Anonymous")) {
                continue;
            }
    
            // wrap MemberName, de lam gi??? 
            MemberName = Utils.wrapit(MemberName);
            
            String MemberPassword = Utils.wrapit(MD5.getBase64FromMD5(RS.getString("user_password")));
            String MemberFirstEmail = Utils.wrapit(RS.getString("user_email"));
            String MemberEmail = MemberFirstEmail;
            
            String MemberEmailVisible = RS.getString("user_viewemail");
            // Keep name invisible (no equivalent in phpbb)
            String MemberNameVisible = "0";
            String MemberFirstIP = Utils.wrapit("0.0.0.0");
            String MemberLastIP = Utils.wrapit("0.0.0.0");
            String MemberViewCount = "0";
            String MemberPostCount = RS.getString("user_posts");
            
//            System.out.println(Utils.DateTimeFromS(RS.getLong("user_regdate"), timeZone));
            
            String MemberCreationDate = Utils.HauDateTimeFromS(RS.getLong("user_regdate"), timeZone);
            MemberCreationDate = Utils.wrapit(MemberCreationDate);
            MemberCreationDate = MemberCreationDate.replaceAll("24:", "00:");
            
            String MemberModifiedDate = MemberCreationDate;
            String MemberExpireDate = MemberCreationDate;
            
            String MemberLastLogon = Utils.HauDateTimeFromS(RS.getLong("user_lastvisit"), timeZone);
            MemberLastLogon = Utils.wrapit(MemberLastLogon);
            MemberLastLogon = MemberLastLogon.replaceFirst("24:", "00:");
            
            String MemberOption = "0";
            String MemberStatus = "0";
            String MemberActivateCode = Utils.wrapit("");
            String MemberTempPassword = Utils.wrapit("");
            String MemberMessageCount = "0";
            String MemberMessageOption = "0";
            String MemberPostsPerPage = "10"; //Default by mvnForum.
            String MemberWarnCount = "0";
            String MemberVoteCount = "0";
            String MemberVoteTotalStars = "0";
            String MemberRewardPoints = "0";
            String MemberTitle = Utils.wrapit("");
            
            String MemberTimeZone = "" + timeZone;
            
            String MemberSignature = Utils.wrapit(RS.getString("user_sig"));
            
            String MemberAvatar = Utils.wrapit(RS.getString("user_avatar"));
            String MemberSkin = Utils.wrapit(""); 
            String MemberLanguage = Utils.wrapit(RS.getString("user_lang"));
            
            //PHPBB does not store names
            String MemberFirstname = MemberName;
            String MemberLastname = Utils.wrapit("");
            
            //PHPBB does not store gender. This can be an issue. Everyone will be made a female
            String MemberGender = "0";
            
            String MemberBirthday = Utils.DateFromS(RS.getLong("user_regdate"));
            MemberBirthday = Utils.wrapit(MemberBirthday);
            
            String MemberAddress = Utils.wrapit(RS.getString("user_from"));
            String MemberCity = Utils.wrapit("");
            String MemberState = Utils.wrapit("");
            String MemberCountry = Utils.wrapit("");
            String MemberPhone = Utils.wrapit("");
            String MemberMobile = Utils.wrapit("");
            String MemberFax = Utils.wrapit("");
            String MemberCareer = Utils.wrapit(RS.getString("user_occ"));
            
            String MemberHomepage = Utils.wrapit(RS.getString("user_website"));
            String MemberYahoo = Utils.wrapit(RS.getString("user_yim"));
            String MemberAol = Utils.wrapit(RS.getString("user_aim"));
            String MemberIcq = Utils.wrapit("");
             String MemberMsn = Utils.wrapit(RS.getString("user_msnm")); 
             
             // No interests collumn in MvnForum. We will just put the interests in CoolLink1 so that
             // no data is lost. 
             String MemberCoolLink1 =  Utils.wrapit(RS.getString("user_interests"));
             String MemberCoolLink2 = Utils.wrapit("");
             
             
             /*
            // table FORUM_PROFILE
            String st1 = "(MEMBER_ID, VIEW_COUNT, POST_COUNT, SIGNATURE, HOME_PAGE, YAHOO_ID, AOL_ID, ICQ, MSN_ID, COOL_LINK1, COOL_LINK2)";
            String st2 = "(" + MemberID + "," + MemberViewCount + "," + MemberPostCount + "," + MemberSignature + "," + MemberHomepage + "," + MemberYahoo + "," + MemberAol + "," + MemberIcq + "," + MemberMsn + "," + MemberCoolLink1 + "," + MemberCoolLink2 + ")";
            String insertStatement = "insert into FORUM_PROFILE " + st1 + " values " + st2;
            */
            
            
             /*
            // table MEMBER
            String st1 = "(ACTOR_ID, EMAIL_ADDRESS, MEMBER_PASSWORD, FIRST_NAME, LAST_NAME)";
            String st2 = "(" + MemberID + "," + MemberID + "," + MemberID + "," + MemberID + "," + MemberID + ")";   

            String insertStatement = "insert into MEMBER " + st1 + " values " + st2;
            */
             
            if (mvn != null) {
                
                // insert new data into table FORUM_PROFILE
                String st1 = "(MEMBER_ID, VIEW_COUNT, POST_COUNT, SIGNATURE, HOME_PAGE, YAHOO_ID, AOL_ID, ICQ, MSN_ID, COOL_LINK1, COOL_LINK2)";
                String st2 = "(" + MemberID + "," + MemberViewCount + "," + MemberPostCount + "," + MemberSignature + "," + MemberHomepage + "," + MemberYahoo + "," + MemberAol + "," + MemberIcq + "," + MemberMsn + "," + MemberCoolLink1 + "," + MemberCoolLink2 + ")";
                String insertStatement = "insert into FORUM_PROFILE " + st1 + " values " + st2;
                PreparedStatement ps = mvn.prepareStatement(insertStatement);
                ps.executeUpdate();
                ps.close();
                
                // update data in table ACTOR
                String updateStatement = "UPDATE PEERFLIX.ACTOR a JOIN peerflix_phpbb.phpbb_users u ON a.ID = u.user_peerflix_id SET a.AVATAR_IMAGE = ?";
                ps = mvn.prepareStatement(updateStatement);
                
                String avatarFileName = RS.getString("user_avatar");
                String pathToAvatar = ""; // config this variable if necessary
                
                File fileIn = new File(pathToAvatar + avatarFileName);
                if (fileIn.exists()) {
                    int fileLength = (int) fileIn.length();
                    InputStream streamedImage = new FileInputStream(fileIn);
                    ps.setBinaryStream(1, streamedImage, fileLength);

                    ps.executeUpdate();
                    ps.close();

                    userCount++;
                    System.out.println(userCount + ":" + MemberName + " updated!");
                }
            }
        }
    
        // Clean up after ourselves
        RS.close();
        Stmt.close();
    }
    
    public static void migratecategories(Connection php, Connection mvn, PrintStream ostream) throws SQLException
    {
        Statement Stmt = php.createStatement();
        ResultSet RS = Stmt.executeQuery("SELECT * from phpbb_categories");

        int categoryCount = 0;
        
        while(RS.next()) 
        {
            String CategoryID = RS.getString("cat_id");
            String ParentCategoryID = "0";
            String CategoryName = Utils.wrapit(RS.getString("cat_title"));
            String CategoryDesc = Utils.wrapit("");
            
            // get current time, because phpbb does not have CategoryCreationDate
            Date x = new java.util.Date();
            String CategoryCreationDate = Utils.wrapit(Utils.DateTimeFromS(x.getTime()/1000));
            CategoryCreationDate = CategoryCreationDate.replaceFirst("24:", "00:");
            
            String CategoryModifiedDate = CategoryCreationDate;
            
            String CategoryOrder = RS.getString("cat_order");
            String CategoryOption = "0";
            String CategoryStatus = "0";
        
            String insertStatement = "insert into mvnforumCategory " + 
                                     "(CategoryID," +
                                     "ParentCategoryID," +
                                     "CategoryName," +
                                     "CategoryDesc," +
                                     "CategoryCreationDate," +
                                     "CategoryModifiedDate," +
                                     "CategoryOrder," +
                                     "CategoryOption," +
                                     "CategoryStatus" +
                                     ")" +
                                     "values (" +
                                     CategoryID + "," +
                                     ParentCategoryID + "," +
                                     CategoryName + "," +
                                     CategoryDesc + "," +
                                     CategoryCreationDate + "," +
                                     CategoryModifiedDate + "," +
                                     CategoryOrder + "," +
                                     CategoryOption + "," +
                                     CategoryStatus +  ");";
            
//            ostream.println(insertStatement);
            if (mvn != null) {
                PreparedStatement ps = mvn.prepareStatement(insertStatement);
                ps.executeUpdate();
                ps.close();
                
                categoryCount++;
                System.out.println(categoryCount + ":" + CategoryName + " added!");
            }
        }
    
        // Clean up after ourselves
        RS.close();
        Stmt.close();
    }
    
    public static void migrateforums(Connection php, Connection mvn, PrintStream ostream) throws SQLException {
    
        Statement Stmt = php.createStatement();
        ResultSet RS = Stmt.executeQuery("SELECT * from phpbb_forums");
        
        Date x = new java.util.Date();
    
        int forumCount = 0;
        
        while(RS.next()) 
        {
            String ForumID = RS.getString("forum_id"); 
            String CategoryID = RS.getString("cat_id");
            
            String last_post_id = RS.getString("forum_last_post_id");
            Statement lastpoststmt = php.createStatement();
            ResultSet lastpostrs = lastpoststmt.executeQuery("SELECT poster_id from phpbb_posts where post_id=" + last_post_id);
            
            lastpostrs.next();
            String poster_id = lastpostrs.getString("poster_id");
            
            lastpostrs.close();
            lastpoststmt.close();

            Statement userstmt = php.createStatement();
            ResultSet userstmtrs = userstmt.executeQuery("SELECT username from phpbb_users where user_id=" +poster_id);
            
            userstmtrs.next();
            String LastPostMemberName = Utils.wrapit(userstmtrs.getString("username"));
            
            userstmtrs.close();
            userstmt.close();
            
            String ForumName = Utils.wrapit(RS.getString("forum_name")); 
            String ForumDesc = Utils.wrapit(RS.getString("forum_desc"));
            
            // get current time, because phpbb does not have ForumCreationDate
            String ForumCreationDate = Utils.wrapit(Utils.DateTimeFromS(x.getTime()/1000));
            ForumCreationDate = ForumCreationDate.replaceFirst("24:", "00:");
            
            String ForumModifiedDate = ForumCreationDate; 
            String ForumLastPostDate = ForumCreationDate;
            
            String ForumOrder = RS.getString("forum_order");
            String ForumType = "0";
            String ForumFormatOption = "0";
            String ForumOption = "0";
            String ForumStatus = RS.getString("forum_status");
            String ForumModerationMode = "0";
            String ForumPassword = Utils.wrapit("");
            String ForumThreadCount = RS.getString("forum_topics");
            String ForumPostCount = RS.getString("forum_posts");

            String insertStatement = "insert into mvnforumForum " +
                                     "( ForumID, CategoryID, LastPostMemberName," +
                                     "ForumName, ForumDesc, ForumCreationDate, ForumModifiedDate," +
                                     "ForumLastPostDate, ForumOrder, ForumType, ForumFormatOption," +
                                     "ForumOption, ForumStatus, ForumModerationMode, ForumPassword," +
                                     "ForumThreadCount, ForumPostCount ) values (" +
                                         ForumID + ", " +
                                        CategoryID + ", " + 
                                        LastPostMemberName + ", " + 
                                        ForumName + ", " + 
                                        ForumDesc + ", " + 
                                        ForumCreationDate + ", " +
                                        ForumModifiedDate + ", " +
                                        ForumLastPostDate + ", " +
                                        ForumOrder + ", " +
                                        ForumType + ", " +
                                        ForumFormatOption + ", " + 
                                        ForumOption + ", " +
                                        ForumStatus + ", " +
                                        ForumModerationMode + ", " + 
                                        ForumPassword + ", " +
                                        ForumThreadCount + ", " + 
                                        ForumPostCount + ");";
            
//            ostream.println(insertStatement);
            if (mvn != null)
            {
                PreparedStatement ps = mvn.prepareStatement(insertStatement);
                ps.executeUpdate();
                ps.close();
                
                forumCount++;
                System.out.println(forumCount + ":" + ForumName + " added!");
            }
        }
    
        // Clean up after ourselves
        RS.close();
        Stmt.close();
    }
    
    public static String delete1Char(String string, int index) {
        
        return string.substring(0, index) +  string.substring(index + 1, string.length());
        
    }
    
    public static String FormalizeString(String postBody, String token) {
    
        int length = token.length();
        
        int index = -1;
        
        while ((index = postBody.indexOf(token, index + 1)) != -1) {
            
            String currentChar = postBody.substring(index + length, index + length + 1);
            
            while (!currentChar.equals("]")) {
                
                postBody = delete1Char(postBody, index + length);
                currentChar = postBody.substring(index + length, index + length + 1);
                
            }
            
        }
        
        return postBody;
        
    }
    
    public static void migrateposts(Connection php, Connection mvn, PrintStream ostream) throws SQLException
    {
        Statement Stmt = php.createStatement();
        ResultSet RS = Stmt.executeQuery("SELECT * from phpbb_posts");
        
        int postCount = 0;

        while(RS.next()) 
        {
            String PostID = RS.getString("post_id");
            String ForumID = RS.getString("forum_id");
            String ThreadID = RS.getString("topic_id");
            String MemberID = RS.getString("poster_id");
            
            Statement ppidstmt = php.createStatement();
            ResultSet ppidrs = ppidstmt.executeQuery("SELECT post_id from phpbb_posts where topic_id=" + ThreadID + " ORDER by post_id");
            
            String ppid = "0";
            while(ppidrs.next())
            {
                String ppidtmp = ppidrs.getString("post_id");
                if (ppidtmp.equals(PostID)) {
                    break;
                } else {
                    ppid = ppidtmp;
                }
            }
    
            ppidrs.close();
            ppidstmt.close();

            String ParentPostID = ppid;
            
            Statement userstmt = php.createStatement();
            ResultSet userstmtrs = userstmt.executeQuery("SELECT username,user_timezone from phpbb_users where user_id=" + MemberID);
            userstmtrs.next();
            String MemberName = Utils.wrapit(userstmtrs.getString("username"));
            
            int timeZone = userstmtrs.getInt("user_timezone"); 
            
            userstmtrs.close();
            userstmt.close();
            
            String LastEditMemberName = MemberName;
            
            Statement posttextstmt = php.createStatement();
            ResultSet posttextrs = posttextstmt.executeQuery("SELECT * from phpbb_posts_text where post_id=" + PostID);
        
            posttextrs.next();
            String PostTopic = Utils.wrapit(posttextrs.getString("post_subject"));
            
//            String PostBody = Utils.wrapit(Utils.stripPHPBBQuotes(posttextrs.getString("post_text")));
            
            String PostBody = posttextrs.getString("post_text");
            
//            if (PostBody.indexOf("[b:cd47965b57]BOLDED[/b:cd47965b57]") != -1) {
//                System.out.println("error");
//            }
            
            PostBody = FormalizeString(PostBody, "[b");
            PostBody = FormalizeString(PostBody, "[quote");
            PostBody = FormalizeString(PostBody, "[/b");
            PostBody = FormalizeString(PostBody, "[/quote");
            
            
            
            PostBody = Utils.wrapit(Utils.stripPHPBBQuotes(PostBody));
            
//            if (postCount == 5294) {
//                System.out.println(PostBody);
//                System.out.println(PostID);
//            }
            
            posttextrs.close();
            posttextstmt.close();
            
            String PostCreationDate = Utils.HauDateTimeFromS(RS.getLong("post_time"), timeZone);
            PostCreationDate = Utils.wrapit(PostCreationDate);
            PostCreationDate = PostCreationDate.replaceFirst("24:", "00:");
            
            String PostLastEditDate = PostCreationDate;
            
            String PostCreationIP = Utils.wrapit(Utils.HexIPtoString(RS.getString("poster_ip")));
            String PostLastEditIP = PostCreationIP;
            String PostEditCount = RS.getString("post_edit_count");
            String PostFormatOption = "0";
            String PostOption = "0";
            String PostStatus = "0";
            String PostIcon = Utils.wrapit("");
            String PostAttachCount = "0";

            String insertStatement = "insert into mvnforumPost " +
            "(PostID, ParentPostID, ForumID, ThreadID," +
            "MemberID, MemberName, LastEditMemberName," +
            "PostTopic, PostBody, PostCreationDate," +
            "PostLastEditDate, PostCreationIP," +
            "PostLastEditIP, PostEditCount, PostFormatOption," +
            "PostOption, PostStatus, PostIcon, PostAttachCount" +
            ") values ( " +
            PostID + "," + 
            ParentPostID + "," +
            ForumID + "," + 
            ThreadID + "," +
            MemberID + "," + 
            MemberName + "," + 
            LastEditMemberName + "," +
            PostTopic + "," + 
            PostBody + "," +
            PostCreationDate + "," + 
            PostLastEditDate + "," + 
            PostCreationIP + "," + 
            PostLastEditIP + "," + 
            PostEditCount + "," + 
            PostFormatOption + "," + 
            PostOption + "," + 
            PostStatus + "," + 
            PostIcon + "," +
            PostAttachCount + ");";
            
//            ostream.println(insertStatement);
            
            if (mvn != null) {
                PreparedStatement ps = mvn.prepareStatement(insertStatement);
                ps.executeUpdate();
                ps.close();
                
                postCount++;
                System.out.println(postCount + ":" + PostID + " added!");
            }
        }
    
        // Clean up after ourselves
        RS.close();
        Stmt.close();
    }
    
    public static void migratethreads(Connection php, Connection mvn, PrintStream ostream) throws SQLException
    {
        Statement Stmt = php.createStatement();
        ResultSet RS = Stmt.executeQuery("SELECT * from phpbb_topics");
        
        int threadCount = 0;
    
        while(RS.next()) {
            
            String ThreadID = RS.getString("topic_id");

            String ForumID = RS.getString("forum_id");
            String MemberName = Utils.wrapit(usernamefromid(php, RS.getString("topic_poster")));

            String LastPostMemberName = "";
            Statement lstmt = php.createStatement();
            ResultSet lrs = lstmt.executeQuery("select poster_id from phpbb_posts where post_id = " + RS.getString("topic_last_post_id"));
            if (lrs.next()) {
                LastPostMemberName = usernamefromid(php, lrs.getString("poster_id"));
            }
            lrs.close();
            lstmt.close();
            
            LastPostMemberName = Utils.wrapit(LastPostMemberName);
            String ThreadTopic = Utils.wrapit(RS.getString("topic_title"));
            
            String ThreadBody = "";
            String first_post_id = RS.getString("topic_first_post_id");
            lstmt = php.createStatement();
            lrs = lstmt.executeQuery("select post_text from phpbb_posts_text where post_id = " + first_post_id);
            
            if (lrs.next()) {
                ThreadBody = lrs.getString("post_text");
                
                ThreadBody = FormalizeString(ThreadBody, "[b");
                ThreadBody = FormalizeString(ThreadBody, "[quote");
                ThreadBody = FormalizeString(ThreadBody, "[/b");
                ThreadBody = FormalizeString(ThreadBody, "[/quote");
            }
            lrs.close();
            lstmt.close();
            
            ThreadBody = Utils.wrapit(Utils.stripPHPBBQuotes(ThreadBody));
            
            String ThreadVoteCount = "0";
            String ThreadVoteTotalStars = "0";
            
            String userID = RS.getString("topic_poster");
            int timeZone = timeZoneFromId(php, userID);
            
            String temp1 = Utils.HauDateTimeFromS(RS.getLong("topic_time"), timeZone);
//            String ThreadCreationDate  = Utils.wrapit(Utils.HauDateTimeFromS(RS.getLong("topic_time"), timeZone));
            String ThreadCreationDate  = Utils.wrapit(temp1);
            
            long myLong = RS.getLong("topic_time");
//            System.out.println(myLong);
//            System.out.println(Utils.HauDateTimeFromS(myLong, timeZone));
            
//            String HauThreadCreationDate  = Utils.wrapit(Utils.HauDateTimeFromS(RS.getLong("topic_time"), timeZone));
            
//            System.out.println("Hau: " + HauThreadCreationDate);
//            System.out.println("anan: " + ThreadCreationDate);
            
            
            ThreadCreationDate = ThreadCreationDate.replaceFirst("24:", "00:");
            
            String ThreadLastPostDate = "";
            String last_post_id = RS.getString("topic_last_post_id");
            lstmt = php.createStatement();
            lrs = lstmt.executeQuery("select post_time,poster_id from phpbb_posts where post_id = " + last_post_id);
            
            if (lrs.next()) 
            {
                String userId = lrs.getString("poster_id");
                int zone = timeZoneFromId(php, userId);
                
                ThreadLastPostDate = Utils.DateTimeFromS(lrs.getLong("post_time"));
            }
            
            lrs.close();
            lstmt.close();
            
            ThreadLastPostDate = Utils.wrapit(ThreadLastPostDate);
            
            ThreadLastPostDate = ThreadLastPostDate.replaceFirst("24:", "00:");

            String ThreadType  = "0";
            String ThreadOption = "0";
            String ThreadStatus = RS.getString("topic_status"); // TODO: Maybe remove status preservation
            String ThreadHasPoll = RS.getString("topic_vote");
            String ThreadViewCount  = RS.getString("topic_views");
            String ThreadReplyCount = RS.getString("topic_replies");
            String ThreadIcon  = Utils.wrapit("");
            String ThreadDuration = "0";
            String ThreadAttachCount = "0";

            String insertStatement = "insert into mvnforumThread" +
                "(ThreadID, ForumID, MemberName," +
                "LastPostMemberName, ThreadTopic," +
                "ThreadBody, ThreadVoteCount," +
                "ThreadVoteTotalStars, ThreadCreationDate," +
                "ThreadLastPostDate, ThreadType," +
                "ThreadOption, ThreadStatus," +
                "ThreadHasPoll, ThreadViewCount," +
                "ThreadReplyCount, ThreadIcon," +
                "ThreadDuration, ThreadAttachCount )" +
                "values (" +
                ThreadID + "," +
                ForumID + "," +
                MemberName + "," +
                LastPostMemberName + "," +
                ThreadTopic + "," +
                ThreadBody + "," +
                ThreadVoteCount + "," +
                ThreadVoteTotalStars + "," +
                ThreadCreationDate + "," +
                ThreadLastPostDate + "," +
                ThreadType + "," +
                ThreadOption + "," +
                ThreadStatus + "," +
                ThreadHasPoll + "," +
                ThreadViewCount + "," +
                ThreadReplyCount + "," +
                ThreadIcon + "," +
                ThreadDuration + "," +
                ThreadAttachCount + ");";
            
//                ostream.println(insertStatement);
                if (mvn != null) {
                    PreparedStatement ps = mvn.prepareStatement(insertStatement);
                    ps.executeUpdate();
                    ps.close();
                    
                    threadCount++;
                    System.out.println(threadCount + ":" + ThreadID + " added!");
                }
        }
    
        // Clean up after ourselves
        RS.close();
        Stmt.close();
    }
}
