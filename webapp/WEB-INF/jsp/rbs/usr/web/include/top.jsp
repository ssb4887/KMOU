<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/usr" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@ include file="commonHead.jsp" %>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/ui.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/common.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<%@ include file="../../../include/designPath.jsp" %>
	<%@ include file="../../../include/contact.jsp"%>
	<%@ include file="../../../include/login_check_js.jsp"%>
</head>
<body>

<!-- skip navi -->
<!-- <ul id="skipNavi">
	<li><a href="#gnb">주메뉴 바로가기</a></li>
	<li><a href="#content">본문내용 바로가기</a></li>
</ul> -->
<!-- //skip navi -->

<%-- <div id="wrapper">
<%@ include file="header.jsp" %>
	<!-- container -->
	<div id="container">
		<div id="contentsWrap">
			<div id="lnbWrap">
				<h2>${crtMenu.menu_name2}</h2>
				<c:set var="lnbMenuList" value="${elfn:getLevelMenuList(crtMenu.menu_idx, 3)}"/>
				<c:if test="${!empty lnbMenuList}">
					<mnui:lnb ulClass="lnb" sid="${crtMenu.menu_idx4}" tid="${crtMenu.menu_idx5}" menuList="${lnbMenuList}" menus="${siteMenus}"/>
				</c:if>
			</div>
			<div id="content">
			<h3>${crtMenu.menu_name}</h3> --%>

<div class="header_wrap position-fixed start-50 translate-middle-x w-100">
<%@ include file="header.jsp" %>
</div>
<!-- container -->
	<!-- <div id="mainContainer">
		<div id="mainContents"> -->
		<c:if test="${isLoginPage ne 'Y' }">
		<div class="container_wrap">
        	<div class="sub_wrap">
		</c:if>