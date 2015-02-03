<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%--
    Model Attributes:
        termList        - TermList
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
 --%>
<portlet:actionURL var="selectTermUrl">
  <portlet:param name="action" value="courseList"/>
</portlet:actionURL>

<portlet:renderURL var="courseScheduleUrl" escapeXml="false">
  <portlet:param name="action" value="showClassSchedule"/>
  <portlet:param name="view" value="grid"/>
  <portlet:param name="termCode" value="${coursesByTerm.termCode}"/>
</portlet:renderURL>

<div class="fl-widget portlet CoursesPortlet isMobile" role="section">
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

    <!-- Portlet Titlebar -->
    <div class="ui-grid-a">
      <div class="ui-block-a">
        <%@ include file="/WEB-INF/jsp/fragments/termSelect.jsp"%>
      </div>
      <div class="ui-block-b">
        <div data-role="navbar" data-theme="c" class="list-sched-toggle">
          <ul>
            <li><a href="javascript:;" class="toggled ui-corner-left ui-shadow">List</a></li>
            <li><a href="${courseScheduleUrl}" class="ui-corner-right ui-shadow">Schedule</a></li>
          </ul>
        </div>
      </div>
    </div>

    <!-- List of Courses -->
    <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseList.jsp"%>
  </div>
</div>

<script type="text/javascript">
  (function($){
    $('#${n}_termPicker').on('change', function(e){
      window.location = '${selectTermUrl}&pP_termCode=' + $(this).val();
    });
  })(coursesPortlet.jQuery);
</script>
