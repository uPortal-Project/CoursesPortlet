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
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!-- Course Resources -->
  <c:choose>
        <c:when test="${ fn:length(courseSectionMeetingList) == 0 }">
        </c:when>
        <c:otherwise>
          <div class="course-resources">
            <c:set var="resourceERROR" value="${ course.courseResources[0].type}" />
              <c:choose>
                <c:when test="${ (resourceERROR=='ERROR') }">
                  <spring:message code="course.resource.notavailable"/>
                </c:when>
                <c:otherwise>
                  <c:forEach items="${ course.courseResources }" var="courseresource">
                    <c:if test="${not empty courseresource.url}">
                      <a href="${ courseresource.url }" target="_blank"><button>
                    </c:if>
                    ${ courseresource.title }
                    <c:if test="${not empty courseresource.url}"></button></a></c:if>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
          </div>
        </c:otherwise>
     </c:choose>
             