$(function(){
});

/***********************************************************
메뉴관리내용(form) 3차 setting : 선택 메뉴정보, 모듈권한, 디자인 템플릿, Branch(콘텐츠) setting
***********************************************************/
var fn_setMenuForm = {
	get:function(theMenuIdx){
		// 메뉴정보에 대한  항목 setting
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
	  				fn_setMenuForm.set(result.info, result.moduleAuthList, result.moduleFnList, result.settingList, result.contentsList, result.branchList, result.multiData, result.optnData);
	  			}
	  		}, 
	  		error:function(request,error){
	  			fn_ajax.checkError(request);
	  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	  		}
	  	});
	}, 
	set: function(theMenuInfo, theModuleAuthList, theModuleFnList, theFnSettingList, theConList, theBranchList, theMultiData, theOptnData, theGroupList, theDepartList, theMemberList) {
		// 메뉴정보 setting
		this.menuInfo = theMenuInfo;
		var varFormId = this.formId;

		var varModuleId = theMenuInfo['moduleId'];
		
		$('#' + varFormId + ' input[type="text"],#' + varFormId + ' input[type="hidden"],#' + varFormId + ' textarea').each(function(i, item){
			var varObjName = $(this).attr('id');
			if(theMenuInfo[varObjName] != "undefined") $(this).val(theMenuInfo[varObjName]);
			else $(this).val('');
		});

		$('#' + varFormId + ' input[type="radio"]').each(function(i, item){
			var varObjName = $(this).attr('name');
			var varIsDefault = $(this).attr("data-default");
			if(typeof((theMenuInfo[varObjName]) == 'undefined' || theMenuInfo[varObjName] == '') && varIsDefault == '1' || 
					(',' + theMenuInfo[varObjName] + ',').indexOf(',' + $(this).val() + ',') != -1) $(this).prop('checked', true);
		});
			
		$('#' + varFormId + ' input[type="checkbox"]').each(function(i, item){
			var varObjName = $(this).attr('name');
			var varIsDefault = $(this).attr("data-default");
			if(typeof((theMenuInfo[varObjName]) == 'undefined' || theMenuInfo[varObjName] == '') && varIsDefault == '1' || 
					(',' + theMenuInfo[varObjName] + ',').indexOf(',' + $(this).val() + ',') != -1) 
				$(this).prop('checked', true);
			else
				$(this).prop('checked', false);
		});
		
		$('#' + varFormId + ' input[type="file"]').each(function(i, item){
			var varObjName = $(this).attr('name');
			fn_setFileReset(varObjName, theMenuInfo[varObjName + '_saved_name'], theMenuInfo[varObjName + '_origin_name']);
		});

		$('#' + varFormId + ' select[multiple!="multiple"]').each(function(i, item){
			var varObjName = $(this).attr('id');
			var varObjVal = theMenuInfo[varObjName];
			if(typeof(varObjVal) == "undefined") varObjVal = "";
			if(varObjName == 'menuLinkIdx') {
				// 메뉴링크위치
				var varSelMenuIdxVal = theMenuInfo['menuIdx'];
				if(varModuleId == '') {
					// 사용기능 선택 안 된 경우
					if(typeof(varObjVal) == 'undefined' || varObjVal == '') {
						// 메뉴링크위치 값 없는 경우
						$(this).find('option[value="' + varSelMenuIdxVal + '"]').prop('selected', true);
					} else $(this).find('option[value="' + varObjVal + '"]').prop('selected', true);
				} else {
					// 사용기능 선택된 경우
					$(this).find('option[value="' + varSelMenuIdxVal + '"]').prop('selected', true);
				}
			} else if(varObjName != 'orderMenuIdx' && varObjName != 'orderMenuLevel'){
				if(varObjName == 'anDesign' || varObjName == 'urDesign') {
					if(varModuleId != ''){
						$(this).find('option[value="default"]').prop('selected', true);
					}else{
						$(this).find('option').eq(0).prop('selected', true);
					}
				} else {
					$(this).find('option[value="' + varObjVal + '"]').prop('selected', true);
				}
			}
		});
		
		var varSelMultiData; 
		var varSelOptnData;
		$('#' + varFormId + ' select[multiple="multiple"]').each(function(i, item){
			var varObjName = $(this).attr('name');
			varSelMultiData = theMultiData[varObjName];
			varSelOptnData = theOptnData[varObjName];
			
			$(this).find('option').remove();
			if(typeof(varSelMultiData) != "undefined" && typeof(varSelOptnData) != "undefined") {
				var varObjId = $(this).attr('id');
				var varSelObj = $(this);
				var varOLen = varSelMultiData.length;
				$.each(varSelMultiData, function(vo, item){
					var varMatchObj = getMatchObject(varSelOptnData, item["itemKey"], "OPTION_CODE");
					if(varMatchObj) {
						varSelObj.append("<option value='" + varMatchObj["OPTION_CODE"] + "'>" + varMatchObj["OPTION_NAME"] + "</option>");
					}
				});
			}
		});
		/*
		 * 추가항목 setting 
		 */
		// 상단 메뉴위치
		var varMenuPath = fn_getTreePathName("", $("#fn_iaMenuUL"), $("#fn_leftMenu_" + theMenuInfo['menuIdx']));
		var varMenuHName = $("#fn_leftMenu_1>a").text();
		if(varMenuPath != '') varMenuPath = varMenuHName + ">" + varMenuPath.replace(/&gt;/gi, ">");
		else varMenuPath = varMenuHName;
		$('#top_ordIdx').val(theMenuInfo['menuIdx']);
		$('#top_menuLevel').val(theMenuInfo['menuLevel']);
		$('#menuPath').val(varMenuPath);
		
		// 메뉴번호
		$('#fn_item_menuIdx').text(theMenuInfo['menuIdx']);
		
		// 위치
		$('#ordIdx option[value="' + theMenuInfo['menuIdx'] + '"]').prop("selected", true);

		if(!fn_isValFill(varModuleId)) {
			// 1. 권한  : 권한정보 삭제, 미사용
			fn_itemModuleAuth.init();
			// 2. 기능 : 기능삭제, 미사용 / DesignType
			fn_itemModuleFn.init();	
			// 3. 콘텐츠 : 콘텐츠 / BranchIdx / BranchType
			fn_itemContents.hide();
			// 4. 메뉴경로 '기본값넣기' 버튼 비활성
			fn_itemMenuLinkBtn.init();
			// 5. 링크메뉴 활성화
			fn_itemMenuLinkIdx.init();
			// 5. 기능설정
			fn_itemFnSettingForm.init();
			// 6. 회원기능설정
			fn_itemMemberSettingForm.init();
			// 7. 통합검색 사용여부 비활성
			fn_itemTotSearch.init();
		} else {
			
			// 1. 기능권한
			fn_itemModuleAuth.set(theModuleAuthList, theMenuInfo['moduleAuth']);

			// 2. 기능
			fn_itemModuleFn.set(theModuleFnList, theMenuInfo['fnIdx']);
			if(varModuleId == "contents") {
				// 콘텐츠
				fn_itemModuleFn.hide();
				// 3. 콘텐츠
				fn_itemContents.set(theConList, theMenuInfo['contentsCode']);
				// 4. branch
				fn_itemBranch.set(theBranchList, theMenuInfo['branchIdx']);
			} else {
				// 일반기능
				fn_itemContents.hide();
			}
			// 5. 기능설정
			fn_itemFnSettingForm.setObject(theFnSettingList);
			// 6. 회원기능설정
			fn_itemMemberSettingForm.set();
			
			// 4. 메뉴경로 '기본값넣기' 버튼 비활성
			fn_itemMenuLinkBtn.init();
			// 5. 링크메뉴 활성화
			fn_itemMenuLinkIdx.init();
			
			var varTotSearchDisabled = true;
			if(varModuleId == "contents" || varModuleId == "board") {
				// 콘텐츠, 다기능게시판
				varTotSearchDisabled = false;
			}
			fn_itemTotSearch.set(varTotSearchDisabled, (theMenuInfo['useTotsearch'] == '1')?true:false);
		}
		
		// 디자인템플릿
		fn_itemDesign.set(theMenuInfo['urDesign'], theMenuInfo['anDesign']);
		
		try {
			if(theMenuInfo['isQRCode']) $('#qrCode').val(theMenuInfo['isQRCode']);
			else  $('#qrCode').val('');
		} catch(e){}
		
		// 위치구분
		fn_itemOrdType.init(varFormId);
	}
};
