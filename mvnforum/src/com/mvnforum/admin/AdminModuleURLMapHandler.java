/*
 * $Header: /cvsroot/mvnforum/mvnforum/src/com/mvnforum/admin/AdminModuleURLMapHandler.java,v 1.107 2008/12/22 02:20:19 lexuanttkhtn Exp $
 * $Author: lexuanttkhtn $
 * $Revision: 1.107 $
 * $Date: 2008/12/22 02:20:19 $
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
package com.mvnforum.admin;

import java.util.Locale;

import net.myvietnam.mvncore.exception.MissingURLMapEntryException;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.GenericParamUtil;
import net.myvietnam.mvncore.util.I18nUtil;
import net.myvietnam.mvncore.web.GenericRequest;

import com.mvnforum.MVNForumResourceBundle;

public class AdminModuleURLMapHandler {

    public AdminModuleURLMapHandler() {
    }

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we don't use the param request
     */
    public URLMap getMap(String requestURI, GenericRequest request)
        throws MissingURLMapEntryException {
        URLMap map = new URLMap();

        // ADMIN module
        if (requestURI.equals("") || requestURI.equals("/")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/index")) {
            map.setResponse("/mvnplugin/mvnforum/admin/index.jsp");
        } else if (requestURI.equals("/changemode")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/clearcache")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/error")) {
            map.setResponse("/mvnplugin/mvnforum/admin/error.jsp");

        } else if (requestURI.equals("/login")) {
            map.setResponse("/mvnplugin/mvnforum/admin/login.jsp");
        } else if (requestURI.equals("/loginprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/index");
        } else if (requestURI.equals("/logout")) {
            map.setResponse("/mvnplugin/mvnforum/admin/login.jsp");
        } else if (requestURI.equals("/testsystem")) {
            map.setResponse("/mvnplugin/mvnforum/admin/testsystem.jsp");
        } else if (requestURI.equals("/misctasks")) {
            map.setResponse("/mvnplugin/mvnforum/admin/misctasks.jsp");
        } else if (requestURI.equals("/importexport")) {
            map.setResponse("/mvnplugin/mvnforum/admin/importexport.jsp");
        } else if (requestURI.equals("/exportprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/exportsuccess.jsp");
        } else if (requestURI.equals("/deleteexportprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/importexport.jsp"); //go back to importexport
        /* "/importprocess" and "/getexportprocess" are handled without
         * using this url map handler, since they directly commit output, and
         * there should be no redirection/forward to JSPs
         */
        } else if (requestURI.equals("/rebuildindex")) {
            map.setResponse("/mvnplugin/mvnforum/admin/rebuildindexsuccess.jsp");

        } else if (requestURI.equals("/rankmanagement")) {
            map.setResponse("/mvnplugin/mvnforum/admin/rankmanagement.jsp");
        } else if (requestURI.equals("/addrankprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addranksuccess.jsp");
        } else if (requestURI.equals("/deleterankprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deleteranksuccess.jsp");
        } else if (requestURI.equals("/editrank")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editrank.jsp");
        } else if (requestURI.equals("/editrankprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editranksuccess.jsp");

        } else if (requestURI.equals("/usermanagement")) {
            map.setResponse("/mvnplugin/mvnforum/admin/usermanagement.jsp");
        } else if (requestURI.equals("/permissionsummary")) {
            map.setResponse("/mvnplugin/mvnforum/admin/permissionsummary.jsp");
        } else if (requestURI.equals("/viewmemberpermissions")) {
            map.setResponse("/mvnplugin/mvnforum/admin/viewmemberpermissions.jsp");
        } else if (requestURI.equals("/deletenonactivatedmembers")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletenonactivatedmembers.jsp");
        } else if (requestURI.equals("/deletenonactivatedmembersprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletenonactivatedmemberssuccess.jsp");
        } else if (requestURI.equals("/listpendingusers")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listpendingusers.jsp");
        } else if (requestURI.equals("/listpendingusersprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listpendinguserssuccess.jsp");

        } else if (requestURI.equals("/addmember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addmember.jsp");
        } else if (requestURI.equals("/addmemberprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/usermanagement");
        } else if (requestURI.equals("/changememberstatusprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/changememberstatussuccess.jsp");
        } else if (requestURI.equals("/viewmember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/viewmember.jsp");
        } else if (requestURI.equals("/editmember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmember.jsp");
        } else if (requestURI.equals("/updatemember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmembersuccess.jsp");
        } else if (requestURI.equals("/changepassword")) {
            map.setResponse("/mvnplugin/mvnforum/admin/changepassword.jsp");
        } else if (requestURI.equals("/changepasswordprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmembersuccess.jsp");
       } else if (requestURI.equals("/editmembertitle")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmembertitle.jsp");
        } else if (requestURI.equals("/editmembertitleprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmembertitlesuccess.jsp");
        } else if (requestURI.equals("/resetsignatureprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/resetsignaturesuccess.jsp");
        } else if (requestURI.equals("/resetavatarprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/resetavatarsuccess.jsp");
        } else if (requestURI.equals("/resetactivationprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/resetactivationsuccess.jsp");
        } else if (requestURI.equals("/deletewatch")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/viewmember?memberid=" + GenericParamUtil.getParameter(request, "memberid"));

        } else if (requestURI.equals("/groupmanagement")) {
            map.setResponse("/mvnplugin/mvnforum/admin/groupmanagement.jsp");
        } else if (requestURI.equals("/addgroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addgroup.jsp");
        } else if (requestURI.equals("/addgroupprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/groupmanagement");
        } else if (requestURI.equals("/deletegroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletegroup.jsp");
        } else if (requestURI.equals("/deletegroupprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/groupmanagement");
        } else if (requestURI.equals("/viewgroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/viewgroup.jsp");
        } else if (requestURI.equals("/assignforumtogroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/assignforumtogroup.jsp");
        } else if (requestURI.equals("/assigngrouptoforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/assigngrouptoforum.jsp");
        } else if (requestURI.equals("/editgroupinfo")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgroupinfo.jsp");
        } else if (requestURI.equals("/updategroupinfo")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgroupinfosuccess.jsp");
        } else if (requestURI.equals("/editgroupowner")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgroupowner.jsp");
        } else if (requestURI.equals("/updategroupowner")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgroupinfosuccess.jsp");
        } else if (requestURI.equals("/listmembergroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listmembergroup.jsp");
        } else if (requestURI.equals("/addmembergroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addmembergroup.jsp");
        } else if (requestURI.equals("/addmembergroupprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/listmembergroup?group=" + GenericParamUtil.getParameter(request, "group"));
        } else if (requestURI.equals("/editmembergroup")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmembergroup.jsp");
        } else if (requestURI.equals("/editmembergroupprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/listmembergroup?group=" + GenericParamUtil.getParameter(request, "groupid")+"&memberid="+ GenericParamUtil.getParameter(request, "memberid"));
        } else if (requestURI.equals("/deletemembergroupprocess")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/listmembergroup?group=" + GenericParamUtil.getParameter(request, "group"));
        } else if (requestURI.equals("/editgrouppermission")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgrouppermission.jsp");
        } else if (requestURI.equals("/updategrouppermission")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/editgrouppermission?group=" + GenericParamUtil.getParameter(request, "group"));

        } else if (requestURI.equals("/forummanagement")) {
            map.setResponse("/mvnplugin/mvnforum/admin/forummanagement.jsp");
        } else if (requestURI.equals("/updatecategoryorder")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/forummanagement");
        } else if (requestURI.equals("/updateforumorder")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/forummanagement");
        } else if (requestURI.equals("/editgroupforumpermission")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editgroupforumpermission.jsp");
        } else if (requestURI.equals("/editmemberforumpermission")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmemberforumpermission.jsp");
        } else if (requestURI.equals("/editmemberpermission")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editmemberpermission.jsp");
        } else if (requestURI.equals("/deletemember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletemember.jsp");
        } else if (requestURI.equals("/deletememberprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletemembersuccess.jsp");
        } else if (requestURI.equals("/updategroupforumpermission")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/editgroupforumpermission?group=" + GenericParamUtil.getParameter(request, "group") + "&forum=" + GenericParamUtil.getParameter(request, "forum") );
        } else if (requestURI.equals("/updatememberforumpermission")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/editmemberforumpermission?memberid=" + GenericParamUtil.getParameter(request, "memberid") + "&forum=" + GenericParamUtil.getParameter(request, "forum") );
        } else if (requestURI.equals("/updatememberpermission")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/editmemberpermission?memberid=" + GenericParamUtil.getParameter(request, "memberid"));

        } else if (requestURI.equals("/addforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addforum.jsp");
        } else if (requestURI.equals("/addforumprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addforumsuccess.jsp");
        } else if (requestURI.equals("/deleteforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deleteforum.jsp");
        } else if (requestURI.equals("/deleteforumprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deleteforumsuccess.jsp");
        } else if (requestURI.equals("/editforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editforum.jsp");
        } else if (requestURI.equals("/updateforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editforumsuccess.jsp");

        } else if (requestURI.equals("/addcategory")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addcategory.jsp");
        } else if (requestURI.equals("/addcategoryprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/addcategorysuccess.jsp");
        } else if (requestURI.equals("/deletecategory")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletecategory.jsp");
        } else if (requestURI.equals("/deletecategoryprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/deletecategorysuccess.jsp");
        } else if (requestURI.equals("/editcategory")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editcategory.jsp");
        } else if (requestURI.equals("/updatecategory")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editcategorysuccess.jsp");
        } else if (requestURI.equals("/listcategories")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listcategories.jsp");

        } else if (requestURI.equals("/sendmail")) {
            map.setResponse("/mvnplugin/mvnforum/admin/sendmail.jsp");
        } else if (requestURI.equals("/sendmailprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/sendmailsuccess.jsp");
        } else if (requestURI.equals("/sendactivatemailtoallprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/sendactivatemailtoallsuccess.jsp");

        } else if (requestURI.equals("/assignforumtomember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/assignforumtomember.jsp");
        } else if (requestURI.equals("/assignmembertoforum")) {
            map.setResponse("/mvnplugin/mvnforum/admin/assignmembertoforum.jsp");

        } else if (requestURI.equals("/configindex")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configindex.jsp");
        } else if (requestURI.equals("/configstepone")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configstepone.jsp");
        } else if (requestURI.equals("/configsteponeprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/configsteptwo")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configsteptwo.jsp");
        } else if (requestURI.equals("/configsteptwoprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/configstepthree")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configstepthree.jsp");
        } else if (requestURI.equals("/configstepthreeprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/configmvncore")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configmvncore.jsp");
        } else if (requestURI.equals("/configmvncoreprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/configurlpattern")) {
            map.setResponse("/mvnplugin/mvnforum/admin/configurlpattern.jsp");
        } else if (requestURI.equals("/configurlpatternprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/commitconfigs")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updateconfigurationsuccess.jsp");
        } else if (requestURI.equals("/configbackupprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/backupconfigurationsuccess.jsp");

        } else if (requestURI.equals("/edittemplate")) {
            map.setResponse("/mvnplugin/mvnforum/admin/edittemplate.jsp");
        } else if (requestURI.equals("/updatetemplate")) {
            map.setResponse("/mvnplugin/mvnforum/admin/edittemplate.jsp");

        } else if (requestURI.equals("/editcss")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editcss.jsp");
        } else if (requestURI.equals("/updatecss")) {
            map.setResponse("/mvnplugin/mvnforum/admin/editcss.jsp");
        } else if (requestURI.equals("/restorecss")) {
            map.setResponse("/mvnplugin/mvnforum/admin/restorecss.jsp");
        } else if (requestURI.equals("/restorecssprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/restorecsssuccess.jsp");

        } else if (requestURI.equals("/viewlogsystem")) {
            map.setResponse("/mvnplugin/mvnforum/admin/viewlogsystem.jsp");
        } else if (requestURI.equals("/logframe")) {
            map.setResponse("/mvnplugin/mvnforum/admin/logframe.jsp");
        } else if (requestURI.equals("/backupsystemlog")) {
            map.setResponse("/mvnplugin/mvnforum/admin/backupsystemlogsuccess.jsp");
        } else if (requestURI.equals("/listlogfiles")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listlogfiles.jsp");
        } else if (requestURI.equals("/deletelogfile")) {
            map.setResponse(AdminModuleConfig.getUrlPattern() + "/listlogfiles");
        /*
        "/downloadlogfile" will return null in Processor, so we don't need mapping here
        */

        } else if (requestURI.equals("/searchmember")) {
            map.setResponse("/mvnplugin/mvnforum/admin/searchmember.jsp");

        } else if (requestURI.equals("/updatememberexpireprocess")) {
            map.setResponse("/mvnplugin/mvnforum/admin/updatememberexpiresuccess.jsp");
        } else if (requestURI.equals("/listuserexpire")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listuserexpire.jsp");

        } else if (requestURI.equals("/bbcode")) {
            map.setResponse("/mvnplugin/mvnforum/user/bbcode.jsp");
        } else if (requestURI.equals("/eventlogs")) {
            map.setResponse("/mvnplugin/mvnforum/admin/listeventlogs.jsp");
        }

        Locale locale = I18nUtil.getLocaleInRequest(request);

        // unknown module, we throw an exception
        if (map.getResponse() == null) {
            //String errorMessage = "Cannot find matching entry in URLMap for '" + requestURI + "'. Please contact the administrator.";
            String localizedMessage = MVNForumResourceBundle.getString(locale, "mvncore.exception.MissingURLMapEntryException.cannot_find_matching_entry.AdminModule", new Object[] {requestURI});
            throw new MissingURLMapEntryException(localizedMessage);
            //throw new MissingURLMapEntryException(errorMessage);
        }
        return map;
    }
}
