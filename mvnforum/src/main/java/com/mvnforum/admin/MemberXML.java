/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/MemberXML.java,v 1.23 2009/12/04 07:51:34 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.23 $
 * $Date: 2009/12/04 07:51:34 $
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
package com.mvnforum.admin;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.mvnforum.MVNForumConstant;
import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.auth.MVNForumPermission;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.MemberDAO;
import com.mvnforum.db.MemberPermissionDAO;
import com.mvnforum.db.MessageFolderDAO;
import com.mvnforum.db.WatchDAO;

import net.myvietnam.mvncore.db.DBUtils;
import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ExportException;
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

/**
 * @author Igor Manic
 * @version $Revision: 1.23 $, $Date: 2009/12/04 07:51:34 $
 * <br/>
 * <code>MemberXML</code> todo Igor: enter description
 *
 */
public class MemberXML {

    public MemberXML() {
        memberID = -1;
    }

    private int memberID;
    /** Returns <code>MemberID</code> of this member or
      * <code>-1</code> if member is not created yet. */
    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(String id) {
        memberID=XMLUtil.stringToIntDef(id, -1);
    }

    /**
     * Creates a member. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param memberName MemberName of a member to be created.
     * @param memberPassword Password of a member to be created.
     * @param memberFirstEmail FirstEmail of a member to be created.
     * @param memberEmail Email of a member to be created.
     * @param memberEmailVisible Can be null.
     * @param memberNameVisible Can be null.
     * @param memberFirstIP Can be null.
     * @param memberLastIP Can be null.
     * @param memberViewCount Can be null.
     * @param memberPostCount Can be null.
     * @param memberCreationDate Can be null.
     * @param memberModifiedDate Can be null.
     * @param memberExpireDate Can be null.
     * @param memberLastLogon Can be null.
     * @param memberOption Can be null.
     * @param memberStatus Can be null.
     * @param memberActivateCode Can be null.
     * @param memberTempPassword Can be null.
     * @param memberMessageCount Can be null.
     * @param memberMessageOption Can be null.
     * @param memberPostsPerPage Can be null.
     * @param memberWarnCount Can be null.
     * @param memberVoteCount Can be null.
     * @param memberVoteTotalStars Can be null.
     * @param memberRewardPoints Can be null.
     * @param memberTitle Can be null.
     * @param memberTimeZone Can be null.
     * @param memberSignature Can be null.
     * @param memberAvatar Can be null.
     * @param memberSkin Can be null.
     * @param memberLanguage Can be null.
     * @param memberFirstname
     * @param memberLastname
     * @param memberGender Can be null.
     * @param memberBirthday Can be null.
     * @param memberAddress Can be null.
     * @param memberCity Can be null.
     * @param memberState Can be null.
     * @param memberCountry Can be null.
     * @param memberPhone Can be null.
     * @param memberMobile Can be null.
     * @param memberFax Can be null.
     * @param memberCareer Can be null.
     * @param memberHomepage Can be null.
     * @param memberYahoo Can be null.
     * @param memberAol Can be null.
     * @param memberIcq Can be null.
     * @param memberMsn Can be null.
     * @param memberCoolLink1 Can be null.
     * @param memberCoolLink2 Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     *
     */
    public void addMember(String memberName, String memberPassword,
                      String memberFirstEmail, String memberEmail,
                      String memberEmailVisible, String memberNameVisible,
                      String memberFirstIP, String memberLastIP,
                      String memberViewCount, String memberPostCount,
                      String memberCreationDate, String memberModifiedDate, String memberExpireDate, String memberPasswordExpireDate,
                      String memberLastLogon,
                      String memberOption, String memberStatus,
                      String memberActivateCode, String memberTempPassword,
                      String memberMessageCount, String memberMessageOption,
                      String memberPostsPerPage,
                      String memberWarnCount,
                      String memberVoteCount, String memberVoteTotalStars,
                      String memberRewardPoints,
                      String memberTitle, String memberTimeZone,
                      String memberSignature, String memberAvatar,
                      String memberSkin, String memberLanguage,
                      String memberFirstname, String memberLastname,
                      String memberGender, String memberBirthday,
                      String memberAddress, String memberCity,
                      String memberState, String memberCountry,
                      String memberPhone, String memberMobile,
                      String memberFax, String memberCareer,
                      String memberHomepage)
    throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException {
        String strMemberID=null;
        if (memberID >= 0) {
            strMemberID = Integer.toString(memberID);
        }

        addMember(strMemberID,
                memberName, memberPassword,
                memberFirstEmail, memberEmail,
                memberEmailVisible, memberNameVisible,
                memberFirstIP, memberLastIP,
                memberViewCount, memberPostCount,
                memberCreationDate, memberModifiedDate, memberExpireDate, memberPasswordExpireDate,
                memberLastLogon,
                memberOption, memberStatus,
                memberActivateCode, memberTempPassword,
                memberMessageCount, memberMessageOption,
                memberPostsPerPage,
                memberWarnCount,
                memberVoteCount, memberVoteTotalStars,
                memberRewardPoints,
                memberTitle, memberTimeZone,
                memberSignature, memberAvatar,
                memberSkin, memberLanguage,
                memberFirstname, memberLastname,
                memberGender, memberBirthday,
                memberAddress, memberCity,
                memberState, memberCountry,
                memberPhone, memberMobile,
                memberFax, memberCareer,
                memberHomepage);
    }

