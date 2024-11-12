<!-- header -->
	<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
<div id="headerWrap">
		<div id="util">
			<div class="utilWrap">
				<div class="utilBtn">
					<c:choose>
						<c:when test="${elfn:isLoginAuth()}">
							<c:out value="${loginVO.memberNameOrg}"/>님 반갑습니다!
							<a href="<%=MenuUtil.getMenuUrl(4) %>">로그아웃</a>
							<c:if test="${elfn:isLogin()}">
							<a href="<%=MenuUtil.getMenuUrl(5) %>">내정보수정</a>
							</c:if>
						</c:when>
						<c:otherwise>
							<a href="<%=MenuUtil.getMenuUrl(3) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_login.gif"/>" alt="로그인" /></a>
							<a href="<%=MenuUtil.getMenuUrl(2) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_join.gif"/>" alt="회원가입" /></a>
						</c:otherwise>
					</c:choose>				
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