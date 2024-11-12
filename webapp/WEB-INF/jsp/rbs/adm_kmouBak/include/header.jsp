<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
	<!-- headerWrap -->
	<div id="headerWrap">
		<div id="header">
			<h1><a href="<c:out value="${contextPath}${indexUrl}"/>"><img src="<c:out value="${contextPath}${imgPath}/layout/rbis_bi.png"/>" alt="R.bis [RBS J]gCMS v4.0 standard edition" /></a></h1>
			<div class="utilWrap">
				<div class="utilSearch">
				<form id="fn_searchTWebsiteForm" name="fn_searchTWebsiteForm" method="post" action="<%=MenuUtil.getMenuUrl(30) %>" target="submit_target">
					<input type="hidden" id="selToUrl" name="selToUrl"/>
					<fieldset>
						<legend class="blind">사이트검색 폼</legend>
						<label class="tit blind" for="selSiteId">사이트선택</label>
						<select id="selSiteId" name="selSiteId" class="select">
							<c:forEach var="websiteDt" items="${usrSiteList}">
							<option value="<c:out value="${websiteDt.OPTION_CODE}"/>"<c:if test="${websiteDt.OPTION_CODE == usrSiteVO.siteId}"> selected="selected"</c:if>><c:out value="${websiteDt.OPTION_NAME}"/></option>
							</c:forEach>
						</select>
						<button type="submit" class="btnSearch" id="fn_btn_search_website">선택</button>
					</fieldset>
				</form>
				</div>
				<div class="utilBtn">
					<ul>
						<li><a href="<%=MenuUtil.getMenuUrl(4) %>">로그아웃</a></li>
						<li><a href="<%=MenuUtil.getMenuUrl(5) %>" id="fn_btn_myInfo">내정보수정</a></li>
						<li class="end"><a href="#" class="btnAllMenu">전체보기</a></li>
					</ul>
				</div>
			</div>
			<script type="text/javascript">
			$(function(){
				
				// 내정보수정
				$("#fn_btn_myInfo").click(function(){
					try {
						var varTitle = "내정보수정";
						fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1");
					}catch(e){}
					return false;
				});
				
				// 사용자사이트 선택
				$("#fn_searchTWebsiteForm").submit(function(){
					var varConfirm = confirm("<spring:message code="message.website.selectSite.change.confirm"/>");
					if(!varConfirm) {
						$(this).reset();
						return false;
					}
					return true;
				});
			});
			</script>
			<!-- gnb -->
			<div id="gnbWrap">
				<h2 class="blind">주메뉴</h2>
				<mnui:gnb ulId="gnb" gid="${crtMenu.menu_idx2}" sid="${crtMenu.menu_idx3}" menuList="${siteMenuList}" menus="${siteMenus}" />
			</div>
		</div>
		<div class="allMenu">
			<h2 class="blind">전체메뉴</h2>
			<mnui:sitemap menuList="${siteMenuList}" menus="${siteMenus}" />
			<div class="btnClose"><a href="#">닫기</a></div>
		</div>
	</div>