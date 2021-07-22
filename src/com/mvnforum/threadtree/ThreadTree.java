/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/threadtree/ThreadTree.java,v 1.7 2009/06/16 03:15:16 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.7 $
 * $Date: 2009/06/16 03:15:16 $
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
 * @author: Eg
 */
package com.mvnforum.threadtree;

import java.text.SimpleDateFormat;
import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.db.*;

/**
 * Builds up HTML thread+list
 * 
 * @author
 */
public class ThreadTree {

    private static final Logger log = LoggerFactory.getLogger(ThreadTree.class);

    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm ");
    protected ThreadTreeBuilder builder; // the builder
    protected int thisThread; // the thread which has to be merged + we don't

    // include this in list

    /**
     * Constructor
     * 
     * @param builder
     * @param thisThread
     */
    public ThreadTree(ThreadTreeBuilder builder, int thisThread) {
        this.builder = builder;
        this.thisThread = thisThread;
    }

    /**
     * Builds up a list from threads, HTML
     * 
     * @return
     * @throws DatabaseException
     */
    public String build(Locale locale) throws DatabaseException {

        log.debug("build start");
        String option = MVNForumResourceBundle.getString(locale, "mvnforum.user.mergethread.option");

        String result = "<select id=\"destthread\" name=\"destthread\">\r\n";
        result += "<option value=\"\">" + option + "</option>";
        result += "<option value=\"\"></option>";

        Collection categories = DAOFactory.getCategoryDAO().getCategories();
        Iterator cIterator = categories.iterator();
        while (cIterator.hasNext()) {
            CategoryBean categoryBean = (CategoryBean) cIterator.next();
            // System.out.println("kat " + categoryBean.getCategoryName());
            result += "<option value=\"\">" + categoryBean.getCategoryName()
                    + "</option>";
            result += "<option value=\"\">---------------------------------</option>";
            int rowsReturn = 0;
            Collection forums = DAOFactory.getForumDAO().getForums_inCategory(categoryBean.getCategoryID());
            Iterator fIterator = forums.iterator();
            while (fIterator.hasNext()) {
                ForumBean forumBean = (ForumBean) fIterator.next();
                rowsReturn = ThreadCache.getInstance()
                        .getNumberOfEnableThreads_inForum(
                                forumBean.getForumID());
                log.debug("*** for " + forumBean.getForumName() + "rows:  " + rowsReturn);
                result += "<option value=\"\">&nbsp;&nbsp;&nbsp;&nbsp;" + forumBean.getForumName() + "</option>";

                if (rowsReturn > 0) {
                    Collection threadBeans = ThreadCache.getInstance()
                            .getNormalEnableThreads_inForum_withSortSupport_limit(
                                    forumBean.getForumID(), 0, rowsReturn,
                                    "ThreadViewCount", "ASC");

                    Iterator tIterator = threadBeans.iterator();
                    while (tIterator.hasNext()) {
                        ThreadBean bean = (ThreadBean) tIterator.next();
                        log.debug("--- tem " + bean.getThreadTopic());

                        // we dont enlist self - we dont merge a thread with
                        // itself
                        boolean include = true;
                        if (builder.getFilterCriteria() != null && builder.getFilterCriteria().length() > 0) {
                            include = bean.getThreadTopic().indexOf(builder.getFilterCriteria()) != -1;
                        }
                        if (bean.getThreadID() != thisThread && include) {
                            result += "<option value=\""
                                    + bean.getThreadID()
                                    + "\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                    + bean.getThreadTopic() + " &nbsp;&nbsp;"
                                    + sdf.format(bean.getThreadCreationDate())
                                    + "</option>\r\n";
                        }
                    }
                }
            }

        }
        result += "</select>\r\n";

        // //Collection beans = builder.getThreadBeans();
        // Iterator iterator = beans.iterator();
        //
        // String result = " <select name=\"destthread\">\r\n";
        // while (iterator.hasNext()) {
        // ThreadBean bean = (ThreadBean) iterator.next();
        // // we dont enlist self - we dont merge a thread with itself
        // if (bean.getThreadID() != thisThread) {
        // result += "<option value=\"" + bean.getThreadID() + "\">" +
        // bean.getThreadTopic() + "</option>\r\n";
        // }
        // }
        // result += "</select>\r\n";

        log.debug("build ends");
        return result;
    }
}
