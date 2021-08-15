/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/MVNForumConfig.java,v 1.292 2010/08/10
 * 11:52:50 minhnn Exp $ $Author: minhnn $ $Revision: 1.292 $ $Date: 2010/08/10 11:52:50 $
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
 *
 * @author: Igor Manic
 */
package com.mvnforum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.MemberBean;
import com.mvnforum.db.WatchBean;
import com.mvnforum.service.MvnForumServiceFactory;
import freemarker.template.Configuration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.security.FloodControlHour;
import net.myvietnam.mvncore.security.FloodControlMinute;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.FileUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.util.StringUtil;

@Slf4j
public final class MVNForumConfig {

  private MVNForumConfig() {}

  private static final String OPTION_FILE_NAME = "mvnforum.xml";
  public static final String DEFAULT = "default";

  @Getter
  @Setter
  private static String googleRecaptchaSiteKey;

  @Getter
  @Setter
  private static String googleRecaptchaSecretKey;

  private static boolean enableBrandName = true;

  public static boolean getEnableBrandName() {
    return enableBrandName;
  }

  public static void setEnableBrandName(boolean enable) {
    enableBrandName = enable;
  }

  // This information will be loaded in the enterprise version
  private static boolean enableChat = false;

  public static boolean getEnableChat() {
    return enableChat;
  }

  public static void setEnableChat(boolean enable) {
    enableChat = enable;
  }

  public static final int CMS_NEWS_VIEW_MODE_DISABLED = 0;
  public static final int CMS_NEWS_VIEW_MODE_LIST = 1;
  public static final int CMS_NEWS_VIEW_MODE_TAB = 2;

  private static int cmsNewsViewMode = CMS_NEWS_VIEW_MODE_DISABLED;

  public static int getCMSNewsViewMode() {
    return cmsNewsViewMode;
  }

  public static void setCMSNewsViewMode(int viewMode) {
    if (viewMode != MVNForumConfig.CMS_NEWS_VIEW_MODE_DISABLED
        && viewMode != MVNForumConfig.CMS_NEWS_VIEW_MODE_LIST
        && viewMode != MVNForumConfig.CMS_NEWS_VIEW_MODE_TAB) {
      throw new IllegalArgumentException(
          "cms_news_view_mode must be " + MVNForumConfig.CMS_NEWS_VIEW_MODE_DISABLED + ", "
              + MVNForumConfig.CMS_NEWS_VIEW_MODE_LIST + ", or "
              + MVNForumConfig.CMS_NEWS_VIEW_MODE_TAB);
    }
    cmsNewsViewMode = viewMode;
  }

  private static int cmsNewsTableWidth = 100;

  public static int getCMSNewsTableWidth() {
    return cmsNewsTableWidth;
  }

  public static void setCMSNewsTableWidth(int tableWidth) {
    if (tableWidth < 0 || tableWidth > 100) {
      throw new IllegalArgumentException(
          "cms_news_table_width must be greater than 0 and less than 100");
    }
    cmsNewsTableWidth = tableWidth;
  }

  private static boolean enablePrivateAlbum = false;

  public static boolean getEnablePrivateAlbum() {
    return enablePrivateAlbum;
  }

  public static void setEnablePrivateAlbum(boolean enable) {
    enablePrivateAlbum = enable;
  }

  private static boolean enablePublicAlbum = false;

  public static boolean getEnablePublicAlbum() {
    return enablePublicAlbum;
  }

  public static void setEnablePublicAlbum(boolean enable) {
    enablePublicAlbum = enable;
  }

  private static boolean enableAppletUploadImage = false;

  public static boolean getEnableAppletUploadImage() {
    return enableAppletUploadImage;
  }

  public static void setEnableAppletUploadImage(boolean enable) {
    enableAppletUploadImage = enable;
  }

  private static boolean enablePoll = false;

  public static boolean getEnablePoll() {
    return enablePoll;
  }

  public static void setEnablePoll(boolean enable) {
    enablePoll = enable;
  }

  private static boolean enableVote = false;

  public static boolean getEnableVote() {
    return enableVote;
  }

  public static void setEnableVote(boolean enable) {
    enableVote = enable;
  }

  private static boolean enableRevote = false;

  public static boolean getEnableRevote() {
    return enableRevote;
  }

  public static void setEnableRevote(boolean enable) {
    enableRevote = enable;
  }

  private static boolean enableViewVoteResult = false;

  public static boolean getEnableViewVoteResult() {
    return enableViewVoteResult;
  }

  public static void setEnableViewVoteResult(boolean enable) {
    enableViewVoteResult = enable;
  }

  private static boolean enableWatchGateway = false;

  public static boolean getEnableWatchGateway() {
    return enableWatchGateway;
  }

  public static void setEnableWatchGateway(boolean enable_watch_gateway) {
    enableWatchGateway = enable_watch_gateway;
  }

  private static boolean onlyNormalThreadTypeInActiveThreads = false;

  public static boolean getOnlyNormalThreadTypeInActiveThreads() {
    return onlyNormalThreadTypeInActiveThreads;
  }

  public static void setOnlyNormalThreadTypeInActiveThreads(boolean enable) {
    onlyNormalThreadTypeInActiveThreads = enable;
  }

  private static boolean enableLowPriorityIcon = false;

  public static boolean getEnableLowPriorityIcon() {
    return enableLowPriorityIcon;
  }

  public static void setEnableLowPriorityIcon(boolean enable) {
    enableLowPriorityIcon = enable;
  }

  private static boolean enableNormalPriorityIcon = false;

  public static boolean getEnableNormalPriorityIcon() {
    return enableNormalPriorityIcon;
  }

  public static void setEnableNormalPriorityIcon(boolean enable) {
    enableNormalPriorityIcon = enable;
  }

  private static boolean enableHighPriorityIcon = true;

  public static boolean getEnableHighPriorityIcon() {
    return enableHighPriorityIcon;
  }

  public static void setEnableHighPriorityIcon(boolean enable) {
    enableHighPriorityIcon = enable;
  }

  private static boolean m_showUserArea = true;

  public static boolean getShouldShowUserArea() {
    return m_showUserArea;
  }

  public static void setShouldShowUserArea(boolean showUserArea) {
    m_showUserArea = showUserArea;
  }

  private static boolean m_guestUserInDatabase = false;

  public static boolean isGuestUserInDatabase() {
    return m_guestUserInDatabase;
  }

  public static void setGuestUserInDatabase(boolean enable) {
    m_guestUserInDatabase = enable;
  }

  private static String tempDir = "";

  private static String searchPostIndexDir = "";
  private static String searchMemberIndexDir = "";
  private static String searchAttachmentIndexDir = "";
  private static String searchAlbumItemIndexDir = "";

  private static String attachmentDir = "";
  private static String pmAttachmentDir = "";
  private static String backupDir = "";
  private static String templateDir = "";
  private static String logDir = "";
  private static String avatarDir = "";
  private static String albumItemDir = "";

  private static String logFile = "";

  private static void setMVNForumHome(String home) {

    // now check the read/write permission by writing a temporary file
    try {
      File homeFile = new File(home);
      log.info("Absolute  mvnForumHome folder = " + homeFile.getAbsolutePath());
      log.info("Canonical mvnForumHome folder = " + homeFile.getCanonicalPath());

      // always create a directory, if the directory already existed, nothing happen
      FileUtil.createDirs(home, true);

      String tempFilename = home + File.separatorChar + "mvnforum_tempfile.tmp";
      File tempFile = new File(tempFilename);
      if (log.isDebugEnabled()) {
        log.debug("Temp file = " + tempFilename);
        log.debug("Absolute filename of temp file for testing write access = "
            + tempFile.getAbsolutePath());
      }

      FileOutputStream fos = new FileOutputStream(tempFilename);
      fos.write(tempFilename.getBytes());
      fos.close();

      tempFile.delete();

      // now create the directories if not exist
      tempDir = MVNForumHome + File.separatorChar + "temp";
      FileUtil.createDirs(tempDir, true);

      searchPostIndexDir =
          MVNForumHome + File.separatorChar + "search" + File.separatorChar + "post";
      FileUtil.createDirs(searchPostIndexDir, true);

      searchMemberIndexDir =
          MVNForumHome + File.separatorChar + "search" + File.separatorChar + "member";
      FileUtil.createDirs(searchMemberIndexDir, true);

      searchAttachmentIndexDir =
          MVNForumHome + File.separatorChar + "search" + File.separatorChar + "attachment";
      FileUtil.createDirs(searchAttachmentIndexDir, true);

      searchAlbumItemIndexDir =
          MVNForumHome + File.separatorChar + "search" + File.separatorChar + "albumitem";
      FileUtil.createDirs(searchAlbumItemIndexDir, true);

      attachmentDir = MVNForumHome + File.separatorChar + "attachment";
      FileUtil.createDirs(attachmentDir, true);

      pmAttachmentDir = MVNForumHome + File.separatorChar + "pm_attachment";
      FileUtil.createDirs(pmAttachmentDir, true);

      backupDir = MVNForumHome + File.separatorChar + "backup";
      FileUtil.createDirs(backupDir, true);

      // this dir is created as a recommended folder to store the log files
      logDir = MVNForumHome + File.separatorChar + "log";
      FileUtil.createDirs(logDir, true);

      avatarDir = MVNForumHome + File.separatorChar + "memberavatars";
      FileUtil.createDirs(avatarDir, true);

      // this dir is created as a recommended folder to store the template files
      templateDir = MVNForumHome + File.separatorChar + "template";
      FileUtil.createDirs(templateDir, true);

      albumItemDir = MVNForumHome + File.separatorChar + "albumdata";
      FileUtil.createDirs(albumItemDir, true);

    } catch (IOException ex) {
      log.error("Cannot setup the mvnForumHome folder. Please correct it first.", ex);
      MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
          .setShouldStop("Check your mvnForumHome. Detail : " + ex.getMessage());
    } catch (AssertionError ae) {
      // should never happen, maybe in the future we should remove the Assertion in getNumberOfBeans
      log.error("Assertion error. Please correct it first.", ae);
      MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
          .setShouldStop("Assertion error. Detail : " + ae.getMessage());
    }
  }

  private static Configuration freeMarkerConfiguration;

