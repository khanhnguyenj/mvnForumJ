/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/service/impl/IPFilterServiceImplDefault.java,v 1.12 2009/01/01 18:30:12 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.12 $
 * $Date: 2009/01/01 18:30:12 $
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
package net.myvietnam.mvncore.service.impl;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.service.IPFilterService;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.StringUtil;

import org.apache.regexp.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IPFilterServiceImplDefault implements IPFilterService {

    private static final Logger log = LoggerFactory.getLogger(IPFilterServiceImplDefault.class);

    private static RE[] blockedIPs = null;

    private static int count;
    
    public IPFilterServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }   

    static {
        String blockedIPAddresses = "";
        String[] blockedIPArray = StringUtil.getStringArray(blockedIPAddresses, ";");
        blockedIPs = new RE[blockedIPArray.length];
        for (int i = 0; i < blockedIPArray.length; i++) {
            String currentIPRegExp = StringUtil.replace(blockedIPArray[i], '*', "(\\d{1,3})");
            currentIPRegExp = "^" + currentIPRegExp + "$";
            try {
                log.debug("currentIPRegExp = " + currentIPRegExp);
                blockedIPs[i] = new RE(currentIPRegExp);
            } catch (Exception ex) {
                log.error("Cannot parse the regular expression = " + currentIPRegExp, ex);
            }
        }
    }

    /**
     * Filter the IP
     * @param request
     * @return true  if the IP in this request is ok
     *         false if the IP in this request is blocked
     */
    public boolean filter(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Cannot accept a null request in IPFilter");
        }
        String checkIP = request.getRemoteAddr();

        for (int i = 0; i < blockedIPs.length; i++) {
            RE currentBlockedIP = blockedIPs[i];
            if (currentBlockedIP != null) {
                synchronized (currentBlockedIP) {
                    if (currentBlockedIP.match(checkIP)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
