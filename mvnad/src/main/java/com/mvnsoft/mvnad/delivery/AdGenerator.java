/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/delivery/AdGenerator.java,v 1.13 2009/12/04 07:08:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/12/04 07:08:54 $
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

import java.sql.Timestamp;
import java.util.Map;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.ParamUtil;

import com.mvnsoft.mvnad.AdModuleUtils;
import com.mvnsoft.mvnad.db.ZoneBean;

public final class AdGenerator {
    
    public static final String SERVER_URL;
    public static final String MVNAD_PARAM = "mvnadparam";
    
    static {
        StringBuffer url = new StringBuffer(128);
        url.append(ParamUtil.getServerPath()).append(ParamUtil.getContextPath()).append(DeliveryModuleConfig.getUrlPattern());
        SERVER_URL = url.toString();
    }
    
    private AdGenerator() {
    }
    
    public static String getZone(int zoneID) {
        StringBuffer javaScriptCode = new StringBuffer(1024);
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        ZoneManager zoneManager = ZoneManager.getInstance(now, zoneID);
        ZoneBean zoneBean = zoneManager.getZoneBean();
        
        if (zoneBean == null) {
            return "";
        }
        
        if (zoneBean.getZoneType() == ZoneBean.ZONE_TYPE_DIRECT_CODE) {
            try {
                return zoneManager.getBannerInZone();
            } catch (Exception e) {
                return "";
            }
        }

        javaScriptCode.append("<script type=\"text/javascript\" language=\"JavaScript\" src=\"");
        javaScriptCode.append(SERVER_URL).append("/getparam.js").append("\"");
        javaScriptCode.append("></script>\n");
        javaScriptCode.append("<script type=\"text/javascript\" language=\"JavaScript\">\n");
        javaScriptCode.append("<!--\n");
        javaScriptCode.append("document.write(\"<\"+\"script language='JavaScript' type='text/javascript' src='");
        javaScriptCode.append(SERVER_URL).append("/getad?zoneID=").append(zoneID);
        javaScriptCode.append("\"+").append(MVNAD_PARAM).append("+\"");
        javaScriptCode.append("'>").append("<\"+\"/script>\");\n").append("//-->\n").append("</script>\n");
        
        return javaScriptCode.toString();
    }

    public static String getZoneCode(int zoneID) {
        StringBuffer javaScriptCode = new StringBuffer(1024);
        
        Timestamp now = DateUtil.getCurrentGMTTimestamp();
        ZoneManager zoneManager = ZoneManager.getInstance(now, zoneID);
        ZoneBean zoneBean = zoneManager.getZoneBean();
        
        if (zoneBean == null) {
            return "";
        }
        
        javaScriptCode.append("<script type=\"text/javascript\" language=\"JavaScript\" src=\"");
        javaScriptCode.append(SERVER_URL).append("/getparam.js").append("\"");
        javaScriptCode.append("></script>\n");
        javaScriptCode.append("<script type=\"text/javascript\" language=\"JavaScript\">\n");
        javaScriptCode.append("<!--\n");
        javaScriptCode.append("document.write(\"<\"+\"script language='JavaScript' type='text/javascript' src='");
        javaScriptCode.append(SERVER_URL).append("/getad?zoneID=").append(zoneID);
        javaScriptCode.append("\"+").append(MVNAD_PARAM).append("+\"");
        javaScriptCode.append("'>");
        javaScriptCode.append("<\"+\"/script>\");\n");
        javaScriptCode.append("//-->\n");
        javaScriptCode.append("</script>\n");
        
        return javaScriptCode.toString();
    }
    
    public static String getZoneInObject(int zoneID, String objectType, int objectID) {
        
        StringBuffer javaScriptCode = new StringBuffer(1024);
        
        javaScriptCode.append("<script language='JavaScript' type='text/javascript'>\n");
        javaScriptCode.append("<!--\n");
        javaScriptCode.append("document.write(\"<\" + \"script language='JavaScript' type='text/javascript' src='\");\n");
        javaScriptCode.append("document.write(\"").append(SERVER_URL).append("/getad?zoneID=").append(zoneID).append("&objectType=").append(objectType).append("&").append(objectType).append("ID=").append(objectID).append("\");\n");
        javaScriptCode.append("document.write(\"'><\" + \"/script>\");\n");
        javaScriptCode.append("//-->\n");
        javaScriptCode.append("</script>\n");
        
        return javaScriptCode.toString();
    }
    
