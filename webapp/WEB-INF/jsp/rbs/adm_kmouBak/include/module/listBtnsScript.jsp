<script type="text/javascript">
$(function(){	
	// dialog
	fn_dialog.init("fn_list");
	
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
			fn_dialog.open(varTitle, $(this).attr("href"), 0, 0, "fn_deleteList");
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
	$(".fn_btn_write_open<c:if test="${mngAuth}">, .fn_btn_modify_open</c:if>").click(function(){
		try {
			<c:choose>
				<c:when test="${!empty input_dialog_title}">
				var varTitle = "<c:out value="${input_dialog_title}"/>";
				</c:when>
				<c:otherwise>
				var varTitle = "<c:out value="${settingInfo.input_title}"/>";
				<c:if test="${mngAuth}">
				if($(this).hasClass('fn_btn_modify_open')) varTitle += " 수정";
				else </c:if>varTitle += " 등록";
				</c:otherwise>
			</c:choose>
			<c:if test="${!empty input_dialog_addTitle}">
			varTitle += " (<c:out value="${input_dialog_addTitle}"/>)";
			</c:if>
			<c:set var="dialogWidth" value="0"/>
			<c:if test="${!empty input_dialog_width}"><c:set var="dialogWidth" value="${input_dialog_width}"/></c:if>
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", ${dialogWidth}<c:if test="${!empty input_dialog_height}">, ${input_dialog_height}</c:if>);
		}catch(e){}
		return false;
	});
	</c:if>

	<c:if test="${mngAuth}">
	<c:if test="${!empty URL_EXCELINPUT}">
	// 엑셀등록
	$(".fn_btn_excelwrite").click(function(){
		try {
			var varTitle = "<c:out value="${settingInfo.input_title}"/> 엑셀등록";
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0, 300);
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