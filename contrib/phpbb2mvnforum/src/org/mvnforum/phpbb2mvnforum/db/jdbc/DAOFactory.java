/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/DAOFactory.java,v 1.15 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.15 $
 * $Date: 2007/01/15 10:27:12 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db.jdbc;

import org.mvnforum.phpbb2mvnforum.db.CategoryDAO;
import org.mvnforum.phpbb2mvnforum.db.ForumDAO;
import org.mvnforum.phpbb2mvnforum.db.MemberDAO;
import org.mvnforum.phpbb2mvnforum.db.MessageDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbAuthAccessDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbCategoriesDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbForumDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbGroupDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbRanksDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsWatchDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserGroupDAO;
import org.mvnforum.phpbb2mvnforum.db.PostDAO;
import org.mvnforum.phpbb2mvnforum.db.ThreadDAO;

import com.mvnforum.db.GroupPermissionDAO;
import com.mvnforum.db.GroupsDAO;
import com.mvnforum.db.MemberPermissionDAO;
import com.mvnforum.db.MessageFolderDAO;
import com.mvnforum.db.RankDAO;
import com.mvnforum.db.WatchDAO;

public class DAOFactory {
    
    private PhpbbUserDAO phpUserDAO = null;    
    private PhpbbCategoriesDAO phpbbCategoriesDAO = null;
    private PhpbbForumDAO phpbbForumDAO = null;
    private PhpbbPostDAO phpbbPostDAO = null;
    private PhpbbPostTextDAO phpbbPostTextDAO = null;
    private PhpbbTopicsDAO phpbbTopicsDAO = null;
    private PhpbbPrivmMsgsDAO phpbbPrivmMsgsDAO = null;
    private PhpbbPrivmMsgsTextDAO phpbbPrivmMsgsTextDAO = null;
    private PhpbbGroupDAO phpbbGroupDAO = null;    
    private PhpbbUserGroupDAO phpbbUserGroupDAO = null;
    private PhpbbTopicsWatchDAO phpbbTopicsWatchDAO = null;
    private PhpbbRanksDAO phpbbRanksDAO = null;
    private PhpbbAuthAccessDAO phpbbAuthAccessDAO = null;
    
    private CategoryDAO categoryDAO = null;
    private ForumDAO forumDAO = null;
    private PostDAO postDAO = null;
    private MemberDAO memberDAO = null;
    private ThreadDAO threadDAO = null;
    private MessageDAO messageDAO = null;
    private RankDAO rankDAO = null;
    private GroupsDAO groupsDAO = null;
    private MessageFolderDAO messageFolderDAO = null;
    private MemberPermissionDAO memberPermissionDAO = null;
    private WatchDAO watchDAO = null;
    private GroupPermissionDAO groupPermissionDAO = null;
    
    private CategoryDAO categoryDAOXML = null;
    private ForumDAO forumDAOXML = null;
    private PostDAO postDAOXML = null;
    private ThreadDAO threadDAOXML = null;

