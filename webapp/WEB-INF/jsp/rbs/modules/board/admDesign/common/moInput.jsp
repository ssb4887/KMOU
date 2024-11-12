<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${empty noMemberAuthName}">
	<c:set var="noMemberAuthName" value="MWT"/>
</c:if>
<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage(noMemberAuthName)}"/>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="itemInputAll"><itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order"/></c:set>
<c:set var="itemInputAll" value="${elfn:getNLinetoBR(itemInputAll)}"/>
var varSource = '';
<c:if test="${isNoMemberAuthPage}">
	varSource += '<tr>';
	varSource += '<th><label for="pwd" class="required"><spring:message code="item.board.password"/></label></th>';
	varSource += '<td><input type="password" id="pwd" name="pwd" class="inputTxt" maxlength="16" required="required" title="<spring:message code="item.board.password"/>"/></td>';
	varSource += '</tr>';
</c:if> 
varSource += '${itemInputAll}';
$("#<c:out value="${inputFormId}"/> table tbody").html(varSource);
$("#<c:out value="${inputFormId}"/>").prop('action', '${elfn:replaceScriptLink(URL_SUBMITPROC)}');
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			location.href="${elfn:replaceScriptLink(URL_LIST)}";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){return false;}
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
