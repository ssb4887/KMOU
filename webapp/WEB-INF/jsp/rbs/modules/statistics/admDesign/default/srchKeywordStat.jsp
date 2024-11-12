<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/srchKeywordStat.jsp"/>
	</jsp:include>
</c:if>

<div id="cms_board_article" class="subConBox">
	<div class="tabMenuC">
		<ul class="tabTypeA tab li4" style="margin-bottom: 40px;">
			<li class="fn_tab_view" id="tab1" title="검색 건수 추이">
				<a href="${URL_SRCH_CNT_STAT}">검색 건수 추이</a>
			</li>
			<li class="fn_tab_view" id="tab2" title="검색 사용자 추이">
				<a href="${URL_SRCH_USR_STAT}">검색 사용자 추이</a>
		 	</li>
			<li class="fn_tab_view on" id="tab3" title="검색어 통계">
				<a href="${URL_SRCH_KEYWORD_STAT}">검색어 통계</a>
		 	</li>
		</ul>
	</div>
	<div class="tbMSearch">
		<form id="searchForm" action="<c:out value="${URL_SRCH_KEYWORD_STAT}"/>" method="post" style="width: 100%;">
		<input type="hidden" name="SEL_RANGE" value="">
			<fieldset>
				<dl>
					<dt>
						<label for="">소속</label>
					</dt>
					<dd class="lw">
						<select id="selectColg" class="select" title="대학" >
						</select>
						<select id="selectDept" class="select" title="학부/학과" >
							<option value="">학부(과)</option>
						</select>
						<select id="selectMj" class="select" title="전공">
							<option value="">전공</option>
						</select>
					</dd>
				</dl>
				<dl>
					<dt>
						<label for="">기간조회</label>
					</dt>
					<dd class="lw">
						<div class="input-group">
							<c:choose>
								<c:when test="${!empty START_DATE && !empty END_DATE}">
									<c:set var="stDt" value="${START_DATE}"/>
									<c:set var="edDt" value="${END_DATE}"/>
								</c:when>
								<c:otherwise>
									<c:set var="stDt" value="${param.searchStDt}"/>
									<c:set var="edDt" value="${param.searchEdDt}"/>
								</c:otherwise>
							</c:choose>
							<input type="text" name="searchStDt" id="searchStDt" title="검색일자" readonly="readonly" style="width:110px;" value="${stDt}">
							<span><button type="button" class="btnCal" id="btnSearchStDt" title="달력선택">달력선택</button></span>
							<span class="input-group-sp">~</span>
							<input type="text" name="searchEdDt" id="searchEdDt" title="검색일자" readonly="readonly" style="width:110px;" value="${edDt}">
							<span><button type="button" class="btnCal" id="btnSearchEdDt" title="달력선택">달력선택</button></span>
						</div>
					</dd>
					<dt>
						<label for="">대상자</label>
					</dt>
					<dd class="sw">
						<select id="searchUserType" name="searchUserType" class="select">
							<option value="">전체</option>
							<option value="5"  <c:if test="${param.searchUserType eq '5' }">selected</c:if>>학생</option>
							<option value="45" <c:if test="${param.searchUserType eq '45'}">selected</c:if>>교수</option>
							<option value="46" <c:if test="${param.searchUserType eq '46'}">selected</c:if>>조교</option>
							<option value="47" <c:if test="${param.searchUserType eq '47'}">selected</c:if>>직원</option>
						</select>
					</dd>
				</dl>
				<dl>
					<dt>
						<label for="">키워드 검색</label>
					</dt>
					<dd>
						<input type="text" name="searchKeyword" id="searchKeyword" style="width:100%;" placeholder="검색어를 입력해 주세요" value="${param.searchKeyword}">
					</dd>
				</dl>
			</fieldset>
			<p style="padding-bottom: 15px;">
				<button type="submit" id="fn_btn_search" class="btnSearch" title="검색">검색</button>
			</p>
		</form>
	</div>

	<table class="tbListA tbContact">
		<colgroup>
			<col width="70px" />
			<col width="160px" />
			<col />
			<col width="100px" />
			<col width="100px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">검색어</th>
				<th scope="col">그래프</th>
				<th scope="col">검색수</th>
				<th scope="col">백분율(%)</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty LIST}">
			<tr>
				<td colspan="5" class="bllist">검색어 목록이 없습니다.</td>
			</tr>
			</c:if>
			<c:set var="listNo" value="${PAGINATIONINFO.firstRecordIndex + 1}" />
			<c:forEach var="listDt" items="${LIST}" varStatus="i">
				<tr>
					<td scope="row" class="num ct"><c:out value="${listNo}"/></td>
					<td scope="row" class="tlt lt" style="text-align:center;"><c:out value="${listDt.KEYWORD}"/></td>
					<td scope="row" class="graph">
						<div class="graph_bg">
							<p class="bar" style="width: ${listDt.PERS}%;"></p>
						</div>
					</td>
					<td scope="row" class="num rt"><c:out value="${listDt.COUNT}"/>건</td>
					<td scope="row" class="num rt"><fmt:formatNumber value="${listDt.PERS}" pattern="#.0" minIntegerDigits="1"/></td>
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
			</c:forEach>
		</tbody>
	</table>

	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_SRCH_KEYWORD_STAT}${searchList}" pgInfo="${PAGINATIONINFO}" usePaging="1" imgPath="${imgPath}" pageName="page"/>
	</div>
	<!-- //paging -->
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>