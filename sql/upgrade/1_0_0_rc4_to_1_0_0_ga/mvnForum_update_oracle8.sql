-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc4_to_1_0_0_ga/mvnForum_update_oracle8.sql,v 1.3 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.3 $
-- $Date: 2010/04/06 11:11:32 $
--
-- This script is used to upgrade mvnForum from RC4 to GA
--
-- Database: Oracle 8i



ALTER TABLE mvnforumMember MODIFY MemberTimeZone FLOAT ;
