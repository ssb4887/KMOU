$(function(){	
	// 결과유형 click
	$("#isquestype").click(function(){
		fn_setPollItem($(this).prop('checked'));
	});
	// 동일항목 click
	$("#ispollitem").click(function(){
		fn_setPollItemList($(this).prop('checked'));
	});
	fn_setPollItem($("#isquestype").prop("checked"));
	
	// 항목 전체 선택
	$('#itemSelectAll').click(function(){
		$('input[name="itemDel"]').prop('checked', $(this).prop('checked'));
	});
	
	// 항목 추가
	$('#fn_item_list_wp .fn_btn_add_item').click(function(event){
		return fn_setItemForm.addItem();
	});
	
	// 항목 삭제
	$('#fn_item_list_wp .fn_btn_del_item').click(function(event){
		return fn_setItemForm.delItem();
	});
});

// 동일항목 checkbox
function fn_setPollItem(theChecked){
	$("#ispollitem").prop("disabled", !theChecked);
	if(!theChecked) $("#ispollitem").prop("checked", false);
	fn_setPollItemList($("#ispollitem").prop("checked"));
}

// 항목 목록
function fn_setPollItemList(theChecked){
	if(theChecked) $("#fn_item_list_wp").removeClass("hidden");
	else {
		fn_setItemForm.allDelItem();
		$("#fn_item_list_wp").addClass("hidden");
	}
}

/*********************************
 * 항목
**********************************/
var fn_setItemForm = {
	tObjName:".fn_item_list>table"
	, addItem:function(){
		/*********************************
		 * 추가
		**********************************/
		var varTObj = $(fn_setItemForm.tObjName);
		var varTBObj = varTObj.find('tbody');
		var varTRObj = varTBObj.find('tr:last');
		var varNewTRObj = varTRObj.clone();
		varNewTRObj.find('input[type="checkbox"]').prop('checked', false);
		varNewTRObj.find('input[type="text"]').val('');
		varTBObj.append(varNewTRObj);
	}, delItem:function() {
		/*********************************
		 * 삭제
		**********************************/
		var varTObj = $(fn_setItemForm.tObjName);
		var varTBObj = varTObj.find('tbody');
		var varTRObj = varTBObj.find('tr');
		
		var varChkObjs = varTBObj.find('input[name="itemDel"]:checked');
		var varChkSize = varChkObjs.size();
		if(varChkSize == 0) {
			alert('삭제할 항목을 선택하세요.');
			return false;
		}
		
		var varTRSize = varTRObj.size();
		if(varTRSize <= 1) {
			varTRObj.find('input[type="checkbox"]').prop('checked', false);
			varTRObj.find('input[type="text"]').val('');
		} else {
			var varTotSize = varTBObj.find('input[name="itemDel"]').size();
			if(varChkSize == varTotSize) $('#fn_item_list_wp .fn_btn_add_item').click();
			$(varChkObjs).parent('td').parent('tr').remove();
		}
			
		$('#itemSelectAll').prop('checked', false);
	}, allDelItem:function() {
		/*********************************
		 * 삭제
		**********************************/
		var varTObj = $(fn_setItemForm.tObjName);
		var varTBObj = varTObj.find('tbody');
		var varTRObj = varTBObj.find('tr');
		
		var varTRSize = varTRObj.size();
		if(varTRSize > 1) {
			varTBObj.find('tr:not(:last)').remove();
			varTRObj = varTBObj.find('tr');
		}
		
		varTRObj.find('input[type="checkbox"]').prop('checked', false);
		varTRObj.find('input[type="text"]').val('');
			
		$('#itemSelectAll').prop('checked', false);
	}
}


function fn_checkFillRequired(varItemNames, varItemItemNames){
	var varChk = true;
	$.each(varItemNames, function(i){
		// 항목 입력 체크
		var varEChk = false;
		var varRChkIdx = -1;
		$.each(varItemItemNames, function(j){
			var varItem = this;
			var varChkObj = $('input.' + varItem[0] + ':eq(' + i + ')');
			var varChkVal = varChkObj.val().trim();
			if(varRChkIdx == -1 && varChkVal == '' && varItem[2] == 1) varRChkIdx = j;
			
			if(!fn_checkStrMaxLenForm(varChkObj, varItem[1], varItem[3])) {
				varChk = false;
				return false;
			}
		});
		
		if(varRChkIdx != -1/* && varEChk*/) {
			// 필수입력 항목 입력 안된 경우/* && 하나라도 입력된 경우*/
			alert(varItemItemNames[varRChkIdx][1] + '을/를 입력하세요.');
			$('input.' + varItemItemNames[varRChkIdx][0] + ':eq(' + i + ')').focus();
			varChk = false;
			return false;
		}
	});
	
	return varChk;
}