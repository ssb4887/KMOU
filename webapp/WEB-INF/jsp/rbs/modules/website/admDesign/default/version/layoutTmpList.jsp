<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.list_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/layoutTmpList.jsp"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<ul class="fn_designList">
	<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
	<c:forEach var="designName" items="${list}" varStatus="i">
		<li>
			<p class="tit"><c:out value="${designName}"/></p>
			<p class="img"><img src="<c:out value="${contextPath}${listImgPath}"/>/${elfn:encode(designName, 'UTF-8')}/dimages/main.jpg" alt="<c:out value="${designName}"/>" onerror="fn_errorImg(this);"/></p>
			<p>
				<input type="button" class="fn_btn_view btnTypeG" data-url="<c:out value="${URL_VIEW}"/>&${listIdxName}=${elfn:encode(designName, 'UTF-8')}" value="상세보기"/>
				<input type="button" class="fn_btn_select btnTypeE" data-value="<c:out value="${designName}"/>" value="선택"/>
			</p>
		</li>
	</c:forEach>
	</ul>
	
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>