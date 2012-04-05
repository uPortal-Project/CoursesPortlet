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
<portlet:renderURL var="formUrl">
    <portlet:param name="action" value="grades"/>
</portlet:renderURL>

<div id="${n}">
    <div data-role="header" class="titlebar portlet-titlebar">
        <h2>Grades</h2>
        <form action="${ formUrl }" method="POST">
            <select name="termCode">
                <c:forEach items="${ terms }" var="term">
                    <option value="${ term.code }" ${ term.code == selectedTerm.code ? 'selected' : '' }>${ term.displayName }</option>
                </c:forEach>
            </select>
        </form>
    </div>
    <div class="portlet ptl-courses view-courses">
        <div class="portlet-content" data-role="content">
            
            <ul data-role="listview" class="course-list">
                <c:if test="${ fn:length(selectedTerm.courses) == 0 }">
                    <li><spring:message code="no.courses.message"/></li>
                </c:if>
                <c:forEach items="${ selectedTerm.courses }" var="course">
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
                    <p>Term credits ${ selectedTerm.credits } credits</p>
                    <p>Term GPA ${ selectedTerm.gpa }</p>
                     
                    <p>Cum. credits ${ credits } credits</p>
                    <p>Cum. GPA ${ gpa }</p>
                </li>
            </ul>            
            
        </div>
    </div>
</div>

<script type="text/javascript">
    up.jQuery(document).ready(function () {
        var $ = up.jQuery;
        $("#${n} select").change(function () {
            $("#${n} form").submit();
        });
    });
</script>