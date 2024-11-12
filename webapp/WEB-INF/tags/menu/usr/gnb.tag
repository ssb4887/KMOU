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
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<% /* 주메뉴 */ %>
<c:forEach var="mainInfo" items="${menuList}">
	<c:if test="${!empty mainInfo['menu-list']}">
<ul id="${ulId}">
<c:forEach var="menuListInfo" items="${mainInfo['menu-list']}" varStatus="i">
	<c:set var="menuLink" value=""/>
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
	<li<c:if test="${gid == menuIdx}"> class="on"</c:if>>
		<span><a href="<c:out value="${contextPath}${menuLink}"/>" title="${menuInfo['menu_name']}">${menuInfo['menu_name']}</a></span>
		<c:if test="${!empty menuListInfo['menu-list']}">
		<div class="depth2Wrap sub${elfn:getLPAD(i.count, '0', 2)}">
			<div class="inner">
				<div class="menuWrap">
					<ul class="depth2">
						<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
							<c:set var="menuLink2" value=""/>
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
							<li<c:if test="${sid == menuIdx2}"> class="on"</c:if>><a href="<c:out value="${contextPath}${menuLink2}"/>" title="${menuInfo2['menu_name']}">${menuInfo2['menu_name']}</a></li>
							</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		</c:if>
	</li>
	</c:if>
	</c:if>
</c:forEach>
	<li><span><a href="#">전체메뉴</a></span>
	<div class="depth2Wrap sub${elfn:getLPAD(fn:length(mainInfo['menu-list']) + 1, '0', 2)}" style="width: 150%;margin-left: -115px;">
		<div class="allMenu">
			<div class="inner">
				<mnui:sitemap menuList="${siteMenuList}" menus="${siteMenus}" />
			</div>
		</div>
	</div>
	</li>
</ul>
	</c:if>
</c:forEach>