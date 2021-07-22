/*
-- $Header: /cvsroot/mvnforum/mvnforum/sql/mvnForum_interbase.sql,v 1.31 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.31 $
-- $Date: 2010/04/06 11:11:32 $
-- Database : Interbase/Firebird
-- Driver   : org.firebirdsql.jdbc.FBDriver
-- Url      : jdbc:firebirdsql:<host>/3050:<database>


-- Things should be considered when port this file to other database
-- AUTO_INCREMENT : generator and trigger
-- LONGVARCHAR    : domain, see notes at the end of the file.
-- DATE           : DATE
-- TIMESTAMP      : TIMESTAMP
-- VARCHAR        : VARCHAR
-- BLOB           : 
-- now()          : now
-- INT            : INTEGER

-- Run isql:(on Windows)
-- Create Data:
--   CREATE DATABASE [fileName] user [username:default='sysdba'] password [password:default='masterkey']
--   Ex : CREATE DATABASE 'D:\data\mvnforum.fdb' user 'sysdba' password 'masterkey'
-- Connect to database:
--   CONNECT         [fileName] user [username:default='sysdba'] password [password:default='masterkey']
--   Ex : CONNECT 'D:\data\mvnforum.fdb' user 'sysdba' password 'masterkey'
-- Now, We are logged in mvnforum database and can run SQL Script for the database:
     IN  [sql script file]
--   Ex : IN 'D:\mvnforum\sql\mvnforum_interbase.sql'

-- Finish
*/


/* Uncomment the following drop table command if you want to drop the tables */
/* Note: drop tables will delete all the data in them.                       */
/* Note: you should always backup your data before run the script            */
/*
DROP TABLE mvnforumCategory;
DROP TABLE mvnforumForum;
DROP TABLE mvnforumGroupForum;
DROP TABLE mvnforumGroupPermission;
DROP TABLE mvnforumGroups;
DROP TABLE mvnforumMember;
DROP TABLE mvnforumMemberGroup;
DROP TABLE mvnforumMemberPermission;
DROP TABLE mvnforumMessageFolder;
DROP TABLE mvnforumPost;
DROP TABLE mvnforumThread;
DROP TABLE mvnforumWatch;
DROP TABLE mvnforumAttachment;
DROP TABLE mvnforumMemberForum;
DROP TABLE mvnforumFavoriteThread;
DROP TABLE mvnforumRank;
DROP TABLE mvnforumMessage;
DROP TABLE mvnforumMessageStatistics;
DROP TABLE mvnforumPmAttachment;
DROP TABLE mvnforumPmAttachMessage;
*/

/* DROP DOMAIN                                                               */
/*
DROP DOMAIN text;
*/

/* CREATE GENERATOR                                                          */
CREATE GENERATOR mvnforumCategory_seq;
CREATE GENERATOR mvnforumForum_seq;
CREATE GENERATOR mvnforumGroups_seq;
CREATE GENERATOR mvnforumMember_seq;
CREATE GENERATOR mvnforumPost_seq;
CREATE GENERATOR mvnforumThread_seq;
CREATE GENERATOR mvnforumWatch_seq;
CREATE GENERATOR mvnforumAttachment_seq;
CREATE GENERATOR mvnforumRank_seq;
CREATE GENERATOR mvnforumMessage_seq;
CREATE GENERATOR mvnforumPmAttachment_seq;

SET GENERATOR mvnforumGroups_seq TO 6;
SET GENERATOR mvnforumMember_seq TO 2;


/* It should be modeled after a CLOB, but then, JDBC code that expect  */
/* to use getString() could be broken.                                 */
CREATE DOMAIN text AS VARCHAR(4096);

COMMIT;

CREATE TABLE mvnforumCategory
(
   CategoryID                     INTEGER                        NOT NULL,
   ParentCategoryID               INTEGER                        NOT NULL,
   CategoryName                   VARCHAR(250)                   NOT NULL,
   CategoryDesc                   TEXT                           NOT NULL,
   CategoryCreationDate           TIMESTAMP                      NOT NULL,
   CategoryModifiedDate           TIMESTAMP                      NOT NULL,
   CategoryOrder                  INTEGER                        NOT NULL,
   CategoryOption                 INTEGER                        NOT NULL,
   CategoryStatus                 INTEGER                        NOT NULL,
   PRIMARY KEY (CategoryID),
   UNIQUE (CategoryName)
);

