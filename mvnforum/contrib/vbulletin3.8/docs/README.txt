******************************************************************
* $Id: README.txt,v 1.7 2009/12/02 10:53:12 lexuanttkhtn Exp $
******************************************************************
* Requirements
- Require 4 projects: myvietnam, mvnforum, myvietnam_enterprise, mvnforum_enterprise

* How to merge vBulletin database to mvnforum
- Configure myvietnam/properties/mvncore.xml file for mvnforum database
- Configure myvietnam/properties/secondary-database.xml file for vBulletin database
- Build mvnforum_enterprise first (run "ant compile") to have all necessary libraries
- Config file com.mvnforum.vbulletin.Migrator in mvnforum/contrib/vbulletin3.8, change
  the key VBULLETIN_HOUR_ADJUSTMENT suitable to your system.
- Change directory to mvnforum/contrib/vbulletin3.8, run ant task "ant run"

*Note

- When migrate users:
 + if user name is not a good name (good name is [a-z][A-Z][0-9](.)(_)(@)),
   the user name will be "User"
 + if user name is too long (greater than 28 digits), it will be cut left 28 digits
 + if user name is existed, then we will add the auto increment number after the user name

 + if email is too long (greater than 58 digits), it will be cut left 58 digits
 + if email is existed, then we will add the auto increment number after the email

 + add member name "Guest" by default, then we use it when member is not existed in user table,
   but that member name is referenced by thread, post,...
   (because in vbulletin, they remove user, but does not remove related threads, posts,...)

- When migrate thread, post:
 + if thread topic is empty, then we add the topic "Untitled thread"
 + if thread body is empty, then we add the body "Empty thread's body."
 + if thread body is too long (greater than 250 digits), it will be cut left 250 digits

 + if post topic is empty, then we add the topic "Untitled post"
 + if post body is empty, then we add the body "Empty post's body."
 + if post body is too long (greater than 65535 digits in mysql, or 1073741823 digits in SQL Server),
   it will be cut left 65535 digits in mysql, or 1073741823 digits in SQL Server
