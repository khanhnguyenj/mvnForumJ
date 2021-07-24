/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/ReloadTomcatTimerTaskExt.java,v 1.9 2009/01/02 15:12:54 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.9 $
 * $Date: 2009/01/02 15:12:54 $
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
package com.mvnforum.common;

import java.io.File;

import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReloadTomcatTimerTaskExt extends TimerTaskAbstract implements TimerTaskExt {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(ReloadTomcatTimerTaskExt.class);

    // static variable
    private static ReloadTomcatTimerTaskExt instance = null;

    // instance variables
    private EnvironmentService environmentService;

    // private constructor will prevent any instantiation
    private ReloadTomcatTimerTaskExt() {
    }

    public long getDelay() {
        return DateUtil.MINUTE * 3;
    }

    public long getPeriod() {
        return DateUtil.MINUTE * 1;
    }

    /**
     * This static method is used to get the Singleton instance of WatchTask
     * @return the singleton instance of WatchTask
     */
    public static synchronized ReloadTomcatTimerTaskExt getInstance() {
        if (instance == null) {
            instance = new ReloadTomcatTimerTaskExt();
        }
        return instance;
    }

    public synchronized void schedule() {
        if (scheduled == false) {
            scheduled = true;
            TimerUtil.getInstance().schedule(this, getDelay(), getPeriod());
        }
    }

    public void run() {
        if (environmentService == null) {
            environmentService = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
        }

        if (environmentService.isShouldRun()) {
            return;
        }
        try {
            log.debug("Begin reloading Tomcat in ReloadTomcatTimerTaskExt");
            String classesPath = FileUtil.getServletClassesPath();
            String libPath = classesPath + ".." + File.separatorChar + "lib" + File.separatorChar + "mvnforum.jar";
            FileUtil.touch(libPath);
        } catch (Exception ex) {
            log.error("Error running reloading Tomcat in ReloadTomcatTimerTaskExt", ex);
        }
    }

}
