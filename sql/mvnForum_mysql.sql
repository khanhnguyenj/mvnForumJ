
# $Header: /cvsroot/mvnforum/mvnforum/sql/mvnForum_mysql.sql,v 1.57 2010/04/06 11:11:32 minhnn Exp $
# $Author: minhnn $
# $Revision: 1.57 $
# $Date: 2010/04/06 11:11:32 $
# Database : MySql
# Driver   : com.mysql.jdbc.Driver or org.gjt.mm.mysql.Driver
# Url      : jdbc:mysql://localhost/<database>?useUnicode=true&characterEncoding=utf-8&useOldUTF8Behavior=true
# Url for MySql 4.1 or later : jdbc:mysql://localhost/mvnforum?useServerPrepStmts=false
# MySql 4.1.x or later should read this: http://dev.mysql.com/doc/refman/5.0/en/connector-j-installing-upgrading.html

# Things should be considered when port this file to other database
# AUTO_INCREMENT : AUTO_INCREMENT
# LONGVARCHAR    : TEXT
# DATE           : DATE
# TIMESTAMP      : DATETIME
# VARCHAR        : VARCHAR
# FLOAT          : FLOAT
# BLOB           : LONGBLOB
# now()          : now()

# NOTE: you can run sql script in mysql, the command look like this
# cd C:\mysql\bin
# mysql -uroot -Dmvnforum < C:\mvnforum\sql\mvnForum_mvnforum.sql
# or after you connect to mysql, run this at the mysql prompt
# source C:\mvnforum\sql\mvnForum_mvnforum.sql

# NOTE: you should change mysql root password:
# SET password=password("newpassword");

# NOTE: how to grant permission when have "Access denied"
# GRANT ALL PRIVILEGES ON [dbname].* to '[user]'@'[hostname]' identified by '[password]'

# MYSQL DRIVER USING NOTES (FOR UNICODE) : FOR MYSQL 4.1.x and later

#0. Resource: http://dev.mysql.com/doc/connector/j/en/cj-jdbc-reference.html
#             http://dev.mysql.com/doc/connector/j/en/cj-character-sets.html
#
#
#1. Create mvnforum database with the "Create database" syntax (for unicode and others):
#   mysql> CREATE DATABASE mvnforum CHARACTER SET [charater_set] COLLATE [collation]
#
#   Where charater_set and collation : @see http://dev.mysql.com/doc/refman/4.1/en/charset-mysql.html
#
#   a, practice to view all supported character set
#      mysql> SHOW CHARACTER SET;
#   b, practice to view all supported collation:
#      mysql> SHOW COLLATION;
#
#   c, Example for Unicode:
#      mysql> CREATE DATABASE mvnforum DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci
#
#  (*) The default character set that will be used when a new schema or table is
#      created and no character set is defined
#      We should configure mysql-server with: default-character-set=utf8,
#      if we have plan site support multi language
#
#2. The character encoding between client and server is automatically detected upon
#   connection. The encoding used by the driver is specified on the server via the
#   configuration variable 'character_set' for server versions older than 4.1.0
#   and 'character_set_server' for server versions 4.1.0 and newer.
#
#      Examples (for server versions 4.1.0 and newer):
#         mysql> SET CHARACTER_SET_SERVER=utf8;
#
#   (*) To override the automatically-detected encoding on the client side,
#   use the "characterEncoding" property in the URL used to connect to
#   the server (same to old way).
#
#3. It is OK, now. But we should use the driver has version corresponding
#   to the database version.
#
#4. The database connection URL should be like this: jdbc:mysql://localhost/mvnforum

# Uncomment the following drop table command if you want to drop the tables
# Note: drop tables will delete all the data in them.
# Note: you should always backup your data before run the script

