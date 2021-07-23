/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/service/impl/MvnForumAdServiceImplDefault.java,v 1.6 2009/08/31 03:41:05 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.6 $
 * $Date: 2009/08/31 03:41:05 $
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
package com.mvnsoft.mvnad.service.impl;

import java.io.File;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.service.MvnForumAdService;
import com.mvnsoft.mvnad.delivery.AdGenerator;

public class MvnForumAdServiceImplDefault implements MvnForumAdService {
    
    private static final Logger log = LoggerFactory.getLogger(MvnForumAdServiceImplDefault.class);

    private static final String OPTION_FILE_NAME = "config_ad_forum.xml";
    
    private static int[] zones;
    
    private static int count;
    
    static {
        zones = new int[ZONE_NAME_FIELD_COUNT];
    }

    public MvnForumAdServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public int getAdZone(int zoneName) {
        if (zoneName < 0 || zoneName >= ZONE_NAME_FIELD_COUNT) {
            return 0;
        }
        return zones[zoneName];
    }

    public String getZone(int zoneID) {
        if (zoneID < 1) {
            return "";
        }
        return AdGenerator.getZone(zoneID);
    }

    public void reload() {

        String configFilename = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;

        try {
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));

            zones[ZONE_NAME_FORUM_AD_POSTITION_HEART]  = conf.getInt("adforum.heart", 0);
            
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_1] = conf.getInt("adforum.header11", 0);
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_2] = conf.getInt("adforum.header12", 0);
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_3] = conf.getInt("adforum.header13", 0);
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_4] = conf.getInt("adforum.header14", 0);
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_5] = conf.getInt("adforum.header15", 0);
            zones[ZONE_NAME_FORUM_HEADER_1_POSITION_6] = conf.getInt("adforum.header16", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_1] = conf.getInt("adforum.header21", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_2] = conf.getInt("adforum.header22", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_3] = conf.getInt("adforum.header23", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_4] = conf.getInt("adforum.header24", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_5] = conf.getInt("adforum.header25", 0);
            zones[ZONE_NAME_FORUM_HEADER_2_POSITION_6] = conf.getInt("adforum.header26", 0);
            zones[ZONE_NAME_FORUM_HEADER_3_POSITION_1] = conf.getInt("adforum.header31", 0);
            zones[ZONE_NAME_FORUM_HEADER_3_POSITION_2] = conf.getInt("adforum.header32", 0);
            zones[ZONE_NAME_FORUM_HEADER_3_POSITION_3] = conf.getInt("adforum.header33", 0);

            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_1] = conf.getInt("adforum.footer11", 0);
            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_2] = conf.getInt("adforum.footer12", 0);
            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_3] = conf.getInt("adforum.footer13", 0);
            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_4] = conf.getInt("adforum.footer14", 0);
            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_5] = conf.getInt("adforum.footer15", 0);
            zones[ZONE_NAME_FORUM_FOOTER_1_POSITION_6] = conf.getInt("adforum.footer16", 0);
            
            zones[ZONE_NAME_FORUM_FIRST_POST]          = conf.getInt("adforum.firstpost", 0);
            zones[ZONE_NAME_FORUM_FIRST_POST_PAGE_2]   = conf.getInt("adforum.firstpost2", 0);
            zones[ZONE_NAME_FORUM_LAST_ODD_POST]       = conf.getInt("adforum.lastoddpost", 0);
            zones[ZONE_NAME_FORUM_LAST_EVEN_POST]      = conf.getInt("adforum.lastevenpost", 0);
            zones[ZONE_NAME_FORUM_FIRST_ATTACHEMNT]    = conf.getInt("adforum.firstattachment", 0);
            zones[ZONE_NAME_FORUM_VIEW_MESSAGE]        = conf.getInt("adforum.viewmessage", 0);
            zones[ZONE_NAME_FORUM_FIRST_NORMAL_THREAD] = conf.getInt("adforum.firstnormalthread", 0);
            zones[ZONE_NAME_FORUM_FIRST_ACTIVE_THREAD] = conf.getInt("adforum.firstactivethread", 0);
            zones[ZONE_NAME_FORUM_FIRST_UNANSWERED_THREAD] = conf.getInt("adforum.firstunansweredthread", 0);
            zones[ZONE_NAME_FORUM_FIRST_RECEND_THREAD] = conf.getInt("adforum.firstrecentthread", 0);

        } catch (Exception e) {
            // Please note that for security reason, the full path file name is logged
            // to the log file only. And the reason that show on the web should only
            // show the filename only
            MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldStop("com.mvnforum.MVNForumConfig: Can't read the configuration file: '" + OPTION_FILE_NAME + "'. Make sure the file is in your CLASSPATH");

            log.error("Error", e);
        }
    }
}
