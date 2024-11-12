<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	
	// 게시물관리
	$(".fn_btn_boardList").click(function(){
		try {
			var varTitle = $(this).attr("title");
			fn_dialog.open(varTitle, $(this).attr("href") + "&mdl=1&mt=" + encodeURIComponent(varTitle), 0, 800, "fn_moduleFnMng_dialog");
		}catch(e){}
		return false;
	});

	<spring:message var="msgItemSettingName" code="item.setting.name"/>
	<spring:message var="msgItemItemsName" code="item.items.name"/>
	// 설정관리
	$(".fn_btn_setting").click(function(){
		try {
			var varTitle = $(this).attr("title");
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0, 800, "fn_moduleFnSet_dialog");
		}catch(e){}
		return false;
	});
	// 항목관리
	$(".fn_btn_items").click(function(){
		try {
			var varTitle = $(this).attr("title");
			fn_dialog.open(varTitle, $(this).attr("href") + "&mdl=1&mt=" + encodeURIComponent(varTitle), 0, 800, "fn_moduleFnItems_dialog");
		}catch(e){}
		return false;
	});
	
	// 적용
	$(".fn_btn_apply").click(function(){
		try {
			fn_dialog.openF($(this).attr("href"), "list_target");
		}catch(e){}
		return false;
	});

});
</script>