    public DAOFactory() {
        super();
        phpUserDAO = new PhpbbUserDAOImpl();
        phpbbCategoriesDAO = new PhpbbCategoriesDAOImpl();
        phpbbForumDAO = new PhpbbForumDAOImpl();
        phpbbPostDAO = new PhpbbPostDAOImpl();
        phpbbPostTextDAO = new PhpbbPostTextDAOImpl();
        phpbbTopicsDAO = new PhpbbTopicsDAOImpl();
        phpbbPrivmMsgsDAO = new PhpbbPrivmMsgsDAOImpl();
        phpbbPrivmMsgsTextDAO = new PhpbbPrivmMsgsTextDAOImpl();
        phpbbGroupDAO = new PhpbbGroupDAOImpl();
        phpbbUserGroupDAO = new PhpbbUserGroupDAOImpl();
        phpbbTopicsWatchDAO = new PhpbbTopicsWatchDAOImpl();
        phpbbRanksDAO = new PhpbbRanksDAOImpl ();
        phpbbAuthAccessDAO = new PhpbbAuthAccessDAOImpl ();
        
        categoryDAO = new CategoryDAOImplJDBC();
        forumDAO = new ForumDAOImplJDBC();
        postDAO = new PostDAOImplJDBC();
        memberDAO = new MemberDAOImplJDBC();
        threadDAO = new ThreadDAOImplJDBC();
        messageDAO = new MessageDAOImplJDBC();
        rankDAO = new RankDAOImplJDBC();
        groupsDAO = new GroupsDAOImplJDBC ();
        messageFolderDAO = new MessageFolderDAOImplJDBC ();
        memberPermissionDAO = new MemberPermissionDAOImplJDBC ();
        watchDAO = new WatchDAOImplJDBC ();
        groupPermissionDAO = new GroupPermissionDAOImplJDBC ();
        
        categoryDAOXML = new CategoryDAOImplXML();
        forumDAOXML = new ForumDAOImplXML();
        postDAOXML = new PostDAOImplXML();
        threadDAOXML = new ThreadDAOImplXML();
    }

    public PhpbbUserDAO getPhpUserDAO() {
        return phpUserDAO;
    }

    public CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void setPhpUserDAO(PhpbbUserDAO phpUserDAO) {
        this.phpUserDAO = phpUserDAO;
    }

    public PhpbbCategoriesDAO getPhpbbCategoriesDAO() {
        return phpbbCategoriesDAO;
    }

    public void setPhpbbCategoriesDAO(PhpbbCategoriesDAO phpbbCategoriesDAO) {
        this.phpbbCategoriesDAO = phpbbCategoriesDAO;
    }

    public PhpbbForumDAO getPhpbbForumDAO() {
        return phpbbForumDAO;
    }

    public void setPhpbbForumDAO(PhpbbForumDAO phpbbForumDAO) {
        this.phpbbForumDAO = phpbbForumDAO;
    }

    public PhpbbPostDAO getPhpbbPostDAO() {
        return phpbbPostDAO;
    }

    public void setPhpbbPostDAO(PhpbbPostDAO phpbbPostDAO) {
        this.phpbbPostDAO = phpbbPostDAO;
    }

    public ForumDAO getForumDAO() {
        return forumDAO;
    }

    public void setForumDAO(ForumDAO forumDAO) {
        this.forumDAO = forumDAO;
    }

    public PhpbbPostTextDAO getPhpbbPostTextDAO() {
        return phpbbPostTextDAO;
    }

    public void setPhpbbPostTextDAO(PhpbbPostTextDAO phpbbPostTextDAO) {
        this.phpbbPostTextDAO = phpbbPostTextDAO;
    }

    public PostDAO getPostDAO() {
        return postDAO;
    }

