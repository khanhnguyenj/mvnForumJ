/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/user/UserModuleURLMapHandler.java,v 1.114 2009/05/15 02:03:45 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.114 $
 * $Date: 2009/05/15 02:03:45 $
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

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.*;

public class UserModuleURLMapHandler {

    public UserModuleURLMapHandler() {
    }

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we don't use the param request
     */
    public URLMap getMap(String requestURI, GenericRequest request, String localeName)
        throws MissingURLMapEntryException {

        //localeName = MyUtil.getLocaleNameAndSlash(localeName);
        URLMap map = new URLMap();

        if (requestURI.equals("/error")) {
            map.setResponse("/mvnplugin/mvnforum/user/error.jsp");
        } else if (requestURI.equals("") || requestURI.equals("/")) {
            map.setResponse(UserModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/index")) {
            if (MVNForumConfig.getEnablePortalLikeIndexPage()) {
                map.setResponse("/mvnplugin/mvnforum/user/index.jsp");//index.jsp
            } else {
                map.setResponse("/mvnplugin/mvnforum/user/listforums.jsp");//index.jsp
            }
        } else if (requestURI.equals("/listonlineusers")) {
            map.setResponse("/mvnplugin/mvnforum/user/listonlineusers.jsp");

        } else if (requestURI.equals("/listforums")) {
            map.setResponse("/mvnplugin/mvnforum/user/listforums.jsp");
        } else if (requestURI.equals("/listthreads")) {
            map.setResponse("/mvnplugin/mvnforum/user/listthreads.jsp");
        } else if (requestURI.equals("/listunansweredthreads")) {
            map.setResponse("/mvnplugin/mvnforum/user/listunansweredthreads.jsp");
        } else if (requestURI.equals("/listrecentthreads")) {
            map.setResponse("/mvnplugin/mvnforum/user/listrecentthreads.jsp");

        } else if (requestURI.equals("/addpost")) {
            map.setResponse("/mvnplugin/mvnforum/user/addpost.jsp");
        } else if (requestURI.equals("/addpostprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addpostsuccess.jsp");
        } else if (requestURI.equals("/editpost")) {
            map.setResponse("/mvnplugin/mvnforum/user/addpost.jsp");//editpost.jsp
        } else if (requestURI.equals("/updatepost")) {
            map.setResponse("/mvnplugin/mvnforum/user/updatepostsuccess.jsp");
        } else if (requestURI.equals("/printpost") || requestURI.startsWith("/printpost_")) {
            map.setResponse("/mvnplugin/mvnforum/user/printpost.jsp");
        } else if (requestURI.equals("/deletepost")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletepost.jsp");
        } else if (requestURI.equals("/deletepostprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletepostsuccess.jsp");

        } else if (requestURI.equals("/addattachment")) {
            map.setResponse("/mvnplugin/mvnforum/user/addattachment.jsp");
        } else if (requestURI.equals("/addattachmentprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addattachmentsuccess.jsp");
        } else if (requestURI.equals("/deleteattachment")) {
            map.setResponse("/mvnplugin/mvnforum/user/deleteattachment.jsp");
        } else if (requestURI.equals("/deleteattachmentprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deleteattachmentsuccess.jsp");
        } else if (requestURI.equals("/editattachment")) {
            map.setResponse("/mvnplugin/mvnforum/user/editattachment.jsp");
        } else if (requestURI.equals("/editattachmentprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/editattachmentsuccess.jsp");
        } else if (requestURI.equals("/listattachments")) {
            map.setResponse("/mvnplugin/mvnforum/user/listattachments.jsp");

        } else if (requestURI.equals("/viewthread") || requestURI.startsWith("/viewthread_")) {
            map.setResponse("/mvnplugin/mvnforum/user/viewthread.jsp");
        } else if (requestURI.equals("/printthread") || requestURI.startsWith("/printthread_")) {
            map.setResponse("/mvnplugin/mvnforum/user/printthread.jsp");

        } else if (requestURI.equals("/modcp")) {
            map.setResponse("/mvnplugin/mvnforum/user/modcp.jsp");
        } else if (requestURI.equals("/listrecentpendingthreads")) {
            map.setResponse("/mvnplugin/mvnforum/user/listrecentpendingthreads.jsp");
        } else if (requestURI.equals("/listthreadswithpendingposts")) {
            map.setResponse("/mvnplugin/mvnforum/user/listthreadswithpendingposts.jsp");
        } else if (requestURI.equals("/listrecentthreadswithpendingposts")) {
            map.setResponse("/mvnplugin/mvnforum/user/listrecentthreadswithpendingposts.jsp");
        } else if (requestURI.equals("/moderatependingthreads")) {
            map.setResponse("/mvnplugin/mvnforum/user/moderatependingthreads.jsp");
        } else if (requestURI.equals("/moderatependingthreadsprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/moderatependingthreadssuccess.jsp");
        } else if (requestURI.equals("/moderatependingposts")) {
            map.setResponse("/mvnplugin/mvnforum/user/moderatependingposts.jsp");
        } else if (requestURI.equals("/moderatependingpostsprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/moderatependingpostssuccess.jsp");

        } else if (requestURI.equals("/listpendingthreadsxml")) {
            map.setResponse("/mvnplugin/mvnforum/user/listpendingthreadsxml.jsp");

        } else if (requestURI.equals("/deletethread")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletethread.jsp");
        } else if (requestURI.equals("/deletethreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletethreadsuccess.jsp");

        } else if (requestURI.equals("/editthreadstatus")) {
            map.setResponse("/mvnplugin/mvnforum/user/editthreadstatus.jsp");
        } else if (requestURI.equals("/editthreadstatusprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/editthreadstatussuccess.jsp");
        } else if (requestURI.equals("/editthreadtype")) {
            map.setResponse("/mvnplugin/mvnforum/user/editthreadtype.jsp");
        } else if (requestURI.equals("/editthreadtypeprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/editthreadtypesuccess.jsp");

        } else if (requestURI.equals("/splitthread")) {
            map.setResponse("/mvnplugin/mvnforum/user/splitthread.jsp");
        } else if (requestURI.equals("/splitthreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/splitthreadsuccess.jsp");
        } else if (requestURI.equals("/movethread")) {
            map.setResponse("/mvnplugin/mvnforum/user/movethread.jsp");
        } else if (requestURI.equals("/movethreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/movethreadsuccess.jsp");
        // zt.add.start.080917
        } else if (requestURI.equals("/mergethread")) {
            map.setResponse("/mvnplugin/mvnforum/user/mergethread.jsp");
        } else if (requestURI.equals("/mergethreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/mergethreadsuccess.jsp");
        // zt.add.end.080917   
            
        } else if (requestURI.equals("/viewmember")) {
            map.setResponse("/mvnplugin/mvnforum/user/viewmember.jsp");
        } else if (requestURI.equals("/listmembers")) {
            map.setResponse("/mvnplugin/mvnforum/user/listmembers.jsp");
        } else if (requestURI.equals("/editmember")) {
            map.setResponse("/mvnplugin/mvnforum/user/editmember.jsp");
        } else if (requestURI.equals("/updatemember")) {
            map.setResponse("/mvnplugin/mvnforum/user/updatemembersuccess.jsp");

        } else if (requestURI.equals("/login")) {
            map.setResponse("/mvnplugin/mvnforum/user/login.jsp");
        } else if (requestURI.equals("/loginprocess")) {
            //map.setResponse("/mvnplugin/mvnforum/user/loginsuccess.jsp");
            map.setResponse(UserModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/logout")) {
            map.setResponse(UserModuleConfig.getUrlPattern() + "/index");
            //map.setResponse("/mvnplugin/mvnforum/user/login.jsp");
        } else if (requestURI.equals("/deletecookieprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletecookiesuccess.jsp");

        } else if (requestURI.equals("/rsssummary")) {
            map.setResponse("/mvnplugin/mvnforum/user/rsssummary.jsp");
        } else if (requestURI.equals("/atom")) {
            map.setResponse("/mvnplugin/mvnforum/user/atom.jsp");
        } else if (requestURI.equals("/rss")) {
            map.setResponse("/mvnplugin/mvnforum/user/rss.jsp");
        } else if (requestURI.equals("/rss2")) {
            map.setResponse("/mvnplugin/mvnforum/user/rss2.jsp");

        } else if (requestURI.equals("/help")) {
            map.setResponse("/mvnplugin/mvnforum/user/help.jsp");
        } else if (requestURI.equals("/helpintro")) {
            map.setResponse("/mvnplugin/mvnforum/docs/intro.html");
        } else if (requestURI.equals("/helpinstall")) {
            map.setResponse("/mvnplugin/mvnforum/docs/install.html");
        } else if (requestURI.equals("/helpuser")) {
            map.setResponse("/mvnplugin/mvnforum/docs/user.html");
        } else if (requestURI.equals("/helpadmin")) {
            map.setResponse("/mvnplugin/mvnforum/docs/admin.html");
        } else if (requestURI.equals("/helpdeveloper")) {
            map.setResponse("/mvnplugin/mvnforum/docs/developer.html");
        } else if (requestURI.equals("/faq")) {
            map.setResponse("/mvnplugin/mvnforum/docs/faq.html");

        } else if (requestURI.equals("/search")) {
            map.setResponse("/mvnplugin/mvnforum/user/search.jsp");
        } else if (requestURI.equals("/searchmember")) {
            map.setResponse("/mvnplugin/mvnforum/user/searchmember.jsp");
        } else if (requestURI.equals("/registermember")) {
            boolean agree = GenericParamUtil.getParameterBoolean(request, "agree");
            if ((MVNForumConfig.getEnableRegisterRule() == false) || agree ) {
                map.setResponse("/mvnplugin/mvnforum/user/addmember.jsp");
            } else {
                map.setResponse("/mvnplugin/mvnforum/user/viewrule.jsp");
            }
        } else if (requestURI.equals("/registermemberprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmembersuccess.jsp");

        } else if (requestURI.equals("/searchattachments")) {
            map.setResponse("/mvnplugin/mvnforum/user/searchattachments.jsp");

        } else if (requestURI.equals("/myprofile")) {
            map.setResponse("/mvnplugin/mvnforum/user/myprofile.jsp");
        } else if (requestURI.equals("/changemypassword")) {
            map.setResponse("/mvnplugin/mvnforum/user/changemypassword.jsp");
        } else if (requestURI.equals("/changemypasswordprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/changemypasswordsuccess.jsp");
        } else if (requestURI.equals("/changeemail")) {
            map.setResponse("/mvnplugin/mvnforum/user/changeemail.jsp");
        } else if (requestURI.equals("/changeemailprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/changeemailsuccess.jsp");
        } else if (requestURI.equals("/changesignature")) {
            map.setResponse("/mvnplugin/mvnforum/user/changesignature.jsp");
        } else if (requestURI.equals("/changesignatureprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/changesignaturesuccess.jsp");

        } else if (requestURI.equals("/changeavatar")) {
            map.setResponse("/mvnplugin/mvnforum/user/changeavatar.jsp");
        } else if (requestURI.equals("/uploadavatar")) {
            map.setResponse(UserModuleConfig.getUrlPattern() + "/myprofile");
        } else if (requestURI.equals("/updateavatar")) {
            map.setResponse(UserModuleConfig.getUrlPattern() + "/myprofile");
        } else if (requestURI.equals("/mywatch")) {
            map.setResponse("/mvnplugin/mvnforum/user/mywatch.jsp");
        } else if (requestURI.equals("/addwatch")) {
            map.setResponse("/mvnplugin/mvnforum/user/addwatch.jsp");
        } else if (requestURI.equals("/addwatchprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addwatchsuccess.jsp");
        } else if (requestURI.equals("/deletewatchprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletewatchsuccess.jsp");
        } else if (requestURI.equals("/editwatch")) {
            map.setResponse("/mvnplugin/mvnforum/user/editwatch.jsp");
        } else if (requestURI.equals("/editwatchprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/editwatchsuccess.jsp");

        } else if (requestURI.equals("/mymessage")) {
            map.setResponse("/mvnplugin/mvnforum/user/mymessage.jsp");
        } else if (requestURI.equals("/addmessage")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessage.jsp");
        } else if (requestURI.equals("/addmessageprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessagesuccess.jsp");
        } else if (requestURI.equals("/sendmail")) {
            map.setResponse("/mvnplugin/mvnforum/user/sendmail.jsp");
        } else if (requestURI.equals("/sendmailprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/sendmailsuccess.jsp");
        } else if (requestURI.equals("/viewmessage")) {
            map.setResponse("/mvnplugin/mvnforum/user/viewmessage.jsp");
        } else if (requestURI.equals("/sendmessageprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessagesuccess.jsp");
        } else if (requestURI.equals("/deletemessageprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletemessagesuccess.jsp");
        } else if (requestURI.equals("/processmessage")) {
            map.setResponse("/mvnplugin/mvnforum/user/processmessagesuccess.jsp");
        } else if (requestURI.equals("/addmessageattachment")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessageattachment.jsp");
        } else if (requestURI.equals("/addmessageattachmentprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessageattachmentsuccess.jsp");

        } else if (requestURI.equals("/mymessagefolder")) {
            map.setResponse("/mvnplugin/mvnforum/user/mymessagefolder.jsp");
        } else if (requestURI.equals("/addmessagefolder")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessagefolder.jsp");
        } else if (requestURI.equals("/addmessagefolderprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addmessagefoldersuccess.jsp");
        } else if (requestURI.equals("/deletemessagefolder")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletemessagefolder.jsp");
        } else if (requestURI.equals("/deletemessagefolderprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletemessagefoldersuccess.jsp");
        } else if (requestURI.equals("/updatefolderorder")) {
            map.setResponse(UserModuleConfig.getUrlPattern() + "/mymessagefolder");

        } else if (requestURI.equals("/myfavoritethread")) {
            map.setResponse("/mvnplugin/mvnforum/user/myfavoritethread.jsp");
        } else if (requestURI.equals("/addfavoritethreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/addfavoritethreadsuccess.jsp");
        } else if (requestURI.equals("/deletefavoritethreadprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/deletefavoritethreadsuccess.jsp");

        } else if (requestURI.equals("/iforgotpasswords")) {
            map.setResponse("/mvnplugin/mvnforum/user/forgotpassword.jsp");
        } else if (requestURI.equals("/forgotpasswordprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/forgotpasswordsuccess.jsp");
        } else if (requestURI.equals("/resetpassword")) {
            map.setResponse("/mvnplugin/mvnforum/user/resetpassword.jsp");
        } else if (requestURI.equals("/resetpasswordprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/resetpasswordsuccess.jsp");
        } else if (requestURI.equals("/bbcode")) {
            map.setResponse("/mvnplugin/mvnforum/user/bbcode.jsp");

        } else if (requestURI.equals("/sendactivationcode")) {
            map.setResponse("/mvnplugin/mvnforum/user/sendactivationcode.jsp");
        } else if (requestURI.equals("/sendactivationcodeprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/sendactivationcodesuccess.jsp");
        } else if (requestURI.equals("/activatemember")) {
            map.setResponse("/mvnplugin/mvnforum/user/activatemember.jsp");
        } else if (requestURI.equals("/activatememberprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/activatemembersuccess.jsp");
        
        } else if (requestURI.equals("/guestsetting")) {
            map.setResponse("/mvnplugin/mvnforum/user/guestsetting.jsp");
        } else if (requestURI.equals("/guestsettingprocess")) {
            map.setResponse("/mvnplugin/mvnforum/user/guestsetting.jsp");
        }
        Locale locale = I18nUtil.getLocaleInRequest(request);
        // unknown module, we throw an exception
        if (map.getResponse() == null) {
            //String errorMessage = "Cannot find matching entry in URLMap for '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] {requestURI});
            throw new MissingURLMapEntryException(localizedMessage);
            //throw new MissingURLMapEntryException(errorMessage);
        }
        return map;
    }
}
