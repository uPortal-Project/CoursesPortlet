
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
<c:choose>
	<c:when
		test="${(selectedTerm.registered==true) and  (fn:length(coursesByTerm.courses) > 0)}">
		<c:forEach items="${ coursesByTerm.courses }" var="course">
			<portlet:renderURL var="courseUrl">
				<portlet:param name="action" value="showCourse" />
				<portlet:param name="courseCode" value="${ course.code }" />
			</portlet:renderURL>
			<div>
				<div class="school_outer_width">
					<div class="fg_school">
						${ course.subject }
					</div>
					<div class="fg_course_code">
						${ course.code }
					</div>
				</div>
				<div class="fg_credits">${ course.credits } cr</div>
				<div class="fg_grade">
					${ course.grade }
				</div>
				<div class="fg_course_title">${ course.title }</div>
			</div>
			<br />
		</c:forEach>
	</c:when>
	<c:otherwise>
		<div>
			<spring:message code="no.courses.message" />
		</div>
	</c:otherwise>
</c:choose>