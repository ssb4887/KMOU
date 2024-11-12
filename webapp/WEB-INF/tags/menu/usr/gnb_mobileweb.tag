<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/usr" %>
<%@ attribute name="ulId" required="true"%>
<%@ attribute name="gid"%>
<%@ attribute name="sid"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<% /* 주메뉴 */ %>

<div data-role="collapsible-set">
	<c:forEach var="mainInfo" items="${siteMenuList}">
		<c:if test="${!empty mainInfo['menu-list']}">
		
			<c:forEach var="menuListInfo" items="${mainInfo['menu-list']}" varStatus="i">
				<c:set var="menuLink" value=""/>
				<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
				<c:set var="menuInfoName" value="menu${menuIdx}"/>
				<c:set var="menuInfo" value="${siteMenus[menuInfoName]}"/>
				<c:set var="menuHidden" value="${menuInfo['ishidden']}"/>
				
				<c:if test="${menuHidden != 1}">
				<c:set var="menuLink" value="${menuInfo['menu_link']}"/>
				<c:set var="menuAuth" value="${elfn:isMenuAuth(menuInfo)}"/>
					<c:if test="${menuAuth}">
						<c:set var="menuLink" value="${elfn:getAuthMenuLink(menuIdx, menuListInfo, siteMenus)}"/>
						<c:if test="${empty menuLink}"><c:set var="menuLink" value="#"/></c:if>
						
						<div data-role="collapsible" data-collapsed="true">
						<h3>${menuInfo['menu_name']}</h3>
						
						<c:if test="${!empty menuListInfo['menu-list']}">
							<ul data-role="listview" data-inset="true" data-theme="d">
								<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
									<c:set var="menuLink2" value=""/>
									<c:set var="menuIdx2" value="${menuListInfo2.menu_idx}"/>
									<c:set var="menuInfoName2" value="menu${menuIdx2}"/>
									<c:set var="menuInfo2" value="${siteMenus[menuInfoName2]}"/>
									<c:set var="menuHidden" value="${menuInfo2['ishidden']}"/>
									
									<c:if test="${menuHidden != 1}">
									<c:set var="menuLink2" value="${menuInfo2['menu_link']}"/>
									<c:set var="menuAuth2" value="${elfn:isMenuAuth(menuInfo2)}"/>
									<c:if test="${menuAuth2}">
										<c:set var="menuLink2" value="${elfn:getAuthMenuLink(menuIdx2, menuListInfo2, siteMenus)}"/>
										<c:if test="${empty menuLink2}"><c:set var="menuLink2" value="#"/></c:if>
										
										<li><a href="<c:url value="${menuLink2}"/>">${menuInfo2['menu_name']}</a></li>
									</c:if>
									</c:if>
									
								</c:forEach>
							</ul>
						</c:if>
						
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		
		</c:if>
	</c:forEach>
</div>