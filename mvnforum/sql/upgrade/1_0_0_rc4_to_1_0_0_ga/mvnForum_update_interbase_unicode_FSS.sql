/*
-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc4_to_1_0_0_ga/mvnForum_update_interbase_unicode_FSS.sql,v 1.2 2006/02/12 16:04:26 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.2 $
-- $Date: 2006/02/12 16:04:26 $
--
-- This script is used to upgrade mvnForum from RC4 to GA
--
-- Database: Interbase/Firebird
*/


ALTER TABLE mvnforumMember ALTER MemberTimeZone TYPE FLOAT ;

