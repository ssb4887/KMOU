	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><c:out value="${siteInfo.site_title}"/>-<c:out value="${crtMenu.menu_name}"/></title>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-ui.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${layoutJsPath}/jcarousellite_1.0.1.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${layoutJsPath}/design.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${layoutJsPath}/ui.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
	<c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
	<c:if test="${!empty JAVASCRIPT_PAGE}"><jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/></c:if>