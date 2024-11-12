<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="ulId"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<% /* sitemap */ %>
<c:forEach var="mainInfo" items="${menuList}">
	<c:if test="${!empty mainInfo['menu-list']}">
<ul<c:if test="${!empty ulId}"> id="${ulId}"</c:if><c:if test="${!empty ulClass}"> class="${ulClass}"</c:if>>
<c:forEach var="menuListInfo" items="${mainInfo['menu-list']}">
	<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
	<c:set var="menuInfoName" value="menu${menuIdx}"/>
	<c:set var="menuInfo" value="${menus[menuInfoName]}"/>
	<c:set var="menuHidden" value="${menuInfo['ishidden']}"/>
	<c:if test="${menuHidden != 1}">
	<c:set var="menuLink" value="${menuInfo['menu_link']}"/>
	<c:set var="menuAuth" value="${elfn:isMenuAuth(menuInfo)}"/>
	<c:if test="${menuAuth}">
		<c:set var="menuLink" value="${elfn:getAuthMenuLink(menuIdx, menuListInfo, menus)}"/>
	<li>
		<h3><a href="<c:out value="${contextPath}${menuLink}"/>" title="${menuInfo['menu_name']}">${menuInfo['menu_name']}</a></h3>
		<c:if test="${!empty menuListInfo['menu-list']}">
		<ul>
		<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
			<c:set var="menuIdx2" value="${menuListInfo2.menu_idx}"/>
			<c:set var="menuInfoName2" value="menu${menuIdx2}"/>
			<c:set var="menuInfo2" value="${menus[menuInfoName2]}"/>
			<c:set var="menuHidden" value="${menuInfo2['ishidden']}"/>
			<c:if test="${menuHidden != 1}">
			<c:set var="menuAuths2" value="${authMap[elfn:toString(menuIdx2)]}"/>
			<c:set var="menuLink2" value="${menuInfo2['menu_link']}"/>
			<c:set var="menuAuth2" value="${elfn:isMenuAuth(menuInfo2)}"/>
				<c:if test="${menuAuth2}">
					<c:set var="menuLink2" value="${elfn:getAuthMenuLink(menuIdx2, menuListInfo2, menus)}"/>
			<li>
				<a href="<c:out value="${contextPath}${menuLink2}"/>" title="${menuInfo2['menu_name']}">${menuInfo2['menu_name']}</a>
				<c:if test="${!empty menuListInfo2['menu-list']}">
				<ul>
				<c:forEach var="menuListInfo3" items="${menuListInfo2['menu-list']}">
					<c:set var="menuIdx3" value="${menuListInfo3.menu_idx}"/>
					<c:set var="menuInfoName3" value="menu${menuIdx3}"/>
					<c:set var="menuInfo3" value="${menus[menuInfoName3]}"/>
					<c:set var="menuHidden" value="${menuInfo3['ishidden']}"/>
					<c:if test="${menuHidden != 1}">
					<c:set var="menuAuths3" value="${authMap[elfn:toString(menuIdx3)]}"/>
					<c:set var="menuLink3" value="${menuInfo3['menu_link']}"/>
					<c:set var="menuAuth3" value="${elfn:isMenuAuth(menuInfo3)}"/>
					<c:if test="${menuAuth3}">
						<c:set var="menuLink3" value="${elfn:getAuthMenuLink(menuIdx3, menuListInfo3, menus)}"/>
					<li>
						<a href="<c:out value="${contextPath}${menuLink3}"/>" title="${menuInfo3['menu_name']}">${menuInfo3['menu_name']}</a>
					</li>
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
		</c:if>
	</li>
	</c:if>
	</c:if>
</c:forEach>
</ul>
	</c:if>
</c:forEach>