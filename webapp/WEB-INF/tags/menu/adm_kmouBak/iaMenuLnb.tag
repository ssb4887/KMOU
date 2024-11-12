<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ attribute name="target"%>
<%@ attribute name="ulId"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<% /* IA(정보구조)관리 > 사용자사이트 메뉴 */ %>
<c:set var="contRUriPath" value="${pageContext.request.contextPath}/${crtSiteId}/menuContents"/>
<c:set var="sid" value="${usrCrtMenu.menu_idx1}"/>
<c:set var="slevel" value="${2}"/>
<c:forEach var="menuListInfo" items="${menuList}">
	<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
	<c:set var="menuName" value="menu${menuIdx}"/>
	<c:set var="menuInfo" value="${menus[menuName]}"/>
	<c:set var="menuName" value="${menuInfo['menu_name']}"/>
	<c:set var="menuLevel" value="${menuInfo['menu_level']}"/>
	<c:set var="menuLink" value="${contRUriPath}${menuInfo['menu_link']}"/>
<ul<c:if test="${!empty ulId}"> id="${ulId}"</c:if><c:if test="${!empty ulClass}"> class="${ulClass}"</c:if>>
	<li class="root<c:if test="${sid == menuIdx}"> on</c:if>" id="fn_leftMenu_${menuIdx}" data-level="<c:out value="${menuLevel}"/>">
		<c:if test="${!empty menuListInfo['menu-list']}"><button type="button" class="btn_tree">닫기</button></c:if>
		<input type="checkbox" id="mngMenuIdx${menuIdx}" name="mngMenuIdx" title="<c:out value="${menuName}"/>" value="<c:out value="${menuIdx}"/>" class="fn_skip"/>
		<a href="#<c:out value="${menuIdx}"/>" target="<c:out value="${target}"/>" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
		<mnui:iaMenuLnbF level="${slevel}" menuList="${menuListInfo['menu-list']}" menus="${menus}"/>
	</li>
</ul>
</c:forEach>