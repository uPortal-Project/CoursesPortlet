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
    <portlet:param name="action" value="course"/>
    <portlet:param name="schoolCode" value="${ school.code }"/>
    <portlet:param name="departmentCode" value="${ department.code }"/>
    <portlet:param name="courseCode" value="${ course.code }"/>
</portlet:renderURL>
<div data-role="header" class="titlebar portlet-titlebar">
    <a data-role="button"  data-icon="back" data-inline="true" href="${ backUrl }">Back</a>
    <h2>Section Detail</h2>
</div>


<div class="fl-widget portlet" role="section">
    <div class="portlet-content" data-role="content">

        <div class="titlebar">
            <h2 class="title">${ course.title }</h2>
            <h3 class="subtitle">Section ${ section.code }</h3>
        </div>
        
        <ul data-role="listview" data-inset="true" class="class-details">
            <li style="font-weight: normal;">
                <c:forEach items="${ section.courseMeetings }" var="meeting">
                    ${ meeting.time }, ${ meeting.location.displayName }
                </c:forEach>
            </li>
            <li style="font-weight: normal; border: none;">
                <c:forEach items="${ section.instructors }" var="instructor">
                    <a class="instructor" title="instructor" href="${ instructorUrls[instructor.identifier] }">${ instructor.fullName }</a>
                </c:forEach>
            </li>
        </div>
    </div>
</div>
