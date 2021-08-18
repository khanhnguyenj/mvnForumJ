/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/AdminModuleProcessor.java,v 1.157
 * 2009/10/14 10:29:14 trungth Exp $ $Author: trungth $ $Revision: 1.157 $ $Date: 2009/10/14
 * 10:29:14 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.admin;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.Action;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.service.ModuleProcessor;
import com.mvnforum.user.UserModuleConfig;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.exception.NotLoginException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.AssertionUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.ParamUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;
import net.myvietnam.mvncore.web.impl.GenericRequestServletImpl;
import net.myvietnam.mvncore.web.impl.GenericResponseServletImpl;

public class AdminModuleProcessor implements ModuleProcessor {

  private static final Logger log = LoggerFactory.getLogger(AdminModuleProcessor.class);

  protected static final String ORIGINAL_REQUEST = "mvnforum.admin.OriginalRequest";

  private static int count;

  private HttpServlet adminServlet = null;

  protected OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
  private AdminModuleURLMapHandler urlMapHandler = new AdminModuleURLMapHandler();

  private ForumWebHandler forumWebHandler = new ForumWebHandler();
  private CategoryWebHandler categoryWebHandler = new CategoryWebHandler();
  private MemberWebHandler memberWebHandler = new MemberWebHandler();
  private WatchWebHandler watchWebHandler = new WatchWebHandler();
  private GroupsWebHandler groupsWebHandler = new GroupsWebHandler();
  private MemberGroupWebHandler memberGroupWebHandler = new MemberGroupWebHandler();
  private GroupPermissionWebHandler groupPermissionWebHandler = new GroupPermissionWebHandler();
  private GroupForumWebHandler groupForumWebHandler = new GroupForumWebHandler();
  private RankWebHandler rankWebHandler = new RankWebHandler();
  private MemberPermissionWebHandler memberPermissionWebHandler = new MemberPermissionWebHandler();
  private MemberForumWebHandler memberForumWebHandler = new MemberForumWebHandler();
  private GeneralAdminTasksWebHandler generalAdminTasksWebHandler =
      new GeneralAdminTasksWebHandler();
  private ConfigurationWebHandler configurationWebHandler = new ConfigurationWebHandler();
  private CssTaskWebHandler cssTaskWebHandler = new CssTaskWebHandler();

  protected ServletContext servletContext = null;

  public AdminModuleProcessor() {
    count++;
    AssertionUtil.doAssert(count == 1, "Assertion: Must have only one instance.");
  }

  @Override
  public void setServlet(HttpServlet servlet) {
    this.adminServlet = servlet;
    servletContext = adminServlet.getServletContext();
  }

