/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/WatchSendTask.java,v 1.9 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/01/02 15:12:54 $
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
package com.mvnforum.common;

import net.myvietnam.mvncore.util.TimerTaskAbstract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.user.WatchWebHandler;

public final class WatchSendTask extends TimerTaskAbstract {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(WatchSendTask.class);

    // static variable
    private static WatchSendTask instance = null;

    // instance variables
    private int count = 0;
    private WatchWebHandler watchWebHandler = new WatchWebHandler();

    // private constructor will prevent any instantiation
    private WatchSendTask() {
    }

    /**
     * This static method is used to get the Singleton instance of WatchTask
     * @return the singleton instance of WatchTask
     */
    public static synchronized WatchSendTask getInstance() {
        if (instance == null) {
            instance = new WatchSendTask();
        }
        return instance;
    }

    public void run() {
        count++;
        long start = System.currentTimeMillis();
        try {
            log.debug("Begin sendMail in WatchSendTask");
            watchWebHandler.sendMail();
        } catch (Throwable ex) {
            log.error("Cannot process sendMail in WatchSendTask", ex);
        } finally{
            long duration = System.currentTimeMillis() - start;
            log.info("WatchSendTask:sendMail process " + count + " times, took " + duration + " ms");
        }
    }
}
