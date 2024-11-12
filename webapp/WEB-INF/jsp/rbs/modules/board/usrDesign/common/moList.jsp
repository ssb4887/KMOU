<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputWinFlag" value="_view"/>		
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
<c:set var="conMmgUTP"><spring:message code="Globals.code.USERTYPE_ADMIN"/></c:set>
<c:set var="wrtUTPAuth" value="${elfn:getModuleUTP('MWT')}"/>

var varSource = '';
varSource += '<div class="btnTopFull">';
varSource += '<div class="right">';
varSource += '<div class="resultCount mgb5">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>';
varSource += '</div>';
varSource += '</div>';
varSource += '<table class="tbListA" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>을 제공하는 표">';
varSource += '<caption><c:out value="${settingInfo.list_title}"/> 댓글 목록</caption>';
varSource += '<colgroup>';
varSource += '<col />';
varSource += '<col width="100px" />';
varSource += '<col width="70px" />';
varSource += '<col width="70px" />';
varSource += '</colgroup>';
varSource += '<tbody class="alignC">';
<c:if test="${empty list}">
varSource += '<tr>';
varSource += '<td colspan="4" class="bllist"><spring:message code="message.no.list"/></td>';
varSource += '</tr>';
</c:if>
<c:set var="listIdxName" value="${settingInfo.memo_idx_name}"/>
<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
<c:forEach var="listDt" items="${list}" varStatus="i">
<c:set var="listBrdKey" value="${listDt[settingInfo.idx_column]}"/>
<c:set var="listKey" value="${listDt[settingInfo.memo_idx_column]}"/>
varSource += '<tr>';
varSource += '<td class="tlt" style="white-space:pre-line !important;">';
varSource += '<c:out value="${elfn:getNLinetoBR(listDt.CONTENTS)}"/>';
varSource += '<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>';
varSource += '</td>';
varSource += '<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/><br/>(<fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/>)</td>';
<c:if test="${wrtUTPAuth < conMmgUTP}">
<c:choose>
<c:when test="${elfn:isDisplayAuth(crtMenu.fn_idx, listBrdKey, listKey, false, listDt['REGI_IDX'], listDt['MEMBER_DUP'], listDt['PWD'])}">
<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage('MWT')}"/>
varSource += '<td><a href="<c:out value="${URL_IDX_MEMOMODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_modify${inputWinFlag}"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listBrdKey}"/>|<c:out value="${listKey}"/>"</c:if>><spring:message code="item.modify.name"/></a></td>';
varSource += '<td><a href="<c:out value="${URL_IDX_MEMODELETEPROC}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_delete${inputWinFlag}"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listBrdKey}"/>|<c:out value="${listKey}"/>"</c:if>><spring:message code="item.delete.name"/></a></td>';
</c:when>
<c:otherwise>
varSource += '<td></td>';
varSource += '<td></td>';
</c:otherwise>
</c:choose>
</c:if>
varSource += '</tr>';
<c:set var="listNo" value="${listNo - 1}"/>
</c:forEach>
varSource += '</tbody>';
varSource += '</table>';
varSource += '<div class="paginate mgt15">';
<c:set var="paging"><pgui:pagination listUrl="${URL_IDX_MEMOLIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.memo_page_name, 'page')}"/></c:set>
varSource += '${elfn:getInline(paging)}';
varSource += '</div>';
$('#${listFormId}').html(varSource);
$(function(){
	fn_dialog.init("fn_list");
	
	$('.paginate .page-num a').click(function(){
		$.get($(this).prop('href'), function(data){
			eval(data.replace(/&lt;br\/&gt;/g, '\\n'));
		});
		return false;
	});
	
	// 삭제
	$(".fn_btn_delete${inputWinFlag}").click(function(){
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
	
	// 등록/수정
	$(".fn_btn_modify${inputWinFlag}").click(function(){
		var varIsNMChk = $(this).attr("data-nm");
		var varAction = $(this).attr('href');
		if(fn_isValFill(varIsNMChk)) {
			<%-- 비회원 글쓰기/댓글쓰기 권한 --%>
			<c:choose>
				<c:when test="${elfn:isNoMemberAuthPage('MWT')}">
					if(confirm('비밀번호를 입력하시겠습니까?'))
						fn_showPwdDialog(varIsNMChk, varAction);
					else {
						$(".fn_btn_write_view").click();
					}
				</c:when>
				<c:otherwise>
					fn_showPwdDialog(varIsNMChk, varAction);
				</c:otherwise>
			</c:choose>
		} else {
			try {
				getAjaxMemoInputForm(varAction);
			}catch(e){alert(e);}
		}
		return false;
	});
});

function getAjaxMemoInputForm(varAction){

	$.ajax({
		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');}, 
		url:varAction,
		success:function(data){
			eval(data.replace(/<br\/>/g, '\\n'));
		}
	});
}