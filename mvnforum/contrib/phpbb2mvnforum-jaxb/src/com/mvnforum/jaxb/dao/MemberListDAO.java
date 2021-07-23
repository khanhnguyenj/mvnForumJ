package com.mvnforum.jaxb.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.mvnforum.jaxb.db.GlobalPermissionList;
import com.mvnforum.jaxb.db.GlobalWatchList;
import com.mvnforum.jaxb.db.GlobalWatchType;
import com.mvnforum.jaxb.db.MemberType;
import com.mvnforum.jaxb.db.MessageFolderList;
import com.mvnforum.jaxb.db.MessageFolderType;
import com.mvnforum.jaxb.db.MessageList;
import com.mvnforum.jaxb.db.MessageType;
import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.ObjectFactory;
import com.mvnforum.jaxb.db.MvnforumType.MemberListType;
import com.mvnforum.jaxb.util.XMLUtil;

public class MemberListDAO {

    public GlobalWatchType getGlobalWatchType(int watchType, int watchOption, int watchStatus,
            String watchCreationDate, String watchLastSentDate, String watchEndDate)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GlobalWatchType globalWatchType = objectFactory.createGlobalWatchType();
        globalWatchType.setWatchType(watchType);
        globalWatchType.setWatchOption(watchOption);
        globalWatchType.setWatchStatus(watchStatus);
        globalWatchType.setWatchCreationDate(watchCreationDate);
        globalWatchType.setWatchLastSentDate(watchLastSentDate);
        globalWatchType.setWatchEndDate(watchEndDate);
        return globalWatchType;
    }

    public GlobalWatchList getGlobalWatchList(Collection globalWatch)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GlobalWatchList globalWatchList = objectFactory.createGlobalWatchList();
        for (Iterator iter = globalWatch.iterator(); iter.hasNext();) {
            globalWatchList.getGlobalWatch().add(iter.next());
        }
        return globalWatchList;
    }

    public MessageFolderType getMessageFolderType(String folderName, int folderOrder, String folderCreationDate,
            String folderModifiedDate, MessageList messageList)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MessageFolderType messageFolderType = objectFactory.createMessageFolderType();
        messageFolderType.setFolderName(folderName);
        messageFolderType.setFolderOrder(folderOrder);
        messageFolderType.setFolderCreationDate(folderCreationDate);
        messageFolderType.setFolderModifiedDate(folderModifiedDate);
        messageFolderType.setMessageList(messageList);
        return messageFolderType;
    }

    public MessageFolderList getMessageFolderList(Collection messageFolder)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MessageFolderList messageFolderList = objectFactory.createMessageFolderList();
        for (Iterator iter = messageFolder.iterator(); iter.hasNext();) {
            messageFolderList.getMessageFolder().add(iter.next());
        }
        return messageFolderList;
    }

    public GlobalPermissionList getGlobalPermissionList(Collection globalPermission)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GlobalPermissionList globalPermissionList = objectFactory.createGlobalPermissionList();
        for (Iterator iter = globalPermission.iterator(); iter.hasNext();) {
            globalPermissionList.getGlobalPermission().add(iter.next());
        }
        return globalPermissionList;
    }

    public MemberType getMemberList(String memberName, String memberPassword, String memberFirstEmail,
            String memberEmail, int memberEmailVisible, int memberNameVisible, String memberFirstIP,
            String memberLastIP, int memberViewCount, int memberPostCount, String memberCreationDate,
            String memberModifiedDate, String memberExpireDate, String memberLastLogon, int memberOption,
            int memberStatus, String memberActivateCode, String memberTempPassword, int memberMessageCount,
            int memberMessageOption, int memberPostsPerPage, int memberWarnCount, int memberVoteCount,
            int memberVoteTotalStars, int memberRewardPoints, String memberTitle, double memberTimeZone,
            String memberSignature, String memberAvatar, String memberSkin, String memberLanguage,
            String memberFirstname, String memberLastname, int memberGender, String memberBirthday,
            String memberAddress, String memberCity, String memberState, String memberCountry, String memberPhone,
            String memberMobile, String memberFax, String memberCareer, String memberHomepage, String memberYahoo,
            String memberAol, String memberIcq, String memberMsn, String memberCoolLink1, String memberCoolLink2,
            GlobalPermissionList globalPermissionList, MessageFolderList messageFolderList,
            GlobalWatchList globalWatchList)
        throws JAXBException {

        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MemberType memberType = objectFactory.createMemberType();
        memberType.setMemberName(memberName);
        memberType.setMemberPassword(memberPassword);
        memberType.setMemberFirstEmail(memberFirstEmail);
        memberType.setMemberEmail(memberEmail);
        memberType.setMemberEmailVisible(memberEmailVisible);
        memberType.setMemberNameVisible(memberNameVisible);
        memberType.setMemberFirstIP(memberFirstIP);
        memberType.setMemberLastIP(memberLastIP);
        memberType.setMemberViewCount(memberViewCount);
        memberType.setMemberPostCount(memberPostCount);
        memberType.setMemberCreationDate(memberCreationDate);
        memberType.setMemberModifiedDate(memberModifiedDate);
        memberType.setMemberExpireDate(memberExpireDate);
        memberType.setMemberLastLogon(memberLastLogon);
        memberType.setMemberOption(memberOption);
        memberType.setMemberStatus(memberStatus);
        memberType.setMemberActivateCode(memberActivateCode);
        memberType.setMemberTempPassword(memberTempPassword);
        memberType.setMemberMessageCount(memberMessageCount);
        memberType.setMemberMessageOption(memberMessageOption);
        memberType.setMemberPostsPerPage(memberPostsPerPage);
        memberType.setMemberWarnCount(memberWarnCount);
        memberType.setMemberVoteCount(memberVoteCount);
        memberType.setMemberVoteTotalStars(memberVoteTotalStars);
        memberType.setMemberRewardPoints(memberRewardPoints);
        memberType.setMemberTitle(memberTitle);
        memberType.setMemberTimeZone(memberTimeZone);
        memberType.setMemberSignature(memberSignature);
        memberType.setMemberAvatar(memberAvatar);
        memberType.setMemberSkin(memberSkin);
        memberType.setMemberLanguage(memberLanguage);
        memberType.setMemberFirstname(memberFirstname);
        memberType.setMemberLastname(memberLastname);
        memberType.setMemberGender(memberGender);
        memberType.setMemberBirthday(memberBirthday);
        memberType.setMemberAddress(memberAddress);
        memberType.setMemberCity(memberCity);
        memberType.setMemberState(memberState);
        memberType.setMemberCountry(memberCountry);
        memberType.setMemberPhone(memberPhone);
        memberType.setMemberMobile(memberMobile);
        memberType.setMemberFax(memberFax);
        memberType.setMemberCareer(memberCareer);
        memberType.setMemberHomepage(memberHomepage);
        memberType.setMemberYahoo(memberYahoo);
        memberType.setMemberAol(memberAol);
        memberType.setMemberIcq(memberIcq);
        memberType.setMemberMsn(memberMsn);
        memberType.setMemberCoolLink1(memberCoolLink1);
        memberType.setMemberCoolLink2(memberCoolLink2);
        memberType.setGlobalPermissionList(globalPermissionList);
        memberType.setMessageFolderList(messageFolderList);
        memberType.setGlobalWatchList(globalWatchList);
        return memberType;
    }

    public MemberListType getMemberListType(Collection memberTypes)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MemberListType memberListType = objectFactory.createMvnforumTypeMemberListType();
        for (Iterator iter = memberTypes.iterator(); iter.hasNext();) {
            memberListType.getMember().add(iter.next());
        }
        return memberListType;
    }

    public MessageList getMessageList(Collection messageLists)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MessageList messageList = objectFactory.createMessageList();
        for (Iterator iter = messageLists.iterator(); iter.hasNext();) {
            messageList.getMessage().add(iter.next());
        }
        return messageList;
    }

    public MessageType getMessageType(String folderName, String messageSenderName, String messageToList,
            String messageCcList, String messageBccList, String messageTopic, String messageBody, int messageType,
            int messageOption, int messageStatus, int messageReadStatus, int messageNotify, String messageIcon,
            int messageAttachCount, String messageIP, String messageCreationDate)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MessageType messageType2 = objectFactory.createMessageType();
        messageType2.setFolderName(folderName);
        messageType2.setMessageSenderName(messageSenderName);
        messageType2.setMessageToList(messageToList);
        messageType2.setMessageCcList(messageCcList);
        messageType2.setMessageBccList(messageBccList);
        messageType2.setMessageTopic(messageTopic);
        messageType2.setMessageBody(messageBody);
        messageType2.setMessageType(messageType);
        messageType2.setMessageOption(messageOption);
        messageType2.setMessageStatus(messageStatus);
        messageType2.setMessageReadStatus(messageReadStatus);
        messageType2.setMessageNotify(messageNotify);
        messageType2.setMessageIcon(messageIcon);
        messageType2.setMessageAttachCount(messageAttachCount);
        messageType2.setMessageIP(messageIP);
        messageType2.setMessageCreationDate(messageCreationDate);
        return messageType2;
    }

    public List importMemberList() throws JAXBException {

        Mvnforum mvnforum = XMLUtil.getMvnforum();
        MemberListType memberListType = mvnforum.getMemberList();
        return memberListType.getMember();
        
    }

}
