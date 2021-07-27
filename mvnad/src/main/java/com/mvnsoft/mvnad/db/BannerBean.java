/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/BannerBean.java,v 1.4 2009/03/20 03:13:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.4 $
 * $Date: 2009/03/20 03:13:16 $
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

public class BannerBean {
    
    public static final int BANNER_TYPE_IMAGE = 0;
    public static final int BANNER_TYPE_FLASH = 1;
    public static final int BANNER_TYPE_MOVIE = 2;
    public static final int BANNER_TYPE_HTML  = 3;
    
    public static final int BANNER_IS_HTML    = 1;
    
    private int bannerID;
    private String memberName;
    private String bannerName;
    private String bannerDesc;
    private String bannerAltText;
    private String bannerMimeType;
    private String bannerPreText;
    private String bannerAfterText;
    private String bannerTargetURL;
    private String bannerImageURL;
    private int bannerWidth;
    private int bannerHeight;
    private int bannerWeight;
    private int bannerMaxImpression;
    private int bannerReceivedImpression;
    private int bannerMaxClick;
    private int bannerReceivedClick;
    private int bannerZonePositionX;
    private int bannerZonePositionY;
    private Timestamp bannerStartDate;
    private Timestamp bannerEndDate;
    private int bannerIsHtml;
    private String bannerHtmlCode;
    private int bannerCanTrackClicks;
    private int bannerOption;
    private int bannerStatus;
    private int bannerType;
    private Timestamp bannerCreationDate;
    private Timestamp bannerModifiedDate;

    public int getBannerID() {
        return bannerID;
    }
    public void setBannerID(int bannerID) {
        this.bannerID = bannerID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBannerName() {
        return bannerName;
    }
    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerDesc() {
        return StringUtil.getEmptyStringIfNull(bannerDesc);
    }
    public void setBannerDesc(String bannerDesc) {
        this.bannerDesc = bannerDesc;
    }

    public String getBannerAltText() {
        return StringUtil.getEmptyStringIfNull(bannerAltText);
    }
    public void setBannerAltText(String bannerAltText) {
        this.bannerAltText = bannerAltText;
    }

    public String getBannerMimeType() {
        return bannerMimeType;
    }
    public void setBannerMimeType(String bannerMimeType) {
        this.bannerMimeType = bannerMimeType;
    }

    public String getBannerPreText() {
        return StringUtil.getEmptyStringIfNull(bannerPreText);
    }
    public void setBannerPreText(String bannerPreText) {
        this.bannerPreText = bannerPreText;
    }

    public String getBannerAfterText() {
        return StringUtil.getEmptyStringIfNull(bannerAfterText);
    }
    public void setBannerAfterText(String bannerAfterText) {
        this.bannerAfterText = bannerAfterText;
    }

    public String getBannerTargetURL() {
        return StringUtil.getEmptyStringIfNull(bannerTargetURL);
    }
    public void setBannerTargetURL(String bannerTargetURL) {
        this.bannerTargetURL = bannerTargetURL;
    }

    public String getBannerImageURL() {
        return StringUtil.getEmptyStringIfNull(bannerImageURL);
    }
    public void setBannerImageURL(String bannerImageURL) {
        this.bannerImageURL = bannerImageURL;
    }

    public int getBannerWidth() {
        return bannerWidth;
    }
    public void setBannerWidth(int bannerWidth) {
        this.bannerWidth = bannerWidth;
    }

    public int getBannerHeight() {
        return bannerHeight;
    }
    public void setBannerHeight(int bannerHeight) {
        this.bannerHeight = bannerHeight;
    }

    public int getBannerWeight() {
        return bannerWeight;
    }
    public void setBannerWeight(int bannerWeight) {
        this.bannerWeight = bannerWeight;
    }

    public int getBannerMaxImpression() {
        return bannerMaxImpression;
    }
    public void setBannerMaxImpression(int bannerMaxImpression) {
        this.bannerMaxImpression = bannerMaxImpression;
    }

    public int getBannerReceivedImpression() {
        return bannerReceivedImpression;
    }
    public void setBannerReceivedImpression(int bannerReceivedImpression) {
        this.bannerReceivedImpression = bannerReceivedImpression;
    }

    public int getBannerMaxClick() {
        return bannerMaxClick;
    }
    public void setBannerMaxClick(int bannerMaxClick) {
        this.bannerMaxClick = bannerMaxClick;
    }

    public int getBannerReceivedClick() {
        return bannerReceivedClick;
    }
    public void setBannerReceivedClick(int bannerReceivedClick) {
        this.bannerReceivedClick = bannerReceivedClick;
    }

    public int getBannerZonePositionX() {
        return bannerZonePositionX;
    }
    public void setBannerZonePositionX(int bannerZonePositionX) {
        this.bannerZonePositionX = bannerZonePositionX;
    }

    public int getBannerZonePositionY() {
        return bannerZonePositionY;
    }
    public void setBannerZonePositionY(int bannerZonePositionY) {
        this.bannerZonePositionY = bannerZonePositionY;
    }

    public Timestamp getBannerStartDate() {
        return bannerStartDate;
    }
    public void setBannerStartDate(Timestamp bannerStartDate) {
        this.bannerStartDate = bannerStartDate;
    }

    public Timestamp getBannerEndDate() {
        return bannerEndDate;
    }
    public void setBannerEndDate(Timestamp bannerEndDate) {
        this.bannerEndDate = bannerEndDate;
    }

    public int getBannerIsHtml() {
        return bannerIsHtml;
    }
    public void setBannerIsHtml(int bannerIsHtml) {
        this.bannerIsHtml = bannerIsHtml;
    }

    public String getBannerHtmlCode() {
        return StringUtil.getEmptyStringIfNull(bannerHtmlCode);
    }
    public void setBannerHtmlCode(String bannerHtmlCode) {
        this.bannerHtmlCode = bannerHtmlCode;
    }

    public int getBannerCanTrackClicks() {
        return bannerCanTrackClicks;
    }
    public void setBannerCanTrackClicks(int bannerCanTrackClicks) {
        this.bannerCanTrackClicks = bannerCanTrackClicks;
    }

    public int getBannerOption() {
        return bannerOption;
    }
    public void setBannerOption(int bannerOption) {
        this.bannerOption = bannerOption;
    }

    public int getBannerStatus() {
        return bannerStatus;
    }
    public void setBannerStatus(int bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public int getBannerType() {
        return bannerType;
    }
    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public Timestamp getBannerCreationDate() {
        return bannerCreationDate;
    }
    public void setBannerCreationDate(Timestamp bannerCreationDate) {
        this.bannerCreationDate = bannerCreationDate;
    }

    public Timestamp getBannerModifiedDate() {
        return bannerModifiedDate;
    }
    public void setBannerModifiedDate(Timestamp bannerModifiedDate) {
        this.bannerModifiedDate = bannerModifiedDate;
    }
    
    public boolean isValid(Timestamp now) {
        if ((now.compareTo(bannerStartDate) >= 0) && (now.compareTo(bannerEndDate) <= 0)) {
            return true;
        }
        return false;
    }
    
    /************************************************
     * Customized methods come below
     ************************************************/
    private int zoneCount;
    public int getZoneCount() {
        return zoneCount;
    }
    public void setZoneCount(int zoneCount) {
        this.zoneCount = zoneCount;
    }   
}
