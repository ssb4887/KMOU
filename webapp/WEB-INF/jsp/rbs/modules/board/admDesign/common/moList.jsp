<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputWinFlag" value="_view"/>		
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
var varSource = '';
varSource += '<div class="btnTopFull">';
varSource += '<div class="right">';
varSource += '<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>';
varSource += '<a href="<c:out value="${URL_IDX_MEMOINPUT}"/>" title="등록" class="btnTW fn_btn_write${inputWinFlag}">등록</a>';
varSource += '</div>';
varSource += '</div>';
varSource += '<table class="tbListA" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <spring:message code="item.delete.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>을 제공하는 표">';
varSource += '<caption><c:out value="${settingInfo.list_title}"/> 댓글 목록</caption>';
varSource += '<colgroup>';
varSource += '<col width="50px" />';
varSource += '<col width="60px" />';
varSource += '<col width="60px" />';
varSource += '<col />';
varSource += '<col width="100px" />';
varSource += '<col width="100px" />';
varSource += '</colgroup>';
varSource += '<thead>';
varSource += '<tr>';
varSource += '<th scope="col"><spring:message code="item.no.name"/></th>';
varSource += '<th scope="col"><spring:message code="item.modify.name"/></th>';
varSource += '<th scope="col"><spring:message code="item.delete.name"/></th>';
varSource += '<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/></th>';
varSource += '<th scope="col"><spring:message code="item.reginame1.name"/></th>';
varSource += '<th scope="col" class="end"><spring:message code="item.regidate1.name"/></th>';
varSource += '</tr>';
varSource += '</thead>';
varSource += '<tbody class="alignC">';
<c:if test="${empty list}">
varSource += '<tr>';
varSource += '<td colspan="6" class="bllist"><spring:message code="message.no.list"/></td>';
varSource += '</tr>';
</c:if>
<c:set var="listIdxName" value="${settingInfo.memo_idx_name}"/>
<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
<c:forEach var="listDt" items="${list}" varStatus="i">
<c:set var="listBrdKey" value="${listDt[settingInfo.idx_column]}"/>
<c:set var="listKey" value="${listDt[settingInfo.memo_idx_column]}"/>
varSource += '<tr>';
varSource += '<td class="num">${listNo}</td>';
<c:choose>
<c:when test="${elfn:isDisplayAuth(usrCrtMenu.fn_idx, listBrdKey, listKey, false, listDt['REGI_IDX'], listDt['MEMBER_DUP'], listDt['PWD'])}">
varSource += '<td><a href="<c:out value="${URL_IDX_MEMOMODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_modify${inputWinFlag}"><spring:message code="item.modify.name"/></a></td>';
varSource += '<td><a href="<c:out value="${URL_IDX_MEMODELETEPROC}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_delete${inputWinFlag}"><spring:message code="item.delete.name"/></a></td>';
</c:when>
<c:otherwise>
varSource += '<td></td>';
varSource += '<td></td>';
</c:otherwise>
</c:choose>
varSource += '<td class="tlt" style="white-space:pre-line !important;">';
varSource += '<c:out value="${elfn:getNLinetoBR(listDt.CONTENTS)}"/>';
varSource += '<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>';
varSource += '</td>';
varSource += '<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>';		
varSource += '<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>';
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
		try {
			// 삭제여부
			var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;
		}catch(e){}
		return true;
	});

	// 등록/수정
	$(".fn_btn_write_view, .fn_btn_modify_view").click(function(){
		var varAction = $(this).attr('href');
		try {
			$.ajax({
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');}, 
				url:varAction,
				success:function(data){
					eval(data.replace(/<br\/>/g, '\\n'));
				}
			});
		}catch(e){alert(e);}
		return false;
	});
	$('.fn_btn_write_view').trigger('click');
});