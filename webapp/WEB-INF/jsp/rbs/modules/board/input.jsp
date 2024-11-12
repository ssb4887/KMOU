<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel, .qna_dele").click(function(){
		try {
			location.href="${elfn:replaceScriptLink(URL_LIST)}";
		}catch(e){return false;}
	});
	
	
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
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
	<c:if test="${param.isNoMemberAuthPage}">
	try{
		<spring:message var="pwdItemName" code="item.board.password"/>
		<c:choose>
		<c:when test="${param.mode == 'm' && (!isAdmMode || !mngAuth)}">
		<spring:message var="prePwdItemName" code="item.pre_password.name" arguments="${pwdItemName}"/>
		if(!fn_checkFill($("#pre_pwd"), "${prePwdItemName}")) return false;
		</c:when>
		<c:otherwise>
		if(!fn_checkFill($("#pwd"), "${pwdItemName}")) return false;
		</c:otherwise>
		</c:choose>
		if(!fn_checkStrLenForm($("#pwd"), "${pwdItemName}", 4, 16)) return false;
	}catch(e){}
	</c:if>
	fn_createMaskLayer();
	return true;
}
</script>
<%@ include file="replyList.jsp"%>
<%@ include file="password.jsp"%>