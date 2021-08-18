/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/ManagerFactory.java,v 1.23 2008/12/31
 * 03:50:25 trungth Exp $ $Author: trungth $ $Revision: 1.23 $ $Date: 2008/12/31 03:50:25 $
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
 * @author: Luis Miguel Hernanz
 *
 * @author: Minh Nguyen
 */
package com.mvnforum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.auth.Authenticator;
import com.mvnforum.auth.OnlineUserFactory;
import com.mvnforum.auth.service.MvnAuthServiceFactory;

/**
 * Instance that returns the right implementation for the different parts of the mvnforum system.
 *
 * @author <a href="luish@germinus.com">Luis Miguel Hernanz</a>
 * @version $Revision: 1.23 $
 */
// @todo : split this class to new class DAOFactory
public final class ManagerFactory {

  private static final Logger log = LoggerFactory.getLogger(ManagerFactory.class);

  /**
   * Creates a new <code>ManagerFactory</code> instance.
   */
  private ManagerFactory() {}

  private static OnlineUserFactory onlineUserFactory = null;
  private static Authenticator authenticator = null;
  private static RequestProcessor requestProcessor = null;

  public static synchronized OnlineUserFactory getOnlineUserFactory() {
    if (onlineUserFactory == null) {
      try {
        // Class c = Class.forName(MVNForumFactoryConfig.getOnlineUserFactoryClassName());
        // onlineUserFactory = (OnlineUserFactory) c.newInstance();
        onlineUserFactory = MvnAuthServiceFactory.getMvnAuthService().getOnlineUserFactory();
        log.info("onlineUserFactory = " + onlineUserFactory);
      } catch (Exception e) {
        log.error("Error returning the online user factory.", e);
        throw new RuntimeException(e.getMessage());
      }
    }
    return onlineUserFactory;
  }

  public static synchronized Authenticator getAuthenticator() {

    if (authenticator != null) {
      return authenticator;
    }

    try {
      String authenticatorClass = MVNForumFactoryConfig.getAuthenticatorClassName();
      if ((null != authenticatorClass) && (authenticatorClass.trim().length() > 0)) {
        log.debug("Try loading the authenticator: " + authenticatorClass);
        if (authenticator == null) {
          Class c = Class.forName(authenticatorClass);
          authenticator = (Authenticator) c.newInstance();
          log.info("authenticator = " + authenticator);
        }
        return authenticator;
      }
    } catch (Exception e) {
      log.error("Error getting the authentication object", e);
    }
    return null;
  }

  public static synchronized RequestProcessor getRequestProcessor() {
    if (requestProcessor == null) {
      try {
        Class c = Class.forName(MVNForumFactoryConfig.getRequestProcessorClassName());
        requestProcessor = (RequestProcessor) c.newInstance();
        log.info("requestProcessor = " + requestProcessor);
      } catch (Exception e) {
        // This should never happen
        log.error("Error returning the requestProcessor.", e);
        throw new RuntimeException(e.getMessage());
      }
    }
    return requestProcessor;
  }

}
