<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/TR/xhtml1/transitional"
                version="1.0">

<!-- change the following path to the root path of the docbook installation 
     if you are using a more recent version of docbook xsl, then by all means
     update the path.  If you have linked your actual docbook xsl path to a simple
     path link 'xsl' (which is a good idea), then this href becomes /xsl/html/docbook.xsl -->
<!-- 
    <xsl:import href="file:///c:/docbook/xsl/html/docbook.xsl" />
    <xsl:import href="/mnt/sda6/dev/softs/docbook/docbook/xsl/html/docbook.xsl" />
-->
<xsl:import href="/usr/share/sgml/docbook/xsl-stylesheets-1.69.1-5/html/docbook.xsl" />


<xsl:output method="html"
            encoding="ISO-8859-1"
            indent="no"/>

<xsl:param name="callout.graphics">1</xsl:param>
<xsl:param name="admon.graphics">1</xsl:param>

<xsl:param name="generate.toc">
  book toc
  chapter nop
</xsl:param>
<xsl:param name="html.stylesheet">document_mvnforum.css</xsl:param>
<xsl:param name="tablecolumns.extensions">1</xsl:param>
<xsl:param name="toc.list.type">ol</xsl:param>

<xsl:param name="table.borders.with.css" select="1"></xsl:param>
<xsl:param name="table.cell.border.style" select="'solid'"></xsl:param>
<xsl:param name="table.cell.border.thickness" select="'0'"></xsl:param>
<xsl:param name="table.frame.border.thickness" select="'0'"></xsl:param>

<xsl:template name="user.footer.content">
   <div id="customfooter">
      mvnForum documentation created using docbook.
   </div>
</xsl:template>
<xsl:template name="user.header.content">
<div id="customheader">

<xsl:text disable-output-escaping="yes">&lt;</xsl:text>!--
 - $Header: /cvsroot/mvnforum/mvnforum/docbook/stylesheet/mvnforum.xsl,v 1.5 2007/06/14 18:44:08 minhnn Exp $
 - $Author: minhnn $
 - $Revision: 1.5 $
 - $Date: 2007/06/14 18:44:08 $
 -
 - ====================================================================
 -
 - Copyright (C) 2002-2006 by MyVietnam.net
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
 --<xsl:text disable-output-escaping="yes">&gt;</xsl:text>

</div>
</xsl:template>
</xsl:stylesheet> 
