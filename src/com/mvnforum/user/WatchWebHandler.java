/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/WatchWebHandler.java,v 1.90 2010/06/04 03:50:10 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.90 $
 * $Date: 2010/06/04 03:50:10 $
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
 */
package com.mvnforum.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

import javax.mail.MessagingException;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.*;
import com.mvnforum.categorytree.*;
import com.mvnforum.db.*;
import com.mvnforum.service.*;

import freemarker.template.TemplateException;

public class WatchWebHandler {

    private static final Logger log = LoggerFactory.getLogger(WatchWebHandler.class);
    
    private static CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();

    private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public WatchWebHandler() {
    }

    public void sendMail()
        throws DatabaseException, MessagingException, BadInputException, ObjectNotFoundException, TemplateException, IOException {

        if (MVNForumConfig.getEnableWatch() == false) {
            log.warn("Ingore Watch sendMail because this feature is disabled by administrator.");
            return;
        }
        String forumBase = ParamUtil.getServerPath() + ParamUtil.getContextPath() + UserModuleConfig.getUrlPattern();
        //log.debug("Forum base = " + forumBase);

        //get the list of watch for each member, the watch is chosen based on oldest last sent time
        Collection beans = DAOFactory.getWatchDAO().getMemberBeans();
        //log.debug("Watch: total member = " + beans.size());

        for (Iterator iterator = beans.iterator(); iterator.hasNext(); ) {

            if (TimerUtil.getInstance().isTimerCanceled()) {
                break;
            }

            WatchBean watchBean = (WatchBean)iterator.next();
            int memberID = watchBean.getMemberID();

            // check if member is enable here
            if (DAOFactory.getMemberDAO().getActivateCode(memberID).equals(MemberBean.MEMBER_ACTIVATECODE_ACTIVATED) == false) {
                // Not activated, then we continue with the next user
                continue;
            }

            // Check the frequency of the update
            Timestamp lastSent = watchBean.getWatchLastSentDate();
            Timestamp now = DateUtil.getCurrentGMTTimestamp();

            // We will now support a single watch standard, but should be
            // bitmasked in future to support multiple watch schedule for the same
            // thread or forum
            long minimumWaitTime = 0;
            int watchOption = watchBean.getWatchOption();
            if (watchOption == WatchBean.WATCH_OPTION_DEFAULT) {
                watchOption = MVNForumConfig.getDefaultWatchOption();
            }

            switch (watchOption) {
                case WatchBean.WATCH_OPTION_LIVE:
                    minimumWaitTime = 0;
                    break;
                case WatchBean.WATCH_OPTION_HOURLY:
                    minimumWaitTime = DateUtil.HOUR;
                    break;
                case WatchBean.WATCH_OPTION_DAILY:
                    minimumWaitTime = DateUtil.DAY;
                    break;
                case WatchBean.WATCH_OPTION_WEEKLY:
                    minimumWaitTime = DateUtil.WEEK;
                    break;
                default:// currently only default is processed (WatchOption = 0)
                    // note that watch option might have any value so we have to have default fallback
                    minimumWaitTime = DateUtil.DAY;
                    break;
            }
            if ( (now.getTime() - lastSent.getTime()) > minimumWaitTime ) {
                sendMail_forMember(memberID, forumBase, lastSent);
            }
        }//for
    }

