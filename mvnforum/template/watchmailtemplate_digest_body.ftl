mvnForum Watch Summary
New Threads since: ${lastSent}
Compiled at: ${now}
---------------------------------------
For the latest Forum updates, visit : ${forumBase}/index
---------------------------------------

<#list threadWatchList as mailbeans >
  <#if mailbeans.leader >
[Category: ${mailbeans.categoryName}] [Forum: ${mailbeans.forumName}]
  </#if>
    Thread [${mailbeans.threadTopic}] given to us by ${mailbeans.memberName}
      last posted by ${mailbeans.lastPostMemberName} on ${mailbeans.threadLastPostDate}
      ${mailbeans.threadUrl}
    
</#list>


This is not a spam mail. If you would not like to receive this email,
please visit : ${forumBase}/mywatch