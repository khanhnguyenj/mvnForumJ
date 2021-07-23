/*
 * ==================================================================== The
 * Apache Software License, Version 1.1
 * 
 * Copyright (c) 2000-2003 The Apache Software Foundation. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 * include the following acknowledgment: "This product includes software
 * developed by the Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself, if and
 * wherever such third-party acknowledgments normally appear.
 * 
 * 4. The names "Apache", "Jakarta", "JAMES" and "Apache Software Foundation"
 * must not be used to endorse or promote products derived from this software
 * without prior written permission. For written permission, please contact
 * apache@apache.org.
 * 
 * 5. Products derived from this software may not be called "Apache", nor may
 * "Apache" appear in their name, without prior written permission of the Apache
 * Software Foundation.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE APACHE
 * SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 * 
 * This software consists of voluntary contributions made by many individuals on
 * behalf of the Apache Software Foundation. For more information on the Apache
 * Software Foundation, please see <http://www.apache.org/>.
 * 
 * Portions of this software are based upon public domain software originally
 * written at the National Center for Supercomputing Applications, University of
 * Illinois, Urbana-Champaign.
 * 
 *  
 */
/**
 * This class does the actual work of delivering the mail to the intended
 * recepient. It is the class of the same name from James with some
 * modifications.
 * 
 * @author kate rhodes masukomi at masukomi dot org
 * 
 *  
 */
//TODO make retries be based on recepients not QuedItems
package net.myvietnam.mvncore.thirdparty.aspirin.masukomi.aspirin.core;
//import org.apache.avalon.framework.component.ComponentException;
//import org.apache.avalon.framework.component.ComponentManager;
//import org.apache.avalon.framework.configuration.DefaultConfiguration;
//import org.apache.james.Constants;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import javax.mail.*;
import javax.mail.Address;
import javax.mail.internet.*;

import net.myvietnam.mvncore.thirdparty.aspirin.apache.james.core.MailImpl;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.mailet.Mail;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.mailet.MailAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.*;
/**
 * Heavily leverages the RemoteDelivery class from James
 */
