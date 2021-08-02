/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/importexport/mvnforum/MvnForumMemberXML.java,v 1.13 2009/12/05 08:11:39 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.13 $
 * $Date: 2009/12/05 08:11:39 $
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
 * @author: Igor Manic
 */
package com.mvnforum.admin.importexport.mvnforum;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.admin.MemberXML;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

/**
 * @author Igor Manic
 * @version $Revision: 1.13 $, $Date: 2009/12/05 08:11:39 $
 * <br/>
 * <code>MvnForumMemberXML</code> class encapsulates processing of
 * members' definitions found in the backup XML file. It implements
 * methods to be called from XML parsing routine, adds some additional
 * processing and checking, and calls corresponding methods of
 * <code>MemberXML</code> and other neccessary classes in order to perform
 * the actual creation of a member, as well as other related items (like
 * message folders, member permissions and global watches). It also ensures
 * that default Admin and virtual Guest are created, even if they weren't
 * found in the XML.
 */
public class MvnForumMemberXML {

    private MemberXML memberXML = null;
    private boolean memberCreated = false;
    private boolean isGuest = false;
    private boolean isAdmin = false;

    String memberName           = null;
    String memberPassword       = null;
    String memberFirstEmail     = null;
    String memberEmail          = null;
    String memberEmailVisible   = null;
    String memberNameVisible    = null;
    String memberFirstIP        = null;
    String memberLastIP         = null;
    String memberViewCount      = null;
    String memberPostCount      = null;
    String memberCreationDate   = null;
    String memberModifiedDate   = null;
    String memberExpireDate     = null;
    String memberPasswordExpireDate = null;
    String memberLastLogon      = null;
    String memberOption         = null;
    String memberStatus         = null;
    String memberActivateCode   = null;
    String memberTempPassword   = null;
    String memberMessageCount   = null;
    String memberMessageOption  = null;
    String memberPostsPerPage   = null;
    String memberWarnCount      = null;
    String memberVoteCount      = null;
    String memberVoteTotalStars = null;
    String memberRewardPoints   = null;
    String memberTitle          = null;
    String memberTimeZone       = null;
    String memberSignature      = null;
    String memberAvatar         = null;
    String memberSkin           = null;
    String memberLanguage       = null;
    String memberFirstname      = null;
    String memberLastname       = null;
    String memberGender         = null;
    String memberBirthday       = null;
    String memberAddress        = null;
    String memberCity           = null;
    String memberState          = null;
    String memberCountry        = null;
    String memberPhone          = null;
    String memberMobile         = null;
    String memberFax            = null;
    String memberCareer         = null;
    String memberHomepage       = null;
    String memberYahoo          = null;
    String memberAol            = null;
    String memberIcq            = null;
    String memberMsn            = null;
    String memberCoolLink1      = null;
    String memberCoolLink2      = null;

    public MvnForumMemberXML() {
        memberXML = new MemberXML();
        memberCreated = false;
        isAdmin = false;
        isGuest = false;
    }

    public int getMemberID() {
        return memberXML.getMemberID();
    }

    public void setMemberID(String id) {
        memberXML.setMemberID(id);
    }

    /**
     * This method simply calls <code>setMemberID()</code>.
     * It's defined only to avoid property-setter problems with digester
     * (since it doesn't seem to recognize <code>setMemberID()</code> as a setter
     * method for <code>memberID</code> property).
     */
    public void setMemberId(String id) {
        setMemberID(id);
    }

    public void setMemberClass(String memberClass) {
        if (memberClass!=null) {
            if (memberClass.equalsIgnoreCase("Guest")) {
                memberXML.setMemberID(Integer.toString(MVNForumConstant.MEMBER_ID_OF_GUEST));
                isGuest=true;
            } else if (memberClass.equalsIgnoreCase("Admin")) {
                memberXML.setMemberID(Integer.toString(MVNForumConstant.MEMBER_ID_OF_ADMIN));
                isAdmin=true;
            }
        }
    }

    public void setMemberName(String value) {
        memberName=value;
    }

    public void setMemberPassword(String value) {
        memberPassword=value;
    }

    public void setMemberFirstEmail(String value) {
        memberFirstEmail=value;
    }

    public void setMemberEmail(String value) {
        memberEmail=value;
    }

    public void setMemberEmailVisible(String value) {
        memberEmailVisible=value;
    }

    public void setMemberNameVisible(String value) {
        memberNameVisible=value;
    }

    public void setMemberFirstIP(String value) {
        memberFirstIP=value;
    }

    public void setMemberLastIP(String value) {
        memberLastIP=value;
    }

    public void setMemberViewCount(String value) {
        memberViewCount=value;
    }

    public void setMemberPostCount(String value) {
        memberPostCount=value;
    }

    public void setMemberCreationDate(String value) {
        memberCreationDate=value;
    }

    public void setMemberModifiedDate(String value) {
        memberModifiedDate=value;
    }

    public void setMemberExpireDate(String value) {
        memberExpireDate=value;
    }

    public void setMemberLastLogon(String value) {
        memberLastLogon=value;
    }

    public void setMemberOption(String value) {
        memberOption=value;
    }

    public void setMemberStatus(String value) {
        memberStatus=value;
    }

    public void setMemberActivateCode(String value) {
        memberActivateCode=value;
    }

    public void setMemberTempPassword(String value) {
        memberTempPassword=value;
    }

    public void setMemberMessageCount(String value) {
        memberMessageCount=value;
    }

    public void setMemberMessageOption(String value) {
        memberMessageOption=value;
    }

