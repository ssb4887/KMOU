<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${siteInfo.locale_lang}" lang="${siteInfo.locale_lang}">
<head>
	<%@ include file="commonHead.jsp" %>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/ui.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/jquery/css/jquery-ui.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/common.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/editor/css/smart_editor2_out.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<c:choose>
		<c:when test="${menuType == 2}">
		<script type="text/javascript">
		</script>
		</c:when>
		<c:otherwise>
			<%@ include file="../../include/contact.jsp"%>
		</c:otherwise>
	</c:choose>
</head>
<body>
<div id="wrapper">
<%@ include file="dashboard_header.jsp" %>
	<!-- container -->
	<div id="container1">
		<div id="content">