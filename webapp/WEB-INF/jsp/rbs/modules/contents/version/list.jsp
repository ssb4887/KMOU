<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	// 콘텐츠 보기
	$(".fn_btn_workView").click(function(){
		try {
			<c:choose>
			<c:when test="${menuType == 2}">
			var varAction = "<c:out value="${pageContext.request.contextPath}${usrCrtMenu.menu_link}"/>&verIdx=" + $(this).attr("data-idx");
			fn_dialog.openF(varAction, "_blank");
			</c:when>
			<c:otherwise>
			fn_dialog.open("", $(this).attr("href") + "&dl=1", 0, 0, "fn_workViewDialog");
			</c:otherwise>
			</c:choose>
		}catch(e){}
		return false;
	});
});
</script>