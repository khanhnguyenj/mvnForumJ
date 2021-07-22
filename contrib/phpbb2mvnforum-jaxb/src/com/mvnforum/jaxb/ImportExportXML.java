package com.mvnforum.jaxb;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.MemberDAO;
import org.mvnforum.phpbb2mvnforum.db.MessageDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbAuthAccess;
import org.mvnforum.phpbb2mvnforum.db.PhpbbAuthAccessDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbCategories;
import org.mvnforum.phpbb2mvnforum.db.PhpbbCategoriesDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbForum;
import org.mvnforum.phpbb2mvnforum.db.PhpbbForumDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbGroup;
import org.mvnforum.phpbb2mvnforum.db.PhpbbGroupDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPost;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostText;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPostTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgs;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbPrivmMsgsTextDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbRanks;
import org.mvnforum.phpbb2mvnforum.db.PhpbbRanksDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopics;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsWatch;
import org.mvnforum.phpbb2mvnforum.db.PhpbbTopicsWatchDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserGroup;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUserGroupDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUsers;
import org.mvnforum.phpbb2mvnforum.db.jdbc.DAOFactory;
import org.mvnforum.util.MD5;
import org.mvnforum.util.Utils;

import com.mvnforum.db.GroupPermissionDAO;
import com.mvnforum.db.GroupsDAO;
import com.mvnforum.db.MemberPermissionDAO;
import com.mvnforum.db.MessageFolderDAO;
import com.mvnforum.db.RankDAO;
import com.mvnforum.db.WatchDAO;
import com.mvnforum.jaxb.dao.CategoryListDAO;
import com.mvnforum.jaxb.dao.GroupListDAO;
import com.mvnforum.jaxb.dao.MemberListDAO;
import com.mvnforum.jaxb.dao.RankListDAO;
import com.mvnforum.jaxb.db.AttachmentList;
import com.mvnforum.jaxb.db.CategoryListType;
import com.mvnforum.jaxb.db.CategoryType;
import com.mvnforum.jaxb.db.CategoryWatchList;
import com.mvnforum.jaxb.db.FavoriteThreadList;
import com.mvnforum.jaxb.db.ForumList;
import com.mvnforum.jaxb.db.ForumType;
import com.mvnforum.jaxb.db.ForumWatchList;
import com.mvnforum.jaxb.db.GlobalPermissionList;
import com.mvnforum.jaxb.db.GlobalWatchList;
import com.mvnforum.jaxb.db.GlobalWatchType;
import com.mvnforum.jaxb.db.GroupForumPermissionList;
import com.mvnforum.jaxb.db.GroupForumPermissionType;
import com.mvnforum.jaxb.db.GroupMemberList;
import com.mvnforum.jaxb.db.GroupMemberType;
import com.mvnforum.jaxb.db.GroupType;
import com.mvnforum.jaxb.db.MemberForumPermissionList;
import com.mvnforum.jaxb.db.MemberType;
import com.mvnforum.jaxb.db.MessageFolderList;
import com.mvnforum.jaxb.db.MessageFolderType;
import com.mvnforum.jaxb.db.MessageList;
import com.mvnforum.jaxb.db.MessageType;
import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.ObjectFactory;
import com.mvnforum.jaxb.db.PostList;
import com.mvnforum.jaxb.db.PostType;
import com.mvnforum.jaxb.db.RankType;
import com.mvnforum.jaxb.db.ThreadList;
import com.mvnforum.jaxb.db.ThreadType;
import com.mvnforum.jaxb.db.ThreadWatchList;
import com.mvnforum.jaxb.db.ThreadWatchType;
import com.mvnforum.jaxb.db.MvnforumType.GroupListType;
import com.mvnforum.jaxb.db.MvnforumType.MemberListType;
import com.mvnforum.jaxb.db.MvnforumType.RankListType;
import com.mvnforum.jaxb.util.ImportExportUtil;
import com.mvnforum.jaxb.util.XMLUtil;

public class ImportExportXML {

    private Mvnforum mvnforum = null;

    private static DAOFactory factory = new DAOFactory();

    private static int GROUP_ADMIN_PERMISSION = 100;

    private static int GROUP_GUEST_PERMISSION = 109;

    private static int ADMIN_ID = 1;

    private static int GUEST_ID = 2;

