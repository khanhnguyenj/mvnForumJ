/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/ThreadPollBean.java,v 1.2 2007/10/09 11:09:20 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.2 $
 * $Date: 2007/10/09 11:09:20 $
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
package com.mvnforum.db;

import java.sql.Timestamp;

public class ThreadPollBean {

    private int pollID;
    private int threadID;
    private int threadPollOption;
    private int threadPollStatus;
    private int threadPollType;
    private Timestamp threadPollCreationDate;
    private Timestamp threadPollModifiedDate;

    public int getPollID() {
        return pollID;
    }
    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    public int getThreadID() {
        return threadID;
    }
    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getThreadPollOption() {
        return threadPollOption;
    }
    public void setThreadPollOption(int threadPollOption) {
        this.threadPollOption = threadPollOption;
    }

    public int getThreadPollStatus() {
        return threadPollStatus;
    }
    public void setThreadPollStatus(int threadPollStatus) {
        this.threadPollStatus = threadPollStatus;
    }

    public int getThreadPollType() {
        return threadPollType;
    }
    public void setThreadPollType(int threadPollType) {
        this.threadPollType = threadPollType;
    }

    public Timestamp getThreadPollCreationDate() {
        return threadPollCreationDate;
    }
    public void setThreadPollCreationDate(Timestamp threadPollCreationDate) {
        this.threadPollCreationDate = threadPollCreationDate;
    }

    public Timestamp getThreadPollModifiedDate() {
        return threadPollModifiedDate;
    }
    public void setThreadPollModifiedDate(Timestamp threadPollModifiedDate) {
        this.threadPollModifiedDate = threadPollModifiedDate;
    }

}
