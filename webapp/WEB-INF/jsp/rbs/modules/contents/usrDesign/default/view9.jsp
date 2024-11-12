<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
	<c:set var="workItemInfo" value="${moduleItem.work_9_item_info}"/>
	<div id="cms_board_article">
		<div id="fn_workViewWrap">
			<itui:objectView itemId="workContents" itemInfo="${workItemInfo}"/>
		</div>
	</div>
	<c:if test="${!empty BOTTOM_SOURCE}"><jsp:include page="${BOTTOM_SOURCE}" flush="false"/></c:if>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>