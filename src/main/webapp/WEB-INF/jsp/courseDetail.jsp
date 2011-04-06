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
<portlet:renderURL var="courseListUrl"/>

<div data-role="header" data-theme="b" class="titlebar portlet-titlebar courses-back-div">
    <a data-role="button" data-icon="back" data-inline="true" class="courses-back-link" href="${ courseListUrl }">Back</a>
    <h2 class="title course-catalog-name">${ course.code }</h2>
</div>
<div class="course-details">
    <div class="titlebar">
        <h2 class="title">${ course.title }</h2>
        <h3 class="subtitle">${ course.school }</h3>
        <div class="grade"><span>${ course.grade }</span></div>
    </div>
    <div>
        <div class="class-details">
            <a data-role="button" class="instructor" title="instructor" href="javascript:;">${ course.instructors[0].fullName }</a>
            <a data-role="button" class="schedule" title="schedule" href="javascript:;">${ course.meetingTimes }</a>
            <a data-role="button" class="location" title="location" href="javascript:;">${ course.location.displayName }</a>
        </div>
        <div class="class-announcements" style="margin-top: 30px;">
            <div>
                <c:choose>
                    <c:when test="${ fn:length(course.announcements) == 0 }">
                        <p class="no-data">No announcements</p>
                    </c:when>
                    <c:otherwise>
                        <ul data-role="listview">
                            <li data-role="list-divider">Announcements</li>
                            <c:forEach items="${ course.announcements }" var="announcement">
                                <li>
                                    <h4 class="title">${ announcement.title }</h4>
                                    <p class="body">${ announcement.description }</p>
                                </li>
                            </c:forEach>
                        </ul>   
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
