/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/MessageWebHandler.java,v 1.119 2009/08/19 10:54:46 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.119 $
 * $Date: 2009/08/19 10:54:46 $
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

import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.auth.*;
import com.mvnforum.common.PrivateMessageUtil;
import com.mvnforum.db.*;

public class MessageWebHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageWebHandler.class);

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

    public MessageWebHandler() {
    }

    public void prepareAdd(GenericRequest request, GenericResponse response)
        throws DatabaseException, AuthenticationException,
        BadInputException, ObjectNotFoundException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        MyUtil.saveVNTyperMode(request, response);

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");
        boolean isForward    = GenericParamUtil.getParameterBoolean(request, "forward");

        int parentMessageID = 0;
        try {
            parentMessageID = GenericParamUtil.getParameterInt(request, "parent");
        } catch (Exception ex) {
            // do nothing
            // NOTE: we cannot return here since user can have a parameter parent = 0
        }

        if (parentMessageID != 0) { // Reply
            MessageBean parentMessageBean = null;
            try {
                parentMessageBean = DAOFactory.getMessageDAO().getMessage(parentMessageID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(parentMessageID)});
                throw new ObjectNotFoundException(localizedMessage);
            }
            if (parentMessageBean.getMemberID() != onlineUser.getMemberID()) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("This Private Message does not belong to you");
            }
            request.setAttribute("ParentMessageBean", parentMessageBean);

            // We prepare showing attached files (if any), it's used for Forward Method
            if (isForward) {
                //MessageBean should have AttachBeans value as default
                Collection attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(parentMessageID);
                request.setAttribute("AttachBeans", attachBeans);
            }
        }

        if (isPreviewing) {

            boolean sendAll = false;
            if (MVNForumConfig.getEnablePublicMessage() && permission.canAdminSystem()) {
                sendAll = GenericParamUtil.getParameterBoolean(request, "sendall");
            }

            int logonMemberID = onlineUser.getMemberID();

            String messageToList = GenericParamUtil.getParameterSafe(request, "MessageToList", !(sendAll));
            messageToList = messageToList.replace(',', ';');
            messageToList = DisableHtmlTagFilter.filter(messageToList);// always disable HTML

            String messageCcList = GenericParamUtil.getParameterSafe(request, "MessageCcList", false);
            messageCcList = messageCcList.replace(',', ';');
            messageCcList = DisableHtmlTagFilter.filter(messageCcList);// always disable HTML

            String messageBccList = GenericParamUtil.getParameterSafe(request, "MessageBccList", false);
            messageBccList = messageBccList.replace(',', ';');
            messageBccList = DisableHtmlTagFilter.filter(messageBccList);// always disable HTML

            GenericParamUtil.getParameter(request, "MessageTopic", true);
            GenericParamUtil.getParameter(request, "message", true);// use message instead of MessageBody

            MemberBean memberBean = null;
            try {
                memberBean = MemberCache.getInstance().getMember(logonMemberID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.memberid_not_exists", new Object[] {new Integer(logonMemberID)});
                throw new ObjectNotFoundException(localizedMessage);
            }

            String[] receivedMembers = StringUtil.getStringArrays(messageToList, messageCcList, messageBccList, ";");

            if (sendAll == false) {
                if (receivedMembers.length == 0) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_send_message.no_receivers");
                    throw new AssertionError(localizedMessage);
                }
                MyUtil.checkMembers(receivedMembers, locale);//check to make sure that members are existed
            }

            request.setAttribute("MemberBean", memberBean);
        }

    }

    public void processAdd(GenericRequest request, GenericResponse response)
        throws ObjectNotFoundException, DatabaseException, CreateException,
        BadInputException, ForeignKeyNotFoundException, AuthenticationException, InterceptorException, FloodException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        String currentIP = request.getRemoteAddr();
        try {
            FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_NEW_MESSAGE_PER_IP, currentIP);
        } catch (FloodException fe) {
            //throw new FloodException("You have reached the maximum number of the private message adding actions for this page. Please try this page later. This is to prevent forum from being flooded.");
            Integer maxMessages = new Integer(FloodControlHour.getActionsPerHour(MVNForumGlobal.FLOOD_ID_NEW_MESSAGE_PER_IP));
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.FloodException.send_message_too_many_times", new Object[] {maxMessages});
            throw new FloodException(localizedMessage);
        }

        MyUtil.saveVNTyperMode(request, response);

        int logonMemberID = onlineUser.getMemberID();
        String memberName = onlineUser.getMemberName();

        boolean sendAll = false;

        if (MVNForumConfig.getEnablePublicMessage() && permission.canAdminSystem()) {
            sendAll = GenericParamUtil.getParameterBoolean(request, "sendall");
        }

        // if messageToList == null permit null value
        String messageToList = GenericParamUtil.getParameterSafe(request, "MessageToList", !(sendAll));
        messageToList = messageToList.replace(',', ';');
        messageToList = DisableHtmlTagFilter.filter(messageToList);// always disable HTML

        String messageCcList = GenericParamUtil.getParameterSafe(request, "MessageCcList", false);
        messageCcList = messageCcList.replace(',', ';');
        messageCcList = DisableHtmlTagFilter.filter(messageCcList);// always disable HTML

        String messageBccList = GenericParamUtil.getParameterSafe(request, "MessageBccList", false);
        messageBccList = messageBccList.replace(',', ';');
        messageBccList = DisableHtmlTagFilter.filter(messageBccList);// always disable HTML

        String messageTopic = GenericParamUtil.getParameter(request, "MessageTopic", true);
        messageTopic = DisableHtmlTagFilter.filter(messageTopic);// always disable HTML
        messageTopic = InterceptorService.getInstance().validateContent(messageTopic);

        String messageBody  = GenericParamUtil.getParameter(request, "message", true);// use message instead of MessageBody
        messageBody = DisableHtmlTagFilter.filter(messageBody);// always disable HTML
        messageBody = InterceptorService.getInstance().validateContent(messageBody);

        String messageIcon = GenericParamUtil.getParameter(request, "MessageIcon");
        messageIcon = DisableHtmlTagFilter.filter(messageIcon);// always disable HTML

        if (sendAll) {
            messageToList  = onlineUser.getMemberName();
            messageBccList = "";
            messageCcList  = "";
        }

        Timestamp now               = DateUtil.getCurrentGMTTimestamp();
        int messageSenderID         = logonMemberID;
        String messageSenderName    = memberName;
        String folderName           = MVNForumConstant.MESSAGE_FOLDER_INBOX;
        // NOTE: in this step, type = quote is not possible since it needs attachment
        int messageType             = (sendAll) ? MessageBean.MESSAGE_TYPE_PUBLIC : MessageBean.MESSAGE_TYPE_DEFAULT;
        int messageOption           = 0;//GenericParamUtil.getParameterInt(request, "MessageOption");
        int messageStatus           = 0;//GenericParamUtil.getParameterInt(request, "MessageStatus");
        int messageReadStatus       = MessageBean.MESSAGE_READ_STATUS_DEFAULT;
        int messageNotify           = 0;//GenericParamUtil.getParameterInt(request, "MessageNotify");
        int messageAttachCount      = 0;
        String messageIP            = currentIP;
        Timestamp messageCreateDate = now; // Get Current time

        int parentMessageID = 0;

        boolean isDraft         = GenericParamUtil.getParameterBoolean(request, "draft");
        boolean isForward       = GenericParamUtil.getParameterBoolean(request, "forward");
        boolean attachMore      = GenericParamUtil.getParameterBoolean(request, "AttachMore");
        boolean addToSentFolder = GenericParamUtil.getParameterBoolean(request, "AddToSentFolder");

        String[] receivedMembers = StringUtil.getStringArrays(messageToList, messageCcList, messageBccList, ";");
        if (sendAll == false)  {
            if (receivedMembers.length == 0) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.AssertionError.cannot_send_message.no_receivers");
                throw new AssertionError(localizedMessage);
            }
        }

        Hashtable receivers = MyUtil.checkMembers(receivedMembers, locale);

        if (isForward) {
            parentMessageID = GenericParamUtil.getParameterInt(request, "parent");
        }

        Collection attachBeans = null;
        if (isForward) {
            MessageBean parentMessageBean = null;
            try {
                parentMessageBean = DAOFactory.getMessageDAO().getMessage(parentMessageID);
            } catch (ObjectNotFoundException e) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(parentMessageID)});
                throw new ObjectNotFoundException(localizedMessage);
            }

            //actually, the jsp does not allow forward a public message
            boolean isPublicMessage = false;
            if (MVNForumConfig.getEnablePublicMessage()) {
                isPublicMessage = (permission.canAdminSystem() && (parentMessageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC));
            }

            if ((parentMessageBean.getMemberID() != logonMemberID) && !isPublicMessage) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("This Private Message does not belong to you");
            }
            attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(parentMessageID);
            messageAttachCount = attachBeans.size();// in case forward, the attachcount could > 0 so we call this method
        } // end forward

        int maxPrivateMessage = MVNForumConfig.getMaxPrivateMessages();
        if (isDraft) { // Click to Draft Button
            folderName = MVNForumConstant.MESSAGE_FOLDER_DRAFT;
            // We will save message to folder Draft with memberID = SenderID
            // @todo: in next version:  when user click to draft message we will open
            //        AddMessage window and display all of its previous data

            int senderMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
            if (senderMessageCount >= maxPrivateMessage) {
                String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.over_private_message_quota", new Object[] {new Integer(maxPrivateMessage)});
                throw new BadInputException(localizedMessage);
                //throw new BadInputException("You cannot push the message into the draft folder. You have already use all your private message quota (" + maxFavorites + ").");
            }

            int draftMessageID = DAOFactory.getMessageDAO().create(folderName, messageSenderID, messageSenderID,
                                                messageSenderName, messageToList, messageCcList,
                                                messageBccList, messageTopic, messageBody,
                                                messageType, messageOption, messageStatus,
                                                messageReadStatus, messageNotify, messageIcon,
                                                messageAttachCount, messageIP, messageCreateDate);
            if (isForward) {
                // We must move PmAttachment from parent to new Message
                for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                    PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                    try {
                        DAOFactory.getPmAttachMessageDAO().create(draftMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                    } catch (DuplicateKeyException ex) {
                        // this should never happen
                        AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                    }
                }
            }// end of isForward

            attachMore = false;
        } else { // end draft
            // click to Send button
            // test the receiver's quota
            if (attachMore) {

                int senderMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
                if (senderMessageCount >= maxPrivateMessage) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.over_private_message_quota", new Object[] {new Integer(maxPrivateMessage)});
                    throw new BadInputException(localizedMessage);
                    //throw new BadInputException("You cannot push the message into the draft folder. You have already use all your private message quota (" + maxFavorites + ").");
                }

                folderName = MVNForumConstant.MESSAGE_FOLDER_DRAFT;// it means sending process is not finished
                // Create but save it as temporary message in Draft folder
                int messageID = DAOFactory.getMessageDAO().create(folderName, messageSenderID, messageSenderID,
                                                messageSenderName, messageToList, messageCcList,
                                                messageBccList, messageTopic, messageBody,
                                                messageType, messageOption, messageStatus,
                                                messageReadStatus, messageNotify, messageIcon,
                                                messageAttachCount, messageIP, messageCreateDate);
                if (isForward) {
                    // We must move PmAttachment from parent to new Message
                    for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                        PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                        try {
                            DAOFactory.getPmAttachMessageDAO().create(messageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                        } catch (DuplicateKeyException ex) {
                            // this should never happen
                            AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                        }
                    }
                }// end of isForward

                // When attach more, we must put attribute for MessageID
                request.setAttribute("MessageID", new Integer(messageID));
            } else { // NO Attachment
                // no attachment means we will send this message now
                StringBuffer overQuotaReceivers = new StringBuffer(128);
                for (Enumeration enumeration = receivers.keys(); enumeration.hasMoreElements(); ) {
                    int receivedMemberID = ((Integer)enumeration.nextElement()).intValue();
                    String receivedMemberName = (String)receivers.get(new Integer(receivedMemberID));

                    int receiverMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(receivedMemberID);
                    if (receiverMessageCount >= maxPrivateMessage) {
                        if (overQuotaReceivers.length() > 0) {
                            overQuotaReceivers.append(", ");
                        }
                        overQuotaReceivers.append(receivedMemberName);
                        continue;
                    }

                    // We need to get MessageID for attached page
                    int eachMessageID = DAOFactory.getMessageDAO().create(folderName, receivedMemberID, messageSenderID,
                                                        messageSenderName, messageToList, messageCcList,
                                                        messageBccList, messageTopic, messageBody,
                                                        messageType, messageOption, messageStatus,
                                                        messageReadStatus, messageNotify, messageIcon,
                                                        messageAttachCount, messageIP, messageCreateDate);

                    // Add to statistics
                    if (messageSenderID != receivedMemberID) {
                        DAOFactory.getMessageStatisticsDAO().create(messageSenderID, receivedMemberID, messageCreateDate,
                                                                    messageAttachCount, messageType, messageOption, messageStatus);
                    }

                    if (isForward) {
                        // We must move PmAttachment from parent to new Message
                        for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                            PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                            try {
                                DAOFactory.getPmAttachMessageDAO().create(eachMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                            } catch (DuplicateKeyException ex) {
                                // this should never happen
                                AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                            }
                        }
                    }// end of isForward
                } // end of for on received members
                request.setAttribute("OverQuotaReceivers", overQuotaReceivers.toString());

                // after that, we create another record to save it in Sent. (if user wants)
                if (addToSentFolder) {
                    int senderMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
                    if (senderMessageCount < maxPrivateMessage) {
                        folderName = MVNForumConstant.MESSAGE_FOLDER_SENT;
                        messageType = MessageBean.MESSAGE_TYPE_DEFAULT;// always a default type in the Sent folder
                        int sentMessageID = DAOFactory.getMessageDAO().create(folderName, messageSenderID, messageSenderID,
                                messageSenderName, messageToList, messageCcList,
                                messageBccList, messageTopic, messageBody,
                                messageType, messageOption, messageStatus,
                                messageReadStatus, messageNotify, messageIcon,
                                messageAttachCount, messageIP, messageCreateDate);
                        if (isForward) {
                            // We must move PmAttachment from parent to new Message
                            for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                                PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                                try {
                                    DAOFactory.getPmAttachMessageDAO().create(sentMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                                } catch (DuplicateKeyException ex) {
                                    // this should never happen
                                    AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                                }
                            }
                        }// end of isForward
                    } else {
                        request.setAttribute("AddSentFolderOverQuota", Boolean.TRUE);
                    }
                }// end of addToSentFolder
            }// else of No Attachment
        } // end of click to button Send

        request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
        request.setAttribute("AddToSentFolder", Boolean.valueOf(addToSentFolder));

        FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_NEW_MESSAGE_PER_IP, currentIP);
    }

    public void prepareViewMessage(GenericRequest request)
        throws ObjectNotFoundException, DatabaseException, BadInputException,
        AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int logonMemberID = onlineUser.getMemberID();

        int messageID = GenericParamUtil.getParameterInt(request, "message");

        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // Check if this message doesn't belong to current user
        boolean isPublicMessage = ((messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC) && MVNForumConfig.getEnablePublicMessage());
        if ((messageBean.getMemberID() != logonMemberID) && !isPublicMessage) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        MemberBean memberSenderBean = null;
        int messageSenderID = messageBean.getMessageSenderID();
        try {
            memberSenderBean = MemberCache.getInstance().getMember(messageSenderID);
        } catch (ObjectNotFoundException e) {
            // In this case, member has been deleted but his message is still there, so we just ignore
            // Note that currently the viewmessage.jsp accept null value of memberSenderBean
        }

        if (isPublicMessage) {
            DAOFactory.getMessageDAO().updateMessageReadStatus(messageID, messageBean.getMemberID(), MessageBean.MESSAGE_READ_STATUS_READ);
        } else {// private message
            DAOFactory.getMessageDAO().updateMessageReadStatus(messageID, logonMemberID, MessageBean.MESSAGE_READ_STATUS_READ);
        }

        // We get attachment of this message
        Collection attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(messageID);
        messageBean.setAttachmentBeans(attachBeans);

        // always update the number of new private message count in this case
        onlineUser.updateNewMessageCount(true);
        
        Collection messageFolderBeans = DAOFactory.getMessageFolderDAO().getMessageFolders_inMember(logonMemberID);

        request.setAttribute("MessageBean", messageBean);
        request.setAttribute("MemberSenderBean", memberSenderBean);
        request.setAttribute("MessageFolderBeans", messageFolderBeans);
    }

    public void prepareList(GenericRequest request)
        throws BadInputException, DatabaseException, ObjectNotFoundException,
        AuthenticationException {

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        // always update the number of new private message count in this case
        onlineUser.updateNewMessageCount(true);

        // @todo: We must check folder to make a correct list
        String folderName = GenericParamUtil.getParameter(request, "folder");
        if (folderName.length() == 0) {
            folderName = MVNForumConstant.MESSAGE_FOLDER_INBOX; // Default as Inbox
        }
        StringUtil.checkGoodName(folderName, locale);

        // for sort and order stuff
        String sort  = GenericParamUtil.getParameter(request, "sort");
        String order = GenericParamUtil.getParameter(request, "order");
        if (sort.length() == 0) {
            sort = "MessageCreationDate";
        }
        if (order.length()== 0) {
            order = "DESC";
        }

        //Process of Show Messages of login member
        int messagePerPage = onlineUser.getMessagesPerPage();
        int offset = 0;
        try {
            offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        } catch (BadInputException ex) {
            // do nothing
        }
        int logonMemberID = onlineUser.getMemberID();

        try {
            DAOFactory.getMessageFolderDAO().findByPrimaryKey(folderName, logonMemberID);
        } catch (ObjectNotFoundException onf) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messagefolder_not_exists", new Object[] {folderName});
            throw new ObjectNotFoundException(localizedMessage);
        }

        int numberOfMessages = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember_inFolder(logonMemberID, folderName);
        if (offset > numberOfMessages) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.offset_greater_than_total_rows");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("The offset is not allowed to be greater than total rows.");
        }

        Collection messageBeans = null;
        if (folderName.equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
            // also get the draft public messages
            messageBeans = DAOFactory.getMessageDAO().getAllMessages_inMember_inFolder_withSortSupport_limit(logonMemberID, folderName, offset, messagePerPage, sort, order);
        } else {
            messageBeans = DAOFactory.getMessageDAO().getNonPublicMessages_inMember_inFolder_withSortSupport_limit(logonMemberID, folderName, offset, messagePerPage, sort, order);
        }

        Collection messagePublicBeans = null;
        if (MVNForumConfig.getEnablePublicMessage()) {
            messagePublicBeans = DAOFactory.getMessageDAO().getPublicMessages();
        }

        // for folder table
        Collection messageFolderBeans = DAOFactory.getMessageFolderDAO().getMessageFolders_inMember(logonMemberID);
        for (Iterator iter = messageFolderBeans.iterator(); iter.hasNext(); ) {
            MessageFolderBean messageFolder = (MessageFolderBean) iter.next();
            int messageCount;
            int unreadMessageCount;
            if (messageFolder.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT)) {
                // also get the draft public messages
                messageCount = DAOFactory.getMessageDAO().getNumberOfAllMessages_inMember_inFolder(logonMemberID, messageFolder.getFolderName());
                unreadMessageCount = DAOFactory.getMessageDAO().getNumberOfUnreadAllMessages_inMember_inFolder(logonMemberID, messageFolder.getFolderName());
            } else {
                messageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember_inFolder(logonMemberID, messageFolder.getFolderName());
                unreadMessageCount = DAOFactory.getMessageDAO().getNumberOfUnreadNonPublicMessages_inMember_inFolder(logonMemberID, messageFolder.getFolderName());
            }
            messageFolder.setMessageCount(messageCount);
            messageFolder.setUnreadMessageCount(unreadMessageCount);
        } // end for on folders

        // for the quota bar
        int max = MVNForumConfig.getMaxPrivateMessages();
        int messageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(logonMemberID);
        double ratio = 0;
        if (max == 0) {
            ratio = 1.0;
        } else {
            ratio = (double)messageCount / max;
        }
        // for the quota bar

        request.setAttribute("QuotaRatio", new Double(ratio*100));
        request.setAttribute("MessagePublicBeans", messagePublicBeans);
        request.setAttribute("MessageFolderBeans", messageFolderBeans);
        request.setAttribute("MessageBeans", messageBeans);
        request.setAttribute("TotalMessages", new Integer(numberOfMessages));
    }

    public void processMessage(GenericRequest request)
        throws ObjectNotFoundException, DatabaseException, BadInputException,
        AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        String btnDelete = request.getParameter("btnDelete");
        String btnUnreadMark = request.getParameter("btnUnreadMark");
        String btnMoveFolder = request.getParameter("btnMoveFolder");

        int logonMemberID = onlineUser.getMemberID();
        int action = 0; // it's better than true-false, expand function later
        /*
         *  action = 1; // Delete action
         *  action = 2; // Mark unread action
         *  action = 3; // move to another folder
         */
        if ((btnDelete != null)) {
            action = 1;
        } else if (btnUnreadMark != null) {
            action = 2;
        } else if (btnMoveFolder != null) {
            action = 3;
        } else {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_specify_action");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot specify the action");
        }

        if (action == 1) {// delete
            //log.debug("ListDelete Message:" + btnDelete);
            String[] deleteList = request.getParameterValues("selectedmessage");
            for (int i = 0; (deleteList != null) && (i < deleteList.length); i++) {
                int messageID = Integer.parseInt(deleteList[i]);

                MessageBean messageBean = null;
                try {
                    messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }

                // Check if this message doesn't belong to current user
                if (messageBean.getMessageType() == MessageBean.MESSAGE_TYPE_PUBLIC) {
                    // this is a public message, only Admin can delete it
                    AssertionUtil.doAssert(permission.canAdminSystem(), "Only Admin can delete public message.");
                    PrivateMessageUtil.deleteMessageInDatabase(messageID, messageBean.getMemberID());
                } else {
                    // not a public message, only the owner can delete it
                    if (messageBean.getMemberID() != logonMemberID) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
                        throw new BadInputException(localizedMessage);
                        //throw new BadInputException("This Private Message does not belong to you");
                    }
                    PrivateMessageUtil.deleteMessageInDatabase(messageID, logonMemberID);
                }
            }
            request.setAttribute("Status", "Delete");
        } else if (action == 2 ) {// mark as unread
            //log.debug("Unread Message:" + btnUnreadMark);
            String[] unreadList = request.getParameterValues("selectedmessage");
            for (int i = 0; (unreadList != null) && (i < unreadList.length); i++) {
                int messageID = Integer.parseInt(unreadList[i]);
                MessageBean messageBean = null;
                try {
                    messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
                if (messageBean.getMessageType() != MessageBean.MESSAGE_TYPE_PUBLIC) {
                    // not a public message, only the owner can mark as unread it
                    if (messageBean.getMemberID() != logonMemberID) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
                        throw new BadInputException(localizedMessage);
                        //throw new BadInputException("This Private Message does not belong to you");
                    }
                    //log.debug("Unread MessageID = " + messageID);
                    DAOFactory.getMessageDAO().updateMessageReadStatus(messageID, logonMemberID, MessageBean.MESSAGE_READ_STATUS_DEFAULT);
                }
            }
            request.setAttribute("Status", "Unread");
        } else if (action == 3) { // move to another folder
            //log.debug("Move Message:" + btnMoveFolder);
            String[] movingList = request.getParameterValues("selectedmessage");
            String destinationFolder = GenericParamUtil.getParameterSafe(request, "DestinationFolder", true);
            StringUtil.checkGoodName(destinationFolder, locale);

            for (int i = 0; (movingList != null) && (i < movingList.length); i++) {
                int messageID = Integer.parseInt(movingList[i]);
                MessageBean messageBean = null;
                try {
                    messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
                } catch (ObjectNotFoundException e) {
                    String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
                    throw new ObjectNotFoundException(localizedMessage);
                }
                if (messageBean.getMessageType() != MessageBean.MESSAGE_TYPE_PUBLIC) {
                    // not a public message, only the owner can move it
                    if (messageBean.getMemberID() != logonMemberID) {
                        String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
                        throw new BadInputException(localizedMessage);
                        //throw new BadInputException("This Private Message does not belong to you");
                    }
                    //log.debug("Move MessageID = " + messageID);
                    DAOFactory.getMessageDAO().updateFolderName(messageID, logonMemberID, destinationFolder);
                }
            }
            request.setAttribute("Status", "MoveFolder");
        }
    }

    public void processSendMessage(GenericRequest request)
        throws ObjectNotFoundException, DatabaseException, BadInputException,
        CreateException, ForeignKeyNotFoundException, AuthenticationException {

        SecurityUtil.checkHttpPostMethod(request);

        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int logonMemberID = onlineUser.getMemberID();
        int messageID = GenericParamUtil.getParameterInt(request, "message");

        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // Check if this message doesn't belong to current user
        if (messageBean.getMemberID() != logonMemberID ) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        if (messageBean.getFolderName().equalsIgnoreCase(MVNForumConstant.MESSAGE_FOLDER_DRAFT) == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.cannot_add_attachment.pm_does_not_in_folder_draft");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("Cannot add attachment because this Private Message does not in the folder Draft");
        }

        // Check if the message is from this logon member
        AssertionUtil.doAssert(messageBean.getMessageSenderID() == logonMemberID, "Assertion: The MessageSenderID must equals the current logined user.");
        AssertionUtil.doAssert(messageBean.getMessageSenderName().equals(onlineUser.getMemberName()), "Assertion: The MessageSenderName must equals the current logined user.");

        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        Collection attachBeans = DAOFactory.getPmAttachmentDAO().getPmAttachments_inMessage(messageBean.getMessageID()); //messageBean is original message

        int maxPrivateMessage = MVNForumConfig.getMaxPrivateMessages();

        String[] receivedMembers = StringUtil.getStringArrays(messageBean.getMessageToList(), messageBean.getMessageCcList(), messageBean.getMessageBccList(), ";");
        Hashtable receivers = MyUtil.checkMembers(receivedMembers, locale);

        StringBuffer overQuotaReceivers = new StringBuffer(128);
        for (Enumeration enumeration = receivers.keys(); enumeration.hasMoreElements(); ) {
            int receivedMemberID = ((Integer)enumeration.nextElement()).intValue();
            String receivedMemberName = (String)receivers.get(new Integer(receivedMemberID));

            int receiverMessageCount = DAOFactory.getMessageDAO().getNumberOfNonPublicMessages_inMember(receivedMemberID);
            if (receiverMessageCount >= maxPrivateMessage) {
                if (overQuotaReceivers.length() > 0) {
                    overQuotaReceivers.append(", ");
                }
                overQuotaReceivers.append(receivedMemberName);
                continue;
            }

            // Create REAL message for receivers when finish. It means we have new messageID for each new receiver
            // We need to get MessageID for attached page
            int eachMessageID = DAOFactory.getMessageDAO().create(MVNForumConstant.MESSAGE_FOLDER_INBOX, receivedMemberID, logonMemberID,
                                                messageBean.getMessageSenderName(), messageBean.getMessageToList(), messageBean.getMessageCcList(),
                                                messageBean.getMessageBccList(), messageBean.getMessageTopic(), messageBean.getMessageBody(),
                                                messageBean.getMessageType(), messageBean.getMessageOption(), messageBean.getMessageStatus(),
                                                MessageBean.MESSAGE_READ_STATUS_DEFAULT, messageBean.getMessageNotify(), messageBean.getMessageIcon(),
                                                messageBean.getMessageAttachCount(), request.getRemoteAddr(), now);

            // Add to statistics
            if (logonMemberID != receivedMemberID) {
                DAOFactory.getMessageStatisticsDAO().create(logonMemberID, receivedMemberID, now,
                        messageBean.getMessageAttachCount(), messageBean.getMessageType(),
                        messageBean.getMessageOption(), messageBean.getMessageStatus());
            }

            // We must create a loop to create Attach for many receivers and many attachments
            for (Iterator attachIter = attachBeans.iterator(); attachIter.hasNext(); ) {
                PmAttachmentBean pmAttachBean = (PmAttachmentBean)attachIter.next();
                try {
                    DAOFactory.getPmAttachMessageDAO().create(eachMessageID, pmAttachBean.getPmAttachID(), 0/*type*/, 0/*option*/, 0/*status*/);
                } catch (DuplicateKeyException ex) {
                    // this should never happen
                    AssertionUtil.doAssert(false, "DuplicateKeyException when create PmAttachMessage");
                }
            }
        } // end of for on receivers
        request.setAttribute("OverQuotaReceivers", overQuotaReceivers.toString());

        // Now delete the message in the draft
        PrivateMessageUtil.deleteMessageInDatabase(messageID, logonMemberID);

        request.setAttribute("MessageID", new Integer(messageID));
        request.setAttribute("AttachMore", Boolean.FALSE);
        request.setAttribute("AddToSentFolder", Boolean.FALSE);
    }

    public void processDelete(GenericRequest request)
        throws ObjectNotFoundException, DatabaseException, BadInputException,
        AuthenticationException {

        SecurityUtil.checkHttpReferer(request);
        SecurityUtil.checkSecurityTokenMethod(request);
        
        Locale locale = I18nUtil.getLocaleInRequest(request);

        if (MVNForumConfig.getEnablePrivateMessage() == false) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "java.lang.IllegalStateException.private_message_disabled");
            throw new IllegalStateException(localizedMessage);
            //throw new IllegalStateException("Cannot operate with private messages. Because the private message feature is disabled");
        }

        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        permission.ensureIsAuthenticated();
        permission.ensureCanUseMessage();

        int logonMemberID = onlineUser.getMemberID();

        int messageID = GenericParamUtil.getParameterInt(request, "message");

        MessageBean messageBean = null;
        try {
            messageBean = DAOFactory.getMessageDAO().getMessage(messageID);
        } catch (ObjectNotFoundException e) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.ObjectNotFoundException.messageid_not_exists", new Object[] {new Integer(messageID)});
            throw new ObjectNotFoundException(localizedMessage);
        }

        // Check if this message doesn't belong to current user
        // Please note that we don't allow to delete public in this case
        if (messageBean.getMemberID() != logonMemberID) {
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.BadInputException.pm_not_belongs_to_you");
            throw new BadInputException(localizedMessage);
            //throw new BadInputException("This Private Message does not belong to you");
        }

        // Now delete the message
        PrivateMessageUtil.deleteMessageInDatabase(messageID, logonMemberID);

        request.setAttribute("FolderName", messageBean.getFolderName());
    }

}
