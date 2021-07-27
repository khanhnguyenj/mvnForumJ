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
 *  
 */
package net.myvietnam.mvncore.thirdparty.aspirin.masukomi.aspirin.core;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.commons.threadpool.DefaultThreadPool;
import net.myvietnam.mvncore.thirdparty.aspirin.apache.commons.threadpool.ThreadPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.masukomi.tools.logging.Logs;
/**
 * DOCUMENT ME!
 * 
 * @author masukomi To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
class QueManager extends Thread {
	static private Logger log = LoggerFactory.getLogger(QueManager.class);
	/** DOCUMENT ME! */
	protected boolean terminateRun = false;
	/** DOCUMENT ME! */
	protected boolean running = false;
	protected boolean pauseNewSends = false;
	/** DOCUMENT ME! */
	ThreadPool threadPool = new DefaultThreadPool(Configuration.getInstance()
			.getDeliveryThreads());
	/**
	 * @return Returns true if the thread is running
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * Terminates the current run when the thread wakes again.
	 */
	public void terminateRun() {
		terminateRun = false;
	}
	public void pauseNewSends() {
		pauseNewSends = true;
	}
	public void unPauseNewSends() {
		pauseNewSends = false;
	}
	
	public boolean isPaused() {
		return pauseNewSends;
	}
	/**
	 * DOCUMENT ME!
	 */
	public void run() {
		running = true;
		while (true) {
			if (!terminateRun) {
				if (!pauseNewSends) {
					QuedItem qi = MailQue.getNextSendable();
					if (qi != null) {
						qi.setStatus(QuedItem.IN_PROCESS);
						try {
							if (log.isDebugEnabled()) {
								log.debug("About to create new RemoteDelivery");
							}
							RemoteDelivery rd = new RemoteDelivery(qi);
							threadPool.invokeLater(rd);
						} catch (Exception e) {
							if (log.isDebugEnabled()) {
								log
										.debug("failed while trying to call threadPool.invokeLater(Runnable)");
							}
							log.error(e.toString());
							qi.setStatus(QuedItem.IN_QUE);
						}
					} else {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							log.error(e.toString());
						}
					}
				} else { // we need to pause the sending of new messages
					// this is most likely so that a watcher can be added or removed
					try {
						sleep(500);
					} catch (InterruptedException e) {
						log.error(e.toString());
					}
				}
				// hang out until we're needed
			} else {
				break;
			}
		}
		running = false;
	}
}
