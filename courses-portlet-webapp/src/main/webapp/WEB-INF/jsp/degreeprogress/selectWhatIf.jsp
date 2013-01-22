<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<portlet:renderURL var="currentReportUrl">
    <portlet:param name="action" value="showProgress"/>
</portlet:renderURL>
<portlet:renderURL var="whatIfReportUrl">
    <portlet:param name="action" value="getScenario"/>
</portlet:renderURL>

<div class="degree-progress">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/degree-progress.css"/>"></link>

<h2>What-If Report</h2>

    <p>Welcome to the what-if reporting tool.  You may create
    a <a href="${ whatIfReportUrl }">what-if analysis report</a> for a new degree.</p>
    
    <p class="largeAlert">The degree progress report is not currently available for your curriculum.</p>
    
    <p>This evaluation is not meant to replace your adviser or the University Catalog, but it serves as a great 
    reference tool to plan for graduation by tracking your progress towards degree completion.
    Undergraduate students can find out <a href="http://www.oakland.edu/?id=17407&sid=219" target="_blank">more information here</a>.</p>

</div>
