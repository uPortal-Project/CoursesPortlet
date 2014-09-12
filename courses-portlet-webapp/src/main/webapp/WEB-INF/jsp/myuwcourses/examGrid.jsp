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
 --%>

<portlet:renderURL var="printViewURL" windowState="detached" escapeXml="false">
  <portlet:param name="action" value="showExams"/>
  <portlet:param name="termCode" value="${coursesByTerm.termCode}"/>
  <portlet:param name="view" value="grid"/>
  <portlet:param name="printView" value="true"/>
</portlet:renderURL>

<div class="fl-widget portlet CoursesPortlet FinalExams noMobile <c:if test="${isStandaloneSchedule}">isStandaloneSchedule</c:if>" role="section">

  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
      <c:choose>
        <c:when test="${isStandaloneSchedule}">
          <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>
        </c:when>
        <c:otherwise>
          <c:set var="activeNav" value="courseSchedule" />
          <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/viewLinks.jsp"%>
        </c:otherwise>
      </c:choose>
      <h2 class="title" role="heading">
          Final Exam Schedule  - ${selectedTerm.displayName}
      </h2>
      <div>
        <ul>
          <li class="grid-print-link">
            <a href="${printViewURL}" target="_blank"><button>Print</button></a>
          </li>
          <li>
            <%@ include file="/WEB-INF/jsp/fragments/termSelect.jsp"%>
          </li>
        </ul>
      </div>
  </div> <!-- end: portlet-titlebar -->

  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">
    <div class="grid-tabs">
      <a href="${classScheduleGridUrl}" class="toggled"><spring:message code="schedulegrid"/></a>
      <a href="javascript:;"><spring:message code="examsgrid"/></a>
    </div>
    <div id="${n}_exam_grid">
      <c:if test="${!empty finalExamsList}">
        <div class="monthDescr">${startMonthDescr}</div>
      </c:if>
    </div>
    <c:if test="${printView}">
      <div id="${n}_exam_list" class="page">
        <br/>
        <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/examList.jsp"%>
      </div>
    </c:if>
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
        false,
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

      // Check if we're in a print view
      if( ${printView} ){
        (function(){
          var $title = $('.CoursesPortlet .title'),
              $month = $('.CoursesPortlet .monthDescr'),
              $list  = $('#${n}_exam_list');
          $title.detach();
          $month.detach();
          $grid.detach();
          $list.detach();
          var $modal =
            $('<div class="CoursesPortlet FinalExams noMobile gridPrint"></div>');
          $('body').append($modal);
          $modal.append($title)
                .append($month)
                .append($grid)
                .append($list)
                .css({height: $(document).height()});
          $grid.trigger('tt.update');

          setTimeout(print, 500);
        })();
      }
    });
  })(coursesPortlet.jQuery);
</script>
