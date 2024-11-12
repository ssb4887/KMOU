			<div class="right">
				<c:choose>
				<c:when test="${settingInfo.use_paging == '1'}">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>				
				</c:when>
				<c:otherwise>
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
				</c:otherwise>
				</c:choose>
			</div>