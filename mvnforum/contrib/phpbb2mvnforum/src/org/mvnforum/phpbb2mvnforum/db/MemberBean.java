/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/MemberBean.java,v 1.6 2007/01/15 10:27:34 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.6 $
 * $Date: 2007/01/15 10:27:34 $
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
 */
package org.mvnforum.phpbb2mvnforum.db;

import java.sql.Date;
import java.sql.Timestamp;

import net.myvietnam.mvncore.util.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 * Included columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
 *                   MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
 *                   MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
 *                   MemberActivateCode, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount,
 *                   MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone,
 *                   MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname,
 *                   MemberLastname, MemberGender, MemberBirthday, MemberAddress, MemberCity,
 *                   MemberState, MemberCountry, MemberPhone, MemberMobile, MemberFax,
 *                   MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq,
 *                   MemberMsn, MemberCoolLink1, MemberCoolLink2
 * Excluded columns: MemberPassword, MemberTempPassword
 */
public class MemberBean {
    
    private int memberID;
    private String memberName;
    private String memberFirstEmail;
    private String memberEmail;
    private String memberPassword;
    private String memberTempPassword;
    private int memberEmailVisible;
    private int memberNameVisible;
    private String memberFirstIP;
    private String memberLastIP;
    private int memberViewCount;
    private int memberPostCount;
    private Timestamp memberCreationDate;
    private Timestamp memberModifiedDate;
    private Timestamp memberExpireDate;
    private Timestamp memberLastLogon;
    private int memberOption;
    private int memberStatus;
    private String memberActivateCode;
    private int memberMessageCount;
    private int memberMessageOption;
    private int memberPostsPerPage;
    private int memberWarnCount;
    private int memberVoteCount;
    private int memberVoteTotalStars;
    private int memberRewardPoints;
    private String memberTitle;
    private double memberTimeZone;
    private String memberSignature;
    private String memberAvatar;
    private String memberSkin;
    private String memberLanguage;
    private String memberFirstname;
    private String memberLastname;
    private int memberGender;
    private Date memberBirthday;
    private String memberAddress;
    private String memberCity;
    private String memberState;
    private String memberCountry;
    private String memberPhone;
    private String memberMobile;
    private String memberFax;
    private String memberCareer;
    private String memberHomepage;
    private String memberYahoo;
    private String memberAol;
    private String memberIcq;
    private String memberMsn;
    private String memberCoolLink1;
    private String memberCoolLink2;

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberFirstEmail() {
        return memberFirstEmail;
    }
    public void setMemberFirstEmail(String memberFirstEmail) {
        this.memberFirstEmail = StringUtil.getEmptyStringIfNull(memberFirstEmail);
    }

    public String getMemberEmail() {
        return memberEmail;
    }
    public void setMemberEmail(String memberEmail) {
        this.memberEmail = StringUtil.getEmptyStringIfNull(memberEmail);
    }

    public int getMemberEmailVisible() {
        return memberEmailVisible;
    }
    public void setMemberEmailVisible(int memberEmailVisible) {
        this.memberEmailVisible = memberEmailVisible;
    }

    public int getMemberNameVisible() {
        return memberNameVisible;
    }
    public void setMemberNameVisible(int memberNameVisible) {
        this.memberNameVisible = memberNameVisible;
    }

    public String getMemberFirstIP() {
        return memberFirstIP;
    }
    public void setMemberFirstIP(String memberFirstIP) {
        this.memberFirstIP = StringUtil.getEmptyStringIfNull(memberFirstIP);
    }

    public String getMemberLastIP() {
        return memberLastIP;
    }
    public void setMemberLastIP(String memberLastIP) {
        this.memberLastIP = StringUtil.getEmptyStringIfNull(memberLastIP);
    }

    public int getMemberViewCount() {
        return memberViewCount;
    }
    public void setMemberViewCount(int memberViewCount) {
        this.memberViewCount = memberViewCount;
    }

    public int getMemberPostCount() {
        return memberPostCount;
    }
    public void setMemberPostCount(int memberPostCount) {
        this.memberPostCount = memberPostCount;
    }

    public Timestamp getMemberCreationDate() {
        return memberCreationDate;
    }
    public void setMemberCreationDate(Timestamp memberCreationDate) {
        this.memberCreationDate = memberCreationDate;
    }

    public Timestamp getMemberModifiedDate() {
        return memberModifiedDate;
    }
    public void setMemberModifiedDate(Timestamp memberModifiedDate) {
        this.memberModifiedDate = memberModifiedDate;
    }

