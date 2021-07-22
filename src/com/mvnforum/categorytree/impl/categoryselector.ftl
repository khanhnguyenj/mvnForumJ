<#list Rows as row>
  <#if row.name == "header">
    <#if row.checkPageNotNull>
      <#if row.isSearchPage>
      <!-- SEARCH -->
      <select id="forum" name="forum">
          <option value="0" selected="selected">${row.search_forum}</option>
      <#else>
        <!-- assignforumtogroup and movethread has the same style -->
        <#if row.isPageContentAssignForumToGroup>
          <#if row.isPageContentMoveThread>
            <select id="destforum" name="destforum">
              <option value="">${row.choose_forum}</option>
          <#else>
            <select id="forum" name="forum" class="liteoption">
              <option value="">${row.choose_forum}</option>
          </#if>
        <#else>
          <!-- listrecentthreads  -->
          <!-- consist of Category (has forum child) and Forum -->
          <#if row.isPageListUnansweredThreads>
            <select id="forum_category_option" name="forum_category_option" onchange= "">
            <option value="">${row.choose_forum}</option>
          <#else>
            <select id="FastGoForum" name="FastGoForum" onchange= "gotoPage(this.options[this.selectedIndex].value)">
            <option value="">${row.choose_forum}</option>
          </#if>  
        </#if>
      </#if>
    <#else>
      <#if row.isAddForum>
        <#if row.addwatch>
          <select id="category" name="category">
        <#else>
          <select id="CategoryID" name="CategoryID">
        </#if>
        <option value="">${row.choose_category}</option>
      </#if>
    </#if>
  </#if>

  <#if row.name == "category">
    <#if row.noForum_Condition>
    <#else>
      <#if row.page_Condition>

        <option value="${row.url_CategoryId}"
        <#if row.forumId_Condition>
          selected="selected"
        <#else>
          <#if row.addForum>
            <#if row.categoryId_Condition>
              selected="selected"
            </#if>
          </#if>
        </#if>
        >

      <#else>
        <option value=""></option>
        <#if row.search_Page>
          <option id="${row.forum_CategoryId}" value = "-${row.url_CategoryId}">
        <#else>
            <#if row.assignforumtogroup_Page>
              <option value ="">
            <#else>
              <option value="${row.destURL}"
              <#if row.isPageListUnansweredThreads>
                <#if row.select_this_category>
                  selected="selected"
                </#if>
              </#if>
              >
            </#if>
        </#if>
      </#if>
      ${row.catName}
      </option>
      <#if row.page_ElseCondition>
        <option value="">--------------------------------</option>
      </#if>
    </#if>
  </#if>

  <#if row.name == "forum">
  <#if row.hasForums>
    <#if row.page_Condition>
      <#if row.permiss>
        <#if row.search_Page>
          <#if row.isMovethreadPage>
            <#if row.checkID>
              <option value="${row.foID}">
            </#if>
          <#else>
            <option value="${row.foID}">
          </#if>
        <#else>
          <option value="${row.destURL}"
          <#if row.isPageListUnansweredThreads>
            <#if row.select_this_forum>
              selected="selected"
            </#if>
          <#else>
            <#if row.forumId_Condition>
              selected="selected"
            </#if>
          </#if>
          >
        </#if>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <#if row.search_Page>
          <#if row.isMovethreadPage>
            <#if row.checkID>
              ${row.forumName}</option>
            </#if>
          <#else>
            ${row.forumName}</option>
          </#if>
        <#else>
          ${row.forumName}</option>
        </#if>
       </#if>
     </#if>
   </#if>
   </#if>

   <#if row.name == "footer">
     </select>
   </#if>
</#list>

