<!-- header -->
	<%@page import="rbs.egovframework.Defines"%>
<div id="headerWrap">
		<div id="util">
			<div class="utilWrap">
				<div class="utilBtn">
					<c:choose>
						<c:when test="${elfn:isLoginAuth()}">
							<c:out value="${loginVO.getMemberNameOrg()}"/>님 반갑습니다!
							<a href="<%=MenuUtil.getMenuUrl(4) %>"><img alt="로그아웃" src="<c:out value="${contextPath}${imgPath}/layout/top_logout.gif"/>"></a>
							<c:if test="${elfn:isLogin()}">
							<a href="<%=MenuUtil.getMenuUrl(5) %>"><img alt="정보수정" src="<c:out value="${contextPath}${imgPath}/layout/top_modify.gif"/>"></a>
							</c:if>
						</c:when>
						<c:otherwise>
							<a href="<%=MenuUtil.getMenuUrl(3) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_login.gif"/>" alt="로그인" /></a>
							<a href="<%=MenuUtil.getMenuUrl(2) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_join.gif"/>" alt="회원가입" /></a>
						</c:otherwise>
					</c:choose>		
					<div class="search">
						<form id="fn_topTotalSearchForm" name="fn_topTotalSearchForm" method="get" action="../main/search.do">
							<input type="hidden" name="mId" id="mId" value="<%=Defines.CODE_MENU_TOTALSEARCH %>"/>
							<fieldset>
								<legend>통합검색</legend>
								<input type="text" name="fn_totalKey" id="fn_totalKey" class="inputTxt" value="" title="검색어를 입력하세요" />
								<input type="submit" class="btnSearch" id="fn_btn_fn_totalKey" value="검색" title="검색">
							</fieldset>
						</form>
					</div>		
				</div>
			</div>
			
			<div id="header">
				<h1><a href="<c:out value="${contextPath}${indexUrl}"/>"><img src="<c:out value="${contextPath}${imgPath}/layout/logo.gif"/>" alt="R.biz v1.0" /></a></h1>
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