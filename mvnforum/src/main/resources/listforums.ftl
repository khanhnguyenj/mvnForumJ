<#import "getcssrow.ftl" as lib>
<#list Rows as row>
<#if row.name == "header">
  <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
    <tr class="portlet-section-header">
      <td colspan="2">${row.forum_name_desc}</td>
      <td align="center">${row.thread_count}</td>
      <td align="center">${row.post_count}</td>
      <td align="center">${row.last_post}</td>
    </tr>
</#if>

<#if row.name == "category">
  <#if row.CanViewForums>
    <tr class="portlet-section-subheader" id="__${row.CategoryId}">
      <td colspan="5">
        <table class="noborder" cellpadding="0" cellspacing="0" width="100%">
          <tbody>
          <tr>
            <td>
              <a onclick="osexecute('__${row.CategoryId}');return false;" href="javascript:void(0)"><img border="0" height="13" width="14" src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/arrow-subnav-down.gif" alt="" /></a>
              <b>${row.CategoryName}</b><br/>
              <span class="portlet-font">${row.filter}</span>
            </td>
            <td valign="top" width="20">
              <#if row.enableEasyWatching>
                <#if row.isWatched>
                  <img src="${row.ImagePath}/button_watching.gif" border="0" alt="" title="${row.WatchingThisCategory}" />
                <#else>
              &nbsp;<a href="${row.addWatchCategoryLink}"><img src="${row.ImagePath}/button_watch.gif" border="0" alt="" title="${row.WatchThisCategory}" /></a>
                </#if>
              </#if>
            </td>
          </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </#if>
</#if>

<#if row.name == "forum">
  <#if row.checkForumToSee>
    <tr class="<@lib.getCSSRow posOfRow = row.forumCountInCurrentCategory/>" id="${row.jsforumprefix}">
      <td width="1%">
        <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/${row.forumIcon}" border="0" alt="" />
      </td>
      <td>
        <a href="${row.ListThreadsLink}" class="messageTopic">${row.ForumName}</a><br />
        ${row.filter}
      </td>
      <td align="center"><b>${row.ForumThreadCount}</b></td>
      <td align="center"><b>${row.ForumPostCount}</b></td>
      <td nowrap="nowrap">
        <#if row.checkCondition>
          ${row.no_post}
        <#else>
        <div>
          <div align="left">
            <a href="${row.ViewLastTopicLink}" class="messageTopic" title="${row.LastPostTopicName}">${row.LastPostTopicName_shorter}</a><br />
            ${row.by}
            <a href="${row.viewmemberLink}" class="memberName">${row.LastPostMemberName}</a>
          </div>
          <div align="right">
            ${row.GMTTimestampFormat}
          </div>
        </div>
        </#if>
      </td>
    </tr>
  </#if>
</#if>
<#if row.name == "footer">
  <#if row.checkNoCategory>
      <tr class="portlet-section-body">
        <td colspan="5" align="center">${row.no_category}</td>
      </tr>
    </table>
  <#else>
    </table>
    <#if row.showIconLegend>
      <br/>
      <table width="95%" border="0" align="center">
        <#if row.hasUnreadActiveCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_active.gif" border="0" alt="" /></td>
          <td>${row.has_new}</td>
        </tr>
        </#if>
        <#if row.hasReadActiveCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_active.gif" border="0" alt="" /></td>
          <td>${row.no_new}</td>
        </tr>
        </#if>
        <#if row.hasUnreadClosedCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_closed.gif" border="0" alt="" /></td>
          <td>${row.closed_has_new}</td>
        </tr>
        </#if>
        <#if row.hasReadClosedCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_closed.gif" border="0" alt="" /></td>
          <td>${row.closed_no_new}</td>
        </tr>
        </#if>
        <#if row.hasUnreadLockedCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_locked.gif" border="0" alt="" /></td>
          <td>${row.locked_has_new}</td>
        </tr>
        </#if>
        <#if row.hasReadLockedCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_locked.gif" border="0" alt="" /></td>
          <td>${row.locked_no_new}</td>
        </tr>
        </#if>
        <#if row.hasUnreadDisabledCurrentForum>
          <tr class="portlet-font">
            <td width="1%">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_disabled.gif" border="0" alt="" /></td>
            <td>${row.disabled_has_new}</td>
          </tr>
        </#if>
        <#if row.hasReadDisabledCurrentForum>
        <tr class="portlet-font">
          <td width="1%">
            <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_disabled.gif" border="0" alt="" /></td>
          <td>${row.disabled_no_new}</td>
        </tr>
        </#if>
      </table>
    </#if>
  </#if>
</#if>
</#list>
