<?xml version="1.0" encoding="utf-8" ?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ page errorPage="fatalerror.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="com.mvnforum.db.*" %>

<%
/*
CategoryCache categoryCache = CategoryCache.getInstance();
Collection categoryBeans = categoryCache.getBeans();
ForumCache forumCache = ForumCache.getInstance();
Collection forumBeans = forumCache.getBeans();
Collection pendingThreadBeans = (Collection) request.getAttribute("PendingThreadBeans");
Collection threadWithPendingPostsBeans = (Collection) request.getAttribute("ThreadWithPendingPostsBeans");

out.println("<RootForum>");
for (Iterator categoryIterator = categoryBeans.iterator(); categoryIterator.hasNext(); ) {
    CategoryBean categoryBean = (CategoryBean)categoryIterator.next();
    out.println("  " + categoryBean.getXMLTag());
    for (Iterator forumIterator = forumBeans.iterator(); forumIterator.hasNext(); ) {
        ForumBean forumBean = (ForumBean)forumIterator.next();
        if (categoryBean.getCategoryID() == forumBean.getCategoryID()) {
            out.println("    " + forumBean.getXMLTag());
            int currentForumID = forumBean.getForumID();
            for (Iterator pendingThreadIterator = pendingThreadBeans.iterator(); pendingThreadIterator.hasNext(); ) {
                ThreadBean threadBean = (ThreadBean)pendingThreadIterator.next();
                if (threadBean.getForumID() == currentForumID) {
                    out.println("      " + threadBean.getXMLTag());
                    out.println("      </Thread>");
                }
            }//loop on pending threads
            for (Iterator threadWithPendingPostsIterator = threadWithPendingPostsBeans.iterator(); threadWithPendingPostsIterator.hasNext(); ) {
                ThreadBean threadBean = (ThreadBean)threadWithPendingPostsIterator.next();
                if (threadBean.getForumID() == currentForumID) {
                    out.println("      " + threadBean.getXMLTag());
                    Collection pendingPosts = threadBean.getPendingPosts();
                    if (pendingPosts != null) {
                        for (Iterator pendingPostIterator = pendingPosts.iterator(); pendingPostIterator.hasNext(); ) {
                            PostBean postBean = (PostBean)pendingPostIterator.next();
                            out.println("        " + postBean.getXMLTag());
                            out.println("        </Post>");
                        }
                    }
                    out.println("      </Thread>");
                }
            }// loop on threads with pending posts
            out.println("    </Forum>");
        }// if it is a current forum in category
    }// loop in forum
    out.println("  </Category>");
}//loop in category
out.println("</RootForum>");
*/
%>
