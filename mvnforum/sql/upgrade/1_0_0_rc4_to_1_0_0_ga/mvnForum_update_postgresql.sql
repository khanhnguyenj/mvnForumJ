-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc4_to_1_0_0_ga/mvnForum_update_postgresql.sql,v 1.4 2008/02/28 01:55:06 lexuanttkhtn Exp $
-- $Author: lexuanttkhtn $
-- $Revision: 1.4 $
-- $Date: 2008/02/28 01:55:06 $
--
-- This script is used to upgrade mvnForum from RC4 to GA
--
-- Database: postgreSQL 8.0 or later
--
-- Note that this ALTER TABLE only supported in version 8.0 or later



ALTER TABLE mvnforumMember ALTER COLUMN MemberTimeZone TYPE FLOAT;

