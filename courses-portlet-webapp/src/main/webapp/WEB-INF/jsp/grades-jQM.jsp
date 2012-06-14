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

<style type="text/css">
.up .ptl-courses .grade {
position: absolute;
top: 10px;
right: 10px;
padding: .1em 10px;
background: #BA3;
color: white;
border: 1px solid #938628;
font-size: 150%;
line-height: 150%;
font-weight: bold;
text-shadow: 0 -1px 1px #433D12;
-moz-border-radius: 5px;
-webkit-border-radius: 5px;
border-radius: 5px;
}
.ptl-courses td,
.ptl-courses th {
    text-align: left;
    padding: 3px 7px;
}
</style>

<div class="portlet ptl-courses view-grades">
    <div class="portlet-content" data-role="content">

        <portlet:actionURL var="selectTermUrl">
          <portlet:param name="action" value="grades" />
        </portlet:actionURL>
        <div style="margin-bottom:20px"><form action="${selectTermUrl}" method="post">
          <select id="${n}_termPicker" name="termCode" onchange="this.form.submit()">
            <c:forEach var="term" items="${termSummary.terms}">
              <c:set var="selected" value="" />
              <c:if test="${term.code == currentTerm.code}">
                  <c:set var="selected" value="selected=\"selected\"" />
              </c:if>
              <option value="${term.code}" ${selected}>${term.displayName}</option>
            </c:forEach>
          </select>
        </form></div>        

        <ul data-role="listview" class="course-list">
            <c:if test="${ fn:length(courseSummary.courses) == 0 }">
                <li><spring:message code="no.courses.message"/></li>
            </c:if>
            <c:forEach items="${ courseSummary.courses }" var="course">
                <portlet:renderURL var="courseUrl"><portlet:param name="action" value="showCourse"/><portlet:param name="courseCode" value="${ course.code }"/></portlet:renderURL>
                <li>
                    <h3 class="title">${ course.title }</h3>
                    <p>
                        <span class="catalog">${ course.code }</span>, ${ course.credits } cr
                    </p>
                    <div class="grade"><span>${ course.grade }</span></div>
                </li>
            </c:forEach>
        </ul>            
        
    <div class="ui-grid-a" style="margin-top: 20px">
        <div class="ui-block-a">
            <table>
                <tr><th>Term credits</th><td>${ courseSummary.credits } credits</td></tr>
                <tr><th>Term GPA</th><td>${ courseSummary.gpa }</td></tr>
            </table>
        </div>
        
        <div class="ui-block-b">
            <table>
                <tr><th>Cum. credits</th><td>${ credits } 80 credits</td></tr>
                <tr><th>Cum. GPA</th><td>${ gpa } 3.7</td></tr>
            </table>
        </div>
        
    </div>
    
    <div class="ui-grid-a utilities">
        <portlet:renderURL var="scheduleUrl">
            <c:if test="${not empty currentTerm.code}">
              <portlet:param name="termCode" value="${currentTerm.code}"/>
            </c:if>
        </portlet:renderURL>
        <div class="ui-block-a">
            <a data-role="button" class="schedule" title="schedule" href="${scheduleUrl }">
                <spring:message code="schedule"/>
            </a>
        </div>
        
        <div class="ui-block-b">
            <a data-role="button" class="grades" title="grades" href="<portlet:renderURL/>">
                <spring:message code="courses"/>
            </a>
        </div>
        
    </div>

    </div>
    
</div>
