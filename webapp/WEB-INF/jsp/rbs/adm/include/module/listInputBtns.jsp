			<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
			<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
			<c:if test="${mngAuth}">
			<div class="left">
			<c:if test="${isMajorInfo eq 'N'}">
				<a href="<c:out value="${URL_DELETEPROC}"/>" title="삭제" class="btnTD fn_btn_delete">삭제</a>
				<a href="<c:out value="${URL_DELETE_LIST}"/>" title="휴지통" class="btnTDL fn_btn_deleteList">휴지통</a>
			</c:if>
			</div>
			</c:if>
			<div class="right">
			<c:if test="${isMajorInfo eq 'N'}">
				<c:choose>
				<c:when test="${settingInfo.use_paging == '1'}">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
				<select id="lunit" name="lunit" class="select" title="목록수">
				<c:forEach var="listUnit" begin="${paginationInfo.unitBeginCount}" end="${paginationInfo.unitEndCount}" step="${paginationInfo.unitStep}">
					<option value="${listUnit}"<c:if test="${listUnit == paginationInfo.recordCountPerPage}"> selected="selected"</c:if>>${listUnit}개씩 조회</option>
				</c:forEach>
				</select>
				<button data-url="<c:out value="${URL_LIMIT_LIST}"/>" title="적용" class="btnTypeF fn_btn_lunit">적용</button>
				</c:when>
				<c:otherwise>
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
				</c:otherwise>
				</c:choose>				
				<c:if test="${wrtAuth}">
				<c:if test="${!empty URL_COPYINPUT}">
				<a href="<c:out value="${URL_COPYINPUT}"/>" title="복제등록" class="btnTEW fn_btn_copywrite">복제등록</a>
				</c:if>
				</c:if>
			</c:if>
			</div>
<script type="text/javascript">
$(function(){	
	//fn_window.resizeTo();
	// dialog
	fn_dialog.init("fn_inputlist");
	
	// 전체 선택/해제 change
	$("#selectAll").change(function(){
		try {
			$("input[name='select']").prop("checked", $(this).prop("checked"));
			if(fn_setAllSelectObjs) fn_setAllSelectObjs(this);
		}catch(e){}
	});

	<c:if test="${mngAuth}">
	// 휴지통
	$(".fn_btn_deleteList").click(function(){
		try {
			<c:choose>
				<c:when test="${!empty deleteList_dialog_title}">
				var varTitle = "<c:out value="${deleteList_dialog_title}"/>";
				</c:when>
				<c:otherwise>
				var varTitle = "<c:out value="${settingInfo.deleteList_title}"/>";
				</c:otherwise>
			</c:choose>
			<c:if test="${!empty deleteList_dialog_addTitle}">
			varTitle += " (<c:out value="${deleteList_dialog_addTitle}"/>)";
			</c:if>
			fn_dialog.open(varTitle, $(this).attr("href"), 0, 0, "fn_inputDeleteList"/*, 850, 700*/);
		}catch(e){/*alert(e);*/}
		return false;
	});
	
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			fn_listDeleteFormSubmit("<c:out value="${listFormId}"/>", $(this).attr("href"));
		}catch(e){}
		return false;
	});
	</c:if>

	<c:if test="${wrtAuth}">
	// 등록/수정
	$(".fn_btn_write<c:if test="${mngAuth}">, .fn_btn_modify</c:if>").click(function(){
		try {
			<c:if test="${empty input_target}">
			<c:set var="input_target" value="fn_ifrm_input"/>
			</c:if>
			<c:if test="${!empty input_iframe_height}">
			$("#${input_target}").css("height", "${input_iframe_height}px");
			</c:if>
			window.open($(this).attr("href"), "${input_target}");
		}catch(e){}
		return false;
	});
	<c:if test="${!empty URL_COPYINPUT}">
	$(".fn_btn_copywrite").click(function(){
		try {
			<c:if test="${empty input_target}">
			<c:set var="input_target" value="fn_ifrm_input"/>
			</c:if>
			<c:if test="${!empty copyInput_iframe_height}">
			$("#${input_target}").css("height", "${copyInput_iframe_height}px");
			</c:if>
			window.open($(this).attr("href"), "${input_target}");
		}catch(e){}
		return false;
	});
	</c:if>
	</c:if>
	
	// 목록수 적용
	$(".fn_btn_lunit").click(function(){
		location.href=$(this).attr("data-url") + "&lunit=" + $("#lunit option:selected").val();
	});
	
});

<c:if test="${mngAuth}">
//삭제
function fn_listDeleteFormSubmit(theFormId, theAction){
	try {
		if(!fn_isValFill(theFormId) || !fn_isValFill(theAction)) return false;
		// 선택
		if(!fn_checkElementChecked("select", "삭제")) return false;
		// 삭제여부
		var varConfirm = confirm("<spring:message code="message.select.all.delete.confirm"/>");
		if(!varConfirm) return false;
		
		$("#" + theFormId).attr("action", theAction);
		
		$("input[name='select']").prop("disabled", false);
		$("#" + theFormId).submit();
	}catch(e){}
	
	return true;
}
</c:if>
</script>