<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<script type="text/javascript">
$(function(){	
	// 상세보기
	$(".fn_btn_view").click(function(){
		try {
			var varTitle = $(this).attr("attr-title");
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 550, 350);
		}catch(e){}
		return false;
	});	
	// 참여자목록보기
	$(".fn_btn_memberList").click(function(){
		try {
			var varTitle = $(this).attr("attr-title");
			fn_dialog.open(varTitle, $(this).attr("href") + "&mdl=1");
		}catch(e){}
		return false;
	});	
});
</script>
