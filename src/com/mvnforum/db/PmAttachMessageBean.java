/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/PmAttachMessageBean.java,v 1.8 2007/10/10 02:16:31 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2007/10/10 02:16:31 $
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

/*
 * Included columns: MessageID, PmAttachID, RelationType, RelationOption, RelationStatus
 * Excluded columns:
 */
public class PmAttachMessageBean {
    private int messageID;
    private int pmAttachID;
    private int relationType;
    private int relationOption;
    private int relationStatus;

    public int getMessageID() {
        return messageID;
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getPmAttachID() {
        return pmAttachID;
    }
    public void setPmAttachID(int pmAttachID) {
        this.pmAttachID = pmAttachID;
    }

    public int getRelationType() {
        return relationType;
    }
    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public int getRelationOption() {
        return relationOption;
    }
    public void setRelationOption(int relationOption) {
        this.relationOption = relationOption;
    }

    public int getRelationStatus() {
        return relationStatus;
    }
    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

} //end of class PmAttachMessageBean
