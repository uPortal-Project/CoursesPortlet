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
<%--
    Model Attributes:
        coursesByTerm   - CoursesByTerm
        course          - Course
 --%>
<%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>

<div data-role="header" class="titlebar portlet-titlebar courses-back-div">
    <a data-role="button" data-icon="back" data-inline="true" class="courses-back-link" href="javascript:void(0);"><spring:message code="back"/></a>
    <h2 class="title course-catalog-name">${ course.courseDepartment.name }&nbsp;${ course.code } &mdash; ${ selectedTerm.displayName }</h2>
</div>
<div class="portlet ptl-courses view-detail CoursesPortlet isMobile">
  <div class="portlet-content" data-role="content">
    <div class="course-details">
      <div class="detail-left">
        <h2>${ course.courseDepartment.name }&nbsp;${ course.code }</h2>
        <h4>${ course.title }</h4>
      </div>
      <div class="detail-right">
        <div class="grade"><div>${not empty course.grade?course.grade:'-'}</div></div>
        <a href="${ finalGradeUrl }"><button>See All Grades</button></a>
      </div>

      <!-- Course Resources -->
      <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/courseresourceCourseDetail.jsp"%>
      <!-- Course Detail -->
      <div class="portlet-section" role="region">
        <div class="content">
          <c:set var="hasExams" value="false"/>
          <div class="section-list">
            <c:forEach items="${ courseSectionMeetingList }" var="courseSectionMeeting">
              <div class="section">
                <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseDetail.jsp"%>
              </div>
              <c:if test="${!empty courseSectionMeeting.locationExamMeetingMap}">
                <c:set var="hasExams" value="true"/>
              </c:if>
            </c:forEach>
            <c:if test="${hasExams}">
              <div class="section exams">
                <h3 class="title" role="heading"><strong><spring:message code="final.exam"/></strong></h3>
                <c:forEach items="${ courseSectionMeetingList }" var="courseSectionMeeting">
                  <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseExam.jsp"%>
                </c:forEach>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
  (function($){
    $(function(){
      $('.courses-back-link').on('click', function(){
        window.history.back();
      });

      $('.portlet-title').on('click', function(){
        window.location = "${courseListUrl}";
      });
    });
  })(coursesPortlet.jQuery);
</script>
