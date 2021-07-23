-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_ga_to_1_1_ga/mvnForum_update_hsqldb.sql,v 1.5 2008/01/14 09:50:09 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.5 $
-- $Date: 2008/01/14 09:50:09 $
--
-- This script is used to upgrade mvnForum from 1.0 GA to 1.1 GA
--
-- Database: hsqldb 1.8.0 or later
--
-- Note that this ALTER TABLE only supported in version 1.8.0 or later
-- so make sure you use the latest 1.8.x before run this script



ALTER TABLE mvnforumForum ADD ForumOwnerName VARCHAR(30) BEFORE LastPostMemberName ;
UPDATE mvnforumForum SET ForumOwnerName = '' ;

ALTER TABLE mvnforumThread ADD ThreadPriority INT BEFORE ThreadOption ;
UPDATE mvnforumThread SET ThreadPriority = 0 ;

ALTER TABLE mvnforumMember ADD COLUMN MemberPasswordExpireDate DATETIME BEFORE MemberLastLogon;
UPDATE mvnforumMember SET MemberPasswordExpireDate = MemberCreationDate ;