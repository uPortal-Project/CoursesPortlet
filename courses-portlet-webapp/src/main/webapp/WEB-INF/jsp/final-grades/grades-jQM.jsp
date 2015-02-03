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

<div id="${n}" class="GradesPortlet isMobile">
  <div class="portlet-content" data-role="main">
    <%-- header --%>
    <%@ include file="/WEB-INF/jsp/fragments/termList.jsp"%>

    <%-- grades --%>
    <%@ include file="/WEB-INF/jsp/final-grades/fragments/gradesUpdate.jsp"%>
  </div>
</div>

<portlet:resourceURL var="gradesCourseListUrl" id="gradesUpdate" />
<spring:message var="errorMessage"
                code="grades.unavailable"
                arguments="${portletSessionScope.helpDeskURL}"
                htmlEscape="false"
                javaScriptEscape="false" />
<script type="text/javascript" language="javascript">
  <rs:compressJs>(function($) {
    $(function() {
      coursesPortlet.updateGradesTermHandler({
        termEl: $('#${n}_termPicker'),
        gradesSelector : '#${n}_grades_data',
        loadingEl: $('#${n}_loading'),
        errorEl: $('#${n}_error'),
        errorMessage : '${errorMessage}',
        dataUrl : '${gradesCourseListUrl}'
      });
    });
  })(coursesPortlet.jQuery);
  </rs:compressJs>
</script>