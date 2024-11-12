<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="chkAuthName" value="WRT"/>
<c:if test="${((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and param.mode eq 'r') or dt.RE_LEVEL > 1}">
	<c:set var="chkAuthName" value="RWT"/>
</c:if>
<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage(chkAuthName)}"/>

<c:set var="JAVASCRIPT_PAGE" value="${param.javascript_page}"/>
<c:if test="${!empty JAVASCRIPT_PAGE}">
	<jsp:include page="${JAVASCRIPT_PAGE}" flush="false"/>
</c:if> 
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${contextPath}${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="fn_boardInputForm"/>
		<jsp:param name="isNoMemberAuthPage" value="${isNoMemberAuthPage}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<script src="${contextPath}${jsAssetPath}/sub.js"></script>
 <!--content-->
		<div class="sub_background commu_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center text-white">Q&amp;A</h3>
				<p class="sub_title_script">Q&amp;A을 확인할 수 있습니다.</p>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-4">
			<!-- <h2 class="qna_title text-center py-3 px-2">2024-1학기 고졸 후학습자 장학금(희망사다리2유형) 신규장학생 신청 안내</h2> -->
			<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>"  enctype="multipart/form-data">
			<input type="hidden" name="name" value="${loginVO.memberName}">
			<table class="table qna_write">
				<caption class="blind">qna 글작성 및 수정테이블</caption>
				<colgroup>
					<col width="20%" />
					<col width="80%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="text-center align-middle">제목</th>
						<td class="writ_tit">
<!-- 							<input type="text" id="input01-01" name="input01-01" class="form-control" placeholder="제목을 입력하세요">      -->
							<itui:objectText itemId="subject" objClass="form-control"/>                               
						</td>
					</tr>
					<%-- <tr>
						<th scope="row" class="text-center align-middle">공개 여부</th>
						<td class="writ_tit">
							<itui:objectRadio itemId="secret"/>                                   
						</td>
					</tr> --%>
					<tr>
						<th scope="row" class="text-center align-middle">첨부파일</th>
						<td class="writ_att">
							<span class="d-flex flex-row gap-2">
								<itui:objectMultiFileError itemId="file" />
							</span>
						</td>
					</tr>
					<tr>
						<th scope="row" class="text-center align-middle">내용</th>
						<td class="writ_con">
								<textarea name="contents" id="input01-03" class="form-control" rows="15"><c:if test="${submitType eq 'modify'}"><c:out value="${dt.CONTENTS}"/></c:if></textarea>   
						</td>
					</tr>
				</tbody>
			</table>
			<div class="noti-btn-line">
				<button type="submit" class="btn_st1">저장</button>
				<button type="button" onclick="javascript:window.history.back();" class="btn_st">작성취소</button>
			</div>
			</form>
		</section>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>
