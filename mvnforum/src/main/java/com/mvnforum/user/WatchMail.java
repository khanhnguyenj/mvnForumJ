/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/WatchMail.java,v 1.44 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.44 $
 * $Date: 2008/12/31 03:50:24 $
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
 * @author: Minh Nguyen
 * @author: Mai  Nguyen
 * @author: Cord         cord_sw@lupinex.com
 */
package com.mvnforum.user;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableMVNCodeFilter;
import net.myvietnam.mvncore.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.MVNForumConfig;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.db.*;
import com.mvnforum.service.MvnForumServiceFactory;
import com.mvnforum.service.WatchMailService;

import freemarker.template.*;

class WatchMail {

    // static variable
    private static final Logger log = LoggerFactory.getLogger(WatchMail.class);

    //Lan Anh: I must to modify these fields to protected for SingleWatchMail
    protected List m_threadList = new ArrayList();
    protected DateFormat m_dateFormat;

    protected String m_forumBase = null;
    protected Timestamp m_lastSent = null;
    private Timestamp m_now = null;

    protected MemberBean m_receiver;
    protected double m_receiverTimeZone = 0;

    protected MVNForumPermission m_permission;

    protected Map m_root = new HashMap();
    protected WatchMailService m_watchMailService;

    private List m_threadWatchList = new ArrayList();

    WatchMail(MemberBean member, MVNForumPermission perm, String forumBase, Timestamp lastSent, Timestamp now)
        throws IllegalArgumentException {

        if (member == null) {
            throw new IllegalArgumentException("MemberBean in WatchMail must not be null.");
        }
        if (perm == null) {
            throw new IllegalArgumentException("Permission in WatchMail must not be null.");
        }

        m_watchMailService = MvnForumServiceFactory.getMvnForumService().getWatchMailService();

        m_receiver = member;
        m_permission = perm;
        m_forumBase = forumBase;
        m_lastSent  = lastSent;
        m_now = now;

        init();
    }

    private void init() {
        m_receiverTimeZone = m_receiver.getMemberTimeZone();
        if (Math.abs(m_receiverTimeZone) > 12) {
            // timeZone < -12 or timeZone > 12
            m_receiverTimeZone = 0;
        }

        String localeName = m_receiver.getMemberLanguage();
        Locale locale = null;
        if (localeName.length() == 0) {
            locale = MVNForumConfig.getDefaultLocale();
        } else {
            locale = I18nUtil.getLocale(localeName);
        }
        m_dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);

        String nowDesc = m_dateFormat.format(DateUtil.convertGMTDate(m_now, m_receiverTimeZone));
        String lastSentDesc = m_dateFormat.format(DateUtil.convertGMTDate(m_lastSent, m_receiverTimeZone));

