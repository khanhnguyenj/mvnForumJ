/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/PostWebHandler.java,v 1.226 2010/08/17
 * 04:23:23 minhnn Exp $ $Author: minhnn $ $Revision: 1.226 $ $Date: 2010/08/17 04:23:23 $
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
package com.mvnforum.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.MVNForumConfig;
import com.mvnforum.MVNForumConstant;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.MyUtil;
import com.mvnforum.auth.Action;
import com.mvnforum.auth.AuthenticationException;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.auth.OnlineUser;
import com.mvnforum.auth.OnlineUserManager;
import com.mvnforum.categorytree.CategoryBuilder;
import com.mvnforum.categorytree.CategoryTree;
import com.mvnforum.categorytree.CategoryTreeListener;
import com.mvnforum.common.OnlineUserUtil;
import com.mvnforum.common.PostChecker;
import com.mvnforum.common.StatisticsUtil;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.ForumCache;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.MemberCache;
import com.mvnforum.db.PostBean;
import com.mvnforum.db.PostCache;
import com.mvnforum.db.ThreadBean;
import com.mvnforum.db.ThreadCache;
import com.mvnforum.search.post.DeletePostIndexTask;
import com.mvnforum.search.post.PostIndexer;
import com.mvnforum.search.post.PostSearchQuery;
import com.mvnforum.service.CategoryBuilderService;
import com.mvnforum.service.CategoryService;
import com.mvnforum.service.MvnForumServiceFactory;
import freemarker.template.TemplateException;
import net.myvietnam.mvncore.MVNCoreResourceBundle;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.FloodException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.InterceptorException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableMVNCodeFilter;
import net.myvietnam.mvncore.interceptor.InterceptorService;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.SecurityUtil;
import net.myvietnam.mvncore.service.EnvironmentService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.FriendlyURLParamUtil;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;
import net.myvietnam.mvncore.web.GenericRequest;
import net.myvietnam.mvncore.web.GenericResponse;

public class PostWebHandler {

  private static final Logger log = LoggerFactory.getLogger(PostWebHandler.class);

  private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();

  private static CategoryService categoryService =
      MvnForumServiceFactory.getMvnForumService().getCategoryService();
  private static CategoryBuilderService categoryBuilderService =
      MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();
  private static InterceptorService interceptorService = InterceptorService.getInstance();

  public PostWebHandler() {}

