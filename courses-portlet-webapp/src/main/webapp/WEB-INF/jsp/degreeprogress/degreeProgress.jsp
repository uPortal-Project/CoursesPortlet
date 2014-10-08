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

<link rel="stylesheet" type="text/css" href="<c:url value='/css/degree-progress.css'/>"/>
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" type="text/css" media="all"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js" type="text/javascript"></script>

<!-- Portlet -->
<div id="${n}" class="degree-progress">
    <div class="fl-flex-2">
        <div class="fl-col one">
            <!--
            <div style="page-break-before: always;">
                <h2 class="title" role="heading">Degree Summary</h2>
                <dl>

                    <dt>Program</dt>
                    <dd>${ report.program }</dd>
                    <c:forEach items="${ program.majors }" var="major">
                        <dt>Major</dt>
                        <dd>${ major.name }</dd>
                    </c:forEach>
                    <c:forEach items="${ program.minors }" var="minor">
                        <dt>Minor</dt>
                        <dd>${ minor.name }</dd>
                    </c:forEach>
                    <c:forEach items="${ program.concentrations }" var="concentration">
                        <dt>Concentration</dt>
                        <dd>${ concentration.name }</dd>
                    </c:forEach>
                    <dt>Catalog Term</dt>
                    <dd>${report.catalogTerm }
                    </dd>
                </dl>
            </div>
            -->
            <div style="page-break-before: always;">
                <h2>Credits and GPA</h2>
                <dl>
                    <dt>Overall GPA</dt>
                    <dd class="${ report.overallGpa >= report.requiredOverallGpa ? 'complete' : 'incomplete' }">
                        <span>${ report.overallGpa }</span>
                        <c:if test="${ report.requiredOverallGpa > 0 }">
                            (Required: ${ report.requiredOverallGpa })
                        </c:if>
                    </dd>

                    <dt>Program GPA</dt>
                    <dd class="${ report.programGpa >= report.requiredProgramGpa ? 'complete' : 'incomplete' }">
                        <span>${ report.programGpa }</span>
                        <c:if test="${ report.requiredProgramGpa > 0 }">
                            (Required: ${ report.requiredProgramGpa })
                        </c:if>
                    </dd>
                    <dt>Institutional Credits</dt>
                    <dd class="${ report.institutionalCredits >= report.requiredInstitutionalCredits ? 'complete' : 'incomplete' }">
                        ${ report.institutionalCredits } / ${ report.requiredInstitutionalCredits }
                    </dd>
                    <dt>Residency Credits</dt>
                    <dd class="${ report.residencyCredits >= report.requiredResidencyCredits ? 'complete' : 'incomplete' }">
                        ${ report.residencyCredits } / ${ report.requiredResidencyCredits }
                    </dd>
                    <dt>Total Credits</dt>
                    <dd class="${ report.credits >= report.requiredCredits ? 'complete' : 'incomplete' }">
                        ${ report.credits } / ${ report.requiredCredits }
                    </dd>
                </dl>
            </div>
            <h2 style="clear:both;page-break-before: always;">Degree Progress</h2>

            <c:set var="progress" value="${ 100*(report.credits/report.requiredCredits) }"/>
            <script type="text/javascript">
                $(function () {
                    $("#progressbar").progressbar({
                        value: <fmt:formatNumber value = "${ progress }" maxFractionDigits = "0" />
                });
                $("#progressbar > div").css({ 'background': '#397D02' });

                })
                ;
            </script>

            <div id="progressbar"></div>
            <p>* Progress bar based on credits.</p>

            <p class="requirements-message">
                Requirements are listed below by area.
                <br><img src='<c:url value="/img/cross.png"/>'>Incomplete areas are marked in red.
                <br><img src='<c:url value="/img/check-mark.png"/>'>Complete areas are marked in green.
                <br><span class="in-progress">In-Progress</span> courses are marked in orange.
            </p>
        </div>

        <div class="fl-col two">
            <c:if test="${ report.programText != null}">
                <p class="programText">${ report.programText }</p>

                <h2>Requirements</h2>
            </c:if>

            <c:forEach items="${ report.sections }" var="section">
                <div class="portlet-section">
                    <h3 class="title" role="heading">${ section.name }</h3>
                    <c:if test="${ not empty section.gpa }">
                        <p class="note ${ section.gpa >= section.requiredGpa and section.credits >= section.requiredCredits ? 'complete' : 'incomplete' }">
                            Area GPA: ${ section.gpa }
                            <c:if test="${ section.requiredGpa > 0 }">
                                (Required: ${ section.requiredGpa })
                            </c:if> /
                            Credits: ${ section.credits }
                            <c:if test="${ section.requiredCredits > 0 }">
                                (Required: ${ section.requiredCredits })
                            </c:if>
                        </p>
                    </c:if>
                    <table width="100%">
                        <tr>
                            <td class="courseNumber">Satisfied By</td>
                            <td class="courseTitle">Title</td>
                            <td class="displayName">Term</td>
                            <td class="credits">Credits</td>
                            <td class="code">Grade</td>
                            <td class="source">Source</td>
                        </tr>
                    </table>
                    <c:forEach items="${ section.courseRequirements }" var="requirement" varStatus="status">
                        <c:if test="${ section.name == 'Courses Not Used'}">
                            <div class="courses-not-used ${ status.index % 2 == 0 ? 'even' : 'odd'}">
                        </c:if>
                        <c:if test="${ section.name != 'Courses Not Used'}">
                            <div class="requirement ${ status.index % 2 == 0 ? 'even' : 'odd' } ${ status.last ? 'last' : '' }">
                                <h4 class="${ requirement.completed ? 'complete' : 'incomplete' }">
                                    <c:if test="${ !requirement.completed }">
                                        <img src='<c:url value="/img/cross.png"/>' alt="incomplete requirement"/>
                                    </c:if>
                                    <c:if test="${ requirement.completed }">
                                        <img src='<c:url value="/img/check-mark.png"/>' alt="completed requirement"/>
                                    </c:if>
                                    ${ requirement.name }</h4>
                                <c:if test="${ not empty requirement.description }">
                                    <p class="requirement-description">${ requirement.description }</p>
                                </c:if>
                        </c:if>
                        <table width="100%">
                            <c:forEach items="${ requirement.registrations }" var="registration">
                                <tr>
                                    <td class="courseNumber">${ registration.course.abbreviation }</td>
                                    <td class="courseTitle">${ registration.course.title }</td>
                                    <td class="displayName">${ registration.semester.name }</td>
                                    <td class="credits"> ${ registration.source == 'Adjustment(WAV)' || registration.source == 'Test' ? '' : registration.credits } </td>
                                    <td class="code"> ${ registration.source == 'Adjustment(WAV)' ? '' : registration.grade.code } </td>
                                    <td ${ registration.source == 'In-Progress' ? 'class="in-progress"' : 'class="source"' }>${ registration.source }</td>
                                </tr>
                            </c:forEach>
                        </table>
                </div>
            </c:forEach>
        </div>
        </c:forEach>

    </div>
</div>
<a style="text-decoration:none;font-size:0.9em;" href="<portlet:renderURL portletMode='view'/>">
    <img src="<c:url value='/icons/arrow_left.png'/>" border="0" height="16" width="16" style="vertical-align:middle"/>
    <spring:message code="back"/>
</a>
</div>