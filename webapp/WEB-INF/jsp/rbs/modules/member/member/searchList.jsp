<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	fn_window.resizeTo(850);
	
	// 전체 선택/해제 change
	$("#selectAll").change(function(){
		try {
			$("input[name='select']").prop("checked", $(this).prop("checked"));
			if(fn_setAllSelectObjs) fn_setAllSelectObjs(this);
		}catch(e){}
	});
	
	// 선택
	$(".fn_btn_select").click(function(){
		if(!fn_checkElementChecked("select", "회원")) return false;
		var varChkObjs = $("input[name='select']:checked");
		opener.fn_setMemberMmbrInfo("<c:out value="${queryString.itemId}"/>", varChkObjs);
		self.close();
	});
});
</script>