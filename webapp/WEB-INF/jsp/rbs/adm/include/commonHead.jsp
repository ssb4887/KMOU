	<c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<title>${siteInfo.site_title}</title>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery.form.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-ui.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/bootstrap.bundle.min.js"/>"></script>
	<c:if test="${!empty JAVASCRIPT_PAGE}"><jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/></c:if>