    /**
     * Adds a member. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param strMemberID Can be null, and it probably will be in most occasions,
     *                    except when you want to setup an explicit value, like
     *                    "0" for guest or "1" for root system admin.
     * @param memberName MemberName of a member to be created.
     * @param memberPassword Password of a member to be created.
     * @param memberFirstEmail FirstEmail of a member to be created.
     * @param memberEmail Email of a member to be created.
     * @param memberEmailVisible Can be null.
     * @param memberNameVisible Can be null.
     * @param memberFirstIP Can be null.
     * @param memberLastIP Can be null.
     * @param memberViewCount Can be null.
     * @param memberPostCount Can be null.
     * @param memberCreationDate Can be null.
     * @param memberModifiedDate Can be null.
     * @param memberLastLogon Can be null.
     * @param memberOption Can be null.
     * @param memberStatus Can be null.
     * @param memberActivateCode Can be null.
     * @param memberTempPassword Can be null.
     * @param memberMessageCount Can be null.
     * @param memberMessageOption Can be null.
     * @param memberPostsPerPage Can be null.
     * @param memberWarnCount Can be null.
     * @param memberVoteCount Can be null.
     * @param memberVoteTotalStars Can be null.
     * @param memberRewardPoints Can be null.
     * @param memberTitle Can be null.
     * @param memberTimeZone Can be null.
     * @param memberSignature Can be null.
     * @param memberAvatar Can be null.
     * @param memberSkin Can be null.
     * @param memberLanguage Can be null.
     * @param memberFirstname Can't be null, but may be empty "", but please avoid that.
     * @param memberLastname Can't be null, but may be empty "", but please avoid that.
     * @param memberGender Can be null.
     * @param memberBirthday Can be null.
     * @param memberAddress Can be null.
     * @param memberCity Can be null.
     * @param memberState Can be null.
     * @param memberCountry Can be null.
     * @param memberPhone Can be null.
     * @param memberMobile Can be null.
     * @param memberFax Can be null.
     * @param memberCareer Can be null.
     * @param memberHomepage Can be null.
     * @param memberYahoo Can be null.
     * @param memberAol Can be null.
     * @param memberIcq Can be null.
     * @param memberMsn Can be null.
     * @param memberCoolLink1 Can be null.
     * @param memberCoolLink2 Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     *
     */
    public void addMember(String strMemberID,
                          String memberName, String memberPassword,
                          String memberFirstEmail, String memberEmail,
                          String memberEmailVisible, String memberNameVisible,
                          String memberFirstIP, String memberLastIP,
                          String memberViewCount, String memberPostCount,
                          String memberCreationDate, String memberModifiedDate, String memberExpireDate, String memberPasswordExpireDate,
                          String memberLastLogon,
                          String memberOption, String memberStatus,
                          String memberActivateCode, String memberTempPassword,
                          String memberMessageCount, String memberMessageOption,
                          String memberPostsPerPage,
                          String memberWarnCount,
                          String memberVoteCount, String memberVoteTotalStars,
                          String memberRewardPoints,
                          String memberTitle, String memberTimeZone,
                          String memberSignature, String memberAvatar,
                          String memberSkin, String memberLanguage,
                          String memberFirstname, String memberLastname,
                          String memberGender, String memberBirthday,
                          String memberAddress, String memberCity,
                          String memberState, String memberCountry,
                          String memberPhone, String memberMobile,
                          String memberFax, String memberCareer,
                          String memberHomepage)
    throws CreateException, DuplicateKeyException, ObjectNotFoundException, DatabaseException {
        if ((memberName==null) || (memberName.equals("")) ||
            (memberPassword==null) || (memberPassword.equals("")) ||
            (memberFirstEmail==null) || (memberFirstEmail.equals("")) ||
            (memberEmail==null) || (memberEmail.equals("")) ||
            (memberFirstname==null) || //first and last name can be empty
            (memberLastname==null)) {
            throw new CreateException("Not enough data to create a member. Check for name, email and password.");
        } else {
            int memberEmailVisible1;
            int memberNameVisible1;
            int memberViewCount1;
            int memberPostCount1;
            java.sql.Timestamp memberCreationDate1;
            java.sql.Timestamp memberModifiedDate1;
            java.sql.Timestamp memberExpireDate1;
            java.sql.Timestamp memberPasswordExpireDate1;
            java.sql.Timestamp memberLastLogon1;
            int memberOption1;
            int memberStatus1;
            int memberMessageCount1;
            int memberMessageOption1;
            int memberPostsPerPage1;
            int memberWarnCount1;
            int memberVoteCount1;
            int memberVoteTotalStars1;
            int memberRewardPoints1;
            int memberTimeZone1;
            int memberGender1;
            java.sql.Date memberBirthday1;

            try {
                memberEmailVisible1 = (XMLUtil.stringToBooleanDef(memberEmailVisible, false)?1:0);
                memberNameVisible1 = (XMLUtil.stringToBooleanDef(memberNameVisible, false)?1:0);
                if (memberFirstIP == null) {
                    memberFirstIP = "0.0.0.0";
                }
                if (memberLastIP == null) {
                    memberLastIP = "0.0.0.0";
                }
                memberViewCount1= XMLUtil.stringToIntDef(memberViewCount, 0);
                memberPostCount1= XMLUtil.stringToIntDef(memberPostCount, 0);
                memberCreationDate1= XMLUtil.stringToSqlTimestampDefNow(memberCreationDate);
                memberModifiedDate1= XMLUtil.stringToSqlTimestampDefNull(memberModifiedDate);
                memberExpireDate1= XMLUtil.stringToSqlTimestampDefNow(memberExpireDate);
                memberPasswordExpireDate1= XMLUtil.stringToSqlTimestampDefNow(memberPasswordExpireDate);
                memberLastLogon1= XMLUtil.stringToSqlTimestampDefNull(memberLastLogon);
                memberOption1= XMLUtil.stringToIntDef(memberOption, 0);
                memberStatus1= XMLUtil.stringToIntDef(memberStatus, 0);
                if (memberActivateCode == null) {
                    memberActivateCode = "";
                }
                if (memberTempPassword == null) {
                    memberTempPassword = "";
                }
                memberMessageCount1= XMLUtil.stringToIntDef(memberMessageCount, 0);
                memberMessageOption1= XMLUtil.stringToIntDef(memberMessageOption, 0);
                memberPostsPerPage1= XMLUtil.stringToIntDef(memberPostsPerPage, 10);
                memberWarnCount1= XMLUtil.stringToIntDef(memberWarnCount, 0);
                memberVoteCount1= XMLUtil.stringToIntDef(memberVoteCount, 0);
                memberVoteTotalStars1= XMLUtil.stringToIntDef(memberVoteTotalStars, 0);
                memberRewardPoints1= XMLUtil.stringToIntDef(memberRewardPoints, 0);
                if (memberTitle == null) {
                    memberTitle = "";
                }
                memberTimeZone1= XMLUtil.stringToIntDef(memberTimeZone, 0);
                if (memberSignature == null) {
                    memberSignature = "";
                }
                if (memberAvatar == null) {
                    memberAvatar = "";
                }
                if (memberSkin == null) {
                    memberSkin = "";
                }
                if (memberLanguage == null) {
                    memberLanguage = "";
                }
                memberGender1 = XMLUtil.stringToGenderDef(memberGender, 1/*Male*/);
                memberBirthday1 = XMLUtil.stringToSqlDateDefNull(memberBirthday);
                if (memberAddress == null) {
                    memberAddress = "";
                }
                if (memberCity == null) {
                    memberCity = "";
                }
                if (memberState == null) {
                    memberState = "";
                }
                if (memberCountry == null) {
                    memberCountry = "";
                }
                if (memberPhone == null) {
                    memberPhone = "";
                }
                if (memberMobile == null) {
                    memberMobile = "";
                }
                if (memberFax == null) {
                    memberFax = "";
                }
                if (memberCareer == null) {
                    memberCareer = "";
                }
                if (memberHomepage == null) {
                    memberHomepage = "";
                }
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a member. Expected a number.");
            }

            //now ensure that strMemberID is valid number, or null
            if ( (strMemberID!=null) && (!strMemberID.equals("")) ) {
                try {
                    if (Integer.parseInt(strMemberID)<0) {
                        strMemberID = null;
                    }
                } catch (NumberFormatException e) {
                    strMemberID = null;
                }
            } else {
                strMemberID = null;
            }

            memberPassword = EnableHtmlTagFilter.filter(memberPassword);
            memberTempPassword = EnableHtmlTagFilter.filter(memberTempPassword);
            memberTitle = EnableHtmlTagFilter.filter(memberTitle);
            memberSignature = EnableHtmlTagFilter.filter(memberSignature);
            //todo Igor: also filter memberAvatar, memberSkin
            memberAddress = EnableHtmlTagFilter.filter(memberAddress);
            memberCountry = EnableHtmlTagFilter.filter(memberCountry);
            memberHomepage = EnableHtmlTagFilter.filter(memberHomepage);

            if (strMemberID == null) {
                DAOFactory.getMemberDAO().create(
                   memberName, memberPassword, memberFirstEmail,
                   memberEmail, memberEmailVisible1, memberNameVisible1,
                   memberFirstIP, memberLastIP, memberViewCount1, memberPostCount1,
                   memberCreationDate1, memberModifiedDate1, memberExpireDate1, memberPasswordExpireDate1, memberLastLogon1,
                   memberOption1, memberStatus1, memberActivateCode, memberTempPassword,
                   memberMessageCount1, memberMessageOption1, memberPostsPerPage1,
                   memberWarnCount1, memberVoteCount1, memberVoteTotalStars1,
                   memberRewardPoints1, memberTitle, memberTimeZone1, memberSignature,
                   memberAvatar, memberSkin, memberLanguage,
                   memberFirstname, memberLastname, memberGender1,
                   memberBirthday1, memberAddress, memberCity,
                   memberState, memberCountry, memberPhone,
                   memberMobile, memberFax, memberCareer,
                   memberHomepage);
            } else {
                //I must change all possible nulls into "", so I don't get "'null'" in sql query
                String memberEmailVisible2 = (XMLUtil.stringToBooleanDef(memberEmailVisible, false)?"1":"0");
                String memberNameVisible2 = (XMLUtil.stringToBooleanDef(memberNameVisible, false)?"1":"0");
                String memberModifiedDate2= XMLUtil.sqlTimestampToStringDefEmpty(memberModifiedDate1);
                String memberLastLogon2= XMLUtil.sqlTimestampToStringDefEmpty(memberLastLogon1);
                String memberBirthday2= XMLUtil.sqlDateToStringDefEmpty(memberBirthday1);

                String query="INSERT INTO "+ MemberDAO.TABLE_NAME +
                    " (MemberID, MemberName, MemberPassword, MemberFirstEmail, MemberEmail," +
                    " MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP," +
                    " MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, MemberPasswordExpireDate," +
                    " MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode," +
                    " MemberTempPassword, MemberMessageCount, MemberMessageOption," +
                    " MemberPostsPerPage, MemberWarnCount, MemberVoteCount," +
                    " MemberVoteTotalStars, MemberRewardPoints, MemberTitle," +
                    " MemberTimeZone, MemberSignature, MemberAvatar, MemberSkin," +
                    " MemberLanguage, MemberFirstname, MemberLastname, MemberGender," +
                    " MemberBirthday, MemberAddress, MemberCity, MemberState, MemberCountry," +
                    " MemberPhone, MemberMobile, MemberFax, MemberCareer, MemberHomepage)" +
                    " VALUES (" +strMemberID+ ", '"+memberName +"', '" +memberPassword+ "', '" +memberFirstEmail+
                    "', '" +memberEmail+ "', " +memberEmailVisible2+ ", " +memberNameVisible2+
                    ", '" +memberFirstIP+ "', '" +memberLastIP+ "', " +memberViewCount1+
                    ", " +memberPostCount1+ ", '" +memberCreationDate1+ "', '" +memberModifiedDate2+ "', '" + memberExpireDate1 + "', '" + memberExpireDate1 +
                    "', '" +memberLastLogon2+ "', " +memberOption1+ ", " +memberStatus1+
                    ", '" +memberActivateCode+ "', '" +memberTempPassword+ "', " +memberMessageCount1+
                    ", " +memberMessageOption1+ ", " +memberPostsPerPage1+ ", " +memberWarnCount1+
                    ", " +memberVoteCount1+ ", " +memberVoteTotalStars1+ ", " +memberRewardPoints1+
                    ", '" +memberTitle+ "', " +memberTimeZone1+ ", '" +memberSignature+
                    "', '" +memberAvatar+ "', '" +memberSkin+ "', '" +memberLanguage+
                    "', '" +memberFirstname+ "', '" +memberLastname+ "', " +memberGender1+
                    ", '" +memberBirthday2+ "', '" +memberAddress+ "', '" +memberCity+
                    "', '" +memberState+ "', '" +memberCountry+ "', '" +memberPhone+
                    "', '" +memberMobile+ "', '" +memberFax+ "', '" +memberCareer+
                    "', '" +memberHomepage+ "')";
                if (DBUtils.getDatabaseType() == DBUtils.DATABASE_SQLSERVER) {
                    query = "SET IDENTITY_INSERT " + MemberDAO.TABLE_NAME + " ON;" + query + ";SET IDENTITY_INSERT " + MemberDAO.TABLE_NAME + " OFF;";
                }
                if (ImportWebHelper.execUpdateQuery(query) != 1) {
                  throw new CreateException("Error adding member into table '" + MemberDAO.TABLE_NAME +"'.");
                }
            }
            this.memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }
    }

