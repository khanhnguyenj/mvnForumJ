<%--
 - $Header: /cvsroot/mvnforum/mvnforum/srcweb/mvnplugin/mvnforum/user/docs.jsp,v 1.42 2009/07/16 03:21:13 lexuanttkhtn Exp $
 - $Author: lexuanttkhtn $
 - $Revision: 1.42 $
 - $Date: 2009/07/16 03:21:13 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2007 by MyVietnam.net
 -
 - All copyright notices regarding mvnForum MUST remain 
 - intact in the scripts and in the outputted HTML.
 - The "powered by" text/logo with a link back to
 - http://www.mvnForum.com and http://www.MyVietnam.net in 
 - the footer of the pages MUST remain visible when the pages
 - are viewed on the internet or intranet.
 -
 - This program is free software; you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation; either version 2 of the License, or
 - any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program; if not, write to the Free Software
 - Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -
 - Support can be obtained from support forums at:
 - http://www.mvnForum.com/mvnforum/index
 -
 - Correspondence and Marketing Questions can be sent to:
 - info at MyVietnam net
 -
 - @author: Minh Nguyen  
 - @author: Mai  Nguyen  
 --%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>

<%@ include file="inc_common.jsp"%>
<%@ include file="inc_doctype.jsp"%>
<fmt:bundle basename="i18n/mvnforum/mvnForum_i18n">
<mvn:html locale="${currentLocale}">
<mvn:head>
    <mvn:title><fmt:message key="mvnforum.common.forum.title_name"/> - Online Documentation</mvn:title>
<%@ include file="/mvnplugin/mvnforum/meta.jsp"%>
<link href="<%=onlineUser.getCssPath()%>" rel="stylesheet" type="text/css"/>
</mvn:head>
<mvn:body>
<%@ include file="header.jsp"%>
<br/>

<div class="nav center">
  <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" class="middle" />
  <a href="index"><fmt:message key="mvnforum.common.nav.index"/></a>&nbsp;&raquo;&nbsp;
  Online Documentation
</div>

<div class="pagedesc">
  Online documentation provides a HowTo approach to installing, administering and using <b>mvnForum</b> successfully.  If you do not see what you are looking for here, take a look at the <a class="command" href="faq">FAQ section</a>, carefully compiled for your edification.
</div>
<br/>

