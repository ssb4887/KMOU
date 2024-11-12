<%@ include file="../../../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="rwtAuth" value="${elfn:isAuth('RWT')}"/>
<c:set var="idxName" value="${settingInfo.idx_name}"/>
<c:set var="idxColumnName" value="${settingInfo.idx_column}"/>
<c:if test="${empty chkListDt}"><c:set var="chkListDt" value="${dt}"/></c:if>
<c:set var="listDtBrdIdx" value="${chkListDt[idxColumnName]}"/>
<c:set var="listDtPntIdx" value="${chkListDt['PNT_IDX']}"/>
<c:set var="listDtQnaCnt" value="${chkListDt['QNA_CNT']}"/>
<div class="btnTopFull">
	<div class="left">
		<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeA fn_btn_write">목록</a>
	</div>
	<c:set var="idxParam" value="&${idxName}=${listDtBrdIdx}"/>
	<div class="right">
		<c:set var="dsetInputBtn" value="${settingInfo.dset_input_btn}"/>
		<c:if test="${empty dsetInputBtn or dsetInputBtn eq '0' or (dsetInputBtn eq '1' and wrtAuth)}">
			<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTypeA fn_btn_write">등록</a>
			<c:if test="${elfn:isDisplayAuth(usrCrtMenu.fn_idx, listDtBrdIdx, 0, (settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') && (chkListDt['RE_LEVEL'] > 1) , chkListDt['REGI_IDX'], chkListDt['MEMBER_DUP'], chkListDt['PWD'])}">
				<a href="<c:out value="${URL_MODIFY}${idxParam}"/>" title="수정" class="btnTypeA fn_btn_modify"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listDtBrdIdx}"/>"</c:if>><spring:message code="item.modify.name"/></a>
				<a href="<c:out value="${URL_DELETEPROC}${idxParam}"/>" title="삭제" class="btnTypeB fn_btn_delete"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listDtBrdIdx}"/>"</c:if>><spring:message code="item.delete.name"/></a>
			</c:if>			
		</c:if>
		<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
		<c:if test="${((settingInfo.use_reply eq '1' and !useQna) or (useQna and listDtBrdIdx eq listDtPntIdx and listDtQnaCnt < 2)) and rwtAuth}">
			<a href="<c:out value="${URL_REPLY}${idxParam}"/>" title="답글" class="btnTypeB fn_btn_replywrite">답글</a>
		</c:if>
	</div>
</div>