/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/ThreadXML.java,v 1.20 2009/09/23 08:42:17 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.20 $
 * $Date: 2009/09/23 08:42:17 $
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
import java.sql.Timestamp;
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.admin.importexport.jive.ImportJive;
import com.mvnforum.db.*;

/**
 * @author Igor Manic
 * @version $Revision: 1.20 $, $Date: 2009/09/23 08:42:17 $
 * <br/>
 * <code>ThreadXML</code> todo Igor: enter description
 *
 */
public class ThreadXML {

    public ThreadXML() {
        threadID = -1;
        parentForumID = -1;
        parentCategoryID = -1;
    }

    private int threadID;
    /** Returns <code>ThreadID</code> of this thread or
      * <code>-1</code> if thread is not created yet. */
    public int getThreadID() { return threadID; }

    private int parentForumID;
    /** Returns <code>ForumID</code> of this thread's parent forum or
      * <code>-1</code> if this thread is not created yet. */
    public int getParentForumID() { return parentForumID; }

    private int parentCategoryID;
    /** Returns <code>CategoryID</code> of this thread's parent category or
      * <code>-1</code> if this thread is not created yet. */
    public int getParentCategoryID() { return parentCategoryID; }

    public void setThreadID(String id) {
        threadID = XMLUtil.stringToIntDef(id, -1);
    }

    public void setParentForum(Object o)
        throws ForeignKeyNotFoundException {
        
        if (o instanceof ForumXML) {
            parentForumID=((ForumXML)o).getForumID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent forum's ID");
        }
    }

    public void setParentForumID(int value) {
        if (value < 0) {
            parentForumID = -1;
        } else {
            parentForumID = value;
        }
    }

