<%@ include file="../../../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="rwtAuth" value="${elfn:isAuth('RWT')}"/>
<c:set var="conMmgUTP"><spring:message code="Globals.code.USERTYPE_ADMIN"/></c:set>
<c:set var="wrtUTPAuth" value="${elfn:getModuleUTP('WRT')}"/>
<c:set var="rwtUTPAuth" value="${elfn:getModuleUTP('RWT')}"/>
<c:set var="idxName" value="${settingInfo.idx_name}"/>
<c:set var="idxColumnName" value="${settingInfo.idx_column}"/>
<c:choose><c:when test="${!empty param.listDtBrdIdx}"><c:set var="listDtBrdIdx" value="${param.listDtBrdIdx}"/></c:when><c:otherwise><c:set var="listDtBrdIdx" value="${dt[idxColumnName]}"/></c:otherwise></c:choose>
<c:choose><c:when test="${!empty param.listDtPntIdx}"><c:set var="listDtPntIdx" value="${param.listDtPntIdx}"/></c:when><c:otherwise><c:set var="listDtPntIdx" value="${dt['PNT_IDX']}"/></c:otherwise></c:choose>
<c:choose><c:when test="${!empty param.listDtQnaCnt}"><c:set var="listDtQnaCnt" value="${param.listDtQnaCnt}"/></c:when><c:otherwise><c:set var="listDtQnaCnt" value="${dt['QNA_CNT']}"/></c:otherwise></c:choose>
<div data-role="controlgroup" data-type="horizontal">
	<c:set var="idxParam" value="&${idxName}=${listDtBrdIdx}"/>
	<c:set var="dsetInputBtn" value="${settingInfo.dset_input_btn}"/>
	<c:if test="${wrtUTPAuth lt conMmgUTP and (empty dsetInputBtn or dsetInputBtn eq '0' or (dsetInputBtn eq '1' and wrtAuth))}">
		<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTypeA fn_btn_write" data-role="button" data-ajax="false">등록</a>
		<c:if test="${elfn:isMngProcAuth(settingInfo, crtMenu.fn_idx, listDtBrdIdx, 0)}">
		<a href="<c:out value="${URL_MODIFY}${idxParam}"/>" title="수정" class="btnTypeA fn_btn_modify" data-role="button" data-ajax="false">수정</a>
		<a href="<c:out value="${URL_DELETEPROC}${idxParam}"/>" title="삭제" class="btnTypeA fn_btn_delete" data-role="button" data-ajax="false">삭제</a>
		</c:if>
	</c:if>
	<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
	<c:if test="${settingInfo.use_reply eq '1' and !useQna and rwtAuth and rwtUTPAuth lt conMmgUTP}">
	<a href="<c:out value="${URL_REPLY}${idxParam}"/>" title="답글" class="btnTypeA fn_btn_replywrite" data-role="button" data-ajax="false">답글</a>
	</c:if>
	<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeC fn_btn_write" data-role="button" data-ajax="false">목록</a>
</div>