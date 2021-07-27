/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PollBean.java,v 1.11 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.11 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 */
package com.mvnforum.db;

import java.sql.Timestamp;

public class PollBean {

    public static final int POLL_VOTE_NO_CHANGE                  = 0;
    public static final int POLL_VOTE_CHANGE                     = 1;

    public static final int POLL_MULTIPLE_NO                     = 0;
    public static final int POLL_MULTIPLE_YES                    = 1;

    public static final int POLL_SHOW_RESULT_TO_ANYONE           = 0;
    public static final int POLL_SHOW_RESULT_AFTER_VOTED         = 1;
    public static final int POLL_SHOW_RESULT_AFTER_EXPIRED       = 2;

    public static final int POLL_NO_PUBLIC                       = 0;
    public static final int POLL_PUBLIC                          = 1;

    public static final int POLL_STATUS_EDITING                  = 0;
    public static final int POLL_STATUS_ENABLE                   = 1;
    public static final int POLL_STATUS_DISABLED                 = 2;

    public static final int POLL_TYPE_NORMAL                     = 0;
    public static final int POLL_TYPE_ANONYMOUS                  = 1;
    
    public static final int ORPHAN                               = 0;
    public static final int THREAD                               = 1;
    public static final int ALBUMITEM                            = 2;
    
    private int pollID;
    private String memberName;
    private String lastEditMemberName;
    private String pollQuestion;
    private String pollIP;
    private int pollVoteChange;
    private int pollHideResult;
    private int pollPublic;
    private int pollMultiple;
    private int pollOption;
    private int pollStatus;
    private int pollType;
    private Timestamp pollCreationDate;
    private Timestamp pollModifiedDate;
    private Timestamp pollStartDate;
    private Timestamp pollEndDate;
    private Timestamp pollLastVoteDate;

    public int getPollID() {
        return pollID;
    }
    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getLastEditMemberName() {
        return lastEditMemberName;
    }
    public void setLastEditMemberName(String lastEditMemberName) {
        this.lastEditMemberName = lastEditMemberName;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }
    public void setPollQuestion(String pollQuestion) {
        this.pollQuestion = pollQuestion;
    }

    public String getPollIP() {
        return pollIP;
    }
    public void setPollIP(String pollIP) {
        this.pollIP = pollIP;
    }

    public int getPollVoteChange() {
        return pollVoteChange;
    }
    public void setPollVoteChange(int pollVoteChange) {
        this.pollVoteChange = pollVoteChange;
    }

    public int getPollHideResult() {
        return pollHideResult;
    }
    public void setPollHideResult(int pollHideResult) {
        this.pollHideResult = pollHideResult;
    }

    public int getPollPublic() {
        return pollPublic;
    }
    public void setPollPublic(int pollPublic) {
        this.pollPublic = pollPublic;
    }

    public int getPollMultiple() {
        return pollMultiple;
    }
    public void setPollMultiple(int pollMultiple) {
        this.pollMultiple = pollMultiple;
    }

    public int getPollOption() {
        return pollOption;
    }
    public void setPollOption(int pollOption) {
        this.pollOption = pollOption;
    }

    public int getPollStatus() {
        return pollStatus;
    }
    public void setPollStatus(int pollStatus) {
        this.pollStatus = pollStatus;
    }

    public int getPollType() {
        return pollType;
    }
    public void setPollType(int pollType) {
        this.pollType = pollType;
    }

    public Timestamp getPollCreationDate() {
        return pollCreationDate;
    }
    public void setPollCreationDate(Timestamp pollCreationDate) {
        this.pollCreationDate = pollCreationDate;
    }

    public Timestamp getPollModifiedDate() {
        return pollModifiedDate;
    }
    public void setPollModifiedDate(Timestamp pollModifiedDate) {
        this.pollModifiedDate = pollModifiedDate;
    }

    public Timestamp getPollStartDate() {
        return pollStartDate;
    }
    public void setPollStartDate(Timestamp pollStartDate) {
        this.pollStartDate = pollStartDate;
    }

    public Timestamp getPollEndDate() {
        return pollEndDate;
    }
    public void setPollEndDate(Timestamp pollEndDate) {
        this.pollEndDate = pollEndDate;
    }

    public Timestamp getPollLastVoteDate() {
        return pollLastVoteDate;
    }
    public void setPollLastVoteDate(Timestamp pollLastVoteDate) {
        this.pollLastVoteDate = pollLastVoteDate;
    }

}
