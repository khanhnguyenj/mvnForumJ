<#list Rows as row>
  <#if row.RowType == "header">
    <table class="tborder" width="95%" cellspacing="0" cellpadding="3" align="center">
    <tr class="portlet-section-header">
      <td colspan="2">${row.name_desc}</td>
      <td align="center">${row.order}</td>
      <td align="center">${row.forumOwnerName}</td>
      <td align="center">${row.creation_date}</td>
      <td align="center">${row.type}</td>
      <td align="center">${row.mode_moderation}</td>
      <td align="center">${row.add}</td>
      <td align="center">${row.group_permission}</td>
      <td align="center">${row.member_permission}</td>
      <td align="center">${row.edit}</td>
      <td align="center">${row.delete}</td>
    </tr>
  </#if>

  <#if row.RowType == "Category">
    <tr class="portlet-section-subheader">
      <td colspan="2">
        <b>${row.CatName}</b><br/>
        <span class="portlet-font">${row.FilteredCatDesc}</span>
      </td>
      <td align="center">
        <#if row.CanEditCat>
          <table width="100%" class="noborder">
            <tr class="portlet-font">
              <td width="40%" align="center">
                <#if row.CatOrder gt 0 >
                  <a href="${row.CatUpLink}">
                    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="${row.moveUp}" title="${row.moveUp}" />
                  </a>
                </#if>
              </td>
              <td width="40%" align="center"><b>${row.CatOrder}</b></td>
              <td width="30%" align="center">
                <a href="${row.CatDownLink}">
                  <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/down.gif" border="0" alt="${row.moveDown}" title="${row.moveDown}" />
                </a>
              </td>
            </tr>
          </table>
        <#else>
          <b>${row.CatOrder}</b>
        </#if>
      </td>
      <td align="center"></td>
      <td align="center" class="portlet-font">${row.GMTCatCreationDate}</td>
      <td align="center"></td>
      <td align="center"></td>
      <td align="center">
        <#if row.CanAddForum>
          <a href="${row.AddForumLink}" class="command">${row.add_forum}</a>
        </#if>
      </td>
      <td align="center">&nbsp;</td>
      <td align="center">&nbsp;</td>
      <td align="center">
        <#if row.CanEditCat>
          <a href="${row.EditCatLink}">
            <img src="${row.ImagePath}/button_edit.gif" border="0" alt="Edit category:  ${row.CatName}" />
          </a>
        </#if>
      </td>
      <td align="center">
        <#if row.CanDeleteCat>
           <a class="command" href="${row.deleteCathref}">${row.delete}</a>
        </#if>
      </td>
    </tr>
    <#if row.isEmptyCategory>
      <tr class="portlet-section-body">
        <td colspan="12" align="center">${row.no_forum_in_category}</td>
      </tr>
    </#if>
  </#if>

  <#if row.RowType == "Forum">
    <#if row.CanReadPost>
      <tr class="portlet-section-body">
      <td width="1%" align="center" nowrap="nowrap">
        <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/${row.forumIcon}" border="0" alt="" />
      </td>
      <td width="30%">
         <b <#if row.Disabled > class= "disabledItem" </#if>>${row.ForumName}</b><br/>
         ${row.FilteredForumDesc}
      </td>
      <td align="center">
        <#if row.CanEditForum>
          <table width="100%" class="noborder">
            <tr class="portlet-section-body">
              <td width="30%" align="center">
                <#if row.ForumOrder gt 0 >
                  <a href="${row.ForumUpLink}">
                    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/up.gif" border="0" alt="${row.moveUp}" title="${row.moveUp}" />
                  </a>
                </#if>
              </td>
              <td width="40%" align="center"><b>${row.ForumOrder}</b></td>
              <td width="30%" align="center">
                <a href="${row.ForumDownLink}">
                  <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/down.gif" border="0" alt="${row.moveDown}" title="${row.moveDown}" />
                </a>
              </td>
            </tr>
          </table>
        <#else>
          <b>${row.ForumOrder}</b>
        </#if>
      </td>
      <td align="center">${row.ForumOwnerName}</td>
      <td align="center">${row.GMTForumCreationDate}</td>
      <td align="center">${row.ForumTypeName}</td>
      <td align="center">${row.ForumModeName}</td>
      <td align="center">&nbsp;</td>
      <td align="center">
      <#if row.CanAssignForum>
        <a href="${row.GroupAssignLink}">
          <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/group.gif" border="0" alt="Edit Group Permissions for forum : ${row.ForumName}" />
        </a>
      </#if>
      </td>
      <td align="center">
      <#if row.CanAssignForum>
        <a href="${row.MemberAssignLink}">
          <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/user.gif" border="0" alt="Edit Member Permissions for forum : ${row.ForumName}" />
        </a>
      </#if>
      </td>
      <td align="center">
      <#if row.CanEditForum>
        <a href="${row.EditForumLink}">
          <img src="${row.ImagePath}/button_edit.gif" border="0" alt="Edit forum: ${row.ForumName}" />
        </a>
      </#if>
      </td>
      <td align="center">
      <#if row.CanDeleteForum>
        <a class="command" href="${row.DeleteForumLink}">${row.delete}</a>
      </#if>
      </td>
    </tr>
    </#if>
  </#if>

  <#if row.RowType == "footer">
    <#if row.checkNoCategory>
        <tr class="portlet-section-body">
          <td colspan="12" align="center">${row.no_category}</td>
        </tr>
      </table>
    <#else>
      </table>
      <#if row.showIconLegend>
        <br/>
        <table width="95%" border="0" align="center">
        <#if row.hasUnreadActiveCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_active.gif" border="0" alt="" /></td>
            <td>${row.has_new}</td>
          </tr>
        </#if>
        <#if row.hasReadActiveCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_active.gif" border="0" alt="" /></td>
            <td>${row.no_new}</td>
          </tr>
        </#if>
        <#if row.hasUnreadClosedCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_closed.gif" border="0" alt="" /></td>
            <td>${row.closed_has_new}</td>
          </tr>
        </#if>
        <#if row.hasReadClosedCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_closed.gif" border="0" alt="" /></td>
            <td>${row.closed_no_new}</td>
          </tr>
        </#if>
        <#if row.hasUnreadLockedCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_locked.gif" border="0" alt="" /></td>
            <td>${row.locked_has_new}</td>
          </tr>
        </#if>
        <#if row.hasReadLockedCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_locked.gif" border="0" alt="" /></td>
            <td>${row.locked_no_new}</td>
          </tr>
        </#if>
        <#if row.hasUnreadDisabledCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_unread_disabled.gif" border="0" alt="" /></td>
            <td>${row.disabled_has_new}</td>
          </tr>
        </#if>
        <#if row.hasReadDisabledCurrentForum>
          <tr class="portlet-font">
            <td width="16">
              <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/f_read_disabled.gif" border="0" alt="" /></td>
            <td>${row.disabled_no_new}</td>
          </tr>
        </#if>
        </table>
      </#if>
    </#if>
  </#if>
</#list>
