<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/srchPoint.jsp"/>
	</jsp:include>
</c:if>

<div id="cms_board_article" class="subConBox">
	<div class="tbMSearch">
		<c:set var="searchList" value=""/>
		<div class="satis_num">평균 <c:out value="${pointAvrg}"/></div>
		<div class="satis_grap">
			<div class="grap">
				<table class="tbListA tbContact">
					<colgroup>
						<col width="120px" />
						<col />
					</colgroup>
					<tbody>
						<c:forEach var="data" items="${pointTypeGraph}">
							<tr>
								<td scope="row" class="num ct"><c:out value="${data.POINT_NAME}"/></td>
								<td scope="row" class="graph" style="height:47px;">
									<div class="graph_bg" style="height:25px; border-radius:15px;">
										<div class="bar" style="width: calc(${data.POINT_TYPE_PER}% + 25px); height: 100%; text-indent: 0px; text-align: right; color: #fff; font-weight: normal; font-size: 14px; padding: 5px; background:var(--colorset-bg-bt4);"><c:out value="${data.POINT_TYPE_CNT}"/>건</div>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div class="grap">
				<table class="tbListA tbContact">
					<colgroup>
						<col width="120px" />
						<col />
					</colgroup>
					<tbody>
						<c:forEach var="data" items="${pointGraph}">
							<tr>
								<td scope="row" class="num ct"><c:out value="${data.POINT}"/></td>
								<td scope="row" class="graph">
									<div class="graph_bg" style="height:25px; border-radius:15px;">
										<div class="bar" style="width: calc(${data.POINT_PER}% + 25px); height: 100%; text-indent: 0px; text-align: right; color: #fff; font-weight: normal; font-size: 14px; padding: 5px;"><c:out value="${data.POINT_CNT}"/>건</div>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="tbMSearch mt20">
		<form id="${searchFormId}" action="<c:out value="${URL_SRCH_POINT}"/>" method="post" style="width: 100%;">
			<input type="hidden" name="searchFlag" value="1">
			<input type="hidden" name="SEL_RANGE" value="">
			<fieldset>
				<dl>
					<dt>
						<label for="">대상자</label>
					</dt>
					<dd class="sw" style="width:45%;">
						<select id="selectColg" class="select" title="대학" >
						</select>
						<select id="selectDept" class="select" title="학부/학과" >
							<option value="">학부(과)</option>
						</select>
						<select id="selectMj" class="select" title="전공">
							<option value="">전공</option>
						</select>
					</dd>
					<dt>
						<label for="">조회기간</label>
					</dt>
					<dd style="width:30%;">
						<div class="input-group">
							<c:choose>
								<c:when test="${!empty startDate && !empty endDate}">
									<c:set var="stDt" value="${startDate}"/>
									<c:set var="edDt" value="${endDate}"/>
								</c:when>
								<c:otherwise>
									<c:set var="stDt" value="${param.searchStDt}"/>
									<c:set var="edDt" value="${param.searchEdDt}"/>
								</c:otherwise>
							</c:choose>
							<input type="text" name="searchStDt" id="searchStDt" title="검색일자" readonly="readonly" style="width:110px; text-align: center;" value="${stDt}">
							<span><button type="button" class="btnCal" id="btnSearchStDt" title="달력선택">달력선택</button></span>
							<span class="input-group-sp">~</span>
							<input type="text" name="searchEdDt" id="searchEdDt" title="검색일자" readonly="readonly" style="width:110px; text-align: center;" value="${edDt}">
							<span><button type="button" class="btnCal" id="btnSearchEdDt" title="달력선택">달력선택</button></span>
						</div>
					</dd>
				</dl>
				<dl>
					<dt>만족도 유형</dt>
					<dd style="width:16.2%">
						<select name="pointType" id="pointType" class="select" title="점수">
							<option value="">전체</option>
							<option value="ACCURACY" <c:if test="${param.pointType eq 'ACCURACY' }">selected</c:if>>검색정확도</option>
							<option value="QUALITY" <c:if test="${param.pointType eq 'QUALITY' }">selected</c:if>>검색정보의 양질</option>
							<option value="CONVENIENCE" <c:if test="${param.pointType eq 'CONVENIENCE' }">selected</c:if>>사용의 편의성</option>
							<option value="ETC" <c:if test="${param.pointType eq 'ETC' }">selected</c:if>>기타</option>
						</select>						
					</dd>
					<dt>점수</dt>
					<dd>
						<select name="point" id="point" class="select" title="점수">
							<option value="">전체</option>
							<option value="1" <c:if test="${param.point eq '1' }">selected</c:if>>1점</option>
							<option value="2" <c:if test="${param.point eq '2' }">selected</c:if>>2점</option>
							<option value="3" <c:if test="${param.point eq '3' }">selected</c:if>>3점</option>
							<option value="4" <c:if test="${param.point eq '4' }">selected</c:if>>4점</option>
							<option value="5" <c:if test="${param.point eq '5' }">selected</c:if>>5점</option>
						</select>
					</dd>
					<dt>
						<label for="">키워드 검색</label>
					</dt>
					<dd style="width:16%">
						<input type="text" name="searchKeyword" id="searchKeyword" readonly="readonly" style="width:100%;">
					</dd>
				</dl>
			</fieldset>
			<p style="padding-bottom: 15px;">
				<button type="submit" id="fn_btn_search" class="btnSearch" title="검색">검색</button>
			</p>
		</form>
	</div>

	<table class="tbListA mt40">
		<caption>No., 대상자구분, 검색구분, 이름, 만족도 유형, 별점, 한줄 평 의견, 작성일의 검색
			만족도 목록표입니다.</caption>
		<colgroup>
			<col style="width: 6%">
			<col style="width: 25%">
			<col style="width: 9%">
			<col style="width: 9%">
			<col style="width: 6%">
			<col>
			<col style="width: 12%">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">소속</th>
				<th scope="col">이름</th>
				<th scope="col">만족도 유형</th>
				<th scope="col">별점</th>
				<th scope="col">한줄 평가</th>
				<th scope="col">작성일</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty list}">
			<tr>
				<td colspan="7" class="bllist">검색 만족도 목록이 없습니다.</td>
			</tr>
			</c:if>
			<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
			<c:forEach var="data" items="${list}">
				<tr>
					<td class="alignC">${listNo}</td>
					<td>${data.DEPT_PATH}</td>
					<td class="alignC">${data.REGI_NAME}</td>
					<td class="alignC">${data.POINT_NAME}</td>
					<td class="alignC">${data.POINT}</td>
					<td class="alignC">${data.CONTENTS}</td>
					<td class="alignC"><fmt:formatDate value="${data.REGI_DATE}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
			</c:forEach>
		</tbody>
	</table>

	<div class="page">
		<!-- 검색조건이 있을 때 -->
		<c:if test="${!empty param.is_mjCd || !empty param.pointType || !empty param.point || !empty param.searchKeyword}">
			<c:set var="searchList" value="&is_mjCd=${is_mjCd}&searchStDt=${stDt}&searchEdDt=${edDt}&pointType=${param.pointType}&point=${param.point}&searchKeyword=${param.searchKeyword}"/>
		</c:if>
		<pgui:pagination2 listUrl="${URL_SRCH_POINT}${searchList}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>

</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>