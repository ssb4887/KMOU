<%@page import="rbs.egovframework.Defines"%>
<!-- header -->
<div id="headerWrap">
		<div id="util">
			<div class="utilWrap">
				<div class="utilBtn">
					<c:choose>
						<c:when test="${elfn:isLoginAuth()}">
							<c:out value="${loginVO.memberNameOrg}"/>님 반갑습니다!
							<a href="<%=MenuUtil.getMenuUrl(4) %>" class="fn_login"><c:out value="${siteMenus.menu4.menu_name}"/></a>
							<c:if test="${elfn:isLogin()}">
							<a href="<%=MenuUtil.getMenuUrl(5) %>" class="fn_join"><c:out value="${siteMenus.menu5.menu_name}"/></a>
							</c:if>
						</c:when>
						<c:otherwise>
							<a href="<%=MenuUtil.getMenuUrl(3) %>" class="fn_login"><c:out value="${siteMenus.menu3.menu_name}"/></a>
							<a href="<%=MenuUtil.getMenuUrl(2) %>" class="fn_join"><c:out value="${siteMenus.menu2.menu_name}"/></a>
						</c:otherwise>
					</c:choose>
					<div class="search">
						<form id="fn_topTotalSearchForm" name="fn_topTotalSearchForm" method="get" action="${contextPath}/main/search.do">
							<input type="hidden" name="mId" id="mId" value="<%=Defines.CODE_MENU_TOTALSEARCH %>"/>
							<fieldset>
								<legend>통합검색</legend>
								<input type="text" name="fn_totalKey" id="fn_totalKey" class="inputTxt" value="" title="검색어를 입력하세요" />
								<input id="fn_btn_fn_totalKey" type="image" alt="검색" src="<c:out value="${contextPath}${themeImgPath}/layout/top_search_btn.gif"/>">
							</fieldset>
						</form>
					</div>		
				</div>
			</div>
			
			<div id="header">
				<h1><a href="<c:out value="${contextPath}${indexUrl}"/>"><img src="<c:out value="${contextPath}${LAYOUT_LOGO_PATH}/${siteInfo.logo_file}"/>" alt="<c:out value="${siteInfo.logo_text}"/>" /></a></h1>
				<!-- gnb -->
				<div id="gnbWrap">
					<h2 class="blind">주메뉴</h2>
					<mnui:gnb ulId="gnb" gid="${crtMenu.menu_idx2}" sid="${crtMenu.menu_idx3}" menuList="${siteMenuList}" menus="${siteMenus}" />
				</div>
				<!-- //gnb -->
			</div>
		</div>
	</div>
	<!-- //header -->