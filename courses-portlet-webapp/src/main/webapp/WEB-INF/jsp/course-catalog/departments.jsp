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
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:renderURL var="backUrl">
    <portlet:param name="action" value="schools"/>
</portlet:renderURL>

<div class="fl-widget portlet" role="section">

  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
      <div class="breadcrumb">
        <a href="${ backUrl }"><spring:message code="schools"/></a>
        <span class="separator">&gt; </span>
      </div>
    <h2 class="title" role="heading">${ school.name }</h2>
      <div class="toolbar">
          <ul>
              <li>
                <portlet:renderURL var="selectTermUrl"/>
                <form action="${selectTermUrl}" method="post">
                  <input type="hidden" name="action" value="courses"/>
                  <input type="hidden" name="schoolCode" value="${ school.code }"/>
                  <input type="hidden" name="departmentCode" value="${ department.code }"/>
                  <select id="${n}_termPicker" name="termCode" onchange="this.form.submit()">
                    <c:forEach var="t" items="${terms}">
                      <c:set var="selected" value="" />
                      <c:if test="${t.code == term}">
                          <c:set var="selected" value="selected=\"selected\"" />
                      </c:if>
                      <option value="${t.code}" ${selected}>${t.displayName}</option>
                    </c:forEach>
                  </select>
                </form>              
              </li>
          </ul>
      </div>
  </div>

  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">

        <ul>
            <c:forEach items="${ departments }" var="department">
                <portlet:renderURL var="departmentUrl">
                    <portlet:param name="action" value="courses"/>
                    <portlet:param name="schoolCode" value="${ school.code }"/>
                    <portlet:param name="departmentCode" value="${ department.code }"/>
                    <portlet:param name="termCode" value="${ term }"/>
                </portlet:renderURL>
                <li>
                    <a href="${ departmentUrl }">${ department.name }</a>
                </li>
            </c:forEach>
        </ul>
        
    </div>
</div>
