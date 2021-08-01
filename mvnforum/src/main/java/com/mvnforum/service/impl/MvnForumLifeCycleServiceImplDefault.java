package com.mvnforum.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.flywaydb.core.Flyway;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.common.DeleteExpiredMemberInGroupTask;
import com.mvnforum.common.DeleteOrphanPmAttachmentTask;
import com.mvnforum.common.WatchSendTask;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.WatchBean;
import com.mvnforum.service.MvnForumLifeCycleService;
import com.mvnforum.service.MvnForumServiceFactory;

import lombok.extern.slf4j.Slf4j;
import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.info.DatabaseInfo;
import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.TimerTaskExt;

@Slf4j
public class MvnForumLifeCycleServiceImplDefault implements MvnForumLifeCycleService {

  private static boolean called;

  private static int count;

  public MvnForumLifeCycleServiceImplDefault() {
    count++;
    AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
  }

  public static boolean isCalled() {
    return called;
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    if (log.isDebugEnabled()) {
      log.debug("Begin calling contextInitialized()");
    }
    called = true;

    EnvironmentService environmentService =
        MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService();
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
      if ((majorVersion < 2) || ((majorVersion == 2) && (mimorVersion < 3))) {
        environmentService.setShouldStop(
            "mvnForum requires Servlet 2.3 or later. Please upgrade your Servlet Container.");
        return;
      }
    }

    // Do database migration
    runDatabaseMigration();

    // now check the database
    try {
      DatabaseInfo databaseInfo = new DatabaseInfo();
      if (databaseInfo.getErrorMessage() != null) {
        log.error("Cannot get database connection. Please correct it first.");
        environmentService.setShouldStop(
            "Check your database configuration. Detail : " + databaseInfo.getErrorMessage());
      } else if (databaseInfo.getDatabaseUrl().toLowerCase().startsWith("jdbc:odbc:")) {
        log.error("Does not support JDBC/ODBC driver. Please use other drivers.");
        environmentService.setShouldStop(
            "Does not support JDBC/ODBC driver. Please use other drivers.");
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
        // Repeated task
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
      TimerTaskExt timerTask = (TimerTaskExt) iter.next();
      timerTask.schedule();
    }

    MvnForumServiceFactory.getMvnForumService().getMvnForumAdService();
    MvnForumServiceFactory.getMvnForumService().getMvnForumCMSService();
  }

  private void runDatabaseMigration() {
    String url = MVNCoreConfig.getDatabaseURL();
    String user = MVNCoreConfig.getDatabaseUser();
    String password = MVNCoreConfig.getDatabasePassword();

    Flyway flyway = Flyway.configure()
        .dataSource(url, user, password)
        .failOnMissingLocations(true)
        .load();

    flyway.migrate();
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    log.debug("Begin calling contextDestroyed()");
  }
}
