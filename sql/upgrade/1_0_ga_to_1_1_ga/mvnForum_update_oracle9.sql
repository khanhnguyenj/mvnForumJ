-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_ga_to_1_1_ga/mvnForum_update_oracle9.sql,v 1.8 2010/04/06 11:11:33 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.8 $
-- $Date: 2010/04/06 11:11:33 $
--
-- This script is used to upgrade mvnForum from 1.0 GA to 1.1 GA
--
-- Database: Oracle 9i or later



ALTER TABLE mvnforumForum ADD ForumOwnerName VARCHAR(30);
UPDATE mvnforumForum SET ForumOwnerName = '' ;

ALTER TABLE mvnforumThread ADD ThreadPriority INT ;
UPDATE mvnforumThread SET ThreadPriority = 0 ;

ALTER TABLE mvnforumMember ADD MemberPasswordExpireDate TIMESTAMP ;
UPDATE mvnforumMember SET MemberPasswordExpireDate = MemberCreationDate ;