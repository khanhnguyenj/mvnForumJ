/*
 * $Header: /cvsroot/mvnforum/mvnforum/contrib/phpbb2mvnforum/src/org/mvnforum/phpbb2mvnforum/db/jdbc/ThreadDAOImplXML.java,v 1.3 2007/01/15 10:27:10 dungbtm Exp $
 * $Author: dungbtm $
 * $Revision: 1.3 $
 * $Date: 2007/01/15 10:27:10 $
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
import net.myvietnam.mvncore.exception.ForeignKeyNotFoundException;
import net.myvietnam.mvncore.exception.ObjectNotFoundException;

import org.mvnforum.phpbb2mvnforum.db.ThreadBean;
import org.mvnforum.phpbb2mvnforum.db.ThreadDAO;
import org.mvnforum.util.DBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ThreadDAOImplXML implements ThreadDAO {

    public void findByPrimaryKey(int threadID)
        throws ObjectNotFoundException, DatabaseException {
        // TODO Auto-generated method stub

    }

    public int createThread(int ThreadID, int forumID, String memberName, String lastPostMemberName,
            String threadTopic, String threadBody, int threadVoteCount, int threadVoteTotalStars,
            Timestamp threadCreationDate, Timestamp threadLastPostDate, int threadType, int threadOption,
            int threadStatus, int threadHasPoll, int threadViewCount, int threadReplyCount, String threadIcon,
            int threadDuration, int threadAttachCount)
        throws ObjectNotFoundException, CreateException, DatabaseException, ForeignKeyNotFoundException,
        DuplicateKeyException {
        // TODO Auto-generated method stub
        return 0;
    }

    public void createMultiple(Collection beans) {
        // TODO Auto-generated method stub
        Document doc = DBUtils.getDomDocument();
        Element element = doc.createElement("ThreadList");
        doc.appendChild(element);
        for (Iterator iter = beans.iterator(); iter.hasNext(); ) {
            ThreadBean bean = (ThreadBean)iter.next();
            bean.getBeanDocument(doc, element);
        }
        //DBUtils.writeXmlFile(doc, Phpbb2MvnforumConfig.EXPORT_XML);
    }

}
