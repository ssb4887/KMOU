<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<spring:message var="useLayoutTmp" code="Globals.layoutTmp.use" text=""/>
<c:if test="${useLayoutTmp == '1'}">
	<spring:message var="layoutItemFlag" code="Globals.layoutTmp.item.flag" text=""/>
</c:if>
<c:set var="itemOrderName" value="${layoutItemFlag}${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	// 레이아웃 템플릿 삭제
	$("#fn_btn_del_${itemId}").click(function(){
		fn_initLayoutTmpRelItems(true);
	});
	</c:if>
	<c:set var="localPathItemName" value="${elfn:getItemName(itemInfo.items['local_path'])}"/>
	<c:set var="itemId" value="include_top"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	// 상단인클루드 경로 기본값 넣기
	$('#fn_btn_${itemId}').click(function(){
		if(!fn_checkFill($('#local_path'), "<c:out value="${localPathItemName}"/>")) return false;
		
		var varLPVal = $('#local_path').val();
		$('#${itemId}').val(varLPVal + "<spring:message code="Globals.include.default.top.path"/>");
	});
	</c:if>
	<c:set var="itemId" value="include_bottom"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	// 하단인클루드 경로 기본값 넣기
	$('#fn_btn_${itemId}').click(function(){
		if(!fn_checkFill($('#local_path'), "<c:out value="${localPathItemName}"/>")) return false;
		
		var varLPVal = $('#local_path').val();
		$('#${itemId}').val(varLPVal + "<spring:message code="Globals.include.default.bottom.path"/>");
	});
	</c:if>
	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	//레이아웃 템플릿 검색
	function fn_layoutTmpListSearchForm() {
		/*try {
			if(!fn_checkFill($("#local_path"), $("label[for='local_path']").text())) return false;
		}catch(e){}*/
		fn_dialog.init("fn_${itemId}_search");
		var varItemId = "${itemId}";
		try {
			var varTitle = "";
			fn_dialog.open(varTitle, "${URL_LAYOUTTMPLIST}&mdl=1&itemId=" + varItemId +"&dl=1");
		}catch(e){}
		return false;
	}
	</c:if>

	<c:set var="itemId" value="layout_theme"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	//레이아웃 테마 검색
	function fn_layoutThemeListSearchForm() {
		fn_dialog.init("fn_${itemId}_search");
		var varItemId = "${itemId}";
		try {
			var varTitle = "";
			fn_dialog.open(varTitle, "${URL_LAYOUTTHEMELIST}&mdl=1&itemId=" + varItemId +"&dl=1");
		}catch(e){}
		return false;
	}
	</c:if>
	
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			location.href="${elfn:replaceScriptLink(URL_INPUT)}";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
});
<c:set var="itemId" value="layout_tmp"/>
<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
//레이아웃 템플릿 검색
function fn_setLayoutTmpForm(theVal){
	$("#${itemId}Name").val(theVal);
	$("#${itemId}").val(theVal);
	
	$("#local_path").val("/<c:out value="${usrSiteVO.siteId}"/>");
	$("#fn_btn_include_top").click();
	$("#fn_btn_include_bottom").click();
	$("#local_path").prop("readonly", true);
	$("#include_top").prop("readonly", true);
	$("#include_bottom").prop("readonly", true);
	$("#layout_theme").parent().find("input, button").prop("disabled", false);
	$("#logo").parent().parent().find("input, button").prop("disabled", false);
	$("label[for='layout_themeName'], label[for='logo']").addClass("required");
	$("#layout_themeName").prop("required", true);
}

//레이아웃 템플릿 연관항목 초기화 : 레이아웃 템플릿 사용하지 않는 경우
function fn_initLayoutTmpRelItems(theDelFlag) {
	if(theDelFlag || !fn_isFill($("#layout_tmp"))) {
		// 템플릿 삭제
		$("#fn_btn_del_layout_theme").click();
		$("#logo").val("");
		$("#logo_text").val("");
		$("#fn_btn_del_logo").click();
		$("#local_path").prop("readonly", false);
		$("#include_top").prop("readonly", false);
		$("#include_bottom").prop("readonly", false);
		$("#layout_theme").parent().find("input, button").prop("disabled", true);
		$("#logo").parent().parent().find("input, button").prop("disabled", true);
		$("label[for='layout_themeName'], label[for='logo']").removeClass("required");
		$("#layout_themeName").prop("required", false);
	} else {
		$("#local_path").prop("readonly", true);
		$("#include_top").prop("readonly", true);
		$("#include_bottom").prop("readonly", true);
		$("#layout_theme").parent().find("input, button").prop("disabled", false);
		$("#logo").parent().parent().find("input, button").prop("disabled", false);
		$("label[for='layout_themeName'], label[for='logo']").addClass("required");
		$("#layout_themeName").prop("required", true);
	}
}
</c:if>

<c:set var="itemId" value="layout_theme"/>
<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
//레이아웃 테마 검색
function fn_setLayoutThemeForm(theVal){
	$("#${itemId}Name").val(theVal);
	$("#${itemId}").val(theVal);
}
</c:if>

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	fn_initLayoutTmpRelItems();
	</c:if>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>

	<c:set var="itemId" value="layout_theme"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	try{
		if($("#${itemId}Name").prop("required") && !fn_checkFill($("#${itemId}Name"), $("label[for='${itemId}Name']").text())) return false;
	}catch(e){}
	</c:if>

	<c:set var="itemId" value="logo"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
	try{
		var varLogText = $("label[for='${itemId}']").text();
		if($("label[for='${itemId}']").hasClass("required") && $("#${itemId}_saved").is("input") && (!fn_isFill($("#${itemId}_saved")) || fn_isFill($("#${itemId}_deleted_idxs"))) && !fn_checkFill($("#${itemId}"), varLogText) || !$("#${itemId}_saved").is("input") && !fn_checkFill($("#${itemId}"), varLogText)) return false;
	}catch(e){}
	</c:if>
	fn_createMaskLayer();
	return true;
}
</script>