<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<script type="text/javascript">
$(function(){

	<c:if test="${mngAuth}">
	// 참여자 목록
	$(".fn_btn_resplist_open").click(function(){
		try {
			var varTitle = "";
			fn_dialog.open(varTitle, $(this).attr("href") + "&mdl=1");
		}catch(e){}
		return false;
	});
	</c:if>
});
</script>