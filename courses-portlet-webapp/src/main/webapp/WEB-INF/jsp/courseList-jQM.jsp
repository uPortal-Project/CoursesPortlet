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

<div class="portlet ptl-courses view-courses">
    <div class="portlet-content" data-role="content">
        
        <c:choose>
            <c:when test="${ fn:length(courseList.courses) == 0 }">
                <p><spring:message code="no.courses.message"/></p>
            </c:when>
            <c:otherwise>
                <ul data-role="listview" class="course-list">
                    <c:forEach items="${ courseList.courses }" var="course">
                        <portlet:renderURL var="courseUrl"><portlet:param name="action" value="showCourse"/><portlet:param name="courseCode" value="${ course.code }"/></portlet:renderURL>
                        <li>
                            <a href="${ courseUrl }">
                                <h3 class="title">${ course.title }</h3>
                                <p>
                                    <span class="catalog">${ course.code }</span><span class="separator">, </span>
                                    <span class="instructor">${ course.instructors[0].abbreviation }</span>
                                </p>
                                <c:set var="newCount" value="${ course.newUpdateCount }"/>
                                <c:if test="${ newCount > 0 }">
                                    <span class="ui-li-count badge">${ newCount }</span>
                                </c:if>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
        
        <div class="ui-grid-a utilities" style="margin-top: 20px">
            <div class="ui-block-a"><a data-role="button" class="schedule" title="schedule" href="#"><spring:message code="schedule"/></a></div>
            <div class="ui-block-b"><a data-role="button" class="grades" title="grades" href="#"><spring:message code="grades"/></a></div>
        </div>
    
    </div>
</div>
