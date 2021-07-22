/*
-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc3_to_1_0_0_rc4/mvnForum_update_interbase.sql,v 1.7 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.7 $
-- $Date: 2010/04/06 11:11:32 $
--
-- This script is used to upgrade mvnForum from RC2/RC3 to RC4
-- This script creates 3 new table : mvnforumMessage
--                                   mvnforumMessageStatistics
--                                   mvnforumPmAttachment
--                                   mvnforumPmAttachMessage
--
-- Database: Interbase/Firebird
*/

/*
drop table mvnforumMessage;
drop table mvnforumMessageStatistics;
drop table mvnforumPmAttachment;
drop table mvnforumPmAttachMessage;
*/

create generator mvnforumMessage_seq;
create generator mvnforumPmAttachment_seq;

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

UPDATE mvnforumMessageFolder SET FolderOrder = 2 WHERE FolderName = 'Sent';

ALTER TABLE mvnforumMessageFolder ADD FolderStatus INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE mvnforumMessageFolder ADD FolderOption INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE mvnforumMessageFolder ADD FolderType INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE mvnforumThread ADD ThreadAttachCount INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE mvnforumAttachment ALTER AttachDesc TYPE TEXT ;

ALTER TABLE mvnforumMember ALTER MemberPassword TYPE VARCHAR(200) ;

ALTER TABLE mvnforumMember ADD MemberExpireDate TIMESTAMP NOT NULL;
UPDATE mvnforumMember SET MemberExpireDate = MemberCreationDate ;

/* Create triggers */

set term ^;
create trigger mvnforumMessage_trig_autoinc for mvnforumMessage
active before insert position 1
as
begin
  if (new.MessageID is null) then
     new.MessageID = gen_id(mvnforumMessage_seq, 1);
end
^

create trigger mvnforumPmAttach_trig_autoinc for mvnforumPmAttachment
active before insert position 1
as
begin
  if (new.PmAttachID is null) then
     new.PmAttachID = gen_id(mvnforumPmAttachment_seq, 1);
end
^
/* Return sentence finalizer to ';'                                          */
set term ;^

commit;
