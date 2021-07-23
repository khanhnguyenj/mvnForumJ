/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/ZoneDAO.java,v 1.4 2008/06/10 03:04:44 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.4 $
 * $Date: 2008/06/10 03:04:44 $
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

public interface ZoneDAO {
    
    public static final int ZONE_TYPE_ALL = -1;
    
    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "Zone";

    public void findByPrimaryKey(int zoneID) 
        throws ObjectNotFoundException, DatabaseException;
    
    public void findByAlternateKey_MemberName_ZoneName(String memberName, String zoneName)
        throws ObjectNotFoundException, DatabaseException;

    public void create(String memberName, String zoneName, String zoneDesc, 
            String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
            int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
            int zoneDirection, int zonerMaxImpression, int zoneReceivedImpression, 
            int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
            int zoneOption, int zoneStatus, int zoneType, 
            Timestamp zoneCreationDate, Timestamp zoneModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;
    
    public int createZone(String memberName, String zoneName, String zoneDesc, 
            String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
            int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
            int zoneDirection, int zonerMaxImpression, int zoneReceivedImpression, 
            int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
            int zoneOption, int zoneStatus, int zoneType, 
            Timestamp zoneCreationDate, Timestamp zoneModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;
    
    public void update(int zoneID, // primary key
            String memberName, String zoneName, String zoneDesc, 
            String zoneTargetWindow, int zoneCellWidth, int zoneCellHeight, 
            int zoneCellHorizontalCount, int zoneCellVerticalCount, int zoneMaxBanners, 
            int zoneDirection, int zonerMaxImpression, int zoneReceivedImpression, 
            int zoneMaxClick, int zoneReceivedClick, int zoneAutoReloadTime, 
            int zoneOption, int zoneStatus, int zoneType, 
            Timestamp zoneModifiedDate)
        throws ObjectNotFoundException, DatabaseException, DuplicateKeyException;

    public void updateReceivedImpression(int zoneID, int zoneReceivedImpression)
        throws ObjectNotFoundException, DatabaseException;
    
    public void updateReceivedClick(int zoneID, int zoneReceivedClick)
        throws ObjectNotFoundException, DatabaseException;

    public void increaseReceivedClick(int zoneID, int receivedClickCount) 
        throws ObjectNotFoundException, DatabaseException;

    public void increaseReceivedImpression(int zoneID, int receivedImpressionCount) 
        throws ObjectNotFoundException, DatabaseException;

    public void delete(int zoneID)
        throws DatabaseException, ObjectNotFoundException;

    public ZoneBean getBean(int zoneID)
        throws ObjectNotFoundException, DatabaseException;
    
    public ZoneBean getBean_byAlternateKey_MemberName_ZoneName(String memberName, String zoneName)
        throws ObjectNotFoundException, DatabaseException;

    public Collection getBeans()
        throws DatabaseException;

    public Collection getBeans_withSortSupport_limit(int offset, int rowsToReturn, String sort, String order)
        throws IllegalArgumentException, DatabaseException;
    
    public int getNumberOfBeans() throws DatabaseException;
}
