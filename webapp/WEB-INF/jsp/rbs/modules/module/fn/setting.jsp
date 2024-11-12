<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${settingItemInfo.items}" itemOrder="${settingItemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${settingItemInfo.items}" itemOrder="${settingItemInfo[itemOrderName]}"/>
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
			self.close();
		}catch(e){alert(e); return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
	
	<c:set var="itemId" value="dset_cate_tab_id"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	// 분류탭
	$('#${itemId}').change(function() {
		fn_setSelToChkDisabled('${itemId}', 'use_cate_tab_total');
	});
	</c:if>

	<c:set var="itemId" value="use_multi_cate"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	// 3차분류 사용
	$('#use_multi_cate').click(function() {
		fn_setChkToObjDisabled('${itemId}', 'dset_cate_master_code');
	});
	</c:if>

	<c:set var="itemId1" value="use_qna"/>
	<c:set var="itemId2" value="use_sms"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId1]) && !elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId2])}">
	// sms연동
	$('#use_qna').click(function(){
		fn_setUseSms();
	});

	// sms연동
	$('#use_sms').click(function(){
		fn_setSmsMgphone();
	});
	</c:if>
	

	<c:if test="${param.moduleId == 'poll'}">
	<c:set var="itemId" value="use_list_noreply"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	$('#${itemId}').click(function() {
		fn_setChkToObjDCed('${itemId}', 'use_notice');
	});
	</c:if>
	<c:set var="itemId" value="use_private"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	$('#${itemId}').click(function() {
		fn_setChkToObjDisabled('${itemId}', 'use_reply');
	});
	</c:if>
	</c:if>
	
	// table scroll
	fn_addScrollObject($("#<c:out value="${param.inputFormId}"/> > table"), "665px", $("#<c:out value="${param.inputFormId}"/>").parent().width() + 20);
});

/**
 * 선택값이 없는 경우 target Object disabled
 */
function fn_setSelToChkDisabled(theSId, theTId){
	var varIsVal = fn_isFill($('#' + theSId).find('option:selected'));
	if(!varIsVal) $('#' + theTId).prop('checked', false);
	$('#fn_' + theTId).prop('disabled', !varIsVal);
}

/**
 * 3차분류 사용
 */
function fn_setChkToObjDisabled(theSId, theTId) {
	var varIsChk = $("#" + theSId).prop('checked');
	$('#' + theTId).prop('disabled', !varIsChk);
}

function fn_setChkToObjDCed(theSId, theTId) {
	var varIsChk = $("#" + theSId).prop('checked');
	$('#' + theTId).prop('disabled', varIsChk);
	if(varIsChk) $('#' + theTId).prop('checked', varIsChk);
}

function fn_setUseSms() {
	var varChecked = $('#use_qna').prop('checked');
	if(!varChecked) {
		$('#use_sms').prop('checked', false);
	}
	$('#use_sms').prop('disabled', !varChecked);
	$('label[for="use_sms"]').prop('disabled', !varChecked);
	fn_setSmsMgphone();
}

/**
 * sms연동
 */
function fn_setSmsMgphone() {
	var varChecked = $('#use_sms').prop('checked');
	if(!varChecked) {
		$('#dset_sms_mgphone').val('');
		$('#dset_sms_mgemail').val('');
	}
	$('#dset_sms_mgphone').prop('disabled', !varChecked);
	$('label[for="dset_sms_mgphone"]').prop('disabled', !varChecked);
	$('#dset_sms_mgemail').prop('disabled', !varChecked);
	$('label[for="dset_sms_mgemail"]').prop('disabled', !varChecked);
	
}

<c:set var="itemId" value="design"/>
//기본 템플릿 검색
function fn_designListSearchForm() {
	fn_dialog.init("fn_${itemId}_search");
	var varItemId = "${itemId}";
	try {
		var varTitle = "";
		fn_dialog.open(varTitle, "${URL_DESIGNLIST}&mdl=1&designType=<c:out value="${dt.DESIGN_TYPE}"/>&itemId=" + varItemId +"&dl=1");
	}catch(e){}
	return false;
}

function fn_setDesignForm(theVal){
	$("#designName").val(theVal);
	$("#design").val(theVal);
}

function fn_boardInputReset(){
	<itui:submitReset items="${settingItemInfo.items}" itemOrder="${settingItemInfo[itemOrderName]}"/>
	<c:set var="itemId" value="dset_cate_tab_id"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	fn_setSelToChkDisabled('${itemId}', 'use_cate_tab_total');
	</c:if>
	<c:set var="itemId" value="use_multi_cate"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	fn_setChkToObjDisabled('${itemId}', 'dset_cate_master_code');
	</c:if>
	<c:set var="itemId1" value="use_qna"/>
	<c:set var="itemId2" value="use_sms"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId1]) && !elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId2])}">
	fn_setSmsMgphone();
	fn_setUseSms();
	</c:if>
	<c:if test="${param.moduleId == 'poll'}">
	<c:set var="itemId" value="use_list_noreply"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	fn_setChkToObjDCed('${itemId}', 'use_notice');
	</c:if>
	<c:set var="itemId" value="use_private"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
	fn_setChkToObjDisabled('${itemId}', 'use_reply');
	</c:if>
	</c:if>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${settingItemInfo.items}" itemOrder="${settingItemInfo[itemOrderName]}"/>
	
	<c:if test="${param.moduleId == 'poll'}">
	<c:set var="itemId" value="use_notice"/>
	<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
		$('#${itemId}').prop('disabled', false);
	</c:if>
	</c:if>
	fn_createMaskLayer();
	return true;
}
</script>