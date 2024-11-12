<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	$("input[name='is_statsType']").click(function(){
		var varVal = $(this).val();
		
		fn_setCurrentDateObj(varVal, $("#is_statsDate1"), $("#is_statsDate2"));
	});
	
	/*$("#${param.searchFormId}").submit(function(){
			var varNum = 1;
			for(var i = 0 ; i < varNum ; i ++) {
				try{if(!fn_checkSelected($("#is_statsDate" + (i + 1)), $("label[for='is_statsDate" + (i + 1) + "']").text())) return false;}catch(e){}
			}
		}
	});*/
});
</script>