    public void setPostDAO(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public MemberDAO getMemberDAO() {
        return memberDAO;
    }

    public void setMemberDAO(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public PhpbbTopicsDAO getPhpbbTopicsDAO() {
        return phpbbTopicsDAO;
    }

    public void setPhpbbTopicsDAO(PhpbbTopicsDAO phpbbTopicsDAO) {
        this.phpbbTopicsDAO = phpbbTopicsDAO;
    }

    public ThreadDAO getThreadDAO() {
        return threadDAO;
    }

    public void setThreadDAO(ThreadDAO threadDAO) {
        this.threadDAO = threadDAO;
    }

    public PhpbbPrivmMsgsDAO getPhpbbPrivmMsgsDAO() {
        return phpbbPrivmMsgsDAO;
    }

    public void setPhpbbPrivmMsgsDAO(PhpbbPrivmMsgsDAO phpbbPrivmMsgsDAO) {
        this.phpbbPrivmMsgsDAO = phpbbPrivmMsgsDAO;
    }

    public PhpbbPrivmMsgsTextDAO getPhpbbPrivmMsgsTextDAO() {
        return phpbbPrivmMsgsTextDAO;
    }

    public void setPhpbbPrivmMsgsTextDAO(PhpbbPrivmMsgsTextDAO phpbbPrivmMsgsTextDAO) {
        this.phpbbPrivmMsgsTextDAO = phpbbPrivmMsgsTextDAO;
    }

    public MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public CategoryDAO getCategoryDAOXML() {
        return categoryDAOXML;
    }

    public void setCategoryDAOXML(CategoryDAO categoryDAOXML) {
        this.categoryDAOXML = categoryDAOXML;
    }

    public ForumDAO getForumDAOXML() {
        return forumDAOXML;
    }

    public void setForumDAOXML(ForumDAO forumDAOXML) {
        this.forumDAOXML = forumDAOXML;
    }

    public PostDAO getPostDAOXML() {
        return postDAOXML;
    }

    public void setPostDAOXML(PostDAO postDAOXML) {
        this.postDAOXML = postDAOXML;
    }

    public ThreadDAO getThreadDAOXML() {
        return threadDAOXML;
    }

    public void setThreadDAOXML(ThreadDAO threadDAOXML) {
        this.threadDAOXML = threadDAOXML;
    }

    public PhpbbGroupDAO getPhpbbGroupDAO() {
        return phpbbGroupDAO;
    }

    public void setPhpbbGroupDAO(PhpbbGroupDAO phpbbGroupDAO) {
        this.phpbbGroupDAO = phpbbGroupDAO;
    }

    public PhpbbUserGroupDAO getPhpbbUserGroupDAO() {
        return phpbbUserGroupDAO;
    }

    public void setPhpbbUserGroupDAO(PhpbbUserGroupDAO phpbbUserGroupDAO) {
        this.phpbbUserGroupDAO = phpbbUserGroupDAO;
    }

    public PhpbbTopicsWatchDAO getPhpbbTopicsWatchDAO() {
        return phpbbTopicsWatchDAO;
    }

    public void setPhpbbTopicsWatchDAO(PhpbbTopicsWatchDAO phpbbTopicsWatchDAO) {
        this.phpbbTopicsWatchDAO = phpbbTopicsWatchDAO;
    }

    public RankDAO getRankDAO() {
        return rankDAO;
    }

    public void setRankDAO(RankDAO rankDAO) {
        this.rankDAO = rankDAO;
    }

    public GroupsDAO getGroupsDAO() {
        return groupsDAO;
    }

    public void setGroupsDAO(GroupsDAO groupsDAO) {
        this.groupsDAO = groupsDAO;
    }

    public MessageFolderDAO getMessageFolderDAO() {
        return messageFolderDAO;
    }

    public void setMessageFolderDAO(MessageFolderDAO messageFolderDAO) {
        this.messageFolderDAO = messageFolderDAO;
    }

    public MemberPermissionDAO getMemberPermissionDAO() {
        return memberPermissionDAO;
    }

    public void setMemberPermissionDAO(MemberPermissionDAO memberPermissionDAO) {
        this.memberPermissionDAO = memberPermissionDAO;
    }

    public WatchDAO getWatchDAO() {
        return watchDAO;
    }

    public void setWatchDAO(WatchDAO watchDAO) {
        this.watchDAO = watchDAO;
    }

    public PhpbbRanksDAO getPhpbbRanksDAO() {
        return phpbbRanksDAO;
    }

    public void setPhpbbRanksDAO(PhpbbRanksDAO phpbbRanksDAO) {
        this.phpbbRanksDAO = phpbbRanksDAO;
    }

    public GroupPermissionDAO getGroupPermissionDAO() {
        return groupPermissionDAO;
    }

    public void setGroupPermissionDAO(GroupPermissionDAO groupPermissionDAO) {
        this.groupPermissionDAO = groupPermissionDAO;
    }

    public PhpbbAuthAccessDAO getPhpbbAuthAccessDAO() {
        return phpbbAuthAccessDAO;
    }

    public void setPhpbbAuthAccessDAO(PhpbbAuthAccessDAO phpbbAuthAccessDAO) {
        this.phpbbAuthAccessDAO = phpbbAuthAccessDAO;
    }

}