  public static Configuration getFreeMarkerConfiguration() {
    return freeMarkerConfiguration;
  }

  private static String MVNForumHome = "mvnForumHome";

  public static String getMVNForumHome() {
    return MVNForumHome;
  }

  public static String getTempDir() {
    return tempDir;
  }

  public static String getSearchPostIndexDirName() {
    if (MvnForumServiceFactory.getMvnForumService().getSearchService().savePostOnDisk()) {
      return searchPostIndexDir;
    }
    throw new IllegalStateException("Cannot get search index dir name because type is not disk.");
  }

  public static String getSearchMemberIndexDirName() {
    if (MvnForumServiceFactory.getMvnForumService().getSearchService().saveMemberOnDisk()) {
      return searchMemberIndexDir;
    }
    throw new IllegalStateException("Cannot get search index dir name because type is not disk.");
  }

  public static String getSearchAttachmentIndexDirName() {
    if (MvnForumServiceFactory.getMvnForumService().getSearchService().saveAttachmentOnDisk()) {
      return searchAttachmentIndexDir;
    }
    throw new IllegalStateException("Cannot get search index dir name because type is not disk.");
  }

  public static String getSearchAlbumItemIndexDirName() {
    if (MvnForumServiceFactory.getMvnForumService().getSearchService().saveAlbumItemOnDisk()) {
      return searchAlbumItemIndexDir;
    }
    throw new IllegalStateException("Cannot get search index dir name because type is not disk.");
  }

  public static String getAttachmentDir() {
    return attachmentDir;
  }

  public static String getPmAttachmentDir() {
    return pmAttachmentDir;
  }

  public static String getTemplateDir() {
    return templateDir;
  }

  public static String getBackupDir() {
    return backupDir;
  }

  public static String getLogDir() {
    return logDir;
  }

  public static String getLogFile() {
    return logFile;
  }

  public static void setLogFile(String file) {
    logFile = file;
  }

  public static String getAvatarDir() {
    return avatarDir;
  }

  public static String getAlbumItemDir() {
    return albumItemDir;
  }

  private static String redirectLoginURL = "/login";

  public static String getRedirectLoginURL() {
    return redirectLoginURL;
  }

  public static void setRedirectLoginURL(String url) {
    redirectLoginURL = url;
  }

  private static String redirectLogoutURL = "/logout";

  public static String getRedirectLogoutURL() {
    return redirectLogoutURL;
  }

  public static void setRedirectLogoutURL(String url) {
    redirectLogoutURL = url;
  }

  private static String webMasterEmail = "youremail@yourdomain.com";

  public static String getWebMasterEmail() {
    return webMasterEmail;
  }

  public static void setWebMasterEmail(String email) {
    webMasterEmail = email;
  }

  private static String watchEmail = "youremail@yourdomain.com";

  public static String getWatchEmail() {
    return watchEmail;
  }

  public static void setWatchEmail(String email) {
    watchEmail = email;
  }

  private static String logoURL = "http://www.mvnForum.com";

  public static String getLogoUrl() {
    return logoURL;
  }

  public static void setLogoUrl(String url) {
    logoURL = url;
  }

  private static String[] supportedLocaleNames = new String[0];
  private static Locale[] supportedLocales = new Locale[0];

  public static String[] getSupportedLocaleNames() {
    return supportedLocaleNames;
  }

  public static Locale[] getSupportedLocales() {
    return supportedLocales;
  }

  private static String[] supportedImageLocaleNames = new String[0];

  public static String[] getSupportedImageLocaleNames() {
    return supportedImageLocaleNames;
  }

  private static String defaultLocaleName = "en";

  public static String getDefaultLocaleName() {
    return defaultLocaleName;
  }

  public static void setDefaultLocaleName(String localeName) {
    defaultLocaleName = localeName;
  }

  private static Locale defaultLocale = null;

  public static Locale getDefaultLocale() {
    return defaultLocale;
  }

  public static void setDefaultLocale(Locale locale) {
    defaultLocale = locale;
  }

  private static String localeParameterName = "lang";

  public static String getLocaleParameterName() {
    return localeParameterName;
  }

  public static void setLocaleParameterName(String name) {
    localeParameterName = name;
  }

  private static Locale eventLogLocale = Locale.ENGLISH;

  public static Locale getEventLogLocale() {
    return eventLogLocale;
  }

  public static void setEventLogLocale(Locale logLocale) {
    eventLogLocale = logLocale;
  }

  /**
   * Default username of a virtual Guest user. Will be overriden with the data from the database, if
   * it exists (for the Guest user). Admin can give him a name he wants, like "Guest", "Anonymous",
   * "Surfer", or even use a language different than English.
   */
  private static String defaultGuestName = "Guest";

  public static String getDefaultGuestName() {
    return defaultGuestName;
  }

  public static void setDefaultGuestName(String name) {
    defaultGuestName = name;
  }

  private static double defaultGuestTimeZone = 0;

  public static double getDefaultGuestTimeZone() {
    return defaultGuestTimeZone;
  }

  public static void setDefaultGuestTimeZone(double timeZone) {
    defaultGuestTimeZone = timeZone;
  }

  /**
   * By default, mvnForum disable passwordless authentication If you want to authenticate user from
   * realm or customized methods, then set the variable to true (AT YOUR OWN RISK, although I have
   * not found any security issues until now)
   */
  private static boolean enablePasswordlessAuth = false;

  public static boolean getEnablePasswordlessAuth() {
    return enablePasswordlessAuth;
  }

  public static void setEnablePasswordlessAuth(boolean enable) {
    enablePasswordlessAuth = enable;
  }

  private static boolean requireActivation = false;

  public static boolean getRequireActivation() {
    return requireActivation;
  }

  public static void setRequireActivation(boolean enable) {
    requireActivation = enable;
  }

  private static boolean enableLoginInfoInCookie = true;

  public static boolean getEnableLoginInfoInCookie() {
    return enableLoginInfoInCookie;
  }

  public static void setEnableLoginInfoInCookie(boolean enable) {
    enableLoginInfoInCookie = enable;
  }

  private static boolean enableLoginInfoInSession = true;

  public static boolean getEnableLoginInfoInSession() {
    return enableLoginInfoInSession;
  }

  public static void setEnableLoginInfoInSession(boolean enable) {
    enableLoginInfoInSession = enable;
  }

  private static boolean enableLoginInfoInRealm = false;

  public static boolean getEnableLoginInfoInRealm() {
    return enableLoginInfoInRealm;
  }

  public static void setEnableLoginInfoInRealm(boolean enable) {
    enableLoginInfoInRealm = enable;
  }

  private static boolean enableLoginInfoInCAS = false;

  public static boolean getEnableLoginInfoInCAS() {
    return enableLoginInfoInCAS;
  }

  public static void setEnableLoginInfoInCAS(boolean enable) {
    enableLoginInfoInCAS = enable;
  }

  private static boolean enableLoginInfoInOpenID = false;

  public static boolean getEnableLoginInfoInOpenID() {
    return enableLoginInfoInOpenID;
  }

  public static void setEnableLoginInfoInOpenID(boolean enable) {
    enableLoginInfoInOpenID = enable;
  }

  private static boolean enableLoginInfoInCustomization = false;

  public static boolean getEnableLoginInfoInCustomization() {
    return enableLoginInfoInCustomization;
  }

  public static void setEnableLoginInfoInCustomization(boolean enable) {
    enableLoginInfoInCustomization = enable;
  }

  private static boolean enableLogin = true;

  public static boolean getEnableLogin() {
    return enableLogin;
  }

  public static void setEnableLogin(boolean enable) {
    enableLogin = enable;
  }

  private static boolean enableCacheMember = true;

  public static boolean getEnableCacheMember() {
    return enableCacheMember;
  }

  public static void setEnableCacheMember(boolean enable) {
    enableCacheMember = enable;
  }

  private static boolean enableCachePost = true;

  public static boolean getEnableCachePost() {
    return enableCachePost;
  }

  public static void setEnableCachePost(boolean enable) {
    enableCachePost = enable;
  }

  private static boolean enableCacheThread = true;

  public static boolean getEnableCacheThread() {
    return enableCacheThread;
  }

  public static void setEnableCacheThread(boolean enable) {
    enableCacheThread = enable;
  }

  private static boolean enableCacheForum = true;

  public static boolean getEnableCacheForum() {
    return enableCacheForum;
  }

  public static void setEnableCacheForum(boolean enable) {
    enableCacheForum = enable;
  }

  private static boolean enableCacheCategory = true;

  public static boolean getEnableCacheCategory() {
    return enableCacheCategory;
  }

  public static void setEnableCacheCategory(boolean enable) {
    enableCacheCategory = enable;
  }

  private static boolean enableGuestViewImageAttachment = false;

  public static boolean getEnableGuestViewImageAttachment() {
    return enableGuestViewImageAttachment;
  }

  public static void setEnableGuestViewImageAttachment(boolean enable) {
    enableGuestViewImageAttachment = enable;
  }

  private static boolean enableGuestViewListUsers = false;

  public static boolean getEnableGuestViewListUsers() {
    return enableGuestViewListUsers;
  }

  public static void setEnableGuestViewListUsers(boolean enable) {
    enableGuestViewListUsers = enable;
  }

  private static boolean enableNewMember = true;

  public static boolean getEnableNewMember() {
    return enableNewMember;
  }

  public static void setEnableNewMember(boolean enable) {
    enableNewMember = enable;
  }

  private static boolean enableNewPost = true;

  public static boolean getEnableNewPost() {
    return enableNewPost;
  }

  public static void setEnableNewPost(boolean enable) {
    enableNewPost = enable;
  }

  private static boolean enableSplitThread = true;

  public static boolean getEnableSplitThread() {
    return enableSplitThread;
  }

  public static void setEnableSplitThread(boolean enable) {
    enableSplitThread = enable;
  }

  private static boolean enableAvatar = true;

  public static boolean getEnableAvatar() {
    return enableAvatar;
  }

  public static void setEnableAvatar(boolean enable) {
    enableAvatar = enable;
  }

  private static boolean enableEmotion = true;

  public static boolean getEnableEmoticon() {
    return enableEmotion;
  }

  public static void setEnableEmoticon(boolean enable) {
    enableEmotion = enable;
  }

