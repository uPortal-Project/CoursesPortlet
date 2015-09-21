<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

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
        <c:when test="${(selectedTerm.registered==true) and  (fn:length(coursesByTerm.courses) > 0)}">
            <c:forEach items="${ coursesByTerm.courses }" var="course">
                <portlet:renderURL var="courseUrl">
                    <portlet:param name="action" value="showCourse" />
                    <portlet:param name="courseCode" value="${ course.code }" />
                </portlet:renderURL>
                <div class="grades-display">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <table>
                                <tr>
                                    <td class="grade-title">GRADE</td>
                                    <td colspan=2 class="grade-sub-code">${ course.subject } ${ course.code }</td>
                                </tr>
                                <tr>
                                    <td class="grade">${ course.grade }</td>
                                    <td class="grade-course">${ course.title }</td>
                                    <td class="grade-credits">${ course.credits } cr</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-danger" role="alert">
                <p><i class="fa fa-exclamation-triangle"></i>&nbsp;&nbsp;<spring:message code="no.courses.message" /></p>
            </div> <!-- end: alert -->
        </c:otherwise>
    </c:choose>