    public Timestamp getMemberExpireDate() {
        return memberExpireDate;
    }
    public void setMemberExpireDate(Timestamp memberExpireDate) {
        this.memberExpireDate = memberExpireDate;
    }

    public Timestamp getMemberLastLogon() {
        return memberLastLogon;
    }
    public void setMemberLastLogon(Timestamp memberLastLogon) {
        this.memberLastLogon = memberLastLogon;
    }

    public int getMemberOption() {
        return memberOption;
    }
    public void setMemberOption(int memberOption) {
        this.memberOption = memberOption;
    }

    public int getMemberStatus() {
        return memberStatus;
    }
    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getMemberActivateCode() {
        return memberActivateCode;
    }
    public void setMemberActivateCode(String memberActivateCode) {
        this.memberActivateCode = StringUtil.getEmptyStringIfNull(memberActivateCode);
    }

    public int getMemberMessageCount() {
        return memberMessageCount;
    }
    public void setMemberMessageCount(int memberMessageCount) {
        this.memberMessageCount = memberMessageCount;
    }

    public int getMemberMessageOption() {
        return memberMessageOption;
    }
    public void setMemberMessageOption(int memberMessageOption) {
        this.memberMessageOption = memberMessageOption;
    }

    public int getMemberPostsPerPage() {
        return memberPostsPerPage;
    }
    public void setMemberPostsPerPage(int memberPostsPerPage) {
        this.memberPostsPerPage = memberPostsPerPage;
    }

    public int getMemberWarnCount() {
        return memberWarnCount;
    }
    public void setMemberWarnCount(int memberWarnCount) {
        this.memberWarnCount = memberWarnCount;
    }

    public int getMemberVoteCount() {
        return memberVoteCount;
    }
    public void setMemberVoteCount(int memberVoteCount) {
        this.memberVoteCount = memberVoteCount;
    }

    public int getMemberVoteTotalStars() {
        return memberVoteTotalStars;
    }
    public void setMemberVoteTotalStars(int memberVoteTotalStars) {
        this.memberVoteTotalStars = memberVoteTotalStars;
    }

    public int getMemberRewardPoints() {
        return memberRewardPoints;
    }
    public void setMemberRewardPoints(int memberRewardPoints) {
        this.memberRewardPoints = memberRewardPoints;
    }

    public String getMemberTitle() {
        return memberTitle;
    }
    public void setMemberTitle(String memberTitle) {
        this.memberTitle = StringUtil.getEmptyStringIfNull(memberTitle);
    }

    public double getMemberTimeZone() {
        return memberTimeZone;
    }
    public void setMemberTimeZone(double memberTimeZone) {
        this.memberTimeZone = memberTimeZone;
    }

