/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/PostXML.java,v 1.17 2009/01/06 18:31:30 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.17 $
 * $Date: 2009/01/06 18:31:30 $
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
import java.util.*;

import net.myvietnam.mvncore.exception.*;
import net.myvietnam.mvncore.filter.DisableHtmlTagFilter;
import net.myvietnam.mvncore.filter.EnableHtmlTagFilter;

import com.mvnforum.admin.importexport.XMLUtil;
import com.mvnforum.admin.importexport.XMLWriter;
import com.mvnforum.db.DAOFactory;
import com.mvnforum.db.PostDAO;

/**
 * @author Igor Manic
 * @version $Revision: 1.17 $, $Date: 2009/01/06 18:31:30 $
 * <br/>
 * <code>PostXML</code> todo Igor: enter description
 *
 */
public class PostXML {

    public PostXML() {
        postID = -1;
        parentPostID = 0;
        parentThreadID = -1;
        parentForumID = -1;
        parentCategoryID = -1;
    }

    private int postID;
    /** Returns <code>PostID</code> of this post or
      * <code>-1</code> if post is not created yet. */
    public int getPostID() { return postID; }

    private int parentPostID;
    /** Returns <code>PostID</code> of this post's parent post (which means this post is
      * reply to that parent post), or <code>0</code> if this post is not created yet or
      * has no parent post (first post in a thread). */
    public int getParentPostID() { return parentPostID; }

    private int parentThreadID;
    /** Returns <code>ThreadID</code> of this post's parent thread or
      * <code>-1</code> if this post is not created yet. */
    public int getParentThreadID() { return parentThreadID; }

    private int parentForumID;
    /** Returns <code>ForumID</code> of this post's parent forum or
      * <code>-1</code> if this post is not created yet. */
    public int getParentForumID() { return parentForumID; }

    private int parentCategoryID;
    /** Returns <code>CategoryID</code> of this post's parent category or
      * <code>-1</code> if this post is not created yet. */
    public int getParentCategoryID() { return parentCategoryID; }

    public void setPostID(String id) {
        postID=XMLUtil.stringToIntDef(id, -1);
    }

    public void setParentPost(Object o)
    throws ForeignKeyNotFoundException {
        if (o instanceof PostXML) {
            parentPostID=((PostXML)o).getPostID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent post's ID");
        }
    }

    public void setParentPostID(int value) {
        if (value < 0) {
            parentPostID = -1;
        } else {
            parentPostID = value;
        }
    }


