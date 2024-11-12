<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){

	$(".fn_pop_view").click(function(){
		try {
			var varTitle = $(this).attr("data-title");
			var varProperties = $(this).attr("data-properties");
			if(varProperties) varProperties += ", scrollbars=yes";
			fn_dialog.openP(varTitle, $(this).attr("href"), "fn_pop_win", varProperties);
		}catch(e){}
		return false;
	});
});
</script>