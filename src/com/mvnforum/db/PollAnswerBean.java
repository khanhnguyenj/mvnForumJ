/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PollAnswerBean.java,v 1.5 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2009/01/02 18:18:50 $
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
 * @author: Phuong, Pham Dinh Duy
 */
package com.mvnforum.db;

import java.sql.Timestamp;

public class PollAnswerBean {

    public static final int POLL_ANSWER_TYPE_NORMAL                 = 0;
    public static final int POLL_ANSWER_TYPE_NEED_YOUR_OPINION      = 1;

    private int pollAnswerID;
    private int pollID;
    private String pollAnswerText;
    private int pollAnswerOrder;
    private int pollAnswerOption;
    private int pollAnswerStatus;
    private int pollAnswerType;
    private Timestamp pollAnswerCreationDate;
    private Timestamp pollAnswerModifiedDate;

    public int getPollAnswerID() {
        return pollAnswerID;
    }
    public void setPollAnswerID(int pollAnswerID) {
        this.pollAnswerID = pollAnswerID;
    }

    public int getPollID() {
        return pollID;
    }
    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    public String getPollAnswerText() {
        return pollAnswerText;
    }
    public void setPollAnswerText(String pollAnswerText) {
        this.pollAnswerText = pollAnswerText;
    }

    public int getPollAnswerOrder() {
        return pollAnswerOrder;
    }
    public void setPollAnswerOrder(int pollAnswerOrder) {
        this.pollAnswerOrder = pollAnswerOrder;
    }

    public int getPollAnswerOption() {
        return pollAnswerOption;
    }
    public void setPollAnswerOption(int pollAnswerOption) {
        this.pollAnswerOption = pollAnswerOption;
    }

    public int getPollAnswerStatus() {
        return pollAnswerStatus;
    }
    public void setPollAnswerStatus(int pollAnswerStatus) {
        this.pollAnswerStatus = pollAnswerStatus;
    }

    public int getPollAnswerType() {
        return pollAnswerType;
    }
    public void setPollAnswerType(int pollAnswerType) {
        this.pollAnswerType = pollAnswerType;
    }

    public Timestamp getPollAnswerCreationDate() {
        return pollAnswerCreationDate;
    }
    public void setPollAnswerCreationDate(Timestamp pollAnswerCreationDate) {
        this.pollAnswerCreationDate = pollAnswerCreationDate;
    }

    public Timestamp getPollAnswerModifiedDate() {
        return pollAnswerModifiedDate;
    }
    public void setPollAnswerModifiedDate(Timestamp pollAnswerModifiedDate) {
        this.pollAnswerModifiedDate = pollAnswerModifiedDate;
    }

}
