<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="ulId"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:forEach var="mainInfo" items="${menuList}">
	<c:if test="${!empty mainInfo['menu-list']}">
<ul<c:if test="${!empty ulId}"> id="${ulId}"</c:if><c:if test="${!empty ulClass}"> class="${ulClass}"</c:if>>
<c:forEach var="menuListInfo" items="${mainInfo['menu-list']}">
	<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
	<c:set var="menuInfoName" value="menu${menuIdx}"/>
	<c:set var="menuInfo" value="${menus[menuInfoName]}"/>
	<li>
		<a href="<c:out value="${contextPath}${menuInfo['menu_link']}"/>" title="${menuInfo['menu_name']}">${menuInfo['menu_name']}</a>
		<c:if test="${!empty menuListInfo['menu-list']}">
		<ul>
		<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
			<c:set var="menuIdx2" value="${menuListInfo2.menu_idx}"/>
			<c:set var="menuInfoName2" value="menu${menuIdx2}"/>
			<c:set var="menuInfo2" value="${menus[menuInfoName2]}"/>
			<li>
				<a href="<c:out value="${contextPath}${menuInfo2['menu_link']}"/>" title="${menuInfo2['menu_name']}">${menuInfo2['menu_name']}</a>
				<c:if test="${!empty menuListInfo2['menu-list']}">
				<ul>
				<c:forEach var="menuListInfo3" items="${menuListInfo2['menu-list']}">
					<c:set var="menuIdx3" value="${menuListInfo3.menu_idx}"/>
					<c:set var="menuInfoName3" value="menu${menuIdx3}"/>
					<c:set var="menuInfo3" value="${menus[menuInfoName3]}"/>
					<li>
						<a href="<c:out value="${contextPath}${menuInfo3['menu_link']}"/>" title="${menuInfo3['menu_name']}">${menuInfo3['menu_name']}</a>
					</li>
				</c:forEach>
				</ul>
				</c:if>
			</li>
		</c:forEach>
		</ul>
		</c:if>
	</li>
</c:forEach>
</ul>
	</c:if>
</c:forEach>