# DROP TABLE IF EXISTS mvnforumCategory;
# DROP TABLE IF EXISTS mvnforumForum;
# DROP TABLE IF EXISTS mvnforumGroupForum;
# DROP TABLE IF EXISTS mvnforumGroupPermission;
# DROP TABLE IF EXISTS mvnforumGroups;
# DROP TABLE IF EXISTS mvnforumMember;
# DROP TABLE IF EXISTS mvnforumMemberGroup;
# DROP TABLE IF EXISTS mvnforumMemberPermission;
# DROP TABLE IF EXISTS mvnforumMessageFolder;
# DROP TABLE IF EXISTS mvnforumPost;
# DROP TABLE IF EXISTS mvnforumThread;
# DROP TABLE IF EXISTS mvnforumWatch;
# DROP TABLE IF EXISTS mvnforumAttachment;
# DROP TABLE IF EXISTS mvnforumMemberForum;
# DROP TABLE IF EXISTS mvnforumFavoriteThread;
# DROP TABLE IF EXISTS mvnforumRank;
# DROP TABLE IF EXISTS mvnforumMessage;
# DROP TABLE IF EXISTS mvnforumMessageStatistics;
# DROP TABLE IF EXISTS mvnforumPmAttachment;
# DROP TABLE IF EXISTS mvnforumPmAttachMessage;


CREATE TABLE IF NOT EXISTS mvnforumCategory
(
   CategoryID                     INT                            NOT NULL AUTO_INCREMENT,
   ParentCategoryID               INT                            NOT NULL,
   CategoryName                   VARCHAR(250)                   NOT NULL,
   CategoryDesc                   TEXT                           NOT NULL,
   CategoryCreationDate           DATETIME                       NOT NULL,
   CategoryModifiedDate           DATETIME                       NOT NULL,
   CategoryOrder                  SMALLINT                       NOT NULL,
   CategoryOption                 INT                            NOT NULL,
   CategoryStatus                 INT                            NOT NULL,
   PRIMARY KEY (CategoryID),
   UNIQUE (CategoryName)
);

CREATE TABLE IF NOT EXISTS mvnforumForum
(
   ForumID                        INT                            NOT NULL AUTO_INCREMENT,
   CategoryID                     INT                            NOT NULL,
   ForumOwnerName                 VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ForumName                      VARCHAR(250)                   NOT NULL,
   ForumDesc                      TEXT                           NOT NULL,
   ForumCreationDate              DATETIME                       NOT NULL,
   ForumModifiedDate              DATETIME                       NOT NULL,
   ForumLastPostDate              DATETIME                       NOT NULL,
   ForumOrder                     SMALLINT                       NOT NULL,
   ForumType                      INT                            NOT NULL,
   ForumFormatOption              INT                            NOT NULL,
   ForumOption                    INT                            NOT NULL,
   ForumStatus                    INT                            NOT NULL,
   ForumModerationMode            INT                            NOT NULL,
   ForumPassword                  VARCHAR(40)                    NOT NULL,
   ForumThreadCount               INT                            NOT NULL,
   ForumPostCount                 INT                            NOT NULL,
   PRIMARY KEY (ForumID),
   UNIQUE (ForumName, CategoryID)
);

CREATE INDEX Forum_CatID_idx on mvnforumForum
(
   CategoryID
);

CREATE TABLE IF NOT EXISTS mvnforumGroupForum
(
   GroupID                        INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (GroupID, ForumID, Permission)
);

CREATE INDEX GroupForum_1_idx on mvnforumGroupForum
(
   GroupID
);

CREATE INDEX GroupForum_2_idx on mvnforumGroupForum
(
   ForumID
);

CREATE TABLE IF NOT EXISTS mvnforumGroupPermission
(
   GroupID                        INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (GroupID, Permission)
);

CREATE INDEX GroupPermission_1_idx on mvnforumGroupPermission
(
   GroupID
);

CREATE TABLE IF NOT EXISTS mvnforumGroups
(
   GroupID                        INT                            NOT NULL AUTO_INCREMENT,
   GroupOwnerID                   INT                            NOT NULL,
   GroupOwnerName                 VARCHAR(30)                    NOT NULL,
   GroupName                      VARCHAR(250)                   NOT NULL,
   GroupDesc                      TEXT                           NOT NULL,
   GroupOption                    INT                            NOT NULL,
   GroupCreationDate              DATETIME                       NOT NULL,
   GroupModifiedDate              DATETIME                       NOT NULL,
   PRIMARY KEY (GroupID),
   UNIQUE (GroupName)
);

