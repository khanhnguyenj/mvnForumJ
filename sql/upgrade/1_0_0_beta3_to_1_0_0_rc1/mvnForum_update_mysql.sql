-- This script is used to upgrade mvnForum from beta3 to rc1
-- This sql script creates 2 new table : mvnforumWatch and mvnforumAttachment
--
-- Database: MySql



# drop table if exists mvnforumWatch;
# drop table if exists mvnforumAttachment;


create table if not exists mvnforumWatch
(
   WatchID                        INT                            NOT NULL AUTO_INCREMENT,
   MemberID                       INT                            NOT NULL,
   CategoryID                     INT                            NOT NULL,
   ForumID                        INT                            NOT NULL,
   ThreadID                       INT                            NOT NULL,
   WatchType                      INT                            NOT NULL,
   WatchOption                    INT                            NOT NULL,
   WatchStatus                    INT                            NOT NULL,
   WatchCreationDate              DATETIME                       NOT NULL,
   WatchLastSentDate              DATETIME                       NOT NULL,
   WatchEndDate                   DATETIME                       NOT NULL,
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

create table if not exists mvnforumAttachment
(
   AttachID                       INT                            NOT NULL AUTO_INCREMENT,
   PostID                         INT                            NOT NULL,
   MemberID                       INT                            NOT NULL,
   AttachFilename                 VARCHAR(250)                   NOT NULL,
   AttachFileSize                 INT                            NOT NULL,
   AttachMimeType                 VARCHAR(70)                    NOT NULL,
   AttachDesc                     VARCHAR(250)                   NOT NULL,
   AttachCreationIP               VARCHAR(20)                    NOT NULL,
   AttachCreationDate             DATETIME                       NOT NULL,
   AttachModifiedDate             DATETIME                       NOT NULL,
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

