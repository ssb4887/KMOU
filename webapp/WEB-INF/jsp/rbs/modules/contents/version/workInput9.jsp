<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemInfo" value="${moduleItem.work_9_item_info}"/>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_boardInputReset();
	
	<c:if test="${verDt.WORK_TYPE == '10' || verDt.WORK_TYPE == '11'}">
	$("#fn_editorIF_workContents").load(function(){
		$(this).contents().find("#se2_tool").hide();
		$(this).contents().find(".se2_converter").hide();
	});
	</c:if>
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			location.href="${elfn:replaceScriptLink(URL_WORKLIST)}";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
	
	// 콘텐츠 보기
	$(".fn_btn_workView").click(function(){
		try {
			<c:choose>
			<c:when test="${menuType == 2}">
			var varAction = "<c:out value="${pageContext.request.contextPath}${usrCrtMenu.menu_link}"/>&verIdx=<c:out value="${queryString.verIdx}"/>";
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

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_createMaskLayer();
	return true;
}
</script>