/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ImportWebHandler.java,v 1.29 2009/09/21 08:17:11 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.29 $
 * $Date: 2009/09/21 08:17:11 $
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
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.MVNCoreGlobal;
import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.FileUploadParserService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.FileUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.admin.importexport.jive.ImportJive;
import com.mvnforum.admin.importexport.mvnforum.ImportMvnForum;
import com.mvnforum.auth.*;
import com.mvnforum.user.UserModuleConfig;

/**
 * @author Igor Manic
 * @version $Revision: 1.29 $, $Date: 2009/09/21 08:17:11 $
 * <br/>
 * <code>ImportWebHandler</code> class implements methods that process HTTP
 * requests for import. Data could be imported from MVN Forum XML file conforming
 * <a href="http://www.mvnforum.com/mvn.dtd">http://www.mvnforum.com/mvn.dtd</a>,
 * or from MVN Forum backup ZIP file. Data can also be migrated from other sources
 * (for now, it's only available for <code>Jive Forums</code>).
 *
 */
public final class ImportWebHandler {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ImportWebHandler.class);

    private static PrintWriter outputHtml = null;

    /** Cannot instantiate. */
    private ImportWebHandler() {
    }

    /**
     * All output messages (status, informational and error) will be written
     * to <code>outHtml</code>.
     *
     * @param outHtml <code>PrintWriter</code> to write all output to. If it is
     *                <code>null</code>, there will be no output messages.
     */
    public static void setOutputHtmlWriter(PrintWriter outHtml) {
        ImportWebHandler.outputHtml = outHtml;
    }

    public static Vector getBackupFilesOnServer() {
        Vector result = new Vector();
        File dir = new File(MVNForumConfig.getBackupDir());
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    result.add(DisableHtmlTagFilter.filter(file.getName()));
                } //else ignore subdirs
            }
        }
        return result;
    }

    /**
     * This is the main import request dispatcher. It parses request
     * parameters, and decides what is the type of file used and what procedure
     * to call (restore from mvnForum XML, or mvnForum ZIP, or migrate from Jive XML, ...).
     * It also extracts additional request parameters, if they exist. Then it calls
     * the desired procedure and gives it all parameters it extracted from the request.<br/>
     * //todo Igor: add @see tags for all importXml/Zip methods
     *
     * @param request <code>HttpServletRequest</code> object of the request.
     * @param response <code>HttpServletResponse</code> object of the request.
     *
     * @throws ImportException
     * @throws AuthenticationException
     * @throws DatabaseException
     * @throws UnsupportedEncodingException 
     *
     */
    public static void importXmlZip(HttpServletRequest request, HttpServletResponse response)
        throws ImportException, AuthenticationException, DatabaseException, UnsupportedEncodingException {

        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        int sizeMax = MVNForumConfig.getMaxImportSize();
        int sizeThreshold = 100000;// max memory used = 100K
        String tempDir = MVNForumConfig.getTempDir();
        List fileItems;
        try {
            GenericRequest genericRequest = new GenericRequestServletImpl(request);
            FileUploadParserService fileUploadParserService = MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();
            fileItems = fileUploadParserService.parseRequest(genericRequest, sizeMax, sizeThreshold, tempDir, "UTF-8");
        } catch (FileUploadException ex) {
            log.error("Cannot upload", ex);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.cannot_upload", new Object[] {ex.getMessage()});
            throw new ImportException(localizedMessage);
            //throw new ImportException("Cannot upload file. Detailed reason: " + ex.getMessage());
        }

        // values that must get from the form
        String serverImportFilename   = null; //if importing from the server backup directory
        String clientImportFilename   = null; //if uploading import file
        int clientImportFileSize      = 0;    //if uploading import file
        FileItem clientImportFileItem = null; //if uploading import file

        int importType = MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML; //default is MVN Forum XML
        boolean clearIfError = true; //default is to reset database in case of error
        int messageLevel = MVNForumConfig.MESSAGE_LEVEL_ALL_MESSAGES;
        Vector otherFieldValues = new Vector();

        int      logonMemberID     = onlineUser.getMemberID();
        String   logonMemberName   = onlineUser.getMemberName();
        Calendar importTime        = Calendar.getInstance();
        String   importIP          = request.getRemoteAddr();
        File     importFile        = null;
        boolean  deleteAfterImport = false;
        String receivedToken = null;

        try {
            for (int i = 0; i < fileItems.size(); i++ ) {
                FileItem currentFileItem = (FileItem)fileItems.get(i);
                //content-type: currentFileItem.getContentType();
                String fieldName = currentFileItem.getFieldName();
                boolean isFormField = currentFileItem.isFormField();

                if ( (fieldName.equals("ServerImportFile")) && (isFormField) ) {
                    serverImportFilename = EnableHtmlTagFilter.filter(currentFileItem.getString());
                    log.debug("serverImportFilename = " + serverImportFilename);
                    currentFileItem.delete(); currentFileItem=null;

                } else if (fieldName.equals("ClientImportFile")) {
                    if (isFormField) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.cannot_process_uploaded_import_file_with_a_form_field");
                        throw new ImportException(localizedMessage);
                        //throw new ImportException("Cannot process uploaded import file with a form field.");
                    }
                    clientImportFileSize = (int)currentFileItem.getSize();
                    String fullFilePath = currentFileItem.getName();
                    clientImportFilename = FileUtil.getFileName(fullFilePath);
                    log.debug("clientImportFilename = " + clientImportFilename);
                    // now save to clientImportFileItem
                    clientImportFileItem = currentFileItem;

                } else if ( (fieldName.equals("ImportType")) && (isFormField) ) {
                    try {
                        importType = Integer.parseInt(currentFileItem.getString());
                    } catch (NumberFormatException e) {
                        importType=MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML; //default
                    }
                    currentFileItem.delete();
                    currentFileItem = null;
                } else if ( (fieldName.equals("ClearIfError")) && (isFormField) ) {
                    int clearInt = 1;
                    try {
                        clearInt = Integer.parseInt(currentFileItem.getString());
                    } catch (NumberFormatException e) {
                        clearInt = 1; //default
                    }
                    if (clearInt == 0) {
                        clearIfError = false;
                    } else {
                        clearIfError = true;
                    }
                    currentFileItem.delete(); 
                    currentFileItem = null;
                } else if ( (fieldName.equals("MessageLevel")) && (isFormField) ) {
                    try {
                        messageLevel=Integer.parseInt(currentFileItem.getString());
                    } catch (NumberFormatException e) {
                        messageLevel=MVNForumConfig.MESSAGE_LEVEL_ALL_MESSAGES; //default
                    }
                    currentFileItem.delete(); 
                    currentFileItem = null;
                    
                } else if (fieldName.startsWith(MVNCoreGlobal.MVNCORE_SECURITY_TOKEN)) {
                    receivedToken = currentFileItem.getString("utf-8");
                    log.debug("receivedToken = " + receivedToken);
                    
                } else { //other field values
                    if (isFormField) {
                        otherFieldValues.add(fieldName);
                        otherFieldValues.add(currentFileItem.getString());
                    }
                    currentFileItem.delete(); 
                    currentFileItem = null;
                }
            } //for (int i = 0; i < fileItems.size(); i++ )

            SecurityUtil.checkSecurityTokenMethod(request, receivedToken);
            
            if ( (serverImportFilename != null) && (!serverImportFilename.equals("")) ) {
                //import from server backup repository
                String filepath = MVNForumConfig.getBackupDir() + File.separatorChar + serverImportFilename;
                importFile = new File(filepath);
                deleteAfterImport = false;

            } else {
                //upload client import file
                deleteAfterImport = true; //delete uploaded file
                if ((clientImportFilename == null) || (clientImportFilename.equals(""))) {
                    log.error("Cannot import. Please choose either a file on server, or upload a file.");
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.cannot_import.choose_a_file_on_server_or_upload_a_file");
                    throw new ImportException(localizedMessage);
                    //throw new ImportException("Cannot import. Please choose either a file on server, or upload a file.");
                } else {
                    log.debug("ImportWebHandler : process upload with temp dir = " + MVNForumConfig.getTempDir());
                    if (clientImportFileSize == 0) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.cannot_process_import.file_size_is_zero");
                        throw new ImportException(localizedMessage);
                        //throw new ImportException("Cannot process an import file with size = 0. Please check the file size or check if your file is missing.");
                    }
                }

                try {
                    String filepath = MVNForumConfig.getTempDir() + File.separatorChar +
                               "import-" +
                               ((importType==MVNForumConfig.IMPORTEXPORT_TYPE_JIVE_XML)?"jive-":"") +
                               importTime.get(Calendar.YEAR) + "-" +
                               importTime.get(Calendar.MONTH) + "-" +
                               importTime.get(Calendar.DAY_OF_MONTH) + "-" +
                               importTime.get(Calendar.HOUR_OF_DAY) + "-" +
                               importTime.get(Calendar.MINUTE) + "-" +
                               importTime.get(Calendar.SECOND) +
                               ((importType==MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP)?".zip":".xml");
                    log.debug("Client import file to save to file system = " + filepath);
                    clientImportFileItem.write(new File(filepath));
                    importFile = new File(filepath);
                    clientImportFileItem.delete();
                    clientImportFileItem = null;
                } catch (Exception ex) {
                    log.error("Cannot save the import file.", ex);
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.cannot_import.cannot_save_import_file");
                    throw new ImportException(localizedMessage);
                    //throw new ImportException("Cannot save the import file to the file system.", ex);
                }
            }

        } finally {
            for (int i = 0; i < fileItems.size(); i++ ) {
                try { 
                    ((FileItem)fileItems.get(i)).delete();
                } catch (Exception e) {
                    // ignore
                }
            }
            fileItems.clear(); 
            fileItems = null;
        }

        if (!importFile.exists()) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.cannot_find_import_file");
            throw new ImportException(localizedMessage);
            //throw new ImportException("Can't find import file.");
        }
        else if (!importFile.isFile()) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.import_file_is_not_a_file");
            throw new ImportException(localizedMessage);
            //throw new ImportException("Import \"file\" is actually not a file (probably a directory).");
        }
        else if (!importFile.canRead()) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.dont_have_read_permission");
            throw new ImportException(localizedMessage);
            //throw new ImportException("Don't have a permission to read an import file from server.");
        }

        try {
            switch (importType) {
                case MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML:
                    ImportMvnForum.importXml(importFile,
                                            request, response,
                                            logonMemberID, logonMemberName,
                                            importTime, importIP,
                                            clearIfError, messageLevel);
                    break;
                case MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP:
                    ImportMvnForum.importZip(importFile,
                                            request, response,
                                            logonMemberID, logonMemberName,
                                            importTime, importIP,
                                            clearIfError, messageLevel);
                    break;
                case MVNForumConfig.IMPORTEXPORT_TYPE_JIVE_XML:
                    ImportJive.importXml(importFile,
                                     request, response,
                                     logonMemberID, logonMemberName,
                                     importTime, importIP,
                                     clearIfError, otherFieldValues, messageLevel);
                    break;

                default:
                    log.error("importXmlZip: invalid importType = " + importType);
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ImportException.invalid_import_type_specified");
                    throw new ImportException(localizedMessage);
                    //throw new ImportException("Invalid import type specified.");
            }
        } catch (ImportException e) {
           /* In case of this exception, I already printed error message.
            * Now I should NOT allow this exception to propagate higher, since
            * it will then initiate errorpage, which I don't want, and can't
            * allow because already committed output.
            * So, I don't do anything here, just catch it.
            */
        } finally {
            /* Logout current user, even if the import raised an exception,
               because database data are probably changed anyway. */
            onlineUserManager.logout(request, response);
            //this didn't work: ManagerFactory.getOnlineUserFactory().logout(request, response);

            //delete uploaded file, but only if it's not already deleted !
            if (deleteAfterImport && (importFile.exists())) {
                importFile.delete();
            }
            importFile = null;
        }
    }


