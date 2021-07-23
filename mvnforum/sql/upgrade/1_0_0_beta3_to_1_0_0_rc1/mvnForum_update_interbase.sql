/*
-- This script is used to upgrade mvnForum from beta3 to rc1
-- This sql script creates 2 new table : mvnforumWatch and mvnforumAttachment
--
-- Database: Interbase/Firebird
*/

/*
drop table mvnforumWatch;
drop table mvnforumAttachment;
*/

/* Create generator                                                          */
create generator mvnforumWatch_seq;
create generator mvnforumAttachment_seq;

commit;


create table mvnforumWatch
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
   AttachID                       INTEGER                        NOT NULL,
   PostID                         INTEGER                        NOT NULL,
   MemberID                       INTEGER                        NOT NULL,
   AttachFilename                 VARCHAR(250)                   NOT NULL,
   AttachFileSize                 INTEGER                        NOT NULL,
   AttachMimeType                 VARCHAR(70)                    NOT NULL,
   AttachDesc                     VARCHAR(250)                   NOT NULL,
   AttachCreationIP               VARCHAR(20)                    NOT NULL,
   AttachCreationDate             TIMESTAMP                      NOT NULL,
   AttachModifiedDate             TIMESTAMP                      NOT NULL,
   AttachDownloadCount            INTEGER                        NOT NULL,
   AttachOption                   INTEGER                        NOT NULL,
   AttachStatus                   INTEGER                        NOT NULL,
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


/* Change sentence finalizer to '!!'                                         */
set term ^;

create trigger mvnforumWatch_trig_autoinc for mvnforumWatch
active before insert position 1
as
begin
  if (new.WatchID is null) then
     new.WatchID = gen_id(mvnforumWatch_seq, 1);
end
^

create trigger mvnforumAttachment_trig_autoinc for mvnforumAttachment
active before insert position 1
as
begin
  if (new.AttachID is null) then
     new.AttachID = gen_id(mvnforumAttachment_seq, 1);
end
^

/* Return sentence finalizer to ';'                                          */
set term ;^

commit;
