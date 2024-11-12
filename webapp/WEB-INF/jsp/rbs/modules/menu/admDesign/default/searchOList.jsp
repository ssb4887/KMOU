<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<c:set var="searchFormId" value="fn_menuSearchForm"/>
<c:set var="listFormId" value="fn_menuListForm"/>
<jsp:include page="${jspSiteIncPath}/dialog_top.jsp" flush="false">
	<jsp:param name="javascript_page" value="${moduleJspRPath}/searchOList.jsp"/>
	<jsp:param name="searchFormId" value="${searchFormId}"/>
	<jsp:param name="listFormId" value="${listFormId}"/>
</jsp:include>
	<div id="cms_board_article">
		<!-- button -->
		<div class="btnTopFull">
			<div class="left">
			<dl id="fn_menuPath">
				<dt>메뉴위치</dt>
				<dd></dd>
			</dl>
			</div>
			<div class="right">
				<button type="button" title="선택" class="btnTW fn_btn_select">선택</button>
			</div>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="submit_target">
			<input type="hidden" id="menu_idx" name="menu_idx" value=""/>
			<input type="hidden" id="menu_name" name="menu_idx" value=""/>
			<input type="hidden" id="mdId" name="mdId" value=""/>
			<input type="hidden" id="sfIdx" name="sfIdx" value=""/>
			<div id="lnbWrap">
				<div class="infoWrap">
					<div class="info">
						<h4>메뉴</h4>
						<mnui:menuSearchLnb ulId="fn_contMenuUL" ulClass="lnb" menuList="${usrSiteMenuList}" menus="${usrSiteMenus}"/>
					</div>
				</div>
				<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tree.js"/>"></script>
				<script type="text/javascript">
				$(function(){
					// 사용자 메뉴 트리 출력
					fn_initTree("C", $("#fn_contMenuUL>li.root>ul"));
					$("#lnbWrap .info .lnb li .btn_tree").click(function(){
						fn_setTreeToggle($(this));
					});
				});
				</script>
			</div>
		</form>
	</div>
<jsp:include page="${jspSiteIncPath}/dialog_bottom.jsp" flush="false"/>