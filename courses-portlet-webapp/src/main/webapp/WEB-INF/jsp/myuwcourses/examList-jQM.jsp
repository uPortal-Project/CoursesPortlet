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
 --%>
<portlet:renderURL var="courseListUrl">
    <portlet:param name="termCode" value="${coursesByTerm.termCode}"/>
</portlet:renderURL>


<div class="fl-widget portlet" role="section">

  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
      <div class="breadcrumb">
          <a class="menu-back-link" href="${ courseListUrl }">
            <spring:message code="courses"/>
          </a> &gt;
      </div>
      <h2 class="title" role="heading">
          Final Exam Schedule  - ${selectedTerm.displayName}
      </h2>
  </div> <!-- end: portlet-titlebar -->
  
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

    <div class="portlet-section" role="region">
        <div class="content">
            <p>
                <c:set var="startDate" value="" />
              <c:forEach items="${ finalExamsList }" var="finalExam">
                <c:if test="${finalExam.courseMeeting.startDate!=startDate}">
                  <strong><joda:format pattern="EEEEEEEEE MMMMMMMMM dd, yyyy" value="${finalExam.courseMeeting.startDate}"/></strong><br/>
                      <c:set var="startDate" value="${finalExam.courseMeeting.startDate}" />
                </c:if>      
                <joda:format pattern="HH:mm aa" value="${finalExam.courseMeeting.startTime}"/> &nbsp;${finalExam.course.courseDepartment.name}&nbsp;${finalExam.course.code}: &nbsp;${finalExam.course.title}<br/>
                <joda:format pattern="HH:mm aa" value="${finalExam.courseMeeting.endTime}"/>&nbsp;${finalExam.courseSection.type}&nbsp;${finalExam.courseSection.code} &nbsp;&nbsp;&nbsp;<a data-role="button" class="location" href="${ locationUrls[finalExam.courseMeeting.location.identifier] }">${finalExam.courseMeeting.location.displayName}</a><br/><br/>
            </c:forEach></p>
        </div>
     </div>
  </div>
</div>
