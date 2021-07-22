<#import "getcssrow.ftl" as lib>
<#list Rows as row>
<#if row.RowType == "header">
  <table class="tborder" border="0" cellspacing="0" cellpadding="3" width="100%">
  <tr>
    <td align="center" class="portlet-section-header"><a href="${row.urlOfListforums}" class="portlet-section-header">${row.title}</a></td>
  </tr>
</#if>
<#if row.RowType == "Category">
  <#if row.CanViewForums>
  <tr>
    <td align="left" class="portlet-section-subheader">
      <a onclick="osexecute('__${row.CategoryId}');return false;" href="javascript:void(0)">
        <img align="right" border="0" height="13" width="14" src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/arrow-subnav-down.gif" alt="" />
      </a><b><a href="${row.ListForumsLink}">${row.CategoryName}</a></b>
    </td>
  </tr>
  </#if>
</#if>

<#if row.RowType == "Forum">
  <#if row.CanReadPost && row.NotDisabled >
  <tr class="<@lib.getCSSRow posOfRow = row.forumCountInCurrentCategory/>" align="center" id="${row.jsforumprefix}">
    <td align="center">
      <a href="${row.ListThreadsLink}">${row.ForumName}</a>
    </td>
  </tr>
  </#if>
</#if>

<#if row.RowType == "footer">
  <#if row.checkNoCategory>
      <tr>
        <td align="center" class="portlet-section-body">${row.no_category}</td>
      </tr>
    </table>
  <#else>
    <!-- for last category -->
    </table>
  </#if>
</#if>
</#list>
