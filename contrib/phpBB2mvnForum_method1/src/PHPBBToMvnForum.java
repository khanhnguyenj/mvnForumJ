/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum_method1/src/PHPBBToMvnForum.java,v 1.5 2007/01/15 10:27:31 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:27:31 $
 *
//==============================================================================
//  The JavaReference.com Software License, Version 1.0 
//  Copyright (c) 2002-2005  JavaReference.com. All rights reserved.
//
//  
//  Redistribution and use in source and binary forms, with or without 
//  modification, are permitted provided that the following conditions 
//  are met: 
//  
//  1. Redistributions of source code must retain the above copyright notice, 
//     this list of conditions and the following disclaimer. 
//  
//  2. Redistributions in binary form must reproduce the above copyright notice, 
//     this list of conditions and the following disclaimer in the documentation 
//     and/or other materials provided with the distribution. 
//     
//  3. The end-user documentation included with the redistribution, if any, must 
//     include the following acknowlegement: 
//     
//     "This product includes software developed by the Javareference.com 
//     (http://www.javareference.com/)." 
//     
//     Alternately, this acknowlegement may appear in the software itself, if and 
//     wherever such third-party acknowlegements normally appear. 
//     
//  4. The names "JavaReference" and "Javareference.com", must not be used to 
//     endorse or promote products derived from this software without prior written 
//     permission. For written permission, please contact webmaster@javareference.com. 
//     
//  5. Products derived from this software may not be called "Javareference" nor may 
//     "Javareference" appear in their names without prior written permission of  
//     Javareference.com. 
//     
//  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
//  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
//  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
//  JAVAREFERENCE.COM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
//  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
//  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
//  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
//  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
//  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
//  
//================================================================================ 
//  Software from this site consists of contributions made by various individuals 
//  on behalf of Javareference.com. For more information on Javareference.com, 
//  please see http://www.javareference.com 
//================================================================================

/* ====================================================================
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
 * @author: Luis Miguel Hernanz 
 * @author: Minh Nguyen  
 */

import java.io.*;
import java.sql.*;

/**
 * @author anandh
 */

public class PHPBBToMvnForum {
    
    Connection phpbb_c = null;
    Connection mvnforum_c = null;
    PrintStream sqlout = null;


    /**
     * @param phphost
     * @param phpdb
     * @param phpuser
     * @param phppass
     * @param mvnhost
     * @param mvndb
     * @param mvnuser
     * @param mvnpass
     * @param ostream
     */
    public PHPBBToMvnForum(String phphost, String phpdb, String phpuser, String phppass, 
                           String mvnhost, String mvndb, String mvnuser, String mvnpass, 
                           PrintStream ostream) throws Exception {

        if ((phphost == null) || (phpdb == null) || (phpuser == null) || (phppass == null)) {
            throw new Exception("php database details were not provided : phphost=" + phphost + " phpdb=" + phpdb +
                                "phpuser=" + phpuser + "phppass=" + phppass);
        }
        
        if (ostream == null) {
            throw new Exception("Outputstream is null.");
        }
        
        String phpurl = "jdbc:mysql://" + phphost + "/" + phpdb;
        String mvnurl = "jdbc:mysql://" + mvnhost + "/" + mvndb;
    
        try 
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (Exception E) 
        {
            System.err.println("Unable to load driver.");
            E.printStackTrace();
        }

       phpbb_c = DriverManager.getConnection(phpurl, phpuser, phppass);
       
       if ((mvnhost == null) || (mvndb == null) || (mvnuser == null) || (mvnpass == null))
       {
           mvnforum_c = null;
       } else {
           mvnforum_c = DriverManager.getConnection(mvnurl, mvnuser, mvnpass);
       }
       
       sqlout = ostream;
    }
    
    /**
     * Performs Conversion of phpbb database to mvnforum database
     * @throws FileNotFoundException 
     */
    public void convert() throws FileNotFoundException {
        try {

            System.out.println("Conversion started...");
            
            Migrator.migrateusers(phpbb_c, mvnforum_c, sqlout);
            System.out.println("Users converted!");
            
            /*
            Migrator.migratecategories(phpbb_c, mvnforum_c, sqlout);
            System.out.println("Categories converted!");
            
            Migrator.migrateforums(phpbb_c, mvnforum_c, sqlout);
            System.out.println("Forums converted!");
            
            Migrator.migrateposts(phpbb_c, mvnforum_c, sqlout);
            System.out.println("Posts converted!");
            
            Migrator.migratethreads(phpbb_c, mvnforum_c, sqlout);
            System.out.println("Threads converted!");
            */

            System.out.println("Conversion complete...");
        }
        catch (SQLException e) {
            System.out.println("Exception during conversion...");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
        }

        try 
        {
            phpbb_c.close();
            if (mvnforum_c != null)
            {
                mvnforum_c.close();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        sqlout.close();
    }

    public static void main(String[] args) throws Exception {
        
        String phphost = "localhost";
        String phpdb = "peerflix_phpbb";
        String phpuser = "root";
        String phppass = "";

        String mvnhost = "localhost";
        String mvndb = "PEERFLIX";
        String mvnuser = "root";
        String mvnpass = "";

        String filename = null;
        
        PrintStream ostream = System.out;
        
        if (filename != null)
        {
            File f = new File(filename);
            FileOutputStream fout = new FileOutputStream(f);
            ostream = new PrintStream(fout, true);
        }

        (new PHPBBToMvnForum(phphost, phpdb, phpuser, phppass, 
                            mvnhost, mvndb, mvnuser, mvnpass,
                            ostream)).convert();
    }

    /**
     * 
     */
    private static void usage() {
        
        System.out.println("JavaReference.com PHPBB to MVNforum converter:\nUsage :");
        System.out.println("java -jar phpbb2mvn.jar [-phphost [php_db_hostname]]  <-phpdb [php_db_name]> <-phpuser [php_db_user]> <-phppass [php_db_password]>");
        System.out.println("                        [-mvnhost [mvn_db_hostname]  [-mvndb [mvn_db_name]] [-mvnuser [mvn_db_user]] [-mvnpass [mvn_db_password]]");
        System.out.println("                        [-f [filename]]");
        System.out.println("All php* parameters (except -phphost) are mandatory. Default phphost is \"localhost\" ");
        System.out.println("Specifying mvn* parameters are optional. If mvn* parameters are specified, the php database will be exported to the mvn database specified. "+
                            "-mvnhost parameter will default to \"localhost\" if not specified. ");
        System.out.println("-f filename is optional, if not specified, output will go to stdout. If specified, a file will be created and the output will go to the file.");
    }
}

