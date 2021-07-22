/*
 * $Header: /cvsroot/mvnforum/mvnforum/docs/README.txt,v 1.41 2010/07/09 02:46:52 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.41 $
 * $Date: 2010/07/09 02:46:52 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2006 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain 
 * intact in the scripts and in the outputted HTML.
 * The "powered by" text/logo with a link back to
 * http://www.mvnForum.com and http://www.MyVietnam.net in 
 * the footer of the pages MUST remain visible when the pages
 * are viewed on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Support can be obtained from support forums at:
 * http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 */

//////////////////////////////////////////////////////////////////////////////////
//      mvnForum 1.2 GA   -  $Date: 2010/07/09 02:46:52 $
//////////////////////////////////////////////////////////////////////////////////




    IMPORTANT: READ THIS FILE THOTHOUGHLY BEFORE USING mvnForum



0. Table of Contents
==================================================================================

0. Table of Contents
1. mvnForum 1.2 GA Introduction
2. Installation/Upgrade Instruction
3. Build Instructions
4. mvnForum Development
5. mvnForum License Agreement



1. mvnForum 1.1 General Availability (GA) Introduction
==================================================================================

Thank you for downloading mvnForum from http://www.mvnForum.com. If you
downloaded this zip-file from an other sites, please check http://www.mvnForum.com
for the latest official release.

This version is the eighth and greatest public release and has a lot of new exciting features.
The next release will have major refactor and require JDK 1.4. 
Please see the file TODO.TXT for the planned tasks.

At this point of development, we need your help with testing, bug report and feature
discussion, please post your feedback, comments and bugs to http://www.mvnForum.com

NOTE: The mvnForum distribution package does not have the file FEATURE.txt. The
      reason for this is mvnForum features are added quite often, so please
      visit http://www.mvnForum.com/mvnforumweb/feature.jsp for the
      latest features list.

NOTE: At this 1.2 GA release we support 9 DBMS: MySql, Oracle, Sql Server, DB2,
      PostgreSql, hsqldb and Interbase/Firebird, SAPDB, Sybase. The next releases (1.1 or later)
      will support more databases. (If mvnForum doesn't support your database, don't worry,
      you can port the script from mvnForum_JDBC.sql to your database easily)

NOTE: At this 1.2 GA release mvnForum supports 23 languages: English, Vietnamese, Russian,
      Traditional Chinese, Simplified Chinese, Spanish, French, Italian,
      German, Danish, Latvian, Serbian (latin), Serbian (cyrillic), Arabic,
      Slovenian, Dutch, Slovenian, Greek, Japanese, Korean, Portuguese, Turkish and Norwegian.
      (If mvnForum has not been localized to your language, don't worry,
      you can translate the file mvnForum_i18n.properties and mvncore_java_i18n.properties
      to your language, and if you would like mvnForum officially supports your language
      in later releases, please email me your translation)

All security bugs should be sent directly to minhnn at MyVietnam _ net and
the subject should begin with [mvnForum SECURITY]

mvnForum should run on any Servlet Container which supports JSP 1.2 and Servlet 2.3.
However, this release has only been tested on the following configurations:

on the following databases:
    * MySql version 3.23.51/5.0.18
    * Oracle 8i 8.1.7
    * Sql Server 2000
    * postgreSQL 7.3/8.1.x
    * hsqldb 1.7.1/1.7.2/1.7.3/1.8.0_02
on the following App Servers:
    * Tomcat 4.1.27
    * Tomcat 5.0.16/5.0.28
    * Tomcat 5.5.16
    * Resin 2.1.11
    * Resin 3.0.8
    * Web Logic 7, Web Logic 8
    * Jboss 3.2.2 & Jetty 4.2.14
on the following JDK:
    * Sun JDK 1.4
    * Sun JDK 1.4.1
    * Sun JDK 1.4.2
    * Sun JDK 1.5.0_06
on the following OS:
    * Windows 2000
    * Fedora Core 4/Fedora Core 5

Developed by
- Minh Nguyen <minhnn at MyVietnam net>
- Mai Nguyen <mai.nh at MyVietnam net>
and the support group at http://www.mvnForum.com

And many thanks to all the contributors to mvnForum, among them are:
- Anatol Pomozov       (aka wassup)     anatol.pomozov at pms-software com
- Cord Thomas          (aka cord)       cord at lupinex com
- Dejan Krsmanovic     (aka dejan)      dejan_krsmanovic at yahoo com
- Gunther Strube       (aka gbs)        gbs at users.sourceforge net
- Imants Firsts        (aka Fii)        imantsf at inbox lv
- Luis Miguel Hernanz                   luish at germinus com
- Sven Kohler          (aka skoehler)   skoehler at upb de
- Igor Manic           (aka imanic)     imanic at users.sourceforge net



2. Installation/Upgrade Instructions
==================================================================================
Please read the file INSTALL.TXT for detailed information on
how to install and/or upgrade mvnForum. This instruction is for
the binary distribution package of mvnForum.



3. Build Instructions
==================================================================================
Please read the file BUILD.TXT for detailed information on
how to build mvnForum. This instruction is for
the source distribution package of mvnForum.



3. mvnForum Development
==================================================================================
If you would like join the mvnForum development team and contribute your
code/patch, please read the file DEVELOPER.TXT first. It contains some
important notes



