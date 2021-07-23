/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/service/impl/MvnForumLifeCycleServiceImplDefault.java,v 1.28 2010/07/19 07:34:07 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.28 $
 * $Date: 2010/07/19 07:34:07 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.service.impl;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.info.DatabaseInfo;
import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.common.*;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.WatchBean;
import com.mvnforum.service.MvnForumLifeCycleService;
import com.mvnforum.service.MvnForumServiceFactory;

public class MvnForumLifeCycleServiceImplDefault implements MvnForumLifeCycleService {

    private static final Logger log = LoggerFactory.getLogger(MvnForumLifeCycleServiceImplDefault.class);

    private static boolean called;

    private static int count;

    public MvnForumLifeCycleServiceImplDefault() {
        count++;
        AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
    }

    public static boolean isCalled() {
        return called;
    }

    public void contextInitialized(ServletContextEvent event) {

        log.debug("Begin calling contextInitialized()");
        called = true;

        EnvironmentService environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
        ServletContext context = event.getServletContext();
        
        // The variable loginedUserMap could be use in case of shared session authentication.
        // The other context could get mvnForum context, then access this variable to store the 
        // sessionid and username. We need to write the Authenticator sub-class to handle this map.
        Map loginedUserMap = new TreeMap();
        context.setAttribute(MVNForumGlobal.MVNFORUM_LOGINED_USER_MAP, loginedUserMap);

        // Check if the Servlet is in specs Servlet 2.3 or later
        if (environmentService.isShouldRun()) {
            int majorVersion = context.getMajorVersion();
            int mimorVersion = context.getMinorVersion();
            if ((majorVersion < 2) ||
                ((majorVersion == 2) && (mimorVersion < 3))) {
                environmentService.setShouldStop("mvnForum requires Servlet 2.3 or later. Please upgrade your Servlet Container.");
                return;
            }
        }

        // now check the database
        try {
            DatabaseInfo databaseInfo = new DatabaseInfo();
            if (databaseInfo.getErrorMessage() != null) {
                log.error("Cannot get database connection. Please correct it first.");
                environmentService.setShouldStop("Check your database configuration. Detail : " + databaseInfo.getErrorMessage());
            } else if (databaseInfo.getDatabaseUrl().toLowerCase().startsWith("jdbc:odbc:")) {
                log.error("Does not support JDBC/ODBC driver. Please use other drivers.");
                environmentService.setShouldStop("Does not support JDBC/ODBC driver. Please use other drivers.");
            } else if (DAOFactory.getMemberDAO().getNumberOfMembers() == 0) { // check if no member
                log.error("There are no members in database. Please correct it first.");
                environmentService.setShouldStop("There are no members in database.");
            }
            // call this method will print the database type to logger with level INFO
            DBUtils.getDatabaseType();

            // now check if Guest user is in database or not
            try {
                DAOFactory.getMemberDAO().getMember(MVNForumConstant.MEMBER_ID_OF_GUEST);
                MVNForumConfig.setGuestUserInDatabase(true);
            } catch (ObjectNotFoundException ex) {
                // don't have Guest user in database, just ignore
            } catch (Exception ex) {
                log.info("Error occured when get Guest user.", ex);
            }
        } catch (DatabaseException dbe) {
            log.error("Error while access database. Please correct it first.", dbe);
            environmentService.setShouldStop("Error while access database. Detail : " + dbe.getMessage());
        }

        // schedule the WatchSendTask
        log.debug("Schedule the WatchSendTask");
        if (MVNForumConfig.getEnableWatch()) {
            if (environmentService.isShouldRun()) {
                log.info("Schedule the WatchSendTask for send mail");
                if (MVNForumConfig.getDefaultWatchOption() == WatchBean.WATCH_OPTION_LIVE) {
                    // The default watch is LIVE, so the timer is called more often (5 minutes)
                    WatchSendTask.getInstance().schedule(DateUtil.MINUTE, DateUtil.MINUTE * 5);
                } else {
                    // Other options, we only check the watch hourly
                    WatchSendTask.getInstance().schedule(DateUtil.MINUTE, DateUtil.HOUR);
                }
            }
        } else {
            log.info("Watch is disabled. Do not schedule the WatchSendTask.");
        }

        // schedule the DeleteOrphanPmAttachmentTask
        if (environmentService.isShouldRun()) {
            log.info("Schedule the DeleteOrphanPmAttachmentTask.");
            if (MVNForumConfig.getEnableMessageAttachment()) {
                //Repeated task
                DeleteOrphanPmAttachmentTask.getInstance().schedule(DateUtil.MINUTE, DateUtil.HOUR);
            } else {
                // Try to delete the PmAttachment at least once time
                DeleteOrphanPmAttachmentTask.getInstance().schedule(DateUtil.MINUTE);
            }
            
            // Try to delete expired members in group 
            DeleteExpiredMemberInGroupTask.getInstance().schedule(DateUtil.MINUTE, DateUtil.HOUR);
        }

        List timerTaskExtList = MVNCoreConfig.getTimerTaskExtList();
        Iterator iter = timerTaskExtList.iterator();

        while (iter.hasNext()) {
            TimerTaskExt timerTask = (TimerTaskExt)iter.next();
            timerTask.schedule();
        }
        
        MvnForumServiceFactory.getMvnForumService().getMvnForumAdService();
        MvnForumServiceFactory.getMvnForumService().getMvnForumCMSService();
    }

    public void contextDestroyed(ServletContextEvent event) {
        log.debug("Begin calling contextDestroyed()");
    }

}
