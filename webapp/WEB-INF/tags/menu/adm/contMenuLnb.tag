<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ attribute name="isAdm"%>
<%@ attribute name="target"%>
<%@ attribute name="ulId"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<%@ variable  name-given="crtSubMenuList" variable-class="net.sf.json.JSONArray" scope="AT_END"%>
<% /* 메뉴콘텐츠관리 > 사용자사이트 메뉴 */ %>
<c:set var="contRUriPath" value="${pageContext.request.contextPath}/${crtSiteId}/menuContents"/>
<c:set var="sid" value="${usrCrtMenu.menu_idx1}"/>
<c:set var="slevel" value="${2}"/>
<c:forEach var="mainListInfo" items="${menuList}">
	<c:set var="mainIdx" value="${mainListInfo.menu_idx}"/>
	<c:set var="mainName" value="menu${mainIdx}"/>
	<c:set var="mainInfo" value="${menus[mainName]}"/>
	<c:set var="mainName" value="${mainInfo['menu_name']}"/>
	<c:choose>
		<c:when test="${!empty mainInfo['an_menu_link']}"><c:set var="mainLink" value="${contRUriPath}${mainInfo['an_menu_link']}"/></c:when>
		<c:when test="${!empty mainInfo['menu_link']}"><c:set var="mainLink" value="${contRUriPath}${mainInfo['menu_link']}"/></c:when>
		<c:otherwise><c:set var="mainLink" value=""/></c:otherwise>
	</c:choose>
	<c:set var="menuAuth" value="${elfn:isMngMenuAuth(mainInfo)}"/>
	<c:if test="${menuAuth}">
		<c:set var="mainLink" value="${elfn:getAuthMngMenuLink(contRUriPath, mainIdx, mainListInfo, menus)}"/>
<!-- 
<ul<c:if test="${!empty ulId}"> id="${ulId}"</c:if><c:if test="${!empty ulClass}"> class="${ulClass}"</c:if>>
	<li class="root<c:if test="${sid == mainIdx}"> on</c:if>">
		<c:if test="${!empty mainListInfo['menu-list']}"></c:if>
		<c:choose>
		<c:when test="${!empty mainLink}">
		<a href="<c:out value="${mainLink}"/>" target="<c:out value="${target}"/>" title="<c:out value="${mainName}"/>"><c:out value="${mainName}"/></a>
		</c:when>
		<c:otherwise>
		<a href="#;"><c:out value="${mainName}"/></a>
		</c:otherwise>
		</c:choose>
	</li>
</ul>
 -->
	</c:if>
</c:forEach>

<mnui:contMenuLnbF level="${slevel}" menuList="${menuList[0]['menu-list']}" menus="${menus}" ulClass="${ulClass}"/>
