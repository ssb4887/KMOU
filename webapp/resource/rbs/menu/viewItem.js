/*****************************************
 * moduleId
 *****************************************/
var fn_itemModule = {
	id:"moduleId"
	, init:function(){
		fn_itemModuleAuth.init();
		fn_itemModuleFn.init();
		fn_itemContents.init();
		fn_itemMenuLink.init();
		fn_itemDesign.init();
	}, set:function(theData, theVal){
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html(theVal);
	}
}

/*****************************************
 * moduleAuth
 *****************************************/
fn_itemModuleAuth = {
	id:"moduleAuth"
	, init:function(){
		// 기능 삭제, 비활성, designType 값 삭제
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
	}, set:function(theVal){
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html(theVal);
	}
}

/*****************************************
 * fnIdx
 *****************************************/
var fn_itemModuleFn = {
	id:"fnIdx"
	, init:function(){
		// 기능 삭제, 비활성, designType 값 삭제
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
		fn_itemDesign.init();

	}, hide:function(){
		fn_itemModuleFn.init();
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
		$(".fn_item_" + varObjId).hide();
	}, set:function(theVal){
		var varObjId = this.id;
		fn_itemModuleFn.init();
		$("#fn_item_" + varObjId).html(theVal);
		$(".fn_item_" + varObjId).show();
	}
}


/******************************************
일반기능 설정 setting
******************************************/
var fn_itemFnSettingForm = {
	id:"#fn_fnSettingDl"
	, init:function(){
		var varSetObj = $(this.id);
		varSetObj.addClass('hidden');
		varSetObj.find("dt, dd").addClass('hidden');
	}, setObject:function(theFnSettingList) {
		fn_itemFnSettingForm.init();
		if(!theFnSettingList) return false;
		var varSetObj = $(this.id);
		//$('#fn_fnSettingDl>td>dl').addClass('hidden');
		var varIsDisplay = false;
		$.each(theFnSettingList, function(index, item){
			$('.fn_' + item).removeClass('hidden');
			varIsDisplay = true;
		});
		
		if(varIsDisplay) varSetObj.removeClass('hidden');
		else varSetObj.addClass('hidden');
	}, set:function(theFnSettingList, theSettingInfo) {
		fn_itemFnSettingForm.init();
		if(!theFnSettingList) return false;
		var varSetObj = $(this.id);
		//$('#fn_fnSettingDl>td>dl').addClass('hidden');
		var varIsDisplay = false;
		$.each(theFnSettingList, function(index, item){
			$('.fn_' + item).removeClass('hidden');
			/*var varObj = $('.fn_' + item).find("input");
			var varObjType = varObj.attr("type");
			if(typeof(theSettingInfo) != "undefined" && typeof(theSettingInfo[item]) != "undefined") {
				var varDVal = theSettingInfo[item];
				if(typeof(varDVal) == "undefined") varDVal = "";
				if(varObjType == "checkbox") {
					if(varDVal == "1") varObj.prop("checked", true);
					else varObj.prop("checked", false);
				} else {
					varObj.filter("[type='text']").val(varDVal);
				}
			} else {
				if(varObjType == "checkbox") varObj.prop("checked", false);
				else varObj.filter("[type='text']").val("");
			}*/
			varIsDisplay = true;
		});
		
		if(varIsDisplay) varSetObj.removeClass('hidden');
		else varSetObj.addClass('hidden');
	}
}


/******************************************
회원기능 설정 setting
******************************************/
var fn_itemMemberSettingForm = {
	id:"#fn_memberDl"
	, init:function(){
		var varSetObj = $(this.id);
		varSetObj.addClass('hidden');
	}, set:function(theModuleId) {
		var varSetObj = $(this.id);
		if(theModuleId == 'member') varSetObj.removeClass('hidden');
		else varSetObj.addClass('hidden');
	}
}

/*****************************************
 * 콘텐츠
 *****************************************/
var fn_itemContents = {
	id:"contentsCode"
	, init:function(){
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
		fn_itemBranch.init();
	}, hide:function(){
		fn_itemContents.init();
		var varObjId = this.id;
		$(".fn_item_" + varObjId).hide();
		fn_itemBranch.hide();
	}, set:function(theVal){
		// contentsCode 목록, 값 setting / contentsType setting
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html(theVal);
		$(".fn_item_" + varObjId).show();
		$(fn_itemBranch.id).show();
	}
}

/*****************************************
 * branchIdx
 *****************************************/
var fn_itemBranch = {
	id:"branchIdx"
	, init:function(){
		// branch 목록 삭제, branchType 삭제
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
	}, hide:function(){
		fn_itemBranch.init();
		var varObjId = this.id;
		$(".fn_item_" + varObjId).hide();
	}, set:function(theVal){
		// branchIdx 목록, 값 setting
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html(theVal);
		$(".fn_item_" + varObjId).show();
	}
}

/*****************************************
 * 디자인
 *****************************************/
var fn_itemDesign = {
	id:"urDesign"
	, init:function(){
		// 삭제
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html("");
	}, set:function(theUrDesign){
		var varObjId = this.id;
		$("#fn_item_" + varObjId).html(theUrDesign);
	}
}