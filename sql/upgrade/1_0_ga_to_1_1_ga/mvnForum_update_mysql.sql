# $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_ga_to_1_1_ga/mvnForum_update_mysql.sql,v 1.10 2008/11/19 07:36:11 lexuanttkhtn Exp $
# $Author: lexuanttkhtn $
# $Revision: 1.10 $
# $Date: 2008/11/19 07:36:11 $
#
# This script is used to upgrade mvnForum from 1.0 GA to 1.1 GA
#
# Database: MySql



ALTER TABLE mvnforumForum ADD ForumOwnerName VARCHAR(30) AFTER CategoryID ;
UPDATE mvnforumForum SET ForumOwnerName = '' ;

ALTER TABLE mvnforumThread ADD ThreadPriority INT AFTER ThreadType ;
UPDATE mvnforumThread SET ThreadPriority = 0 ;

ALTER TABLE mvnforumMember ADD MemberPasswordExpireDate DATETIME  NULL DEFAULT "0000-00-00 00:00:00"  AFTER MemberExpireDate;
UPDATE mvnforumMember SET MemberPasswordExpireDate = MemberCreationDate ;