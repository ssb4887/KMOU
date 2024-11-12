/*****************************************
 * moduleId
 *****************************************/
var fn_itemModule = {
	id:"#moduleId"
	, init:function(){
		fn_itemModuleAuth.init();
		fn_itemModuleFn.init();
		fn_itemContents.init();
		fn_itemMenuLink.init();
		fn_itemDesign.init();
		fn_itemTotSearch.init();
	}, get:function(theModuleId){
		var varAction = this.infoUrl + "&moduleId=" + theModuleId;
		$.ajax({
	  		type:'POST', 
	  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  		dataType:'json', 
	  		url:varAction, 
	  		async:true, 
	  		success:function(result){
				fn_itemModuleAuth.set(result.moduleAuthList);
				fn_itemModuleFn.set(result.moduleFnList);
				if(theModuleId == "contents") {
					// 콘텐츠
					fn_itemModuleFn.hide();
					fn_itemContents.set(result.contentsList);
				} else {
					// 일반기능
					fn_itemContents.hide();
				}
				fn_itemMenuLink.setDefault(theModuleId);
				fn_itemMenuLinkBtn.init();
				fn_itemMenuLinkIdx.init();
				fn_itemDesign.init();
				
				var varTotSearchDisabled = true;
				if(theModuleId == "contents" || theModuleId == "board") {
					// 콘텐츠, 다기능게시판
					varTotSearchDisabled = false;
				}
				fn_itemTotSearch.set(varTotSearchDisabled);
	  		}, 
	  		error:function(request,error){
	  			fn_ajax.checkError(request);
	  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	  		}
	  	});
	}
}

/*****************************************
 * moduleAuth
 *****************************************/
fn_itemModuleAuth = {
	id:"#moduleAuth"
	, gId:"#fn_moduleAuth"
	, init:function(){
		// 기능 삭제, 비활성, designType 값 삭제
		var varObjId = this.id;
		$(varObjId + " option:gt(0)").remove();
		$(varObjId).prop("disabled", true);
		var varRequiredItem = $(this.gId).hasClass("fn_requried_item");
		if(varRequiredItem) $("label[for='" + varObjId.replace("#", '') + "']").removeClass("required");
	}, set:function(theData, theVal){
		var varObjId = this.id;
		$(varObjId + " option:gt(0)").remove();
		var varRequiredItem = $(this.gId).hasClass("fn_requried_item");
		// 권한정보 - 사용
		if(theData && $(theData).size() > 0) {
			$(varObjId).prop("disabled", false);
			if(varRequiredItem) $("label[for='" + varObjId.replace("#", '') + "']").addClass("required");
			$.each(theData, function(i, item){
				$(varObjId).append("<option value='" + item.optionCode + "'>" + item.optionName + "</option>");
			});
			if(typeof(theVal) != 'undefined' && theVal != '') $(varObjId + " option[value='" + theVal + "']").prop("selected", true);
		} else {
			$(varObjId).prop("disabled", true);
			if(varRequiredItem) $("label[for='" + varObjId.replace("#", '') + "']").removeClass("required");
		}
	}
}

/*****************************************
 * fnIdx
 *****************************************/