  private static boolean enableRSS = true;

  public static boolean getEnableRSS() {
    return enableRSS;
  }

  public static void setEnableRSS(boolean enable) {
    enableRSS = enable;
  }

  private static boolean enableSearch = true;

  public static boolean getEnableSearch() {
    return enableSearch;
  }

  public static void setEnableSearch(boolean enable) {
    enableSearch = enable;
  }

  private static int defaultSearchOrderBy = 0;

  public static int getDefaultSearchOrderBy() {
    return defaultSearchOrderBy;
  }

  public static void setDefaultSearchOrderBy(int searchOrderBy) {
    defaultSearchOrderBy = searchOrderBy;
  }

  private static boolean enableWatch = true;

  public static boolean getEnableWatch() {
    return enableWatch;
  }

  public static void setEnableWatch(boolean enable) {
    enableWatch = enable;
  }

  private static boolean enableAttachment = true;

  public static boolean getEnableAttachment() {
    return enableAttachment;
  }

  public static void setEnableAttachment(boolean enable) {
    enableAttachment = enable;
  }

  private static boolean enableMessageAttachment = true;

  public static boolean getEnableMessageAttachment() {
    return enableMessageAttachment;
  }

  public static void setEnableMessageAttachment(boolean enable) {
    enableMessageAttachment = enable;
  }

  private static boolean enableMostActiveThreads = true;

  public static boolean getEnableMostActiveThreads() {
    return enableMostActiveThreads;
  }

  public static void setEnableMostActiveThreads(boolean enable) {
    enableMostActiveThreads = enable;
  }

  private static boolean enableMostActiveMembers = true;

  public static boolean getEnableMostActiveMembers() {
    return enableMostActiveMembers;
  }

  public static void setEnableMostActiveMembers(boolean enable) {
    enableMostActiveMembers = enable;
  }

  private static boolean enableSiteStatisticsOverview = true;

  public static boolean getEnableSiteStatisticsOverview() {
    return enableSiteStatisticsOverview;
  }

  public static void setEnableSiteStatisticsOverview(boolean enable) {
    enableSiteStatisticsOverview = enable;
  }

  private static boolean enableListNewMembersInRecentDays = false;

  public static boolean getEnableListNewMembersInRecentDays() {
    return enableListNewMembersInRecentDays;
  }

  public static void setEnableListNewMembersInRecentDays(boolean enable) {
    enableListNewMembersInRecentDays = enable;
  }

  private static boolean enableListUsersBrowsingForum = false;

  public static boolean getEnableListUsersBrowsingForum() {
    return enableListUsersBrowsingForum;
  }

  public static void setEnableListUsersBrowsingForum(boolean enable) {
    enableListUsersBrowsingForum = enable;
  }

  private static boolean enableListUsersBrowsingThread = false;

  public static boolean getEnableListUsersBrowsingThread() {
    return enableListUsersBrowsingThread;
  }

  public static void setEnableListUsersBrowsingThread(boolean enable) {
    enableListUsersBrowsingThread = enable;
  }

  private static int daysToShowRecentMembers = 1;

  public static int getDaysToShowRecentMembers() {
    return daysToShowRecentMembers;
  }

  public static void setDaysToShowRecentMembers(int days) {
    daysToShowRecentMembers = days;
  }

  private static boolean enableEmailThreateningContent = true;

  public static boolean getEnableEmailThreateningContent() {
    return enableEmailThreateningContent;
  }

  public static void setEnableEmailThreateningContent(boolean enable) {
    enableEmailThreateningContent = enable;
  }

  private static boolean enableEmailToAdminContentWithCensoredWords = true;

  public static boolean getEnableEmailToAdminContentWithCensoredWords() {
    return enableEmailToAdminContentWithCensoredWords;
  }

  public static void setEnableEmailToAdminContentWithCensoredWords(boolean enable) {
    enableEmailToAdminContentWithCensoredWords = enable;
  }

  private static int defaultCategoryID = 1;

  public static int getDefaultCateogryID() {
    return defaultCategoryID;
  }

  public static void setDefaultCateogryID(int defaultID) {
    defaultCategoryID = defaultID;
  }

  private static boolean enableRequestPrivateForum = false;

  public static boolean getEnableRequestPrivateForum() {
    return enableRequestPrivateForum;
  }

  public static void setEnableRequestPrivateForum(boolean enable) {
    enableRequestPrivateForum = enable;
  }

  private static int maxAttachmentSize = 1024;

  public static int getMaxAttachmentSize() {
    return maxAttachmentSize;
  }

  public static void setMaxAttachmentSize(int maxSize) {
    maxAttachmentSize = maxSize;
  }

  private static int maxMessageAttachmentSize = 1024;

  public static int getMaxMessageAttachmentSize() {
    return maxMessageAttachmentSize;
  }

  public static void setMaxMessageAttachmentSize(int maxSize) {
    maxMessageAttachmentSize = maxSize;
  }

  // Default is false, but in KG it should be true
  private static boolean enableAutoForumOwner = false;

  public static boolean getEnableAutoForumOwner() {
    return enableAutoForumOwner;
  }

  public static void setEnableAutoForumOwner(boolean enable) {
    enableAutoForumOwner = enable;
  }

  private static boolean enableCaptcha = false;

  public static boolean getEnableCaptcha() {
    return enableCaptcha;
  }

  public static void setEnableCaptcha(boolean enable) {
    enableCaptcha = enable;
  }

  private static boolean enablePortalLikeIndexPage = true;

  public static boolean getEnablePortalLikeIndexPage() {
    return enablePortalLikeIndexPage;
  }

  public static void setEnablePortalLikeIndexPage(boolean enable) {
    enablePortalLikeIndexPage = enable;
  }

  private static boolean enableAdminCanChangePassword = true;

  public static boolean getEnableAdminCanChangePassword() {
    return enableAdminCanChangePassword;
  }

  public static void setEnableAdminCanChangePassword(boolean enable) {
    enableAdminCanChangePassword = enable;
  }

  private static boolean enableShowLastLoginOfCurrentMember = true;

  public static boolean getEnableShowLastLoginOfCurrentMember() {
    return enableShowLastLoginOfCurrentMember;
  }

  public static void setEnableShowLastLoginOfCurrentMember(boolean enable) {
    enableShowLastLoginOfCurrentMember = enable;
  }

  private static boolean enableAutoWatching = false;

  public static boolean getEnableAutoWatching() {
    return enableAutoWatching;
  }

  public static void setEnableAutoWatching(boolean enable) {
    enableAutoWatching = enable;
  }

  private static boolean enableEasyWatching = true;

  public static boolean getEnableEasyWatching() {
    return enableEasyWatching;
  }

  public static void setEnableEasyWatching(boolean enable) {
    enableEasyWatching = enable;
  }

  private static boolean enableSendWatchMailOfMyOwnPost = false;

  public static boolean getEnableSendWatchMailOfMyOwnPost() {
    return enableSendWatchMailOfMyOwnPost;
  }

  public static void setEnableSendWatchMailOfMyOwnPost(boolean enable) {
    enableSendWatchMailOfMyOwnPost = enable;
  }

  private static boolean enableShowLastLogin = true;

  public static boolean getEnableShowLastLogin() {
    return enableShowLastLogin;
  }

  public static void setEnableShowLastLogin(boolean enable) {
    enableShowLastLogin = enable;
  }

  private static boolean enableShowModifiedDate = true;

  public static boolean getEnableShowModifiedDate() {
    return enableShowModifiedDate;
  }

  public static void setEnableShowModifiedDate(boolean enable) {
    enableShowModifiedDate = enable;
  }

  private static boolean enableShowEmail = true;

  public static boolean getEnableShowEmail() {
    return enableShowEmail;
  }

  public static void setEnableShowEmail(boolean enable) {
    enableShowEmail = enable;
  }

  private static boolean enableShowNameVisible = true;

  public static boolean getEnableShowNameVisible() {
    return enableShowNameVisible;
  }

  public static void setEnableShowNameVisible(boolean enable) {
    enableShowNameVisible = enable;
  }

  private static boolean enableShowEmailVisible = true;

  public static boolean getEnableShowEmailVisible() {
    return enableShowNameVisible;
  }

  public static void setEnableShowEmailVisible(boolean enable) {
    enableShowNameVisible = enable;
  }

  private static boolean enableShowBirthday = true;

  public static boolean getEnableShowBirthday() {
    return enableShowBirthday;
  }

  public static void setEnableShowBirthday(boolean enable) {
    enableShowBirthday = enable;
  }

  private static boolean enableShowGender = true;

  public static boolean getEnableShowGender() {
    return enableShowGender;
  }

  public static void setEnableShowGender(boolean enable) {
    enableShowGender = enable;
  }

  private static boolean enableShowAddress = true;

  public static boolean getEnableShowAddress() {
    return enableShowAddress;
  }

  public static void setEnableShowAddress(boolean enable) {
    enableShowAddress = enable;
  }

  private static boolean enableShowCity = true;

  public static boolean getEnableShowCity() {
    return enableShowCity;
  }

  public static void setEnableShowCity(boolean enable) {
    enableShowCity = enable;
  }

  private static boolean enableShowState = true;

  public static boolean getEnableShowState() {
    return enableShowState;
  }

  public static void setEnableShowState(boolean enable) {
    enableShowState = enable;
  }

  private static boolean enableShowCountry = true;

  public static boolean getEnableShowCountry() {
    return enableShowCountry;
  }

  public static void setEnableShowCountry(boolean enable) {
    enableShowCountry = enable;
  }

  private static boolean enableShowPhone = true;

  public static boolean getEnableShowPhone() {
    return enableShowPhone;
  }

  public static void setEnableShowPhone(boolean enable) {
    enableShowPhone = enable;
  }

  private static boolean enableShowMobile = true;

  public static boolean getEnableShowMobile() {
    return enableShowMobile;
  }

  public static void setEnableShowMobile(boolean enable) {
    enableShowMobile = enable;
  }

  private static boolean enableShowFax = true;

  public static boolean getEnableShowFax() {
    return enableShowFax;
  }

  public static void setEnableShowFax(boolean enable) {
    enableShowFax = enable;
  }

  private static boolean enableShowCareer = true;

