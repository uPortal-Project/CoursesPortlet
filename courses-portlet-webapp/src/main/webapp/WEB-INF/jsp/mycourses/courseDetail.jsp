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
      <h2 class="title" role="heading">${ course.code }: ${ course.title }</h2>
  </div> <!-- end: portlet-titlebar -->
  
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

     <div class="portlet-section" role="region">
        <div class="content">

            <p><c:forEach items="${ course.instructors }" var="instructor">
                <c:if test="${not empty instructorUrls[instructor.identifier]}"><a data-role="button" class="instructor" title="instructor" href="${ instructorUrls[instructor.identifier] }"></c:if>
                ${ instructor.fullName }
                <c:if test="${not empty instructorUrls[instructor.identifier]}"></a></c:if>
            </c:forEach></p>
            <p><c:forEach items="${ course.courseMeetings }" var="meeting">
                <c:if test="${not empty locationUrls[meeting.location.identifier]}"><a data-role="button" class="location" href="${ locationUrls[meeting.location.identifier] }"></c:if>
                ${ meeting.time } at ${ meeting.location.displayName }
                <c:if test="${not empty locationUrls[meeting.location.identifier]}"></a></c:if>
            </c:forEach></p>
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
                            <h4 class="title">
                                <c:if test="${not empty update.url}"><a href="${ update.url }"></c:if>
                                ${ update.title }
                                <c:if test="${not empty update.url}"></a></c:if></h4>
                            <p class="body">${ update.description }</p>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
                
        </div>
    </div>

     <div class="portlet-section" role="region">
        <div class="titlebar">
            <h3 class="title" role="heading"><spring:message code="textbooks"/></h3>
        </div>
        <div class="content">
     
            <div>
                <c:choose>
                    <c:when test="${ fn:length(course.courseTextbooks) == 0 }">
                        <p class="no-data"><spring:message code="no.textbooks"/></p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${ course.courseTextbooks }" var="textbook">
                            <h4 class="title">
                                <c:if test="${not empty textbook.url}"><a href="${ textbook.url }"></c:if>
                                ${ textbook.title }
                                <c:if test="${not empty textbook.url}"></a></c:if></h4>
                            <p class="body">${ textbook.author } - <spring:message code="isbn"/>: ${ textbook.isbn }</p>
                            <p class="body">${ textbook.comments }</p>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
                
        </div>
    </div>

</div>
