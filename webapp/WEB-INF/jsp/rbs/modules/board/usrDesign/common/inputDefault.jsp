<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="chkAuthName" value="WRT"/>
<c:if test="${((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and param.mode eq 'r') or dt.RE_LEVEL > 1}">
	<c:set var="chkAuthName" value="RWT"/>
</c:if>
<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage(chkAuthName)}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="isNoMemberAuthPage" value="${isNoMemberAuthPage}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="exceptIdStr">notice</c:set>
				<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
				<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/>
				<%-- 비회원글쓰기권한 : 비밀번호 등록  --%>
				<%@ include file="inputPwd.jsp"%>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>