<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>

	// 형식 click
	$("input[name='popType']").click(function(){
		fn_setPopTypeItems();
	});
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){alert(e); return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
});

function fn_setPopTypeItems(){
	var varPopTypeVal = $("input[name='popType']:checked").val();
	if(varPopTypeVal == "1") {
		// 페이지링크
		$(".fn_tr_popType1").show();
		$(".fn_popTypeWrap").hide();
		$(".fn_popTypeWrap").find("input, select, textarea, file").prop("disabled", true);
	}
	else {
		// 팝업
		$(".fn_tr_popType1").hide();
		$(".fn_popTypeWrap").show();
		$(".fn_popTypeWrap").find("input, select, textarea, file").prop("disabled", false);
	}
}
function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>
	
	fn_setPopTypeItems();
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>
	fn_createMaskLayer();
	return true;
}
</script>