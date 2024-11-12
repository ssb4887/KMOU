<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<link rel="stylesheet" href="<c:out value="${contextPath}/include/js/jquery/css/jquery-ui.css"/>" />
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-ui.js"/>"></script>
<script type="text/javascript">
$(function(){

   	fn_dialog_div.init("fn_resultCon", 10006, 800, 700);
	$(".fn_btn_view_opinion").click(function(){
		fn_dialog_div.open("의견보기", $(this).val() + "&nfm=1");
		return false;
	});

	<c:if test="${mngAuth && isAdmMode}">
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