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
    <portlet:param name="action" value="courses"/>
    <portlet:param name="schoolCode" value="${ school.code }"/>
    <portlet:param name="departmentCode" value="${ department.code }"/>
</portlet:renderURL>
<div data-role="header" class="titlebar portlet-titlebar">
    <a data-role="button"  data-icon="back" data-inline="true" href="${ backUrl }">Back</a>
    <h2>Course Detail</h2>
</div>


<div class="fl-widget portlet" role="section">
    <div class="portlet-content" data-role="content">

        <h2>${ course.title }</h2>
        <p>${ course.description }</p>
        <p>${ course.credits } credits</p>

        <ul data-role="listview" data-inline="true">
            <c:forEach items="${ course.courseSections }" var="section">
                <li><p>
                    ${ section.meetingTimes }<br/>
                    <c:forEach items="${ section.instructors }" var="instructor" varStatus="status">
                        ${ instructor.abbreviation }${ status.last ? '' : ', ' }
                    </c:forEach>                
                </p></li>
            </c:forEach>
        </ul>        
    
    </div>
</div>
