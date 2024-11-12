<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<jsp:include page="${jspSiteIncPath}/popup_top.jsp" flush="false">
	<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
</jsp:include>
	<c:set var="layoutSize" value="width:${dt.POP_WIDTH}px;"/>
	<c:set var="cssStyle" value="${layoutSize}"/>
	<c:if test="${!empty dt.PBG_SAVED_NAME}">
		<c:set var="cssStyle" value="${cssStyle}background:url(image.do?mId=1&fnIdx=${crtMenu.fn_idx}&id=${elfn:imgNSeedEncrypt(dt.PBG_SAVED_NAME)});"/>
	</c:if>
	
	<div id="cms_board_article"<c:if test="${!empty cssStyle}"> style="${cssStyle}"</c:if> class="se2_outputarea">
		${elfn:removeScriptTag(dt.CONTENTS)}
	</div>
<jsp:include page = "${jspSiteIncPath}/popup_bottom.jsp" flush = "false"/>