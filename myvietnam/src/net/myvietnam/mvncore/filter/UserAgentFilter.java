/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/filter/UserAgentFilter.java,v 1.13 2008/12/30 10:46:29 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.13 $
 * $Date: 2008/12/30 10:46:29 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Minh Nguyen  
 * @author: Mai  Nguyen  
 */
package net.myvietnam.mvncore.filter;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.util.StringUtil;

public final class UserAgentFilter {

    //private static final Logger log = LoggerFactory.getLogger(UserAgentFilter.class);

    private static String[] blockedUserAgent = null;

    private UserAgentFilter() { //prevent instantiation
    }

    static {
        //UserAgentOptions UserAgentOptions = new UserAgentOptions();
        //blockedUserAgent = StringUtil.getStringArray(UserAgentOptions.blockedUserAgent, ";");

        blockedUserAgent = StringUtil.getStringArray(MVNCoreConfig.getBlockedUserAgents(), ";");
    }

    /**
     * Filter the UserAgent
     * @param request
     * @return true  if the UserAgent in this request is ok
     *         false if the UserAgent in this request is blocked
     */
    public static boolean filter(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Cannot accept a null request in UserAgentFilter");
        }
        String checkUserAgent = request.getHeader("User-Agent");

        //@todo: in this case, should we return true or false ?
        if (checkUserAgent == null) {
            return true;
        }

        for (int i = 0; i < blockedUserAgent.length; i++) {
            String currentBlockedUserAgent = blockedUserAgent[i];
            if (checkUserAgent.indexOf(currentBlockedUserAgent) != -1) {
                return false;
            }
        }
        return true;
    }
}
