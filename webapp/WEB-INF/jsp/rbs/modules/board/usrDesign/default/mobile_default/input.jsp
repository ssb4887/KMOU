<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/mobile/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:if test="${((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and param.mode eq 'r') or dt.RE_LEVEL > 1}">
			<jsp:include page="../../common/pntList.jsp" flush="false">
				<jsp:param name="summary" value="${summary}"/>
				<jsp:param name="hnoMargin" value="${true}"/>
			</jsp:include>
		</c:if>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" name="pntIdx" id="pntIdx" value="<c:out value="${dt.PNT_IDX}"/>"/>
		<input type="hidden" name="reStep" id="reStep" value="<c:out value="${dt.RE_STEP}"/>"/>
		<input type="hidden" name="reLevel" id="reLevel" value="<c:out value="${dt.RE_LEVEL}"/>"/>
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:70px;" />
			<col />
			</colgroup>
			<tbody>
				<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order"/>
			</tbody>
		</table>
		<div class="btnCenter" data-role="controlgroup" data-type="horizontal">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장" data-role="button" data-ajax="false"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel" data-role="button" rel="external">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>