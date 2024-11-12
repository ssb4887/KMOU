<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){	
	$("#fn_admMenuUL a").click(function(){
		var varHref = $(this).attr("href");
		if(varHref == "#" || varHref == '') return false;
		$("#fn_comment").hide();
		$("#fn_iframe_input").show();
		var varObj = $(this);
		$("#fn_iframe_input").load(function(){
			$("#fn_admMenuUL li").removeClass("on");
			varObj.parent().addClass("on");
		});
		/*var varUrl = $(this).attr("href");
		
		return false;*/
	});
});
</script>