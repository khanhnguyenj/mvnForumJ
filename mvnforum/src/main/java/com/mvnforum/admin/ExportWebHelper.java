/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ExportWebHelper.java,v 1.19 2009/01/13 17:46:00 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.19 $
 * $Date: 2009/01/13 17:46:00 $
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
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.I18nUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;

/**
 * @author Igor Manic
 * @version $Revision: 1.19 $, $Date: 2009/01/13 17:46:00 $
 * <br/>
 * <code>ExportWebHelper</code> todo Igor: enter description
 *
 */
public final class ExportWebHelper {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ExportWebHelper.class);

    /** Cannot instantiate. */
    private ExportWebHelper() {
    }


// =================================================================
// ===================== MAIN PUBLIC METHODS =======================
// =================================================================
    public static void exportXml(String filename, HttpServletRequest request,
            int logonMemberID, String logonMemberName,
            Calendar backupTime, String backupIP)
        throws ExportException {

        log.debug("Making backup xml = \""+filename+"\".");
        String filepath = MVNForumConfig.getBackupDir() + File.separatorChar + filename;
        File resultFile = new File(filepath);
        XMLWriter xmlWriter = new XMLWriter("   "/*indentString*/, resultFile);
        try {
            createBackupXml(xmlWriter, backupTime, backupIP);
        } catch (ExportException e) {
            /* xmlWriter.close() will be called again in finally section, but,
             * nevermind, I'll ignore IOException then. Anyway, the most
             * important is that it is called in any case, and here, in case
             * of exception, I must first close xmlWriter, before trying
             * to delete resultFile.
             */
            try {
                xmlWriter.close();
            } catch (IOException ee) {
                log.error("I/O error while closing created XML. Ignoring...");
                //ignore this
            } finally {
                xmlWriter=null;
            }
            if (resultFile!=null) { resultFile.delete(); resultFile=null; }
            throw e; //rethrow; just wanted to ensure bad resultFile is deleted
        } finally {
            //see above section "catch ExportException"
            try {
                xmlWriter.close();
            } catch (IOException e) {
                log.error("I/O error while closing created XML. Ignoring...");
                //ignore this
            } finally {
                xmlWriter=null;
            }
        }
        /*return resultFile;*/ resultFile=null;
    }

    public static void exportZip(String filename, HttpServletRequest request,
            int logonMemberID, String logonMemberName,
            Calendar backupTime, String backupIP)
        throws ExportException {

        log.debug("Making backup zip = \""+filename+"\".");
        // First, make the main XML file, in "temp" directory.
        File mainXmlFile = null;

        Locale locale = I18nUtil.getLocaleInRequest(request);

        try {
            mainXmlFile = File.createTempFile("IMPORT", ".xml", new File(MVNForumConfig.getTempDir()));
            XMLWriter xmlWriter = new XMLWriter("   "/*indentString*/, mainXmlFile);
            try {
                createBackupXml(xmlWriter, backupTime, backupIP);
            } finally {
                xmlWriter.close(); 
                xmlWriter = null;
            }
        } catch (IOException e) {
            log.error("I/O error while creating XML.");
            if (mainXmlFile != null) { 
                mainXmlFile.delete(); 
                mainXmlFile = null; 
            }
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ExportException.io_error.create_mail_xml_file");
            throw new ExportException(localizedMessage);
            //throw new ExportException("I/O error while creating main XML file.", e);
        } catch (ExportException e) {
            if (mainXmlFile != null) { 
                mainXmlFile.delete(); 
                mainXmlFile = null; 
            }
            throw e; //rethrow; just wanted to ensure mainXmlFile is deleted
        }

        /* Second, create ZIP file in "backup" directory and add avatarsDir, attachsDir, mainXmlFile.
         * I am adding XML file at the end, since it might be better for import to process
         * XML after the other files are copied. It's not so important, but it's maybe
         * more convenient for the admin to see those file extraction prior to
         * importing XML, since when that XML processing starts, he will be
         * "bombed" with dozens of messages about database items...
         */
        String zipFilePath = MVNForumConfig.getBackupDir() + File.separatorChar + filename;
        String avatarsDir = request.getSession().getServletContext().getRealPath(MVNForumGlobal.UPLOADED_AVATAR_DIR);
        String attachsDir = MVNForumConfig.getAttachmentDir();

        ZipOutputStream out = null;
        try {
            //now create ZIP file
            out = new ZipOutputStream(new FileOutputStream(zipFilePath));
            //Select your choice of STORED (not compressed) or DEFLATED (compressed).
            out.setMethod(ZipOutputStream.DEFLATED);

            //add avatarsDir and attachsDir
            addDirectory(out, new File(avatarsDir), MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip);
            addDirectory(out, new File(attachsDir), MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip);
            //add mainXmlFile
            addFile(out, mainXmlFile, MVNForumConfig.BACKUP_FILE_MainXmlFileNameInZip);
        } catch (FileNotFoundException e) {
            log.error("Can't find a file that has been created.");
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ExportException.cannot_file_created");
            throw new ExportException(localizedMessage);
            //throw new ExportException("Can't find a file that has been created.", e);
        } catch (IOException e) {
            log.error("I/O error while making ZIP file.");
            try { 
                out.close(); 
            } catch (Exception ee) {
                // do nothing
            }
            out = null;
            try { 
                new File(zipFilePath).delete(); } 
            catch (Exception ee) {
                // do nothing
            }
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ExportException.io_error.make_zip_file");
            throw new ExportException(localizedMessage);
            //throw new ExportException("I/O error while making ZIP file.", e);
        } finally {
            if (out!=null) {
                try { 
                    out.flush(); 
                    out.close(); 
                } catch (IOException e) {
                    // ignore
                }
            }
            out = null;
            //delete mainXmlFile, so I don't get both XML and ZIP in the backup repository
            if (mainXmlFile != null) { 
                mainXmlFile.delete(); 
                mainXmlFile = null; 
            }
        }
        /*return new File(zipFilePath);*/
    }

    private static void addFile(ZipOutputStream out, File file, String relativePath)
        throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(file);

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(relativePath));
        //ZipEntry zipentry = new ZipEntry(rgstring[i]);
        //zipentry.setSize(file.length());
        //zipentry.setTime(file.lastModified());
        //out.putNextEntry(zipentry);

        //Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        // Complete the entry
        in.close(); 
        in = null;
        out.closeEntry();
    }

    private static void addDirectory(ZipOutputStream out, File dir, String relativePath)
        throws IOException {
        
        if (dir.isFile()) {
            log.error("Called addDirectory on a file \"" + dir.getAbsolutePath() + "\".");
            throw new IOException("I/O error while adding directory to ZIP.");
        }
        if (relativePath.length()>0) { //ignore starting directory
            if (!relativePath.endsWith("/")) {
                relativePath += "/";
            }
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(relativePath));
            out.closeEntry();
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    addFile(out, file, relativePath+file.getName());
                } else {
                    addDirectory(out, file, relativePath+file.getName()+"/");
                }
            }
        }
    }