CREATE TABLE mvnforumForum
(
   ForumID                        INTEGER                        NOT NULL,
   CategoryID                     INTEGER                        NOT NULL,
   ForumOwnerName                 VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ForumName                      VARCHAR(250)                   NOT NULL,
   ForumDesc                      TEXT                           NOT NULL,
   ForumCreationDate              TIMESTAMP                      NOT NULL,
   ForumModifiedDate              TIMESTAMP                      NOT NULL,
   ForumLastPostDate              TIMESTAMP                      NOT NULL,
   ForumOrder                     INTEGER                        NOT NULL,
   ForumType                      INTEGER                        NOT NULL,
   ForumFormatOption              INTEGER                        NOT NULL,
   ForumOption                    INTEGER                        NOT NULL,
   ForumStatus                    INTEGER                        NOT NULL,
   ForumModerationMode            INTEGER                        NOT NULL,
   ForumPassword                  VARCHAR(40)                    NOT NULL,
   ForumThreadCount               INTEGER                        NOT NULL,
   ForumPostCount                 INTEGER                        NOT NULL,
   PRIMARY KEY (ForumID),
   UNIQUE (ForumName, CategoryID)
);

CREATE INDEX Forum_CatID_idx on mvnforumForum
(
   CategoryID
);

CREATE TABLE mvnforumGroupForum
(
   GroupID                        INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   Permission                     INTEGER                        NOT NULL,
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
   GroupID                        INTEGER                        NOT NULL,
   Permission                     INTEGER                        NOT NULL,
   PRIMARY KEY (GroupID, Permission)
);

CREATE INDEX GroupPermission_1_idx on mvnforumGroupPermission
(
   GroupID
);

CREATE TABLE mvnforumGroups
(
   GroupID                        INTEGER                        NOT NULL,
   GroupOwnerID                   INTEGER                        NOT NULL,
   GroupOwnerName                 VARCHAR(30)                    NOT NULL,
   GroupName                      VARCHAR(250)                   NOT NULL,
   GroupDesc                      TEXT                           NOT NULL,
   GroupOption                    INTEGER                        NOT NULL,
   GroupCreationDate              TIMESTAMP                      NOT NULL,
   GroupModifiedDate              TIMESTAMP                      NOT NULL,
   PRIMARY KEY (GroupID),
   UNIQUE (GroupName)
);

CREATE TABLE mvnforumMember
(
   MemberID                       INTEGER                        NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   MemberPassword                 VARCHAR(200)                   NOT NULL,
   MemberFirstEmail               VARCHAR(60)                    NOT NULL,
   MemberEmail                    VARCHAR(60)                    NOT NULL,
   MemberEmailVisible             INTEGER                        NOT NULL,
   MemberNameVisible              INTEGER                        NOT NULL,
   MemberFirstIP                  VARCHAR(20)                    NOT NULL,
   MemberLastIP                   VARCHAR(20)                    NOT NULL,
   MemberViewCount                INTEGER                        NOT NULL,
   MemberPostCount                INTEGER                        NOT NULL,
   MemberCreationDate             TIMESTAMP                      NOT NULL,
   MemberModifiedDate             TIMESTAMP                      NOT NULL,
   MemberExpireDate               TIMESTAMP                      NOT NULL,
   MemberPasswordExpireDate       TIMESTAMP                      NOT NULL,
   MemberLastLogon                TIMESTAMP                      NOT NULL,
   MemberOption                   INTEGER                        NOT NULL,
   MemberStatus                   INTEGER                        NOT NULL,
   MemberActivateCode             VARCHAR(40)                    NOT NULL,
   MemberTempPassword             VARCHAR(40)                    NOT NULL,
   MemberMessageCount             INTEGER                        NOT NULL,
   MemberMessageOption            INTEGER                        NOT NULL,
   MemberPostsPerPage             INTEGER                        NOT NULL,
   MemberWarnCount                INTEGER                        NOT NULL,
   MemberVoteCount                INTEGER                        NOT NULL,
   MemberVoteTotalStars           INTEGER                        NOT NULL,
   MemberRewardPoints             INTEGER                        NOT NULL,
   MemberTitle                    VARCHAR(250)                   NOT NULL,
   MemberTimeZone                 FLOAT                          NOT NULL,
   MemberSignature                VARCHAR(250)                   NOT NULL,
   MemberAvatar                   VARCHAR(200)                   NOT NULL,
   MemberSkin                     VARCHAR(70)                    NOT NULL,
   MemberLanguage                 VARCHAR(70)                    NOT NULL,
   MemberFirstname                VARCHAR(70)                    NOT NULL,
   MemberLastname                 VARCHAR(70)                    NOT NULL,
   MemberGender                   INTEGER                        NOT NULL,
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
   GroupID                        INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   Privilege                      INTEGER                        NOT NULL,
   CreationDate                   TIMESTAMP                      NOT NULL,
   ModifiedDate                   TIMESTAMP                      NOT NULL,
   ExpireDate                     TIMESTAMP                      NOT NULL,
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
   MemberID                       INTEGER                        NOT NULL,
   Permission                     INTEGER                        NOT NULL,
   PRIMARY KEY (MemberID, Permission)
);

