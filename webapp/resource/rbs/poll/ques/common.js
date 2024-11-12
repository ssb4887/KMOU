$(function(){	
	// 항목유형 선택
	$('#answerType').change(function(){
		fn_setAnswerType($(this).val());
	});
	
	// 연관문항 선택
	$('#relQuesIdx').change(function(){
		fn_setRelItemForm.get($(this).find('option:selected').val());
	});
	
	// 내부문항 사용 체크
	$('#useInques').click(function(){
		fn_setInquesList();
	});
	
	// 분류 추가
	$('#fn_inclass_list_wp .fn_btn_add_inclass').click(function(event){
		return fn_setInclassForm.addInclass(this);
	});
	
	// 분류 삭제
	$('#fn_inclass_list_wp .fn_btn_del_inclass').click(function(event){
		return fn_setInclassForm.delInclass(this);
	});
	
	// 분류 전체 선택
	$('#inclassSelectAll').click(function(){
		$('input[name="inclassDel"]').prop('checked', $(this).prop('checked'));
	});
	
	// 내부문항 추가
	$(document).on('click', '#fn_inclass_list_wp .fn_btn_add_inques', function(event){
		return fn_setInclassForm.addInques(this);
	});
	
	// 내부문항 삭제
	$(document).on('click', '#fn_inclass_list_wp .fn_btn_del_inques', function(event){
		return fn_setInclassForm.delInques(this);
	});
	
	// 내부문항 전체 선택
	$('#inquesSelectAll').click(function(){
		$('input[name="inquesDel"]').prop('checked', $(this).prop('checked'));
	});
	
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


/*********************************
 * 연관문항 항목 가져오기
**********************************/
var fn_setRelItemForm = {
		get:function(theQuesIdx, theRelItemIdxs){
			var varObjClassName = ".fn_relItemIdx";
			var varObj = $("dd" + varObjClassName);
			varObj.html('');
			if(!theQuesIdx || theQuesIdx == '0') {
				$(varObjClassName).addClass('hidden');
				return false;
			}
			// 연관문항 setting
			var varRelItemIdxs = (typeof(theRelItemIdxs) == "undefined")?$("#relItemIdx").val():theRelItemIdxs;
			var varAction = this.infoUrl + "&quesIdx=" + theQuesIdx;
			$.ajax({
		  		type:'POST', 
		  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		  		dataType:'json', 
		  		url:varAction, 
		  		async:true, 
		  		success:function(result){
		  			if(result.list) {
						$(varObjClassName).removeClass('hidden');
		  				$.each(result.list, function(i, item){
		  					var varIdx = i + 1;
		  					var varCon = "<input type='checkbox' id='tmpRelItemIdx"+varIdx+"' name='tmpRelItemIdx' value='" + item.itemIdx + "'";
		  					if(varRelItemIdxs && fn_getIndexOf(',', varRelItemIdxs, this.itemIdx) != -1) varCon += ' checked="checked"';
		  					varCon += "/><label for='tmpRelItemIdx"+varIdx+"'>" + item.contents + "<label>";
		  					varObj.append(varCon);
		  				});
		  			}
		  		}, 
		  		error:function(request,error){
		  			fn_ajax.checkError(request);
		  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		  		}
		  	});
		}
		
}

/*********************************
 * 내부분류
**********************************/
var fn_setInclassForm = {
	tObjName:".fn_inclass_list"
	, maxIdx:1
	, addInclass:function(theBObj) {
		/*********************************
		 * 내부분류추가
		**********************************/
		var varTObj = $(fn_setInclassForm.tObjName);
		var varTBObj = varTObj.find('tbody');
		var varTRObj = varTBObj.find('tr:last');
		var varNewTRObj = varTRObj.clone();
		varNewTRObj.find('input[type="checkbox"]').prop('checked', false);
		varNewTRObj.find('input[type="text"]').val('');
	
		var varInqLis = varNewTRObj.find('.fn_inques_list_ul li');
		var varInqLiSize = varInqLis.size();
		if(varInqLiSize > 1) {
			varInqLis.filter(':gt(0)').remove();
		}
		
		varTBObj.append(varNewTRObj);
		
		fn_setInclassForm.maxIdx ++;
	}, delInclass:function(theBObj) {
		/*********************************
		 * 내부분류삭제
		**********************************/
		var varTObj = $(fn_setInclassForm.tObjName);
		var varTBObj = varTObj.find('tbody');
		var varTRObj = varTBObj.find('tr');
		
		var varChkObjs = varTBObj.find('input[name="inclassDel"]:checked');
		var varChkSize = varChkObjs.size();
		if(varChkSize == 0) {
			alert('삭제할 내부문항 분류를 선택하세요.');
			return false;
		}
		
		var varTRSize = varTRObj.size();
		if(varTRSize <= 1) {
			varTRObj.find('input[type="checkbox"]').prop('checked', false);
			varTRObj.find('input[type="text"]').val('');
		} else {
			var varTotSize = varTObj.find('input[name="inclassDel"]').size();
			if(varChkSize == varTotSize) $(theBObj).siblings('.fn_btn_add_inclass').click();
			$(varChkObjs).parent('td').parent('tr').remove();
		}
			
		$('#inclassSelectAll').prop('checked', false);
	}, addInques:function(theBObj) {
		/*********************************
		 * 내부문항추가
		**********************************/
		var varTObj = $(theBObj).parent("div").parent("div");
		var varTBObj = varTObj.siblings('.fn_inques_list_ul');
		var varTRObj;
		if(varTBObj.is("ul")) {
			varTRObj = varTBObj.find('li:last');
		} else if(varTBObj.is("table")) {
			varTBObj = varTBObj.find('tbody');
			varTRObj = varTBObj.find('tr:last');
		}
		var varNewTRObj = varTRObj.clone();
		varNewTRObj.find('input[type="checkbox"]').prop('checked', false);
		varNewTRObj.find('input[type="text"]').val('');
		varTBObj.append(varNewTRObj);
	},delInques:function(theBObj) {
		/*********************************
		 * 내부문항삭제
		**********************************/
		var varTObj = $(theBObj).parent("div").parent("div");
		var varTBObj = varTObj.siblings('.fn_inques_list_ul');
		var varTRObj;
		if(varTBObj.is("ul")) {
			varTRObj = varTBObj.find('li');
		} else if(varTBObj.is("table")) {
			varTBObj = varTBObj.find('tbody');
			varTRObj = varTBObj.find('tr');
		}
		
		var varChkObjs = varTBObj.find('input[name="inquesDel"]:checked');
		var varChkSize = varChkObjs.size();
		if(varChkSize == 0) {
			var varInquesName = $(theBObj).attr("data-title");
			alert("삭제할 " + varInquesName + "을 선택하세요.");
			return false;
		}
		
		var varTRSize = varTRObj.size();
		if(varTRSize <= 1) {
			varTRObj.find('input[type="checkbox"]').prop('checked', false);
			varTRObj.find('input[type="text"]').val('');
		} else {
			var varTotSize = varTBObj.find('input[name="inquesDel"]').size();
			if(varChkSize == varTotSize) $(theBObj).siblings('.fn_btn_add_inques').click();
			if(varTBObj.is("ul")) {
				$(varChkObjs).parent('li').remove();
			} else if(varTBObj.is("tbody")) {
				$(varChkObjs).parent('td').parent('tr').remove();
			}
		}
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
	}
}


/**********************************
 * 문항선택시 항목 보이기/숨기기
 **********************************/
function fn_setAnswerType(theVal) {
	var varVal = theVal;
	if(varVal == '2') {
		// 주관식
		
		// 선택유형 비활성화
		$('#itemType').hide();
		$('#itemType').prop('disabled', true);
		
		// 내부문항 비활성화
		$('#useInques').prop('checked', false);
		$('#useInques').parent().find('*').prop('disabled', true);
		fn_setInquesList();
		
		// 기타의견 비활성화
		$('#fn_item_list_wp .fn_input_text_td input[type="checkbox"]').prop('checked', false);
		$('#fn_item_list_wp .fn_input_text_td *').prop('disabled', true);
	} else {
		// 객관식
		
		// 선택유형 활성화
		$('#itemType').show();
		$('#itemType').prop('disabled', false);

		// 내부문항 활성화
		$('#useInques').parent().find('*').prop('disabled', false);
		fn_setInquesList();
		
		// 기타의견
		$('#fn_item_list_wp .fn_input_text_td *').prop('disabled', false);
	}
}

/**********************************
 * 내부문항 사용 체크시 내부문항 보이기/숨기기
 **********************************/
function fn_setInquesList() {

	var varChked = $('#useInques').prop('checked');
	
	var varObjName = '#fn_inclass_list_wp';
	if(varChked) {
		$(varObjName + ' *').prop('disabled', false);
		$(varObjName).removeClass('hidden');

		// 기타의견 비활성화
		$('#fn_item_list_wp .fn_input_text_td input[type="checkbox"]').prop('checked', false);
		$('#fn_item_list_wp .fn_input_text_td *').prop('disabled', true);
	} else {
		$(varObjName).addClass('hidden');
		$(varObjName + ' *').prop('disabled', true);
		
		// 기타의견
		$('#fn_item_list_wp .fn_input_text_td *').prop('disabled', false);
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