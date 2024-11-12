<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<script type="text/javascript">
$(function(){	
	<c:if test="${param.dl == '1' && addViews}">
	<% /* 팝업으로 사용시 조회수 출력 */ %>
	try {
		$(opener.document).find("#fn_brd_views<c:out value="${dt.BRD_IDX}"/>").html("<c:out value="${dt.VIEWS}"/>");
	} catch(e) {}
	</c:if>
	

	<c:if test="${mngAuth}">
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;
		}catch(e){return false;}
		return true;
	});
	</c:if>
});
</script>