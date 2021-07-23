/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/util/DBUtils.java,v 1.7 2008/12/04 08:58:45 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.7 $
 * $Date: 2008/12/04 08:58:45 $
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
package org.mvnforum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DBUtils {
    
    private static Document doc = null;
    
    static {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();
            doc.appendChild(doc.createElement("MvnForum"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a connection from the connection pool. The returned connection
     * must be closed by calling DBUtils.closeConnection()
     * @return : a new connection from the pool if succeed
     * @throws SQLException : if cannot get a connection from the pool
     * @throws ClassNotFoundException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static Connection getPhpbbConnection()
        throws SQLException {

        String phpurl = "jdbc:mysql://" + Phpbb2MvnforumConfig.PHP_HOST + "/" + Phpbb2MvnforumConfig.PHP_DB;

        Connection conection = DriverManager.getConnection(phpurl, Phpbb2MvnforumConfig.PHP_USER, Phpbb2MvnforumConfig.PHP_PASS);

        return conection;
    }

    /**
     * Get a connection from the connection pool. The returned connection
     * must be closed by calling DBUtils.closeConnection()
     * @return : a new connection from the pool if succeed
     * @throws SQLException : if cannot get a connection from the pool
     * @throws ClassNotFoundException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static Connection getMvnConnection()
        throws SQLException {

        String phpurl = "jdbc:mysql://" + Phpbb2MvnforumConfig.MVN_HOST + "/" + Phpbb2MvnforumConfig.MVN_DB;

        Connection conection = DriverManager.getConnection(phpurl, Phpbb2MvnforumConfig.MVN_USER, Phpbb2MvnforumConfig.MVN_PASS);

        return conection;
    }

    /**
     * Use this method to close the ResultSet
     * @param rs : the resultset that needs to be closed
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Use this method to close the Statement
     * @param statement : the statement that needs to be closed
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Use this method to return the connection to the connection pool
     * Do not use this method to close connection that is not from
     * the connection pool
     * @param connection : the connection that needs to be returned to the pool
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Document getDomDocument() {
        return doc;
    }
    
    public static void writeXmlFile(Document doc, String filename) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
    
            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);
    
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    
    public static FileInputStream getInput() {
        try {
            FileInputStream file = new FileInputStream(Phpbb2MvnforumConfig.EXPORT_XML);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileOutputStream getOutput() {
        try {
            FileOutputStream file = new FileOutputStream(Phpbb2MvnforumConfig.EXPORT_XML);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
