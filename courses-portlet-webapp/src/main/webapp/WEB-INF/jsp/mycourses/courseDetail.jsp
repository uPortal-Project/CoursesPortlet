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

    <div class="courses-portlet container-fluid" role="section">

    <!-- Portlet Titlebar -->
        <div class="courses-portlet-titlebar row" role="sectionhead">
            <div class="col-md-6 no-col-padding">
                <h2 role="heading">
                    <c:if test="${not empty course.url}">
                        <a href="${ course.url }" target="_new">
                    </c:if>
                    ${ course.code }: ${ course.title }
                    <c:if test="${not empty course.url}">
                        </a>
                    </c:if>
                </h2>
            </div>
            <div class="col-md-6 no-col-padding">
                <div class="pull-right">
                    <a href="${ courseListUrl }"><i class="fa fa-arrow-left"></i>&nbsp;<spring:message code="courses"/>
                    </a>
                </div>
            </div>
        </div> <!-- end: portlet-titlebar -->

        <!-- Portlet Content -->
        <div class="portlet-content" role="main">
            <div class="row">
                <div class="col-md-12" role="section">
                    <p>
                        <c:forEach items="${ course.instructors }" var="instructor">
                            <h3 role="title">
                                <c:if test="${not empty instructorUrls[instructor.identifier]}">
                                    <a data-role="button" title="instructor" href="${ instructorUrls[instructor.identifier] }"><i class="fa fa-user"></i>
                                </c:if>
                                ${ instructor.fullName }
                                <c:if test="${not empty instructorUrls[instructor.identifier]}">
                                    </h3></a>
                                </c:if>
                            </h3>
                        </c:forEach>
                    </p>
                    <p>
                        <c:forEach items="${ course.courseMeetings }" var="meeting">
                            <c:if test="${not empty locationUrls[meeting.location.identifier]}">
                                    <a data-role="button" class="location" href="${ locationUrls[meeting.location.identifier] }">
                            </c:if>
                            ${ meeting.formattedMeetingTime } ${ meeting.formattedMeetingDays } at ${ meeting.location.displayName }
                            <c:if test="${not empty locationUrls[meeting.location.identifier]}">
                                </a>
                            </c:if>
                            <c:if test="${not empty meeting.startDate && not empty meeting.endDate}">
                                <br/><fmt:formatDate value="${meeting.startDate.time}" type="date" dateStyle="MEDIUM"/> - <fmt:formatDate value="${meeting.endDate.time}" type="date" dateStyle="MEDIUM"/>
                            </c:if>
                        </c:forEach>
                    </p>
                </div>

                <c:if test="${displayCourseUpdates}">
                    <div class="col-md-12" role="sectionhead">
                        <h3 role="heading"><i class="fa fa-bell"></i>  <spring:message code="updates"/></h3>
                    </div>
                    <div class="col-md-12" role="section">
                        <c:choose>
                            <c:when test="${ fn:length(course.courseUpdates) == 0 }">
                                <p class="no-data"><spring:message code="no.updates"/></p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${ course.courseUpdates }" var="update">
                                    <h4 class="title">
                                        <c:if test="${not empty update.url}">
                                            <a href="${ update.url }">
                                        </c:if>
                                        ${ update.title }
                                        <c:if test="${not empty update.url}">
                                            </a>
                                        </c:if>
                                    </h4>
                                    <p class="body">${ update.description }</p>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <c:if test="${displayCourseBooks}">
                    <div class="col-md-12">
                        <h3 role="heading"><i class="fa fa-book"></i>   <spring:message code="textbooks"/></h3>
                    </div>
                    <div class="col-md-12" role="section">
                        <c:choose>
                            <c:when test="${ fn:length(course.courseTextbooks) == 0 }">
                                <p class="no-data"><spring:message code="no.textbooks"/></p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${ course.courseTextbooks }" var="textbook">
                                    <h4>
                                        <c:if test="${not empty textbook.url}">
                                            <a href="${ textbook.url }">
                                        </c:if>
                                            ${ textbook.title }
                                        <c:if test="${not empty textbook.url}">
                                            </a>
                                        </c:if>
                                    </h4>
                                    <p>${ textbook.author } - <spring:message code="isbn"/>: ${ textbook.isbn }</p>
                                    <p>${ textbook.comments }</p>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