5. mvnForum License Agreement
==================================================================================
You MUST agree the "mvnForum License Agreement" before using mvnForum:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or any later version.

All copyright notices regarding mvnForum MUST remain intact in
the scripts and in the outputted HTML. The "powered by" text/logo
with a link back to http://www.mvnForum.com and http://www.MyVietnam.net
in the footer of the pages MUST remain visible when the pages are viewed
on the internet or intranet.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

STOP USING MVNFORUM NOW IF YOU DO NOT AGREE THE ABOVE LICENSE

NOTE: In case you need to remove this "Powered by mvnForum", please contact us at
      web site www.MyVietnam.net to order a special "Copyright notice Removal" license
      with a small license fee. After receiving this special license, you can remove
      the copyright notice in this footer.jsp file.

NOTE: This product includes software developed by the
      Apache Software Foundation (http://www.apache.org/) and others:

- monre-portlet

      wp.l2.urlhelper.jar

- mvnad_enterprise

      geoip-1.2.2.jar
      jcommon-1.0.10.jar
      jfreechart-1.0.6.jar

- mvncms

      antlr-runtime-3.1.1.jar
      BeanShell - 1.2b7        http://www.beanshell.org/download.html
      chenillekit-core-1.0.2.jar
      chenillekit-tapestry-1.0.2.jar
      javassist                http://tapestry.apache.org/tapestry5.2-dev/tapestry-ioc/dependencies.html
      oscache.jar
      oscore-2.2.5.jar
      osworkflow-2.8.0.jar
      poi-3.0.1-FINAL-20070705.jar
      propertyset-1.4.jar
      rssutils.jar
      stax-api-1.0.1.jar
      stax2-api-3.0.1.jar
      tapestry-core-5.1.0.5.jar
      tapestry-ioc-5.1.0.5.jar
      tapestry-upload-5.1.0.5.jar
      tapestry5-annotations-5.1.0.5.jar
      tapx-datefield-1.0.0-20090423.121507-40.jar
      woodstox-core-asl-4.0.3.jar

- mvncore-portal

      servlet.jar

- mvncore-portlet

      portlet-api-1.0.jar
      portlet_20.jar

- mvnforum

      FreeMarker               http://freemarker.sourceforge.net
      JCaptcha                 http://jcaptcha.sourceforge.net
      JSTL                     http://jakarta.apache.org/taglibs/index.html
      Lucene                   http://lucene.apache.org/
      Pager Taglib             http://jsptags.com/index.jsp
      standard.jar

- mvnforum_enterprise

      Web Services - Axis      http://ws.apache.org/axis/java/building-axis.html
      casclient.jar
      Commons Discovery        http://commons.apache.org/discovery/
      Commons Httpclient       http://hc.apache.org/httpclient-3.x/index.html
      Compass                  http://baofront.googlecode.com/svn-history/r9/trunk/zdb/lib/compass/compass14-2.2.0.jar
      jaxrpc.jar
      json-20070829.jar
      saaj.jar
      smack.jar
      smackx.jar
      tsik.jar
      wsdl4j-1.5.1.jar

- myvietnam

      Activation Framework     http://java.sun.com/products/javabeans/jaf/index.jsp
      commonj-twm.jar          http://www.java2s.com/Code/Jar/ABC/Downloadcommonjtwmjar.htm
      Common BeanUtils         http://jakarta.apache.org/commons/beanutils
      Common Codec             http://jakarta.apache.org/commons/codec
      Common Collections       http://jakarta.apache.org/commons/collections
      Commons Configuration    http://commons.apache.org/configuration/
      Common Digester          http://jakarta.apache.org/commons/digester
      Commons Fileupload       http://commons.apache.org/fileupload/
      Common IO                http://jakarta.apache.org/commons/io
      Common Lang              http://jakarta.apache.org/commons/lang
      Common Logging           http://jakarta.apache.org/commons/logging
      Concurrent               http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html
      dnsjava                  http://www.dnsjava.org/
      Dom4J                    http://www.dom4j.org
      Jakarta RegExp           http://jakarta.apache.org/regexp/index.html
      jaxen                    http://jaxen.codehaus.org/
      Log4J                    http://logging.apache.org
      Mail                     http://java.sun.com/products/javamail/
      servlet.jar
      slf4j-api-1.5.6.jar
      slf4j-log4j12-1.5.6.jar
      WhirlyCache              https://whirlycache.dev.java.net/
      junit-4.8.1.jar

- myvietnam_enterprise

      hunspell-darwin-i386.jar
      hunspell-darwin-ppc.jar
      hunspell-jws.jar
      hunspell-linux-amd64.jar
      hunspell-linux-i386.jar
      hunspell-win32-x86.jar
      hunspell.jar
      jna-darwin.jar
      jna-jws.jar
      jna-linux-amd64.jar
      jna-linux-i386.jar
      jna-win32-x86.jar
      jna.jar


      Aspirin                  https://aspirin.dev.java.net/
      Jzonic                   http://jlo.jzonic.org

      
      MySql Driver             http://dev.mysql.com/downloads/connector/j/3.0.html
      hsqldb                   http://hsqldb.sourceforge.net
      Jtds Driver              http://jtds.sourceforge.net
      PostgreSQL Driver        http://jdbc.postgresql.org

==================================================================================

Cheers!

minhnn (Minh Nguyen)
http://www.mvnForum.com
minhnn at MyVietnam d0t net
