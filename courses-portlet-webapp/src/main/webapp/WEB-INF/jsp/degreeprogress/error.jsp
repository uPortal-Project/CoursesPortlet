<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/degree-progress.css"/>"></link>
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" type="text/css" media="all" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js" type="text/javascript"></script>

<h2 class="title" role="heading">Sorry we cannot complete your Degree Evaluation at this time.</h2>
<c:if test="${err != null}">
        <p class="requirements-message">${err}</p>
</c:if>
<p>Please report this error if necessary and close browser window.</p>
