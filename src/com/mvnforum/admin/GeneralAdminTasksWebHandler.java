/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/GeneralAdminTasksWebHandler.java,v 1.131 2010/08/02 09:16:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.131 $
 * $Date: 2010/08/02 09:16:42 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 * @author: Igor Manic
 */
package com.mvnforum.admin;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.HtmlNewLineFilter;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.common.SendMailUtil;
import com.mvnforum.db.*;
import com.mvnforum.search.attachment.AttachmentIndexer;
import com.mvnforum.search.attachment.RebuildAttachmentIndexTask;
import com.mvnforum.search.member.MemberIndexer;
import com.mvnforum.search.member.RebuildMemberIndexTask;
import com.mvnforum.search.post.PostIndexer;
import com.mvnforum.search.post.RebuildPostIndexTask;

import freemarker.template.*;

public class GeneralAdminTasksWebHandler {

    private static final Logger log = LoggerFactory.getLogger(GeneralAdminTasksWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();

    public GeneralAdminTasksWebHandler() {
    }

    public void prepareShowIndex(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Timestamp startTimestamp = MVNForumContextListener.getInstance().getStartTimestamp();
        long upTime = now.getTime() - startTimestamp.getTime();

        if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet()) {
            request.setAttribute("PortalInfo", request.getPortalInfo());
        }
        
        request.setAttribute("StartTime", onlineUser.getGMTTimestampFormat(startTimestamp));
        request.setAttribute("NowTime", onlineUser.getGMTTimestampFormat(now));
        request.setAttribute("UpTime", DateUtil.formatDuration(upTime));
    }

    public void prepareTestSystem(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();
    }

    public void changeShowUserArea(GenericRequest request)
        throws DatabaseException, AuthenticationException,
        BadInputException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String mode = GenericParamUtil.getParameterSafe(request, "mode", true);

