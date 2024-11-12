			<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
			<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
			<c:if test="${mngAuth}">
<!-- 			<div class="left"> -->
<%-- 				<a href="<c:out value="${URL_DELETEPROC}"/>" title="삭제" class="btnTD fn_btn_delete">삭제</a> --%>
<%-- 				<a href="<c:out value="${URL_DELETE_LIST}"/>" title="휴지통" class="btnTDL fn_btn_deleteList">휴지통</a> --%>
<!-- 			</div> -->
			</c:if>
			<div class="right">
				<c:choose>
				<c:when test="${settingInfo.use_paging == '1'}">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
				<select id="lunit" name="lunit" class="select" title="목록수">
				<c:forEach var="listUnit" begin="${paginationInfo.unitBeginCount}" end="${paginationInfo.unitEndCount}" step="${paginationInfo.unitStep}">
					<option value="${listUnit}"<c:if test="${listUnit == paginationInfo.recordCountPerPage}"> selected="selected"</c:if>>${listUnit}개씩 조회</option>
				</c:forEach>
				</select>
				<button data-url="<c:out value="${URL_PLANSAMPLELIST}"/>" title="적용" class="btnTDL fn_btn_lunit">적용</button>
				</c:when>
				<c:otherwise>
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
				</c:otherwise>
				</c:choose>
				<c:if test="${wrtAuth}">
<%-- 				<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTW fn_btn_write${inputWinFlag}">등록</a> --%>
				<c:if test="${!empty URL_EXCELINPUT}">
<%-- 				<a href="<c:out value="${URL_EXCELINPUT}"/>" title="엑셀등록" class="btnTEW fn_btn_excelwrite">엑셀등록</a> --%>
				</c:if>
				</c:if>
				<c:if test="${!empty addBtnScript}">${addBtnScript}</c:if>
			</div>
<%@ include file="listBtnsScript.jsp" %>