  public static boolean getEnableShowCareer() {
    return enableShowCareer;
  }

  public static void setEnableShowCareer(boolean enable) {
    enableShowCareer = enable;
  }

  private static boolean enableShowHomepage = true;

  public static boolean getEnableShowHomepage() {
    return enableShowHomepage;
  }

  public static void setEnableShowHomepage(boolean enable) {
    enableShowHomepage = enable;
  }

  private static boolean enableShowJoinDate = true;

  public static boolean getEnableShowJoinDate() {
    return enableShowJoinDate;
  }

  public static void setEnableShowJoinDate(boolean enable) {
    enableShowJoinDate = enable;
  }

  private static boolean enableShowViewCount = true;

  public static boolean getEnableShowViewCount() {
    return enableShowViewCount;
  }

  public static void setEnableShowViewCount(boolean enable) {
    enableShowViewCount = enable;
  }

  private static boolean enableShowSignature = true;

  public static boolean getEnableShowSignature() {
    return enableShowSignature;
  }

  public static void setEnableShowSignature(boolean enable) {
    enableShowSignature = enable;
  }

  private static boolean enableShowPostsPerPage = true;

  public static boolean getEnableShowPostsPerPage() {
    return enableShowPostsPerPage;
  }

  public static void setEnableShowPostsPerPage(boolean enable) {
    enableShowPostsPerPage = enable;
  }

  private static boolean enableUsePopupMenuInViewThread = false;

  public static boolean getEnableUsePopupMenuInViewThread() {
    return enableUsePopupMenuInViewThread;
  }

  public static void setEnableUsePopupMenuInViewThread(boolean enable) {
    enableUsePopupMenuInViewThread = enable;
  }

  private static boolean enableRichTextEditor = false;

  public static boolean getEnableRichTextEditor() {
    return enableRichTextEditor;
  }

  public static void setEnableRichTextEditor(boolean enable) {
    enableRichTextEditor = enable;
  }

  private static int maxLastPostBodyInWatch = 0;

  public static int getMaxLastPostBodyInWatch() {
    return maxLastPostBodyInWatch;
  }

  public static void setMaxLastPostBodyInWatch(int maxCharacters) {
    maxLastPostBodyInWatch = maxCharacters;
  }

  private static boolean enableOnlineUsers = true;

  public static boolean getEnableOnlineUsers() {
    return enableOnlineUsers;
  }

  public static void setEnableOnlineUsers(boolean enable) {
    enableOnlineUsers = enable;
  }

  private static boolean enableDuplicateOnlineUsers = true;

  public static boolean getEnableDuplicateOnlineUsers() {
    return enableDuplicateOnlineUsers;
  }

  public static void setEnableDuplicateOnlineUsers(boolean enable) {
    enableDuplicateOnlineUsers = enable;
  }

  private static boolean enableInvisibleUsers = true;

  public static boolean getEnableInvisibleUsers() {
    return enableInvisibleUsers;
  }

  public static void setEnableInvisibleUsers(boolean enable) {
    enableInvisibleUsers = enable;
  }

  private static boolean enableListMembers = true;

  public static boolean getEnableListMembers() {
    return enableListMembers;
  }

  public static void setEnableListMembers(boolean enable) {
    enableListMembers = enable;
  }

  private static boolean enablePrivateMessage = true;

  public static boolean getEnablePrivateMessage() {
    return enablePrivateMessage;
  }

  public static void setEnablePrivateMessage(boolean enable) {
    enablePrivateMessage = enable;
  }

  private static boolean enablePublicMessage = true;

  public static boolean getEnablePublicMessage() {
    return enablePublicMessage;
  }

  public static void setEnablePublicMessage(boolean enable) {
    enablePublicMessage = enable;
  }

  private static boolean enableListUnansweredThreads = true;

  public static boolean getEnableListUnansweredThreads() {
    return enableListUnansweredThreads;
  }

  public static void setEnableListUnansweredThreads(boolean enable) {
    enableListUnansweredThreads = enable;
  }

  private static boolean enableThumbnail = false;

  public static boolean getEnableThumbnail() {
    return enableThumbnail;
  }

  public static void setEnableThumbnail(boolean enable) {
    enableThumbnail = enable;
  }

  private static int thumbnailWidth = 100;

  public static int getThumbnailWidth() {
    return thumbnailWidth;
  }

  public static void setThumbnailWidth(int width) {
    thumbnailWidth = width;
  }

  private static int thumbnailHeight = 100;

  public static int getThumbnailHeight() {
    return thumbnailHeight;
  }

  public static void setThumbnailHeight(int height) {
    thumbnailHeight = height;
  }

  private static boolean enableRWC = false;

  public static boolean getEnableRWC() {
    return enableRWC;
  }

  public static void setEnableRWC(boolean enable) {
    enableRWC = enable;
  }

  // should not be true, only for RWC
  private static boolean enableMaximizedEditArea = false;

  public static boolean getEnableMaximizedEditArea() {
    return enableMaximizedEditArea;
  }

  public static void setEnableMaximizedEditArea(boolean enable) {
    enableMaximizedEditArea = enable;
  }

  // only set alwaysActivation = true in some special case of integration
  private static boolean alwaysActivation = false;

  public static boolean getAlwaysActivation() {
    return alwaysActivation;
  }

  public static void setAlwaysActivation(boolean enable) {
    alwaysActivation = enable;
  }

  private static boolean enableShowPostCount = true;

  public static boolean getEnableShowPostCount() {
    return enableShowPostCount;
  }

  public static void setEnableShowPostCount(boolean enable) {
    enableShowPostCount = enable;
  }

  private static boolean enableShowOnlineStatus = true;

  public static boolean getEnableShowOnlineStatus() {
    return enableShowOnlineStatus;
  }

  public static void setEnableShowOnlineStatus(boolean enable) {
    enableShowOnlineStatus = enable;
  }

  private static boolean enableShowFirstName = true;

  public static boolean getEnableShowFirstName() {
    return enableShowFirstName;
  }

  public static void setEnableShowFirstName(boolean enable) {
    enableShowFirstName = enable;
  }

  private static boolean enableShowLastName = true;

  public static boolean getEnableShowLastName() {
    return enableShowLastName;
  }

  public static void setEnableShowLastName(boolean enable) {
    enableShowLastName = enable;
  }

  private static boolean enableExpanseCategoryTree = true;

  public static boolean getEnableExpanseCategoryTree() {
    return enableExpanseCategoryTree;
  }

  public static void setEnableExpanseCategoryTree(boolean enable) {
    enableExpanseCategoryTree = enable;
  }

  /**
   * This is the maximum number of favorite threads that a user can add
   */
  private static int maxFavoriteThreads = 128;

  public static int getMaxFavoriteThreads() {
    return maxFavoriteThreads;
  }

  public static void setMaxFavoriteThreads(int maxThread) {
    maxFavoriteThreads = maxThread;
  }

  private static int maxPrivateMessages = 128;

  public static int getMaxPrivateMessages() {
    return maxPrivateMessages;
  }

  public static void setMaxPrivateMessages(int maxMessage) {
    maxPrivateMessages = maxMessage;
  }

  private static int maxHotTopics = 10;

  public static int maxHotTopics() {
    return maxHotTopics;
  }

  public static void maxHotTopics(int maxTopics) {
    maxHotTopics = maxTopics;
  }

  private static int maxPostsPerHourPerIP = 20;

  public static int getMaxPostsPerHourPerIP() {
    return maxPostsPerHourPerIP;
  }

  public static void setMaxPostsPerHourPerIP(int maxPosts) {
    maxPostsPerHourPerIP = maxPosts;
  }

  private static int maxPostsPerHourPerMember = 10;

  public static int getMaxPostsPerHourPerMember() {
    return maxPostsPerHourPerMember;
  }

  public static void setMaxPostsPerHourPerMember(int maxPosts) {
    maxPostsPerHourPerMember = maxPosts;
  }

  private static int maxMembersPerHourPerIP = 2;

  public static int getMaxMembersPerHourPerIP() {
    return maxMembersPerHourPerIP;
  }

  public static void setMaxMembersPerHourPerIP(int maxMembers) {
    maxMembersPerHourPerIP = maxMembers;
  }

  private static int maxLoginsPerHourPerIP = 5;

  public static int getMaxLoginsPerHourPerIP() {
    return maxLoginsPerHourPerIP;
  }

  public static void setMaxLoginsPerHourPerIP(int maxLogins) {
    maxLoginsPerHourPerIP = maxLogins;
  }

  private static int maxHttpRequestsPerHourPerIP = 200;

  public static int getMaxHttpRequestsPerHourPerIP() {
    return maxHttpRequestsPerHourPerIP;
  }

  public static void setMaxHttpRequestsPerHourPerIP(int maxHttpRequests) {
    maxHttpRequestsPerHourPerIP = maxHttpRequests;
  }

  private static int maxHttpRequestsPerMinutePerIP = 20;

  public static int getMaxHttpRequestsPerMinutePerIP() {
    return maxHttpRequestsPerMinutePerIP;
  }

  public static void setMaxHttpRequestsPerMinutePerIP(int maxHttpRequests) {
    maxHttpRequestsPerMinutePerIP = maxHttpRequests;
  }

  private static int maxMessagesPerHourPerIP = 5;

  public static int getMaxMessagesPerHourPerIP() {
    return maxMessagesPerHourPerIP;
  }

  public static void setMaxMessagesPerHourPerIP(int maxMessages) {
    maxMessagesPerHourPerIP = maxMessages;
  }

  private static int maxPasswordDays = 0;

  public static int getMaxPasswordDays() {
    return maxPasswordDays;
  }

  private static int maxActiveThreads = 5;

  public static int getMaxActiveThreads() {
    return maxActiveThreads;
  }

  public static void setMaxActiveThreads(int maxThreads) {
    maxActiveThreads = maxThreads;
  }

  private static int maxActiveMembers = 5;

  public static int getMaxActiveMembers() {
    return maxActiveMembers;
  }

  public static void setMaxActiveMembers(int maxMembers) {
    maxActiveMembers = maxMembers;
  }

