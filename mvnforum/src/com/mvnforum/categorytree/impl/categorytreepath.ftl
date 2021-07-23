<#import "getcssrow.ftl" as lib>
<#list Rows as row>
<#if row.name == "header">
  <img src="${row.ContextPath}/mvnplugin/mvnforum/images/nav2.gif" border="0" height="15" width="15" alt="" />&nbsp;
  <a href="${row.indexURL}" class="nav1">${row.index_desc}</a>
  <#if row.enablePortalLikeIndexPage>
    <#--
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_bar.gif" border="0" height="15" width="15" alt="" />
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/nav2.gif" border="0" height="15" width="15" alt="" />&nbsp;
    -->
    &nbsp;&raquo;&nbsp;<a class="nav1" href="${row.listForumsLink}">${row.listForums}</a>
  </#if>
  <br/>
</#if>
<#if row.name == "category">
  <#assign x=row.Spacer>
    <#list 1..x as i>
      <#if i != 1>
        <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_blank.gif" border="0" height="15" width="15" alt="" />
      </#if>
    </#list>
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_bar.gif" border="0" height="15" width="15" alt="" />
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_folder_open.gif" border="0" height="15" width="15" alt="" />&nbsp;
    <span class="nav1">${row.CategoryPrefix}:</span>&nbsp;<a href="${row.CategoryURL}" class="nav1">${row.CategoryName}</a><br/>
</#if>

<#if row.name == "forum">
  <#assign x= row.Spacer>
    <#list 1..x as i>
      <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_blank.gif" border="0" height="15" width="15" alt="" />
    </#list>
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_bar.gif" border="0" height="15" width="15" alt="" />
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_folder_open_topic.gif" border="0" height="15" width="15" alt="" />&nbsp;
    <span class="nav1">${row.ForumPrefix}:</span>&nbsp;<#if row.ShowAllForumsURL><a href="${row.ForumURL}" class="nav1"><#else><span class="rowcurrent"></#if>${row.ForumName}<#if row.ShowAllForumsURL></a><#else></span></#if>
    <br/>
</#if>

<#if row.name == "footer">
  <#if row.ShowAllForumsURL>
    <#if row.Spacer != 0>
      <#assign x = row.Spacer>
      <#list 1..x as i>
        <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_blank.gif" border="0" height="15" width="15" alt="" />
      </#list>
    </#if>
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_bar.gif" border="0" height="15" width="15" alt="" />
    <img src="${row.ContextPath}/mvnplugin/mvnforum/images/icon/icon_thread_topic.gif" border="0" height="15" width="15" alt="" />&nbsp;
    <span class="rowcurrent">${row.Content}</span>
  </#if>
</#if>
</#list>