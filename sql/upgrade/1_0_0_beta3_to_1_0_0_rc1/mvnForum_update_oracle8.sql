-- This script is used to upgrade mvnForum from beta3 to rc1
-- This sql script creates 2 new table : mvnforumWatch and mvnforumAttachment
--
-- Database: Oracle 8i



-- drop table mvnforumWatch;
-- drop table mvnforumAttachment;

--
-- drop sequence
--
-- drop sequence mvnforumWatch_seq;
-- drop sequence mvnforumAttachment_seq;


create table mvnforumWatch
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

create index Watch_MemberID_idx on mvnforumWatch
(
   MemberID
);

create index Watch_CategoryID_idx on mvnforumWatch
(
   CategoryID
);

create index Watch_ForumID_idx on mvnforumWatch
(
   ForumID
);

create index Watch_ThreadID_idx on mvnforumWatch
(
   ThreadID
);

create table mvnforumAttachment
(
   AttachID                       INT                            NOT NULL,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 VARCHAR2(250)                  NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 VARCHAR2(70)                   NULL,
   AttachDesc                     VARCHAR2(250)                  NULL,
   AttachCreationIP               VARCHAR2(20)                   NOT NULL,
   AttachCreationDate             DATE                           NOT NULL,
   AttachModifiedDate             DATE                           NOT NULL,
   AttachDownloadCount            INT                            NOT NULL,
   AttachOption                   INT                            NOT NULL,
   AttachStatus                   INT                            NOT NULL,
   PRIMARY KEY (AttachID)
);

create index Attachment_PostID_idx on mvnforumAttachment
(
   PostID
);

create index Attachment_MemberID_idx on mvnforumAttachment
(
   MemberID
);

create sequence mvnforumWatch_seq;

create or replace trigger mvnforumWatch_trig_autoinc
before insert on mvnforumWatch
for each row
begin
  if (:new.WatchID is null) then
    select mvnforumWatch_seq.nextval into :new.WatchID from dual;
  end if;
end;
/

create sequence mvnforumAttachment_seq;

create or replace trigger mvnforumAttach_trig_autoinc
before insert on mvnforumAttachment
for each row
begin
  if (:new.AttachID is null) then
    select mvnforumAttachment_seq.nextval into :new.AttachID from dual;
  end if;
end;
/

commit;
