<#import "getcssrow.ftl" as lib>
<#list Rows as row>
  <#if row.name == "header">
    <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
      <tr class="portlet-section-header">
        <td>Forum Names</td>
        <td align="center" style="width:80px;">RSS 0.91</td>
        <td align="center" style="width:80px;">RSS 2.0</td>
        <td align="center" style="width:80px;">ATOM</td>
      </tr>
  </#if>
  <#if row.name == "category">
    <tr class="portlet-section-subheader">
      <td colspan="4" class="messageTextBold">
      ${row.categoryName}
      </td>
    </tr>
  </#if>
  <#if row.name == "forum">
    <#if row.check_permission>
      <tr class="<@lib.getCSSRow posOfRow = row.forumCountInCurrentCategory/>">
        <td>${row.forumName}</td>
        <td align="center" style="width:80px;">
          <a href="${row.urlRSS091}"><img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/xml.gif" alt="RSS 0.91 ${row.forum_specific_feed}" title="RSS 0.91 ${row.forum_specific_feed}" border="0" /></a>
        </td>
        <td align="center" style="width:80px;">
          <a href="${row.urlRSS20}"><img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/rss.gif" alt="RSS 2.0 ${row.forum_specific_feed}" title="RSS 2.0 ${row.forum_specific_feed}" border="0" /></a>
        </td>
        <td align="center" style="width:80px;">
          <a href="${row.urlATOM}"><img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/atom.gif" alt="ATOM ${row.forum_specific_feed}" title="ATOM ${row.forum_specific_feed}" border="0" /></a>
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
  </#if>
</#list>
