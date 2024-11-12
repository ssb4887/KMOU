<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_pollRespForm"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/response.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_poll_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>&pollIdx=<c:out value="${ingDt.POLL_IDX}"/>" target="submit_target">
		<div class="poll_box">
			<div class="ing_icon">진행중</div>
			<div class="wrap">
				<div class="poll_ing" id="fn_poll_respDiv">
					<%@ include file="responseCon.jsp"%>
				</div>
			</div>
		</div>
		<div class="btnCenter" id="wrap_poll_submit_btn">
			<c:if test="${settingInfo.use_tmpsaved == 1}">
			<button type="button" class="btnTypeT" id="fn_btn_tmp_join">임시저장</button>
			</c:if>
			<button type="submit" class="btnTypeA" id="fn_btn_join"><spring:message code="item.poll.join"/></button>
			<button type="button" class="fn_btn_cancel btnTypeB"><spring:message code="button.cancel"/></button>
			<c:if test="${(usePrivate && isPollResp || !usePrivate) && ingDt.ISRESULT == '1'}"><button type="button" class="btnTypeH" id="fn_btn_result" value="${URL_VIEW}&pollIdx=<c:out value="${ingDt.POLL_IDX}"/>"><spring:message code="title.poll.result.name"/></button></c:if>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>