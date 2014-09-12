
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

<%@ include file="/WEB-INF/jsp/include.jsp"%>
<li>
  <div class="ui-grid-c ui-li-heading">
    <div class="ui-block-a">Term</div>
    <div class="ui-block-b">&nbsp;</div>
    <div class="ui-block-c">Credits: ${ courseSummary.credits }</div>
    <div class="ui-block-d">GPA: ${ courseSummary.gpa }</div>
  </div>
  <div class="ui-grid-c ui-li-heading">
    <div class="ui-block-a ellipsis"><strong>Cumulative</strong></div>
    <div class="ui-block-b">&nbsp;</div>
    <div class="ui-block-c"><strong>Credits: ${ courseSummary.overallCredits }</strong></div>
    <div class="ui-block-d"><strong>GPA: ${ courseSummary.overallGpa }</strong></div>
  </div>
</li>
