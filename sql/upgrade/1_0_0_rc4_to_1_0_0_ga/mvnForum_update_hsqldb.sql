-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc4_to_1_0_0_ga/mvnForum_update_hsqldb.sql,v 1.3 2006/02/12 16:04:26 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.3 $
-- $Date: 2006/02/12 16:04:26 $
--
-- This script is used to upgrade mvnForum from RC4 to GA
--
-- Database: hsqldb 1.8.0 or later
--
-- Note that this ALTER TABLE only supported in version 1.8.0 or later
-- so make sure you use the latest 1.8.x before run this script



ALTER TABLE mvnforumMember ALTER COLUMN MemberTimeZone FLOAT NOT NULL ;