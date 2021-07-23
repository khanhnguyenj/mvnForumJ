/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/StepAndChannelListPermission.java,v 1.4 2007/10/09 11:09:12 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
 * $Date: 2007/10/09 11:09:12 $
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
package com.mvnforum.auth;

import java.util.*;

public class StepAndChannelListPermission {

    List stepAndChannelList = new ArrayList();

    //with all channels
    List stepList = new ArrayList();

    boolean allStepAndChannelsPermission = false;

    boolean bypassPrivateChannel = false;

    public StepAndChannelListPermission() {
    }

    void setAllStepAndChannelsPermission(boolean permission) {
        allStepAndChannelsPermission = permission;
    }

    boolean isGlobalPermission() {
        return allStepAndChannelsPermission;
    }

    void setAllChannelsPermission(int stepID, boolean permission) {

        // always remove stepID and channelID
        for (Iterator iterator = stepList.iterator(); iterator.hasNext(); ) {

            int step = ((Integer) iterator.next()).intValue();

            if (step == stepID) {
                iterator.remove();
            }
        }

        // now add to the list if the permission = true
        if (permission) {
            // add permission
            stepList.add(new Integer(stepID));
        }
    }

    void setStepAndChannelPermission(int stepID, int channelID, boolean permission) {

        // always remove stepID and channelID
        for (Iterator iterator = stepAndChannelList.iterator(); iterator.hasNext(); ) {

            StepAndChannel stepAndChannel = (StepAndChannel) iterator.next();

            int step = stepAndChannel.getStepID();
            int channel = stepAndChannel.getChannelID();

            if ((step == stepID) && (channel == channelID)) {
                iterator.remove();
            }
        }

        // now add to the list if the permission = true
        if (permission) {
            // add permission
            stepAndChannelList.add(new StepAndChannel(stepID, channelID));
        }
    }

    boolean hasPermission(int stepID, int channelID) {

        for (Iterator iter = stepList.iterator(); iter.hasNext(); ) {
            int step = ((Integer) iter.next()).intValue();

            if (step == stepID) {
                return true;
            }
        }

        for (Iterator iterator = stepAndChannelList.iterator(); iterator.hasNext(); ) {

            StepAndChannel stepAndChannel = (StepAndChannel) iterator.next();

            int step = stepAndChannel.getStepID();
            int channel = stepAndChannel.getChannelID();

            if ((step == stepID) && (channel == channelID)) {
                return true;
            }
        }

        if (allStepAndChannelsPermission) {
            if (bypassPrivateChannel) {
                return true;
            }

            //TODO should check channelType is private or not
            return true;
        }

        return false;
    }

    boolean hasPermssionInAtLeastOneChannelOrStep() {

        // now check if have permission on any channels by checking the channelList size
        if ((stepList.size() > 0) || (stepAndChannelList.size() > 0)) {
            //stepList or stepAndChannelList size > 0 means there is permission on at least one channel
            return true;
        }

        // have permission on all channels, then we check if this is a Private Channel
        if (allStepAndChannelsPermission) {
            if (bypassPrivateChannel) {
                return true;
            }

            //TODO should check channelType is private or not
            return true;
        }

        // if not found, then we return false (no permission on any channels)
        return false;
    }

    boolean hasPermssionInStepInAtLeastOneChannel(int stepID) {

        for (Iterator iter = stepList.iterator(); iter.hasNext(); ) {

            int step = ((Integer) iter.next()).intValue();

            if (step == stepID) {
                return true;
            }
        }

        for (Iterator iterator = stepAndChannelList.iterator(); iterator.hasNext(); ) {

            StepAndChannel stepAndChannel = (StepAndChannel) iterator.next();

            int step = stepAndChannel.getStepID();

            if (step == stepID) {
                return true;
            }
        }

        if (allStepAndChannelsPermission) {
            if (bypassPrivateChannel) {
                return true;
            }

            //TODO should check channelType is private or not
            return true;
        }

        return false;
    }

    public boolean isBypassPrivateChannel() {
        return bypassPrivateChannel;
    }

    public void setBypassPrivateChannel(boolean ignorePrivateOption) {
        this.bypassPrivateChannel = ignorePrivateOption;
    }

}