/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/common/MemberMapping.java,v 1.11 2008/12/31 03:50:24 trungth Exp $
 * $Author: trungth $
 * $Revision: 1.11 $
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
 * @author: Phong Ta Quoc
 */
package com.mvnforum.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.configuration.DOM4JConfiguration;
import net.myvietnam.mvncore.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MemberMapping {
    
    private static final Logger log = LoggerFactory.getLogger(MemberMapping.class);

    private static final String SCHEMA_MAPPING_FILE = "schemaMapping.xml";

    private static final String DATABASE = "database";
    private static final String DISABLE  = "disable";

    private String memberName = DATABASE;
    private String memberPassword = DATABASE;
    private String memberFirstEmail = DATABASE;
    private String memberEmail = DATABASE;
    private String memberEmailVisible = DATABASE;
    private String memberNameVisible = DATABASE;
    private String memberFirstIP = DATABASE;
    private String memberLastIP = DATABASE;
    private String memberViewCount = DATABASE;
    private String memberPostCount = DATABASE;
    private String memberCreationDate = DATABASE;
    private String memberModifiedDate = DATABASE;
    private String memberExpireDate = DATABASE;
    private String memberLastLogon = DATABASE;
    private String memberOption = DATABASE;
    private String memberStatus = DATABASE;
    private String memberActivateCode = DATABASE;
    private String memberTempPassword = DATABASE;
    private String memberMessageCount = DATABASE;
    private String memberMessageOption = DATABASE;
    private String memberPostsPerPage = DATABASE;
    private String memberWarnCount = DATABASE;
    private String memberVoteCount = DATABASE;
    private String memberVoteTotalStars = DATABASE;
    private String memberRewardPoints = DATABASE;
    private String memberTitle = DATABASE;
    private String memberTimeZone = DATABASE;
    private String memberSignature = DATABASE;
    private String memberAvatar = DATABASE;
    private String memberSkin = DATABASE;
    private String memberLanguage = DATABASE;
    private String memberFirstname = DATABASE;
    private String memberLastname = DATABASE;
    private String memberGender = DATABASE;
    private String memberBirthday = DATABASE;
    private String memberAddress = DATABASE;
    private String memberCity = DATABASE;
    private String memberState = DATABASE;
    private String memberCountry = DATABASE;
    private String memberPhone = DATABASE;
    private String memberMobile = DATABASE;
    private String memberFax = DATABASE;
    private String memberCareer = DATABASE;
    private String memberHomepage = DATABASE;
    private String memberYahoo = DATABASE;
    private String memberAol = DATABASE;
    private String memberIcq = DATABASE;
    private String memberMsn = DATABASE;
    private String memberCoolLink1 = DATABASE;
    private String memberCoolLink2 = DATABASE;

    private Collection localFields = new ArrayList();
    private Collection externalFields = new ArrayList();
    private Collection externalFieldValues = new ArrayList();

    private static MemberMapping instance = new MemberMapping();

    private MemberMapping() {
        this.loadSchemaMapping();
    }

    public static MemberMapping getInstance() {
        return instance;
    }

    public String[] getLocalFields() {
        return (String[]) localFields.toArray(new String[0]);
    }

    public String[] getExternalFields() {
        return (String[]) externalFields.toArray(new String[0]);
    }

    public String[] getExternalFieldValues() {
        return (String[]) externalFieldValues.toArray(new String[0]);
    }

    private void loadSchemaMapping() {

        try {
            String configFilename = FileUtil.getServletClassesPath() + SCHEMA_MAPPING_FILE;

            DOM4JConfiguration mapping = new DOM4JConfiguration(new File (configFilename));

            memberName = mapping.getString("MemberName");
            checkField("MemberName", memberName);

            memberPassword = mapping.getString("MemberPassword");
            checkField("MemberPassword", memberPassword);

            memberFirstEmail = mapping.getString("MemberFirstEmail");
            checkField("MemberFirstEmail", memberFirstEmail);

            memberEmail = mapping.getString("MemberEmail");
            checkField("MemberEmail", memberEmail);

            memberEmailVisible = mapping.getString("MemberEmailVisible");
            checkField("MemberEmailVisible", memberEmailVisible);

            memberNameVisible = mapping.getString("MemberNameVisible");
            checkField("MemberNameVisible", memberNameVisible);

            memberFirstIP = mapping.getString("MemberFirstIP");
            checkField("MemberFirstIP", memberFirstIP);

            memberLastIP = mapping.getString("MemberLastIP");
            checkField("MemberLastIP", memberLastIP);

            memberViewCount = mapping.getString("MemberViewCount");
            checkField("MemberViewCount", memberViewCount);

            memberPostCount = mapping.getString("MemberPostCount");
            checkField("MemberPostCount", memberPostCount);

            memberCreationDate = mapping.getString("MemberCreationDate");
            checkField("MemberCreationDate", memberCreationDate);

            memberModifiedDate = mapping.getString("MemberModifiedDate");
            checkField("MemberModifiedDate", memberModifiedDate);

            memberExpireDate = mapping.getString("MemberExpireDate");
            checkField("MemberExpireDate", memberExpireDate);

            memberLastLogon = mapping.getString("MemberLastLogon");
            checkField("MemberLastLogon", memberLastLogon);

            memberOption = mapping.getString("MemberOption");
            checkField("MemberOption", memberOption);

            memberStatus = mapping.getString("MemberStatus");
            checkField("MemberStatus", memberStatus);

            memberActivateCode = mapping.getString("MemberActivateCode");
            checkField("MemberActivateCode", memberActivateCode);

            memberTempPassword = mapping.getString("MemberTempPassword");
            checkField("MemberTempPassword", memberTempPassword);

            memberMessageCount = mapping.getString("MemberMessageCount");
            checkField("MemberMessageCount", memberMessageCount);

            memberMessageOption = mapping.getString("MemberMessageOption");
            checkField("MemberMessageOption", memberMessageOption);

            memberPostsPerPage = mapping.getString("MemberPostsPerPage");
            checkField("MemberPostsPerPage", memberPostsPerPage);

            memberWarnCount = mapping.getString("MemberWarnCount");
            checkField("MemberWarnCount", memberWarnCount);

            memberVoteCount = mapping.getString("MemberVoteCount");
            checkField("MemberVoteCount", memberVoteCount);

            memberVoteTotalStars = mapping.getString("MemberVoteTotalStars");
            checkField("MemberVoteTotalStars", memberVoteTotalStars);

            memberRewardPoints = mapping.getString("MemberRewardPoints");
            checkField("MemberRewardPoints", memberRewardPoints);

            memberTitle = mapping.getString("MemberTitle");
            checkField("MemberTitle", memberTitle);

            memberTimeZone = mapping.getString("MemberTimeZone");
            checkField("MemberTimeZone", memberTimeZone);

            memberSignature = mapping.getString("MemberSignature");
            checkField("MemberSignature", memberSignature);

            memberAvatar = mapping.getString("MemberAvatar");
            checkField("MemberAvatar", memberAvatar);

            memberSkin = mapping.getString("MemberSkin");
            checkField("MemberSkin", memberSkin);

            memberLanguage = mapping.getString("MemberLanguage");
            checkField("MemberLanguage", memberLanguage);

            memberFirstname = mapping.getString("MemberFirstname");
            checkField("MemberFirstname", memberFirstname);

            memberLastname = mapping.getString("MemberLastname");
            checkField("MemberLastname", memberLastname);

            memberGender = mapping.getString("MemberGender");
            checkField("MemberGender", memberGender);

            memberBirthday = mapping.getString("MemberBirthday");
            checkField("MemberBirthday", memberBirthday);

            memberAddress = mapping.getString("MemberAddress");
            checkField("MemberAddress", memberAddress);

            memberCity = mapping.getString("MemberCity");
            checkField("MemberCity", memberCity);

            memberState = mapping.getString("MemberState");
            checkField("MemberState", memberState);

            memberCountry = mapping.getString("MemberCountry");
            checkField("MemberCountry", memberCountry);

            memberPhone = mapping.getString("MemberPhone");
            checkField("MemberPhone", memberPhone);

            memberMobile = mapping.getString("MemberMobile");
            checkField("MemberMobile", memberMobile);

            memberFax = mapping.getString("MemberFax");
            checkField("MemberFax", memberFax);

            memberCareer = mapping.getString("MemberCareer");
            checkField("MemberCareer", memberCareer);

            memberHomepage = mapping.getString("MemberHomepage");
            checkField("MemberHomepage", memberHomepage);

            memberYahoo = mapping.getString("MemberYahoo");
            checkField("MemberYahoo", memberYahoo);

            memberAol = mapping.getString("MemberAol");
            checkField("MemberAol", memberAol);

            memberIcq = mapping.getString("MemberIcq");
            checkField("MemberIcq", memberIcq);

            memberMsn = mapping.getString("MemberMsn");
            checkField("MemberMsn", memberMsn);

            memberCoolLink1 = mapping.getString("MemberCoolLink1");
            checkField("MemberCoolLink1", memberCoolLink1);

            memberCoolLink2 = mapping.getString("MemberCoolLink2");
            checkField("MemberCoolLink2", memberCoolLink2);
        } catch (Throwable e) {
            log.error("Cannot load the mapping file", e);
        }
    }

    private void checkField(String field, String mappingField) {
        if (isExternalField(mappingField)) {
            externalFields.add(field);
            externalFieldValues.add(mappingField);
        } else if (isLocalField(mappingField)) {
            localFields.add(field);
        }
    }

    public static boolean isLocalField(String field) {
        return field.equals(DATABASE);
    }

    public static boolean isExternalField(String field) {
        return ( (field.equals(DISABLE) == false) && (field.equals(DATABASE) == false) );
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberActivateCode() {
        return memberActivateCode;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public String getMemberAol() {
        return memberAol;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public String getMemberBirthday() {
        return memberBirthday;
    }

    public String getMemberCareer() {
        return memberCareer;
    }

    public String getMemberCity() {
        return memberCity;
    }

    public String getMemberCoolLink1() {
        return memberCoolLink1;
    }

    public String getMemberCoolLink2() {
        return memberCoolLink2;
    }

    public String getMemberCountry() {
        return memberCountry;
    }

    public String getMemberCreationDate() {
        return memberCreationDate;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getMemberEmailVisible() {
        return memberEmailVisible;
    }

    public String getMemberExpireDate() {
        return memberExpireDate;
    }

    public String getMemberFax() {
        return memberFax;
    }

    public String getMemberFirstEmail() {
        return memberFirstEmail;
    }

    public String getMemberFirstIP() {
        return memberFirstIP;
    }

    public String getMemberFirstname() {
        return memberFirstname;
    }

    public String getMemberGender() {
        return memberGender;
    }

    public String getMemberHomepage() {
        return memberHomepage;
    }

    public String getMemberIcq() {
        return memberIcq;
    }

    public String getMemberLanguage() {
        return memberLanguage;
    }

    public String getMemberLastIP() {
        return memberLastIP;
    }

    public String getMemberLastLogon() {
        return memberLastLogon;
    }

    public String getMemberLastname() {
        return memberLastname;
    }

    public String getMemberMessageCount() {
        return memberMessageCount;
    }

    public String getMemberMessageOption() {
        return memberMessageOption;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public String getMemberModifiedDate() {
        return memberModifiedDate;
    }

    public String getMemberMsn() {
        return memberMsn;
    }

    public String getMemberNameVisible() {
        return memberNameVisible;
    }

    public String getMemberOption() {
        return memberOption;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public String getMemberPostCount() {
        return memberPostCount;
    }

    public String getMemberPostsPerPage() {
        return memberPostsPerPage;
    }

    public String getMemberRewardPoints() {
        return memberRewardPoints;
    }

    public String getMemberSignature() {
        return memberSignature;
    }

    public String getMemberSkin() {
        return memberSkin;
    }

    public String getMemberState() {
        return memberState;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public String getMemberTempPassword() {
        return memberTempPassword;
    }

    public String getMemberTimeZone() {
        return memberTimeZone;
    }

    public String getMemberTitle() {
        return memberTitle;
    }

    public String getMemberViewCount() {
        return memberViewCount;
    }

    public String getMemberVoteCount() {
        return memberVoteCount;
    }

    public String getMemberVoteTotalStars() {
        return memberVoteTotalStars;
    }

    public String getMemberWarnCount() {
        return memberWarnCount;
    }

    public String getMemberYahoo() {
        return memberYahoo;
    }
}
