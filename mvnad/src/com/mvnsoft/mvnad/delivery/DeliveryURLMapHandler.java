/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/DeliveryURLMapHandler.java,v 1.4 2008/07/20 08:33:10 tbtrung Exp $
 * $Author: tbtrung $
 * $Revision: 1.4 $
 * $Date: 2008/07/20 08:33:10 $
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
package com.mvnsoft.mvnad.delivery;

import javax.servlet.http.HttpServletRequest;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;

public class DeliveryURLMapHandler {

    public DeliveryURLMapHandler() {
    }

    public URLMap getMap(String requestURI, HttpServletRequest request) throws MissingURLMapEntryException {
        
        URLMap map = new URLMap();

        if (requestURI.equals("/clickad")) {
            String targetURL = (String) request.getAttribute("TargetURL");
            map.setResponse(targetURL);
        } else if (requestURI.equals("/getad")) {
            map.setResponse("/mvnplugin/mvnad/delivery/getad.jsp");
        } else if (requestURI.equals("/getparam.js")) {
            map.setResponse("/mvnplugin/mvnad/delivery/getparam.jsp");
        } else if (requestURI.equals("/getadframe")) {
            map.setResponse("/mvnplugin/mvnad/delivery/getadframe.jsp");
        } else {
            // do no thing
        }

        if (map.getResponse() == null) {
            throw new MissingURLMapEntryException("Cannot map request with URI.");
        }
        
        return map;
    }
}
