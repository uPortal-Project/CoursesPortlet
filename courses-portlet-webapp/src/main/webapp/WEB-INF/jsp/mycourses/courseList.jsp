
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

<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%--
    Model Attributes:
        termList        - TermList
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
 --%>
<div class="courses-portlet container-fluid" role="section">
    <!-- Portlet Titlebar -->
    <div class="courses-portlet-titlebar row" role="sectionhead">
        <div class="col-md-12 no-col-padding">
            <form action="${selectTermUrl}" class="form-inline pull-right" method="post">
                <a href="#">
                    <i class="fa fa-calendar-o"></i>&nbsp;<spring:message code="schedule"/>
                </a> |
                <portlet:renderURL var="gradesUrl">
                    <portlet:param name="action" value="grades"/>
                    <c:if test="${not empty selectedTerm.code}">
                        <portlet:param name="termCode" value="${selectedTerm.code}"/>
                    </c:if>
                </portlet:renderURL>
                <a href="${ gradesUrl }">
                    <i class="fa fa-book"></i>&nbsp;<spring:message code="grades"/>
                </a> |
                <portlet:actionURL var="selectTermUrl">
                    <portlet:param name="action" value="courseList"/>
                </portlet:actionURL>
                <div class="form-group">
                    <label for="${n}_termPicker"><spring:message code="term"/>:</label>
                    <select id="${n}_termPicker" name="termCode" class="form-control" onchange="this.form.submit()">
                        <c:forEach var="term" items="${termList.terms}">
                            <c:set var="selected" value="" />
                            <c:if test="${term.code == selectedTerm.code}">
                                <c:set var="selected" value="selected=\"selected\"" />
                            </c:if>
                            <option value="${term.code}" ${selected}>${term.displayName}</option>
                        </c:forEach>
                    </select>
                </div>
            </form>
        </div>
    </div>

    <!-- Portlet Content -->
    <div class="portlet-content row" role="main">
        <div class="col-sm-12">
            <c:choose>
                <c:when test="${ fn:length(coursesByTerm.courses) == 0 }">
                    <p><spring:message code="no.courses.message"/></p>
                </c:when>
                <c:otherwise>
                    <table class="schedule table table-condensed table-striped table-responsive">
                        <tr>
                            <th><spring:message code="course"/></th>
                            <th><spring:message code="instructors"/></th>
                            <th><spring:message code="days"/></th>
                            <th><spring:message code="time"/></th>
                            <th><spring:message code="location"/></th>
                            <th><spring:message code="start.end"/></th>
                        </tr>
                        <c:forEach items="${ coursesByTerm.courses }" var="course">
                            <portlet:renderURL var="courseUrl">
                                <portlet:param name="action" value="showCourse"/>
                                <portlet:param name="termCode" value="${ coursesByTerm.termCode }"/>
                                <portlet:param name="courseCode" value="${ course.code }"/>
                            </portlet:renderURL>
                            <tr>
                                <td>
                                    <h3><a href="${ courseUrl }">${ course.code } <c:if test="${ course.newUpdateCount > 0 }">(${ course.newUpdateCount })</c:if></a></h3>
                                    <strong>${ course.title }</strong>
                                </td>
                                <td>
                                    <c:forEach items="${ course.instructors }" var="instructor">
                                        ${ instructor.abbreviation }<br/>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${ course.courseMeetings }" var="meeting">
                                        ${ meeting.formattedMeetingDays }<br/>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${ course.courseMeetings }" var="meeting">
                                        ${ meeting.formattedMeetingTime }<br/>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${ course.courseMeetings }" var="meeting">
                                        ${ meeting.location.displayName }<br/>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach items="${ course.courseMeetings }" var="meeting">
                                        <c:if test="${not empty meeting.startDate && not empty meeting.endDate}">
                                            <fmt:formatDate value="${meeting.startDate.time}" type="date" dateStyle="SHORT"/> - <fmt:formatDate value="${meeting.endDate.time}" type="date" dateStyle="SHORT"/><br/>
                                        </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div> <!-- end: portlet-content -->
</div> <!-- end: CoursesPortlet -->
