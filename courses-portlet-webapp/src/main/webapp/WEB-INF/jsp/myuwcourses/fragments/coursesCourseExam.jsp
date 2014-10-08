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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%--
    Model Attributes:
        courseSectionMeeting   - CourseSectionMeeting
--%>

<c:set var="examMeetingMap" value="${ courseSectionMeeting.locationExamMeetingMap }"/>
<!-- Course Meeting EXAM -->
<c:choose>
  <c:when test="${empty examMeetingMap}"></c:when>
  <c:otherwise>
    <p>
      <c:forEach items="${examMeetingMap}" var="entry">
        <c:set var="location" value="${entry.key}" />
        <c:set var="courseMeetingList" value="${entry.value }"/>
        <c:forEach items="${courseMeetingList}" var="meeting">
          <a href="${examGridUrl}">
            <c:if test="${not empty meeting.endDate}"><joda:format value="${meeting.endDate}" style="M-"/>
            </c:if>-${ meeting.formattedMeetingTime }<br/>
          </a>
        </c:forEach>
        Address:<br/>
        <c:choose>
          <c:when test="${empty location.url}">
            ${ location.displayName }
          </c:when>
          <c:otherwise>
            <a href="${location.url }" target="_blank">${ location.room }&nbsp;${ location.displayName }<br/>${location.streetAddress}</br></a>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </p>
  </c:otherwise>
</c:choose>

