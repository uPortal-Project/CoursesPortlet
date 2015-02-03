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
<jsp:directive.include file="/WEB-INF/jsp/header.jsp"/>
<%--
    Model Attributes:
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
        courseList      - List<Course>
        finalExamsList  - List<CourseSectionWrapper>
 --%>
<portlet:renderURL var="courseListUrl">
    <portlet:param name="termCode" value="${coursesByTerm.termCode}"/>
</portlet:renderURL>


<div class="fl-widget portlet" role="section">

  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
      <div class="breadcrumb">
          <a class="menu-back-link" href="${ courseListUrl }">
            <spring:message code="courses"/>
          </a> &gt;
      </div>
      <h2 class="title" role="heading">
          Final Exam Schedule  - ${selectedTerm.displayName}
      </h2>
  </div> <!-- end: portlet-titlebar -->
  
  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

    <div class="portlet-section" role="region">
      <div class="content">
        <p>
          <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/examList.jsp"%>
        </p>
      </div>
     </div>
  </div>
</div>
