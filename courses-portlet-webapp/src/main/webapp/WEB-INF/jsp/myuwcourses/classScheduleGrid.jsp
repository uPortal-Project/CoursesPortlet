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
        coursesByTerm         - CoursesByTerm
        selectedTerm          - Term
        courseList            - List<Course>
        classScheduleList     - List<CourseSectionWrapper>
        otherCourses          - List<CourseSectionWrapper>
        classScheduleListJson - String of classScheduleList in JSON
 --%>

<portlet:renderURL var="printViewURL" windowState="detached" escapeXml="false">
  <portlet:param name="action" value="showClassSchedule"/>
  <portlet:param name="termCode" value="${coursesByTerm.termCode}"/>
  <portlet:param name="view" value="grid"/>
  <portlet:param name="printView" value="true"/>
</portlet:renderURL>

<div class="fl-widget portlet CoursesPortlet noMobile <c:if test="${isStandaloneSchedule}">isStandaloneSchedule</c:if>" role="section">

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
        <spring:message code="schedulegrid"/> - ${selectedTerm.displayName}
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

    <div class="portlet-section" role="region">
      <div class="grid-tabs">
        <a href="javascript:;"><spring:message code="schedulegrid"/></a>
        <a href="${examGridUrl}" class="toggled"><spring:message code="examsgrid"/></a>
      </div>
      <div class="content">
        <div id="${n}_grid"></div>
        <c:if test="${!empty otherCourses}">
          <ul class="course-list noMobile">
            <li><h2 class="title">Other Courses</h2></li>
            <c:forEach var="course" items="${otherCourses}">
              <li>
                <c:choose>
                  <c:when test="${!isStandaloneSchedule}">
                    <a href="${course.url}">
                      <span class="icon-mimic">&nbsp;</span>
                      <h3 class="title">${course.courseDescr}&nbsp;${course.courseCode}-${course.meetingDescr}</h3>
                      <p><span class="catalog">${course.courseTitle}</span></p>
                    </a>
                  </c:when>
                  <c:otherwise>
                    <div>
                      <h3 class="title">${course.courseDescr}&nbsp;${course.courseCode}-${course.meetingDescr}</h3>
                      <p><span class="catalog">${course.courseTitle}</span></p>
                    </div>
                  </c:otherwise>
                </c:choose>
              </li>
            </c:forEach>
          </ul>
        </c:if>
        <c:if test="${printView}">
          <div id="${n}_list" class="page">
            <br/>
            <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/classScheduleList.jsp"%>
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
      var $grid = $('#${n}_grid');
      var tt = coursesPortlet.createScheduleGrid(
        $grid,
        ${classScheduleListJson},
        false,
        ${isStandaloneSchedule},
        {
          NoContentMsg: "<spring:message code="no.scheduled.courses"
                                         htmlEscape="false"
                                         javaScriptEscape="true"
                                         arguments="${portletSessionScope.helpDeskURL}" />",
        }
      );

      $('#${n}_termPicker').on('change', function(e){
        window.location = '${selectTermUrl}&pP_termCode=' + $(this).val();
      });

      // Check if we're in a print view
      if( ${printView} ){
        (function(){
          var $title = $('.CoursesPortlet .title'),
              $list  = $('#${n}_list');
          $title.detach();
          $grid.detach();
          $list.detach();
          var $modal =
            $('<div class="CoursesPortlet noMobile gridPrint"></div>');
          $('body').append($modal);
          $modal.append($title)
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