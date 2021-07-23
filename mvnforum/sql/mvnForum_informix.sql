
-- $Header: /cvsroot/mvnforum/mvnforum/sql/mvnForum_informix.sql,v 1.7 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.7 $
-- $Date: 2010/04/06 11:11:32 $
-- Database : JDBC Database
-- Driver   :
-- Url      :

-- Things should be considered when port this file to other database
-- AUTO_INCREMENT : SERIAL
-- LONGVARCHAR    : LVARCHAR
-- DATE           : DATE
-- TIMESTAMP      : DATETIME YEAR TO SECOND
-- VARCHAR        : VARCHAR
-- FLOAT          : FLOAT
-- BLOB           : 
-- now()          : CURRENT YEAR TO SECOND

-- Uncomment the following drop table command if you want to drop the tables
-- Note: drop tables will delete all the data in them.
-- Note: you should always backup your data before run the script

-- drop table mvnforumCategory;
-- drop table mvnforumForum;
-- drop table mvnforumGroupForum;
-- drop table mvnforumGroupPermission;
-- drop table mvnforumGroups;
-- drop table mvnforumMember;
-- drop table mvnforumMemberGroup;
-- drop table mvnforumMemberPermission;
-- drop table mvnforumMessageFolder;
-- drop table mvnforumPost;
-- drop table mvnforumThread;
-- drop table mvnforumWatch;
-- drop table mvnforumAttachment;
-- drop table mvnforumMemberForum;
-- drop table mvnforumFavoriteThread;
-- drop table mvnforumRank;
-- drop table mvnforumMessage;
-- drop table mvnforumMessageStatistics;
-- drop table mvnforumPmAttachment;
-- drop table mvnforumPmAttachMessage;


CREATE TABLE mvnforumCategory
(
   CategoryID                     SERIAL                         NOT NULL,
   ParentCategoryID               INT                            NOT NULL,
   CategoryName                   VARCHAR(250)                   NOT NULL,
   CategoryDesc                   LVARCHAR                       NOT NULL,
   CategoryCreationDate           DATETIME YEAR TO SECOND        NOT NULL,
   CategoryModifiedDate           DATETIME YEAR TO SECOND        NOT NULL,
   CategoryOrder                  INT                            NOT NULL,
   CategoryOption                 INT                            NOT NULL,
   CategoryStatus                 INT                            NOT NULL,
   PRIMARY KEY (CategoryID),
   UNIQUE (CategoryName)
);

CREATE TABLE mvnforumForum
(
   ForumID                        SERIAL                         NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumOwnerName                 VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ForumName                      VARCHAR(250)                   NOT NULL,
   ForumDesc                      LVARCHAR                       NOT NULL,
   ForumCreationDate              DATETIME YEAR TO SECOND        NOT NULL,
   ForumModifiedDate              DATETIME YEAR TO SECOND        NOT NULL,
   ForumLastPostDate              DATETIME YEAR TO SECOND        NOT NULL,
   ForumOrder                     INT                            NOT NULL,
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
   GroupID                        SERIAL                         NOT NULL,
   GroupOwnerID                   INT                            NOT NULL,
   GroupOwnerName                 VARCHAR(30)                    NOT NULL,
   GroupName                      VARCHAR(250)                   NOT NULL,
   GroupDesc                      LVARCHAR                       NOT NULL,
   GroupOption                    INT                            NOT NULL,
   GroupCreationDate              DATETIME YEAR TO SECOND        NOT NULL,
   GroupModifiedDate              DATETIME YEAR TO SECOND        NOT NULL,
   PRIMARY KEY (GroupID),
   UNIQUE (GroupName)
);