CREATE INDEX MemberPermission_1_idx on mvnforumMemberPermission
(
   MemberID
);

CREATE TABLE mvnforumMessageFolder
(
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   FolderOrder                    INTEGER                        NOT NULL,
   FolderStatus                   INT                            NOT NULL,
   FolderOption                   INT                            NOT NULL,
   FolderType                     INT                            NOT NULL,
   FolderCreationDate             TIMESTAMP                      NOT NULL,
   FolderModifiedDate             TIMESTAMP                      NOT NULL,
   PRIMARY KEY (FolderName, MemberID)
);

CREATE INDEX MessageFolder_1_idx on mvnforumMessageFolder
(
   MemberID
);

CREATE TABLE mvnforumPost
(
   PostID                         INTEGER                        NOT NULL,
   ParentPostID                   INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   ThreadID                       INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastEditMemberName             VARCHAR(30)                    NOT NULL,
   PostTopic                      VARCHAR(250)                   NOT NULL,
   PostBody                       TEXT                           NOT NULL,
   PostCreationDate               TIMESTAMP                      NOT NULL,
   PostLastEditDate               TIMESTAMP                      NOT NULL,
   PostCreationIP                 VARCHAR(20)                    NOT NULL,
   PostLastEditIP                 VARCHAR(20)                    NOT NULL,
   PostEditCount                  INTEGER                        NOT NULL,
   PostFormatOption               INTEGER                        NOT NULL,
   PostOption                     INTEGER                        NOT NULL,
   PostStatus                     INTEGER                        NOT NULL,
   PostIcon                       VARCHAR(10)                    NOT NULL,
   PostAttachCount                INTEGER                        NOT NULL,
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
   ThreadID                       INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   MemberName                     VARCHAR(30)                    NOT NULL,
   LastPostMemberName             VARCHAR(30)                    NOT NULL,
   ThreadTopic                    VARCHAR(250)                   NOT NULL,
   ThreadBody                     TEXT                           NOT NULL,
   ThreadVoteCount                INTEGER                        NOT NULL,
   ThreadVoteTotalStars           INTEGER                        NOT NULL,
   ThreadCreationDate             TIMESTAMP                      NOT NULL,
   ThreadLastPostDate             TIMESTAMP                      NOT NULL,
   ThreadType                     INTEGER                        NOT NULL,
   ThreadPriority                 INTEGER                        NOT NULL,
   ThreadOption                   INTEGER                        NOT NULL,
   ThreadStatus                   INTEGER                        NOT NULL,
   ThreadHasPoll                  INTEGER                        NOT NULL,
   ThreadViewCount                INTEGER                        NOT NULL,
   ThreadReplyCount               INTEGER                        NOT NULL,
   ThreadIcon                     VARCHAR(10)                    ,
   ThreadDuration                 INTEGER                        NOT NULL,
   ThreadAttachCount              INT                            NOT NULL,
   PRIMARY KEY (ThreadID)
);

CREATE INDEX Thread_1_idx on mvnforumThread
(
   ForumID
);

