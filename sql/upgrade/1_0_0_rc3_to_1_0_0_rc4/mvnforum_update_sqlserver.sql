-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc3_to_1_0_0_rc4/mvnforum_update_sqlserver.sql,v 1.6 2007/07/02 13:05:31 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.6 $
-- $Date: 2007/07/02 13:05:31 $
--
-- This script is used to upgrade mvnForum from RC2/RC3 to RC4
-- This script creates 3 new table : mvnforumMessage
--                                   mvnforumMessageStatistics
--                                   mvnforumPmAttachment
--                                   mvnforumPmAttachMessage
--
-- Database: SQL Server



-- drop table mvnforumMessage;
-- drop table mvnforumMessageStatistics;
-- drop table mvnforumPmAttachment;
-- drop table mvnforumPmAttachMessage;


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
   MessageBody                    NTEXT                          NOT NULL,
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
   PmAttachDesc                   NTEXT                          NOT NULL,
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

UPDATE mvnforumMessageFolder SET FolderOrder = 2 WHERE FolderName = 'Sent';

ALTER TABLE mvnforumMessageFolder ADD FolderStatus INT NOT NULL DEFAULT 0;

ALTER TABLE mvnforumMessageFolder ADD FolderOption INT NOT NULL DEFAULT 0;

ALTER TABLE mvnforumMessageFolder ADD FolderType INT NOT NULL DEFAULT 0;

ALTER TABLE mvnforumThread ADD ThreadAttachCount INT NOT NULL DEFAULT 0;

-- Note: When the AttachDesc's date type was NTEXT, comment this query to upgrade
-- Because the altered column cannot be a column with a text, image, ntext, or timestamp data type.
ALTER TABLE mvnforumAttachment ALTER COLUMN AttachDesc NTEXT NOT NULL;

ALTER TABLE mvnforumMember ALTER COLUMN MemberPassword NVARCHAR(200) NOT NULL;

ALTER TABLE mvnforumMember ADD MemberExpireDate DATETIME;
UPDATE mvnforumMember SET MemberExpireDate = MemberCreationDate;

