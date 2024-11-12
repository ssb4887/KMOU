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
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<main id="content-wrap">            
	<div class="content-inner">	
		<%-- <%@ include file="../../../../../usr/web/include/commuTabMenu.jsp" %> --%>	
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" name="pntIdx" id="pntIdx" value="<c:out value="${dt.PNT_IDX}"/>"/>
		<input type="hidden" name="reStep" id="reStep" value="<c:out value="${dt.RE_STEP}"/>"/>
		<input type="hidden" name="reLevel" id="reLevel" value="<c:out value="${dt.RE_LEVEL}"/>"/>
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/> 입력표</c:set>				
			<div class="table-wrap2 mt50">
				<table class="table-write">
					<caption>Q&amp;A 작성 테이블로 제목, 작성자, 내용등의 항목을 나타내는 표입니다.
					</caption>
					<colgroup>
						<col style="width:10%">
						<col>
					</colgroup>
					<tbody>
						<c:set var="exceptIdStr">notice</c:set>
						<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
<%-- 							<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/> --%>
						<tr>
							<th scope="row"><label for="input01-01">제목</label></th>
							<td>
								<itui:objectText itemId="subject" objClass="input-type01" />                                       
<!-- 							    <input type="text" id="input01-01" name="input01-01" class="input-type01" placeholder="제목 입력">                                     -->
							</td>								
						</tr>
		                <tr>
							<th scope="row"><label for="input01-02">작성자</label></th>
							<td>
								<itui:objectText itemId="name" objClass="input-type01" />                                       
<!-- 							    <input type="text" id="input01-02" name="input01-02" class="input-type01" placeholder="작성자 입력">                                     -->
							</td>								
						</tr>
						<tr>
							<th scope="row"><label for="input01-03">내용</label></th>
							<td>    
								<itui:objectTextarea itemId="contents" objClass="input-type01" objStyle="height: 200px;"/> 
<!-- 								<textarea name="input01-03" id="input01-03" class="input-type01" style="height: 200px;" placeholder="문의내용 입력"></textarea>                                                                       -->
							</td>								
						</tr>
					</tbody>
				</table>
			</div>
			<div class="btn-line mt30">
				<a href="#" title="취소" class="btnTypeB fn_btn_cancel btn-large btn-secondary" style="line-height:52px;">취소</a>
				<input type="submit" class="btnTypeA fn_btn_submit btn-large btn-primary" value="등록" title="등록"/>
			</div>
		</form>
    </div>
    <%@ include file="../../common/password.jsp"%>           
</main>
<!-- 	<div id="cms_board_article"> -->
<%-- 		<c:if test="${((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and param.mode eq 'r') or dt.RE_LEVEL > 1}"> --%>
<%-- 			<jsp:include page="../../common/pntList.jsp" flush="false"> --%>
<%-- 				<jsp:param name="summary" value="${summary}"/> --%>
<%-- 				<jsp:param name="hnoMargin" value="${true}"/> --%>
<%-- 			</jsp:include> --%>
<%-- 		</c:if> --%>
<%-- 		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data"> --%>
<%-- 		<input type="hidden" name="pntIdx" id="pntIdx" value="<c:out value="${dt.PNT_IDX}"/>"/> --%>
<%-- 		<input type="hidden" name="reStep" id="reStep" value="<c:out value="${dt.RE_STEP}"/>"/> --%>
<%-- 		<input type="hidden" name="reLevel" id="reLevel" value="<c:out value="${dt.RE_LEVEL}"/>"/> --%>
<%-- 		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/> 입력표</c:set> --%>
<%-- 		<table class="tbWriteA" summary="${summary}"> --%>
<!-- 			<caption> -->
<!-- 			글쓰기 서식 -->
<!-- 			</caption> -->
<!-- 			<colgroup> -->
<!-- 			<col style="width:120px;" /> -->
<!-- 			<col /> -->
<!-- 			</colgroup> -->
<!-- 			<tbody> -->
<%-- 				<c:set var="exceptIdStr">notice</c:set> --%>
<%-- 				<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/> --%>
<%-- 				<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/> --%>
<%-- 				비회원글쓰기권한 : 비밀번호 등록  --%>
<%-- 				<%@ include file="../../common/inputPwd.jsp"%> --%>
<!-- 			</tbody> -->
<!-- 		</table> -->
<!-- 		<div class="btnCenter"> -->
<!-- 			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/> -->
<!-- 			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a> -->
<!-- 		</div> -->
<!-- 		</form> -->
		<%-- 비회원글쓰기/댓글쓰기권한 : 비밀번호 확인  --%>
<%-- 		<%@ include file="../../common/password.jsp"%> --%>
<!-- 	</div> -->
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>