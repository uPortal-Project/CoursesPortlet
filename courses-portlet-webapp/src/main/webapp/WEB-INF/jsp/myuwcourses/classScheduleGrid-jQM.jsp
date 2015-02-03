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
<jsp:directive.include file="/WEB-INF/jsp/header.jsp"/>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page import="org.joda.time.LocalDate" %>
<%--
    Model Attributes:
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
        courseList      - List<Course>
        classScheduleList     - List<CourseSectionWrapper>
        otherCourses          - List<CourseSectionWrapper>
        classScheduleListJson - String of classScheduleList in JSON
 --%>
<%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>

<div class="fl-widget portlet CoursesPortlet isMobile <c:if test="${isStandaloneSchedule}">isStandaloneSchedule</c:if>" role="section">
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

    <!-- Portlet Titlebar -->
    <c:choose>
      <c:when test="${isStandaloneSchedule}">
        <%@ include file="/WEB-INF/jsp/fragments/termSelect.jsp"%>
      </c:when>
      <c:otherwise>
        <div class="ui-grid-a">
          <div class="ui-block-a">
            <%@ include file="/WEB-INF/jsp/fragments/termSelect.jsp"%>
          </div>

          <c:if test="${! isStandaloneSchedule}">
            <div class="ui-block-b">
              <div data-role="navbar" data-theme="c" class="list-sched-toggle">
                <ul>
                  <li><a href="${courseListUrl}" class="ui-corner-left ui-shadow">List</a></li>
                  <li><a href="javascript:;" class="toggled ui-corner-right ui-shadow">Schedule</a></li>
                </ul>
              </div>
            </div>
          </c:if>
        </div>
      </c:otherwise>
    </c:choose>

    <div data-role="navbar" data-theme="c" class="list-sched-nav">
      <ul>
        <li><a href="javascript:;" class="toggled ui-corner-left ui-shadow"><spring:message code="schedulegrid"/></a></li>
        <li><a href="${examGridUrl}" class="ui-corner-right ui-shadow"><spring:message code="examsgrid"/></a></li>
      </ul>
    </div>

    <div class="portlet-section" role="region">
      <div class="content">
        <div id="${n}_grid"></div>
        <c:if test="${!empty otherCourses}">
          <div id="${n}_other">
            <ul data-role="listview" class="otherCourses">
              <li data-role="list-divider">
                Other Courses
              </li>
              <c:forEach var="course" items="${otherCourses}">
                <li>
                  <a href="${course.url}">
                    <h3>${course.courseDescr}&nbsp;${course.courseCode}-${course.meetingDescr}</h3>
                    <p>${course.courseTitle}</p>
                  </a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>
     </div>
  </div>
</div>

<portlet:renderURL var="selectTermUrl" escapeXml="false">
  <portlet:param name="action" value="showClassSchedule" />
  <portlet:param name="view" value="grid" />
</portlet:renderURL>

<script type="text/javascript">
  (function($){
    $(function(){
      coursesPortlet.createScheduleGrid(
        $('#${n}_grid'),
        ${classScheduleListJson},
        true,
        ${isStandaloneSchedule},
        {
            NoContentMsg: "<spring:message code="no.scheduled.courses"
                                           htmlEscape="false"
                                           javaScriptEscape="true"
                                           arguments="${portletSessionScope.helpDeskURL}" />",
        }
      );

      $('#${n}_other').on('click', 'tr', function(e){
        window.location = $(this).data('url');
      });

      $('#${n}_termPicker').on('change', function(e){
        window.location = '${selectTermUrl}&pP_termCode=' + $(this).val();
      });

      $('.portlet-title').on('click', function(){
        window.location = "${courseListUrl}";
      });
    });
  })(coursesPortlet.jQuery);
</script>
