$(function(){
});

/***********************************************************
메뉴관리내용(form) 3차 setting : 선택 메뉴정보, 모듈권한, 디자인 템플릿, Branch(콘텐츠) setting
***********************************************************/
var fn_setMenuForm = {
	//formId:false,
	//infoUrl:false,
	//menu_info : false,
	init:function(){
		
	}, 
	get:function(theMenuIdx){
		// 메뉴정보에 대한  항목 setting
		fn_setMenuForm.init();
		var varAction = this.infoUrl + "&menuIdx=" + theMenuIdx;
		$.ajax({
	  		type:'POST', 
	  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  		dataType:'json', 
	  		url:varAction, 
	  		async:true, 
	  		success:function(result){
	  			if(result.info) {
	  				var varInfo = result.info;
	  				// 항목 setting
	  				fn_setMenuForm.set(result.info, result.settingList, result.multiData, result.optnData);
	  			}
	  		}, 
	  		error:function(request,error){
	  			fn_ajax.checkError(request);
	  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	  		}
	  	});
	}, 
	set: function(theMenuInfo, theFnSettingList, theMultiData, theOptnData) {
		// 메뉴정보 setting
		this.menuInfo = theMenuInfo;
		var varFormId = this.formId;

		var varModuleId = theMenuInfo['moduleId'];
		
		var varObjs = $('#' + varFormId + ' [id^="fn_item_"]');
		
		$.each(varObjs, function(i, item){
			var varObjId = $(item).attr('id');
			var varObjName = varObjId.replace("fn_item_", "");
			var varVal = theMenuInfo[varObjName];

			var varDataType = $(item).attr("data-type");
			if(varDataType == "2") {
				// select
				varVal = theMenuInfo[varObjName + "Name"];
			} else if(varDataType == "14") {
				// 단일 checkbox
				if(varVal == "1") varVal = "사용";
				else varVal = "미사용";
			} else if(varDataType == "11") {
				// multi select + button
				var varSelMultiData = theMenuInfo[varObjName];
				var varSelOptnData = theOptnData[varObjName];
				
				$(this).find('ul').remove();
				if(typeof(varSelMultiData) != "undefined" && typeof(varSelOptnData) != "undefined") {
					varSelMultiData = varSelMultiData.split(",");
					var varSelObj = $(this);
					var varUlObj;
					$.each(varSelMultiData, function(vo, item){
						var varMatchObj = getMatchObject(varSelOptnData, item, "OPTION_CODE");
						if(varMatchObj) {
							varUlObj = varSelObj.find("ul");
							if(!varUlObj.is("ul")) {
								varSelObj.append("<ul></ul>");
								varUlObj = varSelObj.find("ul");
							}
							varUlObj.append("<li>" + varMatchObj["OPTION_NAME"] + "</li>");
						}
					});
				}
			}
			if(!fn_isValFill(varVal)) {
				var varDefaultVal = $(item).attr("data-default");
				if(typeof(varDefaultVal) != "undefined") varVal = varDefaultVal;
			}
			
			if(varDataType != "11") {
				if(typeof(varVal) == "undefined") varVal = "";
				$(this).html(varVal);
			}
		});
		
		/*
		 * 추가항목 setting 
		 */
		// 관리경로
		var varAnMenuLink = $("#fn_item_anMenuLink").html();
		if(!fn_isValFill(varAnMenuLink)) $(".fn_item_anMenuLink").hide();
		else $(".fn_item_anMenuLink").show();
		
		if(!fn_isValFill(varModuleId)) {
			// 1. 권한  : 권한정보 삭제, 미사용
			fn_itemModuleAuth.init();
			// 2. 기능 : 기능삭제, 미사용 / DesignType
			fn_itemModuleFn.hide();	
			// 3. 콘텐츠 : 콘텐츠 / BranchIdx / BranchType
			fn_itemContents.hide();
			// 5. 기능설정
			fn_itemFnSettingForm.init();
			// 6. 회원기능설정
			fn_itemMemberSettingForm.init();
		} else {
			
			var varVal = theMenuInfo['moduleName'];
			if(typeof(varVal) != "undefined") {
				$("#fn_item_moduleId").html(varVal);
			}
			
			// 1. 기능권한
			fn_itemModuleAuth.set(theMenuInfo['authName']);

			// 2. 기능
			fn_itemModuleFn.set(theMenuInfo['fnName']);
			if(varModuleId == "contents") {
				// 콘텐츠
				fn_itemModuleFn.hide();
				// 3. 콘텐츠
				fn_itemContents.set(theMenuInfo['contentsName']);
				// 4. branch
				fn_itemBranch.set(theMenuInfo['branchName']);
			} else {
				// 일반기능
				fn_itemContents.hide();
			}
			// 5. 기능설정
			fn_itemFnSettingForm.setObject(theFnSettingList);
			// 6. 회원기능설정
			fn_itemMemberSettingForm.set(varModuleId);
		}
		
		// 디자인템플릿
		//fn_itemDesign.set(theMenuInfo['urDesign'], theMenuInfo['anDesign']);
	}
};