    public void setParentCategory(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof ForumXML) {
            parentCategoryID = ((ForumXML)o).getParentCategoryID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent category's ID");
        }
    }

    public void setParentCategoryID(int value) {
        if (value < 0) {
            parentCategoryID = -1;
        } else {
            parentCategoryID = value;
        }
    }

    /**
     * Creates a thread. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param memberName Member that created the thread. Can be null.
     * @param lastPostMemberName Can be null.
     * @param threadTopic Thread topic.
     * @param threadBody Thread body (description).
     * @param threadVoteCount Can be null.
     * @param threadVoteTotalStars Can be null.
     * @param threadCreationDate Can be null.
     * @param threadLastPostDate Can be null.
     * @param threadType Can be null.
     * @param threadOption Can be null.
     * @param threadStatus Can be null.
     * @param threadHasPoll Can be null.
     * @param threadViewCount Can be null.
     * @param threadReplyCount Can be null.
     * @param threadIcon Can be null.
     * @param threadDuration Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     */
    public void addThread(String memberName, String lastPostMemberName,
                          String threadTopic, String threadBody,
                          String threadVoteCount, String threadVoteTotalStars,
                          String threadCreationDate, String threadLastPostDate,
                          String threadType, String threadPriority, String threadOption,
                          String threadStatus, String threadHasPoll,
                          String threadViewCount, String threadReplyCount,
                          String threadIcon, String threadDuration, String threadAttachCount)
        throws CreateException, ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {

        if (parentForumID < 0) {
            throw new CreateException("Can't create a thread, because no parent forum assigned yet.");
        } else if (parentCategoryID < 0) {
            throw new CreateException("Can't create a thread, because no parent category assigned yet.");
        } else if ( (threadTopic == null) || (threadBody == null) ) {
            throw new CreateException("Can't create a thread with empty ThreadTopic or empty ThreadBody.");
        } else {
            int threadVoteCount1;
            int threadVoteTotalStars1;
            java.sql.Timestamp threadCreationDate1;
            java.sql.Timestamp threadLastPostDate1;
            int threadType1;
            int threadPriority1;
            int threadOption1;
            int threadStatus1;
            int threadHasPoll1;
            int threadViewCount1;
            int threadReplyCount1;
            int threadDuration1;
            int threadAttachCount1;

            try {
                if (memberName == null) {
                    memberName = "";
                }
                if (lastPostMemberName == null) {
                    lastPostMemberName = "";
                }
                threadVoteCount1 = XMLUtil.stringToIntDef(threadVoteCount, 0);
                threadVoteTotalStars1 = XMLUtil.stringToIntDef(threadVoteTotalStars, 0);
                threadCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(threadCreationDate);
                threadLastPostDate1 = XMLUtil.stringToSqlTimestampDefNull(threadLastPostDate);
                threadType1 = XMLUtil.stringToIntDef(threadType, 0);
                threadPriority1 = XMLUtil.stringToIntDef(threadPriority, 0);
                threadOption1 = XMLUtil.stringToIntDef(threadOption, 0);
                threadStatus1 = XMLUtil.stringToIntDef(threadStatus, 0);
                threadHasPoll1 = XMLUtil.stringToIntDef(threadHasPoll, 0);
                threadViewCount1 = XMLUtil.stringToIntDef(threadViewCount, 0);
                threadReplyCount1 = XMLUtil.stringToIntDef(threadReplyCount, 0);
                threadAttachCount1 = XMLUtil.stringToIntDef(threadAttachCount, 0);
                if (threadIcon == null) {
                    threadIcon = "";
                }
                threadDuration1 = XMLUtil.stringToIntDef(threadDuration, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a thread. Expected a number.");
            }

            threadTopic = EnableHtmlTagFilter.filter(threadTopic);
            threadBody = EnableHtmlTagFilter.filter(threadBody);
            threadIcon = EnableHtmlTagFilter.filter(threadIcon);
            
            memberName = ImportJive.getMemberNameFromMap(memberName);
            lastPostMemberName = ImportJive.getMemberNameFromMap(lastPostMemberName);
            
            this.threadID = DAOFactory.getThreadDAO().createThread(parentForumID,
                                memberName, lastPostMemberName,
                                threadTopic, threadBody,
                                threadVoteCount1, threadVoteTotalStars1,
                                threadCreationDate1, threadLastPostDate1,
                                threadType1, threadPriority1, threadOption1,
                                threadStatus1, threadHasPoll1, threadViewCount1,
                                threadReplyCount1, threadIcon, threadDuration1, threadAttachCount1);
        }
    }

    /**
     * Creates a thread watch for this thread. In order to know which thread we are
     * referring to, this method is supposed to be called after {@link #setThreadID(String)},
     * {@link #addThread(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)}
     * have been called. Otherwise, this watch will be simply ignored.
     *
     * @param memberName
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
     * @throws ObjectNotFoundException
     * @throws DuplicateKeyException
     * @throws ForeignKeyNotFoundException
     */
    public void addThreadWatch(String memberName,
                String watchType, String watchOption,
                String watchStatus, String watchCreationDate,
                String watchLastSentDate, String watchEndDate)
        throws CreateException, DatabaseException, ObjectNotFoundException,
        DuplicateKeyException, ForeignKeyNotFoundException {

        if (threadID<0) {
            throw new CreateException("Found thread watch that is not assigned to any known thread.");
        }

        int watchType1;
        int watchOption1;
        int watchStatus1;
        java.sql.Timestamp watchCreationDate1;
        java.sql.Timestamp watchLastSentDate1;
        java.sql.Timestamp watchEndDate1;

        try {
            if (memberName == null) {
                memberName = "";
            }
            watchType1 = XMLUtil.stringToIntDef(watchType, 0);
            watchOption1 = XMLUtil.stringToIntDef(watchOption, 0);
            watchStatus1 = XMLUtil.stringToIntDef(watchStatus, 0);
            watchCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(watchCreationDate);
            watchLastSentDate1 = XMLUtil.stringToSqlTimestampDefNull(watchLastSentDate);
            watchEndDate1 = XMLUtil.stringToSqlTimestampDefNull(watchEndDate);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a thread watch. Expected a number.");
        }

        //todo Igor: Shoud I allow memberID==0 here?
        int memberID = 0;
        if (!memberName.equals("")) {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }
        DAOFactory.getWatchDAO().create(
             memberID, 0/*categoryID*/, threadID, 0/*forumID*/,
             watchType1, watchOption1, watchStatus1,
             watchCreationDate1, watchLastSentDate1, watchEndDate1);
    }

    public void addFavoriteThread(String memberName,
                String favoriteCreationDate, String favoriteType,
                String favoriteOption, String favoriteStatus)
        throws CreateException, DatabaseException, ObjectNotFoundException,
        DuplicateKeyException, ForeignKeyNotFoundException {

        if (threadID < 0) {
            throw new CreateException("Found favorite-thread record that is not assigned to any known thread.");
        } else if (parentForumID<0) {
            throw new CreateException("Can't create a favorite-thread, because no parent forum assigned yet.");
        }

        java.sql.Timestamp favoriteCreationDate1;
        int favoriteType1;
        int favoriteOption1;
        int favoriteStatus1;

        try {
            if (memberName == null) {
                memberName = "";
            }
            favoriteCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(favoriteCreationDate);
            favoriteType1 = XMLUtil.stringToIntDef(favoriteType, 0);
            favoriteOption1 = XMLUtil.stringToIntDef(favoriteOption, 0);
            favoriteStatus1 = XMLUtil.stringToIntDef(favoriteStatus, 0);
        } catch (NumberFormatException e) {
            throw new CreateException("Invalid data for a favorite-thread. Expected a number.");
        }

        //todo Igor: Shoud I allow memberID==0 here?
        int memberID = 0;
        if (memberName.length() > 0) {
            memberID = DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
        }
        DAOFactory.getFavoriteThreadDAO().create(
             memberID, threadID, parentForumID,
             favoriteCreationDate1, favoriteType1, favoriteOption1, favoriteStatus1);
    }

    public void increaseReplyCount()
        throws ObjectNotFoundException, DatabaseException {

        if (threadID < 0) {
            throw new ObjectNotFoundException("Can't update ThreadReplyCount on thread that is not created yet.");
        }
        DAOFactory.getThreadDAO().increaseReplyCount(threadID);
    }

    public void updateLastPostMemberName(String value)
        throws ObjectNotFoundException, DatabaseException, ForeignKeyNotFoundException {
        if (threadID < 0) {
            throw new ObjectNotFoundException("Can't update LastPostMemberName on thread that is not created yet.");
        }
        DAOFactory.getThreadDAO().updateLastPostMemberName(threadID, value);
    }

    public void updateLastPostDate(Timestamp value)
        throws ObjectNotFoundException, DatabaseException {
        if (threadID < 0) {
            throw new ObjectNotFoundException("Can't update ThreadLastPostDate on thread that is not created yet.");
        }
        DAOFactory.getThreadDAO().updateLastPostDate(threadID, value);
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================
    public static void exportThreadWatchesForThread(XMLWriter xmlWriter, int threadID)
        throws IOException, ExportException, NumberFormatException, ObjectNotFoundException, DatabaseException {
        
        Collection threadWatches=ExportWebHelper.execSqlQuery(
                   "SELECT MemberID, WatchType, WatchOption, WatchStatus, WatchCreationDate, WatchLastSentDate, WatchEndDate"+
                   " FROM "+WatchDAO.TABLE_NAME+
                   " WHERE ThreadID="+Integer.toString(threadID));//AND ForumID=0 AND CategoryID=0
        Iterator iter = threadWatches.iterator();
        String[] threadWatch = null;
        //try {
            xmlWriter.startElement("ThreadWatchList");
            try {
                while ( (threadWatch=(String[])iter.next()) != null) {
                    if (threadWatch.length!=7) {
                        throw new ExportException("Error while retrieving data about thread watch for threadID=="+threadID);
                    }
                    String memberName = DAOFactory.getMemberDAO().getMember(Integer.parseInt(threadWatch[0])).getMemberName();
                    xmlWriter.startElement("ThreadWatch");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("WatchType");
                    xmlWriter.writeData(threadWatch[1]);
                    xmlWriter.endElement("WatchType");
                    xmlWriter.startElement("WatchOption");
                    xmlWriter.writeData(threadWatch[2]);
                    xmlWriter.endElement("WatchOption");
                    xmlWriter.startElement("WatchStatus");
                    xmlWriter.writeData(threadWatch[3]);
                    xmlWriter.endElement("WatchStatus");
                    xmlWriter.startElement("WatchCreationDate");
                    xmlWriter.writeData(threadWatch[4]);
                    xmlWriter.endElement("WatchCreationDate");
                    xmlWriter.startElement("WatchLastSentDate");
                    xmlWriter.writeData(threadWatch[5]);
                    xmlWriter.endElement("WatchLastSentDate");
                    xmlWriter.startElement("WatchEndDate");
                    xmlWriter.writeData(threadWatch[6]);
                    xmlWriter.endElement("WatchEndDate");
                    xmlWriter.endElement("ThreadWatch");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("ThreadWatchList");
         //} catch throw exportexception
    }

    public static void exportFavoriteThreadsForThread(XMLWriter xmlWriter, int threadID)
        throws IOException, ExportException, NumberFormatException, ObjectNotFoundException, DatabaseException {
        
        Collection favoriteThreads=ExportWebHelper.execSqlQuery(
                   "SELECT MemberID, FavoriteCreationDate,"+
                   " FavoriteType, FavoriteOption, FavoriteStatus"+
                   " FROM "+FavoriteThreadDAO.TABLE_NAME+
                   " WHERE ThreadID="+Integer.toString(threadID));
        Iterator iter=favoriteThreads.iterator();
        String[] favoriteThread=null;
        //try {
            xmlWriter.startElement("FavoriteThreadList");
            try {
                while ( (favoriteThread=(String[])iter.next()) != null) {
                    if (favoriteThread.length != 5) {
                        throw new ExportException("Error while retrieving data about favorite-thread records for threadID=="+threadID);
                    }
                    String memberName=DAOFactory.getMemberDAO().getMember(Integer.parseInt(favoriteThread[0])).getMemberName();
                    xmlWriter.startElement("FavoriteThread");
                    xmlWriter.startElement("MemberName");
                    xmlWriter.writeData(memberName);
                    xmlWriter.endElement("MemberName");
                    xmlWriter.startElement("FavoriteCreationDate");
                    xmlWriter.writeData(favoriteThread[1]);
                    xmlWriter.endElement("FavoriteCreationDate");
                    xmlWriter.startElement("FavoriteType");
                    xmlWriter.writeData(favoriteThread[2]);
                    xmlWriter.endElement("FavoriteType");
                    xmlWriter.startElement("FavoriteOption");
                    xmlWriter.writeData(favoriteThread[3]);
                    xmlWriter.endElement("FavoriteOption");
                    xmlWriter.startElement("FavoriteStatus");
                    xmlWriter.writeData(favoriteThread[4]);
                    xmlWriter.endElement("FavoriteStatus");
                    xmlWriter.endElement("FavoriteThread");
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("FavoriteThreadList");
         //} catch throw exportexception
    }

    public static void exportThread(XMLWriter xmlWriter, int threadID)
        throws NumberFormatException, IOException, ExportException, ObjectNotFoundException, DatabaseException {
        
        Collection thread1 = ExportWebHelper.execSqlQuery(
                   "SELECT MemberName, LastPostMemberName,"+
                   " ThreadTopic, ThreadBody, ThreadVoteCount, ThreadVoteTotalStars,"+
                   " ThreadCreationDate, ThreadLastPostDate, ThreadType, ThreadOption,"+
                   " ThreadStatus, ThreadHasPoll, ThreadViewCount, ThreadReplyCount,"+
                   " ThreadIcon, ThreadDuration, ThreadAttachCount"+
                   " FROM "+ThreadDAO.TABLE_NAME+
                   " WHERE ThreadID="+Integer.toString(threadID));
        Iterator iter = thread1.iterator();
        String[] thread = null;
        //try {
            try {
                if ( (thread=(String[])iter.next()) == null) {
                    throw new ExportException("Can't find data for threadID=="+threadID);
                }
                if (thread.length != 17) {
                    throw new ExportException("Error while retrieving data about thread with threadID=="+threadID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for threadID=="+threadID);
            }

            //if I am here, that means I now have correct object thread
            xmlWriter.startElement("Thread");

            xmlWriter.startElement("MemberName");
            xmlWriter.writeData(thread[0]);
            xmlWriter.endElement("MemberName");
            xmlWriter.startElement("ThreadLastPostMemberName");
            xmlWriter.writeData(thread[1]);
            xmlWriter.endElement("ThreadLastPostMemberName");
            xmlWriter.startElement("ThreadTopic");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(thread[2]));
            xmlWriter.endElement("ThreadTopic");
            xmlWriter.startElement("ThreadBody");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(thread[3]));
            xmlWriter.endElement("ThreadBody");
            xmlWriter.startElement("ThreadVoteCount");
            xmlWriter.writeData(thread[4]);
            xmlWriter.endElement("ThreadVoteCount");

            xmlWriter.startElement("ThreadVoteTotalStars");
            xmlWriter.writeData(thread[5]);
            xmlWriter.endElement("ThreadVoteTotalStars");
            xmlWriter.startElement("ThreadCreationDate");
            xmlWriter.writeData(thread[6]);
            xmlWriter.endElement("ThreadCreationDate");
            xmlWriter.startElement("ThreadLastPostDate");
            xmlWriter.writeData(thread[7]);
            xmlWriter.endElement("ThreadLastPostDate");
            xmlWriter.startElement("ThreadType");
            xmlWriter.writeData(thread[8]);
            xmlWriter.endElement("ThreadType");
            xmlWriter.startElement("ThreadOption");
            xmlWriter.writeData(thread[9]);
            xmlWriter.endElement("ThreadOption");

            xmlWriter.startElement("ThreadStatus");
            xmlWriter.writeData(thread[10]);
            xmlWriter.endElement("ThreadStatus");
            xmlWriter.startElement("ThreadHasPoll");
            xmlWriter.writeData(thread[11]);
            xmlWriter.endElement("ThreadHasPoll");
            xmlWriter.startElement("ThreadViewCount");
            xmlWriter.writeData(thread[12]);
            xmlWriter.endElement("ThreadViewCount");
            xmlWriter.startElement("ThreadReplyCount");
            xmlWriter.writeData(thread[13]);
            xmlWriter.endElement("ThreadReplyCount");
            xmlWriter.startElement("ThreadIcon");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(thread[14]));
            xmlWriter.endElement("ThreadIcon");

            xmlWriter.startElement("ThreadDuration");
            xmlWriter.writeData(thread[15]);
            xmlWriter.endElement("ThreadDuration");

            xmlWriter.startElement("ThreadAttachCount");
            xmlWriter.writeData(thread[16]);
            xmlWriter.endElement("ThreadAttachCount");
            exportThreadWatchesForThread(xmlWriter, threadID);
            exportFavoriteThreadsForThread(xmlWriter, threadID);
            PostXML.exportPostList(xmlWriter, threadID);

            xmlWriter.endElement("Thread");
         //} catch throw exportexception
    }

    //todo Igor important: merge exportThreadList and exportThread so I use only one SQL query
    //same for category(list), ...
    public static void exportThreadList(XMLWriter xmlWriter, int parentForumID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        
        Collection threadIDs=ExportWebHelper.execSqlQuery(
                   "SELECT ThreadID"+
                   " FROM "+ThreadDAO.TABLE_NAME+
                   " WHERE ForumID="+Integer.toString(parentForumID));
        Iterator iter=threadIDs.iterator();
        String[] threadID=null;
        //try {
            xmlWriter.startElement("ThreadList");
            try {
                while ( (threadID=(String[])iter.next()) !=null) {
                    if (threadID.length!=1) {
                        throw new ExportException("Error while retrieving list of threads.");
                    }
                    try {
                        int i=Integer.parseInt(threadID[0]);
                        exportThread(xmlWriter, i);
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of threads.");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("ThreadList");
         //} catch throw exportexception
    }

}
