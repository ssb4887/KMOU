			<div id="lnbWrap">
				<h2>${crtMenu.menu_name2}</h2>
				<c:set var="lnbMenuList" value="${elfn:getLevelMenuList(crtMenu.menu_idx, 4)}"/>
				<c:if test="${!empty lnbMenuList}">
				<mnui:lnb ulClass="lnb" sid="${crtMenu.menu_idx4}" tid="${crtMenu.menu_idx5}" menuList="${lnbMenuList}" menus="${siteMenus}"/>
				</c:if>
			</div>
			<div id="content"<c:if test="${(empty menuType || menuType == 0) && empty lnbMenuList}"> class="wfull"</c:if>>
			<h3>${crtMenu.menu_name}</h3>