# $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc3_to_1_0_0_rc4/mvnForum_update_mysql.sql,v 1.21 2007/07/02 13:05:31 minhnn Exp $
# $Author: minhnn $
# $Revision: 1.21 $
# $Date: 2007/07/02 13:05:31 $
#
# This script is used to upgrade mvnForum from RC2/RC3 to RC4
# This script creates 3 new table : mvnforumMessage
#                                   mvnforumMessageStatistics
#                                   mvnforumPmAttachment
#                                   mvnforumPmAttachMessage
#
# Database: MySql



# drop table if exists mvnforumMessage;
# drop table if exists mvnforumMessageStatistics;
# drop table if exists mvnforumPmAttachment;
# drop table if exists mvnforumPmAttachMessage;


CREATE TABLE if not exists mvnforumMessage
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

CREATE TABLE if not exists mvnforumMessageStatistics
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

CREATE TABLE if not exists mvnforumPmAttachment
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

CREATE TABLE if not exists mvnforumPmAttachMessage
(
   MessageID                      INT                            NOT NULL,
   PmAttachID                     INT                            NOT NULL,
   RelationType                   INT                            NOT NULL,
   RelationOption                 INT                            NOT NULL,
   RelationStatus                 INT                            NOT NULL,
   PRIMARY KEY (MessageID, PmAttachID)
);

UPDATE mvnforumMessageFolder SET FolderOrder = 2 WHERE FolderName = 'Sent';

ALTER TABLE mvnforumMessageFolder ADD FolderStatus INT NOT NULL DEFAULT 0 AFTER FolderOrder;

ALTER TABLE mvnforumMessageFolder ADD FolderOption INT NOT NULL DEFAULT 0 AFTER FolderStatus;

ALTER TABLE mvnforumMessageFolder ADD FolderType INT NOT NULL DEFAULT 0 AFTER FolderOption;

ALTER TABLE mvnforumThread ADD ThreadAttachCount INT NOT NULL DEFAULT 0 ;

ALTER TABLE mvnforumAttachment CHANGE AttachDesc AttachDesc TEXT NOT NULL ;

ALTER TABLE mvnforumMember CHANGE MemberPassword MemberPassword VARCHAR(200) NOT NULL ;

ALTER TABLE mvnforumMember ADD MemberExpireDate DATETIME AFTER MemberModifiedDate ;
UPDATE mvnforumMember SET MemberExpireDate = MemberCreationDate ;

