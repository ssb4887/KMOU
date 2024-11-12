<c:if test="${!empty crtMenu.module_id}">
	<c:set var="moduleDesignCssPath" value="style.css"/>
	<c:if test="${crtMenu.module_id == 'obsbook' && crtMenu.use_display_year == '1'}">
	<c:set var="moduleDesignCssPath" value="year_style.css"/>
	</c:if>
	<link href="<c:out value="${contextPath}${moduleResourcePath}/${moduleDesignCssPath}"/>" rel="stylesheet" type="text/css" />
</c:if>
<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/jquery/css/jquery-ui.css"/>"/>