	<c:if test="${settingInfo.use_rss eq '1'}">
	<div class="fl mgt15"><a href="${URL_DEFAULT_LIST}&rss=2.0" target="_blank" class="rss">RSS FEED</a></div>
	</c:if>
	
	<c:choose>
		<c:when test="${settingInfo.use_paging == '1'}">
			<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
		</c:when>
		<c:otherwise>
			<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
		</c:otherwise>
	</c:choose>
