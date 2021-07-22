/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/PhpbbUserDAOImpl.java,v 1.5 2007/01/15 10:27:11 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.5 $
 * $Date: 2007/01/15 10:27:11 $
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
package org.mvnforum.phpbb2mvnforum.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.PhpbbUserDAO;
import org.mvnforum.phpbb2mvnforum.db.PhpbbUsers;
import org.mvnforum.util.DBUtils;

public class PhpbbUserDAOImpl implements PhpbbUserDAO{
    
    public static String TABLE_NAME = "phpbb_users";
    
    public String getUserNameFromUserID (int userID) throws ObjectNotFoundException, DatabaseException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT username");
        sql.append(" FROM " + TABLE_NAME);
        sql.append(" WHERE user_id = ?");
        
        String memName = "";
        
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            statement.setInt(1, userID);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new ObjectNotFoundException("Cannot find user [userID] (" + userID
                        + ") in table 'phpbb_users'.");
            }
            memName = resultSet.getString("username");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new DatabaseException("Error executing SQL in PhpbbUserDAOImpl.getUserNameFromUserID");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
        return memName;
    }
    
    /*
     * Included columns: user_id, user_active, username, user_password, user_session_time, 
     *                   user_session_page, user_lastvisit, user_regdate, user_level, user_posts, 
     *                   user_timezone, user_style, user_lang, user_dateformat, user_new_privmsg, 
     *                   user_unread_privmsg, user_last_privmsg, user_emailtime, user_viewemail, user_attachsig, 
     *                   user_allowhtml, user_allowbbcode, user_allowsmile, user_allowavatar, user_allow_pm, 
     *                   user_allow_viewonline, user_notify, user_notify_pm, user_popup_pm, user_rank, 
     *                   user_avatar, user_avatar_type, user_email, user_icq, user_website, 
     *                   user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, 
     *                   user_msnm, user_occ, user_interests, user_actkey, user_newpasswd
     * Excluded columns: 
     */
    public Collection getBeans() throws DatabaseException {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Collection retValue = new ArrayList();
        StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT user_id, user_active, username, user_password, user_session_time, user_session_page, user_lastvisit, user_regdate, user_level, user_posts, user_timezone, user_style, user_lang, user_dateformat, user_new_privmsg, user_unread_privmsg, user_last_privmsg, user_emailtime, user_viewemail, user_attachsig, user_allowhtml, user_allowbbcode, user_allowsmile, user_allowavatar, user_allow_pm, user_allow_viewonline, user_notify, user_notify_pm, user_popup_pm, user_rank, user_avatar, user_avatar_type, user_email, user_icq, user_website, user_from, user_sig, user_sig_bbcode_uid, user_aim, user_yim, user_msnm, user_occ, user_interests, user_actkey, user_newpasswd");
        sql.append(" FROM " + TABLE_NAME);
        //sql.append(" WHERE "); // @todo: uncomment as needed
        //sql.append(" ORDER BY ColumnName ASC|DESC "); // @todo: uncomment as needed
        try {
            connection = DBUtils.getPhpbbConnection();
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PhpbbUsers bean = new PhpbbUsers();
                bean.setUserId(resultSet.getInt("user_id"));
                bean.setUserActive(resultSet.getInt("user_active"));
                bean.setUsername(resultSet.getString("username"));
                bean.setUserPassword(resultSet.getString("user_password"));
                bean.setUserSessionTime(resultSet.getLong("user_session_time"));
                bean.setUserSessionPage(resultSet.getInt("user_session_page"));
                bean.setUserLastvisit(resultSet.getLong("user_lastvisit"));
                bean.setUserRegdate(resultSet.getLong("user_regdate"));
                bean.setUserLevel(resultSet.getInt("user_level"));
                bean.setUserPosts(resultSet.getInt("user_posts"));
                bean.setUserTimezone(resultSet.getBigDecimal("user_timezone"));
                bean.setUserStyle(resultSet.getInt("user_style"));
                bean.setUserLang(resultSet.getString("user_lang"));
                bean.setUserDateformat(resultSet.getString("user_dateformat"));
                bean.setUserNewPrivmsg(resultSet.getInt("user_new_privmsg"));
                bean.setUserUnreadPrivmsg(resultSet.getInt("user_unread_privmsg"));
                bean.setUserLastPrivmsg(resultSet.getLong("user_last_privmsg"));
                bean.setUserEmailtime(resultSet.getInt("user_emailtime"));
                bean.setUserViewemail(resultSet.getInt("user_viewemail"));
                bean.setUserAttachsig(resultSet.getInt("user_attachsig"));
                bean.setUserAllowhtml(resultSet.getInt("user_allowhtml"));
                bean.setUserAllowbbcode(resultSet.getInt("user_allowbbcode"));
                bean.setUserAllowsmile(resultSet.getInt("user_allowsmile"));
                bean.setUserAllowavatar(resultSet.getInt("user_allowavatar"));
                bean.setUserAllowPm(resultSet.getInt("user_allow_pm"));
                bean.setUserAllowViewonline(resultSet.getInt("user_allow_viewonline"));
                bean.setUserNotify(resultSet.getInt("user_notify"));
                bean.setUserNotifyPm(resultSet.getInt("user_notify_pm"));
                bean.setUserPopupPm(resultSet.getInt("user_popup_pm"));
                bean.setUserRank(resultSet.getInt("user_rank"));
                bean.setUserAvatar(resultSet.getString("user_avatar"));
                bean.setUserAvatarType(resultSet.getInt("user_avatar_type"));
                bean.setUserEmail(resultSet.getString("user_email"));
                bean.setUserIcq(resultSet.getString("user_icq"));
                bean.setUserWebsite(resultSet.getString("user_website"));
                bean.setUserFrom(resultSet.getString("user_from"));
                bean.setUserSig(resultSet.getString("user_sig"));
                bean.setUserSigBbcodeUid(resultSet.getString("user_sig_bbcode_uid"));
                bean.setUserAim(resultSet.getString("user_aim"));
                bean.setUserYim(resultSet.getString("user_yim"));
                bean.setUserMsnm(resultSet.getString("user_msnm"));
                bean.setUserOcc(resultSet.getString("user_occ"));
                bean.setUserInterests(resultSet.getString("user_interests"));
                bean.setUserActkey(resultSet.getString("user_actkey"));
                bean.setUserNewpasswd(resultSet.getString("user_newpasswd"));
                retValue.add(bean);
            }
            return retValue;
        } catch(SQLException sqle) {
            throw new DatabaseException("Error executing SQL in phpbb_usersDAOImplJDBC.getBeans.");
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
        }
    }
    
}
