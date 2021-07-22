/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/ForumDAOImplXML.java,v 1.3 2007/01/15 10:27:12 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:12 $
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import net.myvietnam.mvncore.exception.CreateException;
import net.myvietnam.mvncore.exception.DatabaseException;
import net.myvietnam.mvncore.exception.DuplicateKeyException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.ForumBean;
import org.mvnforum.phpbb2mvnforum.db.ForumDAO;
import org.mvnforum.util.DBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ForumDAOImplXML implements ForumDAO {

    public void findByPrimaryKey(int forumID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public void findByAlternateKey_ForumName_CategoryID(String forumName, int categoryID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public void create(int forumID, int categoryID, String lastPostMemberName, String forumName, String forumDesc,
            Timestamp forumCreationDate, Timestamp forumModifiedDate, Timestamp forumLastPostDate, int forumOrder,
            int forumType, int forumFormatOption, int forumOption, int forumStatus, int forumModerationMode,
            String forumPassword, int forumThreadCount, int forumPostCount)
        throws CreateException, DatabaseException, DuplicateKeyException {

        ForumBean bean = new ForumBean();
        bean.setForumID(forumID);
        bean.setCategoryID(categoryID);
        bean.setLastPostMemberName(lastPostMemberName);
        bean.setForumName(forumName);
        bean.setForumDesc(forumDesc);
        bean.setForumCreationDate(forumCreationDate);
        bean.setForumModifiedDate(forumModifiedDate);
        bean.setForumLastPostDate(forumLastPostDate);
        bean.setForumOrder(forumOrder);
        bean.setForumType(forumType);
        bean.setForumFormatOption(forumFormatOption);
        bean.setForumOption(forumOption);
        bean.setForumStatus(forumStatus);
        bean.setForumModerationMode(forumModerationMode);
        bean.setForumPassword(forumPassword);
        bean.setForumThreadCount(forumThreadCount);
        bean.setForumPostCount(forumPostCount);

    }

    public void createMultiple(Collection beans) {

        Document doc = DBUtils.getDomDocument();
        Element element = doc.createElement("ForumList");
        doc.getFirstChild().appendChild(element);
        for (Iterator iter = beans.iterator(); iter.hasNext(); ) {
            ForumBean bean = (ForumBean)iter.next();
            bean.getBeanDocument(doc, element);
        }
        //DBUtils.writeXmlFile(doc, Phpbb2MvnforumConfig.EXPORT_XML);
        
    }

}
