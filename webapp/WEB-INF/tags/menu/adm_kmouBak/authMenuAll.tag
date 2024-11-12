<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="inputUrl"%>
<%@ attribute name="target"%>
<%@ attribute name="ulId"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="menuList" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="menus" type="net.sf.json.JSONObject" required="true"%>
<c:forEach var="mainInfo" items="${menuList}">
	<c:if test="${!empty mainInfo['menu-list']}">
<ul<c:if test="${!empty ulId}"> id="${ulId}"</c:if><c:if test="${!empty ulClass}"> class="${ulClass}"</c:if>>
<c:forEach var="menuListInfo" items="${mainInfo['menu-list']}">
	<c:set var="menuIdx" value="${menuListInfo.menu_idx}"/>
	<c:set var="menuInfoName" value="menu${menuIdx}"/>
	<c:set var="menuInfo" value="${menus[menuInfoName]}"/>
	<c:set var="menuModuleId" value="${menuInfo['module_id']}"/>
	<c:set var="menuFnIdx" value="${menuInfo['fn_idx']}"/>
	<c:set var="menuName" value="${menuInfo['menu_name']}"/>
	<c:set var="menuLink" value="${inputUrl}&mnuCd=${menuInfo['menu_idx']}"/>
	<li>
		<c:choose>
		<c:when test="${!empty menuLink}">
		<a href="<c:out value="${menuLink}"/>" target="<c:out value="${target}"/>" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
		</c:when>
		<c:otherwise>
		<span><c:out value="${menuName}"/></span>
		</c:otherwise>
		</c:choose>
		<c:if test="${!empty menuListInfo['menu-list']}">
		<ul>
		<c:forEach var="menuListInfo2" items="${menuListInfo['menu-list']}">
			<c:set var="menuIdx2" value="${menuListInfo2.menu_idx}"/>
			<c:set var="menuInfoName2" value="menu${menuIdx2}"/>
			<c:set var="menuInfo2" value="${menus[menuInfoName2]}"/>
			<c:set var="menuModuleId" value="${menuInfo2['module_id']}"/>
			<c:set var="menuFnIdx" value="${menuInfo2['fn_idx']}"/>
			<c:set var="menuName" value="${menuInfo2['menu_name']}"/>
			<c:set var="menuLink" value="${inputUrl}&mnuCd=${menuInfo2['menu_idx']}"/>
			<li>
				<c:choose>
				<c:when test="${!empty menuLink}">
				<a href="<c:out value="${menuLink}"/>" target="<c:out value="${target}"/>" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
				</c:when>
				<c:otherwise>
				<span><c:out value="${menuName}"/></span>
				</c:otherwise>
				</c:choose>
				<c:if test="${!empty menuListInfo2['menu-list']}">
				<ul>
				<c:forEach var="menuListInfo3" items="${menuListInfo2['menu-list']}">
					<c:set var="menuIdx3" value="${menuListInfo3.menu_idx}"/>
					<c:set var="menuInfoName3" value="menu${menuIdx3}"/>
					<c:set var="menuInfo3" value="${menus[menuInfoName3]}"/>
					<c:set var="menuModuleId" value="${menuInfo3['module_id']}"/>
					<c:set var="menuFnIdx" value="${menuInfo3['fn_idx']}"/>
					<c:set var="menuName" value="${menuInfo3['menu_name']}"/>
					<c:set var="menuLink" value="${inputUrl}&mnuCd=${menuInfo3['menu_idx']}"/>
					<li>
						<c:choose>
						<c:when test="${!empty menuLink}">
						<a href="<c:out value="${menuLink}"/>" target="<c:out value="${target}"/>" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
						</c:when>
						<c:otherwise>
						<span><c:out value="${menuName}"/></span>
						</c:otherwise>
						</c:choose>
						<c:if test="${!empty menuListInfo3['menu-list']}">
						<ul>
						<c:forEach var="menuListInfo4" items="${menuListInfo3['menu-list']}">
							<c:set var="menuIdx4" value="${menuListInfo4.menu_idx}"/>
							<c:set var="menuInfoName4" value="menu${menuIdx4}"/>
							<c:set var="menuInfo4" value="${menus[menuInfoName4]}"/>
							<c:set var="menuModuleId" value="${menuInfo4['module_id']}"/>
							<c:set var="menuFnIdx" value="${menuInfo4['fn_idx']}"/>
							<c:set var="menuName" value="${menuInfo4['menu_name']}"/>
							<c:set var="menuLink" value="${inputUrl}&mnuCd=${menuInfo4['menu_idx']}"/>
							<li>
								<c:choose>
								<c:when test="${!empty menuLink}">
								<a href="<c:out value="${menuLink}"/>" target="<c:out value="${target}"/>" title="<c:out value="${menuName}"/>"><c:out value="${menuName}"/></a>
								</c:when>
								<c:otherwise>
								<span><c:out value="${menuName}"/></span>
								</c:otherwise>
								</c:choose>
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
	</li>
</c:forEach>
</ul>
	</c:if>
</c:forEach>