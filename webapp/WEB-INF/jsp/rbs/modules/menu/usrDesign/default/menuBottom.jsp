<c:if test="${crtMenu.use_dmng == 1 || crtMenu.use_poll == 1}">
	<c:set var="menuDesign" value="default"/>
	<%@ include file="../designPath.jsp" %>
	<div class="cms_menu_info">
</c:if>
<c:if test="${crtMenu.use_dmng == 1}">
	<!-- 담당자정보 -->
	<%@ include file="mngView.jsp" %>
</c:if>
<c:if test="${crtMenu.use_poll == 1}">
	<!-- 메뉴 만족도 -->
	<c:set var="menuPointInputPath" value="/${crtSiteId}/menu/pointInput.do?mId=${param.mId}&fnIdx=1"/>
	<c:import url="${menuPointInputPath}" charEncoding="UTF-8"/>
	
	<%-- <%@ include file="point/pointInput.jsp"%> --%>
</c:if>
<c:if test="${crtMenu.use_dmng == 1 || crtMenu.use_poll == 1}">
	</div>
</c:if>