// =================================================================
// ===== PRINTING STATUS AND ERROR MESSAGES TO THE OUTPUT HTML =====
// =================================================================
    private static boolean oddLine = true;
    private static boolean outputHtmlNotEnded = false;
    private static boolean outputNonImportantMessages = true;
    private static boolean outputImportantMessages = true;
    //outputErrors is always true

    public static void setMessageOutputLevel(int messageLevel) {
        switch (messageLevel) {
            case MVNForumConfig.MESSAGE_LEVEL_ONLY_ERRORS:
                outputImportantMessages = false;
                outputNonImportantMessages = false;
                break;
            case MVNForumConfig.MESSAGE_LEVEL_IMPORTANT_MESSAGES:
                outputImportantMessages = true;
                outputNonImportantMessages = false;
                break;
            default: //MVNForumConfig.MESSAGE_LEVEL_ALL_MESSAGES
                outputNonImportantMessages = true;
                outputImportantMessages = true;
                break;
        }
    }

    public static void startHtml(HttpServletRequest request) {
        if (outputHtml != null) {
            outputHtmlNotEnded = true;
            outputHtml.println("<html>");
            outputHtml.println("<head>");
            outputHtml.println("   <title>mvnForum - Import/Restore Process</title>");
            outputHtml.println("   <meta name=\"copyright\" content=\"This software is copyrighted by MyVietnam.net and released under the terms of the GNU General Public License GPL license. All rights reserved.\">");
            outputHtml.println("   <meta name=\"Keywords\" content=\"mvnForum, server side java, online meetings, collaboration, bulletin board, board, forum, discussion board, jsp, servlet, java, j2se, j2ee, database, jdbc, connection pool, mysql, MyVietnam, mvnForum, framework, web app, web application, servlet container, tomcat, jboss, weblogic, websphere\">");
            outputHtml.println("   <meta name=\"Description\" content=\"mvnForum is a powerful Jsp/Servlet forum (discussion board) - based on Java technology.\">");
            outputHtml.println("   <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
            outputHtml.println("   <meta http-equiv=\"Expires\" content=\"-1\">");
            outputHtml.println("   <link href=\""+request.getContextPath()+"/mvnplugin/mvnforum/css/style.css\" rel=\"stylesheet\" type=\"text/css\">");
            outputHtml.println("</head>");
            outputHtml.println("<body leftmargin=\"0\" topmargin=\"0\">");
            outputHtml.println("<br/>");
            outputHtml.println("");
            outputHtml.println("<table class=\"tborder\" width=\"95%\" cellpadding=\"3\" align=\"center\">");
            outputHtml.println("<tr class=\"theader\">");
            outputHtml.println("   <td align=\"center\">Messages during the process</td>");
            outputHtml.println("</tr>");
            outputHtml.flush();
            oddLine=true;
        }
    }

    public static void endHtml() {
        if (outputHtml != null) {
            outputHtml.println("</table>");
            outputHtml.println("");
            outputHtml.println("<br/>");
            outputHtml.println("</body>");
            outputHtml.println("</html>");
            outputHtml.flush();
            outputHtmlNotEnded = false;
        }
    }

    public static void addMessage(String message) {
        if (outputNonImportantMessages) {
            outputHtmlTableRow(message, "messageText");
        }
    }

    public static void addErrorMessage(String message) {
        outputHtmlTableRow(message, "messageTextBoldRed");
    }

    /**
     * Adds warning that the process contained some errors, and the database
     * might be inconsistent now. It also tries to clear the database and
     * create default entries, including "admin" member.
     */
    public static void addFinalErrorHandling(HttpServletRequest request, boolean resetAllData) {
        outputHtmlTableRow("&nbsp;", "messageTextBoldRed"); //add one empty row in output message table
        String message = "There was an error during the import. DATA ARE NOW PROBABLY DAMAGED.<br/>";
        if (resetAllData) {
            message += "Trying to clear the database and create \"admin\" member (password will be \"admin\")...<br/>";
        }
        outputHtmlTableRow(message, "messageTextBoldRed");

        String indexUrl = request.getContextPath()+
                          UserModuleConfig.getUrlPattern()+
                          "/index";
        String finalMessage = "If you still want to import these data that produced an error, " +
               "please correct above errors first.<br/>" +
               "Then proceed to: &nbsp;&nbsp;&nbsp;"+
               "<a href=\"login\" class=\"menuLink\">Login</a>&nbsp;|&nbsp;" +
               "<a href=\"index\" class=\"menuLink\">Admin Index</a>&nbsp;|&nbsp;" +
               "<a href=\""+indexUrl+"\" class=\"menuLink\">Forum Index</a><br/>" +
               "<span class=\"messageTextBoldRed\">" +
               "Notice you should enter new username and password!" +
               "</span>";
        try {
            if (resetAllData) {
                ImportWebHelper.clearDatabase();
                ImportWebHelper.clearFiles(request.getSession().getServletContext());
                ImportWebHelper.createDefaultContents();
            }
        } catch (IOException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } catch (DatabaseException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } catch (CreateException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } catch (DuplicateKeyException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } catch (ObjectNotFoundException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } catch (ForeignKeyNotFoundException e) {
            addErrorMessage("Still got an error. We strongly suggest you to create new empty database.<br/>");
        } finally {
            outputHtmlTableRow(finalMessage, "messageTextBoldRed");
        }
    }

    public static void addSuccessMessage(HttpServletRequest request) {
        String indexUrl = request.getContextPath()+
                          UserModuleConfig.getUrlPattern()+
                          "/index";
        String message = "Successful. Proceed to: &nbsp;&nbsp;&nbsp;"+
                         "<a href=\"login\" class=\"menuLink\">Login</a>&nbsp;|&nbsp;" +
                         "<a href=\"index\" class=\"menuLink\">Admin Index</a>&nbsp;|&nbsp;" +
                         "<a href=\""+indexUrl+"\" class=\"menuLink\">Forum Index</a><br/>" +
                         "<span class=\"messageTextBoldRed\">" +
                         "Notice you should enter new username and password!" +
                         "</span>";
        outputHtmlTableRow(message, "messageTextBold");
    }

    public static void addImportantMessage(String message) {
        if (outputImportantMessages) {
            outputHtmlTableRow(message, "messageTextBoldRed");
        }
    }

    private static void outputHtmlTableRow(String message, String cssClass) {
        if (outputHtml!=null) {
            if (oddLine) {
                outputHtml.println("<tr class=\"trow1\">");
            } else {
                outputHtml.println("<tr class=\"trow2\">");
            }
            outputHtml.println("   <td class=\""+cssClass+"\">"+message+"</td>");
            outputHtml.println("</tr>");
            outputHtml.flush();
            oddLine=!oddLine;
        }
    }

}