CREATE TABLE mvnforumMember
(
   MemberID                       SERIAL                         NOT NULL,
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
   MemberCreationDate             DATETIME YEAR TO SECOND        NOT NULL,
   MemberModifiedDate             DATETIME YEAR TO SECOND        NOT NULL,
   MemberExpireDate               DATETIME YEAR TO SECOND        NOT NULL,
   MemberPasswordExpireDate       DATETIME YEAR TO SECOND        NOT NULL,
   MemberLastLogon                DATETIME YEAR TO SECOND        NOT NULL,
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

CREATE TABLE mvnforumMemberGroup
(
   GroupID                        INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   Privilege                      INT                            NOT NULL,
   CreationDate                   DATETIME YEAR TO SECOND        NOT NULL,
   ModifiedDate                   DATETIME YEAR TO SECOND        NOT NULL,
   ExpireDate                     DATETIME YEAR TO SECOND        NOT NULL,
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
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INT                            NOT NULL,
   FolderOrder                    INT                            NOT NULL,
   FolderStatus                   INT                            NOT NULL,
   FolderOption                   INT                            NOT NULL,
   FolderType                     INT                            NOT NULL,
   FolderCreationDate             DATETIME YEAR TO SECOND        NOT NULL,
   FolderModifiedDate             DATETIME YEAR TO SECOND        NOT NULL,
   PRIMARY KEY (FolderName, MemberID)
);

CREATE INDEX MessageFolder_1_idx on mvnforumMessageFolder
(
   MemberID
);

CREATE TABLE mvnforumPost
(
   PostID                         SERIAL                         NOT NULL,
   ParentPostID                   INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastEditMemberName             VARCHAR(30)                    NOT NULL,
   PostTopic                      VARCHAR(250)                   NOT NULL,
   PostBody                       LVARCHAR                       NOT NULL,
   PostCreationDate               DATETIME YEAR TO SECOND        NOT NULL,
   PostLastEditDate               DATETIME YEAR TO SECOND        NOT NULL,
   PostCreationIP                 VARCHAR(20)                    NOT NULL,
   PostLastEditIP                 VARCHAR(20)                    NOT NULL,
   PostEditCount                  INT                            NOT NULL,
   PostFormatOption               INT                            NOT NULL,
   PostOption                     INT                            NOT NULL,
   PostStatus                     INT                            NOT NULL,
   PostIcon                       VARCHAR(10)                    NOT NULL,
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
   ThreadID                       SERIAL                         NOT NULL,
   ForumID                        INT                            NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ThreadTopic                    VARCHAR(250)                   NOT NULL,
   ThreadBody                     LVARCHAR                       NOT NULL,
   ThreadVoteCount                INT                            NOT NULL,
   ThreadVoteTotalStars           INT                            NOT NULL,
   ThreadCreationDate             DATETIME YEAR TO SECOND        NOT NULL,
   ThreadLastPostDate             DATETIME YEAR TO SECOND        NOT NULL,
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

CREATE TABLE mvnforumWatch
(
   WatchID                        SERIAL                         NOT NULL,
   MemberID                       INT                            NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   WatchType                      INT                            NOT NULL,
   WatchOption                    INT                            NOT NULL,
   WatchStatus                    INT                            NOT NULL,
   WatchCreationDate              DATETIME YEAR TO SECOND        NOT NULL,
   WatchLastSentDate              DATETIME YEAR TO SECOND        NOT NULL,
   WatchEndDate                   DATETIME YEAR TO SECOND        NOT NULL,
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
   AttachID                       SERIAL                         NOT NULL,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 VARCHAR(250)                   NOT NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 VARCHAR(70)                    NOT NULL,
   AttachDesc                     LVARCHAR                       NOT NULL,
   AttachCreationIP               VARCHAR(20)                    NOT NULL,
   AttachCreationDate             DATETIME YEAR TO SECOND        NOT NULL,
   AttachModifiedDate             DATETIME YEAR TO SECOND        NOT NULL,
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
   FavoriteCreationDate           DATETIME YEAR TO SECOND        NOT NULL,
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
   RankID                         SERIAL                         NOT NULL,
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

CREATE TABLE mvnforumMessage
(
   MessageID                      SERIAL                         NOT NULL,
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INT                            NOT NULL,
   MessageSenderID                INT                            NOT NULL,
   MessageSenderName              VARCHAR(30)                    NOT NULL,
   MessageToList                  VARCHAR(250)                   NOT NULL,
   MessageCcList                  VARCHAR(250),
   MessageBccList                 VARCHAR(250),
   MessageTopic                   VARCHAR(250)                   NOT NULL,
   MessageBody                    LVARCHAR                       NOT NULL,
   MessageType                    INT                            NOT NULL,
   MessageOption                  INT                            NOT NULL,
   MessageStatus                  INT                            NOT NULL,
   MessageReadStatus              INT                            NOT NULL,
   MessageNotify                  INT                            NOT NULL,
   MessageIcon                    VARCHAR(10)                    NOT NULL,
   MessageAttachCount             INT                            NOT NULL,
   MessageIP                      VARCHAR(20)                    NOT NULL,
   MessageCreationDate            DATETIME YEAR TO SECOND        NOT NULL,
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
   MessageCreationDate            DATETIME YEAR TO SECOND        NOT NULL,
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
   PmAttachID                     SERIAL                         NOT NULL,
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               VARCHAR(250)                   NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               VARCHAR(70)                    NOT NULL,
   PmAttachDesc                   LVARCHAR                       NOT NULL,
   PmAttachCreationIP             VARCHAR(20)                    NOT NULL,
   PmAttachCreationDate           DATETIME YEAR TO SECOND        NOT NULL,
   PmAttachModifiedDate           DATETIME YEAR TO SECOND        NOT NULL,
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
INSERT INTO mvnforumMember (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2)
                    VALUES ('1', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', 'admin@yourdomain.com', 'admin@yourdomain.com', '0', '1',         '127.0.0.1',   '127.0.0.1',  '0',             '0',             CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND, '0', '0','',                 '',                 '0',                '0',                 '10',               '0',             '0',             '0',                  '0',                '',          '0',            '',              '',           '',         '',             '',              '',             '1',          CURRENT YEAR TO SECOND, '',    '',         '',          '',            '',          '',           '',        '',           '',             '',          '',        '',        '',        '',              '');


--
-- data for table mvnforumMessageFolder
--
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Inbox',    '1',      '0',         0,            0,            0,          CURRENT YEAR TO SECOND,              CURRENT YEAR TO SECOND);
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Sent',     '1',      '2',         0,            0,            0,          CURRENT YEAR TO SECOND,              CURRENT YEAR TO SECOND);


--
-- data for table mvnforumGroups
--
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('1',     '0',          '',             'Guest',   'All anonymous users belong to this group.',              '0',         CURRENT YEAR TO SECOND,             CURRENT YEAR TO SECOND);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('2',     '0',          '',             'Member',  'All registered users belong to this group.',             '0',         CURRENT YEAR TO SECOND,             CURRENT YEAR TO SECOND);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('3',     '1',          'admin',        'Admin',   'This group have SystemAdmin permission by default.',     '0',         CURRENT YEAR TO SECOND,             CURRENT YEAR TO SECOND);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('4',     '1',          'admin',        'Forum Admin', 'This group have ForumAdmin permission by default.',  '0',         CURRENT YEAR TO SECOND,             CURRENT YEAR TO SECOND);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('5',     '1',          'admin',        'Forum Moderator', 'This group have ForumModerator permission by default.','0',   CURRENT YEAR TO SECOND,             CURRENT YEAR TO SECOND);


--
-- data for table mvnforumMemberGroup
--
INSERT INTO mvnforumMemberGroup (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)
                         VALUES ('3', '1', 'admin', '0', CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND, CURRENT YEAR TO SECOND);



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

