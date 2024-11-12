	<c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title><c:choose><c:when test="${!empty param.page_tit}"><c:out value="${param.page_tit}"/>-</c:when><c:when test="${!empty queryString.mt}"><c:out value="${queryString.mt}"/>-</c:when><c:when test="${!empty queryString.tit}"><c:out value="${queryString.tit}"/>-</c:when></c:choose>${crtMenu.menu_name}-${siteInfo.site_title}</title>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery.form.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-ui.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
	<c:if test="${!empty JAVASCRIPT_PAGE}"><jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/></c:if>