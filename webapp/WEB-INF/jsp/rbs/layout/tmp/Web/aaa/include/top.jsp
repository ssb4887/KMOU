<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/usr" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="${siteInfo.locale_lang}" lang="${siteInfo.locale_lang}">
<head>
	<%@ include file="commonHead.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${layoutCssPath}/common.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${themeCssPath}/layout.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<%@ include file="/WEB-INF/jsp/rbs/include/designPath.jsp" %>
	<%@ include file="/WEB-INF/jsp/rbs/include/contact.jsp"%>
	<%@ include file="/WEB-INF/jsp/rbs/include/login_check_js.jsp"%>
</head>
<body>

<!-- skip navi -->
<ul id="skipNavi">
	<li><a href="#gnb">주메뉴 바로가기</a></li>
	<li><a href="#content">본문내용 바로가기</a></li>
</ul>
<!-- //skip navi -->

<div id="wrapper">
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
			<h3>${crtMenu.menu_name}</h3>