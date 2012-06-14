<jsp:directive.include file="/WEB-INF/jsp/header.jsp"/>

<portlet:renderURL var="refreshUrl" />
<div>
    <div>
      <span><spring:message code="missing.emplid" /> </span>
      <a href="${refreshUrl}"><spring:message code="refresh" /></a>
    </div>
</div>
