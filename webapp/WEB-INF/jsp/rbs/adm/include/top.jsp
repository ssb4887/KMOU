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
		$(function(){
				fn_contactSubmitForm('<c:out value="${contextPath}/${crtSiteId}/menuContents/stats/contact.do?mId=${queryString.mId}"/>&mn=${elfn:encode(usrCrtMenu.menu_name, 'UTF-8')}');
			});</script>
		</c:when>
		<c:otherwise>
			<%@ include file="../../include/contact.jsp"%>
		</c:otherwise>
	</c:choose>
</head>
<body>
<div id="wrapper">
<c:set var="crtSubMenu" value=""/>
<%@ include file="header.jsp" %>
	<!-- container -->
	<div id="container1">
		<div id="content">
			<!-- 탭 메뉴 -->
			<c:if test="${!empty crtSubMenuList}" >
			<c:set var="contRUriPath" value="${pageContext.request.contextPath}/${crtSiteId}/menuContents"/>
			
			<div class="tabMenuA">
				<ul>
				<c:forEach items="${crtSubMenuList}" var="subMenu">
					<c:set var="menuIdx" value="${subMenu.menu_idx}"/>
					<c:set var="menuInfoName" value="menu${menuIdx}"/>
					<c:set var="menuInfo" value="${usrSiteMenus[menuInfoName]}"/>
					<c:set var="menuModuleId" value="${menuInfo['module_id']}"/>
					<c:set var="menuFnIdx" value="${menuInfo['fn_idx']}"/>
					<c:set var="menuName" value="${menuInfo['menu_name']}"/>
					<c:set var="menuLevel" value="${menuInfo['menu_level']}"/>
					<c:set var="anIshidden" value="${menuInfo['an_ishidden']}"/>
					<c:set var="usePrivate" value="${menuInfo['fn_use_private']}"/>
					<c:if test="${anIshidden != 1 && usePrivate != 1 && (menuIdx == 1 || menuIdx > 30)}">
					<c:set var="menuLink" value="${contRUriPath}${menuInfo['menu_link']}"/>
					<li<c:if test="${CrtMenu.menu_idx4 == menuIdx}"> class="on"</c:if>>
						<a href="${menuLink}"  title="<c:out value="${menuName}asdasd"/>"><c:out value="${menuName}"/></a>
					</li>
					</c:if>
				</c:forEach>
				</ul>
			</div>
			</c:if>
