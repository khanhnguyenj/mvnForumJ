/*
-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc1_to_1_0_0_rc2/mvnForum_update_interbase_unicode_FSS.sql,v 1.4 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:11:32 $
--
-- This script is used to upgrade mvnForum from RC1 to RC2
-- This script creates 3 new table : mvnforumMemberForum
--                                   mvnforumFavoriteThread
--                                   mvnforumRank
--
-- Database: Interbase Unicode
*/

/*
drop table mvnforumMemberForum;
drop table mvnforumFavoriteThread;
drop table mvnforumRank;
*/

create generator mvnforumRank_seq;

commit;


create table mvnforumMemberForum
(
   MemberID                       INTEGER                        NOT NULL,
   ForumID                        INTEGER                        NOT NULL,
   Permission                     INTEGER                        NOT NULL,
   PRIMARY KEY (MemberID, ForumID, Permission)
);

create index MemberForum_1_idx on mvnforumMemberForum
(
   MemberID
);

create index MemberForum_2_idx on mvnforumMemberForum
(
   ForumID
);

create table mvnforumFavoriteThread
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

create index FavorThread_1_idx on mvnforumFavoriteThread
(
   MemberID
);

create index FavorThread_2_idx on mvnforumFavoriteThread
(
   ThreadID
);

create table mvnforumRank
(
   RankID                         INTEGER                        NOT NULL,
   RankMinPosts                   INTEGER                        NOT NULL,
   RankLevel                      INTEGER                        NOT NULL,
   RankTitle                      VARCHAR(80)                    NOT NULL,
   RankImage                      VARCHAR(250)                   NOT NULL,
   RankType                       INTEGER                        NOT NULL,
   RankOption                     INTEGER                        NOT NULL,
   PRIMARY KEY (RankID),
   UNIQUE (RankMinPosts),
   UNIQUE (RankTitle)
);


/* Change sentence finalizer to '!!'                                         */
set term ^;

create trigger mvnforumRank_trig_autoinc for mvnforumRank
active before insert position 1
as
begin
  if (new.RankID is null) then
     new.RankID = gen_id(mvnforumRank_seq, 1);
end
^

/* Return sentence finalizer to ';'                                          */
set term ;^

commit;



/* data for table mvnforumRank                                               */
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (0, 0, 'Stranger',                   '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (20, 0, 'Newbie',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (50, 0, 'Member',                    '',        0,        0);
INSERT INTO mvnforumRank (RankMinPosts, RankLevel, RankTitle, RankImage, RankType, RankOption)
                  VALUES (100, 0, 'Advanced Member',          '',        0,        0);

commit;
