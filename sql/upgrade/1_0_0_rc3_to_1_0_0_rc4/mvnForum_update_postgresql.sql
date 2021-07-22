-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc3_to_1_0_0_rc4/mvnForum_update_postgresql.sql,v 1.8 2007/07/02 13:05:31 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.8 $
-- $Date: 2007/07/02 13:05:31 $
--
-- This script is used to upgrade mvnForum from RC2/RC3 to RC4
-- This script creates 3 new table : mvnforumMessage
--                                   mvnforumMessageStatistics
--                                   mvnforumPmAttachment
--                                   mvnforumPmAttachMessage
--
-- Database: postgreSQL



-- drop table mvnforumMessage;
-- drop table mvnforumMessageStatistics;
-- drop table mvnforumPmAttachment;
-- drop table mvnforumPmAttachMessage;

--
-- drop sequences
--
-- drop sequence mvnforumMessage_seq;
-- drop sequence mvnforumPmAttachment_seq;

--
-- create sequences
--
create sequence mvnforumMessage_seq;
create sequence mvnforumPmAttachment_seq;

CREATE TABLE mvnforumMessage
(
   MessageID                      INT                            NOT NULL DEFAULT nextval('mvnforumMessage_seq'),
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
   FromID                         INT                            NOT NULL,
   ToID                           INT                            NOT NULL,
   MessageCreationDate            TIMESTAMP                      NOT NULL,
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
   PmAttachID                     INT                            NOT NULL DEFAULT nextval('mvnforumPmAttachment_seq'),
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               VARCHAR(250)                   NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               VARCHAR(70)                    NOT NULL,
   PmAttachDesc                   TEXT                           NOT NULL,
   PmAttachCreationIP             VARCHAR(20)                    NOT NULL,
   PmAttachCreationDate           TIMESTAMP                      NOT NULL,
   PmAttachModifiedDate           TIMESTAMP                      NOT NULL,
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

UPDATE mvnforumMessageFolder SET FolderOrder = 2 WHERE FolderName = 'Sent';

ALTER TABLE mvnforumMessageFolder ADD FolderStatus INT ;
ALTER TABLE mvnforumMessageFolder ALTER FolderStatus SET DEFAULT 0 ;
UPDATE mvnforumMessageFolder SET FolderStatus = 0 ;
ALTER TABLE mvnforumMessageFolder ALTER FolderStatus SET NOT NULL ;

ALTER TABLE mvnforumMessageFolder ADD FolderOption INT ;
ALTER TABLE mvnforumMessageFolder ALTER FolderOption SET DEFAULT 0 ;
UPDATE mvnforumMessageFolder SET FolderOption = 0 ;
ALTER TABLE mvnforumMessageFolder ALTER FolderOption SET NOT NULL ;

ALTER TABLE mvnforumMessageFolder ADD FolderType INT ;
ALTER TABLE mvnforumMessageFolder ALTER FolderType SET DEFAULT 0 ;
UPDATE mvnforumMessageFolder SET FolderType = 0 ;
ALTER TABLE mvnforumMessageFolder ALTER FolderType SET NOT NULL ;

ALTER TABLE mvnforumThread ADD ThreadAttachCount INT ;
ALTER TABLE mvnforumThread ALTER ThreadAttachCount SET DEFAULT 0 ;
UPDATE mvnforumThread SET ThreadAttachCount = 0 ;
ALTER TABLE mvnforumThread ALTER ThreadAttachCount SET NOT NULL ;

--ALTER TABLE mvnforumAttachment CHANGE AttachDesc AttachDesc TEXT NOT NULL ;

--ALTER TABLE mvnforumMember CHANGE MemberPassword MemberPassword VARCHAR(200) NOT NULL ;

ALTER TABLE mvnforumMember ADD MemberExpireDate TIMESTAMP ;
UPDATE mvnforumMember SET MemberExpireDate = MemberCreationDate ;
ALTER TABLE mvnforumMember ALTER MemberExpireDate SET NOT NULL ;

