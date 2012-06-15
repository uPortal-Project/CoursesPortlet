
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
<%--
    Model Attributes:
        termList        - TermList
        coursesByTerm   - CoursesByTerm
        selectedTerm    - Term
 --%>
<div class="box_credits_gpa">
	<div class="left box_credits">
		<div class="left box_credits_text">Term Credits</div>
		<div class="left box_credits_numbers">${ coursesByTerm.credits }</div>

		<div class="clear_left left box_credits_text">Cum. Credits</div>
		<div class="left box_credits_numbers">${
			coursesByTerm.overallCredits }</div>
	</div>

	<div class="left box_gpa">
		<div class="left box_gpa_text ">Term GPA</div>
		<div class="left box_gpa_numbers">${ coursesByTerm.gpa }</div>

		<div class="clear_left left box_gpa_text">Cum. GPA</div>
		<div class="left box_gpa_numbers">${ coursesByTerm.overallGpa }</div>
	</div>
</div>