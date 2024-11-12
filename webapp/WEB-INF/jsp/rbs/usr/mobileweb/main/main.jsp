<%@ include file="../include/commonTop.jsp"%> 
<c:if test="${!empty TOP_PAGE}"><jsp:include page="../include/main_top.jsp" flush = "false"/></c:if>
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
						<c:choose>
						<c:when test="${empty menuListInfo['menu-list']}">
						<ul data-role="listview" data-inset="true" data-theme="d">
							<li><a href="<c:out value="${contextPath}${menuLink}"/>" rel="external">${menuInfo['menu_name']}</a></li>
						</ul>
						</c:when>
						<c:otherwise>
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
										<li><a href="<c:out value="${contextPath}${menuLink2}"/>" rel="external">${menuInfo2['menu_name']}</a></li>
									</c:if>
									</c:if>
								</c:forEach>
							</ul>
						</c:otherwise>
						</c:choose>
						</div>
					</c:if>
				</c:if>
			</c:forEach>
		
		</c:if>
	</c:forEach>
</div>


<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/main_bottom.jsp" flush = "false"/></c:if>