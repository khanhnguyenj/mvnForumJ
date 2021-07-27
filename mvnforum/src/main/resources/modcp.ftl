<#import "getcssrow.ftl" as lib>
<#list Rows as row>
  <#if row.name == "header">
    <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
      <tr class="portlet-section-header">
        <td>${row.forum_name_desc}</td>
        <td align="center">${row.pending_threads}</td>
        <td align="center">${row.threads_with_pending_posts}</td>
        <td align="center">${row.pending_posts}</td>
      </tr>
  </#if>
  <#if row.name == "category">
    <tr class="portlet-section-subheader">
      <td colspan="4">
        <b>${row.categoryName}</b><br/>
        ${row.MyUtilFilter}
      </td>
    </tr>
  </#if>
  <#if row.name == "forum">
    <#if row.checkForumToSee>
      <tr class="<@lib.getCSSRow posOfRow = row.forumCountInCurrentCategory/>">
        <td>
          <a href="${row.urlListThreads}" class="messageTopic">${row.forumName}</a><br/>
            ${row.filterForumDesc}
        </td>
        <td align="center" class ="${row.PendingThreadCountClass}">
          ${row.PendingThreadCount}
          <#if row.checkPendingThreadCount>
            <br/><a class="command" href="${row.moderatependingthreads}">${row.pending_threads}</a>
          </#if>
        </td>
        <td align="center" class="${row.ThreadsWithPendingPostsCountClass}">
          ${row.ThreadsWithPendingPostsCount}
          <#if row.checkThreadsWithPendingPostsCount>
            <br/><a class="command" href="${row.listthreadswithpendingposts}">${row.threads_has_pending_posts}</a>
          </#if>
        </td>
        <td align="center" class="${row.PendingPostCountClass}">
          ${row.PendingPostCount}
        </td>
       </tr>
    </#if>
  </#if>
  <#if row.name == "footer">
    <#if row.checkNoCategory>
      <tr>
        <td colspan="4" align="center" class="portlet-section-body">${row.no_category}</td>
      </tr>
    </#if>
    </table>
  </#if>
  <#if row.name == "separator">
    <#if row.checkNoForum>
      <tr class="portlet-section-body">
        <td colspan="19" align="center">${row.no_forum}</td>
      </tr>
    </#if>
  </#if>
</#list>

