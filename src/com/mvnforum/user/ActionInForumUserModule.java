/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/ActionInForumUserModule.java,v 1.1 2009/02/02 08:49:13 minhnn Exp $
 * $Author: minhnn $
 * $Revision: 1.1 $
 * $Date: 2009/02/02 08:49:13 $
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
package com.mvnforum.user;

import java.util.Locale;

import net.myvietnam.mvncore.exception.BadInputException;
import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.util.*;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;
import com.mvnforum.auth.AbstractLocalizableAction;
import com.mvnforum.auth.Action;

public class ActionInForumUserModule extends AbstractLocalizableAction {

    public ActionInForumUserModule(GenericRequest request, String requestURI) throws MissingURLMapEntryException {
//        lastRequestTime = System.currentTimeMillis();
//        firstRequestTime = lastRequestTime;// be very careful

        url  = null;// url may be null after the code below
        localeKey = null;// but localeKey is never be null
        // the request SHOULD ONLY be used to get the queryString
        String queryString = StringUtil.getEmptyStringIfNull(request.getQueryString());
        Locale locale = I18nUtil.getLocaleInRequest(request);
        
        if (requestURI.equals("/error")) {
            localeKey = "mvnforum.user.action.desc.Error";
        } else if (requestURI.equals("/index") || requestURI.equals("") || requestURI.equals("/")) {
            url = "index";
            localeKey = "mvnforum.user.action.desc.Index";
        } else if (requestURI.equals("/listonlineusers")) {
            url = "listonlineusers";
            localeKey = "mvnforum.user.action.desc.ListOnlineUsers";
        } else if (requestURI.equals("/listforums")) {
            url = "listforums";
            localeKey = "mvnforum.user.action.desc.ListForums";
        } else if (requestURI.equals("/listthreads")) {
            url = "listthreads" + "?" + queryString;

            pageID = Action.PAGE_ID_LISTTHREADS;

            try {
                int forum = GenericParamUtil.getParameterInt(request, "forum");
                this.localeParams = new Object[]{new Integer(forum)};

                pageParam = new Integer(forum);

                localeKey = "mvnforum.user.action.desc.ListThreads.without_forum";
            } catch (BadInputException ex) {
                localeKey = "mvnforum.user.action.desc.ListThreads.with_forum";
            }
        } else if (requestURI.equals("/listrecentthreads")) {
            url = "listrecentthreads" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.ListRecentThreads";
        } else if (requestURI.equals("/listunansweredthreads")) {
            url = "listunansweredthreads" + "?" + queryString;
            try {
                int forum = GenericParamUtil.getParameterInt(request, "forum");
                this.localeParams = new Object[]{new Integer(forum)};
                localeKey = "mvnforum.user.action.desc.ListUnansweredThreads";
            } catch (BadInputException ex) {
                localeKey = "mvnforum.user.action.desc.ListUnansweredThreads";
            }
        } else if (requestURI.equals("/listattachments")) {
            localeKey = "mvnforum.user.action.desc.ListAttachments";


        } else if (requestURI.equals("/addpost")) {
            localeKey = "mvnforum.user.action.desc.AddPost";
        } else if (requestURI.equals("/addpostprocess")) {
            localeKey = "mvnforum.user.action.desc.AddPostProcess";
        } else if (requestURI.equals("/editpost")) {
            // maybe we can allow other users to view the post
            localeKey = "mvnforum.user.action.desc.EditPost";
        } else if (requestURI.equals("/updatepost")) {
            // maybe we can allow other users to view the post
            localeKey = "mvnforum.user.action.desc.UpdatePost";
        } else if (requestURI.equals("/addattachment")) {
            localeKey = "mvnforum.user.action.desc.AddAttachment";
        } else if (requestURI.equals("/addattachmentprocess")) {
            localeKey = "mvnforum.user.action.desc.AddAttachmentProcess";
        } else if (requestURI.equals("/myfavoritethread")) {
            url = "myfavoritethread";
            localeKey = "mvnforum.user.action.desc.MyFavoriteThread";
        } else if (requestURI.equals("/addfavoritethreadprocess")) {
            url = "addfavoritethreadprocess" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.AddFavoriteThreadProcess";
        } else if (requestURI.equals("/deletefavoritethreadprocess")) {
            localeKey = "mvnforum.user.action.desc.DeleteFavoriteThreadProcess";
        } else if (requestURI.equals("/viewthread") || requestURI.startsWith("/viewthread_")) {
            url = requestURI + "?" + queryString;
            if (requestURI.startsWith("/viewthread_")) {
                url = FriendlyURLParamUtil.getURLFromFriendlyURL(requestURI).substring(1);
            }
            pageID = Action.PAGE_ID_VIEWTHREAD;

            int thread = ((Integer)request.getAttribute("thread")).intValue();
            localeParams = new Object[]{new Integer(thread)};

            pageParam = new Integer(thread);

            localeKey = "mvnforum.user.action.desc.ViewThread.without_thread";
        } else if (requestURI.equals("/printthread") || requestURI.startsWith("/printthread_")) {
            url = "printthread" + "?" + queryString;
            if (requestURI.startsWith("/printthread_")) {
                url = FriendlyURLParamUtil.getURLFromFriendlyURL(requestURI).substring(1);
            }
            int thread = ((Integer)request.getAttribute("thread")).intValue();
            localeParams = new Object[]{new Integer(thread)};

            localeKey = "mvnforum.user.action.desc.PrintThread.without_input_error";
        } else if (requestURI.equals("/printpost") || requestURI.startsWith("/printpost_")) {
            url = "printpost" + "?" + queryString;
            if (requestURI.startsWith("/printpost_")) {
                url = FriendlyURLParamUtil.getURLFromFriendlyURL(requestURI).substring(1);
            }
            int post = ((Integer)request.getAttribute("post")).intValue();
            localeParams = new Object[] {new Integer(post)};

            localeKey = "mvnforum.user.action.desc.PrintPost.without_input_error";
        } else if (requestURI.equals("/viewmember")) {
            url = "viewmember" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.ViewMember";
            localeParams = new Object[] {queryString};
        } else if (requestURI.equals("/listmembers")) {
            url = "listmembers" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.ListMembers";
        } else if (requestURI.equals("/editmember")) {
            localeKey = "mvnforum.user.action.desc.EditMember";
        } else if (requestURI.equals("/updatemember")) {
            localeKey = "mvnforum.user.action.desc.UpdateMember";
        } else if (requestURI.equals("/login")) {
            url = "login";
            localeKey = "mvnforum.user.action.desc.Login";
        } else if (requestURI.equals("/loginprocess")) {// will be sendRedirect
            localeKey = "mvnforum.user.action.desc.LoginProcess";
        } else if (requestURI.equals("/logout")) {
            localeKey = "mvnforum.user.action.desc.Logout";
        } else if (requestURI.equals("/deletecookieprocess")) {
            localeKey = "mvnforum.user.action.desc.DeleteCookiesProcess";
        } else if (requestURI.equals("/rsssummary")) {
            url = "rsssummary" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.RSS";
        } else if (requestURI.equals("/atom")) {
            url = "atom" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.RSS";
        } else if (requestURI.equals("/rss")) {
            url = "rss" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.RSS";
        } else if (requestURI.equals("/rss2")) {
            url = "rss2" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.RSS";
            
        } else if (requestURI.equals("/help")) {
            url = "help";
            localeKey = "mvnforum.user.action.desc.Help";
        } else if (requestURI.equals("/helpintro")) {
            url = "helpintro";
            localeKey = "mvnforum.user.action.desc.HelpIntro";
        } else if (requestURI.equals("/helpinstall")) {
            url = "helpinstall";
            localeKey = "mvnforum.user.action.desc.HelpInstall";
        } else if (requestURI.equals("/helpuser")) {
            url = "helpuser";
            localeKey = "mvnforum.user.action.desc.HelpUser";
        } else if (requestURI.equals("/helpadmin")) {
            url = "helpadmin";
            localeKey = "mvnforum.user.action.desc.HelpAdmin";
        } else if (requestURI.equals("/helpdeveloper")) {
            url = "helpdeveloper";
            localeKey = "mvnforum.user.action.desc.HelpDeveloper";
        } else if (requestURI.equals("/faq")) {
            url = "faq";
            localeKey = "mvnforum.user.action.desc.FAQ";
            
        } else if (requestURI.equals("/search")) {
            url = "search";
            localeKey = "mvnforum.user.action.desc.Search";
            
        } else if (requestURI.equals("/searchattachments")) {
            url = "searchattachments";
            localeKey = "mvnforum.user.action.desc.SearchAttachments";

        } else if (requestURI.equals("/searchmember")) {
            url = "searchmember" + "?" + queryString;
            localeKey = "mvnforum.user.action.desc.SearchMember";
        } else if (requestURI.equals("/registermember")) {
            url = "registermember";
            localeKey = "mvnforum.user.action.desc.RegisterMember";
        } else if (requestURI.equals("/registermemberprocess")) {
            localeKey = "mvnforum.user.action.desc.RegisterMemberProcess";

        } else if (requestURI.equals("/myprofile")) {
            localeKey = "mvnforum.user.action.desc.MyProfile";
        } else if (requestURI.equals("/changemypassword")) {
            localeKey = "mvnforum.user.action.desc.ChangePassword";
        } else if (requestURI.equals("/changemypasswordprocess")) {
            localeKey = "mvnforum.user.action.desc.ChangePasswordProcess";
        } else if (requestURI.equals("/changeemail")) {
            localeKey = "mvnforum.user.action.desc.ChangeEmail";
        } else if (requestURI.equals("/changeemailprocess")) {
            localeKey = "mvnforum.user.action.desc.ChangeEmailProcess";
        } else if (requestURI.equals("/changesignature")) {
            localeKey = "mvnforum.user.action.desc.ChangeSignature";
        } else if (requestURI.equals("/changesignatureprocess")) {
            localeKey = "mvnforum.user.action.desc.ChangeSignatureProcess";
        } else if (requestURI.equals("/changeavatar")) {
            localeKey = "mvnforum.user.action.desc.ChangeAvatar";
        } else if (requestURI.equals("/uploadavatar")) {
            localeKey = "mvnforum.user.action.desc.UploadAvatar";
        } else if (requestURI.equals("/updateavatar")) {
            localeKey = "mvnforum.user.action.desc.UpdateAvatar";
            
        } else if (requestURI.equals("/mywatch")) {
            localeKey = "mvnforum.user.action.desc.MyWatch";
        } else if (requestURI.equals("/addwatch")) {
            localeKey = "mvnforum.user.action.desc.AddWatch";
        } else if (requestURI.equals("/addwatchprocess")) {
            localeKey = "mvnforum.user.action.desc.AddWatchProcess";
        } else if (requestURI.equals("/deletewatchprocess")) {
            localeKey = "mvnforum.user.action.desc.DeleteWatchProcess";
        } else if (requestURI.equals("/editwatch")) {
            localeKey = "mvnforum.user.action.desc.EditWatch";
        } else if (requestURI.equals("/editwatchprocess")) {
            localeKey = "mvnforum.user.action.desc.EditWatchProcess";

        } else if (requestURI.equals("/mymessage")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessage")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessageprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/viewmessage")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/sendmessageprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/deletemessageprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/processmessage")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessageattachment")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessageattachmentprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/mymessagefolder")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessagefolder")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/addmessagefolderprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/deletemessagefolder")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/deletemessagefolderprocess")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";
        } else if (requestURI.equals("/updatefolderorder")) {
            localeKey = "mvnforum.user.action.desc.UsingMessage";

        } else if (requestURI.equals("/splitthread")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/splitthreadprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/deletethread")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/deletethreadprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
// zt.add.start.080917
        } else if (requestURI.equals("/mergethread")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/mergethreadprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
// zt.add.end.080917

        } else if (requestURI.equals("/movethread")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/movethreadprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/editthreadstatus")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/editthreadstatusprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/editthreadtype")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/editthreadtypeprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/modcp")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/listrecentpendingthreads")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/listthreadswithpendingposts")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/listrecentthreadswithpendingposts")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/moderatependingthreads")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/moderatependingthreadsprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/moderatependingposts")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/moderatependingpostsprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/listpendingthreadsxml")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/deletepost")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/deletepostprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/deleteattachment")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/deleteattachmentprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/editattachment")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";
        } else if (requestURI.equals("/editattachmentprocess")) {
            localeKey = "mvnforum.user.action.desc.ModerateForum";

        } else if (requestURI.equals("/iforgotpasswords")) {
            url = "iforgotpasswords";
            localeKey = "mvnforum.user.action.desc.IForgotPasswords";
        } else if (requestURI.equals("/forgotpasswordprocess")) {
            localeKey = "mvnforum.user.action.desc.ForgotPasswordProcess";
        } else if (requestURI.equals("/resetpassword")) {
            url = "resetpassword";
            localeKey = "mvnforum.user.action.desc.ResetPassword";
        } else if (requestURI.equals("/resetpasswordprocess")) {
            localeKey = "mvnforum.user.action.desc.ResetPasswordProcess";

        } else if (requestURI.equals("/sendactivationcode")) {
            url = "sendactivationcode";
            localeKey = "mvnforum.user.action.desc.SendActivationCode";
        } else if (requestURI.equals("/sendactivationcodeprocess")) {
            localeKey = "mvnforum.user.action.desc.SendActivationCodeProcess";
        } else if (requestURI.equals("/activatemember")) {
            url = "activatemember";
            localeKey = "mvnforum.user.action.desc.ActivateMember";
        } else if (requestURI.equals("/activatememberprocess")) {
            localeKey = "mvnforum.user.action.desc.ActivateMemberProcess";
            
        } else if (requestURI.equals("/getmvncoreimage")) {
            localeKey = "mvnforum.user.action.desc.GetImage";
        } else if (requestURI.equals("/getmvnforumimage")) {
            localeKey = "mvnforum.user.action.desc.GetImage";
        } else if (requestURI.equals("/getavatar")) {
            localeKey = "mvnforum.user.action.desc.GetImage";
        } else if (requestURI.equals("/captchaimage")) {
            localeKey = "mvnforum.user.action.desc.GetImage";
            
        } else if (requestURI.equals("/getattachment")) {
            localeKey = "mvnforum.user.action.desc.GetAttachment";
        } else if (requestURI.equals("/getpmattachment")) {
            localeKey = "mvnforum.user.action.desc.GetAttachment";

        } else if (requestURI.equals("/sendmail")) {
            localeKey = "mvnforum.user.action.desc.SendEmail";
        } else if (requestURI.equals("/sendmailprocess")) {
            localeKey = "mvnforum.user.action.desc.SendEmailProcess";
        } else if (requestURI.equals("/bbcode")) {
            url = "bbcode";
            localeKey = "mvnforum.user.action.desc.BBCodeHelp";
            
        } else if (requestURI.equals("/guestsetting")) {
            localeKey = "mvnforum.user.action.desc.guestsetting";
        } else if (requestURI.equals("/guestsettingprocess")) {
            localeKey = "mvnforum.user.action.desc.guestsetting";
            
        } else if (requestURI.equals("/ajaxvalidate")) {
            localeKey = "mvnforum.user.action.desc.validate";

        }

        // this localeKey should never be null
        if (localeKey == null) {
            //String errorMessage = "Cannot find matching entry in ActionInUserModule for '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] {requestURI});
            MissingURLMapEntryException e = new MissingURLMapEntryException(localizedMessage);
            throw e;
        }
    }

}
