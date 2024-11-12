<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
<main id="content-wrap">            
<c:set var="profInfo" value="${professorList[0]}"/>
<c:set var="profInfoDeta" value="${professorDetaList}"/>
    <div class="content-inner">
	<div class="cont-top-header">
		<button type="button" class="btn-back" onclick="history.back()"><span class="sr-only">뒤로</span></button>
		<h3 class="h3"><c:out value="${profInfo.korNm}"></c:out></h3>
		<div class="favorite-checkbox" >
<!-- 			<input type="checkbox" id="checkbox01-02"> -->
<!-- 			<label for="checkbox01-02" class="sr-only">즐겨찾기</label> -->
			<i></i>
		</div>
	</div>
	
	<div class="h4-line mt30">
		<h4 class="h4">교수 상세</h4>
	</div>
	            
	<div class="table-wrap mt20">
		<table class="table-type02">
			<caption>교수 상세 테이블로 교수명, 직급, 소속, 주전공, 담당과목, 전화번호, 홈페이지, 이메일 항목을 나타내는 표입니다.
			</caption>
			<colgroup>
				<col style="width:10%">
				<col style="width:15%">
				<col style="width:10%">
				<col style="width:15%">
				<col style="width:10%">
				<col style="width:15%">
				<col style="width:10%">
				<col style="width:15%">
			</colgroup>
																	
			<tbody>
				<tr>
					<th scope="row">교수명</th>
					<td colspan="3">${profInfo.korNm}</td>
					<th scope="row">소속</th>
					<td colspan="3">${profInfo.deptNm}  &gt; ${profInfo.deptNm2}</td>
				</tr>
				<tr>
					<th scope="row">담당과목</th>
					<td colspan="3">${fn:replace(profInfo.scSmry,'\"null\"','-')}</td>
					<th scope="row">전화번호</th>
					<td colspan="3">${fn:replace(profInfo.ofceTelNo,'\"null\"','-')}</td>
				</tr>
				<tr>
					<th scope="row">홈페이지</th>
					<td colspan="3">${fn:replace(profInfo.homePage,'\"null\"','-')}</td>
					<th scope="row">이메일</th>
					<td colspan="3">${fn:replace(profInfo.email,'\"null\"','-')}</td>
				</tr>
				
			</tbody>
		</table>
	</div>
	
	<div class="h4-line mt80">
		<h4 class="h4">강좌 목록 (최근 5년)</h4>									
	</div>
	
	<div class="table-wrap2 mt20">
		<table class="table-type01 orange">
			<caption>강좌 목록 테이블로 학년, 학사, 세부학기, 대학, 학과(부), 교과과정, 교과구분, 교과목코드, 강좌명 항목을 나타내는 표입니다.
			</caption>
			<colgroup>
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col style="width:10%">
				<col>
			</colgroup>
			<thead>
				<tr>												
					<th scope="col">학년</th>
					<th scope="col">학사</th>
					<th scope="col">세부학기</th>
					<th scope="col">대학</th>
					<th scope="col">학과(부)</th>
					<th scope="col">교과과정</th>
					<th scope="col">교과목코드</th>
					<th scope="col">강좌명</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty professorClassList}">
				<tr>
					<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="docCd"/>
				<c:set var="listColumnName" value="${settingInfo.idx_column}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${professorClassList}" varStatus="i">
				<c:set var="listKey" value="${listDt.docId}"/>
				<tr>
					<td>${listDt.yy}</td>
					<td>${listDt.korTmGbn}</td>
					<td>${listDt.korDetaTmGbn}</td>
					<td>${listDt.deptNm}</td>
					<td>${listDt.deptNm2}</td>
					<td>${listDt.korCorsGbn}</td>
					<td>${listDt.scCd}</td>
					<td><span class="bold">${listDt.scNm}</span></td>												
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- start 페이징 -->
	<div class="paginate">
    <pgui:paginationIndex listUrl="${URL_PAGE_VIEW}" pgInfo="${paginationInfo}" pageName="page"/>
	</div>
<!-- end 페이징 -->

	<div class="h4-line mt80">
		<h4 class="h4"><c:out value="학력"/></h4>
	</div>
	<div class="content-list-box mt20">
		<ul class="content-list">
		<c:forEach var="listDt" items="${profInfoDeta}" varStatus="i">
			<c:if test="${listDt.typeGbn == 'EDUCATION'}">
			<li>${listDt.dt2} &nbsp; ${listDt.title1} (${listDt.title2})</li>
			</c:if>
		</c:forEach>
		</ul>
	</div>

	<div class="h4-line mt80">
		<h4 class="h4"><c:out value="경력"/></h4>
	</div>
	<div class="content-list-box mt20">
		<ul class="content-list">
		<c:forEach var="listDt" items="${profInfoDeta}" varStatus="i">
			<c:if test="${listDt.typeGbn == 'CAREER'}">
			<li>${listDt.dt1} ~ ${listDt.dt2}, ${listDt.title1} (${listDt.title2})</li>
			</c:if>
		</c:forEach>
		</ul>
	</div>
	
	<div class="h4-line mt80">
		<h4 class="h4"><c:out value="연구실적-논문"/></h4>
	</div>
	<div class="content-list-box mt20">
		<ul class="content-list">
		<c:forEach var="listDt" items="${profInfoDeta}" varStatus="i">
			<c:if test="${listDt.typeGbn == 'PAPER'}">
			<li>${listDt.title1},${listDt.title2},${listDt.volCtnt},${listDt.volCtnt}
			PP.${listDt.fromPage} ~ ${listDt.toPage}, ${listDt.dt1}</li>
			</c:if>
		</c:forEach>
		</ul>
	</div>
	
	<div class="h4-line mt80">
		<h4 class="h4"><c:out value="연구실적-저서"/></h4>
	</div>
	<div class="content-list-box mt20">
		<ul class="content-list">
			<c:forEach var="listDt" items="${profInfoDeta}" varStatus="i">
			<c:if test="${listDt.typeGbn == 'BOOK'}">
			<li>${listDt.title1},${listDt.title2},${listDt.dt1}</li>
			</c:if>
			</c:forEach>
		</ul>
	</div>
				
        </div>            
    </main>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>