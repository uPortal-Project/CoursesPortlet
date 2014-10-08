<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/header.jsp"/>

<div data-role="header" class="titlebar portlet-titlebar courses-back-div">
  <h2 class="title course-catalog-name">Error</h2>
</div>
<div class="fl-widget portlet CoursesPortlet isMobile" role="section">
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">
    <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>

    <!-- Portlet Titlebar -->
    <div data-role="navbar" data-theme="c" class="list-sched-nav">
      <ul>
        <li><a href="${courseListUrl}" class="ui-corner-left ui-shadow">List</a></li>
        <li><a href="${classScheduleGridUrl}" class="ui-corner-right ui-shadow">Schedule</a></li>
      </ul>
    </div>

    <div data-role="content">
      <spring:message code="${exception.displayMessg}" htmlEscape="false" arguments="${portletSessionScope.helpDeskURL}"/>
      <span style="display:none;">${exception.message}<br/></span>
      <span style="display:none;">${exception.exceptionMessage}<br/></span>
      <span style="display:none;">${exception.printStackTraceMessage}<br/></span>
    </div>
  </div>
</div>
<script type="text/javascript">
  (function($){
    $(function(){
      $('.portlet-title').on('click', function(){
        window.location = "${courseListUrl}";
      });
    });
  })(coursesPortlet.jQuery);
</script>
