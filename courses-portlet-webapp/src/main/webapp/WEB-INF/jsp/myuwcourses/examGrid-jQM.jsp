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
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ page import="org.joda.time.LocalDate" %>
<%--
    Model Attributes:
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
        courseList      - List<Course>
        finalExamsList  - List<CourseSectionWrapper>
 --%>
<%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>

<div data-role="header" class="titlebar portlet-titlebar courses-back-div">
  <h2 class="title course-catalog-name">Final Exam Schedule</h2>
</div>
<div class="portlet CoursesPortlet FinalExams isMobile <c:if test="${isStandaloneSchedule}">isStandaloneSchedule</c:if>" role="section">
  <!-- Portlet Content -->
  <div class="content portlet-content" role="main">
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
          <div class="ui-block-b">
            <a href="${courseListUrl}" data-role="button">Course List</a>
          </div>
        </div>
      </c:otherwise>
    </c:choose>

    <div data-role="navbar" data-theme="c" class="list-sched-nav">
      <ul>
        <li><a href="${classScheduleGridUrl}" class="ui-corner-left ui-shadow"><spring:message code="schedulegrid"/></a></li>
        <li><a href="javascript:;" class="toggled ui-corner-right ui-shadow"><spring:message code="examsgrid"/></a></li>
      </ul>
    </div>

    <c:if test="${!empty finalExamsList}">
      <div class="monthDescr">${startMonthDescr}</div>
    </c:if>
    <div id="${n}_exam_grid"></div>
  </div>
</div>

<portlet:renderURL var="selectTermUrl" escapeXml="false">
  <portlet:param name="action" value="showExams" />
  <portlet:param name="view" value="grid" />
</portlet:renderURL>

<script type="text/javascript">
  (function($){
    $(function(){
      var $grid = $('#${n}_exam_grid'),
          startDate;
      coursesPortlet.createScheduleGrid(
        $grid,
        ${finalExamsListJson},
        true,
        ${isStandaloneSchedule},
        {
          nowLine: false,
          NoContentMsg: "<spring:message code="no.scheduled.exams"
                                         htmlEscape="false"
                                         javaScriptEscape="true"
                                         arguments="${portletSessionScope.helpDeskURL}" />",
          ActivityOptions: {
            content: function() {
              this.setColorForSession();
              return '<div role="link"><div class="tt-activityTitle">' +
                this.courseDescr + " " + this.courseCode + '</div>' +
                "<div>" + this.meetingDescr + "&nbsp;" + "(" +this.sessionCode + ")" + "</div>" +
                "<div>" + this.room + "&nbsp;" + this.locationDisplayName + "</div>" +
                '<div>' + this.timeRangeDisplay + '<br/>' +
                this.startDateDisplay + '</div></div>';
            }
          }
        }
      );

      startDate = ${startDateSunday};

      if(startDate){
        $grid.find('.tt-dayTitle').each(function(){
          $(this).append('<div>' + startDate++ + '</div>');
        });
      }

      $('#${n}_termPicker').on('change', function(e){
        window.location = '${selectTermUrl}&pP_termCode=' + $(this).val();
      });

      $('.portlet-title').on('click', function(){
        window.location = "${courseListUrl}";
      });
    });
  })(coursesPortlet.jQuery);
</script>
