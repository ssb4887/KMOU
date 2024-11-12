<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ attribute name="target"%>
<%@ attribute name="level"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<% /* 메뉴콘텐츠관리 > 사용자사이트 메뉴 재귀함수 */ %>
<c:set var="sid" value="menu_idx${level}"/>
<c:set var="contRUriPath" value="${pageContext.request.contextPath}/${crtSiteId}/menuContents"/>
	<c:if test="${!empty menuList}">
		<ul>
		<c:forEach var="menuListInfo" items="${menuList}">
			<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
			<c:set var="menuInfoName" value="menu${menuIdx}"/>
			<c:set var="menuInfo" value="${menus[menuInfoName]}"/>
			<c:set var="menuModuleId" value="${menuInfo['module_id']}"/>
			<c:set var="menuFnIdx" value="${menuInfo['fn_idx']}"/>
			<c:set var="menuName" value="${menuInfo['menu_name']}"/>
			<c:set var="anIshidden" value="${menuInfo['an_ishidden']}"/>
			<c:set var="usePrivate" value="${menuInfo['fn_use_private']}"/>
			<c:if test="${anIshidden != 1 && usePrivate != 1 && (menuIdx == 1 || menuIdx > 30)}">
			<c:set var="menuLink" value="${contRUriPath}${menuInfo['menu_link']}"/>
			<li<c:if test="${usrCrtMenu[sid] == menuIdx}"> class="on"</c:if>>
				<c:if test="${!empty menuListInfo['menu-list']}"><button type="button" class="btn_tree">닫기</button></c:if>
				<c:choose>
				<c:when test="${!empty menuLink}">
				<a href="#" data-idx="${menuIdx}" data-module="${menuModuleId}" data-sf="${menuFnIdx}" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
				</c:when>
				<c:otherwise>
				<span><c:out value="${menuName}"/></span>
				</c:otherwise>
				</c:choose>
				<c:if test="${!empty menuListInfo['menu-list']}">
				<mnui:menuSearchLnbF level="${level + 1}" menuList="${menuListInfo['menu-list']}" menus="${menus}"/>
				</c:if>
			</li>
			</c:if>
		</c:forEach>
		</ul>
	</c:if>