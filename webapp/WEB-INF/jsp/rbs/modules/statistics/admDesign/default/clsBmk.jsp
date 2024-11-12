<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/clsBmk.jsp"/>
	</jsp:include>
</c:if>
<div class="">
	<ul class="tabTypeA tab li4" style="margin-bottom:40px;">
		<li class="fn_tab_view on"><a id="tab1" href="${URL_CLASS_BOOKMART}">관심강좌 찜수</a></li>
		<li class="fn_tab_view"><a id="tab2" href="${URL_CLASS_MOST_BOOKMART}">관심강좌 인기 찜</a></li>
	</ul>
</div>

<div id="cms_board_article" class="subConBox">

	<div class="tbSearch tbShowDt">
		<form id="searchForm" action="<c:out value="${URL_CLASS_BOOKMART_DATA}"/>" method="post" target="submit_target" style="display: inline-block;">
			<dl>
				<dt>
					<label for="statsType">검색옵션</label>
				</dt>
				<dd>
					<select id="searchStatsType" name="searchStatsType" class="select">
						<option value="1">연도별</option>
						<option value="2" selected>월별</option>
						<option value="3">일별</option>
						<option value="4">시간별</option>
					</select>
				</dd>
				<dt>
					<label for="is_statsDate">검색일자</label>
				</dt>
				<dd>
					<select id="searchStatsDate1" name="searchStatsDate1" class="select">
						<option value="">년 선택</option>
					</select>년
					<select id="searchStatsDate2" name="searchStatsDate2" class="select" disabled>
						<option value="">월 선택</option>
					</select>월
					<select id="searchStatsDate3" name="searchStatsDate3" class="select" disabled>
						<option value="">일 선택</option>
					</select>일
				</dd>
			</dl>
			<input type="button" class="btnSearch" value="검색" title="검색" onclick="getData();">
		</form>
	</div>

    <div class="subBox">
        <canvas id="chartBox"></canvas>
    </div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>