<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	<c:set var="grp_itemIdName" value="menuGrp"/>
	<c:set var="dpt_itemIdName" value="menuDpt"/>
	<c:set var="mbr_itemIdName" value="menuMbr"/>
	<itui:submitInitMultiSelectButton itemId="mbrGrp" id="${grp_itemIdName}" itemInfo="${itemInfo}"/>
	<itui:submitInitMultiSelectButton itemId="mbrDpt" id="${dpt_itemIdName}" itemInfo="${itemInfo}"/>
	<itui:submitInitMultiSelectButton itemId="mbrMbr" id="${mbr_itemIdName}" itemInfo="${itemInfo}"/>
	<c:forEach var="authManagerObject" items="${authManagerArray}" varStatus="i">
		<c:if test="${authManagerObject.adm_closed != 1}">
		<c:set var="grp_itemIdName" value="${authManagerObject.item_id}Grp"/>
		<c:set var="dpt_itemIdName" value="${authManagerObject.item_id}Dpt"/>
		<c:set var="mbr_itemIdName" value="${authManagerObject.item_id}Mbr"/>
		<itui:submitInitMultiSelectButton itemId="mbrGrp" id="${grp_itemIdName}" itemInfo="${itemInfo}"/>
		<itui:submitInitMultiSelectButton itemId="mbrDpt" id="${dpt_itemIdName}" itemInfo="${itemInfo}"/>
		<itui:submitInitMultiSelectButton itemId="mbrMbr" id="${mbr_itemIdName}" itemInfo="${itemInfo}"/>
		</c:if>
	</c:forEach>
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			location.reload();
		}catch(e){alert(e); return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
	/*
	// 그룹 추가
	$(".fn_btn_add").click(function(){
		fn_dialog.init("fn_search");
		var varItemId = $(this).attr("data-id");
		try {
			var varTitle = "회원그룹 검색";
			fn_dialog.open(varTitle, "${URL_GRUPSEARCHLIST}&itemId=" + varItemId +"&type=1&dl=1", 600, 500);
		}catch(e){}
	});
	
	// 그룹 삭제
	$(".fn_btn_del").click(function(){
		try {
			var varItemId = $(this).attr("data-id");
			var varObj = $("#" + varItemId);
			if(!fn_checkSelected(varObj, "삭제")) return false;
			
			varObj.find("option:selected").remove();
		}catch(e){}
	});*/
});


//사용자그룹 검색
function fn_mbrGrpListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원그룹 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_GRUPSEARCHLIST}"/>&itemId=" + varItemId +"&type=1&dl=1", 650, 500);
	}catch(e){}
}

//사용자그룹 setting
function fn_setMemberGrupInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

//사용자부서 검색
function fn_mbrDptListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원부서 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_DPRTSEARCHLIST}"/>&itemId=" + varItemId +"&type=1&dl=1", 650, 500);
	}catch(e){}
}
//사용자부서 setting
function fn_setMemberDprtInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

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

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}
function fn_boardInputFormSubmit(){

	<c:set var="utp_itemIdName" value="menuUtp"/>
	<c:set var="grp_itemIdName" value="menuGrp"/>
	try{
		var varNUObjs = $("#${utp_itemIdName} option:selected").filter("[value='0']");
		var varGObjs = $("#${grp_itemIdName} option");
		var varNUSize = varNUObjs.size();
		var varGSize = varGObjs.size();
		if(varNUSize > 0 && varGSize == 0) {
			alert("권한을 선택하셔야 합니다.");
			varNUObjs.parent().focus();
			return false;
		}
	}catch(e){}
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	<c:forEach var="authManagerObject" items="${authManagerArray}" varStatus="i">
	<c:if test="${authManagerObject.adm_closed != 1}">
	<c:set var="grp_itemIdName" value="${authManagerObject.item_id}Grp"/>
	<c:set var="dpt_itemIdName" value="${authManagerObject.item_id}Dpt"/>
	<c:set var="mbr_itemIdName" value="${authManagerObject.item_id}Mbr"/>
	</c:if>
</c:forEach>
	try{
		$("select[multiple='multiple'] option").prop("selected", true);
	}catch(e){}

	top.fn_createMaskLayer();
	return true;
}
</script>