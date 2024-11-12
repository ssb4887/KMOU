<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	fn_window.resizeTo(580);
	
	<c:choose>
		<c:when test="${param.type == '1'}">
			<c:set var="setModuleId" value="board"/>
		</c:when>
		<c:when test="${param.type == '2'}">
		<c:set var="setModuleId" value="contents"/>
	</c:when>
	</c:choose>
	$("#lnbWrap .info li>a[data-module='${setModuleId}']").parent("li").addClass("set");
	fn_setTreeParentClass("setp", $("#fn_contMenuUL"), $("#lnbWrap .info li>a[data-module='${setModuleId}']").parent("li"));
	
	$("#lnbWrap .info li>a").click(function(){
		var varLiObj = $(this).parent("li");
		if(varLiObj.hasClass("root") || !varLiObj.hasClass("set")) {
			//alert("<spring:message code="message.menuContents.main.no.match.menu"/>");
			return false;
		}
		fn_removeAllTreeOn($("#fn_contMenuUL .root>ul"));
		var varPathName = fn_getTreePathName("", $("#fn_contMenuUL"), varLiObj);
		$("#fn_menuPath dd").html(varPathName);
		$("#menu_idx").val($(this).attr("data-idx"));
		$("#menu_name").val($(this).text());
		$("#mdId").val($(this).attr("data-module"));
		$("#sfIdx").val($(this).attr("data-sf"));
		
	});
	
	// 선택
	$(".fn_btn_select").click(function(){
		if(!fn_isFill($("#menu_idx"))){
			alert(fn_Message.checkText("메뉴"));
			return false;
		}
		var varChkObjs = $("#menu_idx");
		opener.fn_setMenuInfo("<c:out value="${queryString.itemId}"/>", $("#menu_idx").val(), $("#menu_name").val(), $("#fn_menuPath dd").text(), $("#mdId").val(), $("#sfIdx").val());
		self.close();
	});
});
</script>