<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="memoItemOrderName" value="${memoSubmitType}proc_order"/>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
<c:choose><c:when test="${!empty param.listDtBrdIdx}"><c:set var="listDtBrdIdx" value="${param.listDtBrdIdx}"/></c:when><c:otherwise><c:set var="listDtBrdIdx" value="${dt[idxColumnName]}"/></c:otherwise></c:choose>
<script type="text/javascript">
$(function(){	
	<c:if test="${param.dl == '1' && addViews}">
	<% /* 팝업으로 사용시 조회수 출력 */ %>
	try {
		$(opener.document).find("#fn_brd_views<c:out value="${dt.BRD_IDX}"/>").html("<c:out value="${dt.VIEWS}"/>");
	} catch(e) {}
	</c:if>
	
	// 수정
	$(".fn_btn_modify").click(function(){
		var varIsNMChk = $(this).attr("data-nm");
		if(fn_isValFill(varIsNMChk)) {
			<%-- 비회원 글쓰기/댓글쓰기 권한 --%>
			fn_showPwdDialog(varIsNMChk, $(this).attr("href"));
			return false;
		}
			
		return true;
	});
	
	// 삭제
	$(".fn_btn_delete").click(function(){
		var varIsNMChk = $(this).attr("data-nm");
		if(fn_isValFill(varIsNMChk)) {
			<%-- 비회원 글쓰기/댓글쓰기 권한 --%>
			fn_showPwdDialog(varIsNMChk, $(this).attr("href"));
			return false;
		} else {
			try {
				var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
				if(!varConfirm) return false;
			}catch(e){return false;}
			return true;
		}
	});
	<c:if test="${settingInfo.use_memo eq '1'}">
		$.get('${URL_IDX_MEMOLIST}', function(data){
			eval(data.replace(/&lt;br\/&gt;/g, '\\n'));
		});

		// 등록/수정
		$(".fn_btn_write_view").click(function(){
			var varAction = $(this).attr('href');
			try {
				$.get(varAction, function(data){
					eval(data.replace(/<br\/>/g, '\\n'));
				});
			}catch(e){alert(e);}
			return false;
		});
		$('.fn_btn_write_view').trigger('click');
	</c:if>
});
</script>
<%@ include file="viewList.jsp"%>
<%@ include file="replyList.jsp"%>
<%@ include file="password.jsp"%>
<c:if test="${settingInfo.use_filecomt eq '1'}">
<%@ include file="fileComment.jsp"%>
	</c:if>