<table class="tborder" width="95%" cellspacing="0" cellpadding="2" align="center">
<mvn:cssrows>
  <tr>
     <td width="24px"></td><td width="8px"></td><td width="8px"></td><td width="8px"></td><td width="100%"></td>
  </tr>
  <tr class="portlet-section-header">
    <td align="center" colspan="5">Online Documentation - Table of Contents</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="1">
      <b>I</b>
    </td>
    <td colspan="4">
       <a href="#I">Introduction</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      I.1
    </td>
    <td colspan="3">
       <a href="#I1">What is a Forum</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
      I.2
    </td>
    <td colspan="3">
       <a href="#I2">Features</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="1">
      <b>II</b>
    </td>
    <td colspan="4">
       <a href="#II">Installation</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        II.1
    </td>
    <td colspan="3">
        <a href="#II1">System requirements</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        II.2
    </td>
    <td colspan="3">
        <a href="#II2">Application Server</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.2.a
    </td>
    <td colspan="2">
         <a href="#II2a">Apache Tomcat Application Server</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.2.b
    </td>
    <td colspan="2">
         <a href="#II2b">Caucho Resin Application Server</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.2.c
    </td>
    <td colspan="2">
         <a href="#II2c">Other...</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        II.3
    </td>
    <td  colspan="3">
         <a href="#II3">Database Server</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.3.a
    </td>
    <td colspan="2">
         <a href="#II3a">MySQL</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.3.b
    </td>
    <td colspan="2">
         <a href="#II3b">postgreSQL</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.3.c
    </td>
    <td colspan="2">
         <a href="#II3c">Microsoft SQL Server</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.3.d
    </td>
    <td colspan="2">
         <a href="#II3d">Oracle</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.3.e
    </td>
    <td colspan="2">
         <a href="#II3e">hsqldb</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        II.4
    </td>
    <td colspan="3">
        <a href="#II4">First-time Setup </a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        II.5
    </td>
    <td colspan="3">
         <a href="#II5">Versions</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.5.a
    </td>
    <td colspan="2">
         <a href="#II5a">Beta 1</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.5.b
    </td>
    <td colspan="2">
         <a href="#II5b">Beta 2</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.5.c
    </td>
    <td colspan="2">
         <a href="#II5c">Beta 3</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        II.5.d
    </td>
    <td colspan="2">
         <a href="#II5d">Release Candidate 1</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="1">
      <b>III</b>
    </td>
    <td colspan="4">
       <a href="#III">Administration</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        III.1
    </td>
    <td colspan="3">
         <a href="#III1">General</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        III.2
    </td>
    <td colspan="3">
         <a href="#III2">Groups</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td colspan="2">
         III.3
     </td>
     <td colspan="3">
          <a href="#III3">Users</a>
     </td>
  </tr>
  <tr class="<mvn:cssrow/>">
     <td colspan="2">
         III.4
     </td>
     <td colspan="3">
          <a href="#III4">Forums</a>
     </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="1">
      <b>IV</b>
    </td>
    <td colspan="4">
       <a href="#IV">User Guidelines</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.1
    </td>
    <td colspan="3">
        <a href="#IV1">Account Management</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
        IV.1.a
    </td>
    <td colspan="2">
        <a href="#IV1a">Profile settings</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.2
    </td>
    <td colspan="3">
        <a href="#IV2">Posting</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.3
    </td>
    <td colspan="3">
        <a href="#IV3">Groups</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.4
    </td>
    <td colspan="3">
        <a href="#IV4">Searching - to be supported at release xx</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.5
    </td>
    <td colspan="3">
        <a href="#IV5">Watching Topics - to be supported at release Release Candidate 1</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="2">
        IV.6
    </td>
    <td colspan="3">
        <a href="#IV6">Moderating - to be supported at release xx</a>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" height="16px">&nbsp;</td>
  </tr>
  <tr class="portlet-section-header">
    <td align="center" colspan="5">Online Documentation - Body</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      If the documentation did not help, then you might try the frequently asked questions (FAQ) documents to see if someone before you has had a similar problem.  Most of the questions and responses came from <a href="http://www.mvnForum.com">mvnForum web site</a>.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="I">I. Introduction
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="I1">I.1. What is a forum
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="I2">I.2. Features
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" height="16px">&nbsp;</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II">II. Installation
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II1">II.1. System Requirements
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      mvnForum has the following system requirements:
      <ul>
       <li>Any App Server supports Jsp 1.2 and Servlet 2.3
       <li>JDK 1.3 or later
       <li>A JDBC 2.0-compliant database (see <a href="#II3"> for list of supported databases</a><br/>
          (this release includes 4 drivers: mm.mysql, Connector/J, postgreSQL and hsqldb)
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II2">II.2. Application Server Setup
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      Follow these steps to setup the application server.
      <ul>
       <li>Create a home folder for mvnForum, which should not be accessible from the web.  This is where you will edit files and perform temporary actions while installing the forum software.</li>
       <li>Unzip the distributed zip file into this folder.</li>
       <li>Create a context in your app server.  See <a href="#II2a">below</a> for application server-specific configuration options.
       <li>In the extracted folder, find the dir <code>webapp\WEB-INF\classes</code>.
         There are 7 properties files here.  Open each file in a text editor and change
         the config parameters to appropriate values.  In each property file are the instructions for the parameters in the configurations.<br/>
         NOTE: Set database parameters to the correct values.  Database server-specific information is presented <a href="#II3a">below</a> and also in the header of the sql script file.<br/>
         NOTE: Currently you have to restart your App Server if you make changes
        in properties files (if not, your new config cannot take effect)
       <li>Copy the content of webapp dir in the extracted folder to the context dir that you have created above.</li>
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II2a">II.2.a. Apache Tomcat Application Server
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      Following is tomcat-specific application server setup configuration information:
      <ul>
        <li>To create a context, create a folder beneath tomcat/webapps (for example : mvnforum). This dir (context) MUST be in lower case (such as mvnforum).</li>
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II2b">II.2.b. Caucho Resin Application Server
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      Following is resin-specific application server setup configuration information:
      Following is a directory layout for a mvnForum installation in the ROOT context (where the &lt;resin-root&gt; is /var/www):
      <ul>
        <li>&lt;resin-root&gt;/mvnplugin</li>
        <li>&lt;resin-root&gt;/mvnplugin/mvnforum (contains all JSP files)</li>
        <li>&lt;resin-root&gt;/mvnplugin/WEB-INF (created by RESIN)</li>
        <li>&lt;resin-root&gt;/WEB-INF/</li>
        <li>&lt;resin-root&gt;/WEB-INF/web.xml (see below)</li>
        <li>&lt;resin-root&gt;/WEB-INF/lib/</li>
        <li>&lt;resin-root&gt;/WEB-INF/lib/commons-beanutils.jar, commons-logging.jar, mvnforum.jar, myvietnam.jar (activation.jar, jndi.jar, mail.jar, and servlet.jar are in /resin/lib because they are globally used)</li>
        <li>&lt;resin-root&gt;/WEB-INF/classes/</li>
        <li>&lt;resin-root&gt;/WEB-INF/classes/*.properties files (7 files)</li>
        <br/><br/>
      Snippet of web.xml:
      <pre>
      &lt;servlet-mapping&gt;
         &lt;servlet-name&gt;ForumUserServlet&lt;/servlet-name&gt;
         &lt;url-pattern&gt;/brds/*&lt;/url-pattern&gt;
      &lt;/servlet-mapping&gt;
      &lt;servlet-mapping&gt;
         &lt;servlet-name&gt;ForumAdminServlet&lt;/servlet-name&gt;
         &lt;url-pattern&gt;/mvnforumsadmin/*&lt;/url-pattern&gt;
      &lt;/servlet-mapping&gt;
      </pre>
      This results in an application that maps to:<br/>
      &nbsp;&nbsp;&nbsp;http://&lt;host name&gt;/brds/index
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II2c">II.2.c. Other...
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      Following is other-specific application server setup configuration information:
      <ul>
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3">II.3. Database Server Setup
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      Follow these steps to setup the mvnForum database schema and prepare the data tables with seed forum data.
      <ul>
      NOTE: If you are updating from either beta1 or beta2, then you must apply one patch to the existing database schema.  This patch adds the
      ability to have two forums with the same name under separate categories. To upgrade your database schema from
      beta1/beta2 release to beta3 release, run the sql script <code>sql\upgrade\1_0_0_beta2_to_1_0_0_beta3\mvnForum_update_mysql.sql</code><br/>
      The file: mvnForum_&lt;supported database server&gt;.sql can be use for beta1 and beta2, and beta3<br/>
      NOTE: before running the script, we highly recommend that you back up all your data.<br/>
       <li>In the extracted folder, find the file sql/mvnForum_&lt;supported database server&gt;.sql</li>
       <li>Create a database and confirm the correct database parameters have been set in the application server configuration step <a href="#II2">above</a></li>
       <li>Run the script. Please see the documentation for your database server for details on how to run sql scripts.  Details on the sql scripts are presented in the database server-specific sections below.</li>
       <li>Copy the jdbc driver for your database to the WEB-INF/lib folder of your context in step 3 above.  (Note that this release includes 4 drivers for MySql,
            postgreSQL and hsqldb)</li>
        NOTE: mvnForum includes an embedded database for hsqldb in folder sql/hsqldb, so you can
         copy this folder (hsqldb) to your mvnForumHome and set the proper config file
         mvncore_db_DBOptions.properties. This is the quickest way to get started with mvnForum.
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3a">II.3.a. MySQL
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3b">II.3.b. postgreSQL
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3c">II.3.c. Microsoft SQL Server
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3d">II.3.d. Oracle
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II3e">II.3.e. hsqldb
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II4">II.4. First-time setup
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
      With the database and application server setup, you should be able to navigate to your new forum.  The forum is setup with two separate zones, the administrator zone and the user zone.<br/><br/>
      With the default installation, the two zones are:<br/>
      http://www.[yourserver].com/[yourcontext]/mvnforum/index : home for mvnForum<br/>
      http://www.[yourserver].com/[yourcontext]/mvnforumadmin/index : Admin Zone
      <br/>
      If you installed in the ROOT context, the URLS are:<br/>
      http://www.[yourserver].com/mvnforum/index : home for mvnForum<br/>
      http://www.[yourserver].com/mvnforumadmin/index : home for the Admin Zone<br/>
      Follow these steps to login to the administrator zone and start configuring your forums:
      <ul>
       <li>Navigate to the Admin Zone and enter username = admin and password = admin</li>
       <li>If Login is successful, click a Test System Configuration to check that the mvnForum has been properly configured. </li>
       <li>The index page you see when you login lists server, MyVietnam Framework, application server version and database information.</li>
       <li>Change the password and edit your profile by clicking Forum Index, then click MyProfile.</li>
       <li>Click Forum Management to create forum for your community.</li>
       <li>You can create new users and set permissions for these new users.</li>
      </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II5">II.5. Versions
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
       Information about Versions - to maintain some history
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II5a">II.5.a. Beta 1
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
    Release date: 23 October 2002<p>
    New feature in beta1 release: <p>
    <p><strong>General features:</strong>
    <li>Based on MVC architecture
    <li>Built-in Database connection pool
    <li>Support Datasource configuration if your Servlet Container supports Datasource

    <p><strong>Security features:</strong>
    <li>MD5 Encrypted Passwords for greater security
    <li>Password Reset via email (forgot password)

    <p><strong>User features:</strong>
    <li>Member lists
    <li>Who's Online allows you to view all users visiting your board
    <li>Viewable profiles
    <li>Comprehensive User Control Panel
    <li>Users can (optionally) edit posts
    <li>User can choose avatar, either from built-in avatars or upload his own avatar
    <li>Dynamic Signatures

    <p><strong>Administration features:</strong>
    <li>Comprehensive system information and system diagnosis
    <li>Create an unlimited amount of forums
    <li>Re-order, edit forums at any time
    <li>Create an unlimited amount of categories
    <li>Re-order, edit categories at any time
    <li>Assign members to any group at any time
    <li>Restrict permissions for an entire group at a time
    <li>Admin can email in the AdminZone
    <li>Allow access to the moderation features
    <li>Allow access to the Administration Control Panel
</ul>

    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II5b">II.5.b. Beta 2
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
    Release date: 16 December 2002<p>
    New feature in beta2 release:<p>
    <p><strong>General features:</strong>

    <p><strong>Security features:</strong>
    <li>User cannot view email until he have logged in (avoid spam and email-harvest tools)

    <p><strong>User features:</strong>
    <li>Who's Online allows you to view all users visiting your board <font color="#FF0000"><strong>[improved]</strong></font>
    <li>Support 5 sorting options when viewing Recent threads and Threads in forum
    <li>Support most of mvnCode, such as [b] : <b>bold</b>, [color=deeppink] : <span style="color: deeppink">Deep pink text</span>
    <li>Support full mvnEmotion, such as
    [:)] <img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/smile.gif" border="0" alt="smile" />,
    [:D] <img src="<%=contextPath%>/mvnplugin/mvnforum/images/emotion/biggrin.gif" border="0" alt="biggrin" />
    <li>User can choose avatar, either from built-in avatars or upload his own avatar <font color="#FF0000"><strong>[improved]</strong></font>

    <p><strong>Administration features:</strong>
    <li>Admin can view the IP of any post (both creation time and last edit time)
    <li>Admin can delete any forums at any time
    <li>Forum Moderator can edit any posts at any time
    <li>Forum Moderator can delete any threads at any time
    <li>Admin can remove member from group at any time
    <li>Admin can delete any groups at any time
</ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II5c">II.5.c. Beta 3
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
    Release date: 2 March 2003<p>
    New feature in beta3 release:<p>
    <ul>
      <li><b>Multi-databases</b> (currently supports 6 DBMS: MySQL, Oracle, Sql Server, postgreSQL, hsqldb, Interbase/Firebird)
      <li><b>Localization</b> (currently localized in 6 languages: English, Vietnamese, Russian, Traditional Chinese, Simplified Chinese, Spanish)
      <li><b>Internationalization</b> (support all languages in the backend)
      <li>Support IP block to prevent certain IP to access the system
      <li>Enable/disable member (disabled member cannot login)
      <li>In admin module, only show links that user have previledge on them
      <li>ForumAdmin can delete empty category
      <li>User now can choose a prefered displayed language when registering
      <li>New Online FAQ/Documentation
      <li>Support Post Preview
      <li>Date/time are correctly formated based on user preferred locale
      <li>Moderator can delete any posts
      <li>Support Gif/Png Avatar (beta2 only support Jpg Avatar)
      <li>Support 5 additional tags: [quote] , [url] , [size] , [font] and [list]
      <li>Show link for AIM, Yahoo Messenger, ICQ
      <li>Support automatic login (use cookie)
      <li>Support realm authentication
      <li>Support any customized authentication
      <li>Support Thread/Post icons
      <li>Show new/nonew status of a forum
      <li>Show hot/normal and new/nonew status of a thread
      <li>Show member's title based on the number of posts
    </ul>
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="II5d">II.5.d. RC1
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Information about Release Candidate 1 - including date of release and list of features added
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" height="16px">&nbsp;</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="III">III. Administration
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="III1">III.1. General
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        General information about accessing the Admin section of mvnForum, the default admin password,
        and basic administration features supported now and expected for the future.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="III2">III.2. Groups
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Administering User Groups
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="III3">III.3. Users
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Administering Users
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="III4">III.4. Forums
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Administering Forums
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" height="16px">&nbsp;</td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV">IV. User Guidelines
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV1">IV.1. Account Management
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        General information about setting up a user account, and an introduction
        to the user profile section.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV1a">IV.1.a. Profile Settings
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        An itemized list of the profile settings for a user.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV2">IV.2. Posting
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Guidelines for posting to a forum.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV3">IV.3. Groups
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Participating in user groups and the permissions associated with groups.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV4">IV.4. Search the Forum
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        The Search feature will be implemented in a future release of mvnForum.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV5">IV.5. Watching Topics
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        The ability to watch (or be notified of additions to a particular thread) a forum
        or forum thread will be introduced in RC1.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="3">
      <img src="<%=contextPath%>/mvnplugin/mvnforum/images/nav.gif" alt="" />
    </td>
    <td colspan="2">
      <a name="IV6">IV.6. Moderating
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5">
        Forum moderation is expected to be released in a future release.
    </td>
  </tr>
  <tr class="<mvn:cssrow/>">
    <td colspan="5" height="16px">&nbsp;</td>
  </tr>
</mvn:cssrows>
</table>

<br/>
<%@ include file="/mvnplugin/mvnforum/user/footer.jsp"%>
</mvn:body>
</mvn:html>
</fmt:bundle>
