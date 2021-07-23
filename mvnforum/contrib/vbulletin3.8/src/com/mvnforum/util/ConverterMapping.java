/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/vbulletin3.8/src/com/mvnforum/util/ConverterMapping.java,v 1.5 2009/10/12 03:33:13 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.5 $
 * $Date: 2009/10/12 03:33:13 $
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
 * @author: Xuan Tran Le
 */
package com.mvnforum.util;

import java.util.HashMap;
import java.util.Map;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvnforum.db.DAOFactory;
import com.mvnforum.vbulletin.Migrator;

/**
 * The Class ConverterMapping.
 */
public class ConverterMapping {
    
    private static final Logger log = LoggerFactory.getLogger(ConverterMapping.class);

    /** The user name map. */
    private static Map userNameMap = new HashMap();
    
    /** The user id map. */
    private static Map userIDMap = new HashMap();
    
    /**
     * Instantiates a new converter mapping.
     */
    private ConverterMapping() {
    }
    
    /**
     * Gets the user name map.
     * 
     * @return the user name map
     */
    public static Map getUserNameMap() {
        return userNameMap;
    }
    
    /**
     * Gets the user id map.
     * 
     * @return the user id map
     */
    public static Map getUserIDMap() {
        return userIDMap;
    }

    /**
     * Gets the available member name.
     * 
     * @param memberName the member name
     * 
     * @return the available member name
     * 
     * @throws DatabaseException the database exception
     */
    public static String getAvailableMemberName(String memberName) throws DatabaseException {
        
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
        } catch (ObjectNotFoundException e) {
            return memberName;
        }
        
        int index = 1;
        return getAutoIncreaseMemberName(memberName, index);
    }
    
    /**
     * Gets the auto increase member name.
     * 
     * @param memberName the member name
     * @param index the index
     * 
     * @return the auto increase member name
     * 
     * @throws DatabaseException the database exception
     */
    private static String getAutoIncreaseMemberName(String memberName, int index) throws DatabaseException {
        String newMemberName = memberName + index;
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberName(newMemberName);
            return getAutoIncreaseMemberName(memberName, ++index);
        } catch (ObjectNotFoundException e) {
            return newMemberName;
        }
    }

    /**
     * Gets the available member email.
     * 
     * @param memberEmail the member email
     * 
     * @return the available member email
     * 
     * @throws DatabaseException the database exception
     */
    public static String getAvailableMemberEmail(String memberEmail) throws DatabaseException {
        
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(memberEmail);
        } catch (ObjectNotFoundException e) {
            return memberEmail;
        }
        
        int index = 1;
        return getAutoIncreaseMemberEmail(memberEmail, index);
    }
    
    /**
     * Gets the auto increase member email.
     * 
     * @param memberEmail the member email
     * @param index the index
     * 
     * @return the auto increase member email
     * 
     * @throws DatabaseException the database exception
     */
    private static String getAutoIncreaseMemberEmail(String memberEmail, int index) throws DatabaseException {
        String newMemberEmail = memberEmail + index;
        try {
            DAOFactory.getMemberDAO().findByAlternateKey_MemberEmail(newMemberEmail);
            return getAutoIncreaseMemberEmail(memberEmail, ++index);
        } catch (ObjectNotFoundException e) {
            return newMemberEmail;
        }
    }
    
    /**
     * Gets the member name from map.
     * 
     * @param memberName the member name
     * 
     * @return the member name from map
     * @throws DatabaseException 
     */
    public static String getMemberNameFromMap(String memberName) throws DatabaseException {
        if (userNameMap.containsKey(memberName)) {
            memberName = (String) ConverterMapping.getUserNameMap().get(memberName);
        } else {
            if (memberName != null && memberName.length() > 0) {
                try {
                    DAOFactory.getMemberDAO().findByAlternateKey_MemberName(memberName);
                } catch (ObjectNotFoundException e) {
                    log.debug("mvnForum member name \"" + memberName + "\" does not exist or was removed, so member name would be \"" + Migrator.GUEST_NAME + "\"");
                    return Migrator.GUEST_NAME;
                }
            }
        }
        return memberName;
    }
    
    /**
     * Gets the member id from map.
     * 
     * @param memberID the member id
     * 
     * @return the member id from map
     */
    public static int getMemberIDFromMap(int memberID) {
        if (userNameMap.containsKey(new Integer(memberID))) {
            memberID = ((Integer) ConverterMapping.getUserIDMap().get(new Integer(memberID))).intValue();
        }
        return memberID;
    }
}