    void sendMail_forMember(int memberID, String forumBase, Timestamp lastSent)
        throws DatabaseException, MessagingException, BadInputException, ObjectNotFoundException, TemplateException, IOException {

        MemberBean receiver = null;
        try {
            receiver = DAOFactory.getMemberDAO().getMember(memberID);
        } catch (ObjectNotFoundException e) {
            String message = "Cannot get member with id = " + memberID;
            log.error(message, e);

            MailMessageStruct mailMessageStruct = new MailMessageStruct();
            mailMessageStruct.setFrom(MVNForumConfig.getWebMasterEmail());
            mailMessageStruct.setTo(MVNForumConfig.getWebMasterEmail());
            mailMessageStruct.setSubject("Warning of LDAP deleted members");
            mailMessageStruct.setMessage(message);
            MailUtil.sendMail(mailMessageStruct);
            return;// do nothing just return if member does not exist
        }

        if ( (receiver.getMemberEmail() == null) || (receiver.getMemberEmail().length() == 0) ) {
            //AssertionUtil.doAssert(false, "Cannot get email from member with id = " + receiver.getMemberID() + " and login name = " + receiver.getMemberName() + " in WatchWebHandler.sendMail_forMember()");
            String message = "Cannot get email from member with id = " + receiver.getMemberID() + " and login name = " + receiver.getMemberName() + " in WatchWebHandler.sendMail_forMember()";
            log.warn(message);

            MailMessageStruct mailMessageStruct = new MailMessageStruct();
            mailMessageStruct.setFrom(MVNForumConfig.getWebMasterEmail());
            mailMessageStruct.setTo(MVNForumConfig.getWebMasterEmail());
            mailMessageStruct.setSubject("Warning of LDAP members missing email");
            mailMessageStruct.setMessage(message);
            MailUtil.sendMail(mailMessageStruct);
            return;
        }

        MVNForumPermission permission = null;
        try {
            permission = MVNForumPermissionFactory.getAuthenticatedPermission(receiver);
        } catch (AssertionError e) {
            log.error("Cannot create watch mail for Guest with id = " + memberID, e);
            return;// do nothing, just return if member is guest.
        }

        if (permission.isActivated() == false) {
            // if member is not activated, then we ignore this member
            return;
        }

        Collection watchBeans = DAOFactory.getWatchDAO().getWatches_forMember(memberID);
        //log.debug("Watch size = " + watchBeans.size() + " for memberid = " + memberID);
        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        //then optimize the watchBeans
        watchBeans = WatchUtil.optimize(watchBeans);

        WatchMail watchMailDigest = new WatchMail(receiver, permission, forumBase, lastSent, now);
        SingleWatchMail watchMailSingle = new SingleWatchMail(receiver, permission, forumBase, lastSent, now);

        for (Iterator watchIterator = watchBeans.iterator(); watchIterator.hasNext(); ) {
            WatchBean watchBean = (WatchBean)watchIterator.next();
            boolean isDigest = false;
            /*if (watchBean.getWatchType() == WatchBean.WATCH_TYPE_DEFAULT) {
                //@todo : get from config file
                isDigest = true;
            }*/

            switch (watchBean.getWatchType()) {
                case WatchBean.WATCH_TYPE_DIGEST:
                    isDigest = true;
                    break;
                case WatchBean.WATCH_TYPE_NONDIGEST:
                    isDigest = false;
                    break;
                case WatchBean.WATCH_TYPE_DEFAULT:
                    switch (MVNForumConfig.getDefaultWatchType()) {
                    case WatchBean.WATCH_TYPE_DIGEST:
                        isDigest = true;
                        break;
                    case WatchBean.WATCH_TYPE_NONDIGEST:
                        isDigest = false;
                        break;
                    default:
                        isDigest = true;
                        break;
                    }
                    break;
                default:// currently only default is processed (WatchType = 0)
                    // note that watch type might have any value so we have to have default fallback
                    isDigest = true;
                    break;
            }

            if (isDigest) {
                watchMailDigest.appendWatch(watchBean);
            } else {
                watchMailSingle.appendWatch(watchBean);
            }
        }

        if ((watchMailDigest.haveAtLeastOneNewThread() == false) && (watchMailSingle.haveAtLeastOneNewThread() == false)) {
            log.debug("No new thread in watch mail for MemberID = " + memberID);
            return;
        }

        // Send the digest watch email
        if (watchMailDigest.haveAtLeastOneNewThread()) {
            log.debug("About to send digest watch mail to member = " + memberID + " with email = " + receiver.getMemberEmail());
            // send mail now
            String from = MVNForumConfig.getWatchEmail();
            String to = receiver.getMemberEmail();
            String subject = watchMailDigest.getWatchMailSubject();
            String content = watchMailDigest.getWatchMailBody();
            //log.debug("Send message from webmaster to ~ " + to + "~");
            try {
                MailMessageStruct mailMessageStruct = new MailMessageStruct();
                mailMessageStruct.setFrom(from);
                mailMessageStruct.setTo(to);
                mailMessageStruct.setSubject(subject);
                mailMessageStruct.setMessage(content);
                mailMessageStruct.setSendAsHtml(MVNForumConfig.getSendWatchMailAsHTML());
                
                MailUtil.sendMail(mailMessageStruct);
            } catch (UnsupportedEncodingException e) {
                log.error("Cannot support encoding", e);
            }
        }

        // Send the single watch email
        if (watchMailSingle.haveAtLeastOneNewThread()) {
            log.debug("About to send single watch mail to member = " + memberID + " with email = " + receiver.getMemberEmail());
            Collection mailMessageStructs = watchMailSingle.getMailMessageStructs(MVNForumConfig.getWatchEmail(), receiver.getMemberEmail());
            MailUtil.sendMail(mailMessageStructs, null);
        }

        // finally, update the lastsent
        DAOFactory.getWatchDAO().updateLastSentDate_forMember(memberID, now);
    }

