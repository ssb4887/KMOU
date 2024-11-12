/*********************************
메뉴
*********************************/
$(function(){
	// 메뉴 트리 출력
	fn_initTree("C", $("#fn_iaMenuUL>li.root>ul"));
	
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
		return false;
	});
});

// 메뉴트리
var fn_MenuTree = {
	select:function(theMenuIdx, theFlag, theF) {
		$('#fn_leftMenu_' + this.menuIdx).removeClass("on");
		$('#fn_leftMenu_' + theMenuIdx).addClass("on");
		fn_setMenuForm.get(theMenuIdx);

		if(!theFlag){ 
			fn_setTreeOpen($("#fn_iaMenuUL>li.root>ul"), $('#fn_leftMenu_' + theMenuIdx));
		}
		this.menuIdx = theMenuIdx;
	}
}