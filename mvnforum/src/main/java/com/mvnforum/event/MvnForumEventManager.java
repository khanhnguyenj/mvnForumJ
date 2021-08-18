/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/event/MvnForumEventManager.java,v 1.5
 * 2008/12/31 03:50:24 trungth Exp $ $Author: trungth $ $Revision: 1.5 $ $Date: 2008/12/31 03:50:24
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
package com.mvnforum.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MvnForumEventManager {

  private static final Logger log = LoggerFactory.getLogger(MvnForumEventManager.class);

  // static variable
  private static MvnForumEventManager instance = new MvnForumEventManager();

  private transient Collection eventListeners;

  private MvnForumEventManager() {
    eventListeners = new ArrayList();
  }

  public static MvnForumEventManager getInstance() {
    return instance;
  }

  /************************************************************************
   * Event method
   ************************************************************************/
  public synchronized void removeMvnForumEventListener(MvnForumEventListener listener) {
    eventListeners.remove(listener);
  }

  public synchronized void addMvnForumEventListener(MvnForumEventListener listener) {
    log.debug("MvnForumEventManager.addMvnForumEventListener()");
    eventListeners.add(listener);
  }

  public void firePreLogin(MvnForumEvent e) {
    for (Iterator iterator = eventListeners.iterator(); iterator.hasNext();) {
      ((MvnForumEventListener) iterator.next()).onPreLogin(e);
    }
  }

  public void firePostLogin(MvnForumEvent e) {
    for (Iterator iterator = eventListeners.iterator(); iterator.hasNext();) {
      ((MvnForumEventListener) iterator.next()).onPostLogin(e);
    }
  }

  public void firePreLogout(MvnForumEvent e) {
    log.debug("MvnForumEventManager.firePreLogout() " + eventListeners.size());
    for (Iterator iterator = eventListeners.iterator(); iterator.hasNext();) {
      ((MvnForumEventListener) iterator.next()).onPreLogout(e);
    }
  }

  public void firePostLogout(MvnForumEvent e) {
    for (Iterator iterator = eventListeners.iterator(); iterator.hasNext();) {
      ((MvnForumEventListener) iterator.next()).onPostLogout(e);
    }
  }

}
