-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc3_to_1_0_0_rc4/mvnForum_update_oracle9.sql,v 1.8 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.8 $
-- $Date: 2010/04/06 11:11:32 $
--
-- This script is used to upgrade mvnForum from RC2/RC3 to RC4
-- This script creates 3 new table : mvnforumMessage
--                                   mvnforumMessageStatistics
--                                   mvnforumPmAttachment
--                                   mvnforumPmAttachMessage
--
-- Database: Oracle 9i or Oracle 10g
-- Note: in ALTER TABLE "DEFAULT 0" must come before "NOT NULL"



-- drop table mvnforumMessage;
-- drop table mvnforumMessageStatistics;
-- drop table mvnforumPmAttachment;
-- drop table mvnforumPmAttachMessage;

--
-- drop sequence
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
   PmAttachID                     INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   PmAttachFilename               VARCHAR2(250)                  NOT NULL,
   PmAttachFileSize               INT                            NOT NULL,
   PmAttachMimeType               VARCHAR2(70)                   NOT NULL,
   PmAttachDesc                   LONG VARCHAR                   NULL,
   PmAttachCreationIP             VARCHAR2(20)                   NOT NULL,
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

create or replace trigger mvnforumMessage_trig_autoinc
before insert on mvnforumMessage
for each row
begin
  if (:new.MessageID is null) then
    select mvnforumMessage_seq.nextval into :new.MessageID from dual;
  end if;
end;
/

create or replace trigger mvnforumPmAttach_trig_autoinc
before insert on mvnforumPmAttachment
for each row
begin
  if (:new.PmAttachID is null) then
    select mvnforumPmAttachment_seq.nextval into :new.PmAttachID from dual;
  end if;
end;
/

UPDATE mvnforumMessageFolder SET FolderOrder = 2 WHERE FolderName = 'Sent';

ALTER TABLE mvnforumMessageFolder ADD FolderStatus INT DEFAULT 0 NOT NULL ;

ALTER TABLE mvnforumMessageFolder ADD FolderOption INT DEFAULT 0 NOT NULL ;

ALTER TABLE mvnforumMessageFolder ADD FolderType INT DEFAULT 0 NOT NULL ;

ALTER TABLE mvnforumThread ADD ThreadAttachCount INT DEFAULT 0 NOT NULL ;

ALTER TABLE mvnforumAttachment MODIFY AttachDesc LONG VARCHAR ;

ALTER TABLE mvnforumMember MODIFY MemberPassword VARCHAR2(200) ;

ALTER TABLE mvnforumMember ADD MemberExpireDate TIMESTAMP ;
UPDATE mvnforumMember SET MemberExpireDate = MemberCreationDate ;

commit;
