<jsp:directive.include file="/WEB-INF/jsp/header.jsp"/>

<portlet:renderURL var="refreshUrl" />
<div>
    <div>
      <spring:message code="grades.unavailable" arguments="${refreshUrl}" htmlEscape="false" javaScriptEscape="false" />
    </div>
</div>
