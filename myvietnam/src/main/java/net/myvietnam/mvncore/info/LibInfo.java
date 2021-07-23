/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/info/LibInfo.java,v 1.16 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.16 $
 * $Date: 2009/01/01 18:30:12 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen    
 */
package net.myvietnam.mvncore.info;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LibInfo {

    private boolean supportJNDI                 = false;
    private boolean supportJavaxSql             = false;
    private boolean supportJAF                  = false;
    private boolean supportMail                 = false;
    
    private boolean supportBeanUtils            = false;
    private boolean supportCommonLogging        = false;
    private boolean supportCommonCodec          = false;
    private boolean supportCommonCollection     = false;
    private boolean supportCommonDigester       = false;
    private boolean supportCommonLang           = false;
    private boolean supportJakartaRegExp        = false;
    private boolean supportLucene               = false;

    private boolean supportDom4j                = false;

    private boolean supportMmMysqlDriver        = false;
    private boolean supportComMysqlDriver       = false;

    private boolean supportImageProcessing      = false;
    
    private boolean supportCommonIo             = false;
    private boolean supportPagerTaglib          = false;
    private boolean supportWhirlycache          = false;
    private boolean supportJDom                 = false;
    private boolean supportJzonic               = false;
    
    private boolean supportDNSJava              = false;
    private boolean supportAspirinSMTPServer    = false;
    

    public LibInfo() {
        try {
            Class.forName("javax.naming.Name");
            supportJNDI = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("javax.sql.DataSource");
            supportJavaxSql = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("javax.activation.DataSource");
            supportJAF = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("javax.mail.Message");
            supportMail = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.beanutils.MethodUtils");
            supportBeanUtils = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.logging.LogFactory");
            supportCommonLogging = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.codec.Decoder");
            supportCommonCodec = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.collections.ArrayStack");
            supportCommonCollection = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.digester.Digester");
            supportCommonDigester = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.commons.lang.SystemUtils");
            supportCommonLang = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.regexp.RE");
            supportJakartaRegExp = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.apache.lucene.index.IndexWriter");
            supportLucene = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.dom4j.Document");
            supportDom4j = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            supportMmMysqlDriver = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            supportComMysqlDriver = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            Class.forName("org.apache.commons.io.CopyUtils");
            supportCommonIo = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            Class.forName("com.jsptags.navigation.pager.PageTag");
            supportPagerTaglib = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            Class.forName("com.whirlycott.cache.Cache");
            supportWhirlycache = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            Class.forName("org.jdom.Attribute");
            supportJDom = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            Class.forName("org.jzonic.jlo.Channel");
            supportJzonic = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.xbill.DNS.TextParseException");
            supportDNSJava = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }

        try {
            Class.forName("org.masukomi.aspirin.core.QueManager");
            supportAspirinSMTPServer = true;
        } catch (ClassNotFoundException ex) {
            // ignore
        }
        
        try {
            final int IMAGE_SIZE = 10;
            BufferedImage bufferedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawLine(0, 0, IMAGE_SIZE, IMAGE_SIZE);
            g.dispose();// free resource

            supportImageProcessing = true;
        } catch (Throwable ex) {
            // ignore
        }

    }

    public boolean isSupportJAF() {
        return supportJAF;
    }

    public boolean isSupportJavaxSql() {
        return supportJavaxSql;
    }

    public boolean isSupportJNDI() {
        return supportJNDI;
    }

    public boolean isSupportMail() {
        return supportMail;
    }

    public boolean isSupportBeanUtils() {
        return supportBeanUtils;
    }

    public boolean isSupportCommonLogging() {
        return supportCommonLogging;
    }

    public boolean isSupportCommonCodec() {
        return supportCommonCodec;
    }

    public boolean isSupportCommonCollection() {
        return supportCommonCollection;
    }

    public boolean isSupportCommonDigester() {
        return supportCommonDigester;
    }

    public boolean isSupportCommonLang() {
        return supportCommonLang;
    }

    public boolean isSupportJakartaRegExp() {
        return supportJakartaRegExp;
    }

    public boolean isSupportLucene() {
        return supportLucene;
    }

    public boolean isSupportDom4j() {
        return supportDom4j;
    }

    public boolean isSupportMmMysqlDriver() {
        return supportMmMysqlDriver;
    }

    public boolean isSupportComMysqlDriver() {
        return supportComMysqlDriver;
    }

    public boolean isSupportImageProcessing() {
        return supportImageProcessing;
    }
    
    public boolean isSupportCommonIo() {
        return supportCommonIo;
    }
    
    public boolean isSupportPagerTaglib() {
        return supportPagerTaglib;
    }
    
    public boolean isSupportWhirlycache() {
        return supportWhirlycache;
    }
    
    public boolean isSupportJDom() {
        return supportJDom;
    }
    
    public boolean isSupportJzonic() {
        return supportJzonic;
    }
    
    public boolean isSupportDNSJava() {
        return supportDNSJava;
    }

    public boolean isSupportAspirinSMTPServer() {
        return supportAspirinSMTPServer;
    }
    
}