CREATE TABLE mvnforumWatch
(
   WatchID                        INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   CategoryID                     INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   ThreadID                       INTEGER                        NOT NULL,
   WatchType                      INTEGER                        NOT NULL,
   WatchOption                    INTEGER                        NOT NULL,
   WatchStatus                    INTEGER                        NOT NULL,
   WatchCreationDate              TIMESTAMP                      NOT NULL,
   WatchLastSentDate              TIMESTAMP                      NOT NULL,
   WatchEndDate                   TIMESTAMP                      NOT NULL,
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
   AttachID                       INTEGER                        NOT NULL,
   PostID                         INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   AttachFilename                 VARCHAR(250)                   NOT NULL,
   AttachFileSize                 INTEGER                        NOT NULL,
   AttachMimeType                 VARCHAR(70)                    NOT NULL,
   AttachDesc                     TEXT                           NOT NULL,
   AttachCreationIP               VARCHAR(20)                    NOT NULL,
   AttachCreationDate             TIMESTAMP                      NOT NULL,
   AttachModifiedDate             TIMESTAMP                      NOT NULL,
   AttachDownloadCount            INTEGER                        NOT NULL,
   AttachOption                   INTEGER                        NOT NULL,
   AttachStatus                   INTEGER                        NOT NULL,
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
   MemberID                       INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   Permission                     INTEGER                        NOT NULL,
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
   MemberID                       INTEGER                        NOT NULL,
   ThreadID                       INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   FavoriteCreationDate           TIMESTAMP                      NOT NULL,
   FavoriteType                   INTEGER                        NOT NULL,
   FavoriteOption                 INTEGER                        NOT NULL,
   FavoriteStatus                 INTEGER                        NOT NULL,
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
   RankID                         INTEGER                        NOT NULL,
   RankMinPosts                   INTEGER                        NOT NULL,
   RankLevel                      INTEGER                        NOT NULL,
   RankTitle                      VARCHAR(250)                   NOT NULL,
   RankImage                      VARCHAR(250)                   NOT NULL,
   RankType                       INTEGER                        NOT NULL,
   RankOption                     INTEGER                        NOT NULL,
   PRIMARY KEY (RankID),
   UNIQUE (RankMinPosts),
   UNIQUE (RankTitle)
);

CREATE TABLE mvnforumMessage
(
   MessageID                      INTEGER                        NOT NULL,
   FolderName                     VARCHAR(30)                    NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   MessageSenderID                INTEGER                        NOT NULL,
   MessageSenderName              VARCHAR(30)                    NOT NULL,
   MessageToList                  VARCHAR(250)                   NOT NULL,
   MessageCcList                  VARCHAR(250),
   MessageBccList                 VARCHAR(250),
   MessageTopic                   VARCHAR(250)                   NOT NULL,
   MessageBody                    TEXT                           NOT NULL,
   MessageType                    INTEGER                        NOT NULL,
   MessageOption                  INTEGER                        NOT NULL,
   MessageStatus                  INTEGER                        NOT NULL,
   MessageReadStatus              INTEGER                        NOT NULL,
   MessageNotify                  INTEGER                        NOT NULL,
   MessageIcon                    VARCHAR(10)                    NOT NULL,
   MessageAttachCount             INTEGER                        NOT NULL,
   MessageIP                      VARCHAR(20)                    NOT NULL,
   MessageCreationDate            TIMESTAMP                      NOT NULL,
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
   FromID                         INTEGER                            NOT NULL,
   ToID                           INTEGER                            NOT NULL,
   MessageCreationDate            TIMESTAMP                          NOT NULL,
   MessageAttachCount             INTEGER                            NOT NULL,
   MessageType                    INTEGER                            NOT NULL,
   MessageOption                  INTEGER                            NOT NULL,
   MessageStatus                  INTEGER                            NOT NULL
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
   PmAttachID                     INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   PmAttachFilename               VARCHAR(250)                   NOT NULL,
   PmAttachFileSize               INTEGER                        NOT NULL,
   PmAttachMimeType               VARCHAR(70)                    NOT NULL,
   PmAttachDesc                   TEXT                           NOT NULL,
   PmAttachCreationIP             VARCHAR(20)                    NOT NULL,
   PmAttachCreationDate           TIMESTAMP                      NOT NULL,
   PmAttachModifiedDate           TIMESTAMP                      NOT NULL,
   PmAttachDownloadCount          INTEGER                        NOT NULL,
   PmAttachOption                 INTEGER                        NOT NULL,
   PmAttachStatus                 INTEGER                        NOT NULL,
   PRIMARY KEY (PmAttachID)
);

CREATE INDEX PmAttachment_1_idx on mvnforumPmAttachment
(
   MemberID
);

CREATE TABLE mvnforumPmAttachMessage
(
   MessageID                      INTEGER                            NOT NULL,
   PmAttachID                     INTEGER                            NOT NULL,
   RelationType                   INTEGER                            NOT NULL,
   RelationOption                 INTEGER                            NOT NULL,
   RelationStatus                 INTEGER                            NOT NULL,
   PRIMARY KEY (MessageID, PmAttachID)
);

COMMIT;