  /** Do we allow storing backup files on the server? Currently not used. */
  static boolean ENABLE_BACKUP_ON_SERVER = true;
  public static final String BACKUP_FILE_PREFIX = "mvnForum-";
  public static final String BACKUP_FILE_MainXmlFileNameInZip = "IMPORT.xml";
  public static final String BACKUP_FILE_AvatarsDirNameInZip = "AVATARS/"; // must end with '/'
  public static final String BACKUP_FILE_AttachsDirNameInZip = "ATTACHMENTS/"; // must end with '/'

  /**
   * Maximum size of the import file (in bytes) we will allow to be uploaded to server before
   * processing.
   */
  private static int maxImportSize = 4096000;

  public static int getMaxImportSize() {
    return maxImportSize;
  }

  public static void setMaxImportSize(int maxSize) {
    maxImportSize = maxSize;
  }

  /**
   * Type of import/export file: mvnForum XML. Import only database info, no attachments, message
   * folders, avatars, ...
   */
  public static final int IMPORTEXPORT_TYPE_MVN_XML = 0;

  /**
   * Type of import file: mvnForum ZIP. Also import attachments, avatars, message folders
   */
  public static final int IMPORTEXPORT_TYPE_MVN_ZIP = 1;

  /**
   * Type of import file: Jive XML. Using <code>http://www.jivesoftware.com/jive.dtd</code>,
   * xmlversion="1.0".
   */
  public static final int IMPORTEXPORT_TYPE_JIVE_XML = 2;

  /**
   * Output all messages, including errors, important messages and informational/status messages.
   * This constant is used for specifing the level of output in various processess throughout the
   * application.
   */
  public static final int MESSAGE_LEVEL_ALL_MESSAGES = 0;

  /**
   * Output only error messages and important messages (no informational/status messages). This
   * constant is used for specifing the level of output in various processess throughout the
   * application.
   */
  public static final int MESSAGE_LEVEL_IMPORTANT_MESSAGES = 1;

  /**
   * Output only error messages (no important messages, no informational/status messages). This
   * constant is used for specifing the level of output in various processess throughout the
   * application.
   */
  public static final int MESSAGE_LEVEL_ONLY_ERRORS = 2;

  public static final long SESSION_DURATION = 30 * DateUtil.MINUTE; // SHOULD NOT less than 15
                                                                    // minutes

  // public static final boolean DEFAULT_MESSAGE_ENABLE = true;
  // public static final boolean DEFAULT_MEMBER_ENABLE = true;

  /**
   * Configurable number of days that a user can edit a post
   */
  private static int maxEditDays = 7;

  public static int getMaxEditDays() {
    return maxEditDays;
  }

  public static void setMaxEditDays(int maxDays) {
    maxEditDays = maxDays;
  }

  /**
   * Configurable number of days that a user can attach file to a post
   */
  private static int maxAttachDays = 1;

  public static int getMaxAttachDays() {
    return maxAttachDays;
  }

  public static void setMaxAttachDays(int maxDays) {
    maxAttachDays = maxDays;
  }

  /**
   * Configurable number of days that a user can delete a post
   */
  private static int maxDeleteDays = 1;

  public static int getMaxDeleteDays() {
    return maxDeleteDays;
  }

  public static void setMaxDeleteDays(int maxDays) {
    maxDeleteDays = maxDays;
  }

  /**
   * Default number of rows (of Guest user) shown per page
   */
  private static int rowsPerPage = 10;

  public static int getRowsPerPage() {
    return rowsPerPage;
  }

  public static void setRowsPerPage(int rows) {
    rowsPerPage = rows;
  }

  /**
   * This is the number of rows returned when list threads for RSS
   */
  private static int rowsPerRSS = 15; // RSS 0.91

  public static int getRowsPerRSS() {
    return rowsPerRSS;
  }

  public static void setRowsPerRSS(int rows) {
    rowsPerRSS = rows;
  }

  /**
   * This is the default value of watch option
   *
   * @see com.mvnforum.db.WatchBean for the constant values
   */
  private static int defaultWatchOption = 0;

  public static int getDefaultWatchOption() {
    return defaultWatchOption;
  }

  public static void setDefaultWatchOption(int option) {
    defaultWatchOption = option;
  }

  /**
   * This is the default value of watch option
   *
   * @see com.mvnforum.db.WatchBean for the constant values
   */
  private static boolean haveInternet = true;

  public static boolean getHaveInternet() {
    return haveInternet;
  }

  public static void setHaveInternet(boolean internet) {
    haveInternet = internet;
  }

  private static int defaultStatusOfRegisteredMember = 0;

  public static int getDefaultStatusOfRegisteredMember() {
    return defaultStatusOfRegisteredMember;
  }

  public static void setDefaultStatusOfRegisteredMember(int status) {
    defaultStatusOfRegisteredMember = status;
  }

  private static boolean enableRegisterRule = true;

  public static boolean getEnableRegisterRule() {
    return enableRegisterRule;
  }

  public static void setEnableRegisterRule(boolean status) {
    enableRegisterRule = status;
  }

  private static int defaultWatchType = 1;

  public static int getDefaultWatchType() {
    return defaultWatchType;
  }

  public static void setDefaultWatchType(int type) {
    defaultWatchType = type;
  }

  private static boolean sendWatchMailAsHTML = true;

  public static boolean getSendWatchMailAsHTML() {
    return sendWatchMailAsHTML;
  }

  public static void setSendWatchMailAsHTML(boolean asHTML) {
    sendWatchMailAsHTML = asHTML;
  }

  private static boolean enableCheckInvalidSession = true;

  public static boolean getEnableCheckInvalidSession() {
    return enableCheckInvalidSession;
  }

  public static void setEnableCheckInvalidSession(boolean status) {
    enableCheckInvalidSession = status;
  }

  private static int defaultModerationOption = 0;

  public static int getDefaultModerationOption() {
    return defaultModerationOption;
  }

  public static void setDefaultModerationOption(int option) {
    defaultModerationOption = option;
  }

  private static int maxCharsInShortSummary = 100;

  public static int getMaxCharsInShortSummary() {
    // getMaxCharsInPostInIndex
    return maxCharsInShortSummary;
  }

  public static void setMaxCharsInShortSummary(int maxChars) {
    maxCharsInShortSummary = maxChars;
  }

  private static int maxCharsInLongSummary = 1000;

  public static int getMaxCharsInLongSummary() {
    // getMaxCharsInPostInListthreads()
    return maxCharsInLongSummary;
  }

  public static void setMaxCharsInLongSummary(int maxChars) {
    maxCharsInLongSummary = maxChars;
  }

  private static int maxCharsInRSS = 500;

  public static int getMaxCharsInRSS() {
    return maxCharsInRSS;
  }

  public static void setMaxCharsInRSS(int maxChars) {
    maxCharsInRSS = maxChars;
  }

  private static boolean enableEncryptPasswordOnBrowser = true;

  public static boolean getEnableEncryptPasswordOnBrowser() {
    return enableEncryptPasswordOnBrowser;
  }

  public static void setEnableEncryptPasswordOnBrowser(boolean enable) {
    enableEncryptPasswordOnBrowser = enable;
  }

  private static boolean enableExternalUserDatabase = false;

  public static boolean getEnableExternalUserDatabase() {
    return enableExternalUserDatabase;
  }

  public static void setEnableExternalUserDatabase(boolean enable) {
    enableExternalUserDatabase = enable;
  }

  private static boolean enableFriendlyURL = false;

  public static boolean getEnableFriendlyURL() {
    return enableFriendlyURL;
  }

  public static void setEnableFriendlyURL(boolean enable) {
    enableFriendlyURL = enable;
  }

  public static boolean requireRegisterAddress = false;

  public static boolean isRequireRegisterAddress() {
    return requireRegisterAddress;
  }

  public static void setRequireRegisterAddress(boolean requireRegisterAddress) {
    MVNForumConfig.requireRegisterAddress = requireRegisterAddress;
  }

  public static boolean requireRegisterAol = false;

  public static boolean isRequireRegisterAol() {
    return requireRegisterAol;
  }

  public static void setRequireRegisterAol(boolean requireRegisterAol) {
    MVNForumConfig.requireRegisterAol = requireRegisterAol;
  }

  public static boolean requireRegisterBirthday = false;

  public static boolean isRequireRegisterBirthday() {
    return requireRegisterBirthday;
  }

  public static void setRequireRegisterBirthday(boolean requireRegisterBirthday) {
    MVNForumConfig.requireRegisterBirthday = requireRegisterBirthday;
  }

  public static boolean requireRegisterCareer = false;

  public static boolean isRequireRegisterCareer() {
    return requireRegisterCareer;
  }

  public static void setRequireRegisterCareer(boolean requireRegisterCareer) {
    MVNForumConfig.requireRegisterCareer = requireRegisterCareer;
  }

  public static boolean requireRegisterCity = false;

  public static boolean isRequireRegisterCity() {
    return requireRegisterCity;
  }

  public static void setRequireRegisterCity(boolean requireRegisterCity) {
    MVNForumConfig.requireRegisterCity = requireRegisterCity;
  }

  public static boolean requireRegisterCountry = false;

  public static boolean isRequireRegisterCountry() {
    return requireRegisterCountry;
  }

  public static void setRequireRegisterCountry(boolean requireRegisterCountry) {
    MVNForumConfig.requireRegisterCountry = requireRegisterCountry;
  }

  public static boolean requireRegisterFirstname = false;

  public static boolean isRequireRegisterFirstname() {
    return requireRegisterFirstname;
  }

  public static void setRequireRegisterFirstname(boolean requireRegisterFirstname) {
    MVNForumConfig.requireRegisterFirstname = requireRegisterFirstname;
  }

  public static boolean requireRegisterGender = false;

  public static boolean isRequireRegisterGender() {
    return requireRegisterGender;
  }

  public static void setRequireRegisterGender(boolean requireRegisterGender) {
    MVNForumConfig.requireRegisterGender = requireRegisterGender;
  }

  public static boolean requireRegisterHomepage = false;

  public static boolean isRequireRegisterHomepage() {
    return requireRegisterHomepage;
  }

  public static void setRequireRegisterHomepage(boolean requireRegisterHomepage) {
    MVNForumConfig.requireRegisterHomepage = requireRegisterHomepage;
  }

  public static boolean requireRegisterIcq = false;

  public static boolean isRequireRegisterIcq() {
    return requireRegisterIcq;
  }

