
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
<div class="fl-widget portlet CoursesPortlet" role="section">
  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    <h2 class="title" role="heading">
        <spring:message code="courses"/>
    </h2>
    <div class="toolbar">
        <ul>
          <li><a class="button selected" href="#">
            <spring:message code="schedule"/>
          </a></li>
          <portlet:renderURL var="gradesUrl">
            <portlet:param name="action" value="grades"/>
            <c:if test="${not empty selectedTerm.code}">
              <portlet:param name="termCode" value="${selectedTerm.code}"/>
            </c:if>
          </portlet:renderURL>
          <li><a class="button" href="${ gradesUrl }">
            <spring:message code="grades"/>
          </a></li>
          <portlet:actionURL var="selectTermUrl">
            <portlet:param name="action" value="courseList"/>
          </portlet:actionURL>
          <li><form action="${selectTermUrl}" method="post">
            <label for="${n}_termPicker"><spring:message code="term"/>:</label>
            <select id="${n}_termPicker" name="termCode" onchange="this.form.submit()">
              <c:forEach var="term" items="${termList.terms}">
                <c:set var="selected" value="" />
                <c:if test="${term.code == selectedTerm.code}">
                    <c:set var="selected" value="selected=\"selected\"" />
                </c:if>
                <option value="${term.code}" ${selected}>${term.displayName}</option>
              </c:forEach>
            </select>
          </form></li>
        </ul>
    </div>
  </div> <!-- end: portlet-titlebar -->
  
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">
        
        <c:choose>
            <c:when test="${ fn:length(coursesByTerm.courses) == 0 }">
                <p><spring:message code="no.courses.message"/></p>
            </c:when>
            <c:otherwise>
              <table class="schedule">
                <tr>
                  <th><spring:message code="course"/></th>
                  <th><spring:message code="instructors"/></th>
                  <th><spring:message code="time"/></th>
                  <th><spring:message code="start.end"/></th>
                  <th><spring:message code="days"/></th>
                  <th><spring:message code="location"/></th>
                </tr>
                <c:forEach items="${ coursesByTerm.courses }" var="course">
                    <portlet:renderURL var="courseUrl">
                        <portlet:param name="action" value="showCourse"/>
                        <portlet:param name="termCode" value="${ coursesByTerm.termCode }"/>
                        <portlet:param name="courseCode" value="${ course.code }"/>
                    </portlet:renderURL>
                    <tr>
                      <td>
                        <h3>
                          <a href="${ courseUrl }">${ course.code } <c:if test="${ course.newUpdateCount > 0 }">(${ course.newUpdateCount })</c:if></a>
                        </h3>
                        ${ course.title }
                      </td>
                      <td>
                        <c:forEach items="${ course.instructors }" var="instructor">
                          ${ instructor.abbreviation }<br/>
                        </c:forEach>
                      </td>
                      <td>
                        <c:forEach items="${ course.courseMeetings }" var="meeting">
                          ${ meeting.formattedMeetingTime }<br/>
                        </c:forEach>
                      </td>
                      <td>
                        <c:forEach items="${ course.courseMeetings }" var="meeting">
                          <c:if test="${not empty meeting.startDate && not empty meeting.endDate}"><joda:format value="${meeting.startDate}" style="S-"/> - <joda:format value="${meeting.endDate}" style="S-"/><br/></c:if>
                        </c:forEach>
                      </td>
                      <td>
                        <c:forEach items="${ course.courseMeetings }" var="meeting">
                          ${ meeting.formattedMeetingDays }<br/>
                        </c:forEach>
                      </td>
                      <td>
                        <c:forEach items="${ course.courseMeetings }" var="meeting">
                          ${ meeting.location.displayName }<br/>
                        </c:forEach>
                      </td>
                  </tr>
                </c:forEach>
              </table>
            </c:otherwise>
        </c:choose>
    
    </div>
</div>
