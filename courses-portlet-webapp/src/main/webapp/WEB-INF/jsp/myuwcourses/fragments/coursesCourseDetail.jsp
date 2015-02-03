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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%--
    Model Attributes:
        courseSection   - CourseSection
 --%>

<h3 class="title" role="heading"><strong>${ courseSectionMeeting.courseSection.type }&nbsp;${ courseSectionMeeting.courseSection.code }</strong></h3>
<c:set var="classMeetingMap" value="${ courseSectionMeeting.locationClassMeetingMap}"/>

<!-- Section Resources -->
<div class="content">
  <div>
    <c:choose>
      <c:when test="${ (fn:length(courseSectionMeeting.courseSection.resources) ==0) }"></c:when>
      <c:otherwise>
        <div class="resources">
          <c:set var="resourceERROR" value="${ courseSectionMeeting.courseSection.resources[0].type}" />
          <c:choose>
            <c:when test="${ (resourceERROR=='ERROR') }">
              <spring:message code="section.resource.notavailable"/>
            </c:when>
            <c:otherwise>
              <c:forEach items="${ courseSectionMeeting.courseSection.resources }" var="resource">
                <c:if test="${not empty resource.url}"><a href="${ resource.url }" target="_blank"><button></c:if>
                  ${ resource.title }
                <c:if test="${not empty resource.url}"></button></a></c:if>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</div>

            <!-- Course Meeting CLASS -->
            <div class="titlebar">
              <h4 class="title" role="heading"><spring:message code="course.meetings"/></h4>
            </div>
             <c:choose>
                 <c:when test="${empty classMeetingMap}">
                    <p class="no-data"><spring:message code="no.meetings"/></p>
                 </c:when>
               <c:otherwise>
                 <c:forEach items="${classMeetingMap}" var="entry">
                   <p>
                   <c:forEach items="${ courseSectionMeeting.courseSection.sectionAdditionalInfos }" var="additionalInfo">
                       <c:choose>
                         <c:when test="${'sessionStartEndDate' == additionalInfo.key}">
                           ${ additionalInfo.value }<br/>
                         </c:when>
                       </c:choose>
                   </c:forEach>
                     <c:set var="location" value="${entry.key}" />
                     <c:set var="courseMeetingList" value="${entry.value }"/>
                     <c:forEach items="${courseMeetingList}" var="meeting">
                       <c:if test="${not empty meeting.dayIds}">
                         ${ meeting.formattedMeetingTime }&nbsp;${ meeting.dayIds }<br/>
                       </c:if>
                     </c:forEach>
                     <c:choose>
                       <c:when test="${not empty location.url}">
                         Address:<br/>
                         <a href="${location.url}"
                           <c:if test="${courseSectionMeeting.mobile}">target="_blank"</c:if>
                           >${ location.room }&nbsp;${ location.displayName }<br/>
                         ${location.streetAddress}</a></br>
                       </c:when>
                       <c:otherwise>
                         Address:<br/>${ location.displayName }<br/>
                       </c:otherwise>
                     </c:choose>
                   </p>
                 </c:forEach>
               </c:otherwise>
           </c:choose>

               <!-- Instructor Resources -->
               <c:choose>
                  <c:when test="${ fn:length(courseSectionMeeting.courseSection.instructors) == 0 }">
                       <p class="no-data"><spring:message code="no.instructors"/></p>
                  </c:when>
                  <c:otherwise>
                   <div class="titlebar">
                     <h4 class="title" role="heading"><spring:message code="instructors"/></h4>
                   </div>
                   <c:forEach items="${ courseSectionMeeting.courseSection.instructors }" var="instructor">
                     <p>
                       <c:choose>
                         <c:when test="${not empty instructorUrls[instructor.identifier]}">
                           <a class="instructor" title="instructor" href="${ instructorUrls[instructor.identifier] }" target="_blank"
                             >${ instructor.fullName }</a>
                         </c:when>
                         <c:otherwise>
                           ${ instructor.fullName }
                         </c:otherwise>
                       </c:choose><br/>
                       <c:forEach items="${ instructor.instructorAdditionalInfos }" var="additionalInfo">
                         <c:if test="${not empty additionalInfo.key && 'pvi' != additionalInfo.key}">
                           <c:choose>
                             <c:when test="${'ERROR' == additionalInfo.key}">
                               <spring:message code="instructor.resource.notavailable"/><br/>
                             </c:when>
                             <c:when test="${'Web Page' == additionalInfo.key}">
                               <a class="instructor" title="Website" href="${ additionalInfo.value }" target="_blank">
                                 Website</a><br/>
                             </c:when>
                             <c:when test="${'Office Hours' == additionalInfo.key}">
                               ${ additionalInfo.key }:&nbsp;${ additionalInfo.value }<br/>
                             </c:when>
                           </c:choose>
                         </c:if>
                       </c:forEach>
                     </p>
                   </c:forEach>
                 </c:otherwise>
             </c:choose>