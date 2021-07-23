
-- $Header: /cvsroot/mvnforum/mvnforum/sql/mvnForum_Sybase.sql,v 1.26 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.26 $
-- $Date: 2010/04/06 11:11:32 $
-- Database : Sybase
-- Driver   :
-- Url      :


-- Things should be considered when port this file to other database
-- AUTO_INCREMENT : IDENTITY
-- LONGVARCHAR    : TEXT       Is Sybase support NTEXT as in Sql Server ???
-- DATE           : DATETIME
-- TIMESTAMP      : DATETIME
-- VARCHAR        : NVARCHAR
-- BLOB           : IMAGE
-- now()          : GETDATE()

-- Uncomment the following drop table command if you want to drop the tables
-- Note: drop tables will delete all the data in them.
-- Note: you should always backup your data before run the script

-- DROP TABLE mvnforumCategory;
-- DROP TABLE mvnforumForum;
-- DROP TABLE mvnforumGroupForum;
-- DROP TABLE mvnforumGroupPermission;
-- DROP TABLE mvnforumGroups;
-- DROP TABLE mvnforumMember;
-- DROP TABLE mvnforumMemberGroup;
-- DROP TABLE mvnforumMemberPermission;
-- DROP TABLE mvnforumMessageFolder;
-- DROP TABLE mvnforumPost;
-- DROP TABLE mvnforumThread;
-- DROP TABLE mvnforumWatch;
-- DROP TABLE mvnforumAttachment;
-- DROP TABLE mvnforumMemberForum;
-- DROP TABLE mvnforumFavoriteThread;
-- DROP TABLE mvnforumRank;
-- DROP TABLE mvnforumMessage;
-- DROP TABLE mvnforumMessageStatistics;
-- DROP TABLE mvnforumPmAttachment;
-- DROP TABLE mvnforumPmAttachMessage;


CREATE TABLE mvnforumCategory
(
   CategoryID                     INT                            NOT NULL IDENTITY,
   ParentCategoryID               INT                            NOT NULL,
   CategoryName                   NVARCHAR(250)                  NOT NULL,
   CategoryDesc                   TEXT                           NOT NULL,
   CategoryCreationDate           DATETIME                       NOT NULL,
   CategoryModifiedDate           DATETIME                       NOT NULL,
   CategoryOrder                  INT                            NOT NULL,
   CategoryOption                 INT                            NOT NULL,
   CategoryStatus                 INT                            NOT NULL,
   PRIMARY KEY (CategoryID),
   UNIQUE (CategoryName)
);

CREATE TABLE mvnforumForum
(
   ForumID                        INT                            NOT NULL IDENTITY,
   CategoryID                     INT                            NOT NULL,
   ForumOwnerName                 NVARCHAR(30)                   NOT NULL,
   LastPostMemberName             NVARCHAR(30)                   NOT NULL,
   ForumName                      NVARCHAR(250)                  NOT NULL,
   ForumDesc                      TEXT                           NOT NULL,
   ForumCreationDate              DATETIME                       NOT NULL,
   ForumModifiedDate              DATETIME                       NOT NULL,
   ForumLastPostDate              DATETIME                       NOT NULL,
   ForumOrder                     INT                            NOT NULL,
   ForumType                      INT                            NOT NULL,
   ForumFormatOption              INT                            NOT NULL,
   ForumOption                    INT                            NOT NULL,
   ForumStatus                    INT                            NOT NULL,
   ForumModerationMode            INT                            NOT NULL,
   ForumPassword                  NVARCHAR(40)                   NOT NULL,
   ForumThreadCount               INT                            NOT NULL,
   ForumPostCount                 INT                            NOT NULL,
   PRIMARY KEY (ForumID),
   UNIQUE (ForumName, CategoryID)
);

CREATE INDEX Forum_CatID_idx on mvnforumForum
(
   CategoryID
);

CREATE TABLE mvnforumGroupForum
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

CREATE TABLE mvnforumGroupPermission
(
   GroupID                        INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (GroupID, Permission)
);

CREATE INDEX GroupPermission_1_idx on mvnforumGroupPermission
(
   GroupID
);