    public void setMemberPostsPerPage(String value) {
        memberPostsPerPage=value;
    }

    public void setMemberWarnCount(String value) {
        memberWarnCount=value;
    }

    public void setMemberVoteCount(String value) {
        memberVoteCount=value;
    }

    public void setMemberVoteTotalStars(String value) {
        memberVoteTotalStars=value;
    }

    public void setMemberRewardPoints(String value) {
        memberRewardPoints=value;
    }

    public void setMemberTitle(String value) {
        memberTitle=value;
    }

    public void setMemberTimeZone(String value) {
        memberTimeZone=value;
    }

    public void setMemberSignature(String value) {
        memberSignature=value;
    }

    public void setMemberAvatar(String value) {
        memberAvatar=value;
    }

    public void setMemberSkin(String value) {
        memberSkin=value;
    }

    public void setMemberLanguage(String value) {
        memberLanguage=value;
    }

    public void setMemberFirstname(String value) {
        memberFirstname=value;
    }

    public void setMemberLastname(String value) {
        memberLastname=value;
    }

    public void setMemberGender(String value) {
        memberGender=value;
    }

    public void setMemberBirthday(String value) {
        memberBirthday=value;
    }

    public void setMemberAddress(String value) {
        memberAddress=value;
    }

    public void setMemberCity(String value) {
        memberCity=value;
    }

    public void setMemberState(String value) {
        memberState=value;
    }

    public void setMemberCountry(String value) {
        memberCountry=value;
    }

    public void setMemberPhone(String value) {
        memberPhone=value;
    }

    public void setMemberMobile(String value) {
        memberMobile=value;
    }

    public void setMemberFax(String value) {
        memberFax=value;
    }

    public void setMemberCareer(String value) {
        memberCareer=value;
    }

    public void setMemberHomepage(String value) {
        memberHomepage=value;
    }

    public void setMemberYahoo(String value) {
        memberYahoo=value;
    }

    public void setMemberAol(String value) {
        memberAol=value;
    }

    public void setMemberIcq(String value) {
        memberIcq=value;
    }

    public void setMemberMsn(String value) {
        memberMsn=value;
    }

    public void setMemberCoolLink1(String value) {
        memberCoolLink1=value;
    }

    public void setMemberCoolLink2(String value) {
        memberCoolLink2=value;
    }

    public void addMember()
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        /* First, check if the digester already called this method.
         * It will happen even under normal circumstances, if this member has
         * subelements that need it already be defined, so they first call
         * this method to create member before creating data that refer him.
         */
        if (memberCreated) {
            return;
        }
        /* Then, check if Guest and Admin were created (must be first ones to be created).
         * Otherwise, DBMS might have added some other (non-reserved) members with these
         * IDs that are supposed to be reserved for Admin and Guest.
         * IMPORTANT: First I'll check Admin and then Guest, because Guest's
         * ID is higher than Admin's.
         */
        if (!isAdmin) {
            MvnForumXML.checkAdminMember();
        }
        if ((!isAdmin) && (!isGuest)) {
            MvnForumXML.checkGuestMember();
        }

        ImportMvnForum.addMessage("Adding member \""+memberName+"\".");
        memberXML.addMember(memberName, memberPassword, memberFirstEmail,
                            memberEmail, memberEmailVisible, memberNameVisible,
                            memberFirstIP, memberLastIP, memberViewCount,
                            memberPostCount, memberCreationDate, memberModifiedDate, memberExpireDate, memberPasswordExpireDate,
                            memberLastLogon, memberOption, memberStatus,
                            memberActivateCode, memberTempPassword, memberMessageCount,
                            memberMessageOption, memberPostsPerPage, memberWarnCount,
                            memberVoteCount, memberVoteTotalStars, memberRewardPoints,
                            memberTitle, memberTimeZone, memberSignature,
                            memberAvatar, memberSkin, memberLanguage,
                            memberFirstname, memberLastname, memberGender,
                            memberBirthday, memberAddress, memberCity,
                            memberState, memberCountry, memberPhone,
                            memberMobile, memberFax, memberCareer,
                            memberHomepage);
        memberCreated = true;
        if (isAdmin) {
            MvnForumXML.addedAdminMember = true;
        } else if (isGuest) {
            MvnForumXML.addedGuestMember = true;
        }
    }

    public void addMemberPermission(String permission)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        if ( (!memberCreated) || (memberXML.getMemberID()<0) ) {
            addMember();
        }
        ImportMvnForum.addMessage("Adding member permission \""+permission+"\".");
        memberXML.addMemberPermission(permission);
    }

    public void addMessageFolder(String folderName, String folderOrder,
                                 String folderCreationDate, String folderModifiedDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        if ( (!memberCreated) || (memberXML.getMemberID()<0) ) {
            addMember();
        }
        ImportMvnForum.addMessage("Adding message folder \""+folderName+"\".");
        memberXML.addMessageFolder(folderName,
                  folderOrder, folderCreationDate, folderModifiedDate);
    }

    public void addGlobalWatch(String watchType, String watchOption,
                               String watchStatus, String watchCreationDate,
                               String watchLastSentDate, String watchEndDate)
        throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        if ( (!memberCreated) || (memberXML.getMemberID()<0) ) {
            addMember();
        }
        ImportMvnForum.addMessage("Adding global watch.");
        memberXML.addGlobalWatch(
                  watchType, watchOption, watchStatus,
                  watchCreationDate, watchLastSentDate, watchEndDate);
    }

}