    /* WARNING: this post can be reply (in which case next 3 setParent***() methods
     * will get PostXML from the Digester stack), or it can be the first post in
     * (a thread (in which case they will get ThreadXML from the stack)
     */
    public void setParentThread(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof ThreadXML) {
            parentThreadID=((ThreadXML)o).getThreadID();
        } else if (o instanceof PostXML) {
            parentThreadID=((PostXML)o).getParentThreadID();
        } else {
            throw new ForeignKeyNotFoundException("Can't find parent thread's ID");
        }
    }

    public void setParentThreadID(int value) {
        if (value < 0) {
            parentThreadID = -1;
        } else {
            parentThreadID = value;
        }
    }

    public void setParentForum(Object o)
        throws ForeignKeyNotFoundException {
        if (o instanceof ThreadXML) {
            parentForumID=((ThreadXML)o).getParentForumID();
        } else if (o instanceof PostXML) {
            parentForumID=((PostXML)o).getParentForumID();
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
        if (o instanceof ThreadXML) {
            parentCategoryID=((ThreadXML)o).getParentCategoryID();
        } else if (o instanceof PostXML) {
            parentCategoryID=((PostXML)o).getParentCategoryID();
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
     * Creates a post. All argument values (<code>int</code>s, <code>Timestamp</code>s, ...)
     * are represented as <code>String</code>s, because of more convenient using
     * of this method for XML parsing.
     *
     * @param memberName Member that created the post. Can be null.
     * @param lastEditMemberName Can be null.
     * @param postTopic Subject of post.
     * @param postBody Message body.
     * @param postCreationDate Can be null.
     * @param postLastEditDate Can be null.
     * @param postCreationIP Can be null.
     * @param postLastEditIP Can be null.
     * @param postEditCount Can be null.
     * @param postFormatOption Can be null.
     * @param postOption Can be null.
     * @param postStatus Can be null.
     * @param postIcon Can be null.
     * @param postAttachCount Can be null.
     *
     * @throws CreateException
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     * @throws DatabaseException
     * @throws ForeignKeyNotFoundException
     *
     */
    public void addPost(String memberName, String lastEditMemberName,
                        String postTopic, String postBody,
                        String postCreationDate, String postLastEditDate,
                        String postCreationIP, String postLastEditIP,
                        String postEditCount, String postFormatOption,
                        String postOption, String postStatus,
                        String postIcon, String postAttachCount)
        throws CreateException, ObjectNotFoundException,DatabaseException, ForeignKeyNotFoundException {

        if (parentPostID < 0) {
            throw new CreateException("Can't create a post, because no parent post assigned yet.");
        } else if (parentThreadID < 0) {
            throw new CreateException("Can't create a post, because no parent thread assigned yet.");
        } else if (parentForumID < 0) {
            throw new CreateException("Can't create a post, because no parent forum assigned yet.");
        }

        if ((postTopic==null) || (postBody==null)) {
            throw new CreateException("Can't create a post with empty PostBody.");
        } else {
            java.sql.Timestamp postCreationDate1;
            java.sql.Timestamp postLastEditDate1;
            int postEditCount1;
            int postFormatOption1;
            int postOption1;
            int postStatus1;
            int postAttachCount1;

            try {
                if (memberName == null) {
                    memberName = "";
                }
                if (lastEditMemberName == null) {
                    lastEditMemberName = "";
                }
                postCreationDate1 = XMLUtil.stringToSqlTimestampDefNow(postCreationDate);
                postLastEditDate1 = XMLUtil.stringToSqlTimestampDefNull(postLastEditDate);
                if (postCreationIP == null) {
                    postCreationIP = "0.0.0.0";
                }
                if (postLastEditIP == null) {
                    postLastEditIP = "0.0.0.0";
                }
                postEditCount1 = XMLUtil.stringToIntDef(postEditCount, 0);
                postFormatOption1 = XMLUtil.stringToIntDef(postFormatOption, 0);
                postOption1 = XMLUtil.stringToIntDef(postOption, 0);
                postStatus1 = XMLUtil.stringToIntDef(postStatus, 0);
                if (postIcon == null) {
                    postIcon = "";
                }
                postAttachCount1 = XMLUtil.stringToIntDef(postAttachCount, 0);
            } catch (NumberFormatException e) {
                throw new CreateException("Invalid data for a post. Expected a number.");
            }

            int memberID = 0;
            if (!memberName.equals("")) {
                memberID=DAOFactory.getMemberDAO().getMemberIDFromMemberName(memberName);
            }

            postTopic = EnableHtmlTagFilter.filter(postTopic);
            postBody = EnableHtmlTagFilter.filter(postBody);
            postIcon = EnableHtmlTagFilter.filter(postIcon);
            this.postID = DAOFactory.getPostDAO().createPost(parentPostID,
                           parentForumID, parentThreadID,
                           memberID, memberName, lastEditMemberName,
                           postTopic, postBody, postCreationDate1, postLastEditDate1,
                           postCreationIP, postLastEditIP,
                           postEditCount1, postFormatOption1, postOption1,
                           postStatus1, postIcon, postAttachCount1);
        }
    }


    public void increasePostAttachCount()
        throws ObjectNotFoundException, DatabaseException {
        if (postID < 0) {
            throw new ObjectNotFoundException("Can't update PostAttachCount on post that is not created yet.");
        }
        // we don't want the exception to throw below this
        int attachCount = DAOFactory.getAttachmentDAO().getNumberOfAttachments_inPost(postID);
        DAOFactory.getPostDAO().updateAttachCount(postID, attachCount);
    }


// ===============================================================
// ==================== STATIC EXPORT METHODS ====================
// ===============================================================
    public static void exportPost(XMLWriter xmlWriter, int postID)
        throws NumberFormatException, IOException, ExportException, ObjectNotFoundException, DatabaseException {
        
        Collection post1=ExportWebHelper.execSqlQuery(
                   "SELECT MemberName, LastEditMemberName,"+
                   " PostTopic, PostBody, PostCreationDate, PostLastEditDate,"+
                   " PostCreationIP, PostLastEditIP, PostEditCount, PostFormatOption,"+
                   " PostOption, PostStatus, PostIcon, PostAttachCount"+
                   " FROM "+PostDAO.TABLE_NAME+
                   " WHERE PostID="+Integer.toString(postID));
        Iterator iter = post1.iterator();
        String[] post = null;
        //try {
            try {
                if ( (post=(String[])iter.next()) == null) {
                    throw new ExportException("Can't find data for postID="+postID);
                }
                if (post.length != 14) {
                    throw new ExportException("Error while retrieving data about post with postID="+postID);
                }
            } catch (NoSuchElementException e) {
                throw new ExportException("Can't find data for postID=="+postID);
            }

            //if I am here, that means I now have correct object post
            xmlWriter.startElement("Post");

            xmlWriter.startElement("MemberName");
            xmlWriter.writeData(post[0]);
            xmlWriter.endElement("MemberName");
            xmlWriter.startElement("LastEditMemberName");
            xmlWriter.writeData(post[1]);
            xmlWriter.endElement("LastEditMemberName");
            xmlWriter.startElement("PostTopic");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(post[2]));
            xmlWriter.endElement("PostTopic");
            xmlWriter.startElement("PostBody");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(post[3]));
            xmlWriter.endElement("PostBody");
            xmlWriter.startElement("PostCreationDate");
            xmlWriter.writeData(post[4]);
            xmlWriter.endElement("PostCreationDate");

            xmlWriter.startElement("PostLastEditDate");
            xmlWriter.writeData(post[5]);
            xmlWriter.endElement("PostLastEditDate");
            xmlWriter.startElement("PostCreationIP");
            xmlWriter.writeData(post[6]);
            xmlWriter.endElement("PostCreationIP");
            xmlWriter.startElement("PostLastEditIP");
            xmlWriter.writeData(post[7]);
            xmlWriter.endElement("PostLastEditIP");
            xmlWriter.startElement("PostEditCount");
            xmlWriter.writeData(post[8]);
            xmlWriter.endElement("PostEditCount");
            xmlWriter.startElement("PostFormatOption");
            xmlWriter.writeData(post[9]);
            xmlWriter.endElement("PostFormatOption");

            xmlWriter.startElement("PostOption");
            xmlWriter.writeData(post[10]);
            xmlWriter.endElement("PostOption");
            xmlWriter.startElement("PostStatus");
            xmlWriter.writeData(post[11]);
            xmlWriter.endElement("PostStatus");
            xmlWriter.startElement("PostIcon");
            xmlWriter.writeData(DisableHtmlTagFilter.filter(post[12]));
            xmlWriter.endElement("PostIcon");
            xmlWriter.startElement("PostAttachCount");
            xmlWriter.writeData(post[13]);
            xmlWriter.endElement("PostAttachCount");

            AttachmentXML.exportAttachmentList(xmlWriter, postID);
            exportPostList_Replies(xmlWriter, postID/*parentPostID*/);
            xmlWriter.endElement("Post");
         //} catch throw exportexception
    }

    public static void exportPostList_FirstPosts(XMLWriter xmlWriter, int parentThreadID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        
        Collection postIDs=ExportWebHelper.execSqlQuery(
                   "SELECT PostID"+
                   " FROM "+PostDAO.TABLE_NAME+
                   " WHERE ThreadID="+Integer.toString(parentThreadID)+
                   " AND ParentPostID=0");
        Iterator iter = postIDs.iterator();
        String[] postID = null;
        //try {
            xmlWriter.startElement("PostList");
            try {
                while ( (postID=(String[])iter.next()) != null) {
                    if (postID.length != 1) {
                        throw new ExportException("Error while retrieving list of posts in threadID="+parentThreadID+".");
                    }
                    try {
                        int i = Integer.parseInt(postID[0]);
                        exportPost(xmlWriter, i);
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of posts in threadID="+parentThreadID+".");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("PostList");
        //} catch throw exportexception
    }

    public static void exportPostList_Replies(XMLWriter xmlWriter, int parentPostID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        
        Collection postIDs=ExportWebHelper.execSqlQuery(
                   "SELECT PostID"+
                   " FROM "+PostDAO.TABLE_NAME+
                   " WHERE ParentPostID=" + Integer.toString(parentPostID));
        Iterator iter = postIDs.iterator();
        String[] postID = null;
        //try {
            xmlWriter.startElement("PostList");
            try {
                while ( (postID=(String[])iter.next()) !=null) {
                    if (postID.length != 1) {
                        throw new ExportException("Error while retrieving list of replies to postID="+parentPostID+".");
                    }
                    try {
                        int i = Integer.parseInt(postID[0]);
                        exportPost(xmlWriter, i);
                    } catch (NumberFormatException e) {
                        throw new ExportException("Error while retrieving list of replies to postID="+parentPostID+".");
                    }
                }
            } catch (NoSuchElementException e) {
                //no more database records
            }
            xmlWriter.endElement("PostList");
        //} catch throw exportexception
    }

    //todo Igor important: merge exportPostList and exportPost so I use only one SQL query
    //same for category(list), ...
    public static void exportPostList(XMLWriter xmlWriter, int parentThreadID)
        throws IOException, ExportException, ObjectNotFoundException, DatabaseException {
        /* Export only root posts (of thread parentThreadID) here.
         * Replies will be exported under <PostList> of each root <Post>.
         */
        exportPostList_FirstPosts(xmlWriter, parentThreadID);
    }

}
