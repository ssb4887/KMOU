	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=yes">
	<title><c:out value="${siteInfo.site_title}"/>-<c:out value="${crtMenu.menu_name}"/></title>
	
	
	<!-- 해양대 css 및 js  -->
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/style.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/main.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/sub.css"/>">
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/tabulator_bootstrap5.css"/>">
	
	<link rel="shortcut icon" href="../images/layout/favicon.ico">

	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/jquery-ui.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/slick.js"/>"></script>
    <script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/index.global.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/common.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/sub.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/bootstrap.bundle.min.js"/>"></script>
<%-- 	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/bootstrap.min.js"/>"></script> --%>
    
    <c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
	<c:if test="${!empty JAVASCRIPT_PAGE}"><jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/></c:if>