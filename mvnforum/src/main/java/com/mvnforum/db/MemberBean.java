/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/MemberBean.java,v 1.46 2010/07/28
 * 04:31:23 minhnn Exp $ $Author: minhnn $ $Revision: 1.46 $ $Date: 2010/07/28 04:31:23 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2007 by MyVietnam.net
 *
 * All copyright notices regarding mvnForum MUST remain intact in the scripts and in the outputted
 * HTML. The "powered by" text/logo with a link back to http://www.mvnForum.com and
 * http://www.MyVietnam.net in the footer of the pages MUST remain visible when the pages are viewed
 * on the internet or intranet.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *
 * Support can be obtained from support forums at: http://www.mvnForum.com/mvnforum/index
 *
 * Correspondence and Marketing Questions can be sent to: info at MyVietnam net
 *
 * @author: Minh Nguyen
 *
 * @author: Mai Nguyen
 */
package com.mvnforum.db;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mvnforum.MVNForumGlobal;
import com.mvnforum.common.MemberAvatarTimeTracking;
import net.myvietnam.mvncore.security.Encoder;
import net.myvietnam.mvncore.service.BinaryStorageService;
import net.myvietnam.mvncore.service.MvnCoreServiceFactory;
import net.myvietnam.mvncore.util.DateUtil;
import net.myvietnam.mvncore.util.StringUtil;

/*
 * Included columns: MemberID, MemberName, MemberFirstEmail, MemberEmail, MemberEmailVisible,
 * MemberNameVisible, MemberFirstIP, MemberLastIP, MemberViewCount, MemberPostCount,
 * MemberCreationDate, MemberModifiedDate, MemberLastLogon, MemberOption, MemberStatus,
 * MemberActivateCode, MemberMessageCount, MemberMessageOption, MemberPostsPerPage, MemberWarnCount,
 * MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints, MemberTitle, MemberTimeZone,
 * MemberSignature, MemberAvatar, MemberSkin, MemberLanguage, MemberFirstname, MemberLastname,
 * MemberGender, MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry, MemberPhone,
 * MemberMobile, MemberFax, MemberCareer, MemberHomepage, MemberYahoo, MemberAol, MemberIcq,
 * MemberMsn, MemberCoolLink1, MemberCoolLink2 Excluded columns: MemberPassword, MemberTempPassword
 */
public class MemberBean {

  public static final int MEMBER_STATUS_ENABLE = 0;
  public static final int MEMBER_STATUS_DISABLE = 1;
  public static final int MEMBER_STATUS_PENDING = 2;
  public static final int MEMBER_STATUS_DELETED = 3;

  public static final int MEMBER_GENDER_FEMALE = 0;
  public static final int MEMBER_GENDER_MALE = 1;

  public static final int MEMBER_EMAIL_INVISIBLE = 0;
  public static final int MEMBER_EMAIL_VISIBLE = 1;

  public static final int MEMBER_NAME_INVISIBLE = 0;
  public static final int MEMBER_NAME_VISIBLE = 1;

  public static final String MEMBER_ACTIVATECODE_ACTIVATED = "activated";

  public static final String MEMBER_AVATAR_USING_UPLOAD = "uploaded";

  public static final Date MEMBER_NOT_REQUIRE_BIRTHDAY = new Date(0);

  private int memberID;
  private String memberName;
  private String memberFirstEmail;
  private String memberEmail;
  private int memberEmailVisible;
  private int memberNameVisible;
  private String memberFirstIP;
  private String memberLastIP;
  private int memberViewCount;
  private int memberPostCount;
  private Timestamp memberCreationDate;
  private Timestamp memberModifiedDate;
  private Timestamp memberExpireDate;
  private Timestamp memberPasswordExpireDate;
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

  public void setMemberPasswordExpireDate(Timestamp passwordExpireDate) {
    this.memberPasswordExpireDate = passwordExpireDate;
  }

