-- $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_1_ga_to_1_3_ga/mvnForum_update_JDBC.sql,v 1.4 2010/04/06 11:11:32 minhnn Exp $
-- $Author: minhnn $
-- $Revision: 1.4 $
-- $Date: 2010/04/06 11:11:32 $
--
-- This script is used to upgrade mvnForum from 1.1 GA or 1.2 GA to 1.3 GA
--
-- Database: Generic JDBC



ALTER TABLE mvnforumMemberGroup ADD ExpireDate TIMESTAMP ;

UPDATE mvnforumMemberGroup SET ExpireDate = CreationDate ;
