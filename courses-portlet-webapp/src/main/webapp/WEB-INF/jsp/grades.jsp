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

<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<c:set var="n"><portlet:namespace/></c:set>

<div class="fl-widget portlet" role="section">
  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    <h2 class="title" role="heading">
        <spring:message code="grades"/>
    </h2>
    <div class="toolbar">
        <ul>
          <portlet:renderURL var="scheduleUrl">
            <c:if test="${not empty currentTerm.code}">
              <portlet:param name="termCode" value="${currentTerm.code}"/>
            </c:if>
          </portlet:renderURL>
          <li><a class="button" href="${scheduleUrl}">
            <spring:message code="schedule"/>
          </a></li>
          <li><a class="button" href="#">
            <spring:message code="grades"/>
          </a></li>
          <portlet:actionURL var="selectTermUrl">
            <portlet:param name="action" value="grades" />
          </portlet:actionURL>
          <li><form action="${selectTermUrl}" method="post">
            <label for="${n}_termPicker"><spring:message code="term"/>:</label>
            <select id="${n}_termPicker" name="termCode" onchange="this.form.submit()">
              <c:forEach var="term" items="${termSummary.terms}">
                <c:set var="selected" value="" />
                <c:if test="${term.code == currentTerm.code}">
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
        <ul data-role="listview" class="course-list">
            <c:if test="${ fn:length(courseSummary.courses) == 0 }">
                <li><spring:message code="no.courses.message"/></li>
            </c:if>
            <c:forEach items="${ courseSummary.courses }" var="course">
                <portlet:renderURL var="courseUrl"><portlet:param name="action" value="showCourse"/><portlet:param name="courseCode" value="${ course.code }"/></portlet:renderURL>
                <li>
                    <h3 class="title">${ course.title }</h3>
                    <p>
                        <span class="catalog">${ course.code }</span>
                    </p>
                    <p>${ course.credits } cr</p>
                    <p>${ course.grade }</p>
                </li>
            </c:forEach>
            <li>
                <p>Term credits ${ courseSummary.credits } credits</p>
                <p>Term GPA ${ courseSummary.gpa }</p>
                 
                <p>Cum. credits ${ credits } credits</p>
                <p>Cum. GPA ${ gpa }</p>
            </li>
        </ul>            
    </div>
</div>