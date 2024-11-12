<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	fn_boardInputReset();
	
	// 좌우 이동
	$(".fn_setSearchItem .right, .fn_setSearchItem .left").click(function(){
		var varFromObj = $("#" + $(this).attr("data-from"));
		var varToObj = $("#" + $(this).attr("data-to"));
		
		fn_moveLROptions(varFromObj, varToObj, false);
	});
	
	// 아래위 이동
	$(".fn_setSearchItem .up, .fn_setSearchItem .down").click(function(){
		var varObj = $("#" + $(this).attr("data-target"));
		var varFlag = ($(this).hasClass("up"))?"1":"2";
		fn_moveUDOptions(varObj, varObj.attr("title"), varFlag, 0);
	});
	
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
			location.href="${elfn:replaceScriptLink(URL_LIST)}";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
});

function fn_boardInputReset(){
}
function fn_boardInputFormSubmit(){
	try{$("select[name='searchItemKey'] option").prop("selected", true);}catch(e){}
	try{$("select[name='searchItemKeyField'] option").prop("selected", true);}catch(e){}
	fn_createMaskLayer();
	return true;
}
</script>