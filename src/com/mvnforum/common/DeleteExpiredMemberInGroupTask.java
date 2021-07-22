/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/DeleteExpiredMemberInGroupTask.java,v 1.4 2009/01/13 17:46:01 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
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

import com.mvnforum.admin.MemberGroupWebHandler;

public final class DeleteExpiredMemberInGroupTask extends TimerTask {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(DeleteExpiredMemberInGroupTask.class);

    // static variable
    private static DeleteExpiredMemberInGroupTask instance = null;

    // instance variables
    private boolean scheduled = false;
    private int count = 0;
    private MemberGroupWebHandler membergroupWebHandler = new MemberGroupWebHandler();

   /* // private constructor will prevent any instantiation
    private DeleteOrphanMemberGroupTask() {
    }
*/
    /**
     * This static method is used to get the Singleton instance of DeleteOrphanMemberGroupTask
     * @return the singleton instance of DeleteOrphanMemberGroupTask
     */
    public static synchronized DeleteExpiredMemberInGroupTask getInstance() {
        if (instance == null) {
            instance = new DeleteExpiredMemberInGroupTask();
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
            log.debug("Begin calling deleteExpiredMembers in DeleteExpiredMemberInGroupTask");
            membergroupWebHandler.deleteExpiredMembers();
        } catch (Exception ex) {
            log.error("Cannot process run() in DeleteExpiredMemberInGroupTask", ex);
        } finally{
            long duration = System.currentTimeMillis() - start;
            log.debug("DeleteExpiredMemberInGroupTask:run() process " + count + " times, took " + duration + " ms");
        }
    }
   
}
