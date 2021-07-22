/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/ImportMvnForum.java,v 1.22 2009/01/12 15:02:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.22 $
 * $Date: 2009/01/12 15:02:34 $
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
package com.mvnforum.admin.importexport.mvnforum;

import java.io.*;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;

import org.apache.commons.digester.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.admin.ImportWebHelper;
import com.mvnforum.admin.importexport.SetParentRule;

/**
 * @author Igor Manic
 * @version $Revision: 1.22 $, $Date: 2009/01/12 15:02:34 $
 * <br/>
 * <code>ImportMvnForum</code> class encapsulates processing
 * of MVN Forum's XML or ZIP backups, and imports all the data into MVN Forum.
 * For details see {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, int)}
 * and {@link #importZip(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, int)}
 * <br/>
 * This class cannot be instantiated.
 */
public final class ImportMvnForum extends ImportWebHelper {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ImportMvnForum.class);

    /** Cannot instantiate. */
    private ImportMvnForum() {
    }


// =================================================================
// ===================== MAIN PUBLIC METHODS =======================
// =================================================================
    /**
     * This method performs processing of MVN Forum's XML backup file <code>importFile</code>
     * and imports the data into the MVN Forum system. It clears the database
     * and files, does neccessary setup (including startup of message output),
     * and calls {@link #processXml(File, Calendar)} to do actual processing.<br/>
     *
     * @param importFile MVN Forum XML backup file to be imported.
     * @param request Current session's <code>HttpServletRequest</code> object.
     * @param response Current session's <code>HttpServletResponse</code> object.
     * @param logonMemberID MemberID of user who is logged in, and who initiated import process.
     * @param logonMemberName MemberName of user who is logged in, and who initiated import process.
     * @param importTime The moment when import process was started.
     * @param importIP From this IP address admin requested import.
     * @param clearIfError Should it clear/reset the database in case of error.
     * @param messageLevel What messages should be written to output during the process.
     *                     For details see {@link com.mvnforum.MVNForumConfig#MESSAGE_LEVEL_ALL_MESSAGES},
     *                     {@link com.mvnforum.MVNForumConfig#MESSAGE_LEVEL_IMPORTANT_MESSAGES} and
     *                     {@link com.mvnforum.MVNForumConfig#MESSAGE_LEVEL_ONLY_ERRORS}.
     *
     * @exception ImportException If there is an error during the process. See {@link net.myvietnam.mvncore.exception.ImportException}.
     */
    public static void importXml(File importFile,
           HttpServletRequest request, HttpServletResponse response,
           int logonMemberID, String logonMemberName,
           Calendar importTime, String importIP,
           boolean clearIfError, int messageLevel)
        throws ImportException {
        
        try {
            response.setContentType("text/html; charset=utf-8");
            setOutputHtmlWriter(response.getWriter());
            setMessageOutputLevel(messageLevel);
            startHtml(request);
            clearDatabase();
            clearFiles(request.getSession().getServletContext());
        } catch (DatabaseException e) {
            handleFatalError("Database error while clearing previous contents.",
                             e, clearIfError, request);
        } catch (IOException e) {
            handleFatalError("I/O error while clearing previous contents.",
                             e, clearIfError, request);
        }

        try {
            processXml(importFile, importTime);
            handleSuccess(request);
        } catch (ImportException e) {
            handleFatalError(e.getMessage(), e.getException(),
                             clearIfError, request);
        } catch (IOException e) {
            handleFatalError("I/O error while reading XML file.",
                             e, clearIfError, request);
        } catch (SAXException e) {
            if (e.getException()==null) {
                handleFatalError("Error while parsing uploaded XML file.",
                                 e, clearIfError, request);
            } else {
                handleFatalError("Error while parsing uploaded XML file. Detail: "+
                                 e.getException().getMessage(), e.getException(),
                                 clearIfError, request);
            }
        } finally {
            /* Don't delete this XML since this method was maybe started from the
             * command-line, which means this file is not temporary (uploaded)
             * //if ((importFile!=null) && (importFile.exists())) importFile.delete();
             * Anyway, if neccessary, this XML will be deleted in calling WebHandler
             */
        }
    }

    /**
     * This method performs processing of MVN Forum's ZIP backup file <code>importFile</code>
     * and imports the data into the MVN Forum system. It clears the database
     * and files, does neccessary setup (including startup of message output),
     * extracts the files from ZIP, and calls {@link #processXml(File, Calendar)}
     * to do actual processing of the main XML file (found in ZIP).<br/>
     *
     * @param importFile MVN Forum ZIP backup file to be imported.
     * @param request Current session's <code>HttpServletRequest</code> object.
     * @param response Current session's <code>HttpServletResponse</code> object.
     * @param logonMemberID MemberID of user who is logged in, and who initiated import process.
     * @param logonMemberName MemberName of user who is logged in, and who initiated import process.
     * @param importTime The moment when import process was started.
     * @param importIP From this IP address admin requested import.
     * @param clearIfError If <code>true</code>, the database will be cleared/reset
     *                     in case of error rises during the import. Default <code>Guest</code>
     *                     and <code>Admin</code> users will be created.
     * @param messageLevel What amount of messages (informational, important, error)
     *                     will be written to output.
     *
     * @exception ImportException If there is an error during the process. See {@link net.myvietnam.mvncore.exception.ImportException}.
     */
    public static void importZip(File importFile,
           HttpServletRequest request, HttpServletResponse response,
           int logonMemberID, String logonMemberName,
           Calendar importTime, String importIP,
           boolean clearIfError, int messageLevel)
        throws ImportException {
        
        File importXml = null;
        try {
            response.setContentType("text/html; charset=utf-8");
            setOutputHtmlWriter(response.getWriter());
            setMessageOutputLevel(messageLevel);
            startHtml(request);
            clearDatabase();
            clearFiles(request.getSession().getServletContext());
        } catch (DatabaseException e) {
            handleFatalError("Database error while clearing previous contents.",
                             e, clearIfError, request);
        } catch (IOException e) {
            handleFatalError("I/O error while clearing previous contents.",
                             e, clearIfError, request);
        }

       /* Now extract ZIP file into mvnForumHome, and give me back only XML file.
        * IMPORTANT: DON'T CALL clearFiles() AFTER THE EXTRACTION OF THE ZIP!
        * Otherwise, all extracted files will be deleted too
        */
       try {
           addImportantMessage("Extracting the ZIP file.");
           importXml=extractImportZip(importFile, request);
           if (importXml==null) {
               handleFatalError("Error while extracting the file: can't find IMPORT.xml in ZIP file.",
                                null/*error*/, clearIfError, request);
           }
       } catch (IOException e) {
           handleFatalError("I/O error while extracting the file.",
                            e, clearIfError, request);
       } finally {
           importFile.delete(); importFile=null;
       }

       //from this point on, use only importXml file (importFile is deleted)
       try {
           processXml(importXml, importTime);
           handleSuccess(request);
       } catch (ImportException e) {
           handleFatalError(e.getMessage(), e.getException(),
                            clearIfError, request);
       } catch (IOException e) {
           handleFatalError("I/O error while reading XML file.",
                            e, clearIfError, request);
       } catch (SAXException e) {
           if (e.getException()==null) {
               handleFatalError("Error while parsing uploaded XML file.",
                                e, clearIfError, request);
           } else {
               handleFatalError("Error while parsing uploaded XML file. Detail: "+
                                e.getException().getMessage(), e.getException(),
                                clearIfError, request);
           }
       } finally {
           //now delete temporary XML file (extracted from ZIP, and already imported to database)
           if ( (importFile != null) && (importFile.exists()) ) {
               importFile.delete();
           }
           if ((importXml!=null) && (importXml.exists())) {
               importXml.delete();
           }
       }
    }

    /**
     * This method should unpack the zip into mvnForumHome.
     * It should also return importXml file (which was also extracted)
     */
    private static File extractImportZip(File importZipFile, HttpServletRequest request)
    throws IOException {
        String avatarsDir = request.getSession().getServletContext().getRealPath(MVNForumGlobal.UPLOADED_AVATAR_DIR);
        String attachsDir = MVNForumConfig.getAttachmentDir();
        String mvnForumHomeDir = MVNForumConfig.getMVNForumHome();
        if (! new File(avatarsDir).mkdirs()) {
            /* Ignore, since this error will also happen when the directory
             * already exists, which is probably the case here, since we
             * didn't remove it in clearFiles() process.
             */
        }
        if (! new File(attachsDir).mkdirs()) {
            /* Ignore, since this error will also happen when the directory
             * already exists, which is probably the case here, since we
             * didn't remove it in clearFiles() process.
             */
        }
        if (!avatarsDir.endsWith(File.separator)) {
            avatarsDir += File.separator;
        }
        if (!attachsDir.endsWith(File.separator)) {
            attachsDir += File.separator;
        }
        if (!mvnForumHomeDir.endsWith(File.separator)) {
            mvnForumHomeDir += File.separator;
        }

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(importZipFile));
        File foundXmlFile = null;
        while (true) {
            // Get the next ZIP entry.  Break out of the loop if there are no more.
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                break;
            }
            // Read data from the ZIP entry.  The read() method will return -1 when there are no more data to read.
            String srcName=zipEntry.getName();
            String destPath = mvnForumHomeDir+srcName; //temp value; it'll be changed
            //for directory name comparisons, don't use String.startsWith() since it doesn't ignore char case (and we need that)
            if ((srcName.length()>=MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip.length()) && (srcName.substring(0, MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip.length()).equalsIgnoreCase(MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip))) {
                //addImportantMessage("Extracting \""+MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip+"\" from the backup ZIP file.");
                try {
                    String thisAvatar=srcName.substring(MVNForumConfig.BACKUP_FILE_AvatarsDirNameInZip.length(), srcName.length());
                    //now calculate destPath, but ignore avatarsDir itself, since it's already created
                    if (thisAvatar.length()<=0) {
                        destPath = null;
                    } else {
                        destPath = avatarsDir+thisAvatar.replace('/', File.separatorChar);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    //it's probably the "AVATARS/" entry itself, so thisAvatar is empty
                    destPath=null;
                }
            } else if ((srcName.length()>=MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip.length()) && (srcName.substring(0, MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip.length()).equalsIgnoreCase(MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip))) {
                //addImportantMessage("Extracting \""+MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip+"\" from the backup ZIP file.");
                try {
                    String thisAttach=srcName.substring(MVNForumConfig.BACKUP_FILE_AttachsDirNameInZip.length(), srcName.length());
                    //now calculate destPath, but ignore attachsDir itself, since it's already created
                    if (thisAttach.length()<=0) {
                        destPath = null;
                    } else {
                        destPath = attachsDir+thisAttach.replace('/', File.separatorChar);
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    //it's probably the "ATTACHMENTS/" entry itself, so thisAttach is empty
                    destPath = null;
                }
            } else {
                //it isn't neither avatar nor attachment, so don't process/replace path
                destPath = mvnForumHomeDir + srcName.replace('/', File.separatorChar);
            }
            if ((destPath!=null) && (destPath.length()>0)) {
                File destFile = new File(destPath);
                if (zipEntry.isDirectory()) {
                    addMessage("Extracting directory \"" +srcName +"\".");
                    destFile.mkdirs();
                } else {
                    addMessage("Extracting file \"" +srcName +"\", size=" +zipEntry.getSize()+".");
                    FileOutputStream outStream = new FileOutputStream(destFile);
                    byte[] buffer = new byte[1024];
                    int n;
                    while ((n = zipInputStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, n);
                    }
                    outStream.close(); 
                    outStream = null;
                    if (srcName.equalsIgnoreCase(MVNForumConfig.BACKUP_FILE_MainXmlFileNameInZip) || srcName.equalsIgnoreCase("/"+MVNForumConfig.BACKUP_FILE_MainXmlFileNameInZip)) {
                        foundXmlFile=destFile;
                    }
                }
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
        return foundXmlFile;
        //this method doesn't matter if IMPORT.xml doesn't exist - it will return null, and
        //the caller method should decide what to do then
    }


// =================================================================
// ================== MAIN PROCESSING XML METHOD ===================
// =================================================================
    /**
     * This method performs actual processing of MVN Forum's XML file <code>inputFile</code>
     * and imports the data into the MVN Forum system.<br/>
     * Don't use this method directly. Instead, you should use
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, int)}.
     *
     * @param inputFile MVN Forum XML file to be imported.
     * @param importTime The moment when import process was started.
     *
     * @exception IOException If there is an I/O error while processing XML file.
     * @exception SAXException If there is an error while parsing XML file.
     * @exception ImportException If there is some other error during the import.
     */
    private static void processXml(File inputFile, Calendar importTime)
        throws IOException, SAXException, ImportException {
        addImportantMessage("Starting importing database info...");

        //SAXParserFactory factory=SAXParserFactory.newInstance();
        Digester digester=new Digester();
        //should try new Digester(SAXParser), or Digester(XMLReader)
        digester.setValidating(true);
        digester.setNamespaceAware(true);
        //digester.setSchema("mvn.xsd");

        /* =================================================================
         * This is the main part of file - XML processing rules for Digester
         * =================================================================
         *
         * NOTE: Since SetTopRule is firing the desired method at the end of
         * the XML element matching the pattern, I had to implement my own
         * SetParentRule which does exactly the same thing (calls desired
         * child's method with a parent object as an argument). The difference
         * is that it is firing the "setParent" method at the beginning of the
         * corresponding XML element, thus leaving me the chance to, for example,
         * create messages as they arrive from the XML parser, not waiting the
         * end of the whole document (!!!), because I can't add a message, unless
         * I already added it's parent messages, thread, forum and category.
         *
         * NOTE: In SetPropertiesRule (see attachment processing below), I had to
         * define setAttachmentId() instead of setAttachmentID(), to avoid some
         * strange problems that Digester has (because it tries to automatically
         * convert all words to first letter capital, other small, and it
         * doesn't handle well the case with "attachmentID" property, that is,
         * it doesn't "see" setAttachmentID() method).
         * So, I defined attachmentId property and method setAttachmentId() is
         * then executed OK.
         */

        /* First, I'll create root object of class MvnForumXML.
         * It's constructor will create default contents of the database, including
         * admin member (with MemberID=1, MemberName="Admin", MemberPassword="admin").
         * Later, if I find admin in XML, I'll just rewrite this default's admin data,
         * except for the MemberName which has to remain "Admin" and can't be changed.
         */
        digester.addObjectCreate("mvnforum", MvnForumXML.class);
        digester.addSetProperties("mvnforum", "version", "mvnForumXmlVersion");
        digester.addSetProperties("mvnforum", "exportDate", "mvnForumExportDate");


        digester.addCallMethod("mvnforum/MemberList", "postProcessMemberList");
        digester.addObjectCreate("mvnforum/MemberList/Member", MvnForumMemberXML.class);
        digester.addSetProperties("mvnforum/MemberList/Member", "class", "memberClass");
        digester.addCallMethod("mvnforum/MemberList/Member", "addMember");
        digester.addCallMethod("mvnforum/MemberList/Member/MemberName", "setMemberName", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberPassword", "setMemberPassword", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberFirstEmail", "setMemberFirstEmail", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberEmail", "setMemberEmail", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberEmailVisible", "setMemberEmailVisible", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberNameVisible", "setMemberNameVisible", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberFirstIP", "setMemberFirstIP", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberLastIP", "setMemberLastIP", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberViewCount", "setMemberViewCount", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberPostCount", "setMemberPostCount", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCreationDate", "setMemberCreationDate", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberModifiedDate", "setMemberModifiedDate", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberExpireDate", "setMemberExpireDate", 0);
        //log.debug("DIGESTER:::: [ OK ]" + "mvnforum/MemberList/Member/MemberExpireDate");
        digester.addCallMethod("mvnforum/MemberList/Member/MemberLastLogon", "setMemberLastLogon", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberOption", "setMemberOption", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberStatus", "setMemberStatus", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberActivateCode", "setMemberActivateCode", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberTempPassword", "setMemberTempPassword", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberMessageCount", "setMemberMessageCount", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberMessageOption", "setMemberMessageOption", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberPostsPerPage", "setMemberPostsPerPage", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberWarnCount", "setMemberWarnCount", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberVoteCount", "setMemberVoteCount", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberVoteTotalStars", "setMemberVoteTotalStars", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberRewardPoints", "setMemberRewardPoints", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberTitle", "setMemberTitle", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberTimeZone", "setMemberTimeZone", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberSignature", "setMemberSignature", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberAvatar", "setMemberAvatar", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberSkin", "setMemberSkin", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberLanguage", "setMemberLanguage", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberFirstname", "setMemberFirstname", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberLastname", "setMemberLastname", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberGender", "setMemberGender", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberBirthday", "setMemberBirthday", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberAddress", "setMemberAddress", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCity", "setMemberCity", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberState", "setMemberState", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCountry", "setMemberCountry", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberPhone", "setMemberPhone", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberMobile", "setMemberMobile", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberFax", "setMemberFax", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCareer", "setMemberCareer", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberHomepage", "setMemberHomepage", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberYahoo", "setMemberYahoo", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberAol", "setMemberAol", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberIcq", "setMemberIcq", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberMsn", "setMemberMsn", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCoolLink1", "setMemberCoolLink1", 0);
        digester.addCallMethod("mvnforum/MemberList/Member/MemberCoolLink2", "setMemberCoolLink2", 0);

        digester.addCallMethod("mvnforum/MemberList/Member/GlobalPermissionList/GlobalPermission", "addMemberPermission", 0);

        digester.addCallMethod("mvnforum/MemberList/Member/MessageFolderList/MessageFolder", "addMessageFolder", 4);
        digester.addCallParam("mvnforum/MemberList/Member/MessageFolderList/MessageFolder/FolderName", 0);
        digester.addCallParam("mvnforum/MemberList/Member/MessageFolderList/MessageFolder/FolderOrder", 1);
        digester.addCallParam("mvnforum/MemberList/Member/MessageFolderList/MessageFolder/FolderCreationDate", 2);
        digester.addCallParam("mvnforum/MemberList/Member/MessageFolderList/MessageFolder/FolderModifiedDate", 3);

        digester.addCallMethod("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch", "addGlobalWatch", 6);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchType", 0);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchOption", 1);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchStatus", 2);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchCreationDate", 3);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchLastSentDate", 4);
        digester.addCallParam("mvnforum/MemberList/Member/GlobalWatchList/GlobalWatch/WatchEndDate", 5);


        digester.addCallMethod("mvnforum/GroupList", "postProcessGroupList");
        digester.addObjectCreate("mvnforum/GroupList/Group", MvnForumGroupXML.class);
        digester.addSetProperties("mvnforum/GroupList/Group", "class", "groupClass");
        digester.addCallMethod("mvnforum/GroupList/Group", "addGroup");
        digester.addCallMethod("mvnforum/GroupList/Group/GroupOwnerName", "setGroupOwnerName", 0);
        digester.addCallMethod("mvnforum/GroupList/Group/GroupName", "setGroupName", 0);
        digester.addCallMethod("mvnforum/GroupList/Group/GroupDesc", "setGroupDesc", 0);
        digester.addCallMethod("mvnforum/GroupList/Group/GroupOption", "setGroupOption", 0);
        digester.addCallMethod("mvnforum/GroupList/Group/GroupCreationDate", "setGroupCreationDate", 0);
        digester.addCallMethod("mvnforum/GroupList/Group/GroupModifiedDate", "setGroupModifiedDate", 0);

        digester.addCallMethod("mvnforum/GroupList/Group/GlobalPermissionList/GlobalPermission", "addGroupPermission", 0);

        digester.addCallMethod("mvnforum/GroupList/Group/GroupMemberList/GroupMember", "addGroupMember", 4);
        digester.addCallParam("mvnforum/GroupList/Group/GroupMemberList/GroupMember/MemberName", 0);
        digester.addCallParam("mvnforum/GroupList/Group/GroupMemberList/GroupMember/Privilege", 1);
        digester.addCallParam("mvnforum/GroupList/Group/GroupMemberList/GroupMember/CreationDate", 2);
        digester.addCallParam("mvnforum/GroupList/Group/GroupMemberList/GroupMember/ModifiedDate", 3);


        //digester.addCallMethod("mvnforum/GuestGlobalPermissionList/GlobalPermission", "addGuestGlobalPermission", 0);
        //digester.addCallMethod("mvnforum/RegisteredMembersGlobalPermissionList/GlobalPermission", "addRegisteredMembersGlobalPermission", 0);


        digester.addObjectCreate("*/Category", MvnForumCategoryXML.class);
        //digester.addSetTop("*/Category", "setParentCategoryIfHave");
        SetParentRule categoryParentRule = new SetParentRule("setParentCategoryIfHave");
        digester.addRule("*/Category", categoryParentRule);
        digester.addCallMethod("*/Category", "addCategory");
        digester.addCallMethod("*/Category/CategoryName", "setCategoryName", 0);
        digester.addCallMethod("*/Category/CategoryDesc", "setCategoryDesc", 0);
        digester.addCallMethod("*/Category/CategoryCreationDate", "setCategoryCreationDate", 0);
        digester.addCallMethod("*/Category/CategoryModifiedDate", "setCategoryModifiedDate", 0);
        digester.addCallMethod("*/Category/CategoryOrder", "setCategoryOrder", 0);
        digester.addCallMethod("*/Category/CategoryOption", "setCategoryOption", 0);
        digester.addCallMethod("*/Category/CategoryStatus", "setCategoryStatus", 0);

        digester.addCallMethod("*/Category/CategoryWatchList/CategoryWatch", "addCategoryWatch", 7);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/MemberName", 0);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchType", 1);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchOption", 2);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchStatus", 3);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchCreationDate", 4);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchLastSentDate", 5);
        digester.addCallParam("*/Category/CategoryWatchList/CategoryWatch/WatchEndDate", 6);


        digester.addObjectCreate("*/Forum", MvnForumForumXML.class);
        digester.addCallMethod("*/Forum", "addForum");
        //digester.addSetTop("*/Forum", "setParentCategory");
        SetParentRule forumParentRule = new SetParentRule("setParentCategory");
        digester.addRule("*/Forum", forumParentRule);
        digester.addCallMethod("*/Forum/LastPostMemberName", "setForumLastPostMemberName", 0);
        digester.addCallMethod("*/Forum/ForumName", "setForumName", 0);
        digester.addCallMethod("*/Forum/ForumDesc", "setForumDesc", 0);
        digester.addCallMethod("*/Forum/ForumCreationDate", "setForumCreationDate", 0);
        digester.addCallMethod("*/Forum/ForumModifiedDate", "setForumModifiedDate", 0);
        digester.addCallMethod("*/Forum/ForumLastPostDate", "setForumLastPostDate", 0);
        digester.addCallMethod("*/Forum/ForumOrder", "setForumOrder", 0);
        digester.addCallMethod("*/Forum/ForumType", "setForumType", 0);
        digester.addCallMethod("*/Forum/ForumFormatOption", "setForumFormatOption", 0);
        digester.addCallMethod("*/Forum/ForumOption", "setForumOption", 0);
        digester.addCallMethod("*/Forum/ForumStatus", "setForumStatus", 0);
        digester.addCallMethod("*/Forum/ForumModerationMode", "setForumModerationMode", 0);
        digester.addCallMethod("*/Forum/ForumPassword", "setForumPassword", 0);
        digester.addCallMethod("*/Forum/ForumThreadCount", "setForumThreadCount", 0);
        digester.addCallMethod("*/Forum/ForumPostCount", "setForumPostCount", 0);

        digester.addCallMethod("*/Forum/MemberForumPermissionList/MemberForumPermission", "addMemberForumPermission", 2);
        digester.addCallParam("*/Forum/MemberForumPermissionList/MemberForumPermission/MemberName", 0);
        digester.addCallParam("*/Forum/MemberForumPermissionList/MemberForumPermission/ForumPermission", 1);

        digester.addCallMethod("*/Forum/GroupForumPermissionList/GroupForumPermission", "addGroupForumPermission", 2);
        digester.addCallParam("*/Forum/GroupForumPermissionList/GroupForumPermission/GroupName", 0);
        digester.addCallParam("*/Forum/GroupForumPermissionList/GroupForumPermission/ForumPermission", 1);

        //digester.addCallMethod("*/Forum/GuestForumPermissionList/ForumPermission", "addGuestForumPermission", 0);
        //digester.addCallMethod("*/Forum/RegisteredMembersForumPermissionList/ForumPermission", "addRegisteredMembersForumPermission", 0);

        digester.addCallMethod("*/Forum/ForumWatchList/ForumWatch", "addForumWatch", 7);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/MemberName", 0);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchType", 1);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchOption", 2);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchStatus", 3);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchCreationDate", 4);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchLastSentDate", 5);
        digester.addCallParam("*/Forum/ForumWatchList/ForumWatch/WatchEndDate", 6);


        digester.addObjectCreate("*/Thread", MvnForumThreadXML.class);
        //digester.addSetTop("*/Thread", "setParentForum");
        SetParentRule threadParentRule = new SetParentRule("setParentForum");
        digester.addRule("*/Thread", threadParentRule);
        digester.addCallMethod("*/Thread", "addThread");
        digester.addCallMethod("*/Thread/MemberName", "setThreadMemberName", 0);
        digester.addCallMethod("*/Thread/LastPostMemberName", "setThreadLastPostMemberName", 0);
        digester.addCallMethod("*/Thread/ThreadTopic", "setThreadTopic", 0);
        digester.addCallMethod("*/Thread/ThreadBody", "setThreadBody", 0);
        digester.addCallMethod("*/Thread/ThreadVoteCount", "setThreadVoteCount", 0);
        digester.addCallMethod("*/Thread/ThreadVoteTotalStars", "setThreadVoteTotalStars", 0);
        digester.addCallMethod("*/Thread/ThreadCreationDate", "setThreadCreationDate", 0);
        digester.addCallMethod("*/Thread/ThreadLastPostDate", "setThreadLastPostDate", 0);
        digester.addCallMethod("*/Thread/ThreadType", "setThreadType", 0);
        digester.addCallMethod("*/Thread/ThreadOption", "setThreadOption", 0);
        digester.addCallMethod("*/Thread/ThreadStatus", "setThreadStatus", 0);
        digester.addCallMethod("*/Thread/ThreadHasPoll", "setThreadHasPoll", 0);
        digester.addCallMethod("*/Thread/ThreadViewCount", "setThreadViewCount", 0);
        digester.addCallMethod("*/Thread/ThreadReplyCount", "setThreadReplyCount", 0);
        digester.addCallMethod("*/Thread/ThreadIcon", "setThreadIcon", 0);
        digester.addCallMethod("*/Thread/ThreadDuration", "setThreadDuration", 0);

        digester.addCallMethod("*/Thread/ThreadWatchList/ThreadWatch", "addThreadWatch", 7);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/MemberName", 0);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchType", 1);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchOption", 2);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchStatus", 3);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchCreationDate", 4);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchLastSentDate", 5);
        digester.addCallParam("*/Thread/ThreadWatchList/ThreadWatch/WatchEndDate", 6);

        digester.addCallMethod("*/Thread/FavoriteThreadList/FavoriteThread", "addFavoriteThread", 5);
        digester.addCallParam("*/Thread/FavoriteThreadList/FavoriteThread/MemberName", 0);
        digester.addCallParam("*/Thread/FavoriteThreadList/FavoriteThread/FavoriteCreationDate", 1);
        digester.addCallParam("*/Thread/FavoriteThreadList/FavoriteThread/FavoriteType", 2);
        digester.addCallParam("*/Thread/FavoriteThreadList/FavoriteThread/FavoriteOption", 3);
        digester.addCallParam("*/Thread/FavoriteThreadList/FavoriteThread/FavoriteStatus", 4);


        digester.addObjectCreate("*/Post", MvnForumPostXML.class);
        //digester.addSetTop("*/Post", "setParentThreadOrPost");
        SetParentRule postParentRule = new SetParentRule("setParentThreadOrPost");
        digester.addRule("*/Post", postParentRule);
        digester.addCallMethod("*/Post", "addPost");
        digester.addCallMethod("*/Post/MemberName", "setPostMemberName", 0);
        digester.addCallMethod("*/Post/LastEditMemberName", "setPostLastEditMemberName", 0);
        digester.addCallMethod("*/Post/PostTopic", "setPostTopic", 0);
        digester.addCallMethod("*/Post/PostBody", "setPostBody", 0);
        digester.addCallMethod("*/Post/PostCreationDate", "setPostCreationDate", 0);
        digester.addCallMethod("*/Post/PostLastEditDate", "setPostLastEditDate", 0);
        digester.addCallMethod("*/Post/PostCreationIP", "setPostCreationIP", 0);
        digester.addCallMethod("*/Post/PostLastEditIP", "setPostLastEditIP", 0);
        digester.addCallMethod("*/Post/PostEditCount", "setPostEditCount", 0);
        digester.addCallMethod("*/Post/PostFormatOption", "setPostFormatOption", 0);
        digester.addCallMethod("*/Post/PostOption", "setPostOption", 0);
        digester.addCallMethod("*/Post/PostStatus", "setPostStatus", 0);
        digester.addCallMethod("*/Post/PostIcon", "setPostIcon", 0);
        digester.addCallMethod("*/Post/PostAttachCount", "setPostAttachCount", 0);


        digester.addObjectCreate("*/Attachment", MvnForumAttachmentXML.class);
        digester.addSetProperties("*/Attachment", "id", "attachmentId");
        //digester.addSetTop("*/Attachment", "setParentPost");
        SetParentRule attachmentParentRule = new SetParentRule("setParentPost");
        digester.addRule("*/Attachment", attachmentParentRule);
        digester.addCallMethod("*/Attachment", "addAttachment");
        digester.addCallMethod("*/Attachment/MemberName", "setAttachMemberName", 0);
        digester.addCallMethod("*/Attachment/AttachFilename", "setAttachFilename", 0);
        digester.addCallMethod("*/Attachment/AttachFileSize", "setAttachFileSize", 0);
        digester.addCallMethod("*/Attachment/AttachMimeType", "setAttachMimeType", 0);
        digester.addCallMethod("*/Attachment/AttachDesc", "setAttachDesc", 0);
        digester.addCallMethod("*/Attachment/AttachCreationIP", "setAttachCreationIP", 0);
        digester.addCallMethod("*/Attachment/AttachCreationDate", "setAttachCreationDate", 0);
        digester.addCallMethod("*/Attachment/AttachModifiedDate", "setAttachModifiedDate", 0);
        digester.addCallMethod("*/Attachment/AttachDownloadCount", "setAttachDownloadCount", 0);
        digester.addCallMethod("*/Attachment/AttachOption", "setAttachOption", 0);
        digester.addCallMethod("*/Attachment/AttachStatus", "setAttachStatus", 0);


        /* At this point, on top of stack we have only the root MvnForumXML object */


        digester.addCallMethod("mvnforum/RankList/Rank", "addRank", 6);
        digester.addCallParam("mvnforum/RankList/Rank/RankMinPosts", 0);
        digester.addCallParam("mvnforum/RankList/Rank/RankLevel", 1);
        digester.addCallParam("mvnforum/RankList/Rank/RankTitle", 2);
        digester.addCallParam("mvnforum/RankList/Rank/RankImage", 3);
        digester.addCallParam("mvnforum/RankList/Rank/RankType", 4);
        digester.addCallParam("mvnforum/RankList/Rank/RankOption", 5);


        /* ==================================================================
         * This was the main part of file - XML processing rules for Digester
         * ==================================================================
         */

        digester.parse(inputFile);

        try {
            MvnForumXML.finishImport();
        } catch (CreateException e) {
            throw new ImportException(e.getMessage(), e); //just rethrow
        } catch (DuplicateKeyException e) {
            throw new ImportException(e.getMessage(), e); //just rethrow
        } catch (ObjectNotFoundException e) {
            throw new ImportException(e.getMessage(), e); //just rethrow
        } catch (DatabaseException e) {
            throw new ImportException(e.getMessage(), e); //just rethrow
        } catch (ForeignKeyNotFoundException e) {
            throw new ImportException(e.getMessage(), e); //just rethrow
        }
    }

    /**
     * Adds <code>message</code> to the output stream that was setup in
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, int)}.
     * <br/>This method was made public to be available to MVN Forum XML processing classes.
     *
     * @param message Message to be written to output.
     * @see com.mvnforum.admin.ImportWebHelper#addMessage(java.lang.String)
     *
     */
    public static void addMessage(String message) {
        ImportWebHelper.addMessage(message);
    }

    /**
     * Adds important (bold) <code>message</code> to the output stream that was setup in
     * {@link #importXml(File, HttpServletRequest, HttpServletResponse, int, String, Calendar, String, boolean, int)}.
     * <br/>This method was made public to be available to MVN Forum XML processing classes.
     *
     * @param message Message to be written to output.
     * @see com.mvnforum.admin.ImportWebHelper#addImportantMessage(java.lang.String)
     *
     */
    public static void addImportantMessage(String message) {
        ImportWebHelper.addImportantMessage(message);
    }

    /**
     * Ensure that default entries in MVN Forum (admin and virtual guest members, rank titles,
     * <code>Registered Members</code> group, ...) are created.<br/>
     * This method was made public to be available to MVN Forum XML processing classes.<br/>
     * Since these defaults will be imported from XML (they are supposed to be
     * in the XML), this method won't do anything. At the end of import, defaults
     * will be checked and created in case XML didn't contain them.
     *
     * @see com.mvnforum.admin.ImportWebHelper#addMessage(java.lang.String)
     *
     */
    public static void createDefaultContents() {
        //don't create any database entries now
        /* This method overrides ImportWebHelper.createDefaultContents().
         * It's used in MVNForumXML constructor.
         * But, in case of error, ImportWebHandler.finalErrorHandling will be
         * called, and it uses ImportWebHelper.createDefaultContents() version
         * of this method (so, in that case, all required default contents
         * will be created).
         */
    }

    private static void handleSuccess(HttpServletRequest request) {
        addSuccessMessage(request);
        endHtml();
    }

    //it's possible I called this method with e==null
    private static void handleFatalError(String message, Exception e,
             boolean clearIfError, HttpServletRequest request)
        throws ImportException {

        if (e == null) {
            log.error(message);
        } else {
            log.error(message, e);
        }

        if ((e == null) || (e.getMessage() == null)) {
            addErrorMessage(message);
        } else {
            addErrorMessage(message + "<br/>Cause: " + e.getMessage());
        }

        //try to clear the database and rollback to valid empty state with "admin" member
        ImportWebHelper.addFinalErrorHandling(request, clearIfError);
        endHtml();
        if (e == null) {
            throw new ImportException(message);
        } else {
            throw new ImportException(message, e);
        }
    }
}
