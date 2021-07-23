/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/DAOFactory.java,v 1.43 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.43 $
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
 */
package com.mvnforum.db;

import net.myvietnam.mvncore.MVNCoreConfig;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.*;
import com.mvnforum.db.jdbc.*;

/**
 * Instance that returns the right implementation for the different
 * DAO implementation such as JDBC or Hibernate.
 *
 * @author Minh Nguyen
 * @version $Revision: 1.43 $
 */
public final class DAOFactory {

    private static final Logger log = LoggerFactory.getLogger(DAOFactory.class);

    private DAOFactory() {}

    private static MemberDAO             localMemberDAO        = null;
    private static MemberDAO             memberDAO             = null;
    private static MessageFolderDAO      messageFolderDAO      = null;
    private static MemberForumDAO        memberForumDAO        = null;
    private static MemberGroupDAO        memberGroupDAO        = null;
    private static MemberPermissionDAO   memberPermissionDAO   = null;
    private static CategoryDAO           categoryDAO           = null;
    private static ForumDAO              forumDAO              = null;
    private static FavoriteThreadDAO     favoriteThreadDAO     = null;
    private static GroupForumDAO         groupForumDAO         = null;
    private static GroupPermissionDAO    groupPermissionDAO    = null;
    private static GroupsDAO             groupsDAO             = null;
    private static AttachmentDAO         attachmentDAO         = null;
    private static ThreadDAO             threadDAO             = null;
    private static PostDAO               postDAO               = null;
    private static RankDAO               rankDAO               = null;
    private static WatchDAO              watchDAO              = null;
    private static MessageDAO            messageDAO            = null;
    private static MessageStatisticsDAO  messageStatisticsDAO  = null;
    private static PmAttachmentDAO       pmAttachmentDAO       = null;
    private static PmAttachMessageDAO    pmAttachMessageDAO    = null;

    public static MemberDAO getMemberDAO() {
        return memberDAO;
    }

    public static MemberDAO getLocalMemberDAO() {
        return localMemberDAO;
    }

    public static MessageFolderDAO getMessageFolderDAO() {
        return messageFolderDAO;
    }

    public static MemberForumDAO getMemberForumDAO() {
        return memberForumDAO;
    }

    public static MemberGroupDAO getMemberGroupDAO() {
        return memberGroupDAO;
    }

    public static MemberPermissionDAO getMemberPermissionDAO() {
        return memberPermissionDAO;
    }

    public static CategoryDAO getCategoryDAO() {
        return categoryDAO;
    }

    public static ForumDAO getForumDAO() {
        return forumDAO;
    }

    public static FavoriteThreadDAO getFavoriteThreadDAO() {
        return favoriteThreadDAO;
    }

    public static GroupForumDAO getGroupForumDAO() {
        return groupForumDAO;
    }

    public static GroupPermissionDAO getGroupPermissionDAO() {
        return groupPermissionDAO;
    }

    public static GroupsDAO getGroupsDAO() {
        return groupsDAO;
    }

    public static AttachmentDAO getAttachmentDAO() {
        return attachmentDAO;
    }

    public static ThreadDAO getThreadDAO() {
        return threadDAO;
    }

    public static PostDAO getPostDAO() {
        return postDAO;
    }

    public static RankDAO getRankDAO() {
        return rankDAO;
    }

    public static WatchDAO getWatchDAO() {
        return watchDAO;
    }

    public static MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public static MessageStatisticsDAO getMessageStatisticsDAO() {
        return messageStatisticsDAO;
    }

    public static PmAttachmentDAO getPmAttachmentDAO() {
        return pmAttachmentDAO;
    }

    public static PmAttachMessageDAO getPmAttachMessageDAO() {
        return pmAttachMessageDAO;
    }

    static {

        // please note that localMemberDAO should be instantiated first the memberDAO
        // because the memberDAO can refer to the localMemberDAO
        localMemberDAO = new MemberDAOImplJDBC();

        messageFolderDAO = new MessageFolderDAOImplJDBC();

        memberForumDAO = new MemberForumDAOImplJDBC();

        memberGroupDAO = new MemberGroupDAOImplJDBC();

        memberPermissionDAO = new MemberPermissionDAOImplJDBC();

        categoryDAO = new CategoryDAOImplJDBC();

        forumDAO = new ForumDAOImplJDBC();

        favoriteThreadDAO = new FavoriteThreadDAOImplJDBC();

        groupForumDAO = new GroupForumDAOImplJDBC();

        groupPermissionDAO = new GroupPermissionDAOImplJDBC();

        groupsDAO = new GroupsDAOImplJDBC();

        attachmentDAO = new AttachmentDAOImplJDBC();

        threadDAO = new ThreadDAOImplJDBC();

        postDAO = new PostDAOImplJDBC();

        rankDAO = new RankDAOImplJDBC();

        watchDAO = new WatchDAOImplJDBC();

        messageDAO = new MessageDAOImplJDBC();

        messageStatisticsDAO = new MessageStatisticsDAOImplJDBC();

        pmAttachmentDAO = new PmAttachmentDAOImplJDBC();

        pmAttachMessageDAO = new PmAttachMessageDAOImplJDBC();

        boolean enablePortlet = MvnCoreServiceFactory.getMvnCoreService().getEnvironmentService().isPortlet();

        //boolean dontUseCustomizedMemberClass = false;
        String memberImpl = "";

        //@todo assert here
        //assert(MVNForumFactoryConfig.getMemberManagerClassName() != null);

        if (MVNForumFactoryConfig.getMemberManagerClassName().length() > 0) {// we can be sure is not null
            memberImpl = MVNForumFactoryConfig.getMemberManagerClassName();
        } else if (enablePortlet) {
            memberImpl = Portal.getMemberImplementation(MVNCoreConfig.getPortalType());
        } else {
            log.error("Error: not config <member_implementation> properly in mvnforum.xml. Try loading the default implementation for Member.");

            memberImpl = "com.mvnforum.db.jdbc.MemberDAOImplJDBC";
            log.info("Try loading default MemberDAO = " + memberImpl);
        }

        try {
            Class c = Class.forName(memberImpl);
            memberDAO = (MemberDAO) c.newInstance();
            log.info("memberDAO = " + memberDAO);
        } catch (Exception e) {
            log.error("Error returning the DAOFactory.", e);
            log.warn("Cannot init MemberDAO [" + memberImpl + "], about to use the default implementation.");

            memberDAO = new MemberDAOImplJDBC();
        }

    }

}
