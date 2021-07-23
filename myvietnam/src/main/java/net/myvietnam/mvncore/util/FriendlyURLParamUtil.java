/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/util/FriendlyURLParamUtil.java,v 1.8 2009/03/20 03:12:19 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.8 $
 * $Date: 2009/03/20 03:12:19 $
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
 * @author: Phuong Pham Dinh Duy
 */
package net.myvietnam.mvncore.util;

import java.util.*;

public class FriendlyURLParamUtil {
    
    public static final String NO_VALUE = "";
    
    private FriendlyURLParamUtil() {
    }
    
    public static String createFriendlyURL(String url) {
        url = url.replaceAll("&amp;", "&");
        int ampPos = url.indexOf("?");
        if (ampPos != -1) {
            
            int sharpIndex = url.indexOf("#");
            String sharp = null;
            if (sharpIndex != -1) {
                if (sharpIndex + 1 < url.length()) {
                    sharp = url.substring(sharpIndex + 1);
                }
                url = url.substring(0, sharpIndex);
            }
            
            StringBuffer sb = new StringBuffer();
            
            String action = url.substring(0, ampPos);
            sb.append(action).append("_");
            
            String queryString = url.substring(ampPos + 1);
            
            StringTokenizer st = new StringTokenizer(queryString, "&");
            while (st.hasMoreTokens()) {
                String keyValue = st.nextToken();
                int pos = keyValue.indexOf("=");
                if (pos == -1) continue;
                
                String key = keyValue.substring(0, pos);
                String value = keyValue.substring(pos + 1);
                
                sb.append(key).append(",").append(value);
                if (st.hasMoreTokens()) {
                    sb.append("_"); 
                }
            }
            if (sharp != null) {
                sb.append("#").append(sharp);
            }
            return sb.toString();
        }
        return url;
    }
    
    public static String getURLFromFriendlyURL(String friendlyURL) {
        StringBuffer url = new StringBuffer();
        int indexOf_ = friendlyURL.indexOf("_");
        if (indexOf_ == -1) {
            return friendlyURL;
        }
        
        url.append(friendlyURL.substring(0, indexOf_));

        StringTokenizer st = new StringTokenizer(friendlyURL.substring(indexOf_ + 1), "_");
        boolean firstToken = true;
        while(st.hasMoreTokens()) {
            String keyValue = st.nextToken();
            if (firstToken) {
                url.append("?");
            } else {
                url.append("&");
            }
            int pos = keyValue.indexOf(",");
            if (pos != -1) {
                String key = keyValue.substring(0, pos);
                String value = keyValue.substring(pos + 1);
                url.append(key).append("=").append(value);
            } else {
                url.append(keyValue).append("=").append(FriendlyURLParamUtil.NO_VALUE);
            }
            firstToken = false;
        }
        return url.toString();
    }

    public static Map getParameters(String requestURI) {
        
        Map params = new HashMap();
        
        if (requestURI == null) {
            return params;
        }

        String requestURITMP = requestURI;
        if (requestURI.startsWith("/")) {
            requestURITMP = requestURI.substring(1);//remove "/" at the beginning of requestURI
        }
        
        StringTokenizer st = new StringTokenizer(requestURITMP, "_");
        while(st.hasMoreTokens()) {
            String keyValue = st.nextToken();
            int indexOfComma = keyValue.indexOf(",");
            if (indexOfComma != -1) {
                String key = keyValue.substring(0, indexOfComma);
                String value = keyValue.substring(indexOfComma + 1);
                params.put(key, value);
            } else {
                params.put(keyValue, NO_VALUE);
            }
        }
        
        return params;
    }
}
