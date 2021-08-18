/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ForumWebHandler.java,v 1.95 2009/05/04
 * 08:44:11 lexuanttkhtn Exp $ $Author: lexuanttkhtn $ $Revision: 1.95 $ $Date: 2009/05/04 08:44:11
 * $
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

import java.sql.Timestamp;
import java.util.Locale;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryBuilder;
import com.mvnforum.categorytree.CategoryTree;
import com.mvnforum.categorytree.CategoryTreeListener;
import com.mvnforum.common.AttachmentUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.ForumCache;
import com.mvnforum.db.PostCache;
import com.mvnforum.db.ThreadCache;
import com.mvnforum.search.post.DeletePostIndexTask;
import com.mvnforum.search.post.PostIndexer;
import com.mvnforum.service.CategoryBuilderService;
import com.mvnforum.service.CategoryService;
import com.mvnforum.service.MvnForumServiceFactory;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EventLogService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class ForumWebHandler {

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  private static CategoryService categoryService =
      MvnForumServiceFactory.getMvnForumService().getCategoryService();
  private static EventLogService eventLogService =
      MvnCoreServiceFactory.getMvnCoreService().getEventLogService();
  private static CategoryBuilderService categoryBuilderService =
      MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();

  public ForumWebHandler() {}

  public void prepareAdd(GenericRequest request, GenericResponse response)
      throws DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAddForum();

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        true, null, CategoryTreeListener.SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
  }

  public void processAdd(GenericRequest request, GenericResponse response)
      throws BadInputException, CreateException, DatabaseException, DuplicateKeyException,
      AuthenticationException, ObjectNotFoundException, ForeignKeyNotFoundException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    permission.ensureCanAddForum();

    MyUtil.saveVNTyperMode(request, response);
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    int categoryID = GenericParamUtil.getParameterInt(request, "CategoryID");

    String forumOwnerName = GenericParamUtil.getParameterSafe(request, "ForumOwnerName", false);
    StringUtil.checkGoodName(forumOwnerName, locale);
    String forumName = GenericParamUtil.getParameterSafe(request, "ForumName", true);
    forumName = DisableHtmlTagFilter.filter(forumName);
    String forumDesc = GenericParamUtil.getParameterSafe(request, "ForumDesc", false);
    forumDesc = DisableHtmlTagFilter.filter(forumDesc);
    int forumType = GenericParamUtil.getParameterInt(request, "ForumType");
    int forumFormatOption = 0; // @todo review and support it later
    int forumOption = 0; // @todo review and support it later
    int forumStatus = GenericParamUtil.getParameterInt(request, "ForumStatus");
    int forumModerationMode = GenericParamUtil.getParameterInt(request, "ForumModerationMode");
    String forumPassword = ""; // @todo review and support it later

    // check valid
    ForumBean.validateForumType(forumType);
    ForumBean.validateForumFormatOption(forumFormatOption);
    ForumBean.validateForumOption(forumOption);
    ForumBean.validateForumStatus(forumStatus);
    ForumBean.validateForumModerationMode(forumModerationMode);

    try {
      DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
    } catch (Exception e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.categoryid_not_exists",
          new Object[] {new Integer(categoryID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // validate forumOwnerName
    if (forumOwnerName == null) {
      forumOwnerName = "";
    }
    if (forumOwnerName.length() > 0) {
      try {
        forumOwnerName = DAOFactory.getMemberDAO().findByAlternateKey_MemberName(forumOwnerName);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {forumOwnerName});
        throw new ObjectNotFoundException(localizedMessage);
      }
    }

    try {
      DAOFactory.getForumDAO().findByAlternateKey_ForumName_CategoryID(forumName, categoryID);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.forumname_exists", new Object[] {forumName});
      throw new DuplicateKeyException(localizedMessage);
    } catch (ObjectNotFoundException ex) {
    }
    int forumID = DAOFactory.getForumDAO().createForum(categoryID, forumOwnerName,
        ""/* lastPostMemberName */, forumName, forumDesc, now/* forumCreationDate */,
        now/* forumModifiedDate */, now/* forumLastPostDate */, 0/* forumOrder */, forumType,
        forumFormatOption, forumOption, forumStatus, forumModerationMode, forumPassword,
        0/* forumThreadCount */, 0/* forumPostCount */);

    // Check if the user created forum should be the owner (ForumAdmin) of that forum
    // This is used for KG
    if (MVNForumConfig.getEnableAutoForumOwner()) {
      int memberID = onlineUser.getMemberID();
      DAOFactory.getMemberForumDAO().create(memberID, forumID,
          MVNForumPermission.PERMISSION_FORUM_ADMIN);
      onlineUser.reloadPermission();
    }

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.AddForum",
        new Object[] {new Integer(forumID), new Integer(categoryID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "add forum", actionDesc, EventLogService.MEDIUM);

    // Now clear the cache
    ForumCache.getInstance().clear();

    request.setAttribute("ForumName", forumName);

  }

  public void prepareDelete(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // primary key column(s)
    int forumID = GenericParamUtil.getParameterInt(request, "forum");

    permission.ensureCanDeleteForum(forumID);

    ForumBean forumBean = null;
    try {
      forumBean = DAOFactory.getForumDAO().getForum(forumID);
    } catch (ObjectNotFoundException e) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int numberOfThreads = DAOFactory.getThreadDAO().getNumberOfEnableThreads_inForum(forumID);
    int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inForum(forumID);

    int numberOfPendingThreads =
        DAOFactory.getThreadDAO().getNumberOfDisableThreads_inForum(forumID);
    int numberOfPendingPosts = DAOFactory.getPostDAO().getNumberOfDisablePosts_inForum(forumID);

    request.setAttribute("ForumBean", forumBean);
    request.setAttribute("NumberOfThreads", new Integer(numberOfThreads));
    request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
    request.setAttribute("NumberOfPendingThreads", new Integer(numberOfPendingThreads));
    request.setAttribute("NumberOfPendingPosts", new Integer(numberOfPendingPosts));
  }

  public void processDelete(GenericRequest request) throws BadInputException,
      ObjectNotFoundException, DatabaseException, AuthenticationException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // primary key column(s)
    int forumID = GenericParamUtil.getParameterInt(request, "forum");
    try {
      DAOFactory.getForumDAO().findByPrimaryKey(forumID);
    } catch (Exception e) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // user must have been authenticated before he can delete
    permission.ensureIsAuthenticated();

    permission.ensureCanDeleteForum(forumID);

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    // Delete all attachments in this forum,
    // we must call this before any attempt to delete the post/thread/forum
    // That is, the order when delete is VERY IMPORTANT
    AttachmentUtil.deleteAttachments_inForum(forumID);

    DAOFactory.getGroupForumDAO().delete_inForum(forumID);
    DAOFactory.getMemberForumDAO().delete_inForum(forumID);

    DAOFactory.getFavoriteThreadDAO().delete_inForum(forumID);

    DAOFactory.getWatchDAO().delete_inForum(forumID);

    DAOFactory.getPostDAO().delete_inForum(forumID);
    DAOFactory.getThreadDAO().delete_inForum(forumID);

    // now delete the forum, note that we delete it after delete all child objects
    DAOFactory.getForumDAO().delete(forumID);

    String actionDesc = MVNForumResourceBundle.getString(MVNForumConfig.getEventLogLocale(),
        "mvnforum.eventlog.desc.DeleteForum", new Object[] {new Integer(forumID)});
    eventLogService.logEvent(onlineUser.getMemberName(), request.getRemoteAddr(),
        MVNForumConstant.EVENT_LOG_MAIN_MODULE, MVNForumConstant.EVENT_LOG_SUB_MODULE_ADMIN,
        "delete forum", actionDesc, EventLogService.CRITICAL);

    // now update the search index
    PostIndexer.scheduleDeletePostTask(forumID, DeletePostIndexTask.OBJECT_TYPE_FORUM);

    // Clear cache
    PostCache.getInstance().clear();
    ThreadCache.getInstance().clear();
    ForumCache.getInstance().clear();
  }

  public void prepareEdit(GenericRequest request, GenericResponse response)
      throws BadInputException, ObjectNotFoundException, DatabaseException,
      AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // primary key column(s)
    int forumID = GenericParamUtil.getParameterInt(request, "forum");

    permission.ensureCanEditForum(forumID);

    ForumBean forumBean = null;
    try {
      forumBean = DAOFactory.getForumDAO().getForum(forumID);
    } catch (ObjectNotFoundException e) {
      Locale locale = I18nUtil.getLocaleInRequest(request);
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    request.setAttribute("ForumBean", forumBean);

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        forumBean, CategoryTreeListener.SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
  }

  public void processUpdate(GenericRequest request, GenericResponse response)
      throws BadInputException, ObjectNotFoundException, DatabaseException, DuplicateKeyException,
      AuthenticationException, ForeignKeyNotFoundException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // primary key column(s)
    int forumID = GenericParamUtil.getParameterInt(request, "ForumID");
    try {
      DAOFactory.getForumDAO().findByPrimaryKey(forumID);
    } catch (Exception e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    permission.ensureCanEditForum(forumID);
    MyUtil.ensureCorrectCurrentPassword(request);

    MyUtil.saveVNTyperMode(request, response);

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    // column(s) to update
    int categoryID = GenericParamUtil.getParameterInt(request, "CategoryID");

    String forumOwnerName = GenericParamUtil.getParameterSafe(request, "ForumOwnerName", false);
    StringUtil.checkGoodName(forumOwnerName, locale);
    String forumName = GenericParamUtil.getParameterFilter(request, "ForumName", true);
    String forumDesc = GenericParamUtil.getParameterFilter(request, "ForumDesc", false);
    Timestamp forumModifiedDate = now;
    int forumOrder = GenericParamUtil.getParameterUnsignedInt(request, "ForumOrder");
    int forumType = GenericParamUtil.getParameterInt(request, "ForumType");
    int forumFormatOption = 0;// GenericParamUtil.getParameterInt(request, "ForumFormatOption");
    int forumOption = 0;// GenericParamUtil.getParameterInt(request, "ForumOption");
    int forumStatus = GenericParamUtil.getParameterInt(request, "ForumStatus");
    int forumModerationMode = GenericParamUtil.getParameterInt(request, "ForumModerationMode");

    // check valid
    ForumBean.validateForumType(forumType);
    ForumBean.validateForumFormatOption(forumFormatOption);
    ForumBean.validateForumOption(forumOption);
    ForumBean.validateForumStatus(forumStatus);
    ForumBean.validateForumModerationMode(forumModerationMode);

    try {
      DAOFactory.getCategoryDAO().findByPrimaryKey(categoryID);
    } catch (Exception e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.categoryid_not_exists",
          new Object[] {new Integer(categoryID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // validate forumOwnerName
    if (forumOwnerName == null) {
      forumOwnerName = "";
    }

    if (forumOwnerName.length() > 0) {
      try {
        forumOwnerName = DAOFactory.getMemberDAO().findByAlternateKey_MemberName(forumOwnerName);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {forumOwnerName});
        throw new ObjectNotFoundException(localizedMessage);
      }
    }

    DAOFactory.getForumDAO().update(forumID, // primary key
        categoryID, forumOwnerName, forumName, forumDesc, forumModifiedDate, forumOrder, forumType,
        forumFormatOption, forumOption, forumStatus, forumModerationMode);

    // Now clear the cache
    ForumCache.getInstance().clear();

    // Clear post cache because if a forum's status is changed, its threads will be changed -> must
    // reload threads.
    PostCache.getInstance().clear();
  }

  /*
   * @todo: check permission
   */
  public void processUpdateForumOrder(GenericRequest request) throws BadInputException,
      DatabaseException, ObjectNotFoundException, AuthenticationException {

    SecurityUtil.checkHttpReferer(request);
    SecurityUtil.checkSecurityTokenMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // primary key column(s)
    int forumID = GenericParamUtil.getParameterInt(request, "forum");
    try {
      DAOFactory.getForumDAO().findByPrimaryKey(forumID);
    } catch (Exception e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    permission.ensureCanEditForum(forumID);

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    String action = GenericParamUtil.getParameterSafe(request, "action", true);
    if (action.equals("up")) {
      DAOFactory.getForumDAO().decreaseForumOrder(forumID, now);
    } else if (action.equals("down")) {
      DAOFactory.getForumDAO().increaseForumOrder(forumID, now);
    } else {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.cannot_update_order.unknown_action",
          new Object[] {action});
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot update ForumOrder: unknown action: " + action);
    }

    // Now clear the cache
    ForumCache.getInstance().clear();
  }

  public void prepareForumManagement(GenericRequest request, GenericResponse response)
      throws DatabaseException, AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    if ((permission.canEditAtLeastOneForum() == false) && (permission.canAddForum() == false)) {
      permission.ensureCanEditAtLeastOneForum(); // is this the correct permission
      permission.ensureCanAddForum();
    }

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategoryTree(request, response);
    tree.addCategeoryTreeListener(listener);
    String result = tree.build();
    request.setAttribute("Result", result);
  }

}
