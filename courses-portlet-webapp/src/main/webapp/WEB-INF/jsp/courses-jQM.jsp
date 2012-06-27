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

<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="backUrl">
    <portlet:param name="action" value="departments"/>
    <portlet:param name="schoolCode" value="${ school.code }"/>
</portlet:renderURL>
<div data-role="header" class="titlebar portlet-titlebar">
    <a data-role="button"  data-icon="back" data-inline="true" href="${ backUrl }">Back</a>
    <h2>${ department.name }</h2>
</div>

<div class="fl-widget portlet" role="section">
    <div class="portlet-content" data-role="content">

        <ul data-role="listview">
            <c:forEach items="${ courses }" var="course">
                <portlet:renderURL var="courseUrl">
                    <portlet:param name="action" value="course"/>
                    <portlet:param name="courseCode" value="${ course.code }"/>
                    <portlet:param name="schoolCode" value="${ school.code }"/>
                    <portlet:param name="departmentCode" value="${ department.code }"/>
                </portlet:renderURL>
                <li><a href="${ courseUrl }">${ course.title }</a></li>
            </c:forEach>
        </ul>

        <div class="utilities" style="margin-top: 20px">
        
            <portlet:renderURL var="selectTermUrl"/>
            <div class="ui-block-c"><form action="${selectTermUrl}" method="post">
              <input type="hidden" name="action" value="courses"/>
              <input type="hidden" name="schoolCode" value="${ school.code }"/>
              <input type="hidden" name="departmentCode" value="${ department.code }"/>
              <label for="${n}_termPicker"><spring:message code="term"/>:</label>
              <select id="${n}_termPicker" name="termCode" onchange="this.form.submit()">
                <c:forEach var="t" items="${terms}">
                  <c:set var="selected" value="" />
                  <c:if test="${t.code == term}">
                      <c:set var="selected" value="selected=\"selected\"" />
                  </c:if>
                  <option value="${t.code}" ${selected}>${t.displayName}</option>
                </c:forEach>
              </select>
            </form></div>
            
        </div>
    
    </div>
</div>