    /**
     * Adds a permission to this member. In order to know which member we are
     * referring to, this method is supposed to be called after {@link #setMemberID(String)},
     * {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * or {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this permission will be simply ignored.
     *
     * @param permission Permission to be added.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addMemberPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        if (memberID < 0) {
            throw new CreateException("Found member permission that is not assigned to any known member.");
        }
        int permission1;
        try {
            permission1=XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a member permission. Expected a number.");
        }
        try {
            DAOFactory.getMemberPermissionDAO().create(memberID, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public static void addGuestMemberPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        int permission1;
        try {
            permission1=XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a guest member permission. Expected a number.");
        }
        try {
            DAOFactory.getMemberPermissionDAO().create(MVNForumConstant.MEMBER_ID_OF_GUEST, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public static void addAdminMemberPermission(String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException {
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a guest member permission. Expected a number.");
        }
        try {
            DAOFactory.getMemberPermissionDAO().create(MVNForumConstant.MEMBER_ID_OF_ADMIN, permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    public static void addMemberPermission(String memberName, String permission)
        throws CreateException, DatabaseException, ForeignKeyNotFoundException, ObjectNotFoundException {
        int permission1;
        try {
            permission1 = XMLUtil.stringToIntDef(permission, MVNForumPermission.PERMISSION_NO_PERMISSIONS);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a member permission. Expected a number.");
        }
        try {
            DAOFactory.getMemberPermissionDAO().create(DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName), permission1);
        } catch (DuplicateKeyException e) {
            //ignore if already had that permission
        }
    }

    /**
     * Creates a message folder for this member. In order to know which member we are
     * referring to, this method is supposed to be called after {@link #setMemberID(String)},
     * {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * or {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this message folder will be simply ignored.
     *
     * @param folderName Name of a folder to be created.
     * @param folderOrder Can be null.
     * @param folderCreationDate Can be null.
     * @param folderModifiedDate Can be null.
     *
     * @throws CreateException
     * @throws DatabaseException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     */
    public void addMessageFolder(String folderName, String folderOrder,
                String folderCreationDate, String folderModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        if (memberID < 0) {
            throw new CreateException("Found message folder that is not assigned to any known member.");
        }
        if ( (folderName==null) || (folderName.equals("")) ) {
            throw new CreateException("Can't create a message folder with empty FolderName.");
        }

        int folderOrder1;
        java.sql.Timestamp folderCreationDate1;
        java.sql.Timestamp folderModifiedDate1;
        try {
            folderOrder1 = XMLUtil.stringToIntDef(folderOrder, 0);
            folderCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(folderCreationDate);
            folderModifiedDate1 = XMLUtil.stringToSqlTimestampDefNow(folderModifiedDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a message folder. Expected a number.");
        }

        folderName=EnableHtmlTagFilter.filter(folderName);
        int folderStatus = 0;
        int folderOption = 0;
        int folderType = 0;
        DAOFactory.getMessageFolderDAO().create(folderName, memberID, folderOrder1,
                                                folderStatus, folderOption, folderType,
                                                folderCreationDate1, folderModifiedDate1);
    }

    /**
     * Adds a global watch for this member. In order to know which member we are
     * referring to, this method is supposed to be called after {@link #setMemberID(String)},
     * {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * or {@link #addMember(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this watch will be simply ignored.
     *
     * @param watchType Can be null.
     * @param watchOption Can be null.
     * @param watchStatus Can be null.
     * @param watchCreationDate Can be null.
     * @param watchLastSentDate Can be null.
     * @param watchEndDate Can be null.
     *
     * @throws BadInputException
     * @throws CreateException
     * @throws DatabaseException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     */
    public void addGlobalWatch(String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException {
        if (memberID<0) {
            throw new CreateException("Found global watch that is not assigned to any known member.");
        }

        int watchType1;
        int watchOption1;
        int watchStatus1;
        java.sql.Timestamp watchCreationDate1;
        java.sql.Timestamp watchLastSentDate1;
        java.sql.Timestamp watchEndDate1;

        try {
            watchType1= XMLUtil.stringToIntDef(watchType, 0);
            watchOption1= XMLUtil.stringToIntDef(watchOption, 0);
            watchStatus1= XMLUtil.stringToIntDef(watchStatus, 0);
            watchCreationDate1= XMLUtil.stringToSqlTimestampDefNow(watchCreationDate);
            watchLastSentDate1= XMLUtil.stringToSqlTimestampDefNull(watchLastSentDate);
            watchEndDate1= XMLUtil.stringToSqlTimestampDefNull(watchEndDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a global watch. Expected a number.");
        }

        DAOFactory.getWatchDAO().create(
             memberID, 0/*categoryID*/, 0/*forumID*/, 0/*threadID*/,
             watchType1, watchOption1, watchStatus1,
             watchCreationDate1, watchLastSentDate1, watchEndDate1);
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================

    public static void exportMessageFoldersForMember(XMLWriter xmlWriter, String memberName)
        throws ForeignKeyNotFoundException, IOException, DatabaseException, ExportException {
        try {
            exportMessageFoldersForMember(xmlWriter,
                  DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName));
        } catch (ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Can't find member with name \""+memberName+"\".");
        }
    }

    public static void exportMessageFoldersForMember(XMLWriter xmlWriter, int memberID)
        throws IOException, DatabaseException, ExportException {
        Collection messageFolders=ExportWebHelper.execSqlQuery(
                   "SELECT FolderName, FolderOrder, FolderCreationDate, FolderModifiedDate"+
                   " FROM "+MessageFolderDAO.TABLE_NAME+
                   " WHERE MemberID="+Integer.toString(memberID));
        Iterator iter = messageFolders.iterator();
        String[] messageFolder=null;
        //try {
            xmlWriter.startElement("MessageFolderList");
            try {
                while ( (messageFolder=(String[])iter.next()) != null) {
                    if (messageFolder.length != 4) {
                        throw new ExportException("Error while retrieving data about message folder for memberID=="+memberID);
                    }
                    xmlWriter.startElement("MessageFolder");
                    xmlWriter.startElement("FolderName");
                    xmlWriter.writeData(DisableHtmlTagFilter.filter(messageFolder[0]));
                    xmlWriter.endElement("FolderName");
                    xmlWriter.startElement("FolderOrder");
                    xmlWriter.writeData(messageFolder[1]);
                    xmlWriter.endElement("FolderOrder");
                    xmlWriter.startElement("FolderCreationDate");
                    xmlWriter.writeData(messageFolder[2]);
                    xmlWriter.endElement("FolderCreationDate");
                    xmlWriter.startElement("FolderModifiedDate");
                    xmlWriter.writeData(messageFolder[3]);
                    xmlWriter.endElement("FolderModifiedDate");
                    xmlWriter.endElement("MessageFolder");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("MessageFolderList");
         //} catch throw exportexception
    }

    public static void exportGlobalPermissionsForMember(XMLWriter xmlWriter, String memberName)
        throws ForeignKeyNotFoundException, IOException, DatabaseException, ExportException {
        try {
            exportGlobalPermissionsForMember(xmlWriter,
                  DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName));
        } catch (ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Can't find member with name \"" + memberName + "\".");
        }
    }

    public static void exportGlobalPermissionsForMember(XMLWriter xmlWriter, int memberID)
        throws IOException, DatabaseException, ExportException {
        Collection globalPermissions=ExportWebHelper.execSqlQuery(
                   "SELECT Permission"+
                   " FROM "+MemberPermissionDAO.TABLE_NAME+
                   " WHERE MemberID="+Integer.toString(memberID));
        Iterator iter=globalPermissions.iterator();
        String[] globalPermission=null;
        //try {
            xmlWriter.startElement("GlobalPermissionList");
            try {
                while ( (globalPermission=(String[])iter.next()) !=null) {
                    if (globalPermission.length!=1) {
                        throw new ExportException("Error while retrieving data about global permissions for memberID=="+memberID);
                    }
                    xmlWriter.startElement("GlobalPermission");
                    xmlWriter.writeData(globalPermission[0]);
                    xmlWriter.endElement("GlobalPermission");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GlobalPermissionList");
         //} catch throw exportexception
    }

    public static void exportGlobalWatchesForMember(XMLWriter xmlWriter, String memberName)
        throws ForeignKeyNotFoundException, IOException, DatabaseException, ExportException {
        try {
            exportGlobalWatchesForMember(xmlWriter,
                  DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName));
        } catch (ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Can't find member with name \""+memberName+"\".");
        }
    }

    public static void exportGlobalWatchesForMember(XMLWriter xmlWriter, int memberID)
        throws IOException, DatabaseException, ExportException {

        Collection globalWatches=ExportWebHelper.execSqlQuery(
                   "SELECT WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate"+
                   " FROM "+WatchDAO.TABLE_NAME+
                   " WHERE CategoryID=0 AND ForumID=0 AND ThreadID=0"+
                   " AND MemberID="+Integer.toString(memberID));
        Iterator iter=globalWatches.iterator();
        String[] globalWatch=null;
        //try {
            xmlWriter.startElement("GlobalWatchList");
            try {
                while ( (globalWatch=(String[])iter.next()) != null) {
                    if (globalWatch.length!=6) {
                        throw new ExportException("Error while retrieving data about global watch for memberID==" + memberID);
                    }
                    xmlWriter.startElement("GlobalWatch");
                    xmlWriter.startElement("WatchType");
                    xmlWriter.writeData(globalWatch[0]);
                    xmlWriter.endElement("WatchType");
                    xmlWriter.startElement("WatchOption");
                    xmlWriter.writeData(globalWatch[1]);
                    xmlWriter.endElement("WatchOption");
                    xmlWriter.startElement("WatchStatus");
                    xmlWriter.writeData(globalWatch[2]);
                    xmlWriter.endElement("WatchStatus");
                    xmlWriter.startElement("WatchCreationDate");
                    xmlWriter.writeData(globalWatch[3]);
                    xmlWriter.endElement("WatchCreationDate");
                    xmlWriter.startElement("WatchLastSentDate");
                    xmlWriter.writeData(globalWatch[4]);
                    xmlWriter.endElement("WatchLastSentDate");
                    xmlWriter.startElement("WatchEndDate");
                    xmlWriter.writeData(globalWatch[5]);
                    xmlWriter.endElement("WatchEndDate");
                    xmlWriter.endElement("GlobalWatch");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("GlobalWatchList");
         //} catch throw exportexception
    }

    public static void exportMember(XMLWriter xmlWriter, String memberName)
        throws ForeignKeyNotFoundException, IOException, DatabaseException, ExportException {
        try {
            exportMember(xmlWriter,
                  DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName));
        } catch (ObjectNotFoundException e) {
            throw new ForeignKeyNotFoundException("Can't find member with name \""+memberName+"\".");
        }
    }

    public static void exportMember(XMLWriter xmlWriter, int memberID)
        throws IOException, DatabaseException, ExportException {
        Collection member1=ExportWebHelper.execSqlQuery(
                   "SELECT MemberName, MemberPassword, MemberFirstEmail, MemberEmail,"+
                   " MemberEmailVisible, MemberNameVisible, MemberFirstIP, MemberLastIP,"+
                   " MemberViewCount, MemberPostCount, MemberCreationDate, MemberModifiedDate, MemberExpireDate, "+
                   " MemberLastLogon, MemberOption, MemberStatus, MemberActivateCode,"+
                   " MemberTempPassword, MemberMessageCount, MemberMessageOption, MemberPostsPerPage,"+
                   " MemberWarnCount, MemberVoteCount, MemberVoteTotalStars, MemberRewardPoints,"+
                   " MemberTitle, MemberTimeZone, MemberSignature, MemberAvatar,"+
                   " MemberSkin, MemberLanguage, MemberFirstname, MemberLastname,"+
                   " MemberGender, MemberBirthday, MemberAddress, MemberCity,"+
                   " MemberState, MemberCountry, MemberPhone, MemberMobile,"+
                   " MemberFax, MemberCareer, MemberHomepage,"+
                   " FROM "+
                   MemberDAO.TABLE_NAME+
                   " WHERE MemberID="+Integer.toString(memberID));
        Iterator iter=member1.iterator();
        String[] member=null;
        //try {
            try {
                if ( (member=(String[])iter.next()) == null) {
                    throw new ExportException("Can't find data for memberID==" + memberID);
                }
                if (member.length!=50) {
                    throw new ExportException("Error while retrieving data about member with memberID==" + memberID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for memberID==" + memberID);
            }

            //if I am here, that means I now have correct object member
            if (memberID == MVNForumConstant.MEMBER_ID_OF_GUEST) {
                xmlWriter.startElement("Member", new String[]{"class", "Guest"});
            } else if (memberID == MVNForumConstant.MEMBER_ID_OF_ADMIN) {
                xmlWriter.startElement("Member", new String[]{"class", "Admin"});
            } else {
                xmlWriter.startElement("Member");
            }

            xmlWriter.startElement("MemberName");
            xmlWriter.writeData(member[0]);
            xmlWriter.endElement("MemberName");
            xmlWriter.startElement("MemberPassword");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[1]));
            xmlWriter.endElement("MemberPassword");
            xmlWriter.startElement("MemberFirstEmail");
            xmlWriter.writeData(member[2]);
            xmlWriter.endElement("MemberFirstEmail");
            xmlWriter.startElement("MemberEmail");
            xmlWriter.writeData(member[3]);
            xmlWriter.endElement("MemberEmail");
            xmlWriter.startElement("MemberEmailVisible");
            xmlWriter.writeData(member[4]);
            xmlWriter.endElement("MemberEmailVisible");
            xmlWriter.startElement("MemberNameVisible");
            xmlWriter.writeData(member[5]);
            xmlWriter.endElement("MemberNameVisible");
            xmlWriter.startElement("MemberFirstIP");
            xmlWriter.writeData(member[6]);
            xmlWriter.endElement("MemberFirstIP");
            xmlWriter.startElement("MemberLastIP");
            xmlWriter.writeData(member[7]);
            xmlWriter.endElement("MemberLastIP");
            xmlWriter.startElement("MemberViewCount");
            xmlWriter.writeData(member[8]);
            xmlWriter.endElement("MemberViewCount");
            xmlWriter.startElement("MemberPostCount");
            xmlWriter.writeData(member[9]);
            xmlWriter.endElement("MemberPostCount");

            xmlWriter.startElement("MemberCreationDate");
            xmlWriter.writeData(member[10]);
            xmlWriter.endElement("MemberCreationDate");
            xmlWriter.startElement("MemberModifiedDate");
            xmlWriter.writeData(member[11]);
            xmlWriter.endElement("MemberModifiedDate");
            xmlWriter.startElement("MemberExpireDate");
            xmlWriter.writeData(member[12]);
            xmlWriter.endElement("MemberExpireDate");
            xmlWriter.startElement("MemberLastLogon");
            xmlWriter.writeData(member[13]);
            xmlWriter.endElement("MemberLastLogon");
            xmlWriter.startElement("MemberOption");
            xmlWriter.writeData(member[14]);
            xmlWriter.endElement("MemberOption");
            xmlWriter.startElement("MemberStatus");
            xmlWriter.writeData(member[15]);
            xmlWriter.endElement("MemberStatus");
            xmlWriter.startElement("MemberActivateCode");
            xmlWriter.writeData(member[16]);
            xmlWriter.endElement("MemberActivateCode");
            xmlWriter.startElement("MemberTempPassword");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[17]));
            xmlWriter.endElement("MemberTempPassword");
            xmlWriter.startElement("MemberMessageCount");
            xmlWriter.writeData(member[18]);
            xmlWriter.endElement("MemberMessageCount");
            xmlWriter.startElement("MemberMessageOption");
            xmlWriter.writeData(member[19]);
            xmlWriter.endElement("MemberMessageOption");
            xmlWriter.startElement("MemberPostsPerPage");
            xmlWriter.writeData(member[20]);
            xmlWriter.endElement("MemberPostsPerPage");

            xmlWriter.startElement("MemberWarnCount");
            xmlWriter.writeData(member[21]);
            xmlWriter.endElement("MemberWarnCount");
            xmlWriter.startElement("MemberVoteCount");
            xmlWriter.writeData(member[22]);
            xmlWriter.endElement("MemberVoteCount");
            xmlWriter.startElement("MemberVoteTotalStars");
            xmlWriter.writeData(member[23]);
            xmlWriter.endElement("MemberVoteTotalStars");
            xmlWriter.startElement("MemberRewardPoints");
            xmlWriter.writeData(member[24]);
            xmlWriter.endElement("MemberRewardPoints");
            xmlWriter.startElement("MemberTitle");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[25]));
            xmlWriter.endElement("MemberTitle");
            xmlWriter.startElement("MemberTimeZone");
            xmlWriter.writeData(member[26]);
            xmlWriter.endElement("MemberTimeZone");
            xmlWriter.startElement("MemberSignature");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[27]));
            xmlWriter.endElement("MemberSignature");
            //todo Igor: also filter memberAvatar, memberSkin
            xmlWriter.startElement("MemberAvatar");
            xmlWriter.writeData(member[28]);
            xmlWriter.endElement("MemberAvatar");
            xmlWriter.startElement("MemberSkin");
            xmlWriter.writeData(member[29]);
            xmlWriter.endElement("MemberSkin");
            xmlWriter.startElement("MemberLanguage");
            xmlWriter.writeData(member[30]);
            xmlWriter.endElement("MemberLanguage");

            xmlWriter.startElement("MemberFirstname");
            xmlWriter.writeData(member[31]);
            xmlWriter.endElement("MemberFirstname");
            xmlWriter.startElement("MemberLastname");
            xmlWriter.writeData(member[32]);
            xmlWriter.endElement("MemberLastname");
            xmlWriter.startElement("MemberGender");
            xmlWriter.writeData(member[33]);
            xmlWriter.endElement("MemberGender");
            xmlWriter.startElement("MemberBirthday");
            xmlWriter.writeData(member[34]);
            xmlWriter.endElement("MemberBirthday");
            xmlWriter.startElement("MemberAddress");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[35]));
            xmlWriter.endElement("MemberAddress");
            xmlWriter.startElement("MemberCity");
            xmlWriter.writeData(member[36]);
            xmlWriter.endElement("MemberCity");
            xmlWriter.startElement("MemberState");
            xmlWriter.writeData(member[37]);
            xmlWriter.endElement("MemberState");
            xmlWriter.startElement("MemberCountry");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[38]));
            xmlWriter.endElement("MemberCountry");
            xmlWriter.startElement("MemberPhone");
            xmlWriter.writeData(member[39]);
            xmlWriter.endElement("MemberPhone");
            xmlWriter.startElement("MemberMobile");
            xmlWriter.writeData(member[40]);
            xmlWriter.endElement("MemberMobile");

            xmlWriter.startElement("MemberFax");
            xmlWriter.writeData(member[41]);
            xmlWriter.endElement("MemberFax");
            xmlWriter.startElement("MemberCareer");
            xmlWriter.writeData(member[42]);
            xmlWriter.endElement("MemberCareer");
            xmlWriter.startElement("MemberHomepage");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[43]));
            xmlWriter.endElement("MemberHomepage");
            xmlWriter.startElement("MemberYahoo");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[44]));
            xmlWriter.endElement("MemberYahoo");
            xmlWriter.startElement("MemberAol");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[45]));
            xmlWriter.endElement("MemberAol");
            xmlWriter.startElement("MemberIcq");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[46]));
            xmlWriter.endElement("MemberIcq");
            xmlWriter.startElement("MemberMsn");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[47]));
            xmlWriter.endElement("MemberMsn");
            xmlWriter.startElement("MemberCoolLink1");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[48]));
            xmlWriter.endElement("MemberCoolLink1");
            xmlWriter.startElement("MemberCoolLink2");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(member[49]));
            xmlWriter.endElement("MemberCoolLink2");

            exportGlobalPermissionsForMember(xmlWriter, memberID);
            exportMessageFoldersForMember(xmlWriter, memberID);
            exportGlobalWatchesForMember(xmlWriter, memberID);
            xmlWriter.endElement("Member");
        //} catch throw exportexception
    }

    public static void exportMemberList(XMLWriter xmlWriter)
        throws IOException, DatabaseException, ExportException {
        Collection memberIDs=ExportWebHelper.execSqlQuery(
                   "SELECT MemberID"+
                   " FROM "+MemberDAO.TABLE_NAME);
        Iterator iter = memberIDs.iterator();
        String[] memberID = null;
        //try {
            xmlWriter.startElement("MemberList");
            /* First, I'll export guest and root admin. If they don't exist, just continue. */
            if (MVNForumConstant.MEMBER_ID_OF_GUEST < MVNForumConstant.MEMBER_ID_OF_ADMIN) {
                try { exportMember(xmlWriter, MVNForumConstant.MEMBER_ID_OF_GUEST); }
                catch (Exception e) { /* doesn't exist => ignore */ }
                try { exportMember(xmlWriter, MVNForumConstant.MEMBER_ID_OF_ADMIN); }
                catch (Exception e) { /* doesn't exist => ignore */ }
            } else {
                try { exportMember(xmlWriter, MVNForumConstant.MEMBER_ID_OF_ADMIN); }
                catch (Exception e) { /* doesn't exist => ignore */ }
                try { exportMember(xmlWriter, MVNForumConstant.MEMBER_ID_OF_GUEST); }
                catch (Exception e) { /* doesn't exist => ignore */ }
            }
            try {
                while ( (memberID=(String[])iter.next()) !=null) {
                    if (memberID.length!=1) {
                        throw new ExportException("Error while retrieving list of members.");
                    }
                    try {
                        int i=Integer.parseInt(memberID[0]);
                        if ((i!=MVNForumConstant.MEMBER_ID_OF_GUEST) && (i!=MVNForumConstant.MEMBER_ID_OF_ADMIN)) {
                            exportMember(xmlWriter, i);
                        }
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of members.");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("MemberList");
         //} catch throw exportexception
    }



}
