			<div id="lnbWrap">
				<h2>${crtMenu.menu_name2}</h2>
				<div class="infoWrap">
					<div class="info">
						<h4>메뉴</h4>
						<form id="fn_searchSVerForm" name="fn_searchSVerForm" method="get" action="<%=MenuUtil.getMenuUrl(10) %>">
							<input type="hidden" id="mId" name="mId" value="10"/>
							<fieldset>
								<legend class="blind">버전검색 폼</legend>
								<label class="tit blind" for="siteVerIdx">버전검색</label>
								<select id="siteVerIdx" name="siteVerIdx" class="select">
									<c:if test="${!empty usrSiteInfo}">
									<option value="menu">${usrSiteInfo.version}[<spring:message code="message.version.isservice"/>]</option>
									</c:if>
									<c:forEach var="verDt" items="${usrSiteVerList}">
									<option value="<c:out value="${verDt.OPTION_CODE}"/>"<c:if test="${elfn:toString(verDt.OPTION_CODE) == usrSiteVO.siteVerIdx}"> selected="selected"</c:if>><c:out value="${verDt.OPTION_NAME}"/></option>
									</c:forEach>
								</select>
								<button type="submit" class="btnSearch" id="fn_btn_search_sversion">선택</button>
							</fieldset>
						</form>
						<mnui:contMenuLnb ulId="fn_contMenuUL" ulClass="lnb" menuList="${usrSiteMenuList}" menus="${usrSiteMenus}"/>
					</div>
				</div>
				<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tree.js"/>"></script>
				<script type="text/javascript">
				$(function(){
					// 관리사이트 버전 변경
					$("#fn_searchSVerForm").submit(function(){
						if(!fn_checkSelected($('#siteVerIdx'), "버전")) return false;
						if(!confirm('<spring:message code="message.version.selectVersion.change.confirm"/>')) return false;
						return true;
					});
					
					// 사용자 메뉴 트리 출력
					fn_initTree("C", $("#fn_contMenuUL>li.root>ul"));
					$("#lnbWrap .info .lnb li .btn_tree").click(function(){
						fn_setTreeToggle($(this));
					});
				});
				</script>
			</div>
			<div id="content">
			<h3><c:choose><c:when test="${!empty usrCrtMenu}">${usrCrtMenu.menu_name}</c:when><c:otherwise>${crtMenu.menu_name}</c:otherwise></c:choose></h3>
			<c:if test="${!empty param.page_tit}"><h4><c:out value="${param.page_tit}"/></h4></c:if>