CREATE TABLE IF NOT EXISTS mvnforumMember
(
   MemberID                       INT                            NOT NULL AUTO_INCREMENT,
   MemberName                     VARCHAR(30)                    NOT NULL,
   MemberPassword                 VARCHAR(200)                   NOT NULL,
   MemberFirstEmail               VARCHAR(60)                    NOT NULL,
   MemberEmail                    VARCHAR(60)                    NOT NULL,
   MemberEmailVisible             INT                            NOT NULL,
   MemberNameVisible              INT                            NOT NULL,
   MemberFirstIP                  VARCHAR(20)                    NOT NULL,
   MemberLastIP                   VARCHAR(20)                    NOT NULL,
   MemberViewCount                INT                            NOT NULL,
   MemberPostCount                INT                            NOT NULL,
   MemberCreationDate             DATETIME                       NOT NULL,
   MemberModifiedDate             DATETIME                       NOT NULL,
   MemberExpireDate               DATETIME                       NOT NULL,
   MemberPasswordExpireDate       DATETIME                       NOT NULL,
   MemberLastLogon                DATETIME                       NOT NULL,
   MemberOption                   INT                            NOT NULL,
   MemberStatus                   INT                            NOT NULL,
   MemberActivateCode             VARCHAR(40)                    NOT NULL,
   MemberTempPassword             VARCHAR(40)                    NOT NULL,
   MemberMessageCount             INT                            NOT NULL,
   MemberMessageOption            INT                            NOT NULL,
   MemberPostsPerPage             INT                            NOT NULL,
   MemberWarnCount                INT                            NOT NULL,
   MemberVoteCount                INT                            NOT NULL,
   MemberVoteTotalStars           INT                            NOT NULL,
   MemberRewardPoints             INT                            NOT NULL,
   MemberTitle                    VARCHAR(250)                   NOT NULL,
   MemberTimeZone                 FLOAT                          NOT NULL,
   MemberSignature                VARCHAR(250)                   NOT NULL,
   MemberAvatar                   VARCHAR(200)                   NOT NULL,
   MemberSkin                     VARCHAR(70)                    NOT NULL,
   MemberLanguage                 VARCHAR(70)                    NOT NULL,
   MemberFirstname                VARCHAR(70)                    NOT NULL,
   MemberLastname                 VARCHAR(70)                    NOT NULL,
   MemberGender                   INT                            NOT NULL,
   MemberBirthday                 DATE                           NOT NULL,
   MemberAddress                  VARCHAR(150)                   NOT NULL,
   MemberCity                     VARCHAR(70)                    NOT NULL,
   MemberState                    VARCHAR(70)                    NOT NULL,
   MemberCountry                  VARCHAR(70)                    NOT NULL,
   MemberPhone                    VARCHAR(40)                    NOT NULL,
   MemberMobile                   VARCHAR(40)                    NOT NULL,
   MemberFax                      VARCHAR(40)                    NOT NULL,
   MemberCareer                   VARCHAR(50)                    NOT NULL,
   MemberHomepage                 VARCHAR(200)                   NOT NULL,
   MemberYahoo                    VARCHAR(70)                    NOT NULL,
   MemberAol                      VARCHAR(70)                    NOT NULL,
   MemberIcq                      VARCHAR(70)                    NOT NULL,
   MemberMsn                      VARCHAR(70)                    NOT NULL,
   MemberCoolLink1                VARCHAR(200)                   NOT NULL,
   MemberCoolLink2                VARCHAR(200)                   NOT NULL,
   PRIMARY KEY (MemberID),
   UNIQUE (MemberEmail),
   UNIQUE (MemberName)
);

CREATE TABLE IF NOT EXISTS mvnforumMemberGroup
(
   GroupID                        INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   Privilege                      INT                            NOT NULL,
   CreationDate                   DATETIME                       NOT NULL,
   ModifiedDate                   DATETIME                       NOT NULL,
   ExpireDate                     DATETIME                       NOT NULL,
   PRIMARY KEY (GroupID, MemberID)
);

CREATE INDEX MemberGroup_1_idx on mvnforumMemberGroup
(
   MemberID
);

CREATE INDEX MemberGroup_2_idx on mvnforumMemberGroup
(
   GroupID
);