var fn_itemModuleFn = {
	id:"#fnIdx"
	, init:function(){
		// 기능 삭제, 비활성, designType 값 삭제
		var varObjId = this.id;
		$(varObjId + " option:gt(0)").remove();
		$(varObjId).prop("required", false);
		$(varObjId).show();
		
		fn_itemDesignType.init();
		fn_itemDesign.init();
		fn_itemFnSettingForm.init();

		var varConfModule = $("#moduleId option:selected").attr("data-confMd");
		$("#confModule").val((typeof(varConfModule)== 'undefined')?'':varConfModule);
	}, hide:function(){
		var varObjId = this.id;
		fn_itemDesignType.init();
		fn_itemDesign.init();
		$(varObjId).find("option:eq(1)").prop("selected", true);
		$(varObjId).prop("required", false);
		$(varObjId).hide();
	}, get:function(theFnIdx){
		// 기능목록 얻기
		var varObjId = this.id;
		fn_itemDesignType.init();
		fn_itemDesign.init();
		var varModuleId = $("#moduleId option:selected").val();
		if(varModuleId != "contents" && !fn_isValFill(theFnIdx)) {
			fn_itemMenuLink.setDefault(varModuleId);
			fn_itemFnSettingForm.init();
			return false; 
		}
		var varAction = this.infoUrl + "&moduleId=" + varModuleId + "&fId=" + theFnIdx;
		$.ajax({
	  		type:'POST', 
	  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  		dataType:'json', 
	  		url:varAction, 
	  		async:true, 
	  		success:function(result){
				var varFnIdxObj = $(varObjId).find("option:selected");
				var varDesignType = varFnIdxObj.attr("data-type");
				fn_itemDesignType.set(varDesignType);
				var varDesign = (typeof(result.settingInfo.design) == 'undefined')?'':result.settingInfo.design;
				fn_itemDesign.set(varDesign, varDesign);
				if(varModuleId == "board") fn_itemMenuLink.setDefault(varModuleId, varDesignType);
				fn_itemFnSettingForm.set(result.settingList, result.settingInfo);
	  		}, 
	  		error:function(request,error){
	  			fn_ajax.checkError(request);
	  		}
	  	});
	}, set:function(theData, theVal, theSettingList, theSettingInfo){
		var varObjId = this.id;
		fn_itemModuleFn.init();
		$(varObjId).show();
		if(theData) {
			$(varObjId).prop("disabled", false);
			$(varObjId).prop("required", true);
			$.each(theData, function(i, item){
				var varOptStr = "<option value='" + item.optionCode + "'";
				if(typeof(item.optionType) != 'undefined') varOptStr += " data-type='" + item.optionType + "'";
				varOptStr += ">" + item.optionName + "</option>";
				$(varObjId).append(varOptStr);
			});
			if(typeof(theVal) != 'undefined' && theVal != '') {
				$(varObjId + " option[value='" + theVal + "']").prop("selected", true);
				var varDesignType = $(varObjId + " option[value='" + theVal + "']").attr("data-type");

				$("#designType").val(varDesignType);
				fn_itemFnSettingForm.set(theSettingList, theSettingInfo);
			}
		}
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
		var varIsDisplay = false;
		$.each(theFnSettingList, function(index, item){
			$('.fn_' + item).removeClass('hidden');
			var varObj = $('.fn_' + item).find("input");
			var varObjType = varObj.attr("type");
			if(typeof(theSettingInfo) != "undefined" && typeof(theSettingInfo[item]) != "undefined") {
				var varDVal = theSettingInfo[item];
				if(typeof(varDVal) == "undefined") varDVal = "";
				if(varObjType == "checkbox") {
					/*if(varDVal == "1") varObj.prop("checked", true);
					else*/ varObj.prop("checked", false);
				} else {
					varObj.filter("[type='text']").val(varDVal);
				}
			} else {
				if(varObjType == "checkbox") varObj.prop("checked", false);
				else varObj.filter("[type='text']").val("");
			}
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
	}, set:function() {
		var varSetObj = $(this.id);
		var varModuleId = $('#moduleId option:selected').val();
		if(varModuleId == 'member') varSetObj.removeClass('hidden');
		else varSetObj.addClass('hidden');
	}
}

/*****************************************
 * 콘텐츠
 *****************************************/
var fn_itemContents = {
	id:"#contentsCode"
	, init:function(){
		var varObjId = this.id;
		$(varObjId + " option:gt(0)").remove();
		fn_itemContentsType.init();
		fn_itemBranch.init();
	}, hide:function(){
		fn_itemContents.init();
		var varObjId = this.id;
		$(varObjId).prop("disabled", true);
		$(varObjId).hide();
		fn_itemBranch.hide();
	}, get:function(theContCd){
		// contentsType, Branch 목록 얻기
		var varAction = fn_itemModuleFn.infoUrl + "&moduleId=" + $("#moduleId option:selected").val() + "&contCd=" + theContCd;
		$.ajax({
	  		type:'POST', 
	  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  		dataType:'json', 
	  		url:varAction, 
	  		async:true, 
	  		success:function(result){
				// contentsType setting
				fn_itemContentsType.set(result.contentsType);
				// Branch 목록 setting
	  			fn_itemBranch.set(result.branchList);
	  		}, 
	  		error:function(request,error){
	  			fn_ajax.checkError(request);
	  		}
	  	});
	}, set:function(theData, theVal){
		// contentsCode 목록, 값 setting / contentsType setting
		var varObjId = this.id;
		$(varObjId).show();
		$(fn_itemBranch.id).show();
		$(varObjId + " option:gt(0)").remove();
		if(theData) {
			$(varObjId).prop("disabled", false);
			$.each(theData, function(i, item){
				$(varObjId).append("<option value='" + item.optionCode + "' data-type='" + item.optionType + "'>" + item.optionName + "</option>");
			});
			if(typeof(theVal) != 'undefined' && theVal != '') {
				$(varObjId + " option[value='" + theVal + "']").prop("selected", true);
				var varContentsType = $(varObjId + " option[value='" + theVal + "']").attr("data-type");
				fn_itemContentsType.set(varContentsType);
			}
		}
	}
}

/*****************************************
 * contentsType
 *****************************************/
var fn_itemContentsType = {
	init:function(){
		$("#contentsType").val('');
	}, set:function(theContentsType){
		$("#contentsType").val(theContentsType);
	}
}

/*****************************************
 * branchIdx
 *****************************************/
var fn_itemBranch = {
	id:"#branchIdx"
	, init:function(){
		// branch 목록 삭제, branchType 삭제
		var varObjId = this.id;
		$(varObjId + " option:gt(0)").remove();
		fn_itemBranchType.init();
	}, hide:function(){
		fn_itemBranch.init();
		var varObjId = this.id;
		$(varObjId).prop("disabled", true);
		$(varObjId).hide();
	}, set:function(theData, theVal){
		// branchIdx 목록, 값 setting
		var varObjId = this.id;
		$(varObjId).show();
		$(varObjId + " option:gt(0)").remove();
		if(theData) {
			$(varObjId).prop("disabled", false);
			$.each(theData, function(i, item){
				$(varObjId).append("<option value='" + item.optionCode + "' data-type='" + item.optionType + "'>" + item.optionName + "</option>");
			});
			if(typeof(theVal) != 'undefined' && theVal != '') {
				$(varObjId + " option[value='" + theVal + "']").prop("selected", true);
				var varBranchType = $(varObjId + " option[value='" + theVal + "']").attr("data-type");
				fn_itemBranchType.set(varBranchType);
			}
		}
	}
}

/*****************************************
 * branchType
 *****************************************/
var fn_itemBranchType = {
	init:function(){
		$("#branchType").val('');
	}, set:function(theBranchType){
		$("#branchType").val(theBranchType);
	}
}

/*****************************************
 * designType
 *****************************************/
var fn_itemDesignType = {
	id:"#designType"
	, init:function(){
		var varObjId = this.id;
		$(varObjId).val('');
	}, set:function(theDesignType){
		var varObjId = this.id;
		$(varObjId).val(theDesignType);
	}
}



/*****************************************
 * 디자인
 *****************************************/
var fn_itemDesign = {
	init:function(){
		// 삭제
		$("#urDesign").val('');
		$("#urDesignName").val('');
		$("#anDesign").val('');
	}, set:function(theUrDesign, thAnDesign){
		$("#urDesign").val(theUrDesign);
		$("#urDesignName").val(theUrDesign);
		$("#anDesign").val(thAnDesign);
	}
}

/*****************************************
 * 메뉴경로 
 *****************************************/
var fn_itemMenuLink = {
	init:function(){
		$("#menuLink").val('');
		$("#anMenuLink").val('');
		fn_itemMenuLinkBtn.init();
		fn_itemMenuLinkIdx.init();
	}, setDefault:function(theModuleId, theDesignType){
		// 모듈 기본 경로 setting : 모듈정보의 기본경로 setting
		if(typeof(theModuleId) == 'undefined' || theModuleId == '') return false;
		var varMenuIdx = $("#menuIdx").val();
		var varModuleObj = $("#moduleId option[value='" + theModuleId + "']");
		var varUrl, varAnUrl;
		if(theDesignType == "memo") {
			varUrl = varModuleObj.attr("data-link2");
			varAnUrl = varModuleObj.attr("data-anlink2");
		} else {
			varUrl = varModuleObj.attr("data-link");
			varAnUrl = varModuleObj.attr("data-anlink");
		}
		$("#menuLink").val(this.rUrl + varUrl + varMenuIdx);
		$("#anMenuLink").val(this.rUrl + varAnUrl + varMenuIdx);
	}, set:function(theUrl, theAnUrl){
		// 전체 경로 setting
		var varUrl = this.rUrl + theUrl;
		$("#menuLink").val(varUrl);
		if(typeof(theAnUrl) != "undefined") varUrl = this.rUrl + theAnUrl;
		$("#anMenuLink").val(varUrl);
	}
}

/*****************************************
 * 메뉴경로 '기본값넣기'버튼
 *****************************************/
var fn_itemMenuLinkBtn = {
	id:"#fn_btn_menuLink"
	, init:function(){
		var varModuleId = $("#moduleId option:selected").val();
		var varObjId = this.id;
		if(fn_isValFill(varModuleId)) {
			// 기능 사용하는 경우
			$(varObjId).prop('disabled', false);
			$(varObjId).css('opacity', '1');
		} else {
			// 기능 사용하지 않는 경우
			$(varObjId).prop('disabled', true);
			$(varObjId).css('opacity', '0.5');
		}
	}, set:function(theMenuIdx, theLinkIdx){
		var varModuleId = $("#moduleId option:selected").val();
		var varObjId = this.id;
		if(!fn_isValFill(varModuleId) || theLinkIdx != theMenuIdx) {
			// 버튼 비활성화 : 기능 사용하지 않는 경우 || 다른 메뉴링크
			//alert('다른 메뉴 콘텐츠 관리');
			$(varObjId).prop('disabled', true);
			$(varObjId).css('opacity', '0.5');
		} else {
			// 버튼 활성화 : 자신 메뉴 콘텐츠 관리
			$(varObjId).prop('disabled', false);
			$(varObjId).css('opacity', '1');
		}
	}
}

/******************************************
링크메뉴 설정 setting
******************************************/
var fn_itemMenuLinkIdx = {
	id:"#menuLinkIdx"
	, init:function(){
		var varModuleId = $("#moduleId option:selected").val();
		var varObjId = this.id;
		if(fn_isValFill(varModuleId)) {
			// 기능 사용하는 경우
			$(varObjId).prop("disabled", true);
			$(".fn_menuLinkIdx option[value='" + $("#menuIdx").val() + "']").prop("selected", true);
			$(".fn_menuLinkIdx").hide();
		} else {
			// 기능 사용하지 않는 경우
			if(!fn_itemFormSubmit.isDisabled) $(varObjId).prop("disabled", false);
			$(".fn_menuLinkIdx").show();
		}
	}, set:function(theMenuIdx){
		fn_itemMenuLinkIdx.init();
		var varObjId = this.id;
		var varModuleId = $("#moduleId option:selected").val();
		if(typeof(theMenuIdx) != "undefined") {
			$(varObjId + " option[value=" + theMenuIdx + "]").prop("selected", true);
		}
		var varMenuLinkObj = $(varObjId).find('option:selected');
		var varMenuLinkVal = varMenuLinkObj.val();
		var varMenuIdxVal = $('#menuIdx').val();
		fn_itemMenuLinkBtn.set(varMenuLinkVal, varMenuIdxVal);

		// 메뉴링크값 setting
		var varMenuLink = varMenuLinkObj.attr('data-link');
		$('#menuLink').val(varMenuLink);
		var varAnMenuLink = varMenuLinkObj.attr('data-anlink');
		$('#anMenuLink').val(varAnMenuLink);
		/*if(varMenuIdxVal == theMenuIdx) $(".fn_anMenuLink").show();
		else $(".fn_anMenuLink").hide();*/
	}, setBtn:function(theLinkIdx, theMenuIdx){
		if(theLinkIdx != theMenuIdx) {
			// 다른 메뉴 콘텐츠 관리
			//alert('다른 메뉴 콘텐츠 관리');
			$('#fn_btn_menuLink').prop('disabled', true);
			$('#fn_btn_menuLink').css('opacity', '0.5');
		} else {
			// 자신 메뉴 콘텐츠 관리
			$('#fn_btn_menuLink').prop('disabled', false);
			$('#fn_btn_menuLink').css('opacity', '1');
		}
	}
}

/*****************************************
 * 통합검색
 *****************************************/
var fn_itemTotSearch = {
	init:function(){
		// 미사용
		$("#useTotSearch").prop('disabled', true);
	}, set:function(theDisabled, theChked){
		// 사용/미사용
		$("#useTotSearch").prop('disabled', theDisabled);
		if(typeof(theChked) != "undefined") {
			$("#useTotSearch").prop('checked', theChked);
		}
	}
}

/*******************************************
위치구분 reset
*******************************************/
var fn_itemOrdType = {
	init:function(theFormId, theObjName){
		var varSelVal = $('#menuIdx').val();
		if(typeof(theGubun) == 'undefined') theGubun = "";
		var varIdx = $('#' + theFormId + ' input[name="' + theObjName + '"]').filter("[data-default='1']").val();
		if(varSelVal == '' || varSelVal == '1') varIdx = 3;
		$('#' + theObjName + varIdx).prop('checked', true);
		$('#top_ordType' + varIdx).prop('checked', true);
	}, check:function(theForm){
		var varThisType = $(theForm).find('input[name="ordType"]:checked');
		var varThisTypeVal = $(theForm).find('input[name="ordType"]:checked').val();
		var varIsOrderType = false;
		var varMessage;
		var varMenuIdx = $('#menuIdx').val();
		var varOrdIdx = $('#ordIdx').val();
		var varCMObjs = $("#fn_leftMenu_" + varMenuIdx).find("li>input[name='mngMenuIdx']");
		var varParentMenuIdx;
		if(varMenuIdx != '1') varParentMenuIdx = $("#fn_leftMenu_" + varMenuIdx).parent("ul").parent("li").find(">input[name='mngMenuIdx']").val();//$('#parentMenuIdx option:selected').val();
		else varParentMenuIdx = '';
		// 메뉴와 위치가 동일할 경우
		if(varMenuIdx != varOrdIdx){
			// 메뉴 : 최상위
			if(varMenuIdx == '1'){
				varIsOrderType = true;
			}else{
				// 하위메뉴로 이동여부 체크
				$.each(varCMObjs, function(i, item){
					var varCVal = $(this).val();
					if(varCVal == varOrdIdx) {
						// 하위메뉴로 이동하는 경우
						varIsOrderType = true;
						return false;
					}
				});
				if(!varIsOrderType) {
					// 위치구분 : 위
					if(varThisTypeVal != '3'){
						// 위치 : 최상위
						if(varOrdIdx == '1'){
							varIsOrderType = true;
							varMessage = "최상위메뉴의 위나 아래로 이동하실 수 없습니다.";
						}
					}
				}
			}
		}
		if(varIsOrderType){
			if(typeof(varMessage) == "undefined") varMessage = "해당메뉴의 하위메뉴로 이동하실 수 없습니다.";
			alert(varMessage);
			return false;
		}
		return true;
	}
}

/******************************************
form submit 버튼 설정 setting
******************************************/
var fn_itemFormSubmit = {
	disabled:function(){
		//fn_MenuTree.addForm.hide();
		var varAddFormSet = fn_MenuTree.addForm.find("fieldset");
		varAddFormSet.find(">*").hide();
		varAddFormSet.find("label[for='menuPath']").show();
		varAddFormSet.find("#menuPath").show();
		//fn_MenuTree.addForm.find("input[type='submit']").css('opacity', '0.5');
		fn_MenuTree.setForm.find("input[type='submit']").css('opacity', '0.5');
		var varBtnObj = fn_MenuTree.setForm.find("button")
		varBtnObj.prop("disabled", true);
		varBtnObj.css('opacity', '0.5');

		//fn_MenuTree.addForm.find("input").filter("[type!='submit']").prop("disabled", true);
		fn_MenuTree.setForm.find("input").filter("[type!='submit']").prop("disabled", true);
		fn_MenuTree.setForm.find("textarea").prop("disabled", true);
		fn_MenuTree.setForm.find("select").prop("disabled", true);
		
		this.isDisabled = true;
	}
}