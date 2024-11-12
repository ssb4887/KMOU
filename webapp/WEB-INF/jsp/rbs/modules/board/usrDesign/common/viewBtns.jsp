<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="rwtAuth" value="${elfn:isAuth('RWT')}"/>
<c:set var="conMmgUTP"><spring:message code="Globals.code.USERTYPE_ADMIN"/></c:set>
<c:set var="conNmUTP"><spring:message code="Globals.code.USERTYPE_SMEMBER"/></c:set>
<c:set var="wrtUTPAuth" value="${elfn:getModuleUTP('WRT')}"/>
<c:set var="rwtUTPAuth" value="${elfn:getModuleUTP('RWT')}"/>
<c:set var="idxName" value="${settingInfo.idx_name}"/>
<c:set var="idxColumnName" value="${settingInfo.idx_column}"/>
<c:if test="${empty chkListDt}"><c:set var="chkListDt" value="${dt}"/></c:if>
<c:set var="listDtBrdIdx" value="${chkListDt[idxColumnName]}"/>
<div class="listBtn">
	<c:set var="idxParam" value="&${idxName}=${listDtBrdIdx}"/>
	<c:set var="dsetInputBtn" value="${settingInfo.dset_input_btn}"/>
	<c:if test="${(!empty crtModuleAuth.WRT_GRP || wrtUTPAuth lt conMmgUTP) and (empty dsetInputBtn or dsetInputBtn eq '0' or (dsetInputBtn eq '1' and wrtAuth))}">
		<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTypeA fn_btn_write">등록</a>
		<c:if test="${(settingInfo.use_qna == '1' and dt.REPLY_STATE == '0' or settingInfo.use_qna != '1') && elfn:isDisplayAuth(crtMenu.fn_idx, listDtBrdIdx, 0, (settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') && (chkListDt['RE_LEVEL'] > 1) , chkListDt['REGI_IDX'], chkListDt['MEMBER_DUP'], chkListDt['PWD'])}">
			<%-- 비밀번호 입력창 display 여부 --%>
			<c:set var="chkAuthName" value="WRT"/>
			<c:if test="${(settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and chkListDt.RE_LEVEL > 1}">
				<c:set var="chkAuthName" value="RWT"/>
			</c:if>
			<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage(chkAuthName)}"/>
			<a href="<c:out value="${URL_MODIFY}${idxParam}"/>" title="수정" class="btnTypeA fn_btn_modify"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listDtBrdIdx}"/>"</c:if>>수정</a>
			<a href="<c:out value="${URL_DELETEPROC}${idxParam}"/>" title="삭제" class="btnTypeA fn_btn_delete"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listDtBrdIdx}"/>"</c:if>>삭제</a>
		</c:if>
	</c:if>
	<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
	<c:if test="${settingInfo.use_reply eq '1' and !useQna and (rwtAuth or rwtUTPAuth eq conNmUTP) and rwtUTPAuth lt conMmgUTP}">
		<a href="<c:out value="${URL_REPLY}${idxParam}"/>" title="답글" class="btnTypeA fn_btn_replywrite">답글</a>
	</c:if>
	<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeC fn_btn_list">목록</a>
</div>