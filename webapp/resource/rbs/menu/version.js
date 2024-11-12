/*********************************
버전
*********************************/
$(function(){
	// 관리사이트 버전 변경
	$("#fn_searchSVerForm").submit(function(){
		if(!fn_checkSelected($('#verIdx'), "버전")) return false;
		if(!confirm(fn_Message.verChangeConfirmText)) return false;
		return true;
	});
	
	// 버전관리 새창
	$("#fn_btn_version").click(function(){
		try {
			var varTitle = "";
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0, 900);
		}catch(e){}
		return false;
	});
});