    public void prepareList(GenericRequest request)
        throws DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int memberID = onlineUser.getMemberID();
        Locale locale = I18nUtil.getLocaleInRequest(request);

        Collection watchBeans = DAOFactory.getWatchDAO().getWatches_forMember(memberID);

        Collection globalWatchBeans = WatchUtil.getGlobalWatchs(watchBeans);
        Collection categoryWatchBeans = WatchUtil.getCategoryWatchs(watchBeans);
        Collection forumWatchBeans = WatchUtil.getForumWatchs(watchBeans);
        Collection threadWatchBeans = WatchUtil.getThreadWatchs(watchBeans);

        // @todo Improve the performance of the below code
        for (Iterator iter = threadWatchBeans.iterator(); iter.hasNext(); ) {
            WatchBean threadWatchBean = (WatchBean) iter.next();
            int threadID = threadWatchBean.getThreadID();

            ThreadBean threadBean = null;
            try {
                threadBean = DAOFactory.getThreadDAO().getThread(threadID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.threadid_not_exists", new Object[] {new Integer(threadID)});
                //throw new ObjectNotFoundException(localizedMessage);
                iter.remove();
                DAOFactory.getWatchDAO().delete_inThread(threadID);
                log.error(localizedMessage, e);
            }
            threadWatchBean.setThreadBean(threadBean);
        }

        request.setAttribute("WatchBeans", watchBeans);
        request.setAttribute("GlobalWatchBeans", globalWatchBeans);
        request.setAttribute("CategoryWatchBeans", categoryWatchBeans);
        request.setAttribute("ForumWatchBeans", forumWatchBeans);
        request.setAttribute("ThreadWatchBeans", threadWatchBeans);
    }

