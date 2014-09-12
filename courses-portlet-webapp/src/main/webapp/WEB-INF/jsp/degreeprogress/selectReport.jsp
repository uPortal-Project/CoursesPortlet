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
<portlet:renderURL var="currentReportUrl">
    <portlet:param name="action" value="showProgress"/>
</portlet:renderURL>
<portlet:renderURL var="whatIfReportUrl">
    <portlet:param name="action" value="getScenario"/>
</portlet:renderURL>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/degree-progress.css"/>"></link>

<div class="degree-progress">

<h2>Degree Progress Report</h2>

    <p>
        Welcome to the degree progress reporting tool.  Here you can view the progress
        towards your <a href="${ currentReportUrl }">current degree</a>.
    </p>
    
    <p>This evaluation is not meant to replace your adviser or the University Catalog, but it serves as a great 
    reference tool to plan for graduation by tracking your progress towards degree completion.
    Undergraduate students can find out <a href="${ infoLink }" target="_blank">more information here</a>.</p>

</div>