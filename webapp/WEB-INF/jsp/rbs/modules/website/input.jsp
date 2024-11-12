<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${empty param.mode && submitType == 'write'}">
<c:set var="verItemInfo" value="${moduleItem.version_item_info}"/>
<spring:message var="useLayoutTmp" code="Globals.layoutTmp.use" text=""/>
<c:if test="${useLayoutTmp == '1'}">
	<spring:message var="layoutItemFlag" code="Globals.layoutTmp.item.flag" text=""/>
</c:if>
<c:set var="verItemOrderName" value="${layoutItemFlag}siteproc_order"/>
</c:if>
<c:set var="itemViewOrderName" value="${submitType}_order"/>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>

	<%-- 접근제한 --%>
	<spring:message var="useIpRestrict" code="Globals.ip.restrict.use" text="0"/>
	<c:if test="${useIpRestrict == '1'}">
	$("#access_restrict").change(function(){
		var varChked = $(this).prop("checked");
		var varTitle;
		if(varChked) {
			varTitle = "허용IP";
		} else {
			varTitle = "제한IP";
		}
		$("#restrict_ip").attr("title", varTitle);
		$("label[for='restrict_ip']").html(varTitle);
	});
	$("#access_restrict").trigger("change");
	</c:if>
	<c:if test="${!empty verItemInfo}">
	<itui:submitInit items="${verItemInfo.items}" itemOrder="${verItemInfo[verItemOrderName]}"/>	<% /* version 항목 javascript 초기 setting */ %>
	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!empty verItemInfo && !elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	// 레이아웃 템플릿 삭제
	$("#fn_btn_del_${itemId}").click(function(){
		fn_initLayoutTmpRelItems();
	});
	</c:if>
	</c:if>
	<c:set var="itemId" value="manager_member_idxs"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemViewOrderName], itemInfo.items[itemId])}">
	<itui:submitInitMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}"/>
	</c:if>

	<c:if test="${!empty verItemInfo}">
	<c:set var="localPathItemName" value="${elfn:getItemName(verItemInfo.items['local_path'])}"/>
	<c:set var="itemId" value="include_top"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	// 상단인클루드 경로 기본값 넣기
	$('#fn_btn_${itemId}').click(function(){
		if(!fn_checkFill($('#local_path'), "<c:out value="${localPathItemName}"/>")) return false;
		var varLPVal = $('#local_path').val();
		$('#${itemId}').val(varLPVal + "<spring:message code="Globals.include.default.top.path"/>");
	});
	</c:if>
	<c:set var="itemId" value="include_bottom"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	// 하단인클루드 경로 기본값 넣기
	$('#fn_btn_${itemId}').click(function(){
		if(!fn_checkFill($('#local_path'), "<c:out value="${localPathItemName}"/>")) return false;
		var varLPVal = $('#local_path').val();
		$('#${itemId}').val(varLPVal + "<spring:message code="Globals.include.default.bottom.path"/>");
	});
	</c:if>
	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	//레이아웃 템플릿 검색
	function fn_layoutTmpListSearchForm() {
		try {
			if(!fn_checkFill($("#site_id"), $("label[for='site_id']").text())) return false;
		}catch(e){}
		try {
			if(!fn_checkSelected($("#site_type"), $("label[for='site_type']").text())) return false;
		}catch(e){}
		/*try {
			if(!fn_checkFill($("#local_path"), $("label[for='local_path']").text())) return false;
		}catch(e){}*/
		fn_dialog.init("fn_${itemId}_search");
		var varItemId = "${itemId}";
		try {
			var varTitle = "";
			var varSiteType = $("#site_type option:selected").val();
			fn_dialog.open(varTitle, "${URL_LAYOUTTMPLIST}&mdl=1&itemId=" + varItemId +"&dl=1&siteType=" + varSiteType);
		}catch(e){}
		return false;
	}
	</c:if>

	<c:set var="itemId" value="layout_theme"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	//레이아웃 테마 검색
	function fn_layoutThemeListSearchForm() {
		try {
			if(!fn_checkSelected($("#site_type"), $("label[for='site_type']").text())) return false;
		}catch(e){}
		fn_dialog.init("fn_${itemId}_search");
		var varItemId = "${itemId}";
		try {
			var varTitle = "";
			var varSiteType = $("#site_type option:selected").val();
			fn_dialog.open(varTitle, "${URL_LAYOUTTHEMELIST}&mdl=1&itemId=" + varItemId +"&dl=1&siteType=" + varSiteType);
		}catch(e){}
		return false;
	}
	</c:if>
	</c:if>
	
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
<c:if test="${!empty verItemInfo}">
<c:set var="itemId" value="layout_tmp"/>
<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
//레이아웃 템플릿 검색
function fn_setLayoutTmpForm(theVal){
	$("#${itemId}Name").val(theVal);
	$("#${itemId}").val(theVal);

	$("#local_path").val("/" + $("#site_id").val());
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
function fn_initLayoutTmpRelItems() {
	$("#fn_btn_del_layout_theme").click();
	$("#local_path").prop("readonly", false);
	$("#include_top").prop("readonly", false);
	$("#include_bottom").prop("readonly", false);
	$("#layout_theme").parent().find("input, button").prop("disabled", true);
	$("#logo").parent().parent().find("input, button").prop("disabled", true);
	$("label[for='layout_themeName'], label[for='logo']").removeClass("required");
	$("#layout_themeName").prop("required", false);
}
</c:if>

<c:set var="itemId" value="layout_theme"/>
<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
//레이아웃 테마 검색
function fn_setLayoutThemeForm(theVal){
	$("#${itemId}Name").val(theVal);
	$("#${itemId}").val(theVal);
}
</c:if>
</c:if>

<c:set var="itemId" value="manager_member_idxs"/>
<c:if test="${!elfn:isItemClosedArr(itemInfo[itemViewOrderName], itemInfo.items[itemId])}">
//사용자회원 검색
function fn_mbrMbrListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_MMBRSEARCHLIST}"/>&itemId=" + varItemId +"&type=1&dl=1", 650, 500);
	}catch(e){}
}
//사용자회원 setting
function fn_setMemberMmbrInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

