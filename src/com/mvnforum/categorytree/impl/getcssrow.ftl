<!-- Get CSSRow -->
<#macro getCSSRow posOfRow>
  <#if (posOfRow%2 == 0) >
    portlet-section-body
  <#else>
    portlet-section-alternate
  </#if>
</#macro>