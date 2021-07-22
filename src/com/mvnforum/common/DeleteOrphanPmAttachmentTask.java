/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/DeleteOrphanPmAttachmentTask.java,v 1.9 2009/01/13 17:46:01 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/01/13 17:46:01 $
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

import java.util.Date;
import java.util.TimerTask;

import net.myvietnam.mvncore.util.TimerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.user.PmAttachmentWebHandler;

public final class DeleteOrphanPmAttachmentTask extends TimerTask {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(DeleteOrphanPmAttachmentTask.class);

    // static variable
    private static DeleteOrphanPmAttachmentTask instance = null;

    // instance variables
    private boolean scheduled = false;
    private int count = 0;
    private PmAttachmentWebHandler pmAttachmentWebHandler = new PmAttachmentWebHandler();

    // private constructor will prevent any instantiation
    private DeleteOrphanPmAttachmentTask() {
    }

    /**
     * This static method is used to get the Singleton instance of DeleteOrphanPmAttachmentTask
     * @return the singleton instance of DeleteOrphanPmAttachmentTask
     */
    public static synchronized DeleteOrphanPmAttachmentTask getInstance() {
        if (instance == null) {
            instance = new DeleteOrphanPmAttachmentTask();
        }
        return instance;
    }

    public synchronized void schedule(Date firstTime, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, firstTime, period);
        }
    }

    public synchronized void schedule(Date time) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, time);
        }
    }

    public synchronized void schedule(long delay) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay);
        }
    }

    public synchronized void schedule(long delay, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay, period);
        }
    }

    public synchronized void scheduleAtFixedRate(Date firstTime, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, firstTime, period);
        }
    }

    public synchronized void scheduleAtFixedRate(long delay, long period) {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, delay, period);
        }
    }

    public void run() {
        count++;
        long start = System.currentTimeMillis();
        try {
            log.debug("Begin calling deleteOrphanPmAttachment in DeleteOrphanPmAttachmentTask");
            pmAttachmentWebHandler.deleteOrphanPmAttachment();
        } catch (Exception ex) {
            log.error("Cannot process deleteOrphanPmAttachment in DeleteOrphanPmAttachmentTask", ex);
        } finally{
            long duration = System.currentTimeMillis() - start;
            log.debug("DeleteOrphanPmAttachmentTask:run() process " + count + " times, took " + duration + " ms");
        }
    }
}
