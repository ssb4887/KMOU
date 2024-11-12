<%@ include file="../../../include/commonTop.jsp"%>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<%//@ include file="../../../adm/include/module/listBtnsScript.jsp"%>
<script type="text/javascript">
$(function(){	
<c:if test="${mngAuth}">
// 휴지통
$(".fn_btn_branch_deleteList").click(function(){
	try {
		<c:choose>
			<c:when test="${!empty branch_deleteList_dialog_title}">
			var varTitle = "<c:out value="${branch_deleteList_dialog_title}"/>";
			</c:when>
			<c:otherwise>
			var varTitle = "<c:out value="${settingInfo.deleteList_title}"/>";
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty branch_deleteList_dialog_addTitle}">
		varTitle += " (<c:out value="${branch_deleteList_dialog_addTitle}"/>)";
		</c:if>
		fn_dialog.open(varTitle, $(this).attr("href"), 0, 0, "fn_branchDeleteList");
	}catch(e){/*alert(e);*/}
	return false;
});

// 삭제
$(".fn_btn_branch_delete").click(function(){
	try {
		fn_branchDeleteFormSubmit("<c:out value="${branchListFormId}"/>", $(this).attr("href"));
	}catch(e){}
	return false;
});
</c:if>

<c:if test="${wrtAuth}">
// 등록/수정
$(".fn_btn_branch_write_open<c:if test="${mngAuth}">, .fn_btn_branch_modify_open</c:if>").click(function(){
	try {
		<c:choose>
			<c:when test="${!empty branch_input_dialog_title}">
			var varTitle = "<c:out value="${branch_input_dialog_title}"/>";
			</c:when>
			<c:otherwise>
			var varTitle = "<c:out value="${settingInfo.input_title}"/>";
			<c:if test="${mngAuth}">
			if($(this).hasClass('fn_btn_branch_modify_open')) varTitle += " 수정";
			else </c:if>varTitle += " 등록";
			</c:otherwise>
		</c:choose>
		<c:if test="${!empty branch_input_dialog_addTitle}">
		varTitle += " (<c:out value="${branch_input_dialog_addTitle}"/>)";
		</c:if>
		fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 0<c:if test="${!empty branch_input_dialog_height}">, ${branch_input_dialog_height}</c:if>);
	}catch(e){}
	return false;
});
</c:if>
});


<c:if test="${mngAuth}">
//삭제
function fn_branchDeleteFormSubmit(theFormId, theAction){
	try {
		if(!fn_isValFill(theFormId) || !fn_isValFill(theAction)) return false;
		// 삭제여부
		var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
		if(!varConfirm) return false;
		
		$("#" + theFormId).attr("action", theAction);
		$("#" + theFormId).submit();
	}catch(e){}
	
	return true;
}
</c:if>
</script>