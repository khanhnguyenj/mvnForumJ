/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/ChannelListPermission.java,v 1.10 2009/01/06 16:43:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.10 $
 * $Date: 2009/01/06 16:43:12 $
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

import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used in MVNForumPermissionImpl to imnplement channel-specific permission
 * NOTE: This class is NOT thread-safe
 */
class ChannelListPermission {

    private static final Logger log = LoggerFactory.getLogger(ChannelListPermission.class);

    ArrayList channelList = new ArrayList();

    boolean allChannelsPermission = false;

    boolean bypassPrivateChannel = false;

    ChannelListPermission() {
    }

    void setAllChannelsPermission(boolean permission) {
        allChannelsPermission = permission;
    }

    void setChannelPermission(int channelID, boolean permission) {
        // always remove channelID
        Iterator iter = channelList.iterator();
        while (iter.hasNext()) {
            int currentChannelID = ((Integer) iter.next()).intValue();
            if (currentChannelID == channelID) {
                iter.remove();
            }
        } //while

        // now add to the list if the permission = false
        if (permission) {
            // add permission
            channelList.add(new Integer(channelID));
        }
    }

    boolean hasPermission(int channelID) {

        for (int i = 0; i < channelList.size(); i++) {
            int currentChannelID = ((Integer)channelList.get(i)).intValue();
            if (currentChannelID == channelID) {
                return true;
            }
        }

        // have permission on all channels, then we check if this is a Private Channel
        if (allChannelsPermission) {
            if (bypassPrivateChannel) {
                return true;
            }

            // now we assume (channelBean.getChannelType() == ChannelBean.CHANNEL_TYPE_DEFAULT)
            return true;
            /*
            try {
                ChannelBean channelBean = ChannelCache.getInstance().getBean(channelID);
                if (channelBean.getChannelType() == ChannelBean.CHANNEL_TYPE_DEFAULT) {
                    return true;
                }
            } catch (Exception ex) {
                log.error("Cannot get the ChannelBean in ChannelListPermission", ex);
            }*/
        }

        // if not found, then we return false (no permission on the channel)
        return false;
    }

    boolean hasPermssionInAnyChannels() {

        // now check if have permission on any channels by checking the channelList size
        if (channelList.size() > 0) {
            // channelList size > 0 means there is permission on at least one channel
            return true;
        }

        // have permission on all channels, then we check if this is a Private Channel
        if (allChannelsPermission) {
            if (bypassPrivateChannel) {
                return true;
            }

            // now we assume (channelBean.getChannelType() == ChannelBean.CHANNEL_TYPE_DEFAULT)
            return true;
            /*
            try {
                Collection channelBeans = ChannelCache.getInstance().getBeans();
                for (Iterator iter = channelBeans.iterator(); iter.hasNext(); ) {
                    ChannelBean channelBean = (ChannelBean)iter.next();
                    if (channelBean.getChannelType() == ChannelBean.CHANNEL_TYPE_DEFAULT) {
                        return true;
                    }
                }
            } catch (Exception ex) {
                log.error("Cannot get Channel Beans in ChannelListPermission", ex);
            }*/
        }

        // if not found, then we return false (no permission on any channels)
        return false;
    }

    public boolean isBypassPrivateChannel() {
        return bypassPrivateChannel;
    }

    public void setBypassPrivateChannel(boolean ignorePrivateOption) {
        this.bypassPrivateChannel = ignorePrivateOption;
    }
}
