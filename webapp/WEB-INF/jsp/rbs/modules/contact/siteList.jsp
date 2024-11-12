<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	$("#is_statsType").change(function(){
		var varVal = $(this).find("option:selected").val();
		if(varVal != "") {
			var varNum = new Number(varVal);
			for(var i = 0 ; i < varNum ; i ++) {
				$("#is_statsDate" + (i + 1)).prop("disabled", false);
				$("#is_statsDate" + (i + 1)).css('opacity', '1');
			}
			for(var i = varNum ; i <= 3 ; i ++) {
				$("#is_statsDate" + (i + 1)).prop("disabled", true);
				$("#is_statsDate" + (i + 1)).css('opacity', '0.5');
			}
		}
	});
	
	$("#is_statsType").change();
	
	$("#${param.searchFormId}").submit(function(){
		try{if(!fn_checkSelected($("#is_statsType"), $("label[for='is_statsType']").text())) return false;}catch(e){}
		var varVal = $("#is_statsType").find("option:selected").val();
		if(varVal != "") {
			var varNum = new Number(varVal);
			for(var i = 0 ; i < varNum ; i ++) {
				try{if(!fn_checkSelected($("#is_statsDate" + (i + 1)), $("label[for='is_statsDate" + (i + 1) + "']").text())) return false;}catch(e){}
			}
		}
	});
});
</script>