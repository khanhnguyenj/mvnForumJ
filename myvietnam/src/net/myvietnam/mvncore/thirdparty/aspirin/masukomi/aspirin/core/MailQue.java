/*
 * Created on Jan 3, 2004
 * 
 * Copyright (c) 2004 Katherine Rhodes (masukomi at masukomi dot org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.myvietnam.mvncore.thirdparty.aspirin.masukomi.aspirin.core;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import net.myvietnam.mvncore.thirdparty.aspirin.apache.james.core.MailImpl;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.mailet.Mail;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.mailet.MailAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.masukomi.tools.logging.Logs;
/**
 * @author masukomi
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MailQue {
    
	static private Logger log = LoggerFactory.getLogger(MailQue.class);
	
	protected QueManager qm;
	protected Vector que;
	protected Vector listeners;
	static MailQue mq;
	private Vector listenersToRemove;
	private Vector listenersToAdd;
	private int notificationCount;
	
	static synchronized public MailQue getInstance() {
		if (mq == null) {
			mq = new MailQue();
		}
		return mq;
	}
	
	private MailQue() {
		qm = new QueManager();
		que = new Vector();
		listeners = new Vector();
		listenersToRemove = new Vector();
		listenersToAdd = new Vector();
		notificationCount = 0;
	}
	
	static synchronized public void queMail(MimeMessage message) throws MessagingException {
		MailImpl mail = new MailImpl(message);
		service(mail, getInstance().getListeners());
		if (!getInstance().getQueManager().isRunning()) {
			getInstance().getQueManager().start();
		}
	}
	
	static protected void service(Mail genericmail, Collection watchers)
			throws AddressException {
		MailImpl mail = (MailImpl) genericmail;
		// Do I want to give the internal key, or the message's Message ID
		if (log.isDebugEnabled()) {
			log.debug("Remotely delivering mail " + mail.getName());
		}
		Collection recipients = mail.getRecipients();
		String gatewayServer;
		// Must first organize the recipients into distinct servers (name made
		// case insensitive)
		Hashtable targets = new Hashtable();
		for (Iterator i = recipients.iterator(); i.hasNext();) {
			MailAddress target = (MailAddress) i.next();
			String targetServer = target.getHost().toLowerCase(Locale.US);
			Collection temp = (Collection) targets.get(targetServer);
			if (temp == null) {
				temp = new Vector();
				targets.put(targetServer, temp);
			}
			temp.add(target);
		}
		//We have the recipients organized into distinct servers... put them
		// into the
		//delivery store organized like this... this is ultra inefficient I
		// think...
		// Store the new message containers, organized by server, in the que
		// mail repository
		String name = mail.getName();
		for (Iterator i = targets.keySet().iterator(); i.hasNext();) {
			String host = (String) i.next();
			Collection rec = (Collection) targets.get(host);
			if (log.isDebugEnabled()) {
				StringBuffer logMessageBuffer = new StringBuffer(128).append(
						"Sending mail to ").append(rec).append(" on host ")
						.append(host);
				log.debug(logMessageBuffer.toString());
			}
			MailImpl mailItem = (MailImpl) mail.duplicate();
			mailItem.setRecipients(rec);
			StringBuffer nameBuffer = new StringBuffer(128).append(name)
					.append("-to-").append(host);
			mailItem.setName(nameBuffer.toString());
			store(new QuedItem(mailItem));
			//Set it to try to deliver (in a separate thread) immediately
			// (triggered by storage)
		}
		mail.setState(Mail.GHOST);
	}
	
	static protected void store(QuedItem qi) {
		getInstance().getQue().add(qi);
		// try and send it
	}
	
	/**
	 * In addition to findig and returnign the next sendable item this method
	 * will remove any completed items from the que.
	 * 
	 * @return the next sendable item
	 */
	public synchronized static QuedItem getNextSendable() {
		Collections.sort(getInstance().getQue());
		Iterator it = getInstance().getQue().iterator();
		while (it.hasNext()) {
			QuedItem qi = (QuedItem) it.next();
			if (qi.isReadyToSend()) {
				return qi;
			}
		}
		// if we've made it this far there are no mails waiting to send
		// let's clean out the old mails.
		Vector que = getInstance().getQue();
		if (que.size() > 0) {
			for (int i = que.size() - 1; i > -1; i--) {
				QuedItem qi = (QuedItem) que.elementAt(i);
				if (qi.getStatus() == QuedItem.COMPLETED) {
					que.remove(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Occasionally a QuedItem will be dropped from the Que. This method will
	 * re-insert it.
	 * 
	 * @param item
	 */
	public static void reQue(QuedItem item) {
		if (getInstance().getQue().indexOf(item) == -1) {
			getInstance().getQue().add(item);
		}
	}
	
	QueManager getQueManager() {
		if (qm == null) {
			qm = new QueManager();
		}
		return qm;
	}
	
	public Vector getQue() {
		return que;
	}
	
	/**
	 * @deprecated use addWatcher(MailWatcher)
	 * @param watcher
	 */
	public static void addListener(MailWatcher watcher) {
		addWatcher(watcher);
	}
	
	public static void addWatcher(MailWatcher watcher) {
		if (!getInstance().isNotifying()) {
			getInstance().getListeners().add(watcher);
		} else {
			getInstance().getQueManager().pauseNewSends();
			getInstance().listenersToAdd.add(watcher);
		}
	}
	
	/**
	 * @deprecated use removeWatcher(MailWatcher)
	 * @param watcher
	 */
	public static void removeListener(MailWatcher watcher) {
		removeWatcher(watcher);
	}
	
	public static void removeWatcher(MailWatcher watcher) {
		if (!getInstance().isNotifying()) {
			getInstance().getListeners().remove(watcher);
		} else {
			getInstance().getQueManager().pauseNewSends();
			getInstance().listenersToRemove.add(watcher);
		}
	}
	
	public Vector getListeners() {
		return listeners;
	}
	
	public synchronized void incrementNotifiersCount() {
		notificationCount++;
	}
	
	/**
	 * decrements the number of notifiers currently happening and if there are
	 * none in progress it will add or remove watchers as appropriate
	 *  
	 */
	public synchronized void decrementNotifiersCount() {
		notificationCount--;
		if (notificationCount == 0) {
			Iterator removersIt = listenersToRemove.iterator();
			while (removersIt.hasNext()) {
				listeners.add(removersIt.next());
			}
			listenersToRemove.clear();
			Iterator addersIt = listenersToAdd.iterator();
			while (addersIt.hasNext()) {
				listeners.add(addersIt.next());
			}
			listenersToAdd.clear();
			getInstance().getQueManager().unPauseNewSends();
		}
	}
	
	/**
	 * @return Returns the notifying.
	 */
	public synchronized boolean isNotifying() {
		return notificationCount != 0;
	}
}
