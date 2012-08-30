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
        coursesByTerm   - CoursesByTerm
        instructorUrls  - Map<String, String>
        locationUrl     - Location
        course          - Course
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
          <c:if test="${not empty course.url}"><a href="${ course.url }" target="_new"></c:if>
          ${ course.code }: ${ course.title }
          <c:if test="${not empty course.url}"></a></c:if>
      </h2>
  </div> <!-- end: portlet-titlebar -->
  
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

         <div class="portlet-section" role="region">
            <div class="content">

                <p><c:forEach items="${ course.instructors }" var="instructor">
                    <a data-role="button" class="instructor" title="instructor" href="${ instructorUrls[instructor.identifier] }">${ instructor.fullName }</a>
                </c:forEach></p>
                <p><c:if test="${ not empty course.meetingTimes }">
                    <a data-role="button" class="schedule" title="schedule" href="javascript:;">${ course.meetingTimes }</a>
                </c:if></p>
                <p><c:if test="${ not empty course.location }">
                    <a data-role="button" class="location" title="location" href="${ locationUrl }">${ course.location.displayName }</a>
                </c:if></p>
            </div>
         </div>

         <div class="portlet-section" role="region">
            <div class="titlebar">
                <h3 class="title" role="heading"><spring:message code="updates"/></h3>
            </div>
            <div class="content">
         
                    <div>
                        <c:choose>
                            <c:when test="${ fn:length(course.courseUpdates) == 0 }">
                                <p class="no-data"><spring:message code="no.updates"/></p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${ course.courseUpdates }" var="update">
                                    <h4 class="title"><a href="${ update.url }">${ update.title }</a></h4>
                                    <p class="body">${ update.description }</p>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
            </div>

    </div>
</div>
