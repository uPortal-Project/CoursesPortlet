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
<%--
    Model Attributes:
        course          - Course
 --%>
<%@ include file="/WEB-INF/jsp/myuwcourses/fragments/includeLinks.jsp"%>

<div class="fl-widget portlet CoursesPortlet noMobile" role="section">

  <!-- Portlet Titlebar -->
  <!-- Course Title  -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/viewLinks.jsp"%>
      <h2 class="title" role="heading">
          ${ course.courseDepartment.name }&nbsp;${ course.code } &mdash; ${ selectedTerm.displayName }<br/>${ course.title }
      </h2>
  </div>
  <!-- end: portlet-titlebar -->

  <!-- Course Resources -->
  <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/courseresourceCourseDetail.jsp"%>     

  <!-- Portlet Content -->
  <!-- Course Detail -->


  <div class="portlet-section" role="region">
    <div class="content">
      <c:set var="hasExams" value="false"/>
      <ul class="section-list"><!--
        <c:forEach items="${ courseSectionMeetingList }" var="courseSectionMeeting">
          --><li class="section">
            <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseDetail.jsp"%>
          </li><!--
          <c:if test="${!empty courseSectionMeeting.locationExamMeetingMap}">
            <c:set var="hasExams" value="true"/>
          </c:if>
        </c:forEach>
        <c:if test="${hasExams}">
          --><li class="section exams">
            <h3 class="title" role="heading"><strong><spring:message code="final.exam"/></strong></h3>
            <c:forEach items="${ courseSectionMeetingList }" var="courseSectionMeeting">
              <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseExam.jsp"%>
            </c:forEach>
          </li><!--
        </c:if>
        --><li class="section">
          <h3 class="title" role="heading"><strong>FINAL GRADE</strong></h3>

          <div class="grade">${not empty course.grade?course.grade:'-'}</div>

          <p><a href="${ finalGradeUrl }">See all Final Grades</a></p>
        </li><!--
      <!-- --></ul>
    </div>
  </div>
</div>

