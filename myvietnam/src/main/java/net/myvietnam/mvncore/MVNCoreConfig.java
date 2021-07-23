/*
 * $Header: /cvsroot/mvnforum/myvietnam/src/net/myvietnam/mvncore/MVNCoreConfig.java,v 1.50 2009/09/24 06:58:05 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.50 $
 * $Date: 2009/09/24 06:58:05 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding MyVietnam and MyVietnam CoreLib
 * MUST remain intact in the scripts and source code.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Correspondence and Marketing Questions can be sent to:
 * info at MyVietnam net
 *
 * @author: Phong Ta Quoc  
 */
package net.myvietnam.mvncore;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MVNCoreConfig {

    private static final Logger log = LoggerFactory.getLogger(MVNCoreConfig.class);
    
    private static final String OPTION_FILE_NAME = "mvncore.xml";
    
    private MVNCoreConfig() {
        // private constructor make this method as a static class
    }

    /* <Database Options> */
    private static boolean useDataSource = false;
    public static boolean isUseDataSource() {
        return useDataSource;
    }
    public static void setUseDataSource(boolean useDataSource) {
        MVNCoreConfig.useDataSource = useDataSource;
    }

    private static String dataSourceName = "";
    public static String getDataSourceName() {
        return dataSourceName;
    }
    public static void setDataSourceName(String dataSourceName) {
        MVNCoreConfig.dataSourceName = dataSourceName;
    }

    // MUST NOT refer to DBUtils, or we will get an infinite recurse ???
    private static int databaseType = 0;//DATABASE_UNKNOWN
    public static int getDatabaseType() {
        return databaseType;
    }
    public static void setDatabaseType(int databaseType) {
        MVNCoreConfig.databaseType = databaseType;
    }

    private static String driverClassName = "com.mysql.jdbc.Driver";
    public static String getDriverClassName() {
        return driverClassName;
    }
    public static void setDriverClassName(String driverClassName) {
        MVNCoreConfig.driverClassName = driverClassName;
    }

    private static String databaseURL = "jdbc:mysql://localhost/mvnforum";
    public static String getDatabaseURL() {
        return databaseURL;
    }
    public static void setDatabaseURL(String databaseURL) {
        MVNCoreConfig.databaseURL = databaseURL;
    }

    private static String databaseUser = "root";
    public static String getDatabaseUser() {
        return databaseUser;
    }
    public static void setDatabaseUser(String databaseUser) {
        MVNCoreConfig.databaseUser = databaseUser;
    }

    private static String databasePassword = "";
    public static String getDatabasePassword() {
        return databasePassword;
    }
    public static void setDatabasePassword(String databasePassword) {
        MVNCoreConfig.databasePassword = databasePassword;
    }

    private static int maxConnection = 20;
    public static int getMaxConnection() {
        return maxConnection;
    }
    public static void setMaxConnection(int maxConnection) {
        MVNCoreConfig.maxConnection = maxConnection;
    }

    /* by default, the unit of time is millisecond */
    private static int maxTimeToWait = 2000;// 2 seconds
    public static int getMaxTimeToWait() {
        return maxTimeToWait;
    }
    public static void setMaxTimeToWait(int maxTimeToWait) {
        MVNCoreConfig.maxTimeToWait = maxTimeToWait;
    }

    private static int minutesBetweenRefresh = 30;// 30 minutes
    public static int getMinutesBetweenRefresh() {
        return minutesBetweenRefresh;
    }
    public static void setMinutesBetweenRefresh(int minutesBetweenRefresh) {
        MVNCoreConfig.minutesBetweenRefresh = minutesBetweenRefresh;
    }
    /* </Database Options> */

    /* <Mail_Options> */
    private static MailConfig receiveMail = new MailConfig("receive_mail");
    public static MailConfig getReceiveMailConfig() {
        return receiveMail;
    }
    public static void setReceiveMail(MailConfig receiveMail) {
        MVNCoreConfig.receiveMail = receiveMail;
    }

    private static MailConfig sendMail = new MailConfig("send_mail");
    public static MailConfig getSendMailConfig() {
        return sendMail;
    }
    public static void setSendMail(MailConfig sendMail) {
        MVNCoreConfig.sendMail = sendMail;
    }

    private static String defaultMailFrom = "youruser@yourdomain";
    public static String getDefaultMailFrom() {
        return defaultMailFrom;
    }
    public static void setDefaultMailFrom(String defaultMailFrom) {
        MVNCoreConfig.defaultMailFrom = defaultMailFrom;
    }
    /* </Mail_Options> */

    /* <Param Options> */
    private static String contextPath = "";// allow ROOT context
    public static String getContextPath() {
        return contextPath;
    }
    public static void setContextPath(String contextPath) {
        MVNCoreConfig.contextPath = contextPath;
    }

    private static String serverPath = "http://localhost:8080";
    public static String getServerPath() {
        return serverPath;
    }
    public static void setServerPath(String serverPath) {
        MVNCoreConfig.serverPath = serverPath;
    }
    /* <Param Options> */

    /* <Date Options> */
    /* the unit of the server offset is hour*/
    private static int serverHourOffset = 0; /* GMT timezone */
    public static int getServerHourOffset() {
        return serverHourOffset;
    }
    public static void setServerHourOffset(int serverHourOffset) {
        MVNCoreConfig.serverHourOffset = serverHourOffset;
    }
    /* </Date Options> */

    /* <User Agent Options>*/
    private static String blockedUserAgents = "";
    public static String getBlockedUserAgents() {
        return blockedUserAgents;
    }
    public static void setBlockedUserAgents(String blockedUserAgents) {
        MVNCoreConfig.blockedUserAgents = blockedUserAgents;
    }
    /* </User Agent Options>*/

    /* <Interceptor> */
    private static String mailInterceptorClassName = "";
    public static String getMailInterceptorClassName() {
        return mailInterceptorClassName;
    }
    public static void setMailInterceptorClassName(String mailInterceptorClassName) {
        MVNCoreConfig.mailInterceptorClassName = mailInterceptorClassName;
    }

    private static String contentInterceptorClassName = "";
    public static String getContentInterceptorClassName() {
        return contentInterceptorClassName;
    }
    public static void setContentInterceptorClassName(String contentInterceptorClassName) {
        MVNCoreConfig.contentInterceptorClassName = contentInterceptorClassName;
    }

    private static String loginInterceptorClassName = "";
    public static String getLoginIdInterceptorClassName() {
        return loginInterceptorClassName;
    }
    public static void setLoginInterceptorClassName(String loginInterceptorClassName) {
        MVNCoreConfig.loginInterceptorClassName = loginInterceptorClassName;
    }

    private static String passwordInterceptorClassName = "";
    public static String getPasswordInterceptorClassName() {
        return passwordInterceptorClassName;
    }
    public static void setPasswordInterceptorClassName(String passwordInterceptorClassName) {
        MVNCoreConfig.passwordInterceptorClassName = passwordInterceptorClassName;
    }
    /* </Interceptor> */

    private static String timerManagerDataSourceName = "";
    public static String getTimerManagerDataSourceName() {
        return timerManagerDataSourceName;
    }
    public static void setTimerManagerDataSourceName(String timerManagerDataSourceName) {
        MVNCoreConfig.timerManagerDataSourceName = timerManagerDataSourceName;
    }

    private static boolean enableLinkNofollow = false;
    public static boolean getEnableLinkNofollow() {
        return enableLinkNofollow;
    }
    public static void setEnableLinkNofollow(boolean enableLinkNofollow) {
        MVNCoreConfig.enableLinkNofollow = enableLinkNofollow;
    }

    private static boolean enableEncodeURL = true;
    public static boolean getEnableEncodeURL() {
        return enableEncodeURL;
    }
    public static void setEnableEncodeURL(boolean enableEncodeURL) {
        MVNCoreConfig.enableEncodeURL = enableEncodeURL;
    }

    private static String portalType = "non-portal";//Portal.DEFAULT;
    public static String getPortalType() {
        return portalType;
    }
    public static void setPortalType(String portalType) {
        MVNCoreConfig.portalType = portalType;
    }
    
    private static String allowHttpRefererPrefixList = "";
    public static String getAllowHttpRefererPrefixList() {
        return allowHttpRefererPrefixList;
    }
    
    private static String[] allowHttpRefererPrefixListArray = new String[0];
    public static String[] getAllowHttpRefererPrefixListArray() {
        return allowHttpRefererPrefixListArray;
    }
    
    public static void setAllowHttpRefererPrefixList(String refererLists) {
        allowHttpRefererPrefixList = refererLists.toLowerCase();
        allowHttpRefererPrefixListArray = StringUtil.getStringArray(allowHttpRefererPrefixList, ";");
    }

    private static String mvnCoreServiceClassName = "net.myvietnam.mvncore.service.impl.MvnCoreServiceImplDefault";
    public static String getMvnCoreServiceClassName() {
        return mvnCoreServiceClassName;
    }
    public static void setMvnCoreServiceClassName(String mvnCoreServiceClassName) {
        MVNCoreConfig.mvnCoreServiceClassName = mvnCoreServiceClassName;
    }
    
    private static List timerTaskExtList = new ArrayList();
    public static List getTimerTaskExtList() {
        return timerTaskExtList;
    }
    public static void setTimerTaskExtList(List tasks) {
        timerTaskExtList = tasks;
    }
    
    static {  
        load();
    }

    public static void load() {
        reload();
    }

    public static void reload() {
        final String configFilename = FileUtil.getServletClassesPath() + OPTION_FILE_NAME;
        try {
            final DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));

            /* <Database Options> */
            useDataSource = conf.getBoolean("dboptions.use_datasource", false);
            databaseType = conf.getInt("dboptions.database_type", 0);

            if (useDataSource) {
                dataSourceName = conf.getString("dboptions.datasource_name");
            } else {
                driverClassName = conf.getString("dboptions.driver_class_name", driverClassName);
                databaseURL = conf.getString("dboptions.database_url", databaseURL);
                databaseUser = conf.getString("dboptions.database_user", databaseUser);
                databasePassword = conf.getString("dboptions.database_password",databasePassword);

                maxConnection = conf.getInt("dboptions.max_connection", maxConnection);
                maxTimeToWait = conf.getInt("dboptions.max_time_to_wait", maxTimeToWait);

                minutesBetweenRefresh = conf.getInt("dboptions.minutes_between_refresh", minutesBetweenRefresh);
                if (minutesBetweenRefresh < 1) {
                    minutesBetweenRefresh = 1; //min is 1 minute
                }
            }
            /* <Database Options> */

            /* <Mail Options> */
            defaultMailFrom = conf.getString("mailoptions.default_mail_from", defaultMailFrom);

            receiveMail.config(conf);
            sendMail.config(conf);
            /* </Mail Options> */

            /* <Parameter Options> */
            contextPath = conf.getString("paramoptions.context_path", contextPath);
            serverPath = conf.getString("paramoptions.server_path", serverPath);
            if (serverPath.endsWith("/")) {
                serverPath = serverPath.substring(0, serverPath.length() - 1);
            }
            /* </Parameter Options> */

            /* <Date Time Options> */
            serverHourOffset = conf.getInt("dateoptions.server_hour_offset", serverHourOffset);
            if ((serverHourOffset < -13) || (serverHourOffset > 13)) {
                serverHourOffset = 0;
            }
            /* </Date Time Options> */

            /* <User Agent Options>*/
            blockedUserAgents = conf.getString("useragentoptions.blocked_user_agent", blockedUserAgents);
            /* </User Agent Options>*/

            /* <Interceptor Options>*/
            mailInterceptorClassName = conf.getString("interceptor.mailinterceptor_implementation", mailInterceptorClassName);
            contentInterceptorClassName = conf.getString("interceptor.contentinterceptor_implementation", contentInterceptorClassName);
            loginInterceptorClassName = conf.getString("interceptor.loginidinterceptor_implementation", loginInterceptorClassName);
            passwordInterceptorClassName = conf.getString("interceptor.passwordinterceptor_implementation", passwordInterceptorClassName);
            /* </Interceptor Options>*/

            /* <MVNCore Options>*/
            timerManagerDataSourceName = conf.getString("mvncoreconfig.timermanager_datasource", timerManagerDataSourceName);
            enableLinkNofollow = conf.getBoolean("mvncoreconfig.enable_link_nofollow", false);
            enableEncodeURL = conf.getBoolean("mvncoreconfig.enable_encode_url", true);
            portalType = conf.getString("mvncoreconfig.portal_type", portalType);
            setAllowHttpRefererPrefixList(conf.getString("mvncoreconfig.allow_http_referer_prefix_list", allowHttpRefererPrefixList));

            mvnCoreServiceClassName = conf.getString("mvncoreconfig.mvncoreservice_implementation", mvnCoreServiceClassName);
            
            String taskClasses = conf.getString("mvncoreconfig.timertaskext_implementation_list", null);
            if (taskClasses != null) {
                String[] token = StringUtil.getStringArray(taskClasses, ";");
                for (int i = 0; i < token.length; i++) {
                    String className = token[i];
                    if (className.length() > 0) {
                        try {
                            Class c = Class.forName(className);
                            Method getInstanceMethod = c.getMethod("getInstance", (Class[])null);
                            TimerTaskExt timerTaskExt = (TimerTaskExt)getInstanceMethod.invoke(null, (Object[])null);
                            timerTaskExtList.add(timerTaskExt);
                        } catch (Throwable t) {
                            log.warn("Error in loading TimerTaskExt: " + className, t);
                        }
                    }
                }
            }

            /* </MVNCore Options>*/

        } catch (Throwable e) {
            final String message = "com.mvnforum.MVNCoreConfig: Can't read the configuration file: '" + configFilename + "'. Make sure the file is in your CLASSPATH";
            log.error(message, e);
        }
    }
    
    public static class MailConfig {
        
        String configName;

        boolean useMailSource;
        boolean useEmbededSMTPMailServer;
        boolean useSecureConnection;
        String sourceName = "";

        String mailServer = "mail.localhost.com";
        
        int port;
        String userName = "";
        String password = "";
        
        MailConfig(String configName) {
            this.configName = configName;
        }
        
        /**
         * @return Returns the useMailSource.
         */
        public boolean useMailSource() {
            return useMailSource;      
        }

        /**
         * @return Returns the useEmbededSMTPMailServer.
         */
        public boolean useEmbededSMTPMailServer() {
            return useEmbededSMTPMailServer;      
        }

        /**
         * @return Returns the useSecureConnection.
         */
        public boolean isUseSecureConnection() {
            return useSecureConnection;      
        }

        /**
         * @return Returns the sourceName.
         */
        public String getSourceName() {
            return sourceName;
        }

        /**
         * @return Returns the mailServer.
         */
        public String getMailServer() {
            return mailServer;
        }
        /**
         * @return Returns the password.
         */
        public String getPassword() {
            return password;
        }
        /**
         * @return Returns the port.
         */
        public int getPort() {
            return port;
        }
        /**
         * @return Returns the userName.
         */
        public String getUserName() {
            return userName;
        }
        /**
         * @param mailServer The mailServer to set.
         */
        void setMailServer(String mailServer) {
            this.mailServer = mailServer;
        }
        /**
         * @param password The password to set.
         */
        void setPassword(String password) {
            this.password = password;
        }
        /**
         * @param port The port to set.
         */
        void setPort(int port) {
            this.port = port;
        }
        
        /**
         * @param useMailSource The useMailSource to set.
         */
        void setUseMailSource(boolean useMailSource) {
            this.useMailSource = useMailSource;
        }

        /**
         * @param useEmbededSMTPMailServer The useEmbededSMTPMailServer to set.
         */
        void setUseEmbededSMTPMailServer(boolean useEmbededSMTPMailServer) {
            this.useEmbededSMTPMailServer = useEmbededSMTPMailServer;
        }
        
        /**
         * @param userName The userName to set.
         */
        void setUserName(String userName) {
            this.userName = userName;
        }
        
        void config(DOM4JConfiguration conf) {
            
            useMailSource = conf.getBoolean("mailoptions." + configName + ".enable_mail_source", false);
            if (configName.equals("receive_mail") == false) {
                useEmbededSMTPMailServer = conf.getBoolean("mailoptions." + configName + ".use_embeded_smtp_mail_server", true);
            }
            if (useMailSource) {
                sourceName        = conf.getString("mailoptions." + configName + ".mail_source_name");
            } else {
                mailServer        = conf.getString("mailoptions." + configName + ".mail_server", mailServer);
                userName          = conf.getString("mailoptions." + configName + ".username", userName);
                password          = conf.getString("mailoptions." + configName + ".password", password);
                port              = conf.getInt   ("mailoptions." + configName + ".port", port);
                useSecureConnection = conf.getBoolean("mailoptions." + configName + ".use_secure_connection", false);
            }
        }
    }
}