    public static String getTableAd(Timestamp now, int zoneID, String objectType, int objectID, Map map) 
        throws DatabaseException, ObjectNotFoundException {
        
        ZoneManager zoneManager;
        if ((objectID == 0) || ("".equals(objectType))) {
            zoneManager = ZoneManager.getInstance(now, zoneID);
        } else {
            zoneManager = ZoneManager.getInstance(now, zoneID, objectType, objectID);
        }
        
        ZoneBean zoneBean = zoneManager.getZoneBean();
        if (zoneBean == null) {
            return "";
        }

        StringBuffer result = new StringBuffer(1024);
        
        if (zoneBean.getZoneAutoReloadTime() == ZoneBean.ZONE_NOT_AUTO_RELOAD) {
            if (zoneBean.getZoneType() == ZoneBean.ZONE_TYPE_DIRECT_CODE) {
                try {
                    return zoneManager.getBannerInZone(map);
                } catch (Exception e) {
                    return "";
                }
            } else {
                result.append("<table cellspacing=\"0\" cellpadding=\"0\" class=\"noborder\" border=\"0\">");
                for (int i = 0; i < zoneBean.getZoneCellVerticalCount(); i++) {
                    result.append("<tr>");
                    
                    for (int j = 0; j < zoneBean.getZoneCellHorizontalCount(); j++) {
                        result.append("<td width=\"" + zoneBean.getZoneCellWidth() + "\" height=\"" + zoneBean.getZoneCellHeight() + "\">");
                        result.append(zoneManager.getBannerInZone(map));
                        result.append("</td>");
                    }
                    
                    result.append("</tr>");
                }
                result.append("</table>");
            }
        } else {
            result.append("<iframe src='").append(SERVER_URL).append("/getadframe?zoneID=").append(zoneID);
            AdModuleUtils.appendParams(result, map);
            result.append("' width='").append(zoneBean.getZoneCellWidth()).append("' height='").append(zoneBean.getZoneCellHeight()).append("' frameborder='0' scrolling='no' marginheight='0' marginwidth='0'>");
            result.append("</iframe>");
        }

        return result.toString();
    }

    public static String getFrameAd(Timestamp now, int zoneID, String objectType, int objectID, Map map) 
        throws DatabaseException, ObjectNotFoundException {
    
        ZoneManager zoneManager;
        if ((objectID == 0) || ("".equals(objectType))) {
            zoneManager = ZoneManager.getInstance(now, zoneID);
        } else {
            zoneManager = ZoneManager.getInstance(now, zoneID, objectType, objectID);
        }
        
        ZoneBean zoneBean = zoneManager.getZoneBean();
        if (zoneBean == null) {
            return "";
        }
    
        StringBuffer result = new StringBuffer(1024);
        
        result.append("<table cellspacing=\"0\" cellpadding=\"0\" class=\"noborder\" border=\"0\">");
        for (int i = 0; i < zoneBean.getZoneCellVerticalCount(); i++) {
            result.append("<tr>");
            
            for (int j = 0; j < zoneBean.getZoneCellHorizontalCount(); j++) {
                result.append("<td width=\"" + zoneBean.getZoneCellWidth() + "\" height=\"" + zoneBean.getZoneCellHeight() + "\">");
                result.append(zoneManager.getBannerInZone(map));
                result.append("</td>");
            }
            
            result.append("</tr>");
        }
        result.append("</table>");
        
        return result.toString();
    }

    public static String getAutoReloadFrame(Timestamp now, int zoneID, Map map) 
        throws DatabaseException, ObjectNotFoundException {
        
        ZoneManager zoneManager = ZoneManager.getInstance(now, zoneID);
        ZoneBean zoneBean = zoneManager.getZoneBean();
        
        if (zoneBean == null) {
            return "";
        }

        StringBuffer text = new StringBuffer(1024);
        if (zoneBean.getZoneAutoReloadTime() == 0) {
            return zoneManager.getBannerInZone(map);
        }
        text.append("<meta http-equiv=\"refresh\" content=\"" + zoneBean.getZoneAutoReloadTime() + "\">");
        text.append("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">");
        text.append("<meta http-equiv=\"Pragma\" content=\"no-cache\">");
        text.append("<meta http-equiv=\"Expires\" content=\"-1\">");
        text.append("<title>" + zoneBean.getZoneDesc() + "</title>");
        text.append("<body leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" bottommargin=\"0\" rightmargin=\"0\" marginheight=\"0\">");
        text.append("<base target=\"top\"><div id=\"Adbanner\" style=\"position:absolute;\">");
        if (zoneBean.getZoneType() == ZoneBean.ZONE_TYPE_DIRECT_CODE) {
            try {
                text.append(zoneManager.getBannerInZone(map));
            } catch (Exception e) {
                // do nothing
            }
        } else {
            text.append(getFrameAd(now, zoneID, "", 0, map));
        }
        text.append("</div></body>");
        
        return text.toString();
    }
}