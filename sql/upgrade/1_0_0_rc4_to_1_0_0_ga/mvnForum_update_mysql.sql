# $Header: /cvsroot/mvnforum/mvnforum/sql/upgrade/1_0_0_rc4_to_1_0_0_ga/mvnForum_update_mysql.sql,v 1.2 2005/10/12 19:11:11 minhnn Exp $
# $Author: minhnn $
# $Revision: 1.2 $
# $Date: 2005/10/12 19:11:11 $
#
# This script is used to upgrade mvnForum from RC4 to GA
#
# Database: MySql



ALTER TABLE mvnforumMember CHANGE MemberTimeZone MemberTimeZone FLOAT NOT NULL ;

ALTER TABLE mvnforumMember CHANGE MemberEmailVisible MemberEmailVisible INT NOT NULL ;

ALTER TABLE mvnforumMember CHANGE MemberNameVisible MemberNameVisible INT NOT NULL ;

ALTER TABLE mvnforumMember CHANGE MemberGender MemberGender INT NOT NULL ;