  public Timestamp getMemberPasswordExpireDate() {
    return memberPasswordExpireDate;
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

  /////////////////////////////////////////////////////////////////
  // property methods
  HashMap properties = new HashMap();

  public void putProperty(String key, Object value) {
    properties.put(key, value);
  }

  public Object getProperty(String key) {
    return properties.get(key);
  }

  /////////////////////////////////////////////////////////////////
  // utility methods
  /*
   * @todo : review these methods
   */
  public String getMemberGenderString() {
    if (memberGender == MEMBER_GENDER_MALE) {
      return "Male";
    }
    return "Female";
  }

  public String getMemberAvatar_processed(HttpServletRequest request,
      HttpServletResponse response) {
    if (memberAvatar.length() == 0) {// never null here, see setMemberAvatar
      return "";
    }
    String retMemberAvatar = memberAvatar;
    if (retMemberAvatar.equals(MemberBean.MEMBER_AVATAR_USING_UPLOAD)
        || retMemberAvatar.startsWith(BinaryStorageService.BINARY_STORAGE)
        || retMemberAvatar.startsWith(MVNForumGlobal.UPLOADED_AVATAR_DIR)) {
      // relative path
      long timeTracking = MemberAvatarTimeTracking.getInstance().getTimeTracking(memberID);
      if (timeTracking != 0) {
        retMemberAvatar = "getavatar?memberid=" + memberID + "&time=" + timeTracking;
      } else {
        retMemberAvatar = "getavatar?memberid=" + memberID;
      }
      retMemberAvatar = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService()
          .encodeURL(request, response, retMemberAvatar);
    } else {
      // this mean use mvnForum's default avatar
      if (memberAvatar.startsWith("/mvnplugin")) {
        // no context prefix, so we prepend the contextPath
        retMemberAvatar = request.getContextPath() + memberAvatar;
      }
    }
    return retMemberAvatar;
  }

  public String getMemberHomepage_http() {
    if (memberHomepage == null) {
      memberHomepage = "";
    }
    return Encoder.filterUrl(memberHomepage);
    /*
     * String ret = memberHomepage.toLowerCase(); if ( (ret.length() > 0) &&
     * (!ret.startsWith("http://")) ) { ret = "http://" + ret; } return ret;
     */
  }


  public String getMemberCreationDate_short() {
    return DateUtil.getDateDDMMYYYY(memberCreationDate);
  }

  public String getMemberExpireDate_short() {
    return DateUtil.getDateDDMMYYYY(memberExpireDate);
  }

  public boolean isInvisible() {
    return (this.memberNameVisible == MEMBER_NAME_INVISIBLE);
  }

  public boolean showEmail() {
    return (this.memberEmailVisible == MEMBER_EMAIL_VISIBLE);
  }

  public void init() {
    // setMemberID(resultSet.getInt("MemberID"));
    Timestamp now = DateUtil.getCurrentGMTTimestamp();
    setMemberName("");
    setMemberFirstEmail("");
    setMemberEmail("");
    setMemberEmailVisible(MEMBER_EMAIL_INVISIBLE);
    setMemberNameVisible(MEMBER_NAME_VISIBLE);
    setMemberViewCount(0);
    setMemberPostCount(0);
    setMemberCreationDate(now);
    setMemberLastLogon(now);
    setMemberPasswordExpireDate(now);
    setMemberOption(0);
    setMemberStatus(MEMBER_STATUS_ENABLE);
    setMemberWarnCount(0);
    setMemberVoteCount(0);
    setMemberVoteTotalStars(0);
    setMemberRewardPoints(0);
    setMemberTitle("");
    setMemberSignature("");
    setMemberAvatar("");
    setMemberFirstname("");
    setMemberLastname("");
    setMemberGender(MEMBER_GENDER_MALE);
    setMemberBirthday(new java.sql.Date(0));
    setMemberAddress("");
    setMemberCity("");
    setMemberState("");
    setMemberCountry("");
    setMemberPhone("");
    setMemberMobile("");
    setMemberFax("");
    setMemberCareer("");
    setMemberHomepage("");
    setMemberActivateCode("");
  }

  public static void validateMemberStatus(int status) throws IllegalArgumentException {
    if ((status < MEMBER_STATUS_ENABLE) || (status > MEMBER_STATUS_DELETED)) {
      throw new IllegalArgumentException("Invalid MemberStatus = " + status);
    }
  }

} // end of class MemberBean