CREATE TABLE IF NOT EXISTS mvnforumMemberPermission
(
   MemberID                       INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (MemberID, Permission)
);

CREATE INDEX MemberPermission_1_idx on mvnforumMemberPermission
(
   MemberID
);

CREATE TABLE IF NOT EXISTS mvnforumMessageFolder
(
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INT                            NOT NULL,
   FolderOrder                    SMALLINT                       NOT NULL,
   FolderStatus                   INT                            NOT NULL,
   FolderOption                   INT                            NOT NULL,
   FolderType                     INT                            NOT NULL,
   FolderCreationDate             DATETIME                       NOT NULL,
   FolderModifiedDate             DATETIME                       NOT NULL,
   PRIMARY KEY (FolderName, MemberID)
);

CREATE INDEX MessageFolder_1_idx on mvnforumMessageFolder
(
   MemberID
);

CREATE TABLE IF NOT EXISTS mvnforumPost
(
   PostID                         INT                            NOT NULL AUTO_INCREMENT,
   ParentPostID                   INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastEditMemberName             VARCHAR(30)                    NOT NULL,
   PostTopic                      VARCHAR(250)                   NOT NULL,
   PostBody                       TEXT                           NOT NULL,
   PostCreationDate               DATETIME                       NOT NULL,
   PostLastEditDate               DATETIME                       NOT NULL,
   PostCreationIP                 VARCHAR(20)                    NOT NULL,
   PostLastEditIP                 VARCHAR(20)                    NOT NULL,
   PostEditCount                  SMALLINT                       NOT NULL,
   PostFormatOption               INT                            NOT NULL,
   PostOption                     INT                            NOT NULL,
   PostStatus                     INT                            NOT NULL,
   PostIcon                       VARCHAR(10)                    NOT NULL,
   PostAttachCount                SMALLINT                       NOT NULL,
   PRIMARY KEY (PostID)
);

CREATE INDEX Post_1_idx on mvnforumPost
(
   ForumID
);

CREATE INDEX Post_2_idx on mvnforumPost
(
   ThreadID
);

CREATE INDEX Post_3_idx on mvnforumPost
(
   MemberID
);

CREATE INDEX Post_4_idx on mvnforumPost
(
   ParentPostID
);

CREATE TABLE IF NOT EXISTS mvnforumThread
(
   ThreadID                       INT                            NOT NULL AUTO_INCREMENT,
   ForumID                        INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ThreadTopic                    VARCHAR(250)                   NOT NULL,
   ThreadBody                     TEXT                           NOT NULL,
   ThreadVoteCount                INT                            NOT NULL,
   ThreadVoteTotalStars           INT                            NOT NULL,
   ThreadCreationDate             DATETIME                       NOT NULL,
   ThreadLastPostDate             DATETIME                       NOT NULL,
   ThreadType                     INT                            NOT NULL,
   ThreadPriority                 INT                            NOT NULL,
   ThreadOption                   INT                            NOT NULL,
   ThreadStatus                   INT                            NOT NULL,
   ThreadHasPoll                  INT                            NOT NULL,
   ThreadViewCount                INT                            NOT NULL,
   ThreadReplyCount               INT                            NOT NULL,
   ThreadIcon                     VARCHAR(10)                    NOT NULL,
   ThreadDuration                 INT                            NOT NULL,
   ThreadAttachCount              INT                            NOT NULL,
   PRIMARY KEY (ThreadID)
);

CREATE INDEX Thread_1_idx on mvnforumThread
(
   ForumID
);

CREATE TABLE IF NOT EXISTS mvnforumWatch
(
   WatchID                        INT                            NOT NULL AUTO_INCREMENT,
   MemberID                       INT                            NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   WatchType                      INT                            NOT NULL,
   WatchOption                    INT                            NOT NULL,
   WatchStatus                    INT                            NOT NULL,
   WatchCreationDate              DATETIME                       NOT NULL,
   WatchLastSentDate              DATETIME                       NOT NULL,
   WatchEndDate                   DATETIME                       NOT NULL,
   PRIMARY KEY (WatchID),
   UNIQUE (MemberID, CategoryID, ForumID, ThreadID)
);

CREATE INDEX Watch_MemberID_idx on mvnforumWatch
(
   MemberID
);

