
-- $Header: /cvsroot/mvnforum/mvnforum/sql/mvnForum_oracle8.sql,v 1.35 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.35 $
-- $Date: 2010/04/06 11:11:32 $
-- Database : Oracle 8i
-- Driver   : oracle.jdbc.driver.OracleDriver
-- Url      : jdbc:oracle:thin:@<host>:1521:<database>
--
-- Note: If your jdbc driver is before 8.1.7, you MUST upgrade jdbc driver
--       to 8.1.7 or later (You don't have to upgrade Oracle database, just upgrade jdbc driver)
--       mvnForum will not run on Oracle's jdbc driver before 8.1.7
-- Download url : http://otn.oracle.com/software/tech/java/sqlj_jdbc/content.html
--
-- NOTE: You should use UTF8 for Oracle database, you set
--   encoding with Database Configuration Assistant when you create database

-- NOTE: to add permission to create trigger: GRANT CREATE TRIGGER TO "USERNAME"

-- NOTE: Names for tables, clusters, views, indexes, synonyms, tablespaces, and usernames must be 30 characters or less.

-- NOTE: Inside a CREATE TABLE block, does not allow empty line

-- Things should be considered when port this file to other database
-- AUTO_INCREMENT : sequence and trigger
-- LONGVARCHAR    : LONG VARCHAR
-- DATE           : DATE
-- TIMESTAMP      : DATE
-- VARCHAR        : VARCHAR2
-- BLOB           : BLOB
-- now()          : sysdate

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

--
-- DROP SEQUENCE
--
-- DROP SEQUENCE mvnforumCategory_seq;
-- DROP SEQUENCE mvnforumForum_seq;
-- DROP SEQUENCE mvnforumGroups_seq;
-- DROP SEQUENCE mvnforumMember_seq;
-- DROP SEQUENCE mvnforumPost_seq;
-- DROP SEQUENCE mvnforumThread_seq;
-- DROP SEQUENCE mvnforumWatch_seq;
-- DROP SEQUENCE mvnforumAttachment_seq;
-- DROP SEQUENCE mvnforumRank_seq;
-- DROP SEQUENCE mvnforumMessage_seq;
-- DROP SEQUENCE mvnforumPmAttachment_seq;


--
-- CREATE SEQUENCES
--
CREATE SEQUENCE mvnforumCategory_seq;
CREATE SEQUENCE mvnforumForum_seq;
CREATE SEQUENCE mvnforumGroups_seq START WITH 6;
CREATE SEQUENCE mvnforumMember_seq START WITH 2;
CREATE SEQUENCE mvnforumPost_seq;
CREATE SEQUENCE mvnforumThread_seq;
CREATE SEQUENCE mvnforumWatch_seq;
CREATE SEQUENCE mvnforumAttachment_seq;
CREATE SEQUENCE mvnforumRank_seq;
CREATE SEQUENCE mvnforumMessage_seq;
CREATE SEQUENCE mvnforumPmAttachment_seq;


CREATE TABLE mvnforumCategory
(
   CategoryID                     INT                            NOT NULL,
   ParentCategoryID               INT                            NOT NULL,
   CategoryName                   VARCHAR2(250)                  NOT NULL,
   CategoryDesc                   LONG VARCHAR                   NULL,
   CategoryCreationDate           DATE                           NOT NULL,
   CategoryModifiedDate           DATE                           NOT NULL,
   CategoryOrder                  INT                            NOT NULL,
   CategoryOption                 INT                            NOT NULL,
   CategoryStatus                 INT                            NOT NULL,
   PRIMARY KEY (CategoryID),
   UNIQUE (CategoryName)
);

CREATE TABLE mvnforumForum
(
   ForumID                        INT                            NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumOwnerName                 VARCHAR(30)                    NULL,
   LastPostMemberName             VARCHAR2(30)                   NULL,
   ForumName                      VARCHAR2(250)                  NOT NULL,
   ForumDesc                      LONG VARCHAR                   NULL,
   ForumCreationDate              DATE                           NOT NULL,
   ForumModifiedDate              DATE                           NOT NULL,
   ForumLastPostDate              DATE                           NOT NULL,
   ForumOrder                     INT                            NOT NULL,
   ForumType                      INT                            NOT NULL,
   ForumFormatOption              INT                            NOT NULL,
   ForumOption                    INT                            NOT NULL,
   ForumStatus                    INT                            NOT NULL,
   ForumModerationMode            INT                            NOT NULL,
   ForumPassword                  VARCHAR2(40)                   NULL,
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
   GroupID                        INT                            NOT NULL,
   GroupOwnerID                   INT                            NOT NULL,
   GroupOwnerName                 VARCHAR2(30)                   NULL,
   GroupName                      VARCHAR2(250)                  NOT NULL,
   GroupDesc                      LONG VARCHAR                   NULL,
   GroupOption                    INT                            NOT NULL,
   GroupCreationDate              DATE                           NOT NULL,
   GroupModifiedDate              DATE                           NOT NULL,
   PRIMARY KEY (GroupID),
   UNIQUE (GroupName)
);

CREATE TABLE mvnforumMember
(
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR2(30)                   NOT NULL,
   MemberPassword                 VARCHAR2(200)                  NOT NULL,
   MemberFirstEmail               VARCHAR2(60)                   NOT NULL,
   MemberEmail                    VARCHAR2(60)                   NOT NULL,
   MemberEmailVisible             INT                            NOT NULL,
   MemberNameVisible              INT                            NOT NULL,
   MemberFirstIP                  VARCHAR2(20)                   NOT NULL,
   MemberLastIP                   VARCHAR2(20)                   NOT NULL,
   MemberViewCount                INT                            NOT NULL,
   MemberPostCount                INT                            NOT NULL,
   MemberCreationDate             DATE                           NOT NULL,
   MemberModifiedDate             DATE                           NOT NULL,
   MemberExpireDate               DATE                           NOT NULL,
   MemberPasswordExpireDate       DATE                           NOT NULL,
   MemberLastLogon                DATE                           NOT NULL,
   MemberOption                   INT                            NOT NULL,
   MemberStatus                   INT                            NOT NULL,
   MemberActivateCode             VARCHAR2(40)                   NULL,
   MemberTempPassword             VARCHAR2(40)                   NULL,
   MemberMessageCount             INT                            NOT NULL,
   MemberMessageOption            INT                            NOT NULL,
   MemberPostsPerPage             INT                            NOT NULL,
   MemberWarnCount                INT                            NOT NULL,
   MemberVoteCount                INT                            NOT NULL,
   MemberVoteTotalStars           INT                            NOT NULL,
   MemberRewardPoints             INT                            NOT NULL,
   MemberTitle                    VARCHAR2(250)                  NULL,
   MemberTimeZone                 FLOAT                          NOT NULL,
   MemberSignature                VARCHAR2(250)                  NULL,
   MemberAvatar                   VARCHAR2(200)                  NULL,
   MemberSkin                     VARCHAR2(70)                   NULL,
   MemberLanguage                 VARCHAR2(70)                   NULL,
   MemberFirstname                VARCHAR2(70)                   NULL,
   MemberLastname                 VARCHAR2(70)                   NULL,
   MemberGender                   INT                            NOT NULL,
   MemberBirthday                 DATE                           NOT NULL,
   MemberAddress                  VARCHAR2(150)                  NULL,
   MemberCity                     VARCHAR2(70)                   NULL,
   MemberState                    VARCHAR2(70)                   NULL,
   MemberCountry                  VARCHAR2(70)                   NULL,
   MemberPhone                    VARCHAR2(40)                   NULL,
   MemberMobile                   VARCHAR2(40)                   NULL,
   MemberFax                      VARCHAR2(40)                   NULL,
   MemberCareer                   VARCHAR2(50)                   NULL,
   MemberHomepage                 VARCHAR2(200)                  NULL,
   MemberYahoo                    VARCHAR2(70)                   NULL,
   MemberAol                      VARCHAR2(70)                   NULL,
   MemberIcq                      VARCHAR2(70)                   NULL,
   MemberMsn                      VARCHAR2(70)                   NULL,
   MemberCoolLink1                VARCHAR2(200)                  NULL,
   MemberCoolLink2                VARCHAR2(200)                  NULL,
   PRIMARY KEY (MemberID),
   UNIQUE (MemberEmail),
   UNIQUE (MemberName)
);

CREATE TABLE mvnforumMemberGroup
(
   GroupID                        INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR2(30)                   NOT NULL,
   Privilege                      INT                            NOT NULL,
   CreationDate                   DATE                           NOT NULL,
   ModifiedDate                   DATE                           NOT NULL,
   ExpireDate                     DATE                           NOT NULL,
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
   FolderName                     VARCHAR2(30)                   NOT NULL,
   MemberID                       INT                            NOT NULL,
   FolderOrder                    INT                            NOT NULL,
   FolderStatus                   INT                            NOT NULL,
   FolderOption                   INT                            NOT NULL,
   FolderType                     INT                            NOT NULL,
   FolderCreationDate             DATE                           NOT NULL,
   FolderModifiedDate             DATE                           NOT NULL,
   PRIMARY KEY (FolderName, MemberID)
);

CREATE INDEX MessageFolder_1_idx on mvnforumMessageFolder
(
   MemberID
);

CREATE TABLE mvnforumPost
(
   PostID                         INT                            NOT NULL,
   ParentPostID                   INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   MemberName                     VARCHAR2(30)                   NOT NULL,
   LastEditMemberName             VARCHAR2(30)                   NULL,
   PostTopic                      VARCHAR2(250)                  NOT NULL,
   PostBody                       LONG VARCHAR                   NOT NULL,
   PostCreationDate               DATE                           NOT NULL,
   PostLastEditDate               DATE                           NOT NULL,
   PostCreationIP                 VARCHAR2(20)                   NOT NULL,
   PostLastEditIP                 VARCHAR2(20)                   NULL,
   PostEditCount                  INT                            NOT NULL,
   PostFormatOption               INT                            NOT NULL,
   PostOption                     INT                            NOT NULL,
   PostStatus                     INT                            NOT NULL,
   PostIcon                       VARCHAR2(10)                   NULL,
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
   ThreadID                       INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   MemberName                     VARCHAR2(30)                   NOT NULL,
   LastPostMemberName             VARCHAR2(30)                   NOT NULL,
   ThreadTopic                    VARCHAR2(250)                  NOT NULL,
   ThreadBody                     LONG VARCHAR                   NOT NULL,
   ThreadVoteCount                INT                            NOT NULL,
   ThreadVoteTotalStars           INT                            NOT NULL,
   ThreadCreationDate             DATE                           NOT NULL,
   ThreadLastPostDate             DATE                           NOT NULL,
   ThreadType                     INT                            NOT NULL,
   ThreadPriority                 INT                            NOT NULL,
   ThreadOption                   INT                            NOT NULL,
   ThreadStatus                   INT                            NOT NULL,
   ThreadHasPoll                  INT                            NOT NULL,
   ThreadViewCount                INT                            NOT NULL,
   ThreadReplyCount               INT                            NOT NULL,
   ThreadIcon                     VARCHAR2(10)                   NULL,
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
   WatchID                        INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   WatchType                      INT                            NOT NULL,
   WatchOption                    INT                            NOT NULL,
   WatchStatus                    INT                            NOT NULL,
   WatchCreationDate              DATE                           NOT NULL,
   WatchLastSentDate              DATE                           NOT NULL,
   WatchEndDate                   DATE                           NOT NULL,
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
   AttachID                       INT                            NOT NULL,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 VARCHAR2(250)                  NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 VARCHAR2(70)                   NULL,
   AttachDesc                     LONG VARCHAR                   NULL,
   AttachCreationIP               VARCHAR2(20)                   NOT NULL,
   AttachCreationDate             DATE                           NOT NULL,
   AttachModifiedDate             DATE                           NOT NULL,
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
   FavoriteCreationDate           DATE                           NOT NULL,
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
   RankID                         INT                            NOT NULL,
   RankMinPosts                   INT                            NOT NULL,
   RankLevel                      INT                            NOT NULL,
   RankTitle                      VARCHAR2(250)                  NOT NULL,
   RankImage                      VARCHAR2(250)                  NULL,
   RankType                       INT                            NOT NULL,
   RankOption                     INT                            NOT NULL,
   PRIMARY KEY (RankID),
   UNIQUE (RankMinPosts),
   UNIQUE (RankTitle)
);

CREATE TABLE mvnforumMessage
(
   MessageID                      INT                            NOT NULL,
   FolderName                     VARCHAR2(30)                   NOT NULL,
   MemberID                       INT                            NOT NULL,
   MessageSenderID                INT                            NOT NULL,
   MessageSenderName              VARCHAR2(30)                   NOT NULL,
   MessageToList                  VARCHAR2(250)                  NOT NULL,
   MessageCcList                  VARCHAR2(250)                  NULL,
   MessageBccList                 VARCHAR2(250)                  NULL,
   MessageTopic                   VARCHAR2(250)                  NOT NULL,
   MessageBody                    LONG VARCHAR                   NOT NULL,
   MessageType                    INT                            NOT NULL,
   MessageOption                  INT                            NOT NULL,
   MessageStatus                  INT                            NOT NULL,
   MessageReadStatus              INT                            NOT NULL,
   MessageNotify                  INT                            NOT NULL,
   MessageIcon                    VARCHAR2(10)                   NULL,
   MessageAttachCount             INT                            NOT NULL,
   MessageIP                      VARCHAR2(20)                   NOT NULL,
   MessageCreationDate            DATE                           NOT NULL,
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
   MessageCreationDate            DATE                           NOT NULL,
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
   PmAttachID                     INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               VARCHAR2(250)                  NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               VARCHAR2(70)                   NOT NULL,
   PmAttachDesc                   LONG VARCHAR                   NULL,
   PmAttachCreationIP             VARCHAR2(20)                   NOT NULL,
   PmAttachCreationDate           DATE                           NOT NULL,
   PmAttachModifiedDate           DATE                           NOT NULL,
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
                    VALUES ('1', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', 'admin@yourdomain.com', 'admin@yourdomain.com', '0', '1',         '127.0.0.1',   '127.0.0.1',  '0',             '0',             sysdate,            sysdate,            sysdate,          sysdate,                  sysdate,         '0',          '0',          '',                 '',                 '0',                '0',                 '10',               '0',             '0',             '0',                  '0',                '',          '0',            '',              '',           '',         '',             'admin',         'admin',        '1',          sysdate,        '',            '',         '',          '',            '',          '',           '',        '',           '',             '',          '',        '',        '',        '',              '');


--
-- data for table mvnforumMessageFolder
--
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Inbox',    '1',      '0',         0,            0,            0,          sysdate,            sysdate);
INSERT INTO mvnforumMessageFolder (FolderName, MemberID, FolderOrder, FolderStatus, FolderOption, FolderType, FolderCreationDate, FolderModifiedDate)
                           VALUES ('Sent',     '1',      '2',         0,            0,            0,          sysdate,            sysdate);


--
-- data for table mvnforumGroups
--
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('1',     '0',          '',             'Guest',   'All anonymous users belong to this group.',              '0',         sysdate,             sysdate);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('2',     '0',          '',             'Member',  'All registered users belong to this group.',             '0',         sysdate,             sysdate);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('3',     '1',          'admin',        'Admin',   'This group have SystemAdmin permission by default.',     '0',         sysdate,             sysdate);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('4',     '1',          'admin',        'Forum Admin', 'This group have ForumAdmin permission by default.',  '0',         sysdate,             sysdate);
INSERT INTO mvnforumGroups (GroupID, GroupOwnerID, GroupOwnerName, GroupName, GroupDesc,                                                GroupOption, GroupCreationDate, GroupModifiedDate)
                    VALUES ('5',     '1',          'admin',        'Forum Moderator', 'This group have ForumModerator permission by default.','0',   sysdate,             sysdate);


--
-- data for table mvnforumMemberGroup
--
INSERT INTO mvnforumMemberGroup (GroupID, MemberID, MemberName, Privilege, CreationDate, ModifiedDate, ExpireDate)
                         VALUES ('3',     '1',      'admin',    '0',       sysdate,      sysdate,      sysdate);



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

CREATE OR REPLACE TRIGGER mvnforumCategory_trig_autoinc
BEFORE INSERT ON mvnforumCategory
FOR EACH ROW
BEGIN
  IF (:new.CategoryID IS NULL) THEN
    SELECT mvnforumCategory_seq.nextval INTO :new.CategoryID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumForum_trig_autoinc
BEFORE INSERT ON mvnforumForum
FOR EACH ROW
BEGIN
  IF (:new.ForumID IS NULL) THEN
    SELECT mvnforumForum_seq.nextval INTO :new.ForumID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumGroups_trig_autoinc
BEFORE INSERT ON mvnforumGroups
FOR EACH ROW
BEGIN
  IF (:new.GroupID IS NULL) THEN
    SELECT mvnforumGroups_seq.nextval INTO :new.GroupID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumMember_trig_autoinc
BEFORE INSERT ON mvnforumMember
FOR EACH ROW
BEGIN
  IF (:new.MemberID IS NULL) THEN
    SELECT mvnforumMember_seq.nextval INTO :new.MemberID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumPost_trig_autoinc
BEFORE INSERT ON mvnforumPost
FOR EACH ROW
BEGIN
  IF (:new.PostID IS NULL) THEN
    SELECT mvnforumPost_seq.nextval INTO :new.PostID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumThread_trig_autoinc
BEFORE INSERT ON mvnforumThread
FOR EACH ROW
BEGIN
  IF (:new.ThreadID IS NULL) THEN
    SELECT mvnforumThread_seq.nextval INTO :new.ThreadID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumWatch_trig_autoinc
BEFORE INSERT ON mvnforumWatch
FOR EACH ROW
BEGIN
  IF (:new.WatchID IS NULL) THEN
    SELECT mvnforumWatch_seq.nextval INTO :new.WatchID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumAttach_trig_autoinc
BEFORE INSERT ON mvnforumAttachment
FOR EACH ROW
BEGIN
  IF (:new.AttachID IS NULL) THEN
    SELECT mvnforumAttachment_seq.nextval INTO :new.AttachID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumRank_trig_autoinc
BEFORE INSERT ON mvnforumRank
FOR EACH ROW
BEGIN
  IF (:new.RankID IS NULL) THEN
    SELECT mvnforumRank_seq.nextval INTO :new.RankID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumMessage_trig_autoinc
BEFORE INSERT ON mvnforumMessage
FOR EACH ROW
BEGIN
  IF (:new.MessageID IS NULL) THEN
    SELECT mvnforumMessage_seq.nextval INTO :new.MessageID FROM DUAL;
  END IF;
END;
/

CREATE OR REPLACE TRIGGER mvnforumPmAttach_trig_autoinc
BEFORE INSERT ON mvnforumPmAttachment
FOR EACH ROW
BEGIN
  IF (:new.PmAttachID IS NULL) THEN
    SELECT mvnforumPmAttachment_seq.nextval INTO :new.PmAttachID FROM DUAL;
  END IF;
END;
/

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


commit;
