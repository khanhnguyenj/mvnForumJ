/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/admin/ConfigForumWebHandler.java,v 1.6 2009/09/01 11:07:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/09/01 11:07:42 $
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
package com.mvnsoft.mvnad.admin;

import java.io.*;
import java.util.Collection;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.mvnforum.auth.*;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnsoft.mvnad.*;
import com.mvnsoft.mvnad.db.DAOFactoryAd;

public class ConfigForumWebHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfigForumWebHandler.class);
    
    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    
    private static EventLogService eventLogService = MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
    
    private static final String OPTION_FILE_NAME = "config_ad_forum.xml";
    
    public static final String REQUEST_SUCCESS_ATTRIBUTE = "ConfigForumSuccess";


    public void prepare(GenericRequest request) 
        throws AuthenticationException, DatabaseException { 
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();

        permission.ensureCanManageForumAdvertisement();

        Collection zoneBeans = DAOFactoryAd.getZoneDAO().getBeans();

        request.setAttribute("ZoneBeans", zoneBeans);
    }
    
    public void process(GenericRequest request) 
        throws AuthenticationException, DatabaseException, BadInputException, DocumentException, IOException {
        
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
        MVNForumPermission permission = onlineUser.getPermission();
        
        permission.ensureCanManageForumAdvertisement();
        
        SecurityUtil.checkHttpPostMethod(request);
        
        int adHeart = GenericParamUtil.getParameterInt(request, "adHeart", 0);
        int adForum11 = GenericParamUtil.getParameterInt(request, "adForum11", 0);
        int adForum12 = GenericParamUtil.getParameterInt(request, "adForum12", 0);
        int adForum13 = GenericParamUtil.getParameterInt(request, "adForum13", 0);
        int adForum14 = GenericParamUtil.getParameterInt(request, "adForum14", 0);
        int adForum15 = GenericParamUtil.getParameterInt(request, "adForum15", 0);
        int adForum16 = GenericParamUtil.getParameterInt(request, "adForum16", 0);
        int adForum21 = GenericParamUtil.getParameterInt(request, "adForum21", 0);
        int adForum22 = GenericParamUtil.getParameterInt(request, "adForum22", 0);
        int adForum23 = GenericParamUtil.getParameterInt(request, "adForum23", 0);
        int adForum24 = GenericParamUtil.getParameterInt(request, "adForum24", 0);
        int adForum25 = GenericParamUtil.getParameterInt(request, "adForum25", 0);
        int adForum26 = GenericParamUtil.getParameterInt(request, "adForum26", 0);
        int adForum31 = GenericParamUtil.getParameterInt(request, "adForum31", 0);
        int adForum32 = GenericParamUtil.getParameterInt(request, "adForum32", 0);
        int adForum33 = GenericParamUtil.getParameterInt(request, "adForum33", 0);

        int adFooter11 = GenericParamUtil.getParameterInt(request, "adFooter11", 0);
        int adFooter12 = GenericParamUtil.getParameterInt(request, "adFooter12", 0);
        int adFooter13 = GenericParamUtil.getParameterInt(request, "adFooter13", 0);
        int adFooter14 = GenericParamUtil.getParameterInt(request, "adFooter14", 0);
        int adFooter15 = GenericParamUtil.getParameterInt(request, "adFooter15", 0);
        int adFooter16 = GenericParamUtil.getParameterInt(request, "adFooter16", 0);
        
        int adFirstPost = GenericParamUtil.getParameterInt(request, "adFirstPost", 0);
        int adFirstPost2 = GenericParamUtil.getParameterInt(request, "adFirstPost2", 0);
        int adLastOddPost = GenericParamUtil.getParameterInt(request, "adLastOddPost", 0);
        int adLastEvenPost = GenericParamUtil.getParameterInt(request, "adLastEvenPost", 0);
        int adViewMessage = GenericParamUtil.getParameterInt(request, "adViewMessage", 0);
        int adFirstAttachment = GenericParamUtil.getParameterInt(request, "adFirstAttachment", 0);
        int adFirstNormalThread = GenericParamUtil.getParameterInt(request, "adFirstNormalThread", 0);
        int adFirstActiveThread = GenericParamUtil.getParameterInt(request, "adFirstActiveThread", 0);
        int adFirstUnansweredThread = GenericParamUtil.getParameterInt(request, "adFirstUnansweredThread", 0);
        int adFirstRecentThread = GenericParamUtil.getParameterInt(request, "adFirstRecentThread", 0);

        String strPathName = FileUtil.getServletClassesPath();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(strPathName + OPTION_FILE_NAME));
        Element root = document.getRootElement();
        String rootName = "/" + root.getName() + "/";
        
        XMLUtil.updateNode(root, rootName + "adforum/heart", String.valueOf(adHeart));
        XMLUtil.updateNode(root, rootName + "adforum/header11", String.valueOf(adForum11));
        XMLUtil.updateNode(root, rootName + "adforum/header12", String.valueOf(adForum12));
        XMLUtil.updateNode(root, rootName + "adforum/header13", String.valueOf(adForum13));
        XMLUtil.updateNode(root, rootName + "adforum/header14", String.valueOf(adForum14));
        XMLUtil.updateNode(root, rootName + "adforum/header15", String.valueOf(adForum15));
        XMLUtil.updateNode(root, rootName + "adforum/header16", String.valueOf(adForum16));
        XMLUtil.updateNode(root, rootName + "adforum/header21", String.valueOf(adForum21));
        XMLUtil.updateNode(root, rootName + "adforum/header22", String.valueOf(adForum22));
        XMLUtil.updateNode(root, rootName + "adforum/header23", String.valueOf(adForum23));
        XMLUtil.updateNode(root, rootName + "adforum/header24", String.valueOf(adForum24));
        XMLUtil.updateNode(root, rootName + "adforum/header25", String.valueOf(adForum25));
        XMLUtil.updateNode(root, rootName + "adforum/header26", String.valueOf(adForum26));
        XMLUtil.updateNode(root, rootName + "adforum/header31", String.valueOf(adForum31));
        XMLUtil.updateNode(root, rootName + "adforum/header32", String.valueOf(adForum32));
        XMLUtil.updateNode(root, rootName + "adforum/header33", String.valueOf(adForum33));
        
        XMLUtil.updateNode(root, rootName + "adforum/footer11", String.valueOf(adFooter11));
        XMLUtil.updateNode(root, rootName + "adforum/footer12", String.valueOf(adFooter12));
        XMLUtil.updateNode(root, rootName + "adforum/footer13", String.valueOf(adFooter13));
        XMLUtil.updateNode(root, rootName + "adforum/footer14", String.valueOf(adFooter14));
        XMLUtil.updateNode(root, rootName + "adforum/footer15", String.valueOf(adFooter15));
        XMLUtil.updateNode(root, rootName + "adforum/footer16", String.valueOf(adFooter16));
        
        XMLUtil.updateNode(root, rootName + "adforum/firstpost", String.valueOf(adFirstPost));
        XMLUtil.updateNode(root, rootName + "adforum/firstpost2", String.valueOf(adFirstPost2));
        XMLUtil.updateNode(root, rootName + "adforum/lastoddpost", String.valueOf(adLastOddPost));
        XMLUtil.updateNode(root, rootName + "adforum/lastevenpost", String.valueOf(adLastEvenPost));
        XMLUtil.updateNode(root, rootName + "adforum/firstattachment", String.valueOf(adFirstAttachment));
        XMLUtil.updateNode(root, rootName + "adforum/viewmessage", String.valueOf(adViewMessage));
        XMLUtil.updateNode(root, rootName + "adforum/firstnormalthread", String.valueOf(adFirstNormalThread));
        XMLUtil.updateNode(root, rootName + "adforum/firstactivethread", String.valueOf(adFirstActiveThread));
        XMLUtil.updateNode(root, rootName + "adforum/firstunansweredthread", String.valueOf(adFirstUnansweredThread));
        XMLUtil.updateNode(root, rootName + "adforum/firstrecentthread", String.valueOf(adFirstRecentThread));
        XMLUtil.updateNode(root, rootName + "adforum/viewmessage", String.valueOf(adViewMessage));
        
        saveDocument(document, strPathName + OPTION_FILE_NAME);
        
        MvnForumServiceFactory.getMvnForumService().getMvnForumAdService().reload();
        
        log.info("Update zone configuration for mvnForum successfully.");
        
        String actionDesc = MVNAdResourceBundle.getString(MVNAdConfig.getEventLogLocale(), "mvnad.eventlog.desc.update_forum_zone_config");
        eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(), MVNAdConstant.EVENT_LOG_MAIN_MODULE, MVNAdConstant.EVENT_LOG_SUB_MODULE_ADMIN, "update forum zone configuration", actionDesc, EventLogService.LOW);

        request.setAttribute(REQUEST_SUCCESS_ATTRIBUTE, String.valueOf(true));
    }

    private void saveDocument(Document doc, String fileName) throws IOException {

        XMLWriter writer = new XMLWriter(new FileWriter(fileName));
        writer.write(doc);
        writer.close();
        
    }

}