CREATE INDEX Watch_CategoryID_idx on mvnforumWatch
(
   CategoryID
);

CREATE INDEX Watch_ForumID_idx on mvnforumWatch
(
   ForumID
);

CREATE INDEX Watch_ThreadID_idx on mvnforumWatch
(
   ThreadID
);

CREATE TABLE IF NOT EXISTS mvnforumAttachment
(
   AttachID                       INT                            NOT NULL AUTO_INCREMENT,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 VARCHAR(250)                   NOT NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 VARCHAR(70)                    NOT NULL,
   AttachDesc                     TEXT                           NOT NULL,
   AttachCreationIP               VARCHAR(20)                    NOT NULL,
   AttachCreationDate             DATETIME                       NOT NULL,
   AttachModifiedDate             DATETIME                       NOT NULL,
   AttachDownloadCount            INT                            NOT NULL,
   AttachOption                   INT                            NOT NULL,
   AttachStatus                   INT                            NOT NULL,
   PRIMARY KEY (AttachID)
);

CREATE INDEX Attachment_PostID_idx on mvnforumAttachment
(
   PostID
);

CREATE INDEX Attachment_MemberID_idx on mvnforumAttachment
(
   MemberID
);

CREATE TABLE IF NOT EXISTS mvnforumMemberForum
(
   MemberID                       INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (MemberID, ForumID, Permission)
);

CREATE INDEX MemberForum_1_idx on mvnforumMemberForum
(
   MemberID
);

CREATE INDEX MemberForum_2_idx on mvnforumMemberForum
(
   ForumID
);

CREATE TABLE IF NOT EXISTS mvnforumFavoriteThread
(
   MemberID                       INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   FavoriteCreationDate           DATETIME                       NOT NULL,
   FavoriteType                   INT                            NOT NULL,
   FavoriteOption                 INT                            NOT NULL,
   FavoriteStatus                 INT                            NOT NULL,
   PRIMARY KEY (MemberID, ThreadID)
);

CREATE INDEX FavorThread_1_idx on mvnforumFavoriteThread
(
   MemberID
);

CREATE INDEX FavorThread_2_idx on mvnforumFavoriteThread
(
   ThreadID
);

CREATE TABLE IF NOT EXISTS mvnforumRank
(
   RankID                         INT                            NOT NULL AUTO_INCREMENT,
   RankMinPosts                   INT                            NOT NULL,
   RankLevel                      INT                            NOT NULL,
   RankTitle                      VARCHAR(250)                   NOT NULL,
   RankImage                      VARCHAR(250)                   NOT NULL,
   RankType                       INT                            NOT NULL,
   RankOption                     INT                            NOT NULL,
   PRIMARY KEY (RankID),
   UNIQUE (RankMinPosts),
   UNIQUE (RankTitle)
);

CREATE TABLE IF NOT EXISTS mvnforumMessage
(
   MessageID                      INT                            NOT NULL AUTO_INCREMENT,
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INT                            NOT NULL,
   MessageSenderID                INT                            NOT NULL,
   MessageSenderName              VARCHAR(30)                    NOT NULL,
   MessageToList                  VARCHAR(250)                   NOT NULL,
   MessageCcList                  VARCHAR(250),
   MessageBccList                 VARCHAR(250),
   MessageTopic                   VARCHAR(250)                   NOT NULL,
   MessageBody                    TEXT                           NOT NULL,
   MessageType                    INT                            NOT NULL,
   MessageOption                  INT                            NOT NULL,
   MessageStatus                  INT                            NOT NULL,
   MessageReadStatus              INT                            NOT NULL,
   MessageNotify                  INT                            NOT NULL,
   MessageIcon                    VARCHAR(10)                    NOT NULL,
   MessageAttachCount             INT                            NOT NULL,
   MessageIP                      VARCHAR(20)                    NOT NULL,
   MessageCreationDate            DATETIME                       NOT NULL,
   PRIMARY KEY (MessageID)
);

CREATE INDEX Message_1_idx on mvnforumMessage
(
   FolderName,
   MemberID
);

CREATE INDEX Message_2_idx on mvnforumMessage
(
   MessageSenderID
);