    public ImportExportXML()
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        mvnforum = objectFactory.createMvnforum();
    }

    public void exportGroupList()
        throws JAXBException, DatabaseException, ObjectNotFoundException {
        GroupListDAO groupListDAO = new GroupListDAO();
        PhpbbGroupDAO phpbbGroupDAO = factory.getPhpbbGroupDAO();
        PhpbbUserGroupDAO phpbbUserGroupDAO = factory.getPhpbbUserGroupDAO();
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();

        Collection beans = phpbbGroupDAO.getBeans();
        Collection groupTypes = new ArrayList();

        for (Iterator groupIter = beans.iterator(); groupIter.hasNext();) {
            PhpbbGroup phpbbGroup = (PhpbbGroup) groupIter.next();
            int groupID = phpbbGroup.getgroup_id();
            String groupName = phpbbGroup.getgroup_name();
            String groupOwnerName = "";
            if (!(groupName.equalsIgnoreCase("Anonymous") || groupName.equalsIgnoreCase("Admin"))) {
                int groupOwnerID = phpbbGroup.getgroup_moderator();
                if (groupOwnerID == 0)
                    continue;
                groupOwnerName = phpbbUserDAO.getUserNameFromUserID(groupOwnerID);
            }
            int groupOption = 0;
            String groupDesc = phpbbGroup.getgroup_description();

            Collection userGroupBeans = phpbbUserGroupDAO.getBeansByGroupID(groupID);

            Collection groupMemberTypes = new ArrayList();
            for (Iterator userGroupIter = userGroupBeans.iterator(); userGroupIter.hasNext();) {
                PhpbbUserGroup phpbbUserGroup = (PhpbbUserGroup) userGroupIter.next();
                int memberID = phpbbUserGroup.getuser_id();
                String memberName = phpbbUserDAO.getUserNameFromUserID(memberID);

                int privilege = 0;
                String creationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
                String modifiedDate = creationDate;
                GroupMemberType groupMemberType = groupListDAO.getGroupMemberType(memberName, privilege, creationDate,
                        modifiedDate);
                groupMemberTypes.add(groupMemberType);
            }
            GroupMemberList groupMemberList = groupListDAO.getGroupMemberList(groupMemberTypes);

            //phpbb doesn't has globalPermission for Group, so I'll set permission only for Admin's group and 
            //Guest's group
            Collection globalPermissionList = new ArrayList();
            if (groupName.equalsIgnoreCase("Anonymous")) {
                globalPermissionList.add(new Integer(GROUP_GUEST_PERMISSION));
            }
            if (groupName.equalsIgnoreCase("Admin")) {
                globalPermissionList.add(new Integer(GROUP_ADMIN_PERMISSION));
            }
            GlobalPermissionList globalPermissionLists = groupListDAO.getGlobalPermissionList(globalPermissionList);
            //////////////////////////////////
            String groupCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
            String groupModifiedDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
            GroupType groupType = groupListDAO.getGroupType(groupName, groupDesc, groupOption, groupOwnerName,
                    groupCreationDate, groupModifiedDate, globalPermissionLists, groupMemberList);
            groupTypes.add(groupType);
        }

        GroupListType groupListType = groupListDAO.getGroupListType(groupTypes);
        mvnforum.setGroupList(groupListType);
    }

    /*
     * TODO because rank in phpbb differ with rank in mvnforumRank, so i don't export rank.
     * I'll use default rank in mvnforum, so that admin must config rank.
     */
    public void exportRankList()
        throws JAXBException, DatabaseException {
        RankListDAO rankListDAO = new RankListDAO();
        PhpbbRanksDAO phpbbRanksDAO = factory.getPhpbbRanksDAO();

        Collection phpbbRanks = phpbbRanksDAO.getBeans();
        Collection rankTypes = new ArrayList();

        for (Iterator phpbbrankIter = phpbbRanks.iterator(); phpbbrankIter.hasNext();) {
            PhpbbRanks phpbbRank = (PhpbbRanks) phpbbrankIter.next();

            int rankMinposts = phpbbRank.getrank_min();
            int rankLevel = 0;
            String rankTitle = phpbbRank.getrank_title();
            String rankImage = phpbbRank.getrank_image();
            int rankType1 = 0;
            int rankOption = 0;

            RankType rankType = rankListDAO.getRankType(rankMinposts, rankLevel, rankTitle, rankImage, rankType1,
                    rankOption);
            rankTypes.add(rankType);
        }
        RankListType rankListType = rankListDAO.getRankListType(rankTypes);
        mvnforum.setRankList(rankListType);
    }

    public void exportCategoryList()
        throws JAXBException, DatabaseException, ObjectNotFoundException {
        PhpbbCategoriesDAO phpbbCategoriesDAO = factory.getPhpbbCategoriesDAO();
        PhpbbForumDAO phpbbForumDAO = factory.getPhpbbForumDAO();
        PhpbbPostDAO phpbbPostDAO = factory.getPhpbbPostDAO();
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();
        PhpbbTopicsDAO phpbbTopicsDAO = factory.getPhpbbTopicsDAO();
        PhpbbPostTextDAO phpbbPostTextDAO = factory.getPhpbbPostTextDAO();
        PhpbbTopicsWatchDAO phpbbTopicsWatchDAO = factory.getPhpbbTopicsWatchDAO();
        PhpbbAuthAccessDAO phpbbAuthAccessDAO = factory.getPhpbbAuthAccessDAO();
        PhpbbGroupDAO phpbbGroupDAO = factory.getPhpbbGroupDAO();

        CategoryListDAO categoryListDAO = new CategoryListDAO();
        Collection phpbbCategoriesBeans = phpbbCategoriesDAO.getBeans();
        Collection categoryTypes = new ArrayList();

        for (Iterator iter = phpbbCategoriesBeans.iterator(); iter.hasNext();) {
            PhpbbCategories phpbbCategories = (PhpbbCategories) iter.next();
            int catID = phpbbCategories.getCatId();

            Collection forumTypes = new ArrayList();
            Collection phpbbForumBeans = phpbbForumDAO.getBeansByCategoryID(catID);
            for (Iterator forumIter = phpbbForumBeans.iterator(); forumIter.hasNext();) {
                PhpbbForum phpbbForum = (PhpbbForum) forumIter.next();
                int forumID = phpbbForum.getforum_id();

                Collection threadTypes = new ArrayList();
                Collection phpbbTopicBeans = phpbbTopicsDAO.getBeansByForumID(forumID);
                for (Iterator threadIter = phpbbTopicBeans.iterator(); threadIter.hasNext();) {
                    PhpbbTopics bean = (PhpbbTopics) threadIter.next();
                    int threadID = bean.gettopic_id();

                    Collection postLists = new ArrayList();
                    Collection phpbbPostBeans = phpbbPostDAO.getBeansByThreadID(threadID);
                    for (Iterator postIter = phpbbPostBeans.iterator(); postIter.hasNext();) {
                        PhpbbPost phpbbPost = (PhpbbPost) postIter.next();
                        int postID = phpbbPost.getpost_id();

                        //no information about Attach in phpbb
                        Collection attachmentTypes = new ArrayList();
                        //attachmentTypes.add(attachmentType);
                        AttachmentList attachmentList = categoryListDAO.getAttachmentList(attachmentTypes);

                        int memberID = phpbbPost.getposter_id();
                        Collection ppidrs = phpbbPostDAO.getPostIDsFromTopicID(threadID);
                        int ppid = 0;
                        for (Iterator ppidrsIter = ppidrs.iterator(); ppidrsIter.hasNext();) {
                            PhpbbPost phpbbPost_Top = (PhpbbPost) ppidrsIter.next();
                            int ppidtmp = phpbbPost_Top.getpost_id();
                            if (ppidtmp == postID)
                                break;
                            else
                                ppid = ppidtmp;
                        }
    
                        String memberName = phpbbUserDAO.getUserNameFromUserID(memberID);

                        String lastEditMemberName = memberName;
                        PhpbbPostText phpbbPostText = phpbbPostTextDAO.getBean(postID);
                        String postTopic = ImportExportUtil.wrapit(phpbbPostText.getpost_subject());
                        String postBody = ImportExportUtil.wrapit(phpbbPostText.getpost_text());

                        String postCreationDate = ImportExportUtil.dateTimeFormat((long) phpbbPost.getpost_time());
                        String postLastEditDate = postCreationDate;
                        String postCreationIP = ImportExportUtil.wrapit(ImportExportUtil.HexIPtoString(phpbbPost
                                .getposter_ip()));
                        String postLastEditIP = postCreationIP;
                        int postEditCount = phpbbPost.getpost_edit_count();
                        int postFormatOption = 0;
                        int postOption = 0;
                        int postStatus = 0;
                        String postIcon = "";
                        int postAttachCount = 0;

                        PostType postType = categoryListDAO.getPostType(memberName, lastEditMemberName, postTopic,
                                postBody, postCreationDate, postLastEditDate, postCreationIP, postLastEditIP,
                                postEditCount, postFormatOption, postOption, postStatus, postIcon, postAttachCount,
                                attachmentList, categoryListDAO.getPortList());
                        postLists.add(postType);
                    }
                    PostList postList = categoryListDAO.getPostList(postLists);
                    ///////////////////////////

                    //no information about favoriteThreadTypes in phpbb
                    Collection favoriteThreadTypes = new ArrayList();
                    FavoriteThreadList favoriteThreadList = categoryListDAO.getFavoriteThreadList(favoriteThreadTypes);
                    //////////////////////////

                    //phpbb contains only thread_watch, category_watch and forum_watch can find out by thread_id 
                    Collection threadWatchTypes = new ArrayList();
                    Collection phpbbTopicWatchs = phpbbTopicsWatchDAO.getBeansByTopicID(threadID);
                    for (Iterator topicWatchIter = phpbbTopicWatchs.iterator(); topicWatchIter.hasNext();) {
                        PhpbbTopicsWatch phpbbTopicsWatch = (PhpbbTopicsWatch) topicWatchIter.next();
                        int memberID = phpbbTopicsWatch.getuser_id();
                        String memberName = phpbbUserDAO.getUserNameFromUserID(memberID);
                        int watchType = 0;
                        int watchOption = 0;
                        int watchStatus = 0;
                        String watchCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
                        String watchLastSentDate = watchCreationDate;
                        String watchEndDate = watchCreationDate;

                        ThreadWatchType threadWatchType = categoryListDAO.getThreadWatchType(memberName, watchType,
                                watchOption, watchStatus, watchCreationDate, watchLastSentDate, watchEndDate);
                        threadWatchTypes.add(threadWatchType);
                    }
                    ThreadWatchList threadWatchList = categoryListDAO.getThreadWatchList(threadWatchTypes);

                    int memberID = bean.gettopic_poster();
                    String memberName = memberName = phpbbUserDAO.getUserNameFromUserID(memberID);

                    String lastPostMemberName = lastPostMemberName = phpbbUserDAO.getUserNameFromUserID(memberID);
                    String threadTopic = bean.gettopic_title();

                    String threadBody = "";
                    int first_post_id = bean.gettopic_first_post_id();
                    try {
                        threadBody = phpbbPostTextDAO.getPostTextFromPostID(first_post_id);
                    } catch (ObjectNotFoundException oe) {
                    }
                    threadBody = ImportExportUtil.wrapit(ImportExportUtil.stripPHPBBQuotes(threadBody));

                    int threadVoteCount = 0;
                    int threadVoteTotalStars = 0;
                    String threadCreationDate = ImportExportUtil.dateTimeFormat((int) bean.gettopic_time());

                    String threadLastPostDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
                    int last_post_id = bean.gettopic_last_post_id();
                    try {
                        threadLastPostDate = ImportExportUtil.dateTimeFormat(phpbbPostDAO
                                .getPostTimeFromPostID(last_post_id));
                    } catch (ObjectNotFoundException oe) {
                    }

                    int threadType1 = 0;
                    int threadOption = 0;
                    int threadStatus = bean.gettopic_status();
                    int threadHasPoll = bean.gettopic_vote();
                    int threadViewCount = bean.gettopic_views();
                    int threadReplyCount = bean.gettopic_replies();
                    String threadIcon = "";
                    int threadDuration = 0;
                    //int threadAttachCount = 0;
                    ThreadType threadType = categoryListDAO.getThreadType(memberName, lastPostMemberName, threadTopic,
                            threadBody, threadVoteCount, threadVoteTotalStars, threadCreationDate, threadLastPostDate,
                            threadType1, threadOption, threadStatus, threadHasPoll, threadViewCount, threadReplyCount,
                            threadIcon, threadDuration, threadWatchList, favoriteThreadList, postList);
                    threadTypes.add(threadType);
                }
                ThreadList threadList = categoryListDAO.getThreadList(threadTypes);
                /////////////////////////////
                //phpbb contains only threadWatch.
                Collection forumWatchLists = new ArrayList();
                ForumWatchList forumWatchList = categoryListDAO.getForumWatchList(forumWatchLists);
                /////////////////////////////
                
                Collection groupForumPermissionTypes = new ArrayList();
                Collection phpbbAuthAccesses = phpbbAuthAccessDAO.getBeansByForumID(forumID);
                for (Iterator authIter = phpbbAuthAccesses.iterator(); authIter.hasNext(); ) {
                    PhpbbAuthAccess phpbbAuthAccess = (PhpbbAuthAccess) authIter.next();
                    
                    int groupID = phpbbAuthAccess.getgroup_id();
                    PhpbbGroup phpbbGroup = phpbbGroupDAO.getTruthGroup(groupID);
                    if (phpbbGroup == null) continue;
                    String groupName = phpbbGroup.getgroup_name();
                    
                    if (phpbbAuthAccess.getauth_mod() == 1) {
                        GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType(groupName, 106);
                        groupForumPermissionTypes.add(groupForumPermissionType);
                    }
                    
                    if (phpbbAuthAccess.getauth_edit() == 1) {
                        GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType(groupName, 2000);
                        GroupForumPermissionType groupForumPermissionType1 = categoryListDAO.getGroupForumPermissionType(groupName, 2103);
                        groupForumPermissionTypes.add(groupForumPermissionType);
                        groupForumPermissionTypes.add(groupForumPermissionType1);
                    }
                    
                    if (phpbbAuthAccess.getauth_delete() == 1) {
                        GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType(groupName, 2001);
                        GroupForumPermissionType groupForumPermissionType1 = categoryListDAO.getGroupForumPermissionType(groupName, 2104);
                        groupForumPermissionTypes.add(groupForumPermissionType);
                        groupForumPermissionTypes.add(groupForumPermissionType1);
                    }
                    
                    if (phpbbAuthAccess.getauth_pollcreate() == 1) {
                        GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType(groupName, 2105);
                        GroupForumPermissionType groupForumPermissionType1 = categoryListDAO.getGroupForumPermissionType(groupName, 2106);
                        GroupForumPermissionType groupForumPermissionType2 = categoryListDAO.getGroupForumPermissionType(groupName, 2107);
                        groupForumPermissionTypes.add(groupForumPermissionType);
                        groupForumPermissionTypes.add(groupForumPermissionType1);
                        groupForumPermissionTypes.add(groupForumPermissionType2);
                    }
                    
                    if (phpbbAuthAccess.getauth_attachments() == 1) {
                        GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType(groupName, 2109);
                        GroupForumPermissionType groupForumPermissionType1 = categoryListDAO.getGroupForumPermissionType(groupName, 2108);
                        groupForumPermissionTypes.add(groupForumPermissionType);
                        groupForumPermissionTypes.add(groupForumPermissionType1);
                    }
                    
                    /*GroupForumPermissionType groupForumPermissionType = categoryListDAO.getGroupForumPermissionType("Administrator", 12);
                    groupForumPermissionTypes.add(groupForumPermissionType);*/
                }
                
                GroupForumPermissionList groupForumPermissionList = categoryListDAO
                        .getGroupForumPermissionList(groupForumPermissionTypes);
                /////////////////////////////
                Collection memberForumPermissionTypes = new ArrayList();
                /*MemberForumPermissionType memberForumPermissionType = categoryListDAO.getMemberForumPermissionType("admin", 12);
                 memberForumPermissionTypes.add(memberForumPermissionType);*/
                MemberForumPermissionList memberForumPermissionList = categoryListDAO
                        .getMemberForumPermissionList(memberForumPermissionTypes);
                /////////////////////////////
                //int categoryID = phpbbForum.getcat_id();
                int last_post_id = phpbbForum.getforum_last_post_id();
                if (last_post_id == 0) {
                    continue;
                }
                String lastPostMemberName = phpbbUserDAO.getUserNameFromUserID(phpbbPostDAO
                        .getPosterIDFromPostID(last_post_id));
                /*if (last_post_id == PHPBB_ADMIN_ID) {
                    lastPostMemberName = "Admin";
                } else {
                    if (last_post_id == PHPBB_GUEST_ID) {
                        lastPostMemberName = "Guest";
                    } else {
                        lastPostMemberName = phpbbUserDAO.getUserNameFromUserID(phpbbPostDAO
                                .getPosterIDFromPostID(last_post_id));
                    }
                }*/
                String forumName = phpbbForum.getforum_name();
                String forumDesc = phpbbForum.getforum_desc();
                String forumCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
                String forumModifiedDate = forumCreationDate;
                String forumLastPostDate = forumCreationDate;
                int forumOrder = phpbbForum.getforum_order();
                //int forumType1 = 0;
                int forumFormatOption = 0;
                int forumOption = 0;
                int forumStatus = phpbbForum.getforum_status();
                int forumModerationMode = 0;
                String forumPassword = "";
                int forumThreadCount = phpbbForum.getforum_topics();
                int forumPostCount = phpbbForum.getforum_posts();

                ForumType forumType = categoryListDAO.getForumType(lastPostMemberName, forumName, forumDesc,
                        forumCreationDate, forumModifiedDate, forumLastPostDate, forumOrder, forumFormatOption,
                        forumOption, forumStatus, forumModerationMode, forumPassword, forumThreadCount, forumPostCount,
                        memberForumPermissionList, groupForumPermissionList, forumWatchList, threadList);
                forumTypes.add(forumType);

            }

            ForumList forumList = categoryListDAO.getForumList(forumTypes);
            ///////////////////////////////////////////
            Collection categoryWatchs = new ArrayList();
            //categoryWatchs.add("CategoryWatchList");
            CategoryWatchList categoryWatchList = categoryListDAO.getCategoryWatchList(categoryWatchs);
            ///////////////////////////////////////////
            //int parentCategoryID = 0;
            String categoryName = phpbbCategories.getCatTitle();
            String categoryDesc = "";
            String categoryCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
            String categoryModifiedDate = categoryCreationDate;
            int categoryOrder = phpbbCategories.getCatOrder();
            int categoryOption = 0;
            int categoryStatus = 0;

            CategoryType categoryType = categoryListDAO.getCategoryType(categoryName, categoryDesc,
                    categoryCreationDate, categoryModifiedDate, categoryOrder, categoryOption, categoryStatus,
                    categoryWatchList, forumList, categoryListDAO.getCategoryListType());
            categoryTypes.add(categoryType);
        }
        CategoryListType categoryListType = categoryListDAO.getCategoryListType(categoryTypes);

        mvnforum.setCategoryList(categoryListType);
    }

    public void exportMemberList()
        throws DatabaseException, JAXBException, ObjectNotFoundException {
        PhpbbUserDAO phpbbUserDAO = factory.getPhpUserDAO();
        PhpbbPrivmMsgsDAO phpbbPrivmMsgsDAO = factory.getPhpbbPrivmMsgsDAO();
        PhpbbTopicsWatchDAO phpbbTopicsWatchDAO = factory.getPhpbbTopicsWatchDAO();
        PhpbbPrivmMsgsTextDAO phpbbPrivmMsgsTextDAO = factory.getPhpbbPrivmMsgsTextDAO();

        MemberListDAO memberDAO = new MemberListDAO();

        Collection phpbbUserBeans = phpbbUserDAO.getBeans();
        Collection memberTypes = new ArrayList();

        for (Iterator iter = phpbbUserBeans.iterator(); iter.hasNext();) {
            PhpbbUsers bean = (PhpbbUsers) iter.next();
            int memberID = bean.getUserId();
            String memberName = bean.getUsername();
            ////////////////////////////////
            Collection globalWatchLists = new ArrayList();
            Collection phpbbTopicWatchs = phpbbTopicsWatchDAO.getBeans(memberID, 0);
            for (Iterator topicWatchIter = phpbbTopicWatchs.iterator(); topicWatchIter.hasNext();) {
                int watchType = 0;
                int watchOption = 0;
                int watchStatus = 0;
                String watchCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
                String watchLastSentDate = watchCreationDate;
                String watchEndDate = watchCreationDate;

                GlobalWatchType globalWatchType = memberDAO.getGlobalWatchType(watchType, watchOption, watchStatus,
                        watchCreationDate, watchLastSentDate, watchEndDate);
                globalWatchLists.add(globalWatchType);
            }
            GlobalWatchList globalWatchList = memberDAO.getGlobalWatchList(globalWatchLists);
            ////////////////////////////////
            Collection messageFolders = new ArrayList();
            //phpbb don't allow user create new message folder, so every user has 4 message folder Inbox, Sent, Out,
            //Save

            //Inbox contains message receive, don't save
            String folderCreationDate = ImportExportUtil.dateTimeFormat(new Timestamp(0));
            String folderModifiedDate = folderCreationDate;

            Collection messageTypesInSaveBox = new ArrayList();
            Collection saveBoxMessages = phpbbPrivmMsgsDAO.getBeansByType(3, memberID);
            for (Iterator receiveIter = saveBoxMessages.iterator(); receiveIter.hasNext();) {
                PhpbbPrivmMsgs phpbbPrivmMsgs = (PhpbbPrivmMsgs) receiveIter.next();
                int messageID = phpbbPrivmMsgs.getprivmsgs_id();
                int receiverID = phpbbPrivmMsgs.getprivmsgs_from_userid();
                String messageSenderName = phpbbUserDAO.getUserNameFromUserID(receiverID);
                String messageToList = memberName;
                String folderName = "SaveBox";
                String messageCcList = "";
                String messageBccList = "";
                String messageTopic = Utils.wrapIt(phpbbPrivmMsgs.getprivmsgs_subject());
                String messageBody = Utils.wrapIt(phpbbPrivmMsgsTextDAO.getBean(messageID).getprivmsgs_text());
                int messageType = 0;
                int messageOption = 0;
                int messageStatus = 0;
                int messageReadStatus = 1;
                int messageNotify = 0;
                String messageIcon = "";
                int messageAttachCount = 0;
                String messageIP = Utils.wrapIt(Utils.HexIPtoString(phpbbPrivmMsgs.getprivmsgs_ip()));
                String messageCreationDate = ImportExportUtil.dateTimeFormat(phpbbPrivmMsgs.getprivmsgs_date());

                MessageType messageType2 = memberDAO.getMessageType(folderName, messageSenderName, messageToList,
                        messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                        messageStatus, messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                        messageCreationDate);
                messageTypesInSaveBox.add(messageType2);
            }
            MessageList messageListInSaveBox = memberDAO.getMessageList(messageTypesInSaveBox);

            MessageFolderType inboxFolder = memberDAO.getMessageFolderType("SaveBox", 0, folderCreationDate,
                    folderModifiedDate, messageListInSaveBox);
            messageFolders.add(inboxFolder);

            //SentBox contains all of messages has type = 2. Sent box contains messages you sent to your contact and he 
            //has read it
            Collection messageTypesInSentBox = new ArrayList();
            Collection sentBoxMessages = phpbbPrivmMsgsDAO.getBeansByTypeAndReceiveUser(2, memberID);
            for (Iterator receiveIter = sentBoxMessages.iterator(); receiveIter.hasNext();) {
                PhpbbPrivmMsgs phpbbPrivmMsgs = (PhpbbPrivmMsgs) receiveIter.next();
                int messageID = phpbbPrivmMsgs.getprivmsgs_id();
                String folderName = "SentBox";
                String messageSenderName = memberName;
                int receiverID = phpbbPrivmMsgs.getprivmsgs_from_userid();
                String messageToList = phpbbUserDAO.getUserNameFromUserID(receiverID);
                String messageCcList = "";
                String messageBccList = "";
                String messageTopic = Utils.wrapIt(phpbbPrivmMsgs.getprivmsgs_subject());
                String messageBody = Utils.wrapIt(phpbbPrivmMsgsTextDAO.getBean(messageID).getprivmsgs_text());
                int messageType = 0;
                int messageOption = 0;
                int messageStatus = 0;
                int messageReadStatus = 1;
                int messageNotify = 0;
                String messageIcon = "";
                int messageAttachCount = 0;
                String messageIP = Utils.wrapIt(Utils.HexIPtoString(phpbbPrivmMsgs.getprivmsgs_ip()));
                String messageCreationDate = ImportExportUtil.dateTimeFormat(phpbbPrivmMsgs.getprivmsgs_date());

                MessageType messageType2 = memberDAO.getMessageType(folderName, messageSenderName, messageToList,
                        messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                        messageStatus, messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                        messageCreationDate);
                messageTypesInSentBox.add(messageType2);
            }
            MessageList messageListInSentBox = memberDAO.getMessageList(messageTypesInSentBox);
            //SentBox contains message you sent and your contact has read it.
            MessageFolderType sendFolder = memberDAO.getMessageFolderType("SentBox", 1, folderCreationDate,
                    folderModifiedDate, messageListInSentBox);
            messageFolders.add(sendFolder);

            Collection messageTypesInOutBox = new ArrayList();
            //OutBox contains all of messages has type = 1 or 5. OutBox contains messages you sent to your contact, and 
            //your contact hasn't read it.  
            Collection outBoxMessage = phpbbPrivmMsgsDAO.getBeansByTypeAndReceiveUser(1, memberID);
            Collection outBoxMessage1 = phpbbPrivmMsgsDAO.getBeansByTypeAndReceiveUser(5, memberID);

            for (Iterator iter1 = outBoxMessage1.iterator(); iter1.hasNext();) {
                outBoxMessage.add(iter1.next());
            }

            for (Iterator receiveIter = outBoxMessage.iterator(); receiveIter.hasNext();) {
                PhpbbPrivmMsgs phpbbPrivmMsgs = (PhpbbPrivmMsgs) receiveIter.next();
                int messageID = phpbbPrivmMsgs.getprivmsgs_id();
                String folderName = "OutBox";
                String messageSenderName = memberName;
                int receiverID = phpbbPrivmMsgs.getprivmsgs_from_userid();
                String messageToList = phpbbUserDAO.getUserNameFromUserID(receiverID);
                String messageCcList = "";
                String messageBccList = "";
                String messageTopic = Utils.wrapIt(phpbbPrivmMsgs.getprivmsgs_subject());
                String messageBody = Utils.wrapIt(phpbbPrivmMsgsTextDAO.getBean(messageID).getprivmsgs_text());
                int messageType = 0;
                int messageOption = 0;
                int messageStatus = 0;
                int messageReadStatus = 0;
                int messageNotify = 0;
                String messageIcon = "";
                int messageAttachCount = 0;
                String messageIP = Utils.wrapIt(Utils.HexIPtoString(phpbbPrivmMsgs.getprivmsgs_ip()));
                String messageCreationDate = ImportExportUtil.dateTimeFormat(phpbbPrivmMsgs.getprivmsgs_date());

                MessageType messageType2 = memberDAO.getMessageType(folderName, messageSenderName, messageToList,
                        messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                        messageStatus, messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                        messageCreationDate);
                messageTypesInOutBox.add(messageType2);
            }
            MessageList messageListInOutBox = memberDAO.getMessageList(messageTypesInOutBox);
            //Out box contains message sent so outBox Order must be 2
            MessageFolderType draftFolder = memberDAO.getMessageFolderType("OutBox", 2, folderCreationDate,
                    folderModifiedDate, messageListInOutBox);
            messageFolders.add(draftFolder);

            Collection messageTypesInInBox = new ArrayList();
            //InBox contains all of messages has type = 1 or 5 or 0
            Collection inBoxMessage = phpbbPrivmMsgsDAO.getBeansByType(1, memberID);
            Collection inboxMessage1 = phpbbPrivmMsgsDAO.getBeansByType(5, memberID);
            Collection inboxMessage2 = phpbbPrivmMsgsDAO.getBeansByType(0, memberID);

            for (Iterator iter1 = inboxMessage1.iterator(); iter1.hasNext();) {
                inBoxMessage.add(iter1.next());
            }

            for (Iterator iter1 = inboxMessage2.iterator(); iter1.hasNext();) {
                inBoxMessage.add(iter1.next());
            }

            outBoxMessage.add(phpbbPrivmMsgsDAO.getBeansByType(5, memberID));
            outBoxMessage.add(phpbbPrivmMsgsDAO.getBeansByType(0, memberID));
            for (Iterator receiveIter = inBoxMessage.iterator(); receiveIter.hasNext();) {
                PhpbbPrivmMsgs phpbbPrivmMsgs = (PhpbbPrivmMsgs) receiveIter.next();
                int messageID = phpbbPrivmMsgs.getprivmsgs_id();
                String folderName = "InBox";
                int receiverID = phpbbPrivmMsgs.getprivmsgs_from_userid();
                String messageSenderName = phpbbUserDAO.getUserNameFromUserID(receiverID);
                String messageToList = memberName;
                String messageCcList = "";
                String messageBccList = "";
                String messageTopic = Utils.wrapIt(phpbbPrivmMsgs.getprivmsgs_subject());
                String messageBody = Utils.wrapIt(phpbbPrivmMsgsTextDAO.getBean(messageID).getprivmsgs_text());
                int messageType = 0;
                int messageOption = 0;
                int messageStatus = 0;
                int messageRead = phpbbPrivmMsgs.getprivmsgs_type();
                int messageReadStatus = (messageRead == 5) || (messageRead == 1) ? 0 : 1;
                int messageNotify = 0;
                String messageIcon = "";
                int messageAttachCount = 0;
                String messageIP = Utils.wrapIt(Utils.HexIPtoString(phpbbPrivmMsgs.getprivmsgs_ip()));
                String messageCreationDate = ImportExportUtil.dateTimeFormat(phpbbPrivmMsgs.getprivmsgs_date());

                MessageType messageType2 = memberDAO.getMessageType(folderName, messageSenderName, messageToList,
                        messageCcList, messageBccList, messageTopic, messageBody, messageType, messageOption,
                        messageStatus, messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                        messageCreationDate);
                messageTypesInInBox.add(messageType2);
            }
            MessageList messageListInInBox = memberDAO.getMessageList(messageTypesInInBox);
            //SaveBox contains message save
            MessageFolderType trashFolder = memberDAO.getMessageFolderType("InBox", 3, folderCreationDate,
                    folderModifiedDate, messageListInInBox);
            messageFolders.add(trashFolder);

            MessageFolderList messageFolderList = memberDAO.getMessageFolderList(messageFolders);
            ////////////////////////////////
            Collection globalPermissionLists = new ArrayList();
            int userLevel = bean.getUserLevel();
            //if user is admin user
            if (userLevel == 1) {
                globalPermissionLists.add(new Integer(100));
            }
            if (userLevel == 2) {
                globalPermissionLists.add(new Integer(106));
            }
            GlobalPermissionList globalPermissionList = memberDAO.getGlobalPermissionList(globalPermissionLists);
            ////////////////////////////////

            String memberPassword = bean.getUserPassword();
            if (!memberPassword.equals("")) {
                memberPassword = ImportExportUtil.wrapit(MD5.getMD5_Base64(memberPassword));
            }
            String memberFirstEmail = ImportExportUtil.wrapit(bean.getUserEmail());
            String memberEmail = memberFirstEmail;

            int memberEmailVisible = bean.getUserViewemail();
            // Keep name invisible (no equivalent in phpbb)
            int memberNameVisible = 0;
            String memberFirstIP = "0.0.0.0";
            String memberLastIP = "0.0.0.0";
            int memberViewCount = 0;
            int memberPostCount = bean.getUserPosts();
            String memberCreationDate = ImportExportUtil.dateTimeFormat(bean.getUserRegdate());
            String memberModifiedDate = memberCreationDate;
            String memberExpireDate = memberCreationDate;
            String memberLastLogon = ImportExportUtil.dateTimeFormat(bean.getUserLastvisit());
            int memberOption = 0;
            int memberStatus = 0;
            String memberActivateCode = "";
            String memberTempPassword = "";
            int memberMessageCount = 0;
            int memberMessageOption = 0;
            int memberPostsPerPage = 10; //Default by mvnForum.
            int memberWarnCount = 0;
            int memberVoteCount = 0;
            int memberVoteTotalStars = 0;
            int memberRewardPoints = 0;
            String memberTitle = "";

            double tzone = bean.getUserTimezone().doubleValue();

            String memberSignature = ImportExportUtil.wrapit(bean.getUserSig());
            String memberAvatar = ImportExportUtil.wrapit(bean.getUserAvatar());
            String memberSkin = "";
            //replace default language 

            //String memberLanguage = bean.getUserLang();
            String memberLanguage = "en";

            //PHPBB does not store names
            String memberFirstname = memberName;
            //because mvnforum doesn't allow empty name
            String memberLastname = "";

            //PHPBB does not store gender. This can be an issue. Everyone will be made a female
            int memberGender = 0;
            String memberBirthday = ImportExportUtil.dateTimeFormat(new Timestamp(0));
            String memberAddress = bean.getUserFrom();
            String memberCity = "";
            String memberState = "";
            String memberCountry = "";
            String memberPhone = "";
            String memberMobile = "";
            String memberFax = "";
            String memberCareer = ImportExportUtil.wrapit(bean.getUserOcc());
            String memberHomepage = ImportExportUtil.wrapit(bean.getUserWebsite());
            String memberYahoo = ImportExportUtil.wrapit(bean.getUserYim());
            String memberAol = ImportExportUtil.wrapit(bean.getUserAim());
            String memberIcq = "";
            String memberMsn = ImportExportUtil.wrapit(bean.getUserMsnm());
            // No interests collumn in MvnForum. We will just put the interests in CoolLink1 so that
            // no data is lost. 
            String memberCoolLink1 = ImportExportUtil.wrapit(bean.getUserInterests());
            String memberCoolLink2 = "";

            MemberType memberType = memberDAO.getMemberList(memberName, memberPassword, memberFirstEmail, memberEmail,
                    memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                    memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                    memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                    memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount, memberVoteTotalStars,
                    memberRewardPoints, memberTitle, tzone, memberSignature, memberAvatar, memberSkin, memberLanguage,
                    memberFirstname, memberLastname, memberGender, memberBirthday, memberAddress, memberCity,
                    memberState, memberCountry, memberPhone, memberMobile, memberFax, memberCareer, memberHomepage,
                    memberYahoo, memberAol, memberIcq, memberMsn, memberCoolLink1, memberCoolLink2,
                    globalPermissionList, messageFolderList, globalWatchList);

            memberTypes.add(memberType);
        }
        MemberListType memberListType = memberDAO.getMemberListType(memberTypes);
        mvnforum.setMemberList(memberListType);
    }

    public void importRank()
        throws JAXBException, CreateException, DatabaseException, DuplicateKeyException {
        RankListDAO rankListDAO = new RankListDAO();
        RankDAO rankDAO = factory.getRankDAO();
        List rankTypes = rankListDAO.importRankType();
        for (int i = 0; i < rankTypes.size(); i++) {
            RankType rankType = (RankType) rankTypes.get(i);

            int rankMinPosts = rankType.getRankMinPosts();
            int rankLevel = rankType.getRankLevel();
            String rankTitle = rankType.getRankTitle();
            String rankImage = rankType.getRankImage();
            int rankType1 = rankType.getRankType();
            int rankOption = rankType.getRankOption();

            rankDAO.create(rankMinPosts, rankLevel, rankTitle, rankImage, rankType1, rankOption);
        }
    }

    public void importGroup()
        throws JAXBException, CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException,
        ParseException, ObjectNotFoundException {
        GroupListDAO groupListDAO = new GroupListDAO();
        GroupsDAO groupsDAO = factory.getGroupsDAO();
        GroupPermissionDAO groupPermissionDAO = factory.getGroupPermissionDAO();
        List groupTypes = groupListDAO.importGroupTypes();

        for (int i = 0; i < groupTypes.size(); i++) {
            GroupType groupType = (GroupType) groupTypes.get(i);

            String groupName = groupType.getGroupName();
            String groupDesc = groupType.getGroupDesc();
            int groupOption = groupType.getGroupOption();
            String groupOwnerName = groupType.getGroupOwnerName();
            Timestamp groupCreationDate = ImportExportUtil.string2TimeStamp(groupType.getGroupCreationDate());
            Timestamp groupModifiedDate = ImportExportUtil.string2TimeStamp(groupType.getGroupModifiedDate());

            if (groupName.equalsIgnoreCase("Anonymous")) {
                groupsDAO.update(1, groupName, groupDesc, groupModifiedDate);
                groupsDAO.updateOwner(1, groupOwnerName, groupModifiedDate);
            } else if (groupName.equalsIgnoreCase("Admin")) {
                groupsDAO.update(2, groupName, groupDesc, groupModifiedDate);
                groupsDAO.updateOwner(2, groupOwnerName, groupModifiedDate);
            } else {
                groupsDAO.create(groupOwnerName, groupName, groupDesc, groupOption, groupCreationDate,
                        groupModifiedDate);
            }
            int groupID = groupsDAO.getGroupIDFromGroupName(groupName);
            //import to global permission:
            GlobalPermissionList globalPermissionList = groupType.getGlobalPermissionList();
            List globalPermissionLists = globalPermissionList.getGlobalPermission();
            for (int j = 0; j < globalPermissionLists.size(); j++) {
                groupPermissionDAO.create(groupID, ((Integer) globalPermissionLists.get(j)).intValue());
            }
        }
    }

    public void importMember()
        throws JAXBException, ParseException, CreateException, DatabaseException, DuplicateKeyException,
        ObjectNotFoundException, ForeignKeyNotFoundException {
        MemberListDAO memberListDAO = new MemberListDAO();
        MemberDAO memberDAO = factory.getMemberDAO();
        MessageFolderDAO messageFolderDAO = factory.getMessageFolderDAO();
        MemberPermissionDAO memberPermissionDAO = factory.getMemberPermissionDAO();
        MessageDAO messageDAO = factory.getMessageDAO();
        WatchDAO watchDAO = factory.getWatchDAO();

        List memeberTypes = memberListDAO.importMemberList();

        for (int i = 0; i < memeberTypes.size(); i++) {
            MemberType memberType = (MemberType) memeberTypes.get(i);

            String memberName = memberType.getMemberName();
            System.out.println("memberName " + memberName);
            String memberPassword = memberType.getMemberPassword().trim();
            /*System.out.println("memberpassword " + memberPassword);
             if (!memberPassword.equals(""))
             memberPassword = Utils.wrapIt(MD5.getBase64FromMD5(memberPassword));*/
            String memberFirstEmail = memberType.getMemberFirstEmail();
            String memberEmail = memberType.getMemberEmail();

            int memberEmailVisible = memberType.getMemberEmailVisible();
            int memberNameVisible = memberType.getMemberNameVisible();
            String memberFirstIP = memberType.getMemberFirstIP();
            String memberLastIP = memberType.getMemberLastIP();
            int memberViewCount = memberType.getMemberViewCount();
            int memberPostCount = memberType.getMemberPostCount();
            Timestamp memberCreationDate = ImportExportUtil.string2TimeStamp(memberType.getMemberCreationDate());
            Timestamp memberModifiedDate = ImportExportUtil.string2TimeStamp(memberType.getMemberModifiedDate());
            Timestamp memberExpireDate = ImportExportUtil.string2TimeStamp(memberType.getMemberExpireDate());
            Timestamp memberLastLogon = ImportExportUtil.string2TimeStamp(memberType.getMemberLastLogon());
            int memberOption = memberType.getMemberOption();
            int memberStatus = memberType.getMemberStatus();
            String memberActivateCode = memberType.getMemberActivateCode();
            String memberTempPassword = memberType.getMemberTempPassword();
            int memberMessageCount = memberType.getMemberMessageCount();
            int memberMessageOption = memberType.getMemberMessageOption();
            int memberPostsPerPage = memberType.getMemberPostsPerPage();
            int memberWarnCount = memberType.getMemberWarnCount();
            int memberVoteCount = memberType.getMemberVoteCount();
            int memberVoteTotalStars = memberType.getMemberVoteTotalStars();
            int memberRewardPoints = memberType.getMemberRewardPoints();
            String memberTitle = memberType.getMemberTitle();

            double tzone = memberType.getMemberTimeZone();

            String memberSignature = memberType.getMemberSignature();
            String memberAvatar = memberType.getMemberAvatar();
            String memberSkin = memberType.getMemberSkin();
            String memberLanguage = memberType.getMemberLanguage();
            String memberFirstname = memberType.getMemberFirstname();
            String memberLastname = memberType.getMemberLastname();

            int memberGender = memberType.getMemberGender();
            Date memberBirthday = ImportExportUtil.string2Date(memberType.getMemberBirthday());
            String memberAddress = memberType.getMemberAddress();
            String memberCity = memberType.getMemberCity();
            String memberState = memberType.getMemberState();
            String memberCountry = memberType.getMemberCountry();
            String memberPhone = memberType.getMemberPhone();
            String memberMobile = memberType.getMemberMobile();
            String memberFax = memberType.getMemberFax();
            String memberCareer = memberType.getMemberCareer();
            String memberHomepage = memberType.getMemberHomepage();
            String memberYahoo = memberType.getMemberYahoo();
            String memberAol = memberType.getMemberAol();
            String memberIcq = memberType.getMemberIcq();
            String memberMsn = memberType.getMemberMsn();
            String memberCoolLink1 = memberType.getMemberCoolLink1();
            String memberCoolLink2 = memberType.getMemberCoolLink2();

            if (memberName.equalsIgnoreCase("admin")) {
                memberDAO.update(ADMIN_ID, memberName, memberPassword, memberFirstEmail, memberEmail,
                        memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                        memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                        memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                        memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                        memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature, memberAvatar,
                        memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender, memberBirthday,
                        memberAddress, memberCity, memberState, memberCountry, memberPhone, memberMobile, memberFax,
                        memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq, memberMsn, memberCoolLink1,
                        memberCoolLink2);
            } else {
                if (memberName.equalsIgnoreCase("Anonymous")) {
                    memberDAO.create(GUEST_ID, memberName, memberPassword, memberFirstEmail, memberEmail,
                            memberEmailVisible, memberNameVisible, memberFirstIP, memberLastIP, memberViewCount,
                            memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon,
                            memberOption, memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                            memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                            memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature,
                            memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender,
                            memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone,
                            memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq,
                            memberMsn, memberCoolLink1, memberCoolLink2);
                } else {
                    memberDAO.create(memberName, memberPassword, memberFirstEmail, memberEmail, memberEmailVisible,
                            memberNameVisible, memberFirstIP, memberLastIP, memberViewCount, memberPostCount,
                            memberCreationDate, memberModifiedDate, memberExpireDate, memberLastLogon, memberOption,
                            memberStatus, memberActivateCode, memberTempPassword, memberMessageCount,
                            memberMessageOption, memberPostsPerPage, memberWarnCount, memberVoteCount,
                            memberVoteTotalStars, memberRewardPoints, memberTitle, tzone, memberSignature,
                            memberAvatar, memberSkin, memberLanguage, memberFirstname, memberLastname, memberGender,
                            memberBirthday, memberAddress, memberCity, memberState, memberCountry, memberPhone,
                            memberMobile, memberFax, memberCareer, memberHomepage, memberYahoo, memberAol, memberIcq,
                            memberMsn, memberCoolLink1, memberCoolLink2);
                }
            }
        }
        
        for (int i = 0; i < memeberTypes.size(); i++) {
            MemberType memberType = (MemberType) memeberTypes.get(i);
            String memberName = memberType.getMemberName();

            int memberID = memberDAO.getMemberFromMemberName(memberName).getMemberID();
            List messageFolderTypes = memberType.getMessageFolderList().getMessageFolder();
            for (int j = 0; j < messageFolderTypes.size(); j++) {
                MessageFolderType messageFolderType = (MessageFolderType) messageFolderTypes.get(j);

                String folderName = messageFolderType.getFolderName();
                int folderOrder = messageFolderType.getFolderOrder();
                int folderStatus = 0;
                int folderType = 0;
                int folderOption = 0;
                Timestamp folderCreationDate = ImportExportUtil.string2TimeStamp(messageFolderType
                        .getFolderCreationDate());
                Timestamp folderModifiedDate = ImportExportUtil.string2TimeStamp(messageFolderType
                        .getFolderModifiedDate());

                messageFolderDAO.create(folderName, memberID, folderOrder, folderStatus, folderOption, folderType,
                        folderCreationDate, folderModifiedDate);

                //export message
                MessageList messageList = messageFolderType.getMessageList();
                List messageTypes = messageList.getMessage();
                for (int t = 0; t < messageTypes.size(); t++) {
                    MessageType messageType = (MessageType) messageTypes.get(t);

                    String messageSenderName = messageType.getMessageSenderName();
                    int messageSenderID = memberDAO.getMemberFromMemberName(messageSenderName).getMemberID();
                    String messageToList = messageType.getMessageToList();
                    String messageCcList = messageType.getMessageCcList();
                    String messageBccList = messageType.getMessageBccList();
                    String messageTopic = messageType.getMessageTopic();
                    String messageBody = messageType.getMessageBody();
                    int messageType1 = messageType.getMessageType();
                    int messageOption = messageType.getMessageOption();
                    int messageStatus = messageType.getMessageStatus();
                    int messageReadStatus = messageType.getMessageReadStatus();
                    int messageNotify = messageType.getMessageNotify();
                    String messageIcon = messageType.getMessageIcon();
                    int messageAttachCount = messageType.getMessageAttachCount();
                    String messageIP = messageType.getMessageIP();
                    Timestamp messageCreationDate = ImportExportUtil.string2TimeStamp(messageType.getMessageCreationDate());

                    messageDAO.create(folderName, memberID, messageSenderID, messageSenderName, messageToList, messageCcList,
                            messageBccList, messageTopic, messageBody, messageType1, messageOption, messageStatus,
                            messageReadStatus, messageNotify, messageIcon, messageAttachCount, messageIP,
                            messageCreationDate);
                }
            }

            //import globalpermission
            List globalPermissions = memberType.getGlobalPermissionList().getGlobalPermission();
            for (int j = 0; j < globalPermissions.size(); j++) {
                int globalPermission = ((Integer)globalPermissions.get(j)).intValue();
                memberPermissionDAO.create(memberID, globalPermission);
            }

            //import watch list
            List globalWatchs = memberType.getGlobalWatchList().getGlobalWatch();
            for (int j = 0; j < globalWatchs.size(); j++) {
                GlobalWatchType globalWatchType = (GlobalWatchType) globalWatchs.get(j);

                int categoryID = 0;
                int forumID = 0;
                int threadID = 0;
                int watchType = globalWatchType.getWatchType();
                int watchOption = globalWatchType.getWatchOption();
                int watchStatus = globalWatchType.getWatchStatus();
                Timestamp watchCreationDate = ImportExportUtil.string2TimeStamp(globalWatchType.getWatchCreationDate());
                Timestamp watchLastSentDate = ImportExportUtil.string2TimeStamp(globalWatchType.getWatchLastSentDate());
                Timestamp watchEndDate = ImportExportUtil.string2TimeStamp(globalWatchType.getWatchEndDate());
                watchDAO.create(memberID, categoryID, forumID, threadID, watchType, watchOption, watchStatus,
                        watchCreationDate, watchLastSentDate, watchEndDate);

            }
        }
    }

    public void exportXML()
        throws FileNotFoundException, JAXBException {
        Marshaller marshaller = XMLUtil.getMarshaller();
        marshaller.marshal(mvnforum, new FileOutputStream("xml/mvnforum.xml"));
    }

    public static void main(String[] args)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException, ParseException,
        FileNotFoundException {
        try {
            ImportExportXML importExportXML = new ImportExportXML();
            //importExportXML.importMember();
            //importExportXML.exportMemberList();
            //importExportXML.exportGroupList();
            importExportXML.exportCategoryList();
            //importExportXML.exportRankList();
            importExportXML.exportXML();
            //importExportXML.importRank();
            //importExportXML.importGroup();
            System.out.println("done export.");
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ObjectNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}