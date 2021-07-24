/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PollVoteBean.java,v 1.3 2008/07/14 09:45:20 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.3 $
 * $Date: 2008/07/14 09:45:20 $
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

import net.myvietnam.mvncore.util.StringUtil;

public class PollVoteBean {

    private int pollAnswerID;
    private String memberName;
    private String pollVoteOpinion;
    private String pollVoteIP;
    private int pollVoteOption;
    private int pollVoteStatus;
    private int pollVoteType;
    private Timestamp pollVoteCreationDate;
    private Timestamp pollVoteModifiedDate;

    public int getPollAnswerID() {
        return pollAnswerID;
    }
    public void setPollAnswerID(int pollAnswerID) {
        this.pollAnswerID = pollAnswerID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPollVoteOpinion() {
        return pollVoteOpinion;
    }
    public void setPollVoteOpinion(String pollVoteOpinion) {
        this.pollVoteOpinion = StringUtil.getEmptyStringIfNull(pollVoteOpinion);
    }

    public String getPollVoteIP() {
        return pollVoteIP;
    }
    public void setPollVoteIP(String pollVoteIP) {
        this.pollVoteIP = pollVoteIP;
    }

    public int getPollVoteOption() {
        return pollVoteOption;
    }
    public void setPollVoteOption(int pollVoteOption) {
        this.pollVoteOption = pollVoteOption;
    }

    public int getPollVoteStatus() {
        return pollVoteStatus;
    }
    public void setPollVoteStatus(int pollVoteStatus) {
        this.pollVoteStatus = pollVoteStatus;
    }

    public int getPollVoteType() {
        return pollVoteType;
    }
    public void setPollVoteType(int pollVoteType) {
        this.pollVoteType = pollVoteType;
    }

    public Timestamp getPollVoteCreationDate() {
        return pollVoteCreationDate;
    }
    public void setPollVoteCreationDate(Timestamp pollVoteCreationDate) {
        this.pollVoteCreationDate = pollVoteCreationDate;
    }

    public Timestamp getPollVoteModifiedDate() {
        return pollVoteModifiedDate;
    }
    public void setPollVoteModifiedDate(Timestamp pollVoteModifiedDate) {
        this.pollVoteModifiedDate = pollVoteModifiedDate;
    }

}
