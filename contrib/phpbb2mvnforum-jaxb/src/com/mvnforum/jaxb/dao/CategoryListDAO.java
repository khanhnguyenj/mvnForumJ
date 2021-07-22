package com.mvnforum.jaxb.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.mvnforum.jaxb.db.AttachmentList;
import com.mvnforum.jaxb.db.AttachmentType;
import com.mvnforum.jaxb.db.CategoryListType;
import com.mvnforum.jaxb.db.CategoryType;
import com.mvnforum.jaxb.db.CategoryWatchList;
import com.mvnforum.jaxb.db.FavoriteThreadList;
import com.mvnforum.jaxb.db.FavoriteThreadType;
import com.mvnforum.jaxb.db.ForumList;
import com.mvnforum.jaxb.db.ForumType;
import com.mvnforum.jaxb.db.ForumWatchList;
import com.mvnforum.jaxb.db.GroupForumPermissionList;
import com.mvnforum.jaxb.db.GroupForumPermissionType;
import com.mvnforum.jaxb.db.MemberForumPermissionList;
import com.mvnforum.jaxb.db.MemberForumPermissionType;
import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.ObjectFactory;
import com.mvnforum.jaxb.db.PostList;
import com.mvnforum.jaxb.db.PostType;
import com.mvnforum.jaxb.db.ThreadList;
import com.mvnforum.jaxb.db.ThreadType;
import com.mvnforum.jaxb.db.ThreadWatchList;
import com.mvnforum.jaxb.db.ThreadWatchType;
import com.mvnforum.jaxb.util.XMLUtil;

public class CategoryListDAO {
    
    public AttachmentType getAttachmentType(String memberName, String attachFilename, int attachFileSize, String attachMimeType,
            String attachDesc, String attachCreationIP, String attachCreationDate, String attachModifiedDate, int attachDownloadCount, 
            int attachOption, int attachStatus) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        AttachmentType attachmentType = objectFactory.createAttachmentType();
        attachmentType.setMemberName(memberName);
        attachmentType.setAttachFilename(attachFilename);
        attachmentType.setAttachFileSize(attachFileSize);
        attachmentType.setAttachMimeType(attachMimeType);
        attachmentType.setAttachDesc(attachDesc);
        attachmentType.setAttachCreationIP(attachCreationIP);
        attachmentType.setAttachCreationDate(attachCreationDate);
        attachmentType.setAttachModifiedDate(attachModifiedDate);
        attachmentType.setAttachDownloadCount(attachDownloadCount);
        attachmentType.setAttachOption(attachOption);
        attachmentType.setAttachStatus(attachStatus);
        