        m_root.put("now", nowDesc);
        m_root.put("lastSent", lastSentDesc);
        m_root.put("forumBase", m_forumBase);
        m_root.put("threadWatchList", m_threadWatchList);
    }

    boolean haveAtLeastOneNewThread() {
        return (m_threadList.size() > 0);
    }

    void appendWatch(WatchBean watchBean) throws ObjectNotFoundException, DatabaseException {

        AssertionUtil.doAssert(m_receiver.getMemberID() == watchBean.getMemberID(), "Assertion: receiver.getMemberID() must be equal to watchBean.getMemberID()!");

        Timestamp lastSent = watchBean.getWatchLastSentDate();

        int categoryID = watchBean.getCategoryID();
        int forumID = watchBean.getForumID();
        int threadID = watchBean.getThreadID();

        // log.debug("appendWatch called!!! c = " + categoryID + " f = " + forumID + " t = " + threadID);

        Collection threadBeans = null;
        if (categoryID != 0) {
            threadBeans = DAOFactory.getThreadDAO().getEnableThreads_inCategory(categoryID, lastSent);
        } else if (forumID != 0) {
            threadBeans = DAOFactory.getThreadDAO().getEnableThreads_inForum(forumID, lastSent);
        } else if (threadID != 0) {
            if (shouldProcessThread(threadID)) {
                threadBeans = DAOFactory.getThreadDAO().getEnableThreads_inThread(threadID, lastSent);
            } else {
                return; // ignore the reduntdant thread
            }
        } else {
            threadBeans = DAOFactory.getThreadDAO().getEnableThreads_inGlobal(lastSent);
        }

        // check his owner post
        if (MVNForumConfig.getEnableSendWatchMailOfMyOwnPost() == false) {
            checkThreadOwner(threadBeans);
        }

        if (threadBeans.size() == 0) {
            return; // no new thread
        }

        // remember that this WatchMail has process these thread
        rememberThread(threadBeans);

        if (threadBeans.size() > 0) {
            createWatchMessageBean(threadBeans);
        }
    }

    private void createWatchMessageBean(Collection threadBeans)
        throws DatabaseException, ObjectNotFoundException {

        // now, has at least one new thread, then we get the mail content
        int lastForumID = -1;//init it to a not existed forumID

        for (Iterator iterator = threadBeans.iterator(); iterator.hasNext(); ) {
            ThreadBean thread = (ThreadBean)iterator.next();
            SimpleHash beanWatchMail = new SimpleHash();

            ForumBean forumBean = ForumCache.getInstance().getBean(thread.getForumID());
            CategoryBean categoryBean = CategoryCache.getInstance().getBean(forumBean.getCategoryID());
            PostBean lastPostBean = PostCache.getInstance().getLastEnablePost_inThread(thread.getThreadID());

            if ((m_permission.canReadPost(thread.getForumID()) == false) ||
                (forumBean.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED)) {
                continue;
            }

            m_threadWatchList.add(beanWatchMail);

            // if move to a new forum, then we print the summary of category and forum
            if (thread.getForumID() != lastForumID) {
                lastForumID = thread.getForumID();
                beanWatchMail.put("leader", true);
            } else {
                beanWatchMail.put("leader", false);
            }

            String forumName = forumBean.getForumName();
            String categoryName = categoryBean.getCategoryName();
            String memberName = m_receiver.getMemberName();

            String threadTopic = DisableHtmlTagFilter.filter(thread.getThreadTopic()); 
            String lastPostTopic = DisableHtmlTagFilter.filter(lastPostBean.getPostTopic()); 
            
            String threadBody = DisableHtmlTagFilter.filter(thread.getThreadBody());
            String lastPostBody = DisableHtmlTagFilter.filter(lastPostBean.getPostBody());
            threadBody = EnableMVNCodeFilter.removeBBCode(threadBody);
            lastPostBody = EnableMVNCodeFilter.removeBBCode(lastPostBody);

            if (MVNForumConfig.getMaxLastPostBodyInWatch() != 0) {
                threadBody = StringUtil.getShorterString(threadBody, MVNForumConfig.getMaxLastPostBodyInWatch()); 
                lastPostBody = StringUtil.getShorterString(lastPostBody, MVNForumConfig.getMaxLastPostBodyInWatch()); 
            }

            beanWatchMail.put("categoryName", categoryName);
            beanWatchMail.put("forumName", forumName);
            beanWatchMail.put("memberName", thread.getMemberName());
            beanWatchMail.put("lastPostMemberName", thread.getLastPostMemberName());
            beanWatchMail.put("threadLastPostDate", m_dateFormat.format(DateUtil.convertGMTDate(thread.getThreadLastPostDate(),m_receiverTimeZone)));
            
            String threadUrl = "/viewthread?thread=" + thread.getThreadID();
            String lastPostUrl = "/viewthread?thread=" + thread.getThreadID() + "&lastpage=yes#lastpost";
            if (MVNForumConfig.getEnableFriendlyURL()) {
                threadUrl = FriendlyURLParamUtil.createFriendlyURL(threadUrl);
                lastPostUrl = FriendlyURLParamUtil.createFriendlyURL(lastPostUrl);
            }
            beanWatchMail.put("threadTopic", threadTopic);
            beanWatchMail.put("threadBody", threadBody);
            beanWatchMail.put("threadUrl", m_forumBase + threadUrl);
            
            beanWatchMail.put("lastPostTopic", lastPostTopic);
            beanWatchMail.put("lastPostBody", lastPostBody);
            beanWatchMail.put("lastPostUrl", m_forumBase + lastPostUrl);
            
            m_watchMailService.loadCustomizedVariablesForBeanWatchMail(thread, memberName, beanWatchMail);
        }
    }

    private void checkThreadOwner (Collection threadBeans) {
        for (Iterator iter = threadBeans.iterator(); iter.hasNext(); ) {
            String postMemberName = ((ThreadBean) iter.next()).getLastPostMemberName();
            String memberName = m_receiver.getMemberName();
            if (memberName.equals(postMemberName)) {
                iter.remove();
            }
        }
    }

    private boolean shouldProcessThread(int threadID) {

        for (Iterator iter = m_threadList.iterator(); iter.hasNext();) {
            Integer element = (Integer) iter.next();
            int currentThreadID = element.intValue();
            if (currentThreadID == threadID) {
                return false;
            }
        }
        return true;
    }

    /**
     * Also remove the redundant thread in this threadBeans
     * @param threadBeans : the threads to remember
     */
    private void rememberThread(Collection threadBeans) {

        for (Iterator iter = threadBeans.iterator(); iter.hasNext(); ) {
            int currentThreadID = ((ThreadBean)iter.next()).getThreadID();
            if (shouldProcessThread(currentThreadID)) {
                m_threadList.add(new Integer(currentThreadID));
            } else {
                iter.remove();
            }
        }
    }

    String getWatchMailSubject() throws IOException, TemplateException {

        StringWriter subjectWriter = new StringWriter(256);
        Template subjectTemplate = m_watchMailService.getDigestSubjectTemplate();
        subjectTemplate.process(m_root, subjectWriter);

        return subjectWriter.toString();
    }

    String getWatchMailBody() throws IOException, TemplateException {

        StringWriter bodyWriter = new StringWriter(4096);
        Template bodyTemplate = m_watchMailService.getDigestBodyTemplate();
        bodyTemplate.process(m_root, bodyWriter);

        return bodyWriter.toString();
    }

}