/* data for table mvnforumMember                                             */
INSERT INTO mvnforumMember (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail, MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate, MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode, MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq, MemberMsn, MemberCoolLink1, MemberCoolLink2)
                    VALUES ('1', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', 'admin@yourdomain.com', 'admin@yourdomain.com', '0', '1',         '127.0.0.1',   '127.0.0.1',  '0',             '0',             'now',              'now',              'now',            'now',                    'now',           '0',          '0',          '',                 '',                 '0',                '0',                 '10',               '0',             '0',             '0',                  '0',                '',          '0',            '',              '',           '',         '',             'admin',         'admin',        '1',          'now',          '',            '',         '',          '',            '',          '',           '',        '',           '',             '',          '',        '',        '',        '',              '');


/* data for table mvnforumMessageFolder                                      */
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Inbox',    '1',      '0',         0,            0,            0,          'now',              'now');
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Sent',     '1',      '2',         0,            0,            0,          'now',              'now');


/* data for table mvnforumGroups                                             */
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('1',     '0',          '',             'Guest',   'All anonymous users belong to this group.',              '0',         'now',             'now');
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('2',     '0',          '',             'Member',  'All registered users belong to this group.',             '0',         'now',             'now');
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('3',     '1',          'admin',        'Admin',   'This group have SystemAdmin permission by default.',     '0',         'now',             'now');
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('4',     '1',          'admin',        'Forum Admin', 'This group have ForumAdmin permission by default.',  '0',         'now',             'now');
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('5',     '1',          'admin',        'Forum Moderator', 'This group have ForumModerator permission by default.','0',   'now',             'now');


/* data for table mvnforumMemberGroup                                        */
INSERT INTO mvnforumMemberGroup (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)
                         VALUES ('3',     '1',      'admin',    '0',       'now',        'now',        'now');



/* data for table mvnforumMemberPermission                                   */
INSERT INTO mvnforumMemberPermission (MemberID, Permission)
                              VALUES ('1',      '100');


/* data for table mvnforumGroupPermission                                    */
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


/* Change sentence finalizer to '!!'                                         */
SET term ^;

CREATE TRIGGER mvnforumCategory_trig_autoinc for mvnforumCategory
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.CategoryID IS NULL) THEN
     new.CategoryID = gen_id(mvnforumCategory_seq, 1);
END
^

CREATE TRIGGER mvnforumForum_trig_autoinc for mvnforumForum
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.ForumID IS NULL) THEN
     new.ForumID = gen_id(mvnforumForum_seq, 1);
END
^

CREATE TRIGGER mvnforumGroups_trig_autoinc for mvnforumGroups
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.GroupID IS NULL) THEN
     new.GroupID = gen_id(mvnforumGroups_seq, 1);
END
^

CREATE TRIGGER mvnforumMember_trig_autoinc for mvnforumMember
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.MemberID IS NULL) THEN
     new.MemberID = gen_id(mvnforumMember_seq, 1);
END
^

CREATE TRIGGER mvnforumPost_trig_autoinc for mvnforumPost
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.PostID IS NULL) THEN
     new.PostID = gen_id(mvnforumPost_seq, 1);
END
^

CREATE TRIGGER mvnforumThread_trig_autoinc for mvnforumThread
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.ThreadID IS NULL) THEN
     new.ThreadID = gen_id(mvnforumThread_seq, 1);
END
^

CREATE TRIGGER mvnforumWatch_trig_autoinc for mvnforumWatch
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.WatchID IS NULL) THEN
     new.WatchID = gen_id(mvnforumWatch_seq, 1);
END
^

CREATE TRIGGER mvnforumAttachment_trig_autoinc for mvnforumAttachment
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.AttachID IS NULL) THEN
     new.AttachID = gen_id(mvnforumAttachment_seq, 1);
END
^

CREATE TRIGGER mvnforumRank_trig_autoinc for mvnforumRank
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.RankID IS NULL) THEN
     new.RankID = gen_id(mvnforumRank_seq, 1);
END
^

CREATE TRIGGER mvnforumMessage_trig_autoinc for mvnforumMessage
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.MessageID IS NULL) THEN
     new.MessageID = gen_id(mvnforumMessage_seq, 1);
END
^

CREATE TRIGGER mvnforumPmAttach_trig_autoinc for mvnforumPmAttachment
ACTIVE BEFORE INSERT POSITION 1
AS
BEGIN
  IF (new.PmAttachID IS NULL) THEN
     new.PmAttachID = gen_id(mvnforumPmAttachment_seq, 1);
END
^

/* Return sentence finalizer to ';'                                          */
SET term ;^

COMMIT;

/* data for table mvnforumRank                                               */
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (0, 0, 'Stranger',                   '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (20, 0, 'Newbie',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (50, 0, 'Member',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (100, 0, 'Advanced Member',          '',        0,        0);

COMMIT;
