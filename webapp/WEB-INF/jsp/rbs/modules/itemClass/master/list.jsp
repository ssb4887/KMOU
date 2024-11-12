<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	<c:set var="vewAuth" value="${elfn:isAuth('VEW')}"/>
	<c:if test="${vewAuth}">
	// 상세조회
	$(".fn_btn_view").click(function(){
		try {
			var varTitle = "<c:out value="${moduleSetting.optn_setting_info.list_title}"/>";
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0, 0, "fn_code_view_dialog");
		}catch(e){}
		return false;
	});
	</c:if>
	
	// 코드관리
	$(".fn_btn_optnlist").click(function(){
		try {
			var varTitle = "<c:out value="${moduleSetting.optn_setting_info.list_title}"/> 관리 (" + $(this).attr("attr-title") + ")";
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0, 800);
		}catch(e){}
		return false;
	});
	
});
</script>