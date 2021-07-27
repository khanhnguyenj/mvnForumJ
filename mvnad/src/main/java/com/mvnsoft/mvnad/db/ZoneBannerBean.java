/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/ZoneBannerBean.java,v 1.2 2008/06/03 08:20:49 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.2 $
 * $Date: 2008/06/03 08:20:49 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
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
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.db;

import java.sql.Timestamp;

public class ZoneBannerBean {
    
    private int zoneID;
    private int bannerID;
    private int relationCellX;
    private int relationCellY;
    private int relationCellOption;
    private int relationWeight;
    private int relationOption;
    private int relationStatus;
    private int relationType;
    private Timestamp relationPublishStartDate;
    private Timestamp relationPublishEndDate;
    private Timestamp relationCreationDate;
    private Timestamp relationModifiedDate;

    // additional fields
    private BannerBean bannerBean;

    public int getZoneID() {
        return zoneID;
    }
    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public int getBannerID() {
        return bannerID;
    }
    public void setBannerID(int bannerID) {
        this.bannerID = bannerID;
    }
    
    public int getRelationCellX() {
        return relationCellX;
    }
    public void setRelationCellX(int relationCellX) {
        this.relationCellX = relationCellX;
    }

    public int getRelationCellY() {
        return relationCellY;
    }
    public void setRelationCellY(int relationCellY) {
        this.relationCellY = relationCellY;
    }

    public int getRelationCellOption() {
        return relationCellOption;
    }
    public void setRelationCellOption(int relationCellOption) {
        this.relationCellOption = relationCellOption;
    }

    public int getRelationWeight() {
        return relationWeight;
    }
    public void setRelationWeight(int relationWeight) {
        this.relationWeight = relationWeight;
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

    public int getRelationType() {
        return relationType;
    }
    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public Timestamp getRelationPublishStartDate() {
        return relationPublishStartDate;
    }
    public void setRelationPublishStartDate(Timestamp relationPublishStartDate) {
        this.relationPublishStartDate = relationPublishStartDate;
    }

    public Timestamp getRelationPublishEndDate() {
        return relationPublishEndDate;
    }
    public void setRelationPublishEndDate(Timestamp relationPublishEndDate) {
        this.relationPublishEndDate = relationPublishEndDate;
    }

    public Timestamp getRelationCreationDate() {
        return relationCreationDate;
    }
    public void setRelationCreationDate(Timestamp relationCreationDate) {
        this.relationCreationDate = relationCreationDate;
    }

    public Timestamp getRelationModifiedDate() {
        return relationModifiedDate;
    }
    public void setRelationModifiedDate(Timestamp relationModifiedDate) {
        this.relationModifiedDate = relationModifiedDate;
    }

    public boolean isValid(Timestamp now) {
        if ((now.compareTo(relationPublishStartDate) >= 0) && (now.compareTo(relationPublishEndDate) <= 0)) {
            return true;
        }
        return false;
    }
    
    public BannerBean getBannerBean() {
        return bannerBean;
    }
    public void setBannerBean(BannerBean bannerBean) {
        this.bannerBean = bannerBean;
    }

}
