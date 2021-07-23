/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/db/RankCache.java,v 1.14 2009/01/02 18:18:50 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.14 $
 * $Date: 2009/01/02 18:18:50 $
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
package com.mvnforum.db;

import java.util.ArrayList;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import com.mvnforum.db.jdbc.RankDAOImplJDBC;

public final class RankCache {

    // static singleton variable
    private static RankCache instance = new RankCache();

    // instance variable
    private ArrayList beanArray = null;

    /**
     * A private constructor since this is a Singleton
     */
    private RankCache() {
    }

    /**
     * Returns the single instance
     * @return RankCache : the singleton instance.
     *
     * NOTE: if use normal singleton pattern, this method should be synchronized
     */
    public static RankCache getInstance() {
        return instance;
    }

    /**
     * This is a private method, and a util method
     * If a method call ensureNewData(), then it MUST be synchronized
     */
    private void ensureNewData() throws DatabaseException {
        if ( RankDAOImplJDBC.isDirty() || (beanArray == null) ) {
            RankDAOImplJDBC.setDirty(false);
            beanArray = (ArrayList)DAOFactory.getRankDAO().getRanks();
        }
    }

    /**
     * IMPORTANT NOTE: The caller must not alter the returned collection
     * Note: since the return value can be accessed in many threads,
     *       we must be sure that this collection is intact. Now we can only
     *       be sure that each object in the collection is intact (default set methods),
     *       but we can not prevent the collection from being changed
     * #@todo Find a way to make the collection immutable
     */
    public synchronized ArrayList getBeans() throws DatabaseException {
        ensureNewData();
        return beanArray;
    }

    public synchronized RankBean getBean(int rankID)
        throws DatabaseException, ObjectNotFoundException {

        ensureNewData();
        int size = beanArray.size();
        for (int i = 0; i < size; i++) {
            RankBean bean = (RankBean)beanArray.get(i);
            if (bean.getRankID() == rankID) {
                return bean;
            }
        }
        //@todo : localize me
        throw new ObjectNotFoundException("Cannot find the row in table Rank where primary key = (" + rankID + ").");
    }

    /**
     * Reload to get the latest info
     * Normally, this class will detect all the modifications in the table.
     * However, call this method to force a reload
     * Auto call reload after some time (say 1 hour) is also a good idea
     */
    public synchronized void reload() throws DatabaseException {
        RankDAOImplJDBC.setDirty(false);
        beanArray = (ArrayList)DAOFactory.getRankDAO().getRanks();
    }
}
