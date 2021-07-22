/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/OnlineUserAction.java,v 1.31 2008/11/04 03:57:27 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.31 $
 * $Date: 2008/11/04 03:57:27 $
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
package com.mvnforum.auth;

import java.sql.Timestamp;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;

public class OnlineUserAction {

    private String url;
    //private String desc;
    private Action action;
    private Timestamp firstRequestTime;
    private Timestamp lastRequestTime;
    private String remoteAddr;
    private String userAgent;

    // NOTE: these 2 variables seam to duplicate variables in OnlineUserImpl
    // but we need it when display all online users (listonlineuser)
    private int memberID;
    private String memberName;
    private boolean invisible = false;

    private int sessionCount = 1;
    /**
     * default constructor
     */
    OnlineUserAction() {
        /*
         * We must separate 2 duplicate method calls since we will
         * update the lastRequestTime, but not firstRequestTime
         */
        firstRequestTime = DateUtil.getCurrentGMTTimestamp();
        lastRequestTime  = DateUtil.getCurrentGMTTimestamp();
    }

/****************************************************************
 * Public method
 ****************************************************************/

    public String getDesc(GenericRequest request) {
        if (action == null) {
            // This issue is in the Realm authentication integration
            return "Cannot get description because action is null";
        }
        return action.getLocalizedDesc(request);
    }

    public int getPageID() {
        if (action == null) {
            return 0;
        }
        return action.getPageID();
    }

    public Object getPageParam() {
        if (action == null) {
            return null;
        }
        return action.getPageParam();
    }

    public String getUrl() {
        return url;
    }

    public int getMemberID() {
        return memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public boolean isInvisibleMember() {
        return invisible;
    }

    public Timestamp getFirstRequestTime() {
        return firstRequestTime;
    }

    public Timestamp getLastRequestTime() {
        return lastRequestTime;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int count) {
        sessionCount = count;
    }

    public void increaseSessionCount(int delta) {
        sessionCount += delta;
    }

    public void resetSessionCount() {
        sessionCount = 1;
    }

    // util method
    public String getOnlineDurarionDesc(Timestamp currentTime, Locale locale) {
        int duration = (int) (currentTime.getTime() - firstRequestTime.getTime());
        return getTimeString(duration, locale);
    }

    public String getDurationSinceLastRequestDesc(Timestamp currentTime, Locale locale) {
        int duration = (int) (currentTime.getTime() - lastRequestTime.getTime());
        return getTimeString(duration, locale);
    }

    private String getTimeString(int duration, Locale locale) {
        long hours = duration / DateUtil.HOUR;
        long remain = duration - (hours * DateUtil.HOUR);
        long minutes = remain / DateUtil.MINUTE;
        StringBuffer time = new StringBuffer(64);

        if (hours > 0) {//there is hour
            time.append(hours).append(' ');
            if (hours == 1) {
                time.append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.1_hour"));
            } else {
                time.append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.X_hour"));
            }
        }

        if (minutes > 0) {//there is minute
            if (hours > 0) {
                time.append(' ').append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.hhmm_and")).append(" ");
            }
            time.append(minutes).append(' ');
            if (minutes == 1) {
                time.append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.1_minute"));
            } else {
                time.append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.X_minutes"));
            }
        } else {// no minute
            // if there is no hour, should show "0 minute"
            if (hours == 0) {
                time.append("0 ").append(MVNForumResourceBundle.getString(locale, "mvnforum.common.date.1_minute"));
            }
        }

        return time.toString();
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }


    /****************************************************************
     * Default package method
     ****************************************************************/
    protected void initRemoteAddr_UserAgent(HttpServletRequest request) {
        setUserAgent(request.getHeader("User-Agent"));
        setRemoteAddr(request.getRemoteAddr());
    }

    protected void initRemoteAddr_UserAgent(GenericRequest request) {
        if (request.isServletRequest()) {
            setUserAgent(request.getServletRequest().getHeader("User-Agent"));
            setRemoteAddr(request.getServletRequest().getRemoteAddr());
        } else {
            setUserAgent(request.getUserAgent());
            setRemoteAddr(request.getRemoteAddr());
        }
    }

    void setAction(Action action) {
        //this.desc = action.getDesc();
        this.url  = action.getUrl();
        this.action = action;
    }

    /** @todo use method DateUtil.updateCurrentGMTTimestamp() */
    void updateLastRequestTime() {
        DateUtil.updateCurrentGMTTimestamp(lastRequestTime);
    }

    void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    void setMemberInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = DisableHtmlTagFilter.filter(remoteAddr);
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = DisableHtmlTagFilter.filter(StringUtil.getEmptyStringIfNull(userAgent));
    }
}
