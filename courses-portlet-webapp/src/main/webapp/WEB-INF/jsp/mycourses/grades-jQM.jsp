
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

<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%--
    Model Attributes:
        termList        - TermList
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
 --%>
<div id="${n}" class="fl-widget portlet CoursesPortlet" role="section">
    <%-- header --%>
    <div data-role="header" class="titlebar portlet-titlebar">
        <div class="header_height">
            <span style="float: left;"><h2>
                    <spring:message code="grades" />
                </h2></span>
            <div style="padding-top: 15px; float: right;">
                <form method="post">
                    <div>
                        <span id="${n}_loading" style="display: none"><img src="${renderRequest.contextPath}/img/ajax-loader.gif" alt="Loading..." /></span>
                        <select id="${n}_termPicker" name="termCode">
                            <c:forEach var="term" items="${termList.terms}">
                                <c:set var="selected" value="" />
                                <c:if test="${term.code == selectedTerm.code}">
                                    <c:set var="selected" value="selected=\"selected\"" />
                                </c:if>
                                <option value="${term.code}" ${selected}>${term.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div id="${n}_error" style="display: none">
                        <span><img src="${renderRequest.contextPath}/img/error.png" alt="Error" /></span>
                        <span class="error_message"></span>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%-- grades --%>
    <div class="portlet ptl-courses view-courses">
        <div class="portlet-content" data-role="content">
            <div id="${n}_grades-course-list" data-role="listview" class="course-list">
                <%@ include file="/WEB-INF/jsp/fragments/gradesCourseList.jsp" %>
            </div>
        </div>
    </div>

    <%-- footer --%>
    <div data-theme="a" data-role="footer" data-position="fixed">
        <div id="${n}_grades-footer" class="footer_top_padding footer_bottom_padding">
            <%@ include file="/WEB-INF/jsp/fragments/gradesFooter.jsp" %>
        </div>
    </div>
</div>

<portlet:resourceURL var="gradesCourseListUrl" id="gradesUpdate" />
<spring:message var="errorMessage" code="grades.unavailable" htmlEscape="false" javaScriptEscape="false" />
<script type="text/javascript" language="javascript">
<rs:compressJs>
(function($) {
    $(function() {
        coursesPortlet.updateGradesTermHandler({
          termSelector: '#${n}_termPicker',
          coursesContentSelector: '#${n}_grades-course-list',
          footerContentSelector: '#${n}_grades-footer',
          loadingSelector: '#${n}_loading',
          errorSelector: '#${n}_error',
          errorMessage: '${errorMessage}',
          dataUrl: '${gradesCourseListUrl}'
        });
    });    
})(coursesPortlet.jQuery);
</rs:compressJs>
</script>