CREATE TABLE IF NOT EXISTS mvnforumMessageStatistics
(
   FromID                         INT                            NOT NULL,
   ToID                           INT                            NOT NULL,
   MessageCreationDate            DATETIME                       NOT NULL,
   MessageAttachCount             INT                            NOT NULL,
   MessageType                    INT                            NOT NULL,
   MessageOption                  INT                            NOT NULL,
   MessageStatus                  INT                            NOT NULL
);

CREATE INDEX MessageStatistics_1_idx on mvnforumMessageStatistics
(
   FromID
);

CREATE INDEX MessageStatistics_2_idx on mvnforumMessageStatistics
(
   ToID
);

CREATE TABLE IF NOT EXISTS mvnforumPmAttachment
(
   PmAttachID                     INT                            NOT NULL AUTO_INCREMENT,
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               VARCHAR(250)                   NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               VARCHAR(70)                    NOT NULL,
   PmAttachDesc                   TEXT                           NOT NULL,
   PmAttachCreationIP             VARCHAR(20)                    NOT NULL,
   PmAttachCreationDate           DATETIME                       NOT NULL,
   PmAttachModifiedDate           DATETIME                       NOT NULL,
   PmAttachDownloadCount          INT                            NOT NULL,
   PmAttachOption                 INT                            NOT NULL,
   PmAttachStatus                 INT                            NOT NULL,
   PRIMARY KEY (PmAttachID)
);

CREATE INDEX PmAttachment_1_idx on mvnforumPmAttachment
(
   MemberID
);

CREATE TABLE IF NOT EXISTS mvnforumPmAttachMessage
(
   MessageID                      INT                            NOT NULL,
   PmAttachID                     INT                            NOT NULL,
   RelationType                   INT                            NOT NULL,
   RelationOption                 INT                            NOT NULL,
   RelationStatus                 INT                            NOT NULL,
   PRIMARY KEY (MessageID, PmAttachID)
);


#
# data for table mvnforumMember
#
INSERT INTO mvnforumMember (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2)
                    VALUES ("1", "admin", "ISMvKXpXpadDiUoOSoAfww==", "admin@yourdomain.com", "admin@yourdomain.com", "0", "1",         "127.0.0.1",   "127.0.0.1",  "0",             "0",             now(),              now(),              now(),            now(),                    now(),           "0",          "0",          "",                 "",                 "0",                "0",                 "10",               "0",             "0",             "0",                  "0",                "",          "0",            "",              "",           "",         "",             "",              "",             "1",          now(),          "",            "",         "",          "",            "",          "",           "",        "",           "",             "",          "",        "",        "",        "",              "");


#
# data for table mvnforumMessageFolder
#
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Inbox',    1,        0,           0,            0,            0,          now(),              now());
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Sent',     1,        2,           0,            0,            0,          now(),              now());


#
# data for table mvnforumGroups
#
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ("1",     "0",          "",             "Guest",   "All anonymous users belong to this group.",              "0",         now(),             now());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ("2",     "0",          "",             "Member",  "All registered users belong to this group.",             "0",         now(),             now());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ("3",     "1",          "admin",        "Admin",   "This group have SystemAdmin permission by default.",     "0",         now(),             now());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ("4",     "1",          "admin",        "Forum Admin", "This group have ForumAdmin permission by default.",  "0",         now(),             now());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ("5",     "1",          "admin",        "Forum Moderator", "This group have ForumModerator permission by default.", "0",  now(),             now());


#
# data for table mvnforumMemberGroup
#
INSERT INTO mvnforumMemberGroup (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)
                         VALUES ("3",     "1",      "admin",    "0",       now(),        now(),        now() );



#
# data for table mvnforumMemberPermission
#
INSERT INTO mvnforumMemberPermission (MemberID, Permission)
                              VALUES ("1",      "100");


#
# data for table mvnforumGroupPermission
#
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ("1",     "109");
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ("2",     "110");
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ("3",     "100");
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ("4",     "105");
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ("5",     "106");

#
# data for table mvnforumRank
#
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (0, 0, "Stranger",                   "",        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (20, 0, "Newbie",                    "",        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (50, 0, "Member",                    "",        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (100, 0, "Advanced Member",           "",        0,        0);