// =================================================================
// ================= MAIN CREATING XML/ZIP METHOD ==================
// =================================================================
    private static void createBackupXml(XMLWriter xmlWriter,
                                        Calendar backupTime, String backupIP)
        throws ExportException {

        try {
            xmlWriter.startDocument(MVNForumConstant.dtdschemaDecl);
            String strExportDate = XMLUtil.sqlTimestampToStringDefNow(
                   new Timestamp(backupTime.getTime().getTime()));
            xmlWriter.startElement("mvnforum",
                      new String[]{"version", "1.0",
                                   "exportDate", strExportDate});

            MemberXML.exportMemberList(xmlWriter);
            GroupXML.exportGroupList(xmlWriter);
            CategoryXML.exportCategoryList(xmlWriter);
            RankXML.exportRankList(xmlWriter);

            xmlWriter.endElement("mvnforum");
            xmlWriter.endDocument();
        } catch (IOException e) {
            log.error("I/O error while trying to create backup XML file on server.", e);
            throw new ExportException("", e);
        } catch (ObjectNotFoundException e) {
            log.error("Object not found error while trying to createBackupXml()", e);
            throw new ExportException("Database object not found error while trying to create backup XML file on server.", e);
        } catch (DatabaseException e) {
            log.error("Database error while trying to createBackupXml()", e);
            throw new ExportException("Database error while trying to create backup XML file on server.", e);
        }
    }


// =================================================================
// ======================= UTILITY METHODS =========================
// =================================================================
    public static Collection execSqlQuery(String query)
    throws DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            int numCols = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] thisResult = new String[numCols];
                for (int i=0; i<numCols; i++) {
                    thisResult[i]=resultSet.getString(i+1);
                }
                retValue.add(thisResult);
            }
            return retValue;
        } catch (SQLException sqle) {
            log.error("Sql Execution Error!", sqle);
            throw new DatabaseException("Error executing SQL in ExportWebHelper.execSqlQuery.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }


}