        if (mode.equals("on")) {
            MVNForumConfig.setShouldShowUserArea(true);
        } else if (mode.equals("off")) {
            MVNForumConfig.setShouldShowUserArea(false);
        } else {
            throw new BadInputException("Not support this mode " + mode);
        }
    }

    public void processClearCache(GenericRequest request)
        throws BadInputException, AuthenticationException, DatabaseException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String target = GenericParamUtil.getParameterSafe(request, "target", true);

        if (target.equals("member")) {
            MemberCache.getInstance().clear();
        } else if (target.equals("post")) {
            PostCache.getInstance().clear();
        } else if (target.equals("thread")) {
            ThreadCache.getInstance().clear();
        } else if (target.equals("forum")) {
            ForumCache.getInstance().clear();
        } else if (target.equals("category")) {
            CategoryCache.getInstance().clear();
        } else if (target.equals("all")) {
            MemberCache.getInstance().clear();
            PostCache.getInstance().clear();
            ThreadCache.getInstance().clear();
            ForumCache.getInstance().clear();
            CategoryCache.getInstance().clear();
        } else {
            throw new BadInputException("Not support this target " + target);
        }
    }

    public void prepareImportExport(HttpServletRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        request.setAttribute("BackupFilesOnServer", ImportWebHandler.getBackupFilesOnServer());
    }

    public void importXmlZip(HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException, AuthenticationException, ImportException, UnsupportedEncodingException {

        //SecurityUtil.checkHttpPostMethod(request);
        SecurityUtil.checkHttpPostMethod(request, true, true);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        ImportWebHandler.importXmlZip(request, response);
    }

    public void exportXmlZip(HttpServletRequest request)
        throws DatabaseException, AuthenticationException, ExportException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        ExportWebHandler.exportXmlZip(request);
    }

    public void getExportXmlZip(HttpServletRequest request, HttpServletResponse response)
        throws BadInputException, DatabaseException, AuthenticationException, IOException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        ExportWebHandler.getExportXmlZip(request, response);
    }

    public void deleteExportXmlZip(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, IOException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        ExportWebHandler.deleteExportXmlZip(request);
        //now prepare all for redirection to "/importexport"
        request.setAttribute("BackupFilesOnServer", ImportWebHandler.getBackupFilesOnServer());
    }

    public void prepareRebuildIndex(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        int postNumDocs = PostIndexer.getNumDocs();

        int memberNumDocs = -1;
        if (DAOFactory.getMemberDAO().isSupportGetMembers()) {
            memberNumDocs = MemberIndexer.getNumDocs();
        }

        int attachmentNumDocs = -1;
        if (MVNForumConfig.getEnableAttachment()) {
            attachmentNumDocs = AttachmentIndexer.getNumDocs();
        }

        int postCount = DAOFactory.getPostDAO().getNumberOfPosts();

        int memberCount = 0;
        if (DAOFactory.getMemberDAO().isSupportGetNumberOfMembers()) {
            memberCount = DAOFactory.getMemberDAO().getNumberOfMembers();
        }

        int attachmentCount = 0;
        if (MVNForumConfig.getEnableAttachment()) {
            attachmentCount = DAOFactory.getAttachmentDAO().getNumberOfAttachments(-1, -1);
        }

        request.setAttribute("PostNumDocs", new Integer(postNumDocs));
        request.setAttribute("PostCount", new Integer(postCount));

        request.setAttribute("MemberNumDocs", new Integer(memberNumDocs));
        request.setAttribute("MemberCount", new Integer(memberCount));

        request.setAttribute("AttachmentNumDocs", new Integer(attachmentNumDocs));
        request.setAttribute("AttachmentCount", new Integer(attachmentCount));
    }

    public void rebuildIndex(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String target = GenericParamUtil.getParameter(request, "target");

        if (target.equalsIgnoreCase("post")) {

            if (RebuildPostIndexTask.isRebuilding() == false) {
                PostIndexer.scheduleRebuildIndexTask();
            }

        } else if (target.equalsIgnoreCase("member")) {

            /*if (MVNForumConfig.getEnableExternalUserDatabase() == false) {
                log.debug("Start rebuild member index");
                MemberIndexer.scheduleRebuildIndexTask();
            }*/
            if (DAOFactory.getMemberDAO().isSupportGetMembers()) {
                if (RebuildMemberIndexTask.isRebuilding() == false) {
                    log.debug("Start rebuild member index");
                    MemberIndexer.scheduleRebuildIndexTask();
                }
            }

        } else if (target.equalsIgnoreCase("attachment")) {

            if (RebuildAttachmentIndexTask.isRebuilding() == false) {
                AttachmentIndexer.scheduleRebuildIndexTask();
            }

        } else if (target.length() == 0) {

            if (RebuildPostIndexTask.isRebuilding() == false) {
                PostIndexer.scheduleRebuildIndexTask();
            }

            if (DAOFactory.getMemberDAO().isSupportGetMembers()) {
                if (RebuildMemberIndexTask.isRebuilding() == false) {
                    MemberIndexer.scheduleRebuildIndexTask();
                }
            }

            if (MVNForumConfig.getEnableAttachment()) {
                if (RebuildAttachmentIndexTask.isRebuilding() == false) {
                    AttachmentIndexer.scheduleRebuildIndexTask();
                }
            }
        }
    }

    public void prepareSendMail(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanSendMail();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");

        if (isPreviewing) {
            GenericParamUtil.getParameterEmail(request, "From");//just call to check the validity of From
            String to = GenericParamUtil.getParameter(request, "To");
            String cc = GenericParamUtil.getParameter(request, "Cc");
            String bcc = GenericParamUtil.getParameter(request, "Bcc");
            String subject = GenericParamUtil.getParameter(request, "Subject", true);
            String message = GenericParamUtil.getParameter(request, "Message", true);

            int mailToSelector = GenericParamUtil.getParameterInt(request, "MailToSelector");

            String warningMessage = "";

            String previewMessage = message;
            String previewSubject = subject;

            if (mailToSelector == 0) { // that is, send to specific users
                if ((to.length() == 0) && (cc.length() == 0) && (bcc.length() == 0)) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.please_enter_to_or_cc_or_bcc");
                    throw new BadInputException(localizedMessage);
                    //throw new BadInputException("Please enter at least To, CC or BCC.");
                }
            } else { // send to group of users
                // Then add members coresponding to the mailToSelector
                String kind;
                int groupID = -1;
                switch (mailToSelector) {
                    case 1:
                        kind = "all";
                        groupID = GenericParamUtil.getParameterInt(request, "group");
                        to = "Group: " + DAOFactory.getGroupsDAO().getGroup(groupID).getGroupName();
                        break;
                    case 2:
                        kind = "activated";
                        to = "All activated members";
                        break;
                    case 3:
                        kind = "nonactivated";
                        to = "All non-activated members";
                        break;
                    default:
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process_mailto_selector", new Object[] {new Integer(mailToSelector)});
                        throw new BadInputException(localizedMessage);
                        //throw new BadInputException("Cannot process MailToSelector = " + mailToSelector);
                }

                Collection memberBeans = null;

                if (kind.equals("all")) {
                    if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
                        memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inActivationStatus("all");
                    } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_list_member_in_reserved_group");
                        throw new AssertionError(localizedMessage);
                        //throw new AssertionError("Cannot list member in a reserved (virtual) group.");
                    } else {
                        memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inGroup(groupID);
                    }
                } else {
                    memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inActivationStatus(kind);
                }

                if (memberBeans.size() > 0 ) {
                    warningMessage = String.valueOf(memberBeans.size() + " members");
                } else {
                    warningMessage = "No members";
                }
                MemberBean previewedMember = null;
                for (Iterator iterator = memberBeans.iterator();iterator.hasNext();) {
                    previewedMember = (MemberBean)iterator.next();
                    previewMessage = processMailTemplate(previewedMember, message);
                    previewSubject = processMailTemplate(previewedMember, subject);
                    request.setAttribute("PreviewEmail", previewedMember.getMemberEmail());
                    break;
                }
            }// end of send to a group of users
            request.setAttribute("PreviewTo", to);
            request.setAttribute("PreviewCc", cc);
            request.setAttribute("PreviewBcc", bcc);
            request.setAttribute("PreviewSubject", previewSubject);
            request.setAttribute("PreviewMessage", HtmlNewLineFilter.filter(previewMessage));
            request.setAttribute("WarningMessage",  warningMessage);
        }

        Collection groupBeans = DAOFactory.getGroupsDAO().getGroups();
        for (Iterator iterator = groupBeans.iterator(); iterator.hasNext();) {
            GroupsBean groupBean = (GroupsBean)iterator.next();
            if (groupBean.getGroupID() == MVNForumConstant.GROUP_ID_OF_GUEST) {
                iterator.remove();
                break;
            }
        }
        request.setAttribute("GroupBeans", groupBeans);
    }

    public void sendMail(GenericRequest request)
        throws BadInputException, MessagingException, DatabaseException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanSendMail();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        String from = GenericParamUtil.getParameterEmail(request, "From");
        String to = GenericParamUtil.getParameter(request, "To");
        String cc = GenericParamUtil.getParameter(request, "Cc");
        String bcc = GenericParamUtil.getParameter(request, "Bcc");
        String subject = GenericParamUtil.getParameter(request, "Subject", true);
        String message = GenericParamUtil.getParameter(request, "Message", true);

        int mailToSelector = GenericParamUtil.getParameterInt(request, "MailToSelector");

        Collection mailMessageStructs = new ArrayList();
        if (mailToSelector == 0) { // that is, send to specific users
            if ((to.length() == 0) && (cc.length() == 0) && (bcc.length() == 0)) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.please_enter_to_or_cc_or_bcc");
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("Please enter at least To, CC or BCC.");
            }
            MailMessageStruct mailMessageStruct = new MailMessageStruct();
            mailMessageStruct.setFrom(from);
            mailMessageStruct.setTo(to);
            mailMessageStruct.setCc(cc);
            mailMessageStruct.setBcc(bcc);
            mailMessageStruct.setSubject(subject);
            mailMessageStruct.setMessage(message);
            mailMessageStructs.add(mailMessageStruct);
        } else { // send to group of users
            // now add emails in the To, CC, BCC. Please note that we ONLY use the method setTo
            String[] emailArray = MailUtil.getEmails(to, cc, bcc);
            for (int i = 0; i < emailArray.length; i++) {
                MailMessageStruct mailMessage = new MailMessageStruct();
                mailMessage.setFrom(from);
                mailMessage.setTo(emailArray[i]);
                mailMessage.setSubject(subject);
                mailMessage.setMessage(message);
                mailMessageStructs.add(mailMessage);
            }

            // Then add members corresponding to the mailToSelector
            String kind;
            int groupID = -1;
            switch (mailToSelector) {
                case 1:
                    kind = "all";
                    groupID = GenericParamUtil.getParameterInt(request, "group");
                    break;
                case 2:
                    kind = "activated";
                    break;
                case 3:
                    kind = "nonactivated";
                    break;
                default:
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_process_mailto_selector", new Object[] {new Integer(mailToSelector)});
                    throw new BadInputException(localizedMessage);
                    //throw new BadInputException("Cannot process MailToSelector = " + mailToSelector);
            }

            Collection memberBeans = new ArrayList ();
            if (kind.equals("all")) {
                if (groupID == MVNForumConstant.GROUP_ID_OF_REGISTERED_MEMBERS) {
                    if (DAOFactory.getMemberDAO().isSupportGetEnableMembers_inActivationStatus()) {
                        memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inActivationStatus("all");
                    }
                } else if (groupID <= MVNForumConstant.LAST_RESERVED_GROUP_ID) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_list_member_in_reserved_group");
                    throw new AssertionError(localizedMessage);
                    //throw new AssertionError("Cannot list member in a reserved (virtual) group.");
                } else {
                    if (DAOFactory.getMemberDAO().isSupportGetEnableMembers_inGroup()) {
                        memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inGroup(groupID);
                    }
                }
            } else {
                if (DAOFactory.getMemberDAO().isSupportGetEnableMembers_inActivationStatus()) {
                    memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inActivationStatus(kind);
                }
            }
            for (Iterator countIterator = memberBeans.iterator(); countIterator.hasNext(); ) {
                MemberBean memberBean = (MemberBean) countIterator.next();
                MailMessageStruct mailMessage = new MailMessageStruct();

                mailMessage.setFrom(from);
                String memberEmail = memberBean.getMemberEmail();
                String processedSubject = processMailTemplate(memberBean, subject);
                String processedMessage = processMailTemplate(memberBean, message);
                mailMessage.setTo(memberEmail);
                mailMessage.setSubject(processedSubject);
                mailMessage.setMessage(processedMessage);
                mailMessageStructs.add(mailMessage);
            } //for
        }

        try {
            MailUtil.sendMail(mailMessageStructs, locale);
            
            String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.SendMail", new Object[]{from, to, subject});
            eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "send mail", actionDesc, EventLogService.LOW);

        } catch (UnsupportedEncodingException e) {
            log.error("Cannot support encoding", e);
        }
        //check here again, we only need size of mailMessageStruts
        request.setAttribute("SizeOfMailMessageStructs", new Integer(mailMessageStructs.size()));
        request.setAttribute("MailMessageStructs", mailMessageStructs);
    }

    public void sendActivateMailToAll(GenericRequest request)
        throws BadInputException, MessagingException, DatabaseException,
        AuthenticationException, ObjectNotFoundException, IOException, TemplateException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        Locale locale = I18nUtil.getLocaleInRequest(request);
        permission.ensureCanSendMail();//@todo: check if this is the correct permission

        //@todo: review this constant parameter "nonactivated"
        Collection memberBeans = DAOFactory.getMemberDAO().getEnableMembers_inActivationStatus("nonactivated");
        Collection mailMessageStructs = new ArrayList();
        String serverName = ParamUtil.getServerPath();//ParamUtil.getServer2(request);
        for (Iterator countIterator = memberBeans.iterator(); countIterator.hasNext(); ) {
            MemberBean memberBean = (MemberBean) countIterator.next();
            MailMessageStruct mailMessage = SendMailUtil.getActivationCodeEmail(memberBean.getMemberID(), serverName);
            mailMessageStructs.add(mailMessage);
        }//for

        try {
            log.debug("About to send activate mail to all non activated members, total = " + mailMessageStructs.size());
            MailUtil.sendMail(mailMessageStructs, locale);
            
            String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.SendActivateMailToAll");
            eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "send activate mail to all", actionDesc, EventLogService.LOW);

        } catch (UnsupportedEncodingException e) {
            log.error("Cannot support encoding", e);
        }

        request.setAttribute("MailMessageStructs", mailMessageStructs);
    }

    /**
     *Process mail with a template from User
     */
    private String processMailTemplate(MemberBean memberBean, String message) {
        try {
            Map root = new HashMap();
            StringWriter messageWriter = new StringWriter(256);

            //Just assume some variables are needed to be replaced
            root.put("memberID", new Integer(memberBean.getMemberID()));
            root.put("memberName", memberBean.getMemberName());
            root.put("memberFirstname", memberBean.getMemberFirstname());
            root.put("memberLastname", memberBean.getMemberLastname());
            root.put("memberEmail", memberBean.getMemberEmail());

            StringReader stringReader = new StringReader(message);
            Configuration cfg = MVNForumConfig.getFreeMarkerConfiguration();
            Template messageTemplate = new Template("", stringReader, cfg, "");
            messageTemplate.process(root, messageWriter);
            message = messageWriter.toString();
        } catch (IOException ioe) {
            log.error("Cannot process mail template", ioe);
            //if we have problem while processing template, we will return orginal message
        } catch (TemplateException te) {
            log.error("Cannot process mail template", te);
            //if we have problem while processing template, we will return orginal message
        }
        //log.debug("processMailTemplate return = " + message);
        return message;
    }

    public void prepareViewLogSystem(GenericRequest request)
        throws FileNotFoundException, DatabaseException, BadInputException, AuthenticationException, IOException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        // just for checking the valid value of "linecount"
        GenericParamUtil.getParameterUnsignedInt(request, "linecount", 25);
        String shortName = GenericParamUtil.getParameterSafe(request, "filename", false);
        String logDir = MVNForumConfig.getLogDir();
        String logFileName = "";

        // check if the log folder is valid
        File logDirFile = new File(logDir);
        if (!logDirFile.exists()) {
            throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getCanonicalPath());
        }

        if (!logDirFile.isDirectory()) {
            throw new IOException("The log dir is not a directory: " + logDirFile.getCanonicalPath());
        }

        if (shortName.length() == 0) {
            logFileName = MVNForumConfig.getLogFile();
        } else {
            FileUtil.checkGoodFileName(shortName);
            logFileName = logDir + File.separatorChar + shortName;
        }

        File logFile = new File(logFileName);
        if (!logFile.exists()) {
            throw new FileNotFoundException("Cannot find the log file " + logFile.getCanonicalPath());
        }

        long size = logFile.length();
        String humanSize = FileUtil.getHumanSize(size);

        request.setAttribute("FileName", shortName);
        request.setAttribute("LogDir", logDirFile.getCanonicalPath());
        request.setAttribute("LogFileName", logFile.getCanonicalPath());
        request.setAttribute("LogFileSize", String.valueOf(size));
        request.setAttribute("LogFileHumanSize", humanSize);

        if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet()) {

            if (!logFile.canRead()) {
                throw new IOException("Cannot read the log file: " + logFile.getAbsolutePath());
            }
            int lineCount = GenericParamUtil.getParameterUnsignedInt(request, "linecount", 25); // 25, 50 100 200 400 800
            if (lineCount > 5000) {
                lineCount = 5000;
            }
            String[] contentLog = FileUtil.getLastLines(logFile, lineCount);

            request.setAttribute("ContentLog", contentLog);
        }
    }

    public void prepareLogFrame(GenericRequest request)
        throws DatabaseException, AuthenticationException, IOException, FileNotFoundException, BadInputException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String logFileName = GenericParamUtil.getParameterSafe(request, "filename", false);

        if (logFileName.length() == 0) {
            logFileName = MVNForumConfig.getLogFile();
        } else {
            String logDir = MVNForumConfig.getLogDir();
            File logDirFile = new File(logDir);
            if (!logDirFile.exists()) {
                throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getAbsolutePath());
            }

            if (!logDirFile.isDirectory()) {
                throw new IOException("The log dir is not a directory: " + logDirFile.getAbsolutePath());
            }
            FileUtil.checkGoodFileName(logFileName);
            logFileName = logDir + File.separatorChar + logFileName;
        }

        File logFile = new File(logFileName);
        if (!logFile.exists()) {
            throw new FileNotFoundException("Cannot find the log file: " + logFile.getAbsolutePath());
        }
        if (!logFile.canRead()) {
            throw new IOException("Cannot read the log file: " + logFile.getAbsolutePath());
        }

        int lineCount = GenericParamUtil.getParameterUnsignedInt(request, "linecount", 25); // 25, 50 100 200 400 800
        if (lineCount > 5000) {
            lineCount = 5000;
        }
        String[] contentLog = FileUtil.getLastLines(logFile, lineCount);

        request.setAttribute("ContentLog", contentLog);
    }

    public void backupSystemLog(GenericRequest request)
        throws DatabaseException, AuthenticationException, IOException, FileNotFoundException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        boolean empty = GenericParamUtil.getParameterBoolean(request, "empty");

        String logFileName = MVNForumConfig.getLogFile();

        String logDir = MVNForumConfig.getLogDir();
        File logDirFile = new File(logDir);
        if (!logDirFile.exists()) {
            throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getAbsolutePath());
        }
        if (!logDirFile.isDirectory()) {
            throw new IOException("The log dir is not a directory: " + logDirFile.getAbsolutePath());
        }

        String pattern = "yyyy-MM-dd_HH-mm-ss";
        String newFileName = logDir + File.separator + "mvnforumlog_" + DateUtil.format(new Date(), pattern) + ".log";
        File newFile = new File(newFileName);
        // We don't have to check file here, we check it in FileUtil.copyFile
        FileUtil.copyFile(logFileName, newFileName, false);

        if (empty) {
            FileUtil.emptyFile(logFileName);
        }

        long size = newFile.length();
        String humanSize = FileUtil.getHumanSize(size);
        request.setAttribute("LogFileName", StringUtil.escapeBackSlash(newFile.getCanonicalPath()));// we escape because of Pluto 1.1 bug
        request.setAttribute("LogFileSize", String.valueOf(size));
        request.setAttribute("LogFileHumanSize", humanSize);
    }

    public void prepareListLogFiles(GenericRequest request)
        throws DatabaseException, AuthenticationException, IOException, FileNotFoundException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String logDir = MVNForumConfig.getLogDir();
        File logDirFile = new File(logDir);
        if (!logDirFile.exists()) {
            throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getAbsolutePath());
        }
        if (!logDirFile.isDirectory()) {
            throw new IOException("The log dir is not a directory: " + logDirFile.getAbsolutePath());
        }

        File[] files = logDirFile.listFiles();
        File currentLogFile = new File(MVNForumConfig.getLogFile());

        request.setAttribute("LogFiles", files);
        request.setAttribute("CurrentLogFile", currentLogFile);
    }

    public void downloadLogFile(HttpServletRequest request, HttpServletResponse response)
        throws DatabaseException, AuthenticationException, BadInputException, IOException {
        
        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // This method require fileName is not empty
        String fileName = ParamUtil.getParameterSafe(request, "filename", true);// must check empty here
        FileUtil.checkGoodFileName(fileName);

        String logDir = MVNForumConfig.getLogDir();
        File logDirFile = new File(logDir);
        if (!logDirFile.exists()) {
            throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getAbsolutePath());
        }
        if (!logDirFile.isDirectory()) {
            throw new IOException("The log dir is not a directory: " + logDirFile.getAbsolutePath());
        }

        File file = new File(MVNForumConfig.getLogDir() + File.separatorChar + fileName);
        if ((!file.exists()) || (!file.isFile())) {
            log.error("Can't find a file " + file + " to be downloaded (or maybe it's directory).");
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.io.IOException.not_exist_or_not_file_to_be_downloaded");
            throw new IOException(localizedMessage + " " + file);
            //throw new IOException("Can't find a file to be downloaded (or maybe it's directory).");
        }

        BufferedOutputStream output = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Location", fileName);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int length = (int) file.length();
            if (length > 0) {
                response.setContentLength(length);
            }

            output = new BufferedOutputStream(response.getOutputStream(), 1024 /* buffer size */);
            response.setBufferSize(1024);

            //when we start download, we cannot redirect or raise exceptions
            FileUtil.popFile(file, output);
            output.flush();
        } catch (FileNotFoundException e) {
            log.error("Can't find the such log file on server " + fileName);
        } catch (IOException e) {
            log.error("Error while trying to send backup file from server (" + fileName + ").", e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) { }
            }
        }
    }

    public void deleteLogFile(GenericRequest request)
        throws DatabaseException, AuthenticationException, BadInputException, IOException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureCanAdminSystem();

        String fileName = GenericParamUtil.getParameterSafe(request, "filename", true);
        FileUtil.checkGoodFileName(fileName);

        String logDir = MVNForumConfig.getLogDir();
        File logDirFile = new File(logDir);
        if (logDirFile.exists() == false) {
            throw new FileNotFoundException("Cannot find the log dir: " + logDirFile.getAbsolutePath());
        }
        if (logDirFile.isDirectory() == false) {
            throw new IOException("The log dir is not a directory: " + logDirFile.getAbsolutePath());
        }

        fileName = logDir + File.separator + fileName;
        String logFileName = MVNForumConfig.getLogFile();
        File currentLogFile = new File(logFileName);

        if (currentLogFile.equals(new File(fileName))) {
            throw new AssertionError("Cannot delete the current log file: " + fileName);
        }

        FileUtil.deleteFile(fileName);

        String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(), "mvnforum.eventlog.desc.DeleteLogFile");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN, "delete log file", actionDesc, EventLogService.HIGH);

    }

}
