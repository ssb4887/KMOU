<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	$("input[name='is_statsType']").click(function(){
		var varVal = $(this).val();
		
		fn_setCurrentDateObj(varVal, $("#is_statsDate1"), $("#is_statsDate2"));
	});
	
	// 목록수 적용
	$(".fn_btn_lunit").click(function(){
		location.href=$(this).attr("data-url") + "&lunit=" + $("#lunit option:selected").val();
	});
});
</script>