    public String getMemberSignature() {
        return memberSignature;
    }
    public void setMemberSignature(String memberSignature) {
        this.memberSignature = StringUtil.getEmptyStringIfNull(memberSignature);
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }
    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = StringUtil.getEmptyStringIfNull(memberAvatar);
    }

    public String getMemberSkin() {
        return memberSkin;
    }
    public void setMemberSkin(String memberSkin) {
        this.memberSkin = StringUtil.getEmptyStringIfNull(memberSkin);
    }

    public String getMemberLanguage() {
        return memberLanguage;
    }
    public void setMemberLanguage(String memberLanguage) {
        this.memberLanguage = StringUtil.getEmptyStringIfNull(memberLanguage);
    }

    public String getMemberFirstname() {
        return memberFirstname;
    }
    public void setMemberFirstname(String memberFirstname) {
        this.memberFirstname = StringUtil.getEmptyStringIfNull(memberFirstname);
    }

    public String getMemberLastname() {
        return memberLastname;
    }
    public void setMemberLastname(String memberLastname) {
        this.memberLastname = StringUtil.getEmptyStringIfNull(memberLastname);
    }

    public int getMemberGender() {
        return memberGender;
    }
    public void setMemberGender(int memberGender) {
        this.memberGender = memberGender;
    }

    public Date getMemberBirthday() {
        return memberBirthday;
    }
    public void setMemberBirthday(Date memberBirthday) {
        this.memberBirthday = memberBirthday;
    }

    public String getMemberAddress() {
        return memberAddress;
    }
    public void setMemberAddress(String memberAddress) {
        this.memberAddress = StringUtil.getEmptyStringIfNull(memberAddress);
    }

    public String getMemberCity() {
        return memberCity;
    }
    public void setMemberCity(String memberCity) {
        this.memberCity = StringUtil.getEmptyStringIfNull(memberCity);
    }

    public String getMemberState() {
        return memberState;
    }
    public void setMemberState(String memberState) {
        this.memberState = StringUtil.getEmptyStringIfNull(memberState);
    }

    public String getMemberCountry() {
        return memberCountry;
    }
    public void setMemberCountry(String memberCountry) {
        this.memberCountry = StringUtil.getEmptyStringIfNull(memberCountry);
    }

    public String getMemberPhone() {
        return memberPhone;
    }
    public void setMemberPhone(String memberPhone) {
        this.memberPhone = StringUtil.getEmptyStringIfNull(memberPhone);
    }

    public String getMemberMobile() {
        return memberMobile;
    }
    public void setMemberMobile(String memberMobile) {
        this.memberMobile = StringUtil.getEmptyStringIfNull(memberMobile);
    }

    public String getMemberFax() {
        return memberFax;
    }
    public void setMemberFax(String memberFax) {
        this.memberFax = StringUtil.getEmptyStringIfNull(memberFax);
    }

    public String getMemberCareer() {
        return memberCareer;
    }
    public void setMemberCareer(String memberCareer) {
        this.memberCareer = StringUtil.getEmptyStringIfNull(memberCareer);
    }

    public String getMemberHomepage() {
        return memberHomepage;
    }
    public void setMemberHomepage(String memberHomepage) {
        this.memberHomepage = StringUtil.getEmptyStringIfNull(memberHomepage);
    }

    public String getMemberYahoo() {
        return memberYahoo;
    }
    public void setMemberYahoo(String memberYahoo) {
        this.memberYahoo = StringUtil.getEmptyStringIfNull(memberYahoo);
    }

    public String getMemberAol() {
        return memberAol;
    }
    public void setMemberAol(String memberAol) {
        this.memberAol = StringUtil.getEmptyStringIfNull(memberAol);
    }

    public String getMemberIcq() {
        return memberIcq;
    }
    public void setMemberIcq(String memberIcq) {
        this.memberIcq = StringUtil.getEmptyStringIfNull(memberIcq);
    }

    public String getMemberMsn() {
        return memberMsn;
    }
    public void setMemberMsn(String memberMsn) {
        this.memberMsn = StringUtil.getEmptyStringIfNull(memberMsn);
    }

    public String getMemberCoolLink1() {
        return memberCoolLink1;
    }
    public void setMemberCoolLink1(String memberCoolLink1) {
        this.memberCoolLink1 = StringUtil.getEmptyStringIfNull(memberCoolLink1);
    }

    public String getMemberCoolLink2() {
        return memberCoolLink2;
    }
    public void setMemberCoolLink2(String memberCoolLink2) {
        this.memberCoolLink2 = StringUtil.getEmptyStringIfNull(memberCoolLink2);
    }
    
    public void getBeanDocument(Document doc, Element element) {
    
        Element category = doc.createElement("Category");
        element.appendChild(category);
        
        category.appendChild(getNode(doc, "MemberID", String.valueOf(memberID)));
        category.appendChild(getNode(doc, "MemberName", String.valueOf(memberName)));
        category.appendChild(getNode(doc, "MemberPassword", String.valueOf(memberPassword)));
        category.appendChild(getNode(doc, "MemberFirstEmail", String.valueOf(memberFirstEmail)));
        category.appendChild(getNode(doc, "MemberEmail", String.valueOf(memberEmail)));
        category.appendChild(getNode(doc, "MemberEmailVisible", String.valueOf(memberEmailVisible)));
        category.appendChild(getNode(doc, "MemberNameVisible", String.valueOf(memberNameVisible)));
        category.appendChild(getNode(doc, "MemberFirstIP", String.valueOf(memberFirstIP)));
        category.appendChild(getNode(doc, "MemberLastIP", String.valueOf(memberLastIP)));
        category.appendChild(getNode(doc, "MemberViewCount", String.valueOf(memberViewCount)));
        category.appendChild(getNode(doc, "MemberPostCount", String.valueOf(memberPostCount)));
        category.appendChild(getNode(doc, "MemberCreationDate", String.valueOf(memberCreationDate)));
        category.appendChild(getNode(doc, "MemberModifiedDate", String.valueOf(memberModifiedDate)));
        category.appendChild(getNode(doc, "MemberExpireDate", String.valueOf(memberExpireDate)));
        category.appendChild(getNode(doc, "MemberLastLogon", String.valueOf(memberLastLogon)));
        category.appendChild(getNode(doc, "MemberOption", String.valueOf(memberOption)));
        category.appendChild(getNode(doc, "MemberStatus", String.valueOf(memberStatus)));
        category.appendChild(getNode(doc, "MemberActivateCode", String.valueOf(memberActivateCode)));
        category.appendChild(getNode(doc, "MemberTempPassword", String.valueOf(memberTempPassword)));
        category.appendChild(getNode(doc, "MemberMessageCount", String.valueOf(memberMessageCount)));
        category.appendChild(getNode(doc, "MemberMessageOption", String.valueOf(memberMessageOption)));
        category.appendChild(getNode(doc, "MemberPostsPerPage", String.valueOf(memberPostsPerPage)));
        category.appendChild(getNode(doc, "MemberWarnCount", String.valueOf(memberWarnCount)));
        category.appendChild(getNode(doc, "MemberVoteCount", String.valueOf(memberVoteCount)));
        category.appendChild(getNode(doc, "MemberVoteTotalStars", String.valueOf(memberVoteTotalStars)));
        category.appendChild(getNode(doc, "MemberRewardPoints", String.valueOf(memberRewardPoints)));
        category.appendChild(getNode(doc, "MemberTitle", String.valueOf(memberTitle)));
        category.appendChild(getNode(doc, "MemberTimeZone", String.valueOf(memberTimeZone)));
        category.appendChild(getNode(doc, "MemberSignature", String.valueOf(memberSignature)));
        category.appendChild(getNode(doc, "MemberAvatar", String.valueOf(memberAvatar)));
        category.appendChild(getNode(doc, "MemberSkin", String.valueOf(memberSkin)));
        category.appendChild(getNode(doc, "MemberLanguage", String.valueOf(memberLanguage)));
        category.appendChild(getNode(doc, "MemberTempPassword", String.valueOf(memberTempPassword)));
        category.appendChild(getNode(doc, "MemberFirstname", String.valueOf(memberFirstname)));
        category.appendChild(getNode(doc, "MemberLastname", String.valueOf(memberLastname)));
        category.appendChild(getNode(doc, "MemberGender", String.valueOf(memberGender)));
        category.appendChild(getNode(doc, "MemberBirthday", String.valueOf(memberBirthday)));
        category.appendChild(getNode(doc, "MemberAddress", String.valueOf(memberAddress)));
        category.appendChild(getNode(doc, "MemberCity", String.valueOf(memberCity)));
        category.appendChild(getNode(doc, "MemberState", String.valueOf(memberState)));
        category.appendChild(getNode(doc, "MemberCountry", String.valueOf(memberCountry)));
        category.appendChild(getNode(doc, "MemberPhone", String.valueOf(memberPhone)));
        category.appendChild(getNode(doc, "MemberMobile", String.valueOf(memberMobile)));
        category.appendChild(getNode(doc, "MemberFax", String.valueOf(memberFax)));
        category.appendChild(getNode(doc, "MemberCareer", String.valueOf(memberCareer)));
        category.appendChild(getNode(doc, "MemberHomepage", String.valueOf(memberHomepage)));
        category.appendChild(getNode(doc, "MemberYahoo", String.valueOf(memberYahoo)));
        category.appendChild(getNode(doc, "MemberAol", String.valueOf(memberAol)));
        category.appendChild(getNode(doc, "MemberIcq", String.valueOf(memberIcq)));
        category.appendChild(getNode(doc, "MemberMsn", String.valueOf(memberMsn)));
        category.appendChild(getNode(doc, "MemberCoolLink1", String.valueOf(memberCoolLink1)));
        category.appendChild(getNode(doc, "MemberCoolLink2", String.valueOf(memberCoolLink2)));
    }
    
    public static Node getNode (Document doc, String childName, String childValue ) {
        Element child = doc.createElement(childName);
        child.appendChild(doc.createTextNode(childValue));
        return child;
    }

    public String getMemberPassword() {
        return memberPassword;
    }
    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }
    public String getMemberTempPassword() {
        return memberTempPassword;
    }
    public void setMemberTempPassword(String memberTempPassword) {
        this.memberTempPassword = memberTempPassword;
    }
} //end of class MemberBean



