/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/MailUtil.java,v 1.73 2010/04/29 11:26:53 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.73 $
 * $Date: 2010/04/29 11:26:53 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package net.myvietnam.mvncore.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.MVNCoreConfig.MailConfig;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.thirdparty.aspirin.masukomi.aspirin.core.MailQue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MailUtil {

    public static final int MAX_MESSAGES_PER_TRANSPORT = 100;

    private static final Logger log = LoggerFactory.getLogger(MailUtil.class);

    private MailUtil() {// prevent instantiation
    }

    //private static MailOptions mailOption = new MailOptions();

    /**
     * Get the user name part of an email. Ex: input: test@yahoo.com => output: test
     * @param email String the email
     * @return String the user name part of an email
     */
    public static String getEmailUsername(String email) {
        if (email == null) {
            return "";
        }
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return "";
        }
        return email.substring(0, atIndex);
    }

    /**
     * Get the domain part of an email. Ex: input: test@yahoo.com => output: yahoo.com
     * @param email String the email
     * @return String the user name part of an email
     */
    public static String getEmailDomain(String email) {
        if (email == null) {
            return "";
        }
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return "";
        }
        return email.substring(atIndex + 1);
    }

    /**
     * @deprecated should use method checkGoodEmail(String input, Locale locale) 
     * @param input
     * @throws BadInputException if email is not good
     */
    public static void checkGoodEmail(String input) throws BadInputException {
        checkGoodEmail(input, null);
    }
    
    /**
     * Check if an email is good and safe or not.
     * This method should be use for all email input from user
     * @param input String
     * @param locale the locale for 
     * @throws BadInputException if email is not good
     */
    public static void checkGoodEmail(String input, Locale locale) throws BadInputException {
        
        if (input == null) {
            throw new BadInputException("Sorry, null string is not a good email.");//@todo : localize me
        }
        int atIndex = input.indexOf('@');
        int dotIndex = input.lastIndexOf('.');
        if ((atIndex == -1) || (dotIndex == -1) || (atIndex >= dotIndex)) {
            String localizedMessage = MVNCoreResourceBundle.getString(locale, "mvncore.exception.BadInputException.not_good_email", new Object[] {DisableHtmlTagFilter.filter(input)});
            throw new BadInputException(localizedMessage);
        }

        // now check for content of the string
        int length = input.length();
        char c;

        for (int i = 0; i < length; i++) {
            c = input.charAt(i);
            if ((c >= 'a') && (c <= 'z')) {
                // lower char
            } else if ((c >= 'A') && (c <= 'Z')) {
                // upper char
            } else if ((c >= '0') && (c <= '9')/* && (i != 0)*/) {
                // as of 31 Jan 2004, I (minhnn) relax the email checking
                // so that the email can start with an numeric char
                // hopefully it does not introduce a security bug
                // because this value will be inserted into SQL script

                // numeric char
            } else if ( ( (c=='_') || (c=='-') || (c=='.') || (c=='@') ) && (i != 0) ) {
                // _ char
            } else {
                // not good char, throw an BadInputException
                //@todo : localize me
                throw new BadInputException(input + " is not a valid email. Reason: character '" + c + "' is not accepted in an email.");
            }
        }// for

        // last check
        try {
            new javax.mail.internet.InternetAddress(input);
        } catch (Exception ex) {
            log.error("Error when running checkGoodEmail", ex);
            throw new BadInputException("Assertion: don't want to occur in Util.checkGoodEmail");
        }
    }

    /**
     * NOTE: param to, cc, bcc cannot be all empty. At least one must have a valid value
     * @param from : must be a valid email. However, if this param is null,
     *              then the default mail in config file will be use
     * @param to : can be null
     * @param cc : can be null
     * @param bcc: can be null
     * @param subject
     * @param message
     * @throws MessagingException
     * @throws BadInputException
     */
    public static void sendMail(String from, String to, String cc, String bcc, String subject, String message, boolean sendAsHTML)
        throws MessagingException, BadInputException, UnsupportedEncodingException {

        MailMessageStruct mailItem = new MailMessageStruct();
        mailItem.setFrom(from);
        mailItem.setTo(to);
        mailItem.setCc(cc);
        mailItem.setBcc(bcc);
        mailItem.setSubject(subject);
        mailItem.setMessage(message);
        mailItem.setSendAsHtml(sendAsHTML);

        sendMail(mailItem);
    }

    public static void sendMail(InternetAddress from, String to, String cc, String bcc, String subject, String message, boolean sendAsHTML)
        throws MessagingException, BadInputException, UnsupportedEncodingException {
    
        MailMessageStruct mailItem = new MailMessageStruct();
        mailItem.setFrom(from.getAddress());
        mailItem.setFromDisplayName(from.getPersonal());
        mailItem.setTo(to);
        mailItem.setCc(cc);
        mailItem.setBcc(bcc);
        mailItem.setSubject(subject);
        mailItem.setMessage(message);
        mailItem.setSendAsHtml(sendAsHTML);
    
        sendMail(mailItem);
    }

    public static void sendMail(MailMessageStruct mailItem)
        throws MessagingException, BadInputException, UnsupportedEncodingException {

        ArrayList mailList = new ArrayList(1);
        mailList.add(mailItem);
        try {
            sendMail(mailList, null);
        } catch (MessagingException mex) {
            log.error("MessagingException has occured.", mex);
            log.debug("MessagingException has occured. Detail info:");
            log.debug("from = " + mailItem.getFrom());
            log.debug("to = " + mailItem.getTo());
            log.debug("cc = " + mailItem.getCc());
            log.debug("bcc = " + mailItem.getBcc());
            log.debug("subject = " + mailItem.getSubject());
            log.debug("message = " + mailItem.getMessage());
            throw mex;// this may look redundant, but it is not :-)
        }
    }

    public static void sendMail(Collection mailStructCollection, Locale locale)
        throws MessagingException, BadInputException, UnsupportedEncodingException {

        Session session = null;
        Transport transport = null;
        int totalEmails = mailStructCollection.size();
        int count = 0;
        int sendFailedExceptionCount = 0;

        String server = "";
        String userName = "";
        String password = "";
        int port = 25;

        MailConfig config = MVNCoreConfig.getSendMailConfig();
        boolean useMailsource = config.useMailSource();
        boolean useEmbededMailServer = config.useEmbededSMTPMailServer();

        String smtp = "smtp";
        if (config.isUseSecureConnection()) {
            smtp = "smtps";
        }
        
        try {
            for (Iterator iter = mailStructCollection.iterator(); iter.hasNext(); ) {
                if ((transport == null) || (session == null)) {
                    if (useEmbededMailServer) {
                        session = Session.getDefaultInstance(new Properties());
                    } else if (useMailsource) {
                        try {
                            InitialContext ic = new InitialContext();
                            // mailSourceName = "java:comp/env/mail/mailSession";
                            String mailSourceName = config.getSourceName();
                            log.debug("MailUtil : use mailsource = " + mailSourceName);
                            if (mailSourceName.startsWith("mail:")) {
                                session = (Session) ic.lookup(mailSourceName);
                            } else {
                                session = (Session) ic.lookup("java:comp/env/" + mailSourceName);
                            }
                            transport = session.getTransport(smtp);
                        } catch (NamingException e) {
                            log.error("Cannot get Mail session", e);
                            throw new MessagingException("Cannot get the mail session from JNDI. Send mail failed.");
                        }
                    } else {// does not use datasourse
                        // TODO the variable props could be cached outside of the loop
                        Properties props = new Properties();

                        server = config.getMailServer();
                        port = config.getPort();
                        userName = config.getUserName();
                        password = config.getPassword();

                        // Local host name used in the SMTP HELO or EHLO command
                        try {
                            if (InetAddress.getLocalHost().getHostName() == null) {
                                props.put("mail." + smtp + ".localhost", server);
                            }
                        } catch (UnknownHostException e) {
                            props.put("mail." + smtp + ".localhost", server);
                        }
                        props.put("mail." + smtp + ".host", server);
                        props.put("mail." + smtp + ".port", String.valueOf(port));
                        if ((userName != null) && (userName.length() > 0)) {
                            props.put("mail." + smtp + ".auth", "true");
                        }
                        props.put("mail.debug", "true");
                        session = Session.getDefaultInstance(props, null);
                        transport = session.getTransport(smtp);
                    }// end of does not use datasource

                    if (useEmbededMailServer == false) {
                        if ((userName != null) && (userName.length() > 0)) {
                            transport.connect(server, userName, password);
                        } else {
                            transport.connect();
                        }
                    }
                }

                MailMessageStruct mailItem = (MailMessageStruct)iter.next();

                String from = mailItem.getFrom();
                String fromDisplayName = mailItem.getFromDisplayName();
                String to = mailItem.getTo();
                String cc = mailItem.getCc();
                String bcc = mailItem.getBcc();
                String subject = mailItem.getSubject();
                String message = mailItem.getMessage();

                if (from == null) {
                    from = MVNCoreConfig.getDefaultMailFrom();
                }

                try {
                    // this will also check for email error
                    checkGoodEmail(from, locale);
                    InternetAddress fromAddress = null;
                    if (fromDisplayName == null) {
                        fromAddress = new InternetAddress(from);
                    } else {
                        fromAddress = new InternetAddress(from, fromDisplayName);
                    }
                    InternetAddress[] toAddress = getInternetAddressEmails(to, locale);
                    InternetAddress[] ccAddress = getInternetAddressEmails(cc, locale);
                    InternetAddress[] bccAddress = getInternetAddressEmails(bcc, locale);
                    if ((toAddress == null) && (ccAddress == null) && (bccAddress == null)) {
                        //@todo : localize me
                        throw new BadInputException("Cannot send mail since all To, Cc, Bcc addresses are empty.");
                    }

                    // create a message
                    MimeMessage msg = new MimeMessage(session);
                    msg.setSentDate(new Date());
                    msg.setFrom(fromAddress);

                    if (toAddress != null) {
                        msg.setRecipients(Message.RecipientType.TO, toAddress);
                    }
                    if (ccAddress != null) {
                        msg.setRecipients(Message.RecipientType.CC, ccAddress);
                    }
                    if (bccAddress != null) {
                        msg.setRecipients(Message.RecipientType.BCC, bccAddress);
                    }
                    
                    if (mailItem.isSendAsHtml() == false) {
                        //This code is use to display unicode in Subject
                        //msg.setSubject(MimeUtility.encodeText(subject, "iso-8859-1", "Q"));
                        //msg.setText(message);
                        //String content = new String(message.getBytes(""), "UTF-8");
                        msg.setSubject(subject, "UTF-8");
                        msg.setText(message, "UTF-8");
                    } else {
                        //Below code is use for unicode
                        MimeBodyPart messageBodyPart = new MimeBodyPart();
                        msg.setSubject(subject, "UTF-8");
                        messageBodyPart.setText(message, "UTF-8");
                        messageBodyPart.setHeader("Content-Type", "text/html;charset=UTF-8");
                        messageBodyPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
                        MimeMultipart multipart = new MimeMultipart("alternative");
                        multipart.addBodyPart(messageBodyPart);
                        msg.setContent(multipart);
                    }
                    msg.saveChanges();

                    if (useEmbededMailServer) {
                        MailQue.queMail(msg);
                    } else {
                        transport.sendMessage(msg, msg.getAllRecipients());
                    }

                    if (useEmbededMailServer == false) {
                        // now check if sent 100 emails, then close connection (transport)
                        if ( ((count+1) % MAX_MESSAGES_PER_TRANSPORT) == 0 ) {
                            if (transport != null) {
                                try {
                                    transport.close();
                                } catch (MessagingException ex) {
                                    // ignore
                                }
                            }
                            transport = null;
                            session = null;
                        }
                    }

                } catch (SendFailedException ex) {
                    sendFailedExceptionCount++;
                    log.error("SendFailedException has occured.", ex);
                    log.warn("SendFailedException has occured. Detail info:");
                    log.warn("from = " + from);
                    log.warn("to = " + to);
                    log.warn("cc = " + cc);
                    log.warn("bcc = " + bcc);
                    log.warn("subject = " + subject);
                    log.info("message = " + message);
                    if ((totalEmails != 1) && (sendFailedExceptionCount > 10)) {
                        throw ex;// this may look redundant, but it is not :-)
                    }
                } catch (MessagingException mex) {
                    log.error("MessagingException has occured.", mex);
                    log.warn("MessagingException has occured. Detail info:");
                    log.warn("from = " + from);
                    log.warn("to = " + to);
                    log.warn("cc = " + cc);
                    log.warn("bcc = " + bcc);
                    log.warn("subject = " + subject);
                    log.info("message = " + message);
                    throw mex;// this may look redundant, but it is not :-)
                }

                // we increase count when send successfully
                count++;
            }//for
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException ex) {
                    // ignore
                }
            }
            
            if (totalEmails != 1) {
                String sendMailMethod = "";
                if (useEmbededMailServer) {
                    sendMailMethod = "Embeded SMTP Server";
                } else if (useMailsource) {
                    sendMailMethod = "Mail Source";
                } else {
                    sendMailMethod = "External SMTP Server";
                }
                log.info("sendMail: totalEmails = " + totalEmails + " sent count = " + count + " using " + sendMailMethod);
            }
        }
    }

    /**
     * This method trim the email variable, so if it contains only spaces,
     * then it will be empty string, then we have 0 token :-)
     * The returned value is never null
     * @param locale TODO
     */
    public static String[] getEmails(String email, Locale locale) throws BadInputException {
        if (email == null) {
            email = "";
        }
        email = email.trim();// very important
        email = email.replace(',', ';');// replace all occurrence of ',' to ';'
        StringTokenizer t = new StringTokenizer(email, ";");
        String[] ret = new String[t.countTokens()];
        int index = 0;
        while(t.hasMoreTokens()) {
            String mail = t.nextToken().trim();
            checkGoodEmail(mail, locale);
            ret[index] = mail;
            //log.debug(ret[index]);
            index++;
        }
        return ret;
    }

    public static String[] getEmails(String to, String cc, String bcc) throws BadInputException {
        String[] toMail = getEmails(to, null);
        String[] ccMail = getEmails(cc, null);
        String[] bccMail= getEmails(bcc, null);
        String[] ret = new String[toMail.length + ccMail.length + bccMail.length];
        int index = 0;
        for (int i = 0; i < toMail.length; i++) {
            ret[index] = toMail[i];
            index++;
        }
        for (int i = 0; i < ccMail.length; i++) {
            ret[index] = ccMail[i];
            index++;
        }
        for (int i = 0; i < bccMail.length; i++) {
            ret[index] = bccMail[i];
            index++;
        }
        return ret;
    }

    /**
     * This method will return null if there is not any email
     *
     * @param email
     * @param locale TODO
     * @return
     * @throws BadInputException
     * @throws AddressException
     */
    private static InternetAddress[] getInternetAddressEmails(String email, Locale locale)
        throws BadInputException, AddressException {
        String[] mails = getEmails(email, locale);
        if (mails.length == 0) {
            return null;// must return null, not empty array
        }

        //log.debug("to = " + mails);
        InternetAddress[] address = new InternetAddress[mails.length];
        for (int i = 0; i < mails.length; i++) {
            address[i] = new InternetAddress(mails[i]);
            //log.debug("to each element = " + mails[i]);
        }
        return address;
    }

    /**
     * Return an array of MailMessageStruct that use the config in file mvncore.xml.
     * Delegate to method receiveEmail with 5 parameters
     * @return
     */
    public static MailMessageStruct[] receiveEmail() {
        
        MailConfig config = MVNCoreConfig.getReceiveMailConfig();
        return receiveEmail(config.getMailServer(), config.getPort(), config.isUseSecureConnection(), config.getUserName(), config.getPassword());
    }

    public static MailMessageStruct[] receiveEmail(String serverHost, int port, boolean isSecure, String username, String password) {

        Store store = null;
        Folder folder = null;
        MailMessageStruct[] mailMessageStructsArray = null;
        String provider = "pop3";
        if (isSecure) {
            provider = "pop3s";
        }

        try {
            // -- Get hold of the default session --
            Properties props = new Properties();
            props.put("mail.pop3.port", String.valueOf(port));
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            // -- Get hold of a POP3 message store, and connect to it --
            store = session.getStore(provider);
            store.connect(serverHost, username, password);

            // -- Try to get hold of the default folder --
            folder = store.getDefaultFolder();
            if (folder == null) {
                throw new IOException("No default folder");
            }

            // -- ...and its INBOX --
            folder = folder.getFolder("INBOX");
            if (folder == null) {
                throw new IOException("No POP3 INBOX");
            }

            // -- Open the folder for read write --
            folder.open(Folder.READ_WRITE);

            // -- Get the message wrappers and process them --
            Message[] msgs = folder.getMessages();
            mailMessageStructsArray = new MailMessageStruct [msgs.length];
            for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
                mailMessageStructsArray[msgNum] = createMessageStructFromMessage(msgs[msgNum]);
                msgs[msgNum].setFlag(Flag.DELETED, true);
            }

            return mailMessageStructsArray;
        } catch (Exception ex) {
            log.error("Error when receiving email.", ex);
        } finally {
            if (folder != null) {
                try {
                    folder.close(true);
                } catch (MessagingException e) {
                    log.error("Cannot close POP Folder.", e);
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.error("Cannot close POP Store.", e);
                }
            }
        }
        return new MailMessageStruct[0];
    }

    private static MailMessageStruct createMessageStructFromMessage(Message message)
        throws MessagingException, IOException {

        MailMessageStruct mailMessageStruct = new MailMessageStruct();
        String fromEmailString = null;
        Address[] from = message.getFrom();
        if (from != null) {
            Address fromEmail = from[0];
            fromEmailString = ((InternetAddress)fromEmail).getAddress();
        }
        
        String subject = message.getSubject();

        Part messagePart = message;
        Object content = messagePart.getContent();

        // -- or its first body part if it is a multipart message --
        if (content instanceof Multipart) {
            messagePart = ((Multipart)content).getBodyPart(0);
        }

        // -- Get the content type --
        String contentType = (String)messagePart.getContent();
        
        mailMessageStruct.setFrom(fromEmailString);
        mailMessageStruct.setSubject(subject);
        mailMessageStruct.setMessage(contentType);

        return mailMessageStruct;
    }
}