  /**
   * This method is for addpost page
   *
   * @throws InterceptorException
   */
  public void prepareAdd(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, DatabaseException, BadInputException, AuthenticationException,
      InterceptorException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (MVNForumConfig.getEnableNewPost() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.cannot_create_new_post.new_post_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create new post because NEW_POST feature is
      // disabled by administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    if (MVNForumConfig.isGuestUserInDatabase() == false) {
      permission.ensureIsAuthenticated();
    }

    // we set this action attribute first because the return below can make method return
    // prematurely
    request.setAttribute("action", "addnew");

    int parentPostID = 0;
    try {
      // if has parent --> there is no forum
      parentPostID = GenericParamUtil.getParameterInt(request, "parent");
    } catch (Exception ex) {
      // do nothing
      // NOTE: we cannot return here since user can have a parameter parent = 0
    }
    int forumID;
    String mode;
    String replyTopic;

    if (parentPostID == 0) {// new thread
      mode = MVNForumResourceBundle.getString(locale, "mvnforum.user.addpost.mode.addnew");
      replyTopic = "";
      forumID = GenericParamUtil.getParameterInt(request, "forum");

      ForumBean forumBean = null;
      try {
        forumBean = ForumCache.getInstance().getBean(forumID);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
            new Object[] {new Integer(forumID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumBean.ensureNotDisabledForum(locale);
      forumBean.ensureNotClosedForum(locale);
      forumBean.ensureNotLockedForum(locale);

      permission.ensureCanAddThread(forumID);

      // check if this thread is being watched
      Boolean isWatched = Boolean.FALSE;
      if (onlineUser.isMember()) {
        isWatched = Boolean.valueOf(WatchUtil.isForumWatched(onlineUser.getMemberID(), forumBean));
      }
      request.setAttribute("isWatched", isWatched);
    } else {// reply to a post
      // this is a parent post
      mode = MVNForumResourceBundle.getString(locale, "mvnforum.user.addpost.mode.reply");
      PostBean postBean = null;
      try {
        postBean = DAOFactory.getPostDAO().getPost(parentPostID);// can throw DatabaseException
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.postid_not_exists",
            new Object[] {new Integer(parentPostID)});
        throw new ObjectNotFoundException(localizedMessage);
      }

      // check permission
      forumID = postBean.getForumID();
      replyTopic = postBean.getPostTopic();
      ForumBean forumBean = null;
      try {
        forumBean = ForumCache.getInstance().getBean(forumID);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
            new Object[] {new Integer(forumID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumBean.ensureNotDisabledForum(locale);
      forumBean.ensureNotClosedForum(locale);
      forumBean.ensureNotLockedForum(locale);

      permission.ensureCanAddPost(forumID);

      // now we prepare to list latest post in the thread
      int threadID = postBean.getThreadID();

      // now check if thread is closed or locked, if it is, then cannot reply to a post
      ThreadBean threadBean = null;
      try {
        threadBean = DAOFactory.getThreadDAO().getThread(threadID);
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
            new Object[] {new Integer(threadID)});
        throw new ObjectNotFoundException(localizedMessage);
      }

      threadBean.ensureStatusCanReply();

      Collection postBeans = DAOFactory.getPostDAO().getLastEnablePosts_inThread_limit(threadID,
          MVNForumConfig.ROWS_IN_LAST_REPLIES);
      request.setAttribute("ParentPostBean", postBean);
      request.setAttribute("PostBeans", postBeans);

      // check if this thread is being watched
      Boolean isWatched = Boolean.FALSE;
      if (onlineUser.isMember()) {
        isWatched = Boolean
            .valueOf(WatchUtil.isThreadWatched(onlineUser.getMemberID(), threadBean, forumBean));
      }
      request.setAttribute("isWatched", isWatched);
    }

    boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");
    if (isPreviewing) {
      SecurityUtil.checkHttpPostMethod(request);

      MyUtil.saveVNTyperMode(request, response);

      // Get and Check if user enter some text or not
      String prePostTopic = GenericParamUtil.getParameter(request, "PostTopic", true);
      String prePostBody = GenericParamUtil.getParameter(request, "message", true);
      String prePostIcon = GenericParamUtil.getParameter(request, "PostIcon", false);

      EnableMVNCodeFilter.checkValidation(prePostBody, locale);
      // always disable HTML
      prePostTopic = DisableHtmlTagFilter.filter(prePostTopic);
      prePostBody = DisableHtmlTagFilter.filter(prePostBody);
      prePostIcon = DisableHtmlTagFilter.filter(prePostIcon);

      prePostTopic = interceptorService.validateContent(prePostTopic);
      prePostBody = interceptorService.validateContent(prePostBody);

      request.setAttribute("prePostTopic", prePostTopic);
      request.setAttribute("prePostBody", prePostBody);
      request.setAttribute("prePostIcon", prePostIcon);

      MemberBean memberBean = MemberCache.getInstance().getMember(onlineUser.getMemberID());
      request.setAttribute("MemberBean", memberBean);
    }

    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener =
        categoryService.getCategoryTreePath(request, response, forumID, mode, "addnew", replyTopic);
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        "addpost", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());
    MyUtil.setTextAreaSize(request);
    MyUtil.setEditorMode(request);
  }

  public void processAdd(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, DatabaseException, CreateException, BadInputException,
      ForeignKeyNotFoundException, AuthenticationException, FloodException, InterceptorException,
      MessagingException, IOException, TemplateException {

    SecurityUtil.checkHttpPostMethod(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);
    if (MVNForumConfig.getEnableNewPost() == false) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.cannot_create_new_post.new_post_is_disabled");
      throw new IllegalStateException(localizedMessage);
      // throw new IllegalStateException("Cannot create new post because NEW_POST feature is
      // disabled by administrator.");
    }

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    int memberID = onlineUser.getMemberID();
    String memberName = onlineUser.getMemberName();

    try {
      FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_MEMBER,
          memberName);
    } catch (FloodException fe) {
      // throw new FloodException("You have reached the maximum number of the post adding actions
      // for this page. Please try this page later. This is to prevent forum from being flooded.");
      Integer maxPosts = new Integer(
          FloodControlHour.getActionsPerHour(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_MEMBER));
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.FloodException.add_post_too_many_times", new Object[] {maxPosts});
      throw new FloodException(localizedMessage);
    }


    String currentIP = request.getRemoteAddr();
    try {
      FloodControlHour.ensureNotReachMaximum(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_IP, currentIP);
    } catch (FloodException fe) {
      // throw new FloodException("You have reached the maximum number of the post adding actions
      // for this page. Please try this page later. This is to prevent forum from being flooded.");
      Integer maxPosts =
          new Integer(FloodControlHour.getActionsPerHour(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_IP));
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.FloodException.add_post_too_many_times", new Object[] {maxPosts});
      throw new FloodException(localizedMessage);
    }

    MyUtil.saveVNTyperMode(request, response);

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    int parentPostID = GenericParamUtil.getParameterInt(request, "parent");

    boolean attachMore = GenericParamUtil.getParameterBoolean(request, "AttachMore");
    boolean addFavoriteThread =
        GenericParamUtil.getParameterBoolean(request, "AddFavoriteParentThread");
    boolean addWatchThread = GenericParamUtil.getParameterBoolean(request, "AddWatchParentThread");

    String postTopic = GenericParamUtil.getParameter(request, "PostTopic", true);
    postTopic = DisableHtmlTagFilter.filter(postTopic);// always disable HTML
    postTopic = interceptorService.validateContent(postTopic);

    String postBody = GenericParamUtil.getParameter(request, "message", true); // use message
                                                                               // instead of
                                                                               // MessageBody
    postBody = DisableHtmlTagFilter.filter(postBody);// always disable HTML
    String beforeValidated = postBody;
    postBody = interceptorService.validateContent(postBody);

    // check validation bb code
    EnableMVNCodeFilter.checkValidation(postBody, locale);

    String postIcon = GenericParamUtil.getParameter(request, "PostIcon");
    postIcon = DisableHtmlTagFilter.filter(postIcon);// always disable HTML

    int forumID = 0;
    int threadID = 0;
    boolean isPendingThread = false;
    boolean isForumModerator = false;
    if (parentPostID == 0) {// new thread

      forumID = GenericParamUtil.getParameterInt(request, "forum");
      ForumBean forumBean = null;
      try {
        forumBean = ForumCache.getInstance().getBean(forumID);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
            new Object[] {new Integer(forumID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumBean.ensureNotDisabledForum(locale);
      forumBean.ensureNotClosedForum(locale);
      forumBean.ensureNotLockedForum(locale);

      // check permission
      isForumModerator = permission.canModerateThread(forumID);
      permission.ensureCanAddThread(forumID);

      String lastPostMemberName = memberName;

      int threadType = GenericParamUtil.getParameterUnsignedInt(request, "ThreadType",
          ThreadBean.THREAD_TYPE_DEFAULT);
      int threadPriority = GenericParamUtil.getParameterUnsignedInt(request, "ThreadPriority",
          ThreadBean.THREAD_PRIORITY_NORMAL);

      if (threadType > ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT /* 3 */) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.not_support_thread_type",
            new Object[] {new Integer(threadType)});
        throw new BadInputException(localizedMessage);
      }

      if (threadType == ThreadBean.THREAD_TYPE_GLOBAL_ANNOUNCEMENT) {
        permission.ensureCanAdminSystem();
        // forumID = -1; // this thread not belongs to any forum
      } else if (threadType != ThreadBean.THREAD_TYPE_DEFAULT) {
        permission.ensureCanModerateThread(forumID);
      }

      int threadOption = 0; // @todo review and support it later
      int threadStatus = ThreadBean.THREAD_STATUS_DEFAULT;

      // Ensure that moderator don't have to moderate the thread to enable it
      if (forumBean.shouldModerateThread() && !isForumModerator) {
        threadStatus = ThreadBean.THREAD_STATUS_DISABLED;
        isPendingThread = true;
      }

      int threadHasPoll = 0;// @todo review and support it later
      int threadDuration = 0;// @todo review and support it later
      int threadAttachCount = 0;
      threadID = DAOFactory.getThreadDAO().createThread(forumID, memberName, lastPostMemberName,
          postTopic, postBody, 0/* threadVoteCount */, 0/* threadVoteTotalStars */,
          now/* threadCreationDate */, now/* threadLastPostDate */, threadType, threadPriority,
          threadOption, threadStatus, threadHasPoll, 0/* threadViewCount */,
          0/* threadReplyCount */, postIcon, threadDuration, threadAttachCount);

    } else {// reply to a post
      PostBean parentPostBean = null;
      try {
        parentPostBean = DAOFactory.getPostDAO().getPost(parentPostID);// can throw
                                                                       // DatabaseException
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.postid_not_exists",
            new Object[] {new Integer(parentPostID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumID = parentPostBean.getForumID();
      threadID = parentPostBean.getThreadID();

      ForumBean forumBean = null;
      try {
        forumBean = ForumCache.getInstance().getBean(forumID);
      } catch (ObjectNotFoundException e) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
            new Object[] {new Integer(forumID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumBean.ensureNotDisabledForum(locale);
      forumBean.ensureNotClosedForum(locale);
      forumBean.ensureNotLockedForum(locale);

      // check permission
      isForumModerator = permission.canModerateThread(forumID);
      permission.ensureCanAddPost(forumID);

      // now check if thread is closed or locked, if it is, then cannot reply to a post
      ThreadBean threadBean = null;
      try {
        threadBean = DAOFactory.getThreadDAO().getThread(threadID);
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
            new Object[] {new Integer(threadID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      threadBean.ensureStatusCanReply();

    }

    // Timestamp postLastEditDate = now;
    String postCreationIP = currentIP;
    String postLastEditIP = "";// should we init it to postCreationIP ???
    int postFormatOption = 0;
    int postOption = 0;
    int postStatus = PostBean.POST_STATUS_DEFAULT;

    try {
      // Ensure that moderator don't have to moderate the thread to enable it
      if (ForumCache.getInstance().getBean(forumID).shouldModeratePost() && !isForumModerator) {
        // we will NOT disable post that is a thread (parentPostID == 0)
        if (parentPostID != 0) {// replied post
          postStatus = PostBean.POST_STATUS_DISABLED;
        }
      }
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int postAttachCount = 0;

    int postID = DAOFactory.getPostDAO().createPost(parentPostID, forumID, threadID, memberID,
        memberName, ""/* lastEditMemberName */, postTopic, postBody, now/* postCreationDate */,
        now/* postLastEditDate */, postCreationIP, postLastEditIP, 0/* postEditCount */,
        postFormatOption, postOption, postStatus, postIcon, postAttachCount);

    // check whether censored words existed, if so send mail to Admin
    if (MVNForumConfig.getEnableEmailToAdminContentWithCensoredWords()) {
      if (beforeValidated.equals(postBody) == false) {
        PostUtil.sendEmailToAdminBecauseCensoredPost(request, postID, forumID, threadID, memberID);
      }
    }

    StatisticsUtil.updateMemberStatistics(memberID);
    StatisticsUtil.updateForumStatistics(forumID);
    StatisticsUtil.updateThreadStatistics(threadID);

    /** @todo Update PostEditLog table here */

    // add favorite thread if user checked it
    if (addFavoriteThread) {
      permission.ensureIsAuthenticated();
      // @todo: add checking of MVNForumConfig.getEnableFavoriteThread()
      // check to make sure that this user does not exceed his favorite max
      int currentFavoriteCount =
          DAOFactory.getFavoriteThreadDAO().getNumberOfFavoriteThreads_inMember(memberID);
      int maxFavorites = MVNForumConfig.getMaxFavoriteThreads();
      if (currentFavoriteCount < maxFavorites) {
        Timestamp favoriteCreationDate = now;
        int favoriteType = 0; // @todo implement it later
        int favoriteOption = 0; // @todo implement it later
        int favoriteStatus = 0; // @todo implement it later

        // now check permission the this user have the readPost permission
        permission.ensureCanReadPost(forumID);

        // has the permission now, then insert to database
        try {
          DAOFactory.getFavoriteThreadDAO().create(memberID, threadID, forumID,
              favoriteCreationDate, favoriteType, favoriteOption, favoriteStatus);
        } catch (DuplicateKeyException ex) {
          // already add favorite thread, just ignore
        }
      }
    }

    // add watch if user checked it
    if (addWatchThread) {
      permission.ensureIsAuthenticated();
      permission.ensureIsActivated();
      if (MVNForumConfig.getEnableWatch() == false) {
        // should never happen, because if it happen, then the whole process is broken
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.cannot_add_watch.watch_is_disabled");
        throw new IllegalStateException(localizedMessage);
        // throw new IllegalStateException("Cannot add Watch because Watch feature is disabled by
        // administrator.");
      }

      int watchType = 0;// GenericParamUtil.getParameterInt(request, "WatchType");
      int watchOption = 0;// GenericParamUtil.getParameterInt(request, "WatchOption");
      int watchStatus = 0;// GenericParamUtil.getParameterInt(request, "WatchStatus");
      Timestamp watchCreationDate = now;
      Timestamp watchLastSentDate = now;
      Timestamp watchEndDate = now;// @todo: check it !!!

      try {
        DAOFactory.getWatchDAO().create(memberID, 0/* watchCategoryID */, 0/* watchForumID */,
            threadID, watchType, watchOption, watchStatus, watchCreationDate, watchLastSentDate,
            watchEndDate);
      } catch (DuplicateKeyException ex) {
        // User try to create a duplicate watch, just ignore
      }
    }

    // Now clear the cache
    MemberCache.getInstance().clear();
    PostCache.getInstance().clear();
    ThreadCache.getInstance().clear();
    ForumCache.getInstance().clear();// forum store the statistics of posts and threads

    // now, update the Search Index
    // @todo check the performance here
    PostBean justAddedPostBean = null;
    try {
      justAddedPostBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    PostIndexer.scheduleAddPostTask(justAddedPostBean);

    request.setAttribute("ForumID", String.valueOf(forumID));
    request.setAttribute("ThreadID", String.valueOf(threadID));
    request.setAttribute("PostID", String.valueOf(postID));
    request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
    request.setAttribute("AddFavoriteParentThread", Boolean.valueOf(addFavoriteThread));
    request.setAttribute("AddWatchParentThread", Boolean.valueOf(addWatchThread));
    request.setAttribute("IsPendingThread", Boolean.valueOf(isPendingThread));
    /** @todo: review, this variable is still reserved */
    // request.setAttribute("ParentPostID", String.valueOf(parentPostID));

    // for enterprise
    if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
        .getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE) {
      if (MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID)) {
        request.setAttribute("UploadApplet",
            Boolean.valueOf(GenericParamUtil.getParameterBoolean(request, "UploadApplet")));
      }
      if (MVNForumConfig.getEnablePoll()) {
        try {
          ThreadBean threadBean = DAOFactory.getThreadDAO().getThread(threadID);
          if ((permission.canAddPoll(forumID))
              || (threadBean.getMemberName().equals(onlineUser.getMemberName())
                  && permission.canAddOwnThreadPoll(forumID))) {

            boolean addPoll = GenericParamUtil.getParameterBoolean(request, "AddPoll");
            request.setAttribute("AddPoll", Boolean.valueOf(addPoll));
          }
        } catch (ObjectNotFoundException ex) {
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
              new Object[] {new Integer(threadID)});
          throw new ObjectNotFoundException(localizedMessage);
        }
      }
    }

    FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_IP, currentIP);
    FloodControlHour.increaseCount(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_MEMBER, memberName);

  }

  public void addPostSuccessForRender(GenericRequest request, GenericResponse response)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int postID = (Integer.valueOf((String) request.getAttribute("PostID"))).intValue();
    int forumID = (Integer.valueOf((String) request.getAttribute("ForumID"))).intValue();
    // now, update the Search Index
    // @todo check the performance here
    PostBean justAddedPostBean = null;
    try {
      justAddedPostBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    String addPostSuccessLabel =
        MVNForumResourceBundle.getString(locale, "mvnforum.user.addpostsuccess.title");
    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response,
        forumID, null, null, addPostSuccessLabel);
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

    request.setAttribute("PostBean", justAddedPostBean);
  }

  public void preparePrintPost(GenericRequest request, String requestURI)
      throws ObjectNotFoundException, DatabaseException, BadInputException,
      AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int postID = 0;
    Map params = null;
    if (requestURI.startsWith("/printpost_")) {
      params = FriendlyURLParamUtil.getParameters(requestURI);

      if (params.get("post") != null) {
        String inputStr = (String) params.get("post");
        try {
          postID = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
          String localizedMessage = MVNCoreResourceBundle.getString(locale,
              "mvncore.exception.BadInputException.cannot_parse",
              new Object[] {DisableHtmlTagFilter.filter("post"), "int"});
          throw new BadInputException(localizedMessage);
        }
      } else {
        String localizedMessage = MVNCoreResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.not_allow_to_be_empty",
            new Object[] {DisableHtmlTagFilter.filter("post")});
        throw new BadInputException(localizedMessage);
      }
    } else {
      postID = GenericParamUtil.getParameterInt(request, "post");
    }

    PostBean postBean = null;
    try {
      postBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int threadID = postBean.getThreadID();
    int forumID = postBean.getForumID();

    try {
      ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    permission.ensureCanReadPost(forumID);

    // to show the thread topic
    ThreadBean threadBean = null;
    try {
      threadBean = ThreadCache.getInstance().getThread(threadID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
          new Object[] {new Integer(threadID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    MemberBean memberBean = null;
    if (postBean.getMemberID() > 0) {
      memberBean = MemberCache.getInstance().getMember(postBean.getMemberID());
      // memberBean = DAOFactory.getMemberDAO().getMember(postBean.getMemberID());
    }
    postBean.setMemberBean(memberBean);

    int postAttachCount = postBean.getPostAttachCount();
    if ((postAttachCount > 0) && MVNForumConfig.getEnableAttachment()) {
      Collection attachBeans = DAOFactory.getAttachmentDAO().getAttachments_inPost(postID);
      int actualAttachCount = attachBeans.size();

      // now check if the attachCount in talbe Post equals to the actual attachCount in table
      // Attachment
      if (postAttachCount != actualAttachCount) {
        if (actualAttachCount != DAOFactory.getAttachmentDAO()
            .getNumberOfAttachments_inPost(postID)) {
          String localizedMessage = MVNForumResourceBundle.getString(locale,
              "java.lang.AssertionError.serious_error.cannot_process_attachment_count");
          throw new AssertionError(localizedMessage);
          // throw new AssertionError("AssertionError: Serious error: cannot process Attachment
          // Count in table Attachment");
        }
        log.warn(
            "The attachment count in table Post and Attachment are not synchronized. In table Post = "
                + postAttachCount + " and in table Attachment = " + actualAttachCount
                + ". Synchronize to " + actualAttachCount);
        DAOFactory.getPostDAO().updateAttachCount(postID, actualAttachCount);
      }
      if (actualAttachCount > 0) {
        postBean.setAttachmentBeans(attachBeans);
      }
    }

    request.setAttribute("post", new Integer(postID));
    request.setAttribute("PostBean", postBean);
    request.setAttribute("ThreadBean", threadBean);
  }

  /**
   * then, it will be forward to addpost.jsp NOTE: This method MUST NOT use parameter MessageParent
   * (need some process to figure out)
   *
   * @throws IOException
   * @throws InterceptorException
   */
  public void prepareEdit(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, DatabaseException, BadInputException,
      AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // a guest CANNOT edit a post, because it need Authenticated Permission
    permission.ensureIsAuthenticated();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    String mode = MVNForumResourceBundle.getString(locale, "mvnforum.user.addpost.mode.update");

    int postID = GenericParamUtil.getParameterInt(request, "post");
    PostBean postBean = null;
    try {
      postBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int forumID = postBean.getForumID();
    String replyTopic = postBean.getPostTopic();

    ForumBean forumBean = null;
    try {
      forumBean = ForumCache.getInstance().getBean(forumID);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    forumBean.ensureNotDisabledForum(locale);
    forumBean.ensureNotLockedForum(locale);

    // now check if thread is closed or locked, if it is, then cannot reply to a post
    int threadID = postBean.getThreadID();
    ThreadBean threadBean = null;
    try {
      threadBean = DAOFactory.getThreadDAO().getThread(threadID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
          new Object[] {new Integer(threadID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    threadBean.ensureStatusCanEdit();
    int priority = threadBean.getThreadPriority();

    PostChecker.checkEditPost(onlineUser, postBean, forumBean);

    request.setAttribute("PostToEdit", postBean);
    request.setAttribute("Priority", new Integer(priority));
    request.setAttribute("action", "update");

    boolean isPreviewing = GenericParamUtil.getParameterBoolean(request, "preview");
    if (isPreviewing) {
      // Check if user enter some text or not
      GenericParamUtil.getParameter(request, "PostTopic", true);
      GenericParamUtil.getParameter(request, "message", true);// use message instead of MessageBody

      MemberBean memberBean = MemberCache.getInstance().getMember(onlineUser.getMemberID());
      request.setAttribute("MemberBean", memberBean);

      String prePostTopic = GenericParamUtil.getParameter(request, "PostTopic");
      request.setAttribute("prePostTopic", prePostTopic);

      String prePostBody = GenericParamUtil.getParameter(request, "message");
      request.setAttribute("prePostBody", prePostBody);

      String prePostIcon = GenericParamUtil.getParameter(request, "PostIcon");
      request.setAttribute("prePostIcon", prePostIcon);
    }

    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener =
        categoryService.getCategoryTreePath(request, response, forumID, mode, "update", replyTopic);
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        "addpost", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());

    // check if this thread is being watched
    Boolean isWatched = Boolean.FALSE;
    if (onlineUser.isMember()) {
      isWatched = Boolean
          .valueOf(WatchUtil.isThreadWatched(onlineUser.getMemberID(), threadBean, forumBean));
    }
    request.setAttribute("isWatched", isWatched);
    MyUtil.setTextAreaSize(request);
    MyUtil.setEditorMode(request);
  }

  /**
   * @throws MessagingException
   * @throws TemplateException
   * @todo: log the modification
   * @todo: check the comment below, it's obsolete now :(
   * @todo: check if messageTopic could be optional when we reply NOTE: This method MUST NOT get
   *        parameter MessageParent (need some process to figure out) so it needs to call
   *        setAttribute with messageParent for page updatepostsuccess.jsp
   */
  public void processUpdate(GenericRequest request)
      throws ObjectNotFoundException, BadInputException, DatabaseException, CreateException,
      ForeignKeyNotFoundException, AuthenticationException, InterceptorException, IOException,
      MessagingException, TemplateException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // a guest CANNOT edit a post, because it need Authenticated Permission
    permission.ensureIsAuthenticated();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    Timestamp now = DateUtil.getCurrentGMTTimestamp();

    int postID = GenericParamUtil.getParameterInt(request, "post");// do not change this line !

    // check constraint
    PostBean postBean = null;
    try {
      postBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int forumID = postBean.getForumID();
    int threadID = postBean.getThreadID();

    ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
    forumBean.ensureNotDisabledForum(locale);
    forumBean.ensureNotLockedForum(locale);

    // now check if thread is locked, if it is, then cannot reply to a post
    // Please note that if the threadStatus is closed, post can still be edited
    ThreadBean threadBean = null;
    try {
      threadBean = DAOFactory.getThreadDAO().getThread(threadID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
          new Object[] {new Integer(threadID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    threadBean.ensureStatusCanEdit();

    String postTopic = GenericParamUtil.getParameter(request, "PostTopic", true);
    postTopic = DisableHtmlTagFilter.filter(postTopic);// always disable HTML
    postTopic = interceptorService.validateContent(postTopic);

    String postBody = GenericParamUtil.getParameter(request, "message", true);// use message instead
                                                                              // of PostBody
    postBody = DisableHtmlTagFilter.filter(postBody);// always disable HTML
    String beforeValidated = postBody;
    postBody = interceptorService.validateContent(postBody);

    // check validation bb code
    EnableMVNCodeFilter.checkValidation(postBody, locale);

    int logonMemberID = onlineUser.getMemberID();
    String logonMemberName = onlineUser.getMemberName();

    PostChecker.checkEditPost(onlineUser, postBean, forumBean);

    String postLastEditIP = request.getRemoteAddr();
    int postFormatOption = 0;// @todo review and support it later
    int postOption = 0;// @todo review and support it later
    int postStatus = postBean.getPostStatus();// use old post status
    String postIcon = GenericParamUtil.getParameter(request, "PostIcon");
    postIcon = DisableHtmlTagFilter.filter(postIcon);// always disable HTML

    boolean isPendingThread = false;
    boolean isForumModerator = permission.canModerateThread(forumID);
    int parentPostID = postBean.getParentPostID();

    // Ensure that moderator don't have to moderate the thread to enable it
    if (isForumModerator == false) {
      if ((parentPostID == 0) && forumBean.shouldModerateThread()) {
        isPendingThread = true;
        DAOFactory.getThreadDAO().updateThreadStatus(threadID, ThreadBean.THREAD_STATUS_DISABLED);
      } else if ((parentPostID != 0) && forumBean.shouldModeratePost()) {
        // we will not disable post that is a thread (parentPostID == 0)
        postStatus = PostBean.POST_STATUS_DISABLED;
      }
    }

    /*
     * Note that although the 2 methods below can be combined, I don't do that for clearness
     */
    /** @todo log the modification here */
    DAOFactory.getPostDAO().update(postID, // primary key
        logonMemberName, postTopic, postBody, now/* postLastEditDate */, postLastEditIP,
        postFormatOption, postOption, postStatus, postIcon);
    DAOFactory.getPostDAO().increaseEditCount(postID);

    // check whether censored words existed, if so send mail to Admin
    if (MVNForumConfig.getEnableEmailToAdminContentWithCensoredWords()) {
      if (beforeValidated.equals(postBody) == false) {
        PostUtil.sendEmailToAdminBecauseCensoredPost(request, postID, forumID, threadID,
            logonMemberID);
      }
    }
    if (postBean.getParentPostID() == 0) {// edit a top post (thread)
      String threadIcon = postIcon;
      int threadPriority = GenericParamUtil.getParameterUnsignedInt(request, "ThreadPriority",
          ThreadBean.THREAD_PRIORITY_NORMAL);
      DAOFactory.getThreadDAO().updateTopic_Body_Icon(threadID, postTopic, postBody, threadIcon,
          threadPriority);
    }

    boolean attachMore = GenericParamUtil.getParameterBoolean(request, "AttachMore");
    boolean addFavoriteThread =
        GenericParamUtil.getParameterBoolean(request, "AddFavoriteParentThread");
    boolean addWatchThread = GenericParamUtil.getParameterBoolean(request, "AddWatchParentThread");
    // add favorite thread if user checked it
    if (addFavoriteThread) {
      permission.ensureIsAuthenticated();
      // @todo: add checking of MVNForumConfig.getEnableFavoriteThread()
      // check to make sure that this user does not exceed his favorite max
      int currentFavoriteCount =
          DAOFactory.getFavoriteThreadDAO().getNumberOfFavoriteThreads_inMember(logonMemberID);
      int maxFavorites = MVNForumConfig.getMaxFavoriteThreads();
      if (currentFavoriteCount < maxFavorites) {
        Timestamp favoriteCreationDate = now;
        int favoriteType = 0; // @todo implement it later
        int favoriteOption = 0; // @todo implement it later
        int favoriteStatus = 0; // @todo implement it later

        // now check permission the this user have the readPost permission
        permission.ensureCanReadPost(forumID);

        // has the permission now, then insert to database
        try {
          DAOFactory.getFavoriteThreadDAO().create(logonMemberID, threadID, forumID,
              favoriteCreationDate, favoriteType, favoriteOption, favoriteStatus);
        } catch (DuplicateKeyException ex) {
          // already add favorite thread, just ignore
        }
      }
    }

    // add watch if user checked it
    if (addWatchThread) {
      permission.ensureIsAuthenticated();
      permission.ensureIsActivated();
      if (MVNForumConfig.getEnableWatch() == false) {
        // should never happen, because if it happen, then the whole process is broken
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "java.lang.IllegalStateException.cannot_add_watch.watch_is_disabled");
        throw new IllegalStateException(localizedMessage);
        // throw new IllegalStateException("Cannot add Watch because Watch feature is disabled by
        // administrator.");
      }

      int watchType = 0;// GenericParamUtil.getParameterInt(request, "WatchType");
      int watchOption = 0;// GenericParamUtil.getParameterInt(request, "WatchOption");
      int watchStatus = 0;// GenericParamUtil.getParameterInt(request, "WatchStatus");
      Timestamp watchCreationDate = now;
      Timestamp watchLastSentDate = now;
      Timestamp watchEndDate = now;// @todo: check it !!!

      try {
        DAOFactory.getWatchDAO().create(logonMemberID, 0/* watchCategoryID */, 0/* watchForumID */,
            threadID, watchType, watchOption, watchStatus, watchCreationDate, watchLastSentDate,
            watchEndDate);
      } catch (DuplicateKeyException ex) {
        // User try to create a duplicate watch, just ignore
      }
    }

    // - dat bien nay trong render portlet
    // - option add poll
    if (MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
        .getForumRunMode() == EnvironmentService.PRODUCT_ENTERPRISE
        && MVNForumConfig.getEnableAttachment() && permission.canAddAttachment(forumID)) {
      request.setAttribute("UploadApplet",
          Boolean.valueOf(GenericParamUtil.getParameterBoolean(request, "UploadApplet")));
    }

    request.setAttribute("PostID", String.valueOf(postID));
    request.setAttribute("ForumID", String.valueOf(forumID));
    request.setAttribute("ThreadID", String.valueOf(threadID));
    request.setAttribute("AttachMore", Boolean.valueOf(attachMore));
    request.setAttribute("IsPendingThread", Boolean.valueOf(isPendingThread));

    // now update the search index
    // @todo : modify for better performance here
    PostIndexer.scheduleUpdatePostTask(DAOFactory.getPostDAO().getPost(postID));

    // Clear the cache
    PostCache.getInstance().clear();
    ThreadCache.getInstance().clear();
  }

  public void updatePostSuccessForRender(GenericRequest request, GenericResponse response)
      throws DatabaseException, ObjectNotFoundException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int postID = (Integer.valueOf((String) request.getAttribute("PostID"))).intValue();
    int forumID = (Integer.valueOf((String) request.getAttribute("ForumID"))).intValue();

    PostBean postBean = DAOFactory.getPostDAO().getPost(postID);

    String addPostSuccessLabel =
        MVNForumResourceBundle.getString(locale, "mvnforum.user.updatepostsuccess.title");
    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response,
        forumID, null, null, addPostSuccessLabel);
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

    request.setAttribute("PostBean", postBean);
  }

  public void prepareDelete(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, BadInputException, DatabaseException,
      AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // user must have been authenticated before he can delete
    permission.ensureIsAuthenticated();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    // primary key column(s)
    int postID = GenericParamUtil.getParameterInt(request, "post");

    PostBean postBean = null;
    try {
      postBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int forumID = postBean.getForumID();
    try {
      ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // Note that this method will check the permission and
    // check if this post is too old, has replies, or is disabled
    PostChecker.checkDeletePost(onlineUser, postBean);

    request.setAttribute("PostBean", postBean);

    String title = MVNForumResourceBundle.getString(locale, "mvnforum.user.deletepost.title");

    StringBuffer stb = new StringBuffer();
    stb.append(title).append(": ").append(postBean.getPostTopic());
    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener =
        categoryService.getCategoryTreePath(request, response, forumID, null, null, stb.toString());
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

  }

  public void processDelete(GenericRequest request) throws BadInputException, DatabaseException,
      AuthenticationException, ObjectNotFoundException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // user must have been authenticated before he can delete
    permission.ensureIsAuthenticated();

    // now check the password
    MyUtil.ensureCorrectCurrentPassword(request);

    Locale locale = I18nUtil.getLocaleInRequest(request);

    // primary key column(s)
    int postID = GenericParamUtil.getParameterInt(request, "post");
    PostBean postBean = null;
    try {
      postBean = DAOFactory.getPostDAO().getPost(postID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.postid_not_exists",
          new Object[] {new Integer(postID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int forumID = postBean.getForumID();
    try {
      ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // delete the post and children attachments
    deletePost(request, postBean);

    int threadID = postBean.getThreadID();

    // now update the forum and thread statistics
    StatisticsUtil.updateForumStatistics(forumID);
    StatisticsUtil.updateThreadStatistics(threadID);

    // Clear the cache
    PostCache.getInstance().clear();
    ThreadCache.getInstance().clear();

    request.setAttribute("ForumID", String.valueOf(forumID));
    request.setAttribute("ThreadID", String.valueOf(threadID));
  }

  // Note that this method does not update the forum statistics and thread statistics
  private void deletePost(GenericRequest request, PostBean postBean) throws DatabaseException,
      AuthenticationException, BadInputException, ObjectNotFoundException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    // user must have been authenticated before he can delete
    permission.ensureIsAuthenticated();

    int parentPostID = postBean.getParentPostID();

    int postID = postBean.getPostID();

    // Note that this method will check the permission and
    // check if this post is too old, has replies, or is disabled
    PostChecker.checkDeletePost(onlineUser, postBean);

    // Delete all attachments in this post,
    // we must call this before any attempt to delete the post
    AttachmentWebHandler.deleteAttachments_inPost(postID);

    // now delete the post, note that we delete it after delete all child objects (attachment)
    DAOFactory.getPostDAO().delete(postID);

    try {
      DAOFactory.getPostDAO().updateParentPostID(postID, parentPostID);
    } catch (ObjectNotFoundException ex) {
      // we just ignore if no post is affect by this method
    }

    int memberID = postBean.getMemberID();
    StatisticsUtil.updateMemberStatistics(memberID);

    // now update the search index
    PostIndexer.scheduleDeletePostTask(postID, DeletePostIndexTask.OBJECT_TYPE_POST);
  }

  public void deleteSuccessForRender(GenericRequest request, GenericResponse response)
      throws ObjectNotFoundException, DatabaseException, AuthenticationException {

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int forumID = (Integer.valueOf((String) request.getAttribute("ForumID"))).intValue();

    String addPostSuccessLabel =
        MVNForumResourceBundle.getString(locale, "mvnforum.user.deletepostsuccess.title");
    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response,
        forumID, null, null, addPostSuccessLabel);
    categorytree.addCategeoryTreeListener(treelistener);

    request.setAttribute("tree", categorytree.build());
  }

  public void prepareModeratePendingPosts_limit(GenericRequest request) throws DatabaseException,
      AuthenticationException, BadInputException, DatabaseException, ObjectNotFoundException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int threadID = GenericParamUtil.getParameterInt(request, "thread");
    ThreadBean threadBean = null;
    try {
      threadBean = DAOFactory.getThreadDAO().getThread(threadID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
          new Object[] {new Integer(threadID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int forumID = threadBean.getForumID();

    try {
      ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    int numberOfPosts = DAOFactory.getPostDAO().getNumberOfDisablePosts_inThread(threadID);

    // user must have been authenticated before he can view pending/disabled threads
    permission.ensureIsAuthenticated();

    // check normal permission
    permission.ensureCanModerateThread(forumID);

    int postsPerPage = 10000;
    int offset = 0;

    Collection postBeans =
        DAOFactory.getPostDAO().getDisablePosts_inThread_limit(threadID, offset, postsPerPage);

    Iterator iterator = postBeans.iterator();
    while (iterator.hasNext()) {
      PostBean postBean = (PostBean) iterator.next();
      // very slow here
      /** @todo find a better solution */
      MemberBean memberBean = null;
      if (postBean.getMemberID() != 0
          && postBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_GUEST) {
        memberBean = MemberCache.getInstance().getMember(postBean.getMemberID());
      }
      postBean.setMemberBean(memberBean);

      int postAttachCount = postBean.getPostAttachCount();
      if ((postAttachCount > 0) && MVNForumConfig.getEnableAttachment()) {
        int postID = postBean.getPostID();
        Collection attachBeans = DAOFactory.getAttachmentDAO().getAttachments_inPost(postID);
        int actualAttachCount = attachBeans.size();

        // now check if the attachCount in talbe Post equals to the actual attachCount in table
        // Attachment
        if (postAttachCount != actualAttachCount) {
          if (actualAttachCount != DAOFactory.getAttachmentDAO()
              .getNumberOfAttachments_inPost(postID)) {
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                "java.lang.AssertionError.serious_error.cannot_process_attachment_count");
            throw new AssertionError(localizedMessage);
            // throw new AssertionError("AssertionError: Serious error: cannot process Attachment
            // Count in table Attachment");
          }
          log.warn(
              "The attachment count in table Post and Attachment are not synchronized. In table Post = "
                  + postAttachCount + " and in table Attachment = " + actualAttachCount
                  + ". Synchronize to " + actualAttachCount);
          DAOFactory.getPostDAO().updateAttachCount(postID, actualAttachCount);
        }
        if (actualAttachCount > 0) {
          postBean.setAttachmentBeans(attachBeans);
        }
      }
    }

    PostBean firstPostBean = DAOFactory.getPostDAO().getFirstPost_inThread(threadID);
    if (firstPostBean.getMemberID() != 0
        && firstPostBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_GUEST) {
      MemberBean memberBean = MemberCache.getInstance().getMember(firstPostBean.getMemberID());
      firstPostBean.setMemberBean(memberBean);
    }

    request.setAttribute("ThreadBean", threadBean);
    request.setAttribute("FirstPostBean", firstPostBean);
    request.setAttribute("PostBeans", postBeans);
    request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
  }

  public void processModeratePendingPosts(GenericRequest request) throws DatabaseException,
      AuthenticationException, BadInputException, DatabaseException, ObjectNotFoundException {

    SecurityUtil.checkHttpPostMethod(request);

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();
    Locale locale = I18nUtil.getLocaleInRequest(request);

    // user must have been authenticated before he can moderate pending/disabled posts
    permission.ensureIsAuthenticated();

    // check normal permission, note that we don't check
    // permission on a forumID because we allow moderate posts
    // in multiple forums even if the web interface does not support it
    int threadID = -1;
    int forumID = -1;
    try {
      threadID = GenericParamUtil.getParameterInt(request, "thread");
      ThreadBean threadBean = null;
      try {
        threadBean = ThreadCache.getInstance().getThread(threadID);
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
            new Object[] {new Integer(threadID)});
        throw new ObjectNotFoundException(localizedMessage);
      }
      forumID = threadBean.getForumID();
      permission.ensureCanModerateThread(forumID);
      ForumCache.getInstance().getBean(forumID).ensureNotDisabledForum(locale);
    } catch (BadInputException ex) {
      // just ignore, in case of use customized client
    }
    permission.ensureCanModerateThreadInAtLeastOneForum();

    try {
      String prefix = "modpostaction_";
      for (Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements();) {
        String param = (String) enumeration.nextElement();
        if (param.startsWith(prefix)) {
          String modValue = GenericParamUtil.getParameter(request, param, true);
          String strPostID = param.substring(prefix.length());
          int postID = Integer.parseInt(strPostID);
          if (modValue.equals("approve")) {
            PostBean postBean = null;
            try {
              postBean = DAOFactory.getPostDAO().getPost(postID);
            } catch (ObjectNotFoundException ex) {
              String localizedMessage = MVNForumResourceBundle.getString(locale,
                  "mvncore.exception.ObjectNotFoundException.postid_not_exists",
                  new Object[] {new Integer(postID)});
              throw new ObjectNotFoundException(localizedMessage);
            }
            int currentForumID = postBean.getForumID();
            permission.ensureCanModerateThread(currentForumID);
            DAOFactory.getPostDAO().updateStatus(postID, PostBean.POST_STATUS_DEFAULT);
          } else if (modValue.equals("delete")) {
            PostBean postBean = null;
            try {
              postBean = DAOFactory.getPostDAO().getPost(postID);
            } catch (ObjectNotFoundException ex) {
              String localizedMessage = MVNForumResourceBundle.getString(locale,
                  "mvncore.exception.ObjectNotFoundException.postid_not_exists",
                  new Object[] {new Integer(postID)});
              throw new ObjectNotFoundException(localizedMessage);
            }
            deletePost(request, postBean);
          } else {
            // it means ignore, do nothing
          }
        }
      }
    } finally {
      // now update the forum statistics
      if (forumID != -1) {
        StatisticsUtil.updateForumStatistics(forumID);
      }

      // now update the thread statistics
      if (threadID != -1) {
        StatisticsUtil.updateThreadStatistics(threadID);
      }
    }

    // Now clear the cache
    PostCache.getInstance().clear();
    ThreadCache.getInstance().clear();

    request.setAttribute("ForumID", String.valueOf(forumID));
    request.setAttribute("ThreadID", String.valueOf(threadID));
  }

  /**
   * This method is for viewthread page and printthread page
   *
   * @throws DatabaseException
   * @throws AuthenticationException
   * @throws BadInputException
   * @throws ObjectNotFoundException
   * @throws MissingURLMapEntryException
   * @throws IOException
   */
  public void prepareViewThread(GenericRequest request, GenericResponse response, String requestURI)
      throws AuthenticationException, DatabaseException, BadInputException, ObjectNotFoundException,
      MissingURLMapEntryException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    int threadID = 0;
    boolean printAll = false;
    boolean lastPage = false;
    Map params = null;
    String highlightKey = "";
    if (requestURI.startsWith("/viewthread_") || requestURI.startsWith("/printthread_")) {
      params = FriendlyURLParamUtil.getParameters(requestURI);

      if (params.get("thread") != null) {
        String inputStr = (String) params.get("thread");
        try {
          threadID = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
          String localizedMessage = MVNCoreResourceBundle.getString(locale,
              "mvncore.exception.BadInputException.cannot_parse",
              new Object[] {DisableHtmlTagFilter.filter("thread"), "int"});
          throw new BadInputException(localizedMessage);
        }
      } else {
        String localizedMessage = MVNCoreResourceBundle.getString(locale,
            "mvncore.exception.BadInputException.not_allow_to_be_empty",
            new Object[] {DisableHtmlTagFilter.filter("thread")});
        throw new BadInputException(localizedMessage);
      }

      printAll = ((params.get("printall") != null)
          && (((String) params.get("printall")).equalsIgnoreCase("yes"))) ? true : false;
      lastPage = ((params.get("lastpage") != null)
          && (((String) params.get("lastpage")).equalsIgnoreCase("yes"))) ? true : false;
      highlightKey = StringUtil.getEmptyStringIfNull((String) params.get("hl"));
    } else {
      threadID = GenericParamUtil.getParameterInt(request, "thread");
      printAll = GenericParamUtil.getParameterBoolean(request, "printall");
      lastPage = GenericParamUtil.getParameterBoolean(request, "lastpage");
      highlightKey = GenericParamUtil.getParameter(request, "hl", false);
    }

    ThreadBean threadBean = null;
    try {
      threadBean = ThreadCache.getInstance().getThread(threadID);
    } catch (ObjectNotFoundException ex) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
          new Object[] {new Integer(threadID)});
      throw new ObjectNotFoundException(localizedMessage);
    }
    int forumID = threadBean.getForumID();
    int numberOfPosts = DAOFactory.getPostDAO().getNumberOfEnablePosts_inThread(threadID);

    // check normal permission
    permission.ensureCanReadPost(forumID);

    ForumBean forumBean = null;
    try {
      forumBean = ForumCache.getInstance().getBean(forumID);
      forumBean.ensureNotDisabledForum(locale);
    } catch (ObjectNotFoundException e) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.ObjectNotFoundException.forumid_not_exists",
          new Object[] {new Integer(forumID)});
      throw new ObjectNotFoundException(localizedMessage);
    }

    // Only moderator can view disable threads
    if (threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED) {
      permission.ensureCanModerateThread(forumID);
    }

    int postsPerPage = onlineUser.getPostsPerPage();
    int offset = 0;
    if (lastPage) {
      // note that in the worst case, numberOfPosts could equals 0 (bad database)
      int pageCount = numberOfPosts / postsPerPage;
      int odd = numberOfPosts % postsPerPage;
      if (odd > 0) {
        pageCount++;
      }
      if (pageCount < 1) {
        pageCount = 1;// at least, there is one page
      }
      offset = (pageCount - 1) * postsPerPage;
    } else {
      try {
        if (requestURI.startsWith("/viewthread_") || requestURI.startsWith("/printthread_")) {
          String inputStr = (String) params.get("offset");
          try {
            offset = Integer.parseInt(inputStr);
            if (offset < 0) {
              String localizedMessage = MVNCoreResourceBundle.getString(locale,
                  "mvncore.exception.BadInputException.must_be_unsigned_value",
                  new Object[] {DisableHtmlTagFilter.filter("offset")});
              throw new BadInputException(localizedMessage);
            }
          } catch (NumberFormatException e) {
            String localizedMessage = MVNCoreResourceBundle.getString(locale,
                "mvncore.exception.BadInputException.cannot_parse",
                new Object[] {DisableHtmlTagFilter.filter("thread"), "int"});
            throw new BadInputException(localizedMessage);
          }
        } else {
          offset = GenericParamUtil.getParameterUnsignedInt(request, "offset");
        }
      } catch (BadInputException e) {
        // do nothing
      }
    }
    if (printAll) {
      postsPerPage = 10000; // We assume that big number
      offset = 0;
    }

    Collection postBeans = null;
    if (requestURI.startsWith("/viewthreadtree") || requestURI.startsWith("/viewthreadtree_")) {
      postBeans =
          DAOFactory.getPostDAO().getEnablePosts_inThread_limit(threadID, 0, Integer.MAX_VALUE);
    } else {
      postBeans =
          PostCache.getInstance().getEnablePosts_inThread_limit(threadID, offset, postsPerPage);
    }

    Iterator iterator = postBeans.iterator();
    String[] posts = new String[postBeans.size()];
    int postCounter = 0;
    int firstPostID = 0;
    while (iterator.hasNext()) {
      PostBean postBean = (PostBean) iterator.next();

      posts[postCounter] = String.valueOf(postBean.getPostID());

      postCounter++;
      if (postCounter == 1) {
        firstPostID = postBean.getPostID();
      }

      MemberBean memberBean = null;
      if (postBean.getMemberID() != 0
          && postBean.getMemberID() != MVNForumConstant.MEMBER_ID_OF_GUEST) {
        // Use cache for maximum performance
        try {
          memberBean = MemberCache.getInstance().getMember(postBean.getMemberID());
        } catch (ObjectNotFoundException e) {
          // do nothing, we accept null member because sometime in LDAP or portal, we cannot control
          // delete user in LDAP
        }
      }
      postBean.setMemberBean(memberBean);

      int postAttachCount = postBean.getPostAttachCount();
      if ((postAttachCount > 0) && MVNForumConfig.getEnableAttachment()) {
        int postID = postBean.getPostID();
        Collection attachBeans = DAOFactory.getAttachmentDAO().getAttachments_inPost(postID);
        int actualAttachCount = attachBeans.size();

        // now check if the attachCount in table Post equals to the actual attachCount in table
        // Attachment
        if (postAttachCount != actualAttachCount) {
          if (actualAttachCount != DAOFactory.getAttachmentDAO()
              .getNumberOfAttachments_inPost(postID)) {
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                "java.lang.AssertionError.serious_error.cannot_process_attachment_count");
            throw new AssertionError(localizedMessage);
            // throw new AssertionError("AssertionError: Serious error: cannot process Attachment
            // Count in table Attachment");
          }
          log.warn(
              "The attachment count in table Post and Attachment are not synchronized. In table Post = "
                  + postAttachCount + " and in table Attachment = " + actualAttachCount
                  + ". Synchronize to " + actualAttachCount);
          DAOFactory.getPostDAO().updateAttachCount(postID, actualAttachCount);
        }
        if (actualAttachCount > 0) {
          postBean.setAttachmentBeans(attachBeans);
        }
      }
    }

    // int previousTopic = DAOFactory.getThreadDAO().getPreviousEnableThread(forumID, threadID);//
    // can throw AssertionError
    // int nextTopic = DAOFactory.getThreadDAO().getNextEnableThread(forumID, threadID);// can throw
    // AssertionError
    int previousTopic = ThreadCache.getInstance().getPreviousEnableThread(forumID, threadID);// can
                                                                                             // throw
                                                                                             // AssertionError
    int nextTopic = ThreadCache.getInstance().getNextEnableThread(forumID, threadID);// can throw
                                                                                     // AssertionError

    int pendingPostCount = 0;
    if (permission.canModerateThread(forumID)) {
      pendingPostCount = DAOFactory.getPostDAO().getNumberOfDisablePosts_inThread(threadID);
    }

    threadBean.setThreadViewCount(threadBean.getThreadViewCount() + 1);
    DAOFactory.getThreadDAO().increaseViewCount(threadID);
    PostBean lastpostBean =
        PostCache.getInstance().getLastEnablePost_inThread(threadBean.getThreadID());
    request.setAttribute("ThreadBean", threadBean);
    request.setAttribute("PostBeans", postBeans);
    request.setAttribute("lastpostBean", lastpostBean);
    request.setAttribute("NumberOfPosts", new Integer(numberOfPosts));
    request.setAttribute("PreviousTopic", new Integer(previousTopic));
    request.setAttribute("NextTopic", new Integer(nextTopic));
    request.setAttribute("PendingPostCount", new Integer(pendingPostCount));
    request.setAttribute("firstPostID", new Integer(firstPostID));
    request.setAttribute("postsArray", posts);

    String threadLabel = MVNForumResourceBundle.getString(locale, "mvnforum.common.thread");

    CategoryBuilder treebuilder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree categorytree = new CategoryTree(treebuilder);
    CategoryTreeListener treelistener = categoryService.getCategoryTreePath(request, response,
        forumID, 0, threadLabel + ": " + MyUtil.filter(threadBean.getThreadTopic(), false/* html */,
            true/* emotion */, false/* mvnCode */, false/* newLine */, false/* URL */));
    categorytree.addCategeoryTreeListener(treelistener);
    request.setAttribute("tree", categorytree.build());

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener =
        categoryService.getManagementCategorySelector(request, response, "listthreads", forumID);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());

    request.setAttribute("thread", new Integer(threadID));

    // updateOnlineUserAction(request, requestURI) must be called after
    // request.setAttribute("thread", new Integer(threadID));
    // see: ActionInUserModule.ActionInUserModule()
    // int thread = ((Integer)request.getAttribute("thread")).intValue();
    OnlineUserUtil.updateOnlineUserAction(request, requestURI);

    if (MVNForumConfig.getEnableOnlineUsers()) {
      OnlineUserUtil.setRequestAttributeOfOnlineActions(request, Action.PAGE_ID_VIEWTHREAD,
          new Integer(threadID));
    }

    if (MVNForumConfig.getEnableEasyWatching()) {
      // check if this thread is being watched
      Boolean isWatched = Boolean.FALSE;
      if (onlineUser.isMember()) {
        isWatched = Boolean
            .valueOf(WatchUtil.isThreadWatched(onlineUser.getMemberID(), threadBean, forumBean));
      }
      request.setAttribute("isWatched", isWatched);
    }

    request.setAttribute("lastpage", lastPage ? "yes" : "no");
    request.setAttribute("offset", new Integer(offset));

    request.setAttribute("printall", Boolean.valueOf(printAll));
    request.setAttribute("HighlightKey", Encoder.decodeURL(highlightKey));

  }

  public void processSearch(GenericRequest request, GenericResponse response)
      throws BadInputException, IOException, DatabaseException, ObjectNotFoundException,
      AuthenticationException {

    OnlineUser onlineUser = onlineUserManager.getOnlineUser(request);
    MVNForumPermission permission = onlineUser.getPermission();

    Locale locale = I18nUtil.getLocaleInRequest(request);

    if (!MVNForumConfig.getEnableSearch()) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "java.lang.IllegalStateException.search_disabled");
      throw new IllegalStateException(localizedMessage);
    }

    MyUtil.saveVNTyperMode(request, response);

    CategoryBuilder builder = categoryBuilderService.getCategoryTreeBuilder();
    CategoryTree tree = new CategoryTree(builder);
    CategoryTreeListener listener = categoryService.getManagementCategorySelector(request, response,
        "search", CategoryTreeListener.NOT_SHOW_FULL_CATEGORY);
    tree.addCategeoryTreeListener(listener);
    request.setAttribute("Result", tree.build());

    // if user does not enter MemberName or attachment, then user must enter "key"
    String key = GenericParamUtil.getParameter(request, "key");

    String memberName = GenericParamUtil.getParameter(request, "member");
    StringUtil.checkGoodName(memberName, locale);

    int minAttachmentCount = GenericParamUtil.getParameterInt(request, "minattach", 0);

    if ((key.length() == 0) && (memberName.length() == 0) && (minAttachmentCount == 0)) {
      return;
    }
    int forumID = GenericParamUtil.getParameterInt(request, "forum", 0);// negative means category
    int sort =
        GenericParamUtil.getParameterInt(request, "sort", MVNForumConfig.getDefaultSearchOrderBy());
    int offset = GenericParamUtil.getParameterUnsignedInt(request, "offset", 0);
    int rows = GenericParamUtil.getParameterUnsignedInt(request, "rows", 20);
    if (rows == 0) {
      rows = 20;// fix NullPointerException when rows = 0
    }
    if (key.length() == 0 && memberName.length() > 0) {
      if (sort == PostSearchQuery.SEARCH_SORT_DEFAULT) {
        sort = PostSearchQuery.SEARCH_SORT_TIME_DESC;
      }
    }

    // offset should be even when divide with rowsToReturn
    offset = (offset / rows) * rows;

    PostSearchQuery query = new PostSearchQuery();

    if (key.length() > 0) {
      query.setSearchString(key);
      int scopeInPost = GenericParamUtil.getParameterInt(request, "scopeinpost",
          PostSearchQuery.SEARCH_ONLY_BODY | PostSearchQuery.SEARCH_ONLY_TITLE);
      query.setScopeInPost(scopeInPost);
    }

    if (memberName.length() > 0) {
      try {
        int memberID = MemberCache.getInstance().getMemberIDFromMemberName(memberName);
        query.setMemberId(memberID);
      } catch (ObjectNotFoundException ex) {
        String localizedMessage = MVNForumResourceBundle.getString(locale,
            "mvncore.exception.ObjectNotFoundException.membername_not_exists",
            new Object[] {memberName});
        throw new ObjectNotFoundException(localizedMessage);
      }
    }

    if (minAttachmentCount > 0) {
      query.setMinAttachmentCount(minAttachmentCount);
    }

    int searchDate =
        GenericParamUtil.getParameterUnsignedInt(request, "date", PostSearchQuery.SEARCH_ANY_DATE);
    int searchBeforeAfter =
        GenericParamUtil.getParameterInt(request, "beforeafter", PostSearchQuery.SEARCH_NEWER);

    if ((searchDate != PostSearchQuery.SEARCH_ANY_DATE) && (searchDate < 365 * 10)) { // 10 years
      long deltaTime = DateUtil.DAY * searchDate;

      Timestamp now = DateUtil.getCurrentGMTTimestamp();
      Timestamp from = null;
      Timestamp to = null;

      long currentTime = now.getTime();

      if (searchBeforeAfter == PostSearchQuery.SEARCH_NEWER) {
        from = new Timestamp(currentTime - deltaTime);
      } else {// older
        to = new Timestamp(currentTime - deltaTime);
      }

      query.setFromDate(from);
      query.setToDate(to);
    }

    if (forumID > 0) {
      query.setForumId(forumID);
    } else if (forumID < 0) {
      // choose to search in a category
      query.setForumId(forumID);
    } else {
      // forumID equals to 0, it mean global searching
      // just do nothing, lucene will search all forums (globally)
    }

    query.setSort(sort);

    query.searchDocuments(offset, rows, permission);
    int hitCount = query.getHitCount();
    Collection result = query.getPostResult();

    // Remove posts that current user do not have permission
    // NOTE: these below code does not remove the enable posts in
    // a disabled thread. This is not usually the case because
    // normally we will delete the thread instead of change it
    // from Enable to Disabled
    for (Iterator iter = result.iterator(); iter.hasNext();) {
      PostBean postBean = (PostBean) iter.next();
      int currentForumID = postBean.getForumID();
      if (ForumCache.getInstance().getBean(currentForumID)
          .getForumStatus() == ForumBean.FORUM_STATUS_DISABLED) {
        iter.remove();
      } else if ((permission.canReadPost(currentForumID) == false)
          || (postBean.getPostStatus() == PostBean.POST_STATUS_DISABLED)) {
        iter.remove();
      } else if (postBean.getParentPostID() == 0) {// first post
        // Please note that the first post is always Enable even if
        // the thread is Disable. In this case always show result
        // if the current user is moderator
        if (permission.canModerateThread(currentForumID) == false) {
          int threadID = postBean.getThreadID();
          ThreadBean threadBean = null;
          try {
            threadBean = ThreadCache.getInstance().getThread(threadID);
          } catch (ObjectNotFoundException ex) {
            String localizedMessage = MVNForumResourceBundle.getString(locale,
                "mvncore.exception.ObjectNotFoundException.threadid_not_exists",
                new Object[] {new Integer(threadID)});
            throw new ObjectNotFoundException(localizedMessage);
          }
          if (threadBean.getThreadStatus() == ThreadBean.THREAD_STATUS_DISABLED) {
            iter.remove();
          }
        }
      }
    }

    if (offset > hitCount) {
      String localizedMessage = MVNForumResourceBundle.getString(locale,
          "mvncore.exception.BadInputException.offset_greater_than_total_rows");
      throw new BadInputException(localizedMessage);
      // throw new BadInputException("Cannot search with offset > total posts");
    }

    // request.setAttribute("key", Encoder.encodeURL(key));
    // request.setAttribute("member", memberName);
    // request.setAttribute("forum", new Integer(forumID));
    // request.setAttribute("offset", new Integer(offset));
    // request.setAttribute("attachment", Boolean.valueOf(withAttachment));
    request.setAttribute("rows", new Integer(rows));
    request.setAttribute("TotalPosts", new Integer(hitCount));
    request.setAttribute("PostBeans", result);
    request.setAttribute("SearchQuery", query);
    request.setAttribute("forum", new Integer(forumID));
  }

}
