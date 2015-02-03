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
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="schoolsUrl"/>
<portlet:renderURL var="departmentsUrl">
    <portlet:param name="action" value="departments"/>
    <portlet:param name="schoolCode" value="${ school.code }"/>
</portlet:renderURL>
<portlet:renderURL var="coursesUrl">
    <portlet:param name="action" value="courses"/>
    <portlet:param name="schoolCode" value="${ school.code }"/>
    <portlet:param name="departmentCode" value="${ department.code }"/>
    <portlet:param name="termCode" value="${ term }"/>
</portlet:renderURL>

<div class="fl-widget portlet" role="section">

  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
      <div class="breadcrumb">
          <a href="${ schoolsUrl }"><spring:message code="schools"/></a>
          <span class="separator">&gt; </span>
          <a href="${ departmentsUrl }">${ school.name }</a>
          <span class="separator">&gt; </span>
          <a href="${ coursesUrl }">${ department.name }</a>
          <span class="separator">&gt; </span>
      </div>
    <h2 class="title" role="heading">${ course.code }: ${ course.title }</h2>
  </div>

    <!-- Portlet Content -->
    <div class="fl-widget-content content portlet-content" role="main">

        <h3><spring:message code="course.credits"/>: ${ course.credits }</h3>
        <div class="text">
            <p>${ course.prerequisites }</p>
            <p>${ course.description }</p>
        </div>

        <c:forEach items="${ course.courseSections }" var="section">
        
              <div class="portlet-section" role="region">
                <div class="titlebar">
                  <h3 class="title" role="heading">Section ${ section.code }</h3>
                </div>
                <div class="content">
        
                    <p><spring:message code="instructors"/>:
                        <ul>
                            <li>
                                <c:forEach items="${ section.instructors }" var="instructor" varStatus="status">
                                    <a href="${ instructorUrls[instructor.identifier] }">${ instructor.fullName }</a>
                                </c:forEach>
                            </li>
                        </ul>
                    </p>
                    <p><spring:message code="course.meetings"/>:
                        <ul>
                            <c:forEach items="${ section.courseMeetings }" var="meeting" varStatus="status">
                                <li>${ meeting.time } at
                                    <a href="${ locationUrls[meeting.location.identifier] }">
                                        ${ meeting.location.displayName }
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </p>
                </div>
            </div>
        </c:forEach>      

    </div>
</div>
