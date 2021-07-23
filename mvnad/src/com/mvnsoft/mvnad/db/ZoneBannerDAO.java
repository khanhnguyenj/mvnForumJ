/*
 * $Header: /cvsroot/mvnforum/mvnad/src/com/mvnsoft/mvnad/db/ZoneBannerDAO.java,v 1.3 2008/10/27 09:12:24 hahuynh Exp $
 * $Author: hahuynh $
 * $Revision: 1.3 $
 * $Date: 2008/10/27 09:12:24 $
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
import java.util.*;

import net.myvietnam.mvncore.exception.*;

public interface ZoneBannerDAO {
    
    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "ZoneBanner";
    
    public void findByPrimaryKey(int zoneID, int bannerID) 
        throws ObjectNotFoundException, DatabaseException;

    public void create(int zoneID, int bannerID, int relationCellX, 
                       int relationCellY, int relationCellOption, int relationWeight, 
                       int relationOption, int relationStatus, int relationType, 
                       Timestamp relationPublishStartDate, Timestamp relationPublishEndDate, Timestamp relationCreationDate, 
                       Timestamp relationModifiedDate)
        throws CreateException, DatabaseException, DuplicateKeyException, ForeignKeyNotFoundException;

    public void update(int zoneID, int bannerID,
                       int relationCellX, int relationCellY, int relationCellOption, 
                       int relationWeight, int relationOption, int relationStatus, 
                       int relationType, Timestamp relationPublishStartDate, Timestamp relationPublishEndDate, 
                       Timestamp relationModifiedDate)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException;
    
    public void delete(int zoneID, int bannerID)
        throws DatabaseException, ObjectNotFoundException;
    
    public void deleteInBanner(int bannerID) 
        throws DatabaseException;

    public void deleteInZone(int zoneID) 
        throws DatabaseException;

    public Collection getBeans()
        throws DatabaseException;

    public Collection getBeans(int zoneID) 
        throws DatabaseException, ObjectNotFoundException;
    
    public Collection getValidBannersOfZone(Timestamp now, int zoneID) 
        throws DatabaseException, ObjectNotFoundException;
    
    public ZoneBannerBean getBean(int zoneID, int bannerID)
        throws ObjectNotFoundException, DatabaseException;

    public int getNumberOfBeans_inZone(int zoneID)
        throws DatabaseException;
    
    public int getNumberOfBeans_inBanner(int bannerID)
        throws DatabaseException;

}
