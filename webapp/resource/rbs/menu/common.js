/*********************************
메뉴
*********************************/
$(function(){
	// 메뉴 트리 출력
	fn_initTree("C", $("#fn_iaMenuUL>li.root>ul"));
	
	// 선택메뉴 설정
	var varSelMenuIdx = 1;
	var varAnchor = location.hash;
	if(fn_isValFill(varAnchor)) {
		var varTmpMenuIdx = varAnchor.replace("#", "");
		
		var varTmpTreeMenuObj = $("#fn_leftMenu_" + varTmpMenuIdx);
		var varIsTmpTreeMenu = (varTmpTreeMenuObj.size() > 0);
		if(varIsTmpTreeMenu) varSelMenuIdx = varTmpMenuIdx;
	}
	
	var varSelTreeMenuObj = $("#fn_leftMenu_" + varSelMenuIdx);
	var varIsSelTreeMenu = (varSelTreeMenuObj.size() > 0);
	if(varIsSelTreeMenu) {
		fn_MenuTree.select(varSelMenuIdx, true);
		$("#fn_iaMenuUL li.fn_leftMenu_" + varSelMenuIdx + " .btn_tree").click();
		fn_setTreeOpen($("#fn_iaMenuUL>li.root>ul"), $("#fn_leftMenu_" + varSelMenuIdx));
	}
	
	// 메뉴 열기/닫기
	$(document).on("click", "#fn_iaMenuUL li .btn_tree", function(){
		fn_setTreeToggle($(this));
	});

	// 메뉴 전체 열기/닫기
	$('#fn_btn_menu_tree_open').click(function(){
		fn_setTreeAllToggle(($(this).prop("checked"))?"O":"C", $("#fn_iaMenuUL>li.root>ul"));
	});
	
	// 좌측메뉴 선택 : 메뉴정보 setting
	$(document).on('click', '#fn_iaMenuUL li>a', function(){
		var varMenuIdx;
		var varId = $(this).attr('href');
		if(!varId) return false;
		varMenuidx = varId.replace('#', '');
		fn_MenuTree.select(varMenuidx, true);
		//return false;
	});


	// 사용기능 선택
	$("#moduleId").change(function(){
		var varModuleId = $(this).find("option:selected").val();
		if(!fn_isValFill(varModuleId)) {
			fn_itemModuleAuth.init();
			fn_itemModuleFn.init();
			fn_itemContents.hide();
			//fn_itemBranch.init();
			fn_itemMenuLink.init();
			fn_itemMenuLinkIdx.init();
			fn_itemDesign.init();
			// 7. 통합검색 사용여부 비활성
			fn_itemTotSearch.init();
		} else {
			fn_itemModule.get(varModuleId);
		}
	});
	
	// 기능 선택
	$("#fnIdx").change(function(){
		var varFnIdxObj = $(this).find("option:selected");
		var varFnIdx = varFnIdxObj.val();
		fn_itemModuleFn.get(varFnIdx);
	});
	
	// 콘텐츠 선택
	$("#contentsCode").change(function(){
		var varContCd = $(this).find("option:selected").val();
		if(!fn_isValFill(varContCd)) {
			fn_itemBranch.init();
		} else {
			fn_itemContents.get(varContCd);
		}
	});
	
	// Branch 선택
	$("#branchIdx").change(function(){
		var varBranchObj = $(this).find("option:selected");
		var varBranchIdx = varBranchObj.val();
		if(!fn_isValFill(varBranchIdx)) fn_itemBranchType.init();
		else {
			var varBranchType = varBranchObj.attr("data-type");
			if(typeof(varBranchType) == "undefined") varBranchType = "";
			fn_itemBranchType.set(varBranchType);
		}
	});
	
	// 관리메뉴 선택 : 메뉴링크값 setting
	$('#menuLinkIdx').change(function(){
		var varLinkVal = $(this).find("option:selected").val();
		fn_itemMenuLinkIdx.set(varLinkVal);
	});
	
	// 메뉴경로 기본값 넣기
	$('#fn_btn_menuLink').click(function(){
		fn_itemMenuLink.setDefault($("#moduleId option:selected").val(), $("#designType").val());
	});
});

// 메뉴트리
var fn_MenuTree = {
	select:function(theMenuIdx, theFlag, theF) {
		fn_removeTreeOn($("#fn_iaMenuUL>li.root>ul"), $('#fn_leftMenu_' + this.menuIdx));
		//$('#fn_leftMenu_' + this.menuIdx).removeClass("on");
		$('#fn_leftMenu_' + theMenuIdx).addClass("on");
		fn_setMenuForm.get(theMenuIdx);

		if(!theFlag){ 
			fn_setTreeOpen($("#fn_iaMenuUL>li.root>ul"), $('#fn_leftMenu_' + theMenuIdx));
		}
		this.menuIdx = theMenuIdx;
	}
}