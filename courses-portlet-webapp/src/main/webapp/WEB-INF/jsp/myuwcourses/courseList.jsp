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
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<%--
    Model Attributes:
        termList        - TermList
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
        errorMessageList -List<String>
 --%>
<div class="fl-widget portlet CoursesPortlet noMobile" role="section">
  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    <c:set var="activeNav" value="courseList" />
    <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/viewLinks.jsp"%>
    <h2 class="title" role="heading">
        <spring:message code="courses"/>
    </h2>
    <div>
        <ul>
          <portlet:renderURL var="gradesUrl">
            <portlet:param name="action" value="grades"/>
            <c:if test="${not empty selectedTerm.code}">
              <portlet:param name="termCode" value="${selectedTerm.code}"/>
            </c:if>
          </portlet:renderURL>
          <portlet:actionURL var="selectTermUrl">
            <portlet:param name="action" value="courseList"/>
          </portlet:actionURL>
          <li>
            <%@ include file="/WEB-INF/jsp/fragments/termSelect.jsp"%>
          </li>
        </ul>
    </div>
  </div> <!-- end: portlet-titlebar -->
  <!-- List of Courses -->
  <div class="fl-widget-content content portlet-content" role="main">
				<%@ include file="/WEB-INF/jsp/myuwcourses/fragments/coursesCourseList.jsp"%>
    </div>
</div>


<script type="text/javascript">
  (function($){
    $('#${n}_termPicker').on('change', function(e){
      window.location = '${selectTermUrl}&pP_termCode=' + $(this).val();
    });
  })(coursesPortlet.jQuery);
</script>
