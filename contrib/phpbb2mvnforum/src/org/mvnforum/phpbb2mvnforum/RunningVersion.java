/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/RunningVersion.java,v 1.5 2007/12/18 04:58:45 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2007/12/18 04:58:45 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;

public class RunningVersion {

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
    public RunningVersion(String phphost, String phpdb, String phpuser, String phppass, String mvnhost, String mvndb,
            String mvnuser, String mvnpass, PrintStream ostream) throws Exception {

        if ((phphost == null) || (phpdb == null) || (phpuser == null) || (phppass == null)) {
            throw new Exception("php database details were not provided : phphost=" + phphost + " phpdb=" + phpdb
                    + "phpuser=" + phpuser + "phppass=" + phppass);
        }

        if (ostream == null) {
            throw new Exception("Outputstream null!");
        }

        String phpurl = "jdbc:mysql://" + phphost + "/" + phpdb;
        String mvnurl = "jdbc:mysql://" + mvnhost + "/" + mvndb;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception E) {
            System.err.println("Unable to load driver.");
            E.printStackTrace();
        }

        phpbb_c = DriverManager.getConnection(phpurl, phpuser, phppass);

        if ((mvnhost == null) || (mvndb == null) || (mvnuser == null) || (mvnpass == null)) {
            mvnforum_c = null;
        } else {
            mvnforum_c = DriverManager.getConnection(mvnurl, mvnuser, mvnpass);
        }

        sqlout = ostream;
    }

    /**
     * Performs Conversion of phpbb database to mvnforum database
     */
    public void convert() {
        try {

            System.out.println("Conversion started...");

            Migrator.migrateUsers(sqlout, 0);
            System.out.println("Users converted!");

            Migrator.migrateCategories(sqlout);
            System.out.println("Categories converted!");

            Migrator.migrateForums(sqlout);
            System.out.println("Forums converted!");

            Migrator.migratePosts(sqlout);
            System.out.println("Posts converted!");

            Migrator.migrateThreads(sqlout);
            System.out.println("Threads converted!");

            System.out.println("Conversion complete...");

        } catch (SQLException E) {
            System.out.println("Exception during conversion...");
            System.out.println("SQLException: " + E.getMessage());
            System.out.println("SQLState:     " + E.getSQLState());
            System.out.println("VendorError:  " + E.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            phpbb_c.close();
            if (mvnforum_c != null) {
                mvnforum_c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlout.close();
    }

    public static void main(String[] args) throws Exception {

        String phphost = "localhost";
        String phpdb = "peerflix_phpbb";
        String phpuser = "root";
        String phppass = "";

        boolean dboutput = false;
        String mvnhost = "localhost";
        String mvndb = "mvnforum";
        String mvnuser = "root";
        String mvnpass = "";

        String filename = "/home/haunt/Desktop/ele_out/out.sql";

        //    for (int i=0; i<args.length; i++)
        //    {
        //      if (args[i].equals("-phpdb"))
        //      {
        //        phpdb = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-phphost"))
        //      {
        //        phphost = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-phpuser"))
        //      {
        //        phpuser = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-phppass"))
        //      {
        //        phppass = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-mvnhost"))
        //      {
        //        dboutput = true;
        //        mvnhost = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-mvndb"))
        //      {
        //        dboutput = true;
        //        mvndb = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-mvnuser"))
        //      {
        //        dboutput = true;
        //        mvnuser = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-mvnpass"))
        //      {
        //        dboutput = true;
        //        mvnpass = args[i+1];
        //        i++;
        //        continue;
        //      }
        //      else if (args[i].equals("-f"))
        //      {
        //        filename = args[i+1];
        //        i++;
        //        continue; 
        //      }
        //    }
        //    
        //    if ((phpdb == null) || (phpuser == null) || (phppass == null))
        //    {
        //      usage();
        //      System.exit(-1);
        //    }
        //    else if (dboutput == true)
        //    {
        //      if ((mvndb == null) || (mvnuser == null) || (mvnpass == null))
        //      {
        //        usage();
        //        System.exit(-1);
        //      }
        //    }

        PrintStream ostream = System.out;

        if (filename != null) {
            File f = new File(filename);
            FileOutputStream fout = new FileOutputStream(f);
            ostream = new PrintStream(fout, true);
        }

        (new RunningVersion(phphost, phpdb, phpuser, phppass, mvnhost, mvndb, mvnuser, mvnpass, ostream)).convert();
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
        System.out.println("Specifying mvn* parameters are optional. If mvn* parameters are specified, the php database will be exported to the mvn database specified. "
                        + "-mvnhost parameter will default to \"localhost\" if not specified. ");
        System.out.println("-f filename is optional, if not specified, output will go to stdout. If specified, a file will be created and the output will go to the file.");
    }
}
