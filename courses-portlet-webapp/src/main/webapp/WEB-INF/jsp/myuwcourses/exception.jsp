<%@ page import="javax.portlet.PortletSession" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<%--
    Document   : NoTerm
    Created on : Aug 13, 2013, 11:04:03 AM
    Author     : Sengupta5
--%>
<div class="fl-widget portlet CoursesPortlet" role="section">
  <!-- Portlet Titlebar -->
  <div class="fl-widget-titlebar titlebar portlet-titlebar" role="sectionhead">
    <%@ include file="/WEB-INF/jsp/myuwcourses/fragments/viewLinks.jsp"%>
    <h2 class="title" role="heading">
      Error
    </h2>
  </div> <!-- end: portlet-titlebar -->

  <!-- Portlet Content -->
  <div class="fl-widget-content content portlet-content" role="main">
    <spring:message code="${exception.displayMessg}" htmlEscape="false" arguments="${portletSessionScope.helpDeskURL}"/>
    <span style="display:none;">${exception.message}<br/></span>
    <span style="display:none;">${exception.exceptionMessage}<br/></span>
    <span style="display:none;">${exception.printStackTraceMessage}<br/></span>
  </div>
</div>
