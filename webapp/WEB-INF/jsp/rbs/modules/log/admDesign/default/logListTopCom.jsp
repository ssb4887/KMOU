	<!-- button -->
	<div class="btnTopFull">
			<div class="right">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
				<select id="lunit" name="lunit" class="select" title="목록수">
				<c:forEach var="listUnit" begin="${paginationInfo.unitBeginCount}" end="${paginationInfo.unitEndCount}" step="${paginationInfo.unitStep}">
					<option value="${listUnit}"<c:if test="${listUnit == paginationInfo.recordCountPerPage}"> selected="selected"</c:if>>${listUnit}개씩 조회</option>
				</c:forEach>
				</select>
				<button data-url="<c:out value="${URL_LIMIT_LIST}"/>" title="적용" class="btnTypeF fn_btn_lunit">적용</button>
				<a href="<c:out value="${URL_EXCELDOWN}"/>" target="list_target" title="엑셀다운로드" class="btnTFDL fn_btn_exceldown">엑셀다운로드</a>
			</div>
	</div>
	<!-- //button -->