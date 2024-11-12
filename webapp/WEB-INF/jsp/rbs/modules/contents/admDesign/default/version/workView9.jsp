<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.view_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/workView9.jsp"/>
	</jsp:include>
</c:if>
	<c:set var="workItemInfo" value="${moduleItem.work_9_item_info}"/>
	<div id="cms_board_article">
		<%@ include file="viewTop.jsp"%>
		<c:set var="useContentsType" value="${elfn:useSettingtem(contSettingInfo, contItemInfo.items['contType'])}"/>
		<c:set var="workContWidth" value="100%"/>
		<div id="fn_workViewWrap" class="se2_outputarea">
			<itui:objectView itemId="workContents" itemInfo="${workItemInfo}"/>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>