    public void prepareAdd(GenericRequest request, GenericResponse response)
        throws DatabaseException, AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableWatch() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.cannot_add_watch.watch_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Watch because Watch feature is disabled by administrator.");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        if (MVNForumConfig.getRequireActivation() == true) {
            permission.ensureIsActivated();
        }

        CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
        CategoryTree tree = new CategoryTree(builder);
        CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response, true, "category", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
        tree.addCategeoryTreeListener(listener);
        request.setAttribute("Result", tree.build());
    }

    public void processAdd(GenericRequest request)
        throws BadInputException, CreateException, DatabaseException, ObjectNotFoundException, ForeignKeyNotFoundException, AuthenticationException {

        // This method must not check post method because it can be called from viewthread
        //SecurityUtil.checkHttpPostMethod(request);

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnableWatch() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.cannot_add_watch.watch_is_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot add Watch because Watch feature is disabled by administrator.");
        }
     
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        if (MVNForumConfig.getRequireActivation() == true) {
            permission.ensureIsActivated();
        }

        Timestamp now = DateUtil.getCurrentGMTTimestamp();

        int memberID = onlineUser.getMemberID();
        int categoryID              = 0;
        int forumID                 = 0;
        int threadID                = 0;
        int watchType               = GenericParamUtil.getParameterInt(request, "WatchType");
        int watchOption             = WatchBean.WATCH_OPTION_DEFAULT;//GenericParamUtil.getParameterInt(request, "WatchOption");
        int watchStatus             = 0;//GenericParamUtil.getParameterInt(request, "WatchStatus");
        Timestamp watchCreationDate = now;
        Timestamp watchLastSentDate = now;
        Timestamp watchEndDate      = now;// @todo: check it !!!

        int watchSelector = GenericParamUtil.getParameterInt(request, "WatchSelector");
        switch (watchSelector) {
            case WatchBean.SELECT_GLOBAL_WATCH:
                break;
            case WatchBean.SELECT_CATEGORY_WATCH:
                categoryID = GenericParamUtil.getParameterInt(request, "category");
                try {
                    DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.categoryid_not_exists", new Object[] {new Integer(categoryID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
                break;
            case WatchBean.SELECT_FORUM_WATCH:
                forumID = GenericParamUtil.getParameterInt(request, "forum");
                try {
                    ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.forumid_not_exists", new Object[] {new Integer(forumID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
                break;
            case WatchBean.SELECT_THREAD_WATCH:
                threadID = GenericParamUtil.getParameterInt(request, "thread");
                DAOFactory.getThreadDAO().findByPrimaryKey(threadID);
                break;
            default:
                // please do not localize this
                AssertionUtil.doAssert(false, "Cannot process WatchSelector = " + watchSelector);
        }
        
        try {
            DAOFactory.getWatchDAO().create(memberID, categoryID, forumID,
                                       threadID, watchType, watchOption,
                                       watchStatus, watchCreationDate, watchLastSentDate,
                                       watchEndDate);
        } catch (DuplicateKeyException ex) {
            // User try to create a duplicate watch, just ignore
            request.setAttribute("WatchExisted", "true");
        }
    }

    public void processDelete(GenericRequest request)
        throws BadInputException, DatabaseException, AuthenticationException, ObjectNotFoundException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int memberID = onlineUser.getMemberID();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int watchID = GenericParamUtil.getParameterInt(request, "watch");

        WatchBean watchBean = DAOFactory.getWatchDAO().getWatch(watchID);

        // check if the watch is owned by the current member
        if (watchBean.getMemberID() != memberID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_delete.watch_is_not_owned_by_current_member");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot delete watch: this watch is not owned by the current member.");
        }

        //now delete the watch
        DAOFactory.getWatchDAO().delete(watchID);
    }

    public void prepareEdit(GenericRequest request)
        throws BadInputException, ObjectNotFoundException, DatabaseException, AuthenticationException {

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        Locale locale = I18nUtil.getLocaleInRequest(request);
        int memberID = onlineUser.getMemberID();

        int watchID = GenericParamUtil.getParameterInt(request, "watch");

        WatchBean watchBean = DAOFactory.getWatchDAO().getWatch(watchID);

        if (watchBean.getMemberID() != memberID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_edit.watch_is_not_owned_by_current_member");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot edit watch: this watch is not owned by the current member.");
        }

        request.setAttribute("WatchBean", watchBean);
    }

    public void processEdit(GenericRequest request)
        throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException {

        SecurityUtil.checkHttpPostMethod(request);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();

        int memberID = onlineUser.getMemberID();

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // primary key column(s)
        int watchID = GenericParamUtil.getParameterInt(request, "WatchID");
        int watchType = GenericParamUtil.getParameterInt(request, "WatchType");

        WatchBean watchBean = DAOFactory.getWatchDAO().getWatch(watchID);

        if (watchBean.getMemberID() != memberID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_edit.watch_is_not_owned_by_current_member");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot edit watch: this watch is not owned by the current member.");
        }

        DAOFactory.getWatchDAO().updateWatchType(watchID, watchType);
    }
}
