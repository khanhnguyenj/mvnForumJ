<#import "getcssrow.ftl" as lib>
<#list Rows as row>
  <#if row.RowType == "Header">
    <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
      <tr class="portlet-section-header" align="center">
        <td>${row.ForumTitle}</td>
        <td>Edit</td>
        <td>Delete</td>
        <td>Assign</td>

        <td>${row.ThreadTitle}</td>
        <td>Add</td>
        <td>Moderate</td>

        <td>${row.PostTitle}</td>
        <td>Read</td>
        <td>Add</td>
        <td>Edit Own</td>
        <td>Edit Any</td>
        <td>Delete</td>

        <td>${row.AttachmentTitle}</td>
        <td>Add</td>
        <td>Get</td>

        <td>${row.PollTitle}</td>
        <td>Add</td>
        <td>Edit Own</td>
        <td>Edit Any</td>
        <td>Delete</td>
      </tr>
  </#if>

  <#if row.RowType == "Category">
    <tr class="portlet-section-subheader">
      <td colspan="21">
        <b>${row.CategoryName}</b>
      </td>
    </tr>
    <#if row.Empty >
    <tr class="portlet-section-body"><td colspan="21" align="center">${row.no_forum}</td></tr>
    </#if>
  </#if>
  <#if row.RowType == "Forum">
    <tr class="<@lib.getCSSRow posOfRow = row.forumCountInCurrentCategory/>" align="center">
      <td align="left">${row.ForumName}</td>
      <td><@check permission=row.EditForum/></td>
      <td><@check permission=row.DeleteForum/></td>
      <td><@check permission=row.AssignForum/></td>

      <td class="messageTextBold">${row.ThreadTitle} &raquo;</td>
      <td><@check permission=row.AddThread/></td>
      <td><@check permission=row.ModerateThread/></td>

      <td class="messageTextBold">${row.PostTitle} &raquo;</td>
      <td><@check permission=row.ReadPost/></td>
      <td><@check permission=row.AddPost/></td>
      <td><@check permission=row.EditOwnPost/></td>
      <td><@check permission=row.EditAnyPost/></td>
      <td><@check permission=row.DeletePost/></td>

      <td class="messageTextBold">${row.AttachmentTitle} &raquo;</td>
      <td><@check permission=row.AddAttachment/></td>
      <td><@check permission=row.GetAttachment/></td>

      <td class="messageTextBold">${row.PollTitle} &raquo;</td>
      <td><@check permission=row.AddPoll/></td>
      <td><@check permission=row.EditOwnPoll/></td>
      <td><@check permission=row.EditAnyPoll/></td>
      <td><@check permission=row.DeletePoll/></td>
    </tr>
  </#if>
  <#if row.RowType == "Footer">
    <#if row.checkNoCategory>
        <tr class="portlet-section-body">
          <td colspan="21" align="center">${row.no_category}</td>
        </tr>
    </#if>
    </table>
  </#if>
</#list>
<#macro check permission>
  <#if permission>
    <font color="#008000">${YesValue}</font>
  <#else>
    <font color="#FF0080">${NoValue}</font>
  </#if>
</#macro>