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

        <h2>${ course.title }: ${ section.code }</h2>
        
        <div class="class-details">
            <c:forEach items="${ section.instructors }" var="instructor">
                <a data-role="button" class="instructor" title="instructor" href="${ instructorUrls[instructor.identifier] }">${ instructor.fullName }</a>
            </c:forEach>
            <c:forEach items="${ section.courseMeetings }" var="meeting">
                <a data-role="button" class="location" title="location" href="${ locationUrls[meeting.location.identifier] }">${ meeting.time }, ${ meeting.location.displayName }</a>
            </c:forEach>
        </div>
        
    </div>
</div>
