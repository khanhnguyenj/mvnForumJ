/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/util/Phpbb2MvnforumConfig.java,v 1.9 2007/01/15 10:27:31 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.9 $
 * $Date: 2007/01/15 10:27:31 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
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
 * @author: Anh, Dong Thi Lan
 */
package org.mvnforum.util;

import java.io.*;
import java.util.Properties;

public class Phpbb2MvnforumConfig {
    
    public static String PHP_HOST       = "localhost";
    
    public static String PHP_DB         = "peerflix_phpbb";
    
    public static String PHP_USER       = "root";
    
    public static String PHP_PASS       = "";
    
    public static String MVN_HOST       = "localhost";
    
    public static String MVN_DB         = "mvnforum";
    
    public static String MVN_USER       = "root";
    
    public static String MVN_PASS       = "";
    
    public static String FILE_NAME      = "la_output.txt";
    
    public static int GENDER            = 0; 
    
    public static String EXPORT_XML     = "phpbb-export.xml";
    
    public static String DATABASE_CONFIG = "phpbb";
    
    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("db.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        PHP_HOST    = prop.getProperty("phphost");
        PHP_DB      = prop.getProperty("phpdb");
        PHP_USER    = prop.getProperty("phpuser");
        PHP_PASS    = prop.getProperty("phppass");

        MVN_HOST    = prop.getProperty("mvnhost");
        MVN_DB      = prop.getProperty("mvndb");
        MVN_USER    = prop.getProperty("mvnuser");
        MVN_PASS    = prop.getProperty("mvnpass");
        
        DATABASE_CONFIG = "phpbb";
        
        try {
            GENDER = Integer.parseInt(prop.getProperty("gender"));
            if (GENDER != 0 && GENDER != 1) {
                System.out.println("Error: Does not support GENDER = " + GENDER);
                System.out.println("Warning: Use default value for GENDER: 0");
                GENDER = 0;
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            GENDER = 0;
        }

        FILE_NAME = prop.getProperty("f");
        
        EXPORT_XML = prop.getProperty("exportXMLFile");
        if (EXPORT_XML.equals("")) {
            EXPORT_XML = "exportXML.xml";
        }
        
    }

}
