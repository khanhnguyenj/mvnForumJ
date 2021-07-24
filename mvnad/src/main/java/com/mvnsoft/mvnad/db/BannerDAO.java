/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/BannerDAO.java,v 1.5 2008/06/10 18:29:42 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.5 $
 * $Date: 2008/06/10 18:29:42 $
 *
 * ====================================================================
 *
 * Copyright (C) 2002-2008 by MyVietnam.net
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
 * @author: MyVietnam.net developers
 */
package com.mvnsoft.mvnad.db;

import java.sql.Timestamp;
import java.util.Collection;

import net.myvietnam.mvncore.exception.*;

public interface BannerDAO {
    
    public static final int BANNER_TYPE_ALL = -1;
    
    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Banner";

    public void findByPrimaryKey(int bannerID) 
        throws ObjectNotFoundException, DatabaseException;

    public void findByAlternateKey_MemberName_BannerName(String memberName, String bannerName)
        throws ObjectNotFoundException, DatabaseException;

    public void create(String memberName, String bannerName, String bannerDesc, 
                       String bannerAltText, String bannerMimeType, String bannerPreText, 
                       String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
                       int bannerWidth, int bannerHeight, int bannerWeight, 
                       int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
                       int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
                       Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
                       String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
                       int bannerStatus, int bannerType, Timestamp bannerCreationDate, 
                       Timestamp bannerModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;
    
    public int createBanner(String memberName, String bannerName, String bannerDesc, 
            String bannerAltText, String bannerMimeType, String bannerPreText, 
            String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
            int bannerWidth, int bannerHeight, int bannerWeight, 
            int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
            int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
            Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
            String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
            int bannerStatus, int bannerType, Timestamp bannerCreationDate, 
            Timestamp bannerModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;
    
    public void update(int bannerID, // primary key
                       String memberName, String bannerName, String bannerDesc, 
                       String bannerAltText, String bannerMimeType, String bannerPreText, 
                       String bannerAfterText, String bannerTargetURL, String bannerImageURL, 
                       int bannerWidth, int bannerHeight, int bannerWeight, 
                       int bannerMaxImpression, int bannerReceivedImpression, int bannerMaxClick, 
                       int bannerReceivedClick, int bannerZonePositionX, int bannerZonePositionY, 
                       Timestamp bannerStartDate, Timestamp bannerEndDate, int bannerIsHtml, 
                       String bannerHtmlCode, int bannerCanTrackClicks, int bannerOption, 
                       int bannerStatus, int bannerType, Timestamp bannerModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;
    
    public void increaseReceivedClick(int bannerID, int receivedClickCount) 
        throws ObjectNotFoundException, DatabaseException;
    
    public void increaseReceivedImpression(int bannerID, int receivedImpressionCount) 
        throws ObjectNotFoundException, DatabaseException;
    
    public void updateReceivedImpression(int bannerID, // primary key
            int bannerReceivedImpression)
        throws ObjectNotFoundException, DatabaseException;

    public void updateReceivedClick(int bannerID, // primary key
            int bannerReceivedClick)
        throws ObjectNotFoundException, DatabaseException;

    public void delete(int bannerID)
        throws DatabaseException, ObjectNotFoundException;

    public BannerBean getBean(int bannerID)
        throws ObjectNotFoundException, DatabaseException;

    public BannerBean getBean_byAlternateKey_MemberName_BannerName(String memberName, String bannerName)
        throws ObjectNotFoundException, DatabaseException;
    
    public Collection getBeans()
        throws DatabaseException;
    
    public Collection getBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order, String memberName)
        throws IllegalArgumentException, DatabaseException;
    
    public Collection getValidBanners(Timestamp now) 
        throws DatabaseException, ObjectNotFoundException;

    public Collection getBannersOfUser(String memberName)
        throws DatabaseException;

    public Collection getDistinctMemberNamesHasBanner() 
        throws ObjectNotFoundException, DatabaseException;

    public int getNumberOfBeans() throws DatabaseException;
}