//사용자정보 공통 setting
function fn_setMemberComInfo(theItemId, theChkedObjs){
	$.each(theChkedObjs, function(){
		var varCd = $(this).val();
		var varObj =  $("select[name='" + theItemId + "']");
		var varPreChkedObj = varObj.find("option[value='" + varCd + "']");
		if(!varPreChkedObj.is("option")){
			var varBName = $(this).attr("data-name");
			varObj.append("<option value=\"" + varCd + "\">" + varBName + "</option>");
		}
	});
}
</c:if>

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>

	<c:set var="itemId" value="layout_tmp"/>
	<c:if test="${!empty verItemInfo && !elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	fn_initLayoutTmpRelItems();
	</c:if>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>

	<c:set var="itemId" value="manager_member_idxs"/>
	<c:if test="${!elfn:isItemClosedArr(itemInfo[itemViewOrderName], itemInfo.items[itemId])}">
	try{$("select[name='${itemId}'] option").prop("selected", true);}catch(e){}
	</c:if>
	
	<c:if test="${!empty verItemInfo}">
	<c:set var="itemId" value="layout_theme"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	try{
		if($("#${itemId}Name").prop("required") && !fn_checkFill($("#${itemId}Name"), $("label[for='${itemId}Name']").text())) return false;
	}catch(e){}
	</c:if>
	<c:set var="itemId" value="logo"/>
	<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
	try{
		var varLogText = $("label[for='logo']").text();
		if($("label[for='${itemId}']").hasClass("required") && $("#${itemId}_saved").is("input") && (!fn_isFill($("#${itemId}_saved")) || fn_isFill($("#${itemId}_deleted_idxs"))) && !fn_checkFill($("#${itemId}"), varLogText) || !$("#${itemId}_saved").is("input") && !fn_checkFill($("#${itemId}"), varLogText)) return false;
	}catch(e){}
	</c:if>
	</c:if>
	fn_createMaskLayer();
	return true;
}
</script>