  public static void setRequireRegisterIcq(boolean requireRegisterIcq) {
    MVNForumConfig.requireRegisterIcq = requireRegisterIcq;
  }

  public static boolean requireRegisterLastname = false;

  public static boolean isRequireRegisterLastname() {
    return requireRegisterLastname;
  }

  public static void setRequireRegisterLastname(boolean requireRegisterLastname) {
    MVNForumConfig.requireRegisterLastname = requireRegisterLastname;
  }

  public static boolean requireRegisterLink1 = false;

  public static boolean isRequireRegisterLink1() {
    return requireRegisterLink1;
  }

  public static void setRequireRegisterLink_1(boolean requireRegisterLink_1) {
    MVNForumConfig.requireRegisterLink1 = requireRegisterLink_1;
  }

  public static boolean requireRegisterLink2 = false;

  public static boolean isRequireRegisterLink2() {
    return requireRegisterLink2;
  }

  public static void setRequireRegisterLink_2(boolean requireRegisterLink_2) {
    MVNForumConfig.requireRegisterLink2 = requireRegisterLink_2;
  }

  public static boolean requireRegisterMobile = false;

  public static boolean isRequireRegisterMobile() {
    return requireRegisterMobile;
  }

  public static void setRequireRegisterMobile(boolean requireRegisterMobile) {
    MVNForumConfig.requireRegisterMobile = requireRegisterMobile;
  }

  public static boolean requireRegisterFax = false;

  public static boolean isRequireRegisterFax() {
    return requireRegisterFax;
  }

  public static void setRequireRegisterFax(boolean requireRegisterFax) {
    MVNForumConfig.requireRegisterFax = requireRegisterFax;
  }

  public static boolean requireRegisterMsn = false;

  public static boolean isRequireRegisterMsn() {
    return requireRegisterMsn;
  }

  public static void setRequireRegisterMsn(boolean requireRegisterMsn) {
    MVNForumConfig.requireRegisterMsn = requireRegisterMsn;
  }

  public static boolean requireRegisterPhone = false;

  public static boolean isRequireRegisterPhone() {
    return requireRegisterPhone;
  }

  public static void setRequireRegisterPhone(boolean requireRegisterPhone) {
    MVNForumConfig.requireRegisterPhone = requireRegisterPhone;
  }

  public static boolean requireRegisterState = false;

  public static boolean isRequireRegisterState() {
    return requireRegisterState;
  }

  public static void setRequireRegisterState(boolean requireRegisterState) {
    MVNForumConfig.requireRegisterState = requireRegisterState;
  }

  public static boolean requireRegisterYahoo = false;

  public static boolean isRequireRegisterYahoo() {
    return requireRegisterYahoo;
  }

  public static void setRequireRegisterYahoo(boolean requireRegisterYahoo) {
    MVNForumConfig.requireRegisterYahoo = requireRegisterYahoo;
  }

  private static int captchaImageMinWordLength = 6;

  public static int getCaptchaImageMinWordLength() {
    return captchaImageMinWordLength;
  }

  public static void setCaptchaImageMinWordLength(int minLength) {
    captchaImageMinWordLength = minLength;
  }

  private static int captchaImageMaxWordLength = 8;

  public static int getCaptchaImageMaxWordLength() {
    return captchaImageMaxWordLength;
  }

  public static void setCaptchaImageMaxWordLength(int maxLength) {
    captchaImageMaxWordLength = maxLength;
  }

  /**
   * This is the number of reply rows returned when addpost (reply to a topic) /forum/addpost
   */
  public static final int ROWS_IN_LAST_REPLIES = 5;

  /*
   * private static boolean parseBooleanValue(String propertyValue, boolean defaultValue) { String
   * result = "true"; try { result = propertyValue.trim(); if ((result.equalsIgnoreCase("false")) ||
   * (result.equalsIgnoreCase("no"))) { return false; } else if ((result.equalsIgnoreCase("true"))
   * || (result.equalsIgnoreCase("yes"))) { return true; } else { log.
   * warn("Invalid boolean value in properties file. Should be \"true\", \"false\", \"yes\" or \"no\"."
   * ); return defaultValue; } } catch (Exception e) { log.warn(e.getMessage()); return
   * defaultValue; } }
   *
   * private static int parseIntValue(String propertyValue, int defaultValue) { try { return
   * Integer.parseInt(propertyValue.trim()); } catch (Exception e) { log.warn(e.getMessage());
   * return defaultValue; } }
   */