CREATE TABLE mvnforumGroups
(
   GroupID                        INT                            NOT NULL IDENTITY,
   GroupOwnerID                   INT                            NOT NULL,
   GroupOwnerName                 NVARCHAR(30)                   NOT NULL,
   GroupName                      NVARCHAR(250)                  NOT NULL,
   GroupDesc                      TEXT                           NOT NULL,
   GroupOption                    INT                            NOT NULL,
   GroupCreationDate              DATETIME                       NOT NULL,
   GroupModifiedDate              DATETIME                       NOT NULL,
   PRIMARY KEY (GroupID),
   UNIQUE (GroupName)
);

CREATE TABLE mvnforumMember
(
   MemberID                       INT                            NOT NULL IDENTITY,
   MemberName                     NVARCHAR(30)                   NOT NULL,
   MemberPassword                 NVARCHAR(200)                  NOT NULL,
   MemberFirstEmail               NVARCHAR(60)                   NOT NULL,
   MemberEmail                    NVARCHAR(60)                   NOT NULL,
   MemberEmailVisible             INT                            NOT NULL,
   MemberNameVisible              INT                            NOT NULL,
   MemberFirstIP                  NVARCHAR(20)                   NOT NULL,
   MemberLastIP                   NVARCHAR(20)                   NOT NULL,
   MemberViewCount                INT                            NOT NULL,
   MemberPostCount                INT                            NOT NULL,
   MemberCreationDate             DATETIME                       NOT NULL,
   MemberModifiedDate             DATETIME                       NOT NULL,
   MemberExpireDate               DATETIME                       NOT NULL,
   MemberPasswordExpireDate       DATETIME                       NOT NULL,
   MemberLastLogon                DATETIME                       NOT NULL,
   MemberOption                   INT                            NOT NULL,
   MemberStatus                   INT                            NOT NULL,
   MemberActivateCode             NVARCHAR(40)                   NOT NULL,
   MemberTempPassword             NVARCHAR(40)                   NOT NULL,
   MemberMessageCount             INT                            NOT NULL,
   MemberMessageOption            INT                            NOT NULL,
   MemberPostsPerPage             INT                            NOT NULL,
   MemberWarnCount                INT                            NOT NULL,
   MemberVoteCount                INT                            NOT NULL,
   MemberVoteTotalStars           INT                            NOT NULL,
   MemberRewardPoints             INT                            NOT NULL,
   MemberTitle                    NVARCHAR(250)                  NOT NULL,
   MemberTimeZone                 FLOAT                          NOT NULL,
   MemberSignature                NVARCHAR(250)                  NOT NULL,
   MemberAvatar                   NVARCHAR(200)                  NOT NULL,
   MemberSkin                     NVARCHAR(70)                   NOT NULL,
   MemberLanguage                 NVARCHAR(70)                   NOT NULL,
   MemberFirstname                NVARCHAR(70)                   NOT NULL,
   MemberLastname                 NVARCHAR(70)                   NOT NULL,
   MemberGender                   INT                            NOT NULL,
   MemberBirthday                 DATETIME                       NOT NULL,
   MemberAddress                  NVARCHAR(150)                  NOT NULL,
   MemberCity                     NVARCHAR(70)                   NOT NULL,
   MemberState                    NVARCHAR(70)                   NOT NULL,
   MemberCountry                  NVARCHAR(70)                   NOT NULL,
   MemberPhone                    NVARCHAR(40)                   NOT NULL,
   MemberMobile                   NVARCHAR(40)                   NOT NULL,
   MemberFax                      NVARCHAR(40)                   NOT NULL,
   MemberCareer                   NVARCHAR(50)                   NOT NULL,
   MemberHomepage                 NVARCHAR(200)                  NOT NULL,
   MemberYahoo                    NVARCHAR(70)                   NOT NULL,
   MemberAol                      NVARCHAR(70)                   NOT NULL,
   MemberIcq                      NVARCHAR(70)                   NOT NULL,
   MemberMsn                      NVARCHAR(70)                   NOT NULL,
   MemberCoolLink1                NVARCHAR(200)                  NOT NULL,
   MemberCoolLink2                NVARCHAR(200)                  NOT NULL,
   PRIMARY KEY (MemberID),
   UNIQUE (MemberEmail),
   UNIQUE (MemberName)
);

