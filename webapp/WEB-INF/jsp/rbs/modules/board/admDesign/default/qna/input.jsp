<%@ include file="../../../../../include/commonTop.jsp"%>
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
		<c:set var="exceptIdStr">file</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<c:if test="${((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and param.mode eq 'r') or dt.RE_LEVEL > 1}">
			<c:set var="pntFileName" value="pntList.jsp"/>
			<c:if test="${settingInfo.use_reply != '1' && settingInfo.use_qna == '1'}">
			<c:set var="pntFileName" value="pntQna.jsp"/>
			</c:if>
			<jsp:include page="../../common/${pntFileName}" flush="false">
				<jsp:param name="summary" value="${summary}"/>
				<jsp:param name="hnoMargin" value="${true}"/>
			</jsp:include>
			<h4 class="titTypeC">답변글</h4>
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
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="inputTypeFlag" value="${submitType}"/>
				<c:if test="${submitType == 'reply'}">
					<c:choose>
						<c:when test="${param.m == 'm'}"><c:set var="inputTypeFlag" value="modify"/></c:when>
						<c:otherwise><c:set var="inputTypeFlag" value="write"/></c:otherwise>
					</c:choose>
				</c:if>
				<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order" inputTypeName="${inputTypeFlag}"  exceptIds="${exceptIds}"/>
				<tr>
					<th scope="row" class="text-center align-middle">첨부파일</th>
					<td class="writ_att">
						<span class="d-flex flex-row gap-2">
							<itui:objectMultiFileError itemId="file" />
						</span>
					</td>
				</tr>
				<%-- 비회원글쓰기권한 : 비밀번호 등록  --%>
				<%@ include file="../../common/inputPwd.jsp"%>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>