public class RemoteDelivery implements Runnable {
	static private Logger log = LoggerFactory.getLogger(RemoteDelivery.class);
	protected QuedItem qi;
	private static final String SMTPScheme = "smtp://";
	public RemoteDelivery(QuedItem qi) {
		this.qi = qi;
	}
	/**
	 * We can assume that the recipients of this message are all going to the
	 * same mail server. We will now rely on the JNDI to do DNS MX record lookup
	 * and try to deliver to the multiple mail servers. If it fails, it should
	 * throw an exception.
	 * 
	 * Creation date: (2/24/00 11:25:00 PM)
	 * 
	 * @param mail
	 *            org.apache.james.core.MailImpl
	 * @param session
	 *            javax.mail.Session
	 * @return boolean Whether the delivery was successful and the message can
	 *         be deleted
	 */
	private boolean deliver(QuedItem qi, Session session) {
		MailAddress rcpt = null;
		try {
			if (log.isDebugEnabled()) {
				log
						.debug("entering RemoteDelivery.deliver(QuedItem qi, Session session)");
			}
			MailImpl mail = (MailImpl) qi.getMail();
			MimeMessage message = mail.getMessage();
			//Create an array of the recipients as InternetAddress objects
			Collection recipients = mail.getRecipients();
			InternetAddress addr[] = new InternetAddress[recipients.size()];
			int j = 0;
			// funky ass look because you can't getElementAt() in a Collection
			for (Iterator i = recipients.iterator(); i.hasNext(); j++) {
				MailAddress currentRcpt = (MailAddress) i.next();
				addr[j] = currentRcpt.toInternetAddress();
			}
			if (addr.length <= 0) {
				if (log.isDebugEnabled()) {
					log.debug("No recipients specified... returning");
				}
				return true;
			}
			//Figure out which servers to try to send to. This collection
			//  will hold all the possible target servers
			Collection targetServers = null;
			Iterator it = recipients.iterator();
			while (it.hasNext()) {
				rcpt = (MailAddress) recipients.iterator().next();
				if (!qi.recepientHasBeenHandled(rcpt)) {
					break;
				}
			}
			// theoretically it is possible to not hav eone that hasn't been
			// handled
			// however that's only if something has gone really wrong.
			if (rcpt != null) {
				String host = rcpt.getHost();
				//Lookup the possible targets
				try {
					//targetServers = MXLookup.urlsForHost(host); // farking unreliable jndi bs
					targetServers = getMXRecordsForHost(host);
				} catch (Exception e) {
					log.error(e.toString());
				}
				if (targetServers == null || targetServers.size() == 0) {
					log.warn("No mail server found for: " + host);
					StringBuffer exceptionBuffer = new StringBuffer(128)
							.append(
									"I found no MX record entries for the hostname ")
							.append(host)
							.append(
									".  I cannot determine where to send this message.");
					return failMessage(qi, rcpt, new MessagingException(
							exceptionBuffer.toString()), true);
				} else if (log.isTraceEnabled()) {
					log.trace(targetServers.size() + " servers found for "
							+ host);
				}
				MessagingException lastError = null;
				Iterator i = targetServers.iterator();
				while (i.hasNext()) {
					try {
						URLName outgoingMailServer = (URLName) i.next();
						StringBuffer logMessageBuffer = new StringBuffer(256)
								.append("Attempting delivery of ").append(
										mail.getName()).append(" to host ")
								.append(outgoingMailServer.toString()).append(
										" to addresses ").append(
										Arrays.asList(addr));
						if (log.isDebugEnabled()) {
							log.debug(logMessageBuffer.toString());
						}
						//URLName urlname = new URLName("smtp://"
						//		+ outgoingMailServer);
						Properties props = session.getProperties();
						if (mail.getSender() == null) {
							props.put("mail.smtp.from", "<>");
						} else {
							String sender = mail.getSender().toString();
							props.put("mail.smtp.from", sender);
						}
						//Many of these properties are only in later JavaMail
						// versions
						//"mail.smtp.ehlo" //default true
						//"mail.smtp.auth" //default false
						//"mail.smtp.dsn.ret" //default to nothing... appended
						// as
						// RET= after MAIL FROM line.
						//"mail.smtp.dsn.notify" //default to
						// nothing...appended as
						// NOTIFY= after RCPT TO line.
						Transport transport = null;
						try {
							transport = session
									.getTransport(outgoingMailServer);
							try {
								transport.connect();
							} catch (MessagingException me) {
								log.error(me.toString());
								// Any error on connect should cause the mailet
								// to
								// attempt
								// to connect to the next SMTP server associated
								// with this MX record,
								// assuming the number of retries hasn't been
								// exceeded.
								if (failMessage(qi, rcpt, me, false)) {
									return true;
								} else {
									continue;
								}
							}
							transport.sendMessage(message, addr);
							//log.debug("message sent to " +addr);
						} finally {
							if (transport != null) {
								transport.close();
								transport = null;
							}
						}
						logMessageBuffer = new StringBuffer(256).append(
								"Mail (").append(mail.getName()).append(
								") sent successfully to ").append(
								outgoingMailServer);
						log.debug(logMessageBuffer.toString());
						qi.succeededForRecipient(rcpt);
						return true;
					} catch (MessagingException me) {
						log.error(me.toString(), me);
						//MessagingException are horribly difficult to figure
						// out
						// what actually happened.
						StringBuffer exceptionBuffer = new StringBuffer(256)
								.append("Exception delivering message (")
								.append(mail.getName()).append(") - ").append(
										me.getMessage());
						log.warn(exceptionBuffer.toString());
						if ((me.getNextException() != null)
								&& (me.getNextException() instanceof java.io.IOException)) {
							//This is more than likely a temporary failure
							// If it's an IO exception with no nested exception,
							// it's probably
							// some socket or weird I/O related problem.
							lastError = me;
							continue;
						}
						// This was not a connection or I/O error particular to
						// one
						// SMTP server of an MX set. Instead, it is almost
						// certainly
						// a protocol level error. In this case we assume that
						// this
						// is an error we'd encounter with any of the SMTP
						// servers
						// associated with this MX record, and we pass the
						// exception
						// to the code in the outer block that determines its
						// severity.
						throw me;
					} // end catch
				} // end while
				//If we encountered an exception while looping through,
				//throw the last MessagingException we caught. We only
				//do this if we were unable to send the message to any
				//server. If sending eventually succeeded, we exit
				//deliver() though the return at the end of the try
				//block.
				if (lastError != null) {
					throw lastError;
				}
			} // END if (rcpt != null)
			else {
				log.error("unable to find recipient that handn't already been handled");
			}
		} catch (SendFailedException sfe) {
			log.error(sfe.toString());
			boolean deleteMessage = false;
			Collection recipients = qi.getMail().getRecipients();
			//Would like to log all the types of email addresses
			if (log.isDebugEnabled()) {
				log.debug("Recipients: " + recipients);
			}
			/*
			 * The rest of the recipients failed for one reason or another.
			 * 
			 * SendFailedException actually handles this for us. For example, if
			 * you send a message that has multiple invalid addresses, you'll
			 * get a top-level SendFailedException that that has the valid,
			 * valid-unsent, and invalid address lists, with all of the server
			 * response messages will be contained within the nested exceptions.
			 * [Note: the content of the nested exceptions is implementation
			 * dependent.]
			 * 
			 * sfe.getInvalidAddresses() should be considered permanent.
			 * sfe.getValidUnsentAddresses() should be considered temporary.
			 * 
			 * JavaMail v1.3 properly populates those collections based upon the
			 * 4xx and 5xx response codes.
			 *  
			 */
			if (sfe.getInvalidAddresses() != null) {
				Address[] address = sfe.getInvalidAddresses();
				if (address.length > 0) {
					recipients.clear();
					for (int i = 0; i < address.length; i++) {
						try {
							recipients.add(new MailAddress(address[i]
									.toString()));
						} catch (ParseException pe) {
							// this should never happen ... we should have
							// caught malformed addresses long before we
							// got to this code.
							if (log.isDebugEnabled()) {
								log.debug("Can't parse invalid address: "
										+ pe.getMessage());
							}
						}
					}
					if (log.isDebugEnabled()) {
						log.debug("Invalid recipients: " + recipients);
					}
					deleteMessage = failMessage(qi, rcpt, sfe, true);
				}
			}
			if (sfe.getValidUnsentAddresses() != null) {
				Address[] address = sfe.getValidUnsentAddresses();
				if (address.length > 0) {
					recipients.clear();
					for (int i = 0; i < address.length; i++) {
						try {
							recipients.add(new MailAddress(address[i]
									.toString()));
						} catch (ParseException pe) {
							// this should never happen ... we should have
							// caught malformed addresses long before we
							// got to this code.
							if (log.isDebugEnabled()) {
								log.debug("Can't parse unsent address: "
										+ pe.getMessage());
							}
							pe.printStackTrace();
						}
					}
					if (log.isDebugEnabled()) {
						log.debug("Unsent recipients: " + recipients);
					}
					deleteMessage = failMessage(qi, rcpt, sfe, false);
				}
			}
			return deleteMessage;
		} catch (MessagingException ex) {
			log.error(ex.toString());
			// We should do a better job checking this... if the failure is a
			// general
			// connect exception, this is less descriptive than more specific
			// SMTP command
			// failure... have to lookup and see what are the various Exception
			// possibilities
			// Unable to deliver message after numerous tries... fail
			// accordingly
			// We check whether this is a 5xx error message, which
			// indicates a permanent failure (like account doesn't exist
			// or mailbox is full or domain is setup wrong).
			// We fail permanently if this was a 5xx error
			return failMessage(qi, rcpt, ex, ('5' == ex.getMessage().charAt(0)));
		} catch (Throwable t) {
			log.error(t.toString());
		}
		/*
		 * If we get here, we've exhausted the loop of servers without sending
		 * the message or throwing an exception. One case where this might
		 * happen is if we get a MessagingException on each transport.connect(),
		 * e.g., if there is only one server and we get a connect exception.
		 * Return FALSE to keep run() from deleting the message.
		 */
		return false;
	}
	/**
	 * Insert the method's description here. Creation date: (2/25/00 1:14:18 AM)
	 * 
	 * @param mail
	 *            org.apache.james.core.MailImpl
	 * @param exception
	 *            java.lang.Exception
	 * @param boolean
	 *            permanent
	 * @return boolean Whether the message failed fully and can be deleted
	 */
	private boolean failMessage(QuedItem qi, MailAddress recepient,
			MessagingException ex, boolean permanent) {
		log
				.debug("entering failMessage(QuedItem qi, MessagingException ex, boolean permanent)");
		//weird printy bits inherited from JAMES
		MailImpl mail = (MailImpl) qi.getMail();
		StringWriter sout = new StringWriter();
		PrintWriter out = new PrintWriter(sout, true);
		if (permanent) {
			out.print("Permanent");
		} else {
			out.print("Temporary");
		}
		StringBuffer logBuffer = new StringBuffer(64).append(
				" exception delivering mail (").append(mail.getName()).append(
				": ");
		out.print(logBuffer.toString());
		ex.printStackTrace(out);
		if (log.isWarnEnabled()) {
			log.warn(sout.toString());
		}
		////////////////
		/// It is important to note that deliver will pass us a mail with a
		// modified
		/// list of recepients non permanent ones will only have valid
		// recepients left
		///
		if (!permanent) {
			if (!mail.getState().equals(Mail.ERROR)) {
				mail.setState(Mail.ERROR);
				mail.setErrorMessage("0");
				mail.setLastUpdated(new Date());
			}
			if (qi.retryable(recepient)) {
				logBuffer = new StringBuffer(128).append("Storing message ")
						.append(mail.getName()).append(" into que after ")
						.append(qi.getNumAttempts()).append(" attempts");
				if (log.isDebugEnabled()) {
					log.debug(logBuffer.toString());
				}
				qi.retry(recepient);
				mail.setErrorMessage(qi.getNumAttempts() + "");
				mail.setLastUpdated(new Date());
				return false;
			} else {
				logBuffer = new StringBuffer(128).append("Bouncing message ")
						.append(mail.getName()).append(" after ").append(
								qi.getNumAttempts()).append(" attempts");
				if (log.isDebugEnabled()) {
					log.debug(logBuffer.toString());
				}
				qi.failForRecipient(recepient);
			}
		} else {
			qi.failForRecipient(recepient);
		}
		try {
			Bouncer.bounce(mail, ex.toString(), Configuration.getInstance()
					.getPostmaster());
		} catch (MessagingException me) {
			log.debug("failed to bounce");
			log.error(me.toString());
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			Session session = Session.getInstance(System.getProperties(), null);
			deliver(qi, session);
		} catch (Exception e) {
			log.error(e.toString());
			throw new RuntimeException(e);
		}
	}
	
	public Collection getMXRecordsForHost(String hostName){

		Vector recordsColl = null;
		try {
			Record [] records = new Lookup(hostName, Type.MX).run();
			recordsColl = new Vector(records.length);
			for (int i = 0; i < records.length; i++) {
			        MXRecord mx = (MXRecord) records[i];
			        String targetString = mx.getTarget().toString();
			        URLName uName = new URLName(RemoteDelivery.SMTPScheme + targetString.substring(0, targetString.length() -1));
			        recordsColl.add(uName);
			        //System.out.println("Host " + uName.getHost() + " has preference " + mx.getPriority());
			}
		} catch (TextParseException e) {
			log.warn(e.toString());
		}
		return recordsColl;

	}
	
}
