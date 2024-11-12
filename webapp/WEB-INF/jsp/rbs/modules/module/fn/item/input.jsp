<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
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
			location.href="${elfn:replaceScriptLink(URL_LIST)}";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
	
	// 유형 선택에 따른 연관항목 사용
	$("#format_type").change(function(){
		fn_setRelItemOfFormatType();
	});

	// 유형 선택에 따른 연관항목 사용
	$("#object_type").change(function(){
		fn_setRelItemOfObjectType();
	});
	
	// 이미지 선택
	$("input[name='isimage']").change(function(){
		fn_setRelItemOfImage();
	});
	fn_setRelItemOfFormatType(1);
	fn_setRelItemOfImage(1);
	
	// table scroll
	fn_addScrollObject($("#<c:out value="${param.inputFormId}"/> > table"), "665px", $("#<c:out value="${param.inputFormId}"/>").parent().width() + 20);
});

function fn_setDisabledRelObj(theLayer, theIsInit) {
	var varSkipClass = "hidden";
	theLayer.addClass(varSkipClass);
	if(!theIsInit) { 
		theLayer.find("input[type='text']").filter(":not([data-default])").val('');
		$.each(theLayer.find("input[type='text']").filter("[data-default]"), function(){
			$(this).val($(this).attr('data-default'));
		});
		theLayer.find("input[type='checkbox']").prop('checked', false);
	
		theLayer.find("input[type='radio']").filter("[data-default='1']").prop('checked', true);
		var varOptObj = theLayer.find("select option[data-default='1']");
		if(varOptObj.is("option")) varOptObj.prop('selected', true);
		else theLayer.find("select option:eq(0)").prop("selected", true);
	}
	
	theLayer.find("input,select").prop("disabled", true);
	
}

/**
 * 유형 연관항목 설정
 */
function fn_setRelItemOfFormatType(theIsInit){
	var varSkipClass = "hidden";
	var varVal = ($("#format_type").is('input'))?$("#format_type").val():$("#format_type option:selected").val();
	var varRelPLayer = $("#fn_w_format_type");				// 유형 연관항목
	var varObjLayer = $("#fn_w_object_type");				// 일반유형 연관항목
	if(!fn_isFill(varVal) || varVal == '0') {
		// 일반 : 유형 연관항목 전체 숨김, 비활성화
		/*varRelPLayer.addClass(varSkipClass);
		varRelPLayer.find("input,select").prop("disabled", true);*/
		fn_setDisabledRelObj(varRelPLayer, theIsInit);
		
		// 일반유형 출력, 일반유형 기본 연관항목 활성화
		$("#object_type").prop("disabled", false);
		$("#object_type option[data-default='1']").prop("selected", true);
		//varObjLayer.removeClass(varSkipClass);
	} else {
		// 그 외 : 일반유형 비활성화 / 유형 연관항목 출력, 활성화 / 유형 연관항목 아닌 것 숨김, 비활성화
		
		// 일반유형 비활성화
		$("#object_type option:eq(0)").prop("selected", true);
		fn_setRelItemOfObjectType();
		$("#object_type").prop("disabled", true);
		/*varObjLayer.addClass(varSkipClass);
		varObjLayer.find("input,select").prop("disabled", true);*/
		
		//유형 연관항목 출력, 활성화 / 유형 연관항목 아닌 것 숨김, 비활성화
		var varRelLayer = $(".fn_w_format_" + varVal);
		var varHasRel = (varRelLayer.size() > 0);
		var varNRelLayer;
		if(varHasRel) {
			// 유형 연관항목이 있는 경우 : 연관항목 아닌 것 숨김, 비활성화
			varNRelLayer = varRelLayer.siblings();
			/*varNRelLayer.addClass(varSkipClass);
			varNRelLayer.find("input,select").prop("disabled", true);*/
		} else {
			// 유형 연관항목이 없는 경우 : 전체 비활성화
			varNRelLayer = varRelPLayer;
			//varRelPLayer.find("input,select").prop("disabled", true);
		}

		var varHasNRel = (varNRelLayer.size() > 0);
		
		if(varHasNRel) {
			// 유형 연관항목 아닌 것 숨김, 비활성화
			/*varNRelLayer.addClass(varSkipClass);
			varNRelLayer.find("input,select").prop("disabled", true);*/
			fn_setDisabledRelObj(varNRelLayer, theIsInit);
		}
		
		if(varHasRel) {
			// 유형 연관항목 출력, 활성화
			varRelLayer.find("input,select").prop("disabled", false);
			varRelLayer.removeClass(varSkipClass);
			
			// 유형 전체 layer 출력
			varRelPLayer.removeClass(varSkipClass);
		}
	}
	fn_setRelItemOfObjectType(theIsInit);
}


