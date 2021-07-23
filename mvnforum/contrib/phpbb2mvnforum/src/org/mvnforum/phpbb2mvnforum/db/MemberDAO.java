/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/MemberDAO.java,v 1.7 2007/01/15 10:27:34 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.7 $
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
 * @author: 
 */
package org.mvnforum.phpbb2mvnforum.db;

import java.sql.Date;
import java.sql.Timestamp;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

public interface MemberDAO {
    
    static final String TABLE_NAME = "mvnforumMember";
    
    void update(int memberID, // primary key
            String memberName, String memberPassword, String memberFirstEmail, 
            String memberEmail, int memberEmailVisible, int memberNameVisible, 
            String memberFirstIP, String memberLastIP, int memberViewCount, 
            int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, 
            Timestamp memberExpireDate, Timestamp memberLastLogon, int memberOption, 
            int memberStatus, String memberActivateCode, String memberTempPassword, 
            int memberMessageCount, int memberMessageOption, int memberPostsPerPage, 
            int memberWarnCount, int memberVoteCount, int memberVoteTotalStars, 
            int memberRewardPoints, String memberTitle, double memberTimeZone, 
            String memberSignature, String memberAvatar, String memberSkin, 
            String memberLanguage, String memberFirstname, String memberLastname, 
            int memberGender, Date memberBirthday, String memberAddress, 
            String memberCity, String memberState, String memberCountry, 
            String memberPhone, String memberMobile, String memberFax, 
            String memberCareer, String memberHomepage, String memberYahoo, 
            String memberAol, String memberIcq, String memberMsn, 
            String memberCoolLink1, String memberCoolLink2)
    throws ObjectNotFoundException, DatabaseException, DuplicateKeyException;
    
    void create(int memberId, String memberName, String memberPassword, String memberFirstEmail, String memberEmail, 
            int memberEmailVisible, int memberNameVisible, String memberFirstIP, String memberLastIP, int memberViewCount, 
            int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, Timestamp memberExpireDate, 
            Timestamp memberLastLogon, int memberOption, int memberStatus, String memberActivateCode, String memberTempPassword, 
            int memberMessageCount, int memberMessageOption, int memberPostsPerPage, int memberWarnCount, int memberVoteCount, 
            int memberVoteTotalStars, int memberRewardPoints, String memberTitle, double tzone, String memberSignature, 
            String memberAvatar, String memberSkin, String memberLanguage, String memberFirstname, String memberLastname, 
            int memberGender, Date memberBirthday, String memberAddress, String memberCity, String memberState, String memberCountry, 
            String memberPhone, String memberMobile, String memberFax, String memberCareer, String memberHomepage, String memberYahoo, 
            String memberAol, String memberIcq, String memberMsn, String memberCoolLink1, String memberCoolLink2) 
        throws CreateException, DatabaseException, DuplicateKeyException;
    
    void create(String memberName, String memberPassword, String memberFirstEmail, String memberEmail, 
            int memberEmailVisible, int memberNameVisible, String memberFirstIP, String memberLastIP, int memberViewCount, 
            int memberPostCount, Timestamp memberCreationDate, Timestamp memberModifiedDate, Timestamp memberExpireDate, 
            Timestamp memberLastLogon, int memberOption, int memberStatus, String memberActivateCode, String memberTempPassword, 
            int memberMessageCount, int memberMessageOption, int memberPostsPerPage, int memberWarnCount, int memberVoteCount, 
            int memberVoteTotalStars, int memberRewardPoints, String memberTitle, double tzone, String memberSignature, 
            String memberAvatar, String memberSkin, String memberLanguage, String memberFirstname, String memberLastname, 
            int memberGender, Date memberBirthday, String memberAddress, String memberCity, String memberState, String memberCountry, 
            String memberPhone, String memberMobile, String memberFax, String memberCareer, String memberHomepage, String memberYahoo, 
            String memberAol, String memberIcq, String memberMsn, String memberCoolLink1, String memberCoolLink2) 
        throws CreateException, DatabaseException, DuplicateKeyException;    
    
    public String getMemberNameFromMemberID(int memberID) throws ObjectNotFoundException, DatabaseException;
    
    public MemberBean getMemberFromMemberName (String memberName) throws ObjectNotFoundException, DatabaseException;

    public void findByPrimaryKey(int memberID) throws ObjectNotFoundException, DatabaseException;


}
