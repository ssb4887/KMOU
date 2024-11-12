			<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
			<c:set var="cdtAuth" value="${elfn:isAuth('CDT')}"/>
			<div class="left">
				<c:if test="${mngAuth}">
				<a href="<c:out value="${URL_RESTOREPROC}"/>" title="복원" class="btnTD fn_btn_restore">복원</a>
				</c:if>
				<c:if test="${cdtAuth}">
				<a href="<c:out value="${URL_CDELETEPROC}"/>" title="완전삭제" class="btnTDL fn_btn_cdelete">완전삭제</a>
				</c:if>
			</div>
			<div class="right">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
				<select id="lunit" name="lunit" class="select" title="목록수">
				<c:forEach var="listUnit" begin="${paginationInfo.unitBeginCount}" end="${paginationInfo.unitEndCount}" step="${paginationInfo.unitStep}">
					<option value="${listUnit}"<c:if test="${listUnit == paginationInfo.recordCountPerPage}"> selected="selected"</c:if>>${listUnit}개씩 조회</option>
				</c:forEach>
				</select>
				<button data-url="<c:out value="${URL_LIMIT_LIST}"/>" title="적용" class="btnTypeF fn_btn_lunit">적용</button>
			</div>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/calendar.js"/>"></script>
<script type="text/javascript">
$(function(){
	<c:if test="${!empty window_width || !empty window_height}">
	fn_window.resizeTo(<c:choose><c:when test="${!empty window_width}">${window_width}</c:when><c:when test="${!empty window_height}">1100</c:when></c:choose><c:if test="${!empty window_height}">, ${window_height}</c:if>);
	</c:if>
	// 전체 선택/해제 change
	$("#selectAll").change(function(){
		try {
			$("input[name='select']").prop("checked", $(this).prop("checked"));
		}catch(e){}
	});

	<c:if test="${mngAuth}">
	// 복원
	$(".fn_btn_restore").click(function(){
		try {
			fn_listRestoreFormSubmit("<c:out value="${listFormId}"/>", $(this).attr("href"));
		}catch(e){}
		return false;
	});
	</c:if>

	<c:if test="${cdtAuth}">
	// 삭제
	$(".fn_btn_cdelete").click(function(){
		try {
			fn_listDeleteFormSubmit("<c:out value="${listFormId}"/>", $(this).attr("href"));
		}catch(e){}
		return false;
	});
	</c:if>
	
	// 목록수 적용
	$(".fn_btn_lunit").click(function(){
		location.href=$(this).attr("data-url") + "&lunit=" + $("#lunit option:selected").val();
	});
	
});

<c:if test="${mngAuth}">
//복원
function fn_listRestoreFormSubmit(theFormId, theAction){
	try {
		if(!fn_isValFill(theFormId) || !fn_isValFill(theAction)) return false;
		// 선택
		if(!fn_checkElementChecked("select", "복원")) return false;
		// 복원여부
		var varConfirm = confirm("<spring:message code="message${deleteModuleFlag}.select.all.restore.confirm"/>");
		if(!varConfirm) return false;

		$("#" + theFormId).attr("action", theAction);
		$("#" + theFormId).submit();
	}catch(e){}
	
	return true;
}
</c:if>

<c:if test="${cdtAuth}">
//삭제
function fn_listDeleteFormSubmit(theFormId, theAction){
	try {
		if(!fn_isValFill(theFormId) || !fn_isValFill(theAction)) return false;
		// 선택
		if(!fn_checkElementChecked("select", "삭제")) return false;
		// 삭제여부
		var varConfirm = confirm("<spring:message code="message${deleteModuleFlag}.select.all.cdelete.confirm"/>");
		if(!varConfirm) return false;
		
		$("#" + theFormId).attr("action", theAction);
		$("#" + theFormId).submit();
	}catch(e){}
	
	return true;
}
</c:if>
</script>