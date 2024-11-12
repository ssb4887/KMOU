<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/common.js"/>"></script>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>

	fn_boardInputReset();
	
	try {$(document).on('keydown', '#<c:out value="${param.inputFormId}"/> input[name="itemPoints"]', function(event){return fn_onlyNumKD(event);});} catch(e) {}
	
	<c:if test="${respCnt > 0}">
	$("#isquestype").prop("disabled", true);
	$("#ispollitem").prop("disabled", true);
	</c:if>
	<c:if test="${empty param.mode || quesCnt <= 0}">
	$("input[name='isstop']:gt(0)").prop("disabled", true);
	</c:if>
	
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
function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>
	
	var varChk = true;
	var varItemNames = $('input.itemContents');
	var varItemNameSize = varItemNames.size();
	var varItemVal = '';
	var varItemItemNames = [['itemContents', '항목내용', 1, 100], ['itemPoints', '점수', 1, 2]];
	
	if(varItemNameSize > 1) {
		// 객관식, 주관식 다중 항목 체크
		varChk = fn_checkFillRequired(varItemNames, varItemItemNames);
		if(!varChk) return false;
	}
	
	// 항목 name setting
	$.each(varItemNames, function(i){
		var varVal = i + 1;
		if(i > 0) varItemVal += ',';
		varItemVal += varVal;
		$.each(varItemItemNames, function(j){
			var varItem = this;
			$('input.' + varItem[0] + ':eq(' + i + ')').attr('name', varItem[0] + varVal);
		});
	});
	
	$('input[name="itemIdxs"]').val(varItemVal);

	$("#isquestype").prop("disabled", false);
	$("#ispollitem").prop("disabled", false);

	fn_createMaskLayer();
	return true;
}
</script>