  static {
    try {
      load();

      // Load FreeMarker configuration
      freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_31);
      freeMarkerConfiguration.setClassForTemplateLoading(MVNForumConfig.class, "/template");
    } catch (Exception e) {
      String message = "com.mvnforum.MVNForumConfig: Can't read the configuration file: '"
          + OPTION_FILE_NAME + "'. Make sure the file is in your CLASSPATH";
      log.error(message, e);
      MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().setShouldStop(message);
    }
  }

  public static void load() {
    reload();
  }

  public static void reload() {

    String configFilename = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;

    try {
      DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));

      MVNForumHome = conf.getString("mvnforumconfig.mvnforum_home", "");
      setMVNForumHome(MVNForumHome);

      // enableBrandName = conf.getBoolean("mvnforumconfig.enable_brand_name", true);

      webMasterEmail = conf.getString("mvnforumconfig.webmaster_email", "");
      watchEmail = conf.getString("mvnforumconfig.watch_email", "");

      logoURL = conf.getString("mvnforumconfig.logo_url", logoURL);

      logFile = conf.getString("mvnforumconfig.mvnforum_log", "");

      String supportedImageLocalesConfig =
          conf.getString("mvnforumconfig.supported_locales_for_images", "");
      supportedImageLocaleNames = StringUtil.getStringArray(supportedImageLocalesConfig, ";");

      String supportedLocalesConfig = conf.getString("mvnforumconfig.supported_locales", "");
      supportedLocaleNames = StringUtil.getStringArray(supportedLocalesConfig, ";");
      supportedLocales = new Locale[supportedLocaleNames.length];

      for (int i = 0; i < supportedLocaleNames.length; i++) {
        String localeName = supportedLocaleNames[i];
        supportedLocales[i] = I18nUtil.getLocale(localeName);
      }

      try {
        defaultLocaleName = conf.getString("mvnforumconfig.default_locale_name", "");
        if (defaultLocaleName.length() == 0) {
          defaultLocaleName = "en";
        }
      } catch (Exception ex) {
        log.warn(ex.getMessage());
      }
      defaultLocale = I18nUtil.getLocale(defaultLocaleName);
      eventLogLocale = I18nUtil.getLocale(conf.getString("mvnforumconfig.event_log_locale"));

      try {
        defaultGuestName = conf.getString("mvnforumconfig.default_guest_name", defaultGuestName);
      } catch (Exception ex) {
        log.warn(ex.getMessage());
      }

      defaultGuestTimeZone =
          conf.getDouble("mvnforumconfig.default_guest_timezone", defaultGuestTimeZone);
      if (Math.abs(defaultGuestTimeZone) > 12) {
        defaultGuestTimeZone = 0;
      }

      redirectLoginURL = conf.getString("mvnforumconfig.redirect_login_url", redirectLoginURL);
      redirectLogoutURL = conf.getString("mvnforumconfig.redirect_logout_url", redirectLogoutURL);
      localeParameterName =
          conf.getString("mvnforumconfig.locale_parameter_name", localeParameterName);

      enablePasswordlessAuth = conf.getBoolean("mvnforumconfig.enable_passwordless_auth", false);
      requireActivation = conf.getBoolean("mvnforumconfig.require_activation", false);
      enableLoginInfoInCookie = conf.getBoolean("mvnforumconfig.enable_login_info_in_cookie", true);
      enableLoginInfoInSession =
          conf.getBoolean("mvnforumconfig.enable_login_info_in_session", true);
      enableLoginInfoInRealm = conf.getBoolean("mvnforumconfig.enable_login_info_in_realm", false);
      enableLoginInfoInCustomization =
          conf.getBoolean("mvnforumconfig.enable_login_info_in_customization", false);

      enableCheckInvalidSession =
          conf.getBoolean("mvnforumconfig.enable_check_invalid_session", true);

      enableCacheMember = conf.getBoolean("mvnforumconfig.enable_cache_member", true);
      enableCachePost = conf.getBoolean("mvnforumconfig.enable_cache_post", true);
      enableCacheThread = conf.getBoolean("mvnforumconfig.enable_cache_thread", true);
      enableCacheForum = conf.getBoolean("mvnforumconfig.enable_cache_forum", true);
      enableCacheCategory = conf.getBoolean("mvnforumconfig.enable_cache_category", true);

      enableLogin = conf.getBoolean("mvnforumconfig.enable_login", true);
      enableRegisterRule = conf.getBoolean("mvnforumconfig.enable_register_rule", true);
      enableNewMember = conf.getBoolean("mvnforumconfig.enable_new_member", true);
      enableNewPost = conf.getBoolean("mvnforumconfig.enable_new_post", true);
      enableSplitThread = conf.getBoolean("mvnforumconfig.enable_split_thread", true);
      enableAvatar = conf.getBoolean("mvnforumconfig.enable_avatar", true);
      enableEmotion = conf.getBoolean("mvnforumconfig.enable_emoticon", true);
      enableRSS = conf.getBoolean("mvnforumconfig.enable_rss", true);

      enableSearch = conf.getBoolean("mvnforumconfig.enable_search", true);
      defaultSearchOrderBy =
          conf.getInt("mvnforumconfig.default_search_order_by", defaultSearchOrderBy);

      enableWatch = conf.getBoolean("mvnforumconfig.enable_watch", true);
      enableAttachment = conf.getBoolean("mvnforumconfig.enable_attachment", true);
      enableMessageAttachment = conf.getBoolean("mvnforumconfig.enable_message_attachment", true);
      enableCaptcha = conf.getBoolean("mvnforumconfig.enable_captcha", false);
      enablePortalLikeIndexPage =
          conf.getBoolean("mvnforumconfig.enable_portal_like_index_page", true);
      enableAdminCanChangePassword =
          conf.getBoolean("mvnforumconfig.enable_admin_can_change_password", true);
      enableShowLastLoginOfCurrentMember =
          conf.getBoolean("mvnforumconfig.enable_show_last_login_of_current_member", true);
      enableExpanseCategoryTree = conf.getBoolean(
          "mvnforumconfig.enable_expanse_category_tree_by_default", enableExpanseCategoryTree);

      enableShowEmail = conf.getBoolean("mvnforumconfig.enable_show_email", true);
      enableShowNameVisible = conf.getBoolean("mvnforumconfig.enable_show_name_visible", true);
      enableShowEmailVisible = conf.getBoolean("mvnforumconfig.enable_show_email_visible", true);
      enableShowBirthday = conf.getBoolean("mvnforumconfig.enable_show_birthday", true);
      enableShowGender = conf.getBoolean("mvnforumconfig.enable_show_gender", true);
      enableShowAddress = conf.getBoolean("mvnforumconfig.enable_show_address", true);
      enableShowCareer = conf.getBoolean("mvnforumconfig.enable_show_career", true);
      enableShowCity = conf.getBoolean("mvnforumconfig.enable_show_city", true);
      enableShowCountry = conf.getBoolean("mvnforumconfig.enable_show_country", true);
      enableShowHomepage = conf.getBoolean("mvnforumconfig.enable_show_homepage", true);
      enableShowJoinDate = conf.getBoolean("mvnforumconfig.enable_show_join_date", true);
      enableShowMobile = conf.getBoolean("mvnforumconfig.enable_show_mobile", true);
      enableShowFax = conf.getBoolean("mvnforumconfig.enable_show_fax", true);
      enableShowPhone = conf.getBoolean("mvnforumconfig.enable_show_phone", true);
      enableShowState = conf.getBoolean("mvnforumconfig.enable_show_state", true);
      enableShowPostCount = conf.getBoolean("mvnforumconfig.enable_show_post_count", true);
      enableShowOnlineStatus = conf.getBoolean("mvnforumconfig.enable_show_online_status", true);
      enableShowFirstName = conf.getBoolean("mvnforumconfig.enable_show_firstname", true);
      enableShowLastName = conf.getBoolean("mvnforumconfig.enable_show_lastname", true);
      enableShowPostCount = conf.getBoolean("mvnforumconfig.enable_show_post_count", true);
      enableShowViewCount = conf.getBoolean("mvnforumconfig.enable_show_view_count", true);
      enableShowSignature = conf.getBoolean("mvnforumconfig.enable_show_signature", true);
      enableShowPostsPerPage = conf.getBoolean("mvnforumconfig.enable_show_posts_per_page", true);
      enableShowLastLogin = conf.getBoolean("mvnforumconfig.enable_show_last_login", true);
      enableShowModifiedDate = conf.getBoolean("mvnforumconfig.enable_show_modified_date", true);

      requireRegisterFirstname = conf.getBoolean("mvnforumconfig.require_register_firstname", true);
      requireRegisterLastname = conf.getBoolean("mvnforumconfig.require_register_lastname", true);
      requireRegisterGender = conf.getBoolean("mvnforumconfig.require_register_gender", true);
      requireRegisterBirthday = conf.getBoolean("mvnforumconfig.require_register_birthday", true);
      requireRegisterAddress = conf.getBoolean("mvnforumconfig.require_register_address", true);
      requireRegisterCity = conf.getBoolean("mvnforumconfig.require_register_city", true);
      requireRegisterState = conf.getBoolean("mvnforumconfig.require_register_state", true);
      requireRegisterCountry = conf.getBoolean("mvnforumconfig.require_register_country", true);
      requireRegisterPhone = conf.getBoolean("mvnforumconfig.require_register_phone", true);
      requireRegisterMobile = conf.getBoolean("mvnforumconfig.require_register_mobile", true);
      requireRegisterFax = conf.getBoolean("mvnforumconfig.require_register_fax", true);
      requireRegisterCareer = conf.getBoolean("mvnforumconfig.require_register_career", true);
      requireRegisterHomepage = conf.getBoolean("mvnforumconfig.require_register_homepage", true);
      requireRegisterYahoo = conf.getBoolean("mvnforumconfig.require_register_yahoo", true);
      requireRegisterAol = conf.getBoolean("mvnforumconfig.require_register_aol", true);
      requireRegisterIcq = conf.getBoolean("mvnforumconfig.require_register_icq", true);
      requireRegisterMsn = conf.getBoolean("mvnforumconfig.require_register_msn", true);
      requireRegisterLink1 = conf.getBoolean("mvnforumconfig.require_register_link_1", true);
      requireRegisterLink2 = conf.getBoolean("mvnforumconfig.require_register_link_2", true);

      enableAutoWatching = conf.getBoolean("mvnforumconfig.enable_auto_watching", false);
      enableSendWatchMailOfMyOwnPost =
          conf.getBoolean("mvnforumconfig.enable_send_watch_mail_of_my_own_post", false);
      enableEasyWatching = conf.getBoolean("mvnforumconfig.enable_easy_watching", true);
      enableUsePopupMenuInViewThread =
          conf.getBoolean("mvnforumconfig.enable_use_popup_menu_in_viewthread", true);
      enableRichTextEditor = conf.getBoolean("mvnforumconfig.enable_rich_text_editor", true);

      maxLastPostBodyInWatch = conf.getInt("mvnforumconfig.max_last_post_body_in_watch", 0);
      if (maxLastPostBodyInWatch < 0) {
        maxLastPostBodyInWatch = 0;
      }

      enableOnlineUsers = conf.getBoolean("mvnforumconfig.enable_online_users", true);
      enableListMembers = conf.getBoolean("mvnforumconfig.enable_listmembers", true);
      enableDuplicateOnlineUsers =
          conf.getBoolean("mvnforumconfig.enable_duplicate_onlineusers", true);
      enableInvisibleUsers = conf.getBoolean("mvnforumconfig.enable_invisible_users", true);
      enablePrivateMessage = conf.getBoolean("mvnforumconfig.enable_private_message", true);
      enablePublicMessage = conf.getBoolean("mvnforumconfig.enable_public_message", false);
      enableGuestViewImageAttachment =
          conf.getBoolean("mvnforumconfig.enable_guest_view_image_attachment", false);
      enableGuestViewListUsers =
          conf.getBoolean("mvnforumconfig.enable_guest_view_listusers", true);

      enableMostActiveMembers = conf.getBoolean("mvnforumconfig.enable_most_active_members", true);
      enableMostActiveThreads = conf.getBoolean("mvnforumconfig.enable_most_active_threads", true);
      onlyNormalThreadTypeInActiveThreads =
          conf.getBoolean("mvnforumconfig.only_normal_thread_type_in_active_threads", false);

      enableSiteStatisticsOverview =
          conf.getBoolean("mvnforumconfig.enable_site_statistics_overview", false);

      enableListNewMembersInRecentDays =
          conf.getBoolean("mvnforumconfig.enable_list_new_members_in_recent_days", false);
      enableListUsersBrowsingForum =
          conf.getBoolean("mvnforumconfig.enable_list_users_browsing_forum", false);
      enableListUsersBrowsingThread =
          conf.getBoolean("mvnforumconfig.enable_list_users_browsing_thread", false);

      daysToShowRecentMembers = conf.getInt("mvnforumconfig.days_to_show_recent_members", 1);
      if (daysToShowRecentMembers < 1) {
        daysToShowRecentMembers = 1;
      }

      enableEmailThreateningContent =
          conf.getBoolean("mvnforumconfig.enable_email_threatening_content", true);
      enableEmailToAdminContentWithCensoredWords =
          conf.getBoolean("mvnforumconfig.enable_email_to_admin_content_with_censored_words", true);
      defaultCategoryID = conf.getInt("mvnforumconfig.default_category_id");

      enableThumbnail = conf.getBoolean("mvnforumconfig.image_thumbnail.enable", false);
      thumbnailWidth = conf.getInt("mvnforumconfig.image_thumbnail.width", 100);
      if (thumbnailWidth < 0) {
        thumbnailWidth = 0;
      }
      thumbnailHeight = conf.getInt("mvnforumconfig.image_thumbnail.height", 100);
      if (thumbnailHeight < 0) {
        thumbnailHeight = 0;
      }

      enableListUnansweredThreads =
          conf.getBoolean("mvnforumconfig.enable_listunansweredthreads", true);

      // @todo: before release mvnForum, comment the below code
      // ENABLE_ENCRYPTED_PASSWORD = conf.getBoolean("mvnforumconfig.enable_encrypted_password",
      // true);
      enableEncryptPasswordOnBrowser =
          conf.getBoolean("mvnforumconfig.enable_encrypt_password_on_browser", true);
      enableExternalUserDatabase =
          conf.getBoolean("mvnforumconfig.enable_external_user_database", false);
      sendWatchMailAsHTML = conf.getBoolean("mvnforumconfig.send_watchmail_as_html", false);

      enableFriendlyURL = conf.getBoolean("mvnforumconfig.enable_friendly_url", false);

      boolean isPortlet =
          MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet();

      if (isPortlet) {
        enableExternalUserDatabase = true;
        enableFriendlyURL = false;
      }

      if (enableExternalUserDatabase) {
        enablePasswordlessAuth = false;
        enableNewMember = false;
        enableAdminCanChangePassword = false;
        enableListMembers = false;

        enableAdminCanChangePassword = false;
        enableListMembers = false;
        enableShowLastLogin = false;
        enableNewMember = false;
        requireActivation = false;
        // enableCaptcha = false;

        // we check this feature later, should enable or not ?
        // enableSiteStatisticsOverview = false;
      }

      if (isPortlet) {
        // disable any authentication & user involving features
        enablePasswordlessAuth = false;
        enableLoginInfoInCookie = false;
        enableLoginInfoInSession = false;
        enableLogin = false;

        // enable these feature to check for satisfying to create/update forum-account
        if (enableLoginInfoInCustomization == false) {
          enableLoginInfoInRealm = true;
        }
        enableLoginInfoInCustomization = true;

        // String memberImpl = Portal.getMemberImplementation(PORTAL_TYPE);

        // We don't care these features because we prefer to control
        // members accessing my forum than control all
        // of members even the members never click on forum link

        // ENABLE_ONLINE_USERS = false;
        // ENABLE_DUPLICATE_ONLINE_USERS = false;
        // ENABLE_INVISIBLE_USERS = false;
      }

      maxAttachmentSize =
          StringUtil.parseIntSizeValue(conf.getString("mvnforumconfig.max_attachment_size"), 1024);
      if (maxAttachmentSize < -1) {
        maxAttachmentSize = 0; // -1 is a valid value in common-upload, mean no maximum file size
      }

      maxMessageAttachmentSize = StringUtil
          .parseIntSizeValue(conf.getString("mvnforumconfig.max_message_attachment_size"), 1024);
      if (maxMessageAttachmentSize < -1) {
        maxMessageAttachmentSize = 0; // -1 is a valid value in common-upload, mean no maximum file
                                      // size
      }

      maxFavoriteThreads = conf.getInt("mvnforumconfig.max_favorite_thread", 128);
      if (maxFavoriteThreads < 0) {
        maxFavoriteThreads = 0;
      }

      maxPrivateMessages = conf.getInt("mvnforumconfig.max_private_message", 128);
      if (maxPrivateMessages < 0) {
        maxPrivateMessages = 0;
      }

      maxEditDays = conf.getInt("mvnforumconfig.max_edit_days", 7);
      if (maxEditDays < 0) {
        maxEditDays = 0;
      }

      maxAttachDays = conf.getInt("mvnforumconfig.max_attach_days", 1);
      if (maxAttachDays < 0) {
        maxAttachDays = 0;
      }

      maxDeleteDays = conf.getInt("mvnforumconfig.max_delete_days", 1);
      if (maxDeleteDays < 0) {
        maxDeleteDays = 0;
      }

      rowsPerPage = conf.getInt("mvnforumconfig.rows_per_page", 10);
      if (rowsPerPage < 5) {
        rowsPerPage = 5;
      }

      rowsPerRSS = conf.getInt("mvnforumconfig.rows_per_rss", 15);
      if (rowsPerRSS < 5) {
        rowsPerRSS = 5;
      }

      maxHotTopics = conf.getInt("mvnforumconfig.hot_topic_threshold", 10);
      if (maxHotTopics < 5) {
        maxHotTopics = 5;
      }

      maxHttpRequestsPerHourPerIP = conf.getInt("mvnforumconfig.max_http_requests_per_hour_per_ip",
          maxHttpRequestsPerHourPerIP);
      if (maxHttpRequestsPerHourPerIP < 0) {
        maxHttpRequestsPerHourPerIP = 0;
      }

      maxHttpRequestsPerMinutePerIP = conf.getInt(
          "mvnforumconfig.max_http_requests_per_minute_per_ip", maxHttpRequestsPerMinutePerIP);
      if (maxHttpRequestsPerMinutePerIP < 0) {
        maxHttpRequestsPerMinutePerIP = 0;
      }

      maxPostsPerHourPerIP =
          conf.getInt("mvnforumconfig.max_posts_per_hour_per_ip", maxPostsPerHourPerIP);
      if (maxPostsPerHourPerIP < 0) {
        maxPostsPerHourPerIP = 0;
      }

      maxPostsPerHourPerMember =
          conf.getInt("mvnforumconfig.max_posts_per_hour_per_member", maxPostsPerHourPerMember);
      if (maxPostsPerHourPerMember < 0) {
        maxPostsPerHourPerMember = 0;
      }

      maxMembersPerHourPerIP =
          conf.getInt("mvnforumconfig.max_members_per_hour_per_ip", maxMembersPerHourPerIP);
      if (maxMembersPerHourPerIP < 0) {
        maxMembersPerHourPerIP = 0;
      }

      maxLoginsPerHourPerIP =
          conf.getInt("mvnforumconfig.max_logins_per_hour_per_ip", maxLoginsPerHourPerIP);
      if (maxLoginsPerHourPerIP < 0) {
        maxLoginsPerHourPerIP = 0;
      }

      maxMessagesPerHourPerIP =
          conf.getInt("mvnforumconfig.max_messages_per_hour_per_ip", maxMessagesPerHourPerIP);
      if (maxMessagesPerHourPerIP < 0) {
        maxMessagesPerHourPerIP = 0;
      }

      maxPasswordDays = conf.getInt("mvnforumconfig.max_password_days", 1);
      if (maxPasswordDays < 0) {
        maxPasswordDays = 1;
      }

      maxCharsInShortSummary =
          conf.getInt("mvnforumconfig.max_chars_in_short_summary", maxCharsInShortSummary);
      if (maxCharsInShortSummary <= 0) {
        maxCharsInShortSummary = Integer.MAX_VALUE;
      }

      maxCharsInLongSummary =
          conf.getInt("mvnforumconfig.max_chars_in_long_summary", maxCharsInLongSummary);
      if (maxCharsInLongSummary <= 0) {
        maxCharsInLongSummary = Integer.MAX_VALUE;
      }

      maxCharsInRSS = conf.getInt("mvnforumconfig.max_chars_in_rss", maxCharsInRSS);
      if (maxCharsInRSS <= 0) {
        maxCharsInRSS = Integer.MAX_VALUE;
      }

      ENABLE_BACKUP_ON_SERVER = conf.getBoolean("mvnforumconfig.enable_backup_on_server", true);
      maxImportSize = StringUtil
          .parseIntSizeValue(conf.getString("mvnforumconfig.max_import_size", "4096000"), 4096000);

      defaultWatchOption =
          conf.getInt("mvnforumconfig.default_watch_option", WatchBean.WATCH_OPTION_DEFAULT);
      if (defaultWatchOption < WatchBean.WATCH_OPTION_DEFAULT
          || defaultWatchOption > WatchBean.WATCH_OPTION_WEEKLY) {
        defaultWatchOption = WatchBean.WATCH_OPTION_DEFAULT;
      }

      defaultModerationOption = conf.getInt("mvnforumconfig.default_moderation_option",
          ForumBean.FORUM_MODERATION_MODE_SYSTEM_DEFAULT);
      if (defaultModerationOption < ForumBean.FORUM_MODERATION_MODE_SYSTEM_DEFAULT
          || defaultModerationOption > ForumBean.FORUM_MODERATION_MODE_POST_ONLY) {
        defaultModerationOption = ForumBean.FORUM_MODERATION_MODE_SYSTEM_DEFAULT;
      }

      defaultWatchType =
          conf.getInt("mvnforumconfig.default_watch_type", WatchBean.WATCH_TYPE_DEFAULT);
      if (defaultWatchType < WatchBean.WATCH_TYPE_DIGEST
          || defaultWatchType > WatchBean.WATCH_TYPE_NONDIGEST) {
        defaultWatchType = WatchBean.WATCH_TYPE_DIGEST;
      }

      haveInternet = conf.getBoolean("mvnforumconfig.have_internet", false);

      defaultStatusOfRegisteredMember = conf.getInt(
          "mvnforumconfig.default_status_of_registered_member", defaultStatusOfRegisteredMember);
      if (defaultStatusOfRegisteredMember < MemberBean.MEMBER_STATUS_ENABLE
          || defaultStatusOfRegisteredMember > MemberBean.MEMBER_STATUS_PENDING) {
        defaultStatusOfRegisteredMember = MemberBean.MEMBER_STATUS_ENABLE;
      }

      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_NEW_MEMBER_PER_IP, maxMembersPerHourPerIP);
      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_IP, maxPostsPerHourPerIP);
      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_NEW_POST_PER_MEMBER,
          maxPostsPerHourPerMember);
      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_LOGIN_PER_IP, maxLoginsPerHourPerIP);
      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_NEW_MESSAGE_PER_IP,
          maxMessagesPerHourPerIP);
      FloodControlHour.setOption(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP,
          maxHttpRequestsPerHourPerIP);
      FloodControlMinute.setOption(MVNForumGlobal.FLOOD_ID_HTTP_REQUEST_PER_IP,
          maxHttpRequestsPerMinutePerIP);

      enableRWC = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().customizeFor()
          .equals("RWC");

      googleRecaptchaSiteKey = conf.getString("mvnforumconfig.googleRecaptchaSiteKey", "");
      googleRecaptchaSecretKey = conf.getString("mvnforumconfig.googleRecaptchaSecretKey", "");
    } catch (Exception e) {
      // Please note that for security reason, the full path file name is logged
      // to the log file only. And the reason that show on the web should only
      // show the filename only
      String message = "com.mvnforum.MVNForumConfig: Can't read the configuration file: '"
          + configFilename + "'. Make sure the file is in your CLASSPATH";
      log.error(message, e);
      MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService()
          .setShouldStop("com.mvnforum.MVNForumConfig: Can't read the configuration file: '"
              + OPTION_FILE_NAME + "'. Make sure the file is in your CLASSPATH");
    }
  }

  public static boolean supportLocale(String localeName) {

    if ((localeName == null) || (localeName.length() == 0)) {
      return false;
    }

    String[] supportedLocaleNames = MVNForumConfig.getSupportedLocaleNames();
    if (supportedLocaleNames == null) {
      log.error(
          "Assertion in MVNForumConfig: supportedLocales is null. Please check your configuration.");
      return false;
    }

    for (int i = 0; i < supportedLocaleNames.length; i++) {
      if (localeName.equals(supportedLocaleNames[i])) {
        return true;
      }
    }
    return false;
  }

  /**************************************************************************
   * Keys that could not configure via xml, need to configure via EnvironmentService
   **************************************************************************/

  // Allow show link deletecookieprocess in page MyProfile or not
  private static boolean enableDeleteCookie = true;

  public static boolean getEnableDeleteCookie() {
    return enableDeleteCookie;
  }

  public static void setEnableDeleteCookie(boolean enable) {
    enableDeleteCookie = enable;
  }

  // This value is used in MemberUtil.createRemoteUserAccount()
  private static boolean enableShowEmailVisibleAsDefaultWhenCreateRemoteUser = true;

  public static boolean getEnableShowEmailVisibleAsDefaultWhenCreateRemoteUser() {
    return enableShowEmailVisibleAsDefaultWhenCreateRemoteUser;
  }

  public static void setEnableShowEmailVisibleAsDefaultWhenCreateRemoteUser(boolean enable) {
    enableShowEmailVisibleAsDefaultWhenCreateRemoteUser = enable;
  }
}