function fn_setRelItemOfObjectType(theIsInit){
	var varSkipClass = "hidden";
	var varVal = ($("#object_type").is('input'))?$("#object_type").val():$("#object_type option:selected").val();
	var varRelPLayer = $("#fn_w_object_type");				// 유형 연관항목
	var varRejLayers = $(".fn_r_object");					// 유형 연관제외 항목
	var varRejLayer;
	if(!fn_isValFill(varVal)) {
		// 일반 : 유형 연관항목 전체 숨김, 비활성화
		/*varRelPLayer.addClass(varSkipClass);
		varRelPLayer.find("input,select").prop("disabled", true);*/
		fn_setDisabledRelObj(varRelPLayer, theIsInit);
	} else {
		// 그 외 : 일반유형 비활성화 / 유형 연관항목 출력, 활성화 / 유형 연관항목 아닌 것 숨김, 비활성화
		
		//유형 연관항목 출력, 활성화 / 유형 연관항목 아닌 것 숨김, 비활성화
		var varRelLayer = $(".fn_w_object_" + varVal);
		var varHasRel = (varRelLayer.size() > 0);
		var varNRelLayer;
		if(varHasRel) {
			// 유형 연관항목이 있는 경우 : 연관항목 아닌 것 숨김, 비활성화
			varNRelLayer = varRelLayer.siblings();
			/*varNRelLayer.addClass(varSkipClass);
			varNRelLayer.find("input,select").prop("disabled", true);*/
		} else {
			// 유형 연관항목이 없는 경우 : 전체 비활성화
			varNRelLayer = varRelPLayer;
			//varRelPLayer.find("input,select").prop("disabled", true);
		}

		var varHasNRel = (varNRelLayer.size() > 0);
		
		if(varHasNRel) {
			// 유형 연관항목 아닌 것 숨김, 비활성화
			/*varNRelLayer.addClass(varSkipClass);
			varNRelLayer.find("input,select").prop("disabled", true);*/
			fn_setDisabledRelObj(varNRelLayer, theIsInit);
		}
		
		if(varHasRel) {
			// 유형 연관항목 출력, 활성화
			varRelLayer.find("input,select").prop("disabled", false);
			varRelLayer.removeClass(varSkipClass);
			
			// 유형 전체 layer 출력
			varRelPLayer.removeClass(varSkipClass);
		}
		
		// 유형 연관제외 항목에 해당하지 않는 항목
		varRejLayer = varRejLayers.not(".fn_r_object_" + varVal);
		var varHasRej = (varRejLayer.size() > 0);
		var varNRejLayer;
		if(varHasRej) {
			// 유형 연관항목이 있는 경우 : 연관항목 아닌 것 숨김, 비활성화
			varNRejLayer = varRejLayers.filter(".fn_r_object_" + varVal);
		} else {
			// 유형 연관항목이 없는 경우 : 전체 비활성화
			varNRejLayer = varRejLayers;
		}
		
		var varHasNRej = (varNRejLayer.size() > 0);
		if(varHasNRej) {
			// 유형 연관항목 아닌 것 숨김, 비활성화
			/*varNRejLayer.addClass(varSkipClass);
			varNRejLayer.find("input,select").prop("disabled", true);*/
			fn_setDisabledRelObj(varNRejLayer, theIsInit);
		}
		
		if(varHasRej) {
			// 유형 연관항목 출력, 활성화
			varRejLayer.find("input,select").prop("disabled", false);
			varRejLayer.removeClass(varSkipClass);
			
			// 유형 전체 layer 출력
			varRelPLayer.removeClass(varSkipClass);
		}
	}
	
}

function fn_setRelItemOfImage(theIsInit) {
	var varSkipClass = "hidden";
	var varVal = $("input[name='isimage']:checked").val();
	var varRelLayer = $(".fn_w_isimage_1");				// 이미지 연관항목
	if(varVal == '1') {
		// 이미지 사용하는 경우
		varRelLayer.removeClass(varSkipClass);
		varRelLayer.find("input,select").prop("disabled", false);
	} else {
		// 이미지 사용하지 않는 경우
		varRelLayer.addClass(varSkipClass);
		varRelLayer.find("input,select").prop("disabled", true);
	}
}

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}" itemLangList="${langList}" itemLocaleLang="${siteInfo.locale_lang}"/>
	
	var varChk = true;
	var varChkObjIds = [["start_year", "<c:out value="${elfn:getItemName(itemInfo.items['start_year'])}"/>"], ["end_addcnt", "<c:out value="${elfn:getItemName(itemInfo.items['end_addcnt'])}"/>"]];
	$.each(varChkObjIds, function(){
		var varId = this[0];
		var varName = this[1];
		var varDisabled = $("#" + varId).prop("disabled");
		if(!varDisabled && !fn_checkFill($("#" + varId), varName)){
			varChk = false;
			return false;
		}
	});
	
	if(!varChk) return false;
	
	var varUPageChecked = false;
	var varUPageLayer = $("#fn_usePage");
	$.each($(varUPageLayer.find("input[type='checkbox']")), function(){
		if($(this).prop("checked")) {
			varUPageChecked = true;
			return;
		}
	});
	
	if(!varUPageChecked) {
		alert(fn_Message.checkText("<spring:message code="item.items.set.use.page.name"/>"));
		return false;
	}
	
	var varCMCodeObj = $("#class_master_code");
	var varDisabledCMCode = varCMCodeObj.prop("disabled");
	if(!varDisabledCMCode) varCMCodeObj.attr("name", "master_code");

	fn_createMaskLayer();
	return true;
}
</script>