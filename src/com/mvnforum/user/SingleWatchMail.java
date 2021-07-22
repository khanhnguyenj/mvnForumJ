/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/SingleWatchMail.java,v 1.17 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.17 $
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

import freemarker.template.*;

public class SingleWatchMail extends WatchMail {

    private static final Logger log = LoggerFactory.getLogger(SingleWatchMail.class);

    private Map m_threadWatchs = new HashMap();

    public SingleWatchMail(MemberBean member, MVNForumPermission perm, String forumBase, Timestamp lastSent, Timestamp now)
        throws IllegalArgumentException {

        super(member, perm, forumBase, lastSent, now);

    }

    void appendWatch(WatchBean watchBean) throws ObjectNotFoundException, DatabaseException {

        super.appendWatch(watchBean);
        for (Iterator iterator = m_threadList.iterator(); iterator.hasNext(); ) {
            int threadID = Integer.parseInt(iterator.next().toString());
            createWatchMessageBean(threadID);
        }
    }

    private void createWatchMessageBean(int threadID) throws ObjectNotFoundException, DatabaseException {

        Map threadWatch = new HashMap();

        // TODO : why lastSent is set 2 times here ?
        threadWatch.put("lastSent", m_lastSent);
        threadWatch.put("lastSent", m_root.get("lastSent"));
        threadWatch.put("now", m_root.get("now"));
        threadWatch.put("forumBase", m_forumBase);

        ThreadBean thread = ThreadCache.getInstance().getThread(threadID);

        ForumBean forumBean = ForumCache.getInstance().getBean(thread.getForumID());
        CategoryBean categoryBean = CategoryCache.getInstance().getBean(forumBean.getCategoryID());
        PostBean lastPostBean = PostCache.getInstance().getLastEnablePost_inThread(threadID);

        if ((m_permission.canReadPost(thread.getForumID()) == false) ||
            (forumBean.getForumStatus() == ForumBean.FORUM_STATUS_DISABLED)) {
            return;
        }

        int lastForumID = -1;
        SimpleHash beanWatchMail = new SimpleHash();
        threadWatch.put("threadWatch", beanWatchMail);
        m_threadWatchs.put(new Integer(threadID), threadWatch);

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
        beanWatchMail.put("memberName", memberName);
        beanWatchMail.put("lastPostMemberName", thread.getLastPostMemberName());
        beanWatchMail.put("threadLastPostDate", m_dateFormat.format(DateUtil.convertGMTDate(thread.getThreadLastPostDate(), m_receiverTimeZone)));
        
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

    private String getWatchMailSubject(int threadID) throws IOException, TemplateException {

        StringWriter bodyWriter = new StringWriter(4096);
        Template subjectTemplate = m_watchMailService.getSingleSubjectTemplate();
        subjectTemplate.process(m_threadWatchs.get(new Integer(threadID)), bodyWriter);

        return bodyWriter.toString();
    }

    private String getWatchMailContent(int threadID) throws IOException, TemplateException {

        StringWriter subjectWriter = new StringWriter(256);
        Template bodyTemplate = m_watchMailService.getSingleBodyTemplate();
        bodyTemplate.process(m_threadWatchs.get(new Integer(threadID)), subjectWriter);

        return subjectWriter.toString();
    }

    public Collection getMailMessageStructs(String from, String to) throws IOException, TemplateException {

        ArrayList mailMessageStructs = new ArrayList ();
        for (int i = 0; i < m_threadList.size(); i++) {
            int threadID = Integer.parseInt(m_threadList.get(i).toString());
            MailMessageStruct mailMessageStruct = new MailMessageStruct();
            mailMessageStruct.setSubject(getWatchMailSubject(threadID));
            mailMessageStruct.setMessage(getWatchMailContent(threadID));
            mailMessageStruct.setFrom(from);
            mailMessageStruct.setTo(to);
            mailMessageStruct.setSendAsHtml(MVNForumConfig.getSendWatchMailAsHTML());
            mailMessageStructs.add(mailMessageStruct);
        }
        return mailMessageStructs;
    }
}
