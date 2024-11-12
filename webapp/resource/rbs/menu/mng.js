/*********************************
관리 담당 정보
*********************************/
$(function(){
	// 관리 담당 정보  입력폼 열기
	$("#bt_open_mng").click(function() { 
		$("input[name='mngMenuIdx']").removeClass('fn_skip');
		var varDivObj = $(this).attr('data-target');
		
		var varTop = $("#infoMng").css("top");
		var varLeft = $("#infoMng").css("left");
		var varWidth = $(".inWfullContWrap").width() + 10;
		var varHeight = $(".inWfullContWrap").height();
		
		var varTHeight = $("#lnbWrap .info").height() + 20;
		if(varHeight < varTHeight) varHeight = varTHeight;
		fn_dialog_layer.open($("#lnbWrap .info"), "fn_dialog_layer", "z-index:999;top:" + varTop + ";left:" + varLeft + ";width:" + varWidth + "px;height:" + varHeight + "px;");
		$(varDivObj).removeClass('fn_skip');
		$('#fn_menu_select_all').removeClass('fn_skip');

		$("#fn_btn_menu_tree_open").prop("checked", true);
		fn_setTreeAllToggle("O", $("#fn_iaMenuUL>li.root>ul"));
		return false;
	});

	// 관리 담당 정보  입력폼 닫기
	$("#infoMng .btn_close").click(function() { 
		$("input[name='mngMenuIdx']").addClass('fn_skip');
		var varDivObj = $(this).attr('data-target');
		$(varDivObj).addClass('fn_skip');
		fn_dialog_layer.close("fn_dialog_layer");
		
		var varMenuTreeOpenChecked = $('#fn_btn_menu_tree_open').prop('checked');
		
		$("#fn_dmngForm").reset();
		
		$('#fn_btn_menu_tree_open').prop('checked', varMenuTreeOpenChecked);
		$('#fn_menu_select_all').addClass('fn_skip');
		return false;
	});

	// 메뉴 전체 선택/해제
	$('#fn_btn_menu_select_all').click(function(){
		var varChecked = $(this).prop('checked');
		$("#fn_iaMenuUL input[name='mngMenuIdx']").prop('checked', varChecked);
		if(varChecked) { 
			var varOpenObj = $('#fn_btn_menu_tree_open');
			varOpenObj.prop('checked', true);
			fn_setTreeAllToggle("O", $("#fn_iaMenuUL>li.root>ul"));
		}
	});

	// 관리 담당 정보 cancel
	$("#fn_dmngForm .fn_btn_cancel").click(function(){
		try {
			$("#fn_dmngForm").reset();
			//$("#infoMng .btn_close").click();
		}catch(e){return false;}
	});
	
	// 관리 담당 정보 저장
	$('#fn_dmngForm').submit(function(event){
		try{
			var varDmngChecked = $('#all_useDmng').prop('checked');
			if(varDmngChecked && !fn_checkFill($('#all_dmngName'), $('#all_dmngName').attr('title'))) return false;
		}catch(e){}

		
		try {
			if(!fn_checkElementChecked("mngMenuIdx", "메뉴")) return false;
		}catch(e){}

		return true;
	});
});