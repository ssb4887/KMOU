<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="sid"%>
<%@ attribute name="tid"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<% /* 좌측메뉴 */ %>
<ul class="${ulClass}">
<c:forEach var="menuListInfo" items="${menuList}">
	<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
	<c:set var="menuInfoName" value="menu${menuIdx}"/>
	<c:set var="menuInfo" value="${menus[menuInfoName]}"/>
	<c:set var="menuHidden" value="${menuInfo['ishidden']}"/>
	<c:if test="${menuHidden != 1}">
	<c:set var="menuLink" value="${menuInfo['menu_link']}"/>
	<c:set var="menuAuth" value="${elfn:isMenuAuth(menuInfo)}"/>
	<c:if test="${menuAuth}">
		<c:set var="menuLink" value="${elfn:getAuthMenuLink(menuIdx, menuListInfo, menus)}"/>
		<c:if test="${empty menuLink}"><c:set var="menuLink" value="#"/></c:if>
	<li<c:if test="${sid == menuIdx}"> class="on"</c:if>>
		<a href="<c:out value="${contextPath}${menuLink}"/>" title="${menuInfo['menu_name']}">${menuInfo['menu_name']}</a>
		<c:if test="${!empty menuListInfo['menu-list']}">
		<ul>
		<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
			<c:set var="menuIdx2" value="${menuListInfo2.menu_idx}"/>
			<c:set var="menuInfoName2" value="menu${menuIdx2}"/>
			<c:set var="menuInfo2" value="${menus[menuInfoName2]}"/>
			<c:set var="menuHidden" value="${menuInfo2['ishidden']}"/>
			<c:if test="${menuHidden != 1}">
			<c:set var="menuLink2" value="${menuInfo2['menu_link']}"/>
			<c:set var="menuAuth2" value="${elfn:isMenuAuth(menuInfo2)}"/>
				<c:if test="${menuAuth2}">
					<c:set var="menuLink2" value="${elfn:getAuthMenuLink(menuIdx2, menuListInfo2, menus)}"/>
					<c:if test="${empty menuLink2}"><c:set var="menuLink2" value="#"/></c:if>
			<li<c:if test="${tid == menuIdx2}"> class="on"</c:if>><a href="<c:out value="${contextPath}${menuLink2}"/>" title="${menuInfo2['menu_name']}">${menuInfo2['menu_name']}</a></li>			
			</c:if>
			</c:if>
		</c:forEach>
		</ul>
		</c:if>
	</li>
	</c:if>
	</c:if>
</c:forEach>
</ul>