        return attachmentType;
    }
    
    public AttachmentList getAttachmentList (Collection attachmentLists) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        AttachmentList attachmentList = objectFactory.createAttachmentList();
        for (Iterator iter = attachmentLists.iterator(); iter.hasNext(); ) {
            attachmentList.getAttachment().add(iter.next());
        }
        return attachmentList;
    }
    
    public PostType getPostType (String memberName, String lastEditMemberName, String postTopic, String postBody, String postCreationDate, 
            String postLastEditDate, String postCreationIP, String postLastEditIP, int postEditCount, int postFormatOption, 
            int postOption, int postStatus, String postIcon, int postAttachCount, AttachmentList attachmentList, PostList postList) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        PostType postType = objectFactory.createPostType();
        
        postType.setMemberName(memberName);
        postType.setLastEditMemberName(lastEditMemberName);
        postType.setPostTopic(postTopic);
        postType.setPostBody(postBody);
        postType.setPostCreationDate(postCreationDate);
        postType.setPostLastEditDate(postLastEditDate);
        postType.setPostCreationIP(postCreationIP);
        postType.setPostLastEditIP(postLastEditIP);
        postType.setPostEditCount(postEditCount);
        postType.setPostFormatOption(postFormatOption);
        postType.setPostOption(postOption);
        postType.setPostStatus(postStatus);
        postType.setPostIcon(postIcon);
        postType.setPostAttachCount(postAttachCount);
        postType.setAttachmentList(attachmentList);
        postType.setPostList(postList);
        
        return postType;
    }
    
    public PostList getPostList (Collection postLists) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        PostList postList = objectFactory.createPostList();
        for (Iterator iter = postLists.iterator(); iter.hasNext(); ) {
            postList.getPost().add(iter.next());
        }
        return postList;
    }
    
    public PostList getPortList () throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        PostList postList = objectFactory.createPostList();
        return postList;
    }
    
    public FavoriteThreadType getFavoriteThreadType(String memberName, String favoriteCreationDate, int favoriteType, int favoriteOption, int favoriteStatus) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        FavoriteThreadType rank = objectFactory.createFavoriteThreadType();
        rank.setMemberName(memberName);
        rank.setFavoriteCreationDate(favoriteCreationDate);
        rank.setFavoriteType(favoriteType);
        rank.setFavoriteOption(favoriteOption);
        rank.setFavoriteStatus(favoriteStatus);
        return rank;
    }

    public FavoriteThreadList getFavoriteThreadList(Collection favoriteThreadType) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        FavoriteThreadList favoriteThreadList = objectFactory.createFavoriteThreadList();
        for (Iterator iter = favoriteThreadType.iterator(); iter.hasNext(); ) {
            favoriteThreadList.getFavoriteThread().add(iter.next());
        }
        return favoriteThreadList;
    }
    
    public ThreadWatchType getThreadWatchType(String memberName, int watchType, int watchOption, int watchStatus, String watchCreationDate, 
            String watchLastSentDate, String watchEndDate) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ThreadWatchType threadWatchType = objectFactory.createThreadWatchType();
        threadWatchType.setMemberName(memberName);
        threadWatchType.setWatchType(watchType);
        threadWatchType.setWatchOption(watchOption);
        threadWatchType.setWatchStatus(watchStatus);
        threadWatchType.setWatchCreationDate(watchCreationDate);
        threadWatchType.setWatchLastSentDate(watchLastSentDate);
        threadWatchType.setWatchEndDate(watchEndDate);
        return threadWatchType;
    }
    
    public ThreadWatchList getThreadWatchList(Collection threadWatchType) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ThreadWatchList threadWatchList = objectFactory.createThreadWatchList();
        for (Iterator iter = threadWatchType.iterator(); iter.hasNext(); ) {
            threadWatchList.getThreadWatch().add(iter.next());
        }
        return threadWatchList;
    }
    
    public ThreadType getThreadType(String memberName, String threadLastPostMemberName, String threadTopic, String threadBody, 
            int threadVoteCount, int threadVoteTotalStars, String threadCreationDate, String threadLastPostDate, int threadType, 
            int threadOption, int threadStatus, int threadHasPoll, int threadViewCount, int threadReplyCount, String threadIcon, 
            int threadDuration, ThreadWatchList threadWatchList, FavoriteThreadList favoriteThreadList, PostList postList) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ThreadType bean = objectFactory.createThreadType();
        bean.setMemberName(memberName);
        bean.setThreadLastPostMemberName(threadLastPostMemberName);
        bean.setThreadTopic(threadTopic);
        bean.setThreadBody(threadBody);
        bean.setThreadVoteCount(threadVoteCount);
        bean.setThreadVoteTotalStars(threadVoteTotalStars);
        bean.setThreadCreationDate(threadCreationDate);
        bean.setThreadLastPostDate(threadLastPostDate);
        bean.setThreadType(threadType);
        bean.setThreadOption(threadOption);
        bean.setThreadStatus(threadStatus);
        bean.setThreadHasPoll(threadHasPoll);
        bean.setThreadViewCount(threadViewCount);
        bean.setThreadReplyCount(threadReplyCount);
        bean.setThreadIcon(threadIcon);
        bean.setThreadDuration(threadDuration);
        bean.setThreadWatchList(threadWatchList);
        bean.setFavoriteThreadList(favoriteThreadList);
        bean.setPostList(postList);
        return bean;
    }
    
    public ThreadList getThreadList(Collection threadType) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ThreadList threadList = objectFactory.createThreadList();
        for (Iterator iter = threadType.iterator(); iter.hasNext(); ) {
            threadList.getThread().add(iter.next());
        }
        return threadList;
    }
    
    public ForumWatchList getForumWatchList(Collection forumWatchLists) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ForumWatchList forumWatchList = objectFactory.createForumWatchList();
        for (Iterator iter = forumWatchLists.iterator(); iter.hasNext(); ) {
            forumWatchList.getForumWatchList().add(iter.next());
        }
        return forumWatchList;
    }
    
    public GroupForumPermissionType getGroupForumPermissionType(String groupName, int forumPermission) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GroupForumPermissionType groupForumPermissionType = objectFactory.createGroupForumPermissionType();
        groupForumPermissionType.setGroupName(groupName);
        groupForumPermissionType.setForumPermission(forumPermission);
        return groupForumPermissionType;
    }
    
    public GroupForumPermissionList getGroupForumPermissionList(Collection groupForumPermissionTypes) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GroupForumPermissionList groupForumPermissionList = objectFactory.createGroupForumPermissionList();
        for (Iterator iter = groupForumPermissionTypes.iterator(); iter.hasNext(); ) {
            groupForumPermissionList.getGroupForumPermission().add(iter.next());
        }        
        return groupForumPermissionList;
    }
    
    public MemberForumPermissionType getMemberForumPermissionType(String memberName, int forumPermission) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MemberForumPermissionType memberForumPermissionType = objectFactory.createMemberForumPermissionType();
        memberForumPermissionType.setMemberName(memberName);
        memberForumPermissionType.setForumPermission(forumPermission);
        return memberForumPermissionType;
    }
    
    public MemberForumPermissionList getMemberForumPermissionList(Collection memberForumPermissionTypes) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MemberForumPermissionList memberForumPermissionList = objectFactory.createMemberForumPermissionList();
        for (Iterator iter = memberForumPermissionTypes.iterator(); iter.hasNext(); ) {
            memberForumPermissionList.getMemberForumPermission().add(iter.next());
        }    
        return memberForumPermissionList;
    }
    
    public ForumType getForumType(String forumLastPostMemberName, String forumName, String forumDesc, String forumCreationDate, String forumModifiedDate, 
            String forumLastPostDate, int forumOrder, int forumFormatOption, int forumOption, int forumStatus, int forumModerationMode, 
            String forumPassword, int forumThreadCount, int forumPostCount, MemberForumPermissionList memberForumPermissionList, 
            GroupForumPermissionList groupForumPermissionList, ForumWatchList forumWatchList, ThreadList threadList) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ForumType forumType = objectFactory.createForumType();
        forumType.setForumLastPostMemberName(forumLastPostMemberName);
        forumType.setForumName(forumName);
        forumType.setForumDesc(forumDesc);
        forumType.setForumCreationDate(forumCreationDate);
        forumType.setForumModifiedDate(forumModifiedDate);
        forumType.setForumLastPostDate(forumLastPostDate);
        forumType.setForumOrder(forumOrder);
        forumType.setForumFormatOption(forumFormatOption);
        forumType.setForumOption(forumOption);
        forumType.setForumStatus(forumStatus);
        forumType.setForumModerationMode(forumModerationMode);
        forumType.setForumPassword(forumPassword);
        forumType.setForumThreadCount(forumThreadCount);
        forumType.setForumPostCount(forumPostCount);
        forumType.setMemberForumPermissionList(memberForumPermissionList);
        forumType.setGroupForumPermissionList(groupForumPermissionList);
        forumType.setForumWatchList(forumWatchList);
        forumType.setThreadList(threadList);
        return forumType;
    }
    
    public ForumList getForumList(Collection forumType) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        ForumList forumList = objectFactory.createForumList();
        for (Iterator iter = forumType.iterator(); iter.hasNext(); ) {
            forumList.getForum().add(iter.next());
        }
        return forumList;
    }
    
    public CategoryWatchList getCategoryWatchList(Collection categoryWatch) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        CategoryWatchList categoryWatchList = objectFactory.createCategoryWatchList();
        for (Iterator iter = categoryWatch.iterator(); iter.hasNext(); ) {
            categoryWatchList.getCategoryWatch().add(iter.next());
        }
        return categoryWatchList;
    }
    
    public CategoryListType getCategoryListType(Collection categoryType) throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        CategoryListType categoryListType = objectFactory.createCategoryListType();
        for (Iterator iter = categoryType.iterator(); iter.hasNext(); ) {
            categoryListType.getCategory().add(iter.next());
        }
        return categoryListType;
    }
    
    public CategoryListType getCategoryListType () throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        CategoryListType categoryListType = objectFactory.createCategoryListType();
        return categoryListType;
    }

    public CategoryType getCategoryType(String categoryName, String categoryDesc, String categoryCreationDate, String categoryModifiedDate, 
            int categoryOrder, int categoryOption, int categoryStatus, CategoryWatchList categoryWatchList, ForumList forumList, CategoryListType categoryList) 
    throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        CategoryType rank = objectFactory.createCategoryType();
        rank.setCategoryName(categoryName);
        rank.setCategoryDesc(categoryDesc);
        rank.setCategoryCreationDate(categoryCreationDate);
        rank.setCategoryModifiedDate(categoryModifiedDate);
        rank.setCategoryOrder(categoryOrder);
        rank.setCategoryOption(categoryOption);
        rank.setCategoryStatus(categoryStatus);
        rank.setCategoryWatchList(categoryWatchList);
        rank.setForumList(forumList);
        rank.setCategoryList(categoryList);
        return rank;
    }

    public List importCategoryTypes() throws JAXBException {
        
        Mvnforum mvnforum = XMLUtil.getMvnforum();
        CategoryListType categoryListType = mvnforum.getCategoryList();
        
        // System.out.println(categoryListType);
        // return categoryListType.getCategory();
        
        return categoryListType.getCategory();
        
    }

}
