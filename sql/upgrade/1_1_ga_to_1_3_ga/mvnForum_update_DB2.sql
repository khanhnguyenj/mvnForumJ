-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_1_ga_to_1_3_ga/mvnForum_update_DB2.sql,v 1.3 2008/11/12 10:30:41 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.3 $
-- $Date: 2008/11/12 10:30:41 $
--
-- This script is used to upgrade mvnForum from 1.1 GA or 1.2 GA to 1.3 GA
--
-- Database: DB2



ALTER TABLE mvnforumMemberGroup ADD COLUMN ExpireDate TIMESTAMP ;

UPDATE mvnforumMemberGroup SET ExpireDate = CreationDate ;