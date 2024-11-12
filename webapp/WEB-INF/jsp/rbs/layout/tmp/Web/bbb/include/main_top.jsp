<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/usr" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${siteInfo.locale_lang}" lang="${siteInfo.locale_lang}">
<head>
	<%@ include file="commonHead.jsp" %>
	<script type="text/javascript" src="<c:out value="${contextPath}${layoutJsPath}/jquery.orbit.min.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${layoutCssPath}/main.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${themeCssPath}/layout.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<%@ include file="/WEB-INF/jsp/rbs/include/contact.jsp"%>
	<%@ include file="/WEB-INF/jsp/rbs/include/login_check_js.jsp"%>
</head>
<body>

<!-- skip navi -->
<ul id="skipNavi">
	<li><a href="#gnb">주메뉴 바로가기</a></li>
	<li><a href="#mainContents">본문내용 바로가기</a></li>
</ul>
<!-- //skip navi -->

<div id="mainWrapper">
<%@ include file="header.jsp" %>
<!-- container -->
	<div id="mainContainer">
		<div id="mainContents">