package com.mvnforum.jaxb.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.mvnforum.jaxb.db.GlobalPermissionList;
import com.mvnforum.jaxb.db.GroupMemberList;
import com.mvnforum.jaxb.db.GroupMemberType;
import com.mvnforum.jaxb.db.GroupType;
import com.mvnforum.jaxb.db.Mvnforum;
import com.mvnforum.jaxb.db.MvnforumType;
import com.mvnforum.jaxb.db.ObjectFactory;
import com.mvnforum.jaxb.db.MvnforumType.GroupListType;
import com.mvnforum.jaxb.util.XMLUtil;

public class GroupListDAO {

    public GroupListType getGroupListType(Collection groups)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        MvnforumType.GroupListType groupListType = objectFactory.createMvnforumTypeGroupListType();
        for (Iterator iter = groups.iterator(); iter.hasNext();) {
            groupListType.getGroup().add(iter.next());
        }
        return groupListType;
    }

    public GroupType getGroupType(String groupName, String groupDesc, int groupOption, String groupOwnerName,
            String groupCreationDate, String groupModifiedDate, GlobalPermissionList globalPermissionList,
            GroupMemberList groupMemberList)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GroupType group = objectFactory.createGroupType();
        group.setGroupName(groupName);
        group.setGroupDesc(groupDesc);
        group.setGroupOption(groupOption);
        group.setGroupOwnerName(groupOwnerName);
        group.setGroupCreationDate(groupCreationDate);
        group.setGroupModifiedDate(groupModifiedDate);
        group.setGlobalPermissionList(globalPermissionList);
        group.setGroupMemberList(groupMemberList);

        return group;
    }

    public GlobalPermissionList getGlobalPermissionList(Collection globalPermissions)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GlobalPermissionList globalPermissionList = objectFactory.createGlobalPermissionList();
        for (Iterator iter = globalPermissions.iterator(); iter.hasNext();) {
            globalPermissionList.getGlobalPermission().add(iter.next());
        }
        return globalPermissionList;
    }

    public GroupMemberList getGroupMemberList(Collection groupMemberTypes)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GroupMemberList groupMemberList = objectFactory.createGroupMemberList();
        for (Iterator iter = groupMemberTypes.iterator(); iter.hasNext();) {
            groupMemberList.getGroupMember().add(iter.next());
        }
        return groupMemberList;
    }

    public GroupMemberType getGroupMemberType(String memberName, int privilege, String creationDate, String modifiedDate)
        throws JAXBException {
        ObjectFactory objectFactory = XMLUtil.getObjectFactory();
        GroupMemberType groupMemberType = objectFactory.createGroupMemberType();

        groupMemberType.setMemberName(memberName);
        groupMemberType.setPrivilege(privilege);
        groupMemberType.setCreationDate(creationDate);
        groupMemberType.setModifiedDate(modifiedDate);
        return groupMemberType;
    }

    public List importGroupTypes() throws JAXBException {
        
        Mvnforum mvnforum = XMLUtil.getMvnforum();
        GroupListType groupListType = mvnforum.getGroupList();
        System.out.println(groupListType);
        
        return groupListType.getGroup();
        
    }

}
