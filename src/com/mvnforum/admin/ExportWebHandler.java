/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ExportWebHandler.java,v 1.23 2009/01/02 18:31:45 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
 * $Date: 2009/01/02 18:31:45 $
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;

/**
 * @author <a href="mailto:">Igor Manic </a>
 * @version $Revision: 1.23 $, $Date: 2009/01/02 18:31:45 $<br/>
 *          <code>ExportWebHandler</code> class implements methods that
 *          process HTTP requests for export. Data could be exported to MVN
 *          Forum XML file conforming <a
 *          href="http://www.mvnforum.com/mvn.dtd">http://www.mvnforum.com/mvn.dtd
 *          </a>, or to MVN Forum backup ZIP file.
 *
 */
public final class ExportWebHandler {

    /** Message log. */
    private static final Logger log = LoggerFactory.getLogger(ExportWebHandler.class);

    /** Cannot instantiate. */
    private ExportWebHandler() {
    }

    /**
     * Processes export requests made from corresponding HTML page. <br/>
     * Request should contain the parameter <code>ExportType</code> that tells
     * us what type of destination backup file user wants.
     *
     * @param request
     *            <code>HttpServletRequest</code> of the request.
     *
     * @throws DatabaseException
     * @throws AuthenticationException
     * @throws ExportException
     */
    public static void exportXmlZip(HttpServletRequest request)
        throws DatabaseException, AuthenticationException, ExportException {

        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        int logonMemberID = onlineUser.getMemberID();
        String logonMemberName = onlineUser.getMemberName();
        Calendar exportTime = Calendar.getInstance();
        String exportIP = request.getRemoteAddr();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        int exportType = MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML; //default
        try {
            exportType = ParamUtil.getParameterInt(request, "ExportType", MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML);
        } catch (BadInputException e) {
            exportType = MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML; //default;
        }

        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(exportTime.getTime());
        String filename = MVNForumConfig.BACKUP_FILE_PREFIX
                          + timestamp
                          + ((exportType == MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP) ? ".zip"
                             : ".xml");
        log.debug("Will make export/backup file: " + filename);

        switch (exportType) {
        case MVNForumConfig.IMPORTEXPORT_TYPE_MVN_XML:
            ExportWebHelper.exportXml(filename, request, logonMemberID, logonMemberName, exportTime, exportIP);
            break;
        case MVNForumConfig.IMPORTEXPORT_TYPE_MVN_ZIP:
            ExportWebHelper.exportZip(filename, request, logonMemberID, logonMemberName, exportTime, exportIP);
            break;

        default:
            log.error("exportXmlZip: invalid exportType = " + exportType);
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ExportException.invalid_export_type_specified");
            throw new ExportException(localizedMessage);
            //throw new ExportException("Invalid export type specified.");
        }
    }

    public static void getExportXmlZip(HttpServletRequest request, HttpServletResponse response)
        throws BadInputException, DatabaseException, AuthenticationException, IOException {

        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        String filename = ParamUtil.getParameterSafe(request, "filename", true);// must check empty
        FileUtil.checkGoodFileName(filename);
        filename = DisableHtmlTagFilter.filter(filename);

        File f = new File(MVNForumConfig.getBackupDir() + File.separatorChar + filename);
        if ((!f.exists()) || (!f.isFile())) {
            log.error("Can't find a file to be downloaded (or maybe it's directory).");
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_exist_or_not_file_to_be_downloaded");
            throw new IOException(localizedMessage);
            //throw new IOException("Can't find a file to be downloaded (or maybe it's directory).");
        } else {
            try {
                response.setContentType("application/octet-stream");
                response.setHeader("Location", filename);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                int len = (int) f.length();
                if (len > 0) {
                    response.setContentLength(len);
                }

                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(f), 1024 /* buffer size */);
                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 1024 /* buffer size */);
                response.setBufferSize(1024);
                //when we start download, we cannot redirect or raise
                // exceptions
                sendToUser(inputStream, outputStream);

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                    inputStream = null;
                }
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                    }
                    outputStream = null;
                }
            } catch (FileNotFoundException e) {
                log.error("Can't find a backup file on server.", e);
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_found_backup_file");
                throw new IOException(localizedMessage);
                //throw new IOException("Can't find a backup file on server.");
                //rethrow - the user will be redirected to errorpage
            } catch (IOException e) {
                log.error("Error while trying to send backup file from server.", e);
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.error_while_sending_backup_file");
                throw new IOException(localizedMessage);
                //throw new IOException("Error while trying to send backup file from server.");
                //rethrow - the user will be redirected to errorpage
            } finally {
                f = null;
            }
        }
    }

    public static void deleteExportXmlZip(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, IOException {

        OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        String filename = GenericParamUtil.getParameter(request, "filename", true);
        FileUtil.checkGoodFileName(filename);
        filename = DisableHtmlTagFilter.filter(filename);

        File f = new File(MVNForumConfig.getBackupDir() + File.separatorChar + filename);
        if ((!f.exists()) || (!f.isFile())) {
            log.error("Can't find a file to be deleted (or maybe it's directory).");
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_exist_or_not_file_to_be_deleted");
            throw new IOException(localizedMessage);
            //throw new IOException("Can't find a file to be deleted (or maybe it's directory).");
        } else {
            f.delete();
            f = null;
        }
    }

    /**
     * Sends (downloads) a file from server to the user.
     *
     * @param inputStream
     *            <code>BufferedInputStream</code> connected to the backup
     *            file that was made on server.
     * @param outputStream
     *            <code>BufferedOutputStream</code> connected to the
     *            <code>HTTP response</code>.
     */
    private static void sendToUser(BufferedInputStream inputStream,
                                   BufferedOutputStream outputStream) {
        //this method should not throw any exception, since we are now starting
        // commiting output
        try {
            //used stream objects are already buffered, so I won't do buffering
            int b = 0;
            while ((b = inputStream.read()) >= 0) {
                outputStream.write(b);
            }
        } catch (IOException e) {
            try {
                outputStream.write("FATAL ERROR. Can't continue download.".getBytes());
            } catch (IOException ee) {
                /*
                 * Nothing we can do now. Since output was already commited, we
                 * can't raise exception here.
                 */
            }
        }
    }
}