  /**
   * This method handles the <code>requestURI</code>, and invokes the needed procedure (if the
   * current user has the permission to perform that task). <br/>
   * For example, URI <code>"/addforumprocess"</code> invokes the call to
   * <code>forumWebHandler.processAdd(request);</code>.<br/>
   * After the task is performed, we use the <code>AdminModuleURLMapHandler</code> to get the
   * <code>responseURI</code>. That <code>responseURI</code> is returned back to the calling method,
   * so it can decide and act on it (to redirect to that URI).<br/>
   *
   * @param request The <code>HttpServletRequest</code> object of this HTTP request.
   * @param response The <code>HttpServletResponse</code> object of this HTTP request.
   * @return responseURI to be redirected to. <b>It could be null</b>, which means we are not
   *         supposed to do any redirection, since the output was already committed (for example, if
   *         we sent (downloaded) a file to the user.
   */
  @Override
  public String process(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    long start = 0;
    String requestURI = StringUtil.getEmptyStringIfNull(request.getPathInfo());
    String responseURI = null;
    OnlineUser onlineUser = null;
    if (log.isDebugEnabled()) {
      start = System.currentTimeMillis();
      log.debug("AdminModuleProcessor : requestURI  = " + requestURI);
    }

    GenericRequest genericRequest = new GenericRequestServletImpl(request, servletContext);
    GenericResponse genericResponse = new GenericResponseServletImpl(response);

    // step 1: some command need to be processed before we do the URI mapping (of the MODAL)
    try {
      // always check for Authenticated User
      // @todo could throw Exception, so onlineUser will be null, caused NPE later
      onlineUser = onlineUserManager.getOnlineUser(request);
      onlineUser.updateNewMessageCount(false);

      MVNForumPermission permission = onlineUser.getPermission();
      if (!requestURI.equals("") && !requestURI.equals("/") && !requestURI.equals("/login")
          && !requestURI.equals("/loginprocess") && !requestURI.equals("/logout")) {
        permission.ensureIsAuthenticated();
        if (onlineUser.getAuthenticationType() == OnlineUser.AUTHENTICATION_TYPE_COOKIE) {
          throw new AuthenticationException(NotLoginException.COOKIE_NOT_ALLOWED);
        }
      }

      if (requestURI.equals("/forummanagement")) {
        forumWebHandler.prepareForumManagement(genericRequest, genericResponse);
      } else if (requestURI.equals("/editgroupforumpermission")) {
        groupForumWebHandler.prepareList(genericRequest);
      } else if (requestURI.equals("/updategroupforumpermission")) {
        groupForumWebHandler.processUpdate(genericRequest);

      } else if (requestURI.equals("/addforum")) {
        forumWebHandler.prepareAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/addforumprocess")) {
        forumWebHandler.processAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/deleteforum")) {
        forumWebHandler.prepareDelete(genericRequest);
      } else if (requestURI.equals("/deleteforumprocess")) {
        forumWebHandler.processDelete(genericRequest);
      } else if (requestURI.equals("/editforum")) {
        forumWebHandler.prepareEdit(genericRequest, genericResponse);
      } else if (requestURI.equals("/updateforum")) {
        forumWebHandler.processUpdate(genericRequest, genericResponse);
      } else if (requestURI.equals("/updateforumorder")) {
        forumWebHandler.processUpdateForumOrder(genericRequest);

      } else if (requestURI.equals("/addcategory")) {
        categoryWebHandler.prepareAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/addcategoryprocess")) {
        categoryWebHandler.processAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/deletecategory")) {
        categoryWebHandler.prepareDelete(genericRequest);
      } else if (requestURI.equals("/deletecategoryprocess")) {
        categoryWebHandler.processDelete(genericRequest);
      } else if (requestURI.equals("/editcategory")) {
        categoryWebHandler.prepareEdit(genericRequest);
      } else if (requestURI.equals("/updatecategory")) {
        categoryWebHandler.processUpdate(genericRequest, genericResponse);
      } else if (requestURI.equals("/updatecategoryorder")) {
        categoryWebHandler.processUpdateCategoryOrder(genericRequest);

      } else if (requestURI.equals("/rankmanagement")) {
        rankWebHandler.prepareList(genericRequest);
      } else if (requestURI.equals("/editrank")) {
        rankWebHandler.prepareEdit(genericRequest);
      } else if (requestURI.equals("/editrankprocess")) {
        rankWebHandler.processUpdate(genericRequest, genericResponse);
      } else if (requestURI.equals("/addrankprocess")) {
        rankWebHandler.processAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/deleterankprocess")) {
        rankWebHandler.processDelete(genericRequest);

      } else if (requestURI.equals("/usermanagement")) {
        memberWebHandler.prepareShowUserManagement(genericRequest);
      } else if (requestURI.equals("/permissionsummary")) {
        memberWebHandler.preparePermissionSummary(genericRequest);
      } else if (requestURI.equals("/viewmemberpermissions")) {
        memberWebHandler.prepareViewPermission(genericRequest);
      } else if (requestURI.equals("/deletenonactivatedmembers")) {
        memberWebHandler.prepareDeleteNonActivatedNoPostMembers(genericRequest);
      } else if (requestURI.equals("/deletenonactivatedmembersprocess")) {
        memberWebHandler
            .processDeleteNonActivatedNoPostMembers(/* servletContext, */ genericRequest);
      } else if (requestURI.equals("/listpendingusers")) {
        memberWebHandler.prepareListPendingUsers(genericRequest);
      } else if (requestURI.equals("/listpendingusersprocess")) {
        memberWebHandler.processListPendingUsers(genericRequest);
      } else if (requestURI.equals("/addmember")) {
        memberWebHandler.prepareAdd(genericRequest);
      } else if (requestURI.equals("/addmemberprocess")) {
        memberWebHandler.processAdd(genericRequest);
      } else if (requestURI.equals("/changememberstatusprocess")) {
        memberWebHandler.processUpdateMemberStatus(genericRequest);
      } else if (requestURI.equals("/viewmember")) {
        memberWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/editmember")) {
        memberWebHandler.prepareEdit(genericRequest);
      } else if (requestURI.equals("/updatemember")) {
        memberWebHandler.processEdit(genericRequest, genericResponse);
      } else if (requestURI.equals("/changepassword")) {
        memberWebHandler.prepareChangePassword(genericRequest);
      } else if (requestURI.equals("/changepasswordprocess")) {
        memberWebHandler.processChangePassword(genericRequest);
      } else if (requestURI.equals("/editmembertitle")) {
        memberWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/editmembertitleprocess")) {
        memberWebHandler.processUpdateMemberTitle(genericRequest, genericResponse);
      } else if (requestURI.equals("/resetsignatureprocess")) {
        memberWebHandler.processResetMemberSignature(genericRequest);
      } else if (requestURI.equals("/resetavatarprocess")) {
        memberWebHandler.processResetMemberAvatar(genericRequest);
      } else if (requestURI.equals("/resetactivationprocess")) {
        memberWebHandler.processResetMemberActivation(genericRequest);
      } else if (requestURI.equals("/deletewatch")) {
        watchWebHandler.processDelete_forMember(genericRequest);

      } else if (requestURI.equals("/addgroupprocess")) {
        groupsWebHandler.processAdd(genericRequest, genericResponse);
      } else if (requestURI.equals("/deletegroup")) {
        groupsWebHandler.prepareDelete(genericRequest);
      } else if (requestURI.equals("/deletegroupprocess")) {
        groupsWebHandler.processDelete(genericRequest);
      } else if (requestURI.equals("/groupmanagement")) {
        groupsWebHandler.prepareList(genericRequest);
      } else if (requestURI.equals("/viewgroup")) {
        groupsWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/editgroupinfo")) {
        groupsWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/updategroupinfo")) {
        groupsWebHandler.processUpdate(genericRequest, genericResponse);
      } else if (requestURI.equals("/editgroupowner")) {
        groupsWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/updategroupowner")) {
        groupsWebHandler.processUpdateGroupOwner(genericRequest);
      } else if (requestURI.equals("/listmembergroup")) {
        memberGroupWebHandler.prepareList_inGroup_limit(genericRequest);
      } else if (requestURI.equals("/addmembergroup")) {
        groupsWebHandler.prepareView(genericRequest);
      } else if (requestURI.equals("/addmembergroupprocess")) {
        memberGroupWebHandler.processAdd(genericRequest);
      } else if (requestURI.equals("/editmembergroup")) {
        memberGroupWebHandler.prepareEdit(genericRequest);
      } else if (requestURI.equals("/editmembergroupprocess")) {
        memberGroupWebHandler.processUpdate(genericRequest);
      } else if (requestURI.equals("/deletemembergroupprocess")) {
        memberGroupWebHandler.processDelete(genericRequest);
      } else if (requestURI.equals("/editgrouppermission")) {
        groupPermissionWebHandler.prepareList(genericRequest);
      } else if (requestURI.equals("/updategrouppermission")) {
        groupPermissionWebHandler.processUpdate(genericRequest);

      } else if (requestURI.equals("/assignforumtogroup")) {
        groupForumWebHandler.prepareAssignForumToGroup(genericRequest, genericResponse);
      } else if (requestURI.equals("/assigngrouptoforum")) {
        groupForumWebHandler.prepareAssignGroupToForum(genericRequest);
      } else if (requestURI.equals("/assignforumtomember")) {
        memberForumWebHandler.prepareAssignForumToMember(genericRequest, genericResponse);
      } else if (requestURI.equals("/assignmembertoforum")) {
        memberForumWebHandler.prepareAssignMemberToForum(genericRequest);
      } else if (requestURI.equals("/editmemberforumpermission")) {
        memberForumWebHandler.prepareList(genericRequest);
      } else if (requestURI.equals("/updatememberforumpermission")) {
        memberForumWebHandler.processUpdate(genericRequest);
      } else if (requestURI.equals("/editmemberpermission")) {
        memberPermissionWebHandler.prepareListPermission(genericRequest);
      } else if (requestURI.equals("/updatememberpermission")) {
        memberPermissionWebHandler.processUpdate(genericRequest);
      } else if (requestURI.equals("/deletemember")) {
        memberWebHandler.prepareDeleteMember(genericRequest);
      } else if (requestURI.equals("/deletememberprocess")) {
        memberWebHandler.processDeleteMember(/* servletContext, */ genericRequest);

      } else if (requestURI.equals("/edittemplate")) {
        configurationWebHandler.prepareEditTemplate(genericRequest);
      } else if (requestURI.equals("/updatetemplate")) {
        configurationWebHandler.processEditTemplate(genericRequest);

      } else if (requestURI.equals("/editcss")) {
        cssTaskWebHandler.prepareEditCSS(genericRequest);
      } else if (requestURI.equals("/updatecss")) {
        cssTaskWebHandler.processEditCSS(genericRequest);
        cssTaskWebHandler.processEditCSS_forRender(genericRequest);
      } else if (requestURI.equals("/restorecssprocess")) {
        cssTaskWebHandler.processRestoreCSS(genericRequest);

      } else if (requestURI.equals("/viewlogsystem")) {
        generalAdminTasksWebHandler.prepareViewLogSystem(genericRequest);
      } else if (requestURI.equals("/logframe")) {
        generalAdminTasksWebHandler.prepareLogFrame(genericRequest);
      } else if (requestURI.equals("/backupsystemlog")) {
        generalAdminTasksWebHandler.backupSystemLog(genericRequest);
      } else if (requestURI.equals("/listlogfiles")) {
        generalAdminTasksWebHandler.prepareListLogFiles(genericRequest);
      } else if (requestURI.equals("/downloadlogfile")) {
        generalAdminTasksWebHandler.downloadLogFile(request, response);
        return null;// already committed some messages, no further process is needed
      } else if (requestURI.equals("/deletelogfile")) {
        generalAdminTasksWebHandler.deleteLogFile(genericRequest);

      } else if (requestURI.equals("/configindex")) {
        configurationWebHandler.prepareConfigMVNCore(genericRequest);
        configurationWebHandler.prepareConfigMVNForum(genericRequest);
      } else if (requestURI.equals("/configstepone")) {
        configurationWebHandler.prepareConfigMVNForum(genericRequest);
      } else if (requestURI.equals("/configsteponeprocess")) {
        configurationWebHandler.updateConfigStepOne(genericRequest);
      } else if (requestURI.equals("/configsteptwo")) {
        configurationWebHandler.prepareConfigMVNForum(genericRequest);
      } else if (requestURI.equals("/configsteptwoprocess")) {
        configurationWebHandler.updateConfigStepTwo(genericRequest);
      } else if (requestURI.equals("/configstepthree")) {
        configurationWebHandler.prepareConfigMVNForum(genericRequest);
      } else if (requestURI.equals("/configstepthreeprocess")) {
        configurationWebHandler.updateConfigStepThree(genericRequest);
      } else if (requestURI.equals("/configmvncore")) {
        configurationWebHandler.prepareConfigMVNCore(genericRequest);
      } else if (requestURI.equals("/configmvncoreprocess")) {
        configurationWebHandler.updateConfigMVNCore(genericRequest);
      } else if (requestURI.equals("/configurlpattern")) {
        configurationWebHandler.prepareConfigMVNForum(genericRequest);
      } else if (requestURI.equals("/configurlpatternprocess")) {
        configurationWebHandler.updateUrlPattern(genericRequest);
      } else if (requestURI.equals("/configbackupprocess")) {
        configurationWebHandler.configBackupProcess(genericRequest);
      } else if (requestURI.equals("/commitconfigs")) {
        configurationWebHandler.commitConfig(genericRequest);

      } else if (requestURI.equals("/restoreconfigbackupprocess")) {
        // configurationWebHandler.configBackupProcess();

      } else if (requestURI.equals("/index")) {
        generalAdminTasksWebHandler.prepareShowIndex(genericRequest);
      } else if (requestURI.equals("/changemode")) {
        generalAdminTasksWebHandler.changeShowUserArea(genericRequest);
      } else if (requestURI.equals("/clearcache")) {
        generalAdminTasksWebHandler.processClearCache(genericRequest);
      } else if (requestURI.equals("/testsystem")) {
        generalAdminTasksWebHandler.prepareTestSystem(genericRequest);
      } else if (requestURI.equals("/importexport")) {
        // generalAdminTasksWebHandler.prepareImportExport(request);
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.importexport_is_disabled");
        throw new IllegalStateException(localizedMessage);
      } else if (requestURI.equals("/importprocess")) {
        // generalAdminTasksWebHandler.importXmlZip(request, response);
        // return null;//already commited some messages, no further process is needed
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.importexport_is_disabled");
        throw new IllegalStateException(localizedMessage);
      } else if (requestURI.equals("/exportprocess")) {
        // generalAdminTasksWebHandler.exportXmlZip(request);
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.importexport_is_disabled");
        throw new IllegalStateException(localizedMessage);
      } else if (requestURI.equals("/getexportprocess")) {
        // generalAdminTasksWebHandler.getExportXmlZip(request, response);
        // return null;//already commited file or raised exception, no further process is needed
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.importexport_is_disabled");
        throw new IllegalStateException(localizedMessage);
      } else if (requestURI.equals("/deleteexportprocess")) {
        // generalAdminTasksWebHandler.deleteExportXmlZip(genericRequest);
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.importexport_is_disabled");
        throw new IllegalStateException(localizedMessage);
      } else if (requestURI.equals("/misctasks")) {
        generalAdminTasksWebHandler.prepareRebuildIndex(genericRequest);
      } else if (requestURI.equals("/rebuildindex")) {
        generalAdminTasksWebHandler.rebuildIndex(genericRequest);
      } else if (requestURI.equals("/sendmail")) {
        generalAdminTasksWebHandler.prepareSendMail(genericRequest);
      } else if (requestURI.equals("/sendmailprocess")) {
        generalAdminTasksWebHandler.sendMail(genericRequest);
      } else if (requestURI.equals("/sendactivatemailtoallprocess")) {
        generalAdminTasksWebHandler.sendActivateMailToAll(genericRequest);

      } else if (requestURI.equals("/searchmember")) {
        memberWebHandler.processSearch(genericRequest);

      } else if (requestURI.equals("/updatememberexpireprocess")) {
        memberWebHandler.updateMemberExpireProcess(genericRequest);
      } else if (requestURI.equals("/listuserexpire")) {
        // memberWebHandler.prepareListUserExpire(genericRequest);
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.list_user_expire_is_disabled");
        throw new IllegalStateException(localizedMessage);

      } else if (requestURI.equals("/login")) {
        if (MVNForumConfig.getRedirectLoginURL().equals(MVNForumConfig.DEFAULT) == false) {
          // if not checking, we will have recursive bug
          responseURI = MVNForumConfig.getRedirectLoginURL();
        }

      } else if (requestURI.equals("/loginprocess")) {
        // if (MVNForumConfig.getEnableLogin() == false) {
        // throw new AuthenticationException(NotLoginException.LOGIN_DISABLED);
        // }
        onlineUserManager.processLogin(request, response, true);

        String url = ParamUtil.getParameter(request, "url");
        if (onlineUserManager.getOnlineUser(request).isPasswordExpired()) {
          responseURI = UserModuleConfig.getUrlPattern() + "/changemypassword";
        } else if (url.length() > 0) {
          responseURI = url;
        } else {
          String originalRequest = ParamUtil.getAttribute(request.getSession(), ORIGINAL_REQUEST);
          if (originalRequest.length() > 0) {
            request.getSession().setAttribute(ORIGINAL_REQUEST, "");
            responseURI = originalRequest;
          }
        }
      } else if (requestURI.equals("/logout")) {
        onlineUserManager.logout(request, response);
        Locale locale = I18nUtil.getLocaleInRequest(genericRequest);
        request.setAttribute("Reason",
            MVNForumResourceBundle.getString(locale, "mvnforum.common.action.logout_success"));
        if (MVNForumConfig.getRedirectLogoutURL().equals(MVNForumConfig.DEFAULT) == false) {
          // if not checking, we will have recursive bug
          responseURI = MVNForumConfig.getRedirectLogoutURL();
        }
      }
    } catch (AuthenticationException e) {
      // make sure not from login page, we cannot set original request in this situation
      // and also make sure the request's method must be GET to set the OriginalRequest
      boolean shouldSaveOriginalRequest = (e.getReason() == NotLoginException.NOT_LOGIN)
          || (e.getReason() == NotLoginException.NOT_ENOUGH_RIGHTS);
      if (shouldSaveOriginalRequest && (request.getMethod().equals("GET"))) {
        String url = AdminModuleConfig.getUrlPattern() + requestURI;
        if (request.getQueryString() != null) {
          url = url + "?" + request.getQueryString();
        }
        request.getSession().setAttribute(ORIGINAL_REQUEST, url);
      }

      if (MVNForumConfig.getRedirectLoginURL().equals(MVNForumConfig.DEFAULT)) {
        requestURI = "/login";
      } else {
        responseURI = MVNForumConfig.getRedirectLoginURL();
      }

      if (onlineUser != null) {
        request.setAttribute("Reason", e.getReasonExplanation(onlineUser.getLocale()));
      } else {
        request.setAttribute("Reason", e.getReasonExplanation());
        log.error("Error cannot get online user", e);
      }
    } catch (Throwable e) {
      if (e instanceof BadInputException) {
        // we log in WARN level if this is the exception from user input
        log.warn("Exception in AdminModuleProcessor e = " + e.getMessage(), e);
      } else if (e instanceof AssertionError) {
        // we log in FATAL level if this is the exception from user input
        log.error("Exception in AdminModuleProcessor e = " + e.getMessage(), e);
      } else {
        log.error("Exception in AdminModuleProcessor [" + e.getClass().getName() + "] : "
            + e.getMessage(), e);
      }
      requestURI = "/error";


      String message = StringUtil.getEmptyStringIfNull(e.getMessage());
      if (message.length() == 0) {
        message = e.getClass().getName();
      }
      request.getSession().setAttribute("ErrorMessage", DisableHtmlTagFilter.filter(message));
    }

    // step 2: map the URI (of the CONTROLLER)
    try {
      // See note in the ActionInUserModule
      Action action = new ActionInForumAdminModule(genericRequest, requestURI);// may throw
                                                                               // MissingURLMapEntryException
      onlineUserManager.updateOnlineUserAction(genericRequest, action);
      if (responseURI == null) {
        URLMap map = urlMapHandler.getMap(requestURI, genericRequest);
        responseURI = map.getResponse();
      }
    } catch (MissingURLMapEntryException e) {
      log.error("Exception: missing urlmap entry in admin module: requestURI = " + requestURI);
      responseURI = "/mvnplugin/mvnforum/admin/error.jsp";
      request.getSession().setAttribute("ErrorMessage",
          DisableHtmlTagFilter.filter(e.getMessage()));
    } catch (Exception e) {
      // This will catch AuthenticationException, AssertionError, DatabaseException
      // in the method onlineUserManager.updateOnlineUserAction(request, action)
      responseURI = "/mvnplugin/mvnforum/admin/error.jsp";
      request.getSession().setAttribute("ErrorMessage",
          DisableHtmlTagFilter.filter(e.getMessage()));
    }

    // step 3: return URI to be forwarded to or dispatched to the VIEW
    if (log.isDebugEnabled()) {
      long duration = System.currentTimeMillis() - start;
      log.debug("AdminModuleProcessor : responseURI = " + responseURI + ". (" + duration + " ms)");
    }

    return responseURI;
  }// process method
}
