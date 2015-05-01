
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

<portlet:renderURL var="scheduleUrl">
    <c:if test="${not empty selectedTerm.code}">
        <portlet:param name="termCode" value="${selectedTerm.code}"/>
    </c:if>
</portlet:renderURL>
<portlet:actionURL var="selectTermUrl">
    <portlet:param name="action" value="courseList"/>
</portlet:actionURL>

    <div class="courses-portlet container-fluid" role="section">
        <!-- Portlet Titlebar -->
        <div class="courses-portlet-titlebar row" role="sectionhead">
            <div class="col-md-12 no-col-padding">
                <form action="${selectTermUrl}" class="form-inline pull-right" method="post">
                    <a href="${ scheduleUrl }">
                        <i class="fa fa-calendar-o"></i>&nbsp;<spring:message code="schedule"/>
                    </a> |
                    <a href="#">
                        <i class="fa fa-book"></i>&nbsp;<spring:message code="grades"/>
                    </a> |
                    <div class="form-group">
                        <label for="${n}_termPicker"><spring:message code="term"/>:</label>
                        <select id="${n}_termPicker" name="termCode" class="form-control" onchange="this.form.submit()">
                        <c:forEach var="term" items="${termList.terms}">
                        <c:set var="selected" value="" />
                        <c:if test="${term.code == selectedTerm.code}">
                        <c:set var="selected" value="selected=\"selected\"" />
                        </c:if>
                        <option value="${term.code}" ${selected}>${term.displayName}</option>
                        </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <div id="${n}" class="portlet-content row" role="section">

            <%-- grades --%>
            <div class="col-md-6" data-role="content">
                <div id="${n}_grades-course-list" data-role="listview">
                    <%@ include file="/WEB-INF/jsp/fragments/gradesCourseList.jsp"%>
                </div>
            </div>

            <%-- footer --%>
            <div class="col-md-6" data-theme="a" data-role="content">
                <div id="${n}_grades-footer">
                    <%@ include file="/WEB-INF/jsp/fragments/gradesFooter.jsp"%>
                </div>
            </div>
        </div>
    </div> <!-- end: CoursesPortlet -->


<portlet:resourceURL var="gradesCourseListUrl" id="gradesUpdate" />
<spring:message var="errorMessage" code="grades.unavailable"
    htmlEscape="false" javaScriptEscape="false" />
<script type="text/javascript" language="javascript">
    <rs:compressJs>(function($) {
        $(function() {
            coursesPortlet.updateGradesTermHandler({
                termSelector : '#${n}_termPicker',
                coursesContentSelector : '#${n}_grades-course-list',
                footerContentSelector : '#${n}_grades-footer',
                loadingSelector : '#${n}_loading',
                errorSelector : '#${n}_error',
                errorMessage : '${errorMessage}',
                dataUrl : '${gradesCourseListUrl}'
            });
        });
    })(coursesPortlet.jQuery);
    </rs:compressJs>
</script>
