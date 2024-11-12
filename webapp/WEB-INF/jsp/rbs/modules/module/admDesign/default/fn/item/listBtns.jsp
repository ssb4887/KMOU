			<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
			<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
			<c:if test="${mngAuth}">
			<div class="left">
				<a href="<c:out value="${URL_DELETEPROC}"/>" title="삭제" class="btnTD fn_btn_delete">삭제</a>
				<a href="<c:out value="${URL_DELETE_LIST}"/>" title="휴지통" class="btnTDL fn_btn_deleteList">휴지통</a>
			</div>
			</c:if>
			<div class="right">
				<div class="resultCount">총 <strong>${fn:length(list)}</strong>건</div>
				<c:if test="${wrtAuth}">
				<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTW fn_btn_write${inputWinFlag}">등록</a>
				<a href="<c:out value="${URL_SEARCHINPUT}"/>" title="검색항목등록" class="btnTFEW fn_btn_write${inputWinFlag}">검색항목등록</a>
				</c:if>
			</div>
<%@ include file="../../../../../../adm/include/module/listBtnsScript.jsp" %>