/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/ZoneBean.java,v 1.3 2008/06/10 03:04:44 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.3 $
 * $Date: 2008/06/10 03:04:44 $
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

import net.myvietnam.mvncore.util.StringUtil;

public class ZoneBean {
    
    public static final int ZONE_DIRECTION_HORIZONTAL = 0;
    public static final int ZONE_DIRECTION_VERTICAL   = 1;
    
    public static final int ZONE_NOT_AUTO_RELOAD      = 0;
    
    public static final int ZONE_TYPE_NORMAL          = 0;
    public static final int ZONE_TYPE_DIRECT_CODE     = 1;

    private int zoneID;
    private String memberName;
    private String zoneName;
    private String zoneDesc;
    private String zoneTargetWindow;
    private int zoneCellWidth;
    private int zoneCellHeight;
    private int zoneCellHorizontalCount;
    private int zoneCellVerticalCount;
    private int zoneMaxBanners;
    private int zoneDirection;
    private int zoneMaxImpression;
    private int zoneReceivedImpression;
    private int zoneMaxClick;
    private int zoneReceivedClick;
    private int zoneAutoReloadTime;
    private int zoneOption;
    private int zoneStatus;
    private int zoneType;
    private Timestamp zoneCreationDate;
    private Timestamp zoneModifiedDate;

    public int getZoneID() {
        return zoneID;
    }
    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getZoneName() {
        return zoneName;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneDesc() {
        return StringUtil.getEmptyStringIfNull(zoneDesc);
    }
    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
    }

    public String getZoneTargetWindow() {
        return zoneTargetWindow;
    }
    public void setZoneTargetWindow(String zoneTargetWindow) {
        this.zoneTargetWindow = zoneTargetWindow;
    }

    public int getZoneCellWidth() {
        return zoneCellWidth;
    }
    public void setZoneCellWidth(int zoneCellWidth) {
        this.zoneCellWidth = zoneCellWidth;
    }

    public int getZoneCellHeight() {
        return zoneCellHeight;
    }
    public void setZoneCellHeight(int zoneCellHeight) {
        this.zoneCellHeight = zoneCellHeight;
    }

    public int getZoneCellHorizontalCount() {
        return zoneCellHorizontalCount;
    }
    public void setZoneCellHorizontalCount(int zoneCellHorizontalCount) {
        this.zoneCellHorizontalCount = zoneCellHorizontalCount;
    }

    public int getZoneCellVerticalCount() {
        return zoneCellVerticalCount;
    }
    public void setZoneCellVerticalCount(int zoneCellVerticalCount) {
        this.zoneCellVerticalCount = zoneCellVerticalCount;
    }

    public int getZoneMaxBanners() {
        return zoneMaxBanners;
    }
    public void setZoneMaxBanners(int zoneMaxBanners) {
        this.zoneMaxBanners = zoneMaxBanners;
    }

    public int getZoneDirection() {
        return zoneDirection;
    }
    public void setZoneDirection(int zoneDirection) {
        this.zoneDirection = zoneDirection;
    }

    public int getZoneMaxImpression() {
        return zoneMaxImpression;
    }
    public void setZoneMaxImpression(int zoneMaxImpression) {
        this.zoneMaxImpression = zoneMaxImpression;
    }

    public int getZoneReceivedImpression() {
        return zoneReceivedImpression;
    }
    public void setZoneReceivedImpression(int zoneReceivedImpression) {
        this.zoneReceivedImpression = zoneReceivedImpression;
    }

    public int getZoneMaxClick() {
        return zoneMaxClick;
    }
    public void setZoneMaxClick(int zoneMaxClick) {
        this.zoneMaxClick = zoneMaxClick;
    }

    public int getZoneReceivedClick() {
        return zoneReceivedClick;
    }
    public void setZoneReceivedClick(int zoneReceivedClick) {
        this.zoneReceivedClick = zoneReceivedClick;
    }

    public int getZoneAutoReloadTime() {
        return zoneAutoReloadTime;
    }
    public void setZoneAutoReloadTime(int zoneAutoReloadTime) {
        this.zoneAutoReloadTime = zoneAutoReloadTime;
    }

    public int getZoneOption() {
        return zoneOption;
    }
    public void setZoneOption(int zoneOption) {
        this.zoneOption = zoneOption;
    }

    public int getZoneStatus() {
        return zoneStatus;
    }
    public void setZoneStatus(int zoneStatus) {
        this.zoneStatus = zoneStatus;
    }

    public int getZoneType() {
        return zoneType;
    }
    public void setZoneType(int zoneType) {
        this.zoneType = zoneType;
    }

    public Timestamp getZoneCreationDate() {
        return zoneCreationDate;
    }
    public void setZoneCreationDate(Timestamp zoneCreationDate) {
        this.zoneCreationDate = zoneCreationDate;
    }

    public Timestamp getZoneModifiedDate() {
        return zoneModifiedDate;
    }
    public void setZoneModifiedDate(Timestamp zoneModifiedDate) {
        this.zoneModifiedDate = zoneModifiedDate;
    }
    
    /************************************************
     * Customized methods come below
     ************************************************/
    private int bannerCount;
    public int getBannerCount() {
        return bannerCount;
    }
    public void setBannerCount(int bannerCount) {
        this.bannerCount = bannerCount;
    }    
}