CREATE TABLE mvnforumMemberGroup
(
   GroupID                        INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     NVARCHAR(30)                   NOT NULL,
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

CREATE TABLE mvnforumMemberPermission
(
   MemberID                       INT                            NOT NULL,
   Permission                     INT                            NOT NULL,
   PRIMARY KEY (MemberID, Permission)
);

CREATE INDEX MemberPermission_1_idx on mvnforumMemberPermission
(
   MemberID
);

CREATE TABLE mvnforumMessageFolder
(
   FolderName                     NVARCHAR(30)                   NOT NULL,
   MemberID                       INT                            NOT NULL,
   FolderOrder                    INT                            NOT NULL,
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

CREATE TABLE mvnforumPost
(
   PostID                         INT                            NOT NULL IDENTITY,
   ParentPostID                   INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     NVARCHAR(30)                   NOT NULL,
   LastEditMemberName             NVARCHAR(30)                   NOT NULL,
   PostTopic                      NVARCHAR(250)                  NOT NULL,
   PostBody                       TEXT                           NOT NULL,
   PostCreationDate               DATETIME                       NOT NULL,
   PostLastEditDate               DATETIME                       NOT NULL,
   PostCreationIP                 NVARCHAR(20)                   NOT NULL,
   PostLastEditIP                 NVARCHAR(20)                   NOT NULL,
   PostEditCount                  INT                            NOT NULL,
   PostFormatOption               INT                            NOT NULL,
   PostOption                     INT                            NOT NULL,
   PostStatus                     INT                            NOT NULL,
   PostIcon                       NVARCHAR(10)                   NOT NULL,
   PostAttachCount                INT                            NOT NULL,
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

CREATE TABLE mvnforumThread
(
   ThreadID                       INT                            NOT NULL IDENTITY,
   ForumID                        INT                            NOT NULL,
   MemberName                     NVARCHAR(30)                   NOT NULL,
   LastPostMemberName             NVARCHAR(30)                   NOT NULL,
   ThreadTopic                    NVARCHAR(250)                  NOT NULL,
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
   ThreadIcon                     NVARCHAR(10)                   NOT NULL,
   ThreadDuration                 INT                            NOT NULL,
   ThreadAttachCount              INT                            NOT NULL,
   PRIMARY KEY (ThreadID)
);

CREATE INDEX Thread_1_idx on mvnforumThread
(
   ForumID
);

CREATE TABLE mvnforumWatch
(
   WatchID                        INT                            NOT NULL IDENTITY,
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

CREATE TABLE mvnforumAttachment
(
   AttachID                       INT                            NOT NULL IDENTITY,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 NVARCHAR(250)                  NOT NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 NVARCHAR(70)                   NOT NULL,
   AttachDesc                     TEXT                           NOT NULL,
   AttachCreationIP               NVARCHAR(20)                   NOT NULL,
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

CREATE TABLE mvnforumMemberForum
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

CREATE TABLE mvnforumFavoriteThread
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

CREATE TABLE mvnforumRank
(
   RankID                         INT                            NOT NULL IDENTITY,
   RankMinPosts                   INT                            NOT NULL,
   RankLevel                      INT                            NOT NULL,
   RankTitle                      NVARCHAR(250)                  NOT NULL,
   RankImage                      NVARCHAR(250)                  NOT NULL,
   RankType                       INT                            NOT NULL,
   RankOption                     INT                            NOT NULL,
   PRIMARY KEY (RankID),
   UNIQUE (RankMinPosts),
   UNIQUE (RankTitle)
);

CREATE TABLE mvnforumMessage
(
   MessageID                      INT                            NOT NULL IDENTITY,
   FolderName                     NVARCHAR(30)                   NOT NULL,
   MemberID                       INT                            NOT NULL,
   MessageSenderID                INT                            NOT NULL,
   MessageSenderName              NVARCHAR(30)                   NOT NULL,
   MessageToList                  NVARCHAR(250)                  NOT NULL,
   MessageCcList                  NVARCHAR(250),
   MessageBccList                 NVARCHAR(250),
   MessageTopic                   NVARCHAR(250)                  NOT NULL,
   MessageBody                    TEXT                           NOT NULL,
   MessageType                    INT                            NOT NULL,
   MessageOption                  INT                            NOT NULL,
   MessageStatus                  INT                            NOT NULL,
   MessageReadStatus              INT                            NOT NULL,
   MessageNotify                  INT                            NOT NULL,
   MessageIcon                    NVARCHAR(10)                   NOT NULL,
   MessageAttachCount             INT                            NOT NULL,
   MessageIP                      NVARCHAR(20)                   NOT NULL,
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

CREATE TABLE mvnforumMessageStatistics
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

CREATE TABLE mvnforumPmAttachment
(
   PmAttachID                     INT                            NOT NULL IDENTITY,
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               NVARCHAR(250)                  NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               NVARCHAR(70)                   NOT NULL,
   PmAttachDesc                   TEXT                           NOT NULL,
   PmAttachCreationIP             NVARCHAR(20)                   NOT NULL,
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

CREATE TABLE mvnforumPmAttachMessage
(
   MessageID                      INT                            NOT NULL,
   PmAttachID                     INT                            NOT NULL,
   RelationType                   INT                            NOT NULL,
   RelationOption                 INT                            NOT NULL,
   RelationStatus                 INT                            NOT NULL,
   PRIMARY KEY (MessageID, PmAttachID)
);


--
-- data for table mvnforumMember
--
SET IDENTITY_INSERT mvnforumMember ON
INSERT INTO mvnforumMember (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2)
                    VALUES ('1', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', 'admin@yourdomain.com', 'admin@yourdomain.com', '0', '1',         '127.0.0.1',   '127.0.0.1',  '0',             '0',             GETDATE(),          GETDATE(),          GETDATE(),        GETDATE(),                GETDATE(),       '0',          '0',          '',                 '',                 '0',                '0',                 '10',               '0',             '0',             '0',                  '0',                '',          '0',            '',              '',           '',         '',             '',              '',             '1',          GETDATE(),      '',            '',         '',          '',            '',          '',           '',        '',           '',             '',          '',        '',        '',        '',              '');
SET IDENTITY_INSERT mvnforumMember OFF

--
-- data for table mvnforumMessageFolder
--
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Inbox',    '1',      '0',         0,            0,            0,          GETDATE(),          GETDATE());
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Sent',     '1',      '2',         0,            0,            0,          GETDATE(),          GETDATE());


--
-- data for table mvnforumGroups
--
SET IDENTITY_INSERT mvnforumGroups ON

INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('1',     '0',          '',             'Guest',   'All anonymous users belong to this group.',              '0',         GETDATE(),             GETDATE());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('2',     '0',          '',             'Member',  'All registered users belong to this group.',             '0',         GETDATE(),             GETDATE());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('3',     '1',          'admin',        'Admin',   'This group have SystemAdmin permission by default.',     '0',         GETDATE(),             GETDATE());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('4',     '1',          'admin',        'Forum Admin', 'This group have ForumAdmin permission by default.',  '0',         GETDATE(),             GETDATE());
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('5',     '1',          'admin',        'Forum Moderator', 'This group have ForumModerator permission by default.','0',   GETDATE(),             GETDATE());
SET IDENTITY_INSERT mvnforumGroups OFF

--
-- data for table mvnforumMemberGroup
--
INSERT INTO mvnforumMemberGroup (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)
                         VALUES ('3',     '1',      'admin',    '0',       GETDATE(),    GETDATE(),    GETDATE());



--
-- data for table mvnforumMemberPermission
--
INSERT INTO mvnforumMemberPermission (MemberID, Permission)
                              VALUES ('1',      '100');


--
-- data for table mvnforumGroupPermission
--
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ('1',     '109');
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ('2',     '110');
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ('3',     '100');
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ('4',     '105');
INSERT INTO mvnforumGroupPermission (GroupID, Permission)
                             VALUES ('5',     '106');

--
-- data for table mvnforumRank
--
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (0, 0, 'Stranger',                   '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (20, 0, 'Newbie',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (50, 0, 'Member',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (100, 0, 'Advanced Member',          '',        0,        0);

