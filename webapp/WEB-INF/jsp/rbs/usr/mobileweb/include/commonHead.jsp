	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
	<title><c:out value="${siteInfo.site_title}"/>-<c:out value="${crtMenu.menu_name}"/></title>
	
	<!-- eGov Mobile CSS -->
	<link rel="stylesheet" href="${contextPath}${cssPath}/jquery.mobile-1.4.5.css"/>
	<link rel="stylesheet" href="${contextPath}${cssPath}/EgovMobile-1.4.5.css"/>
	
	<!-- eGov Mobile JS -->
	<script type="text/javascript" src="${contextPath}${jsPath}/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="${contextPath}${jsPath}/jquery.mobile-1.4.5.js"></script>
	<script type="text/javascript" src="${contextPath}${jsPath}/EgovMobile-1.4.5.js"></script>
	
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
	<c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
	<c:if test="${!empty JAVASCRIPT_PAGE}"><jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/></c:if>
