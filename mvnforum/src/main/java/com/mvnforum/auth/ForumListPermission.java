/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/auth/ForumListPermission.java,v 1.17
 * 2009/01/06 16:43:12 minhnn Exp $ $Author: minhnn $ $Revision: 1.17 $ $Date: 2009/01/06 16:43:12 $
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
package com.mvnforum.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mvnforum.db.ForumBean;
import com.mvnforum.db.ForumCache;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

/**
 * This class is used in MVNForumPermissionImpl to imnplement forum-specific permission NOTE: This
 * class is NOT thread-safe
 */
class ForumListPermission {

  private static final Logger log = LoggerFactory.getLogger(ForumListPermission.class);

  ArrayList forumList = new ArrayList();

  boolean allForumsPermission = false;

  boolean bypassPrivateForum = false;

  ForumListPermission() {}

  void setAllForumsPermission(boolean permission) {
    allForumsPermission = permission;
  }

  boolean isGlobalPermission() {
    return allForumsPermission;
  }

  void setForumPermission(int forumID, boolean permission) {
    // always remove forumid
    for (Iterator iter = forumList.iterator(); iter.hasNext();) {
      int currentForumID = ((Integer) iter.next()).intValue();
      if (currentForumID == forumID) {
        iter.remove();
      }
    } // for

    // now add to the list if the permission = true
    if (permission) {
      // add permission
      forumList.add(new Integer(forumID));
    }
  }

  boolean hasPermission(int forumID) {
    for (int i = 0; i < forumList.size(); i++) {
      int currentForumID = ((Integer) forumList.get(i)).intValue();
      if (currentForumID == forumID) {
        return true;
      }
    }

    // have permission on all forums, then we check if this is a Private Forum
    if (allForumsPermission) {
      if (bypassPrivateForum) {
        return true;
      }

      try {
        ForumBean forumBean = ForumCache.getInstance().getBean(forumID);
        if (forumBean.getForumType() == ForumBean.FORUM_TYPE_DEFAULT) {
          return true;
        }
      } catch (ObjectNotFoundException ex) {
        log.error("Cannot get the ForumBean in ForumListPermission (ObjectNotFoundException)", ex);
      } catch (Exception ex) {
        log.error("Cannot get the ForumBean in ForumListPermission", ex);
      }
    }

    // if not found, then we return false (no permission on the forum)
    return false;
  }

  boolean hasPermssionInAtLeastOneForum() {

    // now check if have permission on any forums by checking the forumList size
    if (forumList.size() > 0) {
      // forumList size > 0 means there is permission on at least one forum
      return true;
    }

    // have permission on all forums, then we check if this is a Private Forum
    if (allForumsPermission) {
      if (bypassPrivateForum) {
        return true;
      }

      try {
        Collection forumBeans = ForumCache.getInstance().getBeans();
        for (Iterator iter = forumBeans.iterator(); iter.hasNext();) {
          ForumBean forumBean = (ForumBean) iter.next();
          if (forumBean.getForumType() == ForumBean.FORUM_TYPE_DEFAULT) {
            return true;
          }
        }
      } catch (Exception ex) {
        log.error("Cannot get ForumBeans in ForumListPermission", ex);
      }
    }

    // if not found, then we return false (no permission on any forums)
    return false;
  }

  public boolean isBypassPrivateForum() {
    return bypassPrivateForum;
  }

  public void setBypassPrivateForum(boolean ignorePrivateOption) {
    this.bypassPrivateForum = ignorePrivateOption;
  }
}
